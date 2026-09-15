import {
  test,
  expect
} from "../../fixtures/testFixtures";


test.describe(
  "NovaBank - Authentication & Session Management",
  () => {


    test.beforeEach(
      async ({
        authPage
      }) => {

        await authPage.open();

      }
    );


    test(
      "AUTH-001 - login screen displays available demo roles",
      async ({
        authPage
      }) => {

        await authPage
          .expectLoginScreenVisible();

      }
    );


    test(
      "AUTH-002 - customer can start authenticated session",
      async ({
        authPage
      }) => {

        await authPage
          .loginAsCustomer();

        await authPage
          .expectCustomerAccess();

        await authPage
          .expectSessionStorageCreated();

      }
    );


    test(
      "AUTH-003 - customer cannot access admin navigation",
      async ({
        authPage
      }) => {

        await authPage
          .loginAsCustomer();

        await authPage
          .expectCustomerAccess();

      }
    );


    test(
      "AUTH-004 - admin can start authenticated session",
      async ({
        authPage
      }) => {

        await authPage
          .loginAsAdmin();

        await authPage
          .expectAdminAccess();

        await authPage
          .expectSessionStorageCreated();

      }
    );


    test(
      "AUTH-005 - admin can access admin console",
      async ({
        authPage
      }) => {

        await authPage
          .loginAsAdmin();

        await authPage
          .openAdminConsole();

      }
    );


    test(
      "AUTH-006 - authenticated customer session survives page reload",
      async ({
        authPage
      }) => {

        await authPage
          .loginAsCustomer();

        await authPage
          .reloadAndExpectAuthenticated(
            "customer"
          );

        await authPage
          .expectSessionStorageCreated();

      }
    );


    test(
      "AUTH-007 - authenticated admin session survives page reload",
      async ({
        authPage
      }) => {

        await authPage
          .loginAsAdmin();

        await authPage
          .reloadAndExpectAuthenticated(
            "admin"
          );

        await authPage
          .expectAdminAccess();

      }
    );


    test(
      "AUTH-008 - customer can log out successfully",
      async ({
        authPage
      }) => {

        await authPage
          .loginAsCustomer();

        await authPage
          .logout();

        await authPage
          .expectLoginScreenVisible();

        await authPage
          .expectSessionStorageCleared();

      }
    );


    test(
      "AUTH-009 - admin can log out successfully",
      async ({
        authPage
      }) => {

        await authPage
          .loginAsAdmin();

        await authPage
          .logout();

        await authPage
          .expectLoginScreenVisible();

        await authPage
          .expectSessionStorageCleared();

      }
    );


    test(
      "AUTH-010 - each new browser context starts unauthenticated",
      async ({
        browser
      }) => {

        const context = await browser.newContext({
      baseURL:
        process.env.BASE_URL ??
        "https://novabank-qa-proxy.onrender.com",
    });

    const page =
          await context.newPage();


        await page.goto(
          "/",
          {
            waitUntil:
              "domcontentloaded"
          }
        );


        await expect(
          page.locator("#login")
        ).toBeVisible();


        await expect(
          page.locator("#app")
        ).toBeHidden();


        const session =
          await page.evaluate(
            () => ({

              token:
                sessionStorage.getItem(
                  "nb_token"
                ),

              user:
                sessionStorage.getItem(
                  "nb_user"
                )

            })
          );


        expect(
          session.token
        ).toBeNull();


        expect(
          session.user
        ).toBeNull();


        await context.close();

      }
    );

  }
);
