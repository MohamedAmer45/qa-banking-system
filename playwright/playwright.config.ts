import { defineConfig, devices } from "@playwright/test";

/*
 * Defaults to the deployed environment so a fresh clone runs with no local
 * setup. CI sets BASE_URL to the app it starts inside the runner; run
 * locally the same way:
 *   BASE_URL=http://localhost:3000 npx playwright test
 *
 * The deployed environment has one shared database, so tests that move money
 * mutate it. Balance assertions stay relative for exactly this reason
 * (LIM-005 in docs/known-issues-and-limitations.md).
 */
const baseURL =
  process.env.BASE_URL ??
  "https://novabank-banking-system.vercel.app";

export default defineConfig({
  testDir: "./tests",

  fullyParallel: true,

  forbidOnly: !!process.env.CI,

  retries: process.env.CI ? 2 : 0,

  workers: process.env.CI ? 1 : 4,

  timeout: 60_000,

  expect: {
    timeout: 15_000,
  },

  reporter: [
    ["list"],
    ["html", { open: "never" }],
  ],

  use: {
    baseURL,

    trace: "retain-on-failure",

    screenshot: "only-on-failure",

    video: "retain-on-failure",

    actionTimeout: 20_000,

    navigationTimeout: 30_000,
  },

  projects: [
    {
      name: "chromium",
      use: {
        ...devices["Desktop Chrome"],
      },
    },

    {
      name: "firefox",
      use: {
        ...devices["Desktop Firefox"],
      },
    },

    {
      name: "webkit",
      use: {
        ...devices["Desktop Safari"],
      },
    },
  ],
});
