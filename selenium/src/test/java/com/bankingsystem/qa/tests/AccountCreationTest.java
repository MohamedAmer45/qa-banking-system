package com.bankingsystem.qa.tests;

import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Legacy coverage retained intentionally.
 *
 * Current status:
 * Blocked: account creation is not currently exposed by the rebuilt NovaBank customer interface.
 *
 * This test class is NOT deleted because the functionality is expected
 * to return to the NovaBank QA application later.
 */
public class AccountCreationTest {

    @Test(
            groups = {"blocked", "legacy"},
            description = "Blocked: account creation is not currently exposed by the rebuilt NovaBank customer interface."
    )
    public void featureCurrentlyBlocked() {

        throw new SkipException(
                "Blocked: account creation is not currently exposed by the rebuilt NovaBank customer interface."
        );
    }
}