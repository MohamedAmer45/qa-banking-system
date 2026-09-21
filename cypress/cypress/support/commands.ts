/// <reference types="cypress" />

import { MFA_CODE, SeedUser, users } from "./credentials";

type ViewName =
  | "overview" | "accounts" | "transfers" | "beneficiaries" | "transactions"
  | "statements" | "cards" | "bills" | "loans" | "kyc" | "notifications"
  | "security" | "admin-dashboard" | "admin-customers" | "admin-kyc"
  | "admin-accounts" | "admin-transfers" | "admin-loans" | "admin-fraud"
  | "admin-audit" | "admin-users";

declare global {
  namespace Cypress {
    interface Chainable {
      /** Resolve an element by the application's data-testid attribute. */
      byTestId(id: string): Chainable<JQuery<HTMLElement>>;

      /** Full credential + MFA handshake, ending on an authenticated shell. */
      login(user?: SeedUser): Chainable<void>;

      /** Obtain a session token through the API, without driving the UI. */
      apiLogin(user?: SeedUser): Chainable<string>;

      /** Navigate through the sidebar and wait for the view to finish rendering. */
      openView(view: ViewName): Chainable<void>;

      /** Wait for the current view's render to settle. */
      waitForView(): Chainable<void>;
    }
  }
}

Cypress.Commands.add("byTestId", (id: string) =>
  cy.get(`[data-testid="${id}"]`)
);

/*
 * The application blanks its view to a placeholder and then awaits its API
 * calls, so the heading updates before the body exists. Every navigation waits
 * for the placeholder to clear, otherwise assertions race the render.
 */
Cypress.Commands.add("waitForView", () => {
  cy.byTestId("view").should("not.contain.text", "Loading…");
});

Cypress.Commands.add("login", (user: SeedUser = users.customer) => {
  cy.visit("/");

  cy.intercept("POST", "**/api/auth/login").as("login");
  cy.intercept("POST", "**/api/auth/mfa").as("mfa");

  cy.byTestId("login-email").clear().type(user.email);
  cy.byTestId("login-password").clear().type(user.password, { log: false });
  cy.byTestId("login-submit").click();

  cy.wait("@login").its("response.statusCode").should("eq", 200);

  // Credentials alone never yield a session; the challenge must be answered.
  cy.byTestId("mfa-form").should("be.visible");
  cy.byTestId("mfa-code").clear().type(MFA_CODE);
  cy.byTestId("mfa-submit").click();

  cy.wait("@mfa").its("response.statusCode").should("eq", 200);

  cy.byTestId("user-role").should("have.text", user.role);
  cy.waitForView();
});

Cypress.Commands.add("apiLogin", (user: SeedUser = users.customer) =>
  cy
    .request("POST", "/api/auth/login", {
      email: user.email,
      password: user.password
    })
    .then(login =>
      cy.request("POST", "/api/auth/mfa", {
        challenge: login.body.challenge,
        code: MFA_CODE
      })
    )
    .then(mfa => mfa.body.token as string)
);

Cypress.Commands.add("openView", (view: ViewName) => {
  cy.get("body").then($body => {
    // A rejected submission leaves its modal up, blocking the nav click.
    if ($body.find('[data-testid="modal"]').length) {
      cy.byTestId("modal-close").click();
    }
  });

  cy.waitForView();
  cy.byTestId(`nav-${view}`).click();
  cy.waitForView();
});

export {};
