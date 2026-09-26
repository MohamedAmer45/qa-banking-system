# JMeter Performance Suite

Five load shapes over two plans. Four measure what the application costs to
serve under different traffic profiles; the fifth measures whether it stays
honest when many debits hit one balance at the same instant.

| Shape | What it does | Moves money | Gated |
|---|---|---|---|
| `load` | Steady paced traffic across six read endpoints | No | Yes |
| `stress` | Ramps well past expected load to find where it degrades | No | Observed |
| `spike` | Steady traffic, then a burst of simultaneous sign-ins | No | Observed |
| `endurance` | Modest load held long enough for drift to show | No | Yes |
| `concurrency` | Every thread debits the **same** account at once | Yes | Yes |

Two plans serve all five. `read-path-load.jmx` covers the first four — they differ
in load profile, not in script, and a second copy of the sign-in handshake per
shape would be four places to fix a change. `transfer-concurrency.jmx` is the
odd one out because it needs setUp and tearDown around the debits.

**Observed** means 5xx does not fail the run. A stress ramp that returned no
errors has not found the limit it went looking for, so failing on that would
punish the test for working. Correctness is gated in every shape regardless: the
ledger must reconcile whatever else happens.

The read shapes cover the six endpoints a customer session actually hits:
`/api/me`, `/api/accounts`, `/api/accounts/{id}/transactions`, `/api/transfers`,
`/api/bill-payments` and `/api/notifications`.

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

bash run.sh load          10 60      # threads, seconds
bash run.sh stress        150 180    # peak threads, seconds
bash run.sh spike         10 100     # baseline threads, burst threads
bash run.sh endurance     10 900     # threads, seconds
bash run.sh concurrency   20 50      # threads, amount in EGP

npm test                             # concurrency, the default
npm run test:load
```

Every run writes a raw `.jtl` and JMeter's own HTML dashboard to
`results/dashboard-<shape>/index.html` — the response-time-over-time and
throughput graphs that a percentile table cannot show.

`JMETER_HOST`, `JMETER_PORT` and `JMETER_PROTOCOL` override the target.

---

## What running this against a remote database looks like

Worth recording, because it is the clearest argument for the exception above.

Pointed at the deployed Neon database over the internet, the concurrency plan at
30 threads does not merely run slowly — it breaks in ways that have nothing to do
with the application:

| Pool size | Outcome |
|---|---|
| 10 (shipped default) | 17 x `201`, 13 x `500 {"error":"timeout exceeded when trying to connect"}` |
| 30 | 24 x `201`, 6 client-side `SocketTimeoutException` after 60s |

Each transfer holds a pooled connection for the whole transaction, and every
statement in it costs an internet round-trip, so connections are held for seconds.
Thirty of those serialising on one row lock exhausts a ten-connection pool, and
raising the pool just moves the failure to the client's own timeout.

**None of this is an application defect.** The same plan at the same concurrency
against a `postgres:16` container in the CI runner returns 30 x `201` with no 5xx,
because each transaction completes in milliseconds and the connection is handed
straight back. The ledger reconciled exactly in every one of these runs, including
the ones where requests timed out — correctness held throughout; only availability
degraded, and only because of where the database was.

That is the whole case for this suite not targeting the deployed environment: the
numbers it would produce there describe the distance to the database, and the
failures it would report would be the test's, not the application's.

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

**Reported every run:** error rate, throughput in requests per second, average,
p50, p90, p95, p99 and max — overall and per request label — plus latency broken
into quarters of the run. That last one is what makes the endurance and spike
shapes readable: an aggregate p95 averages a healthy beginning with a degraded
end and hides both. A soak that finishes slower than it started is the signature
of something accumulating, and a spike that recovers looks like this:

```text
Q1  avg=538   p95=1233 ms     baseline
Q2  avg=3166  p95=4824 ms     the burst arrives
Q3  avg=1991  p95=3914 ms     draining
Q4  avg=735   p95=1749 ms     recovered
```

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
as simultaneously as the client can make them. Outcomes verified in CI against a
`postgres:16` container, and locally against Neon:

| Where | Amount | Threads | 201 | 409 | Balance moved | Reconciles to |
|---|---|---:|---:|---:|---:|---|
| CI | 50 EGP | 30 | 30 | 0 | 165,000 | 30 x (5,000 + 500) |
| CI | 20,000 EGP | 30 | 7 | 23 | 14,014,000 | 7 x (2,000,000 + 2,000) |
| local | 7,890 EGP | 20 | 9 | 11 | 7,108,101 | 9 x (789,000 + 789) |
| local | 500,000 EGP | 20 | 0 | 20 | 0 | nothing succeeded, nothing moved |

The second row is the one that matters. Seven debits committed and twenty-three
were refused against a single balance, and the ledger came out exact to the minor
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
  read-path-load.jmx          Read path: load, stress, spike and endurance
                              shapes, plus a spike thread group that stays
                              dormant at zero threads
  transfer-concurrency.jmx    Simultaneous debits on one account
analyze.mjs                   Reads the .jtl, reports the metrics, enforces
                              the invariants
thresholds.json               What is gated, what is not, and why
run.sh                        Runs a shape, then the analyzer
results/                      .jtl, recorded balances, and the HTML
                              dashboard per shape (gitignored)
```
