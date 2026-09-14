import AdminPage from "../../pages/AdminPage";

describe("NovaBank - Admin", () => {

  it("should hide the Admin navigation option from customers", () => {

    AdminPage.visit();
    AdminPage.loginAsCustomer();

    AdminPage.getAdminNav()
      .should("have.class", "hidden");

  });


  it("should show the Admin navigation option to admins", () => {

    AdminPage.visit();
    AdminPage.loginAsAdmin();

    AdminPage.getAdminNav()
      .should("be.visible")
      .and("not.have.class", "hidden");

  });


  it("should display the admin role in the header", () => {

    AdminPage.visit();
    AdminPage.loginAsAdmin();

    AdminPage.getLoggedInUser()
      .should("be.visible")
      .invoke("text")
      .should("match", /admin/i);

  });


  it("should open the Admin section", () => {

    AdminPage.visit();
    AdminPage.loginAsAdmin();
    AdminPage.openAdmin();

    AdminPage.getAdminSection()
      .should("be.visible")
      .and("have.class", "on");

    cy.contains(
      "#admin h1",
      /^Admin console$/
    )
      .should("be.visible");

  });


  it("should load admin summary cards", () => {

    cy.intercept(
      "GET",
      "**/api/admin/summary"
    ).as("adminSummary");

    AdminPage.visit();
    AdminPage.loginAsAdmin();

    cy.wait("@adminSummary")
      .its("response.statusCode")
      .should("eq", 200);

    AdminPage.openAdmin();

    AdminPage.getAdminCards()
      .should("have.length.greaterThan", 0);

  });


  it("should display numeric admin summary values", () => {

    AdminPage.visit();
    AdminPage.loginAsAdmin();

    cy.get("#adminData .card")
      .should("have.length.greaterThan", 0)
      .each(($card) => {

        cy.wrap($card)
          .find(".metric")
          .invoke("text")
          .then((value) => {

            const normalized =
              value.replace(/,/g, "").trim();

            expect(normalized)
              .to.match(/^\d+$/);

          });

      });

  });


  it("should not allow customer navigation into the Admin section", () => {

    AdminPage.visit();
    AdminPage.loginAsCustomer();

    AdminPage.getAdminNav()
      .should("have.class", "hidden");

    AdminPage.getAdminSection()
      .should("not.be.visible");

  });

});
