import { test, expect } from "../../fixtures/testFixtures";

test.describe("NovaBank - accounts", () => {
  test.beforeEach(async ({ customerSession, accountsPage }) => {
    await accountsPage.open();
  });

  test("ACC-001 - the seeded customer's accounts are listed", async ({ accountsPage }) => {
    await accountsPage.expectLoaded();
    expect(await accountsPage.count()).toBeGreaterThanOrEqual(3);
  });

  test("ACC-002 - balances are exposed as integer minor units", async ({ accountsPage }) => {
    const balance = await accountsPage.balanceMinor(0);

    expect(Number.isInteger(balance)).toBe(true);
    expect(balance).toBeGreaterThan(0);
  });

  test("ACC-003 - every account card shows a type and a status", async ({ accountsPage }) => {
    const cards = accountsPage.cards;
    const count = await cards.count();

    for (let i = 0; i < count; i += 1) {
      await expect(cards.nth(i)).toContainText(/CURRENT|SAVINGS/);
      await expect(cards.nth(i)).toContainText(/ACTIVE|FROZEN|DORMANT|CLOSED/);
    }
  });
});
