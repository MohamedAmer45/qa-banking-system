import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import { BasePage } from "./BasePage";


export class BillsPage extends BasePage {

  readonly navigationButton: Locator;

  readonly section: Locator;

  readonly heading: Locator;

  readonly billerSelect: Locator;

  readonly amountInput: Locator;

  readonly payButton: Locator;

  readonly toast: Locator;


  constructor(page: Page) {

    super(page);

    this.navigationButton =
      page.locator(
        '#nav button[data-s="bills"]'
      );

    this.section =
      page.locator("#bills");

    this.heading =
      this.section.getByRole(
        "heading",
        {
          name: "Bills"
        }
      );

    this.billerSelect =
      page.locator("#biller");

    this.amountInput =
      page.locator("#billAmount");

    this.payButton =
      this.section.getByRole(
        "button",
        {
          name: "Pay bill"
        }
      );

    this.toast =
      page.locator("#toast");

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
      this.billerSelect
    ).toBeVisible();

    await expect(
      this.amountInput
    ).toBeVisible();

    await expect(
      this.payButton
    ).toBeVisible();

  }


  async expectBillers(
    expectedBillers: string[]
  ): Promise<void> {

    const options =
      this.billerSelect.locator(
        "option"
      );

    await expect(
      options
    ).toHaveCount(
      expectedBillers.length
    );

    for (
      let index = 0;
      index < expectedBillers.length;
      index++
    ) {

      await expect(
        options.nth(index)
      ).toHaveText(
        expectedBillers[index]
      );

    }

  }


  async selectBiller(
    biller: string
  ): Promise<void> {

    await this.billerSelect.selectOption({
      label: biller
    });

  }


  async enterAmount(
    amount: number
  ): Promise<void> {

    await this.amountInput.fill(
      String(amount)
    );

  }


  async payBill(
    biller: string,
    amount: number
  ): Promise<void> {

    await this.selectBiller(
      biller
    );

    await this.enterAmount(
      amount
    );

    const responsePromise =
      this.page.waitForResponse(
        response =>
          response.url().includes(
            "/api/bills/pay"
          ) &&
          response.request().method() ===
            "POST"
      );

    await this.payButton.click();

    const response =
      await responsePromise;

    expect(
      response.ok()
    ).toBeTruthy();

    await expect(
      this.toast
    ).toHaveText(
      "Bill paid."
    );

    await expect(
      this.toast
    ).toBeVisible();

  }


  async attemptRejectedBill(
    biller: string,
    amount: number,
    expectedMessage: string
  ): Promise<void> {

    await this.selectBiller(
      biller
    );

    await this.enterAmount(
      amount
    );

    await this.payButton.click();

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


  async expectFormReset():
    Promise<void> {

    await expect(
      this.amountInput
    ).toHaveValue("");

    await expect(
      this.billerSelect
    ).toHaveValue(
      "Electricity"
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
