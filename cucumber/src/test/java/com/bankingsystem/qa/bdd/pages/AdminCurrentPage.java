package com.bankingsystem.qa.bdd.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By adminNavigation =
            By.id("adminNav");

    private final By section =
            By.cssSelector("#admin.section.on");

    private final By heading =
            By.cssSelector("#admin h1");

    private final By metricCards =
            By.cssSelector("#adminData > .card");

    public AdminCurrentPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    public void open() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        adminNavigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        section
                )
        );

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        metricCards,
                        0
                )
        );
    }

    public boolean isLoaded() {

        try {

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            section
                    )
            );

            return "Admin console".equals(
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

    public int getMetricCount() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        metricCards,
                        0
                )
        );

        return driver.findElements(
                metricCards
        ).size();
    }

    public Map<String, String> getMetrics() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        metricCards,
                        0
                )
        );

        List<WebElement> cards =
                driver.findElements(
                        metricCards
                );

        Map<String, String> metrics =
                new LinkedHashMap<>();

        for (WebElement card : cards) {

            String name =
                    card.findElement(
                            By.cssSelector(".muted")
                    ).getText();

            String value =
                    card.findElement(
                            By.cssSelector(".metric")
                    ).getText();

            metrics.put(
                    name,
                    value
            );
        }

        return metrics;
    }

    public boolean allMetricsHaveValues() {

        return getMetrics()
                .values()
                .stream()
                .allMatch(value ->
                        value != null &&
                        !value.isBlank()
                );
    }

    public boolean containsMetric(
            String expectedName
    ) {

        return getMetrics()
                .keySet()
                .stream()
                .anyMatch(name ->
                        name.equalsIgnoreCase(
                                expectedName
                        )
                );
    }

    public String getMetricValue(
            String expectedName
    ) {

        return getMetrics()
                .entrySet()
                .stream()
                .filter(entry ->
                        entry.getKey()
                                .equalsIgnoreCase(
                                        expectedName
                                )
                )
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("");
    }
}