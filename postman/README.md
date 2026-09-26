# Postman Collection

Functional API coverage organised as **journeys** rather than endpoint-by-endpoint
checks. 103 requests, 440 assertions.

Newman reports 104 requests executed rather than 103: it counts calls made by
`pm.sendRequest` from a script as well as the requests in the file, and folder
06 signs in from a pre-request to raise the fraud signal it then triages. The
same reason explains why a standalone folder run executes more requests than
it defines — its pre-request helper establishes the session it needs, which in
a full run is already there and so sends nothing.

## Running

```bash
npm ci
npm test                  # the deployed environment (default)
npm run test:local        # localhost:3000
npm run test:ci           # the app CI starts in the runner, with a JUnit report
npm run test:smoke        # deployed, read-only folders only
```

The default target is `https://novabank-banking-system.vercel.app`, so a fresh
clone runs with no local server.

`npm test` runs the whole collection, which **moves money** in the deployed
environment's shared database. That is intended — the assertions are written
as deltas rather than absolutes for this reason (`LIM-005`). Use
`npm run test:smoke` to check a deployment is alive without changing anything.

Or open `NovaBank.postman_collection.json` in Postman and pick an environment
from `environments/`.

Start the application first, from the
[`novabank-banking-system`](https://github.com/MohamedAmer45/novabank-banking-system)
repository:

```bash
npm run db:reset && npm start
```

## Folders

| Folder | Requests | What it demonstrates |
|---|---:|---|
| `00 Environment` | 1 | Target is reachable and is the PostgreSQL build |
| `01 Authentication` | 5 | Two-step handshake; a replayed challenge is refused |
| `02 Onboarding journey` | 9 | Register → verify → sign in → KYC, each step feeding the next |
| `03 Money movement` | 7 | Transfer with balance and ledger checked on both sides |
| `04 Idempotency` | 4 | One key, two submissions, one debit |
| `05 Error contract` | 7 | Every documented status code |
| `06 Back office` | 10 | Admin views, fraud triage, and the audit trail finding this run's own transfer |
| `07 Credential recovery` | 12 | forgot → reset → sign in → change |
| `08 Card lifecycle` | 10 | The card state machine, including the moves it refuses |
| `09 Account state transitions` | 12 | freeze/dormant/activate, each with a transfer attempted |
| `10 Currency and limits` | 7 | FX conversion reconciled against the published rate; the daily ceiling |
| `11 Lending and bills` | 18 | Loan apply → approve → disburse → repay; a bill paid and made recurring |

## Coverage

Between this collection and the other six suites, all 65 endpoints the
application serves are exercised by at least one of them.

Four behaviours were reachable but untested anywhere until folders 10 and 11:
cross-currency conversion, the daily transfer ceiling, the loan lifecycle, and
recurring bill payments. `BUG-DB-001` was about the precision of `fx_rate`, so
until folder 10 its regression check was a column type rather than an actual
conversion.

## Why this exists alongside REST Assured

They divide the work rather than duplicating it.

| | Postman | REST Assured |
|---|---|---|
| Chained journeys | Primary | Awkward |
| Authorization matrix | No | Primary (63 cases) |
| JSON schema contracts | No | Primary |
| Exploration by hand | Primary | No |
| Regression sweep | Secondary | Primary |

A collection is the better medium for a story whose steps depend on one
another, and the worse medium for a 63-cell permission grid. Each suite takes
the shape it is good at.

## Chaining

Every journey carries values forward with `pm.collectionVariables.set()`:

```
login          → challenge
mfa            → token, customerId
list accounts  → accountId, balanceBefore
transfer       → transferReference, transferFee
confirm debit  → asserts balanceBefore − balanceAfter === amount + fee
audit trail    → finds transferReference in the log
```

The last line is the one worth noting: folder 06 proves the audit trail
recorded *this run's* transfer, by reference. Asserting the trail is merely
non-empty would pass against a log that records nothing real.

## Environments

`local`, `ci` and `production` differ only in `baseUrl`. Passwords are marked
`secret` so Postman masks them.

Folders `00` and `05` are read-only and safe anywhere, including production.
Folders `02`, `03` and `04` create data and move money — point those at a local
or CI database.

Six folders carry a pre-request script and can be run on their own: `05`, `07`,
`08`, `09`, `10` and `11`. Each establishes the session — and where needed, the
beneficiary — that its requests depend on.

That independence was got wrong three separate times. Folders `05`, `09` and
`10` each shipped using a variable an earlier folder produced without resolving
it themselves, and each passed in a full run because the variable happened to be
set. Only running the folder in isolation exposed it: standalone, `05` returned
`401` where `403`/`400`/`404` were expected, and `09` and `10` got
`400 "destination or beneficiary required"` instead of the state and limit
refusals they were asserting.

The generator now refuses to build a collection where a folder claims
independence it does not have, so the mistake cannot be made quietly again.

## Collection-level assertions

Two run against every request:

- responds within 10 seconds
- never returns a 5xx

A `500` anywhere fails the run regardless of what the individual request was
checking.

## QA mode

With `QA_MODE=true` the API returns the MFA code, email verification code and
beneficiary OTP in its responses, so the journeys need no mail server. Requests
read `demoCode` when present and fall back to the environment's `mfaCode`
otherwise, so the collection still runs where those affordances are disabled.
