# NovaBank Current Build Status

Last synchronized: 2026-09-21

## Live Environment

`https://novabank-banking-system.vercel.app`

Application source: [`novabank-banking-system`](https://github.com/MohamedAmer45/novabank-banking-system)
(a separate repository; this one holds only the QA project).

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
