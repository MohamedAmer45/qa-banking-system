describe("NovaBank - Framework State Isolation", () => {

  it("should restore default account state after a reset", () => {

    cy.visit("/");
    cy.loginAsCustomer();

    cy.window().then((win) => {

      win.localStorage.setItem(
        "nb_state",
        JSON.stringify({
          accounts: [
            {
              type: "Corrupted Test Account",
              number: "**** 0000",
              balance: 1
            }
          ],
          transactions: [],
          cards: [],
          loan: {
            type: "Test",
            balance: 0,
            apr: 0,
            nextPayment: 0,
            dueDate: "2000-01-01"
          },
          notes: []
        })
      );

    });

    cy.reload();

    cy.resetNovaBankState();
    cy.loginAsCustomer();

    cy.openModule("accounts");

    cy.get("#accountCards .card")
      .should("have.length", 2);

    cy.contains(
      "#accountCards .card",
      "Checking"
    )
      .should("contain.text", "$12,840.75");

    cy.contains(
      "#accountCards .card",
      "Savings"
    )
      .should("contain.text", "$32,500.00");

  });


  it("should start a clean customer session", () => {

    cy.visit("/");
    cy.loginAsCustomer();

    cy.get("#who")
      .should("contain.text", "customer");

    cy.get("#adminNav")
      .should("have.class", "hidden");

  });


  it("should start a clean admin session", () => {

    cy.visit("/");
    cy.loginAsAdmin();

    cy.get("#who")
      .should("contain.text", "admin");

    cy.get("#adminNav")
      .should("be.visible")
      .and("not.have.class", "hidden");

  });

});
