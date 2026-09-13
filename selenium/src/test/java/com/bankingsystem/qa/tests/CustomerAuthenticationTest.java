package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.DashboardPage;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.pages.MfaPage;
import com.bankingsystem.qa.utils.TestCredentials;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CustomerAuthenticationTest extends BaseTest {

    @Test
    public void customerShouldLoginWithMfaSuccessfully() {

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
                "MFA verification page should be displayed after valid login."
        );


        mfaPage.verify(
                TestCredentials.mfaCode()
        );


        DashboardPage dashboardPage =
                new DashboardPage(
                        getDriver()
                );

        Assert.assertTrue(
                dashboardPage.isLoaded(),
                "Customer dashboard should load after successful MFA verification."
        );

        Assert.assertEquals(
                dashboardPage.getPageTitleText(),
                "Overview",
                "Customer should land on the Overview dashboard."
        );
    }
}
