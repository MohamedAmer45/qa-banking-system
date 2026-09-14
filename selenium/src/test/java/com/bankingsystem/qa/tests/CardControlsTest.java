package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.CardsCurrentPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CardControlsTest extends CustomerTestBase {

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
            groups = {"regression", "cards", "controls"},
            description = "Customer can freeze and unfreeze a card"
    )
    public void customerShouldToggleCardStatus() {

        CardsCurrentPage cardsPage =
                openCardsPage();

        String cardId =
                "card_1";

        String originalStatus =
                cardsPage.getCardStatus(
                        cardId
                );

        String expectedNewStatus =
                originalStatus.equalsIgnoreCase(
                        "active"
                )
                ? "frozen"
                : "active";

        cardsPage.toggleCard(
                cardId
        );

        cardsPage.waitForStatus(
                cardId,
                expectedNewStatus
        );

        Assert.assertEquals(
                cardsPage.getCardStatus(cardId)
                        .toLowerCase(),
                expectedNewStatus,
                "Card status should change after using the card control."
        );

        cardsPage.waitForToastToContain(
                expectedNewStatus
        );

        Assert.assertTrue(
                cardsPage.getToastText()
                        .toLowerCase()
                        .contains(expectedNewStatus),
                "Confirmation message should show the new card status."
        );


        /*
         * Restore the card to its original status so this test
         * does not leave persistent browser state behind.
         */

        cardsPage.toggleCard(
                cardId
        );

        cardsPage.waitForStatus(
                cardId,
                originalStatus
        );

        Assert.assertEquals(
                cardsPage.getCardStatus(cardId)
                        .toLowerCase(),
                originalStatus.toLowerCase(),
                "Card should be restored to its original status."
        );
    }

    @Test(
            groups = {"regression", "cards", "controls"},
            description = "Freeze button changes to Unfreeze after card is frozen"
    )
    public void cardActionButtonShouldChangeWithStatus() {

        CardsCurrentPage cardsPage =
                openCardsPage();

        String cardId =
                "card_1";

        String originalStatus =
                cardsPage.getCardStatus(
                        cardId
                );

        String originalButton =
                cardsPage.getToggleButtonText(
                        cardId
                );

        cardsPage.toggleCard(
                cardId
        );

        String expectedStatus =
                originalStatus.equalsIgnoreCase("active")
                ? "frozen"
                : "active";

        cardsPage.waitForStatus(
                cardId,
                expectedStatus
        );

        String newButton =
                cardsPage.getToggleButtonText(
                        cardId
                );

        Assert.assertNotEquals(
                newButton,
                originalButton,
                "Card action button should change after the status changes."
        );


        // Restore original state
        cardsPage.toggleCard(
                cardId
        );

        cardsPage.waitForStatus(
                cardId,
                originalStatus
        );
    }
}