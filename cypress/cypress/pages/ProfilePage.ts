class ProfilePage {

  private readonly customerLogin =
    "#login button.enter[data-role='customer']";

  private readonly profileNav =
    "#nav button[data-s='profile']";

  private readonly profileSection =
    "#profile";

  private readonly profileCard =
    "#profileCard";

  private readonly loggedInUser =
    "#who";


  visitAsCustomer(): void {
    cy.visit("/");
    cy.loginAsCustomer();
  }


  openProfile(): void {
    cy.openModule("profile");
  }


  getProfileSection() {
    return cy.get(this.profileSection);
  }


  getProfileCard() {
    return cy.get(this.profileCard);
  }


  getProfileName() {
    return cy.get("#profileCard h2");
  }


  getProfileEmail() {
    return cy.get("#profileCard p")
      .first();
  }


  getProfileRole() {
    return cy.get("#profileCard p.muted");
  }


  getLoggedInUser() {
    return cy.get(this.loggedInUser);
  }

}

export default new ProfilePage();


