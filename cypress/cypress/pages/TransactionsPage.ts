class TransactionsPage {

  private readonly customerLogin =
    "#login button.enter[data-role='customer']";

  private readonly transactionsNav =
    "#nav button[data-s='transactions']";

  private readonly transactionsSection =
    "#transactions";

  private readonly transactionTable =
    "#tx table";

  private readonly rows =
    "#tx tbody tr";


  visitAsCustomer(): void {

    cy.visit("/");

    cy.get(this.customerLogin)
      .should("be.visible")
      .and("be.enabled")
      .click();

  }


  openTransactions(): void {

    cy.get(this.transactionsNav)
      .should("be.visible")
      .and("be.enabled")
      .click();

  }


  getTransactionsSection() {
    return cy.get(this.transactionsSection);
  }


  getTable() {
    return cy.get(this.transactionTable);
  }


  getRows() {
    return cy.get(this.rows);
  }


  getHeaders() {
    return cy.get("#tx thead th");
  }


  getTransaction(description: string) {

    return cy.contains(
      "#tx tbody tr",
      description
    );

  }

}

export default new TransactionsPage();
