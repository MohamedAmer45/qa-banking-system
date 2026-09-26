# QA Banking System Testing Project

[![Selenium Tests](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/selenium.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/selenium.yml)
[![Cypress Tests](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/cypress.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/cypress.yml)
[![Playwright Tests](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/playwright.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/playwright.yml)
[![Cucumber BDD Tests](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/cucumber.yml/badge.svg?branch=main)](https://github.com/MohamedAmer45/qa-banking-system/actions/workflows/cucumber.yml)

## Overview

This repository contains a complete Quality Assurance project for a fully functional banking system.

The project is designed to demonstrate end-to-end QA engineering across:

- Requirements analysis
- Test planning
- Manual testing
- UI automation
- API testing
- Backend/unit testing
- Database testing
- Performance testing
- BDD
- CI/CD
- Reporting
- Risk-based testing

The banking system includes realistic customer, employee, and administrator workflows with a strong focus on financial integrity, authorization, concurrency, and data consistency.

---

## Running the suites

Every suite targets the deployed application by default:

**https://novabank-banking-system.vercel.app**

A fresh clone runs without installing PostgreSQL, seeding a database or
starting a server.

| Suite | Command | From |
|---|---|---|
| Playwright | `npm ci && npx playwright install --with-deps && npm test` | `playwright/` |
| Cypress | `npm ci && npm run validate` | `cypress/` |
| Selenium | `mvn clean test` | `selenium/` |
| Cucumber | `mvn clean test` | `cucumber/` |
| REST Assured | `mvn clean test` | `rest-assured/` |
| Postman / Newman | `npm ci && npm test` | `postman/` |
| Database | `DATABASE_URL=… mvn clean test` | `database-testing/` |
| Jest (unit) | `npm test` | the application repository |
| JMeter (performance) | `JMETER_HOME=… npm test` | `jmeter/` |

Two suites are different. The database suite needs a connection string rather
than a URL, because it reads SQL directly, and the deployed database's
credentials are deliberately not committed here. And **JMeter deliberately does
not target the deployed environment** — its concurrency plan exhausts an
account's balance on purpose, which would drain the data every other suite
reads, so it runs against a local or CI-started application. See
[`jmeter/README.md`](jmeter/README.md).

### Running against localhost

Each suite takes one override:

```bash
BASE_URL=http://localhost:3000 npm test              # Playwright
CYPRESS_BASE_URL=http://localhost:3000 npm run validate   # Cypress
mvn clean test -Dbase.url=http://localhost:3000      # Selenium, Cucumber
mvn clean test -Dapi.base.url=http://localhost:3000  # REST Assured, Database
npm run test:local                                   # Postman / Newman
```

### Which target to trust

The deployed environment has **one** database and it is not reset between runs.
Suites that move money mutate it, and two people running at once can interfere.
The assertions are written for this — balances are compared as deltas, never as
absolutes (`LIM-005` in
[`docs/known-issues-and-limitations.md`](docs/known-issues-and-limitations.md)).

CI does not use the deployed environment. Each workflow starts the application
inside the runner against a throwaway `postgres:16` container, so every run
begins from an identical seed and no run can see another's state. **That is the
target to trust for a clean result.** The deployed default is for convenience
and demonstration.

---

# Project Goals

The main goals of this project are to:

- Build a realistic banking application suitable for professional QA testing.
- Define clear requirements and business rules before test execution.
- Design comprehensive manual test coverage.
- Automate critical workflows using multiple frameworks.
- Validate APIs independently from the UI.
- Validate database integrity and financial consistency.
- Test concurrency and duplicate transaction protection.
- Perform performance testing using JMeter.
- Implement BDD using Cucumber.
- Integrate test execution into GitHub Actions and Jenkins.
- Generate professional test reports.
- Demonstrate a complete QA lifecycle suitable for a portfolio.

---

# Banking Modules

The system includes the following major modules:

```text
Authentication
Registration
KYC
Dashboard
Bank Accounts
Beneficiaries
Transfers
Payments
Cards
Transaction History
Loans
Notifications
Security Settings
Administration
Audit Logging
Database Integrity
```

---

# User Roles

The application supports:

```text
CUSTOMER
BANK_EMPLOYEE
ADMIN
```

Each role has separate permissions and authorization boundaries.

Detailed permissions are documented in:

```text
requirements/roles-and-permissions.md
```

---

# Technology Stack

## UI Automation

```text
Selenium
Java
TestNG
Maven

Cypress
TypeScript

Playwright
TypeScript
```

---

## Backend / Unit Testing

```text
Jest
```

---

## API Testing

```text
Postman
REST Assured
```

---

## Database Testing

```text
SQL
Direct database validation
Automated database verification
```

The exact database technology and supporting tools will be finalized when the application implementation is created.

---

## Performance Testing

```text
Apache JMeter
```

---

## BDD

```text
Cucumber
Gherkin
```

---

## CI/CD

```text
GitHub Actions
Jenkins
```

---

## Reporting

The project may use:

```text
Allure
Playwright HTML Reporter
Cypress Reports
JUnit XML
Maven Surefire
JMeter HTML Dashboard
GitHub Actions Artifacts
Jenkins Reports
```

---

# Testing Types

The project will include:

```text
Functional Testing
Manual Testing
Exploratory Testing
Smoke Testing
Sanity Testing
Regression Testing
Positive Testing
Negative Testing
Boundary Value Testing
Equivalence Partitioning
State Transition Testing
Decision Table Testing
End-to-End Testing
API Testing
Database Testing
Authorization Testing
Security-Oriented Functional Testing
Concurrency Testing
Idempotency Testing
Performance Testing
Load Testing
Stress Testing
Spike Testing
Endurance Testing
Cross-Browser Testing
BDD Testing
```

---

# Repository Structure

```text
qa-banking-system/
│
├── requirements/
│   ├── requirements-catalog.md
│   ├── business-rules.md
│   ├── roles-and-permissions.md
│   └── test-data-requirements.md
│
├── test-planning/
│   ├── test-plan.md
│   ├── test-strategy.md
│   └── risk-analysis.md
│
├── manual-testing/
│   ├── test-scenarios/
│   ├── test-cases/
│   └── defect-reports/
│
├── selenium/
├── cypress/
├── playwright/
├── postman/
├── rest-assured/
├── database-testing/
├── cucumber/
├── jmeter/
│
├── docs/
│   ├── automation-status.md
│   ├── test-execution-status.md
│   ├── current-build-status.md
│   ├── known-issues-and-limitations.md
│   ├── test-environment.md
│   ├── api/
│   └── defects/
│
├── .github/
│   ├── actions/
│   │   └── start-novabank/
│   └── workflows/
│
├── .gitignore
└── README.md
```

The Jest unit suite is not in this tree. It lives in the
[application repository](https://github.com/MohamedAmer45/novabank-banking-system)
under `tests/`, because it imports application internals directly rather than
driving the running application over HTTP. Every other suite here talks to the
application the way a client does, which is why they can live apart from it.

A Jenkins pipeline is planned and not yet written; see
[`docs/automation-status.md`](docs/automation-status.md) for what is and is not
built.

---

# Requirements Documentation

The requirements folder contains the specification used as the source of truth for the project.

## Requirements Catalog

```text
requirements/requirements-catalog.md
```

Contains functional requirements for all banking modules.

Example requirement IDs:

```text
AUTH-001
ACC-001
TRF-001
PAY-001
CARD-001
LOAN-001
ADMIN-001
DB-001
```

---

## Business Rules

```text
requirements/business-rules.md
```

Contains detailed rules such as:

```text
Login lockout
OTP expiration
Password requirements
Transfer limits
Account states
Financial precision
Transaction atomicity
Idempotency
Concurrency
Database integrity
Authorization
```

---

## Roles and Permissions

```text
requirements/roles-and-permissions.md
```

Defines permissions for:

```text
CUSTOMER
BANK_EMPLOYEE
ADMIN
```

including:

```text
Horizontal authorization
Vertical authorization
Resource ownership
API authorization
Administrative access
Privilege escalation prevention
```

---

## Test Data Requirements

```text
requirements/test-data-requirements.md
```

Defines reusable test users, accounts, cards, beneficiaries, balances, loans, transactions, and edge-case datasets.

---

# Test Planning

The test-planning folder contains the overall QA approach.

## Test Plan

```text
test-planning/test-plan.md
```

Defines:

```text
Scope
Objectives
Test levels
Test types
Entry criteria
Exit criteria
Defect handling
Test deliverables
Reporting
CI/CD approach
```

---

## Test Strategy

```text
test-planning/test-strategy.md
```

Defines how testing will be executed using the selected technologies.

---

## Risk Analysis

```text
test-planning/risk-analysis.md
```

Defines risk-based testing priorities.

Critical risk areas include:

```text
Financial integrity
Transfers
Authorization
Balances
Concurrency
Idempotency
Database integrity
Performance
```

---

# Requirement Traceability

The project uses traceable identifiers.

Example:

```text
Requirement
TRF-005

↓

Business Rule
BR-TRF-003

↓

Test Scenario
TS-TRF-005

↓

Test Case
TC-TRF-005

↓

Automation / API / Database Validation

↓

Execution Result

↓

Defect if applicable
```

This makes it possible to trace tests back to the original requirement.

---

# Financial Integrity

Financial behavior is treated as the highest-risk area of the project.

Critical operations must verify:

```text
Correct debit
Correct credit
Correct fee
Correct balance
Unique transaction reference
Correct transaction status
No duplicate transaction
No partial transaction
Correct database state
Correct audit record
```

---

# Transfer Limits

Initial test configuration:

```text
Minimum transfer amount: 0.01 EGP
Maximum single transfer: 100,000 EGP
Daily transfer limit: 250,000 EGP
Overdraft: Disabled
```

These values are intended to be configurable.

---

# Authentication Configuration

Initial testing configuration:

```text
Failed login attempts: 5
Account lock duration: 15 minutes
OTP length: 6 digits
OTP expiration: 5 minutes
OTP maximum attempts: 5
Session inactivity timeout: 15 minutes
Password reset expiration: 30 minutes
Minimum password length: 12 characters
Maximum password length: 128 characters
```

---

# Automation Strategy

The automation frameworks will intentionally have different responsibilities.

## Selenium

Primary focus:

```text
Java-based UI automation
Page Object Model
Enterprise-style test framework
Core banking workflows
Cross-browser testing
```

---

## Playwright

Primary focus:

```text
Modern E2E testing
Parallel execution
Multi-browser testing
Authentication state reuse
Advanced workflows
Network-aware testing
```

---

## Cypress

Primary focus:

```text
Frontend testing
Form validation
Network request validation
API-assisted setup
Selected UI regression
```

---

## Jest

Primary focus:

```text
Backend logic
Business rules
Financial calculation logic
Validation utilities
```

---

## Postman

Primary focus:

```text
Manual API exploration
Collections
Request chaining
Environment variables
Functional API testing
```

---

## REST Assured

Primary focus:

```text
Java API automation
Regression testing
Schema validation
Authorization testing
Reusable API framework
```

---

## JMeter

Primary focus:

```text
Load testing
Stress testing
Spike testing
Endurance testing
Concurrency testing
```

---

## Cucumber

Primary focus:

```text
BDD
Business-readable scenarios
Acceptance testing
Requirement traceability
```

---

# Database Testing

Database testing will verify:

```text
Primary keys
Foreign keys
Unique constraints
NOT NULL constraints
Financial precision
Transaction persistence
Balance consistency
Rollback behavior
Concurrency
Audit records
Data ownership
```

Critical financial workflows will be verified across:

```text
UI
API
Database
Audit
```

---

# Authorization Testing

The project will include extensive access-control testing.

Coverage includes:

```text
Unauthenticated access
Horizontal privilege escalation
Vertical privilege escalation
Direct URL access
Direct API access
Resource ID manipulation
Role manipulation
Cross-customer access
Cross-role access
```

Example:

```text
Customer A
attempts to access
Customer B account

Expected:
Access denied
```

---

# Concurrency Testing

Concurrency testing is especially important for financial operations.

Example:

```text
Starting balance = 1,000 EGP

Transfer A = 800 EGP
Transfer B = 800 EGP
```

Expected:

```text
Both transfers must not succeed.

The final balance must remain valid.
```

---

# Idempotency Testing

Duplicate transaction protection will be validated.

Example:

```text
POST /api/transfers
Idempotency-Key: TEST-123
```

Submitting the same operation repeatedly with the same valid idempotency key must not create duplicate transfers.

---

# Performance Testing

JMeter will be used to test:

```text
Login
Dashboard
Accounts
Transaction History
Transfers
Payments
```

Performance metrics include:

```text
Average response time
P90
P95
P99
Throughput
Requests per second
Error rate
Concurrent users
```

---

# Test Environments

The project may use:

```text
LOCAL
TEST
CI
PERFORMANCE
```

All environments shall use synthetic data.

Production banking data shall never be used.

---

# Test Data

Reusable test identities will include:

```text
Standard customer
Pending-KYC customer
Rejected-KYC customer
Locked customer
Frozen-account customer
Low-balance customer
Zero-balance customer
High-balance customer
Multiple-account customer
Standard employee
KYC reviewer
Loan reviewer
Restricted employee
Administrator
```

---

# Security and Secrets

Secrets must not be committed to GitHub.

Examples:

```text
Passwords
Database credentials
Tokens
API secrets
Private keys
OTP secrets
```

Environment-specific secrets shall be managed through environment variables or CI/CD secret storage.

---

# Environment Variables

Potential configuration:

```text
BASE_URL
API_BASE_URL

CUSTOMER_USERNAME
CUSTOMER_PASSWORD

EMPLOYEE_USERNAME
EMPLOYEE_PASSWORD

ADMIN_USERNAME
ADMIN_PASSWORD

DB_HOST
DB_PORT
DB_NAME
DB_USERNAME
DB_PASSWORD
```

A safe:

```text
.env.example
```

may eventually be committed.

The real:

```text
.env
```

must remain ignored.

---

# CI/CD Strategy

CI execution will provide fast feedback first.

Conceptual pipeline:

```text
Checkout
↓
Install Dependencies
↓
Build
↓
Start Test Environment
↓
Run Database Migrations
↓
Seed Test Data
↓
Run Jest Tests
↓
Run API Tests
↓
Run UI Smoke Tests
↓
Run Regression Tests
↓
Generate Reports
↓
Publish Artifacts
```

---

# GitHub Actions

Each UI/BDD suite has a workflow that starts the NovaBank application inside
the runner, against a `postgres:16` service container, using the shared
composite action in `.github/actions/start-novabank`.

| Workflow | Runs |
|---|---|
| [Selenium](.github/workflows/selenium.yml) | Java 21, Maven, TestNG, headless Chrome |
| [Cypress](.github/workflows/cypress.yml) | Node 22, TypeScript validation, Chrome |
| [Playwright](.github/workflows/playwright.yml) | Chromium, Firefox and WebKit matrix |
| [Cucumber](.github/workflows/cucumber.yml) | Java 21, Maven, TestNG, headless Chrome |

CI no longer polls a hosted environment. Every run migrates and seeds its own
database, so suites start from identical state and never observe data left
behind by another run.

All four are currently `workflow_dispatch` only — see **Current status** below.

---

# Current status

## Application

The application under test is
[NovaBank](https://github.com/MohamedAmer45/novabank-banking-system), a
separate repository, deployed at
`https://novabank-banking-system.vercel.app`.

It runs on PostgreSQL and implements every module in the requirements catalog:
authentication with MFA, KYC, accounts, beneficiaries, transfers with FX and
idempotency, cards, bills, loans, transactions, statements, notifications, the
back office, RBAC across six roles, fraud rules and an audit trail.

Until 2026-09-20 this project tested a stub — a static page holding state in
`localStorage`, whose `POST /api/transfers` returned a random UUID and the
string `"completed"` without touching an account. That stub has been deleted.

## Completed

- Requirements and planning
- Manual test design
- Application implementation and deployment
- Nine automated suites, all passing in CI
- GitHub Actions for every suite, running against an app started in the runner

| Suite | Tests | Browsers |
|---|---:|---|
| Playwright | 37 | Chromium, Firefox, WebKit |
| Cypress | 26 | Chrome |
| Selenium | 27 | Chrome |
| Cucumber | 24 scenarios | Chrome |
| Database (JDBC + TestNG) | 59 | n/a |
| REST Assured | 113 | n/a |
| Postman / Newman | 103 requests, 440 assertions | n/a |
| Jest (unit, in the app repo) | 134 | n/a |
| JMeter (performance) | 2 plans | n/a |

They divide the work rather than duplicating it; see
[`docs/automation-status.md`](docs/automation-status.md). That division is what
found the defects: `BUG-UI-001` (Cypress), `BUG-UI-002` (Selenium),
`BUG-DB-001` (database suite, by reading `information_schema` rather than
driving the application), `BUG-API-001` (REST Assured) and `BUG-DASH-001`
(found by writing the `DASH` tests — three requirements had never been
rendered, which is why the module had no coverage to begin with).

All 65 endpoints the application serves are exercised by at least one suite,
and all 187 requirements across all 16 modules are covered.

**No defect is open.** All eight are closed, each with regression cover in the
suite that found it.

## Not yet started

A Jenkins pipeline that drives these suites, axe-core accessibility and OWASP
ZAP. JMeter is done — see [`jmeter/`](jmeter/) — and k6 was not added alongside
it, because a second load tool would measure the same thing twice. Pact, WireMock and Testcontainers were
evaluated and deliberately not adopted, with reasons recorded in
[`docs/automation-status.md`](docs/automation-status.md).

There is no remaining coverage gap. `DASH` was the last one, and closing it is
the most instructive thing in this repository: the module was not untested
because nobody had got to it, but because three of its seven requirements were
never implemented. Suites passed through that dashboard constantly and none
could notice a panel that was absent.

---

# Quality Principle

The objective is not to maximise the number of test cases.

The objective is to build confidence that the system performs the correct
operation, rejects invalid operations, protects customer and financial data,
maintains correct balances, prevents duplicate transactions, handles concurrent
requests correctly, persists correct database state, and remains auditable.

Critical financial functionality is validated across layers rather than
relying on UI success messages. The reference example is the ledger invariant:
every account balance must equal the `balance_after_minor` of that account's
most recent transaction. A UI assertion cannot catch a violation of it; a SQL
assertion can.
