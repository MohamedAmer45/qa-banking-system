package com.bankingsystem.qa.bdd.context;

import com.bankingsystem.qa.bdd.pages.BankingPage;
import com.bankingsystem.qa.bdd.pages.LoginPage;

import org.openqa.selenium.WebDriver;

/**
 * Scenario-scoped state, injected into step classes by picocontainer. Page
 * objects are created lazily so a scenario only builds what it uses.
 */
public final class TestContext {

    private WebDriver driver;
    private LoginPage loginPage;
    private BankingPage bankingPage;

    /** Balance captured before a money movement, for delta assertions. */
    private Long balanceBefore;

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

    public LoginPage loginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(getDriver());
        }
        return loginPage;
    }

    public BankingPage bankingPage() {
        if (bankingPage == null) {
            bankingPage = new BankingPage(getDriver());
        }
        return bankingPage;
    }

    public void rememberBalance(long minor) {
        this.balanceBefore = minor;
    }

    public long rememberedBalance() {
        if (balanceBefore == null) {
            throw new IllegalStateException(
                    "No balance was captured earlier in this scenario."
            );
        }
        return balanceBefore;
    }

    public void clear() {
        driver = null;
        loginPage = null;
        bankingPage = null;
        balanceBefore = null;
    }
}
