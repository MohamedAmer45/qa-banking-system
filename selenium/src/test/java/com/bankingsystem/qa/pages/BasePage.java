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
        // Fail clearly if the shell never rendered, rather than spinning on a
        // condition that can never become true.
        wait.waitForPresent(testId("view"));

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

    /**
     * Type into a field and confirm the value actually landed.
     *
     * sendKeys dispatches real key events to whatever holds focus, and under
     * rapid successive sessions ChromeDriver sometimes delivers none of them:
     * clear() empties the field, the keys go nowhere, and the field stays
     * empty without any exception being raised.
     *
     * That failed silently in the worst possible way here. The sign-in inputs
     * are `required`, so an empty field makes the browser block the submit
     * locally: no request is sent, no toast appears, and the test times out
     * thirty seconds later waiting for a response nobody asked for. The
     * failure pointed at the wait rather than the typing.
     *
     * So the value is read back. Clicking first to take focus, then retrying,
     * fixes almost every occurrence; the JavaScript fallback covers the rest.
     * The fallback only fills the field — the test still clicks the real
     * button and still asserts the real outcome.
     */
    protected void typeInto(String id, String text) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            WebElement field = clickable(id);

            field.click();
            field.clear();
            field.sendKeys(text);

            if (text.equals(field.getAttribute("value"))) {
                return;
            }
        }

        WebElement field = find(id);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];"
                        + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                field, text
        );

        if (!text.equals(field.getAttribute("value"))) {
            throw new IllegalStateException(
                    "Could not type into '" + id + "': the value did not stick after three "
                            + "attempts and a direct assignment. Last value: '"
                            + field.getAttribute("value") + "'"
            );
        }
    }

    /**
     * Click, then confirm the click actually did something.
     *
     * Same class of problem as {@link #typeInto}: ChromeDriver occasionally
     * synthesizes a click that dispatches no event. On a submit button that
     * fails silently — no request is made, the form simply stays as it was —
     * and the test then waits out its full timeout on a response that was
     * never requested. Confirmed by reading performance.getEntriesByType:
     * after such a click, the only request the page had ever made was the
     * previous one.
     *
     * The application is not at fault here; the same flow runs cleanly under a
     * different driver. So this retries the real click and only falls back to
     * dispatching one directly, rather than working around the page.
     *
     * @param settled JavaScript returning true once the click has taken effect
     */
    protected void clickUntilSettled(String id, String settled) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            clickable(id).click();

            if (waitBriefly(settled)) {
                return;
            }
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", find(id));

        if (!waitBriefly(settled)) {
            throw new IllegalStateException(
                    "Clicking '" + id + "' had no effect after three attempts and a "
                            + "dispatched click."
            );
        }
    }

    /** Poll a condition for a few seconds without failing the test on timeout. */
    private boolean waitBriefly(String condition) {
        for (int i = 0; i < 20; i++) {
            Object result = ((JavascriptExecutor) driver).executeScript("return (" + condition + ");");

            if (Boolean.TRUE.equals(result)) {
                return true;
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return false;
    }

    public void openView(String view) {
        dismissModal();
        waitForViewReady();

        clickable("nav-" + view).click();
        waitForViewReady();
    }

    /**
     * Open a view without using the sidebar.
     *
     * Since BUG-UI-002 was fixed the sidebar only offers modules the role may
     * open, so a test for a withheld module has no button to click. This is
     * also the more honest check: it reaches the view the way a bookmark or a
     * hand-edited link would, and asserts the server still refuses it.
     */
    public void openViewDirectly(String view) {
        dismissModal();
        waitForViewReady();

        ((JavascriptExecutor) driver).executeScript("navigate(arguments[0]);", view);
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
