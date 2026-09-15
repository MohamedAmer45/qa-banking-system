import { test, expect } from "@playwright/test";

test.describe("NovaBank - Application Smoke Tests", () => {

  test("banking application loads successfully", async ({ page }) => {

    const response = await page.goto("/");

    expect(response).not.toBeNull();
    expect(response?.ok()).toBeTruthy();

    await expect(page.locator("body")).toBeVisible();

    await expect(page).toHaveURL(
      /novabank-banking-system\.vercel\.app/
    );
  });

});
