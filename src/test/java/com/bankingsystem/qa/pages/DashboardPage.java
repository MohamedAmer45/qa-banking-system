package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heading =
            By.cssSelector("#dashboard h1");

    private final By loggedInUser =
            By.id("who");

    private final By totalBalance =
            By.id("total");

    private final By accountsNavigation =
            By.cssSelector("#nav button[data-s='accounts']");

    private final By transfersNavigation =
            By.cssSelector("#nav button[data-s='transfers']");

    private final By transactionsNavigation =
            By.cssSelector("#nav button[data-s='transactions']");

    private final By logoutButton =
            By.id("logout");

    private final By adminNavigation =
            By.id("adminNav");

    public DashboardPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    public boolean isLoaded() {

        try {

            String headingText =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    heading
                            )
                    ).getText();

            return "Dashboard".equals(headingText);

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

    public String getLoggedInUserText() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        loggedInUser
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

    public boolean isAdminNavigationDisplayed() {

        try {

            return driver.findElement(adminNavigation)
                    .isDisplayed();

        } catch (Exception exception) {

            return false;
        }
    }

    public void openAccounts() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        accountsNavigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#accounts h1")
                )
        );
    }

    public void openTransfers() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        transfersNavigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#transfers h1")
                )
        );
    }

    public void openTransactions() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        transactionsNavigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("#transactions h1")
                )
        );
    }

    public void logout() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        logoutButton
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("login")
                )
        );
    }
}
