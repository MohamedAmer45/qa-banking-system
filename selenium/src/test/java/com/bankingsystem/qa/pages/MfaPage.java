package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MfaPage extends BasePage {

    private final By mfaForm =
            By.id("mfa-form");

    private final By codeInput =
            By.cssSelector(
                    "#mfa-form input[name='code']"
            );

    private final By verifyButton =
            By.cssSelector(
                    "#mfa-form button[type='submit']"
            );

    public MfaPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        return isDisplayed(mfaForm)
                && isDisplayed(codeInput)
                && isDisplayed(verifyButton);
    }

    public MfaPage enterCode(
            String code
    ) {

        clear(codeInput);

        type(
                codeInput,
                code
        );

        return this;
    }

    public MfaPage clickVerify() {

        click(verifyButton);

        return this;
    }

    public MfaPage verify(
            String code
    ) {

        enterCode(code);
        clickVerify();

        return this;
    }
}
