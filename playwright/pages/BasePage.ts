import { Page, Locator, expect } from "@playwright/test";

/**
 * Shared plumbing for every page object.
 *
 * Locators resolve through `data-testid` attributes the application exposes
 * specifically for automation, rather than through layout classes or visible
 * copy, so a styling or wording change does not break a suite.
 */
export abstract class BasePage {
  protected readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  /** Resolve an element by its `data-testid`. */
  protected testId(id: string): Locator {
    return this.page.getByTestId(id);
  }

  get toast(): Locator {
    return this.testId("toast");
  }

  get pageTitle(): Locator {
    return this.testId("page-title");
  }

  get modal(): Locator {
    return this.testId("modal");
  }

  get view(): Locator {
    return this.testId("view");
  }

  /**
   * Wait for a view render to finish.
   *
   * navigate() blanks the view to a "Loading…" placeholder and then awaits its
   * API calls, so the heading updates before the body exists. Without this,
   * a click issued straight after sign-in races the initial render and the
   * late-arriving overview overwrites the view the test just asked for.
   */
  async waitForViewReady(): Promise<void> {
    await expect(this.view).not.toContainText("Loading…", { timeout: 15_000 });
  }

  async navigate(path = "/"): Promise<void> {
    await this.page.goto(path, { waitUntil: "domcontentloaded" });
  }

  /**
   * Dismiss an open modal if there is one. A rejected form submission leaves
   * its modal up, which would otherwise swallow the next navigation click.
   */
  async dismissModal(): Promise<void> {
    if (await this.modal.isVisible()) {
      await this.testId("modal-close").click();
      await expect(this.modal).toBeHidden();
    }
  }

  /**
   * Move to an application view through the sidebar and wait for the heading
   * to settle, so callers never assert against the previous view's content.
   */
  async openView(view: string, expectedTitle: RegExp): Promise<void> {
    await this.dismissModal();
    await this.waitForViewReady();

    await this.testId(`nav-${view}`).click();
    await expect(this.pageTitle).toHaveText(expectedTitle);
    await this.waitForViewReady();
  }

  async expectToast(text: string | RegExp): Promise<void> {
    await expect(this.toast).toContainText(text);
  }

  async reload(): Promise<void> {
    await this.page.reload({ waitUntil: "domcontentloaded" });
  }

  async getCurrentUrl(): Promise<string> {
    return this.page.url();
  }
}
