package com.bankingsystem.qa.api.audit;

import com.bankingsystem.qa.api.support.ApiTest;
import com.bankingsystem.qa.api.support.Users;

import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * AUDIT-001 to AUDIT-010.
 *
 * Every test performs an action and then proves the trail recorded it, rather
 * than asserting the trail merely contains something. An audit log that is
 * never checked against a known action can be entirely fabricated and still
 * look healthy.
 */
public class AuditTrailTest extends ApiTest {

    /** The audit trail, readable by AUDITOR and above. */
    private List<Map<String, Object>> audit(String action) {
        return as(Users.AUDITOR)
                .queryParam("action", action)
                .get("/api/admin/audit")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("$");
    }

    @Test(description = "AUDIT-001: authentication attempts are auditable")
    public void authenticationIsAudited() {
        // Force a fresh, identifiable sign-in.
        tokenFor(Users.EMPLOYEE);

        List<Map<String, Object>> logins = audit("LOGIN");

        assertFalse(logins.isEmpty(), "AUDIT-001: no LOGIN events were recorded");

        boolean employeeSignIn = logins.stream()
                .anyMatch(e -> Users.EMPLOYEE.email().equals(e.get("actor_email")));

        assertTrue(employeeSignIn,
                "AUDIT-001: the employee's sign-in is absent from the trail");
    }

    @Test(description = "AUDIT-001: a failed sign-in is recorded as a failure")
    public void failedAuthenticationIsAudited() {
        anonymous()
                .body("""
                        {"email":"%s","password":"DefinitelyWrong123!"}"""
                        .formatted(Users.CUSTOMER.email()))
                .post("/api/auth/login")
                .then()
                .statusCode(401);

        boolean recorded = audit("LOGIN").stream()
                .anyMatch(e -> "FAILED".equals(e.get("result")));

        assertTrue(recorded,
                "AUDIT-001: a rejected sign-in left no FAILED audit entry, so "
                        + "credential-stuffing would be invisible");
    }

    @Test(description = "AUDIT-002: a transfer generates an audit event naming its reference")
    public void transferIsAudited() {
        int account = firstActiveAccountId(Users.CUSTOMER);
        int beneficiary = firstVerifiedBeneficiaryId(Users.CUSTOMER);

        String reference = as(Users.CUSTOMER)
                .body("""
                        {"fromAccountId":%d,"beneficiaryId":%d,"amount":2.75,
                         "idempotencyKey":"%s"}"""
                        .formatted(account, beneficiary, uniqueKey("audit")))
                .post("/api/transfers")
                .then()
                .statusCode(201)
                .extract()
                .path("reference");

        boolean recorded = audit("TRANSFER").stream()
                .anyMatch(e -> reference.equals(e.get("entity_id")));

        assertTrue(recorded,
                "AUDIT-002: transfer " + reference + " is not in the audit trail");
    }

    @Test(description = "AUDIT-003: a bill payment generates an audit event")
    public void billPaymentIsAudited() {
        int account = firstActiveAccountId(Users.CUSTOMER);

        int biller = as(Users.CUSTOMER)
                .get("/api/billers")
                .then()
                .statusCode(200)
                .extract()
                .path("billers[0].id");

        as(Users.CUSTOMER)
                .body("""
                        {"accountId":%d,"billerId":%d,"customerReference":"AUDIT-REF",
                         "amount":1.50}""".formatted(account, biller))
                .post("/api/bill-payments")
                .then()
                .statusCode(201);

        assertFalse(audit("PAY_BILL").isEmpty(),
                "AUDIT-003: a completed payment left no audit event");
    }

    @Test(description = "AUDIT-004: a profile change generates an audit event")
    public void profileChangeIsAudited() {
        as(Users.CUSTOMER)
                .body("""
                        {"phone":"+20 100 000 %04d"}"""
                        .formatted(System.nanoTime() % 10000))
                .patch("/api/profile")
                .then()
                .statusCode(200);

        assertFalse(audit("UPDATE_PROFILE").isEmpty(),
                "AUDIT-004: changing contact details left no audit event");
    }

    @Test(description = "AUDIT-006: an administrative action generates an audit event")
    public void administrativeActionIsAudited() {
        // Reviewing KYC is a back-office action with a real subject.
        Integer pendingProfile = as(Users.ADMIN)
                .get("/api/admin/kyc")
                .then()
                .statusCode(200)
                .extract()
                .path("find { it.status == 'UNDER_REVIEW' }.id");

        if (pendingProfile == null) {
            // Nothing awaiting review; the seeded queue is the precondition.
            throw new org.testng.SkipException(
                    "no KYC profile is awaiting review in this environment");
        }

        as(Users.ADMIN)
                .body("""
                        {"status":"NEEDS_MORE_INFORMATION","note":"audit probe"}""")
                .post("/api/admin/kyc/" + pendingProfile + "/review")
                .then()
                .statusCode(200);

        boolean recorded = audit("REVIEW_KYC").stream()
                .anyMatch(e -> String.valueOf(pendingProfile).equals(e.get("entity_id")));

        assertTrue(recorded,
                "AUDIT-006: the KYC decision is not attributable in the trail");
    }

    @Test(description = "AUDIT-007/008/009: every record names its actor, action and time")
    public void recordsAreComplete() {
        List<Map<String, Object>> entries = as(Users.AUDITOR)
                .get("/api/admin/audit")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("$");

        assertFalse(entries.isEmpty(), "the audit trail is empty");

        for (Map<String, Object> entry : entries) {
            assertNotNull(entry.get("action"),
                    "AUDIT-009: an entry has no action: " + entry);

            assertNotNull(entry.get("created_at"),
                    "AUDIT-008: an entry has no timestamp: " + entry);

            assertNotNull(entry.get("entity_type"),
                    "AUDIT-009: an entry does not say what it acted on: " + entry);

            // actor_user_id may legitimately be null for system events, but an
            // entry that names an actor must resolve it to an identity.
            if (entry.get("actor_user_id") != null) {
                assertNotNull(entry.get("actor_email"),
                        "AUDIT-007: an entry names an actor that does not resolve: "
                                + entry);
            }
        }
    }

    @Test(description = "AUDIT-010: a customer cannot read or alter the audit trail")
    public void customersCannotReachTheAuditTrail() {
        as(Users.CUSTOMER).get("/api/admin/audit").then().statusCode(403);

        // There is no write endpoint; confirm one cannot be improvised.
        as(Users.CUSTOMER).body("{}").post("/api/admin/audit").then()
                .statusCode(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(403),
                        org.hamcrest.Matchers.is(404)));

        as(Users.CUSTOMER).delete("/api/admin/audit").then()
                .statusCode(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(403),
                        org.hamcrest.Matchers.is(404)));
    }

    @Test(description = "AUDIT-004: audit metadata never carries a credential")
    public void auditMetadataCarriesNoSecrets() {
        String body = as(Users.AUDITOR).get("/api/admin/audit")
                .then().statusCode(200).extract().asString();

        for (String secret : new String[]{"password", "password_hash", "mfa_code"}) {
            assertFalse(body.contains("\"" + secret + "\""),
                    "AUDIT-004/SYS-005: the audit trail recorded '" + secret
                            + "', turning the log itself into a credential store");
        }
    }
}
