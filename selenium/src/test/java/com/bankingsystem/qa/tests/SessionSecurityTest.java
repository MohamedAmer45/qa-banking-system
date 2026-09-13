package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;

import com.bankingsystem.qa.pages.DashboardPage;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.pages.MfaPage;

import com.bankingsystem.qa.utils.TestCredentials;

import org.openqa.selenium.JavascriptExecutor;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SessionSecurityTest extends BaseTest {

    private DashboardPage dashboardPage;

    @BeforeMethod
    public void loginCustomer() {

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
                "MFA page should be displayed."
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
                "Dashboard should load before session tests execute."
        );
    }


    @Test
    public void authenticationTokenShouldExistAfterLogin() {

        JavascriptExecutor js =
                (JavascriptExecutor) getDriver();

        Object token =
                js.executeScript(
                        "return window.localStorage.getItem('novabank_token');"
                );

        Assert.assertNotNull(
                token,
                "Authentication token should exist after successful login."
        );

        Assert.assertFalse(
                token.toString().isBlank(),
                "Authentication token should not be empty."
        );
    }


    @Test
    public void logoutShouldClearSessionAndReturnToLogin() {

        Assert.assertTrue(
                dashboardPage.isLogoutButtonDisplayed(),
                "Sign out button should be displayed."
        );

        dashboardPage.logout();


        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        Assert.assertTrue(
                loginPage.isLoaded(),
                "Login page should be displayed after logout."
        );


        JavascriptExecutor js =
                (JavascriptExecutor) getDriver();

        Object token =
                js.executeScript(
                        "return window.localStorage.getItem('novabank_token');"
                );

        Assert.assertNull(
                token,
                "Authentication token should be removed after logout."
        );
    }
}
