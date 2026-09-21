package com.bankingsystem.qa.bdd.utils;

import com.bankingsystem.qa.bdd.config.ConfigReader;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(
                                ConfigReader.getInt(
                                        "explicit.wait.seconds"
                                )
                        )
                );
    }

    public WebElement waitForVisible(By locator) {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    public WebElement waitForClickable(By locator) {

        return wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    public WebElement waitForPresent(By locator) {

        return wait.until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );
    }

    public boolean waitForInvisible(By locator) {

        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(locator)
        );
    }

    /**
     * Wait for an element reference to become stale.
     *
     * Used where a container replaces its contents — the toast area rewrites
     * itself on every message, so waiting for the previous element to go stale
     * is what distinguishes a new message from the one already on screen.
     */
    public boolean waitForStaleness(WebElement element) {

        return wait.until(
                ExpectedConditions.stalenessOf(element)
        );
    }

    /**
     * Wait for a JavaScript expression to return a truthy value.
     *
     * The script is executed as a function body, so it must {@code return} its
     * result.
     */
    public boolean waitForJavaScriptCondition(String script) {

        return wait.until(driver -> {
            Object result =
                    ((JavascriptExecutor) driver).executeScript(script);

            return Boolean.TRUE.equals(result);
        });
    }

    public WebDriver getDriver() {
        return driver;
    }
}
