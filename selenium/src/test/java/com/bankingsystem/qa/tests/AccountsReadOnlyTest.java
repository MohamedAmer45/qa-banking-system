package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.AccountsPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class AccountsReadOnlyTest extends CustomerTestBase {

    private AccountsPage accountsPage;

    @BeforeMethod(alwaysRun = true)
    public void openAccountsPage() {

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
