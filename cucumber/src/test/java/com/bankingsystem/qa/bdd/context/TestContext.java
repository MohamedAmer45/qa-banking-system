package com.bankingsystem.qa.bdd.context;

import com.bankingsystem.qa.bdd.pages.AccountsCurrentPage;
import com.bankingsystem.qa.bdd.pages.DashboardCurrentPage;
import com.bankingsystem.qa.bdd.pages.LoginPage;

import org.openqa.selenium.WebDriver;

public final class TestContext {

    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardCurrentPage dashboardPage;
    private AccountsCurrentPage accountsPage;

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {

        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not available in the current scenario."
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
                    "LoginPage is not available in the current scenario."
            );
        }

        return loginPage;
    }

    public void setDashboardPage(
            DashboardCurrentPage dashboardPage
    ) {
        this.dashboardPage = dashboardPage;
    }

    public DashboardCurrentPage getDashboardPage() {

        if (dashboardPage == null) {
            throw new IllegalStateException(
                    "Dashboard page is not available in the current scenario."
            );
        }

        return dashboardPage;
    }

    public void setAccountsPage(
            AccountsCurrentPage accountsPage
    ) {
        this.accountsPage = accountsPage;
    }

    public AccountsCurrentPage getAccountsPage() {

        if (accountsPage == null) {
            throw new IllegalStateException(
                    "Accounts page is not available in the current scenario."
            );
        }

        return accountsPage;
    }

    public void clear() {
        driver = null;
        loginPage = null;
        dashboardPage = null;
        accountsPage = null;
    }
}