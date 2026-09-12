# Banking System Test Data Requirements

## 1. Purpose

This document defines the test data required to validate the Banking System across:

- Manual testing
- Selenium
- Cypress
- Playwright
- Jest
- Postman
- REST Assured
- Database testing
- JMeter
- Cucumber
- CI/CD pipelines

The goal is to maintain predictable, reusable, isolated, and traceable data for positive, negative, boundary, authorization, database, concurrency, and performance testing.

No real customer or production banking data shall be used.

---

# 2. General Test Data Principles

## TD-001 — Synthetic Data Only

All project test data shall be fictional and created specifically for testing.

Real:

```text
Customer information
Bank account information
Card information
National identification numbers
Passwords
Authentication tokens
Phone numbers
Financial records
```

shall not be used.

---

## TD-002 — Environment Isolation

Test data shall be isolated from production environments.

Expected environments may include:

```text
LOCAL
TEST
CI
PERFORMANCE
```

---

## TD-003 — Repeatability

Tests shall use predictable data so that the same test can be executed repeatedly with the same expected behavior.

---

## TD-004 — Test Independence

Whenever possible, tests shall not depend on another test running successfully first.

Example:

```text
TC-TRF-001 should not require TC-BEN-001 to run immediately before it.
```

Required data should already exist or be created during test setup.

---

## TD-005 — Data Cleanup

Tests that create temporary data shall clean up that data where practical.

Examples:

```text
Temporary beneficiaries
Temporary scheduled transfers
Temporary notifications
Temporary sessions
Temporary loan applications
```

Financial transaction history shall not be deleted simply to clean up a test.

---

## TD-006 — Unique Generated Data

Tests that require unique values shall generate them dynamically.

Examples:

```text
Email address
Transaction reference
Idempotency key
External reference
Temporary beneficiary name
Test-run identifier
```

Example:

```text
qa.customer+20260912-001@example.test
```

---

## TD-007 — Secrets

Secrets shall not be committed to GitHub.

This includes:

```text
Passwords
Database passwords
API secrets
Private keys
Authentication tokens
Real OTP secrets
Connection strings containing credentials
```

Secrets shall be supplied using environment variables or approved secret-management mechanisms.

---

# 3. Standard Test User Accounts

The test environment shall contain predefined users representing important system states.

---

# 4. Customer Test Accounts

## CUSTOMER-001 — Standard Active Customer

```text
Alias: customer.standard
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
MFA: ENABLED
Failed login attempts: 0
Account locked: NO
```

Purpose:

```text
Normal positive customer flows
Dashboard
Accounts
Transfers
Payments
Cards
Transactions
Beneficiaries
Loans
Notifications
Security settings
```

Recommended test email:

```text
customer.standard@example.test
```

---

## CUSTOMER-002 — Customer With Pending KYC

```text
Alias: customer.kyc_pending
Role: CUSTOMER
Status: ACTIVE
KYC: PENDING
MFA: ENABLED
```

Purpose:

```text
KYC restrictions
Blocked external transfers
Restricted financial functionality
KYC status validation
```

Recommended email:

```text
customer.kyc.pending@example.test
```

---

## CUSTOMER-003 — Customer With Rejected KYC

```text
Alias: customer.kyc_rejected
Role: CUSTOMER
Status: ACTIVE
KYC: REJECTED
```

Purpose:

```text
Rejected KYC flows
Restricted operations
Resubmission behavior
```

---

## CUSTOMER-004 — Locked Customer

```text
Alias: customer.locked
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
Account locked: YES
```

Purpose:

```text
Login lockout
Unlock flows
Authentication restrictions
```

---

## CUSTOMER-005 — Customer With MFA Disabled

```text
Alias: customer.no_mfa
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
MFA: DISABLED
```

Purpose:

```text
MFA enrollment
Security settings
Conditional authentication flows
```

---

## CUSTOMER-006 — Customer With Multiple Accounts

```text
Alias: customer.multiple_accounts
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
Accounts:
- Current account
- Savings account
```

Purpose:

```text
Own-account transfers
Dashboard aggregation
Multiple account navigation
Account-selection testing
```

---

## CUSTOMER-007 — Customer With Frozen Bank Account

```text
Alias: customer.frozen_account
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
Primary account status: FROZEN
```

Purpose:

```text
Blocked outgoing transfers
Blocked payments
Account-state validation
```

---

## CUSTOMER-008 — Customer With Suspended Account

```text
Alias: customer.suspended_account
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
Primary account status: SUSPENDED
```

Purpose:

```text
Suspended account restrictions
```

---

## CUSTOMER-009 — Customer With Closed Account

```text
Alias: customer.closed_account
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
Primary account status: CLOSED
```

Purpose:

```text
Historical-data visibility
Blocked financial operations
```

---

## CUSTOMER-010 — Low-Balance Customer

```text
Alias: customer.low_balance
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
Available balance: 10.00 EGP
```

Purpose:

```text
Insufficient funds
Boundary transfers
Fee validation
Payment failures
```

---

## CUSTOMER-011 — Zero-Balance Customer

```text
Alias: customer.zero_balance
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
Available balance: 0.00 EGP
```

Purpose:

```text
Zero-balance transaction attempts
Insufficient funds
```

---

## CUSTOMER-012 — High-Balance Customer

```text
Alias: customer.high_balance
Role: CUSTOMER
Status: ACTIVE
KYC: APPROVED
Available balance: 500,000.00 EGP
```

Purpose:

```text
Transfer-limit validation
Daily-limit testing
Large-value transactions
Boundary analysis
```

---

# 5. Employee Test Accounts

## EMPLOYEE-001 — Standard Bank Employee

```text
Alias: employee.standard
Role: BANK_EMPLOYEE
Status: ACTIVE
```

Purpose:

```text
Customer search
Permitted back-office access
Operational authorization
```

---

## EMPLOYEE-002 — KYC Reviewer

```text
Alias: employee.kyc_reviewer
Role: BANK_EMPLOYEE
Permission set: KYC_REVIEW
```

Purpose:

```text
KYC review
KYC approval
KYC rejection
Authorization testing
```

---

## EMPLOYEE-003 — Loan Reviewer

```text
Alias: employee.loan_reviewer
Role: BANK_EMPLOYEE
Permission set: LOAN_REVIEW
```

Purpose:

```text
Loan approval
Loan rejection
Loan review
```

---

## EMPLOYEE-004 — Restricted Employee

```text
Alias: employee.restricted
Role: BANK_EMPLOYEE
Permission set: LIMITED
```

Purpose:

```text
Negative authorization testing
Employee privilege boundaries
```

---

# 6. Administrator Test Accounts

## ADMIN-001 — Standard Administrator

```text
Alias: admin.standard
Role: ADMIN
Status: ACTIVE
MFA: ENABLED
```

Purpose:

```text
Administrative dashboard
Customer administration
Account administration
System configuration
Audit access
Security administration
```

---

# 7. Bank Account Test Data

Each predefined account should have a stable alias.

Example:

```text
ACC-CUST001-CURRENT
```

Actual database IDs may vary.

---

## ACCOUNT-001 — Standard Current Account

```text
Alias: account.standard.current
Owner: customer.standard
Type: CURRENT
Status: ACTIVE
Currency: EGP
Ledger balance: 50,000.00
Available balance: 50,000.00
```

---

## ACCOUNT-002 — Standard Savings Account

```text
Alias: account.standard.savings
Owner: customer.multiple_accounts
Type: SAVINGS
Status: ACTIVE
Currency: EGP
Ledger balance: 75,000.00
Available balance: 75,000.00
```

---

## ACCOUNT-003 — Second Current Account

```text
Alias: account.multiple.current
Owner: customer.multiple_accounts
Type: CURRENT
Status: ACTIVE
Currency: EGP
Ledger balance: 25,000.00
Available balance: 25,000.00
```

Purpose:

```text
Own-account transfers
```

---

## ACCOUNT-004 — Low-Balance Account

```text
Alias: account.low_balance
Owner: customer.low_balance
Type: CURRENT
Status: ACTIVE
Currency: EGP
Ledger balance: 10.00
Available balance: 10.00
```

---

## ACCOUNT-005 — Zero-Balance Account

```text
Alias: account.zero_balance
Owner: customer.zero_balance
Type: CURRENT
Status: ACTIVE
Currency: EGP
Ledger balance: 0.00
Available balance: 0.00
```

---

## ACCOUNT-006 — High-Balance Account

```text
Alias: account.high_balance
Owner: customer.high_balance
Type: CURRENT
Status: ACTIVE
Currency: EGP
Ledger balance: 500,000.00
Available balance: 500,000.00
```

---

## ACCOUNT-007 — Frozen Account

```text
Alias: account.frozen
Owner: customer.frozen_account
Type: CURRENT
Status: FROZEN
Currency: EGP
```

---

## ACCOUNT-008 — Suspended Account

```text
Alias: account.suspended
Owner: customer.suspended_account
Type: CURRENT
Status: SUSPENDED
Currency: EGP
```

---

## ACCOUNT-009 — Closed Account

```text
Alias: account.closed
Owner: customer.closed_account
Type: CURRENT
Status: CLOSED
Currency: EGP
```

---

# 8. Transfer Boundary Data

The initial configured values are:

```text
Minimum transfer = 0.01 EGP
Maximum single transfer = 100,000.00 EGP
Daily limit = 250,000.00 EGP
```

Boundary values shall include:

|               Test Value | Purpose                            |
| -----------------------: | ---------------------------------- |
|                    -1.00 | Negative amount                    |
|                    -0.01 | Negative boundary                  |
|                     0.00 | Zero                               |
|                    0.001 | Unsupported precision              |
|                    0.009 | Below minimum                      |
|                     0.01 | Exact minimum                      |
|                     0.02 | Just above minimum                 |
|                99,999.99 | Below maximum                      |
|               100,000.00 | Exact maximum                      |
|               100,000.01 | Above maximum                      |
|               250,000.00 | Exact daily limit where applicable |
|               250,000.01 | Above daily limit                  |
| Very large numeric value | Overflow/input validation          |
|        Non-numeric input | Validation                         |
|                     Null | Required-field validation          |
|             Empty string | Validation                         |

---

# 9. Transfer Balance Test Data

Important balance/amount combinations shall include:

## Exact Balance

```text
Available balance: 1,000.00
Transfer amount: 1,000.00
Fee: 0.00
```

Expected:

```text
Allowed
Ending balance: 0.00
```

---

## Above Available Balance

```text
Available balance: 1,000.00
Transfer amount: 1,000.01
```

Expected:

```text
Rejected
```

---

## Amount Plus Fee Exceeds Balance

```text
Available balance: 1,000.00
Transfer amount: 990.00
Fee: 20.00
```

Expected:

```text
Rejected
```

---

## Amount Plus Fee Equals Balance

```text
Available balance: 1,000.00
Transfer amount: 980.00
Fee: 20.00
```

Expected:

```text
Allowed
Ending balance: 0.00
```

---

# 10. Beneficiary Test Data

## BENEFICIARY-001 — Verified Internal Beneficiary

```text
Alias: beneficiary.internal.verified
Type: SAME_BANK
Status: VERIFIED
Owner: customer.standard
```

---

## BENEFICIARY-002 — Unverified Internal Beneficiary

```text
Alias: beneficiary.internal.unverified
Type: SAME_BANK
Status: PENDING_VERIFICATION
Owner: customer.standard
```

---

## BENEFICIARY-003 — Verified External Beneficiary

```text
Alias: beneficiary.external.verified
Type: EXTERNAL_BANK
Status: VERIFIED
Owner: customer.standard
```

---

## BENEFICIARY-004 — Another Customer's Beneficiary

```text
Alias: beneficiary.other_customer
Owner: customer.multiple_accounts
```

Purpose:

```text
Horizontal authorization testing
```

---

## BENEFICIARY-005 — Invalid Beneficiary

Test data shall be available for:

```text
Invalid account number
Missing account number
Unsupported bank
Malformed bank identifier
Nonexistent destination account
```

---

# 11. Card Test Data

## CARD-001 — Active Card

```text
Alias: card.active
Owner: customer.standard
Status: ACTIVE
Online transactions: ENABLED
International transactions: ENABLED
```

---

## CARD-002 — Frozen Card

```text
Alias: card.frozen
Status: FROZEN
```

---

## CARD-003 — Blocked Card

```text
Alias: card.blocked
Status: BLOCKED
```

---

## CARD-004 — Expired Card

```text
Alias: card.expired
Status: EXPIRED
```

---

## CARD-005 — Online Transactions Disabled

```text
Alias: card.online_disabled
Status: ACTIVE
Online transactions: DISABLED
```

---

## CARD-006 — International Transactions Disabled

```text
Alias: card.international_disabled
Status: ACTIVE
International transactions: DISABLED
```

---

## CARD-007 — Another Customer's Card

```text
Alias: card.other_customer
Owner: customer.multiple_accounts
```

Purpose:

```text
Authorization testing
ID manipulation testing
```

---

# 12. Card Data Security

Test cards shall use fictional numbers reserved for testing.

Real payment-card information shall never be stored in the repository.

Displayed card values should be masked.

Example:

```text
**** **** **** 1234
```

Test data shall verify that sensitive values do not appear in:

```text
UI where not required
Logs
URLs
API responses
Audit records
Error messages
```

---

# 13. Transaction Test Data

The environment shall contain examples of:

```text
COMPLETED transactions
PENDING transactions
FAILED transactions
CANCELLED transactions
REVERSED transactions
```

Transaction types shall include examples of:

```text
Own-account transfer
Same-bank transfer
External transfer
Bill payment
Loan repayment
Reversal
```

Each transaction shall contain:

```text
Unique transaction reference
Source account where applicable
Destination where applicable
Amount
Currency
Status
Timestamp
Transaction type
```

---

# 14. Transaction Search and Filter Data

Transaction data should span multiple:

```text
Dates
Amounts
Statuses
Transaction types
Beneficiaries
References
```

Recommended date coverage:

```text
Today
Yesterday
7 days ago
30 days ago
90 days ago
Previous calendar year
```

This allows testing:

```text
Date filters
Sorting
Pagination
Search
Combined filters
Boundary dates
```

---

# 15. Scheduled Transfer Test Data

The environment shall support scheduled transfers with states including:

```text
Scheduled for future execution
Due today
Cancelled
Completed
Failed
```

Dates should include:

```text
Future date
Current date
Past date
End of month
Beginning of month
End of year
Beginning of year
```

Invalid scheduled dates shall also be tested.

---

# 16. Recurring Transfer Test Data

Supported recurring frequencies:

```text
WEEKLY
MONTHLY
```

Test recurring transfers shall include:

```text
Active recurring transfer
Cancelled recurring transfer
Recurring transfer with next execution date
Recurring transfer with insufficient funds
Recurring transfer associated with frozen account
```

---

# 17. Payment Test Data

The system shall contain test billers such as:

```text
Electricity Provider
Water Provider
Internet Provider
Mobile Provider
```

These shall be fictional test entities.

Payment states shall include:

```text
PENDING
COMPLETED
FAILED
CANCELLED
REVERSED
```

Payment amounts shall include:

```text
Valid normal amount
Minimum value
Exact available balance
Above available balance
Zero
Negative amount
Very large amount
Invalid decimal value
```

---

# 18. Loan Test Data

## LOAN-001 — Pending Application

```text
Owner: customer.standard
Status: PENDING
```

---

## LOAN-002 — Under Review

```text
Status: UNDER_REVIEW
```

---

## LOAN-003 — Approved Loan

```text
Status: APPROVED
Outstanding balance: > 0
Repayment schedule: PRESENT
```

---

## LOAN-004 — Rejected Application

```text
Status: REJECTED
```

---

## LOAN-005 — Fully Repaid Loan

```text
Outstanding balance: 0.00
```

---

## LOAN-006 — Another Customer's Loan

Used for authorization testing.

---

# 19. Loan Boundary Data

Loan amount values should include:

```text
Negative amount
0
0.01
Normal valid amount
Maximum supported amount
Amount above maximum
Extremely large numeric value
Null
Empty input
Non-numeric input
```

Repayment amounts should include:

```text
Below outstanding balance
Exact outstanding balance
Above outstanding balance
Zero
Negative amount
```

---

# 20. KYC Test Data

The following KYC states shall exist:

```text
PENDING
APPROVED
REJECTED
REVIEW_REQUIRED
```

Test document states shall include:

```text
Valid supported document
Missing document
Unsupported file type
Oversized document
Corrupted document
Duplicate submission
```

All documents shall contain synthetic identities.

---

# 21. Authentication Test Data

Authentication data shall support:

```text
Valid email + valid password
Valid email + invalid password
Invalid email + password
Empty email
Empty password
Malformed email
Locked account
Disabled/restricted user where applicable
Expired session
Valid session
Invalid token
Expired token
Manipulated token
```

---

# 22. Password Boundary Data

Configured password requirements:

```text
Minimum length: 12
Maximum length: 128
At least 1 uppercase letter
At least 1 lowercase letter
At least 1 number
At least 1 special character
```

Required test values shall include:

```text
11 characters
12 characters
13 characters

127 characters
128 characters
129 characters

No uppercase
No lowercase
No number
No special character

Whitespace-only value
Leading spaces
Trailing spaces
Unicode characters
Very long string
Common weak password where applicable
```

Passwords used in automated tests shall come from environment configuration rather than hard-coded repository secrets.

---

# 23. OTP Test Data

OTP length:

```text
6 numeric digits
```

Test data shall cover:

```text
Valid OTP
Invalid OTP
Expired OTP
Previously used OTP
5-digit OTP
7-digit OTP
Alphabetic OTP
Alphanumeric OTP
Empty OTP
Null OTP
Correct OTP after multiple failed attempts
OTP after maximum attempt count
```

For automated environments, OTP retrieval may be implemented through a dedicated test-only mechanism.

Production security shall not be weakened to support testing.

---

# 24. Session Test Data

Tests shall support:

```text
Valid active session
Expired session
Logged-out session
Session terminated remotely
Multiple active sessions
Invalid session identifier
Another user's session identifier
```

---

# 25. Notification Test Data

Notifications shall include:

```text
READ
UNREAD
```

Types shall include examples of:

```text
Transfer success
Transfer failure
Payment success
Payment failure
Password change
New login
Account lock
KYC update
Loan update
```

Another customer's notification shall exist for authorization testing.

---

# 26. Audit Test Data

Audit events shall exist for:

```text
Login success
Login failure
Account lock
Password change
Transfer attempt
Transfer success
Transfer failure
Payment
KYC approval
KYC rejection
Account freeze
Account unfreeze
Loan approval
Loan rejection
Administrative configuration change
```

Expected audit fields include:

```text
Audit ID
Actor ID
Actor role
Action
Target resource
Timestamp
Outcome
Reference ID where applicable
```

---

# 27. Authorization Test Data

At least two distinct CUSTOMER identities are required.

Example:

```text
Customer A
Customer B
```

Both must have separate:

```text
Accounts
Cards
Beneficiaries
Transactions
Loans
Notifications
```

This enables testing of horizontal privilege escalation.

Example:

```text
Customer A authenticates.

Customer A attempts:

GET /api/accounts/{Customer-B-account-id}
```

Expected:

```text
Access denied.
Customer B data is not returned.
```

---

# 28. Role Authorization Data

Tests shall be executed using:

```text
CUSTOMER token
BANK_EMPLOYEE token
ADMIN token
Unauthenticated request
Expired token
Invalid token
```

This data shall validate:

```text
401 responses
403 responses
Successful access
Role restrictions
Privilege escalation prevention
```

---

# 29. Database Test Data

The database shall contain related data for:

```text
Users
Customers
Accounts
Beneficiaries
Cards
Transactions
Transfers
Payments
Loans
Notifications
Sessions
Audit logs
```

Data relationships shall allow testing of:

```text
Primary keys
Foreign keys
Unique constraints
NOT NULL constraints
Referential integrity
Transaction rollback
Decimal precision
Duplicate prevention
Cascade behavior where defined
Historical integrity
```

---

# 30. Invalid Database Data Attempts

Database or API tests shall attempt operations such as:

```text
Duplicate account number
Duplicate transaction reference
Account with nonexistent customer
Transaction with nonexistent account
Null mandatory field
Invalid foreign key
Duplicate unique email
Unsupported status value
Invalid monetary precision
```

Expected result:

```text
Invalid data is rejected.
Database integrity remains intact.
```

---

# 31. Concurrency Test Data

Concurrency testing shall include a dedicated account.

Example:

```text
Alias: account.concurrency
Starting balance: 1,000.00 EGP
```

Simultaneous requests:

```text
Transfer A = 800.00 EGP
Transfer B = 800.00 EGP
```

Expected:

```text
Both transfers must not complete successfully.

Final balance must remain valid.

No overspending is permitted.
```

---

# 32. Idempotency Test Data

API tests shall generate unique idempotency keys.

Example:

```text
qa-test-20260912-000001
```

Tests shall include:

```text
Same key + same payload
Same key sent concurrently
Same key sent repeatedly
Different keys + same payload
Malformed key
Missing key where required
```

Expected behavior shall follow the applicable API business rules.

---

# 33. Decimal Precision Test Data

Financial test values shall include:

```text
0.01
0.10
0.99
1.00
10.10
999.99
1000.00
99999.99
100000.00
```

Invalid or edge precision values shall include:

```text
0.001
1.999
99999.9999
Extremely large decimal
```

The system must not introduce floating-point rounding errors.

---

# 34. Date and Time Test Data

Date/time scenarios shall include:

```text
Current date
Past date
Future date
Month boundary
Year boundary
Leap-year date
End of February
Midnight boundary
Session expiration boundary
OTP expiration boundary
Scheduled-transfer boundary
```

Where relevant, server time rather than browser time shall determine financial and security behavior.

---

# 35. API Data Variations

API test requests shall cover:

```text
Valid payload
Missing required field
Null field
Empty string
Incorrect data type
Unexpected field
Malformed JSON
Invalid enum value
Very large payload
Duplicate request
Invalid identifier
Nonexistent resource
Unauthorized resource
```

---

# 36. String Input Data

String inputs shall be tested using:

```text
Normal text
Empty string
Single character
Maximum allowed length
Maximum + 1
Very long input
Leading spaces
Trailing spaces
Unicode
Arabic text
Special characters
HTML-like input
SQL-like input
Script-like input
```

These cases are intended to verify validation and safe handling, not to alter or damage the environment.

---

# 37. Performance Test Data

Performance tests shall use dedicated generated data.

Performance datasets may include:

```text
Hundreds of customers
Thousands of accounts
Thousands of beneficiaries
Tens of thousands of transactions
Large transaction histories
Concurrent authenticated sessions
```

Performance testing shall not depend on manually maintained customer records.

---

# 38. JMeter User Data

JMeter scenarios should use parameterized datasets.

Example CSV:

```csv
username,password,accountId,beneficiaryId
user001@example.test,${PASSWORD},ACC001,BEN001
user002@example.test,${PASSWORD},ACC002,BEN002
user003@example.test,${PASSWORD},ACC003,BEN003
```

Real passwords shall not be stored in source-controlled CSV files.

---

# 39. Automation Data

UI automation shall avoid sharing mutable data between parallel tests.

Preferred approaches include:

```text
Dedicated user per worker
Dynamic resource creation
Test-run-specific identifiers
API-based setup
Database seeding in controlled environments
Cleanup after execution
```

---

# 40. CI Test Data

CI pipelines shall be capable of preparing required test data automatically.

Potential flow:

```text
Start test environment
↓
Apply database migrations
↓
Seed baseline data
↓
Run backend tests
↓
Run API tests
↓
Run UI tests
↓
Generate reports
↓
Destroy/reset environment
```

CI shall not depend on manual data preparation.

---

# 41. Seed Data

The application should eventually contain a repeatable seed process.

Example command may later resemble:

```text
npm run db:seed
```

or an equivalent backend-specific command.

The seed process shall create:

```text
Standard customer
Multiple-account customer
Low-balance customer
High-balance customer
Frozen-account customer
KYC-pending customer
Employees
Administrator
Beneficiaries
Cards
Loans
Transactions
Notifications
```

---

# 42. Reset Strategy

The development/test environment should eventually support a reset mechanism.

Conceptual process:

```text
Reset database
↓
Run migrations
↓
Seed baseline data
↓
Environment returns to known state
```

This is especially important for:

```text
Automated tests
CI/CD
API tests
Database tests
Performance preparation
```

---

# 43. Data Mutation Rules

Tests that change important baseline data shall either:

```text
Restore the original value after execution
```

or:

```text
Use dedicated disposable data
```

Example:

A test that freezes a card should not leave the standard test user's only active card frozen for every later test.

---

# 44. Parallel Execution

When tests run in parallel, mutable resources must not be shared unless the test specifically validates concurrency.

Avoid:

```text
10 tests modifying the same account balance.
```

Prefer:

```text
Worker 1 → Customer 101
Worker 2 → Customer 102
Worker 3 → Customer 103
```

---

# 45. Test Run Identification

Dynamically created resources should contain or reference a test-run identifier where practical.

Example:

```text
RUN-20260912-001
```

Possible generated values:

```text
beneficiary-RUN-20260912-001
qa+RUN-20260912-001@example.test
IDEMP-RUN-20260912-001-001
```

This helps with:

```text
Debugging
Cleanup
Traceability
Reporting
```

---

# 46. Sensitive Data Masking

Test validation shall verify masking where required.

Examples:

## Card

```text
**** **** **** 1234
```

## Account

Where applicable:

```text
******7890
```

## Sensitive identity fields

Only permitted portions should be displayed according to application rules.

---

# 47. Data Ownership Requirements

Every customer-owned entity must be traceable to the correct owner.

Expected ownership relationships include:

```text
Customer → Accounts
Customer → Beneficiaries
Customer → Loans
User → Notifications
Account → Cards
Account → Transactions
```

These relationships will later be verified directly through database testing.

---

# 48. Required Core Dataset Summary

The minimum reusable dataset shall include:

| Data Type                  | Minimum Required |
| -------------------------- | ---------------: |
| Standard customers         |                2 |
| KYC pending customer       |                1 |
| KYC rejected customer      |                1 |
| Locked customer            |                1 |
| Low-balance customer       |                1 |
| Zero-balance customer      |                1 |
| High-balance customer      |                1 |
| Frozen-account customer    |                1 |
| Suspended-account customer |                1 |
| Closed-account customer    |                1 |
| Multiple-account customer  |                1 |
| Bank employee              |                1 |
| KYC reviewer               |                1 |
| Loan reviewer              |                1 |
| Restricted employee        |                1 |
| Administrator              |                1 |
| Active accounts            |         Multiple |
| Frozen account             |                1 |
| Suspended account          |                1 |
| Closed account             |                1 |
| Verified beneficiaries     |         Multiple |
| Unverified beneficiary     |                1 |
| Active card                |                1 |
| Frozen card                |                1 |
| Blocked card               |                1 |
| Expired card               |                1 |
| Completed transactions     |         Multiple |
| Pending transaction        |                1 |
| Failed transaction         |                1 |
| Reversed transaction       |                1 |
| Pending loan               |                1 |
| Approved loan              |                1 |
| Rejected loan              |                1 |
| Notifications              |         Multiple |
| Audit events               |         Multiple |

---

# 49. Test Data Naming Convention

Recommended aliases:

```text
customer.standard
customer.low_balance
customer.high_balance

account.standard.current
account.standard.savings
account.concurrency

beneficiary.internal.verified
beneficiary.external.verified

card.active
card.frozen

employee.standard
employee.kyc_reviewer

admin.standard
```

Tests should prefer aliases or dynamically resolved IDs rather than hard-coded database primary keys.

---

# 50. Test Data Traceability

Test cases should identify important required data.

Example:

```text
Test Case:
TC-TRF-INSUFFICIENT-001

Required Data:
customer.low_balance
account.low_balance
beneficiary.internal.verified

Starting Balance:
10.00 EGP

Transfer Amount:
50.00 EGP

Expected:
Transfer rejected
Balance unchanged
Failure response generated
Audit record generated where required
```

---

# 51. Environment Variables

Credentials and environment-specific values shall eventually be provided through variables such as:

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

A safe template may be committed as:

```text
.env.example
```

but the actual:

```text
.env
```

file shall remain excluded through `.gitignore`.

---

# 52. Final Test Data Principle

The test-data strategy shall ensure that tests are:

```text
Repeatable
Independent
Deterministic where appropriate
Secure
Traceable
Reusable
Suitable for automation
Suitable for parallel execution
Easy to reset
Safe for source control
```

Test data is part of the test architecture and shall not be treated as an afterthought.
