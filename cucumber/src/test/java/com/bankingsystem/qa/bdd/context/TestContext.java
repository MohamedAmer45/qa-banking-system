package com.bankingsystem.qa.bdd.context;

import com.bankingsystem.qa.bdd.pages.LoginPage;

import org.openqa.selenium.WebDriver;

public final class TestContext {

    private WebDriver driver;
    private LoginPage loginPage;

    public void setDriver(WebDriver driver) {

        this.driver = driver;
    }

    public WebDriver getDriver() {

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not available in the current scenario context."
            );
        }

        return driver;
    }

    public boolean hasDriver() {

        return driver != null;
    }

    public void setLoginPage(LoginPage loginPage) {

        this.loginPage = loginPage;
    }

    public LoginPage getLoginPage() {

        if (loginPage == null) {
            throw new IllegalStateException(
                    "LoginPage is not available in the current scenario context."
            );
        }

        return loginPage;
    }

    public void clear() {

        driver = null;
        loginPage = null;
    }
}