package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.TransactionsCurrentPage;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.testng.Assert;

public final class TransactionHistorySteps {

    private final TestContext context;

    private TransactionsCurrentPage transactionsPage;

    public TransactionHistorySteps(
            TestContext context
    ) {
        this.context = context;
    }

    @When("the customer opens transaction history")
    public void customerOpensTransactionHistory() {

        transactionsPage =
                new TransactionsCurrentPage(
                        context.getDriver()
                ).open();

        Assert.assertTrue(
                transactionsPage.isLoaded(),
                "Transaction history should load."
        );
    }

    @Then("the transaction-history heading should be {string}")
    public void transactionHeadingShouldBe(
            String expectedHeading
    ) {

        Assert.assertEquals(
                page().getPageTitleText(),
                expectedHeading,
                "Transaction-history heading should match."
        );
    }

    @Then("at least one transaction should be displayed")
    public void transactionShouldBeDisplayed() {

        Assert.assertTrue(
                page().getTransactionCount() > 0,
                "At least one transaction should be displayed."
        );
    }

    @Then("every transaction should display a date")
    public void everyTransactionShouldDisplayDate() {

        Assert.assertTrue(
                page().allTransactionsHaveDates(),
                "Every transaction should have a date."
        );
    }

    @Then("every transaction should display a description")
    public void everyTransactionShouldDisplayDescription() {

        Assert.assertTrue(
                page().allTransactionsHaveDescriptions(),
                "Every transaction should have a description."
        );
    }

    @Then("every transaction should display a status")
    public void everyTransactionShouldDisplayStatus() {

        Assert.assertTrue(
                page().allTransactionsHaveStatuses(),
                "Every transaction should have a status."
        );
    }

    @Then("every transaction should display a currency amount")
    public void everyTransactionShouldDisplayCurrencyAmount() {

        Assert.assertTrue(
                page().allTransactionsHaveAmounts(),
                "Every transaction should display a currency amount."
        );
    }

    @Then("all transaction columns should contain matching record counts")
    public void transactionColumnCountsShouldMatch() {

        int transactionCount =
                page().getTransactionCount();

        Assert.assertEquals(
                page().getDates().size(),
                transactionCount,
                "Each transaction should contain a date."
        );

        Assert.assertEquals(
                page().getDescriptions().size(),
                transactionCount,
                "Each transaction should contain a description."
        );

        Assert.assertEquals(
                page().getStatuses().size(),
                transactionCount,
                "Each transaction should contain a status."
        );

        Assert.assertEquals(
                page().getAmounts().size(),
                transactionCount,
                "Each transaction should contain an amount."
        );
    }

    @Then("transaction history should contain credit and debit activity")
    public void historyShouldContainCreditAndDebitActivity() {

        Assert.assertTrue(
                page().hasCreditTransaction(),
                "History should contain a credit transaction."
        );

        Assert.assertTrue(
                page().hasDebitTransaction(),
                "History should contain a debit transaction."
        );
    }

    @Then("every seeded transaction should have completed status")
    public void seededTransactionsShouldBeCompleted() {

        Assert.assertTrue(
                page().allStatusesAreCompleted(),
                "Every seeded transaction should be completed."
        );
    }

    private TransactionsCurrentPage page() {

        if (transactionsPage == null) {
            throw new IllegalStateException(
                    "Transaction history has not been opened."
            );
        }

        return transactionsPage;
    }
}