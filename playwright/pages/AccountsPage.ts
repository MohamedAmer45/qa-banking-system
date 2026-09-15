import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import { BasePage } from "./BasePage";


export class AccountsPage extends BasePage {

  readonly navigationButton: Locator;

  readonly section: Locator;

  readonly heading: Locator;

  readonly accountCards: Locator;


  constructor(page: Page) {

    super(page);

    this.navigationButton =
      page.locator(
        '#nav button[data-s="accounts"]'
      );

    this.section =
      page.locator("#accounts");

    this.heading =
      this.section.getByRole(
        "heading",
        {
          name: "Accounts"
        }
      );

    this.accountCards =
      page.locator(
        "#accountCards .card"
      );

  }


  async open(): Promise<void> {

    await this.navigationButton.click();

    await expect(
      this.section
    ).toBeVisible();

    await expect(
      this.heading
    ).toBeVisible();

  }


  async expectLoaded(): Promise<void> {

    await expect(
      this.section
    ).toBeVisible();

    await expect(
      this.heading
    ).toBeVisible();

    /*
     * accountCards intentionally matches multiple cards.
     *
     * Playwright strict mode does not allow:
     *
     * expect(this.accountCards).toBeVisible()
     *
     * when more than one element matches.
     *
     * Instead:
     * 1. Verify at least one account exists.
     * 2. Verify the first account card is visible.
     */

    const accountCount =
      await this.accountCards.count();

    expect(
      accountCount
    ).toBeGreaterThan(0);

    await expect(
      this.accountCards.first()
    ).toBeVisible();

  }


  async expectAccountCount(
    count: number
  ): Promise<void> {

    await expect(
      this.accountCards
    ).toHaveCount(count);

  }


  private accountCard(
    accountType: string
  ): Locator {

    return this.accountCards.filter({
      has: this.page.getByRole(
        "heading",
        {
          name: accountType,
          exact: true
        }
      )
    });

  }


  async expectAccount(
    accountType: string,
    maskedNumber: string,
    balance: string
  ): Promise<void> {

    const card =
      this.accountCard(
        accountType
      );

    await expect(
      card
    ).toBeVisible();

    await expect(
      card.getByRole(
        "heading",
        {
          name: accountType,
          exact: true
        }
      )
    ).toBeVisible();

    await expect(
      card
    ).toContainText(
      maskedNumber
    );

    await expect(
      card.locator(".metric")
    ).toHaveText(
      balance
    );

  }


  async getAccountBalance(
    accountType: string
  ): Promise<number> {

    const text =
      await this
        .accountCard(accountType)
        .locator(".metric")
        .innerText();

    return Number(
      text
        .replace("$", "")
        .replace(/,/g, "")
    );

  }


  async getAccountNumber(
    accountType: string
  ): Promise<string> {

    const card =
      this.accountCard(
        accountType
      );

    const text =
      await card
        .locator(".muted")
        .innerText();

    return text.trim();

  }


  async expectAllAccountNumbersMasked():
    Promise<void> {

    const count =
      await this.accountCards.count();

    for (
      let index = 0;
      index < count;
      index++
    ) {

      const number =
        await this.accountCards
          .nth(index)
          .locator(".muted")
          .innerText();

      expect(
        number.trim()
      ).toMatch(
        /^\*{4}\s\d{4}$/
      );

    }

  }

}
