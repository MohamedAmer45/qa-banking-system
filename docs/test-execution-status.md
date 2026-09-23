# Test Execution Status

Last synchronized: 2026-09-23

## Status

Nothing is blocked. Every documented module is implemented, and all eight
suites pass.

| Layer | Executable | Result |
|---|---|---|
| Manual testing | Yes | Ready to execute |
| Playwright | Yes | **37 passing**, 3 browsers |
| Cypress | Yes | 26 passing |
| Selenium | Yes | **27 passing** |
| Cucumber | Yes | **24 scenarios passing** |
| REST Assured | Yes | **113 passing** |
| Postman / Newman | Yes | **103 requests, 440 assertions passing** |
| Database (SQL) | Yes | **59 passing** |
| Jest (unit, app repo) | Yes | **134 passing** |
| Performance (JMeter, k6) | Yes | Not yet written |

Seven of the eight drive the running application. Jest is the exception: it
imports application internals directly, so it lives in the application
repository and runs with no server and no database.

See `docs/automation-status.md` for how the suites divide the work.

## Open defects

**None.** Every recorded defect is closed, each with regression cover in the
suite that found it.

| Defect | Status |
|---|---|
| `BUG-UI-001` | Closed — fixed; found by Cypress |
| `BUG-DB-001` | Closed — fixed; FX and interest rates were binary floats, found by the database suite |
| `BUG-API-001` | Closed — fixed; oversized bodies dropped the connection instead of returning the documented 413 |
| `BUG-BEN-001` | Closed — fixed 2026-09-23; the beneficiary list returned soft-deleted rows |
| `BUG-UI-002` | Closed — fixed 2026-09-23; the back-office sidebar was not role-filtered |
| `BUG-DASH-001` | Closed — fixed 2026-09-23; three dashboard requirements were never rendered |
| `BUG-AUTH-001` | Closed — not reproducible after the PostgreSQL port |
| `BUG-ACC-001` | Closed — obsolete; the UI it described no longer exists |

## Previously blocked areas

| Area | Previous reason | Now |
|---|---|---|
| MFA | Not exposed | Implemented — mandatory challenge step |
| Beneficiaries | Module not exposed | Implemented |
| Account creation | Not exposed | Implemented — requires verified KYC |
| Extended account controls | Not exposed | Implemented |
| Dedicated statements | Not exposed | Implemented, with CSV export |
| SQL persistence verification | No persistent database | Implemented — PostgreSQL |

Test cases marked `Blocked` for these reasons must be re-executed and given a
real result. A `Blocked` status that outlived its cause is worse than no
status, because it reads as a product gap that no longer exists.

## Result classification

`Passed`, `Failed`, `Blocked`, `Not Run`.

`Blocked` means the test cannot run, and the reason must name what is missing.
Absent functionality is never recorded as `Passed`.
