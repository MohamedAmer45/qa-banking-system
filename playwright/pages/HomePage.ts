import { expect, Locator, Page } from "@playwright/test";
import { BasePage } from "./BasePage";

export class HomePage extends BasePage {
  readonly body: Locator;

  constructor(page: Page) {
    super(page);

    this.body = page.locator("body");
  }

  async open(): Promise<void> {
    await this.navigate("/");
  }

  async expectLoaded(): Promise<void> {
    await expect(this.body).toBeVisible();

    const expectedOrigin =
      new URL(
        process.env.BASE_URL ??
        "http://localhost:3000"
      ).origin;

    expect(
      new URL(this.page.url()).origin
    ).toBe(
      expectedOrigin
    );
  }
}

