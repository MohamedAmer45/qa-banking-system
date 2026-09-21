package com.bankingsystem.qa.db.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Drives the application so a test can assert on what it persisted.
 *
 * The cross-layer requirements — a transfer writing its ledger rows, a failure
 * leaving nothing behind, a retry not duplicating — can only be tested by
 * performing a real operation and then reading the database directly.
 */
public final class Api {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String MFA_CODE = "123456";

    private final String baseUrl;
    private String token;

    public Api() {
        this.baseUrl = Config.apiBaseUrl();
    }

    public record Result(int status, JsonNode body) {

        public String text(String field) {
            JsonNode value = body.get(field);
            return value == null || value.isNull() ? null : value.asText();
        }

        public long number(String field) {
            return body.get(field).asLong();
        }
    }

    private Result send(HttpRequest request) {
        try {
            HttpResponse<String> response =
                    CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode body = response.body() == null || response.body().isBlank()
                    ? MAPPER.createObjectNode()
                    : MAPPER.readTree(response.body());

            return new Result(response.statusCode(), body);
        } catch (Exception e) {
            throw new IllegalStateException("API request failed: " + request.uri(), e);
        }
    }

    private HttpRequest.Builder request(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json");

        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        return builder;
    }

    public Result get(String path) {
        return send(request(path).GET().build());
    }

    public Result post(String path, String json) {
        return send(request(path)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build());
    }

    public Result delete(String path) {
        return send(request(path).DELETE().build());
    }

    /**
     * Full credential and MFA handshake. Every seeded user has MFA enabled, so
     * a password alone never yields a usable session.
     */
    public Api signIn(String email, String password) {
        Result login = post("/api/auth/login",
                """
                {"email":"%s","password":"%s"}""".formatted(email, password));

        if (login.status() != 200) {
            throw new IllegalStateException(
                    "Sign-in failed for " + email + ": " + login.body());
        }

        Result mfa = post("/api/auth/mfa",
                """
                {"challenge":"%s","code":"%s"}"""
                        .formatted(login.text("challenge"), MFA_CODE));

        if (mfa.status() != 200) {
            throw new IllegalStateException("MFA failed for " + email + ": " + mfa.body());
        }

        this.token = mfa.text("token");
        return this;
    }

    public Api signInAsCustomer() {
        return signIn("customer@novabank.test", "Demo123!");
    }

    public Api signInAsAdmin() {
        return signIn("admin@novabank.test", "Admin123!");
    }

    public String token() {
        return token;
    }

    /** The signed-in customer's numeric id, as stored. */
    public long userId() {
        return get("/api/me").body().get("user").get("id").asLong();
    }
}
