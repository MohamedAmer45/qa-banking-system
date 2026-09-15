import {
  test,
  expect
} from "../../fixtures/testFixtures";

import {
  loansData
} from "../../test-data/loansData";


test.describe(
  "NovaBank - Loans",
  () => {

    test.beforeEach(
      async ({
        authPage,
        loansPage
      }) => {

        await authPage.open();

        await authPage
          .loginAsCustomer();

        await loansPage.open();

      }
    );


    test(
      "LOAN-001 - customer can navigate to Loans",
      async ({
        loansPage
      }) => {

        await loansPage
          .expectLoaded();

      }
    );


    test(
      "LOAN-002 - current loan details are displayed correctly",
      async ({
        loansPage
      }) => {

        await loansPage.expectLoanDetails(
          loansData.currentLoan.type,
          loansData.currentLoan.balance,
          loansData.currentLoan.apr,
          loansData.currentLoan.nextPayment,
          loansData.currentLoan.dueDate
        );

      }
    );


    test(
      "LOAN-003 - loan amount field has expected boundaries",
      async ({
        loansPage
      }) => {

        await loansPage
          .expectAmountConstraints(
            String(
              loansData.limits.minimum
            ),
            String(
              loansData.limits.maximum
            )
          );

      }
    );


    test(
      "LOAN-004 - supported loan terms are displayed",
      async ({
        loansPage
      }) => {

        await loansPage
          .expectSupportedTerms(
            loansData.terms.labels
          );

        await loansPage
          .expectTermValues(
            loansData.terms.values
          );

      }
    );


    test(
      "LOAN-005 - minimum loan amount is accepted",
      async ({
        loansPage
      }) => {

        await loansPage.submitLoan(
          loansData.limits.minimum,
          12
        );

        await loansPage
          .expectSuccessToast();

      }
    );


    test(
      "LOAN-006 - maximum loan amount is accepted",
      async ({
        loansPage
      }) => {

        await loansPage.submitLoan(
          loansData.limits.maximum,
          12
        );

        await loansPage
          .expectSuccessToast();

      }
    );


    test(
      "LOAN-007 - amount below minimum is rejected by input validation",
      async ({
        loansPage
      }) => {

        await loansPage
          .expectAmountValidity(
            loansData.limits
              .belowMinimum,
            false
          );

      }
    );


    test(
      "LOAN-008 - amount above maximum is rejected by input validation",
      async ({
        loansPage
      }) => {

        await loansPage
          .expectAmountValidity(
            loansData.limits
              .aboveMaximum,
            false
          );

      }
    );


    test(
      "LOAN-009 - zero loan amount is rejected by input validation",
      async ({
        loansPage
      }) => {

        await loansPage
          .expectAmountValidity(
            loansData.limits.zero,
            false
          );

      }
    );


    test(
      "LOAN-010 - negative loan amount is rejected by input validation",
      async ({
        loansPage
      }) => {

        await loansPage
          .expectAmountValidity(
            loansData.limits.negative,
            false
          );

      }
    );


    test(
      "LOAN-011 - 24 month application sends expected API payload",
      async ({
        page,
        loansPage
      }) => {

        const requestPromise =
          page.waitForRequest(
            request =>
              request.url().includes(
                "/api/loans/apply"
              ) &&
              request.method() ===
                "POST"
          );

        await loansPage.submitLoan(
          10000,
          24
        );

        const request =
          await requestPromise;

        expect(
          request.postDataJSON()
        ).toEqual({
          amount:
            10000,

          termMonths:
            24
        });

      }
    );


    test(
      "LOAN-012 - 36 month application sends expected API payload",
      async ({
        page,
        loansPage
      }) => {

        const requestPromise =
          page.waitForRequest(
            request =>
              request.url().includes(
                "/api/loans/apply"
              ) &&
              request.method() ===
                "POST"
          );

        await loansPage.submitLoan(
          25000,
          36
        );

        const request =
          await requestPromise;

        expect(
          request.postDataJSON()
        ).toEqual({
          amount:
            25000,

          termMonths:
            36
        });

      }
    );


    test(
      "LOAN-013 - successful application resets the form",
      async ({
        loansPage
      }) => {

        await loansPage.submitLoan(
          5000,
          24
        );

        await loansPage
          .expectFormReset();

        await loansPage
          .expectSuccessToast();

      }
    );


    test(
      "LOAN-014 - successful loan application creates persistent notification",
      async ({
        loansPage
      }) => {

        const result =
          await loansPage.submitLoan(
            15000,
            36
          );

        expect(
          result.body.id
        ).toBeTruthy();

        const notification =
          `Loan application ${result.body.id} is under review.`;

        await loansPage
          .openNotifications();

        await loansPage
          .expectNewestNotification(
            notification
          );

        await loansPage
          .reloadAndOpenNotifications();

        await loansPage
          .expectNewestNotification(
            notification
          );

      }
    );

  }
);


test.describe(
  "NovaBank - Admin Loan Access",
  () => {

    test(
      "LOAN-015 - admin can access Loans module",
      async ({
        authPage,
        loansPage
      }) => {

        await authPage.open();

        await authPage
          .loginAsAdmin();

        await loansPage.open();

        await loansPage
          .expectLoaded();

      }
    );

  }
);
