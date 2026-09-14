class BillsPage {

  private readonly customerLogin =
    "#login button.enter[data-role='customer']";

  private readonly billsNav =
    "#nav button[data-s='bills']";

  private readonly billsSection =
    "#bills";

  private readonly billerSelect =
    "#biller";

  private readonly amountInput =
    "#billAmount";

  private readonly submitButton =
    "#billForm button";

  private readonly toast =
    "#toast";


  visitAsCustomer(): void {
    cy.visit("/");
    cy.loginAsCustomer();
  }


  openBills(): void {
    cy.openModule("bills");
  }


  getBillsSection() {
    return cy.get(this.billsSection);
  }


  getBillerSelect() {
    return cy.get(this.billerSelect);
  }


  getAmountInput() {
    return cy.get(this.amountInput);
  }


  getSubmitButton() {
    return cy.get(this.submitButton);
  }


  getToast() {
    return cy.get(this.toast);
  }


  selectBiller(biller: string): void {

    this.getBillerSelect()
      .select(biller);

  }


  enterAmount(amount: string): void {

    this.getAmountInput()
      .clear()
      .type(amount);

  }


  submit(): void {

    this.getSubmitButton()
      .click();

  }


  payBill(
    biller: string,
    amount: string
  ): void {

    this.selectBiller(biller);
    this.enterAmount(amount);
    this.submit();

  }

}

export default new BillsPage();


