package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.LoansCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoanApplicationTest extends CustomerTestBase {

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
            groups = {"regression", "loans"},
            description = "Customer can select loan term"
    )
    public void customerShouldSelectLoanTerm() {

        LoansCurrentPage loansPage =
                openLoansPage();

        loansPage.selectTerm(
                "24 months"
        );

        Assert.assertEquals(
                loansPage.getSelectedTerm(),
                "24 months",
                "24-month term should be selected."
        );
    }

    @Test(
            groups = {"regression", "loans", "boundary"},
            description = "Minimum loan amount is valid"
    )
    public void minimumLoanAmountShouldBeValid() {

        LoansCurrentPage loansPage =
                openLoansPage();

        loansPage.enterAmount(
                "1000"
        );

        Assert.assertTrue(
                loansPage.isAmountValid(),
                "$1,000 should be a valid loan amount."
        );
    }

    @Test(
            groups = {"regression", "loans", "boundary"},
            description = "Maximum loan amount is valid"
    )
    public void maximumLoanAmountShouldBeValid() {

        LoansCurrentPage loansPage =
                openLoansPage();

        loansPage.enterAmount(
                "50000"
        );

        Assert.assertTrue(
                loansPage.isAmountValid(),
                "$50,000 should be a valid loan amount."
        );
    }

    @Test(
            groups = {"regression", "loans", "validation"},
            description = "Amount below minimum loan value is invalid"
    )
    public void amountBelowMinimumShouldBeInvalid() {

        LoansCurrentPage loansPage =
                openLoansPage();

        loansPage.enterAmount(
                "999"
        );

        Assert.assertFalse(
                loansPage.isAmountValid(),
                "$999 should be rejected by loan amount validation."
        );
    }

    @Test(
            groups = {"regression", "loans", "validation"},
            description = "Amount above maximum loan value is invalid"
    )
    public void amountAboveMaximumShouldBeInvalid() {

        LoansCurrentPage loansPage =
                openLoansPage();

        loansPage.enterAmount(
                "50001"
        );

        Assert.assertFalse(
                loansPage.isAmountValid(),
                "$50,001 should be rejected by loan amount validation."
        );
    }

    @Test(
            groups = {"regression", "loans"},
            description = "Customer can submit a valid loan application"
    )
    public void validLoanApplicationShouldBeSubmitted() {

        LoansCurrentPage loansPage =
                openLoansPage();

        loansPage.applyForLoan(
                "5000",
                "24 months"
        );

        loansPage.waitForToastToContain(
                "Loan application submitted"
        );

        Assert.assertEquals(
                loansPage.getToastText(),
                "Loan application submitted.",
                "Valid loan application should be submitted successfully."
        );
    }

    @Test(
            groups = {"regression", "loans", "boundary"},
            description = "Minimum boundary loan application can be submitted"
    )
    public void minimumBoundaryApplicationShouldBeSubmitted() {

        LoansCurrentPage loansPage =
                openLoansPage();

        loansPage.applyForLoan(
                "1000",
                "12 months"
        );

        loansPage.waitForToastToContain(
                "Loan application submitted"
        );

        Assert.assertEquals(
                loansPage.getToastText(),
                "Loan application submitted.",
                "Minimum valid loan amount should be accepted."
        );
    }

    @Test(
            groups = {"regression", "loans", "boundary"},
            description = "Maximum boundary loan application can be submitted"
    )
    public void maximumBoundaryApplicationShouldBeSubmitted() {

        LoansCurrentPage loansPage =
                openLoansPage();

        loansPage.applyForLoan(
                "50000",
                "36 months"
        );

        loansPage.waitForToastToContain(
                "Loan application submitted"
        );

        Assert.assertEquals(
                loansPage.getToastText(),
                "Loan application submitted.",
                "Maximum valid loan amount should be accepted."
        );
    }
}