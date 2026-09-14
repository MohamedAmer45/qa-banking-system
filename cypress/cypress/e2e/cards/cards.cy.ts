import CardsPage from "../../pages/CardsPage";

describe("NovaBank - Cards", () => {

  beforeEach(() => {
    CardsPage.visitAsCustomer();
    CardsPage.openCards();
  });


  it("should display the Cards section", () => {

    CardsPage.getCardsSection()
      .should("be.visible")
      .and("have.class", "on");

    cy.contains("#cards h1", /^Cards$/)
      .should("be.visible");

  });


  it("should display both customer cards", () => {

    CardsPage.getCards()
      .should("have.length", 2);

  });


  it("should display the Visa Debit card correctly", () => {

    CardsPage.getCardByType("Visa Debit")
      .should("be.visible")
      .within(() => {

        cy.contains("h2", /^Visa Debit$/)
          .should("be.visible");

        cy.contains("**** 4242")
          .should("be.visible");

        cy.contains(".tag", "active")
          .should("be.visible");

        cy.contains("button", "Freeze card")
          .should("be.visible")
          .and("be.enabled");

      });

  });


  it("should display the Virtual Card correctly", () => {

    CardsPage.getCardByType("Virtual Card")
      .should("be.visible")
      .within(() => {

        cy.contains("h2", /^Virtual Card$/)
          .should("be.visible");

        cy.contains("**** 8831")
          .should("be.visible");

        cy.contains(".tag", "frozen")
          .should("be.visible");

        cy.contains("button", "Unfreeze card")
          .should("be.visible")
          .and("be.enabled");

      });

  });


  it("should mask all card numbers", () => {

    CardsPage.getCards()
      .each(($card) => {

        cy.wrap($card)
          .find(".muted")
          .invoke("text")
          .then((number) => {

            expect(number.trim())
              .to.match(/^\*{4} \d{4}$/);

          });

      });

  });


  it("should freeze an active card", () => {

    cy.intercept("PATCH", "**/api/cards")
      .as("updateCard");

    CardsPage.toggleCard("Visa Debit");

    cy.wait("@updateCard")
      .its("response.statusCode")
      .should("be.oneOf", [200, 204]);

    CardsPage.getToast()
      .should("be.visible")
      .and("contain.text", "Card frozen.");

    CardsPage.getCardByType("Visa Debit")
      .within(() => {

        cy.contains(".tag", "frozen")
          .should("be.visible");

        cy.contains("button", "Unfreeze card")
          .should("be.visible");

      });

  });


  it("should unfreeze a frozen card", () => {

    cy.intercept("PATCH", "**/api/cards")
      .as("updateCard");

    CardsPage.toggleCard("Virtual Card");

    cy.wait("@updateCard")
      .its("response.statusCode")
      .should("be.oneOf", [200, 204]);

    CardsPage.getToast()
      .should("be.visible")
      .and("contain.text", "Card active.");

    CardsPage.getCardByType("Virtual Card")
      .within(() => {

        cy.contains(".tag", "active")
          .should("be.visible");

        cy.contains("button", "Freeze card")
          .should("be.visible");

      });

  });


  it("should preserve updated card state after reload", () => {

    cy.intercept("PATCH", "**/api/cards")
      .as("updateCard");

    CardsPage.toggleCard("Visa Debit");

    cy.wait("@updateCard")
      .its("response.statusCode")
      .should("be.oneOf", [200, 204]);

    CardsPage.getCardByType("Visa Debit")
      .should("contain.text", "frozen");

    // Reload preserves session and localStorage,
    // but the visible section returns to Dashboard.
    cy.reload();

    // Navigate back to Cards after reload.
    CardsPage.openCards();

    CardsPage.getCardsSection()
      .should("be.visible")
      .and("have.class", "on");

    CardsPage.getCardByType("Visa Debit")
      .within(() => {

        cy.contains(".tag", "frozen")
          .should("be.visible");

        cy.contains("button", "Unfreeze card")
          .should("be.visible");

      });

  });

});
