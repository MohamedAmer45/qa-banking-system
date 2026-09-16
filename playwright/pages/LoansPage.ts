import {
  expect,
  Locator,
  Page,
  Response
} from "@playwright/test";

import { BasePage } from "./BasePage";

export class LoansPage extends BasePage {

  readonly navigationButton: Locator;

  readonly section: Locator;

  readonly heading: Locator;

  readonly loanInfo: Locator;

  readonly form: Locator;

  readonly amountInput: Locator;

  readonly termSelect: Locator;

  readonly applyButton: Locator;

  readonly toast: Locator;

  readonly notificationsNavigationButton: Locator;

  readonly notificationsSection: Locator;

  readonly notifications: Locator;


  constructor(page: Page) {

    super(page);

    this.navigationButton =
      page.locator(
        '#nav button[data-s="loans"]'
      );

    this.section =
      page.locator("#loans");

    this.heading =
      this.section.getByRole(
        "heading",
        {
          name: "Loans"
        }
      );

    this.loanInfo =
      page.locator("#loanInfo");

    this.form =
      page.locator("#loanForm");

    this.amountInput =
      page.locator("#loanAmount");

    this.termSelect =
      page.locator("#term");

    this.applyButton =
      this.form.getByRole(
        "button",
        {
          name: "Apply"
        }
      );

    this.toast =
      page.locator("#toast");

    this.notificationsNavigationButton =
      page.locator(
        '#nav button[data-s="notifications"]'
      );

    this.notificationsSection =
      page.locator("#notifications");

    this.notifications =
      page.locator("#notes .card");
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
      this.loanInfo
    ).toBeVisible();

    await expect(
      this.form
    ).toBeVisible();
  }


  async expectLoanDetails(
    type: string,
    balance: string,
    apr: string,
    nextPayment: string,
    dueDate: string
  ): Promise<void> {

    await expect(
      this.loanInfo.getByRole(
        "heading",
        {
          name: type,
          exact: true
        }
      )
    ).toBeVisible();

    await expect(
      this.loanInfo
    ).toContainText(balance);

    await expect(
      this.loanInfo
    ).toContainText(
      `APR ${apr}`
    );

    await expect(
      this.loanInfo
    ).toContainText(
      `Next ${nextPayment}`
    );

    await expect(
      this.loanInfo
    ).toContainText(
      dueDate
    );
  }


  async expectAmountConstraints(
    min: string,
    max: string
  ): Promise<void> {

    await expect(
      this.amountInput
    ).toHaveAttribute(
      "type",
      "number"
    );

    await expect(
      this.amountInput
    ).toHaveAttribute(
      "min",
      min
    );

    await expect(
      this.amountInput
    ).toHaveAttribute(
      "max",
      max
    );

    await expect(
      this.amountInput
    ).toHaveAttribute(
      "required",
      ""
    );
  }


  async expectSupportedTerms(
    expectedTerms: string[]
  ): Promise<void> {

    const options =
      (
        await this.termSelect
          .locator("option")
          .allTextContents()
      ).map(
        text => text.trim()
      );

    expect(
      options
    ).toEqual(
      expectedTerms
    );
  }


  async expectTermValues(
    expectedValues: string[]
  ): Promise<void> {

    const options =
      this.termSelect.locator(
        "option"
      );

    const count =
      await options.count();

    const actualValues: string[] =
      [];

    for (
      let index = 0;
      index < count;
      index++
    ) {

      const value =
        await options
          .nth(index)
          .getAttribute("value");

      actualValues.push(
        value ?? ""
      );
    }

    expect(
      actualValues
    ).toEqual(
      expectedValues
    );
  }


  async expectAmountValidity(
    amount: number,
    expectedValid: boolean
  ): Promise<void> {

    await this.amountInput.fill(
      String(amount)
    );

    const valid =
      await this.amountInput.evaluate(
        element =>
          (
            element as HTMLInputElement
          ).checkValidity()
      );

    expect(
      valid
    ).toBe(
      expectedValid
    );
  }


  async submitLoan(
    amount: number,
    termMonths: number
  ): Promise<{
    response: Response;
    body: {
      id?: string;
      [key: string]: unknown;
    };
  }> {

    await this.amountInput.fill(
      String(amount)
    );

    await this.termSelect.selectOption(
      String(termMonths)
    );

    const responsePromise =
      this.page.waitForResponse(
        response =>
          response.url().includes(
            "/api/loans/apply"
          ) &&
          response.request().method() ===
            "POST"
      );

    await this.applyButton.click();

    const response =
      await responsePromise;

    expect(
      response.ok()
    ).toBeTruthy();

    const body =
      await response.json();

    return {
      response,
      body
    };
  }


  async expectSuccessToast():
    Promise<void> {

    await expect(
      this.toast
    ).toHaveText(
      "Loan application submitted."
    );
  }


  async expectFormReset():
    Promise<void> {

    await expect(
      this.amountInput
    ).toHaveValue("");

    await expect(
      this.termSelect
    ).toHaveValue("12");
  }


  async openNotifications():
    Promise<void> {

    await this
      .notificationsNavigationButton
      .click();

    await expect(
      this.notificationsSection
    ).toBeVisible();
  }


  async expectNewestNotification(
    expectedText: string
  ): Promise<void> {

    await expect(
      this.notifications.first()
    ).toHaveText(
      expectedText
    );
  }


  async reloadAndOpenNotifications():
    Promise<void> {

    await this.page.reload();

    await this.page.waitForLoadState(
      "domcontentloaded"
    );

    await this.openNotifications();
  }
}

