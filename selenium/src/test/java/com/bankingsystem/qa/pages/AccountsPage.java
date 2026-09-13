package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AccountsPage extends BasePage {

    private final By pageTitle =
            By.id("page-title");

    private final By openAccountButton =
            By.xpath(
                    "//button[contains(normalize-space(.),'Open account')]"
            );

    private final By accountCards =
            By.cssSelector(
                    ".account-card"
            );

    private final By accountTypes =
            By.cssSelector(
                    ".account-card .account-type"
            );

    private final By accountBalances =
            By.cssSelector(
                    ".account-card .balance"
            );

    private final By accountMetadata =
            By.cssSelector(
                    ".account-card .account-meta"
            );

    public AccountsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        try {

            wait.waitForText(
                    pageTitle,
                    "Accounts"
            );

            wait.waitForVisible(
                    openAccountButton
            );

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public String getPageTitleText() {

        return getText(pageTitle);
    }

    public int getAccountCount() {

        wait.waitForPresent(
                accountCards
        );

        return driver.findElements(
                accountCards
        ).size();
    }

    public List<String> getAccountTypes() {

        wait.waitForPresent(
                accountTypes
        );

        return driver.findElements(
                        accountTypes
                )
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getAccountBalances() {

        wait.waitForPresent(
                accountBalances
        );

        return driver.findElements(
                        accountBalances
                )
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getAccountMetadata() {

        wait.waitForPresent(
                accountMetadata
        );

        return driver.findElements(
                        accountMetadata
                )
                .stream()
                .map(WebElement::getText)
                .toList();
    }
}
