package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DashboardCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By section =
            By.cssSelector("#dashboard.section.on");

    private final By heading =
            By.cssSelector("#dashboard h1");

    private final By totalBalance =
            By.id("total");

    private final By metricCards =
            By.cssSelector("#metrics > .card");

    private final By metricNames =
            By.cssSelector("#metrics > .card .muted");

    private final By metricValues =
            By.cssSelector("#metrics > .card .metric");

    private final By recentTransactionsTable =
            By.cssSelector("#recent table");

    private final By recentTransactionRows =
            By.cssSelector("#recent table tbody tr");

    private final By recentDescriptions =
            By.cssSelector("#recent table tbody tr td:nth-child(2)");

    private final By recentStatuses =
            By.cssSelector("#recent table tbody tr td:nth-child(3)");

    private final By recentAmounts =
            By.cssSelector("#recent table tbody tr td:nth-child(4)");

    public DashboardCurrentPage(WebDriver driver) {

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

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            totalBalance
                    )
            );

            return "Dashboard".equals(
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

    public String getTotalBalance() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        totalBalance
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

    public List<String> getMetricNames() {

        return driver.findElements(
                metricNames
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getMetricValues() {

        return driver.findElements(
                metricValues
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public boolean containsMetric(
            String expectedName
    ) {

        return getMetricNames()
                .stream()
                .anyMatch(name ->
                        name.equalsIgnoreCase(
                                expectedName
                        )
                );
    }

    public boolean allMetricValuesContainCurrency() {

        return getMetricValues()
                .stream()
                .allMatch(value ->
                        value != null &&
                        !value.isBlank() &&
                        value.contains("$")
                );
    }

    public boolean isRecentTransactionsTableDisplayed() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            recentTransactionsTable
                    )
            ).isDisplayed();

        } catch (Exception exception) {

            return false;
        }
    }

    public int getRecentTransactionCount() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        recentTransactionRows,
                        0
                )
        );

        return driver.findElements(
                recentTransactionRows
        ).size();
    }

    public List<String> getRecentDescriptions() {

        return driver.findElements(
                recentDescriptions
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getRecentStatuses() {

        return driver.findElements(
                recentStatuses
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getRecentAmounts() {

        return driver.findElements(
                recentAmounts
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public boolean allRecentTransactionsHaveContent() {

        int count =
                getRecentTransactionCount();

        return getRecentDescriptions().size() == count
                &&
                getRecentStatuses().size() == count
                &&
                getRecentAmounts().size() == count
                &&
                getRecentDescriptions()
                        .stream()
                        .allMatch(text ->
                                text != null &&
                                !text.isBlank()
                        );
    }
}