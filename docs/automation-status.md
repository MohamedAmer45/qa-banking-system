# Automation Status

Last synchronized: 2026-09-21

## Summary

All four UI/BDD suites are retargeted at the real application and passing.

| Framework | Tests | Status | Browsers |
|---|---:|---|---|
| Playwright + TypeScript | 24 | Passing | Chromium, Firefox, WebKit |
| Cypress + TypeScript | 26 | Passing | Chrome / Electron |
| Selenium + Java + TestNG | 21 | Passing | Chrome |
| Cucumber JVM | 21 scenarios | Passing | Chrome |
| Database (JDBC + TestNG) | 59 | Passing | n/a |

Every suite starts the application inside the CI runner against a `postgres:16`
service container, so runs are isolated and begin from an identical seed. No
suite depends on a hosted environment.

## How the four divide the work

They are deliberately not four copies of the same coverage.

| | Playwright | Cypress | Selenium | Cucumber |
|---|---|---|---|---|
| End-to-end money movement | Primary | No | Yes | Yes |
| Cross-browser | Yes | No | Configurable | No |
| Form validation | Minimal | Primary | Some | Some |
| Network contract assertions | Minimal | Primary | No | No |
| Uncaught page exceptions | Opt-in guard | Fails by default | No | No |
| Requirement traceability | Test ids | Test ids | Test ids | Gherkin tags |

That division has already paid for itself twice:

- **Cypress found `BUG-UI-001`** — a `TypeError` thrown on every page load —
  on its first `cy.visit`, because it fails a test on any uncaught application
  exception. Playwright had been driving the same screens throughout the
  retarget without noticing. The Playwright fixtures now opt into a
  `pageerror` guard.
- **Selenium found a stale-toast race.** A successful credential step raises
  its own "MFA required" toast, still on screen when the one-time code is
  submitted. Playwright and Cypress retry assertions until the text matches,
  which silently papers over the stale element; Selenium's explicit waits do
  not, so the bad-code test was asserting against the wrong message.
- **Selenium also found `BUG-UI-002`** — the back-office sidebar is not
  role-filtered.
- **The database suite found `BUG-DB-001`** by reading `information_schema`
  rather than driving the application: two money-adjacent columns were stored
  as binary floats.

## Shared conventions

Applied identically across all four:

**Selectors.** Everything resolves by `data-testid`. No CSS class or
visible-text selectors — those are what broke the previous generation of these
suites when the application changed.

**Sign-in is two steps.** Credentials raise an MFA challenge; the challenge is
exchanged for a session. Each suite has one helper that performs both, and at
least one test asserts that no session exists between them.

**Waiting.** The application updates its page heading before view data
arrives. Every suite waits for the view placeholder to clear, on both sides of
a navigation. There are no fixed sleeps anywhere.

**Money.** Amounts are asserted in integer minor units, read from
`data-balance-minor`, never parsed from a formatted currency string.

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
| Postman / Newman | Not started. Unblocked |
| REST Assured | Not started. Unblocked |
| Jest | Not started. Unblocked |

| JMeter / k6 | Not started. Unblocked |
| Jenkins | Pipeline exists in the application repository; not yet driving these suites |

None of these depend on the UI work.

The `DB` module is now complete — all 15 requirements covered by
`database-testing/`, and it immediately found BUG-DB-001: FX and interest rates
stored as single-precision floats, drifting 2 minor units on a 10,000.00
conversion. No API-level test could have seen it, because the endpoint returns
a rounded figure that looks correct. The error was in the storage type.
