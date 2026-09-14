package com.bankingsystem.qa.pages;

import com.bankingsystem.qa.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By loginContainer = By.id("login");

    private final By customerButton =
            By.cssSelector("#login button.enter[data-role='customer']");

    private final By adminButton =
            By.cssSelector("#login button.enter[data-role='admin']");

    private final By appContainer = By.id("app");

    private final By loggedInUser = By.id("who");

    private final By logoutButton = By.id("logout");

    private final By adminNavigation = By.id("adminNav");

    public LoginPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    private String getBaseUrl() {

        try {

            String value = ConfigReader.get("baseUrl");

            if (value != null && !value.isBlank()) {
                return value;
            }

        } catch (Exception ignored) {
        }

        return "https://novabank-banking-system.vercel.app";
    }

    public LoginPage open() {

        driver.get(getBaseUrl());

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        loginContainer
                )
        );

        return this;
    }

    public boolean isLoaded() {

        try {

            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            loginContainer
                    )
            ).isDisplayed();

        } catch (Exception exception) {

            return false;
        }
    }

    public boolean isCustomerButtonDisplayed() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        customerButton
                )
        ).isDisplayed();
    }

    public boolean isAdminButtonDisplayed() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        adminButton
                )
        ).isDisplayed();
    }

    public void enterAsCustomer() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        customerButton
                )
        ).click();

        waitForApplication();
    }

    public void enterAsAdmin() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        adminButton
                )
        ).click();

        waitForApplication();
    }

    private void waitForApplication() {

        wait.until(driver -> {

            String classes =
                    driver.findElement(appContainer)
                            .getAttribute("class");

            return classes == null ||
                    !classes.contains("hidden");
        });

        wait.until(driver -> {

            String text =
                    driver.findElement(loggedInUser)
                            .getText();

            return text != null &&
                    !text.isBlank();
        });
    }

    public boolean isApplicationDisplayed() {

        try {

            return driver.findElement(appContainer)
                    .isDisplayed();

        } catch (Exception exception) {

            return false;
        }
    }

    public String getLoggedInUserText() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        loggedInUser
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

    public void logout() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        logoutButton
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        loginContainer
                )
        );
    }
}
