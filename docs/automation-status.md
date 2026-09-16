# NovaBank Automation Status

Last synchronized: 2026-09-16

| Framework / Area | Current Status |
|---|---|
| Selenium Java | Synchronized with the current UI; GitHub Actions regression passing |
| Cypress TypeScript | Synchronized with the current UI; GitHub Actions regression passing |
| Playwright TypeScript | Synchronized with the current UI; Chromium, Firefox, and WebKit GitHub Actions jobs passing |
| Cucumber JVM | Synchronized with the current UI; GitHub Actions BDD regression passing |
| GitHub Actions | Four UI/BDD workflows implemented and passing |
| Postman | Next phase; collection and environment not yet implemented |
| REST Assured | Planned after the Postman collection |
| Jest | Planned for backend and business-logic validation |
| JMeter | Planned for performance testing |
| Database testing | Blocked because persistent database-backed banking state is unavailable |
| Jenkins | Planned after the remaining automation phases |

## Current GitHub Actions Coverage

| Workflow | Validation | Artifacts |
|---|---|---|
| Selenium Tests | Java 21, Maven, TestNG, headless Chrome regression | Surefire and Allure results |
| Cypress Tests | TypeScript validation and Chrome regression | Mochawesome/JUnit results, screenshots, and videos |
| Playwright Tests | Chromium, Firefox, and WebKit regression | HTML report, traces, screenshots, videos, and test results |
| Cucumber BDD Tests | Java 21, Maven, TestNG, headless Chrome BDD regression | HTML, JSON, JUnit, Surefire, and Allure results |

All four workflows support manual execution, relevant push and pull-request triggers, concurrency cancellation, QA-environment availability checks, and 14-day artifact retention.

## Current Automated Functional Coverage

- Authentication and demo sessions
- Dashboard
- Accounts
- Transactions
- Transfers
- Bills
- Cards
- Loans
- Notifications
- Profile
- Admin console
- Role authorization
- Session security

## Blocked Functional Coverage

- MFA
- Beneficiary management
- Account creation
- Extended account controls
- Dedicated statements
- Persistent SQL/database verification

Blocked functionality remains documented and must not be represented as passing coverage.

## Next Phase

Build current-API coverage with Postman and Newman first, followed by a REST Assured Java regression framework and their GitHub Actions workflows.
