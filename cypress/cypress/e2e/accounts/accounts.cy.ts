import AccountsPage from "../../pages/AccountsPage";

describe("NovaBank - Accounts", () => {

  beforeEach(() => {

    AccountsPage.visitAsCustomer();
    AccountsPage.openAccounts();

  });


  it("should display the Accounts section", () => {

    AccountsPage.getAccountsSection()
      .should("be.visible")
      .and("have.class", "on");

    cy.contains("#accounts h1", /^Accounts$/)
      .should("be.visible");

  });


  it("should display both customer accounts", () => {

    AccountsPage.getAccountCards()
      .should("have.length", 2);

  });


  it("should display the Checking account correctly", () => {

    AccountsPage.getAccountByType("Checking")
      .should("be.visible")
      .within(() => {

        cy.contains("h2", /^Checking$/)
          .should("be.visible");

        cy.contains("**** 4821")
          .should("be.visible");

        cy.contains("$12,840.75")
          .should("be.visible");

      });

  });


  it("should display the Savings account correctly", () => {

    AccountsPage.getAccountByType("Savings")
      .should("be.visible")
      .within(() => {

        cy.contains("h2", /^Savings$/)
          .should("be.visible");

        cy.contains("**** 7742")
          .should("be.visible");

        cy.contains("$32,500.00")
          .should("be.visible");

      });

  });


  it("should mask all account numbers", () => {

    AccountsPage.getAccountNumbers()
      .should("have.length", 2)
      .each(($number) => {

        const accountNumber = $number.text().trim();

        expect(accountNumber)
          .to.match(/^\*{4} \d{4}$/);

      });

  });


  it("should display valid positive balances for all accounts", () => {

    AccountsPage.getAccountBalances()
      .should("have.length", 2)
      .each(($balance) => {

        const text = $balance.text()
          .replace("$", "")
          .replace(/,/g, "")
          .trim();

        const balance = Number(text);

        expect(balance).to.be.greaterThan(0);

      });

  });

});
