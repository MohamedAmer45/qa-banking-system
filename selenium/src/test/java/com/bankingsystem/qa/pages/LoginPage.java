package com.bankingsystem.qa.pages;

import com.bankingsystem.qa.utils.ConfigReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By identifierInput =
            By.cssSelector(
                    "#login-form input[name='email']"
            );

    private final By passwordInput =
            By.cssSelector(
                    "#login-form input[name='password']"
            );

    private final By loginButton =
            By.cssSelector(
                    "#login-form button[type='submit']"
            );

    private final By errorMessage =
            By.cssSelector(
                    "#toast-root .toast.error"
            );

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {

        String baseUrl =
                ConfigReader.get("base.url");

        String loginPath =
                ConfigReader.get("login.path");

        driver.get(
                normalizeUrl(
                        baseUrl,
                        loginPath
                )
        );

        return this;
    }

    public LoginPage enterIdentifier(
            String identifier
    ) {

        type(
                identifierInput,
                identifier
        );

        return this;
    }

    public LoginPage enterPassword(
            String password
    ) {

        type(
                passwordInput,
                password
        );

        return this;
    }

    public LoginPage clickLogin() {

        click(loginButton);

        return this;
    }

    public LoginPage login(
            String identifier,
            String password
    ) {

        enterIdentifier(identifier);
        enterPassword(password);
        clickLogin();

        return this;
    }

    public boolean isIdentifierFieldDisplayed() {

        return isDisplayed(identifierInput);
    }

    public boolean isPasswordFieldDisplayed() {

        return isDisplayed(passwordInput);
    }

    public boolean isLoginButtonDisplayed() {

        return isDisplayed(loginButton);
    }

    public boolean isLoaded() {

        return isIdentifierFieldDisplayed()
                && isPasswordFieldDisplayed()
                && isLoginButtonDisplayed();
    }

    public String getPasswordFieldType() {

        return getAttribute(
                passwordInput,
                "type"
        );
    }

    public boolean isErrorMessageDisplayed() {

        return isDisplayed(errorMessage);
    }

    public String getErrorMessage() {

        return getText(errorMessage);
    }

    public String getIdentifierValue() {

        return getAttribute(
                identifierInput,
                "value"
        );
    }

    public String getPasswordValue() {

        return getAttribute(
                passwordInput,
                "value"
        );
    }

    public void clearIdentifier() {

        clear(identifierInput);
    }

    public void clearPassword() {

        clear(passwordInput);
    }

    private String normalizeUrl(
            String baseUrl,
            String path
    ) {

        String normalizedBase =
                baseUrl.endsWith("/")
                        ? baseUrl.substring(
                                0,
                                baseUrl.length() - 1
                        )
                        : baseUrl;

        String normalizedPath =
                path.startsWith("/")
                        ? path
                        : "/" + path;

        return normalizedBase
                + normalizedPath;
    }
}
