package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BackOfficeSteps {

    private final TestContext context;

    public BackOfficeSteps(TestContext context) {
        this.context = context;
    }

    @When("the customer directory is opened")
    public void theCustomerDirectoryIsOpened() {
        context.bankingPage().openBackOffice("admin-customers");
    }

    @When("the audit trail is opened")
    public void theAuditTrailIsOpened() {
        context.bankingPage().openBackOffice("admin-audit");
    }

    @When("user administration is opened directly")
    public void userAdministrationIsOpenedDirectly() {
        context.bankingPage().openBackOfficeDirectly("admin-users");
    }

    @Then("the {string} module is not offered in the sidebar")
    public void theModuleIsNotOffered(String view) {
        assertFalse(context.bankingPage().hasNavItem(view),
                view + " must not be offered to a role the server refuses it");
    }

    @Then("the {string} module is offered in the sidebar")
    public void theModuleIsOffered(String view) {
        assertTrue(context.bankingPage().hasNavItem(view),
                view + " should be offered to this role");
    }

    @Then("the directory lists {string}")
    public void theDirectoryLists(String email) {
        assertTrue(context.bankingPage().customerDirectoryText().contains(email));
    }

    @Then("the audit trail records a sign-in")
    public void theAuditTrailRecordsASignIn() {
        assertTrue(context.bankingPage().auditTrailText().contains("LOGIN"));
    }

    @Then("no user data is shown")
    public void noUserDataIsShown() {
        String view = context.bankingPage().viewText().toLowerCase();

        assertTrue(view.contains("permission") || view.contains("unable"),
                "a denied module must report the failure rather than render data: " + view);
    }

    @Then("banking navigation is available")
    public void bankingNavigationIsAvailable() {
        assertTrue(context.bankingPage().hasNavItem("transfers"));
    }

    @Then("back-office navigation is not available")
    public void backOfficeNavigationIsNotAvailable() {
        assertFalse(context.bankingPage().hasNavItem("admin-dashboard"));
        assertFalse(context.bankingPage().hasNavItem("admin-users"));
    }
}
