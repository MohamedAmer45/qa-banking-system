import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class AccountsPage extends BasePage {
  readonly cards: Locator;
  readonly balances: Locator;

  constructor(page: Page) {
    super(page);
    this.cards = this.testId("account-card");
    this.balances = this.testId("account-balance");
  }

  async open(): Promise<void> {
    await this.openView("accounts", /Accounts/i);
  }

  async count(): Promise<number> {
    return this.cards.count();
  }

  /**
   * Reads the integer minor-unit balance the ledger holds, rather than parsing
   * the formatted currency string, so assertions never depend on locale.
   */
  async balanceMinor(index: number): Promise<number> {
    const value = await this.balances.nth(index).getAttribute("data-balance-minor");
    return Number(value);
  }

  async balanceMinorTotal(): Promise<number> {
    const values = await this.balances.evaluateAll(nodes =>
      nodes.map(n => Number(n.getAttribute("data-balance-minor")))
    );
    return values.reduce((sum, v) => sum + v, 0);
  }

  async expectLoaded(): Promise<void> {
    await expect(this.cards.first()).toBeVisible();
  }
}
