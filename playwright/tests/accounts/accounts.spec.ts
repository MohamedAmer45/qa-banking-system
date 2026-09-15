import {
  test,
  expect
} from "../../fixtures/testFixtures";

import {
  accountsData
} from "../../test-data/accountsData";


test.describe(
  "NovaBank - Accounts",
  () => {


    test.beforeEach(
      async ({
        authPage,
        accountsPage
      }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

        await accountsPage.open();

      }
    );


    test(
      "ACC-001 - customer can navigate to accounts",
      async ({ accountsPage }) => {

        await accountsPage.expectLoaded();

      }
    );


    test(
      "ACC-002 - expected number of accounts is displayed",
      async ({ accountsPage }) => {

        await accountsPage.expectAccountCount(
          accountsData.expectedAccountCount
        );

      }
    );


    test(
      "ACC-003 - checking account details are correct",
      async ({ accountsPage }) => {

        await accountsPage.expectAccount(
          accountsData.checking.type,
          accountsData.checking.number,
          accountsData.checking.balance
        );

      }
    );


    test(
      "ACC-004 - savings account details are correct",
      async ({ accountsPage }) => {

        await accountsPage.expectAccount(
          accountsData.savings.type,
          accountsData.savings.number,
          accountsData.savings.balance
        );

      }
    );


    test(
      "ACC-005 - account numbers are masked",
      async ({ accountsPage }) => {

        await accountsPage
          .expectAllAccountNumbersMasked();

      }
    );


    test(
      "ACC-006 - checking account number exposes only final four digits",
      async ({ accountsPage }) => {

        const number =
          await accountsPage
            .getAccountNumber("Checking");

        expect(number).toBe(
          accountsData.checking.number
        );

        expect(number).not.toContain(
          "00004821"
        );

      }
    );


    test(
      "ACC-007 - savings account number exposes only final four digits",
      async ({ accountsPage }) => {

        const number =
          await accountsPage
            .getAccountNumber("Savings");

        expect(number).toBe(
          accountsData.savings.number
        );

        expect(number).not.toContain(
          "00007742"
        );

      }
    );


    test(
      "ACC-008 - combined account balances equal expected total balance",
      async ({ accountsPage }) => {

        const checking =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        const savings =
          await accountsPage
            .getAccountBalance(
              "Savings"
            );

        expect(
          checking + savings
        ).toBeCloseTo(
          accountsData.expectedTotalBalance,
          2
        );

      }
    );


    test(
      "ACC-009 - account balances contain valid non-negative values",
      async ({ accountsPage }) => {

        const checking =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        const savings =
          await accountsPage
            .getAccountBalance(
              "Savings"
            );

        expect(checking).toBeGreaterThanOrEqual(
          0
        );

        expect(savings).toBeGreaterThanOrEqual(
          0
        );

      }
    );


    test(
      "ACC-010 - Accounts navigation remains selected after opening section",
      async ({
        accountsPage,
        page
      }) => {

        await expect(
          page.locator(
            '#nav button[data-s="accounts"]'
          )
        ).toHaveClass(
          /on/
        );

        await accountsPage.expectLoaded();

      }
    );

  }
);


test.describe(
  "NovaBank - Admin Account Access",
  () => {


    test(
      "ACC-011 - admin can access account information",
      async ({
        authPage,
        accountsPage
      }) => {

        await authPage.open();

        await authPage.loginAsAdmin();

        await accountsPage.open();

        await accountsPage.expectLoaded();

        await accountsPage.expectAccountCount(
          accountsData.expectedAccountCount
        );

      }
    );


    test(
      "ACC-012 - account total matches dashboard total",
      async ({
        authPage,
        dashboardPage,
        accountsPage
      }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

        const dashboardTotal =
          await dashboardPage
            .getDisplayedTotalBalance();

        await accountsPage.open();

        const checking =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        const savings =
          await accountsPage
            .getAccountBalance(
              "Savings"
            );

        expect(
          checking + savings
        ).toBeCloseTo(
          dashboardTotal,
          2
        );

      }
    );

  }
);
