class TransfersPage {

  private readonly customerLogin =
    "#login button.enter[data-role='customer']";

  private readonly transfersNav =
    "#nav button[data-s='transfers']";

  private readonly transfersSection =
    "#transfers";

  private readonly fromAccount =
    "#from";

  private readonly recipient =
    "#recipient";

  private readonly amount =
    "#transferAmount";

  private readonly submitButton =
    "#transferForm button[type='submit'], #transferForm button";

  private readonly toast =
    "#toast";


  visitAsCustomer(): void {
    cy.visit("/");
    cy.loginAsCustomer();
  }


  openTransfers(): void {
    cy.openModule("transfers");
  }


  getTransfersSection() {
    return cy.get(this.transfersSection);
  }


  getFromAccount() {
    return cy.get(this.fromAccount);
  }


  getRecipient() {
    return cy.get(this.recipient);
  }


  getAmountInput() {
    return cy.get(this.amount);
  }


  getSubmitButton() {
    return cy.get(this.submitButton);
  }


  getToast() {
    return cy.get(this.toast);
  }


  selectAccount(account: "0" | "1"): void {
    this.getFromAccount().select(account);
  }


  selectRecipient(name: string): void {
    this.getRecipient().select(name);
  }


  enterAmount(amount: string): void {
    this.getAmountInput()
      .clear()
      .type(amount);
  }


  submit(): void {
    this.getSubmitButton().click();
  }


  transfer(
    account: "0" | "1",
    recipient: string,
    amount: string
  ): void {

    this.selectAccount(account);
    this.selectRecipient(recipient);
    this.enterAmount(amount);
    this.submit();
  }

}

export default new TransfersPage();


