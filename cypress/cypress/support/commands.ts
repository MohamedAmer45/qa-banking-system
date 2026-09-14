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
       * Enter NovaBank using the customer demo role.
       * Assumes the login screen is already open.
       */
      loginAsCustomer(): Chainable<void>;

      /**
       * Enter NovaBank using the admin demo role.
       * Assumes the login screen is already open.
       */
      loginAsAdmin(): Chainable<void>;

      /**
       * Navigate to a NovaBank application module.
       */
      openModule(module: NovaBankModule): Chainable<void>;
    }
  }
}


Cypress.Commands.add("loginAsCustomer", () => {

  cy.get("#login button.enter[data-role='customer']")
    .should("be.visible")
    .and("be.enabled")
    .click();

  cy.get("#app")
    .should("be.visible");

  cy.get("#who")
    .should("be.visible")
    .and("contain.text", "customer");

});


Cypress.Commands.add("loginAsAdmin", () => {

  cy.get("#login button.enter[data-role='admin']")
    .should("be.visible")
    .and("be.enabled")
    .click();

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
