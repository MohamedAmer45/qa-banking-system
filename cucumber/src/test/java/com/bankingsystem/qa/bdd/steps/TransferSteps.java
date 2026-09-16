package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.TransfersCurrentPage;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.testng.Assert;

import java.util.List;

public final class TransferSteps {

    private final TestContext context;

    private TransfersCurrentPage transfersPage;

    private double recordedCheckingBalance;
    private int recordedTransactionCount;
    private boolean transferStateRecorded;

    public TransferSteps(TestContext context) {
        this.context = context;
    }

    @When("the customer opens the transfers workspace")
    public void customerOpensTransfersWorkspace() {

        transfersPage =
                new TransfersCurrentPage(
                        context.getDriver()
                ).open();

        Assert.assertTrue(
                transfersPage.isLoaded(),
                "Transfers workspace should load."
        );
    }

    @Then("the transfers heading should be {string}")
    public void transfersHeadingShouldBe(
            String expectedHeading
    ) {

        Assert.assertEquals(
                page().getPageTitleText(),
                expectedHeading,
                "Transfers heading should match."
        );
    }

    @Then("these transfer source accounts should be available:")
    public void sourceAccountsShouldBeAvailable(
            DataTable dataTable
    ) {

        List<String> actualAccounts =
                page().getFromAccountOptions();

        for (String expectedAccount :
                dataTable.asList()) {

            Assert.assertTrue(
                    actualAccounts.stream()
                            .anyMatch(account ->
                                    account.contains(
                                            expectedAccount
                                    )
                            ),
                    "Source account should be available: " +
                    expectedAccount
            );
        }
    }

    @Then("these transfer recipients should be available:")
    public void recipientsShouldBeAvailable(
            DataTable dataTable
    ) {

        List<String> actualRecipients =
                page().getRecipientOptions();

        for (String expectedRecipient :
                dataTable.asList()) {

            Assert.assertTrue(
                    actualRecipients.contains(
                            expectedRecipient
                    ),
                    "Recipient should be available: " +
                    expectedRecipient
            );
        }
    }

    @Given("the current transfer balance and transaction count are recorded")
    public void recordTransferState() {

        recordedCheckingBalance =
                page().getCheckingBalance();

        recordedTransactionCount =
                page().getTransactionRowCount();

        transferStateRecorded = true;
    }

    @When("the customer sends {string} from Checking to {string}")
    public void customerSendsTransfer(
            String amount,
            String recipient
    ) {

        page().transfer(
                0,
                recipient,
                amount
        );
    }

    @When("the customer bypasses browser minimum validation and sends {string} to {string}")
    public void submitNonPositiveTransfer(
            String amount,
            String recipient
    ) {

        page().removeAmountMinimum();

        page().transfer(
                0,
                recipient,
                amount
        );
    }

    @Then("the transfer notification should be {string}")
    public void transferNotificationShouldBe(
            String expectedNotification
    ) {

        page().waitForToastToContain(
                expectedNotification
        );

        Assert.assertEquals(
                page().getToastText(),
                expectedNotification,
                "Transfer notification should match."
        );
    }

    @Then("the Checking balance should decrease by {string}")
    public void checkingBalanceShouldDecreaseBy(
            String amount
    ) {

        requireRecordedState();

        double transferAmount =
                Double.parseDouble(amount);

        double expectedBalance =
                recordedCheckingBalance -
                transferAmount;

        page().waitForCheckingBalance(
                expectedBalance
        );

        Assert.assertEquals(
                page().getCheckingBalance(),
                expectedBalance,
                0.01,
                "Checking balance should decrease by the transfer amount."
        );
    }

    @Then("one transfer transaction should be added")
    public void oneTransactionShouldBeAdded() {

        requireRecordedState();

        page().waitForTransactionRowCountToIncrease(
                recordedTransactionCount
        );

        Assert.assertEquals(
                page().getTransactionRowCount(),
                recordedTransactionCount + 1,
                "Successful transfer should create one transaction."
        );
    }

    @Then("the rejected transfer should not change balance or transaction history")
    public void rejectedTransferShouldNotChangeState() {

        requireRecordedState();

        Assert.assertEquals(
                page().getCheckingBalance(),
                recordedCheckingBalance,
                0.01,
                "Rejected transfer should not change the balance."
        );

        Assert.assertEquals(
                page().getTransactionRowCount(),
                recordedTransactionCount,
                "Rejected transfer should not create a transaction."
        );
    }

    private TransfersCurrentPage page() {

        if (transfersPage == null) {
            throw new IllegalStateException(
                    "Transfers workspace has not been opened."
            );
        }

        return transfersPage;
    }

    private void requireRecordedState() {

        if (!transferStateRecorded) {
            throw new IllegalStateException(
                    "Transfer state was not recorded."
            );
        }
    }
}