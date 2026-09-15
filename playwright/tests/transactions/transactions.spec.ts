import {
  test
} from "../../fixtures/testFixtures";

import {
  transactionsData
} from "../../test-data/transactionsData";


test.describe(
  "NovaBank - Transactions",
  () => {


    test.beforeEach(
      async ({
        authPage,
        transactionsPage
      }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

        await transactionsPage.open();

      }
    );


    test(
      "TXN-001 - customer can navigate to transaction history",
      async ({
        transactionsPage
      }) => {

        await transactionsPage
          .expectLoaded();

      }
    );


    test(
      "TXN-002 - transaction table contains expected columns",
      async ({
        transactionsPage
      }) => {

        await transactionsPage
          .expectHeaders();

      }
    );


    test(
      "TXN-003 - initial transaction history contains three transactions",
      async ({
        transactionsPage
      }) => {

        await transactionsPage
          .expectTransactionCount(
            transactionsData.initialCount
          );

      }
    );


    test(
      "TXN-004 - salary deposit details are correct",
      async ({
        transactionsPage
      }) => {

        const tx =
          transactionsData.salary;

        await transactionsPage
          .expectTransaction(
            tx.description,
            tx.date,
            tx.status,
            tx.amount
          );

      }
    );


    test(
      "TXN-005 - electricity bill transaction details are correct",
      async ({
        transactionsPage
      }) => {

        const tx =
          transactionsData.electricity;

        await transactionsPage
          .expectTransaction(
            tx.description,
            tx.date,
            tx.status,
            tx.amount
          );

      }
    );


    test(
      "TXN-006 - card purchase transaction details are correct",
      async ({
        transactionsPage
      }) => {

        const tx =
          transactionsData.cardPurchase;

        await transactionsPage
          .expectTransaction(
            tx.description,
            tx.date,
            tx.status,
            tx.amount
          );

      }
    );


    test(
      "TXN-007 - all initial transactions have completed status",
      async ({
        transactionsPage
      }) => {

        await transactionsPage
          .expectAllStatuses(
            "completed"
          );

      }
    );


    test(
      "TXN-008 - salary deposit is represented as a credit",
      async ({
        transactionsPage
      }) => {

        await transactionsPage
          .expectCreditTransaction(
            transactionsData
              .salary
              .description
          );

      }
    );


    test(
      "TXN-009 - electricity payment is represented as a debit",
      async ({
        transactionsPage
      }) => {

        await transactionsPage
          .expectDebitTransaction(
            transactionsData
              .electricity
              .description
          );

      }
    );


    test(
      "TXN-010 - card purchase is represented as a debit",
      async ({
        transactionsPage
      }) => {

        await transactionsPage
          .expectDebitTransaction(
            transactionsData
              .cardPurchase
              .description
          );

      }
    );


    test(
      "TXN-011 - transactions are ordered newest first",
      async ({
        transactionsPage
      }) => {

        await transactionsPage
          .expectNewestFirst();

      }
    );

  }
);


test.describe(
  "NovaBank - Transaction Integration",
  () => {


    test(
      "TXN-012 - completed transfer is added to transaction history",
      async ({
        authPage,
        transfersPage,
        transactionsPage
      }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

        await transfersPage.open();

        await transfersPage
          .performSuccessfulTransfer(
            0,
            "Alex Johnson",
            125
          );

        await transactionsPage.open();

        await transactionsPage
          .expectTransactionCount(4);

        await transactionsPage
          .expectFirstTransaction(
            "Transfer to Alex Johnson",
            "-$125.00"
          );

      }
    );


    test(
      "TXN-013 - transfer transaction is represented as a debit",
      async ({
        authPage,
        transfersPage,
        transactionsPage
      }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

        await transfersPage.open();

        await transfersPage
          .performSuccessfulTransfer(
            0,
            "Sam Lee",
            50
          );

        await transactionsPage.open();

        await transactionsPage
          .expectDebitTransaction(
            "Transfer to Sam Lee"
          );

      }
    );


    test(
      "TXN-014 - transfer transaction persists after page reload",
      async ({
        authPage,
        transfersPage,
        transactionsPage
      }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

        await transfersPage.open();

        await transfersPage
          .performSuccessfulTransfer(
            0,
            "Alex Johnson",
            99.99
          );

        await transactionsPage.open();

        await transactionsPage
          .expectFirstTransaction(
            "Transfer to Alex Johnson",
            "-$99.99"
          );

        await transactionsPage
          .reloadAndOpen();

        await transactionsPage
          .expectTransactionCount(4);

        await transactionsPage
          .expectFirstTransaction(
            "Transfer to Alex Johnson",
            "-$99.99"
          );

      }
    );

  }
);
