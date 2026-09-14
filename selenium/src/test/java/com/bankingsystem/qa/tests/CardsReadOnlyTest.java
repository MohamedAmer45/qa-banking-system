package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.CardsCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class CardsReadOnlyTest extends CustomerTestBase {

    private CardsCurrentPage openCardsPage() {

        dashboardPage.openCards();

        CardsCurrentPage cardsPage =
                new CardsCurrentPage(
                        getDriver()
                );

        Assert.assertTrue(
                cardsPage.isLoaded(),
                "Cards page should load successfully."
        );

        return cardsPage;
    }

    @Test(
            groups = {"smoke", "cards"},
            description = "Cards page loads successfully"
    )
    public void cardsPageShouldLoadSuccessfully() {

        CardsCurrentPage cardsPage =
                openCardsPage();

        Assert.assertEquals(
                cardsPage.getPageTitleText(),
                "Cards",
                "Cards page heading should be displayed."
        );
    }

    @Test(
            groups = {"smoke", "cards"},
            description = "Customer cards are displayed"
    )
    public void customerCardsShouldBeDisplayed() {

        CardsCurrentPage cardsPage =
                openCardsPage();

        Assert.assertTrue(
                cardsPage.getCardCount() >= 2,
                "At least two cards should be displayed."
        );
    }

    @Test(
            groups = {"regression", "cards"},
            description = "Expected card types are displayed"
    )
    public void expectedCardTypesShouldBeDisplayed() {

        CardsCurrentPage cardsPage =
                openCardsPage();

        List<String> cardTypes =
                cardsPage.getCardTypes();

        Assert.assertTrue(
                cardTypes.contains(
                        "Visa Debit"
                ),
                "Visa Debit card should be displayed."
        );

        Assert.assertTrue(
                cardTypes.contains(
                        "Virtual Card"
                ),
                "Virtual Card should be displayed."
        );
    }

    @Test(
            groups = {"regression", "cards", "security"},
            description = "Displayed card numbers are masked"
    )
    public void cardNumbersShouldBeMasked() {

        CardsCurrentPage cardsPage =
                openCardsPage();

        Assert.assertEquals(
                cardsPage.getCardNumbers().size(),
                cardsPage.getCardCount(),
                "Every card should display a card number."
        );

        Assert.assertTrue(
                cardsPage.allCardNumbersAreMasked(),
                "Every displayed card number should be masked."
        );
    }

    @Test(
            groups = {"regression", "cards"},
            description = "Every card displays a valid status"
    )
    public void everyCardShouldDisplayValidStatus() {

        CardsCurrentPage cardsPage =
                openCardsPage();

        Assert.assertEquals(
                cardsPage.getCardStatuses().size(),
                cardsPage.getCardCount(),
                "Every card should display a status."
        );

        Assert.assertTrue(
                cardsPage.allStatusesAreValid(),
                "Card status should be active or frozen."
        );
    }

    @Test(
            groups = {"regression", "cards"},
            description = "Card action button corresponds to card status"
    )
    public void cardActionShouldMatchStatus() {

        CardsCurrentPage cardsPage =
                openCardsPage();

        String status =
                cardsPage.getCardStatus(
                        "card_1"
                );

        String action =
                cardsPage.getToggleButtonText(
                        "card_1"
                );

        if (status.equalsIgnoreCase("active")) {

            Assert.assertTrue(
                    action.toLowerCase()
                            .contains("freeze"),
                    "Active card should provide a Freeze action."
            );

        } else {

            Assert.assertTrue(
                    action.toLowerCase()
                            .contains("unfreeze"),
                    "Frozen card should provide an Unfreeze action."
            );
        }
    }
}