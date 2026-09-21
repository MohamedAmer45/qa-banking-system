package com.bankingsystem.qa.api.system;

import com.bankingsystem.qa.api.support.ApiTest;
import com.bankingsystem.qa.api.support.Users;

import io.restassured.response.Response;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * SYS-002, 003, 006, 007, 008, 010: the behaviours that make the API usable as
 * a test target and safe under misuse.
 */
public class SystemBehaviourTest extends ApiTest {

    // ------------------------------------------------------------- SYS-002

    @DataProvider(name = "statusCodes")
    public Object[][] statusCodes() {
        return new Object[][]{
                {"unknown route", "/api/does-not-exist", 404},
                {"unauthenticated", "/api/accounts", 401}
        };
    }

    @Test(dataProvider = "statusCodes",
            description = "SYS-002: the API answers with the appropriate status")
    public void statusCodesAreAppropriate(String scenario, String path, int expected) {
        anonymous().get(path).then().statusCode(expected);
    }

    @Test(description = "SYS-002: an unknown API route returns JSON, not an HTML page")
    public void unknownRouteReturnsJson() {
        anonymous()
                .get("/api/does-not-exist")
                .then()
                .statusCode(404)
                .contentType(io.restassured.http.ContentType.JSON)
                .body("error", org.hamcrest.Matchers.notNullValue());
    }

    @Test(description = "SYS-002: a business rejection is 409, not 400 or 500")
    public void businessRejectionIsConflict() {
        int account = firstActiveAccountId(Users.CUSTOMER);
        int beneficiary = firstVerifiedBeneficiaryId(Users.CUSTOMER);

        as(Users.CUSTOMER)
                .body("""
                        {"fromAccountId":%d,"beneficiaryId":%d,"amount":99999999,
                         "idempotencyKey":"%s"}"""
                        .formatted(account, beneficiary, uniqueKey("sys")))
                .post("/api/transfers")
                .then()
                .statusCode(409);
    }

    @Test(description = "SYS-002: malformed JSON is a client error, not a crash")
    public void malformedJsonIsRejectedCleanly() {
        as(Users.CUSTOMER)
                .body("{ this is not json")
                .post("/api/transfers")
                .then()
                .statusCode(400)
                .body("error", org.hamcrest.Matchers.notNullValue());
    }

    @Test(description = "SYS-008: an oversized body is refused rather than absorbed")
    public void oversizedBodyIsRefused() {
        String huge = "x".repeat(2_000_000);

        Response response = as(Users.CUSTOMER)
                .body("{\"memo\":\"" + huge + "\"}")
                .post("/api/transfers");

        assertTrue(response.statusCode() == 413 || response.statusCode() == 400,
                "SYS-008: a 2MB body should be refused with 413 or 400, was "
                        + response.statusCode());
    }

    // ------------------------------------------------------------- SYS-006

    @Test(description = "SYS-006: every financial operation returns a unique reference")
    public void financialOperationsAreTraceable() {
        int account = firstActiveAccountId(Users.CUSTOMER);
        int beneficiary = firstVerifiedBeneficiaryId(Users.CUSTOMER);

        String first = createTransfer(account, beneficiary);
        String second = createTransfer(account, beneficiary);

        assertNotNull(first);
        assertNotNull(second);

        assertTrue(first.matches("^TRF-\\d{8}-[A-F0-9]+$"),
                "SYS-006: reference is not in the documented format: " + first);

        assertTrue(!first.equals(second),
                "SYS-006: two transfers shared the reference " + first
                        + ", so neither can be traced unambiguously");
    }

    private String createTransfer(int account, int beneficiary) {
        return as(Users.CUSTOMER)
                .body("""
                        {"fromAccountId":%d,"beneficiaryId":%d,"amount":1,
                         "idempotencyKey":"%s"}"""
                        .formatted(account, beneficiary, uniqueKey("trace")))
                .post("/api/transfers")
                .then()
                .statusCode(201)
                .extract()
                .path("reference");
    }

    @Test(description = "SYS-006: a transfer is retrievable by the reference it returned")
    public void referenceResolvesToItsTransfer() {
        int account = firstActiveAccountId(Users.CUSTOMER);
        String reference = createTransfer(account, firstVerifiedBeneficiaryId(Users.CUSTOMER));

        List<String> references = as(Users.CUSTOMER)
                .get("/api/transfers")
                .then()
                .statusCode(200)
                .extract()
                .path("reference");

        assertTrue(references.contains(reference),
                "SYS-006: the returned reference " + reference
                        + " does not appear in the customer's own history");
    }

    // ------------------------------------------------------------- SYS-007

    @Test(description = "SYS-007: timestamps are ISO-8601 and parse consistently")
    public void timestampsAreConsistent() {
        String serverTime = anonymous().get("/api/health")
                .then().statusCode(200).extract().path("time");

        // Parses, and therefore sorts lexicographically as it does chronologically.
        OffsetDateTime parsed = OffsetDateTime.parse(serverTime);
        assertNotNull(parsed);

        List<String> created = as(Users.CUSTOMER)
                .get("/api/transfers")
                .then()
                .statusCode(200)
                .extract()
                .path("created_at");

        for (String timestamp : created) {
            OffsetDateTime.parse(timestamp);
        }
    }

    @Test(description = "SYS-007: history is returned newest first")
    public void historyIsOrderedConsistently() {
        List<String> created = as(Users.CUSTOMER)
                .get("/api/transfers")
                .then()
                .statusCode(200)
                .extract()
                .path("created_at");

        if (created.size() < 2) {
            throw new org.testng.SkipException("not enough history to check ordering");
        }

        for (int i = 1; i < created.size(); i++) {
            assertTrue(created.get(i - 1).compareTo(created.get(i)) >= 0,
                    "SYS-007: transfer history is not in descending time order at "
                            + "position " + i + ": " + created.get(i - 1)
                            + " then " + created.get(i));
        }
    }

    // ------------------------------------------------------------- SYS-010

    @Test(description = "SYS-010: health is reachable without authentication")
    public void healthIsAvailableToAutomation() {
        anonymous()
                .get("/api/health")
                .then()
                .statusCode(200)
                .body("status", org.hamcrest.Matchers.equalTo("ok"));
    }

    @Test(description = "SYS-010: the deployed build reports its storage engine")
    public void buildReportsItsStorage() {
        // A suite pointed at the wrong environment is a common and expensive
        // mistake; this makes the target self-identifying.
        anonymous()
                .get("/api/health")
                .then()
                .statusCode(200)
                .body("database", org.hamcrest.Matchers.equalTo("postgresql"));
    }

    @Test(description = "SYS-009: the API agrees with itself across two reads")
    public void repeatedReadsAgree() {
        int account = firstActiveAccountId(Users.CUSTOMER);

        Integer first = as(Users.CUSTOMER).get("/api/accounts").then()
                .statusCode(200).extract()
                .path("find { it.id == " + account + " }.balance_minor");

        Integer second = as(Users.CUSTOMER).get("/api/accounts").then()
                .statusCode(200).extract()
                .path("find { it.id == " + account + " }.balance_minor");

        assertEquals(second, first,
                "SYS-009: two consecutive reads of the same account disagreed");
    }
}
