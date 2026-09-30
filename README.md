# QA Banking System Testing Project

[![Selenium](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/selenium.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/selenium.yml)
[![Cypress](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/cypress.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/cypress.yml)
[![Playwright](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/playwright.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/playwright.yml)
[![Cucumber](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/cucumber.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/cucumber.yml)
[![API](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/api.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/api.yml)
[![Database](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/database.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/database.yml)
[![Performance](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/jmeter.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/jmeter.yml)
[![Security](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/zap.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/zap.yml)

A full QA engineering project against a real banking application: requirements,
traceability, manual test design, and nine automated suites covering UI, API,
database, BDD, unit, performance, accessibility and security.

The application under test is
[NovaBank](https://github.com/MohamedAmer45/novabank-banking-system) — a separate
repository, PostgreSQL-backed, deployed at
[`novabank-banking-system.vercel.app`](https://novabank-banking-system.vercel.app).

---

## What the testing found

The point of a test suite is the defects it catches, so those come first.

| Defect | Found by | What it was |
|---|---|---|
| `BUG-DASH-001` | Writing `DASH` coverage | Three of the dashboard's seven requirements had **never been implemented**. The module looked "covered incidentally" because every UI suite crossed that page on the way somewhere else — and an absent panel is invisible to a suite that was never told to look for it. |
| `BUG-DB-001` | Database suite | FX and interest rates stored as binary floats, drifting two minor units on a 10,000.00 conversion. Found by reading `information_schema` rather than driving the application, because the endpoint returned a rounded figure that looked correct. |
| `BUG-A11Y-001` | First axe-core scan | 73 form controls exposed no accessible name — one markup pattern repeated across every form in the application. Plus dialogs that announced nothing, trapped no focus and returned none. |
| `BUG-UI-001` | Cypress | A `TypeError` on every page load. Cypress caught it on its first `cy.visit` because it fails on any uncaught exception; Playwright had been driving the same screens throughout without noticing. |
| `BUG-API-001` | REST Assured | The API documented `413` for an oversized body but dropped the connection instead. `curl` had always shown the 413; a second HTTP client did not. |
| `BUG-UI-002` | Selenium | The back-office sidebar was not role-filtered, offering four of five staff roles modules the server refuses. |

Those are six of the **nine recorded defects**, all of which are closed with
regression cover in the suite that found them. The full set, each with its
investigation, is in [`docs/defects/`](docs/defects/).

Two further defects were in the *test tooling* rather than the application, and
were found by running the Jenkins pipeline instead of reading it: a parameter
that arrives empty on a job's first build only, and a missing Newman environment
file. Neither is visible to a linter, and a third — a spread that throws once a
results file gets large enough — only appeared when the stress load was raised
far enough to produce one.

Different again: **five gaps in the tests themselves**, found by mutation
testing rather than by any failing assertion. Stryker changed the application
source underneath the 134 green unit tests — `<=` to `<`, `length - 4` to
`+ 4`, a `.sort()` deleted — and five of those faults went unnoticed. The most
instructive was a deliberate choice that backfired: assertions written to check
the *property* ("only the last four digits are legible") rather than the exact
output turned out to be robust to cosmetic change and blind to magnitude.
Eighteen mask characters satisfy that property as well as six do. All five are
closed, the suite is 141, and the caveats are in
[`MUTATION.md`](https://github.com/MohamedAmer45/novabank-banking-system/blob/main/MUTATION.md).

### Things that were measured, not assumed

- **Concurrency.** 30 threads debiting one account simultaneously: 7 committed,
  23 refused, and the balance fell by exactly `7 × (2,000,000 + 2,000)`. The
  ledger reconciled to the minor unit under contention.
- **Load.** p95 of 2 ms against a 100 ms budget derived from CI baselines.
- **Stress.** 293,156 samples at 400 threads, p95 climbing 13 → 27 → 41 → 51 ms
  across the run — degradation without failure.
- **Security.** The application sent no security headers at all; only HSTS, and
  that came from the host rather than the code.

---

## Status

| Suite | Tests | Browsers |
|---|---:|---|
| Playwright | 67 | Chromium, Firefox, WebKit |
| Cypress | 26 | Chrome / Electron |
| Selenium | 27 | Chrome |
| Cucumber | 24 scenarios | Chrome |
| REST Assured | 130 | n/a |
| Database (JDBC + TestNG) | 59 | n/a |
| Postman / Newman | 103 requests, 440 assertions | n/a |
| Jest (unit, in the app repo) | 141 | n/a |
| JMeter | 5 load shapes | n/a |

Accessibility is 30 of Playwright's 67, not a tenth suite. All **65 endpoints**
are exercised by at least one suite, and all **210 requirements across 19
modules** are covered — see the
[traceability matrix](manual-testing/traceability/requirements-traceability-matrix.md).

The Jenkins pipeline runs the whole regression and has been verified end to end:
build green in 19.9 minutes, 763 tests published.

### What is deliberately not finished

- **Three accessibility cases are manual-only** — screen reader coherence,
  reading order, 200% zoom. Automated checks find a minority of WCAG issues and
  none of the judgement-based ones.
- **`style-src` still allows `'unsafe-inline'`** for inline style attributes.
  Outside `WEBSEC-001`, which covers scripts, objects and document base.
- **Deployment is manual.** `vercel git connect` is blocked on a GitHub login
  connection only the account owner can authorize.

---

## Running the suites

Every suite targets the deployed application by default, so a fresh clone runs
without installing PostgreSQL, seeding a database or starting a server.

| Suite | Command | From |
|---|---|---|
| Playwright | `npm ci && npx playwright install --with-deps && npm test` | `playwright/` |
| Cypress | `npm ci && npm run validate` | `cypress/` |
| Selenium | `mvn clean test` | `selenium/` |
| Cucumber | `mvn clean test` | `cucumber/` |
| REST Assured | `mvn clean test` | `rest-assured/` |
| Postman / Newman | `npm ci && npm test` | `postman/` |
| Database | `DATABASE_URL=… mvn clean test` | `database-testing/` |
| Jest (unit) | `npm test` | the application repository |
| Mutation (Stryker) | `npm run test:mutation` | the application repository |
| JMeter | `JMETER_HOME=… npm test` | `jmeter/` — local or CI, never the deployed app |

Two are different. The **database suite** needs a connection string rather than
a URL, and the deployed database's credentials are deliberately not committed.
**JMeter and the ZAP scan never target the deployed environment**: one exhausts
an account's balance on purpose, the other spiders the host. Both would damage
the data every other suite reads. See [`jmeter/README.md`](jmeter/README.md) and
[`docs/test-environment.md`](docs/test-environment.md).

### Against localhost instead

```bash
BASE_URL=http://localhost:3000 npm test                   # Playwright
CYPRESS_BASE_URL=http://localhost:3000 npm run validate   # Cypress
mvn clean test -Dbase.url=http://localhost:3000           # Selenium, Cucumber
mvn clean test -Dapi.base.url=http://localhost:3000       # REST Assured, Database
npm run test:local                                        # Postman / Newman
```

### Which target to trust

The deployed environment has one database and it is not reset between runs, so
suites that move money mutate shared state. Assertions are written as deltas
rather than absolutes for exactly this reason (`LIM-005`).

CI does not use it. Each workflow starts the application inside the runner
against a throwaway `postgres:16` container, so every run begins from an
identical seed. **That is the target to trust**; the deployed default is for
convenience and demonstration.

---

## How the nine suites divide the work

They are not nine copies of the same coverage. Cypress fails on uncaught page
exceptions where Playwright opts in; Selenium's explicit waits expose races that
auto-retrying assertions paper over; the database suite asserts stored state
rather than API responses. That division is what found most of the defects above.

The full matrix, and the reasoning for each tool, is in
[`docs/automation-status.md`](docs/automation-status.md).

---

## Where everything lives

| | |
|---|---|
| [`requirements/`](requirements/) | 210 requirements across 19 modules, the source of truth |
| [`manual-testing/`](manual-testing/) | Scenarios, test cases, design techniques, traceability |
| [`test-planning/`](test-planning/) | Test plan, strategy, risk analysis |
| [`docs/`](docs/) | Status, environments, known issues, API reference, defect reports |
| [`docs/project-reference.md`](docs/project-reference.md) | Goals, modules, tooling and CI detail |
| `playwright/` `cypress/` `selenium/` `cucumber/` | UI and BDD suites |
| `rest-assured/` `postman/` `database-testing/` | API and data suites |
| [`jmeter/`](jmeter/) [`security/`](security/) | Performance shapes and the ZAP gate |
| [`Jenkinsfile`](Jenkinsfile) | Full regression pipeline |

---

## Quality principle

The objective is not to maximise the number of test cases.

It is to build confidence that the system performs the correct operation,
rejects invalid ones, protects customer and financial data, maintains correct
balances, prevents duplicate transactions, handles concurrent requests
correctly, persists correct state, and remains auditable.

Critical financial functionality is validated across layers rather than through
UI success messages. The reference example is the ledger invariant: every account
balance must equal the `balance_after_minor` of that account's most recent
transaction. A UI assertion cannot catch a violation of it. A SQL assertion can.

A second principle earned the hard way in this repository: **a status document
that is wrong in the flattering direction is worse than one that is merely
stale.** `DASH` was marked "Implemented: Yes" while three of its requirements did
not exist. Several commits here exist only to correct a claim that was too
generous.
