package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.BillsCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class BillsReadOnlyTest extends CustomerTestBase {

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
            groups = {"smoke", "bills"},
            description = "Bills page loads successfully"
    )
    public void billsPageShouldLoadSuccessfully() {

        BillsCurrentPage billsPage =
                openBillsPage();

        Assert.assertEquals(
                billsPage.getPageTitleText(),
                "Bills",
                "Bills page heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "bills"},
            description = "Available billers are displayed"
    )
    public void availableBillersShouldBeDisplayed() {

        BillsCurrentPage billsPage =
                openBillsPage();

        List<String> billers =
                billsPage.getBillerOptions();

        Assert.assertTrue(
                billers.size() >= 4,
                "At least four billers should be available."
        );

        Assert.assertTrue(
                billers.contains("Electricity"),
                "Electricity biller should be available."
        );

        Assert.assertTrue(
                billers.contains("Water"),
                "Water biller should be available."
        );

        Assert.assertTrue(
                billers.contains("Internet"),
                "Internet biller should be available."
        );

        Assert.assertTrue(
                billers.contains("Mobile"),
                "Mobile biller should be available."
        );
    }

    @Test(
            groups = {"regression", "bills"},
            description = "Customer can select a biller"
    )
    public void customerShouldSelectBiller() {

        BillsCurrentPage billsPage =
                openBillsPage();

        billsPage.selectBiller(
                "Internet"
        );

        Assert.assertEquals(
                billsPage.getSelectedBiller(),
                "Internet",
                "Internet should be selected."
        );
    }

    @Test(
            groups = {"regression", "bills"},
            description = "Valid bill payment completes successfully"
    )
    public void validBillPaymentShouldComplete() {

        BillsCurrentPage billsPage =
                openBillsPage();

        billsPage.payBill(
                "Electricity",
                "50"
        );

        billsPage.waitForToastToContain(
                "Bill paid"
        );

        Assert.assertEquals(
                billsPage.getToastText(),
                "Bill paid.",
                "Successful bill payment confirmation should be displayed."
        );
    }
}