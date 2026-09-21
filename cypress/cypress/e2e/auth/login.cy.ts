import { MFA_CODE, users } from "../../support/credentials";

describe("NovaBank - authentication", () => {
  beforeEach(() => {
    cy.visit("/");
  });

  it("requires an MFA challenge before issuing a session", () => {
    cy.intercept("POST", "**/api/auth/login").as("login");

    cy.byTestId("login-email").clear().type(users.customer.email);
    cy.byTestId("login-password").clear().type(users.customer.password);
    cy.byTestId("login-submit").click();

    cy.wait("@login").then(({ response }) => {
      expect(response?.statusCode).to.eq(200);
      expect(response?.body).to.have.property("mfaRequired", true);
      expect(response?.body).to.have.property("challenge");
      // The session token is never part of the credential response.
      expect(response?.body).to.not.have.property("token");
    });

    cy.byTestId("mfa-form").should("be.visible");
    cy.window().then(win => {
      expect(win.localStorage.getItem("novabank_token")).to.be.null;
    });
  });

  it("rejects an invalid password with a 401", () => {
    cy.intercept("POST", "**/api/auth/login").as("login");

    cy.byTestId("login-email").clear().type(users.customer.email);
    cy.byTestId("login-password").clear().type("WrongPassword123!");
    cy.byTestId("login-submit").click();

    cy.wait("@login").its("response.statusCode").should("eq", 401);
    cy.byTestId("toast").should("contain.text", "Invalid");
    cy.byTestId("login-form").should("be.visible");
  });

  it("rejects an incorrect one-time code with a 401", () => {
    cy.intercept("POST", "**/api/auth/mfa").as("mfa");

    cy.byTestId("login-email").clear().type(users.customer.email);
    cy.byTestId("login-password").clear().type(users.customer.password);
    cy.byTestId("login-submit").click();

    cy.byTestId("mfa-code").clear().type("000000");
    cy.byTestId("mfa-submit").click();

    cy.wait("@mfa").its("response.statusCode").should("eq", 401);
    cy.byTestId("toast").should("contain.text", "Invalid one-time code");
  });

  it("stores a session token once the challenge is answered", () => {
    cy.login();

    cy.window().then(win => {
      expect(win.localStorage.getItem("novabank_token")).to.be.a("string").and.not.be.empty;
    });
  });

  it("clears the session on sign out", () => {
    cy.login();
    cy.byTestId("logout").click();

    cy.byTestId("login-form").should("be.visible");
    cy.window().then(win => {
      expect(win.localStorage.getItem("novabank_token")).to.be.null;
    });
  });

  it("signs staff in with their own role", () => {
    cy.login(users.admin);
    cy.byTestId("user-role").should("have.text", "ADMIN");
  });

  it("uses the same one-time code for every seeded identity", () => {
    expect(MFA_CODE).to.eq("123456");
  });
});
