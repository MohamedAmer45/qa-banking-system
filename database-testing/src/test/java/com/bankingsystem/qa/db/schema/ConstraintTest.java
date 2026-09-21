package com.bankingsystem.qa.db.schema;

import com.bankingsystem.qa.db.support.Database;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Schema-level guarantees: identity, referential integrity and money
 * representation.
 *
 * These read the catalog rather than the data, so they hold regardless of what
 * the application happens to have written. A uniqueness test that only samples
 * rows proves nothing about the next insert; a test that asserts the
 * constraint exists does.
 */
public class ConstraintTest {

    // ------------------------------------------------- DB-001, DB-002, DB-004

    @DataProvider(name = "identityTables")
    public Object[][] identityTables() {
        return new Object[][]{
                {"users", "DB-001"},
                {"accounts", "DB-002"},
                {"transactions", "DB-004"},
                {"transfers", "DB-004"},
                {"beneficiaries", "DB-002"},
                {"loans", "DB-002"},
                {"bill_payments", "DB-004"}
        };
    }

    @Test(dataProvider = "identityTables",
            description = "Every entity table has a generated primary key")
    public void tableHasGeneratedPrimaryKey(String table, String requirement) {
        Map<String, Object> key = Database.row("""
                SELECT c.column_name, c.is_identity, c.data_type
                  FROM information_schema.table_constraints tc
                  JOIN information_schema.key_column_usage k
                    ON k.constraint_name = tc.constraint_name
                  JOIN information_schema.columns c
                    ON c.table_name = tc.table_name
                   AND c.column_name = k.column_name
                 WHERE tc.table_schema = 'public'
                   AND tc.table_name = ?
                   AND tc.constraint_type = 'PRIMARY KEY'
                """, table);

        assertTrue(key != null && !key.isEmpty(),
                requirement + ": " + table + " has no primary key");

        assertEquals(key.get("column_name"), "id",
                requirement + ": " + table + " should be keyed by id");

        assertEquals(key.get("is_identity"), "YES",
                requirement + ": " + table + ".id should be database-generated, "
                        + "so the application cannot choose or collide on it");
    }

    @Test(description = "DB-001: an email cannot be registered twice")
    public void emailIsUnique() {
        long constraints = Database.number("""
                SELECT COUNT(*)
                  FROM information_schema.table_constraints tc
                  JOIN information_schema.key_column_usage k
                    ON k.constraint_name = tc.constraint_name
                 WHERE tc.table_schema = 'public'
                   AND tc.table_name = 'users'
                   AND tc.constraint_type = 'UNIQUE'
                   AND k.column_name = 'email'
                """);

        assertTrue(constraints > 0,
                "DB-001: users.email must carry a UNIQUE constraint");
    }

    @Test(description = "DB-002: account numbers and IBANs cannot repeat")
    public void accountIdentifiersAreUnique() {
        for (String column : List.of("account_number", "iban")) {
            long constraints = Database.number("""
                    SELECT COUNT(*)
                      FROM information_schema.table_constraints tc
                      JOIN information_schema.key_column_usage k
                        ON k.constraint_name = tc.constraint_name
                     WHERE tc.table_schema = 'public'
                       AND tc.table_name = 'accounts'
                       AND tc.constraint_type = 'UNIQUE'
                       AND k.column_name = ?
                    """, column);

            assertTrue(constraints > 0,
                    "DB-002: accounts." + column + " must be unique");
        }
    }

    @DataProvider(name = "referenceTables")
    public Object[][] referenceTables() {
        return new Object[][]{{"transactions"}, {"transfers"}};
    }

    @Test(dataProvider = "referenceTables",
            description = "DB-004: a transaction reference cannot repeat")
    public void referenceIsUnique(String table) {
        long constraints = Database.number("""
                SELECT COUNT(*)
                  FROM information_schema.table_constraints tc
                  JOIN information_schema.key_column_usage k
                    ON k.constraint_name = tc.constraint_name
                 WHERE tc.table_schema = 'public'
                   AND tc.table_name = ?
                   AND tc.constraint_type = 'UNIQUE'
                   AND k.column_name = 'reference'
                """, table);

        assertTrue(constraints > 0,
                "DB-004: " + table + ".reference must be unique, otherwise two "
                        + "movements can share one identifier");
    }

    // --------------------------------------------------------- DB-006, DB-007

    @Test(description = "DB-007: no money column is stored as floating point")
    public void moneyIsNeverFloatingPoint() {
        List<Map<String, Object>> offenders = Database.rows("""
                SELECT table_name, column_name, data_type
                  FROM information_schema.columns
                 WHERE table_schema = 'public'
                   AND (column_name LIKE '%_minor'
                        OR column_name LIKE '%amount%'
                        OR column_name LIKE '%balance%')
                   AND data_type IN ('real', 'double precision', 'float')
                 ORDER BY table_name, column_name
                """);

        assertTrue(offenders.isEmpty(),
                "DB-007: money must never be stored as floating point, because "
                        + "binary floats cannot represent decimal currency "
                        + "exactly. Offending columns: " + offenders);
    }

    @Test(description = "DB-006: money is stored as integer minor units")
    public void moneyIsIntegerMinorUnits() {
        List<Map<String, Object>> columns = Database.rows("""
                SELECT table_name, column_name, data_type
                  FROM information_schema.columns
                 WHERE table_schema = 'public'
                   AND column_name LIKE '%_minor'
                 ORDER BY table_name, column_name
                """);

        assertFalse(columns.isEmpty(),
                "DB-006: expected money columns named *_minor");

        for (Map<String, Object> column : columns) {
            assertEquals(column.get("data_type"), "bigint",
                    "DB-006: " + column.get("table_name") + "."
                            + column.get("column_name")
                            + " should be bigint minor units, not "
                            + column.get("data_type"));
        }
    }

    @Test(description = "DB-006: interest and FX rates keep exact decimal precision")
    public void ratesUseExactNumerics() {
        List<Map<String, Object>> rates = Database.rows("""
                SELECT table_name, column_name, data_type
                  FROM information_schema.columns
                 WHERE table_schema = 'public'
                   AND (column_name LIKE '%rate%')
                 ORDER BY table_name, column_name
                """);

        for (Map<String, Object> rate : rates) {
            assertFalse(
                    List.of("real", "double precision", "float")
                            .contains(String.valueOf(rate.get("data_type"))),
                    "DB-006: " + rate.get("table_name") + "."
                            + rate.get("column_name")
                            + " is floating point; rates feed money calculations"
            );
        }
    }

    // --------------------------------------------------------- DB-003, DB-013

    @DataProvider(name = "ownership")
    public Object[][] ownership() {
        return new Object[][]{
                {"accounts", "user_id", "users"},
                {"beneficiaries", "user_id", "users"},
                {"cards", "user_id", "users"},
                {"transfers", "user_id", "users"},
                {"transactions", "account_id", "accounts"},
                {"loans", "user_id", "users"},
                {"bill_payments", "user_id", "users"},
                {"notifications", "user_id", "users"},
                {"kyc_profiles", "user_id", "users"},
                {"sessions", "user_id", "users"}
        };
    }

    @Test(dataProvider = "ownership",
            description = "Ownership and linkage columns are enforced by foreign keys")
    public void relationshipIsEnforcedByForeignKey(
            String table, String column, String referenced) {

        long keys = Database.number("""
                SELECT COUNT(*)
                  FROM information_schema.table_constraints tc
                  JOIN information_schema.key_column_usage k
                    ON k.constraint_name = tc.constraint_name
                  JOIN information_schema.constraint_column_usage ccu
                    ON ccu.constraint_name = tc.constraint_name
                 WHERE tc.table_schema = 'public'
                   AND tc.constraint_type = 'FOREIGN KEY'
                   AND tc.table_name = ?
                   AND k.column_name = ?
                   AND ccu.table_name = ?
                """, table, column, referenced);

        assertTrue(keys > 0,
                "DB-003/DB-013: " + table + "." + column
                        + " must be a foreign key to " + referenced
                        + ", otherwise an orphan financial record is possible");
    }

    @Test(description = "DB-013: money-bearing columns are NOT NULL")
    public void moneyColumnsAreNotNullable() {
        List<Map<String, Object>> nullable = Database.rows("""
                SELECT table_name, column_name
                  FROM information_schema.columns
                 WHERE table_schema = 'public'
                   AND column_name IN ('amount_minor', 'balance_minor',
                                       'available_minor')
                   AND is_nullable = 'YES'
                 ORDER BY table_name, column_name
                """);

        assertTrue(nullable.isEmpty(),
                "DB-013: a nullable money column allows a row with no amount: "
                        + nullable);
    }
}
