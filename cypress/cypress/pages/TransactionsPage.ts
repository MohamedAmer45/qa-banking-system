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
    cy.loginAsCustomer();
  }


  openTransactions(): void {
    cy.openModule("transactions");
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


