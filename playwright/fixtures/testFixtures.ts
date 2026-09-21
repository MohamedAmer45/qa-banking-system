import { test as base, expect } from "@playwright/test";

import { AuthPage } from "../pages/AuthPage";
import { DashboardPage } from "../pages/DashboardPage";
import { AccountsPage } from "../pages/AccountsPage";
import { TransfersPage } from "../pages/TransfersPage";
import { BeneficiariesPage } from "../pages/BeneficiariesPage";
import { AdminPage } from "../pages/AdminPage";

type BankingFixtures = {
  authPage: AuthPage;
  dashboardPage: DashboardPage;
  accountsPage: AccountsPage;
  transfersPage: TransfersPage;
  beneficiariesPage: BeneficiariesPage;
  adminPage: AdminPage;

  /** An authenticated customer session, already on the overview. */
  customerSession: DashboardPage;
};

export const test = base.extend<BankingFixtures>({
  authPage: async ({ page }, use) => { await use(new AuthPage(page)); },
  dashboardPage: async ({ page }, use) => { await use(new DashboardPage(page)); },
  accountsPage: async ({ page }, use) => { await use(new AccountsPage(page)); },
  transfersPage: async ({ page }, use) => { await use(new TransfersPage(page)); },
  beneficiariesPage: async ({ page }, use) => { await use(new BeneficiariesPage(page)); },
  adminPage: async ({ page }, use) => { await use(new AdminPage(page)); },

  customerSession: async ({ authPage, dashboardPage }, use) => {
    await authPage.open();
    await authPage.loginAsCustomer();
    await use(dashboardPage);
  }
});

export { expect };
