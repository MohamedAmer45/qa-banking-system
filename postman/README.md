# Postman Collection

Functional API coverage organised as **journeys** rather than endpoint-by-endpoint
checks. 37 requests, 152 assertions.

## Running

```bash
npm ci
npm run test              # local
npm run test:ci           # CI, with a JUnit report
npm run test:production   # read-only folders only
```

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
| `06 Back office` | 4 | Admin views, and the audit trail finding this run's own transfer |

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

`05` signs itself in through a folder-level pre-request when no session exists,
so running it alone actually works. It did not at first: three of its requests
needed the session folder `01` establishes, and running the folder standalone
against production returned `401` where `403`, `400` and `404` were expected.
The folder claimed to be independently runnable, so the fix was to make the
claim true rather than to document the dependency.

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
