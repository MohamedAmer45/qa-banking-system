package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.AccountsCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class AccountsReadOnlyTest extends CustomerTestBase {

    private AccountsCurrentPage openAccountsPage() {

        dashboardPage.openAccounts();

        AccountsCurrentPage accountsPage =
                new AccountsCurrentPage(
                        getDriver()
                );

        Assert.assertTrue(
                accountsPage.isLoaded(),
                "Accounts page should load successfully."
        );

        return accountsPage;
    }

    @Test(
            groups = {"smoke", "accounts"},
            description = "Accounts page loads successfully"
    )
    public void accountsPageShouldLoadSuccessfully() {

        AccountsCurrentPage accountsPage =
                openAccountsPage();

        Assert.assertEquals(
                accountsPage.getPageTitleText(),
                "Accounts",
                "Accounts page heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "accounts"},
            description = "Customer has banking accounts displayed"
    )
    public void customerAccountsShouldBeDisplayed() {

        AccountsCurrentPage accountsPage =
                openAccountsPage();

        Assert.assertTrue(
                accountsPage.getAccountCount() > 0,
                "At least one customer account should be displayed."
        );
    }

    @Test(
            groups = {"regression", "accounts"},
            description = "Checking and savings accounts are displayed"
    )
    public void expectedAccountTypesShouldBeDisplayed() {

        AccountsCurrentPage accountsPage =
                openAccountsPage();

        Assert.assertTrue(
                accountsPage.containsAccountType(
                        "Checking"
                ),
                "Checking account should be displayed."
        );

        Assert.assertTrue(
                accountsPage.containsAccountType(
                        "Savings"
                ),
                "Savings account should be displayed."
        );
    }

    @Test(
            groups = {"regression", "accounts"},
            description = "Each account displays a balance"
    )
    public void accountBalancesShouldBeDisplayed() {

        AccountsCurrentPage accountsPage =
                openAccountsPage();

        List<String> balances =
                accountsPage.getAccountBalances();

        Assert.assertEquals(
                balances.size(),
                accountsPage.getAccountCount(),
                "Each account should have a displayed balance."
        );

        for (String balance : balances) {

            Assert.assertFalse(
                    balance.isBlank(),
                    "Account balance should not be blank."
            );
        }
    }

    @Test(
            groups = {"regression", "accounts"},
            description = "Account balances display currency"
    )
    public void accountBalancesShouldDisplayCurrency() {

        AccountsCurrentPage accountsPage =
                openAccountsPage();

        Assert.assertTrue(
                accountsPage.allBalancesContainCurrency(),
                "Every account balance should contain a currency symbol."
        );
    }

    @Test(
            groups = {"regression", "accounts", "security"},
            description = "Account identifiers are masked"
    )
    public void accountNumbersShouldBeMasked() {

        AccountsCurrentPage accountsPage =
                openAccountsPage();

        List<String> accountNumbers =
                accountsPage.getAccountNumbers();

        Assert.assertEquals(
                accountNumbers.size(),
                accountsPage.getAccountCount(),
                "Each account should display an account identifier."
        );

        Assert.assertTrue(
                accountsPage.allAccountNumbersAreMasked(),
                "Every displayed account number should be masked."
        );
    }
}