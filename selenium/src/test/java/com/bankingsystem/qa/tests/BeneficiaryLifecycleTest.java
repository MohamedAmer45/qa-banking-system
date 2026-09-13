package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;

import com.bankingsystem.qa.pages.BeneficiariesPage;
import com.bankingsystem.qa.pages.DashboardPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BeneficiaryLifecycleTest extends CustomerTestBase {

    private BeneficiariesPage beneficiariesPage;

    @BeforeMethod(alwaysRun = true)
    public void openBeneficiariesPage() {

        beneficiariesPage =
                dashboardPage.openBeneficiaries();

        Assert.assertTrue(
                beneficiariesPage.isLoaded(),
                "Beneficiaries page should load successfully."
        );
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-BEN-001: deleted beneficiary remains after fresh reload."
    )
    public void customerShouldCreateVerifyAndDeleteBeneficiary() {

        long uniqueValue =
                System.currentTimeMillis();

        String name =
                "QA Beneficiary " + uniqueValue;

        String nickname =
                "QA " + uniqueValue;

        String accountIdentifier =
                "QA" + uniqueValue;


        // ----------------------------------------------------
        // CREATE
        // ----------------------------------------------------

        beneficiariesPage.createBeneficiary(
                name,
                nickname,
                "CAIRO TEST BANK",
                "EGP",
                accountIdentifier
        );

        Assert.assertTrue(
                beneficiariesPage.beneficiaryExists(name),
                "New beneficiary should appear in the beneficiary list."
        );


        // ----------------------------------------------------
        // VERIFY WITH OTP
        // ----------------------------------------------------

        beneficiariesPage.verifyBeneficiary(
                name,
                "123456"
        );

        Assert.assertTrue(
                beneficiariesPage.isBeneficiaryVerified(name),
                "Beneficiary should show VERIFIED after valid OTP."
        );


        // ----------------------------------------------------
        // DELETE
        // ----------------------------------------------------

        beneficiariesPage.deleteBeneficiary(
                name
        );

        Assert.assertTrue(
                beneficiariesPage
                        .getToastMessage()
                        .contains("Beneficiary deleted"),
                "Application should confirm successful deletion."
        );


        // ----------------------------------------------------
        // FULL RELOAD
        //
        // This verifies persistent backend state rather than
        // relying on the previous DOM being refreshed correctly.
        // ----------------------------------------------------

        getDriver().navigate().refresh();

        DashboardPage refreshedDashboard =
                new DashboardPage(
                        getDriver()
                );

        Assert.assertTrue(
                refreshedDashboard.isLoaded(),
                "Dashboard should reload with the authenticated session."
        );

        BeneficiariesPage refreshedBeneficiaries =
                refreshedDashboard.openBeneficiaries();

        Assert.assertTrue(
                refreshedBeneficiaries.isLoaded(),
                "Beneficiaries page should load after refresh."
        );


        // ----------------------------------------------------
        // VERIFY DELETION
        // ----------------------------------------------------

        Assert.assertFalse(
                refreshedBeneficiaries.beneficiaryExists(name),
                "Deleted beneficiary should not exist after a fresh application load."
        );
    }
}

