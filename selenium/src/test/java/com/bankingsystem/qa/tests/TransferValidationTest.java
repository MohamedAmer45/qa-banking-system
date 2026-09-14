package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.TransfersCurrentPage;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TransferValidationTest extends CustomerTestBase {

    private TransfersCurrentPage openTransfersPage() {

        dashboardPage.openTransfers();

        TransfersCurrentPage transfersPage =
                new TransfersCurrentPage(
                        getDriver()
                );

        Assert.assertTrue(
                transfersPage.isLoaded(),
                "Transfers page should load successfully."
        );

        return transfersPage;
    }

    @Test(
            groups = {"regression", "transfers", "validation"},
            description = "Zero transfer amount is rejected"
    )
    public void zeroAmountShouldBeRejected() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        /*
         * HTML number validation blocks normal submission for zero
         * because min=0.01. We remove the browser-side min attribute
         * so the application validation itself can be tested.
         */

        JavascriptExecutor js =
                (JavascriptExecutor) getDriver();

        js.executeScript(
                "document.getElementById('transferAmount').removeAttribute('min');"
        );

        transfersPage.transfer(
                0,
                "Alex Johnson",
                "0"
        );

        transfersPage.waitForToastToContain(
                "Amount must be positive"
        );

        Assert.assertEquals(
                transfersPage.getToastText(),
                "Amount must be positive.",
                "Zero transfer amount should be rejected."
        );
    }

    @Test(
            groups = {"regression", "transfers", "validation"},
            description = "Negative transfer amount is rejected"
    )
    public void negativeAmountShouldBeRejected() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        JavascriptExecutor js =
                (JavascriptExecutor) getDriver();

        js.executeScript(
                "document.getElementById('transferAmount').removeAttribute('min');"
        );

        transfersPage.transfer(
                0,
                "Alex Johnson",
                "-1"
        );

        transfersPage.waitForToastToContain(
                "Amount must be positive"
        );

        Assert.assertEquals(
                transfersPage.getToastText(),
                "Amount must be positive.",
                "Negative transfer amount should be rejected."
        );
    }

    @Test(
            groups = {"regression", "transfers", "boundary"},
            description = "Amount immediately above transfer limit is rejected"
    )
    public void amountImmediatelyAboveLimitShouldBeRejected() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        transfersPage.transfer(
                0,
                "Alex Johnson",
                "10000.01"
        );

        transfersPage.waitForToastToContain(
                "Transfer limit exceeded"
        );

        Assert.assertEquals(
                transfersPage.getToastText(),
                "Transfer limit exceeded.",
                "Amount immediately above the transfer limit should be rejected."
        );
    }
}