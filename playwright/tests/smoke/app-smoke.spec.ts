import { test, expect } from "../../fixtures/testFixtures";

test.describe("NovaBank - smoke", () => {
  test("SMOKE-001 - application serves the sign-in screen", async ({ authPage }) => {
    await authPage.open();
    await authPage.expectLoginScreen();
  });

  test("SMOKE-002 - health endpoint reports a PostgreSQL-backed service", async ({ request }) => {
    const response = await request.get("/api/health");
    expect(response.status()).toBe(200);

    const body = await response.json();
    expect(body.status).toBe("ok");
    expect(body.database).toBe("postgresql");
  });

  test("SMOKE-003 - customer can sign in and reach the overview", async ({ customerSession }) => {
    await customerSession.expectLoaded();
  });
});
