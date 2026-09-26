package com.bankingsystem.qa.db.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Resolution order: system property, then environment variable, then
 * config.properties. The environment variable is what CI sets, and it is the
 * same DATABASE_URL the application itself uses, so tests and application can
 * never drift onto different databases.
 */
public final class Config {

    private static final Properties FILE = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (in != null) {
                FILE.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
    }

    private Config() {
    }

    public static String get(String key, String environmentVariable, String fallback) {
        String system = System.getProperty(key);
        if (system != null && !system.isBlank()) {
            return system;
        }

        String environment = System.getenv(environmentVariable);
        if (environment != null && !environment.isBlank()) {
            return environment;
        }

        return FILE.getProperty(key, fallback);
    }

    public static String databaseUrl() {
        String url = get("database.url", "DATABASE_URL", null);

        if (url == null || url.isBlank()) {
            throw new IllegalStateException(
                    "No database URL. Set DATABASE_URL, or -Ddatabase.url, "
                            + "or database.url in src/test/resources/config.properties."
            );
        }

        return url;
    }

    public static String apiBaseUrl() {
        return get("api.base.url", "BASE_URL", "https://novabank-banking-system.vercel.app");
    }
}
