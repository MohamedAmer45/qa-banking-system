package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.LoansCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class LoansReadOnlyTest extends CustomerTestBase {

    private LoansCurrentPage openLoansPage() {

        dashboardPage.openLoans();

        LoansCurrentPage loansPage =
                new LoansCurrentPage(
                        getDriver()
                );

        Assert.assertTrue(
                loansPage.isLoaded(),
                "Loans page should load successfully."
        );

        return loansPage;
    }

    @Test(
            groups = {"smoke", "loans"},
            description = "Loans page loads successfully"
    )
    public void loansPageShouldLoadSuccessfully() {

        LoansCurrentPage loansPage =
                openLoansPage();

        Assert.assertEquals(
                loansPage.getPageTitleText(),
                "Loans",
                "Loans heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "loans"},
            description = "Existing loan information is displayed"
    )
    public void currentLoanInformationShouldBeDisplayed() {

        LoansCurrentPage loansPage =
                openLoansPage();

        Assert.assertEquals(
                loansPage.getLoanType(),
                "Personal Loan",
                "Existing loan type should be displayed."
        );

        Assert.assertFalse(
                loansPage.getLoanBalance().isBlank(),
                "Loan balance should be displayed."
        );

        Assert.assertTrue(
                loansPage.getLoanBalance().contains("$"),
                "Loan balance should display currency."
        );

        Assert.assertFalse(
                loansPage.getLoanDetails().isBlank(),
                "Loan details should be displayed."
        );
    }

    @Test(
            groups = {"regression", "loans"},
            description = "Loan details include APR information"
    )
    public void loanDetailsShouldIncludeApr() {

        LoansCurrentPage loansPage =
                openLoansPage();

        Assert.assertTrue(
                loansPage.getLoanDetails()
                        .toUpperCase()
                        .contains("APR"),
                "Loan details should contain APR information."
        );
    }

    @Test(
            groups = {"regression", "loans"},
            description = "Supported loan terms are displayed"
    )
    public void supportedLoanTermsShouldBeDisplayed() {

        LoansCurrentPage loansPage =
                openLoansPage();

        List<String> terms =
                loansPage.getTermOptions();

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

    @Test(
            groups = {"regression", "loans"},
            description = "Loan amount boundaries are configured correctly"
    )
    public void loanAmountBoundariesShouldBeConfigured() {

        LoansCurrentPage loansPage =
                openLoansPage();

        Assert.assertEquals(
                loansPage.getMinimumAmount(),
                "1000",
                "Minimum loan amount should be $1,000."
        );

        Assert.assertEquals(
                loansPage.getMaximumAmount(),
                "50000",
                "Maximum loan amount should be $50,000."
        );
    }
}