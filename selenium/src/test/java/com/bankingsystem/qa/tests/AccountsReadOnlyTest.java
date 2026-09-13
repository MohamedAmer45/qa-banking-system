package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;

import com.bankingsystem.qa.pages.AccountsPage;
import com.bankingsystem.qa.pages.DashboardPage;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.pages.MfaPage;

import com.bankingsystem.qa.utils.TestCredentials;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class AccountsReadOnlyTest extends BaseTest {

    private AccountsPage accountsPage;

    @BeforeMethod
    public void loginAndOpenAccounts() {

        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();

        loginPage.clearIdentifier();
        loginPage.clearPassword();

        loginPage.login(
                TestCredentials.customerEmail(),
                TestCredentials.customerPassword()
        );


        MfaPage mfaPage =
                new MfaPage(
                        getDriver()
                );

        Assert.assertTrue(
                mfaPage.isLoaded(),
                "MFA page should be displayed."
        );

        mfaPage.verify(
                TestCredentials.mfaCode()
        );


        DashboardPage dashboardPage =
                new DashboardPage(
                        getDriver()
                );

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Dashboard should load successfully."
        );


        accountsPage =
                dashboardPage.openAccounts();

        Assert.assertTrue(
                accountsPage.isLoaded(),
                "Accounts page should load successfully."
        );
    }


    @Test
    public void accountsPageShouldOpenSuccessfully() {

        Assert.assertEquals(
                accountsPage.getPageTitleText(),
                "Accounts",
                "Page title should be Accounts."
        );
    }


    @Test
    public void customerShouldHaveAtLeastOneAccount() {

        Assert.assertTrue(
                accountsPage.getAccountCount() > 0,
                "Seeded customer should have at least one bank account."
        );
    }


    @Test
    public void accountCardsShouldDisplayCoreAccountInformation() {

        int accountCount =
                accountsPage.getAccountCount();

        List<String> accountTypes =
                accountsPage.getAccountTypes();

        List<String> balances =
                accountsPage.getAccountBalances();

        List<String> metadata =
                accountsPage.getAccountMetadata();


        Assert.assertEquals(
                accountTypes.size(),
                accountCount,
                "Every account should display its account type."
        );

        Assert.assertEquals(
                balances.size(),
                accountCount,
                "Every account should display its balance."
        );

        Assert.assertEquals(
                metadata.size(),
                accountCount,
                "Every account should display account metadata."
        );


        for (String accountType : accountTypes) {

            Assert.assertFalse(
                    accountType.isBlank(),
                    "Account type should not be empty."
            );
        }


        for (String balance : balances) {

            Assert.assertFalse(
                    balance.isBlank(),
                    "Account balance should not be empty."
            );
        }


        for (String accountMeta : metadata) {

            Assert.assertFalse(
                    accountMeta.isBlank(),
                    "Account metadata should not be empty."
            );
        }
    }
}
