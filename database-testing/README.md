# Database Suite

SQL-level coverage of the `DB` requirements: the fifteen that no API test can
observe, because they are about what is *stored* rather than what is returned.

59 tests. Every assertion reads the database directly.

## Running

```bash
DATABASE_URL=postgresql://user:pass@host/db mvn clean test
```

`DATABASE_URL` is the same connection string the application uses, so the suite
and the application can never end up on different databases. It has **no
default**: pointing this suite at the deployed database means supplying that
database's credentials, and those are deliberately not committed here. It is
the one setting in this project you always have to provide.

The HTTP base URL defaults to the deployed application (`https://novabank-banking-system.vercel.app`) like
every other suite. It is only needed by the cross-layer tests, which drive a
real operation before inspecting what it persisted. Override it with
`-Dapi.base.url=http://localhost:3000`.

Point both at the same place. A suite reading one database while driving an app
backed by another reports failures that are really just two different databases.

Start the application first, from the
[`novabank-banking-system`](https://github.com/MohamedAmer45/novabank-banking-system)
repository:

```bash
npm run db:reset && npm start
```

## What it covers

| Class | Requirements | Approach |
|---|---|---|
| `ConstraintTest` | DB-001–007, 013 | Reads `information_schema`. Asserts the *constraint exists*, not that current rows happen to satisfy it |
| `TransferPersistenceTest` | DB-008, 009, 011, 015 | Performs a real transfer, then reads the rows it wrote |
| `ConcurrencyTest` | DB-010 | Fires overlapping transfers at one account and checks for lost updates |
| `HistoryPreservationTest` | DB-012 | Deletes an entity and proves its financial history survived |
| `ReferentialIntegrityTest` | DB-003, 005, 013, 014 | Sweeps for orphans and impossible states |

## Why schema assertions beat data sampling

A uniqueness test that inspects existing rows proves nothing about the next
insert — it passes on an empty table. `ConstraintTest` asserts the `UNIQUE`
index is declared, which holds for every future write.

Applied to identity: `tableHasGeneratedPrimaryKey` checks `is_identity = YES`,
so the key is database-generated and the application cannot choose or collide
on it.

## Why the cross-layer tests exist

An endpoint reporting a correct balance proves the endpoint agrees with itself.
Only the ledger rows prove the money moved and was recorded. These tests drive
the API and then assert on storage:

- **DB-008** — a completed transfer writes a transfer row, a debit, and (for
  same-bank) a matching credit, with `balance_after_minor` equal to the
  account's actual balance.
- **DB-009 / DB-015** — a forced failure leaves the balance untouched and no
  ledger rows behind; a business rejection rolls back its own transfer row
  rather than persisting as an abandoned record.
- **DB-011** — a retried request yields exactly one stored transfer, one debit,
  and one debited amount. A separate test asserts the unique index exists,
  because idempotency enforced only in application code can be defeated by two
  concurrent requests.
- **DB-012** — a deleted beneficiary is retired, not removed, and the transfers
  naming it still resolve.

## Concurrency

`ConcurrencyTest` holds six requests at a `CyclicBarrier` so they overlap
rather than queue, then checks that the balance never went negative, that every
response was a clean accept or refusal, and that the ledger still reconciles.

This requirement was untestable in every previous build: SQLite serialised all
writers so contention never occurred, and the stub had no persistent state to
corrupt.

## Ordering

`testng-database.xml` runs single-threaded, schema first and integrity last.
Schema first because a wrong constraint makes the behavioural failures
consequences rather than separate findings; integrity last so the sweep also
proves the behavioural tests left nothing inconsistent.

## Testcontainers

Not used. It would give a disposable database per run, but it requires Docker,
which would stop the suite running on a machine without it. Reading
`DATABASE_URL` works against a local PostgreSQL, a hosted one, and the CI
service container alike. Testcontainers remains a reasonable future swap for
the schema tests, which need no application.
