package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.LoginPage;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class AuthenticationSteps {

    /** Seeded identities, addressed by role so scenarios stay readable. */
    private static final Map<String, String[]> USERS = Map.of(
            "customer", new String[]{"customer@novabank.test", "Demo123!", "CUSTOMER"},
            "receiver", new String[]{"receiver@novabank.test", "Demo123!", "CUSTOMER"},
            "administrator", new String[]{"admin@novabank.test", "Admin123!", "ADMIN"},
            "manager", new String[]{"manager@novabank.test", "Manager123!", "MANAGER"},
            "support agent", new String[]{"support@novabank.test", "Support123!", "SUPPORT"},
            "auditor", new String[]{"auditor@novabank.test", "Auditor123!", "AUDITOR"}
    );

    private final TestContext context;

    public AuthenticationSteps(TestContext context) {
        this.context = context;
    }

    private static String[] user(String role) {
        String[] user = USERS.get(role);

        if (user == null) {
            throw new IllegalArgumentException("Unknown seeded role: " + role);
        }

        return user;
    }

    @Given("the NovaBank sign-in page is open")
    public void theSignInPageIsOpen() {
        context.loginPage().open();
    }

    @Given("a signed-in {string}")
    public void aSignedIn(String role) {
        String[] user = user(role);
        context.loginPage().open();
        context.loginPage().loginAs(user[0], user[1]);
    }

    @When("the {string} submits valid credentials")
    public void submitsValidCredentials(String role) {
        String[] user = user(role);
        context.loginPage().submitCredentials(user[0], user[1]);
    }

    @When("the {string} submits an incorrect password")
    public void submitsIncorrectPassword(String role) {
        context.loginPage().submitCredentials(user(role)[0], "WrongPassword123!");
    }

    @When("the one-time code {string} is submitted")
    public void theOneTimeCodeIsSubmitted(String code) {
        context.loginPage().submitMfa(code);
    }

    @When("the correct one-time code is submitted")
    public void theCorrectOneTimeCodeIsSubmitted() {
        context.loginPage().submitMfa(LoginPage.MFA_CODE);
    }

    @When("the customer signs out")
    public void theCustomerSignsOut() {
        context.loginPage().logout();
    }

    @Then("a one-time code is requested")
    public void aOneTimeCodeIsRequested() {
        assertTrue(context.loginPage().isMfaChallengeDisplayed(),
                "credentials alone should raise an MFA challenge");
    }

    @Then("no session is established")
    public void noSessionIsEstablished() {
        assertNull(context.loginPage().storedToken(),
                "a session must not exist at this point");

        assertFalse(context.loginPage().isAuthenticated());
    }

    @Then("the session is established")
    public void theSessionIsEstablished() {
        assertTrue(context.loginPage().isAuthenticated());
        assertNotNull(context.loginPage().storedToken());
    }

    @Then("the signed-in role is {string}")
    public void theSignedInRoleIs(String role) {
        assertEquals(context.loginPage().signedInRole(), role);
    }

    @Then("the sign-in page is shown again")
    public void theSignInPageIsShownAgain() {
        assertTrue(context.loginPage().isLoginFormDisplayed());
    }

    @And("the failure is reported as {string}")
    public void theFailureIsReportedAs(String fragment) {
        String toast = context.loginPage().toastText();

        assertTrue(toast.toLowerCase().contains(fragment.toLowerCase()),
                "expected the message to mention " + fragment + ", saw: " + toast);
    }
}
