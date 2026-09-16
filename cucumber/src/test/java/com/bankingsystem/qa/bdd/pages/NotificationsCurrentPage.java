package com.bankingsystem.qa.bdd.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class NotificationsCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By notificationsNavigation =
            By.cssSelector("#nav button[data-s='notifications']");

    private final By section =
            By.cssSelector("#notifications.section.on");

    private final By heading =
            By.cssSelector("#notifications h1");

    private final By notificationCards =
            By.cssSelector("#notes > .card");

    public NotificationsCurrentPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    public boolean isLoaded() {

        try {

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            section
                    )
            );

            return "Notifications".equals(
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    heading
                            )
                    ).getText()
            );

        } catch (Exception exception) {

            return false;
        }
    }

    public String getPageTitleText() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        heading
                )
        ).getText();
    }

    public int getNotificationCount() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        notificationCards,
                        0
                )
        );

        return driver.findElements(
                notificationCards
        ).size();
    }

    public List<String> getNotifications() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        notificationCards,
                        0
                )
        );

        return driver.findElements(
                notificationCards
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public boolean allNotificationsHaveContent() {

        return getNotifications()
                .stream()
                .allMatch(notification ->
                        notification != null &&
                        !notification.isBlank()
                );
    }

    public boolean containsText(
            String expectedText
    ) {

        return getNotifications()
                .stream()
                .anyMatch(notification ->
                        notification
                                .toLowerCase()
                                .contains(
                                        expectedText.toLowerCase()
                                )
                );
    }

    public void open() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        notificationsNavigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        section
                )
        );
    }
}