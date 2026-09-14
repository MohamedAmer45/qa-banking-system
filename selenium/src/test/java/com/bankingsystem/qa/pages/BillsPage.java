package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class BillsPage extends BasePage {

    private final By pageTitle =
            By.id("page-title");

    private final By payBillButton =
            By.xpath(
                    "//button[normalize-space()='Pay bill']"
            );

    private final By saveBillerButton =
            By.xpath(
                    "//button[normalize-space()='Save biller']"
            );

    private final By paymentHistoryHeading =
            By.xpath(
                    "//h3[normalize-space()='Payment history']"
            );

    private final By savedBillersHeading =
            By.xpath(
                    "//h3[normalize-space()='Saved billers']"
            );

    private final By paymentHistoryRows =
            By.xpath(
                    "//h3[normalize-space()='Payment history']" +
                    "/ancestor::div[contains(@class,'card')][1]" +
                    "//tbody/tr"
            );

    private final By paymentHistoryEmpty =
            By.xpath(
                    "//h3[normalize-space()='Payment history']" +
                    "/ancestor::div[contains(@class,'card')][1]" +
                    "//*[contains(@class,'empty')]"
            );

    private final By savedBillerItems =
            By.xpath(
                    "//h3[normalize-space()='Saved billers']" +
                    "/ancestor::div[contains(@class,'card')][1]" +
                    "//*[contains(@class,'list-item')]"
            );

    private final By savedBillersEmpty =
            By.xpath(
                    "//h3[normalize-space()='Saved billers']" +
                    "/ancestor::div[contains(@class,'card')][1]" +
                    "//*[contains(@class,'empty')]"
            );


    // ========================================================
    // PAY BILL MODAL
    // ========================================================

    private final By payBillForm =
            By.id("pay-bill");

    private final By payFromAccountSelect =
            By.cssSelector(
                    "#pay-bill select[name='accountId']"
            );

    private final By payBillerSelect =
            By.cssSelector(
                    "#pay-bill select[name='billerId']"
            );

    private final By customerReferenceInput =
            By.cssSelector(
                    "#pay-bill input[name='customerReference']"
            );

    private final By amountInput =
            By.cssSelector(
                    "#pay-bill input[name='amount']"
            );

    private final By scheduleInput =
            By.cssSelector(
                    "#pay-bill input[name='scheduleFor']"
            );

    private final By recurringSelect =
            By.cssSelector(
                    "#pay-bill select[name='recurringFrequency']"
            );

    private final By submitPaymentButton =
            By.xpath(
                    "//form[@id='pay-bill']//button[" +
                    "normalize-space()='Submit payment'" +
                    "]"
            );


    // ========================================================
    // SAVE BILLER MODAL
    // ========================================================

    private final By saveBillerForm =
            By.id("save-biller");

    private final By saveBillerSelect =
            By.cssSelector(
                    "#save-biller select[name='billerId']"
            );

    private final By aliasInput =
            By.cssSelector(
                    "#save-biller input[name='alias']"
            );

    private final By saveCustomerReferenceInput =
            By.cssSelector(
                    "#save-biller input[name='customerReference']"
            );

    private final By saveBillerSubmitButton =
            By.xpath(
                    "//form[@id='save-biller']//button[" +
                    "normalize-space()='Save biller'" +
                    "]"
            );


    private final By modalCloseButton =
            By.cssSelector(
                    "#modal-root .close"
            );


    public BillsPage(WebDriver driver) {
        super(driver);
    }


    public boolean isLoaded() {

        try {

            wait.waitForText(
                    pageTitle,
                    "Bills & payments"
            );

            wait.waitForVisible(
                    payBillButton
            );

            wait.waitForVisible(
                    saveBillerButton
            );

            wait.waitForVisible(
                    paymentHistoryHeading
            );

            wait.waitForVisible(
                    savedBillersHeading
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


    public boolean hasPaymentHistoryOrEmptyState() {

        return driver.findElements(
                paymentHistoryRows
        ).size() > 0
                || driver.findElements(
                        paymentHistoryEmpty
                ).size() > 0;
    }


    public boolean hasSavedBillersOrEmptyState() {

        return driver.findElements(
                savedBillerItems
        ).size() > 0
                || driver.findElements(
                        savedBillersEmpty
                ).size() > 0;
    }


    // ========================================================
    // PAY BILL
    // ========================================================

    public BillsPage openPayBillModal() {

        click(
                payBillButton
        );

        wait.waitForVisible(
                payBillForm
        );

        return this;
    }


    public boolean isPayBillModalDisplayed() {

        return isDisplayed(
                payBillForm
        );
    }


    public List<String> getPayFromAccountOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                payFromAccountSelect
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }


    public List<String> getPayBillerOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                payBillerSelect
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }


    public boolean isCustomerReferenceDisplayed() {

        return isDisplayed(
                customerReferenceInput
        );
    }


    public boolean isAmountDisplayed() {

        return isDisplayed(
                amountInput
        );
    }


    public boolean isScheduleDisplayed() {

        return isDisplayed(
                scheduleInput
        );
    }


    public boolean isRecurringDisplayed() {

        return isDisplayed(
                recurringSelect
        );
    }


    public boolean isSubmitPaymentDisplayed() {

        return isDisplayed(
                submitPaymentButton
        );
    }


    public String getAmountMinimum() {

        return getAttribute(
                amountInput,
                "min"
        );
    }


    public String getAmountStep() {

        return getAttribute(
                amountInput,
                "step"
        );
    }


    public String getScheduleType() {

        return getAttribute(
                scheduleInput,
                "type"
        );
    }


    public List<String> getRecurringOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                recurringSelect
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }


    // ========================================================
    // SAVE BILLER
    // ========================================================

    public BillsPage openSaveBillerModal() {

        click(
                saveBillerButton
        );

        wait.waitForVisible(
                saveBillerForm
        );

        return this;
    }


    public boolean isSaveBillerModalDisplayed() {

        return isDisplayed(
                saveBillerForm
        );
    }


    public List<String> getSaveBillerOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                saveBillerSelect
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }


    public boolean isAliasFieldDisplayed() {

        return isDisplayed(
                aliasInput
        );
    }


    public boolean isSaveCustomerReferenceDisplayed() {

        return isDisplayed(
                saveCustomerReferenceInput
        );
    }


    public boolean isSaveBillerSubmitDisplayed() {

        return isDisplayed(
                saveBillerSubmitButton
        );
    }


    public BillsPage closeModal() {

        click(
                modalCloseButton
        );

        return this;
    }
}
