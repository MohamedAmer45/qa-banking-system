package com.bankingsystem.qa.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {

        this.wait = new WebDriverWait(
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
                ExpectedConditions.visibilityOfElementLocated(
                        locator
                )
        );
    }

    public WebElement waitForClickable(By locator) {

        return wait.until(
                ExpectedConditions.elementToBeClickable(
                        locator
                )
        );
    }

    public WebElement waitForPresent(By locator) {

        return wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        locator
                )
        );
    }

    public boolean waitForInvisible(By locator) {

        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        locator
                )
        );
    }

    public boolean waitForText(
            By locator,
            String text
    ) {

        return wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        locator,
                        text
                )
        );
    }

    public boolean waitForUrlContains(
            String text
    ) {

        return wait.until(
                ExpectedConditions.urlContains(
                        text
                )
        );
    }

    public boolean waitForTitleContains(
            String text
    ) {

        return wait.until(
                ExpectedConditions.titleContains(
                        text
                )
        );
    }
}
