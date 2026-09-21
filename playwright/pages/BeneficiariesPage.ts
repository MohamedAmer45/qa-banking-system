import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class BeneficiariesPage extends BasePage {
  readonly table: Locator;
  readonly submit: Locator;

  constructor(page: Page) {
    super(page);
    this.table = this.testId("beneficiary-table");
    this.submit = this.testId("beneficiary-submit");
  }

  async open(): Promise<void> {
    await this.openView("beneficiaries", /Beneficiar/i);
  }

  async expectLoaded(): Promise<void> {
    await expect(this.table).toBeVisible();
  }

  async rowCount(): Promise<number> {
    return this.table.locator("tbody tr").count();
  }

  async expectContains(name: string): Promise<void> {
    await expect(this.table).toContainText(name);
  }
}
