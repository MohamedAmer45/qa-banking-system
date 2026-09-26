# Requirements Traceability Matrix

Last synchronized: 2026-09-26
Source of truth: `requirements/requirements-catalog.md` — **187 requirements**

## Purpose

Link every requirement to the tests that cover it, and make uncovered
requirements visible. A requirement with no test is the thing this document
exists to surface.

## Identifier scheme

The catalog defines requirements as `<MODULE>-<NNN>`, for example `TRF-001`.
There is no `REQ-` prefix. Earlier versions of this matrix used one, and also
invented module codes the catalog never defined, so nothing in it resolved.
Both are corrected here.

Test cases use `<MODULE>-TC-<NNN>`. Where a test-case module code differs from
the catalog's, the mapping is explicit:

| Catalog | Test cases | Note |
|---|---|---|
| `ADMIN-` | `ADM-TC-` | Abbreviated in test cases |
| `NOTIF-` | `NOT-TC-` | Abbreviated in test cases |
| `KYC-` | `CUST-TC-` | KYC cases live in the customer file |
| `AUDIT-` | `ADM-TC-`, `SEC-TC-` | No dedicated audit file |
| `DASH-` | `DASH-TC-` | Dedicated file added 2026-09-23 |
| `DB-`, `SYS-` | — | Verified by SQL and API layers, not manual cases |
| — | `STMT-TC-` | Statements are catalogued under `ACC-` |
| — | `PROF-TC-` | Profile is catalogued under `KYC-` |

Any new module must be added to the catalog first. Test cases must not
introduce a module the catalog does not define — that is how the fabricated
deposits module (`DEP-TC-*`, ~5,500 lines for a feature with no requirement and
no implementation) entered the project and survived unnoticed.

## Coverage by module

| Module | Reqs | Scenarios | Test cases | Implemented | Manual coverage | Automated |
|---|---:|---|---|---|---|---|
| `AUTH` | 17 | `authentication-scenarios.md` | `authentication-test-cases.md` | Yes | Covered | **Automated** |
| `KYC` | 12 | `customer-scenarios.md` | `customer-test-cases.md`, `profile-settings-test-cases.md` | Yes | Covered | Automated |
| `ACC` | 12 | `account-scenarios.md`, `statement-scenarios.md` | `account-test-cases.md`, `statement-test-cases.md` | Yes | Covered | Automated |
| `BEN` | 9 | `beneficiary-scenarios.md` | `beneficiary-test-cases.md` | Yes | Covered | Automated |
| `TRF` | 22 | `transfer-scenarios.md` | `transfer-test-cases.md` | Yes | Covered | Automated |
| `PAY` | 10 | `payment-scenarios.md` | `payment-test-cases.md` | Yes | Covered | Automated |
| `CARD` | 10 | `card-scenarios.md` | `card-test-cases.md` | Yes | Covered | Automated |
| `LOAN` | 11 | `loan-scenarios.md` | `loan-test-cases.md` | Yes | Covered | Automated |
| `TXN` | 13 | `transaction-scenarios.md` | `transaction-test-cases.md` | Yes | Covered | Automated |
| `NOTIF` | 7 | `notification-scenarios.md` | `notification-test-cases.md` | Yes | Covered | Automated |
| `DASH` | 7 | `dashboard-scenarios.md` | `dashboard-test-cases.md` | Yes | Covered | **Automated** |
| `ADMIN` | 12 | `admin-scenarios.md` | `admin-test-cases.md` | Yes | Covered | Automated |
| `AUDIT` | 10 | `admin-scenarios.md` | `rest-assured/` | Yes | Covered | **Automated** |
| `SEC` | 10 | `security-scenarios.md` | `security-test-cases.md` | Yes | Covered | Automated |
| `DB` | 15 | — | `database-testing/` | Yes | Covered | **Automated** |
| `SYS` | 10 | — | `rest-assured/` | Yes | Covered | **Automated** |

"Automated" means at least one suite covers the module against the running
application. Which suite, and why that one, is in `docs/automation-status.md`.

Cucumber feature files tag the requirement ids they trace to, so a single
requirement can be run on its own — `mvn test -Dcucumber.filter.tags="@TRF-004"`.

**The Jest unit suite is deliberately not counted in that column.** It covers
functions, not modules, and it never starts the application, so counting it
would inflate this table against its own definition. It adds depth beneath
`SEC` (password hashing and token generation), `TRF` (FX conversion and
rounding), `LOAN` (amortisation), `ADMIN` (the role/permission grid asserted as
a matrix) and `DB` (the placeholder rewriter every query passes through). It
closes no module that is not already closed above, and it changes no number in
this matrix.

## Gaps

**None.** All 187 requirements across all 16 modules are covered.

`DASH` was the last one, and closing it turned up something worth recording.
The module was not untested because nobody had got to it: three of its seven
requirements — recent transactions, active cards and upcoming scheduled
payments — were never rendered, so there was nothing to write a test against.
It had nonetheless been marked "Implemented: Yes" here, which was wrong in the
flattering direction. That is `BUG-DASH-001`, now fixed, and the module has
scenarios, test cases and 13 Playwright tests.

The lesson is about what "covered incidentally" was worth. Other suites passed
through the overview constantly on their way somewhere else, and not one of
them could notice a panel that was absent.

`DB` (15) is closed by `database-testing/`, and `SYS` (10) and `AUDIT` (10) by
`rest-assured/`. Between them those two suites cover 35 requirements that no
UI test could reach.

`DB` was previously blocked because no persistent database existed, then the
largest single gap. It is now the only module covered by assertions against
stored state rather than API responses, and it found BUG-DB-001.

## Test data

Test cases reference generic identities (`CUST-001`, `ACC-001`, `ACC-003`).
These predate the seeded application and do not resolve on their own. The
mapping to real seed data:

| Case reference | Real identity |
|---|---|
| `CUST-001` | `customer@novabank.test` / `Demo123!` — KYC verified |
| `CUST-002` | `receiver@novabank.test` / `Demo123!` — transfer destination |
| `CUST-003` | `pending@novabank.test` / `Demo123!` — KYC under review |
| `ACC-001` | `1000000001` — CURRENT, EGP, daily limit 15,000,000 minor |
| `ACC-002` | `1000000002` — SAVINGS, EGP |
| `ACC-003` | `2000000001` — SAVINGS, USD (use for FX cases) |
| `ACC-004` | `1000000003` — CURRENT, EGP, owned by `CUST-002` |
| Admin | `admin@novabank.test` / `Admin123!` |
| Manager | `manager@novabank.test` / `Manager123!` |
| Read-only staff | `support@novabank.test`, `auditor@novabank.test`, `employee@novabank.test` |

MFA code is `123456` for every seeded user. Full list in
`docs/api/api-reference.md`.

## Conventions that affect expected results

Three application behaviours change what a test case should assert. Cases
written before the PostgreSQL port may contradict them:

1. **Ownership failures return `404`, not `403`.** The API does not confirm
   that another customer's resource exists. IDOR cases asserting `403` will
   fail against correct behaviour.
2. **Amounts are integer minor units.** `25000000` is 250,000.00 EGP. Assert in
   minor units; never compare floats.
3. **A failed transfer still creates a record.** `422` with status `FAILED` is
   correct. The assertion is that no balance moved, not that no row was written.

## Re-execution required

Every test case marked `Blocked` against MFA, beneficiaries, account creation,
extended account controls, statements, or database persistence must be
re-executed. All six are now implemented — see
`docs/known-issues-and-limitations.md`. A `Blocked` status that outlived its
cause reads as a product gap that no longer exists.

## Defects

None open.

| Defect | Requirement | Status |
|---|---|---|
| `BUG-BEN-001` | `BEN-007` — deleted beneficiary must not be usable | Closed, fixed 2026-09-23 |
| `BUG-DB-001` | `DB-006`, `DB-007` — financial precision | Closed, fixed 2026-09-21 |
| `BUG-API-001` | `SYS-002`, `SYS-008` — documented status for oversized bodies | Closed, fixed 2026-09-22 |
| `BUG-UI-002` | `SEC-003`, `ADMIN-002` — role boundaries in the interface | Closed, fixed 2026-09-23 |
| `BUG-DASH-001` | `DASH-003`, `DASH-004`, `DASH-005` — dashboard panels never rendered | Closed, fixed 2026-09-23 |
| `BUG-UI-001` | `SYS-*` — application stability | Closed, fixed 2026-09-21 |
| `BUG-AUTH-001` | `AUTH-012` — session validity | Closed, not reproducible after the port |
| `BUG-ACC-001` | `ACC-001` — account opening | Closed as obsolete; UI replaced |

## Maintenance

Update when a requirement is added, changed or removed; when a test case is
written; when automation lands; and when a defect is raised or closed.

Two rules keep this document honest:

- A module may not appear here unless `requirements/requirements-catalog.md`
  defines it.
- A coverage claim must name the file that provides it.
