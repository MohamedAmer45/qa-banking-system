package com.bankingsystem.qa.base;

import com.bankingsystem.qa.pages.DashboardPage;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.pages.MfaPage;
import com.bankingsystem.qa.utils.TestCredentials;

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

        loginPage.clearIdentifier();
        loginPage.clearPassword();

        loginPage.login(
                TestCredentials.customerEmail(),
                TestCredentials.customerPassword()
        );


        MfaPage mfaPage =
                new MfaPage(
                        getDriver()
                );

        Assert.assertTrue(
                mfaPage.isLoaded(),
                "MFA page should be displayed after valid credentials."
        );

        mfaPage.verify(
                TestCredentials.mfaCode()
        );


        dashboardPage =
                new DashboardPage(
                        getDriver()
                );

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Customer dashboard should load after MFA verification."
        );
    }
}
