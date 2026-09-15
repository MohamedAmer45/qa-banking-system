import {
  test,
  expect
} from "../../fixtures/testFixtures";


test.describe(
  "NovaBank - Admin Console",
  () => {

    test.beforeEach(
      async ({
        authPage,
        adminPage
      }) => {

        await authPage.open();

        await authPage
          .loginAsAdmin();

        await adminPage.open();

      }
    );


    test(
      "ADMIN-001 - admin navigation is visible for admin user",
      async ({
        adminPage
      }) => {

        await adminPage
          .expectNavigationVisible();

      }
    );


    test(
      "ADMIN-002 - admin can navigate to Admin console",
      async ({
        adminPage
      }) => {

        await adminPage
          .expectLoaded();

      }
    );


    test(
      "ADMIN-003 - admin summary API returns data",
      async ({
        adminPage
      }) => {

        const summary =
          await adminPage
            .getSummaryFromApi();

        expect(
          Object.keys(summary).length
        ).toBeGreaterThan(0);

      }
    );


    test(
      "ADMIN-004 - admin summary values are numeric",
      async ({
        adminPage
      }) => {

        const summary =
          await adminPage
            .getSummaryFromApi();

        for (
          const value
          of Object.values(summary)
        ) {

          expect(
            typeof value
          ).toBe("number");

          expect(
            Number.isFinite(value)
          ).toBeTruthy();

        }

      }
    );


    test(
      "ADMIN-005 - displayed metric count matches API summary",
      async ({
        adminPage
      }) => {

        const summary =
          await adminPage
            .getSummaryFromApi();

        await adminPage
          .expectMetricCount(
            Object.keys(summary).length
          );

      }
    );


    test(
      "ADMIN-006 - displayed admin metrics match API summary",
      async ({
        adminPage
      }) => {

        await adminPage
          .expectRenderedSummaryMatchesApi();

      }
    );


    test(
      "ADMIN-007 - all admin metrics contain labels and numeric values",
      async ({
        adminPage
      }) => {

        await adminPage
          .expectAllMetricsNonEmpty();

      }
    );


    test(
      "ADMIN-008 - Admin console remains available after reload",
      async ({
        adminPage
      }) => {

        await adminPage
          .reloadAndOpen();

        await adminPage
          .expectLoaded();

        await adminPage
          .expectRenderedSummaryMatchesApi();

      }
    );

  }
);


test.describe(
  "NovaBank - Admin Access Control",
  () => {

    test(
      "ADMIN-009 - customer does not see Admin navigation",
      async ({
        authPage,
        adminPage
      }) => {

        await authPage.open();

        await authPage
          .loginAsCustomer();

        await adminPage
          .expectNavigationHidden();

      }
    );


    test(
      "ADMIN-010 - unauthenticated admin summary API request is rejected",
      async ({
        request
      }) => {

        const response =
          await request.get(
            "/api/admin/summary"
          );

        expect(
          response.status()
        ).toBe(401);

      }
    );

  }
);
