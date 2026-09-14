import TransactionsPage from "../../pages/TransactionsPage";

describe("NovaBank - Transactions", () => {

  beforeEach(() => {

    TransactionsPage.visitAsCustomer();
    TransactionsPage.openTransactions();

  });


  it("should display the Transactions section", () => {

    TransactionsPage.getTransactionsSection()
      .should("be.visible")
      .and("have.class", "on");

    cy.contains("#transactions h1", /^Transactions$/)
      .should("be.visible");

  });


  it("should display the transaction table headers", () => {

    TransactionsPage.getHeaders()
      .should("have.length", 4)
      .then(($headers) => {

        const headers = [...$headers]
          .map((header) =>
            header.textContent?.trim()
          );

        expect(headers).to.deep.equal([
          "Date",
          "Description",
          "Status",
          "Amount"
        ]);

      });

  });


  it("should display the default transaction history", () => {

    TransactionsPage.getRows()
      .should("have.length", 3);

  });


  it("should display the salary deposit as a credit", () => {

    TransactionsPage
      .getTransaction("Salary deposit")
      .should("be.visible")
      .within(() => {

        cy.contains("2026-09-13")
          .should("be.visible");

        cy.contains("completed")
          .should("be.visible");

        cy.contains("+$5,200.00")
          .should("be.visible")
          .and("have.class", "credit");

      });

  });


  it("should display the electricity bill as a debit", () => {

    TransactionsPage
      .getTransaction("Electricity bill")
      .should("be.visible")
      .within(() => {

        cy.contains("2026-09-12")
          .should("be.visible");

        cy.contains("completed")
          .should("be.visible");

        cy.contains("-$86.35")
          .should("be.visible")
          .and("have.class", "debit");

      });

  });


  it("should display the card purchase as a debit", () => {

    TransactionsPage
      .getTransaction("Card purchase")
      .should("be.visible")
      .within(() => {

        cy.contains("2026-09-11")
          .should("be.visible");

        cy.contains("completed")
          .should("be.visible");

        cy.contains("-$42.90")
          .should("be.visible")
          .and("have.class", "debit");

      });

  });


  it("should show all transactions as completed", () => {

    TransactionsPage.getRows()
      .each(($row) => {

        cy.wrap($row)
          .find(".tag")
          .should("have.text", "completed");

      });

  });


  it("should display transactions newest first", () => {

    TransactionsPage.getRows()
      .find("td:first-child")
      .then(($dates) => {

        const dates = [...$dates]
          .map((date) =>
            date.textContent?.trim() ?? ""
          );

        const sortedDates = [...dates]
          .sort((a, b) =>
            b.localeCompare(a)
          );

        expect(dates)
          .to.deep.equal(sortedDates);

      });

  });

});
