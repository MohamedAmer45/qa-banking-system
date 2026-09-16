package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

import java.util.Locale;

public class SessionSecuritySteps {

    private static final String SESSION_TOKEN_KEY =
            "nb_token";

    private final TestContext testContext;
    private LoginPage loginPage;

    public SessionSecuritySteps(TestContext testContext) {

        this.testContext = testContext;
    }

    private LoginPage requireLoginPage() {

        Assert.assertNotNull(
                loginPage,
                "A customer session must be started before testing session security."
        );

        return loginPage;
    }

    private JavascriptExecutor getJavascriptExecutor() {

        return (JavascriptExecutor) testContext.getDriver();
    }

    private Object getSessionToken() {

        return getJavascriptExecutor()
                .executeScript(
                        "return sessionStorage.getItem(arguments[0]);",
                        SESSION_TOKEN_KEY
                );
    }

    @Given("a customer session is active for security testing")
    public void aCustomerSessionIsActiveForSecurityTesting() {

        loginPage =
                new LoginPage(
                        testContext.getDriver()
                );

        loginPage.open();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "NovaBank session page should load."
        );

        loginPage.enterAsCustomer();

        Assert.assertTrue(
                loginPage.isApplicationDisplayed(),
                "NovaBank application should load for the customer."
        );

        Assert.assertTrue(
                loginPage.getLoggedInUserText()
                        .toLowerCase(Locale.ROOT)
                        .contains("customer"),
                "Authenticated session should identify the customer role."
        );
    }

    @Then("the active demo session token should exist")
    public void theActiveDemoSessionTokenShouldExist() {

        Assert.assertNotNull(
                getSessionToken(),
                "Session token should exist after authentication."
        );
    }

    @When("the customer logs out of the secure session")
    public void theCustomerLogsOutOfTheSecureSession() {

        requireLoginPage().logout();
    }

    @Then("the active demo session token should be removed")
    public void theActiveDemoSessionTokenShouldBeRemoved() {

        Assert.assertNull(
                getSessionToken(),
                "Session token should be removed."
        );
    }

    @When("the customer clears browser session storage and refreshes the page")
    public void theCustomerClearsBrowserSessionStorageAndRefreshesThePage() {

        getJavascriptExecutor()
                .executeScript(
                        "sessionStorage.clear();"
                );

        testContext.getDriver()
                .navigate()
                .refresh();
    }

    @Then("the session selection page should be displayed")
    public void theSessionSelectionPageShouldBeDisplayed() {

        Assert.assertTrue(
                requireLoginPage().isLoaded(),
                "Application should return to the session selection page."
        );
    }

    @Then("the customer session should not expose Admin navigation")
    public void theCustomerSessionShouldNotExposeAdminNavigation() {

        Assert.assertFalse(
                requireLoginPage()
                        .isAdminNavigationDisplayed(),
                "Admin navigation must not be visible to customer sessions."
        );
    }
}
