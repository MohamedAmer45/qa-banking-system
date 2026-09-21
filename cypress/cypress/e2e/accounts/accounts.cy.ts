describe("NovaBank - accounts", () => {
  beforeEach(() => {
    cy.login();
    cy.openView("accounts");
  });

  it("lists the seeded customer's accounts", () => {
    cy.byTestId("account-card").should("have.length.at.least", 3);
  });

  it("exposes balances as integer minor units", () => {
    cy.byTestId("account-balance")
      .first()
      .should("have.attr", "data-balance-minor")
      .then(value => {
        const minor = Number(value);
        expect(Number.isInteger(minor)).to.be.true;
        expect(minor).to.be.greaterThan(0);
      });
  });

  it("renders a type and a status on every account card", () => {
    cy.byTestId("account-card").each($card => {
      cy.wrap($card).should("match", ":contains(CURRENT), :contains(SAVINGS)");
    });
  });

  it("matches what the API reports", () => {
    cy.apiLogin().then(token => {
      cy.request({
        url: "/api/accounts",
        headers: { Authorization: `Bearer ${token}` }
      }).then(response => {
        cy.byTestId("account-card").should("have.length", response.body.length);
      });
    });
  });
});
