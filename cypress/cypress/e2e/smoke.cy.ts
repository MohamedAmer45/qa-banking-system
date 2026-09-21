describe("NovaBank - smoke", () => {
  it("serves the sign-in screen", () => {
    cy.visit("/");
    cy.byTestId("login-form").should("be.visible");
    cy.byTestId("login-email").should("be.visible");
    cy.byTestId("login-submit").should("be.enabled");
  });

  it("reports a PostgreSQL-backed service", () => {
    cy.request("/api/health").then(response => {
      expect(response.status).to.eq(200);
      expect(response.body.status).to.eq("ok");
      expect(response.body.database).to.eq("postgresql");
    });
  });

  it("signs a customer in and lands on the overview", () => {
    cy.login();
    cy.byTestId("page-title").should("contain.text", "Overview");
  });
});
