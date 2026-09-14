package com.bankingsystem.qa.tests;

import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Legacy coverage retained intentionally.
 *
 * Current status:
 * Blocked: the rebuilt NovaBank application does not currently expose the previous account-control workflow.
 *
 * This test class is NOT deleted because the functionality is expected
 * to return to the NovaBank QA application later.
 */
public class AccountControlsTest {

    @Test(
            groups = {"blocked", "legacy"},
            description = "Blocked: the rebuilt NovaBank application does not currently expose the previous account-control workflow."
    )
    public void featureCurrentlyBlocked() {

        throw new SkipException(
                "Blocked: the rebuilt NovaBank application does not currently expose the previous account-control workflow."
        );
    }
}