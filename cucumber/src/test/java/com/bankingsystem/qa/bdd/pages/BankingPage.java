package com.bankingsystem.qa.bdd.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Customer-facing banking surfaces. One page object rather than one per view:
 * the scenarios read as business workflows that cross modules, and splitting
 * them would scatter a single Gherkin step across several classes.
 */
public class BankingPage extends BasePage {

    public BankingPage(WebDriver driver) {
        super(driver);
    }

    // ---------------------------------------------------------- accounts

    /**
     * Waiting for the view placeholder to clear is not enough on its own: the
     * check can run before the placeholder is even set and pass against the
     * previous view. Waiting for an account card guarantees this view rendered.
     */
    public void openAccounts() {
        openView("accounts");
        find("account-card");
    }

    public int accountCount() {
        return findAll("account-card").size();
    }

    /** The integer minor-unit balance the ledger holds. */
    public long balanceMinor(int index) {
        return Long.parseLong(
                findAll("account-balance").get(index).getAttribute("data-balance-minor")
        );
    }

    // --------------------------------------------------------- transfers

    public void openTransfers() {
        openView("transfers");
        find("new-transfer");
    }

    /** The transfer form is a modal; it does not exist until this is clicked. */
    public void openTransferForm() {
        clickable("new-transfer").click();
        find("transfer-form");
    }

    public int sourceAccountCount() {
        return new Select(find("transfer-from")).getOptions().size();
    }

    public void selectFirstSourceAndBeneficiary() {
        new Select(find("transfer-from")).selectByIndex(0);
        new Select(find("transfer-dest-type")).selectByValue("beneficiary");
        new Select(find("transfer-beneficiary")).selectByIndex(0);
    }

    public void enterAmount(String amount) {
        WebElement field = find("transfer-amount");
        field.clear();
        field.sendKeys(amount);
    }

    /** Submit and wait for the outcome toast, so balances can be re-read. */
    public void submitTransfer() {
        WebElement button = clickable("transfer-submit");
        scrollIntoView(button);
        button.click();
        wait.waitForVisible(testId("toast"));
    }

    public boolean isAmountAccepted(String amount) {
        enterAmount(amount);

        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return document.querySelector(\"[data-testid='transfer-amount']\")"
                        + ".checkValidity();"
        );
    }

    // ------------------------------------------------------- back office

    public void openBackOffice(String view) {
        openView(view);
    }

    public String viewText() {
        return find("view").getText();
    }

    public String customerDirectoryText() {
        return find("customer-table").getText();
    }

    public String auditTrailText() {
        return find("audit-table").getText();
    }
}
