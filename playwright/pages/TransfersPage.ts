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
   * Choose a source account that can actually fund the transfer.
   *
   * Picking the first account in the list assumes it has both the balance and
   * the remaining daily allowance for the amount. Against a shared environment
   * that is never reset, that assumption expires: once earlier runs have used
   * up a day's allowance on that account, the transfer is refused by the daily
   * limit and the test reports a transfer defect that does not exist.
   *
   * Selecting on the account's actual headroom keeps the test about the thing
   * it is named for.
   *
   * @param amountMinor the transfer amount in minor units
   * @returns the id of the account selected in the form
   */
  async selectFundedSource(amountMinor: number): Promise<number> {
    const account = await this.page.evaluate(async (needed: number) => {
      const token = window.localStorage.getItem("novabank_token");
      const response = await fetch("/api/accounts", {
        headers: { Authorization: `Bearer ${token}` }
      });
      const accounts = await response.json();

      // Headroom above the amount, because an external beneficiary also
      // attracts a fee and the daily counter includes it.
      const margin = Math.max(Math.round(needed * 0.1), 10_000);

      return accounts.find(
        (a: Record<string, number | string>) =>
          a.status === "ACTIVE" &&
          a.currency === "EGP" &&
          Number(a.available_minor) >= needed + margin &&
          Number(a.daily_limit_minor) - Number(a.daily_transferred_minor) >= needed + margin
      ) ?? null;
    }, amountMinor);

    if (!account) {
      throw new Error(
        `No ACTIVE EGP account has both the balance and the remaining daily ` +
        `allowance for ${amountMinor} minor units. Against a shared environment ` +
        `this means the day's allowance is spent, not that transfers are broken.`
      );
    }

    await this.fromAccount.selectOption({ value: String(account.id) });
    return Number(account.id);
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
