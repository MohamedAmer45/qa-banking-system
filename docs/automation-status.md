# Automation Status

Last synchronized: 2026-09-26

## Summary

Nine suites, all passing against the PostgreSQL build.

| Suite | Tests | Browsers |
|---|---|---|
| Playwright + TypeScript | 37 | Chromium, Firefox, WebKit |
| Cypress + TypeScript | 26 | Chrome / Electron |
| Selenium + Java + TestNG | 27 | Chrome |
| Cucumber JVM | 24 scenarios | Chrome |
| Database (JDBC + TestNG) | 59 | n/a |
| REST Assured | 113 | n/a |
| Postman / Newman | 103 requests, 440 assertions | n/a |
| Jest (unit) | 134 | n/a |
| JMeter (performance) | 5 shapes | n/a |

Every suite starts the application inside the CI runner against a `postgres:16`
service container, so runs are isolated and begin from an identical seed. No
suite depends on a hosted environment.

All 65 endpoints the application serves are exercised by at least one suite.

The Jest suite is the exception to the layout: it lives in the **application**
repository, not this one, because it imports application internals directly.
Holding it here would mean either publishing those internals as a package or
reaching across repositories on every run. It is listed here because it is part
of the same testing stack and the same coverage argument.

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

Jest is absent from that table on purpose. Every column above describes a suite
driving the running application; Jest calls functions directly, with no server
and no database. Adding it as a column would be a row of "No" that reads like a
gap rather than a different layer.

That division has earned itself six times:

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
- **Selenium also found `BUG-UI-002`** — the back-office sidebar was not
  role-filtered, so four of the five staff roles were offered modules the
  server refuses. Fixed; the sidebar is now filtered on the permissions the
  server reports.
- **The database suite found `BUG-DB-001`** by reading `information_schema`
  rather than driving the application: two money-adjacent columns were stored as
  binary floats, drifting two minor units on a 10,000.00 conversion. The
  endpoint returned a rounded figure that looked correct.
- **REST Assured found `BUG-API-001`** — the API documented `413` for an
  oversized body but dropped the connection instead. curl had always shown the
  413; a different HTTP client did not, which is the point of testing a contract
  with more than one consumer.
- **Writing `DASH` coverage found `BUG-DASH-001`** — the sixth, and the one
  that says most about the other five. Three of the dashboard's seven
  requirements were never rendered, so the module had nothing to test. Every UI
  suite crossed that page constantly, and an absent panel is invisible to a
  suite that was never told to look for it. Coverage measured against
  requirements caught what coverage measured against the application could not.

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

## What the unit layer adds

The seven application-driving suites exercise these functions constantly, but
they report a fault in one as something else. A bug in the `?` to `$n`
placeholder rewriter surfaces as "the transfer credited the wrong account"; a
wrong cell in the permission table surfaces as a 403 nobody expected. Jest names
the failure at the line that caused it.

| File | Tests | Covers |
|---|---|---|
| `database.test.js` | 29 | `toPositional`, `withReturningId` |
| `security.test.js` | 30 | Password hashing, token generation, masking, ISO-8601 ordering |
| `money.test.js` | 31 | `moneyMinor`, `convertMinor`, loan amortisation, recurrence dates |
| `access.test.js` | 44 | The role/permission grid, `permissionsFor`, `publicUser`, `sanitizeIdNumber` |

The permission grid is asserted as a whole rather than case by case. A
permission table is only correct if every cell is, and the failure worth
catching is a role quietly gaining access — which shows up in a matrix as one
wrong cell, and in case-by-case tests as a test nobody thought to write. A
further test fails if a permission is added to the table without the grid
deciding who holds it.

Two tests assert behaviour that is not ideal, rather than omitting it:

- `toPositional` rewrites a question mark inside a string literal. No query in
  the application contains one, which is why the simple approach is safe, but a
  future query with a literal `?` would break and the test says so.
- `moneyMinor(8.165)` is 816, because `8.165 * 100` is `816.4999999999999`.
  Reachable only with sub-cent input, which `step="0.01"` prevents.

A test that passes while hiding a known limitation is worth less than one that
states it.

**Coverage is 17.3% of statements, and that is the unscoped figure.**
`security.js` reaches 100% and `access.js` 32.4%, while `banking.js` sits at
7.6%, because the rest of `banking.js` is asynchronous database work that the
API, UI and database suites cover instead. Narrowing the denominator to the tested files would produce a
flattering number that measures nothing. The per-file table is in the coverage
output so the split stays visible.

## Performance, and the one environment exception

`jmeter/` holds five load shapes over two plans: `load`, `stress`, `spike` and
`endurance` against the read path, and `concurrency`, in which every thread
debits the **same** account at the same instant, released together by a
Synchronizing Timer.

`stress` and `spike` are *observed* rather than gated: 5xx does not fail them,
because a ramp that produced no errors has not found the limit it went looking
for. Correctness is gated in all five regardless. Every run reports error rate,
throughput, average, p50/p90/p95/p99 and max, and writes JMeter's HTML
dashboard; latency is also broken into quarters, which is what makes a spike
recovering or a soak degrading legible at all.

The second is the point. The application claims row-level locking in
deterministic id order so that simultaneous debits against one balance either
commit completely or are refused. That claim is now tested at a concurrency the
database suite's `ConcurrencyTest` cannot reach, and through the API rather than
through SQL:

| Where | Amount | Threads | 201 | 409 | Balance moved | Reconciles to |
|---|---|---:|---:|---:|---:|---|
| CI | 50 EGP | 30 | 30 | 0 | 165,000 | 30 x (5,000 + 500) |
| CI | 20,000 EGP | 30 | 7 | 23 | 14,014,000 | 7 x (2,000,000 + 2,000) |
| local | 7,890 EGP | 20 | 9 | 11 | 7,108,101 | 9 x (789,000 + 789) |
| local | 500,000 EGP | 20 | 0 | 20 | 0 | nothing succeeded, nothing moved |

The second row is the one worth having. Seven debits committed and twenty-three
were refused against one balance, and the ledger came out exact to the minor
unit. CI runs the plan twice for this reason — once sized to fit, once sized to
exhaust — because a run where everything succeeds has not shown that refusal
works.

**This is the only suite that does not target the deployed environment.** It runs
against an application started in the runner against a throwaway `postgres:16`
container. Load testing a shared database would drain the seeded data every other
suite reads, and the figures would describe network latency to a free-tier
database rather than this application: the same plan measured p95 near 37 seconds
locally over the internet. `jmeter/README.md` and `docs/test-environment.md` both
record the exception.

**Latency is reported, not gated.** There is no performance requirement in the
catalog to enforce, and a p95 target invented here would be an SLA nobody asked
for. What is gated is correctness, which travels between machines: no 5xx, every
concurrent debit `201` or `409`, and the balance falling by exactly the amount
plus fee of every success. `jmeter/thresholds.json` records the order for
changing that — baseline from CI, add `PERF` requirements to the catalog, then
enforce them.

## Jenkins

`Jenkinsfile` at the root of this repository runs the full regression: it clones
the application, resets and seeds a database, starts it, then runs every suite
and publishes per-test results.

Two things about it are deliberate.

**The suites run sequentially, not in parallel.** They all move money through the
same seeded accounts, and Jenkins runs them against one application and one
database. In parallel they would observe each other's balances and fail on
arithmetic none of them controlled, which is `LIM-005`. GitHub Actions
parallelises them safely only because each workflow gets its own application and
its own throwaway database; reproducing that on Jenkins means an instance per
suite, which is a larger change than it looks.

**There is no `tools` block.** That directive refers to tool installations
configured on the controller *by name*, so a Jenkinsfile naming them breaks on
any Jenkins that calls them something else. A Preflight stage checks for what it
needs and names what is missing instead.

Every suite emits JUnit XML so the results are per test rather than per stage.
Playwright gained a `junit` reporter for this; the JVM suites already had
Surefire, Cypress `mocha-junit-reporter`, and Newman its own.

**What has and has not been verified.** The pipeline is parsed as Groovy — the
AST builds, so it has no syntax error — and its directives and steps are the
standard Declarative set. It has **not** been executed on a Jenkins controller,
because there is no Jenkins in the environment this was written in. Treat the
first real run as the thing that proves it: expect to fix credential and tool
availability on the agent before anything else, since those are the parts a
Jenkinsfile cannot assert about itself.

That is also why the Preflight stage exists and why there is no `tools` block —
the two failure modes most likely on a first run are made loud and early rather
than left to surface midway through a suite.

The application repository keeps its own separate `Jenkinsfile`, which builds
and smoke-tests the application alone. That one is not redundant with this: it
answers "is the build good", where this answers "does the build pass the
regression".

## Not yet started

| Area | Status |
|---|---|
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

**All 187 requirements across all 16 modules are covered.**

`DASH` (7) was the last gap and is now automated, in
`playwright/tests/dashboard/dashboard.spec.ts`. Closing it turned up something
worth recording rather than quietly fixing: the module was not untested because
nobody had reached it. Three of its seven requirements — recent transactions,
active cards and upcoming scheduled payments — were never rendered at all, so
there was nothing to assert against, and the overview carried no `data-testid`
attributes either. That is `BUG-DASH-001`.

It is the sharpest illustration in this project of what "covered incidentally"
is worth. Every UI suite passed through the customer overview constantly on the
way somewhere else, and not one of them could notice a panel that was absent,
because none of them had been told to look for it.

`DB` (15) is closed by `database-testing/`; `SYS` (10) and `AUDIT` (10) by
`rest-assured/`. Those two suites together cover 35 requirements no UI test
could reach.

## Open defects

**None.** All eight recorded defects are closed:

| Defect | Found by | Resolution |
|---|---|---|
| `BUG-UI-001` | Cypress | Fixed — `TypeError` on every page load |
| `BUG-DB-001` | Database suite | Fixed — FX and interest rates were binary floats |
| `BUG-API-001` | REST Assured | Fixed — oversized bodies dropped the connection |
| `BUG-BEN-001` | API re-verification | Fixed — the list returned soft-deleted rows |
| `BUG-UI-002` | Selenium | Fixed — the back-office sidebar was not role-filtered |
| `BUG-DASH-001` | Writing `DASH` coverage | Fixed — three requirements were never rendered |
| `BUG-AUTH-001` | — | Closed, not reproducible after the PostgreSQL port |
| `BUG-ACC-001` | — | Closed as obsolete; the UI it described no longer exists |

Each carries regression cover in the suite that found it, so a reappearance
fails the same suite rather than waiting for someone to re-check by hand.
