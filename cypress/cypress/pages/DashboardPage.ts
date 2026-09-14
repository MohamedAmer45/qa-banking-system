class DashboardPage {

  visitAsCustomer(): void {
    cy.visit("/");

    cy.get("#login button.enter[data-role='customer']")
      .should("be.visible")
      .and("be.enabled")
      .click();
  }


  getDashboardHeading() {
    return cy.get("#dashboard h1");
  }


  getAccountOverview() {
    return cy.get("#dashboard p.muted");
  }


  getTotalBalanceLabel() {
    return cy.get("#dashboard .hero .muted");
  }


  getTotalBalance() {
    return cy.get("#total");
  }


  getRecentTransactionsHeading() {
    return cy.get("#dashboard .card h2");
  }


  getNavigationButton(label: string) {
    return cy.contains(
      "#nav button",
      new RegExp(`^${label}$`, "i")
    );
  }


  openModule(label: string): void {
    this.getNavigationButton(label)
      .should("be.visible")
      .and("be.enabled")
      .click();
  }

}

export default new DashboardPage();
