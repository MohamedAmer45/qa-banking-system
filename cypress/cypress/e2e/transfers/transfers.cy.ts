import TransfersPage from "../../pages/TransfersPage";

describe("NovaBank - Transfers", () => {

  beforeEach(() => {

    TransfersPage.visitAsCustomer();
    TransfersPage.openTransfers();

  });


  it("should display the transfer form and rules", () => {

    TransfersPage.getTransfersSection()
      .should("be.visible")
      .and("have.class", "on");

    cy.contains("#transfers h1", /^Transfers$/)
      .should("be.visible");

    TransfersPage.getFromAccount()
      .should("be.visible");

    TransfersPage.getRecipient()
      .should("be.visible");

    TransfersPage.getAmountInput()
      .should("be.visible");

    TransfersPage.getSubmitButton()
      .should("be.visible")
      .and("be.enabled");

    cy.contains(/Maximum single transfer: \$10,000/i)
      .should("be.visible");

  });


  it("should display both source accounts", () => {

    TransfersPage.getFromAccount()
      .find("option")
      .should("have.length", 2);

    TransfersPage.getFromAccount()
      .find("option")
      .eq(0)
      .should("contain.text", "Checking")
      .and("contain.text", "$12,840.75");

    TransfersPage.getFromAccount()
      .find("option")
      .eq(1)
      .should("contain.text", "Savings")
      .and("contain.text", "$32,500.00");

  });


  it("should display available recipients", () => {

    TransfersPage.getRecipient()
      .find("option")
      .should("have.length", 2);

    TransfersPage.getRecipient()
      .find("option")
      .eq(0)
      .should("have.text", "Alex Johnson");

    TransfersPage.getRecipient()
      .find("option")
      .eq(1)
      .should("have.text", "Sam Lee");

  });


  it("should reject an amount above the transfer limit", () => {

    TransfersPage.transfer(
      "0",
      "Alex Johnson",
      "10000.01"
    );

    TransfersPage.getToast()
      .should("be.visible")
      .and("contain.text", "Transfer limit exceeded.");

  });


  it("should enforce a positive transfer amount", () => {

    TransfersPage.getAmountInput()
      .clear()
      .type("0")
      .then(($input) => {

        const input =
          $input[0] as HTMLInputElement;

        expect(input.checkValidity())
          .to.equal(false);

        expect(input.validity.rangeUnderflow)
          .to.equal(true);

      });

  });


  it("should complete a valid transfer", () => {

    TransfersPage.transfer(
      "0",
      "Alex Johnson",
      "100"
    );

    TransfersPage.getToast()
      .should("be.visible")
      .and("contain.text", "Transfer completed.");

    TransfersPage.getFromAccount()
      .find("option")
      .eq(0)
      .should("contain.text", "$12,740.75");

  });


  it("should create a transaction after a successful transfer", () => {

    TransfersPage.transfer(
      "0",
      "Sam Lee",
      "250"
    );

    TransfersPage.getToast()
      .should("contain.text", "Transfer completed.");

    cy.get("#nav button[data-s='transactions']")
      .click();

    cy.get("#transactions")
      .should("be.visible")
      .and("have.class", "on");

    cy.contains("#tx", "Transfer to Sam Lee")
      .should("be.visible");

    cy.contains("#tx", "-$250.00")
      .should("be.visible");

  });


  it("should allow a transfer at the exact $10,000 limit", () => {

    TransfersPage.transfer(
      "0",
      "Alex Johnson",
      "10000"
    );

    TransfersPage.getToast()
      .should("be.visible")
      .and("contain.text", "Transfer completed.");

    TransfersPage.getFromAccount()
      .find("option")
      .eq(0)
      .should("contain.text", "$2,840.75");

  });

});
