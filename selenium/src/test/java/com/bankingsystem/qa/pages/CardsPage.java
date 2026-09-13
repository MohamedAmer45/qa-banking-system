package com.bankingsystem.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class CardsPage extends BasePage {

    private final By pageTitle =
            By.id("page-title");

    private final By requestCardButton =
            By.xpath(
                    "//button[contains(normalize-space(.),'Request card')]"
            );

    private final By bankCards =
            By.cssSelector(
                    "#view .bank-card"
            );

    private final By cardNumbers =
            By.cssSelector(
                    "#view .bank-card .digits"
            );

    private final By cardStatuses =
            By.cssSelector(
                    "#view .bank-card .badge"
            );

    private final By emptyCardsState =
            By.xpath(
                    "//*[@id='view']//*[contains(@class,'empty') " +
                    "and contains(normalize-space(.),'No cards issued')]"
            );

    private final By controlMatrixHeading =
            By.xpath(
                    "//h3[normalize-space()='Card control matrix']"
            );

    private final By controlMatrixRows =
            By.xpath(
                    "//h3[normalize-space()='Card control matrix']" +
                    "/ancestor::div[contains(@class,'card')][1]" +
                    "//tbody/tr"
            );

    private final By requestCardForm =
            By.id("card-request");

    private final By linkedAccountSelect =
            By.cssSelector(
                    "#card-request select[name='accountId']"
            );

    private final By modalRequestButton =
            By.xpath(
                    "//form[@id='card-request']//button[" +
                    "contains(normalize-space(.),'Request card')" +
                    "]"
            );

    private final By modalCloseButton =
            By.cssSelector(
                    "#modal-root .close"
            );

    public CardsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {

        try {

            wait.waitForText(
                    pageTitle,
                    "Cards"
            );

            wait.waitForVisible(
                    requestCardButton
            );

            wait.waitForVisible(
                    controlMatrixHeading
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

    public int getCardCount() {

        return driver.findElements(
                bankCards
        ).size();
    }

    public boolean hasCardsOrEmptyState() {

        return getCardCount() > 0
                || driver.findElements(
                        emptyCardsState
                ).size() > 0;
    }

    public List<String> getDisplayedCardNumbers() {

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

    public int getControlMatrixRowCount() {

        return driver.findElements(
                controlMatrixRows
        ).size();
    }

    public CardsPage openRequestCardModal() {

        click(
                requestCardButton
        );

        wait.waitForVisible(
                requestCardForm
        );

        return this;
    }

    public boolean isRequestCardModalDisplayed() {

        return isDisplayed(
                requestCardForm
        );
    }

    public boolean isLinkedAccountSelectDisplayed() {

        return isDisplayed(
                linkedAccountSelect
        );
    }

    public boolean isModalRequestButtonDisplayed() {

        return isDisplayed(
                modalRequestButton
        );
    }

    public List<String> getLinkedAccountOptions() {

        Select select =
                new Select(
                        wait.waitForVisible(
                                linkedAccountSelect
                        )
                );

        return select.getOptions()
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    public CardsPage closeRequestCardModal() {

        click(
                modalCloseButton
        );

        wait.waitForInvisible(
                requestCardForm
        );

        return this;
    }
}
