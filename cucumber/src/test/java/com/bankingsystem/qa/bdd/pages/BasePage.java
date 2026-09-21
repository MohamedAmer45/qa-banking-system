package com.bankingsystem.qa.bdd.pages;

import com.bankingsystem.qa.bdd.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Shared behaviour for every page object.
 *
 * Elements resolve through the {@code data-testid} attributes the application
 * exposes for automation, so styling and copy changes do not break scenarios.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    protected static By testId(String id) {
        return By.cssSelector("[data-testid='" + id + "']");
    }

    protected WebElement find(String id) {
        return wait.waitForVisible(testId(id));
    }

    protected WebElement clickable(String id) {
        return wait.waitForClickable(testId(id));
    }

    protected List<WebElement> findAll(String id) {
        return driver.findElements(testId(id));
    }

    protected boolean isPresent(String id) {
        return !driver.findElements(testId(id)).isEmpty();
    }

    public String pageTitle() {
        return find("page-title").getText();
    }

    public String toastText() {
        return find("toast").getText();
    }

    /**
     * Wait for a view render to finish. The application updates the heading
     * before its data arrives, so without this a following interaction races
     * the render.
     */
    public void waitForViewReady() {
        // Fail clearly if the shell never rendered, rather than spinning on a
        // condition that can never become true.
        wait.waitForPresent(testId("view"));

        wait.waitForJavaScriptCondition(
                "const v = document.querySelector(\"[data-testid='view']\");"
                        + "return v && !v.textContent.includes('Loading…');"
        );
    }

    /** A rejected submission leaves its modal up, blocking the next click. */
    public void dismissModal() {
        if (isPresent("modal")) {
            clickable("modal-close").click();
            wait.waitForInvisible(testId("modal"));
        }
    }

    public void openView(String view) {
        dismissModal();
        waitForViewReady();
        clickable("nav-" + view).click();
        waitForViewReady();
    }

    public boolean hasNavItem(String view) {
        return !driver.findElements(testId("nav-" + view)).isEmpty();
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }
}
