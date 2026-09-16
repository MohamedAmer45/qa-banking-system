package com.bankingsystem.qa.bdd.driver;

import com.bankingsystem.qa.bdd.config.ConfigReader;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Locale;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initializeDriver() {

        if (DRIVER.get() != null) {
            throw new IllegalStateException(
                    "WebDriver is already initialized for the current thread."
            );
        }

        String browser =
                ConfigReader.get("browser").toLowerCase(Locale.ROOT);

        boolean headless = ConfigReader.getBoolean("headless");

        WebDriver webDriver =
                switch (browser) {
                    case "chrome" -> createChromeDriver(headless);
                    case "edge" -> createEdgeDriver(headless);
                    case "firefox" -> createFirefoxDriver(headless);
                    default -> throw new IllegalArgumentException(
                            "Unsupported browser: " + browser
                    );
                };

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
                .implicitlyWait(Duration.ZERO);

        if (headless) {
            webDriver.manage()
                    .window()
                    .setSize(new Dimension(1920, 1080));
        } else {
            webDriver.manage()
                    .window()
                    .maximize();
        }

        DRIVER.set(webDriver);
    }

    public static WebDriver getDriver() {

        WebDriver webDriver = DRIVER.get();

        if (webDriver == null) {
            throw new IllegalStateException(
                    "WebDriver has not been initialized for the current thread."
            );
        }

        return webDriver;
    }

    public static boolean hasDriver() {

        return DRIVER.get() != null;
    }

    public static void quitDriver() {

        WebDriver webDriver = DRIVER.get();

        if (webDriver != null) {
            try {
                webDriver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {

        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments(
                    "--headless=new",
                    "--window-size=1920,1080"
            );
        } else {
            options.addArguments("--start-maximized");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {

        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments(
                    "--headless=new",
                    "--window-size=1920,1080"
            );
        } else {
            options.addArguments("--start-maximized");
        }

        return new EdgeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
        }

        return new FirefoxDriver(options);
    }
}