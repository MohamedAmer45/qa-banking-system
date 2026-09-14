import LoansPage from "../../pages/LoansPage";

describe("NovaBank - Loans", () => {

  beforeEach(() => {

    LoansPage.visitAsCustomer();
    LoansPage.openLoans();

  });


  it("should display the Loans section", () => {

    LoansPage.getLoansSection()
      .should("be.visible")
      .and("have.class", "on");

    cy.contains("#loans h1", /^Loans$/)
      .should("be.visible");

  });


  it("should display the existing loan details", () => {

    LoansPage.getLoanInfo()
      .should("be.visible")
      .within(() => {

        cy.contains("Personal Loan")
          .should("be.visible");

        cy.contains("$6,450.00")
          .should("be.visible");

        cy.contains("APR 7.9%")
          .should("be.visible");

        cy.contains("$320.00")
          .should("be.visible");

        cy.contains("2026-10-05")
          .should("be.visible");

      });

  });


  it("should configure the correct loan amount boundaries", () => {

    LoansPage.getAmountInput()
      .should("have.attr", "min", "1000")
      .and("have.attr", "max", "50000")
      .and("have.attr", "required");

  });


  it("should display all available loan terms", () => {

    LoansPage.getTermSelect()
      .find("option")
      .should("have.length", 3)
      .then(($options) => {

        const terms = [...$options]
          .map((option) =>
            option.textContent?.trim()
          );

        expect(terms).to.deep.equal([
          "12 months",
          "24 months",
          "36 months"
        ]);

      });

  });


  it("should reject an amount below $1,000", () => {

    LoansPage.getAmountInput()
      .clear()
      .type("999")
      .then(($input) => {

        const input =
          $input[0] as HTMLInputElement;

        expect(input.checkValidity())
          .to.equal(false);

        expect(input.validity.rangeUnderflow)
          .to.equal(true);

      });

  });


  it("should reject an amount above $50,000", () => {

    LoansPage.getAmountInput()
      .clear()
      .type("50001")
      .then(($input) => {

        const input =
          $input[0] as HTMLInputElement;

        expect(input.checkValidity())
          .to.equal(false);

        expect(input.validity.rangeOverflow)
          .to.equal(true);

      });

  });


  it("should submit a valid loan application with the correct API payload", () => {

    cy.intercept("POST", "**/api/loans/apply")
      .as("loanApplication");

    LoansPage.applyForLoan(
      "2500",
      "24"
    );

    cy.wait("@loanApplication")
      .then((interception) => {

        expect(interception.request.body)
          .to.deep.equal({
            amount: 2500,
            termMonths: 24
          });

        expect(interception.response?.statusCode)
          .to.be.oneOf([200, 201]);

      });

    LoansPage.getToast()
      .should("be.visible")
      .and("contain.text", "Loan application submitted.");

  });


  it("should create a notification after a successful loan application", () => {

    cy.intercept("POST", "**/api/loans/apply")
      .as("loanApplication");

    LoansPage.applyForLoan(
      "5000",
      "36"
    );

    cy.wait("@loanApplication");

    LoansPage.getToast()
      .should("contain.text", "Loan application submitted.");

    cy.get("#nav button[data-s='notifications']")
      .click();

    cy.get("#notifications")
      .should("be.visible")
      .and("have.class", "on");

    cy.get("#notes .card")
      .first()
      .invoke("text")
      .should(
        "match",
        /Loan application .+ is under review\./
      );

  });

});
