# Banking System — Manual Test Plan

## 1. Document Information

| Field      | Value                                          |
| ---------- | ---------------------------------------------- |
| Project    | Banking System Testing Project                 |
| Document   | Manual Test Plan                               |
| Test Level | System / Integration / End-to-End / Acceptance |
| Test Type  | Manual Functional and Non-Functional Testing   |
| Status     | Draft                                          |
| Version    | 1.0                                            |
| Owner      | QA Engineering                                 |
| Repository | Banking System Testing Project                 |

---

# 2. Purpose

This document defines the manual testing approach for the Banking System.

The objective is to verify that the application satisfies its defined functional requirements, business rules, security expectations, usability requirements, and banking workflows.

The testing activities defined in this plan will validate both normal customer behavior and abnormal or high-risk conditions such as:

* Unauthorized transactions
* Invalid transfers
* Duplicate transactions
* Insufficient funds
* Account restrictions
* Limit violations
* Invalid authentication attempts
* Concurrent banking operations
* Incorrect balance calculations
* Unauthorized administrative actions
* Failed transaction recovery
* Data consistency issues
* Invalid beneficiary operations
* Session and authorization failures

The test plan also establishes the basis for later automation, API testing, database testing, performance testing, BDD, and CI/CD execution.

---

# 3. Project Overview

The Banking System is a full-stack banking application designed to simulate the primary services offered by a modern digital bank.

The platform supports customer-facing banking functionality and administrative banking operations.

The system includes functionality for:

* User registration
* Authentication
* Multi-factor authentication
* Customer profiles
* Bank accounts
* Account balances
* Beneficiaries
* Internal transfers
* External transfers
* Scheduled transfers
* Recurring transfers
* Transaction history
* Payments
* Cards
* Loans
* Deposits
* Statements
* Notifications
* Security controls
* Banking limits
* Fees
* Administrative operations
* Audit logging

The application will be tested from both:

* Customer perspective
* Administrative / operational perspective

---

# 4. Test Objectives

The primary objectives of manual testing are to:

1. Verify that all banking requirements are implemented correctly.

2. Validate critical end-to-end banking workflows.

3. Confirm that account balances remain accurate after financial transactions.

4. Validate banking business rules and transaction restrictions.

5. Ensure that unauthorized users cannot access protected banking resources.

6. Verify transaction limits and account restrictions.

7. Validate beneficiary management.

8. Confirm transfer processing behavior.

9. Verify correct handling of failed and rejected transactions.

10. Validate transaction history and statements.

11. Verify notification behavior.

12. Validate card operations.

13. Verify loan and deposit functionality.

14. Validate administrative controls.

15. Verify audit logging.

16. Identify usability problems.

17. Validate browser compatibility.

18. Identify security-related weaknesses observable through functional testing.

19. Identify edge cases and unexpected user behavior.

20. Build a stable regression suite.

21. Create traceability between requirements and tests.

22. Provide test evidence for release readiness.

---

# 5. Scope

## 5.1 In Scope

The following modules are included in manual testing.

### Authentication

* Registration
* Login
* Logout
* Password validation
* Password reset
* Forgot password
* Multi-factor authentication
* Failed login attempts
* Account lockout
* Session timeout
* Session invalidation
* Remember-me functionality where applicable

### Customer Management

* Customer registration
* Customer profile
* Personal information
* Contact information
* Identity information
* Customer status
* Profile updates
* Account restrictions

### Account Management

* Account creation
* Account details
* Account number validation
* Account type
* Account balance
* Available balance
* Current balance
* Account status
* Account freezing
* Account closing
* Multiple accounts
* Account ownership

### Beneficiary Management

* Add beneficiary
* Remove beneficiary
* Edit beneficiary
* Beneficiary validation
* Duplicate beneficiary prevention
* Beneficiary verification
* Beneficiary activation
* Beneficiary limits

### Transfers

* Transfer between own accounts
* Transfer to another customer
* External transfer
* Beneficiary transfer
* Scheduled transfer
* Recurring transfer
* Transfer cancellation
* Transfer confirmation
* Transfer limits
* Insufficient funds
* Invalid account destination
* Frozen accounts
* Closed accounts
* Duplicate transactions
* Failed transfers
* Pending transfers

### Payments

* Bill payment
* Merchant payment
* Payment validation
* Payment limits
* Failed payments
* Duplicate payments
* Payment status
* Payment history

### Transaction Management

* Transaction creation
* Transaction status
* Transaction history
* Transaction filtering
* Transaction searching
* Transaction reference
* Debit transactions
* Credit transactions
* Pending transactions
* Failed transactions
* Reversed transactions

### Cards

* Card creation
* Card information
* Card activation
* Card freezing
* Card unfreezing
* Card blocking
* Card replacement
* Card limits
* Card transactions
* Card status
* Expired cards

### Loans

* Loan products
* Loan applications
* Loan eligibility
* Loan approval
* Loan rejection
* Interest calculation
* Installments
* Loan repayment
* Loan status

### Deposits

* Deposit products
* Deposit creation
* Deposit balance
* Deposit maturity
* Interest calculations
* Deposit withdrawal restrictions
* Deposit status

### Statements

* Account statements
* Date filtering
* Transaction details
* Statement generation
* Statement accuracy
* Statement downloads

### Notifications

* Login notifications
* Transfer notifications
* Payment notifications
* Security notifications
* Account notifications
* Card notifications
* Notification status

### Profile and Settings

* Personal information
* Password change
* Security settings
* Notification settings
* Preferences
* Account settings

### Security

* Authentication validation
* Authorization
* Access control
* Session handling
* Privilege separation
* Input validation
* Sensitive data exposure
* Unauthorized URL access
* Direct object access
* Rate-limiting behavior where visible
* Account lockout behavior

### Administration

* Customer management
* Account management
* Transaction management
* User restrictions
* Account freezing
* Account unfreezing
* Card administration
* Loan administration
* Transaction review
* Audit log access
* Administrative authorization

### Audit Logging

* Authentication events
* Profile changes
* Account changes
* Transfer events
* Payment events
* Card events
* Administrative actions
* Security events

---

# 6. Out of Scope for Manual Testing

The following areas will be handled in dedicated testing stages.

### Automation

Automated UI regression testing will be implemented later using:

* Selenium with Java
* Cypress with TypeScript
* Playwright with TypeScript
* Jest where appropriate

### API Automation

API testing and automation will be implemented using:

* Postman
* REST Assured

### Performance Testing

Performance testing will be executed separately using:

* JMeter
* Postman where applicable
* Performance reporting and monitoring tools

### Database Testing

Database validation will be executed in a dedicated SQL testing phase.

Manual tests may specify expected database behavior, but direct database validation will be documented separately.

### CI/CD

Continuous testing pipelines will later be implemented using:

* GitHub Actions
* Jenkins

### BDD Automation

Business scenarios will later be represented using:

* Cucumber
* Gherkin

---

# 7. Test Levels

The following testing levels will be covered.

## 7.1 System Testing

Validate the complete Banking System as a fully integrated application.

Example:

Customer logs in, creates a beneficiary, transfers money, and verifies that the transaction appears in transaction history.

---

## 7.2 Integration Testing

Validate interactions between application modules.

Examples:

* Transfers updating account balances
* Card transactions appearing in transaction history
* Loan repayments updating loan balances
* Account changes generating notifications
* Administrative actions generating audit logs

---

## 7.3 End-to-End Testing

Validate realistic customer banking workflows.

Example:

Login → Add Beneficiary → Verify Beneficiary → Transfer Funds → Receive Confirmation → Verify Balance → Verify Transaction History.

---

## 7.4 User Acceptance Testing

Validate that the application supports realistic customer and banking staff workflows.

---

# 8. Test Types

The manual testing effort will include the following types of testing.

## 8.1 Functional Testing

Verify application functionality against requirements.

---

## 8.2 Positive Testing

Verify valid operations.

Example:

Transfer money to a valid beneficiary with sufficient balance.

---

## 8.3 Negative Testing

Verify invalid operations are rejected safely.

Example:

Attempt to transfer an amount greater than the available balance.

---

## 8.4 Boundary Value Testing

Validate values around limits.

Examples:

If the minimum transfer amount is 1:

* 0
* 1
* 2

If the maximum transfer amount is 100,000:

* 99,999
* 100,000
* 100,001

---

## 8.5 Equivalence Partitioning

Inputs will be divided into valid and invalid classes.

Example:

Transfer amount:

Valid:

1–100,000

Invalid:

* Negative amount
* Zero
* Greater than 100,000
* Non-numeric input

---

## 8.6 Decision Table Testing

Used when multiple conditions determine an outcome.

Example:

Transfer approval may depend on:

* Account active
* Beneficiary active
* Sufficient balance
* Transfer amount within limit
* Account not frozen

---

## 8.7 State Transition Testing

Used for functionality whose behavior depends on current state.

Examples:

Card:

Active → Frozen → Active → Blocked

Account:

Active → Restricted → Frozen → Closed

Transaction:

Created → Pending → Completed

or

Created → Pending → Failed

---

## 8.8 Exploratory Testing

Structured exploratory sessions will target:

* Financial logic
* Navigation
* Unexpected workflows
* Data manipulation
* Concurrent behavior
* Error recovery
* Security-related behavior

---

## 8.9 Regression Testing

Validate existing functionality after system changes.

---

## 8.10 Smoke Testing

Verify critical system functionality after deployment.

---

## 8.11 Sanity Testing

Validate specific functionality after limited changes or bug fixes.

---

## 8.12 Usability Testing

Evaluate:

* Navigation
* Error messages
* Form clarity
* Banking terminology
* Transaction confirmation
* Important warnings
* Customer feedback

---

## 8.13 Compatibility Testing

Test across supported browsers and viewport sizes.

---

## 8.14 Security-Oriented Functional Testing

Manual checks will include:

* Unauthorized access attempts
* Privilege escalation attempts
* Direct URL navigation
* Session expiration
* Account access isolation
* Sensitive information exposure
* Input manipulation
* Repeated authentication attempts

---

# 9. High-Risk Banking Areas

The following functionality is classified as high risk.

| Area                       | Risk     |
| -------------------------- | -------- |
| Authentication             | Critical |
| Authorization              | Critical |
| Transfers                  | Critical |
| Balance calculation        | Critical |
| Transaction processing     | Critical |
| Account ownership          | Critical |
| Administrative permissions | Critical |
| Audit logging              | High     |
| Beneficiary management     | High     |
| Card management            | High     |
| Payments                   | High     |
| Loan calculations          | High     |
| Statement accuracy         | High     |
| Notifications              | Medium   |
| Profile management         | Medium   |
| UI appearance              | Low      |

High-risk functionality will receive:

* More test coverage
* More negative testing
* More boundary testing
* More regression coverage
* More exploratory testing

---

# 10. Critical Banking Invariants

The following rules must always remain true.

## 10.1 Balance Integrity

Money must never disappear or appear without a corresponding financial operation.

For a successful internal transfer:

Source balance decreases by transfer amount plus any applicable fees.

Destination balance increases by the transferred amount.

---

## 10.2 Failed Transaction Integrity

A failed transaction must not incorrectly change account balances.

---

## 10.3 Duplicate Transaction Prevention

Repeated submission must not unintentionally create duplicate financial transactions.

---

## 10.4 Authorization

A customer must never access another customer's:

* Accounts
* Cards
* Transactions
* Statements
* Beneficiaries
* Loans
* Deposits
* Profile information

---

## 10.5 Transaction Atomicity

Financial transactions must either complete successfully or fail completely.

Partial transfers are unacceptable.

---

## 10.6 Account Status Enforcement

Frozen, blocked, restricted, or closed accounts must not perform prohibited operations.

---

## 10.7 Limits Enforcement

Transaction limits must be enforced consistently.

---

## 10.8 Auditability

Critical banking operations must generate traceable records.

---

# 11. Test Environment

Manual testing will be performed against the designated test environment.

Expected environment components:

* Banking frontend
* Banking backend
* Database
* Authentication service
* API layer
* Test users
* Test financial data

Testing must never use real customer financial information.

---

# 12. Browser Coverage

The application should be tested using current stable versions of major browsers.

Primary browsers:

* Google Chrome
* Microsoft Edge
* Mozilla Firefox

Secondary testing may include:

* Safari where environment availability allows

---

# 13. Responsive Testing

The banking application will be tested across representative viewport sizes.

Examples:

### Desktop

* 1920 × 1080
* 1366 × 768

### Tablet

* 768 × 1024

### Mobile

* 390 × 844
* 360 × 800

Testing will verify:

* Responsive layout
* Navigation
* Forms
* Tables
* Transaction screens
* Modals
* Confirmation dialogs
* Buttons
* Error messages

---

# 14. Test Data

Test data must support both successful and unsuccessful workflows.

Required test users include:

### Standard Customer

Customer with:

* Active account
* Sufficient balance
* Active card
* Valid beneficiary

### Low-Balance Customer

Customer with insufficient funds.

### Frozen Account Customer

Customer whose account is frozen.

### Restricted Customer

Customer with restricted banking functionality.

### Locked Customer

Customer locked after repeated failed authentication attempts.

### Multiple-Account Customer

Customer owning multiple bank accounts.

### Loan Customer

Customer with active loan.

### Deposit Customer

Customer with active deposit product.

### Administrator

Administrative user with valid permissions.

### Limited Administrator

Administrator with restricted permissions.

---

# 15. Test Data Principles

Test data must:

* Never contain real financial information.
* Be reproducible.
* Support positive and negative scenarios.
* Include boundary values.
* Include invalid values.
* Support role-based testing.
* Support account-state testing.
* Support repeatable regression execution.

---

# 16. Entry Criteria

Manual test execution may begin when:

* Requirements are available.
* Business rules are documented.
* Application build is deployed.
* Test environment is accessible.
* Core services are operational.
* Required test accounts exist.
* Critical test data exists.
* Blocking deployment issues are resolved.

---

# 17. Exit Criteria

Manual testing may be considered complete when:

* All planned critical test cases have been executed.
* All critical requirements have test coverage.
* No unresolved blocker defects remain.
* No unresolved critical defects remain unless formally accepted.
* High-severity defects have been reviewed.
* Critical regression tests pass.
* Smoke tests pass.
* Requirements traceability is complete.
* Test summary report is produced.
* Release risk has been evaluated.

---

# 18. Test Case Statuses

The following statuses will be used.

| Status         | Meaning                               |
| -------------- | ------------------------------------- |
| Not Run        | Test has not been executed            |
| Pass           | Expected behavior observed            |
| Fail           | Actual behavior differs from expected |
| Blocked        | Test cannot be executed               |
| Skipped        | Test intentionally not executed       |
| Not Applicable | Test does not apply                   |

---

# 19. Defect Severity

## Blocker

The system or critical functionality cannot be used.

Examples:

* Application unavailable
* Users cannot log in
* All transfers fail

---

## Critical

A serious banking or security defect.

Examples:

* Money transferred incorrectly
* Unauthorized account access
* Incorrect balance
* Duplicate financial transaction
* Authentication bypass

---

## Major

Important functionality does not work correctly but the system remains usable.

Examples:

* Statement generation fails
* Beneficiary deletion fails
* Card freeze action fails

---

## Minor

Limited functional impact.

Examples:

* Incorrect validation message
* Incorrect sorting
* Minor UI issue affecting usability

---

## Trivial

Cosmetic issue with no functional impact.

---

# 20. Defect Priority

## P0 — Immediate

Must be fixed before testing or release can continue.

## P1 — High

Should be fixed before release.

## P2 — Medium

Should be addressed but may not block release.

## P3 — Low

Can be scheduled for future improvement.

---

# 21. Test Scenario Naming Convention

Test scenarios will follow the format:

```text
TS-[MODULE]-[NUMBER]
```

Examples:

```text
TS-AUTH-001
TS-ACC-001
TS-BEN-001
TS-TRF-001
TS-PAY-001
TS-CARD-001
```

---

# 22. Test Case Naming Convention

Test cases will follow:

```text
TC-[MODULE]-[NUMBER]
```

Examples:

```text
TC-AUTH-001
TC-AUTH-002

TC-ACC-001

TC-TRF-001
TC-TRF-002
```

---

# 23. Requirement Naming Convention

Requirements will use:

```text
REQ-[MODULE]-[NUMBER]
```

Examples:

```text
REQ-AUTH-001
REQ-ACC-001
REQ-TRF-001
```

These identifiers will allow traceability between:

Requirement → Scenario → Test Case → Defect

---

# 24. Defect Naming Convention

Defects will use:

```text
BUG-[MODULE]-[NUMBER]
```

Examples:

```text
BUG-AUTH-001
BUG-TRF-001
BUG-CARD-001
```

---

# 25. Test Case Structure

Each detailed test case should contain:

* Test Case ID
* Requirement ID
* Module
* Title
* Priority
* Severity if failure occurs
* Preconditions
* Test data
* Steps
* Expected result
* Actual result
* Status
* Defect reference
* Notes

---

# 26. Test Execution Strategy

Manual execution will be performed in the following order.

## Phase 1 — Build Verification

Run smoke tests.

Verify:

* Application loads
* Login works
* Dashboard loads
* Accounts appear
* Basic transfer works
* Logout works

---

## Phase 2 — Functional Testing

Execute module-specific test cases.

---

## Phase 3 — Negative and Edge-Case Testing

Test invalid data and unusual workflows.

---

## Phase 4 — Integration Testing

Validate module interactions.

---

## Phase 5 — End-to-End Testing

Execute complete customer banking journeys.

---

## Phase 6 — Exploratory Testing

Perform structured exploratory sessions.

---

## Phase 7 — Regression Testing

Execute full regression coverage.

---

## Phase 8 — UAT

Validate major customer and banking workflows.

---

# 27. Sample End-to-End Banking Journeys

## Journey 1 — Customer Transfer

```text
Login
→ View account
→ Add beneficiary
→ Activate beneficiary
→ Initiate transfer
→ Confirm transfer
→ Verify balance
→ Verify transaction
→ Verify notification
→ Verify statement
```

---

## Journey 2 — Card Management

```text
Login
→ Open cards
→ View card
→ Freeze card
→ Verify card status
→ Attempt card operation
→ Unfreeze card
→ Verify card restored
```

---

## Journey 3 — Loan Workflow

```text
Login
→ View loan products
→ Apply for loan
→ Submit application
→ Administrator reviews application
→ Approve loan
→ Customer sees active loan
→ Repayment becomes available
```

---

## Journey 4 — Security Workflow

```text
Attempt invalid login repeatedly
→ Account becomes locked
→ Valid login fails while locked
→ Account unlock process occurs
→ Customer logs in successfully
→ Audit record exists
```

---

# 28. Negative Testing Strategy

Negative testing will intentionally attempt invalid actions including:

* Empty fields
* Invalid characters
* Excessively long values
* Negative values
* Zero values
* Decimal precision issues
* Invalid accounts
* Invalid beneficiaries
* Duplicate beneficiaries
* Duplicate transactions
* Expired sessions
* Unauthorized resource IDs
* Transfers from frozen accounts
* Transfers to closed accounts
* Payments exceeding limits
* Invalid card actions
* Invalid loan values

---

# 29. Edge-Case Testing Strategy

Examples include:

* Exact account balance transfer
* One unit below transaction limit
* Exact transaction limit
* One unit above transaction limit
* Extremely small transfer
* Extremely large transfer
* Multiple transactions submitted rapidly
* Browser refresh during transaction
* Browser back button during transfer confirmation
* Double-clicking transfer submission
* Session expiry while submitting transaction
* Account frozen during active session
* Beneficiary removed before transfer completes
* Multiple tabs modifying account state

---

# 30. Concurrency Testing Scenarios

Concurrency risks will be manually explored.

Examples:

### Simultaneous Transfers

Two transfers attempt to use the same available balance.

Expected:

The system must not allow the combined transfers to exceed the available balance.

### Multiple Sessions

Customer logs in from multiple browser sessions.

Verify expected session behavior.

### Administrative State Changes

Administrator freezes an account while the customer is logged in.

Expected:

Subsequent prohibited operations must fail.

---

# 31. Financial Precision Testing

Financial values must be tested carefully.

Testing will include:

* Decimal handling
* Rounding
* Fees
* Interest
* Available balance
* Current balance
* Loan balances
* Deposit interest
* Currency values

Example:

```text
Balance = 1000.00
Transfer = 100.55
Fee = 2.50

Expected balance = 896.95
```

---

# 32. Error Handling Validation

Errors must:

* Clearly identify the problem.
* Avoid exposing internal technical details.
* Avoid exposing database information.
* Avoid exposing stack traces.
* Avoid exposing secrets.
* Provide actionable user feedback.

---

# 33. Security-Oriented Manual Checks

Manual functional security testing will include:

* Access account URL belonging to another customer.
* Manipulate account identifiers.
* Manipulate transaction identifiers.
* Access admin URLs as customer.
* Reuse expired session.
* Submit requests after logout.
* Attempt brute-force-style repeated login attempts.
* Test account lockout.
* Verify password masking.
* Inspect browser-visible sensitive information.
* Test restricted actions for unauthorized roles.

Dedicated security penetration testing is outside the scope of this manual test phase.

---

# 34. Accessibility Checks

Basic accessibility checks include:

* Keyboard navigation
* Focus order
* Visible focus indicators
* Form labels
* Error identification
* Button naming
* Color contrast observations
* Screen zoom usability
* Required field indication

---

# 35. Usability Checks

Banking workflows should clearly communicate:

* Account selected
* Transfer recipient
* Transfer amount
* Applicable fees
* Final amount
* Confirmation
* Transaction status
* Errors
* Warnings

High-risk banking operations should require clear confirmation.

---

# 36. Cross-Browser Testing

Critical workflows will be executed across supported browsers.

Priority workflows:

* Login
* Account dashboard
* Beneficiary management
* Transfers
* Payments
* Card controls
* Statements
* Logout

---

# 37. Regression Strategy

The regression suite will prioritize:

### Critical

* Authentication
* Account balances
* Transfers
* Payments
* Authorization
* Transaction integrity

### High

* Beneficiaries
* Cards
* Statements
* Account restrictions
* Admin controls

### Medium

* Notifications
* Profile
* Preferences
* UI behavior

---

# 38. Smoke Test Coverage

Smoke testing will include at minimum:

1. Application is reachable.
2. Customer can log in.
3. Dashboard loads.
4. Accounts are visible.
5. Balance is displayed.
6. Customer can open transaction history.
7. Customer can create a valid transfer.
8. Transfer updates balance.
9. Transaction appears in history.
10. Customer can log out.

---

# 39. Release Blocking Conditions

Release must be blocked when:

* A blocker defect exists.
* A critical security issue exists.
* Account balances are incorrect.
* Duplicate financial transactions can occur.
* Unauthorized users can access other customers' data.
* Transfers cause data corruption.
* Authentication can be bypassed.
* Critical regression tests fail.
* Financial records are inconsistent.

---

# 40. Release Acceptance Conditions

A release may be recommended when:

* Smoke suite passes.
* Critical regression suite passes.
* No blocker defects remain.
* No unresolved critical financial defects remain.
* No critical authorization defects remain.
* Business-critical workflows work correctly.
* Remaining known issues are documented.
* Residual risk is considered acceptable.

---

# 41. Test Deliverables

Manual testing will produce:

* Test plan
* Test strategy
* Test scenarios
* Detailed test cases
* Boundary value analysis
* Equivalence partitions
* Decision tables
* State transition models
* Exploratory testing charters
* Smoke suite
* Sanity suite
* Regression suite
* Security-focused suite
* Cross-browser suite
* Defect reports
* Requirements traceability matrix
* Execution reports
* UAT scenarios
* Test summary report
* Release readiness report

---

# 42. Risks

Potential testing risks include:

| Risk                            | Impact | Mitigation                                  |
| ------------------------------- | ------ | ------------------------------------------- |
| Unstable test environment       | High   | Stabilize environment before execution      |
| Missing test data               | High   | Create reusable seeded data                 |
| Changing requirements           | Medium | Maintain requirement traceability           |
| Complex financial rules         | High   | Validate against business rules             |
| Shared test accounts            | Medium | Isolate test data                           |
| Database resets                 | Medium | Maintain reproducible seed data             |
| Third-party dependency failures | Medium | Use controlled test behavior where possible |
| Limited browser coverage        | Low    | Prioritize major browsers                   |

---

# 43. Assumptions

This plan assumes that:

* Requirements are documented.
* Banking business rules are available.
* Test users can be created.
* Test balances can be controlled.
* Application builds are deployable.
* Defects can be recorded and tracked.
* Banking operations use test data only.

---

# 44. QA Completion Definition

Manual QA for a release is considered complete when:

```text
Requirements reviewed
        ↓
Scenarios created
        ↓
Test cases created
        ↓
Smoke testing completed
        ↓
Functional testing completed
        ↓
Negative testing completed
        ↓
Exploratory testing completed
        ↓
Defects reviewed
        ↓
Critical defects resolved
        ↓
Regression completed
        ↓
Traceability verified
        ↓
Test summary generated
        ↓
Release recommendation provided
```

---

# 45. Final Testing Principle

The Banking System must not only work correctly under expected conditions.

It must also behave safely and predictably when:

* Users make mistakes.
* Users attempt invalid actions.
* Accounts have restricted states.
* Transactions fail.
* Sessions expire.
* Requests are repeated.
* Multiple operations occur concurrently.
* Unauthorized access is attempted.
* Financial values reach their boundaries.

For all critical financial workflows, correctness, consistency, authorization, and traceability take priority over cosmetic or convenience features.

<!-- NOVABANK-TEST-PLAN-SYNC-START -->

## Current Build Test Plan Synchronization

### Test Object

NovaBank Banking System QA Application

Production URL:

`https://novabank-banking-system.vercel.app`

### Objectives

Validate:

- Functional behavior
- Positive workflows
- Negative workflows
- Boundary values
- Role authorization
- Session handling
- UI behavior
- API behavior
- Regression behavior
- Cross-browser behavior
- Integration behavior
- Automation maintainability
- Performance where applicable
- Data behavior where applicable

### Current In-Scope Modules

- Customer demo session
- Admin demo session
- Logout
- Dashboard
- Accounts
- Transactions
- Transfers
- Bills
- Cards
- Loans
- Notifications
- Profile
- Admin
- Authorization
- Session security

### Temporarily Blocked Areas

- Email/password login
- MFA
- Beneficiaries
- Account creation
- Extended account controls
- Dedicated statements
- Persistent database validation
- SQL persistence testing

Blocked functionality remains within overall project scope.

### Testing Types

- Smoke
- Functional
- Regression
- Negative
- Boundary-value
- Authorization
- Security/session
- UI
- API
- Cross-browser
- Exploratory
- Integration
- End-to-end where supported
- Performance where supported

### Automation Stack

- Selenium + Java + TestNG + Maven
- Cypress + TypeScript
- Playwright + TypeScript
- Postman
- REST Assured
- Jest
- Cucumber
- JMeter
- GitHub Actions
- Jenkins

### Entry Criteria

- Production QA application is reachable.
- Health endpoint responds successfully.
- Required current-build modules are available.
- Automation dependencies are installed.
- Required browser/runtime is available.
- Test state strategy is defined.

### Exit Criteria

- Smoke suite passes.
- Critical executable banking flows are tested.
- Migrated regression tests pass.
- Defects are documented.
- Blocked functionality is clearly identified.
- Failures are classified correctly as product, test, data, or environment issues.
- Required evidence and reports are retained.

### State Consideration

The current application uses browser-local deterministic state.

Tests that mutate balances, transactions, cards, loans, or notifications must use isolated state or restore/reset state.

### Database Limitation

The deployed build currently does not use persistent PostgreSQL-backed banking state.

SQL/database persistence testing remains part of the project plan but is blocked until a persistent database layer is introduced.

<!-- NOVABANK-TEST-PLAN-SYNC-END -->