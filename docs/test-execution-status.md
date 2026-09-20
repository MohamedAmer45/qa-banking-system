# Test Execution Status

Last synchronized: 2026-09-21

## Status

All previously blocked functionality is now implemented, and the stub the
suites were written against has been deleted. That inverts the execution
picture: nothing is blocked by a missing feature, and nothing is currently
executable either, because the UI page objects target the old DOM.

| Layer | Executable | Reason |
|---|---|---|
| Manual testing | Yes | The application implements every documented module |
| Playwright | No | Page objects target the deleted stub |
| Cypress | No | Page objects target the deleted stub |
| Selenium | No | Page objects target the deleted stub |
| Cucumber | No | Step definitions and page objects target the deleted stub |
| API (Postman, REST Assured) | Yes | Not yet written, but unblocked |
| Database (SQL) | Yes | Not yet written, but unblocked |
| Performance (JMeter, k6) | Yes | Not yet written, but unblocked |

See `docs/automation-status.md` for the retarget plan.

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
