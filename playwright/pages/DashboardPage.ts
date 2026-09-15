import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import { BasePage } from "./BasePage";

export class DashboardPage extends BasePage {

  readonly section: Locator;
  readonly heading: Locator;

  readonly totalBalance: Locator;
  readonly metrics: Locator;
  readonly recentTransactions: Locator;

  readonly recentTransactionRows: Locator;

  constructor(page: Page) {
    super(page);

    this.section = page.locator("#dashboard");

    this.heading = this.section.getByRole(
      "heading",
      {
        name: "Dashboard"
      }
    );

    this.totalBalance = page.locator("#total");

    this.metrics = page.locator("#metrics");

    this.recentTransactions = page.locator("#recent");

    this.recentTransactionRows =
      this.recentTransactions.locator(
        "tbody tr"
      );
  }


  async expectLoaded(): Promise<void> {

    await expect(this.section).toBeVisible();

    await expect(this.heading).toBeVisible();

    await expect(this.totalBalance).toBeVisible();

    await expect(this.metrics).toBeVisible();

    await expect(
      this.recentTransactions
    ).toBeVisible();

  }


  async expectTotalBalance(
    balance: string
  ): Promise<void> {

    await expect(
      this.totalBalance
    ).toHaveText(balance);

  }


  private metricCard(
    name: string
  ): Locator {

    return this.metrics
      .locator(".card")
      .filter({
        hasText: name
      });

  }


  async expectMetric(
    name: string,
    value: string
  ): Promise<void> {

    const card =
      this.metricCard(name);

    await expect(card).toBeVisible();

    await expect(
      card.locator(".metric")
    ).toHaveText(value);

  }


  async expectRecentTransactionCount(
    count: number
  ): Promise<void> {

    await expect(
      this.recentTransactionRows
    ).toHaveCount(count);

  }


  async expectRecentTransaction(
    description: string,
    amount: string
  ): Promise<void> {

    const row =
      this.recentTransactionRows
      .filter({
        hasText: description
      });

    await expect(row).toBeVisible();

    await expect(row).toContainText(
      description
    );

    await expect(row).toContainText(
      amount
    );

  }


  async getDisplayedTotalBalance():
    Promise<number> {

    const text =
      await this.totalBalance.innerText();

    return Number(
      text
        .replace("$", "")
        .replace(/,/g, "")
    );

  }


  async getMetricBalance(
    name: string
  ): Promise<number> {

    const text =
      await this
        .metricCard(name)
        .locator(".metric")
        .innerText();

    return Number(
      text
        .replace("$", "")
        .replace(/,/g, "")
    );

  }

}
