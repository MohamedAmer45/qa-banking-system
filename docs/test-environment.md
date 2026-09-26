# Test Environment

Last synchronized: 2026-09-23

## Environments

| Environment | URL | Database | Reset |
|---|---|---|---|
| **Deployed (default)** | `https://novabank-banking-system.vercel.app` | Neon PostgreSQL | `npm run db:reset` in the app repo |
| Local | `http://localhost:3000` | Local PostgreSQL or Neon | `npm run db:reset` |
| CI | `http://127.0.0.1:3000` | `postgres:16` service container | Fresh per run |

**Every suite targets the deployed environment by default.** A fresh clone runs
without installing PostgreSQL, seeding a database or starting a server. Point a
suite at localhost with the variable in the table below.

The application is hosted on Vercel serverless. It does not sleep, so no
environment wake-up step is required before a suite runs.

### What the default costs you

The deployed environment has **one** database, and it is not reset between runs.
Two consequences, both of which the suites are already written for:

- Tests that move money mutate shared state. Balance assertions are relative
  ("decreased by at least N"), never absolute. This is `LIM-005`.
- Two people running a suite at the same time can interfere with each other.

CI therefore keeps its own throwaway database — it starts the application inside
the runner against a `postgres:16` container and overrides the URL. That is the
environment to trust for a clean, repeatable result; the deployed default is for
convenience and demonstration.

## Configuration

No suite may hard-code a URL. Each framework reads one variable:

| Framework | Variable | Default |
|---|---|---|
| Playwright | `BASE_URL` | deployed |
| Cypress | `CYPRESS_BASE_URL` | deployed |
| Selenium | `-Dbase.url` / `BASE_URL` | deployed, via `config.properties` |
| Cucumber | `-Dbase.url` / `BASE_URL` | deployed, via `config.properties` |
| REST Assured | `-Dapi.base.url` / `BASE_URL` | deployed, via `config.properties` |
| Database | `-Dapi.base.url` / `BASE_URL` | deployed, via `config.properties` |
| Database connection | `-Ddatabase.url` / `DATABASE_URL` | **none — must be supplied** |

To run any suite against localhost instead:

```bash
BASE_URL=http://localhost:3000 npm test              # Playwright
CYPRESS_BASE_URL=http://localhost:3000 npm test      # Cypress
mvn test -Dbase.url=http://localhost:3000            # Selenium, Cucumber
mvn test -Dapi.base.url=http://localhost:3000        # REST Assured, Database
npm run test:local                                   # Postman / Newman
```

### The one exception

The database suite needs a PostgreSQL connection string, not an HTTP URL, and
there is no default for it. Pointing it at the deployed database means supplying
that database's credentials, which are deliberately not committed to this
repository:

```bash
DATABASE_URL='postgresql://…' mvn clean test
```

Its `api.base.url` defaults to the deployed app like every other suite; only the
connection string has to come from you.

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
