package com.bankingsystem.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream =
                     ConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "config.properties was not found in src/test/resources"
                );
            }

            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to load config.properties",
                    e
            );
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {

        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String value = properties.getProperty(key);

        if (value == null) {
            throw new RuntimeException(
                    "Configuration property not found: " + key
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
}
