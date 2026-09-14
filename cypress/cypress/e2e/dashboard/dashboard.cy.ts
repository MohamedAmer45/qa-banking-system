import DashboardPage from "../../pages/DashboardPage";

describe("NovaBank - Customer Dashboard", () => {

  beforeEach(() => {
    DashboardPage.visitAsCustomer();
  });


  it("should display the dashboard after customer login", () => {

    DashboardPage.getDashboardHeading()
      .should("be.visible");

  });


  it("should display the account overview", () => {

    DashboardPage.getAccountOverview()
      .should("be.visible");

  });


  it("should display the total balance", () => {

    DashboardPage.getTotalBalanceLabel()
      .should("be.visible");

  });


  it("should display recent transactions", () => {

    DashboardPage.getRecentTransactionsHeading()
      .should("be.visible");

  });


  it("should display all customer navigation modules", () => {

    const modules = [
      "Dashboard",
      "Accounts",
      "Transfers",
      "Transactions",
      "Bills",
      "Cards",
      "Loans",
      "Notifications",
      "Profile"
    ];

    modules.forEach((module) => {

      DashboardPage.getNavigationButton(module)
        .should("be.visible")
        .and("be.enabled");

    });

  });


  it("should navigate from Dashboard to Accounts", () => {

    DashboardPage.openModule("Accounts");

    cy.contains("h1", /^Accounts$/i)
      .should("be.visible");

  });

});
