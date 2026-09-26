# NovaBank API Reference

Last verified: 2026-09-21, against the deployed build.

The authoritative specification lives with the application, in
`novabank-banking-system/docs/API.md`. This file records what the QA project
needs in order to drive it: base URLs, the session handshake, the conventions
the assertions depend on, and the QA-mode affordances.

## Environments

| Environment | Base URL |
|---|---|
| **Deployed (the default for every suite)** | `https://novabank-banking-system.vercel.app/api` |
| Local | `http://localhost:3000/api` |
| CI | `http://127.0.0.1:3000/api` (app started inside the runner) |

Never hard-code these. Every framework reads the base URL from configuration;
see `docs/test-environment.md`.

## Health

```http
GET /api/health
```

Unauthenticated. Returns `status`, `service`, `time`, `database`, `qaMode` and
the FX table. `"database": "postgresql"` confirms the build is on persistent
storage — a useful guard at the start of a suite.

## Session handshake

Login does **not** return a session token. It returns an MFA challenge, which
is exchanged for one. Every authenticated test needs both calls.

```http
POST /api/auth/login
Content-Type: application/json

{ "email": "customer@novabank.test", "password": "Demo123!" }
```

```json
{ "mfaRequired": true, "challenge": "<challenge>", "demoCode": "123456" }
```

```http
POST /api/auth/mfa
Content-Type: application/json

{ "challenge": "<challenge>", "code": "123456" }
```

```json
{ "token": "<session-token>", "user": { "id": 1, "email": "...", "role": "CUSTOMER" } }
```

Then `Authorization: Bearer <session-token>` on every authenticated request.

Notes that affect test design:

- The challenge is **single-use** and expires after **5 minutes**. A test that
  replays a challenge must expect `400`.
- `demoCode` is only present when `QA_MODE=true`. Do not assert on it in a
  suite that might run against a hardened environment.
- `"rememberDevice": true` on either call yields a 30-day session instead of 8
  hours.
- Sessions are opaque tokens in a `sessions` table, not JWTs. They can be
  revoked server-side, and `POST /api/auth/reset-password` deletes every
  session for that user.

## Status code conventions

| Code | Meaning |
|---|---|
| `400` | Malformed request, or a value outside its allowed range |
| `401` | Missing/expired session, wrong password, or wrong one-time code |
| `403` | Authenticated but not permitted — role, KYC state, or unverified email |
| `404` | Not found, **or owned by another customer** |
| `409` | Rejected by a business rule — insufficient funds, limit, or entity state |
| `413` | Request body over 1 MB |
| `422` | Transfer recorded but failed downstream; no account was debited |
| `423` | Account locked after 5 failed logins (15 minutes) |

Two of these matter more than the rest when writing authorization tests:

- **Ownership failures return `404`, not `403`.** The API deliberately refuses
  to confirm that another customer's resource exists. An IDOR test must assert
  `404`; asserting `403` will fail against correct behaviour.
- **`422` is not a rejection.** The transfer row exists with status `FAILED`.
  The assertion is that no balance moved, not that no record was written.

## Money and time representation

- **All amounts are integer minor units.** `balance_minor`, `amount_minor`,
  `fee_minor`, `daily_limit_minor`. 25000000 is 250,000.00 EGP. Request bodies
  take a major-unit `amount` (`12.34`); responses return minor units. Assert in
  minor units; never compare floats.
- **Timestamps are ISO-8601 strings**, so lexicographic order equals
  chronological order and `from`/`to` range filters can be built as strings.
- Currencies: `EGP`, `USD`, `EUR`, `GBP`. FX rates come from `/api/health`.

## Endpoints

55 routes. The full list is in the application's `docs/API.md`. Grouped:

| Group | Endpoints |
|---|---|
| Auth | register, verify-email, login, mfa, logout, forgot-password, reset-password, change-password, `GET /me`, `PATCH /profile` |
| KYC | `GET/POST /kyc`, `GET /kyc/document` |
| Accounts | list, open, `:id/freeze`, `:id/close`, `:id/transactions`, `:id/statement` |
| Beneficiaries | list, add, `PATCH :id`, `DELETE :id`, `:id/verify` |
| Transfers | `GET /transfers`, `POST /transfers` |
| Cards | list, request, `:id/{activate,freeze,unfreeze,replace,cancel,settings}` |
| Bills | `GET /billers`, `POST /billers/saved`, `GET/POST /bill-payments` |
| Loans | list, apply, `:id/pay` |
| Notifications | list, `:id/read` |
| Back office | dashboard, customers, kyc + review, accounts + actions, transfers + reverse, loans + review, fraud, audit, users + role |

Filters on `GET /accounts/:id/transactions`: `from`, `to`, `type`, `status`,
`reference`, `minAmount`, `maxAmount`.
`GET /accounts/:id/statement` additionally takes `format=csv`.

## Idempotency

`POST /api/transfers` accepts an idempotency key, either as a body field
(`idempotencyKey`) or the `Idempotency-Key` header. Reusing a key for the same
user returns the **original transfer unchanged** — same `reference`, same `id`,
no second debit.

This is verifiable end to end: post twice with one key, assert both responses
carry the same `reference` and that the balance moved once.

## Concurrency

Money movements take row locks, and accounts are locked in id order. Under
contention the guarantees are:

- the total debited never exceeds the available balance
- no balance goes negative
- losers are rejected with `409`, not silently dropped

Reference result: five simultaneous transfers of 20,000 against a balance of
80,000 settle as four `201` and one `409`, with a closing balance of exactly 0.

## Roles and permissions

| Role | Permissions |
|---|---|
| `CUSTOMER` | Own resources only |
| `SUPPORT` | Read customers, accounts, transfers |
| `AUDITOR` | SUPPORT + read audit and fraud |
| `EMPLOYEE` | SUPPORT + review KYC |
| `MANAGER` | All of the above + manage accounts/limits, reverse transfers, review loans, manage fraud |
| `ADMIN` | Everything, including user role changes |

Role separation is enforced server-side. A customer calling any `/api/admin/*`
endpoint receives `403`; an unauthenticated caller receives `401`.

## QA mode

With `QA_MODE=true` the API returns values a real bank would deliver out of
band, so no mail server is needed:

| Endpoint | Field | Purpose |
|---|---|---|
| `POST /auth/login` | `demoCode` | MFA code |
| `POST /auth/register` | `demoVerificationCode` | Email verification |
| `POST /auth/forgot-password` | `demoResetToken` | Password reset |
| `POST /beneficiaries` | `demoOtp` | Beneficiary verification |

Fault injection:

- `POST /api/transfers` with `"qaSimulation": "failure"` forces a safe failure:
  `422`, transfer status `FAILED`, no debit. This is how the "failed transfer
  does not move money" requirement is tested without breaking infrastructure.
- A beneficiary whose `bank_name` is `FAILBANK` fails the same way.
- Header `X-QA-Country: US` on `POST /auth/mfa` triggers the unusual-login
  fraud rule.

## Test accounts

MFA code is `123456` for every seeded user.

| Email | Password | Role | Purpose |
|---|---|---|---|
| `customer@novabank.test` | `Demo123!` | CUSTOMER | Primary. KYC verified, 3 accounts, card, loan awaiting review |
| `receiver@novabank.test` | `Demo123!` | CUSTOMER | Same-bank transfer destination; cross-customer IDOR target |
| `pending@novabank.test` | `Demo123!` | CUSTOMER | KYC under review — blocks account opening and loan application |
| `admin@novabank.test` | `Admin123!` | ADMIN | Full back office |
| `manager@novabank.test` | `Manager123!` | MANAGER | Reversals, limits, reviews |
| `support@novabank.test` | `Support123!` | SUPPORT | Read-only — negative authorization cases |
| `auditor@novabank.test` | `Auditor123!` | AUDITOR | Audit and fraud read access |
| `employee@novabank.test` | `Employee123!` | EMPLOYEE | KYC review only |

Seed state is restored by `npm run db:reset` in the application repository.

## Direct database access

The application runs on PostgreSQL, so SQL assertions run against the same
data the API serves. `DATABASE_URL` reaches it from JDBC, DBeaver, psql or a
CI service container.

The reconciliation invariant the application asserts, and which database tests
should reuse: every `accounts.balance_minor` equals the `balance_after_minor`
of that account's most recent `transactions` row.
