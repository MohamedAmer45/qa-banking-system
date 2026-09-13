# Banking System Testing Project — Manual Testing

## 1. Overview

This directory contains the complete manual-testing documentation for the Banking System Testing Project.

The manual QA layer is designed as a portfolio-grade testing package covering:

* Test planning
* Risk-based test strategy
* Test scenarios
* Detailed test cases
* Test design techniques
* Exploratory testing
* Smoke testing
* Sanity testing
* Regression testing
* Security-focused manual testing
* Cross-browser testing
* Defect management
* Requirements traceability
* Test execution
* Test reporting
* UAT
* Release-readiness assessment

The objective is not simply to verify that individual screens work.

The manual-testing layer is designed to prove that the banking platform preserves:

```text
Financial Integrity

Authorization

Customer Isolation

Transaction Atomicity

Idempotency

Correct Fees and Limits

Account and Product State Rules

Auditability

Security

Cross-Layer Consistency
```

---

# 2. Manual Testing Status

## Overall Status

```text
Manual Test Design:
COMPLETE

Detailed Test Case Design:
COMPLETE

Sample Execution Documentation:
COMPLETE

Actual Application Execution:
PENDING APPLICATION IMPLEMENTATION
```

The manual-testing design phase is complete.

Actual PASS/FAIL execution results will only be recorded after the banking application is implemented and available for real testing.

No designed test case should be marked `PASS` until it has actually been executed.

---

# 3. Directory Structure

```text
manual-testing/
├── test-planning/
│   ├── test-plan.md
│   ├── test-strategy.md
│   ├── test-data-strategy.md
│   ├── environment-strategy.md
│   └── risk-assessment.md
│
├── test-scenarios/
│   ├── authentication-scenarios.md
│   ├── customer-scenarios.md
│   ├── account-scenarios.md
│   ├── beneficiary-scenarios.md
│   ├── transfer-scenarios.md
│   ├── payment-scenarios.md
│   ├── card-scenarios.md
│   ├── loan-scenarios.md
│   ├── deposit-scenarios.md
│   ├── transaction-scenarios.md
│   ├── statement-scenarios.md
│   ├── notification-scenarios.md
│   ├── profile-settings-scenarios.md
│   ├── security-scenarios.md
│   └── admin-scenarios.md
│
├── test-cases/
│   ├── authentication-test-cases.md
│   ├── customer-test-cases.md
│   ├── account-test-cases.md
│   ├── beneficiary-test-cases.md
│   ├── transfer-test-cases.md
│   ├── payment-test-cases.md
│   ├── card-test-cases.md
│   ├── loan-test-cases.md
│   ├── deposit-test-cases.md
│   ├── transaction-test-cases.md
│   ├── statement-test-cases.md
│   ├── notification-test-cases.md
│   ├── profile-settings-test-cases.md
│   ├── security-test-cases.md
│   └── admin-test-cases.md
│
├── test-design-techniques/
│   ├── boundary-value-analysis.md
│   ├── equivalence-partitioning.md
│   ├── decision-tables.md
│   ├── state-transition-testing.md
│   └── pairwise-testing.md
│
├── exploratory-testing/
│   ├── exploratory-charters.md
│   └── session-reports.md
│
├── test-suites/
│   ├── smoke-suite.md
│   ├── sanity-suite.md
│   ├── regression-suite.md
│   ├── security-suite.md
│   └── cross-browser-suite.md
│
├── defects/
│   ├── defect-template.md
│   ├── sample-defects.md
│   └── defect-severity-priority.md
│
├── traceability/
│   └── requirements-traceability-matrix.md
│
├── test-execution/
│   ├── execution-template.md
│   ├── smoke-execution.md
│   └── regression-execution.md
│
├── reports/
│   ├── test-summary-report.md
│   └── release-readiness-report.md
│
├── uat/
│   ├── uat-scenarios.md
│   └── uat-signoff.md
│
└── README.md
```

---

# 4. Functional Coverage

The manual testing package covers the following Banking System modules:

```text
Authentication

Customers

Accounts

Beneficiaries

Transfers

Payments

Cards

Loans

Deposits

Transactions

Statements

Notifications

Profile & Settings

Security

Admin / Operations
```

Additional cross-cutting coverage includes:

```text
Audit Logs

Limits

Fees

Authorization

Session Management

MFA

Financial Reconciliation

Concurrency

Idempotency

Database Validation

API Validation

Browser Compatibility

Responsive Behavior

Accessibility
```

---

# 5. Test Scenario Coverage

The project contains approximately:

```text
3,059 manual test scenarios
```

across the Banking System.

## Scenario Count by Module

| Module             | Scenarios |
| ------------------ | --------: |
| Authentication     |       212 |
| Customers          |       190 |
| Accounts           |       213 |
| Beneficiaries      |       196 |
| Transfers          |       240 |
| Payments           |       225 |
| Cards              |       201 |
| Loans              |       215 |
| Deposits           |       222 |
| Transactions       |       212 |
| Statements         |       178 |
| Notifications      |       212 |
| Profile & Settings |       226 |
| Security           |       259 |
| Admin / Operations |       258 |
| **Total**          | **3,059** |

These scenarios include:

```text
Positive Testing

Negative Testing

Boundary Testing

Authorization Testing

Financial Integrity Testing

Concurrency Testing

Error Handling

State Transitions

Security Testing

Cross-Layer Validation

End-to-End Testing
```

---

# 6. Detailed Test Case Coverage

All detailed module test-case files are now complete.

The project currently contains:

```text
2,486 detailed manual test cases
```

## Detailed Test Cases by Module

| Module             | Detailed Test Cases |
| ------------------ | ------------------: |
| Authentication     |                 150 |
| Customers          |                 116 |
| Accounts           |                 140 |
| Beneficiaries      |                 144 |
| Transfers          |                 189 |
| Payments           |                 170 |
| Cards              |                 137 |
| Loans              |                 191 |
| Deposits           |                 182 |
| Transactions       |                 163 |
| Statements         |                 147 |
| Notifications      |                 153 |
| Profile & Settings |                 164 |
| Security           |                 220 |
| Admin / Operations |                 220 |
| **Total**          |           **2,486** |

---

# 7. Detailed Test Case Design

Detailed test cases are intentionally more execution-focused than the broader scenario catalogs.

They include, where relevant:

```text
Test Case ID

Priority

Requirement

Risk

Preconditions

Test Data

Execution Steps

Expected Result

Automation Candidate

API Validation

Database Validation

Evidence Requirements
```

Not every scenario is copied one-for-one into the detailed test-case files.

Instead, scenarios are consolidated into practical executable coverage while preserving the major functional, financial, security, and edge-case risks.

---

# 8. Highest-Risk Modules

The highest-risk Banking System areas receive additional depth.

## Transfers

Coverage includes:

```text
Source ownership

Beneficiary validation

Available balance

Fees

Limits

Atomicity

Idempotency

Double submission

Retries

Scheduled transfers

Recurring transfers

Reversals

Concurrency

Database reconciliation
```

---

## Payments

Coverage includes:

```text
Provider integration

Failed-payment neutrality

Provider/local mismatches

Duplicate processing

Idempotency

Refunds

Scheduled payments

Recurring payments

Balance reconciliation
```

---

## Cards

Coverage includes:

```text
Activation

Freeze/unfreeze

Permanent block

Replacement

Expiry

Purchase authorization

Holds

Settlement

Refunds

Card limits

Sensitive data
```

---

## Loans

Coverage includes:

```text
Eligibility

Applications

Interest

Fees

Approval/rejection

Exactly-once disbursement

Installment schedules

Repayments

Overdue/default

Penalties

Early settlement

Closure
```

---

## Deposits

Coverage includes:

```text
Opening

Funding

Interest

Maturity

Exactly-once payout

Early withdrawal

Penalties

Renewal

Concurrency

Financial reconciliation
```

---

## Security

Coverage includes:

```text
Authentication

MFA

OTP lifecycle

Password reset

Session security

IDOR/BOLA

RBAC

Privilege escalation

Mass assignment

Financial parameter tampering

Replay

Rate limiting

Sensitive-data exposure

Fail-closed behavior
```

---

## Admin

Coverage includes:

```text
Admin authentication

RBAC

KYC management

Customer status

Account controls

Transaction investigation

Reversals

Loan operations

Deposit operations

Limits

Fees

Audit logs

Dual control

Concurrency

Administrative traceability
```

---

# 9. Core Banking QA Invariants

The project repeatedly validates several critical banking invariants.

## Balance Integrity

```text
Opening Balance
+ Credits
- Debits
- Fees
± Valid Adjustments
=
Closing Balance
```

---

## Failed Transaction Neutrality

```text
Failed Financial Operation
=
No Invalid Settled Debit
+
No Invalid Credit
+
No Duplicate Fee
```

---

## Atomicity

A transaction must not leave money in a partially processed state.

Example:

```text
Source Debited
+
Destination Not Credited
```

must never remain an unexplained final state.

---

## Exactly-Once Processing

```text
One Authorized Customer Intent
=
One Intended Financial Effect
```

This is tested against:

```text
Double clicks

Browser refresh

Retries

Timeouts

Duplicate callbacks

Concurrent workers

Repeated API requests
```

---

## Authorization

```text
Authenticated
does not automatically mean
Authorized
```

Every protected resource must verify ownership, role, and permission.

---

## Server-Side Authority

The client must never be authoritative for:

```text
Balance

Fees

Limits

Roles

KYC status

Transaction status

Card state

Loan state

Deposit state

Authorization
```

---

# 10. Test Design Techniques

The project contains dedicated test-design documentation for:

## Boundary Value Analysis

File:

```text
test-design-techniques/boundary-value-analysis.md
```

Coverage includes:

```text
Amounts

Limits

Password lengths

Date ranges

Pagination

Financial thresholds

Terms

Fees

Interest
```

---

## Equivalence Partitioning

File:

```text
test-design-techniques/equivalence-partitioning.md
```

Coverage includes valid and invalid partitions across all major modules.

---

## Decision Tables

File:

```text
test-design-techniques/decision-tables.md
```

Used for combinations such as:

```text
Balance + Fee + Limit + Account State

Customer Eligibility + KYC + Loan Product

Card State + Balance + Limit

Role + Resource + Action
```

---

## State Transition Testing

File:

```text
test-design-techniques/state-transition-testing.md
```

Examples include:

```text
Account states

Card lifecycle

Loan lifecycle

Deposit lifecycle

Transaction lifecycle

Customer status

Beneficiary state
```

---

## Pairwise Testing

File:

```text
test-design-techniques/pairwise-testing.md
```

Pairwise coverage helps reduce unnecessary combinations.

However:

```text
Pairwise testing is not considered sufficient
for critical banking invariants.
```

Critical financial and security combinations are tested explicitly.

---

# 11. Exploratory Testing

The project contains:

```text
75 exploratory testing charters
```

in:

```text
exploratory-testing/exploratory-charters.md
```

Coverage includes:

```text
Authentication

Transfers

Payments

Cards

Loans

Deposits

Statements

Concurrency

Error handling

Security

Financial reconciliation

Responsive behavior

Accessibility

Time/date behavior
```

The session report document contains reusable reporting templates plus sample portfolio sessions.

Sample findings are clearly identified as:

```text
SAMPLE / ILLUSTRATIVE
```

until reproduced against the implemented application.

---

# 12. Test Suites

## Smoke Suite

File:

```text
test-suites/smoke-suite.md
```

Contains approximately:

```text
87 smoke cases
```

covering the most important system-health and banking flows.

---

## Sanity Suite

File:

```text
test-suites/sanity-suite.md
```

Contains approximately:

```text
99 focused sanity cases
```

for targeted post-change validation.

---

## Regression Suite

File:

```text
test-suites/regression-suite.md
```

Organized into:

```text
TIER-1

TIER-2

TIER-3

TIER-4
```

to support risk-based regression selection.

---

## Security Suite

File:

```text
test-suites/security-suite.md
```

Contains dedicated high-risk security checks covering:

```text
Authentication

MFA

Sessions

IDOR

RBAC

Financial tampering

Replay

Sensitive data

API authorization

Auditability
```

---

## Cross-Browser Suite

File:

```text
test-suites/cross-browser-suite.md
```

Browser coverage includes:

```text
Chrome

Edge

Firefox

WebKit / Safari
```

Example viewport coverage:

```text
1920 × 1080

1366 × 768

1024 × 768

768 × 1024

390 × 844

360 × 800
```

---

# 13. Risk-Based Testing

Risk assessment is maintained in:

```text
test-planning/risk-assessment.md
```

Risk scoring uses:

```text
Probability × Impact
```

with categories:

| Score | Risk     |
| ----- | -------- |
| 1–4   | Low      |
| 5–9   | Medium   |
| 10–16 | High     |
| 17–25 | Critical |

Examples of Critical/High risks include:

```text
Incorrect balance

Unauthorized customer access

Duplicate transaction

Partial transfer

Authentication bypass

Privilege escalation

Insufficient-funds bypass

Limit bypass

Concurrency corruption

Sensitive-data exposure

Unauthorized API access

IDOR

UI/backend state mismatch
```

---

# 14. Defect Management

Defect documentation includes:

```text
defects/defect-template.md

defects/sample-defects.md

defects/defect-severity-priority.md
```

---

## Severity Levels

```text
Critical

High

Medium

Low
```

---

## Priority Levels

```text
P0

P1

P2

P3
```

---

## Sample Defects

The portfolio includes approximately:

```text
30 sample defects
```

Examples include:

```text
Duplicate transfer

Incorrect fee validation

Statement IDOR

MFA bypass

Frozen-account transaction

Failed-payment balance impact

Frozen-card transaction

Duplicate loan disbursement

Duplicate deposit payout

False success notification

Read-only admin reversal

Retry duplication
```

These defects remain illustrative until reproduced against the real application.

---

# 15. Requirements Traceability

The Requirements Traceability Matrix is located at:

```text
traceability/requirements-traceability-matrix.md
```

Traceability follows:

```text
Requirement
↓
Business Rule
↓
Risk
↓
Scenario
↓
Detailed Test Case
↓
Test Suite
↓
Execution
↓
Defect
↓
Automation
```

This supports:

```text
Coverage analysis

Change-impact analysis

Regression selection

Release readiness

Auditability
```

---

# 16. Test Execution

Execution documentation is located in:

```text
test-execution/
```

Files include:

```text
execution-template.md

smoke-execution.md

regression-execution.md
```

The sample smoke/regression executions are portfolio examples only.

They are not presented as actual application execution.

---

# 17. Test Execution Status Values

Actual execution should use:

```text
NOT_RUN

PASS

FAIL

BLOCKED

SKIPPED

NOT_APPLICABLE
```

Until a test is actually executed:

```text
Status = NOT_RUN
```

---

# 18. Sample Smoke Execution

The sample smoke report demonstrates a simulated release where:

```text
19 / 20 tests passed

1 Critical/P0 issue failed

Pass Rate:
95%
```

The example duplicate-transfer defect causes the release gate to fail.

This demonstrates an important QA principle:

```text
Pass percentage alone
does not determine release readiness.
```

A single severe financial-integrity issue may be sufficient to block release.

---

# 19. Sample Regression Execution

The sample Tier-1 regression run demonstrates:

```text
41 selected cases

40 PASS

1 PASS WITH OBSERVATION

0 FAIL
```

This is an illustrative report showing:

```text
Defect retesting

Regression

Idempotency

Reconciliation

Security

Concurrency

Financial calculations
```

It is not a claim of real system execution.

---

# 20. UAT

UAT documentation is located in:

```text
uat/
```

Files:

```text
uat-scenarios.md

uat-signoff.md
```

The UAT package covers business-level acceptance for:

```text
Authentication

Balances

Transfers

Payments

Cards

Loans

Deposits

Statements

Profile

Security

Operations
```

Actual UAT execution remains pending implementation.

---

# 21. Test Summary and Release Readiness

Reports are located in:

```text
reports/
```

## Test Summary Report

```text
reports/test-summary-report.md
```

Summarizes:

```text
Scope

Coverage

Risks

Scenarios

Design techniques

Execution examples

Defects

Automation readiness

Remaining work
```

---

## Release Readiness Report

```text
reports/release-readiness-report.md
```

Supports decisions:

```text
GO

CONDITIONAL GO

NO-GO
```

The current project status is not production-ready because real application validation remains pending.

---

# 22. Manual Testing Metrics

Current design metrics:

| Metric                     |    Value |
| -------------------------- | -------: |
| Functional modules         |       15 |
| Manual scenarios           |    3,059 |
| Detailed test cases        |    2,486 |
| Exploratory charters       |       75 |
| Detailed test-case files   |       15 |
| Design technique documents |        5 |
| Major test suites          |        5 |
| Sample defects             |      ~30 |
| UAT package                | Complete |
| RTM                        | Complete |
| Manual test design         | Complete |
| Real execution             |  Pending |

---

# 23. Automation Mapping

Manual test design will be reused when automation begins.

## Selenium — Java

Primary use:

```text
Frontend E2E automation

Page Object Model

TestNG

Maven

Data-driven tests

Regression
```

---

## Playwright — TypeScript

Primary use:

```text
Frontend E2E

Multi-browser testing

Multi-user contexts

Concurrency scenarios

Session testing

Network interception

Trace/video evidence
```

---

## Cypress — TypeScript

Primary use:

```text
Frontend workflows

API-assisted UI setup

Network validation

Fast regression coverage
```

---

## Jest

Primary use:

```text
Business logic

Calculations

Fees

Interest

Limits

State rules

Utility/backend logic
```

---

## Postman

Primary use:

```text
API exploration

Manual API validation

Environment variables

Request chaining

Collections
```

---

## REST Assured — Java

Primary use:

```text
Automated API regression

Authentication

Authorization

Schemas

Idempotency

Negative testing

Financial API validation
```

---

## SQL

Primary use:

```text
Database validation

Ledger reconciliation

Relationships

Uniqueness

Persistence

Financial integrity
```

---

## JMeter

Primary use:

```text
Load testing

Stress testing

Spike testing

Endurance testing

Concurrency

Rate-limit testing

Financial correctness under load
```

---

## Cucumber / Gherkin

Primary use:

```text
BDD scenarios

Business-readable acceptance criteria

High-value workflows
```

---

# 24. CI/CD Mapping

Automation will eventually run through:

```text
GitHub Actions

Jenkins
```

Potential pipeline:

```text
Build
↓
Unit / Jest
↓
API Smoke
↓
UI Smoke
↓
Database Validation
↓
Regression
↓
Reports
```

Performance and extended browser suites may run separately.

---

# 25. Future Real Execution Workflow

Once the Banking System application is implemented:

```text
1. Deploy application to QA.

2. Validate environment health.

3. Seed synthetic test data.

4. Execute smoke suite.

5. Log defects.

6. Retest resolved defects.

7. Execute risk-based regression.

8. Perform API validation.

9. Perform database reconciliation.

10. Execute security-focused testing.

11. Execute browser/responsive coverage.

12. Execute performance testing.

13. Execute UAT.

14. Generate real Test Summary Report.

15. Produce final Release Readiness decision.
```

---

# 26. Real Execution Evidence

Execution evidence should include, where applicable:

```text
Test Case ID

Environment

Build

Customer/Test Data ID

Expected Result

Actual Result

Status

Screenshots

API request/response

Database evidence

Logs

Transaction reference

Defect ID

Tester

Execution timestamp
```

---

# 27. Important Portfolio Rule

The project distinguishes clearly between:

```text
TEST DESIGN
```

and:

```text
REAL TEST EXECUTION
```

The documentation does not claim that designed or sample cases have passed before they are actually executed.

This protects the credibility of the portfolio.

---

# 28. Manual Testing Completion Checklist

## Planning

* [x] Test Plan
* [x] Test Strategy
* [x] Test Data Strategy
* [x] Environment Strategy
* [x] Risk Assessment

## Scenario Design

* [x] Authentication Scenarios
* [x] Customer Scenarios
* [x] Account Scenarios
* [x] Beneficiary Scenarios
* [x] Transfer Scenarios
* [x] Payment Scenarios
* [x] Card Scenarios
* [x] Loan Scenarios
* [x] Deposit Scenarios
* [x] Transaction Scenarios
* [x] Statement Scenarios
* [x] Notification Scenarios
* [x] Profile & Settings Scenarios
* [x] Security Scenarios
* [x] Admin Scenarios

## Detailed Test Cases

* [x] Authentication Test Cases
* [x] Customer Test Cases
* [x] Account Test Cases
* [x] Beneficiary Test Cases
* [x] Transfer Test Cases
* [x] Payment Test Cases
* [x] Card Test Cases
* [x] Loan Test Cases
* [x] Deposit Test Cases
* [x] Transaction Test Cases
* [x] Statement Test Cases
* [x] Notification Test Cases
* [x] Profile & Settings Test Cases
* [x] Security Test Cases
* [x] Admin Test Cases

## Test Design Techniques

* [x] Boundary Value Analysis
* [x] Equivalence Partitioning
* [x] Decision Tables
* [x] State Transition Testing
* [x] Pairwise Testing

## Exploratory Testing

* [x] Exploratory Charters
* [x] Session Report Template / Samples

## Test Suites

* [x] Smoke Suite
* [x] Sanity Suite
* [x] Regression Suite
* [x] Security Suite
* [x] Cross-Browser Suite

## Defect Management

* [x] Defect Template
* [x] Sample Defects
* [x] Severity/Priority Guide

## Traceability

* [x] Requirements Traceability Matrix

## Execution

* [x] Execution Template
* [x] Sample Smoke Execution
* [x] Sample Regression Execution
* [ ] Real Smoke Execution
* [ ] Real Regression Execution

## Reports

* [x] Test Summary Report Framework
* [x] Release Readiness Report Framework
* [ ] Final Real Test Summary
* [ ] Final Production Release Recommendation

## UAT

* [x] UAT Scenarios
* [x] UAT Signoff Template
* [ ] Actual UAT Execution
* [ ] Business Signoff

---

# 29. Step 2 Status

```text
STEP 2 — MANUAL TEST DESIGN
STATUS: COMPLETE
```

Completed deliverables include:

```text
Planning

Risk assessment

3,059 scenarios

2,486 detailed test cases

Test design techniques

Exploratory testing

Smoke/sanity/regression/security/browser suites

Defect management

Traceability

Execution templates

Sample execution reports

Test summary framework

Release readiness framework

UAT package
```

The remaining items are intentionally dependent on a functioning Banking System:

```text
Real execution

Actual defect discovery

Actual retesting

Actual regression results

Actual UAT

Final release decision
```

---

# 30. Final Manual Testing Principle

The purpose of this manual-testing package is not to create the largest possible number of test cases.

Its purpose is to provide evidence that the Banking System can preserve:

```text
Customer Money

Customer Identity

Authorization

Account Ownership

Financial Accuracy

Transaction Integrity

Security

Auditability
```

The project follows one central rule:

```text
A Banking System is not considered correct
because its screens appear to work.

It is considered correct only when
its financial state,
security state,
business rules,
API state,
database state,
and user-visible behavior
all describe the same reality.
```
