import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import { BasePage } from "./BasePage";


export type SessionUser = {

  name:
    string;

  email:
    string;

  role:
    string;

};


export class ProfilePage extends BasePage {

  readonly navigationButton:
    Locator;

  readonly section:
    Locator;

  readonly heading:
    Locator;

  readonly profileCard:
    Locator;

  readonly name:
    Locator;

  readonly email:
    Locator;

  readonly role:
    Locator;


  constructor(page: Page) {

    super(page);

    this.navigationButton =
      page.locator(
        '#nav button[data-s="profile"]'
      );

    this.section =
      page.locator("#profile");

    this.heading =
      this.section.getByRole(
        "heading",
        {
          name: "Profile"
        }
      );

    this.profileCard =
      page.locator("#profileCard");

    this.name =
      this.profileCard.locator("h2");

    this.email =
      this.profileCard.locator(
        "p:not(.muted)"
      );

    this.role =
      this.profileCard.locator(
        "p.muted"
      );
  }


  async open():
    Promise<void> {

    await this.navigationButton
      .click();

    await this.expectLoaded();
  }


  async expectLoaded():
    Promise<void> {

    await expect(
      this.section
    ).toBeVisible();

    await expect(
      this.heading
    ).toBeVisible();

    await expect(
      this.profileCard
    ).toBeVisible();
  }


  async getSessionUser():
    Promise<SessionUser> {

    return await this.page.evaluate(
      () => {

        const raw =
          sessionStorage.getItem(
            "nb_user"
          );

        if (!raw) {

          throw new Error(
            "nb_user is missing from sessionStorage."
          );

        }

        return JSON.parse(raw);

      }
    );
  }


  async getDisplayedName():
    Promise<string> {

    return (
      await this.name.innerText()
    ).trim();
  }


  async getDisplayedEmail():
    Promise<string> {

    return (
      await this.email.innerText()
    ).trim();
  }


  async getDisplayedRole():
    Promise<string> {

    const text =
      (
        await this.role.innerText()
      ).trim();

    return text.replace(
      /^Role:\s*/,
      ""
    );
  }


  async expectProfileMatchesSession():
    Promise<void> {

    const user =
      await this.getSessionUser();

    await expect(
      this.name
    ).toHaveText(
      user.name
    );

    await expect(
      this.email
    ).toHaveText(
      user.email
    );

    await expect(
      this.role
    ).toHaveText(
      `Role: ${user.role}`
    );
  }


  async expectRole(
    expectedRole: string
  ): Promise<void> {

    await expect(
      this.role
    ).toHaveText(
      `Role: ${expectedRole}`
    );
  }


  async expectValidEmail():
    Promise<void> {

    const value =
      await this.getDisplayedEmail();

    expect(
      value
    ).toMatch(
      /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    );
  }


  async expectIdentityFieldsNonEmpty():
    Promise<void> {

    expect(
      await this.getDisplayedName()
    ).not.toBe("");

    expect(
      await this.getDisplayedEmail()
    ).not.toBe("");

    expect(
      await this.getDisplayedRole()
    ).not.toBe("");
  }


  async expectReadOnly():
    Promise<void> {

    await expect(
      this.profileCard.locator(
        "input"
      )
    ).toHaveCount(0);

    await expect(
      this.profileCard.locator(
        "textarea"
      )
    ).toHaveCount(0);

    await expect(
      this.profileCard.locator(
        "select"
      )
    ).toHaveCount(0);

    await expect(
      this.profileCard.locator(
        "button"
      )
    ).toHaveCount(0);
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
