package com.bankingsystem.qa.bdd.pages;

import com.bankingsystem.qa.bdd.config.ConfigReader;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class LoginPage extends BasePage {

    public static final String MFA_CODE = "123456";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(ConfigReader.get("base.url"));
        find("login-form");
    }

    public boolean isLoginFormDisplayed() {
        return isPresent("login-form");
    }

    /** Credentials only; the session stays on the MFA challenge. */
    public void submitCredentials(String email, String password) {
        typeInto("login-email", email);
        typeInto("login-password", password);

        clickUntilSettled("login-submit",
                "performance.getEntriesByType('resource')"
                        + ".some(r => r.name.includes('/api/auth/login'))");

        wait.waitForJavaScriptCondition(
                "return !!document.querySelector(\"[data-testid='mfa-form']\")"
                        + " || !!document.querySelector(\"[data-testid='toast']\");"
        );
    }

    public boolean isMfaChallengeDisplayed() {
        return isPresent("mfa-form");
    }

    /**
     * Answer the challenge.
     *
     * A successful credential step raises its own "MFA required" toast, still
     * on screen at this point. The toast container replaces its contents on
     * every message, so waiting for the previous element to go stale
     * guarantees the next read is the verification result and not the stale
     * one.
     */
    public void submitMfa(String code) {
        Optional<WebElement> staleToast =
                driver.findElements(testId("toast")).stream().findFirst();

        typeInto("mfa-code", code);

        clickUntilSettled("mfa-submit",
                "performance.getEntriesByType('resource')"
                        + ".some(r => r.name.includes('/api/auth/mfa'))");

        staleToast.ifPresent(wait::waitForStaleness);

        wait.waitForJavaScriptCondition(
                "return !!document.querySelector(\"[data-testid='user-chip']\")"
                        + " || !!document.querySelector(\"[data-testid='toast']\");"
        );
    }

    public void loginAs(String email, String password) {
        submitCredentials(email, password);
        submitMfa(MFA_CODE);
        wait.waitForVisible(testId("user-chip"));
        waitForViewReady();
    }

    public boolean isAuthenticated() {
        return isPresent("user-chip");
    }

    public String signedInRole() {
        return find("user-role").getText();
    }

    public void logout() {
        clickable("logout").click();
        wait.waitForVisible(testId("login-form"));
    }

    public Object storedToken() {
        return ((JavascriptExecutor) driver)
                .executeScript("return localStorage.getItem('novabank_token');");
    }
}
