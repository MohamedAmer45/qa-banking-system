import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class AdminPage extends BasePage {
  readonly customerTable: Locator;
  readonly auditTable: Locator;

  constructor(page: Page) {
    super(page);
    this.customerTable = this.testId("customer-table");
    this.auditTable = this.testId("audit-table");
  }

  async openDashboard(): Promise<void> {
    await this.openView("admin-dashboard", /Operations/i);
  }

  async openCustomers(): Promise<void> {
    await this.openView("admin-customers", /Customers/i);
    await expect(this.customerTable).toBeVisible();
  }

  async openAudit(): Promise<void> {
    await this.openView("admin-audit", /Audit/i);
    await expect(this.auditTable).toBeVisible();
  }

  async expectMetricsVisible(): Promise<void> {
    await expect(this.page.getByText(/Customers/i).first()).toBeVisible();
  }
}
