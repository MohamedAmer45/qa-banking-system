package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.BillsCurrentPage;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.testng.Assert;

import java.util.List;

public final class BillPaymentSteps {

    private final TestContext context;

    private BillsCurrentPage billsPage;

    public BillPaymentSteps(
            TestContext context
    ) {
        this.context = context;
    }

    @When("the customer opens the bills workspace")
    public void customerOpensBillsWorkspace() {

        billsPage =
                new BillsCurrentPage(
                        context.getDriver()
                ).open();

        Assert.assertTrue(
                billsPage.isLoaded(),
                "Bills workspace should load."
        );
    }

    @Then("the bills heading should be {string}")
    public void billsHeadingShouldBe(
            String expectedHeading
    ) {

        Assert.assertEquals(
                page().getPageTitleText(),
                expectedHeading,
                "Bills heading should match."
        );
    }

    @Then("these billers should be available:")
    public void billersShouldBeAvailable(
            DataTable dataTable
    ) {

        List<String> availableBillers =
                page().getBillerOptions();

        Assert.assertTrue(
                availableBillers.size() >= 4,
                "At least four billers should be available."
        );

        for (String expectedBiller :
                dataTable.asList()) {

            Assert.assertTrue(
                    availableBillers.contains(
                            expectedBiller
                    ),
                    "Biller should be available: " +
                    expectedBiller
            );
        }
    }

    @When("the customer pays {string} to the {string} biller")
    public void customerPaysBill(
            String amount,
            String biller
    ) {

        page().payBill(
                biller,
                amount
        );
    }

    @When("the customer bypasses browser bill minimum validation and pays {string} to {string}")
    public void customerSubmitsNonPositiveBill(
            String amount,
            String biller
    ) {

        page().removeAmountMinimum();

        page().payBill(
                biller,
                amount
        );
    }

    @Then("the bill-payment notification should be {string}")
    public void billPaymentNotificationShouldBe(
            String expectedNotification
    ) {

        page().waitForToastToContain(
                expectedNotification
        );

        Assert.assertEquals(
                page().getToastText(),
                expectedNotification,
                "Bill-payment notification should match."
        );
    }

    private BillsCurrentPage page() {

        if (billsPage == null) {
            throw new IllegalStateException(
                    "Bills workspace has not been opened."
            );
        }

        return billsPage;
    }
}