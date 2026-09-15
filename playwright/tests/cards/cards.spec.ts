import {
  test,
  expect
} from "../../fixtures/testFixtures";

import {
  cardsData
} from "../../test-data/cardsData";


test.describe(
  "NovaBank - Cards",
  () => {


    test.beforeEach(
      async ({
        authPage,
        cardsPage
      }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

        await cardsPage.open();

      }
    );


    test(
      "CARD-001 - customer can navigate to Cards",
      async ({
        cardsPage
      }) => {

        await cardsPage
          .expectLoaded();

      }
    );


    test(
      "CARD-002 - expected number of cards is displayed",
      async ({
        cardsPage
      }) => {

        await cardsPage
          .expectCardCount(
            cardsData.expectedCount
          );

      }
    );


    test(
      "CARD-003 - Visa Debit card details are correct",
      async ({
        cardsPage
      }) => {

        await cardsPage.expectCard(
          cardsData.visaDebit.type,
          cardsData.visaDebit.number,
          cardsData.visaDebit.initialStatus
        );

      }
    );


    test(
      "CARD-004 - Virtual Card details are correct",
      async ({
        cardsPage
      }) => {

        await cardsPage.expectCard(
          cardsData.virtualCard.type,
          cardsData.virtualCard.number,
          cardsData.virtualCard.initialStatus
        );

      }
    );


    test(
      "CARD-005 - all card numbers are masked",
      async ({
        cardsPage
      }) => {

        await cardsPage
          .expectAllNumbersMasked();

      }
    );


    test(
      "CARD-006 - active Visa card displays Freeze action",
      async ({
        cardsPage
      }) => {

        await cardsPage
          .expectToggleButton(
            cardsData.visaDebit.type,
            "Freeze card"
          );

      }
    );


    test(
      "CARD-007 - frozen Virtual Card displays Unfreeze action",
      async ({
        cardsPage
      }) => {

        await cardsPage
          .expectToggleButton(
            cardsData.virtualCard.type,
            "Unfreeze card"
          );

      }
    );


    test(
      "CARD-008 - active Visa card can be frozen",
      async ({
        cardsPage
      }) => {

        await cardsPage.toggleCard(
          cardsData.visaDebit.type,
          "frozen"
        );

        await cardsPage
          .expectToggleButton(
            cardsData.visaDebit.type,
            "Unfreeze card"
          );

      }
    );


    test(
      "CARD-009 - frozen Virtual Card can be unfrozen",
      async ({
        cardsPage
      }) => {

        await cardsPage.toggleCard(
          cardsData.virtualCard.type,
          "active"
        );

        await cardsPage
          .expectToggleButton(
            cardsData.virtualCard.type,
            "Freeze card"
          );

      }
    );


    test(
      "CARD-010 - freezing Visa card does not change Virtual Card status",
      async ({
        cardsPage
      }) => {

        const virtualBefore =
          await cardsPage.getCardStatus(
            cardsData.virtualCard.type
          );

        await cardsPage.toggleCard(
          cardsData.visaDebit.type,
          "frozen"
        );

        const virtualAfter =
          await cardsPage.getCardStatus(
            cardsData.virtualCard.type
          );

        expect(
          virtualAfter
        ).toBe(
          virtualBefore
        );

        expect(
          virtualAfter
        ).toBe(
          "frozen"
        );

      }
    );


    test(
      "CARD-011 - Visa card frozen state persists after reload",
      async ({
        cardsPage
      }) => {

        await cardsPage.toggleCard(
          cardsData.visaDebit.type,
          "frozen"
        );

        await cardsPage
          .reloadAndOpen();

        await cardsPage.expectCard(
          cardsData.visaDebit.type,
          cardsData.visaDebit.number,
          "frozen"
        );

        await cardsPage
          .expectToggleButton(
            cardsData.visaDebit.type,
            "Unfreeze card"
          );

      }
    );


    test(
      "CARD-012 - Virtual Card active state persists after reload",
      async ({
        cardsPage
      }) => {

        await cardsPage.toggleCard(
          cardsData.virtualCard.type,
          "active"
        );

        await cardsPage
          .reloadAndOpen();

        await cardsPage.expectCard(
          cardsData.virtualCard.type,
          cardsData.virtualCard.number,
          "active"
        );

      }
    );


    test(
      "CARD-013 - card status change is saved in localStorage",
      async ({
        cardsPage
      }) => {

        await cardsPage.toggleCard(
          cardsData.visaDebit.type,
          "frozen"
        );

        const storedCards =
          await cardsPage
            .getStoredCards();

        const visa =
          storedCards.find(
            card =>
              card.type ===
              cardsData.visaDebit.type
          );

        expect(
          visa
        ).toBeDefined();

        expect(
          visa?.status
        ).toBe(
          "frozen"
        );

      }
    );


    test(
      "CARD-014 - Visa card can be frozen and restored to active",
      async ({
        cardsPage
      }) => {

        await cardsPage.toggleCard(
          cardsData.visaDebit.type,
          "frozen"
        );

        await cardsPage.toggleCard(
          cardsData.visaDebit.type,
          "active"
        );

        await cardsPage.expectCard(
          cardsData.visaDebit.type,
          cardsData.visaDebit.number,
          "active"
        );

        await cardsPage
          .expectToggleButton(
            cardsData.visaDebit.type,
            "Freeze card"
          );

      }
    );

  }
);


test.describe(
  "NovaBank - Admin Card Access",
  () => {


    test(
      "CARD-015 - admin can access Cards module",
      async ({
        authPage,
        cardsPage
      }) => {

        await authPage.open();

        await authPage.loginAsAdmin();

        await cardsPage.open();

        await cardsPage
          .expectLoaded();

        await cardsPage
          .expectCardCount(
            cardsData.expectedCount
          );

      }
    );

  }
);
