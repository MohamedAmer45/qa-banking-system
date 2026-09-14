class DashboardPage {

  visitAsCustomer(): void {
    cy.visit("/");

    cy.contains("button", /^Enter as customer$/i)
      .should("be.visible")
      .click();
  }


  getDashboardHeading(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.contains("h1", /^Dashboard$/i);
  }


  getAccountOverview(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.contains(/Account overview and recent activity/i);
  }


  getTotalBalanceLabel(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.contains(/^Total balance$/i);
  }


  getRecentTransactionsHeading(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.contains("h2", /^Recent transactions$/i);
  }


  getNavigationButton(label: string): Cypress.Chainable<JQuery<HTMLButtonElement>> {
    return cy.contains("button", new RegExp(`^${label}$`, "i"));
  }


  openModule(label: string): void {
    this.getNavigationButton(label)
      .should("be.visible")
      .and("be.enabled")
      .click();
  }

}

export default new DashboardPage();
