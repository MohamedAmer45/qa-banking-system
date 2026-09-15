import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import { BasePage } from "./BasePage";


export class TransactionsPage extends BasePage {

  readonly navigationButton: Locator;

  readonly section: Locator;

  readonly heading: Locator;

  readonly table: Locator;

  readonly headers: Locator;

  readonly rows: Locator;


  constructor(page: Page) {

    super(page);

    this.navigationButton =
      page.locator(
        '#nav button[data-s="transactions"]'
      );

    this.section =
      page.locator("#transactions");

    this.heading =
      this.section.getByRole(
        "heading",
        {
          name: "Transactions"
        }
      );

    this.table =
      page.locator(
        "#tx table"
      );

    this.headers =
      this.table.locator(
        "thead th"
      );

    this.rows =
      this.table.locator(
        "tbody tr"
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
      this.table
    ).toBeVisible();

    const count =
      await this.rows.count();

    expect(
      count
    ).toBeGreaterThan(0);

    await expect(
      this.rows.first()
    ).toBeVisible();

  }


  async expectHeaders(): Promise<void> {

    await expect(
      this.headers
    ).toHaveCount(4);

    await expect(
      this.headers.nth(0)
    ).toHaveText("Date");

    await expect(
      this.headers.nth(1)
    ).toHaveText("Description");

    await expect(
      this.headers.nth(2)
    ).toHaveText("Status");

    await expect(
      this.headers.nth(3)
    ).toHaveText("Amount");

  }


  async expectTransactionCount(
    count: number
  ): Promise<void> {

    await expect(
      this.rows
    ).toHaveCount(count);

  }


  private transactionRow(
    description: string
  ): Locator {

    return this.rows
      .filter({
        hasText: description
      })
      .first();

  }


  async expectTransaction(
    description: string,
    expectedDate: string,
    expectedStatus: string,
    expectedAmount: string
  ): Promise<void> {

    const row =
      this.transactionRow(
        description
      );

    await expect(
      row
    ).toBeVisible();

    const cells =
      row.locator("td");

    await expect(
      cells
    ).toHaveCount(4);

    await expect(
      cells.nth(0)
    ).toHaveText(
      expectedDate
    );

    await expect(
      cells.nth(1)
    ).toHaveText(
      description
    );

    await expect(
      cells.nth(2)
    ).toContainText(
      expectedStatus
    );

    await expect(
      cells.nth(3)
    ).toHaveText(
      expectedAmount
    );

  }


  async expectAllStatuses(
    expectedStatus: string
  ): Promise<void> {

    const count =
      await this.rows.count();

    for (
      let index = 0;
      index < count;
      index++
    ) {

      const status =
        this.rows
          .nth(index)
          .locator("td")
          .nth(2);

      await expect(
        status
      ).toContainText(
        expectedStatus
      );

    }

  }


  async expectCreditTransaction(
    description: string
  ): Promise<void> {

    const amountCell =
      this.transactionRow(
        description
      )
        .locator("td")
        .nth(3);

    await expect(
      amountCell
    ).toHaveClass(
      /credit/
    );

    await expect(
      amountCell
    ).toContainText(
      "+"
    );

  }


  async expectDebitTransaction(
    description: string
  ): Promise<void> {

    const amountCell =
      this.transactionRow(
        description
      )
        .locator("td")
        .nth(3);

    await expect(
      amountCell
    ).toHaveClass(
      /debit/
    );

    await expect(
      amountCell
    ).toContainText(
      "-"
    );

  }


  async getDates(): Promise<string[]> {

    const count =
      await this.rows.count();

    const dates: string[] = [];

    for (
      let index = 0;
      index < count;
      index++
    ) {

      const date =
        await this.rows
          .nth(index)
          .locator("td")
          .nth(0)
          .innerText();

      dates.push(
        date.trim()
      );

    }

    return dates;

  }


  async expectNewestFirst():
    Promise<void> {

    const dates =
      await this.getDates();

    const timestamps =
      dates.map(
        date =>
          new Date(
            `${date}T00:00:00Z`
          ).getTime()
      );

    const sorted =
      [...timestamps].sort(
        (a, b) => b - a
      );

    expect(
      timestamps
    ).toEqual(
      sorted
    );

  }


  async expectFirstTransaction(
    description: string,
    amount: string
  ): Promise<void> {

    const firstRow =
      this.rows.first();

    await expect(
      firstRow
    ).toContainText(
      description
    );

    await expect(
      firstRow
    ).toContainText(
      amount
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
