package com.bankingsystem.qa.api.contract;

import com.bankingsystem.qa.api.support.ApiTest;
import com.bankingsystem.qa.api.support.Users;

import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

/**
 * Response-shape validation — this suite's distinct contribution.
 *
 * The UI suites assert on rendered values and the database suite on stored
 * rows. Neither notices when a field changes type, an enum gains a value, or a
 * response starts carrying something it should not. A schema does.
 */
public class SchemaTest extends ApiTest {

    @Test(description = "SYS-002/SYS-010: health reports a stable, typed contract")
    public void healthMatchesSchema() {
        anonymous()
                .get("/api/health")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/health.json"));
    }

    @Test(description = "ACC-001: the accounts collection matches its contract")
    public void accountsMatchSchema() {
        as(Users.CUSTOMER)
                .get("/api/accounts")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/account.json"));
    }

    @Test(description = "TRF-001: a created transfer matches its contract")
    public void createdTransferMatchesSchema() {
        int account = firstActiveAccountId(Users.CUSTOMER);
        int beneficiary = firstVerifiedBeneficiaryId(Users.CUSTOMER);

        as(Users.CUSTOMER)
                .body("""
                        {"fromAccountId":%d,"beneficiaryId":%d,"amount":1.25,
                         "idempotencyKey":"%s"}"""
                        .formatted(account, beneficiary, uniqueKey("schema")))
                .post("/api/transfers")
                .then()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/transfer.json"));
    }

    @Test(description = "SYS-003: an error body matches the error contract")
    public void errorMatchesSchema() {
        anonymous()
                .get("/api/accounts")
                .then()
                .statusCode(401)
                .body(matchesJsonSchemaInClasspath("schemas/error.json"));
    }

    @Test(description = "AUTH-003: the session response matches its contract")
    public void sessionMatchesSchema() {
        String challenge = anonymous()
                .body("""
                        {"email":"%s","password":"%s"}"""
                        .formatted(Users.RECEIVER.email(), Users.RECEIVER.password()))
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("challenge");

        anonymous()
                .body("""
                        {"challenge":"%s","code":"%s"}"""
                        .formatted(challenge, Users.MFA_CODE))
                .post("/api/auth/mfa")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/session.json"));
    }

    @Test(description = "DB-006: every money field is an integer, not a decimal")
    public void moneyFieldsAreIntegers() {
        /*
         * A float here would still render correctly in the UI and still be
         * stored correctly, while quietly breaking any consumer that assumes
         * minor units. The schema pins the type; this pins it at the value
         * level for the fields the schema cannot reach individually.
         */
        as(Users.CUSTOMER)
                .get("/api/accounts")
                .then()
                .statusCode(200)
                .body("balance_minor", everyItem(org.hamcrest.Matchers.isA(Integer.class)))
                .body("available_minor", everyItem(org.hamcrest.Matchers.isA(Integer.class)))
                .body("daily_limit_minor", everyItem(org.hamcrest.Matchers.isA(Integer.class)));
    }

    @Test(description = "SYS-005: an account never carries credential material")
    public void accountsCarryNoCredentials() {
        as(Users.CUSTOMER)
                .get("/api/accounts")
                .then()
                .statusCode(200)
                .body("$", everyItem(not(hasKey("password_hash"))))
                .body("$", everyItem(not(hasKey("mfa_code"))));
    }
}
