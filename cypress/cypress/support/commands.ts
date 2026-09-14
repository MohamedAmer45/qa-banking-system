/// <reference types="cypress" />

type NovaBankModule =
  | "dashboard"
  | "accounts"
  | "transfers"
  | "transactions"
  | "bills"
  | "cards"
  | "loans"
  | "notifications"
  | "profile"
  | "admin";

declare global {
  namespace Cypress {
    interface Chainable {

      /**
       * Reset NovaBank browser state to the deterministic defaults.
       * The application must already be loaded.
       */
      resetNovaBankState(): Chainable<void>;

      /**
       * Enter NovaBank using a clean customer session.
       */
      loginAsCustomer(): Chainable<void>;

      /**
       * Enter NovaBank using a clean admin session.
       */
      loginAsAdmin(): Chainable<void>;

      /**
       * Navigate to a NovaBank application module.
       */
      openModule(module: NovaBankModule): Chainable<void>;
    }
  }
}


Cypress.Commands.add("resetNovaBankState", () => {

  cy.window().then((win) => {

    // Reset all persistent application data.
    win.localStorage.removeItem("nb_state");

    // Reset authentication/session data.
    win.sessionStorage.removeItem("nb_token");
    win.sessionStorage.removeItem("nb_user");

  });

  // Reload so NovaBank rebuilds its deterministic default state.
  cy.reload();

  cy.get("#login")
    .should("be.visible");

  cy.get("#app")
    .should("have.class", "hidden");

});


Cypress.Commands.add("loginAsCustomer", () => {

  cy.resetNovaBankState();

  cy.intercept("POST", "**/api/session")
    .as("customerSession");

  cy.get("#login button.enter[data-role='customer']")
    .should("be.visible")
    .and("be.enabled")
    .click();

  cy.wait("@customerSession")
    .its("response.statusCode")
    .should("be.oneOf", [200, 201]);

  cy.get("#app")
    .should("be.visible");

  cy.get("#who")
    .should("be.visible")
    .and("contain.text", "customer");

});


Cypress.Commands.add("loginAsAdmin", () => {

  cy.resetNovaBankState();

  cy.intercept("POST", "**/api/session")
    .as("adminSession");

  cy.get("#login button.enter[data-role='admin']")
    .should("be.visible")
    .and("be.enabled")
    .click();

  cy.wait("@adminSession")
    .its("response.statusCode")
    .should("be.oneOf", [200, 201]);

  cy.get("#app")
    .should("be.visible");

  cy.get("#who")
    .should("be.visible")
    .and("contain.text", "admin");

});


Cypress.Commands.add(
  "openModule",
  (module: NovaBankModule) => {

    cy.get(`#nav button[data-s='${module}']`)
      .should("be.visible")
      .and("be.enabled")
      .click();

    cy.get(`#${module}`)
      .should("be.visible")
      .and("have.class", "on");

  }
);

export {};
