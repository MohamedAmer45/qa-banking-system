package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.LoginPage;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.testng.Assert;

import java.util.Locale;

public final class AuthenticationSteps {

    private final TestContext context;

    public AuthenticationSteps(TestContext context) {

        this.context = context;
    }

    @When("the user starts the {string} demo session")
    public void startDemoSession(String role) {

        LoginPage loginPage = context.getLoginPage();

        switch (normalize(role)) {
            case "customer" -> loginPage.enterAsCustomer();
            case "admin" -> loginPage.enterAsAdmin();
            default -> throw new IllegalArgumentException(
                    "Unsupported demo role: " + role
            );
        }
    }

    @Then("the NovaBank application should open for the {string} role")
    public void applicationShouldOpenForRole(String role) {

        LoginPage loginPage = context.getLoginPage();

        Assert.assertTrue(
                loginPage.isApplicationDisplayed(),
                "NovaBank application should be displayed."
        );

        Assert.assertTrue(
                loginPage.getLoggedInUserText()
                        .toLowerCase(Locale.ROOT)
                        .contains(normalize(role)),
                "Authenticated session should use the " +
                role + " role."
        );
    }

    @Then("the admin navigation should be {string}")
    public void adminNavigationShouldBe(String expectedState) {

        boolean actuallyDisplayed =
                context.getLoginPage()
                        .isAdminNavigationDisplayed();

        switch (normalize(expectedState)) {
            case "visible" ->
                    Assert.assertTrue(
                            actuallyDisplayed,
                            "Admin navigation should be visible."
                    );

            case "hidden" ->
                    Assert.assertFalse(
                            actuallyDisplayed,
                            "Admin navigation should be hidden."
                    );

            default -> throw new IllegalArgumentException(
                    "Unsupported navigation state: " +
                    expectedState
            );
        }
    }

    @When("the user logs out")
    public void userLogsOut() {

        context.getLoginPage().logout();
    }

    @Then("the NovaBank entry page should be displayed again")
    public void entryPageShouldBeDisplayedAgain() {

        LoginPage loginPage = context.getLoginPage();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "NovaBank entry page should be displayed after logout."
        );

        Assert.assertFalse(
                loginPage.isApplicationDisplayed(),
                "Authenticated application should be hidden after logout."
        );
    }

    private String normalize(String value) {

        return value.trim()
                .toLowerCase(Locale.ROOT);
    }
}