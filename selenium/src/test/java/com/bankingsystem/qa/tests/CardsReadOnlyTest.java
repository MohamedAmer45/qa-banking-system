package com.bankingsystem.qa.tests;

import com.bankingsystem.qa.base.CustomerTestBase;
import com.bankingsystem.qa.pages.CardsPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class CardsReadOnlyTest extends CustomerTestBase {

    private CardsPage cardsPage;


    @BeforeMethod(alwaysRun = true)
    public void openCardsPage() {

        cardsPage =
                dashboardPage.openCards();

        Assert.assertTrue(
                cardsPage.isLoaded(),
                "Cards page should load successfully."
        );
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: authenticated API requests intermittently return 401."
    )
    public void cardsPageShouldOpenSuccessfully() {

        Assert.assertEquals(
                cardsPage.getPageTitleText(),
                "Cards",
                "Page title should be Cards."
        );

        Assert.assertTrue(
                cardsPage.hasCardsOrEmptyState(),
                "Cards module should display issued cards or an empty state."
        );
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: authenticated API requests intermittently return 401."
    )
    public void cardControlMatrixShouldMatchDisplayedCards() {

        Assert.assertEquals(
                cardsPage.getControlMatrixRowCount(),
                cardsPage.getCardCount(),
                "Each displayed card should have one control-matrix row."
        );
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: authenticated API requests intermittently return 401."
    )
    public void displayedCardNumbersShouldBeMasked() {

        List<String> cardNumbers =
                cardsPage.getDisplayedCardNumbers();

        for (String number : cardNumbers) {

            Assert.assertTrue(
                    number.startsWith(
                            "\u2022\u2022\u2022\u2022"
                    ),
                    "Card number should be masked. Value: " + number
            );

            Assert.assertTrue(
                    number.matches(".*\\d{4}$"),
                    "Only the final four card digits should remain visible. Value: "
                            + number
            );
        }
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: authenticated API requests intermittently return 401."
    )
    public void displayedCardsShouldHaveStatuses() {

        List<String> statuses =
                cardsPage.getCardStatuses();

        Assert.assertEquals(
                statuses.size(),
                cardsPage.getCardCount(),
                "Every displayed card should show a status."
        );

        for (String status : statuses) {

            Assert.assertFalse(
                    status.isBlank(),
                    "Card status should not be blank."
            );
        }
    }


    @Test(
            enabled = false,
            description = "Known defect BUG-AUTH-001: authenticated API requests intermittently return 401."
    )
    public void requestCardModalShouldDisplayRequiredControls() {

        cardsPage.openRequestCardModal();

        Assert.assertTrue(
                cardsPage.isRequestCardModalDisplayed(),
                "Request Card modal should be displayed."
        );

        Assert.assertTrue(
                cardsPage.isLinkedAccountSelectDisplayed(),
                "Linked account selector should be displayed."
        );

        Assert.assertTrue(
                cardsPage.isModalRequestButtonDisplayed(),
                "Request Card submit button should be displayed."
        );


        List<String> accounts =
                cardsPage.getLinkedAccountOptions();

        Assert.assertFalse(
                accounts.isEmpty(),
                "At least one active account should be available for card issuance."
        );

        for (String account : accounts) {

            Assert.assertFalse(
                    account.isBlank(),
                    "Linked account option should not be blank."
            );
        }


        cardsPage.closeRequestCardModal();
    }
}
