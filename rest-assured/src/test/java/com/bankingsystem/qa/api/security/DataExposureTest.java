package com.bankingsystem.qa.api.security;

import com.bankingsystem.qa.api.support.ApiTest;
import com.bankingsystem.qa.api.support.Users;

import io.restassured.response.Response;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * SYS-005: sensitive information must not leak through responses, and
 * SEC-002: a customer must not reach another customer's resources.
 *
 * Both are invisible to the UI suites, which only see what the interface
 * chooses to render. A response can carry a password hash the page never
 * displays.
 */
public class DataExposureTest extends ApiTest {

    private static final String[] FORBIDDEN_FIELDS = {
            "password_hash", "password", "mfa_code", "stack", "stacktrace"
    };

    @DataProvider(name = "customerEndpoints")
    public Object[][] customerEndpoints() {
        return new Object[][]{
                {"/api/me"}, {"/api/accounts"}, {"/api/beneficiaries"},
                {"/api/transfers"}, {"/api/cards"}, {"/api/loans"},
                {"/api/notifications"}, {"/api/kyc"}
        };
    }

    @Test(dataProvider = "customerEndpoints",
            description = "SYS-005: no response body carries credential material")
    public void noCredentialMaterialInResponses(String endpoint) {
        String body = as(Users.CUSTOMER).get(endpoint).then().extract().asString();

        for (String field : FORBIDDEN_FIELDS) {
            assertFalse(body.contains("\"" + field + "\""),
                    "SYS-005: " + endpoint + " exposed '" + field + "'");
        }
    }

    @Test(description = "SYS-005: the admin user list carries no credential material")
    public void adminUserListCarriesNoCredentials() {
        String body = as(Users.ADMIN).get("/api/admin/users").then()
                .statusCode(200).extract().asString();

        for (String field : new String[]{"password_hash", "mfa_code"}) {
            assertFalse(body.contains("\"" + field + "\""),
                    "SYS-005: the user directory exposed '" + field + "', which "
                            + "would let an administrator harvest credentials");
        }
    }

    @Test(description = "SYS-005: a KYC profile masks the national identifier")
    public void kycIdentifierIsMasked() {
        String idNumber = as(Users.CUSTOMER).get("/api/kyc").then()
                .statusCode(200).extract().path("id_number");

        if (idNumber != null) {
            assertTrue(idNumber.startsWith("*"),
                    "SYS-005: the identity document number was returned in full: "
                            + idNumber);
        }
    }

    @Test(description = "SYS-003: a server error never returns an internal detail")
    public void errorsDoNotLeakInternals() {
        Response response = as(Users.CUSTOMER)
                .body("{\"fromAccountId\":\"not-a-number\",\"amount\":\"abc\"}")
                .post("/api/transfers");

        String body = response.asString().toLowerCase();

        assertTrue(response.statusCode() >= 400 && response.statusCode() < 500,
                "SYS-003: malformed input should be a client error, was "
                        + response.statusCode());

        for (String leak : new String[]{"select ", "insert into", "postgres",
                "at object.", "node_modules", "/src/"}) {

            assertFalse(body.contains(leak),
                    "SYS-003: the error response leaked '" + leak + "': " + body);
        }
    }

    // ------------------------------------------------------------- SEC-002

    @Test(description = "SEC-002: another customer's account is not readable")
    public void cannotReadAnotherCustomersAccount() {
        int theirAccount = firstActiveAccountId(Users.RECEIVER);

        // 404 rather than 403: the API must not confirm the resource exists.
        as(Users.CUSTOMER)
                .get("/api/accounts/" + theirAccount + "/transactions")
                .then()
                .statusCode(404);
    }

    @Test(description = "SEC-002: another customer's statement is not readable")
    public void cannotReadAnotherCustomersStatement() {
        int theirAccount = firstActiveAccountId(Users.RECEIVER);

        as(Users.CUSTOMER)
                .get("/api/accounts/" + theirAccount + "/statement")
                .then()
                .statusCode(404);
    }

    @Test(description = "SEC-002: a transfer cannot be sent from someone else's account")
    public void cannotTransferFromAnotherCustomersAccount() {
        int theirAccount = firstActiveAccountId(Users.RECEIVER);
        int myBeneficiary = firstVerifiedBeneficiaryId(Users.CUSTOMER);

        as(Users.CUSTOMER)
                .body("""
                        {"fromAccountId":%d,"beneficiaryId":%d,"amount":1,
                         "idempotencyKey":"%s"}"""
                        .formatted(theirAccount, myBeneficiary, uniqueKey("idor")))
                .post("/api/transfers")
                .then()
                .statusCode(404);
    }

    @Test(description = "SEC-002: a customer's own listing contains only their rows")
    public void listingsAreScopedToTheCaller() {
        int myUserId = as(Users.CUSTOMER).get("/api/me").then()
                .statusCode(200).extract().path("user.id");

        java.util.List<Integer> owners = as(Users.CUSTOMER)
                .get("/api/accounts")
                .then()
                .statusCode(200)
                .extract()
                .path("user_id");

        for (Integer owner : owners) {
            assertEquals(owner, Integer.valueOf(myUserId),
                    "SEC-002: the accounts listing returned another customer's row");
        }
    }
}
