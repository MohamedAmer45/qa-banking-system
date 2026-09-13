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

    private final By logoutButton =
            By.xpath(
                    "//button[contains(.,'Sign out')]"
            );

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        return isDisplayed(appShell)
                && isDisplayed(pageTitle)
                && isDisplayed(userChip)
                && isDisplayed(overviewNavigation);
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

    public void logout() {

        click(logoutButton);
    }
}
