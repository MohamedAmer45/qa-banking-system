package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.DashboardPage;
import com.bankingsystem.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CustomerAuthenticationTest extends BaseTest {

    @Test(
            groups = {"smoke", "authentication"},
            description = "Customer can authenticate using demo customer session"
    )
    public void customerShouldStartDemoSessionSuccessfully() {

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
                "Logged-in role should be customer."
        );

        Assert.assertFalse(
                dashboardPage.isAdminNavigationDisplayed(),
                "Customer must not see the admin console."
        );
    }
}
