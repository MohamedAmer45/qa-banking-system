import { test, expect } from "../../fixtures/testFixtures";
import { credentials } from "../../test-data/credentials";

test.describe("NovaBank - authentication", () => {
  test.beforeEach(async ({ authPage }) => {
    await authPage.open();
  });

  test("AUTH-001 - valid credentials raise an MFA challenge rather than a session", async ({ authPage }) => {
    await authPage.submitCredentials(
      credentials.customer.email,
      credentials.customer.password
    );

    await authPage.expectMfaChallenge();

    // No session exists until the challenge is answered.
    expect(await authPage.storedToken()).toBeNull();
  });

  test("AUTH-002 - completing MFA establishes the session", async ({ authPage }) => {
    await authPage.loginAsCustomer();
    expect(await authPage.storedToken()).toBeTruthy();
  });

  test("AUTH-003 - invalid password is rejected", async ({ authPage }) => {
    await authPage.submitCredentials(
      credentials.invalid.email,
      credentials.invalid.password
    );

    await authPage.expectLoginRejected(/invalid/i);
  });

  test("AUTH-004 - incorrect MFA code is rejected", async ({ authPage }) => {
    await authPage.submitCredentials(
      credentials.customer.email,
      credentials.customer.password
    );

    await authPage.expectMfaChallenge();
    await authPage.submitMfa("000000");

    await authPage.expectToast(/invalid one-time code/i);
    expect(await authPage.storedToken()).toBeNull();
  });

  test("AUTH-005 - session survives a page reload", async ({ authPage }) => {
    await authPage.loginAsCustomer();
    await authPage.expectAuthenticatedAfterReload("CUSTOMER");
  });

  test("AUTH-006 - logout clears the session", async ({ authPage }) => {
    await authPage.loginAsCustomer();
    await authPage.logout();

    expect(await authPage.storedToken()).toBeNull();
  });

  test("AUTH-007 - staff sign in with their own role", async ({ authPage }) => {
    await authPage.loginAs(credentials.admin);
    await expect(authPage.userRole).toHaveText("ADMIN");
  });
});
