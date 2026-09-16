package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.LoansCurrentPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Locale;

public class LoanApplicationSteps {

    private final TestContext testContext;
    private LoansCurrentPage loansPage;

    public LoanApplicationSteps(TestContext testContext) {

        this.testContext = testContext;
    }

    private LoansCurrentPage requireLoansPage() {

        Assert.assertNotNull(
                loansPage,
                "The Loans page must be opened before using it."
        );

        return loansPage;
    }

    @Given("the customer opens the Loans page")
    public void theCustomerOpensTheLoansPage() {

        loansPage =
                new LoansCurrentPage(
                        testContext.getDriver()
                );

        loansPage.open();

        Assert.assertTrue(
                loansPage.isLoaded(),
                "Loans page should load successfully."
        );
    }

    @Then("the Loans page should be displayed")
    public void theLoansPageShouldBeDisplayed() {

        LoansCurrentPage page =
                requireLoansPage();

        Assert.assertTrue(
                page.isLoaded(),
                "Loans page should be visible."
        );

        Assert.assertEquals(
                page.getPageTitleText(),
                "Loans",
                "Loans heading should be displayed."
        );
    }

    @Then("the current personal loan information should be displayed")
    public void theCurrentPersonalLoanInformationShouldBeDisplayed() {

        LoansCurrentPage page =
                requireLoansPage();

        Assert.assertEquals(
                page.getLoanType(),
                "Personal Loan",
                "Existing loan type should be displayed."
        );

        Assert.assertFalse(
                page.getLoanBalance().isBlank(),
                "Loan balance should be displayed."
        );

        Assert.assertTrue(
                page.getLoanBalance().contains("$"),
                "Loan balance should display currency."
        );

        Assert.assertFalse(
                page.getLoanDetails().isBlank(),
                "Loan details should be displayed."
        );

        Assert.assertTrue(
                page.getLoanDetails()
                        .toUpperCase(Locale.ROOT)
                        .contains("APR"),
                "Loan details should contain APR information."
        );
    }

    @Then("the supported 12, 24, and 36 month loan terms should be available")
    public void supportedLoanTermsShouldBeAvailable() {

        List<String> terms =
                requireLoansPage().getTermOptions();

        Assert.assertTrue(
                terms.contains("12 months"),
                "12-month term should be available."
        );

        Assert.assertTrue(
                terms.contains("24 months"),
                "24-month term should be available."
        );

        Assert.assertTrue(
                terms.contains("36 months"),
                "36-month term should be available."
        );
    }

    @Then("the loan amount range should be {string} through {string}")
    public void theLoanAmountRangeShouldBe(
            String minimum,
            String maximum
    ) {

        LoansCurrentPage page =
                requireLoansPage();

        Assert.assertEquals(
                page.getMinimumAmount(),
                minimum,
                "Minimum loan amount is incorrect."
        );

        Assert.assertEquals(
                page.getMaximumAmount(),
                maximum,
                "Maximum loan amount is incorrect."
        );
    }

    @When("the customer selects the {string} loan term")
    public void theCustomerSelectsTheLoanTerm(
            String term
    ) {

        requireLoansPage().selectTerm(
                term
        );
    }

    @Then("the selected loan term should be {string}")
    public void theSelectedLoanTermShouldBe(
            String expectedTerm
    ) {

        Assert.assertEquals(
                requireLoansPage().getSelectedTerm(),
                expectedTerm,
                "The expected loan term should be selected."
        );
    }

    @When("the customer enters loan amount {string}")
    public void theCustomerEntersLoanAmount(
            String amount
    ) {

        requireLoansPage().enterAmount(
                amount
        );
    }

    @Then("the loan amount should be valid")
    public void theLoanAmountShouldBeValid() {

        Assert.assertTrue(
                requireLoansPage().isAmountValid(),
                "The loan amount should satisfy browser validation."
        );
    }

    @Then("the loan amount should be invalid")
    public void theLoanAmountShouldBeInvalid() {

        Assert.assertFalse(
                requireLoansPage().isAmountValid(),
                "The loan amount should be rejected by browser validation."
        );
    }

    @When("the customer applies for a loan of {string} for {string}")
    public void theCustomerAppliesForALoan(
            String amount,
            String term
    ) {

        requireLoansPage().applyForLoan(
                amount,
                term
        );
    }

    @Then("the loan application should be submitted successfully")
    public void theLoanApplicationShouldBeSubmittedSuccessfully() {

        LoansCurrentPage page =
                requireLoansPage();

        page.waitForToastToContain(
                "Loan application submitted"
        );

        Assert.assertEquals(
                page.getToastText(),
                "Loan application submitted.",
                "Valid loan application should be submitted successfully."
        );
    }
}
