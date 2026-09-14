class CardsPage {

  private readonly customerLogin =
    "#login button.enter[data-role='customer']";

  private readonly cardsNav =
    "#nav button[data-s='cards']";

  private readonly cardsSection =
    "#cards";

  private readonly cardCards =
    "#cardCards .card";

  private readonly toast =
    "#toast";


  visitAsCustomer(): void {
    cy.visit("/");
    cy.loginAsCustomer();
  }


  openCards(): void {
    cy.openModule("cards");
  }


  getCardsSection() {
    return cy.get(this.cardsSection);
  }


  getCards() {
    return cy.get(this.cardCards);
  }


  getCardByType(type: string) {

    return cy.contains(
      "#cardCards .card",
      type
    );

  }


  getToast() {
    return cy.get(this.toast);
  }


  toggleCard(type: string): void {

    this.getCardByType(type)
      .find("button.toggle")
      .should("be.visible")
      .and("be.enabled")
      .click();

  }

}

export default new CardsPage();


