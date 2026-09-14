package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.DashboardPage;
import com.bankingsystem.qa.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RoleAuthorizationTest extends BaseTest {

    @Test(
            groups = {"smoke", "authorization"},
            description = "Customer cannot see admin navigation"
    )
    public void customerShouldNotSeeAdminNavigation() {

        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();
        loginPage.enterAsCustomer();

        DashboardPage dashboardPage =
                new DashboardPage(
                        getDriver()
                );

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Customer dashboard should load."
        );

        Assert.assertFalse(
                dashboardPage.isAdminNavigationDisplayed(),
                "Customer must not see the Admin navigation option."
        );
    }

    @Test(
            groups = {"smoke", "authorization"},
            description = "Admin can see admin navigation"
    )
    public void adminShouldSeeAdminNavigation() {

        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();
        loginPage.enterAsAdmin();

        DashboardPage dashboardPage =
                new DashboardPage(
                        getDriver()
                );

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Admin dashboard should load."
        );

        Assert.assertTrue(
                dashboardPage.isAdminNavigationDisplayed(),
                "Admin should see the Admin navigation option."
        );
    }

    @Test(
            groups = {"regression", "authorization"},
            description = "Customer admin navigation remains hidden in the DOM"
    )
    public void customerAdminNavigationShouldHaveHiddenState() {

        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();
        loginPage.enterAsCustomer();

        WebElement adminNavigation =
                getDriver().findElement(
                        By.id("adminNav")
                );

        Assert.assertFalse(
                adminNavigation.isDisplayed(),
                "Admin navigation should remain hidden for customer role."
        );

        String classes =
                adminNavigation.getAttribute(
                        "class"
                );

        Assert.assertTrue(
                classes != null &&
                classes.contains("hidden"),
                "Admin navigation should have the hidden CSS class for customers."
        );
    }

    @Test(
            groups = {"regression", "authorization"},
            description = "Admin navigation is enabled after admin authentication"
    )
    public void adminNavigationShouldBecomeVisibleForAdmin() {

        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();
        loginPage.enterAsAdmin();

        WebElement adminNavigation =
                getDriver().findElement(
                        By.id("adminNav")
                );

        Assert.assertTrue(
                adminNavigation.isDisplayed(),
                "Admin navigation should be visible."
        );

        String classes =
                adminNavigation.getAttribute(
                        "class"
                );

        Assert.assertTrue(
                classes == null ||
                !classes.contains("hidden"),
                "Admin navigation should not have the hidden class for admin sessions."
        );
    }

    @Test(
            groups = {"regression", "authorization"},
            description = "Customer and admin sessions expose different roles"
    )
    public void roleInformationShouldMatchAuthenticatedSession() {

        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();
        loginPage.enterAsCustomer();

        DashboardPage dashboardPage =
                new DashboardPage(
                        getDriver()
                );

        Assert.assertTrue(
                dashboardPage.getLoggedInUserText()
                        .toLowerCase()
                        .contains("customer"),
                "Customer session should identify customer role."
        );

        loginPage.logout();

        loginPage.enterAsAdmin();

        Assert.assertTrue(
                dashboardPage.getLoggedInUserText()
                        .toLowerCase()
                        .contains("admin"),
                "Admin session should identify admin role."
        );
    }
}