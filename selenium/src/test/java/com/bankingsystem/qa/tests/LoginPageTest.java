package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.utils.ConfigReader;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void openLoginPage() {

        loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();
    }

    @Test
    public void loginPageShouldLoadSuccessfully() {

        Assert.assertTrue(
                loginPage.isLoaded(),
                "Login form should be displayed."
        );
    }

    @Test
    public void passwordFieldShouldBeMasked() {

        Assert.assertEquals(
                loginPage.getPasswordFieldType(),
                "password",
                "Password field should use type=password."
        );
    }

    @Test
    public void shouldRejectInvalidCredentials() {

        String invalidUsername =
                ConfigReader.get(
                        "invalid.username"
                );

        String invalidPassword =
                ConfigReader.get(
                        "invalid.password"
                );

        loginPage.clearIdentifier();
        loginPage.clearPassword();

        loginPage.login(
                invalidUsername,
                invalidPassword
        );

        Assert.assertTrue(
                loginPage.isErrorMessageDisplayed(),
                "An error message should be displayed for invalid credentials."
        );

        Assert.assertTrue(
                loginPage.getErrorMessage()
                        .contains("Sign in failed"),
                "The login failure message should indicate that sign in failed."
        );
    }

    @Test
    public void shouldNotAuthenticateWithEmptyCredentials() {

        loginPage.clearIdentifier();
        loginPage.clearPassword();

        loginPage.clickLogin();

        Assert.assertTrue(
                loginPage.isIdentifierFieldDisplayed(),
                "User should remain on the login form."
        );

        Assert.assertTrue(
                loginPage.isPasswordFieldDisplayed(),
                "Password field should remain visible."
        );
    }

    @Test
    public void identifierFieldShouldAcceptInput() {

        String value =
                "test.user@novabank.test";

        loginPage.clearIdentifier();

        loginPage.enterIdentifier(
                value
        );

        Assert.assertEquals(
                loginPage.getIdentifierValue(),
                value,
                "Identifier field should contain the entered value."
        );
    }

    @Test
    public void passwordFieldShouldAcceptInput() {

        String value =
                "TestPassword123!";

        loginPage.clearPassword();

        loginPage.enterPassword(
                value
        );

        Assert.assertEquals(
                loginPage.getPasswordValue(),
                value,
                "Password field should contain the entered value."
        );
    }
}
