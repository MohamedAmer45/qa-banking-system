package com.bankingsystem.qa.pages;

import com.bankingsystem.qa.utils.ConfigReader;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    /*
     * Locators intentionally support several common locator conventions.
     *
     * Once the Banking System DOM is finalized, these should be replaced
     * with the application's exact stable data-testid selectors.
     */

    private final By identifierInput = By.cssSelector(
            "[data-testid='login-identifier'], " +
            "[data-testid='login-email'], " +
            "#email, " +
            "#username, " +
            "input[name='email'], " +
            "input[name='username']"
    );

    private final By passwordInput = By.cssSelector(
            "[data-testid='login-password'], " +
            "#password, " +
            "input[name='password']"
    );

    private final By loginButton = By.cssSelector(
            "[data-testid='login-submit'], " +
            "button[type='submit']"
    );

    private final By errorMessage = By.cssSelector(
            "[data-testid='login-error'], " +
            "[role='alert'], " +
            ".error-message, " +
            ".alert-danger"
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
                normalizeUrl(baseUrl, loginPath)
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

    public void clickLogin() {

        click(loginButton);
    }

    public void login(
            String identifier,
            String password
    ) {

        enterIdentifier(identifier);
        enterPassword(password);
        clickLogin();
    }

    public boolean isIdentifierFieldDisplayed() {

        return isDisplayed(
                identifierInput
        );
    }

    public boolean isPasswordFieldDisplayed() {

        return isDisplayed(
                passwordInput
        );
    }

    public boolean isLoginButtonDisplayed() {

        return isDisplayed(
                loginButton
        );
    }

    public boolean isLoaded() {

        return isIdentifierFieldDisplayed()
                && isPasswordFieldDisplayed()
                && isLoginButtonDisplayed();
    }

    public String getErrorMessage() {

        return getText(
                errorMessage
        );
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
