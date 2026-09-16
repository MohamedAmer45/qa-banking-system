package com.bankingsystem.qa.bdd.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class CustomerNavigationPage extends BasePage {

    private final By accountsNavigation =
            By.cssSelector(
                    "#nav button[data-s='accounts']"
            );

    private final By accountsSection =
            By.cssSelector(
                    "#accounts.section.on"
            );

    public CustomerNavigationPage(WebDriver driver) {
        super(driver);
    }

    public AccountsCurrentPage openAccounts() {

        click(accountsNavigation);
        wait.waitForVisible(accountsSection);

        return new AccountsCurrentPage(driver);
    }
}