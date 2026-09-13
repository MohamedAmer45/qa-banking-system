package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.TransactionsPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class TransactionsReadOnlyTest extends CustomerTestBase {

    private TransactionsPage transactionsPage;


    @BeforeMethod(alwaysRun = true)
    public void openTransactionsPage() {

        transactionsPage =
                dashboardPage.openTransactions();

        Assert.assertTrue(
                transactionsPage.isLoaded(),
                "Transactions page should load successfully."
        );
    }


    @Test
    public void transactionsPageShouldOpenSuccessfully() {

        Assert.assertEquals(
                transactionsPage.getPageTitleText(),
                "Transactions",
                "Page title should be Transactions."
        );

        Assert.assertTrue(
                transactionsPage.hasTransactionTableOrEmptyState(),
                "Transactions module should display either history rows or an empty state."
        );
    }


    @Test
    public void customerShouldBeAbleToSelectTransactionAccount() {

        List<String> accounts =
                transactionsPage.getAccountOptions();

        Assert.assertFalse(
                accounts.isEmpty(),
                "At least one account should be available for transaction history."
        );

        for (String account : accounts) {

            Assert.assertFalse(
                    account.isBlank(),
                    "Account selector entries should not be blank."
            );
        }
    }


    @Test
    public void transactionHistoryShouldExposeSupportedFilters() {

        Assert.assertTrue(
                transactionsPage.isReferenceFilterDisplayed(),
                "Reference filter should be displayed."
        );

        Assert.assertTrue(
                transactionsPage.isFromDateFilterDisplayed(),
                "From-date filter should be displayed."
        );

        Assert.assertTrue(
                transactionsPage.isToDateFilterDisplayed(),
                "To-date filter should be displayed."
        );

        Assert.assertTrue(
                transactionsPage.isMinimumAmountFilterDisplayed(),
                "Minimum amount filter should be displayed."
        );

        Assert.assertTrue(
                transactionsPage.isMaximumAmountFilterDisplayed(),
                "Maximum amount filter should be displayed."
        );
    }


    @Test
    public void transactionTypeAndStatusFiltersShouldExposeExpectedOptions() {

        List<String> transactionTypes =
                transactionsPage.getTransactionTypeOptions();

        Assert.assertTrue(
                transactionTypes.contains("All types"),
                "All types option should be available."
        );

        Assert.assertTrue(
                transactionTypes.contains("TRANSFER"),
                "TRANSFER type should be available."
        );

        Assert.assertTrue(
                transactionTypes.contains("DEPOSIT"),
                "DEPOSIT type should be available."
        );

        Assert.assertTrue(
                transactionTypes.contains("BILL_PAYMENT"),
                "BILL_PAYMENT type should be available."
        );

        Assert.assertTrue(
                transactionTypes.contains("LOAN_DISBURSEMENT"),
                "LOAN_DISBURSEMENT type should be available."
        );

        Assert.assertTrue(
                transactionTypes.contains("LOAN_PAYMENT"),
                "LOAN_PAYMENT type should be available."
        );

        Assert.assertTrue(
                transactionTypes.contains("REVERSAL"),
                "REVERSAL type should be available."
        );


        List<String> statuses =
                transactionsPage.getTransactionStatusOptions();

        Assert.assertTrue(
                statuses.contains("All statuses"),
                "All statuses option should be available."
        );

        Assert.assertTrue(
                statuses.contains("COMPLETED"),
                "COMPLETED status should be available."
        );
    }


    @Test
    public void transactionDateAndAmountFiltersShouldUseCorrectInputTypes() {

        Assert.assertEquals(
                transactionsPage.getFromDateType(),
                "date",
                "From filter should be a date input."
        );

        Assert.assertEquals(
                transactionsPage.getToDateType(),
                "date",
                "To filter should be a date input."
        );

        Assert.assertEquals(
                transactionsPage.getMinimumAmountType(),
                "number",
                "Minimum amount should be numeric."
        );

        Assert.assertEquals(
                transactionsPage.getMaximumAmountType(),
                "number",
                "Maximum amount should be numeric."
        );

        Assert.assertEquals(
                transactionsPage.getMinimumAmountStep(),
                "0.01",
                "Minimum amount should support two decimal places."
        );

        Assert.assertEquals(
                transactionsPage.getMaximumAmountStep(),
                "0.01",
                "Maximum amount should support two decimal places."
        );
    }


    @Test
    public void clearFiltersShouldResetReferenceFilter() {

        String reference =
                "QA-TEST-REFERENCE";

        transactionsPage.enterReference(
                reference
        );

        Assert.assertEquals(
                transactionsPage.getReferenceValue(),
                reference,
                "Reference filter should contain the entered value."
        );


        transactionsPage.clearFilters();


        Assert.assertEquals(
                transactionsPage.getReferenceValue(),
                "",
                "Reference filter should be empty after clearing filters."
        );
    }
}
