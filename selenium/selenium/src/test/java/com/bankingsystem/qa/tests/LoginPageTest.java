package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.utils.ConfigReader;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {

    @Test
    public void loginPageShouldLoadSuccessfully() {

        String baseUrl =
                ConfigReader.get("base.url");

        if (baseUrl.contains(
                "REPLACE_WITH_DEPLOYED_BANKING_URL"
        )) {

            throw new SkipException(
                    "Banking application base URL has not been configured yet."
            );
        }

        LoginPage loginPage =
                new LoginPage(
                        getDriver()
                );

        loginPage.open();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "Login page should display the identifier field, password field, and login button."
        );
    }
}
