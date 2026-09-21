import { users } from "../../support/credentials";

/*
 * Contract-level checks that do not need a browser session. These guard the
 * conventions the rest of the suites depend on.
 */
describe("NovaBank - API contract", () => {
  it("returns 401 for an unauthenticated protected request", () => {
    cy.request({ url: "/api/accounts", failOnStatusCode: false })
      .its("status")
      .should("eq", 401);
  });

  it("returns 403 when a customer calls a back-office endpoint", () => {
    cy.apiLogin().then(token => {
      cy.request({
        url: "/api/admin/dashboard",
        headers: { Authorization: `Bearer ${token}` },
        failOnStatusCode: false
      })
        .its("status")
        .should("eq", 403);
    });
  });

  it("returns 404, not 403, for another customer's resource", () => {
    cy.apiLogin(users.receiver).then(token => {
      // Account 1 belongs to the primary customer, not the receiver.
      cy.request({
        url: "/api/accounts/1/transactions",
        headers: { Authorization: `Bearer ${token}` },
        failOnStatusCode: false
      })
        .its("status")
        .should("eq", 404);
    });
  });

  it("treats an idempotency key as a replay guard", () => {
    const key = `cypress-idem-${Date.now()}`;

    cy.apiLogin().then(token => {
      const headers = { Authorization: `Bearer ${token}` };
      const body = { fromAccountId: 1, beneficiaryId: 1, amount: 1, idempotencyKey: key };

      cy.request({ method: "POST", url: "/api/transfers", headers, body }).then(first => {
        cy.request({ method: "POST", url: "/api/transfers", headers, body }).then(second => {
          expect(second.body.reference).to.eq(first.body.reference);
          expect(second.body.id).to.eq(first.body.id);
        });
      });
    });
  });

  it("reports amounts in integer minor units", () => {
    cy.apiLogin().then(token => {
      cy.request({
        url: "/api/accounts",
        headers: { Authorization: `Bearer ${token}` }
      }).then(response => {
        response.body.forEach((account: { balance_minor: number }) => {
          expect(Number.isInteger(account.balance_minor)).to.be.true;
        });
      });
    });
  });
});
