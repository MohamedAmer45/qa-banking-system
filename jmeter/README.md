# JMeter Performance Suite

Two plans. One measures what the application costs to read under concurrency;
the other measures whether it stays honest when many debits hit one balance at
the same instant.

| Plan | What it does | Moves money |
|---|---|---|
| `plans/read-path-load.jmx` | Each thread signs in once, then loops `/api/me`, `/api/accounts`, `/api/transfers`, `/api/notifications` | No |
| `plans/transfer-concurrency.jmx` | Every thread debits the **same** account simultaneously | Yes |

---

## This suite is the exception: it does not target the deployed environment

Every other suite in this repository defaults to
`https://novabank-banking-system.vercel.app`. This one defaults to
`127.0.0.1:3000` and CI runs it against an application started inside the runner,
backed by a throwaway `postgres:16` container.

Three reasons, and they are not stylistic:

- **Load testing a shared database corrupts the measurement for everyone.** The
  concurrency plan exists to exhaust an account's balance and daily allowance.
  Doing that to the deployed environment would drain the seeded data every other
  suite reads.
- **The numbers would not be about the application.** A local run reaches Neon
  over the internet, so one transfer costs a few hundred milliseconds before the
  application does any work. Twenty concurrent debits serialising on row locks
  over that link measured p95 near 37 seconds — a figure about network latency
  and a free-tier database, not about this code.
- **Deliberately hammering someone else's hosted service is not a test.**

`docs/test-environment.md` records the exception alongside the rule.

---

## Running

Needs Java 21 and JMeter 5.6.3
([download](https://jmeter.apache.org/download_jmeter.cgi)), with `JMETER_HOME`
set or `jmeter` on `PATH`.

Start the application first, from
[`novabank-banking-system`](https://github.com/MohamedAmer45/novabank-banking-system):

```bash
npm run db:reset && npm start
```

Then:

```bash
export JMETER_HOME=/path/to/apache-jmeter-5.6.3

npm run test:read                    # 10 threads, 60s
npm run test:concurrency             # 20 threads, 50 EGP each

bash run.sh read 20 120              # threads, duration
bash run.sh concurrency 30 5000      # threads, amount in EGP
```

`JMETER_HOST`, `JMETER_PORT` and `JMETER_PROTOCOL` override the target.

---

## Pacing, and why the first CI run failed

The read plan paces each request with a think time of 200-500ms. That is not
cosmetic, and it was not there at first.

Without pacing a thread loops as fast as the server answers, so **the load the
plan generates is set by how fast the system under test is.** Run locally against
a database across the internet, each request cost around 460ms and twenty threads
offered about 20 requests a second. The identical plan in CI, against a
`postgres:16` container in the same runner, answered in milliseconds and so
offered orders of magnitude more — enough to exhaust the application's
ten-connection pool and return 5xx. The gate caught it and failed the build.

That failure was real, but it was a property of the test, not of the
application: a load test whose intensity depends on the backend's speed cannot be
compared between environments and will fail intermittently in the faster one. It
passed on a re-run, which is the signature worth distrusting.

With a think time the offered rate is `threads / think time`, set by the plan
rather than discovered by it.

## What is gated, and what is only reported

JMeter's non-GUI mode exits 0 whether or not the numbers were acceptable, so on
its own it reports rather than gates. `analyze.mjs` reads the `.jtl` and decides.

**Enforced — a failure here fails the build:**

- no sample returns 5xx
- every concurrent debit answers `201` or `409`, never anything else
- the balance falls by **exactly** the amount plus fee of every success
- if nothing succeeded, nothing moved

The fee rule is mirrored from `src/banking.js`
(`max(500, amount × 0.001)` on an external transfer), so the expected debit is
known exactly rather than bounded. That is what makes the reconciliation an
equality instead of an inequality.

**Reported, not enforced: latency.** There is no performance requirement in
`requirements/requirements-catalog.md` to enforce, and inventing a p95 target
here would put an SLA in the repository that no requirement asked for. The
figures are also dominated by where the database is rather than by the
application, so a threshold that passes locally would fail in CI or the reverse.
`thresholds.json` spells this out, including the order to fix it in: baseline
from CI, then add `PERF` requirements to the catalog, then enforce them here.

---

## What the concurrency plan actually proves

All threads are released together by a Synchronizing Timer, so the debits arrive
as simultaneously as the client can make them. Three outcomes, all verified
locally against a real PostgreSQL:

| Amount | Threads | 201 | 409 | Balance moved | Reconciles |
|---|---:|---:|---:|---:|---|
| 50 EGP | 20 | 20 | 0 | 110,000 | 20 × (5,000 + 500) |
| 7,890 EGP | 20 | 9 | 11 | 7,108,101 | 9 × (789,000 + 789) |
| 500,000 EGP | 20 | 0 | 20 | 0 | nothing succeeded, nothing moved |

The middle row is the one that matters. Nine debits committed and eleven were
refused against a single balance, and the ledger came out exact to the minor
unit. That is the claim `lockAccounts` makes in `src/banking.js`, tested at a
scale the database suite's `ConcurrencyTest` cannot reach.

A run where every debit succeeds has not shown that refusal works, so
`analyze.mjs` says so in its output and CI runs the plan twice — once sized to
fit and once sized to exhaust.

---

## Traceability

`DB-010` — *concurrent financial operations shall maintain correct balances.*

This suite introduces no new requirement. `DB-010` is already covered by the
database suite; this exercises the same rule at higher concurrency and through
the API rather than through SQL. No module is added to the catalog, because
[the matrix](../manual-testing/traceability/requirements-traceability-matrix.md)
is explicit that test material must not invent one.

---

## Layout

```
plans/
  read-path-load.jmx          Read path under concurrent sessions
  transfer-concurrency.jmx    Simultaneous debits on one account
analyze.mjs                   Reads the .jtl and enforces the invariants
thresholds.json               What is gated, what is not, and why
run.sh                        Runs a plan, then the analyzer
results/                      .jtl output and recorded balances (gitignored)
```
