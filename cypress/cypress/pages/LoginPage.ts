class LoginPage {

  private readonly loginContainer = "#login";

  private readonly customerButton =
    "#login button.enter[data-role='customer']";

  private readonly adminButton =
    "#login button.enter[data-role='admin']";

  private readonly app = "#app";

  private readonly loggedInUser = "#who";

  private readonly logoutButton = "#logout";

  private readonly adminNavigation = "#adminNav";


  visit(): void {
    cy.visit("/");
  }


  getLoginContainer(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(this.loginContainer);
  }


  getCustomerButton(): Cypress.Chainable<JQuery<HTMLButtonElement>> {
    return cy.get(this.customerButton);
  }


  getAdminButton(): Cypress.Chainable<JQuery<HTMLButtonElement>> {
    return cy.get(this.adminButton);
  }


  getApp(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(this.app);
  }


  getLoggedInUser(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(this.loggedInUser);
  }


  getLogoutButton(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(this.logoutButton);
  }


  getAdminNavigation(): Cypress.Chainable<JQuery<HTMLElement>> {
    return cy.get(this.adminNavigation);
  }


  loginAsCustomer(): void {
    this.getCustomerButton()
      .should("be.visible")
      .and("be.enabled")
      .click();
  }


  loginAsAdmin(): void {
    this.getAdminButton()
      .should("be.visible")
      .and("be.enabled")
      .click();
  }


  logout(): void {
    this.getLogoutButton()
      .should("be.visible")
      .click();
  }

}

export default new LoginPage();
