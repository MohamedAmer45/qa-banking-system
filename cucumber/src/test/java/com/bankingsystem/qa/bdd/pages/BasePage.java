package com.bankingsystem.qa.bdd.pages;

import com.bankingsystem.qa.bdd.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils wait;

    protected BasePage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    protected void click(By locator) {

        wait.waitForClickable(locator).click();
    }

    protected void type(By locator, String value) {

        WebElement element = wait.waitForVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String getText(By locator) {

        return wait.waitForVisible(locator).getText();
    }

    protected boolean isDisplayed(By locator) {

        try {
            return wait.waitForVisible(locator).isDisplayed();
        } catch (WebDriverException exception) {
            return false;
        }
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public String getPageTitle() {

        return driver.getTitle();
    }
}