# BUG-DASH-001 — Three dashboard requirements were never rendered

| Field | Value |
|---|---|
| Module | Dashboard (`DASH`) |
| Severity | Medium |
| Priority | Medium |
| Status | **Closed — fixed 2026-09-23** |
| Found by | Writing `DASH` coverage, 2026-09-23 |
| Requirements | `DASH-003`, `DASH-004`, `DASH-005` |

## Summary

The customer overview did not render recent transactions, active cards or
upcoming scheduled payments, all three of which the requirements catalog says
it shall display.

## How it was found

`DASH` was the last module in the traceability matrix with no automated
coverage, recorded as "Incidental only". The assumption had been that this was
a gap in the test suite.

It was not. Writing the tests meant reading the view first, and the view had
nothing to assert against:

| Requirement | Catalog text | Rendered |
|---|---|---|
| `DASH-001` | Overview of their accounts after login | Yes |
| `DASH-002` | Current **and available** balances | Balance only |
| `DASH-003` | Recent transactions | **No** — only a transfer *count* |
| `DASH-004` | Active cards | **No** |
| `DASH-005` | Upcoming scheduled payments | **No** |
| `DASH-006` | Unread notifications | Partly — shown, never counted |
| `DASH-007` | Only their own information | Yes |

The module had also been marked "Implemented: Yes" in the matrix, which was
wrong, and wrong in the flattering direction.

A second, smaller finding: the overview carried no `data-testid` attributes of
its own. Every other view in the application exposes them. So even the panels
that did exist could not be selected without falling back to layout classes or
visible copy.

## Assessment

This is the more interesting half of the defect. A missing test looks like
missing effort; this was a missing *feature* wearing a missing test as a
disguise. The module reported as "covered incidentally" because other suites
passed through the overview on their way elsewhere, and passing through it is
exactly the kind of coverage that cannot notice an absent panel.

None of the data was missing. Accounts already carried `available_minor`,
`transactions` was fully populated, `cards` held status, and both
`bill_payments` and `transfers` had a `scheduled_for`. The dashboard simply
never read any of it.

## Fix

`renderOverview` in `public/app.js` now renders all seven requirements:

- recent transactions, merged across the customer's accounts and capped at five
- active cards, filtered on `status === 'ACTIVE'`
- upcoming scheduled payments, combining scheduled bill payments and scheduled
  transfers, future-dated and ordered soonest first
- an available balance on every account card, beside the balance
- an explicit unread count, agreeing with the topbar badge

Every panel has a `data-testid`, and every panel has an explicit empty state, so
"this customer has none" is distinguishable from "the panel did not render".

Transactions are read per account and merged in the client rather than through
a new aggregate endpoint, so the dashboard adds no API surface of its own and
no new route needs documenting, schema-testing or authorizing.

## Verification

`playwright/tests/dashboard/dashboard.spec.ts` — 13 tests across all seven
requirements, passing. Confirmed against live data that every panel had real
content rather than passing on an empty state:

```text
active cards       : 1
unread notifications: 44
upcoming bills     : 1
recent transactions: 8 on the primary account
```

Full suite after the change: Playwright 37, Cypress 26 (which fails on any
uncaught page exception), Selenium 27, Cucumber 24 scenarios, REST Assured 113,
database 59, Postman 440 assertions, Jest 134.

## Impact on testing

`DASH` moves from "Incidental only" to automated, closing the last module gap
in the traceability matrix. `manual-testing/test-scenarios/dashboard-scenarios.md`
and `manual-testing/test-cases/dashboard-test-cases.md` were written alongside
it; the module previously had neither.
