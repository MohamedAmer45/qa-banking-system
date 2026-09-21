package com.bankingsystem.qa.api.security;

import com.bankingsystem.qa.api.support.ApiTest;
import com.bankingsystem.qa.api.support.Users;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * SYS-004 / SEC-003: every role reaches exactly what its permissions allow.
 *
 * Driven as a matrix rather than one test per case. The interesting property
 * is the whole grid — a permission table is only correct if every cell is, and
 * a matrix makes an accidental widening visible as a single failing cell
 * instead of a missing test nobody wrote.
 */
public class AuthorizationMatrixTest extends ApiTest {

    /*
     * Permission model, from requirements/roles-and-permissions.md:
     *   CUSTOMER  own resources only
     *   SUPPORT   read customers, accounts, transfers
     *   AUDITOR   SUPPORT + audit and fraud
     *   EMPLOYEE  SUPPORT + KYC review
     *   MANAGER   the above + manage accounts/limits, reverse, review loans
     *   ADMIN     everything, including user administration
     */
    @DataProvider(name = "readMatrix")
    public Object[][] readMatrix() {
        return new Object[][]{
                // endpoint,                    user,             expected
                {"/api/admin/dashboard", Users.CUSTOMER, 403},
                {"/api/admin/dashboard", Users.SUPPORT, 200},
                {"/api/admin/dashboard", Users.AUDITOR, 200},
                {"/api/admin/dashboard", Users.MANAGER, 200},
                {"/api/admin/dashboard", Users.ADMIN, 200},

                {"/api/admin/customers", Users.CUSTOMER, 403},
                {"/api/admin/customers", Users.SUPPORT, 200},
                {"/api/admin/customers", Users.ADMIN, 200},

                {"/api/admin/accounts", Users.CUSTOMER, 403},
                {"/api/admin/accounts", Users.SUPPORT, 200},
                {"/api/admin/accounts", Users.ADMIN, 200},

                {"/api/admin/transfers", Users.CUSTOMER, 403},
                {"/api/admin/transfers", Users.SUPPORT, 200},
                {"/api/admin/transfers", Users.ADMIN, 200},

                // Audit and fraud are AUDITOR/MANAGER/ADMIN only.
                {"/api/admin/audit", Users.CUSTOMER, 403},
                {"/api/admin/audit", Users.SUPPORT, 403},
                {"/api/admin/audit", Users.EMPLOYEE, 403},
                {"/api/admin/audit", Users.AUDITOR, 200},
                {"/api/admin/audit", Users.MANAGER, 200},
                {"/api/admin/audit", Users.ADMIN, 200},

                {"/api/admin/fraud", Users.CUSTOMER, 403},
                {"/api/admin/fraud", Users.SUPPORT, 403},
                {"/api/admin/fraud", Users.AUDITOR, 200},
                {"/api/admin/fraud", Users.ADMIN, 200},

                // User administration is ADMIN alone.
                {"/api/admin/users", Users.CUSTOMER, 403},
                {"/api/admin/users", Users.SUPPORT, 403},
                {"/api/admin/users", Users.AUDITOR, 403},
                {"/api/admin/users", Users.EMPLOYEE, 403},
                {"/api/admin/users", Users.MANAGER, 403},
                {"/api/admin/users", Users.ADMIN, 200},

                // Customer-facing endpoints stay open to customers.
                {"/api/accounts", Users.CUSTOMER, 200},
                {"/api/beneficiaries", Users.CUSTOMER, 200},
                {"/api/transfers", Users.CUSTOMER, 200},
                {"/api/notifications", Users.CUSTOMER, 200}
        };
    }

    @Test(dataProvider = "readMatrix",
            description = "SYS-004: read access matches the permission table")
    public void readAccessMatchesRole(String endpoint, Users.User user, int expected) {
        as(user)
                .get(endpoint)
                .then()
                .statusCode(expected);
    }

    @DataProvider(name = "writeMatrix")
    public Object[][] writeMatrix() {
        return new Object[][]{
                // Freezing an account requires MANAGE_ACCOUNTS.
                {"/api/admin/accounts/1/freeze", Users.CUSTOMER, 403},
                {"/api/admin/accounts/1/freeze", Users.SUPPORT, 403},
                {"/api/admin/accounts/1/freeze", Users.AUDITOR, 403},
                {"/api/admin/accounts/1/freeze", Users.EMPLOYEE, 403},

                // Changing a role requires ADMIN.
                {"/api/admin/users/1/role", Users.CUSTOMER, 403},
                {"/api/admin/users/1/role", Users.SUPPORT, 403},
                {"/api/admin/users/1/role", Users.MANAGER, 403}
        };
    }

    @Test(dataProvider = "writeMatrix",
            description = "SYS-004: write access matches the permission table")
    public void writeAccessMatchesRole(String endpoint, Users.User user, int expected) {
        as(user)
                .body("{}")
                .post(endpoint)
                .then()
                .statusCode(expected);
    }

    @DataProvider(name = "protectedEndpoints")
    public Object[][] protectedEndpoints() {
        return new Object[][]{
                {"/api/me"}, {"/api/accounts"}, {"/api/beneficiaries"},
                {"/api/transfers"}, {"/api/cards"}, {"/api/loans"},
                {"/api/notifications"}, {"/api/billers"}, {"/api/kyc"},
                {"/api/admin/dashboard"}, {"/api/admin/audit"}
        };
    }

    @Test(dataProvider = "protectedEndpoints",
            description = "SEC-001: no protected endpoint answers without a session")
    public void protectedEndpointsRequireAuthentication(String endpoint) {
        anonymous()
                .get(endpoint)
                .then()
                .statusCode(401);
    }

    @Test(dataProvider = "protectedEndpoints",
            description = "SEC-001: a malformed token is refused, not ignored")
    public void malformedTokenIsRefused(String endpoint) {
        anonymous()
                .header("Authorization", "Bearer not-a-real-token")
                .get(endpoint)
                .then()
                .statusCode(401);
    }
}
