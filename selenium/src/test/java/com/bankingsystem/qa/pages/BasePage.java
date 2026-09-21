package com.bankingsystem.qa.pages;

import com.bankingsystem.qa.utils.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Shared behaviour for every page object.
 *
 * Elements resolve through the {@code data-testid} attributes the application
 * exposes for automation, rather than layout classes or visible copy, so a
 * styling or wording change does not break the suite.
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
     * Wait for a view render to finish.
     *
     * The application blanks the view to a placeholder and then awaits its API
     * calls, so the heading updates before the body exists. Without this, a
     * click issued immediately after sign-in races the initial render and the
     * late-arriving view overwrites the one that was requested.
     */
    public void waitForViewReady() {
        wait.waitForJavaScriptCondition(
                "const v = document.querySelector(\"[data-testid='view']\");"
                        + "return v && !v.textContent.includes('Loading…');"
        );
    }

    /**
     * Dismiss an open modal. A rejected submission leaves its modal up, where
     * it would swallow the next navigation click.
     */
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

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    public boolean hasNavItem(String view) {
        try {
            return !driver.findElements(testId("nav-" + view)).isEmpty();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
