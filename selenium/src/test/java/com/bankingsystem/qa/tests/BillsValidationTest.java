package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.BillsCurrentPage;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BillsValidationTest extends CustomerTestBase {

    private BillsCurrentPage openBillsPage() {

        dashboardPage.openBills();

        BillsCurrentPage billsPage =
                new BillsCurrentPage(
                        getDriver()
                );

        Assert.assertTrue(
                billsPage.isLoaded(),
                "Bills page should load successfully."
        );

        return billsPage;
    }

    @Test(
            groups = {"regression", "bills", "validation"},
            description = "Zero bill amount is rejected"
    )
    public void zeroAmountShouldBeRejected() {

        BillsCurrentPage billsPage =
                openBillsPage();

        JavascriptExecutor js =
                (JavascriptExecutor) getDriver();

        js.executeScript(
                "document.getElementById('billAmount').removeAttribute('min');"
        );

        billsPage.payBill(
                "Electricity",
                "0"
        );

        billsPage.waitForToastToContain(
                "Invalid bill amount"
        );

        Assert.assertEquals(
                billsPage.getToastText(),
                "Invalid bill amount.",
                "Zero bill amount should be rejected."
        );
    }

    @Test(
            groups = {"regression", "bills", "validation"},
            description = "Negative bill amount is rejected"
    )
    public void negativeAmountShouldBeRejected() {

        BillsCurrentPage billsPage =
                openBillsPage();

        JavascriptExecutor js =
                (JavascriptExecutor) getDriver();

        js.executeScript(
                "document.getElementById('billAmount').removeAttribute('min');"
        );

        billsPage.payBill(
                "Water",
                "-10"
        );

        billsPage.waitForToastToContain(
                "Invalid bill amount"
        );

        Assert.assertEquals(
                billsPage.getToastText(),
                "Invalid bill amount.",
                "Negative bill amount should be rejected."
        );
    }

    @Test(
            groups = {"regression", "bills", "validation"},
            description = "Bill payment above available balance is rejected"
    )
    public void amountAboveAvailableBalanceShouldBeRejected() {

        BillsCurrentPage billsPage =
                openBillsPage();

        billsPage.payBill(
                "Internet",
                "999999"
        );

        billsPage.waitForToastToContain(
                "Invalid bill amount"
        );

        Assert.assertEquals(
                billsPage.getToastText(),
                "Invalid bill amount.",
                "Bill amount greater than available balance should be rejected."
        );
    }

    @Test(
            groups = {"regression", "bills", "boundary"},
            description = "Minimum valid bill amount is accepted"
    )
    public void minimumValidAmountShouldBeAccepted() {

        BillsCurrentPage billsPage =
                openBillsPage();

        billsPage.payBill(
                "Mobile",
                "0.01"
        );

        billsPage.waitForToastToContain(
                "Bill paid"
        );

        Assert.assertEquals(
                billsPage.getToastText(),
                "Bill paid.",
                "Minimum valid bill amount should be accepted."
        );
    }
}