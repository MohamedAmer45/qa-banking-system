package com.bankingsystem.qa.bdd.pages;

import com.bankingsystem.qa.bdd.config.ConfigReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public final class LoginPage extends BasePage {

    private final By loginContainer = By.id("login");

    private final By customerButton =
            By.cssSelector(
                    "#login button.enter[data-role='customer']"
            );

    private final By adminButton =
            By.cssSelector(
                    "#login button.enter[data-role='admin']"
            );

    private final By appContainer = By.id("app");
    private final By loggedInUser = By.id("who");
    private final By logoutButton = By.id("logout");
    private final By adminNavigation = By.id("adminNav");

    public LoginPage(WebDriver driver) {

        super(driver);
    }

    public LoginPage open() {

        driver.get(ConfigReader.get("base.url"));
        wait.waitForVisible(loginContainer);

        return this;
    }

    public boolean isLoaded() {

        return isDisplayed(loginContainer);
    }

    public boolean isCustomerOptionAvailable() {

        return isDisplayed(customerButton);
    }

    public boolean isAdminOptionAvailable() {

        return isDisplayed(adminButton);
    }

    public void enterAsCustomer() {

        click(customerButton);
        waitForApplication();
    }

    public void enterAsAdmin() {

        click(adminButton);
        waitForApplication();
    }

    public boolean isApplicationDisplayed() {

        List<WebElement> elements =
                driver.findElements(appContainer);

        return !elements.isEmpty() &&
                elements.getFirst().isDisplayed();
    }

    public String getLoggedInUserText() {

        return getText(loggedInUser);
    }

    public boolean isAdminNavigationDisplayed() {

        List<WebElement> elements =
                driver.findElements(adminNavigation);

        return !elements.isEmpty() &&
                elements.getFirst().isDisplayed();
    }

    public void logout() {

        click(logoutButton);
        wait.waitForVisible(loginContainer);
    }

    private void waitForApplication() {

        wait.waitForVisible(appContainer);
        wait.waitForVisible(loggedInUser);
    }
}