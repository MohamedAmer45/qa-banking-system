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
                adminPage.getMetricCount() >= 3,
                "At least three admin summary metrics should be displayed."
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
            description = "Admin summary contains current banking metrics"
    )
    public void expectedAdminMetricsShouldBeAvailable() {

        AdminCurrentPage adminPage =
                loginAndOpenAdmin();

        Map<String, String> metrics =
                adminPage.getMetrics();

        Assert.assertTrue(
                metrics.containsKey("active users"),
                "Active users metric should be displayed."
        );

        Assert.assertTrue(
                metrics.containsKey("open accounts"),
                "Open accounts metric should be displayed."
        );

        Assert.assertTrue(
                metrics.containsKey("pending reviews"),
                "Pending reviews metric should be displayed."
        );
    }

    @Test(
            groups = {"regression", "admin"},
            description = "Admin summary returns current deterministic QA data"
    )
    public void adminMetricsShouldMatchSeededData() {

        AdminCurrentPage adminPage =
                loginAndOpenAdmin();

        Assert.assertEquals(
                adminPage.getMetricValue("active users"),
                "1,284",
                "Active user count should match seeded QA data."
        );

        Assert.assertEquals(
                adminPage.getMetricValue("open accounts"),
                "2,310",
                "Open account count should match seeded QA data."
        );

        Assert.assertEquals(
                adminPage.getMetricValue("pending reviews"),
                "17",
                "Pending review count should match seeded QA data."
        );
    }
}
