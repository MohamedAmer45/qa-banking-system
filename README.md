# QA Banking System Testing Project

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
├── jest/
├── postman/
├── rest-assured/
├── database-testing/
├── jmeter/
├── cucumber/
│
├── ci-cd/
│   ├── github-actions/
│   └── jenkins/
│
├── reports/
│
├── .gitignore
└── README.md
```

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

GitHub Actions will eventually support:

```text
Pull request validation
Smoke testing
API regression
UI regression
Scheduled regression
Report publication
```

---

# Jenkins

Jenkins will demonstrate enterprise pipeline functionality such as:

```text
Parameterized builds
Environment selection
Parallel execution
Scheduled builds
Report publication
```

---

# Reporting

The project will generate testing evidence such as:

```text
Test results
Screenshots
Videos
Traces
API responses
Database evidence
Performance reports
Logs
```

Sensitive values shall be masked.

---

# Project Workflow

The project is being developed in phases.

## Step 1 — Requirements and Planning

```text
Requirements catalog
Business rules
Roles and permissions
Test data requirements
Test plan
Test strategy
Risk analysis
Repository foundation
```

## Step 2 — Manual Testing

```text
Test scenarios
Test cases
Edge cases
Boundary cases
Negative tests
Authorization scenarios
Defect templates
Traceability
```

## Step 3 — Application Foundation

```text
Banking application implementation
Database
Backend APIs
Frontend
Authentication
Seed data
Deployment
```

## Step 4 — Selenium

```text
Java
TestNG
Maven
Page Object Model
Allure
```

## Step 5 — Playwright

```text
TypeScript
E2E automation
Fixtures
Parallel execution
Multi-browser testing
```

## Step 6 — Cypress

```text
TypeScript
Frontend automation
Network validation
```

## Step 7 — Jest

```text
Backend and business logic testing
```

## Step 8 — API Testing

```text
Postman
REST Assured
```

## Step 9 — Database Testing

```text
SQL
Data integrity
Financial validation
Transaction consistency
```

## Step 10 — BDD

```text
Cucumber
Gherkin
```

## Step 11 — Performance Testing

```text
JMeter
Load
Stress
Spike
Endurance
Concurrency
```

## Step 12 — CI/CD

```text
GitHub Actions
Jenkins
```

## Step 13 — Reporting and Final Documentation

```text
Reports
Coverage review
README updates
Portfolio presentation
```

The exact order may be adjusted when implementation dependencies require it.

---

# Current Project Status

## Step 1 — Requirements and Planning

Status:

```text
COMPLETE
```

Completed artifacts:

```text
requirements/requirements-catalog.md
requirements/business-rules.md
requirements/roles-and-permissions.md
requirements/test-data-requirements.md

test-planning/test-plan.md
test-planning/test-strategy.md
test-planning/risk-analysis.md
```

Next phase:

```text
Step 2 — Manual Testing
```

---

# Quality Principle

The objective of this project is not to maximize the number of test cases.

The objective is to build confidence that the Banking System:

```text
Performs the correct operation
Rejects invalid operations
Protects customer data
Protects financial data
Maintains correct balances
Prevents duplicate transactions
Handles concurrent requests correctly
Persists correct database state
Maintains auditability
Remains testable and maintainable
```

Critical financial functionality shall be validated across multiple layers rather than relying only on UI success messages.

<!-- NOVABANK-CURRENT-BUILD-START -->

## Current NovaBank QA Build

Live application:

`https://novabank-banking-system.vercel.app`

NovaBank is a banking-system QA portfolio project covering:

- Manual testing
- Selenium + Java
- Cypress + TypeScript
- Playwright + TypeScript
- Postman
- REST Assured
- SQL/database testing
- JMeter
- Jest
- Cucumber BDD
- GitHub Actions
- Jenkins

Current deployed modules:

`Dashboard -> Accounts -> Transfers -> Transactions -> Bills -> Cards -> Loans -> Notifications -> Profile -> Admin`

### Authentication

The current QA deployment uses deterministic customer/admin demo sessions rather than production banking credentials.

### Current Build Gaps

The following intended banking functionality remains part of the project requirements but is not available in the current deployed build:

- MFA
- Beneficiary management
- Account creation
- Extended account controls
- Dedicated statements
- Persistent database-backed banking state

Associated requirements, scenarios, test cases, and automation are preserved and marked blocked rather than deleted.

See `docs/current-build-status.md` for the implementation and coverage status.

<!-- NOVABANK-CURRENT-BUILD-END -->