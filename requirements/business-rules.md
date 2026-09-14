# Banking System Business Rules

## 1. Purpose

This document defines the business rules that govern the Banking System.

These rules supplement the functional requirements defined in `requirements-catalog.md` and provide detailed constraints for authentication, accounts, beneficiaries, transfers, payments, cards, loans, security, administration, database integrity, APIs, concurrency, and financial transactions.

Each business rule has a unique identifier so it can later be referenced by:

- Manual test scenarios
- Manual test cases
- Selenium tests
- Cypress tests
- Playwright tests
- Jest tests
- Postman tests
- REST Assured tests
- Database tests
- JMeter performance tests
- Cucumber scenarios
- CI/CD pipelines

---

# 2. Authentication Rules

## BR-AUTH-001 — Login Credentials

A customer must provide a registered email address and valid password to authenticate.

## BR-AUTH-002 — Email Case Sensitivity

Email addresses must be treated as case-insensitive.

Example:

```text
user@example.com
USER@EXAMPLE.COM
User@Example.com
```

All of the above shall represent the same login identifier.

## BR-AUTH-003 — Password Case Sensitivity

Passwords must be case-sensitive.

Example:

```text
Password123!
password123!
```

These shall be treated as different passwords.

## BR-AUTH-004 — Invalid Login Response

The system must not reveal whether an email address exists.

Invalid authentication attempts shall return a generic message such as:

```text
Invalid email or password.
```

## BR-AUTH-005 — Failed Login Limit

A customer account shall be temporarily locked after:

```text
5 consecutive failed login attempts
```

## BR-AUTH-006 — Account Lock Duration

A temporary authentication lock shall last:

```text
15 minutes
```

unless an authorized administrator unlocks the account earlier.

## BR-AUTH-007 — Successful Login Reset

A successful login shall reset the failed-login counter to zero.

## BR-AUTH-008 — OTP Format

Authentication OTP codes shall contain:

```text
6 numeric digits
```

Example:

```text
483921
```

## BR-AUTH-009 — OTP Expiration

OTP codes shall expire after:

```text
5 minutes
```

## BR-AUTH-010 — OTP Usage

An OTP may only be successfully used once.

## BR-AUTH-011 — OTP Attempt Limit

A maximum of:

```text
5 invalid OTP attempts
```

shall be allowed for each verification challenge.

## BR-AUTH-012 — Session Timeout

Authenticated customer sessions shall expire after:

```text
15 minutes of inactivity
```

## BR-AUTH-013 — Password Reset Token Expiration

Password-reset tokens shall expire after:

```text
30 minutes
```

## BR-AUTH-014 — Password Reset Token Usage

A password-reset token may only be successfully used once.

## BR-AUTH-015 — Logout

Logging out shall immediately invalidate the active authentication session.

---

# 3. Password Rules

## BR-PWD-001 — Minimum Length

Passwords must contain at least:

```text
12 characters
```

## BR-PWD-002 — Maximum Length

Passwords may contain a maximum of:

```text
128 characters
```

## BR-PWD-003 — Complexity

Passwords must contain at least:

```text
1 uppercase letter
1 lowercase letter
1 number
1 special character
```

## BR-PWD-004 — Whitespace

Leading and trailing whitespace must not be silently introduced into stored passwords.

## BR-PWD-005 — Password Storage

Passwords must never be stored in plain text.

Passwords must be stored using an appropriate secure password hashing mechanism.

## BR-PWD-006 — Password Exposure

Passwords must never appear in:

```text
API responses
Application logs
URLs
Audit logs
Client-side storage
```

## BR-PWD-007 — Password Reset Invalidation

Successfully changing a password shall invalidate existing password-reset tokens.

## BR-PWD-008 — Current Password Verification

Changing a password from an authenticated account shall require verification of the current password.

---

# 4. Customer and KYC Rules

## BR-KYC-001 — Unique Customer Email

A registered email address may belong to only one customer profile.

## BR-KYC-002 — Required KYC Information

Required customer information shall include at minimum:

```text
Full name
Date of birth
Email address
Phone number
Residential address
Government-issued identification number
```

## BR-KYC-003 — KYC Statuses

Supported KYC statuses are:

```text
PENDING
APPROVED
REJECTED
REVIEW_REQUIRED
```

## BR-KYC-004 — Financial Restrictions

Customers whose KYC status is not `APPROVED` shall not perform restricted financial operations such as external transfers.

## BR-KYC-005 — KYC Approval

Only authorized bank employees may approve or reject KYC submissions.

## BR-KYC-006 — KYC Audit

KYC status changes must generate an audit record.

## BR-KYC-007 — Customer Data Ownership

Customers shall only access their own customer profile information.

## BR-KYC-008 — Sensitive Changes

Changes to sensitive customer information shall require additional verification where configured.

---

# 5. Account Rules

## BR-ACC-001 — Account Ownership

Every bank account must belong to exactly one customer.

## BR-ACC-002 — Account Number Uniqueness

Every bank account number must be unique.

## BR-ACC-003 — Supported Account Types

The initial system shall support:

```text
CURRENT
SAVINGS
```

## BR-ACC-004 — Account Statuses

Supported account statuses are:

```text
ACTIVE
FROZEN
SUSPENDED
CLOSED
```

## BR-ACC-005 — Active Accounts

Only `ACTIVE` accounts may initiate standard financial transactions.

## BR-ACC-006 — Frozen Accounts

A `FROZEN` account shall not initiate outgoing financial transactions.

## BR-ACC-007 — Suspended Accounts

A `SUSPENDED` account shall not initiate financial transactions until reinstated.

## BR-ACC-008 — Closed Accounts

A `CLOSED` account shall not perform new financial transactions.

Historical transactions must remain available to authorized users.

## BR-ACC-009 — Negative Balances

Account balances shall not become negative unless an explicit overdraft facility is enabled.

For the initial implementation:

```text
Overdraft = Disabled
```

## BR-ACC-010 — Monetary Precision

Account balances shall use fixed-point decimal monetary arithmetic.

Floating-point values must not be used for stored financial balances.

Recommended database representation:

```text
DECIMAL(19,4)
```

## BR-ACC-011 — Display Precision

For EGP, monetary values shall normally be displayed using two decimal places.

Examples:

```text
1000.00
250.75
0.01
```

## BR-ACC-012 — Available Balance

Available balance represents the amount currently available for customer use.

## BR-ACC-013 — Ledger Balance

Ledger/current balance may include transactions not yet reflected in the available balance depending on transaction state.

## BR-ACC-014 — Cross-Customer Access

A customer shall never be able to access an account belonging to another customer.

---

# 6. Beneficiary Rules

## BR-BEN-001 — Ownership

Beneficiaries shall belong to the customer who created them.

## BR-BEN-002 — Cross-Customer Access

A customer must never access another customer's beneficiary records.

## BR-BEN-003 — Required Details

A beneficiary shall require valid destination-account information.

## BR-BEN-004 — Duplicate Beneficiaries

The same customer shall not create duplicate beneficiaries containing identical destination-account information.

## BR-BEN-005 — Verification

New beneficiaries may require OTP verification before becoming eligible for transfers.

## BR-BEN-006 — Beneficiary Status

A beneficiary requiring verification shall not be eligible for transfers until verification is complete.

## BR-BEN-007 — Deleted Beneficiaries

Deleting a beneficiary shall not delete historical transactions previously made to that beneficiary.

---

# 7. Transfer Rules

## BR-TRF-001 — Transfer Amount

A transfer amount must be:

```text
Greater than 0
```

## BR-TRF-002 — Minimum Transfer

The minimum allowed transfer amount shall be:

```text
0.01 EGP
```

## BR-TRF-003 — Available Funds

The transfer amount plus applicable fees must not exceed the source account's available balance.

## BR-TRF-004 — Per-Transaction Limit

The initial maximum transfer amount shall be:

```text
100,000 EGP per transaction
```

## BR-TRF-005 — Daily Transfer Limit

The initial customer daily transfer limit shall be:

```text
250,000 EGP per customer per day
```

The limit shall be configurable.

## BR-TRF-006 — Own-Account Transfers

Transfers between eligible accounts belonging to the same customer shall be supported.

## BR-TRF-007 — Same-Bank Transfers

Transfers between eligible customers of the same bank shall be supported.

## BR-TRF-008 — External Transfers

External transfers shall only be permitted to valid and supported destination accounts.

## BR-TRF-009 — Transaction Reference

Every transfer shall receive a globally unique transaction reference.

Example:

```text
TRF-20260912-A7F2C91B
```

The exact format may vary, but uniqueness is mandatory.

## BR-TRF-010 — Atomicity

A financial transfer must be atomic.

A transfer must either:

```text
Complete entirely
```

or:

```text
Fail entirely
```

Partial balance changes are not permitted.

## BR-TRF-011 — Debit/Credit Consistency

For a successful internal transfer:

```text
Sender balance decreases by the transfer amount.
Recipient balance increases by the transfer amount.
```

Applicable fees shall be handled separately and correctly.

## BR-TRF-012 — Failed Transfers

A failed transfer must not incorrectly modify account balances.

## BR-TRF-013 — Duplicate Requests

Repeated submission of the same transfer request must not unintentionally create duplicate financial transactions.

## BR-TRF-014 — Idempotency

Transfer APIs supporting idempotency shall accept an idempotency key.

Requests containing the same valid idempotency key and identical payload shall not create multiple transactions.

## BR-TRF-015 — Confirmation

The customer must be shown transfer details before final confirmation.

These details shall include:

```text
Source account
Destination account/beneficiary
Transfer amount
Fee
Total amount
```

## BR-TRF-016 — Transfer Statuses

Transfers may have the following statuses:

```text
PENDING
COMPLETED
FAILED
CANCELLED
REVERSED
```

## BR-TRF-017 — Completed Transfers

A completed transfer cannot normally be cancelled.

A financial correction shall instead use an appropriate reversal process.

## BR-TRF-018 — Scheduled Transfer Date

A scheduled transfer must have an execution date later than the current processing time.

## BR-TRF-019 — Scheduled Transfer Cancellation

A scheduled transfer may only be cancelled before processing begins.

## BR-TRF-020 — Recurring Transfer Frequency

Supported recurring-transfer frequencies shall initially include:

```text
WEEKLY
MONTHLY
```

## BR-TRF-021 — Scheduled Transfer Funds

Available funds must be validated again when a scheduled transfer is executed.

## BR-TRF-022 — Insufficient Scheduled Funds

If sufficient funds are unavailable during scheduled execution:

```text
Transfer = FAILED
Balances = unchanged
```

## BR-TRF-023 — Self Transfer

A transfer from an account to the exact same account shall be rejected.

## BR-TRF-024 — Transfer Audit

Every transfer attempt shall generate an appropriate auditable event.

## BR-TRF-025 — Source Account Status

The source account must be `ACTIVE` when the transfer is executed.

## BR-TRF-026 — Destination Validation

The destination account must exist and be eligible to receive the transaction where applicable.

---

# 8. Concurrent Transaction Rules

## BR-CON-001 — Concurrent Balance Protection

Concurrent transfers must not allow an account to spend more than its available balance.

Example:

```text
Starting balance = 1,000 EGP

Transfer A = 800 EGP
Transfer B = 800 EGP
```

Both transfers must not complete successfully.

## BR-CON-002 — Lost Updates

Concurrent balance updates must not overwrite valid committed updates.

## BR-CON-003 — Transaction Isolation

Database transaction handling shall prevent inconsistent financial states caused by concurrent operations.

## BR-CON-004 — Duplicate Concurrent Requests

Two simultaneous requests containing the same valid idempotency key shall result in at most one financial transaction.

## BR-CON-005 — Balance Validation

Balance validation and balance modification must be handled safely so that race conditions cannot create invalid negative balances.

---

# 9. Payment Rules

## BR-PAY-001 — Payment Amount

Payment amounts must be greater than zero.

## BR-PAY-002 — Sufficient Funds

Payments must not exceed the available balance plus any explicitly supported credit facility.

## BR-PAY-003 — Failed Payments

Failed payments must not incorrectly deduct funds.

## BR-PAY-004 — Payment Reference

Every payment must have a unique reference number.

## BR-PAY-005 — Scheduled Payments

Scheduled payments must validate account status and available funds at execution time.

## BR-PAY-006 — Recurring Payments

Recurring payments shall execute according to their configured frequency until cancelled or expired.

## BR-PAY-007 — Payment Statuses

Payments may contain statuses such as:

```text
PENDING
COMPLETED
FAILED
CANCELLED
REVERSED
```

## BR-PAY-008 — Payment Audit

Financial payment attempts shall generate appropriate auditable records.

---

# 10. Card Rules

## BR-CARD-001 — Card Ownership

A card shall belong to a valid customer/account relationship.

## BR-CARD-002 — Card Masking

Full card numbers must not normally be displayed.

Example:

```text
**** **** **** 1234
```

## BR-CARD-003 — CVV Protection

CVV values must never be stored in application logs.

## BR-CARD-004 — Frozen Cards

Frozen cards shall reject new card transactions.

## BR-CARD-005 — Card Unfreeze

Only eligible cards previously frozen through an allowed process may be unfrozen.

## BR-CARD-006 — Blocked Cards

Blocked cards cannot be reactivated through the normal customer unfreeze operation.

## BR-CARD-007 — Expired Cards

Expired cards shall not process new card transactions.

## BR-CARD-008 — Spending Limits

Customer-configurable card limits must not exceed system-defined maximum limits.

## BR-CARD-009 — Online Payments

If online payments are disabled for a card, new online card transactions shall be rejected.

## BR-CARD-010 — International Transactions

If international usage is disabled, transactions classified as international shall be rejected.

## BR-CARD-011 — Cross-Customer Access

Customers must not access or modify cards belonging to other customers.

---

# 11. Transaction History Rules

## BR-TXN-001 — Transaction Persistence

Successful financial transactions shall create persistent transaction records.

## BR-TXN-002 — Failed Transaction Records

Failed transactions may be retained for auditing and troubleshooting.

## BR-TXN-003 — Ownership

Customers may only view transaction records associated with their own accounts.

## BR-TXN-004 — Immutable Financial History

Historical completed financial transaction records shall not be directly modified by normal customers.

## BR-TXN-005 — Reversal

Financial corrections shall normally be represented through a new reversal transaction rather than deleting the original transaction.

## BR-TXN-006 — Timestamp

Every transaction shall include a system-generated timestamp.

## BR-TXN-007 — Transaction Reference

Every financial transaction shall have a unique reference identifier.

## BR-TXN-008 — Balance Consistency

Transaction history must remain consistent with the corresponding account balance changes.

---

# 12. Loan Rules

## BR-LOAN-001 — KYC Requirement

A customer must have approved KYC before submitting a loan application.

## BR-LOAN-002 — Loan Amount

Loan applications must request an amount greater than zero.

## BR-LOAN-003 — Application Statuses

Loan application statuses may include:

```text
PENDING
UNDER_REVIEW
APPROVED
REJECTED
CANCELLED
```

## BR-LOAN-004 — Authorization

Only authorized bank employees may approve or reject loan applications.

## BR-LOAN-005 — Approval Audit

Loan approvals and rejections must generate audit events.

## BR-LOAN-006 — Repayment

Successful loan repayments shall reduce the outstanding loan balance correctly.

## BR-LOAN-007 — Overpayment

A repayment must not cause the outstanding loan balance to become negative.

## BR-LOAN-008 — Loan Ownership

Customers shall only view loan information associated with their own customer profile.

---

# 13. Notification Rules

## BR-NOTIF-001 — Financial Events

Configured financial events may generate customer notifications.

## BR-NOTIF-002 — Security Events

Security-sensitive events shall generate notifications where configured.

Examples include:

```text
Password changed
New login
Account locked
MFA changed
Suspicious activity
```

## BR-NOTIF-003 — Notification Ownership

Customers shall only access notifications belonging to their own user account.

## BR-NOTIF-004 — Notification State

Notifications shall support:

```text
READ
UNREAD
```

---

# 14. Authorization Rules

## BR-AUTHZ-001 — Customer Access

Customers may access only resources that belong to them.

## BR-AUTHZ-002 — Employee Access

Bank employees may access only functionality permitted by their assigned role.

## BR-AUTHZ-003 — Admin Access

Administrative endpoints shall reject unauthorized users.

## BR-AUTHZ-004 — Server-Side Enforcement

Authorization must be enforced by the backend.

Hiding UI elements alone is not considered valid authorization.

## BR-AUTHZ-005 — Unauthorized Response

Unauthenticated API requests to protected resources should normally return:

```text
401 Unauthorized
```

## BR-AUTHZ-006 — Forbidden Response

Authenticated users attempting an operation they are not authorized to perform should normally receive:

```text
403 Forbidden
```

## BR-AUTHZ-007 — Direct URL Access

Users shall not bypass permissions by manually navigating to restricted URLs.

## BR-AUTHZ-008 — Direct API Access

Users shall not bypass UI restrictions by directly calling restricted backend APIs.

---

# 15. Audit Rules

## BR-AUDIT-001 — Audit Timestamp

Audit events shall contain a trusted system timestamp.

## BR-AUDIT-002 — Acting User

Audit events shall identify the acting user or system process where applicable.

## BR-AUDIT-003 — Action

Audit entries shall identify the action performed.

## BR-AUDIT-004 — Target

Audit entries shall identify the affected resource where applicable.

## BR-AUDIT-005 — Financial Events

Financial transactions shall generate appropriate audit records.

## BR-AUDIT-006 — Administrative Events

Sensitive administrative actions must generate audit records.

## BR-AUDIT-007 — Security Events

Security-sensitive operations must generate audit records.

## BR-AUDIT-008 — Audit Protection

Normal customers shall not modify or delete audit records.

## BR-AUDIT-009 — Audit Failure

Failure to create an audit record for critical operations shall be handled according to system policy and must not silently corrupt financial consistency.

---

# 16. Administration Rules

## BR-ADMIN-001 — Customer Search

Only authorized bank employees may search customer records through administrative functionality.

## BR-ADMIN-002 — Account Freeze

Only authorized roles may freeze customer accounts.

## BR-ADMIN-003 — Account Unfreeze

Only authorized roles may unfreeze eligible customer accounts.

## BR-ADMIN-004 — KYC Decisions

Only authorized employees may approve or reject KYC applications.

## BR-ADMIN-005 — Loan Decisions

Only authorized employees may approve or reject loan applications.

## BR-ADMIN-006 — Administrative Audit

Sensitive administrative changes shall generate audit records.

## BR-ADMIN-007 — Customer Restrictions

Normal customer accounts shall not access administrative pages or APIs.

---

# 17. Database Rules

## BR-DB-001 — Primary Keys

Core entities shall have unique primary keys.

## BR-DB-002 — Foreign Keys

Entity relationships shall maintain referential integrity.

## BR-DB-003 — Orphan Records

The database shall prevent invalid orphan financial records.

## BR-DB-004 — Financial Data Type

Financial amounts shall use fixed-point decimal data types.

Example:

```sql
DECIMAL(19,4)
```

## BR-DB-005 — Database Transaction Rollback

If a financial operation fails before completion, all related database changes shall roll back.

## BR-DB-006 — Unique Transaction References

Database constraints or equivalent protections shall ensure transaction-reference uniqueness.

## BR-DB-007 — Historical Integrity

Deleting or disabling customers or accounts must not destroy required financial transaction history.

## BR-DB-008 — Account Ownership Integrity

Every account owner reference must point to a valid customer.

## BR-DB-009 — Transaction Relationship Integrity

Transactions must reference valid related accounts and entities where applicable.

## BR-DB-010 — Null Restrictions

Mandatory financial and identity fields shall enforce appropriate non-null constraints.

---

# 18. Date and Time Rules

## BR-TIME-001 — Database Time Storage

System timestamps shall be stored consistently.

UTC shall be preferred for persistent storage.

## BR-TIME-002 — User Display

Dates and times may be converted to the customer's configured or local timezone for display.

## BR-TIME-003 — Scheduled Operations

Scheduled transactions shall use a clearly defined timezone and must not depend on the user's browser clock.

## BR-TIME-004 — Server Authority

Security and financial expiration rules shall rely on trusted server time.

## BR-TIME-005 — Transaction Timestamp

Financial timestamps shall be generated by a trusted backend or database component rather than customer-controlled input.

---

# 19. API Rules

## BR-API-001 — Validation

API endpoints shall validate required request fields.

## BR-API-002 — Invalid Input

Malformed or invalid requests shall return an appropriate `4xx` response.

## BR-API-003 — Authentication

Protected endpoints require valid authentication.

## BR-API-004 — Authorization

Authenticated requests require appropriate authorization for the requested operation and resource.

## BR-API-005 — Sensitive Fields

API responses shall not expose:

```text
Passwords
Password hashes
OTP secrets
Internal authentication secrets
Authentication tokens belonging to other sessions
Full sensitive card information
Private system configuration
```

## BR-API-006 — Server Errors

Unexpected backend failures shall return an appropriate `5xx` response without exposing stack traces or sensitive internal implementation details.

## BR-API-007 — Content Validation

APIs shall validate supported content types and request formats.

## BR-API-008 — Resource Ownership

Changing a resource identifier in an API request must not allow a customer to access another customer's data.

## BR-API-009 — HTTP Methods

API endpoints shall use appropriate HTTP methods for their intended operations.

## BR-API-010 — Response Status

API endpoints shall return status codes appropriate to the operation outcome.

---

# 20. Financial Integrity Rules

## BR-FIN-001 — No Floating Point

Financial arithmetic must not use floating-point calculations where precision loss could affect monetary values.

## BR-FIN-002 — Atomic Financial Transactions

Financial operations involving multiple related account updates must execute atomically.

## BR-FIN-003 — Balance Conservation

For an internal transfer without fees:

```text
Amount debited from sender
=
Amount credited to recipient
```

## BR-FIN-004 — Fee Accounting

Where fees apply, fees shall be separately and correctly accounted for.

## BR-FIN-005 — No Silent Data Loss

Financial operations must never silently discard or overwrite transaction data.

## BR-FIN-006 — Traceability

Every completed financial operation shall be traceable through a unique transaction reference.

## BR-FIN-007 — Failure Consistency

System failures, network interruptions, application exceptions, or database errors shall not leave financial data in a partially completed state.

## BR-FIN-008 — Transaction Reversal

A reversal shall preserve the original transaction history and create a corresponding reversal record.

## BR-FIN-009 — Precision

Financial calculations shall preserve the configured currency precision.

## BR-FIN-010 — Integrity Across Layers

Financial data exposed through the UI and APIs shall remain consistent with persisted database values.

---

# 21. Validation Rules

## BR-VAL-001 — Required Fields

Required fields shall reject missing values.

## BR-VAL-002 — Blank Input

Fields requiring meaningful text shall reject invalid blank values where applicable.

## BR-VAL-003 — Maximum Length

Input fields shall enforce configured maximum lengths.

## BR-VAL-004 — Format Validation

Structured values such as email addresses, phone numbers, account numbers, and dates shall be validated against supported formats.

## BR-VAL-005 — Server-Side Validation

Critical validation must be performed by the backend even when equivalent client-side validation exists.

## BR-VAL-006 — Invalid Numeric Values

Financial inputs shall reject unsupported values including:

```text
Negative values
Zero where prohibited
Invalid decimal formats
Values exceeding configured limits
Non-numeric values
```

---

# 22. Error Handling Rules

## BR-ERR-001 — User-Friendly Errors

Customer-facing errors shall provide useful information without exposing sensitive system internals.

## BR-ERR-002 — No Stack Traces

Stack traces shall not be exposed to normal application users.

## BR-ERR-003 — Financial Failure

An application error occurring during a financial operation shall not leave partial financial changes.

## BR-ERR-004 — Retry Safety

Retrying a failed or uncertain financial request must not unintentionally create duplicate financial transactions.

---

# 23. Security Rules

## BR-SEC-001 — Sensitive Transport

Sensitive application traffic shall use secure transport in deployed environments.

## BR-SEC-002 — Authentication Tokens

Authentication tokens shall not be exposed unnecessarily.

## BR-SEC-003 — Server Authorization

All protected resources shall enforce authorization on the server.

## BR-SEC-004 — Session Invalidation

Expired or terminated sessions shall no longer access protected resources.

## BR-SEC-005 — Input Security

Backend endpoints shall safely handle malicious or malformed input.

## BR-SEC-006 — Sensitive Logging

Sensitive credentials and secrets shall not be written to logs.

## BR-SEC-007 — Cross-User Isolation

One authenticated customer shall never be able to obtain another customer's protected data by manipulating request parameters.

---

# 24. Initial Configurable Values

The initial system implementation shall use the following values for testing:

| Configuration                |    Initial Value |
| ---------------------------- | ---------------: |
| Failed login attempts        |                5 |
| Account lock period          |       15 minutes |
| OTP length                   |         6 digits |
| OTP expiration               |        5 minutes |
| OTP maximum attempts         |                5 |
| Session inactivity timeout   |       15 minutes |
| Password-reset expiration    |       30 minutes |
| Minimum password length      |    12 characters |
| Maximum password length      |   128 characters |
| Minimum transfer amount      |         0.01 EGP |
| Maximum single transfer      |      100,000 EGP |
| Daily transfer limit         |      250,000 EGP |
| Overdraft                    |         Disabled |
| Supported account types      | Current, Savings |
| Recurring transfer frequency |  Weekly, Monthly |

These values shall eventually be configurable rather than permanently hard-coded.

---

# 25. Business Rule Traceability

Business rules shall be traceable to requirements and tests.

Example:

```text
Requirement:
TRF-005

Business Rule:
BR-TRF-003

Test Scenario:
TS-TRF-005

Test Case:
TC-TRF-005

UI Automation:
TRF-005

API Test:
TRF-005

Database Validation:
TRF-005
```

A single requirement may reference multiple business rules, and a single business rule may be validated by multiple test cases.

---

# 26. Business Rule Categories

| Prefix   | Category            |
| -------- | ------------------- |
| BR-AUTH  | Authentication      |
| BR-PWD   | Password            |
| BR-KYC   | Customer / KYC      |
| BR-ACC   | Accounts            |
| BR-BEN   | Beneficiaries       |
| BR-TRF   | Transfers           |
| BR-CON   | Concurrency         |
| BR-PAY   | Payments            |
| BR-CARD  | Cards               |
| BR-TXN   | Transactions        |
| BR-LOAN  | Loans               |
| BR-NOTIF | Notifications       |
| BR-AUTHZ | Authorization       |
| BR-AUDIT | Audit               |
| BR-ADMIN | Administration      |
| BR-DB    | Database            |
| BR-TIME  | Date / Time         |
| BR-API   | API                 |
| BR-FIN   | Financial Integrity |
| BR-VAL   | Validation          |
| BR-ERR   | Error Handling      |
| BR-SEC   | Security            |

<!-- NOVABANK-BUSINESS-RULES-SYNC-START -->

## Current Build Business Rules

### Sessions and Authorization

1. Users can enter the QA application as customer or admin.
2. Customer sessions must not expose the Admin navigation module.
3. Admin sessions may access the Admin console.
4. Protected API resources require an authorized demo session.
5. Logout must remove active browser session state.

### Accounts

1. The deterministic customer contains Checking and Savings accounts.
2. Displayed account identifiers must remain masked.
3. Current balances are represented in USD.

### Transfers

1. Transfer amount must be greater than `0`.
2. Maximum single transfer is `$10,000`.
3. `$10,000` is a valid boundary.
4. `$10,000.01` must be rejected.
5. Transfer amount must not exceed available source-account balance.
6. Successful transfers must generate transaction activity.

### Bills

1. Bill amount must be greater than `0`.
2. Bill amount must not exceed the available Checking-account balance.
3. Minimum valid amount is `$0.01`.
4. Current billers are Electricity, Water, Internet, and Mobile.

### Cards

1. Card numbers must remain masked.
2. Supported states are `active` and `frozen`.
3. Active cards may be frozen.
4. Frozen cards may be unfrozen.
5. Card-control labels must reflect current state.

### Loans

1. Minimum application amount is `$1,000`.
2. Maximum application amount is `$50,000`.
3. `$999` is invalid.
4. `$50,001` is invalid.
5. Supported terms are 12, 24, and 36 months.
6. Valid applications enter an under-review state.

### Admin

Admin console access requires the admin role.

Seeded admin metrics:

| Metric | Value |
|---|---:|
| Customers | 1,248 |
| Accounts | 1,984 |
| Transactions today | 378 |
| Total deposits | 8,420,000 |
| Flagged transactions | 7 |

### Legacy / Intended Rules

Business rules related to MFA, beneficiary management, account creation, account controls, statements, and persistent database operations remain part of the intended full banking scope and are blocked against the current build.

<!-- NOVABANK-BUSINESS-RULES-SYNC-END -->