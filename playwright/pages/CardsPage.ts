import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import { BasePage } from "./BasePage";


export class CardsPage extends BasePage {

  readonly navigationButton: Locator;

  readonly section: Locator;

  readonly heading: Locator;

  readonly cards: Locator;

  readonly toast: Locator;


  constructor(page: Page) {

    super(page);

    this.navigationButton =
      page.locator(
        '#nav button[data-s="cards"]'
      );

    this.section =
      page.locator("#cards");

    this.heading =
      this.section.getByRole(
        "heading",
        {
          name: "Cards"
        }
      );

    this.cards =
      page.locator(
        "#cardCards .card"
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

    const count =
      await this.cards.count();

    expect(
      count
    ).toBeGreaterThan(0);

    await expect(
      this.cards.first()
    ).toBeVisible();

  }


  async expectCardCount(
    count: number
  ): Promise<void> {

    await expect(
      this.cards
    ).toHaveCount(count);

  }


  private card(
    cardType: string
  ): Locator {

    return this.cards.filter({
      has: this.page.getByRole(
        "heading",
        {
          name: cardType,
          exact: true
        }
      )
    });

  }


  async expectCard(
    cardType: string,
    maskedNumber: string,
    status: string
  ): Promise<void> {

    const card =
      this.card(cardType);

    await expect(
      card
    ).toBeVisible();

    await expect(
      card.getByRole(
        "heading",
        {
          name: cardType,
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
      card.locator(".tag")
    ).toHaveText(
      status
    );

  }


  async expectAllNumbersMasked():
    Promise<void> {

    const count =
      await this.cards.count();

    for (
      let index = 0;
      index < count;
      index++
    ) {

      const number =
        await this.cards
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


  async expectToggleButton(
    cardType: string,
    expectedText: string
  ): Promise<void> {

    const card =
      this.card(cardType);

    await expect(
      card.locator("button.toggle")
    ).toHaveText(
      expectedText
    );

  }


  async getCardStatus(
    cardType: string
  ): Promise<string> {

    return (
      await this
        .card(cardType)
        .locator(".tag")
        .innerText()
    ).trim();

  }


  async toggleCard(
    cardType: string,
    expectedStatus: string
  ): Promise<void> {

    const card =
      this.card(cardType);

    const button =
      card.locator(
        "button.toggle"
      );

    const responsePromise =
      this.page.waitForResponse(
        response =>
          response.url().includes(
            "/api/cards"
          ) &&
          response.request().method() ===
            "PATCH"
      );

    await button.click();

    const response =
      await responsePromise;

    expect(
      response.ok()
    ).toBeTruthy();

    await expect(
      this.card(cardType).locator(".tag")
    ).toHaveText(
      expectedStatus
    );

    await expect(
      this.toast
    ).toHaveText(
      `Card ${expectedStatus}.`
    );

  }


  async getStoredCards():
    Promise<Array<{
      id: string;
      type: string;
      number: string;
      status: string;
    }>> {

    return await this.page.evaluate(() => {

      const raw =
        localStorage.getItem(
          "nb_state"
        );

      if (!raw) {
        return [];
      }

      const state =
        JSON.parse(raw);

      return state.cards ?? [];

    });

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
