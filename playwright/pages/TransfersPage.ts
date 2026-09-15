import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import { BasePage } from "./BasePage";


export class TransfersPage extends BasePage {

  readonly navigationButton: Locator;

  readonly section: Locator;

  readonly heading: Locator;

  readonly fromSelect: Locator;

  readonly recipientSelect: Locator;

  readonly amountInput: Locator;

  readonly submitButton: Locator;

  readonly toast: Locator;

  readonly transactionsNavigationButton: Locator;

  readonly transactionsSection: Locator;

  readonly transactionRows: Locator;


  constructor(page: Page) {

    super(page);

    this.navigationButton =
      page.locator(
        '#nav button[data-s="transfers"]'
      );

    this.section =
      page.locator("#transfers");

    this.heading =
      this.section.getByRole(
        "heading",
        {
          name: "Transfers"
        }
      );

    this.fromSelect =
      page.locator("#from");

    this.recipientSelect =
      page.locator("#recipient");

    this.amountInput =
      page.locator("#transferAmount");

    this.submitButton =
      this.section.getByRole(
        "button",
        {
          name: "Send transfer"
        }
      );

    this.toast =
      page.locator("#toast");

    this.transactionsNavigationButton =
      page.locator(
        '#nav button[data-s="transactions"]'
      );

    this.transactionsSection =
      page.locator("#transactions");

    this.transactionRows =
      page.locator(
        "#tx tbody tr"
      );

  }


  async open(): Promise<void> {

    await this.navigationButton.click();

    await this.expectLoaded();

  }


  async expectLoaded(): Promise<void> {

    await expect(
      this.section
    ).toBeVisible();

    await expect(
      this.heading
    ).toBeVisible();

    await expect(
      this.fromSelect
    ).toBeVisible();

    await expect(
      this.recipientSelect
    ).toBeVisible();

    await expect(
      this.amountInput
    ).toBeVisible();

    await expect(
      this.submitButton
    ).toBeVisible();

  }


  async expectSourceAccounts(): Promise<void> {

    const options =
      this.fromSelect.locator("option");

    await expect(
      options
    ).toHaveCount(2);

    await expect(
      options.nth(0)
    ).toContainText("Checking");

    await expect(
      options.nth(0)
    ).toContainText("$12,840.75");

    await expect(
      options.nth(1)
    ).toContainText("Savings");

    await expect(
      options.nth(1)
    ).toContainText("$32,500.00");

  }


  async expectRecipients(): Promise<void> {

    const options =
      this.recipientSelect.locator("option");

    await expect(
      options
    ).toHaveCount(2);

    await expect(
      options.nth(0)
    ).toHaveText("Alex Johnson");

    await expect(
      options.nth(1)
    ).toHaveText("Sam Lee");

  }


  async selectSource(
    index: number
  ): Promise<void> {

    await this.fromSelect.selectOption(
      String(index)
    );

  }


  async selectRecipient(
    recipient: string
  ): Promise<void> {

    await this.recipientSelect.selectOption({
      label: recipient
    });

  }


  async enterAmount(
    amount: number
  ): Promise<void> {

    await this.amountInput.fill(
      String(amount)
    );

  }


  async getSourceBalance(
    index: number
  ): Promise<number> {

    const text =
      await this.fromSelect
        .locator("option")
        .nth(index)
        .innerText();

    const match =
      text.match(
        /\$([\d,]+\.\d{2})/
      );

    if (!match) {
      throw new Error(
        "Could not parse account balance from: " + text
      );
    }

    return Number(
      match[1].replace(/,/g, "")
    );

  }


  async performSuccessfulTransfer(
    sourceIndex: number,
    recipient: string,
    amount: number
  ): Promise<void> {

    await this.selectSource(
      sourceIndex
    );

    await this.selectRecipient(
      recipient
    );

    await this.enterAmount(
      amount
    );

    const responsePromise =
      this.page.waitForResponse(
        response =>
          response.url().includes(
            "/api/transfers"
          ) &&
          response.request().method() ===
            "POST"
      );

    await this.submitButton.click();

    const response =
      await responsePromise;

    expect(
      response.ok()
    ).toBeTruthy();

    await expect(
      this.toast
    ).toHaveText(
      "Transfer completed."
    );

    await expect(
      this.toast
    ).toBeVisible();

  }


  async attemptClientRejectedTransfer(
    sourceIndex: number,
    recipient: string,
    amount: number,
    expectedMessage: string
  ): Promise<void> {

    await this.selectSource(
      sourceIndex
    );

    await this.selectRecipient(
      recipient
    );

    await this.enterAmount(
      amount
    );

    await this.submitButton.click();

    await expect(
      this.toast
    ).toHaveText(
      expectedMessage
    );

    await expect(
      this.toast
    ).toBeVisible();

  }


  async expectNativeAmountInvalid(
    amount: number
  ): Promise<void> {

    await this.enterAmount(
      amount
    );

    const validity =
      await this.amountInput.evaluate(
        (element: HTMLInputElement) => ({
          valid:
            element.checkValidity(),

          rangeUnderflow:
            element.validity.rangeUnderflow,

          valueMissing:
            element.validity.valueMissing
        })
      );

    expect(
      validity.valid
    ).toBeFalsy();

    expect(
      validity.rangeUnderflow
    ).toBeTruthy();

  }


  async openTransactions():
    Promise<void> {

    await this.transactionsNavigationButton.click();

    await expect(
      this.transactionsSection
    ).toBeVisible();

  }


  async expectTransaction(
    description: string,
    amount: string
  ): Promise<void> {

    const row =
      this.transactionRows.filter({
        hasText: description
      }).first();

    await expect(
      row
    ).toBeVisible();

    await expect(
      row
    ).toContainText(
      description
    );

    await expect(
      row
    ).toContainText(
      amount
    );

    await expect(
      row
    ).toContainText(
      "completed"
    );

  }


  async expectTransactionCount(
    count: number
  ): Promise<void> {

    await expect(
      this.transactionRows
    ).toHaveCount(
      count
    );

  }


  async reloadAndOpen():
    Promise<void> {

    await this.page.reload();

    await this.page.waitForLoadState(
      "domcontentloaded"
    );

    await this.open();

  }

}
