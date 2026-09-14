describe("NovaBank - Smoke Test", () => {

  it("should load the banking application successfully", () => {

    cy.visit("/");

    cy.get("body")
      .should("be.visible");

    cy.location("origin")
      .should("eq", "https://novabank-banking-system.vercel.app");

    cy.title()
      .should("not.be.empty");

  });

});
