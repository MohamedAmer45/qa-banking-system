package com.bankingsystem.qa.db.history;

import com.bankingsystem.qa.db.support.Api;
import com.bankingsystem.qa.db.support.Database;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * DB-012: deleting or disabling an entity must not destroy the financial
 * history that references it.
 *
 * A hard delete of a beneficiary would either break the transfers that name it
 * or silently cascade them away. Either outcome loses a record of money that
 * actually moved, so the expected behaviour is retirement, not removal.
 */
public class HistoryPreservationTest {

    private Api api;
    private long userId;

    @BeforeClass(alwaysRun = true)
    public void signIn() {
        api = new Api().signInAsCustomer();
        userId = api.userId();
    }

    @Test(description = "DB-012: deleting a beneficiary retires it and keeps its transfers")
    public void deletingBeneficiaryPreservesTransfers() {
        // A beneficiary of our own, so the test never disturbs seeded fixtures.
        Api.Result created = api.post("/api/beneficiaries", """
                {"name":"History Probe","bankName":"NILE EXTERNAL BANK",
                 "accountIdentifier":"EXT-HIST-%d","currency":"EGP"}"""
                .formatted(System.nanoTime() % 1_000_000));

        assertEquals(created.status(), 201, created.body().toString());
        long beneficiaryId = created.number("id");

        // Verify it so a transfer can use it.
        assertEquals(
                api.post("/api/beneficiaries/" + beneficiaryId + "/verify",
                        """
                        {"code":"123456"}""").status(),
                200);

        long account = Database.number("""
                SELECT id FROM accounts
                 WHERE user_id = ? AND status = 'ACTIVE' AND currency = 'EGP'
                 ORDER BY id LIMIT 1
                """, userId);

        Api.Result transfer = api.post("/api/transfers", """
                {"fromAccountId":%d,"beneficiaryId":%d,"amount":3,
                 "idempotencyKey":"hist-%d"}"""
                .formatted(account, beneficiaryId, System.nanoTime()));

        assertEquals(transfer.status(), 201, transfer.body().toString());
        String reference = transfer.text("reference");

        // Now delete the beneficiary.
        assertEquals(api.delete("/api/beneficiaries/" + beneficiaryId).status(), 200);

        // The row is retired, not removed.
        Map<String, Object> retired = Database.row(
                "SELECT id, status FROM beneficiaries WHERE id = ?", beneficiaryId);

        assertNotNull(retired,
                "DB-012: the beneficiary row was hard deleted, taking the link "
                        + "from its transfers with it");

        assertEquals(retired.get("status"), "DELETED",
                "DB-012: a deleted beneficiary should be marked, not removed");

        // The transfer still exists and still names it.
        Map<String, Object> preserved = Database.row("""
                SELECT beneficiary_id, amount_minor, status
                  FROM transfers WHERE reference = ?
                """, reference);

        assertNotNull(preserved,
                "DB-012: deleting the beneficiary destroyed the transfer record");

        assertEquals(((Number) preserved.get("beneficiary_id")).longValue(), beneficiaryId,
                "DB-012: the transfer lost its link to the beneficiary");

        assertEquals(preserved.get("status"), "COMPLETED",
                "DB-012: a completed transfer must stay completed");
    }

    @Test(description = "DB-012: a closed account keeps its transaction history")
    public void closedAccountsKeepHistory() {
        long orphanedHistory = Database.number("""
                SELECT COUNT(*)
                  FROM accounts a
                 WHERE a.status = 'CLOSED'
                   AND NOT EXISTS (
                        SELECT 1 FROM transactions t WHERE t.account_id = a.id
                   )
                   AND EXISTS (
                        SELECT 1 FROM transfers x WHERE x.from_account_id = a.id
                   )
                """);

        assertEquals(orphanedHistory, 0L,
                "DB-012: a closed account that made transfers has no ledger rows, "
                        + "so its history was discarded on closure");
    }

    @Test(description = "DB-012: reversals compensate rather than erase")
    public void reversalsPreserveTheOriginal() {
        long erased = Database.number("""
                SELECT COUNT(*)
                  FROM transfers
                 WHERE status = 'REVERSED'
                   AND NOT EXISTS (
                        SELECT 1 FROM transactions t
                         WHERE t.transfer_id = transfers.id
                           AND t.transaction_type = 'TRANSFER'
                   )
                """);

        assertEquals(erased, 0L,
                "DB-012: a reversed transfer must keep its original ledger rows "
                        + "and add compensating ones, not delete the originals");
    }

    @Test(description = "DB-012: no ledger row is ever deleted once written")
    public void ledgerReferencesAreContiguous() {
        // Every transaction that claims a transfer must still find it, which
        // would fail if transfers were pruned rather than retired.
        long dangling = Database.number("""
                SELECT COUNT(*)
                  FROM transactions t
                 WHERE t.transfer_id IS NOT NULL
                   AND NOT EXISTS (
                        SELECT 1 FROM transfers x WHERE x.id = t.transfer_id
                   )
                """);

        assertEquals(dangling, 0L,
                "DB-012: ledger rows survive transfers that were deleted");
    }

    @Test(dependsOnMethods = "deletingBeneficiaryPreservesTransfers",
            description = "DB-012: audit history outlives the entities it describes")
    public void auditSurvivesEntityRetirement() {
        long auditRows = Database.number("""
                SELECT COUNT(*) FROM audit_logs
                 WHERE entity_type = 'BENEFICIARY' AND action = 'DELETE_BENEFICIARY'
                """);

        assertTrue(auditRows > 0,
                "DB-012: deleting a beneficiary should leave an audit record; "
                        + "none found, so the action is untraceable afterwards");
    }
}
