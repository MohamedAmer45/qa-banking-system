import {
  expect,
  Locator,
  Page
} from "@playwright/test";

import { BasePage } from "./BasePage";


export type AdminSummary = {
  [key: string]: number;
};


export class AdminPage extends BasePage {

  readonly navigationButton:
    Locator;

  readonly section:
    Locator;

  readonly heading:
    Locator;

  readonly metrics:
    Locator;


  constructor(page: Page) {

    super(page);

    this.navigationButton =
      page.locator("#adminNav");

    this.section =
      page.locator("#admin");

    this.heading =
      this.section.getByRole(
        "heading",
        {
          name: "Admin console"
        }
      );

    this.metrics =
      page.locator(
        "#adminData .card"
      );
  }


  async open():
    Promise<void> {

    await expect(
      this.navigationButton
    ).toBeVisible();

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
      this.metrics.first()
    ).toBeVisible();
  }


  async expectNavigationVisible():
    Promise<void> {

    await expect(
      this.navigationButton
    ).toBeVisible();
  }


  async expectNavigationHidden():
    Promise<void> {

    await expect(
      this.navigationButton
    ).toBeHidden();
  }


  async getSummaryFromApi():
    Promise<AdminSummary> {

    return await this.page.evaluate(
      async () => {

        const token =
          sessionStorage.getItem(
            "nb_token"
          );

        if (!token) {

          throw new Error(
            "nb_token is missing."
          );

        }

        const response =
          await fetch(
            "/api/admin/summary",
            {
              headers: {
                Authorization:
                  `Bearer ${token}`
              }
            }
          );

        if (!response.ok) {

          throw new Error(
            `Admin summary request failed with ${response.status}.`
          );

        }

        return await response.json();

      }
    );
  }


  async expectMetricCount(
    expectedCount: number
  ): Promise<void> {

    await expect(
      this.metrics
    ).toHaveCount(
      expectedCount
    );
  }


  async getRenderedMetrics():
    Promise<Array<{
      label: string;
      value: string;
    }>> {

    const count =
      await this.metrics.count();

    const results: Array<{
      label: string;
      value: string;
    }> = [];

    for (
      let index = 0;
      index < count;
      index++
    ) {

      const card =
        this.metrics.nth(index);

      const label =
        (
          await card
            .locator(".muted")
            .innerText()
        ).trim();

      const value =
        (
          await card
            .locator(".metric")
            .innerText()
        ).trim();

      results.push({
        label,
        value
      });
    }

    return results;
  }


  async expectRenderedSummaryMatchesApi():
    Promise<void> {

    const summary =
      await this.getSummaryFromApi();

    const rendered =
      await this.getRenderedMetrics();

    expect(
      rendered.length
    ).toBe(
      Object.keys(summary).length
    );

    for (
      const [key, value]
      of Object.entries(summary)
    ) {

      const expectedLabel =
        key.replaceAll(
          "_",
          " "
        );

      const matchingMetric =
        rendered.find(
          metric =>
            metric.label ===
            expectedLabel
        );

      expect(
        matchingMetric,
        `Missing admin metric: ${expectedLabel}`
      ).toBeTruthy();

      const displayedNumber =
        Number(
          matchingMetric!
            .value
            .replaceAll(",", "")
        );

      expect(
        displayedNumber
      ).toBe(
        Number(value)
      );
    }
  }


  async expectAllMetricsNonEmpty():
    Promise<void> {

    const rendered =
      await this.getRenderedMetrics();

    expect(
      rendered.length
    ).toBeGreaterThan(0);

    for (
      const metric of rendered
    ) {

      expect(
        metric.label
      ).not.toBe("");

      expect(
        metric.value
      ).not.toBe("");

      expect(
        Number.isNaN(
          Number(
            metric.value
              .replaceAll(",", "")
          )
        )
      ).toBeFalsy();
    }
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
