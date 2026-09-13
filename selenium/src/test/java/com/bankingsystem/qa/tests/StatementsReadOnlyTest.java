package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.StatementsPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class StatementsReadOnlyTest extends CustomerTestBase {

    private StatementsPage statementsPage;


    @BeforeMethod(alwaysRun = true)
    public void openStatementsPage() {

        statementsPage =
                dashboardPage.openStatements();

        Assert.assertTrue(
                statementsPage.isLoaded(),
                "Statements page should load successfully."
        );
    }


    @Test
    public void statementsPageShouldOpenSuccessfully() {

        Assert.assertEquals(
                statementsPage.getPageTitleText(),
                "Statements",
                "Page title should be Statements."
        );

        Assert.assertTrue(
                statementsPage.hasTransactionsOrEmptyState(),
                "Statement should show transactions or an empty-state message."
        );
    }


    @Test
    public void statementAccountSelectorShouldContainAccounts() {

        List<String> accounts =
                statementsPage.getAccountOptions();

        Assert.assertFalse(
                accounts.isEmpty(),
                "At least one account should be available for statements."
        );

        for (String account : accounts) {

            Assert.assertFalse(
                    account.isBlank(),
                    "Statement account option should not be blank."
            );
        }
    }


    @Test
    public void statementDateRangeShouldUseDateInputs() {

        Assert.assertEquals(
                statementsPage.getFromDateType(),
                "date",
                "Statement From field should use a date input."
        );

        Assert.assertEquals(
                statementsPage.getToDateType(),
                "date",
                "Statement To field should use a date input."
        );

        Assert.assertFalse(
                statementsPage.getFromDateValue().isBlank(),
                "Default statement From date should be populated."
        );

        Assert.assertFalse(
                statementsPage.getToDateValue().isBlank(),
                "Default statement To date should be populated."
        );

        Assert.assertTrue(
                statementsPage.isApplyButtonDisplayed(),
                "Apply button should be displayed."
        );
    }


    @Test
    public void statementSummaryShouldDisplayFinancialTotals() {

        List<String> labels =
                statementsPage.getSummaryLabels();

        Assert.assertTrue(
                labels.contains("Opening"),
                "Statement should display opening balance."
        );

        Assert.assertTrue(
                labels.contains("Credits"),
                "Statement should display credits."
        );

        Assert.assertTrue(
                labels.contains("Debits"),
                "Statement should display debits."
        );

        Assert.assertTrue(
                labels.contains("Fees"),
                "Statement should display fees."
        );

        Assert.assertTrue(
                labels.contains("Closing"),
                "Statement should display closing balance."
        );


        List<String> values =
                statementsPage.getSummaryValues();

        Assert.assertEquals(
                values.size(),
                5,
                "Statement summary should contain five financial totals."
        );

        for (String value : values) {

            Assert.assertFalse(
                    value.isBlank(),
                    "Statement summary value should not be blank."
            );
        }
    }


    @Test
    public void statementShouldExposeExportControls() {

        Assert.assertTrue(
                statementsPage.isDownloadCsvDisplayed(),
                "Download CSV control should be displayed."
        );

        Assert.assertTrue(
                statementsPage.isPrintPdfDisplayed(),
                "Print / PDF control should be displayed."
        );
    }


    @Test
    public void customStatementDateRangeShouldAcceptInput() {

        String fromDate =
                "2026-01-01";

        String toDate =
                "2026-01-31";


        statementsPage.enterFromDate(
                fromDate
        );

        statementsPage.enterToDate(
                toDate
        );


        Assert.assertEquals(
                statementsPage.getFromDateValue(),
                fromDate,
                "Custom From date should be accepted."
        );

        Assert.assertEquals(
                statementsPage.getToDateValue(),
                toDate,
                "Custom To date should be accepted."
        );
    }
}
