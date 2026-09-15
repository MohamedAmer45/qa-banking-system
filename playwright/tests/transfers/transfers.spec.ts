import {
  test,
  expect
} from "../../fixtures/testFixtures";

import {
  transferData
} from "../../test-data/transferData";


test.describe(
  "NovaBank - Transfers",
  () => {


    test.beforeEach(
      async ({
        authPage,
        transfersPage
      }) => {

        await authPage.open();

        await authPage.loginAsCustomer();

        await transfersPage.open();

      }
    );


    test(
      "TRF-001 - customer can navigate to Transfers",
      async ({
        transfersPage
      }) => {

        await transfersPage
          .expectLoaded();

      }
    );


    test(
      "TRF-002 - source account dropdown displays Checking and Savings",
      async ({
        transfersPage
      }) => {

        await transfersPage
          .expectSourceAccounts();

      }
    );


    test(
      "TRF-003 - recipient dropdown displays supported recipients",
      async ({
        transfersPage
      }) => {

        await transfersPage
          .expectRecipients();

      }
    );


    test(
      "TRF-004 - successful Checking transfer decreases balance and creates transaction",
      async ({
        transfersPage
      }) => {

        const before =
          await transfersPage
            .getSourceBalance(
              transferData.sources
                .checking.index
            );

        await transfersPage
          .performSuccessfulTransfer(
            transferData.sources
              .checking.index,
            transferData.recipients.alex,
            100
          );

        const after =
          await transfersPage
            .getSourceBalance(
              transferData.sources
                .checking.index
            );

        expect(
          after
        ).toBeCloseTo(
          before - 100,
          2
        );

        await transfersPage
          .openTransactions();

        await transfersPage
          .expectTransaction(
            "Transfer to Alex Johnson",
            "-$100.00"
          );

        await transfersPage
          .expectTransactionCount(4);

      }
    );


    test(
      "TRF-005 - successful Savings transfer decreases only Savings balance",
      async ({
        transfersPage
      }) => {

        const checkingBefore =
          await transfersPage
            .getSourceBalance(
              transferData.sources
                .checking.index
            );

        const savingsBefore =
          await transfersPage
            .getSourceBalance(
              transferData.sources
                .savings.index
            );

        await transfersPage
          .performSuccessfulTransfer(
            transferData.sources
              .savings.index,
            transferData.recipients.sam,
            250
          );

        const checkingAfter =
          await transfersPage
            .getSourceBalance(
              transferData.sources
                .checking.index
            );

        const savingsAfter =
          await transfersPage
            .getSourceBalance(
              transferData.sources
                .savings.index
            );

        expect(
          checkingAfter
        ).toBeCloseTo(
          checkingBefore,
          2
        );

        expect(
          savingsAfter
        ).toBeCloseTo(
          savingsBefore - 250,
          2
        );

        await transfersPage
          .openTransactions();

        await transfersPage
          .expectTransaction(
            "Transfer to Sam Lee",
            "-$250.00"
          );

      }
    );


    test(
      "TRF-006 - transfer above maximum limit is rejected",
      async ({
        transfersPage
      }) => {

        const before =
          await transfersPage
            .getSourceBalance(0);

        await transfersPage
          .attemptClientRejectedTransfer(
            0,
            transferData.recipients.alex,
            10000.01,
            "Transfer limit exceeded."
          );

        const after =
          await transfersPage
            .getSourceBalance(0);

        expect(
          after
        ).toBeCloseTo(
          before,
          2
        );

      }
    );


    test(
      "TRF-007 - transfer exceeding available balance is rejected",
      async ({
        transfersPage
      }) => {

        /*
         * First transfer $10,000 from Checking.
         *
         * Initial:
         * $12,840.75
         *
         * Remaining:
         * $2,840.75
         */

        await transfersPage
          .performSuccessfulTransfer(
            0,
            transferData.recipients.alex,
            10000
          );

        const remainingBalance =
          await transfersPage
            .getSourceBalance(0);

        expect(
          remainingBalance
        ).toBeCloseTo(
          2840.75,
          2
        );

        /*
         * $3,000 is under the $10,000
         * transfer limit but greater than
         * the remaining account balance.
         */

        await transfersPage
          .attemptClientRejectedTransfer(
            0,
            transferData.recipients.sam,
            3000,
            "Insufficient funds."
          );

        const afterRejectedTransfer =
          await transfersPage
            .getSourceBalance(0);

        expect(
          afterRejectedTransfer
        ).toBeCloseTo(
          remainingBalance,
          2
        );

      }
    );


    test(
      "TRF-008 - exact maximum transfer of 10000 is accepted",
      async ({
        transfersPage
      }) => {

        const before =
          await transfersPage
            .getSourceBalance(0);

        await transfersPage
          .performSuccessfulTransfer(
            0,
            transferData.recipients.alex,
            transferData.maximumTransfer
          );

        const after =
          await transfersPage
            .getSourceBalance(0);

        expect(
          after
        ).toBeCloseTo(
          before -
            transferData.maximumTransfer,
          2
        );

        expect(
          after
        ).toBeCloseTo(
          2840.75,
          2
        );

      }
    );


    test(
      "TRF-009 - zero transfer amount is rejected by input validation",
      async ({
        transfersPage
      }) => {

        await transfersPage
          .expectNativeAmountInvalid(
            0
          );

      }
    );


    test(
      "TRF-010 - negative transfer amount is rejected by input validation",
      async ({
        transfersPage
      }) => {

        await transfersPage
          .expectNativeAmountInvalid(
            -1
          );

      }
    );


    test(
      "TRF-011 - minimum valid transfer of 0.01 is accepted",
      async ({
        transfersPage
      }) => {

        const before =
          await transfersPage
            .getSourceBalance(0);

        await transfersPage
          .performSuccessfulTransfer(
            0,
            transferData.recipients.alex,
            transferData.minimumTransfer
          );

        const after =
          await transfersPage
            .getSourceBalance(0);

        expect(
          after
        ).toBeCloseTo(
          before -
            transferData.minimumTransfer,
          2
        );

        await transfersPage
          .openTransactions();

        await transfersPage
          .expectTransaction(
            "Transfer to Alex Johnson",
            "-$0.01"
          );

      }
    );


    test(
      "TRF-012 - transfer equal to exact available balance is accepted",
      async ({
        transfersPage
      }) => {

        await transfersPage
          .performSuccessfulTransfer(
            0,
            transferData.recipients.alex,
            10000
          );

        const remaining =
          await transfersPage
            .getSourceBalance(0);

        expect(
          remaining
        ).toBeCloseTo(
          2840.75,
          2
        );

        await transfersPage
          .performSuccessfulTransfer(
            0,
            transferData.recipients.sam,
            remaining
          );

        const finalBalance =
          await transfersPage
            .getSourceBalance(0);

        expect(
          finalBalance
        ).toBeCloseTo(
          0,
          2
        );

        await transfersPage
          .openTransactions();

        await transfersPage
          .expectTransaction(
            "Transfer to Sam Lee",
            "-$2,840.75"
          );

      }
    );


    test(
      "TRF-013 - successful transfer persists after page reload",
      async ({
        transfersPage
      }) => {

        const before =
          await transfersPage
            .getSourceBalance(0);

        await transfersPage
          .performSuccessfulTransfer(
            0,
            transferData.recipients.alex,
            123.45
          );

        const expectedBalance =
          before - 123.45;

        await transfersPage
          .reloadAndOpen();

        const afterReload =
          await transfersPage
            .getSourceBalance(0);

        expect(
          afterReload
        ).toBeCloseTo(
          expectedBalance,
          2
        );

        await transfersPage
          .openTransactions();

        await transfersPage
          .expectTransaction(
            "Transfer to Alex Johnson",
            "-$123.45"
          );

      }
    );


    test(
      "TRF-014 - selected recipient appears in transaction description",
      async ({
        transfersPage
      }) => {

        await transfersPage
          .performSuccessfulTransfer(
            0,
            transferData.recipients.sam,
            75.50
          );

        await transfersPage
          .openTransactions();

        await transfersPage
          .expectTransaction(
            "Transfer to Sam Lee",
            "-$75.50"
          );

      }
    );

  }
);
