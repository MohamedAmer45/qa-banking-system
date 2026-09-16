# NovaBank Cypress Automation Framework

End-to-end UI automation framework for the NovaBank Banking System QA project.

## Technology Stack

- Cypress
- TypeScript
- Node.js
- Chrome
- Microsoft Edge
- Firefox
- Page Object Model
- Cypress custom commands
- Mochawesome reporting
- JUnit reporting

## Application Under Test

NovaBank QA Banking Sandbox

Application URL:

https://novabank-qa-proxy.onrender.com

NovaBank provides deterministic customer and administrator demo sessions designed for automated software testing.

## Framework Structure

cypress/
  cypress/
    e2e/
      accounts/
      admin/
      auth/
      bills/
      cards/
      dashboard/
      framework/
      loans/
      notifications/
      profile/
      transactions/
      transfers/
      smoke.cy.ts
    pages/
    support/
      commands.ts
      e2e.ts
    screenshots/
    videos/
    results/
  cypress.config.ts
  reporter-config.json
  tsconfig.json
  package.json
  README.md

## Covered Modules

The Cypress automation suite covers:

- Smoke testing
- Authentication and demo sessions
- Customer dashboard
- Accounts
- Transfers
- Transactions
- Bills
- Cards
- Loans
- Notifications
- Profile
- Administrator console
- Role-based access
- Customer/admin session behavior
- State isolation
- Cross-browser execution

## Framework Design

The framework follows the Page Object Model.

Reusable application behavior is implemented through Cypress custom commands.

Main reusable commands:

    cy.loginAsCustomer();
    cy.loginAsAdmin();
    cy.openModule("accounts");
    cy.resetNovaBankState();

This reduces duplicated selectors and setup logic across test modules.

## State Isolation

NovaBank stores application state in localStorage and authentication data in sessionStorage.

The framework resets:

    localStorage.nb_state
    sessionStorage.nb_token
    sessionStorage.nb_user

before clean customer and administrator sessions.

This prevents state-changing tests involving transfers, bill payments, card status changes, and loan applications from affecting other tests.

## Install Dependencies

Run:

    npm ci

## Open Cypress

Run:

    npm run cy:open

## Smoke Test

Run:

    npm run cy:smoke

## Full Chrome Regression

Run:

    npm run cy:regression

or:

    npm run cy:chrome

## Cross-Browser Testing

Chrome:

    npm run cy:chrome

Edge:

    npm run cy:edge

Firefox:

    npm run cy:firefox

The requested browser must be installed on the execution machine.

## TypeScript Validation

Run:

    npm run typecheck

## Complete Framework Validation

Runs TypeScript validation followed by the full Chrome regression suite:

    npm run validate

## Reporting

Generate the complete regression suite and reports:

    npm run report:all

Generated artifacts are stored under:

    cypress/results/
    cypress/screenshots/
    cypress/videos/

Mochawesome HTML report:

    cypress/results/html/index.html

Combined JUnit report:

    cypress/results/junit/combined.xml

Generated reports, screenshots, videos, downloads, and logs are excluded from Git.

## Failure Evidence

Cypress automatically captures screenshots when tests fail.

Video recording is enabled during command-line regression execution.

These artifacts are uploaded by the Cypress GitHub Actions workflow after every run, including failed runs when files are available.

## CI/CD

The implemented `.github/workflows/cypress.yml` workflow:

- Supports manual execution.
- Runs when Cypress files or the workflow change on pushes and pull requests targeting `main`.
- Uses Node.js 24 and installs the locked dependencies with `npm ci`.
- Verifies that the deployed QA environment is available.
- Runs `npm run validate` for TypeScript validation and the full Chrome regression suite.
- Uploads reports, screenshots, and videos with 14-day retention.
- Cancels superseded runs for the same Git reference.

## Main Validation Command

For normal framework validation run:

    npm run validate

This performs:

1. TypeScript compilation validation.
2. Full Cypress Chrome regression.
