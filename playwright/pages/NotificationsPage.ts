import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import { BasePage } from "./BasePage";


export class NotificationsPage extends BasePage {

  readonly navigationButton: Locator;

  readonly section: Locator;

  readonly heading: Locator;

  readonly notifications: Locator;


  constructor(page: Page) {

    super(page);

    this.navigationButton =
      page.locator(
        '#nav button[data-s="notifications"]'
      );

    this.section =
      page.locator("#notifications");

    this.heading =
      this.section.getByRole(
        "heading",
        {
          name: "Notifications"
        }
      );

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
  }


  async expectNotificationCount(
    count: number
  ): Promise<void> {

    await expect(
      this.notifications
    ).toHaveCount(count);
  }


  async expectNotificationAt(
    index: number,
    expectedText: string
  ): Promise<void> {

    await expect(
      this.notifications.nth(index)
    ).toHaveText(
      expectedText
    );
  }


  async expectNotificationPresent(
    expectedText: string
  ): Promise<void> {

    await expect(
      this.notifications.filter({
        hasText: expectedText
      })
    ).toHaveCount(1);
  }


  async getNotificationTexts():
    Promise<string[]> {

    return (
      await this.notifications
        .allTextContents()
    ).map(
      text => text.trim()
    );
  }


  async getStoredNotifications():
    Promise<string[]> {

    return await this.page.evaluate(
      () => {

        const raw =
          localStorage.getItem(
            "nb_state"
          );

        if (!raw) {
          return [];
        }

        const state =
          JSON.parse(raw);

        return state.notes ?? [];

      }
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
