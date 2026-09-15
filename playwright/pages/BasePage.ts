import { Page, Locator, expect } from "@playwright/test";

export abstract class BasePage {
  protected readonly page: Page;

  constructor(page: Page) {
    this.page = page;
  }

  async navigate(path: string = "/"): Promise<void> {
    await this.page.goto(path);
    await this.waitForPageReady();
  }

  async waitForPageReady(): Promise<void> {
    await this.page.waitForLoadState("domcontentloaded");
  }

  async getPageTitle(): Promise<string> {
    return await this.page.title();
  }

  async getCurrentUrl(): Promise<string> {
    return this.page.url();
  }

  async click(locator: Locator): Promise<void> {
    await locator.click();
  }

  async fill(locator: Locator, value: string): Promise<void> {
    await locator.fill(value);
  }

  async clear(locator: Locator): Promise<void> {
    await locator.clear();
  }

  async expectVisible(locator: Locator): Promise<void> {
    await expect(locator).toBeVisible();
  }

  async expectHidden(locator: Locator): Promise<void> {
    await expect(locator).toBeHidden();
  }

  async expectText(locator: Locator, text: string | RegExp): Promise<void> {
    await expect(locator).toContainText(text);
  }
}
