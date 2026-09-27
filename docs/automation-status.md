# Automation Status

Last synchronized: 2026-09-26

## Summary

Nine suites, all passing against the PostgreSQL build. Accessibility runs
inside Playwright rather than as a tenth.

| Suite | Tests | Browsers |
|---|---|---|
| Playwright + TypeScript | 67 | Chromium, Firefox, WebKit |
| Cypress + TypeScript | 26 | Chrome / Electron |
| Selenium + Java + TestNG | 27 | Chrome |
| Cucumber JVM | 24 scenarios | Chrome |
| Database (JDBC + TestNG) | 59 | n/a |
| REST Assured | 130 | n/a |
| Postman / Newman | 103 requests, 440 assertions | n/a |
| Jest (unit) | 134 | n/a |
| JMeter (performance) | 5 shapes | n/a |
| axe-core (accessibility) | 30, counted within the Playwright total above | Chromium |

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

**Latency is now gated for the two steady shapes**, in the order
`jmeter/thresholds.json` had prescribed: baseline from CI, then the `PERF`
requirements in the catalog, then the thresholds. `PERF-001` sets p95 under 100 ms
and `PERF-003` caps drift across a run at 200 ms.

The budget applies only in CI against a loopback target, and both conditions are
needed. The first attempt used loopback alone and failed a perfectly healthy run
at 1372 ms — a locally served application can still be talking to a database
across the internet, which is exactly the cross-environment flakiness the file
warned about. Elsewhere the figures are printed and gated on nothing.

The headroom is deliberate: 100 ms against an observed 2–7 ms. A threshold set
near the observed figure fails on ordinary runner variance, and a gate that cries
wolf gets ignored, which is worse than no gate.

Stress, spike and concurrency stay reported. A ramp past capacity is *meant* to
degrade; thirty debits serialising on one row lock are *meant* to queue, so a p95
there measures queue length rather than health. Their correctness is gated
absolutely by `PERF-002` and `PERF-004`.

**The stress ceiling is raised from 150 threads to 400.** At 150 the application
sustained 79,845 samples with zero errors and a flat 3–4 ms p95 across every
quarter, which means the ramp never reached the knee it exists to find. If 400
also comes back flat, the honest conclusion is that the bottleneck is the runner
or JMeter rather than the application, and the answer is a bigger load generator
rather than a bigger number.

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

**Verified by running it.** A Jenkins 2.583 controller was stood up locally, the
pipeline linted clean through `/pipeline-model-converter/validate`, and the whole
job ran end to end against a throwaway database:

```text
build #5   SUCCESS   19.9 min

  Preflight               node v26.7.0, java 21.0.12, Maven 3.9.16
  Start NovaBank          cloned, migrated --reset, seeded, healthy
  API — REST Assured      130 passed
  Database — JDBC          59 passed
  Postman — Newman        104 requests, 440 assertions, 0 failed
  UI — Playwright          67 passed
  UI — Cypress             26 passed
  UI — Selenium            27 passed
  BDD — Cucumber           24 scenarios
  Performance — JMeter    skipped (RUN_PERFORMANCE=false)
  Ledger reconciliation   ran

  post: 763 tests recorded, 0 failed, 13 artifacts archived
```

The JUnit publisher collected all 763 across seven suites, which is what the
Playwright `junit` reporter was added for.

**Two defects in the pipeline were found by running it, and neither would have
been found by reading it.**

`APP_REF` arrived empty on the job's *first* build. Jenkins registers a
`parameters` block only after a build has parsed it, so the declared default of
`main` was not applied and the clone ran `--branch ''`. Every later build worked,
which makes it the worst kind of bug: it bites only the first person to set the
job up. The shell now defaults the ref itself.

The Newman stage passed `--env-var baseUrl` and no environment file. That file
also carries the credentials the collection signs in with, so the first request
failed 401 and cascaded into 153 of 407 assertions before the pipeline aborted.
A lint cannot see either of these.

One limit worth stating: the controller ran on Windows, where `sh` resolves to
Git Bash (`MINGW64_NT`). The pipeline is written for a POSIX shell and got one, so
this exercised the same code path a Linux agent would, but it is not proof
against a Linux-specific difference such as `playwright install --with-deps`
behaving differently there.

The application repository keeps its own separate `Jenkinsfile`, which builds
and smoke-tests the application alone. That one is not redundant with this: it
answers "is the build good", where this answers "does the build pass the
regression".

## Accessibility

`playwright/tests/accessibility/` — 30 tests against the nine `A11Y`
requirements added to the catalog on 2026-09-26. axe-core 4.13 scans 23 views
plus an open modal, asserting **zero** violations rather than a tolerated list.

It is in Playwright only. Running the same engine over the same DOM from Cypress
as well would duplicate rather than divide, which is the one thing the table
above exists to avoid.

**The first scan found five violation types across 24 views**, recorded as
`BUG-A11Y-001` and fixed:

| Rule | Count | Cause |
|---|---|---|
| `label` | 73 controls | `<div class="field"><label>X</label><input>` — adjacent, never associated |
| `select-name` | 6 selects | Filter and account selects with no name at all |
| `color-contrast` | 1 | Sidebar heading at 3.73:1 where 4.5:1 is required |
| `scrollable-region-focusable` | 1 | The audit table scrolls with a pointer, not a keyboard |

One pattern repeated 73 times is why a single root cause produced the largest
violation count in the project. Sighted users read the label-field pairing from
the layout; nothing in the markup stated it.

A fifth fault was invisible to the scanner and found by driving the interaction:
modals had no `role="dialog"`, moved no focus, trapped no focus, ignored Escape,
and returned focus nowhere. They now do all four.

**What "Automated" means here, and what it does not.** `A11Y-001` to `A11Y-006`
are decided by the engine — a control either exposes a name or it does not.
`A11Y-007` to `A11Y-009` are not fully machine-decidable and are driven as
interactions: focus entering a dialog and staying there, Escape returning focus
to the trigger, a status message announced without stealing focus, sign-in
completed with no pointer.

Beyond that, automated checks find only a minority of WCAG issues. No engine
judges whether alt text is useful, whether reading order makes sense, or whether
a screen reader announces a transfer coherently. Three test cases in
`accessibility-test-cases.md` are marked manual-only for exactly that, and are
deliberately absent from the automated count. A green run is a floor, not a claim
that the application is accessible.

## Security scanning

Two layers, deliberately different in kind.

**`rest-assured/.../SecurityHeadersTest` — 17 tests, one per `WEBSEC`
requirement.** These run on every API build and hold the named controls in
place: framing refused, MIME sniffing forbidden, referrer withheld, CORS granted
to no unknown origin, account data uncacheable, no server version disclosed,
unused browser features denied.

**OWASP ZAP baseline scan — `.github/workflows/zap.yml`.** Passive: it reads the
responses it receives while spidering and sends no attack payloads, so it cannot
corrupt seeded data and the run repeats. It finds the things nobody thought to
assert.

A latch and a net. The assertions know what they are looking for; the scan does
not, which is the point of having both.

**The scan never targets the deployed environment.** It runs against an
application started in the runner. Pointing a spider at the hosted deployment
would probe infrastructure on a shared provider — traffic a provider is entitled
to treat as an attack — and do it against the database every other suite reads.
The same exception as JMeter, recorded in `docs/test-environment.md`.

**What the first pass found.** The application sent no security headers at all;
only HSTS was present, and that came from the hosting platform rather than the
code. The API answered every preflight with `Access-Control-Allow-Origin: *` —
for a bank, the wrong default even with bearer tokens rather than cookies. Both
are fixed; the headers are now set centrally in `send`, `sendFile` and
`serveStatic`, and on the static routes in `vercel.json`, because on the hosting
platform the shell is served by the static builder and never reaches the
application's own code. That last part is the sort of gap a test against
localhost alone would never have shown.

**The limitation that was stated rather than hidden is now closed.**
`WEBSEC-001` asks for a CSP restricting script sources, and for a while
`script-src` had to keep `'unsafe-inline'`: the interface attached 62 handlers as
inline attributes, and an inline handler cannot run without it — a nonce does not
help, because nonces apply to `<script>` elements and not to handler attributes.

All 62 are now delegated. Markup carries `data-action="cardAction" data-a1="7"
data-a2="freeze"` and three delegated listeners resolve it against a whitelist of
permitted action names — a whitelist rather than a `window[name]` lookup, because
resolving an arbitrary action name through the global scope would be a smaller
hole of exactly the kind this change closes. `script-src` is `'self'` alone.

Verified by re-running every suite that drives the interface against the
tightened policy: Playwright 67, Cypress 26 (which fails on any uncaught page
exception), Selenium 27, Cucumber 24. A test asserts `'unsafe-inline'` cannot
return, and the ZAP suppression for rule 10055 is narrowed to `style-src` so an
alert naming `script-src` fails the scan instead of being absorbed.

`style-src` still carries `'unsafe-inline'` for inline `style` attributes. That
sits outside `WEBSEC-001`, which covers scripts, objects and document base: an
inline style cannot execute script.

`security/zap-rules.tsv` carries a reason beside every suppressed rule, and
`security/analyze-zap.mjs` refuses an `IGNORE` that has none — the rules file
enforces its own discipline, because an unexplained suppression is how a scanner
quietly stops reporting things that matter.

**The gate is ours, not the tool's**, and that was not the first design. The
`zaproxy/action-baseline` wrapper was tried first and did two things badly: it
copies its rules file into the container by basename while handing ZAP the full
relative path, so ZAP found no config and applied none of the suppressions with
nothing in the output to say so; and its own internal artifact upload fails on
current runners, failing the step regardless of `fail_action`. ZAP is now run
directly from the same image, its exit code discarded, and
`security/analyze-zap.mjs` decides — the same shape as `jmeter/analyze.mjs`, for
the same reason. A tool whose exit code is the only gate is a tool you cannot
reason about, and this one can be run against a saved report on a laptop.

**What the first scan reported, and what was done with each of the ten alerts.**
One was fixed: `Cross-Origin-Embedder-Policy` was missing, and `require-corp` is
safe here because the interface loads nothing cross-origin — checked before
setting it. Five were accepted with reasons: the two `unsafe-inline` CSP alerts
(the documented `WEBSEC-001` limitation), `Non-Storable Content` (which fires
because account data sets `no-store` on purpose), `Modern Web Application`
(informational SPA detection), `Suspicious Comments` (ordinary identifiers plus
`BUG` where a comment cites a defect id), and the `Sec-Fetch-*` request headers
the client sends rather than the server. Two stay at `WARN` so a change surfaces
rather than passes quietly.

## Not yet started

Nothing. Every tool the original plan named is built, wired into CI, and has
been run: the seven functional suites, Jest, JMeter, axe-core, OWASP ZAP and the
Jenkins pipeline.

What that does **not** mean is that the testing is finished. Two things are worth
naming, because an empty list invites the wrong conclusion:

- **Three accessibility test cases are manual-only** and deliberately outside the
  automated count: screen reader coherence, reading order, and 200% zoom.
  Automated checks find a minority of WCAG issues and none of the
  judgement-based ones.

### Evaluated and not adopted

Recorded rather than silently dropped, since the original plan named them:

| Tool | Why not |
|---|---|
| Pact | Contract testing addresses consumer/provider drift across teams. There is one frontend and one backend. It would be ceremony without a second consumer |
| WireMock | Simulates external services. The application already exposes `qaSimulation: "failure"` for fault injection, which the suites use. WireMock would replace a working mechanism with a heavier one |
| Testcontainers | Would give the database suite a disposable database, but requires Docker, which would stop the suite running on a machine without it. `DATABASE_URL` works against a local database, a hosted one and the CI service container alike |

## Coverage

**All 210 requirements across all 19 modules are covered.**

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

**None.** All nine recorded defects are closed:

| Defect | Found by | Resolution |
|---|---|---|
| `BUG-UI-001` | Cypress | Fixed — `TypeError` on every page load |
| `BUG-DB-001` | Database suite | Fixed — FX and interest rates were binary floats |
| `BUG-API-001` | REST Assured | Fixed — oversized bodies dropped the connection |
| `BUG-BEN-001` | API re-verification | Fixed — the list returned soft-deleted rows |
| `BUG-UI-002` | Selenium | Fixed — the back-office sidebar was not role-filtered |
| `BUG-DASH-001` | Writing `DASH` coverage | Fixed — three requirements were never rendered |
| `BUG-A11Y-001` | First axe-core scan | Fixed — 73 unnamed controls, one unreachable region, dialogs not announced |
| `BUG-AUTH-001` | — | Closed, not reproducible after the PostgreSQL port |
| `BUG-ACC-001` | — | Closed as obsolete; the UI it described no longer exists |

Each carries regression cover in the suite that found it, so a reappearance
fails the same suite rather than waiting for someone to re-check by hand.
