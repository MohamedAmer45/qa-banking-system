package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.TransactionsCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class TransactionsReadOnlyTest extends CustomerTestBase {

    private TransactionsCurrentPage openTransactionsPage() {

        dashboardPage.openTransactions();

        TransactionsCurrentPage transactionsPage =
                new TransactionsCurrentPage(
                        getDriver()
                );

        Assert.assertTrue(
                transactionsPage.isLoaded(),
                "Transactions page should load successfully."
        );

        return transactionsPage;
    }

    @Test(
            groups = {"smoke", "transactions"},
            description = "Transactions page loads successfully"
    )
    public void transactionsPageShouldLoadSuccessfully() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        Assert.assertEquals(
                transactionsPage.getPageTitleText(),
                "Transactions",
                "Transactions heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "transactions"},
            description = "Customer transaction history is displayed"
    )
    public void transactionHistoryShouldBeDisplayed() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        Assert.assertTrue(
                transactionsPage.getTransactionCount() > 0,
                "At least one transaction should be displayed."
        );
    }

    @Test(
            groups = {"regression", "transactions"},
            description = "Every transaction displays a date"
    )
    public void everyTransactionShouldDisplayDate() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        Assert.assertTrue(
                transactionsPage.allTransactionsHaveDates(),
                "Every transaction should have a date."
        );
    }

    @Test(
            groups = {"regression", "transactions"},
            description = "Every transaction displays a description"
    )
    public void everyTransactionShouldDisplayDescription() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        Assert.assertTrue(
                transactionsPage.allTransactionsHaveDescriptions(),
                "Every transaction should have a description."
        );
    }

    @Test(
            groups = {"regression", "transactions"},
            description = "Every transaction displays a status"
    )
    public void everyTransactionShouldDisplayStatus() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        Assert.assertTrue(
                transactionsPage.allTransactionsHaveStatuses(),
                "Every transaction should display a status."
        );
    }

    @Test(
            groups = {"regression", "transactions"},
            description = "Every transaction displays an amount"
    )
    public void everyTransactionShouldDisplayAmount() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        Assert.assertTrue(
                transactionsPage.allTransactionsHaveAmounts(),
                "Every transaction should display a currency amount."
        );
    }

    @Test(
            groups = {"regression", "transactions"},
            description = "Transaction table contains credit activity"
    )
    public void transactionHistoryShouldContainCredit() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        Assert.assertTrue(
                transactionsPage.hasCreditTransaction(),
                "Transaction history should contain at least one credit transaction."
        );
    }

    @Test(
            groups = {"regression", "transactions"},
            description = "Transaction table contains debit activity"
    )
    public void transactionHistoryShouldContainDebit() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        Assert.assertTrue(
                transactionsPage.hasDebitTransaction(),
                "Transaction history should contain at least one debit transaction."
        );
    }

    @Test(
            groups = {"regression", "transactions"},
            description = "Seeded transactions have completed status"
    )
    public void seededTransactionsShouldBeCompleted() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        Assert.assertTrue(
                transactionsPage.allStatusesAreCompleted(),
                "Seeded transaction statuses should be completed."
        );
    }

    @Test(
            groups = {"regression", "transactions"},
            description = "Transaction columns contain equal numbers of records"
    )
    public void transactionColumnsShouldRemainConsistent() {

        TransactionsCurrentPage transactionsPage =
                openTransactionsPage();

        int transactionCount =
                transactionsPage.getTransactionCount();

        List<String> dates =
                transactionsPage.getDates();

        List<String> descriptions =
                transactionsPage.getDescriptions();

        List<String> statuses =
                transactionsPage.getStatuses();

        List<String> amounts =
                transactionsPage.getAmounts();

        Assert.assertEquals(
                dates.size(),
                transactionCount,
                "Each transaction should contain a date."
        );

        Assert.assertEquals(
                descriptions.size(),
                transactionCount,
                "Each transaction should contain a description."
        );

        Assert.assertEquals(
                statuses.size(),
                transactionCount,
                "Each transaction should contain a status."
        );

        Assert.assertEquals(
                amounts.size(),
                transactionCount,
                "Each transaction should contain an amount."
        );
    }
}