import {
  test as base,
  expect
} from "@playwright/test";

import { AccountsPage } from "../pages/AccountsPage";
import { AuthPage } from "../pages/AuthPage";
import { BillsPage } from "../pages/BillsPage";
import { CardsPage } from "../pages/CardsPage";
import { DashboardPage } from "../pages/DashboardPage";
import { HomePage } from "../pages/HomePage";
import { LoansPage } from "../pages/LoansPage";
import { NotificationsPage } from "../pages/NotificationsPage";
import { ProfilePage } from "../pages/ProfilePage";
import { TransactionsPage } from "../pages/TransactionsPage";
import { TransfersPage } from "../pages/TransfersPage";


type BankingFixtures = {

  homePage:
    HomePage;

  authPage:
    AuthPage;

  dashboardPage:
    DashboardPage;

  accountsPage:
    AccountsPage;

  transfersPage:
    TransfersPage;

  transactionsPage:
    TransactionsPage;

  billsPage:
    BillsPage;

  cardsPage:
    CardsPage;

  loansPage:
    LoansPage;

  notificationsPage:
    NotificationsPage;

  profilePage:
    ProfilePage;

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

      },


    transfersPage:
      async ({ page }, use) => {

        await use(
          new TransfersPage(page)
        );

      },


    transactionsPage:
      async ({ page }, use) => {

        await use(
          new TransactionsPage(page)
        );

      },


    billsPage:
      async ({ page }, use) => {

        await use(
          new BillsPage(page)
        );

      },


    cardsPage:
      async ({ page }, use) => {

        await use(
          new CardsPage(page)
        );

      },


    loansPage:
      async ({ page }, use) => {

        await use(
          new LoansPage(page)
        );

      },


    notificationsPage:
      async ({ page }, use) => {

        await use(
          new NotificationsPage(page)
        );

      },


    profilePage:
      async ({ page }, use) => {

        await use(
          new ProfilePage(page)
        );

      }

  });


export { expect };
