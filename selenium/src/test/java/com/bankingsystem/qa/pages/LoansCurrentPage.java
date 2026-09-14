package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoansCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By section =
            By.cssSelector("#loans.section.on");

    private final By heading =
            By.cssSelector("#loans h1");

    private final By loanInfo =
            By.id("loanInfo");

    private final By loanType =
            By.cssSelector("#loanInfo h2");

    private final By loanBalance =
            By.cssSelector("#loanInfo .metric");

    private final By loanDetails =
            By.cssSelector("#loanInfo .muted");

    private final By amount =
            By.id("loanAmount");

    private final By term =
            By.id("term");

    private final By submitButton =
            By.cssSelector("#loanForm button");

    private final By toast =
            By.id("toast");

    public LoansCurrentPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    public boolean isLoaded() {

        try {

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            section
                    )
            );

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            loanInfo
                    )
            );

            return "Loans".equals(
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    heading
                            )
                    ).getText()
            );

        } catch (Exception exception) {

            return false;
        }
    }

    public String getPageTitleText() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        heading
                )
        ).getText();
    }

    public String getLoanType() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        loanType
                )
        ).getText();
    }

    public String getLoanBalance() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        loanBalance
                )
        ).getText();
    }

    public String getLoanDetails() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        loanDetails
                )
        ).getText();
    }

    public List<String> getTermOptions() {

        Select select =
                new Select(
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        term
                                )
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public void selectTerm(String visibleText) {

        Select select =
                new Select(
                        wait.until(
                                ExpectedConditions.elementToBeClickable(
                                        term
                                )
                        )
                );

        select.selectByVisibleText(
                visibleText
        );
    }

    public String getSelectedTerm() {

        Select select =
                new Select(
                        driver.findElement(
                                term
                        )
                );

        return select.getFirstSelectedOption()
                .getText();
    }

    public void enterAmount(String value) {

        WebElement input =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                amount
                        )
                );

        input.clear();
        input.sendKeys(value);
    }

    public String getAmountValue() {

        return driver.findElement(
                amount
        ).getAttribute(
                "value"
        );
    }

    public String getMinimumAmount() {

        return driver.findElement(
                amount
        ).getAttribute(
                "min"
        );
    }

    public String getMaximumAmount() {

        return driver.findElement(
                amount
        ).getAttribute(
                "max"
        );
    }

    public boolean isAmountValid() {

        WebElement input =
                driver.findElement(
                        amount
                );

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        return (Boolean) js.executeScript(
                "return arguments[0].checkValidity();",
                input
        );
    }

    public void submitApplication() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        submitButton
                )
        ).click();
    }

    public void applyForLoan(
            String loanAmount,
            String termText
    ) {

        enterAmount(
                loanAmount
        );

        selectTerm(
                termText
        );

        submitApplication();
    }

    public void waitForToastToContain(
            String expectedText
    ) {

        wait.until(driver -> {

            try {

                WebElement element =
                        driver.findElement(
                                toast
                        );

                return element.isDisplayed()
                        &&
                        element.getText()
                                .contains(
                                        expectedText
                                );

            } catch (Exception exception) {

                return false;
            }
        });
    }

    public String getToastText() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        toast
                )
        ).getText();
    }
}