package com.bankingsystem.qa.pages;

import com.bankingsystem.qa.utils.ConfigReader;
import com.bankingsystem.qa.utils.TestCredentials;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
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
        typeInto("login-email", email);
        typeInto("login-password", password);

        clickUntilSettled("login-submit",
                "performance.getEntriesByType('resource')"
                        + ".some(r => r.name.includes('/api/auth/login'))");
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

        typeInto("mfa-code", code);

        /*
         * Settled once the page has actually asked the server to verify the
         * code. Anything weaker cannot tell "the click did nothing" apart from
         * "the server has not answered yet".
         */
        clickUntilSettled("mfa-submit",
                "performance.getEntriesByType('resource')"
                        + ".some(r => r.name.includes('/api/auth/mfa'))");

        staleToast.ifPresent(wait::waitForStaleness);

        try {
            wait.waitForJavaScriptCondition(
                    "return !!document.querySelector(\"[data-testid='user-chip']\")"
                            + " || !!document.querySelector(\"[data-testid='toast']\");"
            );
        } catch (TimeoutException e) {
            /*
             * A bare "expected condition failed" says nothing about which of
             * the two outcomes was missing, and sign-in sits under every test
             * in the suite. Name what the page was actually showing.
             */
            throw new TimeoutException(
                    "Sign-in did not resolve: after submitting the one-time code, neither the "
                            + "application shell nor a toast appeared within the wait. "
                            + "Page state: " + describeAuthState(), e);
        }

        return this;
    }

    /**
     * What the page is showing, for a sign-in failure message.
     *
     * Includes the API calls the page has actually made. Whether a request was
     * issued at all is the difference between "the server is slow" and "the
     * click did nothing", and without it the two look identical from a
     * timeout.
     */
    private String describeAuthState() {
        String requests = String.valueOf(((JavascriptExecutor) driver).executeScript(
                "return performance.getEntriesByType('resource')"
                        + ".filter(r => r.name.includes('/api/'))"
                        + ".map(r => r.name.split('/api/')[1])"
                        + ".join(', ') || 'none';"
        ));

        return "login form " + (isPresent("login-form") ? "present" : "absent")
                + ", MFA form " + (isPresent("mfa-form") ? "present" : "absent")
                + ", shell " + (isPresent("user-chip") ? "present" : "absent")
                + ", toast " + (isPresent("toast") ? "present" : "absent")
                + "; requests made: " + requests;
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
