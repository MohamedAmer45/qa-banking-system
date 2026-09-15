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

    await expect(this.page).toHaveURL(
      /novabank-banking-system\.vercel\.app/
    );
  }
}
