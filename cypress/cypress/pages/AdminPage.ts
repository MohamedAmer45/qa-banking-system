class AdminPage {

  private readonly customerLogin =
    "#login button.enter[data-role='customer']";

  private readonly adminLogin =
    "#login button.enter[data-role='admin']";

  private readonly adminNav =
    "#adminNav";

  private readonly adminSection =
    "#admin";

  private readonly adminCards =
    "#adminData .card";

  private readonly loggedInUser =
    "#who";


  visit(): void {
    cy.visit("/");
  }


  loginAsCustomer(): void {
    cy.loginAsCustomer();
  }


  loginAsAdmin(): void {
    cy.loginAsAdmin();
  }


  openAdmin(): void {
    cy.openModule("admin");
  }


  getAdminNav() {
    return cy.get(this.adminNav);
  }


  getAdminSection() {
    return cy.get(this.adminSection);
  }


  getAdminCards() {
    return cy.get(this.adminCards);
  }


  getLoggedInUser() {
    return cy.get(this.loggedInUser);
  }

}

export default new AdminPage();

