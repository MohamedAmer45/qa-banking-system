package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.AdminCurrentPage;
import com.bankingsystem.qa.pages.DashboardPage;
import com.bankingsystem.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class AdminConsoleTest extends BaseTest {

    private AdminCurrentPage loginAndOpenAdmin() {

        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "NovaBank session page should load."
        );

        loginPage.enterAsAdmin();

        DashboardPage dashboardPage =
                new DashboardPage(
                        getDriver()
                );

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Dashboard should load for admin."
        );

        Assert.assertTrue(
                dashboardPage.getLoggedInUserText()
                        .toLowerCase()
                        .contains("admin"),
                "Authenticated session should use the admin role."
        );

        AdminCurrentPage adminPage =
                new AdminCurrentPage(
                        getDriver()
                );

        adminPage.open();

        Assert.assertTrue(
                adminPage.isLoaded(),
                "Admin console should load."
        );

        return adminPage;
    }

    @Test(
            groups = {"smoke", "admin"},
            description = "Admin console loads successfully"
    )
    public void adminConsoleShouldLoadSuccessfully() {

        AdminCurrentPage adminPage =
                loginAndOpenAdmin();

        Assert.assertEquals(
                adminPage.getPageTitleText(),
                "Admin console",
                "Admin console heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "admin"},
            description = "Admin summary metrics are displayed"
    )
    public void adminMetricsShouldBeDisplayed() {

        AdminCurrentPage adminPage =
                loginAndOpenAdmin();

        Assert.assertTrue(
                adminPage.getMetricCount() >= 5,
                "At least five admin summary metrics should be displayed."
        );
    }

    @Test(
            groups = {"regression", "admin"},
            description = "All admin metrics contain values"
    )
    public void allAdminMetricsShouldContainValues() {

        AdminCurrentPage adminPage =
                loginAndOpenAdmin();

        Assert.assertTrue(
                adminPage.allMetricsHaveValues(),
                "Every admin metric should contain a value."
        );
    }

    @Test(
            groups = {"regression", "admin"},
            description = "Admin summary contains expected banking metrics"
    )
    public void expectedAdminMetricsShouldBeAvailable() {

        AdminCurrentPage adminPage =
                loginAndOpenAdmin();

        Map<String, String> metrics =
                adminPage.getMetrics();

        Assert.assertTrue(
                metrics.containsKey("customers"),
                "Customers metric should be displayed."
        );

        Assert.assertTrue(
                metrics.containsKey("accounts"),
                "Accounts metric should be displayed."
        );

        Assert.assertTrue(
                metrics.containsKey("transactionsToday"),
                "Today's transactions metric should be displayed."
        );

        Assert.assertTrue(
                metrics.containsKey("totalDeposits"),
                "Total deposits metric should be displayed."
        );

        Assert.assertTrue(
                metrics.containsKey("flaggedTransactions"),
                "Flagged transactions metric should be displayed."
        );
    }

    @Test(
            groups = {"regression", "admin"},
            description = "Admin summary returns expected deterministic QA data"
    )
    public void adminMetricsShouldMatchSeededData() {

        AdminCurrentPage adminPage =
                loginAndOpenAdmin();

        Assert.assertEquals(
                adminPage.getMetricValue("customers"),
                "1,248",
                "Customer count should match seeded QA data."
        );

        Assert.assertEquals(
                adminPage.getMetricValue("accounts"),
                "1,984",
                "Account count should match seeded QA data."
        );

        Assert.assertEquals(
                adminPage.getMetricValue("transactionsToday"),
                "378",
                "Transaction count should match seeded QA data."
        );

        Assert.assertEquals(
                adminPage.getMetricValue("totalDeposits"),
                "8,420,000",
                "Total deposits should match seeded QA data."
        );

        Assert.assertEquals(
                adminPage.getMetricValue("flaggedTransactions"),
                "7",
                "Flagged transaction count should match seeded QA data."
        );
    }
}