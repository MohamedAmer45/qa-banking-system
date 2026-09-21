import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class DashboardPage extends BasePage {
  readonly unreadCount: Locator;

  constructor(page: Page) {
    super(page);
    this.unreadCount = this.testId("unread-count");
  }

  async open(): Promise<void> {
    await this.openView("overview", /Overview/i);
  }

  async expectLoaded(): Promise<void> {
    await expect(this.view).toBeVisible();
    await expect(this.page.getByText(/EGP balance/i)).toBeVisible();
  }

  /** Every nav destination available to the signed-in role. */
  async visibleNavItems(): Promise<string[]> {
    return this.page
      .locator("[data-testid^='nav-']")
      .evaluateAll(nodes =>
        nodes.map(n => n.getAttribute("data-testid")!.replace("nav-", ""))
      );
  }
}
