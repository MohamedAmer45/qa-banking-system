import { test, expect } from "../../fixtures/testFixtures";

test.describe("NovaBank - Application Smoke Tests", () => {

  test("banking application loads successfully", async ({ homePage }) => {

    await homePage.open();

    await homePage.expectLoaded();

    const title = await homePage.getPageTitle();

    expect(title).toBeTruthy();

  });

});
