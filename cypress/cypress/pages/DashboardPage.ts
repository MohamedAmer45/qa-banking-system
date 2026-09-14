class DashboardPage {

  visitAsCustomer(): void {
    cy.visit("/");
    cy.loginAsCustomer();
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

    const module =
      label.toLowerCase() as
        | "dashboard"
        | "accounts"
        | "transfers"
        | "transactions"
        | "bills"
        | "cards"
        | "loans"
        | "notifications"
        | "profile"
        | "admin";

    cy.openModule(module);
  }

}

export default new DashboardPage();


