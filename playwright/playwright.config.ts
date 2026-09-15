import {
  defineConfig,
  devices
} from "@playwright/test";

import {
  existsSync,
  readFileSync
} from "node:fs";

import {
  resolve
} from "node:path";


function loadLocalEnvironment(): void {

  const envPath =
    resolve(
      __dirname,
      "../.env.local"
    );


  if (!existsSync(envPath)) {

    return;

  }


  const lines =
    readFileSync(
      envPath,
      "utf8"
    ).split(/\r?\n/);


  for (const line of lines) {

    const trimmed =
      line.trim();


    if (
      !trimmed ||
      trimmed.startsWith("#")
    ) {

      continue;

    }


    const separator =
      trimmed.indexOf("=");


    if (separator === -1) {

      continue;

    }


    const key =
      trimmed
        .slice(
          0,
          separator
        )
        .trim();


    let value =
      trimmed
        .slice(
          separator + 1
        )
        .trim();


    if (
      (
        value.startsWith('"') &&
        value.endsWith('"')
      ) ||
      (
        value.startsWith("'") &&
        value.endsWith("'")
      )
    ) {

      value =
        value.slice(
          1,
          -1
        );

    }


    if (!process.env[key]) {

      process.env[key] =
        value;

    }

  }

}


loadLocalEnvironment();


const baseURL =
  process.env.BASE_URL ??
  "https://novabank-qa-proxy.onrender.com";


const bypassSecret =
  process.env.VERCEL_AUTOMATION_BYPASS_SECRET;


if (!bypassSecret) {

  throw new Error(
    [
      "",
      "VERCEL_AUTOMATION_BYPASS_SECRET is required.",
      "",
      "Add it to:",
      "C:\\Projects\\qa-banking-system\\.env.local",
      ""
    ].join("\n")
  );

}


export default defineConfig({

  testDir:
    "./tests",

  timeout:
    60_000,

  fullyParallel:
    true,

  forbidOnly:
    !!process.env.CI,

  retries:
    process.env.CI
      ? 2
      : 0,

  /*
   * Normal local parallelism.
   */
  workers:
    process.env.CI
      ? 1
      : 4,

  reporter: [

    [
      "list"
    ],

    [
      "html",
      {
        outputFolder:
          "playwright-report",

        open:
          "never"
      }
    ]

  ],

  use: {

    baseURL,

    /*
     * Official Vercel Protection Bypass
     * for automated testing.
     */
    extraHTTPHeaders: {

      "x-vercel-protection-bypass":
        bypassSecret,

      "x-vercel-set-bypass-cookie":
        "true"

    },

    trace:
      "retain-on-failure",

    screenshot:
      "only-on-failure",

    video:
      "retain-on-failure",

    actionTimeout:
      20_000,

    navigationTimeout:
      30_000

  },

  expect: {

    timeout:
      15_000

  },

  projects: [

    {

      name:
        "chromium",

      use: {
        ...devices[
          "Desktop Chrome"
        ]
      }

    },

    {

      name:
        "firefox",

      use: {
        ...devices[
          "Desktop Firefox"
        ]
      }

    },

    {

      name:
        "webkit",

      use: {
        ...devices[
          "Desktop Safari"
        ]
      }

    }

  ]

});

