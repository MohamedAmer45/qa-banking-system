import {
  test,
  expect
} from "../../fixtures/testFixtures";

import {
  profileData
} from "../../test-data/profileData";


test.describe(
  "NovaBank - Customer Profile",
  () => {

    test.beforeEach(
      async ({
        authPage,
        profilePage
      }) => {

        await authPage.open();

        await authPage
          .loginAsCustomer();

        await profilePage.open();

      }
    );


    test(
      "PROF-001 - customer can navigate to Profile",
      async ({
        profilePage
      }) => {

        await profilePage
          .expectLoaded();

      }
    );


    test(
      "PROF-002 - customer profile matches authenticated session",
      async ({
        profilePage
      }) => {

        await profilePage
          .expectProfileMatchesSession();

      }
    );


    test(
      "PROF-003 - customer profile name email and role are non-empty",
      async ({
        profilePage
      }) => {

        await profilePage
          .expectIdentityFieldsNonEmpty();

      }
    );


    test(
      "PROF-004 - customer email has valid format",
      async ({
        profilePage
      }) => {

        await profilePage
          .expectValidEmail();

      }
    );


    test(
      "PROF-005 - customer role is displayed correctly",
      async ({
        profilePage
      }) => {

        await profilePage.expectRole(
          profileData.roles.customer
        );

      }
    );


    test(
      "PROF-006 - customer profile is read-only",
      async ({
        profilePage
      }) => {

        await profilePage
          .expectReadOnly();

      }
    );


    test(
      "PROF-007 - customer profile persists after reload",
      async ({
        profilePage
      }) => {

        const before =
          await profilePage
            .getSessionUser();

        await profilePage
          .reloadAndOpen();

        await profilePage
          .expectProfileMatchesSession();

        const after =
          await profilePage
            .getSessionUser();

        expect(
          after
        ).toEqual(
          before
        );

      }
    );

  }
);


test.describe(
  "NovaBank - Admin Profile",
  () => {

    test.beforeEach(
      async ({
        authPage,
        profilePage
      }) => {

        await authPage.open();

        await authPage
          .loginAsAdmin();

        await profilePage.open();

      }
    );


    test(
      "PROF-008 - admin can navigate to Profile",
      async ({
        profilePage
      }) => {

        await profilePage
          .expectLoaded();

      }
    );


    test(
      "PROF-009 - admin profile matches authenticated session",
      async ({
        profilePage
      }) => {

        await profilePage
          .expectProfileMatchesSession();

      }
    );


    test(
      "PROF-010 - admin role is displayed correctly",
      async ({
        profilePage
      }) => {

        await profilePage.expectRole(
          profileData.roles.admin
        );

      }
    );


    test(
      "PROF-011 - admin profile is read-only",
      async ({
        profilePage
      }) => {

        await profilePage
          .expectReadOnly();

      }
    );


    test(
      "PROF-012 - admin profile persists after reload",
      async ({
        profilePage
      }) => {

        const before =
          await profilePage
            .getSessionUser();

        await profilePage
          .reloadAndOpen();

        await profilePage
          .expectProfileMatchesSession();

        const after =
          await profilePage
            .getSessionUser();

        expect(
          after
        ).toEqual(
          before
        );

      }
    );

  }
);
