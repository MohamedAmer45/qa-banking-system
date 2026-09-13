package com.bankingsystem.qa.pages;

import com.bankingsystem.qa.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils wait;

    protected BasePage(WebDriver driver) {

        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    protected void click(By locator) {

        wait.waitForClickable(locator)
                .click();
    }

    protected void type(
            By locator,
            String text
    ) {

        WebElement element =
                wait.waitForVisible(locator);

        element.clear();

        element.sendKeys(text);
    }

    protected String getText(
            By locator
    ) {

        return wait.waitForVisible(locator)
                .getText();
    }

    protected String getAttribute(
            By locator,
            String attribute
    ) {

        return wait.waitForPresent(locator)
                .getAttribute(attribute);
    }

    protected boolean isDisplayed(
            By locator
    ) {

        try {

            return wait.waitForVisible(locator)
                    .isDisplayed();

        } catch (Exception e) {

            return false;
        }
    }

    protected WebElement find(
            By locator
    ) {

        return wait.waitForPresent(locator);
    }

    protected void clear(
            By locator
    ) {

        wait.waitForVisible(locator)
                .clear();
    }

    public String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    public String getPageTitle() {

        return driver.getTitle();
    }
}
