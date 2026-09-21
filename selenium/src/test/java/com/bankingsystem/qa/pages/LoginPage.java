package com.bankingsystem.qa.pages;

import com.bankingsystem.qa.utils.ConfigReader;
import com.bankingsystem.qa.utils.TestCredentials;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

/**
 * Sign-in is a two-step handshake: credentials raise an MFA challenge, and the
 * challenge is exchanged for a session. Both are always required — every
 * seeded user has MFA enabled.
 */
public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.get("base.url"));
        find("login-form");
        return this;
    }

    public boolean isLoginFormDisplayed() {
        return isPresent("login-form");
    }

    /**
     * Submit credentials only, leaving the session on the MFA challenge.
     *
     * Waits for the server to answer — either the challenge form appears, or a
     * toast reports the rejection — so callers never assert against the form
     * as it was before the request completed.
     */
    public LoginPage submitCredentials(String email, String password) {
        find("login-email").clear();
        find("login-email").sendKeys(email);

        find("login-password").clear();
        find("login-password").sendKeys(password);

        clickable("login-submit").click();
        waitForCredentialOutcome();
        return this;
    }

    private void waitForCredentialOutcome() {
        wait.waitForJavaScriptCondition(
                "return !!document.querySelector(\"[data-testid='mfa-form']\")"
                        + " || !!document.querySelector(\"[data-testid='toast']\");"
        );
    }

    public boolean isMfaChallengeDisplayed() {
        return isPresent("mfa-form");
    }

    /**
     * Answer the challenge and wait for the outcome: either the shell renders,
     * or a toast reports the rejected code.
     *
     * A successful credential step raises its own "MFA required" toast, which
     * is still on screen when this runs. Waiting for "a toast" would latch
     * onto that stale one and read it as the verification result. The toast
     * container replaces its contents on every message, so waiting for the
     * previous element to go stale guarantees the next read is the new one.
     */
    public LoginPage submitMfa(String code) {
        Optional<WebElement> staleToast =
                driver.findElements(testId("toast")).stream().findFirst();

        find("mfa-code").clear();
        find("mfa-code").sendKeys(code);

        clickable("mfa-submit").click();

        staleToast.ifPresent(wait::waitForStaleness);

        wait.waitForJavaScriptCondition(
                "return !!document.querySelector(\"[data-testid='user-chip']\")"
                        + " || !!document.querySelector(\"[data-testid='toast']\");"
        );

        return this;
    }

    /** The full handshake, through to an authenticated shell. */
    public LoginPage loginAs(TestCredentials.User user) {
        submitCredentials(user.email(), user.password());
        submitMfa(TestCredentials.MFA_CODE);

        wait.waitForVisible(testId("user-chip"));
        waitForViewReady();
        return this;
    }

    public LoginPage loginAsCustomer() {
        return loginAs(TestCredentials.CUSTOMER);
    }

    public LoginPage loginAsAdmin() {
        return loginAs(TestCredentials.ADMIN);
    }

    public String signedInRole() {
        return find("user-role").getText();
    }

    public boolean isAuthenticated() {
        return isPresent("user-chip");
    }

    public void logout() {
        clickable("logout").click();
        wait.waitForVisible(testId("login-form"));
    }

    /** The session token the application persists for reload survival. */
    public Object storedToken() {
        return ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return localStorage.getItem('novabank_token');");
    }
}
