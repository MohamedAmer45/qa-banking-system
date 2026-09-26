package com.bankingsystem.qa.api.support;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Shared request configuration and sign-in for the API suite.
 *
 * Tokens are cached per role for the life of the run. A sign-in is two round
 * trips plus a bcrypt-class password hash, and the authorization matrix alone
 * would otherwise repeat it for every endpoint it probes.
 */
public abstract class ApiTest {

    private static final Map<String, String> TOKENS = new HashMap<>();

    protected static RequestSpecification spec;

    @BeforeSuite(alwaysRun = true)
    public void configure() {
        RestAssured.baseURI = baseUrl();

        spec = new RequestSpecBuilder()
                .setBaseUri(baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                // Logged only on failure, so a passing run stays readable and
                // a failing one carries the request that caused it.
                .log(LogDetail.URI)
                .addFilter(new AllureRestAssured())
                .build();
    }

    protected static String baseUrl() {
        String system = System.getProperty("api.base.url");
        if (system != null && !system.isBlank()) {
            return system;
        }

        String environment = System.getenv("BASE_URL");
        if (environment != null && !environment.isBlank()) {
            return environment;
        }

        Properties properties = new Properties();

        try (InputStream in = ApiTest.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }

        return properties.getProperty("api.base.url", "https://novabank-banking-system.vercel.app");
    }

    /** A request with no credentials. */
    protected RequestSpecification anonymous() {
        return io.restassured.RestAssured.given().spec(spec);
    }

    /** A request carrying the given role's bearer token. */
    protected RequestSpecification as(Users.User user) {
        return io.restassured.RestAssured.given()
                .spec(spec)
                .header("Authorization", "Bearer " + tokenFor(user));
    }

    /**
     * Completes the credential and MFA handshake. A password alone never
     * yields a usable session, so both calls are always required.
     */
    protected static String tokenFor(Users.User user) {
        return TOKENS.computeIfAbsent(user.email(), email -> {
            String challenge = io.restassured.RestAssured.given()
                    .spec(spec)
                    .body("""
                            {"email":"%s","password":"%s"}"""
                            .formatted(user.email(), user.password()))
                    .post("/api/auth/login")
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("challenge");

            return io.restassured.RestAssured.given()
                    .spec(spec)
                    .body("""
                            {"challenge":"%s","code":"%s"}"""
                            .formatted(challenge, Users.MFA_CODE))
                    .post("/api/auth/mfa")
                    .then()
                    .statusCode(200)
                    .extract()
                    .path("token");
        });
    }

    /** The first active EGP account belonging to the signed-in customer. */
    protected int firstActiveAccountId(Users.User user) {
        return as(user)
                .get("/api/accounts")
                .then()
                .statusCode(200)
                .extract()
                .path("find { it.status == 'ACTIVE' && it.currency == 'EGP' }.id");
    }

    protected int firstVerifiedBeneficiaryId(Users.User user) {
        return as(user)
                .get("/api/beneficiaries")
                .then()
                .statusCode(200)
                .extract()
                .path("find { it.status == 'ACTIVE' && it.verified == 1 }.id");
    }

    protected static String uniqueKey(String prefix) {
        return prefix + "-" + System.nanoTime();
    }
}
