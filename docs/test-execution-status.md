# Test Execution Status

Last synchronized: 2026-09-21

## Status

Nothing is blocked. Every documented module is implemented, and all four UI
suites are retargeted at the real application and passing.

| Layer | Executable | Result |
|---|---|---|
| Manual testing | Yes | Ready to execute |
| Playwright | Yes | 24 passing, 3 browsers |
| Cypress | Yes | 26 passing |
| Selenium | Yes | 21 passing |
| Cucumber | Yes | 21 scenarios passing |
| REST Assured | Yes | **107 passing** |
| Postman / Newman | Yes | **104 requests, 440 assertions passing** |
| Database (SQL) | Yes | **59 passing** |
| Performance (JMeter, k6) | Yes | Not yet written |

See `docs/automation-status.md` for how the four suites divide the work.

## Open defects

| Defect | Status |
|---|---|
| `BUG-BEN-001` | Open — deleted beneficiaries are still returned by the API |
| `BUG-UI-002` | Open — the back-office sidebar is not role-filtered |
| `BUG-UI-001` | Closed — fixed; found by Cypress |
| `BUG-DB-001` | Closed — fixed; FX and interest rates were binary floats, found by the database suite |
| `BUG-API-001` | Closed — fixed; oversized bodies dropped the connection instead of returning the documented 413 |
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
