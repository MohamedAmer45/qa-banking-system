package com.bankingsystem.qa.bdd.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream =
                     ConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "config.properties was not found in src/test/resources."
                );
            }

            PROPERTIES.load(inputStream);

        } catch (IOException exception) {
            throw new ExceptionInInitializerError(
                    "Failed to load config.properties: " +
                    exception.getMessage()
            );
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {

        String value = resolveValue(key);

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Configuration value is missing: " + key
            );
        }

        return value.trim();
    }

    public static boolean getBoolean(String key) {

        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key) {

        return Integer.parseInt(get(key));
    }

    private static String resolveValue(String key) {

        String systemProperty = System.getProperty(key);

        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }

        String environmentVariable =
                System.getenv(toEnvironmentVariableName(key));

        if (environmentVariable != null && !environmentVariable.isBlank()) {
            return environmentVariable;
        }

        return PROPERTIES.getProperty(key);
    }

    private static String toEnvironmentVariableName(String key) {

        return key.toUpperCase(Locale.ROOT)
                .replace('.', '_')
                .replace('-', '_');
    }
}