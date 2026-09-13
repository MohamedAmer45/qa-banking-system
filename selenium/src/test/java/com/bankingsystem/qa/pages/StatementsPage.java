package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class StatementsPage extends BasePage {

    private final By pageTitle =
            By.id("page-title");

    private final By accountSelect =
            By.xpath(
                    "//h2[normalize-space()='Statements']" +
                    "/ancestor::div[contains(@class,'section-head')][1]" +
                    "/following-sibling::div[contains(@class,'card')][1]" +
                    "//select"
            );

    private final By fromDateInput =
            By.id("statement-from");

    private final By toDateInput =
            By.id("statement-to");

    private final By applyButton =
            By.xpath(
                    "//input[@id='statement-to']" +
                    "/following-sibling::button[normalize-space()='Apply']"
            );

    private final By downloadCsvButton =
            By.xpath(
                    "//button[normalize-space()='Download CSV']"
            );

    private final By printPdfButton =
            By.xpath(
                    "//button[normalize-space()='Print / PDF']"
            );

    private final By statementBody =
            By.id("statement-body");

    private final By summaryLabels =
            By.cssSelector(
                    "#statement-body .statement-summary .mini label"
            );

    private final By summaryValues =
            By.cssSelector(
                    "#statement-body .statement-summary .mini strong"
            );

    private final By transactionRows =
            By.cssSelector(
                    "#statement-body .table tbody tr"
            );

    private final By emptyState =
            By.cssSelector(
                    "#statement-body .empty"
            );

    public StatementsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        try {

            wait.waitForText(
                    pageTitle,
                    "Statements"
            );

            wait.waitForVisible(
                    accountSelect
            );

            wait.waitForVisible(
                    fromDateInput
            );

            wait.waitForVisible(
                    toDateInput
            );

            wait.waitForVisible(
                    statementBody
            );

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public String getPageTitleText() {

        return getText(
                pageTitle
        );
    }

    public List<String> getAccountOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                accountSelect
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public String getFromDateType() {

        return getAttribute(
                fromDateInput,
                "type"
        );
    }

    public String getToDateType() {

        return getAttribute(
                toDateInput,
                "type"
        );
    }

    public String getFromDateValue() {

        return getAttribute(
                fromDateInput,
                "value"
        );
    }

    public String getToDateValue() {

        return getAttribute(
                toDateInput,
                "value"
        );
    }

    public StatementsPage enterFromDate(
            String value
    ) {

        setDateValue(
                fromDateInput,
                value
        );

        return this;
    }

    public StatementsPage enterToDate(
            String value
    ) {

        setDateValue(
                toDateInput,
                value
        );

        return this;
    }

    public boolean isApplyButtonDisplayed() {

        return isDisplayed(
                applyButton
        );
    }

    public boolean isDownloadCsvDisplayed() {

        return isDisplayed(
                downloadCsvButton
        );
    }

    public boolean isPrintPdfDisplayed() {

        return isDisplayed(
                printPdfButton
        );
    }

    public List<String> getSummaryLabels() {

        wait.waitForPresent(
                summaryLabels
        );

        return driver.findElements(
                        summaryLabels
                )
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getSummaryValues() {

        wait.waitForPresent(
                summaryValues
        );

        return driver.findElements(
                        summaryValues
                )
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getTransactionRowCount() {

        return driver.findElements(
                transactionRows
        ).size();
    }

    public boolean hasTransactionsOrEmptyState() {

        return getTransactionRowCount() > 0
                || driver.findElements(
                        emptyState
                ).size() > 0;
    }

    private void setDateValue(
            By locator,
            String value
    ) {

        WebElement element =
                wait.waitForVisible(
                        locator
                );

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                "arguments[0].value = arguments[1];" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                element,
                value
        );
    }
}

