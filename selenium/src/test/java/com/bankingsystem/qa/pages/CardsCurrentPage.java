package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CardsCurrentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By section =
            By.cssSelector("#cards.section.on");

    private final By heading =
            By.cssSelector("#cards h1");

    private final By cardContainers =
            By.cssSelector("#cardCards > .card");

    private final By cardTypes =
            By.cssSelector("#cardCards > .card h2");

    private final By cardNumbers =
            By.cssSelector("#cardCards > .card .muted");

    private final By cardStatuses =
            By.cssSelector("#cardCards > .card .tag");

    private final By toast =
            By.id("toast");

    public CardsCurrentPage(WebDriver driver) {

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

            return "Cards".equals(
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

    public int getCardCount() {

        wait.until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        cardContainers,
                        0
                )
        );

        return driver.findElements(
                cardContainers
        ).size();
    }

    public List<String> getCardTypes() {

        return driver.findElements(
                cardTypes
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getCardNumbers() {

        return driver.findElements(
                cardNumbers
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public List<String> getCardStatuses() {

        return driver.findElements(
                cardStatuses
        )
        .stream()
        .map(WebElement::getText)
        .toList();
    }

    public boolean allCardNumbersAreMasked() {

        return getCardNumbers()
                .stream()
                .allMatch(number ->
                        number.contains("****")
                );
    }

    public boolean allStatusesAreValid() {

        return getCardStatuses()
                .stream()
                .allMatch(status ->
                        status.equalsIgnoreCase("active")
                        ||
                        status.equalsIgnoreCase("frozen")
                );
    }

    private By toggleButton(String cardId) {

        return By.cssSelector(
                ".toggle[data-id='" + cardId + "']"
        );
    }

    private WebElement getCardContainer(String cardId) {

        WebElement button =
                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                toggleButton(cardId)
                        )
                );

        return button.findElement(
                By.xpath(
                        "./ancestor::div[contains(concat(' ', normalize-space(@class), ' '), ' card ')][1]"
                )
        );
    }

    public String getCardStatus(String cardId) {

        return getCardContainer(cardId)
                .findElement(
                        By.cssSelector(".tag")
                )
                .getText();
    }

    public String getToggleButtonText(String cardId) {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        toggleButton(cardId)
                )
        ).getText();
    }

    public void toggleCard(String cardId) {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        toggleButton(cardId)
                )
        ).click();
    }

    public void waitForStatus(
            String cardId,
            String expectedStatus
    ) {

        wait.until(driver -> {

            try {

                return getCardStatus(cardId)
                        .equalsIgnoreCase(
                                expectedStatus
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
                                .toLowerCase()
                                .contains(
                                        expectedText.toLowerCase()
                                );

            } catch (Exception exception) {

                return false;
            }
        });
    }
}