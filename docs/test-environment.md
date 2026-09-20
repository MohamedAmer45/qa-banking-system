# Test Environment

Last synchronized: 2026-09-21

## Environments

| Environment | URL | Database | Reset |
|---|---|---|---|
| Production | `https://novabank-banking-system.vercel.app` | Neon PostgreSQL | `npm run db:reset` in the app repo |
| Local | `http://localhost:3000` | Local PostgreSQL or Neon | `npm run db:reset` |
| CI | `http://127.0.0.1:3000` | `postgres:16` service container | Fresh per run |

The application is hosted on Vercel serverless. It does not sleep, so no
environment wake-up step is required before a suite runs.

## Configuration

No suite may hard-code a URL. Each framework reads one variable:

| Framework | Variable | Fallback |
|---|---|---|
| Playwright | `BASE_URL` | `http://localhost:3000` |
| Cypress | `CYPRESS_BASE_URL` | `http://localhost:3000` |
| Selenium | `-Dbase.url` / `BASE_URL` | `config.properties` |
| Cucumber | `-Dbase.url` / `BASE_URL` | `config.properties` |
| API / DB | `API_BASE_URL`, `DATABASE_URL` | — |

## Running the application locally

```bash
git clone https://github.com/MohamedAmer45/novabank-banking-system
cd novabank-banking-system
npm install
cp .env.example .env          # set DATABASE_URL
npm run db:migrate && npm run db:seed
npm start
```

`docker compose up` brings up PostgreSQL and the application together.

## Preferred target

**Run suites against a locally started application, not production.** The
hosted environment has a single shared database, so any suite that moves money
changes state other suites observe. CI provisions its own PostgreSQL and
reseeds per run, which is the only way to make absolute-value assertions safe.

Against a shared environment, assert on deltas ("balance decreased by 100"),
never absolutes ("balance is 24,900,000").

## Database access

`DATABASE_URL` reaches the same data the API serves, so SQL assertions can run
alongside API and UI checks. Usable from JDBC, DBeaver, psql and CI.

Amounts are `BIGINT` minor units. Timestamps are ISO-8601 text, so string
comparison equals chronological comparison.

## Browsers

Chromium, Firefox and WebKit via Playwright; Chrome via Cypress and Selenium.
Headless in CI, headed locally.

## Credentials

Eight seeded users covering all six roles. MFA code `123456` for all of them.
Listed in `docs/api/api-reference.md`.

Real banking data is never used. Every environment is synthetic.
