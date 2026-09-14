class LoansPage {

  private readonly customerLogin =
    "#login button.enter[data-role='customer']";

  private readonly loansNav =
    "#nav button[data-s='loans']";

  private readonly loansSection =
    "#loans";

  private readonly loanInfo =
    "#loanInfo";

  private readonly amountInput =
    "#loanAmount";

  private readonly termSelect =
    "#term";

  private readonly submitButton =
    "#loanForm button";

  private readonly toast =
    "#toast";


  visitAsCustomer(): void {
    cy.visit("/");
    cy.loginAsCustomer();
  }


  openLoans(): void {
    cy.openModule("loans");
  }


  getLoansSection() {
    return cy.get(this.loansSection);
  }


  getLoanInfo() {
    return cy.get(this.loanInfo);
  }


  getAmountInput() {
    return cy.get(this.amountInput);
  }


  getTermSelect() {
    return cy.get(this.termSelect);
  }


  getSubmitButton() {
    return cy.get(this.submitButton);
  }


  getToast() {
    return cy.get(this.toast);
  }


  enterAmount(amount: string): void {

    this.getAmountInput()
      .clear()
      .type(amount);

  }


  selectTerm(term: "12" | "24" | "36"): void {

    this.getTermSelect()
      .select(term);

  }


  submit(): void {

    this.getSubmitButton()
      .click();

  }


  applyForLoan(
    amount: string,
    term: "12" | "24" | "36"
  ): void {

    this.enterAmount(amount);
    this.selectTerm(term);
    this.submit();

  }

}

export default new LoansPage();


