# Automation Status

Last synchronized: 2026-09-23

## Summary

Seven suites, all passing against the PostgreSQL build.

| Suite | Tests | Browsers |
|---|---|---|
| Playwright + TypeScript | 24 | Chromium, Firefox, WebKit |
| Cypress + TypeScript | 26 | Chrome / Electron |
| Selenium + Java + TestNG | 21 | Chrome |
| Cucumber JVM | 21 scenarios | Chrome |
| Database (JDBC + TestNG) | 59 | n/a |
| REST Assured | 107 | n/a |
| Postman / Newman | 103 requests, 440 assertions | n/a |

Every suite starts the application inside the CI runner against a `postgres:16`
service container, so runs are isolated and begin from an identical seed. No
suite depends on a hosted environment.

All 65 endpoints the application serves are exercised by at least one suite.

## How the suites divide the work

They are deliberately not seven copies of the same coverage.

| | Playwright | Cypress | Selenium | Cucumber | Database | REST Assured | Postman |
|---|---|---|---|---|---|---|---|
| End-to-end money movement | Primary | No | Yes | Yes | Yes | Some | Yes |
| Cross-browser | Yes | No | Configurable | No | — | — | — |
| Form validation | Minimal | Primary | Some | Some | — | — | — |
| Network contract | Minimal | Primary | No | No | — | Schemas | Some |
| Uncaught page exceptions | Opt-in guard | Fails by default | No | No | — | — | — |
| Authorization grid | No | Some | Some | Some | — | Primary | Some |
| Stored state | No | No | No | No | Primary | No | No |
| Chained journeys | Some | No | No | Yes | No | No | Primary |
| Requirement traceability | Test ids | Test ids | Test ids | Gherkin tags | Requirement ids | Requirement ids | Folder names |

That division has earned itself five times:

- **Cypress found `BUG-UI-001`** — a `TypeError` thrown on every page load — on
  its first `cy.visit`, because it fails a test on any uncaught application
  exception. Playwright had been driving the same screens throughout the
  retarget without noticing. The Playwright fixtures now opt into a `pageerror`
  guard.
- **Selenium found a stale-toast race.** A successful credential step raises its
  own "MFA required" toast, still on screen when the one-time code is submitted.
  Playwright and Cypress retry assertions until the text matches, which silently
  papers over the stale element; Selenium's explicit waits do not, so the
  bad-code test was asserting against the wrong message.
- **Selenium also found `BUG-UI-002`** — the back-office sidebar is not
  role-filtered.
- **The database suite found `BUG-DB-001`** by reading `information_schema`
  rather than driving the application: two money-adjacent columns were stored as
  binary floats, drifting two minor units on a 10,000.00 conversion. The
  endpoint returned a rounded figure that looked correct.
- **REST Assured found `BUG-API-001`** — the API documented `413` for an
  oversized body but dropped the connection instead. curl had always shown the
  413; a different HTTP client did not, which is the point of testing a contract
  with more than one consumer.

## Shared conventions

Applied identically across every suite that drives the UI:

**Selectors.** Everything resolves by `data-testid`. No CSS class or
visible-text selectors — those are what broke the previous generation of these
suites when the application changed.

**Sign-in is two steps.** Credentials raise an MFA challenge; the challenge is
exchanged for a session. Each suite has one helper that performs both, and at
least one test asserts that no session exists between them.

**Waiting.** The application updates its page heading before view data arrives.
Every suite waits for the view placeholder to clear, on both sides of a
navigation. There are no fixed sleeps anywhere.

**Money.** Amounts are asserted in integer minor units, read from
`data-balance-minor` or the API field, never parsed from a formatted currency
string.

**Balance assertions are deltas.** "Decreased by at least 10000 minor units",
never "equals 24,900,000". Absolute values are only stable against a freshly
seeded database.

**Modals.** Transfer, beneficiary and account-opening forms are modals that do
not exist until opened, and a rejected submission leaves the modal up where it
swallows the next navigation click. Every suite dismisses an open modal before
navigating.

## Not yet started

| Area | Status |
|---|---|
| Jest | Not started. Ten pure functions have no direct test, including the `?` to `$n` placeholder rewriter every query passes through |
| JMeter / k6 | Not started. The row-locking work gives load testing something real to prove |
| Jenkins | A `Jenkinsfile` exists in the application repository but drives none of these suites |
| axe-core | Not started. No accessibility coverage anywhere |
| OWASP ZAP | Not started |

### Evaluated and not adopted

Recorded rather than silently dropped, since the original plan named them:

| Tool | Why not |
|---|---|
| Pact | Contract testing addresses consumer/provider drift across teams. There is one frontend and one backend. It would be ceremony without a second consumer |
| WireMock | Simulates external services. The application already exposes `qaSimulation: "failure"` for fault injection, which the suites use. WireMock would replace a working mechanism with a heavier one |
| Testcontainers | Would give the database suite a disposable database, but requires Docker, which would stop the suite running on a machine without it. `DATABASE_URL` works against a local database, a hosted one and the CI service container alike |

## Coverage

180 of 187 requirements are covered. The remainder is `DASH` (7), which has no
dedicated scenarios or test cases and is reached only incidentally through
account and transaction coverage.

`DB` (15) is closed by `database-testing/`; `SYS` (10) and `AUDIT` (10) by
`rest-assured/`. Those two suites together cover 35 requirements no UI test
could reach.
