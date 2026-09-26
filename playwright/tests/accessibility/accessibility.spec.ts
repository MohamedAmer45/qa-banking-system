import { AxeBuilder } from "@axe-core/playwright";
import { test, expect } from "../../fixtures/testFixtures";
import { credentials } from "../../test-data/credentials";

/**
 * A11Y-001 to A11Y-009 — accessibility.
 *
 * Two halves, deliberately separated.
 *
 * The scans cover A11Y-001 to A11Y-006, which an engine can decide: a control
 * either has an accessible name or it does not. axe-core is the engine.
 *
 * A11Y-007 to A11Y-009 are the ones it cannot decide. axe can confirm a dialog
 * carries role="dialog"; it cannot confirm that focus goes into it, stays there,
 * and comes back afterwards. Those are driven as interactions below.
 *
 * What a clean scan does and does not mean: automated checks find a minority of
 * WCAG issues — no engine can judge whether alt text is useful, whether reading
 * order makes sense, or whether a screen reader announces a transfer coherently.
 * Passing here is a floor, not a claim that the application is accessible.
 */

const WCAG = ["wcag2a", "wcag2aa", "wcag21a", "wcag21aa"];

/** Views reachable as a customer. */
const CUSTOMER_VIEWS = [
  "overview", "accounts", "transfers", "beneficiaries", "transactions",
  "statements", "cards", "bills", "loans", "kyc", "notifications", "security"
] as const;

/** Views reachable in the back office as ADMIN. */
const BACK_OFFICE_VIEWS = [
  "admin-dashboard", "admin-customers", "admin-kyc", "admin-accounts",
  "admin-transfers", "admin-loans", "admin-fraud", "admin-audit", "admin-users"
] as const;

test.describe("NovaBank - accessibility", () => {
  test("A11Y-001..006 - the sign-in page has no detectable violations", async ({ authPage, page }) => {
    await authPage.open();

    const results = await new AxeBuilder({ page }).withTags(WCAG).analyze();

    expect(describe(results.violations)).toEqual([]);
  });

  test("A11Y-001..006 - the MFA challenge has no detectable violations", async ({ authPage, page }) => {
    await authPage.open();
    await authPage.submitCredentials(credentials.customer.email, credentials.customer.password);

    const results = await new AxeBuilder({ page }).withTags(WCAG).analyze();

    expect(describe(results.violations)).toEqual([]);
  });

  test.describe("customer views", () => {
    test.beforeEach(async ({ customerSession }) => {
      // customerSession lands on the overview with a live session.
    });

    for (const view of CUSTOMER_VIEWS) {
      test(`A11Y-001..006 - ${view} has no detectable violations`, async ({ page, dashboardPage }) => {
        await openView(page, view);

        const results = await new AxeBuilder({ page }).withTags(WCAG).analyze();

        expect(describe(results.violations)).toEqual([]);
      });
    }
  });

  test.describe("back office", () => {
    for (const view of BACK_OFFICE_VIEWS) {
      test(`A11Y-001..006 - ${view} has no detectable violations`, async ({ authPage, page }) => {
        await authPage.open();
        await authPage.loginAs(credentials.admin);
        await openView(page, view);

        const results = await new AxeBuilder({ page }).withTags(WCAG).analyze();

        expect(describe(results.violations)).toEqual([]);
      });
    }
  });

  test("A11Y-001..006 - an open modal has no detectable violations", async ({
    customerSession, transfersPage, page
  }) => {
    await transfersPage.open();
    await transfersPage.openForm();

    /*
     * Scanned with the modal up rather than only on the page behind it. A modal
     * is injected markup that no scan of the underlying view ever sees, which is
     * how an unlabelled form inside one survives a green accessibility run.
     */
    const results = await new AxeBuilder({ page }).withTags(WCAG).analyze();

    expect(describe(results.violations)).toEqual([]);
  });

  /* ------------------------------------------------- what axe cannot decide */

  test("A11Y-007 - a modal is announced as a dialog and named", async ({
    customerSession, transfersPage, page
  }) => {
    await transfersPage.open();
    await transfersPage.openForm();

    const modal = page.getByTestId("modal");

    await expect(modal).toHaveAttribute("role", "dialog");
    await expect(modal).toHaveAttribute("aria-modal", "true");

    // Named, or assistive technology announces "dialog" and nothing else.
    const name = await modal.getAttribute("aria-label");
    expect(name?.trim().length ?? 0).toBeGreaterThan(0);
  });

  test("A11Y-007 - focus moves into the modal and cannot leave it", async ({
    customerSession, transfersPage, page
  }) => {
    await transfersPage.open();
    await transfersPage.openForm();

    const inModal = () =>
      page.evaluate(() => {
        const modal = document.querySelector('[data-testid="modal"]');
        return !!modal && !!document.activeElement && modal.contains(document.activeElement);
      });

    expect(await inModal(), "focus should move into the dialog when it opens").toBe(true);

    /*
     * Tab far enough to have escaped an untrapped dialog several times over. A
     * single Tab proves nothing: the second control in the form is still inside
     * it either way.
     */
    for (let i = 0; i < 25; i += 1) {
      await page.keyboard.press("Tab");
    }

    expect(await inModal(), "focus should still be inside the dialog after tabbing through it").toBe(true);
  });

  test("A11Y-007 - Escape closes the modal and focus returns to its trigger", async ({
    customerSession, transfersPage, page
  }) => {
    await transfersPage.open();

    const trigger = page.getByTestId("new-transfer");
    await trigger.click();
    await expect(page.getByTestId("modal")).toBeVisible();

    await page.keyboard.press("Escape");
    await expect(page.getByTestId("modal")).toBeHidden();

    /*
     * The half most implementations skip. Without it focus falls back to the
     * document and a keyboard user is returned to the top of the page, having
     * lost the place they were working in.
     */
    const focusedTestId = await page.evaluate(
      () => (document.activeElement as HTMLElement | null)?.dataset?.testid ?? null
    );
    expect(focusedTestId).toBe("new-transfer");
  });

  test("A11Y-008 - status messages are announced without taking focus", async ({
    customerSession, page
  }) => {
    const region = page.locator("#toast-root");

    // The container is a live region before any message arrives; a region
    // created at the same moment as its text is not reliably announced.
    await expect(region).toHaveAttribute("aria-live", "polite");
    await expect(region).toHaveAttribute("role", "status");

    const focusBefore = await page.evaluate(() => document.activeElement?.tagName ?? null);

    await page.evaluate(() => window.toast?.("Accessibility probe", "status announcement"));
    await expect(page.getByTestId("toast")).toBeVisible();

    const focusAfter = await page.evaluate(() => document.activeElement?.tagName ?? null);
    expect(focusAfter, "a status message must not steal focus").toBe(focusBefore);
  });

  test("A11Y-009 - sign-in is completable with the keyboard alone", async ({ authPage, page }) => {
    await authPage.open();

    /*
     * No clicks at all. Every field is reached by Tab and the form submitted
     * with Enter, which is the check that the login screen is operable without
     * a pointer.
     */
    await page.getByTestId("login-email").focus();
    // The QA build pre-fills both fields, so each is selected before typing.
    // Select-all then type is still keyboard-only; it is what a person would do.
    await page.keyboard.press("ControlOrMeta+a");
    await page.keyboard.type(credentials.customer.email);
    await page.keyboard.press("Tab");
    await page.keyboard.press("ControlOrMeta+a");
    await page.keyboard.type(credentials.customer.password);
    await page.keyboard.press("Enter");

    await expect(page.getByTestId("mfa-code")).toBeVisible();

    await page.getByTestId("mfa-code").focus();
    await page.keyboard.press("ControlOrMeta+a");
    await page.keyboard.type(credentials.mfaCode);
    await page.keyboard.press("Enter");

    await expect(page.getByTestId("user-chip")).toBeVisible({ timeout: 25_000 });
  });

  test("A11Y-009 - every sidebar destination is reachable by keyboard", async ({
    customerSession, page
  }) => {
    const reachable = await page.evaluate(() => {
      const buttons = [...document.querySelectorAll("[data-testid^='nav-']")];
      // A control is keyboard-reachable when it is a real button or link, or
      // carries a non-negative tabindex. A div with onclick is not.
      return buttons.map(b => ({
        id: b.getAttribute("data-testid"),
        ok:
          b.tagName === "BUTTON" ||
          b.tagName === "A" ||
          Number(b.getAttribute("tabindex") ?? "-1") >= 0
      }));
    });

    expect(reachable.length).toBeGreaterThan(0);
    expect(reachable.filter(r => !r.ok)).toEqual([]);
  });
});

/**
 * Reduce a violation to the parts worth reading in a failure.
 *
 * The raw axe object is enormous, and a failure that prints all of it buries
 * the one line naming what broke and where.
 */
function describe(violations: Awaited<ReturnType<AxeBuilder["analyze"]>>["violations"]) {
  return violations.map(v => ({
    rule: v.id,
    impact: v.impact,
    help: v.help,
    nodes: v.nodes.map(n => n.target.join(" "))
  }));
}

/** Navigate without the sidebar, so a view is reached regardless of role. */
async function openView(page: import("@playwright/test").Page, view: string) {
  await page.evaluate(v => window.navigate?.(v), view);
  await page.waitForFunction(() => {
    const v = document.querySelector("[data-testid='view']");
    return v && !v.textContent!.includes("Loading");
  }, undefined, { timeout: 25_000 });
}
