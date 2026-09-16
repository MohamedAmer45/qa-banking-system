package com.bankingsystem.qa.bdd.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TransactionsCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By section =
            By.cssSelector("#transactions.section.on");

    private final By heading =
            By.cssSelector("#transactions h1");

    private final By table =
            By.cssSelector("#tx table");

    private final By rows =
            By.cssSelector("#tx table tbody tr");

    private final By dates =
            By.cssSelector("#tx table tbody tr td:nth-child(1)");

    private final By descriptions =
            By.cssSelector("#tx table tbody tr td:nth-child(2)");

    private final By statuses =
            By.cssSelector("#tx table tbody tr td:nth-child(3)");

    private final By amounts =
            By.cssSelector("#tx table tbody tr td:nth-child(4)");

    public TransactionsCurrentPage(WebDriver driver) {

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
                            table
                    )
            );

            return "Transactions".equals(
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

    public int getTransactionCount() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        rows,
                        0
                )
        );

        return driver.findElements(
                rows
        ).size();
    }

    public List<String> getDates() {

        return driver.findElements(
                dates
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getDescriptions() {

        return driver.findElements(
                descriptions
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getStatuses() {

        return driver.findElements(
                statuses
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getAmounts() {

        return driver.findElements(
                amounts
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public boolean allTransactionsHaveDates() {

        return getDates()
                .stream()
                .allMatch(date ->
                        date != null &&
                        !date.isBlank()
                );
    }

    public boolean allTransactionsHaveDescriptions() {

        return getDescriptions()
                .stream()
                .allMatch(description ->
                        description != null &&
                        !description.isBlank()
                );
    }

    public boolean allTransactionsHaveStatuses() {

        return getStatuses()
                .stream()
                .allMatch(status ->
                        status != null &&
                        !status.isBlank()
                );
    }

    public boolean allTransactionsHaveAmounts() {

        return getAmounts()
                .stream()
                .allMatch(amount ->
                        amount != null &&
                        !amount.isBlank() &&
                        amount.contains("$")
                );
    }

    public boolean hasCreditTransaction() {

        return getAmounts()
                .stream()
                .anyMatch(amount ->
                        amount.trim().startsWith("+")
                );
    }

    public boolean hasDebitTransaction() {

        return getAmounts()
                .stream()
                .anyMatch(amount ->
                        amount.trim().startsWith("-")
                );
    }

    public boolean allStatusesAreCompleted() {

        return getStatuses()
                .stream()
                .allMatch(status ->
                        status.equalsIgnoreCase(
                                "completed"
                        )
                );
    }

    public TransactionsCurrentPage open() {

        By navigation =
                By.cssSelector(
                        "#nav button[data-s='transactions']"
                );

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        navigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        section
                )
        );

        return this;
    }
}
