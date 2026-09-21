package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.AccountsPage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class AccountsTest extends CustomerTestBase {

    private AccountsPage accountsPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void openAccounts() {
        accountsPage = new AccountsPage(getDriver()).open();
    }

    @Test(description = "The seeded customer's accounts are listed")
    public void accountsAreListed() {
        assertTrue(accountsPage.count() >= 3,
                "expected at least three seeded accounts, found " + accountsPage.count());
    }

    @Test(description = "Balances are exposed as integer minor units")
    public void balancesAreMinorUnits() {
        long balance = accountsPage.balanceMinor(0);
        assertTrue(balance > 0, "expected a positive seeded balance");
    }

    @Test(description = "Every account card carries a type and a status")
    public void accountCardsShowTypeAndStatus() {
        assertTrue(accountsPage.allCardsShowTypeAndStatus());
    }
}
