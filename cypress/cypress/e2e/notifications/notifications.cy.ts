import NotificationsPage from "../../pages/NotificationsPage";

describe("NovaBank - Notifications", () => {

  beforeEach(() => {

    NotificationsPage.visitAsCustomer();
    NotificationsPage.openNotifications();

  });


  it("should display the Notifications section", () => {

    NotificationsPage.getNotificationsSection()
      .should("be.visible")
      .and("have.class", "on");

    cy.contains(
      "#notifications h1",
      /^Notifications$/
    )
      .should("be.visible");

  });


  it("should display the default notifications", () => {

    NotificationsPage.getNotifications()
      .should("have.length", 2);

  });


  it("should display the monthly statement notification", () => {

    NotificationsPage
      .getNotification(
        "Your monthly statement is ready."
      )
      .should("be.visible");

  });


  it("should display the card usage notification", () => {

    NotificationsPage
      .getNotification(
        "Card ending 4242 was used for $42.90."
      )
      .should("be.visible");

  });


  it("should display notifications in the expected order", () => {

    NotificationsPage.getNotifications()
      .then(($notifications) => {

        const texts = [...$notifications]
          .map((notification) =>
            notification.textContent?.trim()
          );

        expect(texts).to.deep.equal([
          "Your monthly statement is ready.",
          "Card ending 4242 was used for $42.90."
        ]);

      });

  });


  it("should not display empty notification cards", () => {

    NotificationsPage.getNotifications()
      .each(($notification) => {

        const text =
          $notification.text().trim();

        expect(text)
          .to.not.equal("");

      });

  });


  it("should add a loan application notification", () => {

    cy.get("#nav button[data-s='loans']")
      .click();

    cy.intercept(
      "POST",
      "**/api/loans/apply"
    )
      .as("loanApplication");

    cy.get("#loanAmount")
      .clear()
      .type("3000");

    cy.get("#term")
      .select("24");

    cy.get("#loanForm button")
      .click();

    cy.wait("@loanApplication")
      .its("response.statusCode")
      .should("be.oneOf", [200, 201]);

    cy.get("#nav button[data-s='notifications']")
      .click();

    NotificationsPage.getNotifications()
      .should("have.length", 3);

    NotificationsPage.getNotifications()
      .first()
      .invoke("text")
      .should(
        "match",
        /^Loan application .+ is under review\.$/
      );

  });

});
