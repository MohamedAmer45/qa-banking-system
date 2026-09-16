package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.ProfileCurrentPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

import java.util.Locale;

public class ProfileSteps {

    private final TestContext testContext;
    private ProfileCurrentPage profilePage;

    public ProfileSteps(TestContext testContext) {

        this.testContext = testContext;
    }

    private ProfileCurrentPage requireProfilePage() {

        Assert.assertNotNull(
                profilePage,
                "The Profile page must be opened before using it."
        );

        return profilePage;
    }

    @Given("the customer opens the Profile page")
    public void theCustomerOpensTheProfilePage() {

        profilePage =
                new ProfileCurrentPage(
                        testContext.getDriver()
                );

        profilePage.open();

        Assert.assertTrue(
                profilePage.isLoaded(),
                "Profile page should load successfully."
        );
    }

    @Then("the Profile page should be displayed")
    public void theProfilePageShouldBeDisplayed() {

        ProfileCurrentPage page =
                requireProfilePage();

        Assert.assertTrue(
                page.isLoaded(),
                "Profile page should be visible."
        );

        Assert.assertEquals(
                page.getPageTitleText(),
                "Profile",
                "Profile heading should be displayed."
        );
    }

    @Then("the customer profile identity fields should be populated")
    public void theCustomerProfileIdentityFieldsShouldBePopulated() {

        ProfileCurrentPage page =
                requireProfilePage();

        Assert.assertFalse(
                page.getCustomerName().isBlank(),
                "Customer name should not be blank."
        );

        Assert.assertFalse(
                page.getEmail().isBlank(),
                "Customer email should not be blank."
        );

        Assert.assertFalse(
                page.getRole().isBlank(),
                "Customer role should not be blank."
        );
    }

    @Then("the displayed profile email should have a valid basic format")
    public void theDisplayedProfileEmailShouldHaveAValidBasicFormat() {

        String email =
                requireProfilePage().getEmail();

        Assert.assertFalse(
                email.isBlank(),
                "Customer email should not be blank."
        );

        Assert.assertTrue(
                email.contains("@"),
                "Displayed customer email should contain @."
        );

        Assert.assertTrue(
                email.contains("."),
                "Displayed customer email should contain a domain."
        );
    }

    @Then("the profile role should identify the user as a customer")
    public void theProfileRoleShouldIdentifyTheUserAsACustomer() {

        String role =
                requireProfilePage()
                        .getRole()
                        .toLowerCase(Locale.ROOT);

        Assert.assertTrue(
                role.contains("customer"),
                "Profile role should identify the authenticated user as customer."
        );

        Assert.assertFalse(
                role.contains("admin"),
                "Customer profile must not be identified as admin."
        );
    }
}
