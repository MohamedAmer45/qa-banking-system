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

  /**
   * The balance of one specific account.
   *
   * Preferred over balanceMinor(index) whenever the test chose its account for
   * a reason: the accounts view lists every account while the transfer form
   * lists only the ACTIVE ones, so the same position is not the same account.
   */
  async balanceMinorFor(accountId: number): Promise<number> {
    const value = await this.page
      .locator(`[data-testid="account-card"][data-account-id="${accountId}"]`)
      .locator('[data-testid="account-balance"]')
      .getAttribute("data-balance-minor");

    if (value === null) {
      throw new Error(`No account card rendered for account ${accountId}`);
    }

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
