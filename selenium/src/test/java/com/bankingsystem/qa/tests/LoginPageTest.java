package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.DashboardPage;
import com.bankingsystem.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {

    @Test(
            groups = {"smoke", "authentication"},
            description = "Demo session page loads successfully"
    )
    public void loginPageShouldLoadSuccessfully() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        loginPage.open();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "Demo session page should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "authentication"},
            description = "Customer demo session option is displayed"
    )
    public void customerSessionOptionShouldBeDisplayed() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        loginPage.open();

        Assert.assertTrue(
                loginPage.isCustomerButtonDisplayed(),
                "Enter as customer button should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "authentication"},
            description = "Admin demo session option is displayed"
    )
    public void adminSessionOptionShouldBeDisplayed() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        loginPage.open();

        Assert.assertTrue(
                loginPage.isAdminButtonDisplayed(),
                "Enter as admin button should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "authentication"},
            description = "Customer can start a demo banking session"
    )
    public void customerShouldEnterApplication() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        loginPage.open();
        loginPage.enterAsCustomer();

        DashboardPage dashboardPage =
                new DashboardPage(getDriver());

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Customer dashboard should load."
        );

        Assert.assertTrue(
                dashboardPage.getLoggedInUserText()
                        .toLowerCase()
                        .contains("customer"),
                "Session should use customer role."
        );

        Assert.assertFalse(
                dashboardPage.isAdminNavigationDisplayed(),
                "Customer should not have access to admin navigation."
        );
    }

    @Test(
            groups = {"regression", "authentication"},
            description = "Admin can start an admin demo session"
    )
    public void adminShouldEnterApplication() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        loginPage.open();
        loginPage.enterAsAdmin();

        DashboardPage dashboardPage =
                new DashboardPage(getDriver());

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Dashboard should load for admin."
        );

        Assert.assertTrue(
                dashboardPage.getLoggedInUserText()
                        .toLowerCase()
                        .contains("admin"),
                "Session should use admin role."
        );

        Assert.assertTrue(
                dashboardPage.isAdminNavigationDisplayed(),
                "Admin navigation should be displayed."
        );
    }

    @Test(
            groups = {"regression", "authentication"},
            description = "Logout returns user to demo session page"
    )
    public void logoutShouldReturnToSessionPage() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        loginPage.open();
        loginPage.enterAsCustomer();
        loginPage.logout();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "Demo session page should return after logout."
        );
    }
}
