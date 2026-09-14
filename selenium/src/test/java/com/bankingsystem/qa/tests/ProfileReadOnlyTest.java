package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.ProfileCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProfileReadOnlyTest extends CustomerTestBase {

    private ProfileCurrentPage openProfilePage() {

        dashboardPage.openProfile();

        ProfileCurrentPage profilePage =
                new ProfileCurrentPage(
                        getDriver()
                );

        Assert.assertTrue(
                profilePage.isLoaded(),
                "Profile page should load successfully."
        );

        return profilePage;
    }

    @Test(
            groups = {"smoke", "profile"},
            description = "Profile page loads successfully"
    )
    public void profilePageShouldLoadSuccessfully() {

        ProfileCurrentPage profilePage =
                openProfilePage();

        Assert.assertEquals(
                profilePage.getPageTitleText(),
                "Profile",
                "Profile heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "profile"},
            description = "Customer name is displayed"
    )
    public void customerNameShouldBeDisplayed() {

        ProfileCurrentPage profilePage =
                openProfilePage();

        Assert.assertFalse(
                profilePage.getCustomerName().isBlank(),
                "Customer name should not be blank."
        );
    }

    @Test(
            groups = {"regression", "profile"},
            description = "Customer email is displayed"
    )
    public void customerEmailShouldBeDisplayed() {

        ProfileCurrentPage profilePage =
                openProfilePage();

        String email =
                profilePage.getEmail();

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

    @Test(
            groups = {"regression", "profile", "authorization"},
            description = "Customer role is displayed correctly"
    )
    public void customerRoleShouldBeDisplayed() {

        ProfileCurrentPage profilePage =
                openProfilePage();

        String role =
                profilePage.getRole();

        Assert.assertTrue(
                role.toLowerCase()
                        .contains("customer"),
                "Profile role should identify the authenticated user as customer."
        );

        Assert.assertFalse(
                role.toLowerCase()
                        .contains("admin"),
                "Customer profile must not be identified as admin."
        );
    }

    @Test(
            groups = {"regression", "profile"},
            description = "Profile contains all expected customer identity fields"
    )
    public void profileShouldContainExpectedIdentityFields() {

        ProfileCurrentPage profilePage =
                openProfilePage();

        Assert.assertFalse(
                profilePage.getCustomerName().isBlank(),
                "Name should be displayed."
        );

        Assert.assertFalse(
                profilePage.getEmail().isBlank(),
                "Email should be displayed."
        );

        Assert.assertFalse(
                profilePage.getRole().isBlank(),
                "Role should be displayed."
        );
    }
}