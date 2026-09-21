package com.bankingsystem.qa.db.integrity;

import com.bankingsystem.qa.db.support.Database;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * DB-003, DB-005, DB-013, DB-014: no financial record may reference an entity
 * that is not there.
 *
 * ConstraintTest proves the foreign keys are declared. These prove the stored
 * data actually satisfies them — which can diverge if a constraint was added
 * after the fact, or if rows were written around the application.
 */
public class ReferentialIntegrityTest {

    @DataProvider(name = "relationships")
    public Object[][] relationships() {
        return new Object[][]{
                {"accounts", "user_id", "users", "DB-003"},
                {"transactions", "account_id", "accounts", "DB-005"},
                {"transfers", "from_account_id", "accounts", "DB-005"},
                {"transfers", "user_id", "users", "DB-003"},
                {"beneficiaries", "user_id", "users", "DB-003"},
                {"cards", "account_id", "accounts", "DB-013"},
                {"loans", "user_id", "users", "DB-013"},
                {"loan_payments", "loan_id", "loans", "DB-013"},
                {"bill_payments", "account_id", "accounts", "DB-013"},
                {"saved_billers", "biller_id", "billers", "DB-013"},
                {"notifications", "user_id", "users", "DB-013"},
                {"kyc_profiles", "user_id", "users", "DB-013"},
                {"fraud_alerts", "user_id", "users", "DB-014"}
        };
    }

    @Test(dataProvider = "relationships",
            description = "No row references a missing parent")
    public void noOrphanRows(String table, String column, String parent, String requirement) {
        long orphans = Database.number("""
                SELECT COUNT(*)
                  FROM %s child
                 WHERE child.%s IS NOT NULL
                   AND NOT EXISTS (
                        SELECT 1 FROM %s parent WHERE parent.id = child.%s
                   )
                """.formatted(table, column, parent, column));

        assertEquals(orphans, 0L,
                requirement + ": " + orphans + " row(s) in " + table
                        + " reference a " + parent + " that does not exist");
    }

    @Test(description = "DB-005: every transaction belongs to a real account")
    public void transactionsReferenceRealAccounts() {
        long orphans = Database.number("""
                SELECT COUNT(*)
                  FROM transactions t
                  LEFT JOIN accounts a ON a.id = t.account_id
                 WHERE a.id IS NULL
                """);

        assertEquals(orphans, 0L,
                "DB-005: transactions exist against accounts that do not");
    }

    @Test(description = "DB-005: a transaction linked to a transfer links to a real one")
    public void transactionTransferLinksResolve() {
        long orphans = Database.number("""
                SELECT COUNT(*)
                  FROM transactions t
                 WHERE t.transfer_id IS NOT NULL
                   AND NOT EXISTS (
                        SELECT 1 FROM transfers x WHERE x.id = t.transfer_id
                   )
                """);

        assertEquals(orphans, 0L,
                "DB-005: ledger rows point at transfers that do not exist");
    }

    @Test(description = "DB-014: audit records reference real actors where one is named")
    public void auditActorsResolve() {
        long orphans = Database.number("""
                SELECT COUNT(*)
                  FROM audit_logs a
                 WHERE a.actor_user_id IS NOT NULL
                   AND NOT EXISTS (
                        SELECT 1 FROM users u WHERE u.id = a.actor_user_id
                   )
                """);

        assertEquals(orphans, 0L,
                "DB-014: audit entries name actors who do not exist, which makes "
                        + "the trail unusable for attribution");
    }

    @Test(description = "DB-014: every audit record records what it acted on")
    public void auditRecordsDescribeTheirSubject() {
        long incomplete = Database.number("""
                SELECT COUNT(*) FROM audit_logs
                 WHERE action IS NULL OR action = ''
                    OR entity_type IS NULL OR entity_type = ''
                    OR created_at IS NULL
                """);

        assertEquals(incomplete, 0L,
                "DB-014: an audit row without an action, entity type or timestamp "
                        + "cannot answer who did what, when");
    }

    @Test(description = "DB-013: a transfer books each movement against an account once")
    public void transferBooksEachMovementOnce() {
        /*
         * A reversed transfer legitimately touches the same account twice: the
         * original TRANSFER leg and the compensating REVERSAL leg. What must
         * never repeat is the same movement — the same type and direction —
         * against the same account, which would double-count it.
         */
        List<Map<String, Object>> duplicated = Database.rows("""
                SELECT transfer_id, account_id, transaction_type, direction,
                       COUNT(*) AS legs
                  FROM transactions
                 WHERE transfer_id IS NOT NULL
                 GROUP BY transfer_id, account_id, transaction_type, direction
                HAVING COUNT(*) > 1
                """);

        assertTrue(duplicated.isEmpty(),
                "DB-013: the same movement was booked more than once against an "
                        + "account, which double-counts it: " + duplicated);
    }

    @Test(description = "DB-013: a reversal books against exactly the accounts the original touched")
    public void reversalMirrorsTheOriginal() {
        List<Map<String, Object>> mismatched = Database.rows("""
                SELECT t.transfer_id,
                       COUNT(*) FILTER (WHERE t.transaction_type = 'TRANSFER') AS original,
                       COUNT(*) FILTER (WHERE t.transaction_type = 'REVERSAL') AS reversal
                  FROM transactions t
                  JOIN transfers x ON x.id = t.transfer_id
                 WHERE x.status = 'REVERSED'
                 GROUP BY t.transfer_id
                HAVING COUNT(*) FILTER (WHERE t.transaction_type = 'TRANSFER')
                    <> COUNT(*) FILTER (WHERE t.transaction_type = 'REVERSAL')
                """);

        assertTrue(mismatched.isEmpty(),
                "DB-013: a reversal must compensate every leg of the original, "
                        + "otherwise money is left stranded on one side: " + mismatched);
    }

    @Test(description = "DB-013: no account carries a negative balance")
    public void noNegativeBalances() {
        List<Map<String, Object>> negative = Database.rows("""
                SELECT id, account_number, balance_minor, available_minor
                  FROM accounts
                 WHERE balance_minor < 0 OR available_minor < 0
                """);

        assertTrue(negative.isEmpty(),
                "DB-013: overdraft is disabled, so a negative balance means money "
                        + "was moved that did not exist: " + negative);
    }
}
