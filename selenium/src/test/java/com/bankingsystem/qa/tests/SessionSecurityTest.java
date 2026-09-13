package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.LoginPage;

import org.openqa.selenium.JavascriptExecutor;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SessionSecurityTest extends CustomerTestBase {

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
