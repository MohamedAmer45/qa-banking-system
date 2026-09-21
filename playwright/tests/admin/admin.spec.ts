import { test, expect } from "../../fixtures/testFixtures";
import { credentials } from "../../test-data/credentials";

test.describe("NovaBank - back office", () => {
  test("ADMIN-001 - an administrator reaches the operations dashboard", async ({ authPage, adminPage }) => {
    await authPage.open();
    await authPage.loginAsAdmin();

    await adminPage.openDashboard();
    await adminPage.expectMetricsVisible();
  });

  test("ADMIN-002 - the customer directory loads", async ({ authPage, adminPage }) => {
    await authPage.open();
    await authPage.loginAsAdmin();

    await adminPage.openCustomers();
    await expect(adminPage.customerTable).toContainText(credentials.customer.email);
  });

  test("ADMIN-003 - the audit trail records activity", async ({ authPage, adminPage }) => {
    await authPage.open();
    await authPage.loginAsAdmin();

    await adminPage.openAudit();
    await expect(adminPage.auditTable).toContainText(/LOGIN/);
  });

  test("ADMIN-004 - a customer is not offered back-office navigation", async ({ authPage, dashboardPage }) => {
    await authPage.open();
    await authPage.loginAsCustomer();

    const views = await dashboardPage.visibleNavItems();

    expect(views).toContain("transfers");
    expect(views.some(v => v.startsWith("admin-"))).toBe(false);
  });

  test("ADMIN-005 - a read-only role cannot reach privileged endpoints", async ({ authPage, request }) => {
    await authPage.open();
    await authPage.loginAs(credentials.support);

    // SUPPORT may read customers but must not change account state.
    const response = await request.post("/api/admin/accounts/1/freeze", {
      headers: { Authorization: `Bearer ${await authPage.storedToken()}` },
      data: {}
    });

    expect(response.status()).toBe(403);
  });
});
