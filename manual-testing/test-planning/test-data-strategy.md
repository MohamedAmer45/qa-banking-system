# Banking System — Test Data Strategy

## 1. Document Information

| Field     | Value                          |
| --------- | ------------------------------ |
| Project   | Banking System Testing Project |
| Document  | Test Data Strategy             |
| Version   | 1.0                            |
| Status    | Draft                          |
| Owner     | QA Engineering                 |
| Data Type | Synthetic Test Data Only       |

---

# 2. Purpose

This document defines the strategy for creating, organizing, using, resetting, and protecting test data across the Banking System Testing Project.

The same logical test data model should support:

* Manual testing
* Selenium
* Cypress
* Playwright
* Jest
* Postman
* REST Assured
* SQL testing
* JMeter
* Cucumber
* GitHub Actions
* Jenkins

The objective is to ensure that tests are:

* Repeatable
* Predictable
* Independent
* Easy to understand
* Easy to reset
* Safe to store
* Suitable for automation
* Suitable for negative and boundary testing

---

# 3. Test Data Principles

The Banking System will use synthetic data only.

The project must not contain:

* Real customer names tied to real banking information
* Real bank account numbers
* Real card numbers
* Real passwords
* Production authentication tokens
* Production API keys
* Real identity documents
* Real transaction data

All test data must be created exclusively for testing purposes.

---

# 4. Test Data Categories

The project will use the following primary categories:

1. Customer data
2. Authentication data
3. Account data
4. Beneficiary data
5. Transfer data
6. Payment data
7. Transaction data
8. Card data
9. Loan data
10. Deposit data
11. Administrator data
12. Notification data
13. Boundary test data
14. Invalid test data
15. Performance test data
16. Security-oriented test data

---

# 5. Naming Convention

Test users should use easy-to-recognize identifiers.

Example:

```text
customer_active_01
customer_low_balance_01
customer_frozen_01
customer_locked_01
customer_multi_account_01
customer_loan_01
customer_deposit_01

admin_full_01
admin_limited_01
```

Email format:

```text
customer.active01@banktest.local
customer.lowbalance01@banktest.local
customer.frozen01@banktest.local
admin.full01@banktest.local
```

These addresses are synthetic and should not represent real external mailboxes.

---

# 6. Standard Password Strategy

Passwords should be defined through test environment configuration.

Example placeholder:

```text
TEST_PASSWORD
```

Actual passwords must not be hardcoded in the public repository.

For local execution:

```text
.env
```

may contain:

```text
TEST_PASSWORD=example-local-password
```

The `.env` file must be excluded from Git.

A safe template may be committed:

```text
.env.example
```

with:

```text
TEST_PASSWORD=
ADMIN_PASSWORD=
```

---

# 7. Core Customer Test Data

The project should maintain a predictable group of customers.

## CUSTOMER-001 — Standard Active Customer

Purpose:

Primary positive testing account.

```text
Customer ID: CUST-001
Username: customer_active_01
Email: customer.active01@banktest.local
Status: ACTIVE
KYC Status: VERIFIED
MFA Enabled: YES
Risk Level: LOW
```

Expected characteristics:

* Can log in
* Can transfer money
* Can manage beneficiaries
* Can use cards
* Can view statements
* Can change profile settings

---

## CUSTOMER-002 — Low-Balance Customer

Purpose:

Insufficient funds testing.

```text
Customer ID: CUST-002
Username: customer_low_balance_01
Email: customer.lowbalance01@banktest.local
Status: ACTIVE
KYC Status: VERIFIED
MFA Enabled: YES
Risk Level: LOW
```

Expected characteristics:

* Account active
* Small available balance
* Used for insufficient-funds scenarios

---

## CUSTOMER-003 — Frozen Account Customer

Purpose:

Account restriction testing.

```text
Customer ID: CUST-003
Username: customer_frozen_01
Status: ACTIVE
KYC Status: VERIFIED
```

Account state:

```text
FROZEN
```

Expected:

Customer may be able to log in and view information but must not perform prohibited financial operations.

---

## CUSTOMER-004 — Locked Authentication Customer

Purpose:

Authentication lockout testing.

```text
Customer ID: CUST-004
Username: customer_locked_01
Customer Status: ACTIVE
Authentication Status: LOCKED
```

Expected:

Correct password should still fail while the authentication lock is active.

---

## CUSTOMER-005 — Multiple Account Customer

Purpose:

Own-account transfer testing.

```text
Customer ID: CUST-005
Username: customer_multi_account_01
Status: ACTIVE
```

Accounts:

```text
Checking Account
Savings Account
```

Used for:

* Own-account transfers
* Account switching
* Consolidated dashboard validation

---

## CUSTOMER-006 — Loan Customer

Purpose:

Loan functionality.

```text
Customer ID: CUST-006
Username: customer_loan_01
Status: ACTIVE
Active Loan: YES
```

Used for:

* Repayment
* Installment validation
* Outstanding balance
* Loan history

---

## CUSTOMER-007 — Deposit Customer

Purpose:

Deposit testing.

```text
Customer ID: CUST-007
Username: customer_deposit_01
Status: ACTIVE
Active Deposit: YES
```

Used for:

* Deposit balance
* Interest
* Maturity
* Withdrawal restrictions

---

## CUSTOMER-008 — Restricted Customer

Purpose:

Partial account restriction testing.

```text
Customer ID: CUST-008
Username: customer_restricted_01
Customer Status: RESTRICTED
```

Expected behavior depends on business rules.

Possible allowed operations:

* Login
* View accounts
* View transaction history

Possible restricted operations:

* Transfers
* Payments
* Card operations

---

## CUSTOMER-009 — Unverified Customer

Purpose:

KYC restriction testing.

```text
Customer ID: CUST-009
Username: customer_unverified_01
Status: ACTIVE
KYC Status: PENDING
```

Used to validate functionality restricted until identity verification is complete.

---

## CUSTOMER-010 — Closed Customer Account

Purpose:

Closed-account behavior.

```text
Customer ID: CUST-010
Username: customer_closed_account_01
Customer Status: ACTIVE
Account Status: CLOSED
```

Expected:

No new financial transactions can be created from the closed account.

---

# 8. Account Test Data

Each customer account should have a stable logical test ID.

Example:

| Test ID | Owner    | Type     | Currency |   Balance | Status |
| ------- | -------- | -------- | -------- | --------: | ------ |
| ACC-001 | CUST-001 | Checking | EGP      | 50,000.00 | Active |
| ACC-002 | CUST-002 | Checking | EGP      |     50.00 | Active |
| ACC-003 | CUST-003 | Checking | EGP      | 25,000.00 | Frozen |
| ACC-004 | CUST-005 | Checking | EGP      | 20,000.00 | Active |
| ACC-005 | CUST-005 | Savings  | EGP      | 80,000.00 | Active |
| ACC-006 | CUST-010 | Checking | EGP      |      0.00 | Closed |

---

# 9. Account Number Strategy

Actual account numbers should be generated by the application or seed process.

Tests should avoid relying directly on environment-specific database IDs.

Prefer logical references such as:

```text
ACC-001
ACC-002
ACC-003
```

Automation can resolve these logical IDs to actual environment data.

This reduces brittle test dependencies.

---

# 10. Balance Profiles

Several balance profiles should exist.

## Zero Balance

```text
0.00
```

Purpose:

* Minimum balance scenarios
* Transfer rejection

## Very Low Balance

```text
0.50
```

Purpose:

* Small-value and insufficient-funds testing

## Low Balance

```text
50.00
```

## Normal Balance

```text
5,000.00
```

## High Balance

```text
100,000.00
```

## Very High Test Balance

```text
1,000,000.00
```

Used only when required for upper-limit scenarios.

---

# 11. Beneficiary Test Data

Beneficiary records should include different states.

## BEN-001 — Valid Active Beneficiary

```text
Beneficiary ID: BEN-001
Name: Test Beneficiary One
Status: ACTIVE
Account Status: ACTIVE
```

---

## BEN-002 — Newly Added Beneficiary

```text
Status: PENDING_ACTIVATION
```

Purpose:

Validate cooldown or activation behavior if implemented.

---

## BEN-003 — Disabled Beneficiary

```text
Status: DISABLED
```

Expected:

Cannot receive transfer through normal customer workflow.

---

## BEN-004 — Closed Destination Account

```text
Beneficiary Status: ACTIVE
Destination Account Status: CLOSED
```

Used to validate downstream account-state enforcement.

---

## BEN-005 — Duplicate Beneficiary Data

Uses the same destination account as an existing beneficiary.

Purpose:

Validate duplicate prevention.

---

# 12. Transfer Test Data

Transfer tests should use reusable amount categories.

## Standard Amount

```text
100.00
```

## Small Valid Amount

```text
1.00
```

## Exact Available Balance

Example:

```text
Available Balance: 5000.00
Transfer Amount: 5000.00
```

Used to verify whether fees or minimum retained balance affect the result.

---

## Amount Greater Than Balance

```text
Available Balance: 5000.00
Transfer Amount: 5000.01
```

Expected:

Rejected when overdraft is not supported.

---

# 13. Transfer Boundary Data

Assume an example business rule:

```text
Minimum transfer amount = 1.00
Maximum transfer amount = 100,000.00
```

Boundary data:

```text
0.99
1.00
1.01

99,999.99
100,000.00
100,000.01
```

The exact limits must be updated if the business rules define different values.

---

# 14. Invalid Transfer Amount Data

Include:

```text
0
-1
-100
100000.01
999999999999
abc
!
1e10
blank
spaces only
```

Also test:

```text
1.999
100.555
```

to validate decimal precision rules.

---

# 15. Payment Test Data

Payment data should cover:

* Valid payee
* Invalid payee
* Expired bill
* Already-paid bill
* Amount below allowed minimum
* Amount above allowed maximum
* Duplicate payment
* Payment from frozen account

Example:

```text
PAYEE-001
Type: Utility
Status: ACTIVE
Reference: TEST-BILL-001
Amount Due: 350.00
```

---

# 16. Transaction Test Data

Transaction history should contain multiple types:

```text
Credit
Debit
Transfer
Payment
Card Transaction
Loan Repayment
Deposit Interest
Fee
Reversal
```

Statuses:

```text
PENDING
COMPLETED
FAILED
CANCELLED
REVERSED
```

This allows testing:

* Filtering
* Sorting
* Search
* Status display
* Statement generation

---

# 17. Card Test Data

## CARD-001 — Active Card

```text
Owner: CUST-001
Type: Debit
Status: ACTIVE
```

---

## CARD-002 — Frozen Card

```text
Status: FROZEN
```

---

## CARD-003 — Blocked Card

```text
Status: BLOCKED
```

---

## CARD-004 — Expired Card

```text
Status: EXPIRED
```

---

## CARD-005 — Inactive Card

```text
Status: INACTIVE
```

Used for activation testing.

---

# 18. Card Number Handling

Full sensitive card data must not be exposed unnecessarily.

UI should normally display masked values such as:

```text
**** **** **** 1234
```

Test documentation should use only synthetic values.

Never commit production PANs, CVVs, PINs, or payment credentials.

---

# 19. Card Limit Data

Example limits:

```text
Daily Purchase Limit: 20,000.00
Daily Withdrawal Limit: 10,000.00
Online Purchase Limit: 5,000.00
```

Boundary testing should include:

```text
Limit - 0.01
Exact Limit
Limit + 0.01
```

---

# 20. Loan Test Data

Loan data should cover different lifecycle states.

## LOAN-001 — Active Loan

```text
Customer: CUST-006
Principal: 100,000.00
Status: ACTIVE
Installments Remaining: 24
```

---

## LOAN-002 — Pending Application

```text
Status: PENDING
```

---

## LOAN-003 — Approved Application

```text
Status: APPROVED
```

---

## LOAN-004 — Rejected Application

```text
Status: REJECTED
```

---

## LOAN-005 — Fully Repaid Loan

```text
Status: CLOSED
Outstanding Balance: 0.00
```

---

# 21. Loan Boundary Data

Example loan amount rule:

```text
Minimum: 10,000
Maximum: 1,000,000
```

Boundary cases:

```text
9,999
10,000
10,001

999,999
1,000,000
1,000,001
```

---

# 22. Deposit Test Data

## DEP-001 — Active Deposit

```text
Customer: CUST-007
Principal: 50,000.00
Status: ACTIVE
```

---

## DEP-002 — Matured Deposit

```text
Status: MATURED
```

---

## DEP-003 — Closed Deposit

```text
Status: CLOSED
```

---

# 23. Authentication Test Data

Authentication test data should cover:

```text
Valid username + valid password
Valid username + invalid password
Invalid username + valid-format password
Empty username
Empty password
Both fields empty
Locked user
Disabled user
Expired password
Unverified user
```

---

# 24. Password Boundary Data

If the password rule is:

```text
Minimum length: 8
Maximum length: 64
```

Test:

```text
7 characters
8 characters
9 characters

63 characters
64 characters
65 characters
```

Also cover:

```text
No uppercase
No lowercase
No number
No special character
Spaces
Leading space
Trailing space
Unicode
```

Adjust these cases to the actual password policy.

---

# 25. OTP Test Data

OTP scenarios should include:

```text
Valid OTP
Invalid OTP
Expired OTP
Already-used OTP
Too many OTP attempts
Empty OTP
Short OTP
Long OTP
Non-numeric OTP
```

Automation should avoid relying on hardcoded production-like OTPs.

Test environments should provide a deterministic mechanism for retrieving or bypassing test OTPs safely.

---

# 26. Administrator Test Data

## ADMIN-001 — Full Administrator

```text
Role: SUPER_ADMIN
Status: ACTIVE
```

Permissions may include:

* Customer management
* Account management
* Transaction review
* Card management
* Loan management
* Account restrictions
* Audit logs

---

## ADMIN-002 — Limited Administrator

```text
Role: SUPPORT_ADMIN
Status: ACTIVE
```

Restricted from high-risk operations.

Used for role-based authorization testing.

---

## ADMIN-003 — Disabled Administrator

```text
Status: DISABLED
```

Expected:

Cannot authenticate.

---

# 27. Role-Based Data Matrix

| Role          | Accounts         | Transfers      | Cards          | Loans          | Admin Functions | Audit Logs |
| ------------- | ---------------- | -------------- | -------------- | -------------- | --------------- | ---------- |
| Customer      | Own only         | Allowed        | Own only       | Own only       | No              | No         |
| Support Admin | View limited     | Restricted     | Limited        | Limited        | Partial         | Limited    |
| Full Admin    | Authorized scope | Administrative | Administrative | Administrative | Yes             | Yes        |

The exact permissions must align with implementation requirements.

---

# 28. Profile Test Data

Profile fields should include representative values.

Example:

```text
First Name: Mohamed
Last Name: TestUser
Phone: +201000000001
City: Cairo
Country: Egypt
```

Also create test values for:

* Minimum-length name
* Maximum-length name
* Hyphenated name
* Apostrophe
* Arabic characters
* Long address
* Invalid phone number
* Invalid email

---

# 29. International Character Data

Where supported, test Unicode data.

Examples:

```text
محمد
أحمد
José
François
Müller
李明
```

Purpose:

Validate character encoding, storage, search, and display.

---

# 30. Input Validation Dataset

Reusable invalid inputs should include:

```text
''
' '
'   '
null
undefined
<script>alert(1)</script>
' OR '1'='1
../../../etc/passwd
AAAAAAAA...[very long string]
<>&"'/
```

These inputs should be used only in controlled test environments.

The goal is to validate application input handling and safe error behavior.

---

# 31. Date Test Data

Dates should cover:

```text
Today's date
Yesterday
Tomorrow
End of month
Start of month
End of year
Leap day
Past date
Far-future date
Invalid date
```

Example leap-day values:

```text
2024-02-29
2025-02-29
```

Expected:

The valid leap date is accepted where relevant, while invalid dates are rejected.

---

# 32. Statement Test Data

Transaction history should be prepared across:

* One day
* One week
* One month
* Three months
* One year

This allows validation of statement date filters.

Include accounts with:

```text
0 transactions
1 transaction
50 transactions
500+ transactions
```

---

# 33. Notification Test Data

Notification categories:

```text
LOGIN
SECURITY
TRANSFER
PAYMENT
CARD
LOAN
ACCOUNT
PROFILE
```

Statuses:

```text
UNREAD
READ
```

Data should support:

* Empty notification state
* Single notification
* Large number of notifications

---

# 34. Boundary Value Dataset

Maintain reusable boundary values.

## Generic Integer

```text
-1
0
1
```

## Percentage

```text
-0.01
0
0.01
99.99
100
100.01
```

## Money

```text
-0.01
0
0.01
0.99
1.00
1.01
99999.99
100000.00
100000.01
```

---

# 35. Equivalence Classes

For each major field, define equivalence groups.

Example transfer amount:

## Valid

```text
1.00 to 100,000.00
```

## Invalid Low

```text
< 1.00
```

## Invalid High

```text
> 100,000.00
```

## Invalid Type

```text
Text
Symbols
Blank
Null
```

---

# 36. Account State Dataset

Accounts should exist in all relevant states:

```text
ACTIVE
FROZEN
RESTRICTED
SUSPENDED
CLOSED
```

This dataset will support state-transition and authorization testing.

---

# 37. Card State Dataset

Cards should exist in:

```text
INACTIVE
ACTIVE
FROZEN
BLOCKED
EXPIRED
CANCELLED
```

---

# 38. Transaction State Dataset

Transactions should cover:

```text
CREATED
PENDING
PROCESSING
COMPLETED
FAILED
CANCELLED
REVERSED
```

Only statuses supported by the application should remain in the final dataset.

---

# 39. Performance Test Data

Performance testing must not rely on a single user.

Create scalable data such as:

```text
PERF-CUST-0001
PERF-CUST-0002
PERF-CUST-0003
...
PERF-CUST-1000
```

Each performance user should have:

* Unique credentials
* Account
* Stable balance
* Valid data
* Required permissions

---

# 40. Performance Account Balances

Load-testing users should have sufficiently large synthetic balances so expected transactions do not fail simply because earlier iterations consumed the balance.

Example:

```text
1,000,000.00
```

Alternatively, the performance environment should reset balances between runs.

---

# 41. Data for Concurrency Testing

Create dedicated accounts for race-condition testing.

Example:

```text
CONC-ACC-001
Starting Balance: 1000.00
```

Concurrent requests:

```text
Transfer A: 800.00
Transfer B: 500.00
```

Expected:

The system must preserve financial rules and never allow an unintended negative balance.

---

# 42. Duplicate Request Testing Data

Use a dedicated test customer and account when testing:

* Double clicks
* Request retries
* Idempotency keys
* Duplicate API submissions

This avoids corrupting standard regression data.

---

# 43. API Test Data Strategy

Postman and REST Assured should use:

* Environment variables
* Dynamic IDs
* Generated data
* Reusable logical aliases

Example:

```text
{{baseUrl}}
{{customerUsername}}
{{customerPassword}}
{{sourceAccountId}}
{{beneficiaryId}}
{{authToken}}
```

Dynamic data should be captured from previous responses rather than manually hardcoded where possible.

---

# 44. UI Automation Data Strategy

Selenium, Cypress, and Playwright should avoid tightly coupling tests to one permanent user when possible.

Preferred approaches:

1. Seed required state before the test.
2. Create data through APIs.
3. Reset data after execution.
4. Use dedicated reusable users only for stable smoke scenarios.

---

# 45. Selenium Data Strategy

Selenium should load configuration from properties or environment variables.

Example:

```text
base.url
customer.username
customer.password
```

Sensitive values should not be committed.

TestNG DataProviders may later provide:

* User roles
* Transfer amounts
* Invalid login data
* Browser combinations

---

# 46. Cypress Data Strategy

Cypress may use:

```text
fixtures/
```

for static, non-sensitive data.

Examples:

```text
customers.json
transfer-data.json
invalid-inputs.json
```

Secrets should use environment configuration rather than fixtures.

---

# 47. Playwright Data Strategy

Playwright can use:

```text
fixtures
storageState
environment variables
API setup
```

Authentication state can be reused for test speed, but tests must still validate authentication separately.

---

# 48. Jest Data Strategy

Jest should use deterministic test datasets.

For example:

```text
calculateFee(100)
calculateFee(1000)
calculateInterest(...)
```

Financial logic tests should avoid random values unless random generation is seeded and reproducible.

---

# 49. SQL Test Data Strategy

Database testing should identify rows through logical values rather than unstable internal IDs.

Example:

Prefer:

```sql
SELECT *
FROM customers
WHERE email = 'customer.active01@banktest.local';
```

over relying on:

```sql
WHERE id = 14982;
```

unless database seed IDs are guaranteed.

---

# 50. JMeter Data Strategy

JMeter should use CSV datasets.

Example:

```text
username,password,accountId,beneficiaryId
perf_user_001,...,...,...
perf_user_002,...,...,...
perf_user_003,...,...,...
```

Credentials must be synthetic.

Sensitive files can be excluded from Git and generated during environment setup.

---

# 51. Cucumber Data Strategy

BDD scenarios should describe business-relevant data rather than technical database IDs.

Preferred:

```gherkin
Given an active customer with a balance of 5000 EGP
```

instead of:

```gherkin
Given customer ID 8427 exists
```

This keeps scenarios readable and reusable.

---

# 52. Static vs Dynamic Data

## Static Data

Suitable for:

* Standard roles
* Account states
* Boundary values
* Stable smoke users

## Dynamic Data

Suitable for:

* Registration
* New beneficiaries
* New transfers
* New loan applications
* New transaction references

Dynamic data reduces collision between repeated executions.

---

# 53. Unique Data Generation

For fields requiring uniqueness, generate values using:

* Timestamp
* UUID
* Test execution ID
* Random suffix

Example:

```text
qa.user.20260912.001@banktest.local
```

or:

```text
qa-user-<UUID>
```

---

# 54. Random Data Rules

Random data must not make tests unpredictable.

Bad:

```text
Generate any random transfer amount.
```

Better:

```text
Generate a random amount between 100 and 500 using a fixed seed.
```

Best for most regression tests:

Use deterministic values.

---

# 55. Test Data Reset Strategy

The environment should support restoring a known state.

Possible reset methods:

* Database seed script
* API reset endpoint available only in test environments
* Container restart with seed data
* Database snapshot restore
* Test setup scripts

---

# 56. Seed Data

A future seed process should create:

```text
Customers
Accounts
Balances
Cards
Beneficiaries
Loans
Deposits
Admin users
Transactions
Notifications
```

Running the seed process should result in a predictable environment.

---

# 57. Test Isolation

Tests should avoid sharing mutable records.

Bad:

```text
TC-TRF-001 transfers money from ACC-001.

TC-TRF-002 expects ACC-001 to still contain the original balance.
```

This introduces dependency.

Preferred:

```text
Reset ACC-001 before each test.
```

or use separate accounts.

---

# 58. Data Cleanup

Tests creating records should clean up when appropriate.

Examples:

* Beneficiaries
* Loan applications
* Scheduled transfers
* Test notifications

Financial transaction history may intentionally remain immutable.

If transactions cannot be deleted by design, data reset should occur at environment level.

---

# 59. Financial Data Immutability

Completed banking transactions should generally not be manually deleted through application workflows.

Corrections should instead be represented through valid mechanisms such as:

```text
Reversal
Refund
Adjustment
```

This principle should be respected in test design.

---

# 60. Data Consistency Validation

After a financial operation, QA should validate relevant representations.

Example transfer:

```text
Source Account
Destination Account
Transaction History
Statement
API
Database
Audit Log
Notification
```

All should represent the same valid transaction state.

---

# 61. Example Transfer Dataset

Initial state:

```text
Customer A: CUST-001
Source Account: ACC-001
Source Balance: 50,000.00

Customer B: CUST-011
Destination Account: ACC-011
Destination Balance: 10,000.00

Transfer: 1,000.00
Fee: 10.00
```

Expected:

```text
Source Balance = 48,990.00
Destination Balance = 11,000.00
```

Expected transaction records:

```text
Source debit = 1,010.00
Destination credit = 1,000.00
Fee record = 10.00
```

if the application's accounting model separates the fee.

---

# 62. Financial Precision Dataset

Include decimal values such as:

```text
0.01
0.10
1.11
10.99
100.55
999.99
```

Testing should detect issues such as:

```text
100.10 being represented as 100.099999...
```

Financial calculations should use appropriate decimal handling.

---

# 63. Currency Data

If multiple currencies are implemented, test data should include:

```text
EGP
USD
EUR
GBP
```

Tests should validate:

* Currency display
* Currency-specific accounts
* Exchange rules
* Decimal precision
* Transfer restrictions
* Unsupported currency combinations

If the project implements only EGP initially, additional currency tests remain future scope.

---

# 64. Time-Dependent Data

Time-based features should include data for:

* Scheduled transfers
* Recurring transfers
* OTP expiration
* Session expiration
* Card expiry
* Loan installments
* Deposit maturity

Avoid tests that depend on manually waiting for long periods.

The test environment should allow controllable time conditions where practical.

---

# 65. Scheduled Transfer Data

Example:

```text
SCH-TRF-001
Execution Date: Tomorrow
Status: SCHEDULED
```

Also create:

```text
Past date
Current date
Far future date
Invalid date
Cancelled scheduled transfer
```

---

# 66. Recurring Transfer Data

Example:

```text
REC-TRF-001
Frequency: Monthly
Amount: 500.00
Status: ACTIVE
```

Other states:

```text
PAUSED
CANCELLED
COMPLETED
```

---

# 67. Security-Oriented Test Accounts

Create dedicated data for security validation.

Examples:

```text
SEC-CUST-A
SEC-CUST-B
SEC-ADMIN-LIMITED
```

These accounts allow testing unauthorized access attempts without affecting primary regression users.

Example:

Customer A attempts to access Customer B's account resource.

---

# 68. Test Data for Authorization

Prepare resource mappings such as:

```text
SEC-CUST-A owns:
ACC-A
CARD-A
LOAN-A

SEC-CUST-B owns:
ACC-B
CARD-B
LOAN-B
```

Authorization tests can then deliberately attempt cross-customer access.

Expected:

```text
DENIED
```

---

# 69. Invalid Identifier Data

API and UI tests should include:

```text
Nonexistent account ID
Nonexistent card ID
Nonexistent transaction ID
Malformed UUID
Empty ID
Another customer's valid ID
```

This tests both validation and authorization.

---

# 70. Search and Filtering Data

Transaction data should include enough variety to validate:

* Date filtering
* Amount filtering
* Transaction type filtering
* Status filtering
* Search
* Sorting

Example:

```text
Transaction A: 10 EGP
Transaction B: 100 EGP
Transaction C: 1000 EGP
```

with different dates and statuses.

---

# 71. Large Dataset Testing

Prepare some accounts with large histories.

Example:

```text
Account with 1,000 transactions
```

Used for:

* Pagination
* Search
* Sorting
* Statement generation
* Performance observations

---

# 72. Empty State Data

Every module should support an empty-state scenario.

Examples:

```text
Customer with no beneficiaries
Customer with no notifications
Customer with no loans
Customer with no deposits
Account with no transactions
```

---

# 73. Maximum Data Scenarios

Test data should include maximum supported cases where meaningful.

Examples:

* Maximum beneficiaries
* Maximum cards
* Maximum saved payees
* Maximum text length
* Maximum transaction history page size

These limits should be based on requirements.

---

# 74. Test Data Storage Structure

A future repository structure may contain:

```text
test-data/
│
├── common/
│   ├── users.json
│   ├── accounts.json
│   ├── beneficiaries.json
│   ├── cards.json
│   ├── invalid-inputs.json
│   └── boundaries.json
│
├── api/
│   └── request-data.json
│
├── performance/
│   └── users.csv
│
└── sql/
    └── seed-data.sql
```

Actual framework-specific fixtures may also exist within each automation project.

---

# 75. Data Ownership

QA owns:

* Test-data design
* Data requirements
* Data coverage
* Synthetic datasets
* Reset requirements

Development or DevOps may support:

* Seed scripts
* Database snapshots
* Environment resets
* Secure configuration
* Test endpoints

---

# 76. Environment-Specific Data

Logical test identities should remain consistent across environments where possible.

Example:

```text
customer_active_01
```

may exist in both:

```text
Test
Staging
```

but internal database IDs may differ.

Tests must not assume identical internal IDs across environments.

---

# 77. Data Refresh Rules

Before major regression execution:

1. Restore known environment state.
2. Seed standard customers.
3. Seed standard accounts.
4. Verify balances.
5. Verify account statuses.
6. Verify required cards.
7. Verify beneficiaries.
8. Confirm application services are healthy.
9. Begin execution.

---

# 78. Pre-Test Data Validation

Before running critical financial tests, verify:

```text
Customer exists
Account exists
Account status correct
Expected starting balance correct
Beneficiary status correct
Authentication works
```

If starting state is wrong, the test result may be invalid.

---

# 79. Post-Test Validation

After critical financial tests, validate:

```text
Final balance
Transaction record
Transaction status
Reference ID
Timestamp
Related audit record
Notification where required
```

---

# 80. Test Data Troubleshooting

When a test unexpectedly fails, investigate whether the failure originates from:

* Application defect
* Incorrect test data
* Previous test contamination
* Expired data
* Account state change
* Environment reset failure
* Duplicate data
* Incorrect permissions

Do not immediately classify every failed test as a product defect.

---

# 81. Test Data for Defect Reproduction

Defect reports should document enough test data to reproduce the issue.

Example:

```text
Customer: customer_low_balance_01
Account Alias: ACC-002
Starting Balance: 50.00
Transfer Amount: 100.00
Beneficiary: BEN-001
```

Sensitive credentials should not be included in defect reports.

---

# 82. CI/CD Data Strategy

Automated CI runs should not rely on manually prepared data.

The pipeline should eventually perform:

```text
Environment Ready
        ↓
Seed / Prepare Data
        ↓
Execute Tests
        ↓
Collect Results
        ↓
Optional Cleanup
```

This makes executions reproducible.

---

# 83. Parallel Execution Strategy

Parallel automated tests require isolated data.

Example:

Bad:

```text
10 workers all use ACC-001.
```

Better:

```text
Worker 1 → ACC-PAR-001
Worker 2 → ACC-PAR-002
Worker 3 → ACC-PAR-003
...
```

This reduces race conditions caused by the test suite itself.

---

# 84. Test Data Versioning

Seed data and static test datasets should be version-controlled.

Changes should be reviewed when:

* Business rules change
* New account states are added
* Limits change
* New roles are introduced
* New modules are added

---

# 85. Test Data Traceability

Important test cases should clearly state their required data.

Example:

```text
TC-TRF-001

Required Data:
- CUST-001
- ACC-001
- BEN-001
- Balance >= 1000 EGP
```

This makes manual and automated execution easier.

---

# 86. Recommended Baseline Dataset

The minimum reusable banking dataset should contain:

```text
10+ customers
10+ accounts
5+ beneficiaries
5+ cards
Multiple transaction states
Multiple account states
At least one active loan
At least one pending loan
At least one deposit
At least two administrator roles
Empty-state customer
High-volume transaction account
Dedicated performance users
Dedicated concurrency account
Dedicated authorization users
```

---

# 87. Test Data Security

All committed test data must be safe for public exposure.

Never commit:

```text
.env
passwords
tokens
secret keys
production URLs with credentials
database passwords
private certificates
```

Git ignore rules must cover secret-bearing files.

---

# 88. Data Masking Principle

Where sensitive-style data is displayed, validate masking.

Examples:

```text
Account Number:
****1234

Card:
**** **** **** 5678
```

Full sensitive values should only appear where explicitly required and authorized.

---

# 89. Logging and Test Data

Automated logs should avoid displaying:

* Passwords
* MFA secrets
* Full tokens
* Card security values

Where tokens must appear for debugging, they should be redacted.

Example:

```text
Authorization: Bearer eyJ...REDACTED
```

---

# 90. Test Data Success Criteria

The test data strategy is considered effective when:

* Tests can be repeated.
* Starting states are predictable.
* Test failures are reproducible.
* Tests do not depend on manual data repair.
* Parallel tests do not interfere.
* Financial states can be validated.
* Sensitive values are protected.
* Manual and automated tests can reuse the same logical datasets.

---

# 91. Final Test Data Model

The Banking System project will use the following logical model:

```text
Customer
   │
   ├── Authentication
   │
   ├── Profile
   │
   ├── Accounts
   │      │
   │      ├── Transactions
   │      ├── Beneficiaries
   │      ├── Transfers
   │      ├── Payments
   │      └── Statements
   │
   ├── Cards
   │
   ├── Loans
   │
   ├── Deposits
   │
   └── Notifications

Administrator
   │
   ├── Customer Management
   ├── Account Management
   ├── Transaction Review
   ├── Card Management
   ├── Loan Management
   └── Audit Logs
```

The same logical entities will later be referenced across manual testing, automation frameworks, API testing, SQL validation, performance testing, BDD, and CI/CD.

---

# 92. Final Principle

Reliable testing requires reliable test data.

For this Banking System, financial tests must begin from a known state and finish with a verifiable state.

A test should never pass simply because the expected result happened accidentally due to unknown existing data.

Every important financial test should therefore answer:

```text
What was the starting state?

What operation occurred?

What should the final state be?

How can the final state be verified?
```

This principle will guide all later manual, automated, API, database, and performance testing.

<!-- NOVABANK-TEST-DATA-SYNC-START -->

## Current Deterministic Test Data

### Accounts

| Type | Masked Number | Initial Balance |
|---|---|---:|
| Checking | `**** 4821` | `$12,840.75` |
| Savings | `**** 7742` | `$32,500.00` |

Initial total:

`$45,340.75`

### Seed Transactions

- Salary deposit: `$5,200`
- Electricity bill: `$86.35`
- Card purchase: `$42.90`

### Transfer Recipients

- Alex Johnson
- Sam Lee

### Billers

- Electricity
- Water
- Internet
- Mobile

### Cards

| Card | Number | Initial Status |
|---|---|---|
| Visa Debit | `**** 4242` | active |
| Virtual Card | `**** 8831` | frozen |

### Existing Loan

| Field | Value |
|---|---|
| Type | Personal Loan |
| Balance | `$6,450` |
| APR | `7.9%` |
| Next payment | `$320` |
| Due date | `2026-10-05` |

Loan range:

`$1,000 - $50,000`

Terms:

- 12 months
- 24 months
- 36 months

### Admin Data

| Metric | Value |
|---|---:|
| Customers | 1,248 |
| Accounts | 1,984 |
| Transactions today | 378 |
| Total deposits | 8,420,000 |
| Flagged transactions | 7 |

### State Management

Current authentication state:

`sessionStorage`

Current banking UI state:

`localStorage`

Tests that mutate state must isolate, reset, or restore that state where necessary.

<!-- NOVABANK-TEST-DATA-SYNC-END -->