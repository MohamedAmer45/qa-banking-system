# Automation Status

Last synchronized: 2026-09-21

## Summary

All four UI suites were written against the deterministic stub that this
project tested until 2026-09-20. That stub has been deleted and replaced by the
real NovaBank application. **The suites compile and are well built, but every
page object selects against a DOM that no longer exists.** They must be
retargeted before they can run.

This is recorded as a real status rather than hidden, and the suites are kept
rather than deleted, because the test *design* is still valid — only the
selectors and the login flow changed.

## Why the selectors do not carry over

| | Stub | NovaBank |
|---|---|---|
| Sign in | Click `#login button.enter[data-role='customer']` | Email + password form, then an MFA challenge |
| Navigation | `#nav button[data-s='transfers']` | `.nav-btn[data-view='transfers']` |
| Session | `sessionStorage.nb_token` | Opaque token from `POST /api/auth/mfa` |
| Transfer form | `#from`, `#recipient`, `#transferAmount` | `#transfer-form` fields |
| State | `localStorage.nb_state`, resettable from the browser | PostgreSQL, reset via `npm run db:reset` |

There is no selector overlap. `#who`, `#logout`, `#adminNav`, `#from`,
`#recipient` and `#transferForm` do not exist in the application.

The login change matters more than the selectors: every test's `beforeEach`
must now perform a two-step credential + MFA handshake instead of clicking a
demo button.

## Framework status

| Framework | Code quality | Retarget needed | Notes |
|---|---|---|---|
| Playwright + TypeScript | Good — POM, fixtures, balance-delta assertions | 9 of 13 page objects | Best starting point: fixtures and structure carry over unchanged |
| Cypress + TypeScript | Good — POM, network aliases, no fixed waits | 11 of 11 page objects | Custom commands (`loginAsCustomer`) need rewriting around the MFA handshake |
| Selenium + Java | Good — POM, explicit waits, no `Thread.sleep` | `LoginPage` + all `*CurrentPage` | 8 dead page objects from the pre-stub generation have been deleted |
| Cucumber JVM | Good — DI via picocontainer, clean hooks | 9 of 33 files | Feature files are mostly reusable; step definitions and page objects are not |

Across all four: no `Thread.sleep`, no `waitForTimeout`, no `cy.wait(<number>)`.
Every wait is conditional. That discipline is worth preserving through the
retarget.

## Recommended sequence

1. **Add `data-testid` attributes to the application first.** Retargeting
   against ids and text, then again later against test ids, is doing the work
   twice. This is a small change in `public/app.js` and removes LIM-001
   permanently.
2. **Retarget Playwright first.** It has the cleanest structure, and the
   resulting selector map and auth helper can be transcribed into the other
   three rather than rediscovered.
3. **Build a shared auth helper per framework** that performs login → MFA →
   token once, rather than repeating the handshake in every spec.
4. **Point CI at a locally started application** with a PostgreSQL service
   container, so suites run against a freshly seeded database. This also
   removes the environment wake-up polling the workflows currently carry.

## Not yet started

| Area | Status |
|---|---|
| Postman / Newman | Not started. Unblocked — the API is real and documented |
| REST Assured | Not started |
| Jest | Not started |
| SQL / database testing | Not started. Unblocked — `DATABASE_URL` reaches the same data the API serves |
| JMeter / k6 | Not started |
| Jenkins | Pipeline exists in the application repository; not yet driving these suites |

API, database and performance work no longer depends on the UI retarget. Any
of them can proceed in parallel, and against a real backend for the first time.
