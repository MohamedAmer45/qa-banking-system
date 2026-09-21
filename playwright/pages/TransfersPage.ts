import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class TransfersPage extends BasePage {
  readonly newTransferButton: Locator;
  readonly form: Locator;
  readonly fromAccount: Locator;
  readonly destinationType: Locator;
  readonly beneficiary: Locator;
  readonly ownAccount: Locator;
  readonly amount: Locator;
  readonly memo: Locator;
  readonly submit: Locator;

  constructor(page: Page) {
    super(page);

    this.newTransferButton = this.testId("new-transfer");
    this.form = this.testId("transfer-form");
    this.fromAccount = this.testId("transfer-from");
    this.destinationType = this.testId("transfer-dest-type");
    this.beneficiary = this.testId("transfer-beneficiary");
    this.ownAccount = this.testId("transfer-own-account");
    this.amount = this.testId("transfer-amount");
    this.memo = this.testId("transfer-memo");
    this.submit = this.testId("transfer-submit");
  }

  /** Transfers view: history plus the button that opens the transfer form. */
  async open(): Promise<void> {
    await this.openView("transfers", /Transfers/i);
    await expect(this.newTransferButton).toBeVisible();
  }

  /** The transfer form is a modal; it does not exist until this is clicked. */
  async openForm(): Promise<void> {
    await this.newTransferButton.click();
    await expect(this.form).toBeVisible();
  }

  async expectLoaded(): Promise<void> {
    await expect(this.form).toBeVisible();
    await expect(this.fromAccount).toBeVisible();
    await expect(this.amount).toBeVisible();
    await expect(this.submit).toBeEnabled();
  }

  async sourceAccountCount(): Promise<number> {
    return this.fromAccount.locator("option").count();
  }

  get history(): Locator {
    return this.page.locator("table.table");
  }

  /**
   * Submits a transfer and waits for the API to answer, returning the status
   * so a caller can distinguish a business rejection (409) from a downstream
   * failure (422) without re-reading the DOM.
   */
  async submitTransfer(options: {
    fromIndex?: number;
    beneficiaryIndex?: number;
    amount: number | string;
    memo?: string;
  }): Promise<number> {
    if (options.fromIndex !== undefined) {
      await this.fromAccount.selectOption({ index: options.fromIndex });
    }

    if (options.beneficiaryIndex !== undefined) {
      await this.destinationType.selectOption("beneficiary");
      await this.beneficiary.selectOption({ index: options.beneficiaryIndex });
    }

    await this.amount.fill(String(options.amount));

    if (options.memo) {
      await this.memo.fill(options.memo);
    }

    const response = this.page.waitForResponse(
      r => r.url().includes("/api/transfers") && r.request().method() === "POST"
    );

    await this.submit.click();
    return (await response).status();
  }

  async expectNativeAmountInvalid(value: string): Promise<void> {
    await this.amount.fill(value);

    const valid = await this.amount.evaluate(
      node => (node as HTMLInputElement).checkValidity()
    );

    expect(valid).toBe(false);
  }
}
