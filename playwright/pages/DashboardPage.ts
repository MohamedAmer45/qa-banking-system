import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class DashboardPage extends BasePage {
  readonly unreadCount: Locator;

  // DASH-001..007. Each panel carries its own test id so a missing panel
  // fails as "the cards panel is absent", not as a text search that found
  // the word somewhere else on the page.
  readonly accountsPanel: Locator;
  readonly alertsPanel: Locator;
  readonly transactionsPanel: Locator;
  readonly cardsPanel: Locator;
  readonly scheduledPanel: Locator;

  readonly accountCards: Locator;
  readonly transactionRows: Locator;
  readonly cardItems: Locator;
  readonly scheduledItems: Locator;
  readonly alertItems: Locator;

  readonly overviewUnreadCount: Locator;
  readonly cardsCount: Locator;
  readonly scheduledCount: Locator;
  readonly accountsKpiValue: Locator;
  readonly egpBalanceKpi: Locator;

  constructor(page: Page) {
    super(page);
    this.unreadCount = this.testId("unread-count");

    this.accountsPanel = this.testId("overview-accounts");
    this.alertsPanel = this.testId("overview-alerts");
    this.transactionsPanel = this.testId("overview-transactions");
    this.cardsPanel = this.testId("overview-cards");
    this.scheduledPanel = this.testId("overview-scheduled");

    this.accountCards = this.testId("account-card");
    this.transactionRows = this.testId("overview-transaction-row");
    this.cardItems = this.testId("overview-card-item");
    this.scheduledItems = this.testId("overview-scheduled-item");
    this.alertItems = this.testId("overview-alert-item");

    this.overviewUnreadCount = this.testId("overview-unread-count");
    this.cardsCount = this.testId("overview-cards-count");
    this.scheduledCount = this.testId("overview-scheduled-count");
    this.accountsKpiValue = this.testId("kpi-accounts-value");
    this.egpBalanceKpi = this.testId("kpi-egp-balance");
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

  /**
   * A panel is present with either its rows or its own empty state. A panel
   * that rendered neither has failed to render at all, which is the condition
   * worth separating from "this customer happens to have none".
   */
  async panelIsPopulatedOrEmpty(panel: Locator, emptyTestId: string): Promise<boolean> {
    await expect(panel).toBeVisible();
    return (await panel.getByTestId(emptyTestId).count()) === 0;
  }

  /** The EGP total the dashboard claims, in minor units. */
  async egpTotalMinor(): Promise<number> {
    const raw = await this.egpBalanceKpi.getAttribute("data-balance-minor");
    return Number(raw);
  }

  /** Balances the account cards show, in minor units. */
  async accountBalancesMinor(): Promise<number[]> {
    return this.testId("account-balance").evaluateAll(nodes =>
      nodes.map(n => Number(n.getAttribute("data-balance-minor")))
    );
  }

  async accountAvailableMinor(): Promise<number[]> {
    return this.testId("account-available").evaluateAll(nodes =>
      nodes.map(n => Number(n.getAttribute("data-available-minor")))
    );
  }

  /** The whole dashboard as text, for own-data assertions. */
  async text(): Promise<string> {
    return (await this.view.textContent()) ?? "";
  }
}
