# Known Issues and Limitations

Last synchronized: 2026-09-23

## Closed gaps

Every gap recorded against the previous stub build is now closed. They are kept
here rather than deleted, because the scenarios and test cases written against
them were marked `Blocked` and now need executing rather than rewriting.

| ID | Area | Former limitation | Status |
|---|---|---|---|
| GAP-001 | Authentication | Real credential login unavailable | **Closed** — email + password |
| GAP-002 | Authentication | MFA unavailable | **Closed** — mandatory challenge step |
| GAP-003 | Beneficiaries | Module unavailable | **Closed** — add, edit, delete, OTP verify |
| GAP-004 | Accounts | Account creation unavailable | **Closed** — requires verified KYC |
| GAP-005 | Accounts | Extended controls unavailable | **Closed** — freeze, close, dormant, limits |
| GAP-006 | Statements | Dedicated statements unavailable | **Closed** — with CSV export |
| GAP-007 | Database | No persistent storage | **Closed** — PostgreSQL |
| GAP-008 | SQL | No persisted-data validation | **Closed** — direct `DATABASE_URL` access |

Any test still marked `Blocked` against one of these should be re-executed and
given a real Pass or Fail.

## Open limitations

| ID | Area | Limitation | Impact on testing |
|---|---|---|---|
| ~~LIM-001~~ | UI automation | ~~No `data-testid` attributes~~ | **Closed** — 48 test ids added; every suite resolves through them |
| ~~LIM-002~~ | UI automation | ~~Page objects target the deleted stub~~ | **Closed** — every UI suite retargeted and passing |
| LIM-003 | Hosting | Vercel serverless has no long-lived process | Scheduled transfers and bills are swept on API traffic, at most once per 30s. A scheduled-item test must make a request after the due time rather than waiting passively |
| LIM-004 | Environment | Production and CI share no database | CI provisions its own PostgreSQL. Tests must not assume state created in one environment exists in the other |
| LIM-005 | Test data | Suites mutate shared seed data | Against the hosted environment, tests that move money are order-dependent. Prefer CI (reseeded per run), or make assertions relative to a balance read at test start rather than absolute |
| LIM-006 | Security | `QA_MODE=true` exposes one-time codes | Intentional, so tests need no mail server. Any test asserting on `demoCode`/`demoOtp` is invalid against a hardened environment |

## Open defects

**None.** All eight recorded defects are closed, each with regression cover in
the suite that found it. See `docs/defects/` and the table in
`docs/test-execution-status.md`.

`BUG-BEN-001` and `BUG-UI-002` were carried open through the defect workflow
for two days rather than quietly patched, and were fixed on 2026-09-23 along
with `BUG-DASH-001`.

## Notes

**LIM-005** is the one most likely to cause confusing failures. The reference
balance in the seed is 25,000,000 minor units on account `1000000001`, but any
suite that has already run will have moved it. Assertions of the form
"balance decreased by exactly N" survive; assertions of the form
"balance equals 24,900,000" do not.

A worked example, from 2026-09-23. The Postman request *Refuse a transfer
beyond the daily ceiling* asked for the whole daily limit plus 10,000, on the
assumption that the account's balance exceeds its daily limit. After the other
suites had moved money it no longer did, so the **balance** check refused the
transfer before the daily-limit check could, and the assertion failed against
the wrong rule — reporting a limits defect where there was none.

The fix generalises past this one request: exceed the *remaining* allowance by
one minor unit rather than the whole limit by a round number. That is the
smallest amount that still triggers the rule, so it is the amount least likely
to trip a different one first. Where even that cannot fit the balance, the
request now says the rule is not isolable instead of asserting the wrong
reason.
