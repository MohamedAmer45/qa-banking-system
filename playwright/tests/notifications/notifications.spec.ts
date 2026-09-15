import {
  test,
  expect
} from "../../fixtures/testFixtures";

import {
  notificationsData
} from "../../test-data/notificationsData";


test.describe(
  "NovaBank - Notifications",
  () => {

    test.beforeEach(
      async ({
        authPage,
        notificationsPage
      }) => {

        await authPage.open();

        await authPage
          .loginAsCustomer();

        await notificationsPage.open();

      }
    );


    test(
      "NOTE-001 - customer can navigate to Notifications",
      async ({
        notificationsPage
      }) => {

        await notificationsPage
          .expectLoaded();

      }
    );


    test(
      "NOTE-002 - expected number of initial notifications is displayed",
      async ({
        notificationsPage
      }) => {

        await notificationsPage
          .expectNotificationCount(
            notificationsData.initialCount
          );

      }
    );


    test(
      "NOTE-003 - monthly statement notification is displayed",
      async ({
        notificationsPage
      }) => {

        await notificationsPage
          .expectNotificationPresent(
            notificationsData.monthlyStatement
          );

      }
    );


    test(
      "NOTE-004 - card purchase notification is displayed",
      async ({
        notificationsPage
      }) => {

        await notificationsPage
          .expectNotificationPresent(
            notificationsData.cardPurchase
          );

      }
    );


    test(
      "NOTE-005 - notifications appear in expected initial order",
      async ({
        notificationsPage
      }) => {

        await notificationsPage
          .expectNotificationAt(
            0,
            notificationsData.monthlyStatement
          );

        await notificationsPage
          .expectNotificationAt(
            1,
            notificationsData.cardPurchase
          );

      }
    );


    test(
      "NOTE-006 - default notifications render before localStorage state is created",
      async ({
        notificationsPage
      }) => {

        const rendered =
          await notificationsPage
            .getNotificationTexts();

        const stored =
          await notificationsPage
            .getStoredNotifications();

        expect(
          rendered
        ).toEqual(
          notificationsData.initialNotifications
        );

        expect(
          stored
        ).toEqual([]);

      }
    );


    test(
      "NOTE-007 - initial notifications persist after page reload",
      async ({
        notificationsPage
      }) => {

        await notificationsPage
          .reloadAndOpen();

        await notificationsPage
          .expectNotificationCount(
            notificationsData.initialCount
          );

        await notificationsPage
          .expectNotificationPresent(
            notificationsData.monthlyStatement
          );

        await notificationsPage
          .expectNotificationPresent(
            notificationsData.cardPurchase
          );

      }
    );


    test(
      "NOTE-008 - successful loan application creates a new notification",
      async ({
        loansPage,
        notificationsPage
      }) => {

        await loansPage.open();

        const result =
          await loansPage.submitLoan(
            10000,
            24
          );

        expect(
          result.body.id
        ).toBeTruthy();

        const expectedNotification =
          `Loan application ${result.body.id} is under review.`;

        await notificationsPage.open();

        await notificationsPage
          .expectNotificationAt(
            0,
            expectedNotification
          );

        await notificationsPage
          .expectNotificationCount(
            notificationsData.initialCount + 1
          );

      }
    );


    test(
      "NOTE-009 - new loan notification is saved in localStorage",
      async ({
        loansPage,
        notificationsPage
      }) => {

        await loansPage.open();

        const result =
          await loansPage.submitLoan(
            12000,
            36
          );

        const expectedNotification =
          `Loan application ${result.body.id} is under review.`;

        await notificationsPage.open();

        const stored =
          await notificationsPage
            .getStoredNotifications();

        expect(
          stored[0]
        ).toBe(
          expectedNotification
        );

      }
    );


    test(
      "NOTE-010 - new loan notification persists after reload",
      async ({
        loansPage,
        notificationsPage
      }) => {

        await loansPage.open();

        const result =
          await loansPage.submitLoan(
            15000,
            12
          );

        const expectedNotification =
          `Loan application ${result.body.id} is under review.`;

        await notificationsPage.open();

        await notificationsPage
          .reloadAndOpen();

        await notificationsPage
          .expectNotificationAt(
            0,
            expectedNotification
          );

      }
    );

  }
);


test.describe(
  "NovaBank - Admin Notification Access",
  () => {

    test(
      "NOTE-011 - admin can access Notifications module",
      async ({
        authPage,
        notificationsPage
      }) => {

        await authPage.open();

        await authPage
          .loginAsAdmin();

        await notificationsPage.open();

        await notificationsPage
          .expectLoaded();

      }
    );

  }
);
