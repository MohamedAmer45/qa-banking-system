package com.bankingsystem.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class TransfersPage extends BasePage {

    public TransfersPage(WebDriver driver) {
        super(driver);
    }

    public TransfersPage open() {
        openView("transfers");
        find("new-transfer");
        return this;
    }

    /** The transfer form is a modal; it does not exist until this is clicked. */
    public TransfersPage openForm() {
        clickable("new-transfer").click();
        find("transfer-form");
        return this;
    }

    public int sourceAccountCount() {
        return new Select(find("transfer-from")).getOptions().size();
    }

    public TransfersPage selectSource(int index) {
        new Select(find("transfer-from")).selectByIndex(index);
        return this;
    }

    public TransfersPage selectBeneficiary(int index) {
        new Select(find("transfer-dest-type")).selectByValue("beneficiary");
        new Select(find("transfer-beneficiary")).selectByIndex(index);
        return this;
    }

    public TransfersPage enterAmount(String amount) {
        WebElement field = find("transfer-amount");
        field.clear();
        field.sendKeys(amount);
        return this;
    }

    /**
     * Submit and wait for the outcome. A success closes the modal and raises a
     * toast; a rejection leaves the modal open and raises a toast. Waiting for
     * the toast covers both, so a caller can re-read balances safely.
     */
    public TransfersPage submit() {
        WebElement button = clickable("transfer-submit");
        scrollIntoView(button);
        button.click();

        wait.waitForVisible(testId("toast"));
        return this;
    }

    /** Whether the browser's own constraint validation rejects the amount. */
    public boolean isAmountValid(String amount) {
        enterAmount(amount);

        return (Boolean) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript(
                        "return document.querySelector(\"[data-testid='transfer-amount']\")"
                                + ".checkValidity();"
                );
    }
}
