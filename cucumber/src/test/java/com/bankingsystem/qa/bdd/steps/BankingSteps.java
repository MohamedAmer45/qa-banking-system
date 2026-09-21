package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BankingSteps {

    private final TestContext context;

    public BankingSteps(TestContext context) {
        this.context = context;
    }

    @When("the accounts view is opened")
    public void theAccountsViewIsOpened() {
        context.bankingPage().openAccounts();
    }

    @When("the transfer form is opened")
    public void theTransferFormIsOpened() {
        context.bankingPage().openTransfers();
        context.bankingPage().openTransferForm();
    }

    @When("the source account balance is noted")
    public void theSourceAccountBalanceIsNoted() {
        context.bankingPage().openAccounts();
        context.rememberBalance(context.bankingPage().balanceMinor(0));
    }

    @When("a transfer of {string} is submitted to the first beneficiary")
    public void aTransferIsSubmitted(String amount) {
        context.bankingPage().openTransfers();
        context.bankingPage().openTransferForm();
        context.bankingPage().selectFirstSourceAndBeneficiary();
        context.bankingPage().enterAmount(amount);
        context.bankingPage().submitTransfer();
    }

    @Then("at least {int} accounts are listed")
    public void atLeastAccountsAreListed(int expected) {
        int actual = context.bankingPage().accountCount();

        assertTrue(actual >= expected,
                "expected at least " + expected + " accounts, found " + actual);
    }

    @Then("at least {int} source accounts can be chosen")
    public void atLeastSourceAccountsCanBeChosen(int expected) {
        assertTrue(context.bankingPage().sourceAccountCount() >= expected);
    }

    @Then("the source account is debited by at least {long} minor units")
    public void theSourceAccountIsDebitedBy(long minimum) {
        context.bankingPage().openAccounts();

        long debited = context.rememberedBalance() - context.bankingPage().balanceMinor(0);

        assertTrue(debited >= minimum,
                "expected a debit of at least " + minimum + " minor units, saw " + debited);
    }

    @Then("the source account balance is unchanged")
    public void theSourceAccountBalanceIsUnchanged() {
        context.bankingPage().openAccounts();

        assertEquals(context.bankingPage().balanceMinor(0), context.rememberedBalance(),
                "a rejected transfer must not move money");
    }

    @Then("the amount {string} is accepted by the form")
    public void theAmountIsAccepted(String amount) {
        assertTrue(context.bankingPage().isAmountAccepted(amount));
    }

    @Then("the amount {string} is refused by the form")
    public void theAmountIsRefused(String amount) {
        assertFalse(context.bankingPage().isAmountAccepted(amount));
    }

    @And("the outcome is reported as {string}")
    public void theOutcomeIsReportedAs(String fragment) {
        String toast = context.bankingPage().toastText();

        assertTrue(toast.toLowerCase().contains(fragment.toLowerCase()),
                "expected the message to mention " + fragment + ", saw: " + toast);
    }
}
