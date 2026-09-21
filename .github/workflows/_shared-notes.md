# Workflow notes

All four UI/BDD workflows start the NovaBank application inside the runner via
`.github/actions/start-novabank`, against a `postgres:16` service container.
They no longer poll a hosted environment, so runs are isolated and every suite
begins from an identical seed.

## Status

| Workflow | Triggers | Coverage |
|---|---|---|
| Playwright | push, PR, dispatch | 24 tests, 3 browsers |
| Cypress | push, PR, dispatch | 26 tests |
| Selenium | push, PR, dispatch | 21 tests |
| Cucumber | push, PR, dispatch | 21 scenarios |

All four are retargeted at the real application and passing.

## Why each suite exists

They are not four copies of the same coverage:

- **Playwright** — end-to-end money movement and cross-browser execution.
- **Cypress** — form validation, network contract assertions, and page health.
  It fails a test on any uncaught application exception, which is how
  `BUG-UI-001` was found.
- **Selenium** — enterprise-style Java POM with explicit waits. Because its
  waits are explicit rather than auto-retrying, it surfaced a stale-toast race
  the other two had papered over.
- **Cucumber** — business-readable acceptance scenarios, tagged with the
  requirement ids they trace to.

## Shared conventions

Every suite: resolves elements by `data-testid`, performs the two-step
credential + MFA sign-in, asserts money in integer minor units, and expresses
balance assertions as deltas rather than absolutes.
