package com.bankingsystem.qa.bdd.utils;

import com.bankingsystem.qa.bdd.config.ConfigReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class WaitUtils {

    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {

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
}