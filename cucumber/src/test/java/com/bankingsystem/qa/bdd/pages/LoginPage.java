package com.bankingsystem.qa.bdd.pages;

import com.bankingsystem.qa.bdd.config.ConfigReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
}