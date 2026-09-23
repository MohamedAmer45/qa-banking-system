package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.BaseTest;
import com.bankingsystem.qa.pages.AdminPage;
import com.bankingsystem.qa.pages.LoginPage;
import com.bankingsystem.qa.utils.TestCredentials;

import org.testng.annotations.DataProvider;
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
         * The sidebar is filtered on the permissions /api/me reports, so this
         * entry is no longer offered (BUG-UI-002, fixed). Hiding it is not the
         * authorization, though, and this test does not treat it as such: the
         * property asserted below is that the module still yields no user data
         * when opened directly.
         */
        assertFalse(admin.hasNavItem("admin-users"),
                "SUPPORT must not be offered user administration");

        admin.openUsersExpectingDenial();

        assertTrue(admin.viewText().toLowerCase().contains("permission")
                        || admin.viewText().toLowerCase().contains("unable"),
                "a denied module must report the failure, not render data: "
                        + admin.viewText());
    }

    /*
     * BUG-UI-002 regression. The defect was that every staff role received the
     * full nine-item sidebar, so four of the five were offered modules the
     * server refuses. Driving it by role rather than writing one test per role
     * means a new role added to the table is a row here, not a forgotten case.
     */
    @DataProvider(name = "staffNavigation")
    public Object[][] staffNavigation() {
        return new Object[][]{
                //        role                        offered              withheld
                {TestCredentials.SUPPORT,  new String[]{"admin-customers", "admin-accounts", "admin-transfers"},
                                           new String[]{"admin-fraud", "admin-audit", "admin-users"}},
                {TestCredentials.EMPLOYEE, new String[]{"admin-customers", "admin-kyc", "admin-accounts"},
                                           new String[]{"admin-fraud", "admin-audit", "admin-users"}},
                {TestCredentials.AUDITOR,  new String[]{"admin-audit", "admin-fraud", "admin-customers"},
                                           new String[]{"admin-users"}},
                {TestCredentials.MANAGER,  new String[]{"admin-audit", "admin-fraud", "admin-loans"},
                                           new String[]{"admin-users"}},
                {TestCredentials.ADMIN,    new String[]{"admin-users", "admin-audit", "admin-fraud"},
                                           new String[]{}}
        };
    }

    @Test(dataProvider = "staffNavigation",
          description = "The sidebar offers a staff role only the modules it may open")
    public void sidebarIsFilteredByRole(TestCredentials.User user,
                                        String[] offered,
                                        String[] withheld) {
        new LoginPage(getDriver()).open().loginAs(user);

        AdminPage admin = new AdminPage(getDriver());

        for (String view : offered) {
            assertTrue(admin.hasNavItem(view),
                    user.role() + " should be offered " + view);
        }

        for (String view : withheld) {
            assertFalse(admin.hasNavItem(view),
                    user.role() + " must not be offered " + view
                            + ", which the server refuses it (BUG-UI-002)");
        }
    }

    @Test(description = "A withheld module is still refused when opened directly")
    public void withheldModuleIsRefusedWhenOpenedDirectly() {
        /*
         * The sidebar filter is a convenience. This asserts the boundary it
         * sits in front of: an AUDITOR that reaches user administration
         * without the nav entry still gets nothing.
         */
        new LoginPage(getDriver()).open().loginAs(TestCredentials.AUDITOR);

        AdminPage admin = new AdminPage(getDriver());

        assertFalse(admin.hasNavItem("admin-users"),
                "AUDITOR should not be offered user administration");

        admin.openUsersExpectingDenial();

        assertFalse(admin.viewText().contains(TestCredentials.CUSTOMER.email()),
                "user data must not render for a role without the permission");
    }
}
