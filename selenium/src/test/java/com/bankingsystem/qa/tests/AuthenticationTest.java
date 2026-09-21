package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.utils.TestCredentials;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class AuthenticationTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void openApplication() {
        loginPage = new LoginPage(getDriver()).open();
    }

    @Test(description = "The sign-in form is served to an unauthenticated visitor")
    public void loginFormIsDisplayed() {
        assertTrue(loginPage.isLoginFormDisplayed());
    }

    @Test(description = "Valid credentials raise an MFA challenge rather than a session")
    public void credentialsRaiseMfaChallenge() {
        loginPage.submitCredentials(
                TestCredentials.CUSTOMER.email(),
                TestCredentials.CUSTOMER.password()
        );

        assertTrue(loginPage.isMfaChallengeDisplayed(),
                "credentials alone should produce a challenge");

        assertNull(loginPage.storedToken(),
                "no session may exist until the challenge is answered");
    }

    @Test(description = "Completing MFA establishes the session")
    public void mfaCompletesSignIn() {
        loginPage.loginAsCustomer();

        assertTrue(loginPage.isAuthenticated());
        assertEquals(loginPage.signedInRole(), "CUSTOMER");
        assertNotNull(loginPage.storedToken());
    }

    @Test(description = "An invalid password is rejected")
    public void invalidPasswordIsRejected() {
        loginPage.submitCredentials(
                TestCredentials.CUSTOMER.email(),
                "WrongPassword123!"
        );

        assertTrue(loginPage.toastText().toLowerCase().contains("invalid"));
        assertFalse(loginPage.isAuthenticated());
    }

    @Test(description = "An incorrect one-time code is rejected")
    public void invalidMfaCodeIsRejected() {
        loginPage.submitCredentials(
                TestCredentials.CUSTOMER.email(),
                TestCredentials.CUSTOMER.password()
        );

        loginPage.submitMfa("000000");

        String toast = loginPage.toastText();

        assertTrue(toast.toLowerCase().contains("invalid"),
                "expected the rejection to be reported, saw: " + toast);

        assertNull(loginPage.storedToken(),
                "a rejected code must not establish a session");
    }

    @Test(description = "Signing out clears the session")
    public void logoutClearsSession() {
        loginPage.loginAsCustomer();
        loginPage.logout();

        assertTrue(loginPage.isLoginFormDisplayed());
        assertNull(loginPage.storedToken());
    }

    @Test(description = "Staff sign in with their own role")
    public void staffSignInWithTheirRole() {
        loginPage.loginAs(TestCredentials.ADMIN);
        assertEquals(loginPage.signedInRole(), "ADMIN");
    }
}
