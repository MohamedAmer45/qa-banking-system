package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class TransfersPage extends BasePage {

    private final By pageTitle =
            By.id("page-title");

    private final By newTransferButton =
            By.xpath(
                    "//button[contains(normalize-space(.),'New transfer')]"
            );

    private final By transferHistoryHeading =
            By.xpath(
                    "//h3[normalize-space()='Transfer history']"
            );

    private final By transferHistoryRows =
            By.xpath(
                    "//h3[normalize-space()='Transfer history']" +
                    "/ancestor::div[contains(@class,'card')][1]" +
                    "//tbody/tr"
            );

    private final By transferForm =
            By.id("transfer-form");

    private final By fromAccountSelect =
            By.cssSelector(
                    "#transfer-form select[name='fromAccountId']"
            );

    private final By destinationTypeSelect =
            By.id("dest-type");

    private final By beneficiaryField =
            By.id("beneficiary-field");

    private final By beneficiarySelect =
            By.cssSelector(
                    "#beneficiary-field select[name='beneficiaryId']"
            );

    private final By ownAccountField =
            By.id("own-field");

    private final By ownAccountSelect =
            By.cssSelector(
                    "#own-field select[name='toOwnAccountId']"
            );

    private final By amountInput =
            By.cssSelector(
                    "#transfer-form input[name='amount']"
            );

    private final By scheduleInput =
            By.cssSelector(
                    "#transfer-form input[name='scheduleFor']"
            );

    private final By memoInput =
            By.cssSelector(
                    "#transfer-form input[name='memo']"
            );

    private final By qaSimulationSelect =
            By.cssSelector(
                    "#transfer-form select[name='qaSimulation']"
            );

    private final By submitButton =
            By.xpath(
                    "//form[@id='transfer-form']//button[" +
                    "contains(normalize-space(.),'Review & submit')" +
                    "]"
            );

    private final By modalCloseButton =
            By.cssSelector(
                    "#modal-root .close"
            );

    public TransfersPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        try {

            wait.waitForText(
                    pageTitle,
                    "Transfers"
            );

            wait.waitForVisible(
                    newTransferButton
            );

            wait.waitForVisible(
                    transferHistoryHeading
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

    public boolean isTransferHistoryDisplayed() {

        return isDisplayed(
                transferHistoryHeading
        );
    }

    public int getTransferHistoryRowCount() {

        return driver.findElements(
                transferHistoryRows
        ).size();
    }

    public TransfersPage openNewTransferModal() {

        click(
                newTransferButton
        );

        wait.waitForVisible(
                transferForm
        );

        return this;
    }

    public boolean isTransferFormDisplayed() {

        return isDisplayed(
                transferForm
        );
    }

    public boolean isAmountFieldDisplayed() {

        return isDisplayed(
                amountInput
        );
    }

    public boolean isScheduleFieldDisplayed() {

        return isDisplayed(
                scheduleInput
        );
    }

    public boolean isMemoFieldDisplayed() {

        return isDisplayed(
                memoInput
        );
    }

    public boolean isSubmitButtonDisplayed() {

        return isDisplayed(
                submitButton
        );
    }

    public List<String> getSourceAccountOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                fromAccountSelect
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getDestinationTypeOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                destinationTypeSelect
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public TransfersPage selectDestinationType(
            String value
    ) {

        Select select =
                new Select(
                        wait.waitForVisible(
                                destinationTypeSelect
                        )
                );

        select.selectByValue(
                value
        );

        return this;
    }

    public boolean isBeneficiaryFieldDisplayed() {

        return isDisplayed(
                beneficiaryField
        );
    }

    public boolean isOwnAccountFieldDisplayed() {

        return isDisplayed(
                ownAccountField
        );
    }

    public int getBeneficiaryOptionCount() {

        Select select =
                new Select(
                        wait.waitForPresent(
                                beneficiarySelect
                        )
                );

        return select.getOptions()
                .size();
    }

    public int getOwnAccountOptionCount() {

        Select select =
                new Select(
                        wait.waitForPresent(
                                ownAccountSelect
                        )
                );

        return select.getOptions()
                .size();
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

    public String getScheduleFieldType() {

        return getAttribute(
                scheduleInput,
                "type"
        );
    }

    public String getMemoMaxLength() {

        return getAttribute(
                memoInput,
                "maxlength"
        );
    }

    public List<String> getQaSimulationOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                qaSimulationSelect
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public TransfersPage closeModal() {

        click(
                modalCloseButton
        );

        wait.waitForInvisible(
                transferForm
        );

        return this;
    }
}
