package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class BeneficiariesPage extends BasePage {

    private final By pageTitle =
            By.id("page-title");

    private final By addBeneficiaryButton =
            By.xpath(
                    "//button[contains(normalize-space(.),'Add beneficiary')]"
            );

    private final By searchInput =
            By.id("beneficiary-search");

    private final By beneficiaryTable =
            By.id("beneficiary-table");

    private final By beneficiaryRows =
            By.cssSelector(
                    "#beneficiary-table tbody tr"
            );

    private final By beneficiaryForm =
            By.id("ben-form");

    private final By nameInput =
            By.cssSelector(
                    "#ben-form input[name='name']"
            );

    private final By nicknameInput =
            By.cssSelector(
                    "#ben-form input[name='nickname']"
            );

    private final By bankSelect =
            By.cssSelector(
                    "#ben-form select[name='bankName']"
            );

    private final By currencySelect =
            By.cssSelector(
                    "#ben-form select[name='currency']"
            );

    private final By accountIdentifierInput =
            By.cssSelector(
                    "#ben-form input[name='accountIdentifier']"
            );

    private final By modalCloseButton =
            By.cssSelector(
                    "#modal-root .close"
            );

    public BeneficiariesPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        try {

            wait.waitForText(
                    pageTitle,
                    "Beneficiaries"
            );

            wait.waitForVisible(
                    addBeneficiaryButton
            );

            wait.waitForVisible(
                    searchInput
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

    public BeneficiariesPage openAddBeneficiaryModal() {

        click(
                addBeneficiaryButton
        );

        wait.waitForVisible(
                beneficiaryForm
        );

        return this;
    }

    public boolean isAddBeneficiaryModalDisplayed() {

        return isDisplayed(
                beneficiaryForm
        );
    }

    public boolean isNameFieldDisplayed() {

        return isDisplayed(
                nameInput
        );
    }

    public boolean isNicknameFieldDisplayed() {

        return isDisplayed(
                nicknameInput
        );
    }

    public boolean isAccountIdentifierFieldDisplayed() {

        return isDisplayed(
                accountIdentifierInput
        );
    }

    public List<String> getBankOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                bankSelect
                        )
                );

        return select
                .getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getCurrencyOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                currencySelect
                        )
                );

        return select
                .getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public BeneficiariesPage enterSearch(
            String value
    ) {

        type(
                searchInput,
                value
        );

        return this;
    }

    public String getSearchValue() {

        return getAttribute(
                searchInput,
                "value"
        );
    }

    public boolean isBeneficiaryTableDisplayed() {

        return driver.findElements(
                beneficiaryTable
        ).size() > 0;
    }

    public int getBeneficiaryRowCount() {

        if (!isBeneficiaryTableDisplayed()) {
            return 0;
        }

        return driver.findElements(
                beneficiaryRows
        ).size();
    }

    public BeneficiariesPage closeModal() {

        click(
                modalCloseButton
        );

        wait.waitForInvisible(
                beneficiaryForm
        );

        return this;
    }
}
