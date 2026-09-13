package com.bankingsystem.qa.base;

import com.bankingsystem.qa.utils.ConfigReader;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> driver =
            new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initializeDriver() {

        String browser =
                ConfigReader.get("browser").toLowerCase();

        boolean headless =
                ConfigReader.getBoolean("headless");

        WebDriver webDriver;

        switch (browser) {

            case "chrome" -> {

                ChromeOptions options =
                        new ChromeOptions();

                options.addArguments(
                        "--start-maximized"
                );

                if (headless) {
                    options.addArguments(
                            "--headless=new",
                            "--window-size=1920,1080"
                    );
                }

                webDriver =
                        new ChromeDriver(options);
            }

            case "edge" -> {

                EdgeOptions options =
                        new EdgeOptions();

                options.addArguments(
                        "--start-maximized"
                );

                if (headless) {
                    options.addArguments(
                            "--headless=new",
                            "--window-size=1920,1080"
                    );
                }

                webDriver =
                        new EdgeDriver(options);
            }

            case "firefox" -> {

                FirefoxOptions options =
                        new FirefoxOptions();

                if (headless) {
                    options.addArguments(
                            "-headless"
                    );
                }

                webDriver =
                        new FirefoxDriver(options);

                webDriver.manage()
                        .window()
                        .maximize();
            }

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported browser: " + browser
                    );
        }

        webDriver.manage()
                .timeouts()
                .pageLoadTimeout(
                        Duration.ofSeconds(
                                ConfigReader.getInt(
                                        "page.load.timeout.seconds"
                                )
                        )
                );

        webDriver.manage()
                .timeouts()
                .implicitlyWait(
                        Duration.ZERO
                );

        driver.set(webDriver);
    }

    public static WebDriver getDriver() {

        WebDriver webDriver =
                driver.get();

        if (webDriver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized."
            );
        }

        return webDriver;
    }

    public static void quitDriver() {

        WebDriver webDriver =
                driver.get();

        if (webDriver != null) {

            webDriver.quit();

            driver.remove();
        }
    }
}
