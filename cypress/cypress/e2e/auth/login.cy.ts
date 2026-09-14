import LoginPage from "../../pages/LoginPage";

describe("NovaBank - Login", () => {

  beforeEach(() => {
    LoginPage.visit();
  });


  it("should display the login screen", () => {

    LoginPage.getLoginContainer()
      .should("be.visible");

  });


  it("should display the customer login option", () => {

    LoginPage.getCustomerButton()
      .should("be.visible")
      .and("be.enabled");

  });


  it("should display the admin login option", () => {

    LoginPage.getAdminButton()
      .should("be.visible")
      .and("be.enabled");

  });


  it("should log in as a customer", () => {

    LoginPage.loginAsCustomer();

    LoginPage.getApp()
      .should("be.visible");

    LoginPage.getLoggedInUser()
      .should("be.visible")
      .invoke("text")
      .should("not.be.empty");

  });


  it("should log in as an admin", () => {

    LoginPage.loginAsAdmin();

    LoginPage.getApp()
      .should("be.visible");

    LoginPage.getLoggedInUser()
      .should("be.visible")
      .invoke("text")
      .should("not.be.empty");

    LoginPage.getAdminNavigation()
      .should("be.visible");

  });


  it("should allow the user to logout", () => {

    LoginPage.loginAsCustomer();

    LoginPage.getApp()
      .should("be.visible");

    LoginPage.logout();

    LoginPage.getLoginContainer()
      .should("be.visible");

  });

});
