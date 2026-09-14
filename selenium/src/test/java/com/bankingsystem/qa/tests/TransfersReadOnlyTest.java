package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.TransfersCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class TransfersReadOnlyTest extends CustomerTestBase {

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
            groups = {"smoke", "transfers"},
            description = "Transfers page loads successfully"
    )
    public void transfersPageShouldLoadSuccessfully() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        Assert.assertEquals(
                transfersPage.getPageTitleText(),
                "Transfers",
                "Transfers page heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "transfers"},
            description = "Transfer source accounts are displayed"
    )
    public void sourceAccountsShouldBeDisplayed() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        List<String> accounts =
                transfersPage.getFromAccountOptions();

        Assert.assertTrue(
                accounts.size() >= 2,
                "At least two source accounts should be available."
        );

        Assert.assertTrue(
                accounts.stream()
                        .anyMatch(account ->
                                account.contains("Checking")
                        ),
                "Checking account should be available."
        );

        Assert.assertTrue(
                accounts.stream()
                        .anyMatch(account ->
                                account.contains("Savings")
                        ),
                "Savings account should be available."
        );
    }

    @Test(
            groups = {"smoke", "transfers"},
            description = "Transfer recipients are displayed"
    )
    public void recipientsShouldBeDisplayed() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        List<String> recipients =
                transfersPage.getRecipientOptions();

        Assert.assertTrue(
                recipients.size() >= 2,
                "At least two recipients should be available."
        );

        Assert.assertTrue(
                recipients.contains(
                        "Alex Johnson"
                ),
                "Alex Johnson should be available."
        );

        Assert.assertTrue(
                recipients.contains(
                        "Sam Lee"
                ),
                "Sam Lee should be available."
        );
    }

    @Test(
            groups = {"regression", "transfers"},
            description = "Customer can select a source account"
    )
    public void customerShouldSelectSourceAccount() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        transfersPage.selectFromAccountByIndex(
                1
        );

        Assert.assertTrue(
                transfersPage.getSelectedFromAccountText()
                        .contains("Savings"),
                "Savings account should be selected."
        );
    }

    @Test(
            groups = {"regression", "transfers"},
            description = "Customer can select a transfer recipient"
    )
    public void customerShouldSelectRecipient() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        transfersPage.selectRecipient(
                "Sam Lee"
        );

        Assert.assertEquals(
                transfersPage.getSelectedRecipientText(),
                "Sam Lee",
                "Selected recipient should be Sam Lee."
        );
    }

    @Test(
            groups = {"regression", "transfers"},
            description = "Valid transfer completes successfully"
    )
    public void validTransferShouldCompleteSuccessfully() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        transfersPage.transfer(
                0,
                "Alex Johnson",
                "100"
        );

        transfersPage.waitForToastToContain(
                "Transfer completed"
        );

        Assert.assertEquals(
                transfersPage.getToastText(),
                "Transfer completed.",
                "Successful transfer confirmation should be displayed."
        );
    }

    @Test(
            groups = {"regression", "transfers", "validation"},
            description = "Transfer above single transfer limit is rejected"
    )
    public void transferAboveLimitShouldBeRejected() {

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
                "Transfer above $10,000 should be rejected."
        );
    }

    @Test(
            groups = {"regression", "transfers", "boundary"},
            description = "Maximum transfer limit is accepted"
    )
    public void maximumTransferLimitShouldBeAccepted() {

        TransfersCurrentPage transfersPage =
                openTransfersPage();

        transfersPage.transfer(
                0,
                "Sam Lee",
                "10000"
        );

        transfersPage.waitForToastToContain(
                "Transfer completed"
        );

        Assert.assertEquals(
                transfersPage.getToastText(),
                "Transfer completed.",
                "$10,000 transfer should be allowed."
        );
    }
}