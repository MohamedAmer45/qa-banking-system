# NovaBank Current Build Status

Last synchronized: 2026-09-26

## Live Environment

`https://novabank-banking-system.vercel.app`

Application source: [`novabank-banking-system`](https://github.com/MohamedAmer45/novabank-banking-system)
(a separate repository; this one holds only the QA project).

**Deployed and current as of 2026-09-27**, and the default target for every
suite except the two that must not touch it. See `docs/automation-status.md`.

Deployment is manual (`vercel --prod`): the Vercel project is CLI-linked rather
than connected to the GitHub repository, so pushing to `main` does not redeploy.
That is why this environment was four commits behind on 2026-09-23.

`vercel git connect` was attempted and is blocked on something only the account
owner can do:

```text
Error: You need to add a Login Connection to your GitHub account first. (400)
```

Vercel needs a GitHub login connection on the account before it can link a
repository, which is a browser authorization. Until that is done in
**vercel.com → Account Settings → Authentication**, the Git integration cannot
be used at all.

So the application repository takes the other route. A `Deploy` workflow runs
the Vercel CLI after **NovaBank CI** finishes green on `main`, which reaches the
same outcome without linking the two accounts — and is arguably the better
trigger, since the Git integration deploys on push and would not wait for the
suite that decides whether the ledger still reconciles.

Deploying is not the same as working: a missing environment variable, an
unreachable database or a broken `vercel.json` all deploy green and fail at
runtime. The workflow is not finished until four things hold on the new build.

| Check | What it would catch |
|---|---|
| `/api/health` serves JSON and reports `ok` | The function did not boot |
| A wrong password returns `401`, not `5xx` | The database is unreachable. Being *rejected* means the row was looked up, which a health endpoint returning a static object cannot tell you. A rejected login reads and writes nothing. |
| CSP, `nosniff`, frame-options and referrer-policy on the static shell | An edit to `vercel.json`, which no unit test can reach |
| `novabank-banking-system.vercel.app` serves *this* build | Promotion failed. The first three pass against the deployment URL even then, leaving the address everyone uses on the old build. |

The verification script was run against the live deployment before it was
committed, and against a host that is not it, to confirm it fails with the
right message rather than only passing.

It needs three repository secrets — `VERCEL_TOKEN`, `VERCEL_ORG_ID`,
`VERCEL_PROJECT_ID` — which only the account owner can create. Until they
exist the workflow skips with a notice naming the missing ones rather than
failing, so deployment stays manual and this environment can still fall behind
`main`.

A correction worth keeping, since it was recorded here as fact. That staleness
was first reported as reaching back to the 2026-09-22 `413` fix. It did not: the
check behind that claim sent 200KB of non-JSON to an endpoint that authenticates
before it reads the body, so the `400` it returned said nothing about the body
limit. A 2MB authenticated body returned `413` on the deployed build all along.
Only the four commits of 2026-09-23 were actually missing.

## What changed

Until 2026-09-20 this project tested a deterministic stub — a single static
HTML page holding its state in `localStorage`, served by a script that returned
hardcoded responses. It had no database, no real authentication, and no money
movement. `POST /api/transfers` returned a random UUID and the literal string
`"completed"` without touching an account.

That stub existed because the real application stored its data in SQLite on
Vercel's serverless filesystem, which is read-only and per-invocation, so every
write was lost. The application has now been ported to PostgreSQL (Neon) and
redeployed. The stub has been deleted.

## Authentication model

Real credentials with a mandatory MFA step. There are no "enter as customer"
demo buttons any more.

```
POST /api/auth/login  ->  { mfaRequired, challenge, demoCode }
POST /api/auth/mfa    ->  { token, user }
```

The challenge is single-use and expires after 5 minutes. The session token is
an opaque value in a `sessions` table and can be revoked server-side. Eight
seeded users cover every role; see `docs/api/api-reference.md`.

## Functional status

| Module | Status |
|---|---|
| Registration and email verification | Available |
| Login, logout, MFA, remember device | Available |
| Account lockout (5 attempts, 15 minutes) | Available |
| Forgot / reset / change password | Available |
| Customer profile | Available |
| KYC workflow with document upload and download | Available |
| Accounts — current/savings, EGP/USD/EUR/GBP | Available |
| Account states — active/frozen/dormant/closed | Available |
| Account opening and closure | Available |
| Beneficiaries — add, edit, delete, OTP verify | Available |
| Transfers — own account, same bank, external | Available |
| FX conversion | Available |
| Scheduled and recurring transfers | Available |
| Transfer idempotency | Available |
| Transfer reversal | Available |
| Transaction ledger and filtering | Available |
| Statements, including CSV export | Available |
| Cards — request, activate, freeze, replace, cancel, limits | Available |
| Bills — billers, saved billers, immediate/scheduled/recurring | Available |
| Loans — apply, review, disburse, repay | Available |
| Notifications | Available |
| Back office — dashboard, customers, KYC, accounts, transfers, loans, fraud, audit, users | Available |
| Role-based permissions (6 roles) | Available |
| Fraud rules — large transfer, velocity, repeated failure, unusual country | Available |
| Audit trail | Available |
| Persistent PostgreSQL storage | Available |

Nothing in the requirements catalog is blocked by a missing implementation any
more. The previous `GAP-001` through `GAP-008` are all closed; see
`docs/known-issues-and-limitations.md`.

## Persistence

PostgreSQL 14+, 17 tables. Amounts are `BIGINT` minor units; timestamps are
ISO-8601 text so lexicographic and chronological order agree.

`DATABASE_URL` gives JDBC, DBeaver, psql and CI direct access to the same data
the API serves, which unblocks the SQL and database-testing phase.

The application asserts one cross-layer invariant, and database tests should
reuse it: every `accounts.balance_minor` equals the `balance_after_minor` of
that account's most recent `transactions` row.

## Concurrency

Money movements take row locks, acquired in id order so reciprocal transfers
cannot deadlock. Under contention: the total debited never exceeds the
available balance, no balance goes negative, and losers are rejected with
`409`.

Reference result — five simultaneous transfers of 20,000 against a balance of
80,000: four `201`, one `409`, closing balance exactly 0.

## QA affordances

`QA_MODE=true` returns the MFA code, verification code, reset token and
beneficiary OTP in API responses, and accepts `qaSimulation: "failure"` on
transfers to force a safe, non-debiting failure. See
`docs/api/api-reference.md`.
