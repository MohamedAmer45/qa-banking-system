package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.AccountsPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class AccountCreationTest extends CustomerTestBase {

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
    public void openAccountModalShouldDisplaySupportedOptions() {

        accountsPage.openAccountModal();

        Assert.assertTrue(
                accountsPage.isAccountModalDisplayed(),
                "Open Account modal should be displayed."
        );


        List<String> accountTypes =
                accountsPage.getAccountTypeOptions();

        Assert.assertTrue(
                accountTypes.contains("CURRENT"),
                "CURRENT account type should be available."
        );

        Assert.assertTrue(
                accountTypes.contains("SAVINGS"),
                "SAVINGS account type should be available."
        );


        List<String> currencies =
                accountsPage.getCurrencyOptions();

        Assert.assertTrue(
                currencies.contains("EGP"),
                "EGP should be available."
        );

        Assert.assertTrue(
                currencies.contains("USD"),
                "USD should be available."
        );

        Assert.assertTrue(
                currencies.contains("EUR"),
                "EUR should be available."
        );

        Assert.assertTrue(
                currencies.contains("GBP"),
                "GBP should be available."
        );


        accountsPage.closeAccountModal();
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-ACC-001: Open Account form does not submit."
    )
    public void customerShouldOpenNewSavingsAccount() {

        int accountCountBefore =
                accountsPage.getAccountCount();

        accountsPage.createAccount(
                "SAVINGS",
                "GBP"
        );

        int accountCountAfter =
                accountsPage.waitForAccountCountToIncrease(
                        accountCountBefore
                );

        Assert.assertEquals(
                accountCountAfter,
                accountCountBefore + 1,
                "Exactly one new account should be created."
        );

        Assert.assertTrue(
                accountsPage
                        .getToastMessage()
                        .contains("Account opened"),
                "Success message should confirm that the account was opened."
        );
    }
}
