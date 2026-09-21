package com.bankingsystem.qa.db.ledger;

import com.bankingsystem.qa.db.support.Api;
import com.bankingsystem.qa.db.support.Database;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Cross-layer coverage: perform a real operation through the API, then read
 * the stored rows directly.
 *
 * This is the only way to test these requirements honestly. An API response
 * saying a transfer completed proves the endpoint agrees with itself; only the
 * ledger rows prove the money actually moved and was recorded.
 */
public class TransferPersistenceTest {

    private Api api;
    private long userId;

    @BeforeClass(alwaysRun = true)
    public void signIn() {
        api = new Api().signInAsCustomer();
        userId = api.userId();
    }

    private long sourceAccountId() {
        return Database.number("""
                SELECT id FROM accounts
                 WHERE user_id = ? AND status = 'ACTIVE' AND currency = 'EGP'
                 ORDER BY id
                 LIMIT 1
                """, userId);
    }

    private long verifiedBeneficiaryId() {
        return Database.number("""
                SELECT id FROM beneficiaries
                 WHERE user_id = ? AND status = 'ACTIVE' AND verified = 1
                 ORDER BY id
                 LIMIT 1
                """, userId);
    }

    private long balanceMinor(long accountId) {
        return Database.number(
                "SELECT balance_minor FROM accounts WHERE id = ?", accountId);
    }

    // ------------------------------------------------------------- DB-008

    @Test(description = "DB-008: a completed transfer persists its ledger rows")
    public void completedTransferPersistsLedgerRows() {
        long source = sourceAccountId();
        long beneficiary = verifiedBeneficiaryId();
        long before = balanceMinor(source);

        String key = "db-test-" + System.nanoTime();

        Api.Result result = api.post("/api/transfers", """
                {"fromAccountId":%d,"beneficiaryId":%d,"amount":25.50,
                 "memo":"ledger persistence","idempotencyKey":"%s"}"""
                .formatted(source, beneficiary, key));

        assertEquals(result.status(), 201, "transfer should be accepted: " + result.body());

        String reference = result.text("reference");
        assertNotNull(reference);

        // The transfer row exists and is complete.
        Map<String, Object> transfer = Database.row("""
                SELECT id, status, amount_minor, fee_minor, completed_at
                  FROM transfers WHERE reference = ?
                """, reference);

        assertNotNull(transfer, "DB-008: no transfer row was written for " + reference);
        assertEquals(transfer.get("status"), "COMPLETED");
        assertEquals(((Number) transfer.get("amount_minor")).longValue(), 2550L,
                "DB-008: 25.50 must persist as 2550 minor units");
        assertNotNull(transfer.get("completed_at"),
                "DB-008: a completed transfer must record when it completed");

        long transferId = ((Number) transfer.get("id")).longValue();

        // A matching debit exists on the source account.
        Map<String, Object> debit = Database.row("""
                SELECT amount_minor, fee_minor, direction, balance_after_minor, status
                  FROM transactions
                 WHERE transfer_id = ? AND account_id = ? AND direction = 'DEBIT'
                """, transferId, source);

        assertNotNull(debit, "DB-008: no debit transaction was recorded");
        assertEquals(debit.get("status"), "COMPLETED");

        // The debit's recorded closing balance matches the account itself.
        long after = balanceMinor(source);
        long fee = ((Number) transfer.get("fee_minor")).longValue();

        assertEquals(((Number) debit.get("balance_after_minor")).longValue(), after,
                "DB-008: the ledger's balance_after must equal the account balance");

        assertEquals(before - after, 2550L + fee,
                "DB-008: the account must be debited by exactly amount + fee");
    }

    @Test(description = "DB-008: a same-bank transfer credits the destination ledger too")
    public void sameBankTransferWritesBothSides() {
        long source = sourceAccountId();

        Long sameBank = Database.rows("""
                SELECT b.id
                  FROM beneficiaries b
                  JOIN accounts a ON a.account_number = b.account_identifier
                 WHERE b.user_id = ? AND b.bank_name = 'NOVABANK'
                   AND b.status = 'ACTIVE' AND b.verified = 1
                 LIMIT 1
                """, userId).stream()
                .map(r -> ((Number) r.get("id")).longValue())
                .findFirst()
                .orElse(null);

        assertNotNull(sameBank, "expected a verified NOVABANK beneficiary in the seed");

        String key = "db-samebank-" + System.nanoTime();

        Api.Result result = api.post("/api/transfers", """
                {"fromAccountId":%d,"beneficiaryId":%d,"amount":10,
                 "idempotencyKey":"%s"}""".formatted(source, sameBank, key));

        assertEquals(result.status(), 201, result.body().toString());

        long transferId = Database.number(
                "SELECT id FROM transfers WHERE reference = ?", result.text("reference"));

        List<Map<String, Object>> legs = Database.rows("""
                SELECT direction, amount_minor, account_id
                  FROM transactions
                 WHERE transfer_id = ?
                 ORDER BY direction
                """, transferId);

        assertEquals(legs.size(), 2,
                "DB-008: a same-bank transfer must write both a debit and a credit, "
                        + "found: " + legs);

        assertEquals(legs.get(0).get("direction"), "CREDIT");
        assertEquals(legs.get(1).get("direction"), "DEBIT");
    }

    // ------------------------------------------------------ DB-009, DB-015

    @Test(description = "DB-009/DB-015: a failed transfer leaves no partial balance change")
    public void failedTransferLeavesNoPartialChange() {
        long source = sourceAccountId();
        long before = balanceMinor(source);

        long transactionsBefore = Database.number(
                "SELECT COUNT(*) FROM transactions WHERE account_id = ?", source);

        String key = "db-fail-" + System.nanoTime();

        // qaSimulation forces the downstream leg to fail after the transfer row
        // is created, which is the case that would leave a half-applied debit
        // if the operation were not transactional.
        Api.Result result = api.post("/api/transfers", """
                {"fromAccountId":%d,"beneficiaryId":%d,"amount":15,
                 "qaSimulation":"failure","idempotencyKey":"%s"}"""
                .formatted(source, verifiedBeneficiaryId(), key));

        assertEquals(result.status(), 422,
                "the simulated failure should be reported as 422: " + result.body());

        Map<String, Object> transfer = Database.row("""
                SELECT status, failure_reason, completed_at
                  FROM transfers WHERE reference = ?
                """, result.text("reference"));

        assertEquals(transfer.get("status"), "FAILED");
        assertNotNull(transfer.get("failure_reason"),
                "DB-009: a failed transfer should record why");
        assertEquals(transfer.get("completed_at"), null,
                "DB-009: a failed transfer must not be marked completed");

        assertEquals(balanceMinor(source), before,
                "DB-009: a failed transfer must not move money");

        assertEquals(
                Database.number(
                        "SELECT COUNT(*) FROM transactions WHERE account_id = ?", source),
                transactionsBefore,
                "DB-015: a failed transfer must not leave ledger rows behind");
    }

    @Test(description = "DB-015: a rejected transfer writes nothing at all")
    public void rejectedTransferIsRolledBackEntirely() {
        long source = sourceAccountId();
        long before = balanceMinor(source);

        long transfersBefore = Database.number(
                "SELECT COUNT(*) FROM transfers WHERE user_id = ?", userId);

        // Rejected on insufficient funds, before any row is written.
        Api.Result result = api.post("/api/transfers", """
                {"fromAccountId":%d,"beneficiaryId":%d,"amount":99999999,
                 "idempotencyKey":"db-reject-%d"}"""
                .formatted(source, verifiedBeneficiaryId(), System.nanoTime()));

        assertEquals(result.status(), 409, result.body().toString());

        assertEquals(balanceMinor(source), before,
                "DB-015: a rejected transfer must not move money");

        assertEquals(
                Database.number(
                        "SELECT COUNT(*) FROM transfers WHERE user_id = ?", userId),
                transfersBefore,
                "DB-015: a transfer rejected by a business rule must roll back "
                        + "its own row, not persist as an abandoned record");
    }

    // ------------------------------------------------------------- DB-011

    @Test(description = "DB-011: a repeated request creates exactly one transfer")
    public void idempotentRetryCreatesOneTransfer() {
        long source = sourceAccountId();
        long beneficiary = verifiedBeneficiaryId();
        long before = balanceMinor(source);

        String key = "db-idem-" + System.nanoTime();
        String body = """
                {"fromAccountId":%d,"beneficiaryId":%d,"amount":5,
                 "idempotencyKey":"%s"}""".formatted(source, beneficiary, key);

        Api.Result first = api.post("/api/transfers", body);
        Api.Result second = api.post("/api/transfers", body);

        assertEquals(first.status(), 201, first.body().toString());
        assertEquals(second.text("reference"), first.text("reference"),
                "DB-011: a retry must return the original transfer");

        assertEquals(
                Database.number("""
                        SELECT COUNT(*) FROM transfers
                         WHERE user_id = ? AND idempotency_key = ?
                        """, userId, key),
                1L,
                "DB-011: the idempotency key must yield exactly one stored transfer");

        long transferId = Database.number(
                "SELECT id FROM transfers WHERE idempotency_key = ?", key);

        assertEquals(
                Database.number("""
                        SELECT COUNT(*) FROM transactions
                         WHERE transfer_id = ? AND direction = 'DEBIT'
                        """, transferId),
                1L,
                "DB-011: a retry must not write a second debit");

        assertEquals(before - balanceMinor(source), 500L,
                "DB-011: the account must be debited once, not twice");
    }

    @Test(description = "DB-011: the idempotency key is unique per customer at the schema level")
    public void idempotencyIsEnforcedBySchema() {
        long constraints = Database.number("""
                SELECT COUNT(*)
                  FROM pg_indexes
                 WHERE schemaname = 'public'
                   AND tablename = 'transfers'
                   AND indexdef ILIKE '%UNIQUE%'
                   AND indexdef ILIKE '%idempotency_key%'
                """);

        assertTrue(constraints > 0,
                "DB-011: idempotency enforced only in application code can be "
                        + "defeated by two concurrent requests; it needs a unique "
                        + "index on (user_id, idempotency_key)");
    }
}
