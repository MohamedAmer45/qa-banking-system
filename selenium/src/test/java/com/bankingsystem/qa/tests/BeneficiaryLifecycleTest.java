package com.bankingsystem.qa.tests;

import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Legacy coverage retained intentionally.
 *
 * Current status:
 * Blocked: beneficiary create/update/delete functionality is not currently present in the rebuilt NovaBank interface.
 *
 * This test class is NOT deleted because the functionality is expected
 * to return to the NovaBank QA application later.
 */
public class BeneficiaryLifecycleTest {

    @Test(
            groups = {"blocked", "legacy"},
            description = "Blocked: beneficiary create/update/delete functionality is not currently present in the rebuilt NovaBank interface."
    )
    public void featureCurrentlyBlocked() {

        throw new SkipException(
                "Blocked: beneficiary create/update/delete functionality is not currently present in the rebuilt NovaBank interface."
        );
    }
}