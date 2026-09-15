import { test as base, expect } from "@playwright/test";
import { HomePage } from "../pages/HomePage";

type BankingFixtures = {
  homePage: HomePage;
};

export const test = base.extend<BankingFixtures>({
  homePage: async ({ page }, use) => {
    const homePage = new HomePage(page);

    await use(homePage);
  },
});

export { expect };
