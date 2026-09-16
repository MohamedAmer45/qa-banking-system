package com.bankingsystem.qa.bdd.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AccountsCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By section =
            By.cssSelector("#accounts.section.on");

    private final By heading =
            By.cssSelector("#accounts h1");

    private final By accountCards =
            By.cssSelector("#accountCards > .card");

    private final By accountTypes =
            By.cssSelector("#accountCards > .card h2");

    private final By accountNumbers =
            By.cssSelector("#accountCards > .card .muted");

    private final By accountBalances =
            By.cssSelector("#accountCards > .card .metric");

    public AccountsCurrentPage(WebDriver driver) {

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

            return "Accounts".equals(
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

    public int getAccountCount() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        accountCards,
                        0
                )
        );

        return driver.findElements(
                accountCards
        ).size();
    }

    public List<String> getAccountTypes() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        accountTypes,
                        0
                )
        );

        return driver.findElements(
                accountTypes
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getAccountNumbers() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        accountNumbers,
                        0
                )
        );

        return driver.findElements(
                accountNumbers
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getAccountBalances() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        accountBalances,
                        0
                )
        );

        return driver.findElements(
                accountBalances
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public boolean allAccountNumbersAreMasked() {

        return getAccountNumbers()
                .stream()
                .allMatch(number ->
                        number.contains("****")
                );
    }

    public boolean allBalancesContainCurrency() {

        return getAccountBalances()
                .stream()
                .allMatch(balance ->
                        balance.contains("$")
                );
    }

    public boolean containsAccountType(String expectedType) {

        return getAccountTypes()
                .stream()
                .anyMatch(type ->
                        type.equalsIgnoreCase(
                                expectedType
                        )
                );
    }
}