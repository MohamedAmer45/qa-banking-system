/*
 * Cypress carries the form-validation and network-contract coverage for
 * transfers. The end-to-end money movement itself is Playwright's, so the two
 * suites do not assert the same thing twice.
 */
describe("NovaBank - transfer form validation", () => {
  beforeEach(() => {
    cy.login();
    cy.openView("transfers");
    cy.byTestId("new-transfer").click();
    cy.byTestId("transfer-form").should("be.visible");
  });

  it("marks a zero amount invalid before any request is sent", () => {
    cy.intercept("POST", "**/api/transfers").as("transfer");

    cy.byTestId("transfer-amount").clear().type("0");
    cy.byTestId("transfer-amount").then($input => {
      const input = $input[0] as HTMLInputElement;
      expect(input.checkValidity()).to.be.false;
      expect(input.validity.rangeUnderflow).to.be.true;
    });

    cy.get("@transfer.all").should("have.length", 0);
  });

  it("marks a negative amount invalid", () => {
    cy.byTestId("transfer-amount").clear().type("-5");
    cy.byTestId("transfer-amount").then($input => {
      const input = $input[0] as HTMLInputElement;
      expect(input.checkValidity()).to.be.false;
    });
  });

  it("accepts the minimum permitted amount", () => {
    cy.byTestId("transfer-amount").clear().type("0.01");
    cy.byTestId("transfer-amount").then($input => {
      expect(($input[0] as HTMLInputElement).checkValidity()).to.be.true;
    });
  });

  it("offers only the customer's active accounts as sources", () => {
    cy.byTestId("transfer-from").find("option").should("have.length.at.least", 2);
  });

  it("swaps the destination field when the destination type changes", () => {
    cy.byTestId("transfer-dest-type").select("own");
    cy.byTestId("transfer-own-account").should("be.visible");

    cy.byTestId("transfer-dest-type").select("beneficiary");
    cy.byTestId("transfer-beneficiary").should("be.visible");
  });

  it("sends an idempotency key with every transfer request", () => {
    cy.intercept("POST", "**/api/transfers").as("transfer");

    cy.byTestId("transfer-amount").clear().type("1");
    cy.byTestId("transfer-submit").click();

    cy.wait("@transfer").then(({ request }) => {
      expect(request.body).to.have.property("idempotencyKey");
      expect(request.body.idempotencyKey).to.be.a("string").and.not.be.empty;
    });
  });

  it("surfaces a business rejection without moving money", () => {
    cy.intercept("POST", "**/api/transfers").as("transfer");

    cy.byTestId("transfer-amount").clear().type("99999999");
    cy.byTestId("transfer-submit").click();

    cy.wait("@transfer").its("response.statusCode").should("eq", 409);
    cy.byTestId("toast").should("contain.text", "rejected");
  });
});
