# Workflow notes

Every workflow starts the NovaBank application inside the runner via
`.github/actions/start-novabank`, against a `postgres:16` service container.
None polls a hosted environment, so runs are isolated and every suite begins
from an identical seed.

## Status

| Workflow | File | Triggers | Coverage |
|---|---|---|---|
| Playwright | `playwright.yml` | push, PR, dispatch | 24 tests, 3 browsers |
| Cypress | `cypress.yml` | push, PR, dispatch | 26 tests |
| Selenium | `selenium.yml` | push, PR, dispatch | 21 tests |
| Cucumber | `cucumber.yml` | push, PR, dispatch | 21 scenarios |
| Database | `database.yml` | push, PR, dispatch | 59 tests |
| REST Assured | `api.yml` | push, PR, dispatch | 107 tests |
| Postman | `postman.yml` | push, PR, dispatch | 103 requests, 440 assertions |

All seven pass against the running application.

Each workflow's `paths` filter includes `.github/actions/start-novabank/**`, so
a change to the shared action runs every suite that depends on it rather than
shipping untested.

## Why each suite exists

They are not seven copies of the same coverage:

- **Playwright** — end-to-end money movement and cross-browser execution.
- **Cypress** — form validation, network contract assertions, and page health.
  It fails a test on any uncaught application exception, which is how
  `BUG-UI-001` was found.
- **Selenium** — enterprise-style Java POM with explicit waits. Because its
  waits are explicit rather than auto-retrying, it surfaced a stale-toast race
  the others had papered over, and found `BUG-UI-002`.
- **Cucumber** — business-readable acceptance scenarios, tagged with the
  requirement ids they trace to.
- **Database** — assertions against stored state rather than API responses:
  schema constraints, referential integrity, rollback, and concurrency. Found
  `BUG-DB-001` by reading `information_schema`, which no API test could see.
- **REST Assured** — the contract layer: response schemas, the authorization
  grid across all six roles, and payload hygiene. Found `BUG-API-001`.
- **Postman** — chained journeys, where each step consumes what the previous
  one returned. The shape a collection is good at and the others are not.

## Shared conventions

Every suite: resolves elements by `data-testid`, performs the two-step
credential and MFA sign-in, asserts money in integer minor units, and expresses
balance assertions as deltas rather than absolutes.
