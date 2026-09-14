package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.LoginPage;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SessionSecurityTest extends BaseTest {

    @Test(
            groups = {"regression", "security"},
            description = "Logging out clears the active demo session"
    )
    public void logoutShouldClearSession() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        loginPage.open();
        loginPage.enterAsCustomer();

        JavascriptExecutor js =
                (JavascriptExecutor) getDriver();

        Object tokenBeforeLogout =
                js.executeScript(
                        "return sessionStorage.getItem('nb_token');"
                );

        Assert.assertNotNull(
                tokenBeforeLogout,
                "Session token should exist after authentication."
        );

        loginPage.logout();

        Object tokenAfterLogout =
                js.executeScript(
                        "return sessionStorage.getItem('nb_token');"
                );

        Assert.assertNull(
                tokenAfterLogout,
                "Session token should be removed after logout."
        );
    }

    @Test(
            groups = {"regression", "security"},
            description = "Removing session storage prevents continued authenticated access"
    )
    public void clearingSessionStorageShouldEndSession() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        loginPage.open();
        loginPage.enterAsCustomer();

        JavascriptExecutor js =
                (JavascriptExecutor) getDriver();

        js.executeScript(
                "sessionStorage.clear();"
        );

        getDriver().navigate().refresh();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "Application should return to session selection page when session storage is cleared."
        );
    }

    @Test(
            groups = {"regression", "security"},
            description = "Customer role cannot access admin navigation"
    )
    public void customerShouldNotSeeAdminNavigation() {

        LoginPage loginPage =
                new LoginPage(getDriver());

        loginPage.open();
        loginPage.enterAsCustomer();

        Assert.assertFalse(
                loginPage.isAdminNavigationDisplayed(),
                "Admin navigation must not be visible to customer sessions."
        );
    }
}
