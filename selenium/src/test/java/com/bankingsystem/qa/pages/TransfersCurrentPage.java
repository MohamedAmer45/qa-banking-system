package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TransfersCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By section =
            By.cssSelector("#transfers.section.on");

    private final By heading =
            By.cssSelector("#transfers h1");

    private final By fromAccount =
            By.id("from");

    private final By recipient =
            By.id("recipient");

    private final By amount =
            By.id("transferAmount");

    private final By submitButton =
            By.cssSelector("#transferForm button");

    private final By toast =
            By.id("toast");

    private final By checkingBalance =
            By.cssSelector("#accountCards > .card:nth-child(1) .metric");

    private final By transactionRows =
            By.cssSelector("#tx table tbody tr");

    public TransfersCurrentPage(WebDriver driver) {

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

            return "Transfers".equals(
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

    public List<String> getFromAccountOptions() {

        Select select =
                new Select(
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        fromAccount
                                )
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> getRecipientOptions() {

        Select select =
                new Select(
                        wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                        recipient
                                )
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public void selectFromAccountByIndex(int index) {

        Select select =
                new Select(
                        wait.until(
                                ExpectedConditions.elementToBeClickable(
                                        fromAccount
                                )
                        )
                );

        select.selectByIndex(index);
    }

    public void selectRecipient(String name) {

        Select select =
                new Select(
                        wait.until(
                                ExpectedConditions.elementToBeClickable(
                                        recipient
                                )
                        )
                );

        select.selectByVisibleText(name);
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

    public void submitTransfer() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        submitButton
                )
        ).click();
    }

    public void transfer(
            int fromIndex,
            String recipientName,
            String transferAmount
    ) {

        selectFromAccountByIndex(fromIndex);
        selectRecipient(recipientName);
        enterAmount(transferAmount);
        submitTransfer();
    }

    public String getToastText() {

        WebElement toastElement =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                toast
                        )
                );

        return toastElement.getText();
    }

    public void waitForToastToContain(String expectedText) {

        wait.until(driver -> {

            try {

                WebElement element =
                        driver.findElement(toast);

                return element.isDisplayed()
                        && element.getText()
                                .contains(expectedText);

            } catch (Exception exception) {

                return false;
            }
        });
    }

    public String getTransferAmountValue() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        amount
                )
        ).getAttribute("value");
    }

    public String getSelectedFromAccountText() {

        Select select =
                new Select(
                        driver.findElement(
                                fromAccount
                        )
                );

        return select.getFirstSelectedOption()
                .getText();
    }

    public String getSelectedRecipientText() {

        Select select =
                new Select(
                        driver.findElement(
                                recipient
                        )
                );

        return select.getFirstSelectedOption()
                .getText();
    }

    public double getBalanceFromSelectedAccountOption() {

        String text =
                getSelectedFromAccountText();

        String numeric =
                text.replaceAll(
                        "[^0-9.]",
                        ""
                );

        return Double.parseDouble(
                numeric
        );
    }
}