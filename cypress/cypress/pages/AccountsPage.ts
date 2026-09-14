class AccountsPage {

  private readonly customerLoginButton =
    "#login button.enter[data-role='customer']";

  private readonly accountsNavButton =
    "#nav button[data-s='accounts']";

  private readonly accountsSection =
    "#accounts";

  private readonly accountCards =
    "#accountCards .card";

  private readonly accountNumbers =
    "#accountCards .muted";

  private readonly accountBalances =
    "#accountCards .metric";


  visitAsCustomer(): void {
    cy.visit("/");

    cy.get(this.customerLoginButton)
      .should("be.visible")
      .and("be.enabled")
      .click();
  }


  openAccounts(): void {
    cy.get(this.accountsNavButton)
      .should("be.visible")
      .and("be.enabled")
      .click();
  }


  getAccountsSection(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(this.accountsSection);
  }


  getAccountCards(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(this.accountCards);
  }


  getAccountNumbers(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(this.accountNumbers);
  }


  getAccountBalances(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(this.accountBalances);
  }


  getAccountByType(type: string): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.contains(
      "#accountCards .card",
      new RegExp(type, "i")
    );
  }

}

export default new AccountsPage();
