package com.bankingsystem.qa.base;

import com.bankingsystem.qa.pages.DashboardPage;
import com.bankingsystem.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

public abstract class CustomerTestBase extends BaseTest {

    protected DashboardPage dashboardPage;

    @BeforeMethod(alwaysRun = true)
    public void authenticateCustomer() {

        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "NovaBank demo session page should load."
        );

        loginPage.enterAsCustomer();

        dashboardPage =
                new DashboardPage(
                        getDriver()
                );

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Customer dashboard should load after starting the customer demo session."
        );

        Assert.assertTrue(
                dashboardPage.getLoggedInUserText()
                        .toLowerCase()
                        .contains("customer"),
                "Authenticated session should use the customer role."
        );
    }
}
