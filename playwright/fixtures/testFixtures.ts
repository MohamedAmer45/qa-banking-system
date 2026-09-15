import {
  test as base,
  expect
} from "@playwright/test";

import { AccountsPage } from "../pages/AccountsPage";
import { AuthPage } from "../pages/AuthPage";
import { DashboardPage } from "../pages/DashboardPage";
import { HomePage } from "../pages/HomePage";


type BankingFixtures = {

  homePage: HomePage;

  authPage: AuthPage;

  dashboardPage: DashboardPage;

  accountsPage: AccountsPage;

};


export const test =
  base.extend<BankingFixtures>({

    homePage:
      async ({ page }, use) => {

        await use(
          new HomePage(page)
        );

      },


    authPage:
      async ({ page }, use) => {

        await use(
          new AuthPage(page)
        );

      },


    dashboardPage:
      async ({ page }, use) => {

        await use(
          new DashboardPage(page)
        );

      },


    accountsPage:
      async ({ page }, use) => {

        await use(
          new AccountsPage(page)
        );

      }

  });


export { expect };
