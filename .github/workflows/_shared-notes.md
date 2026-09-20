# Workflow notes

All four UI/BDD workflows start the NovaBank application inside the runner via
`.github/actions/start-novabank`, against a `postgres:16` service container.
They no longer poll a hosted environment, so runs are isolated and every suite
begins from an identical seed.

## Why they are `workflow_dispatch` only

The page objects in all four suites select against the stub this project tested
until 2026-09-20. They compile, but they cannot pass against the real
application — the DOM and the login flow are both different.

Rather than leave `main` permanently red, the push and pull_request triggers
are commented out. Re-enable each one as its suite is retargeted; the
infrastructure above is already correct and needs no further change.

See `docs/automation-status.md` for the retarget plan.
