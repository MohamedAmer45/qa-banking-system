class NotificationsPage {

  private readonly customerLogin =
    "#login button.enter[data-role='customer']";

  private readonly notificationsNav =
    "#nav button[data-s='notifications']";

  private readonly notificationsSection =
    "#notifications";

  private readonly notificationCards =
    "#notes .card";


  visitAsCustomer(): void {

    cy.visit("/");

    cy.get(this.customerLogin)
      .should("be.visible")
      .and("be.enabled")
      .click();

  }


  openNotifications(): void {

    cy.get(this.notificationsNav)
      .should("be.visible")
      .and("be.enabled")
      .click();

  }


  getNotificationsSection() {
    return cy.get(this.notificationsSection);
  }


  getNotifications() {
    return cy.get(this.notificationCards);
  }


  getNotification(text: string) {

    return cy.contains(
      "#notes .card",
      text
    );

  }

}

export default new NotificationsPage();
