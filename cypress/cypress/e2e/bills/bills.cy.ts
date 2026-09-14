import BillsPage from "../../pages/BillsPage";

describe("NovaBank - Bills", () => {

  beforeEach(() => {

    BillsPage.visitAsCustomer();
    BillsPage.openBills();

  });


  it("should display the Bills section", () => {

    BillsPage.getBillsSection()
      .should("be.visible")
      .and("have.class", "on");

    cy.contains("#bills h1", /^Bills$/)
      .should("be.visible");

  });


  it("should display all available billers", () => {

    BillsPage.getBillerSelect()
      .find("option")
      .should("have.length", 4)
      .then(($options) => {

        const billers = [...$options]
          .map((option) =>
            option.textContent?.trim()
          );

        expect(billers).to.deep.equal([
          "Electricity",
          "Water",
          "Internet",
          "Mobile"
        ]);

      });

  });


  it("should require a bill amount", () => {

    BillsPage.getAmountInput()
      .should("have.attr", "required");

  });


  it("should enforce a minimum bill amount of $0.01", () => {

    BillsPage.getAmountInput()
      .should("have.attr", "min", "0.01");

    BillsPage.getAmountInput()
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


  it("should reject a bill amount greater than the available balance", () => {

    BillsPage.payBill(
      "Electricity",
      "20000"
    );

    BillsPage.getToast()
      .should("be.visible")
      .and("contain.text", "Invalid bill amount.");

  });


  it("should successfully pay a valid bill", () => {

    BillsPage.payBill(
      "Internet",
      "100"
    );

    BillsPage.getToast()
      .should("be.visible")
      .and("contain.text", "Bill paid.");

  });


  it("should reduce the checking balance after bill payment", () => {

    BillsPage.payBill(
      "Water",
      "100"
    );

    BillsPage.getToast()
      .should("contain.text", "Bill paid.");

    cy.get("#nav button[data-s='accounts']")
      .click();

    cy.contains(
      "#accountCards .card",
      "Checking"
    )
      .should("contain.text", "$12,740.75");

  });


  it("should create a transaction after bill payment", () => {

    BillsPage.payBill(
      "Mobile",
      "75.50"
    );

    BillsPage.getToast()
      .should("contain.text", "Bill paid.");

    cy.get("#nav button[data-s='transactions']")
      .click();

    cy.get("#transactions")
      .should("be.visible")
      .and("have.class", "on");

    cy.contains(
      "#tx tbody tr",
      "Mobile bill"
    )
      .should("be.visible")
      .within(() => {

        cy.contains("completed")
          .should("be.visible");

        cy.contains("-$75.50")
          .should("be.visible")
          .and("have.class", "debit");

      });

  });

});
