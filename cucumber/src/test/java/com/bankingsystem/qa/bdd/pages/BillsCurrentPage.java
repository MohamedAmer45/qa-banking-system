package com.bankingsystem.qa.bdd.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BillsCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By section =
            By.cssSelector("#bills.section.on");

    private final By heading =
            By.cssSelector("#bills h1");

    private final By biller =
            By.id("biller");

    private final By amount =
            By.id("billAmount");

    private final By submitButton =
            By.cssSelector("#billForm button");

    private final By toast =
            By.id("toast");

    public BillsCurrentPage(WebDriver driver) {

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

            return "Bills".equals(
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

    public List<String> getBillerOptions() {

        Select select =
                new Select(
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        biller
                                )
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public void selectBiller(String billerName) {

        Select select =
                new Select(
                        wait.until(
                                ExpectedConditions.elementToBeClickable(
                                        biller
                                )
                        )
                );

        select.selectByVisibleText(
                billerName
        );
    }

    public String getSelectedBiller() {

        Select select =
                new Select(
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        biller
                                )
                        )
                );

        return select.getFirstSelectedOption()
                .getText();
    }

    public void enterAmount(String value) {

        WebElement amountInput =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                amount
                        )
                );

        amountInput.clear();
        amountInput.sendKeys(value);
    }

    public String getAmountValue() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        amount
                )
        ).getAttribute("value");
    }

    public void submitPayment() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        submitButton
                )
        ).click();
    }

    public void payBill(
            String billerName,
            String paymentAmount
    ) {

        selectBiller(
                billerName
        );

        enterAmount(
                paymentAmount
        );

        submitPayment();
    }

    public String getToastText() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        toast
                )
        ).getText();
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
                        && element.getText()
                                .contains(
                                        expectedText
                                );

            } catch (Exception exception) {

                return false;
            }
        });
    }

    public BillsCurrentPage open() {

        By navigation =
                By.cssSelector(
                        "#nav button[data-s='bills']"
                );

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        navigation
                )
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        section
                )
        );

        return this;
    }

    public void removeAmountMinimum() {

        org.openqa.selenium.JavascriptExecutor javascript =
                (org.openqa.selenium.JavascriptExecutor) driver;

        javascript.executeScript(
                "document.getElementById('billAmount')" +
                ".removeAttribute('min');"
        );
    }
}
