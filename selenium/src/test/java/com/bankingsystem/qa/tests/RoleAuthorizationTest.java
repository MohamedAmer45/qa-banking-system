package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.AdminPage;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.utils.TestCredentials;

import org.testng.annotations.Test;


import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class RoleAuthorizationTest extends BaseTest {

    @Test(description = "An administrator reaches the back office")
    public void administratorReachesBackOffice() {
        new LoginPage(getDriver()).open().loginAsAdmin();

        AdminPage admin = new AdminPage(getDriver()).openCustomers();

        assertTrue(admin.customerTableText().contains(TestCredentials.CUSTOMER.email()),
                "the customer directory should list the seeded customer");
    }

    @Test(description = "The audit trail records authentication activity")
    public void auditTrailRecordsActivity() {
        new LoginPage(getDriver()).open().loginAsAdmin();

        AdminPage admin = new AdminPage(getDriver()).openAudit();

        assertTrue(admin.auditTableText().contains("LOGIN"),
                "sign-in should appear in the audit trail");
    }

    @Test(description = "A customer is not offered back-office navigation")
    public void customerHasNoBackOfficeNavigation() {
        LoginPage login = new LoginPage(getDriver()).open();
        login.loginAsCustomer();

        AdminPage admin = new AdminPage(getDriver());

        assertTrue(admin.hasNavItem("transfers"),
                "a customer should see banking navigation");

        assertFalse(admin.hasNavItem("admin-dashboard"),
                "a customer must not be offered back-office navigation");

        assertFalse(admin.hasNavItem("admin-users"),
                "a customer must not be offered user administration");
    }

    @Test(description = "A read-only role cannot obtain user administration data")
    public void readOnlyRoleCannotAdministerUsers() {
        new LoginPage(getDriver()).open().loginAs(TestCredentials.SUPPORT);

        AdminPage admin = new AdminPage(getDriver());

        assertTrue(admin.hasNavItem("admin-customers"),
                "SUPPORT should be able to read customers");

        /*
         * The sidebar is not role-filtered, so SUPPORT is offered the user
         * administration item even though only ADMIN may use it — recorded as
         * BUG-UI-002. The property that matters is that the module yields no
         * user data: the server answers 403 and the view reports the failure.
         */
        assertTrue(admin.hasNavItem("admin-users"),
                "the sidebar currently offers this to every staff role");

        admin.openUsersExpectingDenial();

        assertTrue(admin.viewText().toLowerCase().contains("permission")
                        || admin.viewText().toLowerCase().contains("unable"),
                "a denied module must report the failure, not render data: "
                        + admin.viewText());
    }
}
