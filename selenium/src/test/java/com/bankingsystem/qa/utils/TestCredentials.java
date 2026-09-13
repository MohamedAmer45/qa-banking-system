package com.bankingsystem.qa.utils;

public final class TestCredentials {

    private TestCredentials() {
    }

    public static String customerEmail() {
        return getRequired(
                "NOVABANK_CUSTOMER_EMAIL"
        );
    }

    public static String customerPassword() {
        return getRequired(
                "NOVABANK_CUSTOMER_PASSWORD"
        );
    }

    public static String mfaCode() {
        return getRequired(
                "NOVABANK_MFA_CODE"
        );
    }

    private static String getRequired(
            String variableName
    ) {

        String value =
                System.getenv(variableName);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is missing: "
                            + variableName
            );
        }

        return value.trim();
    }
}
