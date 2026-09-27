package com.bankingsystem.qa.api.security;

import com.bankingsystem.qa.api.support.ApiTest;
import com.bankingsystem.qa.api.support.Users;

import io.qameta.allure.Description;
import io.qameta.allure.Story;

import io.restassured.response.Response;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * WEBSEC-001 to WEBSEC-009 — transport and browser hardening.
 *
 * These live here rather than only in the ZAP scan for a reason of cadence. A
 * scanner runs on its own workflow and finds things nobody thought to look for;
 * these run on every API build and hold the specific controls in place. The
 * scanner is the net, this is the latch.
 *
 * They are also the class of defect that fails silently. Nothing breaks when a
 * header disappears — no test goes red, no user complains — so without an
 * assertion the absence survives indefinitely.
 */
@Story("Web security hardening")
public class SecurityHeadersTest extends ApiTest {

    /** Endpoints spanning each response path: JSON, error, and the SPA itself. */
    @DataProvider(name = "responsePaths")
    public Object[][] responsePaths() {
        return new Object[][]{
                {"/api/health", "an unauthenticated JSON response"},
                {"/api/nope", "a 404 error response"},
                {"/", "the application shell"}
        };
    }

    @Test(dataProvider = "responsePaths", description = "WEBSEC-002: framing is refused")
    @Description("Clickjacking: a bank that can be framed can be operated through an overlay")
    public void refusesToBeFramed(String path, String what) {
        Response response = anonymous().get(path);

        String frameOptions = response.header("X-Frame-Options");
        String csp = response.header("Content-Security-Policy");

        boolean denied =
                "DENY".equalsIgnoreCase(frameOptions)
                        || "SAMEORIGIN".equalsIgnoreCase(frameOptions)
                        || (csp != null && csp.contains("frame-ancestors"));

        assertTrue(denied,
                "WEBSEC-002: " + what + " must refuse framing via X-Frame-Options or "
                        + "frame-ancestors. X-Frame-Options=" + frameOptions);
    }

    @Test(dataProvider = "responsePaths", description = "WEBSEC-003: content type is not sniffed")
    public void forbidsMimeSniffing(String path, String what) {
        String header = anonymous().get(path).header("X-Content-Type-Options");

        assertEquals(header, "nosniff",
                "WEBSEC-003: " + what + " must send X-Content-Type-Options: nosniff");
    }

    @Test(dataProvider = "responsePaths", description = "WEBSEC-004: the referrer does not leak")
    public void withholdsReferrer(String path, String what) {
        String header = anonymous().get(path).header("Referrer-Policy");

        assertNotNull(header, "WEBSEC-004: " + what + " must declare a Referrer-Policy");
        assertTrue(
                header.contains("no-referrer") || header.contains("same-origin")
                        || header.contains("strict-origin"),
                "WEBSEC-004: Referrer-Policy must not permit sending the full URL "
                        + "cross-origin, was: " + header
        );
    }

    @Test(description = "WEBSEC-001: a content security policy is declared")
    public void declaresAContentSecurityPolicy() {
        String csp = anonymous().get("/api/health").header("Content-Security-Policy");

        assertNotNull(csp, "WEBSEC-001: responses must declare a Content-Security-Policy");

        // The directives that hold regardless of the inline-handler limitation
        // recorded against WEBSEC-001 in the catalog.
        assertTrue(csp.contains("default-src 'self'"), "CSP must default to same-origin: " + csp);
        assertTrue(csp.contains("object-src 'none'"), "CSP must refuse plugins: " + csp);
        assertTrue(csp.contains("base-uri 'self'"),
                "CSP must stop an injected <base> re-pointing relative URLs: " + csp);
    }

    @Test(description = "WEBSEC-001: script-src does not permit inline script")
    @Description("The directive that decides whether injected script can execute")
    public void scriptSourcesDoNotPermitInlineOrEval() {
        String csp = anonymous().get("/api/health").header("Content-Security-Policy");

        String scriptSrc = java.util.Arrays.stream(csp.split(";"))
                .map(String::trim)
                .filter(d -> d.startsWith("script-src"))
                .findFirst()
                .orElse("");

        assertFalse(scriptSrc.isEmpty(), "WEBSEC-001: the policy must declare script-src: " + csp);

        /*
         * This was permitted once, because the interface attached 62 handlers as
         * inline attributes. They are delegated from data-action attributes now,
         * so nothing in the page needs it and its return would be a regression
         * that re-opens injected script rather than a documented compromise.
         */
        assertFalse(scriptSrc.contains("'unsafe-inline'"),
                "WEBSEC-001: script-src must not permit inline script, was: " + scriptSrc);
        assertFalse(scriptSrc.contains("'unsafe-eval'"),
                "WEBSEC-001: script-src must not permit eval, was: " + scriptSrc);
        assertFalse(scriptSrc.contains("*"),
                "WEBSEC-001: script sources must not be wildcarded, was: " + scriptSrc);
    }

    @Test(description = "WEBSEC-005: cross-origin access is not granted to everyone")
    @Description("A wildcard CORS grant tells any site it may read this API's responses")
    public void doesNotGrantCorsToAnyOrigin() {
        Response response = anonymous()
                .header("Origin", "https://attacker.example")
                .get("/api/health");

        String allowed = response.header("Access-Control-Allow-Origin");

        assertFalse("*".equals(allowed),
                "WEBSEC-005: the API must not answer with Access-Control-Allow-Origin: *");
        assertFalse("https://attacker.example".equals(allowed),
                "WEBSEC-005: an unknown origin must not be echoed back as permitted");
    }

    @Test(description = "WEBSEC-005: a preflight from an unknown origin is not granted")
    public void preflightFromUnknownOriginIsNotGranted() {
        Response response = anonymous()
                .header("Origin", "https://attacker.example")
                .header("Access-Control-Request-Method", "POST")
                .options("/api/transfers");

        assertFalse("*".equals(response.header("Access-Control-Allow-Origin")),
                "WEBSEC-005: preflight must not answer with a wildcard");
    }

    @Test(description = "WEBSEC-006: account data is not cached")
    public void accountDataIsNotStoredByCaches() {
        String cacheControl = as(Users.CUSTOMER).get("/api/accounts").header("Cache-Control");

        assertNotNull(cacheControl, "WEBSEC-006: account responses must set Cache-Control");
        assertTrue(cacheControl.contains("no-store"),
                "WEBSEC-006: account data must not be stored by any cache, was: " + cacheControl);
    }

    @Test(description = "WEBSEC-007: HTTPS is enforced where the target uses it")
    @Description("Skipped against a plain-http local target, where HSTS means nothing")
    public void enforcesHttpsWhereApplicable() {
        if (!baseUrl().startsWith("https://")) {
            throw new org.testng.SkipException(
                    "WEBSEC-007 applies to an HTTPS deployment; this run targets " + baseUrl());
        }

        String hsts = anonymous().get("/api/health").header("Strict-Transport-Security");

        assertNotNull(hsts, "WEBSEC-007: an HTTPS deployment must send Strict-Transport-Security");

        // A max-age short enough to lapse between visits protects nobody.
        java.util.regex.Matcher m =
                java.util.regex.Pattern.compile("max-age=(\\d+)").matcher(hsts);
        assertTrue(m.find(), "WEBSEC-007: HSTS must declare a max-age, was: " + hsts);
        assertTrue(Long.parseLong(m.group(1)) >= 15_552_000L,
                "WEBSEC-007: HSTS max-age should be at least 180 days, was: " + hsts);
    }

    @Test(description = "WEBSEC-008: the server does not advertise its technology")
    public void doesNotAdvertiseServerTechnology() {
        Response response = anonymous().get("/api/health");

        assertNull(response.header("X-Powered-By"),
                "WEBSEC-008: X-Powered-By discloses the framework");

        String server = response.header("Server");
        if (server != null) {
            /*
             * A hosting platform may set its own Server header and that is
             * outside the application's control. What matters is that it does
             * not carry a version an attacker can match to a known advisory.
             */
            assertFalse(server.matches(".*\\d+\\.\\d+.*"),
                    "WEBSEC-008: the Server header must not disclose a version, was: " + server);
        }
    }

    @Test(description = "WEBSEC-009: unused browser features are denied")
    public void deniesUnusedBrowserFeatures() {
        String policy = anonymous().get("/").header("Permissions-Policy");

        assertNotNull(policy, "WEBSEC-009: the application shell must send a Permissions-Policy");

        // A bank has no use for these, so they are denied rather than left
        // available to anything that manages to run in the page.
        for (String feature : new String[]{"camera", "microphone", "geolocation"}) {
            assertTrue(policy.contains(feature + "=()"),
                    "WEBSEC-009: " + feature + " should be denied, policy was: " + policy);
        }
    }

    private static void assertNull(Object value, String message) {
        assertTrue(value == null, message + " (was: " + value + ")");
    }
}
