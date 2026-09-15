import {
  test,
  expect
} from "../../fixtures/testFixtures";

import {
  billsData
} from "../../test-data/billsData";


test.describe(
  "NovaBank - Bills",
  () => {


    test.beforeEach(
      async ({
        authPage,
        billsPage
      }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

        await billsPage.open();

      }
    );


    test(
      "BILL-001 - customer can navigate to Bills",
      async ({
        billsPage
      }) => {

        await billsPage
          .expectLoaded();

      }
    );


    test(
      "BILL-002 - supported billers are displayed",
      async ({
        billsPage
      }) => {

        await billsPage
          .expectBillers(
            billsData.billers
          );

      }
    );


    test(
      "BILL-003 - successful Electricity payment decreases Checking balance",
      async ({
        billsPage,
        accountsPage
      }) => {

        await accountsPage.open();

        const before =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        await billsPage.open();

        await billsPage.payBill(
          "Electricity",
          100
        );

        await accountsPage.open();

        const after =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        expect(
          after
        ).toBeCloseTo(
          before - 100,
          2
        );

      }
    );


    test(
      "BILL-004 - bill payment does not change Savings balance",
      async ({
        billsPage,
        accountsPage
      }) => {

        await accountsPage.open();

        const savingsBefore =
          await accountsPage
            .getAccountBalance(
              "Savings"
            );

        await billsPage.open();

        await billsPage.payBill(
          "Water",
          75
        );

        await accountsPage.open();

        const savingsAfter =
          await accountsPage
            .getAccountBalance(
              "Savings"
            );

        expect(
          savingsAfter
        ).toBeCloseTo(
          savingsBefore,
          2
        );

      }
    );


    test(
      "BILL-005 - successful bill payment creates debit transaction",
      async ({
        billsPage,
        transactionsPage
      }) => {

        await billsPage.payBill(
          "Internet",
          120
        );

        await transactionsPage.open();

        await transactionsPage
          .expectFirstTransaction(
            "Internet bill",
            "-$120.00"
          );

        await transactionsPage
          .expectDebitTransaction(
            "Internet bill"
          );

      }
    );


    test(
      "BILL-006 - successful bill payment increases transaction count",
      async ({
        billsPage,
        transactionsPage
      }) => {

        await billsPage.payBill(
          "Mobile",
          50
        );

        await transactionsPage.open();

        await transactionsPage
          .expectTransactionCount(4);

      }
    );


    test(
      "BILL-007 - bill amount greater than available Checking balance is rejected",
      async ({
        billsPage,
        accountsPage
      }) => {

        await accountsPage.open();

        const before =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        await billsPage.open();

        await billsPage
          .attemptRejectedBill(
            "Electricity",
            before + 0.01,
            "Invalid bill amount."
          );

        await accountsPage.open();

        const after =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        expect(
          after
        ).toBeCloseTo(
          before,
          2
        );

      }
    );


    test(
      "BILL-008 - zero bill amount is rejected by input validation",
      async ({
        billsPage
      }) => {

        await billsPage
          .expectNativeAmountInvalid(
            0
          );

      }
    );


    test(
      "BILL-009 - negative bill amount is rejected by input validation",
      async ({
        billsPage
      }) => {

        await billsPage
          .expectNativeAmountInvalid(
            -1
          );

      }
    );


    test(
      "BILL-010 - minimum payment of 0.01 is accepted",
      async ({
        billsPage,
        accountsPage
      }) => {

        await accountsPage.open();

        const before =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        await billsPage.open();

        await billsPage.payBill(
          "Water",
          billsData.minimumPayment
        );

        await accountsPage.open();

        const after =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        expect(
          after
        ).toBeCloseTo(
          before -
            billsData.minimumPayment,
          2
        );

      }
    );


    test(
      "BILL-011 - payment equal to full Checking balance is accepted",
      async ({
        billsPage,
        accountsPage
      }) => {

        await accountsPage.open();

        const fullBalance =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        await billsPage.open();

        await billsPage.payBill(
          "Electricity",
          fullBalance
        );

        await accountsPage.open();

        const finalBalance =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        expect(
          finalBalance
        ).toBeCloseTo(
          0,
          2
        );

      }
    );


    test(
      "BILL-012 - payment form resets after successful submission",
      async ({
        billsPage
      }) => {

        await billsPage.payBill(
          "Internet",
          25
        );

        await billsPage
          .expectFormReset();

      }
    );


    test(
      "BILL-013 - paid bill persists after page reload",
      async ({
        billsPage,
        accountsPage,
        transactionsPage
      }) => {

        await accountsPage.open();

        const before =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        await billsPage.open();

        await billsPage.payBill(
          "Mobile",
          88.88
        );

        await billsPage
          .reloadAndOpen();

        await accountsPage.open();

        const afterReload =
          await accountsPage
            .getAccountBalance(
              "Checking"
            );

        expect(
          afterReload
        ).toBeCloseTo(
          before - 88.88,
          2
        );

        await transactionsPage.open();

        await transactionsPage
          .expectFirstTransaction(
            "Mobile bill",
            "-$88.88"
          );

      }
    );


    test(
      "BILL-014 - dashboard total decreases by paid bill amount",
      async ({
        billsPage,
        dashboardPage,
        page
      }) => {

        const before =
          await dashboardPage
            .getDisplayedTotalBalance();

        await billsPage.open();

        await billsPage.payBill(
          "Water",
          45.50
        );

        await page.locator(
          '#nav button[data-s="dashboard"]'
        ).click();

        await dashboardPage
          .expectLoaded();

        const after =
          await dashboardPage
            .getDisplayedTotalBalance();

        expect(
          after
        ).toBeCloseTo(
          before - 45.50,
          2
        );

      }
    );

  }
);

