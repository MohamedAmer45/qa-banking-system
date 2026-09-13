package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class TransactionsPage extends BasePage {

    private final By pageTitle =
            By.id("page-title");

    private final By accountSelect =
            By.xpath(
                    "//h2[normalize-space()='Transaction history']" +
                    "/ancestor::div[contains(@class,'section-head')][1]" +
                    "//select"
            );

    private final By referenceFilter =
            By.id("tx-ref");

    private final By typeFilter =
            By.id("tx-type");

    private final By statusFilter =
            By.id("tx-status");

    private final By fromDateFilter =
            By.id("tx-from");

    private final By toDateFilter =
            By.id("tx-to");

    private final By minimumAmountFilter =
            By.id("tx-min");

    private final By maximumAmountFilter =
            By.id("tx-max");

    private final By applyButton =
            By.xpath(
                    "//button[normalize-space()='Apply']"
            );

    private final By clearButton =
            By.xpath(
                    "//button[normalize-space()='Clear']"
            );

    private final By transactionRows =
            By.cssSelector(
                    "#view .table tbody tr"
            );

    private final By emptyState =
            By.xpath(
                    "//*[@id='view']//*[contains(@class,'empty') " +
                    "and contains(normalize-space(.),'No transactions match')]"
            );

    public TransactionsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        try {

            wait.waitForText(
                    pageTitle,
                    "Transactions"
            );

            wait.waitForVisible(
                    accountSelect
            );

            wait.waitForVisible(
                    referenceFilter
            );

            wait.waitForVisible(
                    applyButton
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

    public List<String> getTransactionTypeOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                typeFilter
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getTransactionStatusOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                statusFilter
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public boolean isReferenceFilterDisplayed() {

        return isDisplayed(
                referenceFilter
        );
    }

    public boolean isFromDateFilterDisplayed() {

        return isDisplayed(
                fromDateFilter
        );
    }

    public boolean isToDateFilterDisplayed() {

        return isDisplayed(
                toDateFilter
        );
    }

    public boolean isMinimumAmountFilterDisplayed() {

        return isDisplayed(
                minimumAmountFilter
        );
    }

    public boolean isMaximumAmountFilterDisplayed() {

        return isDisplayed(
                maximumAmountFilter
        );
    }

    public String getFromDateType() {

        return getAttribute(
                fromDateFilter,
                "type"
        );
    }

    public String getToDateType() {

        return getAttribute(
                toDateFilter,
                "type"
        );
    }

    public String getMinimumAmountType() {

        return getAttribute(
                minimumAmountFilter,
                "type"
        );
    }

    public String getMaximumAmountType() {

        return getAttribute(
                maximumAmountFilter,
                "type"
        );
    }

    public String getMinimumAmountStep() {

        return getAttribute(
                minimumAmountFilter,
                "step"
        );
    }

    public String getMaximumAmountStep() {

        return getAttribute(
                maximumAmountFilter,
                "step"
        );
    }

    public TransactionsPage enterReference(
            String reference
    ) {

        clear(
                referenceFilter
        );

        type(
                referenceFilter,
                reference
        );

        return this;
    }

    public String getReferenceValue() {

        return getAttribute(
                referenceFilter,
                "value"
        );
    }

    public TransactionsPage clearFilters() {

        /*
         * NovaBank re-renders the Transactions module
         * asynchronously after Clear is clicked.
         *
         * Keep a reference to the old field and wait until
         * that DOM element becomes stale before reading the
         * newly rendered filter controls.
         */
        WebElement oldReferenceField =
                driver.findElement(
                        referenceFilter
                );

        click(
                clearButton
        );

        wait.waitForStaleness(
                oldReferenceField
        );

        wait.waitForVisible(
                referenceFilter
        );

        return this;
    }

    public int getTransactionRowCount() {

        return driver.findElements(
                transactionRows
        ).size();
    }

    public boolean hasTransactionTableOrEmptyState() {

        return getTransactionRowCount() > 0
                || driver.findElements(
                        emptyState
                ).size() > 0;
    }
}

