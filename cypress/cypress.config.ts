import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    baseUrl: "https://novabank-banking-system.vercel.app",

    specPattern: "cypress/e2e/**/*.cy.ts",

    supportFile: "cypress/support/e2e.ts",

    fixturesFolder: "cypress/fixtures",

    screenshotsFolder: "cypress/screenshots",

    videosFolder: "cypress/videos",

    downloadsFolder: "cypress/downloads",

    viewportWidth: 1440,
    viewportHeight: 900,

    defaultCommandTimeout: 10000,
    requestTimeout: 10000,
    responseTimeout: 30000,
    pageLoadTimeout: 60000,

    video: true,
    screenshotOnRunFailure: true,

    retries: {
      runMode: 2,
      openMode: 0
    },

    setupNodeEvents(on, config) {
      return config;
    }
  }
});
