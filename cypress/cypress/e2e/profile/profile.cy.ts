import ProfilePage from "../../pages/ProfilePage";

describe("NovaBank - Profile", () => {

  beforeEach(() => {

    ProfilePage.visitAsCustomer();
    ProfilePage.openProfile();

  });


  it("should display the Profile section", () => {

    ProfilePage.getProfileSection()
      .should("be.visible")
      .and("have.class", "on");

    cy.contains(
      "#profile h1",
      /^Profile$/
    )
      .should("be.visible");

  });


  it("should display the customer profile card", () => {

    ProfilePage.getProfileCard()
      .should("be.visible");

  });


  it("should display a customer name", () => {

    ProfilePage.getProfileName()
      .should("be.visible")
      .invoke("text")
      .then((name) => {

        expect(name.trim())
          .to.not.equal("");

      });

  });


  it("should display a valid email address", () => {

    ProfilePage.getProfileEmail()
      .should("be.visible")
      .invoke("text")
      .then((email) => {

        expect(email.trim())
          .to.match(
            /^[^\s@]+@[^\s@]+\.[^\s@]+$/
          );

      });

  });


  it("should display the customer role", () => {

    ProfilePage.getProfileRole()
      .should("be.visible")
      .and("contain.text", "Role: customer");

  });


  it("should show the same user name in the profile and header", () => {

    ProfilePage.getProfileName()
      .invoke("text")
      .then((profileName) => {

        ProfilePage.getLoggedInUser()
          .invoke("text")
          .then((headerText) => {

            expect(headerText)
              .to.contain(profileName.trim());

          });

      });

  });


  it("should show the customer role in the header", () => {

    ProfilePage.getLoggedInUser()
      .should("be.visible")
      .invoke("text")
      .should(
        "match",
        /customer/i
      );

  });

});
