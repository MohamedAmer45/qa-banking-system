package com.bankingsystem.qa.tests;

import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Legacy coverage retained intentionally.
 *
 * Current status:
 * Blocked: the rebuilt NovaBank application currently exposes transaction history but not the previous dedicated Statements module.
 *
 * This test class is NOT deleted because the functionality is expected
 * to return to the NovaBank QA application later.
 */
public class StatementsReadOnlyTest {

    @Test(
            groups = {"blocked", "legacy"},
            description = "Blocked: the rebuilt NovaBank application currently exposes transaction history but not the previous dedicated Statements module."
    )
    public void featureCurrentlyBlocked() {

        throw new SkipException(
                "Blocked: the rebuilt NovaBank application currently exposes transaction history but not the previous dedicated Statements module."
        );
    }
}