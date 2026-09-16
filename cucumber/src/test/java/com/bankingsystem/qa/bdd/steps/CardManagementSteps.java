package com.bankingsystem.qa.bdd.steps;

import com.bankingsystem.qa.bdd.context.TestContext;
import com.bankingsystem.qa.bdd.pages.CardsCurrentPage;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.testng.Assert;

import java.util.List;

public final class CardManagementSteps {

    private final TestContext context;

    private CardsCurrentPage cardsPage;

    private String controlledCardId;
    private String originalStatus;
    private String expectedStatus;
    private String originalAction;

    public CardManagementSteps(
            TestContext context
    ) {
        this.context = context;
    }

    @When("the customer opens the cards workspace")
    public void customerOpensCardsWorkspace() {

        cardsPage =
                new CardsCurrentPage(
                        context.getDriver()
                ).open();

        Assert.assertTrue(
                cardsPage.isLoaded(),
                "Cards workspace should load."
        );
    }

    @Then("the cards heading should be {string}")
    public void cardsHeadingShouldBe(
            String expectedHeading
    ) {

        Assert.assertEquals(
                page().getPageTitleText(),
                expectedHeading,
                "Cards heading should match."
        );
    }

    @Then("at least {int} customer cards should be displayed")
    public void customerCardsShouldBeDisplayed(
            int minimumCount
    ) {

        Assert.assertTrue(
                page().getCardCount() >= minimumCount,
                "Expected at least " +
                minimumCount +
                " cards."
        );
    }

    @Then("these card types should be displayed:")
    public void cardTypesShouldBeDisplayed(
            DataTable dataTable
    ) {

        List<String> actualTypes =
                page().getCardTypes();

        for (String expectedType :
                dataTable.asList()) {

            Assert.assertTrue(
                    actualTypes.contains(
                            expectedType
                    ),
                    "Card type should be displayed: " +
                    expectedType
            );
        }
    }

    @Then("every displayed card number should be masked")
    public void everyCardNumberShouldBeMasked() {

        Assert.assertEquals(
                page().getCardNumbers().size(),
                page().getCardCount(),
                "Every card should display a card number."
        );

        Assert.assertTrue(
                page().allCardNumbersAreMasked(),
                "Every card number should be masked."
        );
    }

    @Then("every card should display a valid status")
    public void everyCardShouldDisplayValidStatus() {

        Assert.assertEquals(
                page().getCardStatuses().size(),
                page().getCardCount(),
                "Every card should display a status."
        );

        Assert.assertTrue(
                page().allStatusesAreValid(),
                "Every status should be active or frozen."
        );
    }

    @Then("the action for card {string} should correspond to its status")
    public void cardActionShouldMatchStatus(
            String cardId
    ) {

        String status =
                page().getCardStatus(cardId);

        String action =
                page().getToggleButtonText(cardId)
                        .toLowerCase();

        if (status.equalsIgnoreCase("active")) {
            Assert.assertTrue(
                    action.contains("freeze"),
                    "An active card should provide a Freeze action."
            );
        } else {
            Assert.assertTrue(
                    action.contains("unfreeze"),
                    "A frozen card should provide an Unfreeze action."
            );
        }
    }

    @When("the customer toggles card {string}")
    public void customerTogglesCard(
            String cardId
    ) {

        controlledCardId = cardId;

        originalStatus =
                page().getCardStatus(cardId);

        originalAction =
                page().getToggleButtonText(cardId);

        expectedStatus =
                originalStatus.equalsIgnoreCase("active")
                ? "frozen"
                : "active";

        page().toggleCard(cardId);
    }

    @Then("the controlled card status should change")
    public void controlledCardStatusShouldChange() {

        requireControlledCard();

        page().waitForStatus(
                controlledCardId,
                expectedStatus
        );

        Assert.assertEquals(
                page().getCardStatus(controlledCardId)
                        .toLowerCase(),
                expectedStatus,
                "Card status should change."
        );
    }

    @Then("the card notification should describe the new status")
    public void notificationShouldDescribeStatus() {

        requireControlledCard();

        page().waitForToastToContain(
                expectedStatus
        );

        Assert.assertTrue(
                page().getToastText()
                        .toLowerCase()
                        .contains(expectedStatus),
                "Notification should contain the new card status."
        );
    }

    @Then("the controlled card action should change")
    public void controlledCardActionShouldChange() {

        requireControlledCard();

        Assert.assertNotEquals(
                page().getToggleButtonText(
                        controlledCardId
                ),
                originalAction,
                "Card action should change with its status."
        );
    }

    @When("the customer restores the controlled card")
    public void customerRestoresControlledCard() {

        requireControlledCard();

        page().toggleCard(
                controlledCardId
        );

        page().waitForStatus(
                controlledCardId,
                originalStatus
        );
    }

    @Then("the card should return to its original status")
    public void cardShouldReturnToOriginalStatus() {

        requireControlledCard();

        Assert.assertEquals(
                page().getCardStatus(controlledCardId)
                        .toLowerCase(),
                originalStatus.toLowerCase(),
                "Card should return to its original status."
        );

        Assert.assertEquals(
                page().getToggleButtonText(
                        controlledCardId
                ),
                originalAction,
                "Card action should return to its original value."
        );
    }

    private CardsCurrentPage page() {

        if (cardsPage == null) {
            throw new IllegalStateException(
                    "Cards workspace has not been opened."
            );
        }

        return cardsPage;
    }

    private void requireControlledCard() {

        if (controlledCardId == null) {
            throw new IllegalStateException(
                    "No card control operation was started."
            );
        }
    }
}