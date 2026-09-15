import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import {
  BasePage
} from "./BasePage";

import {
  DemoRole
} from "../test-data/types";


export class AuthPage extends BasePage {

  readonly loginContainer: Locator;
  readonly applicationContainer: Locator;

  readonly customerLoginButton: Locator;
  readonly adminLoginButton: Locator;

  readonly currentUser: Locator;
  readonly logoutButton: Locator;

  readonly dashboardSection: Locator;

  readonly adminNavigationButton: Locator;
  readonly adminSection: Locator;


  constructor(page: Page) {

    super(page);

    this.loginContainer =
      page.locator("#login");

    this.applicationContainer =
      page.locator("#app");

    this.customerLoginButton =
      page.locator(
        'button.enter[data-role="customer"]'
      );

    this.adminLoginButton =
      page.locator(
        'button.enter[data-role="admin"]'
      );

    this.currentUser =
      page.locator("#who");

    this.logoutButton =
      page.locator("#logout");

    this.dashboardSection =
      page.locator("#dashboard");

    this.adminNavigationButton =
      page.locator("#adminNav");

    this.adminSection =
      page.locator("#admin");

  }


  async open(): Promise<void> {

    await this.navigate("/");

  }


  async expectLoginScreenVisible():
    Promise<void> {

    await expect(
      this.loginContainer
    ).toBeVisible();

    await expect(
      this.customerLoginButton
    ).toBeVisible();

    await expect(
      this.adminLoginButton
    ).toBeVisible();

    await expect(
      this.applicationContainer
    ).toBeHidden();

  }


  async loginAs(
    role: DemoRole
  ): Promise<void> {

    const button =
      role === "customer"
        ? this.customerLoginButton
        : this.adminLoginButton;


    await expect(
      this.loginContainer
    ).toBeVisible();


    const responsePromise =
      this.page.waitForResponse(
        response =>
          response.url().includes(
            "/api/session"
          ) &&
          response.request().method() ===
            "POST"
      );


    await button.click();


    const response =
      await responsePromise;


    expect(
      response.status()
    ).toBe(200);


    await expect(
      this.applicationContainer
    ).toBeVisible();


    await expect(
      this.loginContainer
    ).toBeHidden();


    await expect(
      this.dashboardSection
    ).toBeVisible();


    await expect(
      this.currentUser
    ).toContainText(
      new RegExp(
        role,
        "i"
      )
    );

  }


  async loginAsCustomer():
    Promise<void> {

    await this.loginAs(
      "customer"
    );

  }


  async loginAsAdmin():
    Promise<void> {

    await this.loginAs(
      "admin"
    );

  }


  async logout(): Promise<void> {

    const responsePromise =
      this.page.waitForResponse(
        response =>
          response.url().includes(
            "/api/logout"
          ) &&
          response.request().method() ===
            "POST"
      );


    await this.logoutButton.click();


    const response =
      await responsePromise;


    expect(
      response.status()
    ).toBe(204);


    await expect(
      this.loginContainer
    ).toBeVisible();


    await expect(
      this.applicationContainer
    ).toBeHidden();

  }


  async expectCustomerAccess():
    Promise<void> {

    await expect(
      this.currentUser
    ).toContainText(
      /customer/i
    );

    await expect(
      this.adminNavigationButton
    ).toBeHidden();

    await expect(
      this.adminSection
    ).toBeHidden();

  }


  async expectAdminAccess():
    Promise<void> {

    await expect(
      this.currentUser
    ).toContainText(
      /admin/i
    );

    await expect(
      this.adminNavigationButton
    ).toBeVisible();

  }


  async openAdminConsole():
    Promise<void> {

    await this.adminNavigationButton.click();


    await expect(
      this.adminSection
    ).toBeVisible();


    await expect(
      this.adminSection.getByRole(
        "heading",
        {
          name:
            "Admin console"
        }
      )
    ).toBeVisible();

  }


  async reloadAndExpectAuthenticated(
    role: DemoRole
  ): Promise<void> {

    await this.page.reload({
      waitUntil:
        "domcontentloaded"
    });


    await expect(
      this.loginContainer
    ).toBeHidden();


    await expect(
      this.applicationContainer
    ).toBeVisible();


    await expect(
      this.currentUser
    ).toContainText(
      new RegExp(
        role,
        "i"
      )
    );

  }


  async expectSessionStorageCreated():
    Promise<void> {

    const session =
      await this.page.evaluate(
        () => ({
          token:
            sessionStorage.getItem(
              "nb_token"
            ),

          user:
            sessionStorage.getItem(
              "nb_user"
            )
        })
      );


    expect(
      session.token
    ).toBeTruthy();


    expect(
      session.user
    ).toBeTruthy();

  }


  async expectSessionStorageCleared():
    Promise<void> {

    const session =
      await this.page.evaluate(
        () => ({
          token:
            sessionStorage.getItem(
              "nb_token"
            ),

          user:
            sessionStorage.getItem(
              "nb_user"
            )
        })
      );


    expect(
      session.token
    ).toBeNull();


    expect(
      session.user
    ).toBeNull();

  }

}
