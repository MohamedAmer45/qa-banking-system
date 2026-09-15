import {
  test,
  expect
} from "../../fixtures/testFixtures";

import {
  dashboardData
} from "../../test-data/dashboardData";


test.describe(
  "NovaBank - Dashboard",
  () => {


    test.beforeEach(
      async ({ authPage }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

      }
    );


    test(
      "DASH-001 - dashboard loads after customer authentication",
      async ({ dashboardPage }) => {

        await dashboardPage.expectLoaded();

      }
    );


    test(
      "DASH-002 - total balance is displayed correctly",
      async ({ dashboardPage }) => {

        await dashboardPage.expectTotalBalance(
          dashboardData.totalBalance
        );

      }
    );


    test(
      "DASH-003 - checking account balance is displayed correctly",
      async ({ dashboardPage }) => {

        await dashboardPage.expectMetric(
          dashboardData.checking.name,
          dashboardData.checking.balance
        );

      }
    );


    test(
      "DASH-004 - savings account balance is displayed correctly",
      async ({ dashboardPage }) => {

        await dashboardPage.expectMetric(
          dashboardData.savings.name,
          dashboardData.savings.balance
        );

      }
    );


    test(
      "DASH-005 - loan balance is displayed correctly",
      async ({ dashboardPage }) => {

        await dashboardPage.expectMetric(
          dashboardData.loan.name,
          dashboardData.loan.balance
        );

      }
    );


    test(
      "DASH-006 - total balance equals checking plus savings balances",
      async ({ dashboardPage }) => {

        const total =
          await dashboardPage
            .getDisplayedTotalBalance();

        const checking =
          await dashboardPage
            .getMetricBalance("Checking");

        const savings =
          await dashboardPage
            .getMetricBalance("Savings");

        expect(total).toBeCloseTo(
          checking + savings,
          2
        );

      }
    );


    test(
      "DASH-007 - dashboard shows exactly three recent transactions",
      async ({ dashboardPage }) => {

        await dashboardPage
          .expectRecentTransactionCount(3);

      }
    );


    test(
      "DASH-008 - salary deposit appears in recent transactions",
      async ({ dashboardPage }) => {

        const transaction =
          dashboardData.recentTransactions[0];

        await dashboardPage
          .expectRecentTransaction(
            transaction.description,
            transaction.amount
          );

      }
    );


    test(
      "DASH-009 - electricity payment appears in recent transactions",
      async ({ dashboardPage }) => {

        const transaction =
          dashboardData.recentTransactions[1];

        await dashboardPage
          .expectRecentTransaction(
            transaction.description,
            transaction.amount
          );

      }
    );


    test(
      "DASH-010 - card purchase appears in recent transactions",
      async ({ dashboardPage }) => {

        const transaction =
          dashboardData.recentTransactions[2];

        await dashboardPage
          .expectRecentTransaction(
            transaction.description,
            transaction.amount
          );

      }
    );

  }
);


test.describe(
  "NovaBank - Admin Dashboard Access",
  () => {


    test(
      "DASH-011 - admin can access the standard banking dashboard",
      async ({
        authPage,
        dashboardPage
      }) => {

        await authPage.open();

        await authPage.loginAsAdmin();

        await dashboardPage.expectLoaded();

        await dashboardPage
          .expectTotalBalance(
            dashboardData.totalBalance
          );

      }
    );

  }
);
