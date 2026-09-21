package com.bankingsystem.qa.db.ledger;

import com.bankingsystem.qa.db.support.Api;
import com.bankingsystem.qa.db.support.Database;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CyclicBarrier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * DB-010: concurrent financial operations maintain correct balances.
 *
 * The interesting failure is the lost update: two requests read the same
 * balance, both decide they can afford the transfer, and both commit. The
 * account then holds less than zero, or holds a balance that does not match
 * its own ledger.
 *
 * This is the requirement the previous builds of this project could not test
 * at all. SQLite serialised every writer, so contention never occurred, and
 * the stub had no persistent state to corrupt.
 */
public class ConcurrencyTest {

    private static final int CONCURRENT_REQUESTS = 6;

    private Api api;
    private long userId;

    @BeforeClass(alwaysRun = true)
    public void signIn() {
        api = new Api().signInAsCustomer();
        userId = api.userId();
    }

    private long beneficiaryId() {
        return Database.number("""
                SELECT id FROM beneficiaries
                 WHERE user_id = ? AND status = 'ACTIVE' AND verified = 1
                 ORDER BY id LIMIT 1
                """, userId);
    }

    /** Fires n transfers at once, returning each response status. */
    private List<Integer> fireConcurrently(long accountId, long beneficiary, double amount) {
        CyclicBarrier startLine = new CyclicBarrier(CONCURRENT_REQUESTS);
        List<Callable<Integer>> calls = new ArrayList<>();

        for (int i = 0; i < CONCURRENT_REQUESTS; i++) {
            final int index = i;

            calls.add(() -> {
                // Every thread waits here so the requests overlap rather than
                // arriving in sequence, which is what creates the contention.
                startLine.await();

                return new Api().signInAsCustomer().post("/api/transfers", """
                        {"fromAccountId":%d,"beneficiaryId":%d,"amount":%s,
                         "idempotencyKey":"conc-%d-%d"}"""
                        .formatted(accountId, beneficiary, amount,
                                System.nanoTime(), index)).status();
            });
        }

        try (ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT_REQUESTS)) {
            List<Integer> statuses = new ArrayList<>();

            for (Future<Integer> future : pool.invokeAll(calls)) {
                statuses.add(future.get());
            }

            return statuses;
        } catch (Exception e) {
            throw new IllegalStateException("Concurrent execution failed", e);
        }
    }

    @Test(description = "DB-010: concurrent transfers never overdraw an account")
    public void concurrentTransfersNeverOverdraw() {
        long account = Database.number("""
                SELECT id FROM accounts
                 WHERE user_id = ? AND status = 'ACTIVE' AND currency = 'EGP'
                 ORDER BY balance_minor DESC LIMIT 1
                """, userId);

        long before = Database.number(
                "SELECT balance_minor FROM accounts WHERE id = ?", account);

        /*
         * Each transfer is sized so that a minority can succeed and the rest
         * must be refused. If the balance is read without a lock, every
         * request sees the original figure, all of them pass the affordability
         * check, and the account ends up negative.
         */
        double amount = Math.floor((before / 100.0) / (CONCURRENT_REQUESTS - 2.0));

        assertTrue(amount > 0,
                "the seeded balance is too small for a meaningful contention test");

        List<Integer> statuses = fireConcurrently(account, beneficiaryId(), amount);

        long after = Database.number(
                "SELECT balance_minor FROM accounts WHERE id = ?", account);

        assertTrue(after >= 0,
                "DB-010: the balance went negative (" + after + ") — a lost update. "
                        + "Responses: " + statuses);

        long accepted = statuses.stream().filter(s -> s == 201).count();
        long refused = statuses.stream().filter(s -> s == 409).count();

        assertTrue(accepted > 0, "DB-010: expected some transfers to succeed");
        assertEquals(accepted + refused, (long) CONCURRENT_REQUESTS,
                "DB-010: every request should be a clean accept or refusal, "
                        + "not an error. Responses: " + statuses);

        long moved = before - after;
        assertTrue(moved <= before,
                "DB-010: more money left the account than it held");
    }

    @Test(dependsOnMethods = "concurrentTransfersNeverOverdraw",
            description = "DB-010: the ledger still reconciles after contention")
    public void ledgerReconcilesAfterContention() {
        List<Map<String, Object>> drift = Database.rows("""
                SELECT a.id, a.account_number, a.balance_minor,
                       t.balance_after_minor
                  FROM accounts a
                  JOIN LATERAL (
                        SELECT balance_after_minor
                          FROM transactions
                         WHERE account_id = a.id
                         ORDER BY created_at DESC, id DESC
                         LIMIT 1
                  ) t ON TRUE
                 WHERE a.balance_minor <> t.balance_after_minor
                """);

        assertTrue(drift.isEmpty(),
                "DB-010: every account balance must equal the balance recorded "
                        + "on its most recent transaction. Drifted: " + drift);
    }

    @Test(dependsOnMethods = "concurrentTransfersNeverOverdraw",
            description = "DB-010: contention produced no duplicate references")
    public void noDuplicateReferencesUnderContention() {
        long duplicates = Database.number("""
                SELECT COUNT(*) FROM (
                    SELECT reference FROM transfers
                     GROUP BY reference HAVING COUNT(*) > 1
                ) d
                """);

        assertEquals(duplicates, 0L,
                "DB-010: concurrent transfers generated colliding references");
    }
}
