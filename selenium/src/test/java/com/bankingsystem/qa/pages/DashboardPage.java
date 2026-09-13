package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    private final By appShell =
            By.cssSelector(".app-shell");

    private final By pageTitle =
            By.id("page-title");

    private final By userChip =
            By.cssSelector(".user-chip");

    private final By overviewNavigation =
            By.cssSelector(
                    "[data-view='overview']"
            );

    private final By accountsNavigation =
            By.cssSelector(
                    "[data-view='accounts']"
            );

    private final By overviewContent =
            By.cssSelector(
                    "#view .hero-card"
            );

    private final By logoutButton =
            By.xpath(
                    "//button[contains(.,'Sign out')]"
            );

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        try {

            wait.waitForVisible(appShell);

            wait.waitForVisible(userChip);

            wait.waitForClickable(
                    overviewNavigation
            );

            wait.waitForText(
                    pageTitle,
                    "Overview"
            );

            /*
             * Important:
             * Do not consider the dashboard fully loaded
             * until the async Overview content has finished
             * rendering.
             */
            wait.waitForVisible(
                    overviewContent
            );

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public String getPageTitleText() {

        return getText(pageTitle);
    }

    public String getLoggedInUserText() {

        return getText(userChip);
    }

    public boolean isLogoutButtonDisplayed() {

        return isDisplayed(logoutButton);
    }

    public AccountsPage openAccounts() {

        wait.waitForClickable(
                accountsNavigation
        );

        click(
                accountsNavigation
        );

        return new AccountsPage(
                driver
        );
    }

    public void logout() {

        click(logoutButton);
    }
}
