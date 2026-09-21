import { test, expect } from "../../fixtures/testFixtures";

/*
 * These tests move money against a shared database. Running them in parallel
 * would make each one observe balances another had already changed, so the
 * file is serial and every assertion is expressed as a delta against a balance
 * read at the start of the test rather than a fixed expected total.
 */
test.describe.configure({ mode: "serial" });

test.describe("NovaBank - transfers", () => {
  test.beforeEach(async ({ customerSession, transfersPage }) => {
    await transfersPage.open();
  });

  test("TRF-001 - the transfer form offers the customer's active accounts", async ({ transfersPage }) => {
    await transfersPage.openForm();
    await transfersPage.expectLoaded();
    expect(await transfersPage.sourceAccountCount()).toBeGreaterThanOrEqual(2);
  });

  test("TRF-002 - a valid transfer debits the source account", async ({ transfersPage, accountsPage }) => {
    await accountsPage.open();
    const before = await accountsPage.balanceMinor(0);

    await transfersPage.open();
    await transfersPage.openForm();
    const status = await transfersPage.submitTransfer({
      fromIndex: 0,
      beneficiaryIndex: 0,
      amount: 100,
      memo: "playwright delta check"
    });

    expect(status).toBe(201);
    await transfersPage.expectToast(/transfer/i);

    await accountsPage.open();
    const after = await accountsPage.balanceMinor(0);

    // 100.00 major units is 10000 minor units. An external beneficiary also
    // attracts a fee, so the debit is at least the transfer amount.
    expect(before - after).toBeGreaterThanOrEqual(10000);
  });

  test("TRF-003 - a transfer beyond the available balance is rejected and moves nothing", async ({ transfersPage, accountsPage }) => {
    await accountsPage.open();
    const before = await accountsPage.balanceMinor(0);

    await transfersPage.open();
    await transfersPage.openForm();
    const status = await transfersPage.submitTransfer({
      fromIndex: 0,
      beneficiaryIndex: 0,
      amount: 99_999_999
    });

    expect(status).toBe(409);

    await accountsPage.open();
    expect(await accountsPage.balanceMinor(0)).toBe(before);
  });

  test("TRF-004 - zero is rejected by input validation before submission", async ({ transfersPage }) => {
    await transfersPage.openForm();
    await transfersPage.expectNativeAmountInvalid("0");
  });

  test("TRF-005 - a negative amount is rejected by input validation", async ({ transfersPage }) => {
    await transfersPage.openForm();
    await transfersPage.expectNativeAmountInvalid("-1");
  });

  test("TRF-006 - a QA-simulated downstream failure does not debit the account", async ({ page, transfersPage, accountsPage }) => {
    await accountsPage.open();
    const before = await accountsPage.balanceMinor(0);

    /*
     * The form has no fault-injection control wired to a test id, so the
     * simulation flag is added to the outgoing request. The assertion is that
     * a 422 leaves the ledger untouched.
     */
    await page.route("**/api/transfers", async route => {
      if (route.request().method() !== "POST") return route.continue();

      const body = JSON.parse(route.request().postData() ?? "{}");
      body.qaSimulation = "failure";

      await route.continue({ postData: JSON.stringify(body) });
    });

    await transfersPage.open();
    await transfersPage.openForm();
    const status = await transfersPage.submitTransfer({
      fromIndex: 0,
      beneficiaryIndex: 0,
      amount: 25
    });

    expect(status).toBe(422);

    await accountsPage.open();
    expect(await accountsPage.balanceMinor(0)).toBe(before);
  });
});
