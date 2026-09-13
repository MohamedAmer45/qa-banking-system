package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class AccountsPage extends BasePage {

    private final By pageTitle =
            By.id("page-title");

    private final By openAccountButton =
            By.xpath(
                    "//button[contains(normalize-space(.),'Open account')]"
            );

    private final By accountCards =
            By.cssSelector(
                    ".account-card"
            );

    private final By accountTypes =
            By.cssSelector(
                    ".account-card .account-type"
            );

    private final By accountBalances =
            By.cssSelector(
                    ".account-card .balance"
            );

    private final By accountMetadata =
            By.cssSelector(
                    ".account-card .account-meta"
            );

    private final By accountControlRows =
            By.cssSelector(
                    ".table tbody tr"
            );

    private final By accountStatusBadges =
            By.cssSelector(
                    ".table tbody tr td .badge"
            );

    private final By dailyLimitCells =
            By.cssSelector(
                    ".table tbody tr td:nth-child(5)"
            );

    private final By activeAccountRows =
            By.xpath(
                    "//table[contains(@class,'table')]//tbody/tr[" +
                    ".//span[contains(@class,'badge') and " +
                    "contains(normalize-space(.),'ACTIVE')]" +
                    "]"
            );

    private final By freezeButtons =
            By.xpath(
                    "//table[contains(@class,'table')]//tbody/tr[" +
                    ".//span[contains(@class,'badge') and " +
                    "contains(normalize-space(.),'ACTIVE')]" +
                    "]//button[contains(normalize-space(.),'Freeze')]"
            );

    private final By accountForm =
            By.id("account-form");

    private final By accountTypeSelect =
            By.cssSelector(
                    "#account-form select[name='accountType']"
            );

    private final By currencySelect =
            By.cssSelector(
                    "#account-form select[name='currency']"
            );

    private final By createAccountButton =
            By.xpath(
                    "//form[@id='account-form']//button[" +
                    "contains(normalize-space(.),'Open account')" +
                    "]"
            );

    private final By modalCloseButton =
            By.cssSelector(
                    "#modal-root .close"
            );

    private final By toast =
            By.cssSelector(
                    "#toast-root .toast"
            );

    public AccountsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        try {

            wait.waitForText(
                    pageTitle,
                    "Accounts"
            );

            wait.waitForVisible(
                    openAccountButton
            );

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public String getPageTitleText() {

        return getText(pageTitle);
    }

    public int getAccountCount() {

        wait.waitForPresent(
                accountCards
        );

        return driver.findElements(
                accountCards
        ).size();
    }

    public int getAccountControlRowCount() {

        wait.waitForPresent(
                accountControlRows
        );

        return driver.findElements(
                accountControlRows
        ).size();
    }

    public List<String> getAccountTypes() {

        return getTexts(
                accountTypes
        );
    }

    public List<String> getAccountBalances() {

        return getTexts(
                accountBalances
        );
    }

    public List<String> getAccountMetadata() {

        return getTexts(
                accountMetadata
        );
    }

    public List<String> getAccountStatuses() {

        return getTexts(
                accountStatusBadges
        );
    }

    public List<String> getDailyLimits() {

        return getTexts(
                dailyLimitCells
        );
    }

    public int getActiveAccountCount() {

        return driver.findElements(
                activeAccountRows
        ).size();
    }

    public int getFreezeButtonCount() {

        return driver.findElements(
                freezeButtons
        ).size();
    }

    public AccountsPage openAccountModal() {

        click(
                openAccountButton
        );

        wait.waitForVisible(
                accountForm
        );

        return this;
    }

    public boolean isAccountModalDisplayed() {

        return isDisplayed(
                accountForm
        );
    }

    public List<String> getAccountTypeOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                accountTypeSelect
                        )
                );

        return select.getOptions()
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

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public AccountsPage selectAccountType(
            String accountType
    ) {

        Select select =
                new Select(
                        wait.waitForVisible(
                                accountTypeSelect
                        )
                );

        select.selectByVisibleText(
                accountType
        );

        return this;
    }

    public AccountsPage selectCurrency(
            String currency
    ) {

        Select select =
                new Select(
                        wait.waitForVisible(
                                currencySelect
                        )
                );

        select.selectByVisibleText(
                currency
        );

        return this;
    }

    public AccountsPage submitAccountCreation() {

        click(
                createAccountButton
        );

        return this;
    }

    public AccountsPage createAccount(
            String accountType,
            String currency
    ) {

        openAccountModal();
        selectAccountType(accountType);
        selectCurrency(currency);
        submitAccountCreation();

        return this;
    }

    public int waitForAccountCountToIncrease(
            int previousCount
    ) {

        return wait.waitForMoreThan(
                accountCards,
                previousCount
        ).size();
    }

    public String getToastMessage() {

        return getText(
                toast
        );
    }

    public AccountsPage closeAccountModal() {

        click(
                modalCloseButton
        );

        wait.waitForInvisible(
                accountForm
        );

        return this;
    }

    private List<String> getTexts(
            By locator
    ) {

        wait.waitForPresent(
                locator
        );

        return driver.findElements(
                        locator
                )
                .stream()
                .map(WebElement::getText)
                .toList();
    }
}
