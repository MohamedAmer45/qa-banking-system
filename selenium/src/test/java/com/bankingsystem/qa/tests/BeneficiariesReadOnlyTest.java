package com.bankingsystem.qa.tests;

import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Legacy coverage retained intentionally.
 *
 * Current status:
 * Blocked: the Beneficiaries module is not currently present in the rebuilt NovaBank interface.
 *
 * This test class is NOT deleted because the functionality is expected
 * to return to the NovaBank QA application later.
 */
public class BeneficiariesReadOnlyTest {

    @Test(
            groups = {"blocked", "legacy"},
            description = "Blocked: the Beneficiaries module is not currently present in the rebuilt NovaBank interface."
    )
    public void featureCurrentlyBlocked() {

        throw new SkipException(
                "Blocked: the Beneficiaries module is not currently present in the rebuilt NovaBank interface."
        );
    }
}