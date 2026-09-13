package com.bankingsystem.qa.pages;

import org.openqa.selenium.Alert;
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

private final By addBeneficiarySubmit =
        By.xpath(
                "//form[@id='ben-form']//button[" +
                "contains(normalize-space(.),'Add beneficiary')" +
                "]"
        );

private final By verifyForm =
        By.id("verify-ben");

private final By verifyCodeInput =
        By.cssSelector(
                "#verify-ben input[name='code']"
        );

private final By verifySubmitButton =
        By.xpath(
                "//form[@id='verify-ben']//button[" +
                "contains(normalize-space(.),'Verify')" +
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

    wait.waitForJavaScriptCondition(
            "return document.querySelector('#ben-form') !== null " +
            "&& typeof document.querySelector('#ben-form').onsubmit === 'function';"
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

public BeneficiariesPage createBeneficiary(
        String name,
        String nickname,
        String bank,
        String currency,
        String accountIdentifier
) {

    openAddBeneficiaryModal();

    type(
            nameInput,
            name
    );

    type(
            nicknameInput,
            nickname
    );

    new Select(
            wait.waitForVisible(
                    bankSelect
            )
    ).selectByVisibleText(
            bank
    );

    new Select(
            wait.waitForVisible(
                    currencySelect
            )
    ).selectByVisibleText(
            currency
    );

    type(
            accountIdentifierInput,
            accountIdentifier
    );

    click(
            addBeneficiarySubmit
    );

    wait.waitForInvisible(
            beneficiaryForm
    );

    return this;
}

public boolean beneficiaryExists(
        String name
) {

    By row =
            beneficiaryRow(
                    name
            );

    try {

        return wait.waitForVisible(
                row
        ).isDisplayed();

    } catch (Exception e) {

        return false;
    }
}

public BeneficiariesPage openVerification(
        String name
) {

    By verifyButton =
            By.xpath(
                    beneficiaryRowXPath(name) +
                    "//button[contains(normalize-space(.),'Verify OTP')]"
            );

    click(
            verifyButton
    );

    wait.waitForVisible(
            verifyForm
    );

    wait.waitForJavaScriptCondition(
            "return document.querySelector('#verify-ben') !== null " +
            "&& typeof document.querySelector('#verify-ben').onsubmit === 'function';"
    );

    return this;
}

public BeneficiariesPage verifyBeneficiary(
        String name,
        String otp
) {

    openVerification(
            name
    );

    clear(
            verifyCodeInput
    );

    type(
            verifyCodeInput,
            otp
    );

    click(
            verifySubmitButton
    );

    wait.waitForInvisible(
            verifyForm
    );

    return this;
}

public boolean isBeneficiaryVerified(
        String name
) {

    By verifiedBadge =
            By.xpath(
                    beneficiaryRowXPath(name) +
                    "//span[contains(@class,'badge') " +
                    "and contains(normalize-space(.),'VERIFIED')]"
            );

    try {

        return wait.waitForVisible(
                verifiedBadge
        ).isDisplayed();

    } catch (Exception e) {

        return false;
    }
}

    public BeneficiariesPage deleteBeneficiary(
            String name
    ) {

        By deleteButton =
                By.xpath(
                        beneficiaryRowXPath(name) +
                        "//button[contains(normalize-space(.),'Delete')]"
                );

        click(
                deleteButton
        );

        Alert alert =
                driver.switchTo()
                        .alert();

        alert.accept();

        /*
         * NovaBank confirms successful deletion using:
         * toast('Beneficiary deleted')
         *
         * Do not wait on the previous table row because the
         * beneficiary table is re-rendered asynchronously.
         */
        wait.waitForText(
                toast,
                "Beneficiary deleted"
        );

        return this;
    }


public String getToastMessage() {

    return getText(
            toast
    );
}

public BeneficiariesPage closeModal() {

    click(
            modalCloseButton
    );

    return this;
}

private By beneficiaryRow(
        String name
) {

    return By.xpath(
            beneficiaryRowXPath(
                    name
            )
    );
}

private String beneficiaryRowXPath(
        String name
) {

    return "//table[@id='beneficiary-table']//tbody/tr[" +
            ".//td//*[normalize-space()=" +
            xpathLiteral(name) +
            "]" +
            "]";
}

private String xpathLiteral(
        String value
) {

    if (!value.contains("'")) {
        return "'" + value + "'";
    }

    if (!value.contains("\"")) {
        return "\"" + value + "\"";
    }

    throw new IllegalArgumentException(
            "Value contains unsupported quote combination."
    );
}

}


