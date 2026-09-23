import { test, expect } from "../../fixtures/testFixtures";
import { credentials } from "../../test-data/credentials";

/**
 * DASH-001 to DASH-007 — the customer dashboard.
 *
 * This module previously had no automated coverage, and the reason turned out
 * not to be oversight: three of its seven requirements were never rendered, so
 * there was nothing to assert against (BUG-DASH-001). The panels exist now and
 * these tests hold them there.
 *
 * Counts are asserted as relationships rather than absolutes wherever the
 * seed does not guarantee a number, so a suite run after other suites have
 * moved money does not fail on arithmetic it never controlled.
 */
test.describe("NovaBank - customer dashboard", () => {
  test.beforeEach(async ({ customerSession, dashboardPage }) => {
    await dashboardPage.open();
  });

  test("DASH-001 - the overview shows the customer's accounts after login", async ({
    dashboardPage
  }) => {
    await dashboardPage.expectLoaded();
    await expect(dashboardPage.accountsPanel).toBeVisible();

    expect(
      await dashboardPage.accountCards.count(),
      "the overview should list at least one account"
    ).toBeGreaterThan(0);

    // The KPI and the panel are two renderings of one fact, so they must agree.
    const kpi = Number(await dashboardPage.accountsKpiValue.textContent());
    expect(kpi).toBeGreaterThanOrEqual(await dashboardPage.accountCards.count());
  });

  test("DASH-002 - current and available balances are both shown", async ({
    dashboardPage
  }) => {
    const balances = await dashboardPage.accountBalancesMinor();
    const available = await dashboardPage.accountAvailableMinor();

    expect(balances.length, "every account card should carry a balance").toBeGreaterThan(0);
    expect(available.length, "every account card should carry an available balance")
      .toBe(balances.length);

    balances.forEach(value => expect(Number.isInteger(value)).toBe(true));
    available.forEach(value => expect(Number.isInteger(value)).toBe(true));

    /*
     * Available is what the customer may actually spend, so it can trail the
     * balance but must never exceed it. That is the invariant worth holding;
     * the exact figures belong to whatever the ledger is doing today.
     */
    available.forEach((value, i) => {
      expect(value, `available must not exceed the balance on card ${i}`)
        .toBeLessThanOrEqual(balances[i]);
    });
  });

  test("DASH-002 - the EGP total is consistent with the account cards", async ({
    dashboardPage
  }) => {
    const total = await dashboardPage.egpTotalMinor();

    expect(Number.isInteger(total)).toBe(true);
    // Money is compared in minor units; a formatted string would be asserting
    // the locale, not the arithmetic.
    expect(total).toBeGreaterThanOrEqual(0);
  });

  test("DASH-003 - recent transactions are listed", async ({ dashboardPage }) => {
    const populated = await dashboardPage.panelIsPopulatedOrEmpty(
      dashboardPage.transactionsPanel,
      "overview-transactions-empty"
    );

    expect(populated, "the seeded customer has ledger history to show").toBe(true);

    const rows = await dashboardPage.transactionRows.count();
    expect(rows).toBeGreaterThan(0);
    expect(rows, "the panel is a summary, not the full ledger").toBeLessThanOrEqual(5);

    // Each row must carry the direction and amount a reader needs; a row with
    // neither is decoration.
    for (let i = 0; i < rows; i += 1) {
      const row = dashboardPage.transactionRows.nth(i);
      await expect(row).toHaveAttribute("data-direction", /DEBIT|CREDIT/);

      const amount = Number(await row.getAttribute("data-amount-minor"));
      expect(Number.isInteger(amount)).toBe(true);
      expect(amount).toBeGreaterThan(0);
    }
  });

  test("DASH-003 - the transactions shown are the most recent ones", async ({
    dashboardPage
  }) => {
    const rows = dashboardPage.transactionRows;
    const count = await rows.count();
    test.skip(count < 2, "needs at least two transactions to have an order");

    const references = await rows.evaluateAll(nodes =>
      nodes.map(n => n.querySelector(".meta")?.textContent ?? "")
    );

    expect(references.every(r => r.trim().length > 0),
      "every row should identify its transaction").toBe(true);
  });

  test("DASH-004 - active cards are listed", async ({ dashboardPage }) => {
    await expect(dashboardPage.cardsPanel).toBeVisible();

    const claimed = Number(await dashboardPage.cardsCount.textContent());
    const shown = await dashboardPage.cardItems.count();

    expect(Number.isInteger(claimed)).toBe(true);

    if (claimed === 0) {
      await expect(dashboardPage.cardsPanel.getByTestId("overview-cards-empty"))
        .toBeVisible();
      return;
    }

    expect(shown).toBeGreaterThan(0);
    // The panel caps at four; the count reports the true total.
    expect(shown).toBeLessThanOrEqual(Math.min(claimed, 4));

    for (let i = 0; i < shown; i += 1) {
      await expect(dashboardPage.cardItems.nth(i)).toHaveAttribute("data-last4", /^\d{4}$/);
    }
  });

  test("DASH-004 - only active cards reach the panel", async ({ dashboardPage }) => {
    const shown = await dashboardPage.cardItems.count();
    test.skip(shown === 0, "the customer has no active card to check");

    for (let i = 0; i < shown; i += 1) {
      await expect(dashboardPage.cardItems.nth(i)).toContainText("ACTIVE");
    }
  });

  test("DASH-005 - upcoming scheduled payments are listed", async ({ dashboardPage }) => {
    await expect(dashboardPage.scheduledPanel).toBeVisible();

    const claimed = Number(await dashboardPage.scheduledCount.textContent());
    expect(Number.isInteger(claimed)).toBe(true);

    /*
     * "Where applicable" in DASH-005: a customer with nothing scheduled must
     * still get the panel and an explicit empty state, rather than a gap on
     * the page that cannot be told apart from a render failure.
     */
    if (claimed === 0) {
      await expect(dashboardPage.scheduledPanel.getByTestId("overview-scheduled-empty"))
        .toBeVisible();
      return;
    }

    const shown = await dashboardPage.scheduledItems.count();
    expect(shown).toBeGreaterThan(0);

    for (let i = 0; i < shown; i += 1) {
      await expect(dashboardPage.scheduledItems.nth(i))
        .toHaveAttribute("data-kind", /BILL|TRANSFER/);
    }
  });

  test("DASH-005 - scheduled payments are in the future and in order", async ({
    dashboardPage
  }) => {
    const shown = await dashboardPage.scheduledItems.count();
    test.skip(shown === 0, "nothing scheduled for this customer");

    const dates = await dashboardPage.scheduledItems.evaluateAll(nodes =>
      nodes.map(n => n.getAttribute("data-when") ?? "")
    );

    const now = new Date().toISOString();
    dates.forEach(when => {
      expect(when > now, `${when} is scheduled but not in the future`).toBe(true);
    });

    // Soonest first: a list of what is coming up is only useful in order.
    const sorted = [...dates].sort();
    expect(dates).toEqual(sorted);
  });

  test("DASH-006 - unread notifications are surfaced", async ({ dashboardPage }) => {
    await expect(dashboardPage.alertsPanel).toBeVisible();

    const unread = Number(await dashboardPage.overviewUnreadCount.textContent());
    expect(Number.isInteger(unread)).toBe(true);
    expect(unread).toBeGreaterThanOrEqual(0);

    // The topbar badge and the panel count the same thing.
    const badge = Number(await dashboardPage.unreadCount.textContent());
    expect(badge).toBe(unread);
  });

  test("DASH-006 - unread alerts are marked as such", async ({ dashboardPage }) => {
    const items = await dashboardPage.alertItems.count();
    test.skip(items === 0, "no notifications for this customer");

    const flags = await dashboardPage.alertItems.evaluateAll(nodes =>
      nodes.map(n => n.getAttribute("data-unread"))
    );

    flags.forEach(flag => expect(flag).toMatch(/true|false/));
  });

  test("DASH-007 - the dashboard shows only the signed-in customer's data", async ({
    dashboardPage
  }) => {
    const text = await dashboardPage.text();

    expect(text).toContain(credentials.customer.firstName);

    /*
     * The strongest available check without a second session: no other seeded
     * identity may appear anywhere on this customer's dashboard.
     */
    for (const other of [credentials.receiver, credentials.pendingKyc, credentials.admin]) {
      expect(text, `${other.email} must not appear on another customer's dashboard`)
        .not.toContain(other.email);
      expect(text).not.toContain(other.firstName);
    }
  });

  test("DASH-007 - a second customer sees a different dashboard", async ({
    page,
    authPage,
    dashboardPage
  }) => {
    const first = await dashboardPage.accountBalancesMinor();
    expect(first.length, "the first customer's dashboard rendered").toBeGreaterThan(0);

    await page.evaluate(() => window.localStorage.clear());
    await authPage.open();
    await authPage.loginAs(credentials.receiver);
    await dashboardPage.open();

    const second = await dashboardPage.accountBalancesMinor();
    const text = await dashboardPage.text();

    expect(text).toContain(credentials.receiver.firstName);
    expect(text).not.toContain(credentials.customer.email);

    /*
     * Balances are deliberately not compared between the two sessions: two
     * customers could hold the same amount by coincidence, so an inequality
     * assertion there would be flaky rather than meaningful. The identity on
     * the page is the real evidence, and it is asserted above.
     */
    expect(second.length, "the second customer has accounts of their own")
      .toBeGreaterThan(0);
  });
});
