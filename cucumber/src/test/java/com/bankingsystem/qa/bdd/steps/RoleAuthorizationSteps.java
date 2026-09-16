package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.Locale;

public class RoleAuthorizationSteps {

    private final TestContext testContext;
    private LoginPage loginPage;

    public RoleAuthorizationSteps(TestContext testContext) {

        this.testContext = testContext;
    }

    private LoginPage requireLoginPage() {

        Assert.assertNotNull(
                loginPage,
                "A role session must be started before checking authorization."
        );

        return loginPage;
    }

    private WebElement getAdminNavigation() {

        return testContext.getDriver()
                .findElement(
                        By.id("adminNav")
                );
    }

    private void openSessionPage() {

        loginPage =
                new LoginPage(
                        testContext.getDriver()
                );

        loginPage.open();

        Assert.assertTrue(
                loginPage.isLoaded(),
                "NovaBank session page should load."
        );
    }

    @Given("a customer session is active for authorization testing")
    public void aCustomerSessionIsActiveForAuthorizationTesting() {

        openSessionPage();

        loginPage.enterAsCustomer();

        Assert.assertTrue(
                loginPage.isApplicationDisplayed(),
                "NovaBank application should load for the customer."
        );

        Assert.assertTrue(
                loginPage.getLoggedInUserText()
                        .toLowerCase(Locale.ROOT)
                        .contains("customer"),
                "Authenticated session should use the customer role."
        );
    }

    @Given("an administrator session is active for authorization testing")
    public void anAdministratorSessionIsActiveForAuthorizationTesting() {

        openSessionPage();

        loginPage.enterAsAdmin();

        Assert.assertTrue(
                loginPage.isApplicationDisplayed(),
                "NovaBank application should load for the administrator."
        );

        Assert.assertTrue(
                loginPage.getLoggedInUserText()
                        .toLowerCase(Locale.ROOT)
                        .contains("admin"),
                "Authenticated session should use the admin role."
        );
    }

    @Then("the Admin navigation should not be visible")
    public void theAdminNavigationShouldNotBeVisible() {

        Assert.assertFalse(
                requireLoginPage()
                        .isAdminNavigationDisplayed(),
                "Customer must not see the Admin navigation option."
        );
    }

    @Then("the Admin navigation should have the hidden CSS class")
    public void theAdminNavigationShouldHaveTheHiddenCssClass() {

        WebElement adminNavigation =
                getAdminNavigation();

        Assert.assertFalse(
                adminNavigation.isDisplayed(),
                "Admin navigation should remain hidden for the customer role."
        );

        String classes =
                adminNavigation.getAttribute(
                        "class"
                );

        Assert.assertTrue(
                classes != null &&
                classes.contains("hidden"),
                "Admin navigation should have the hidden CSS class for customers."
        );
    }

    @Then("the Admin navigation should be visible")
    public void theAdminNavigationShouldBeVisible() {

        Assert.assertTrue(
                requireLoginPage()
                        .isAdminNavigationDisplayed(),
                "Administrator should see the Admin navigation option."
        );

        Assert.assertTrue(
                getAdminNavigation().isDisplayed(),
                "Admin navigation should be visible."
        );
    }

    @Then("the Admin navigation should not have the hidden CSS class")
    public void theAdminNavigationShouldNotHaveTheHiddenCssClass() {

        String classes =
                getAdminNavigation()
                        .getAttribute("class");

        Assert.assertTrue(
                classes == null ||
                !classes.contains("hidden"),
                "Admin navigation should not have the hidden class for admin sessions."
        );
    }

    @Then("the authenticated session should identify the {string} role")
    public void theAuthenticatedSessionShouldIdentifyTheRole(
            String expectedRole
    ) {

        String displayedUser =
                requireLoginPage()
                        .getLoggedInUserText()
                        .toLowerCase(Locale.ROOT);

        Assert.assertTrue(
                displayedUser.contains(
                        expectedRole.toLowerCase(Locale.ROOT)
                ),
                "Authenticated session should identify the " +
                        expectedRole +
                        " role."
        );
    }

    @When("the customer logs out and starts an administrator session")
    public void theCustomerLogsOutAndStartsAnAdministratorSession() {

        LoginPage page =
                requireLoginPage();

        page.logout();

        Assert.assertTrue(
                page.isLoaded(),
                "Session selection page should appear after logout."
        );

        page.enterAsAdmin();

        Assert.assertTrue(
                page.isApplicationDisplayed(),
                "NovaBank application should load for the administrator."
        );
    }
}
