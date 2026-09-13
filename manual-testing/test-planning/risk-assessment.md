# Banking System — Risk Assessment

## 1. Document Information

| Field           | Value                                                    |
| --------------- | -------------------------------------------------------- |
| Project         | Banking System Testing Project                           |
| Document        | Risk Assessment                                          |
| Version         | 1.0                                                      |
| Status          | Draft                                                    |
| Owner           | QA Engineering                                           |
| Assessment Type | Product, Technical, Security and Testing Risk Assessment |

---

# 2. Purpose

This document identifies, evaluates, and prioritizes risks associated with the Banking System.

The objective is to ensure that testing effort is proportional to business impact and likelihood of failure.

Risk assessment will influence:

* Test depth
* Test priority
* Regression coverage
* Automation priority
* Exploratory testing focus
* Performance testing
* API testing
* Database validation
* Security-oriented validation
* Release decisions

The Banking System contains financial and security-sensitive workflows, so risk-based testing is essential.

---

# 3. Risk Model

Each risk will be evaluated using:

* Probability
* Impact
* Risk Score

Risk Score is calculated as:

```text
Risk Score = Probability × Impact
```

Both Probability and Impact use a scale from 1 to 5.

---

# 4. Probability Scale

| Score | Probability | Description                             |
| ----- | ----------- | --------------------------------------- |
| 1     | Rare        | Very unlikely to occur                  |
| 2     | Unlikely    | May occur occasionally                  |
| 3     | Possible    | Could reasonably occur                  |
| 4     | Likely      | Expected to occur under some conditions |
| 5     | Very Likely | High probability of occurrence          |

---

# 5. Impact Scale

| Score | Impact     | Description                                                                  |
| ----- | ---------- | ---------------------------------------------------------------------------- |
| 1     | Negligible | Cosmetic or insignificant issue                                              |
| 2     | Low        | Limited user inconvenience                                                   |
| 3     | Moderate   | Important functionality affected                                             |
| 4     | High       | Major business or customer impact                                            |
| 5     | Critical   | Financial loss, security compromise, data corruption, or system-wide failure |

---

# 6. Risk Score Classification

| Score | Risk Level |
| ----: | ---------- |
|   1–4 | Low        |
|   5–9 | Medium     |
| 10–16 | High       |
| 17–25 | Critical   |

---

# 7. Testing Response by Risk

## Low Risk

Testing may include:

* Basic functional coverage
* Exploratory checks
* Selected regression tests

## Medium Risk

Testing should include:

* Positive testing
* Negative testing
* Regression coverage
* Selected boundaries

## High Risk

Testing should include:

* Extensive functional testing
* Boundary testing
* Negative testing
* Integration testing
* Automation
* Exploratory testing
* API validation
* Database validation where applicable

## Critical Risk

Testing should include:

* Extensive positive and negative coverage
* Boundary value analysis
* Decision tables
* State transition testing
* Concurrency testing
* API testing
* Database validation
* Security-oriented testing
* Automation
* Regression
* End-to-end testing
* Release-blocking criteria

---

# 8. Overall Banking Risk Areas

The following areas are considered the highest risk:

1. Authentication
2. Authorization
3. Account ownership
4. Balance integrity
5. Transfer processing
6. Duplicate transaction prevention
7. Transaction atomicity
8. Account-state enforcement
9. Administrative access
10. Financial calculations
11. Database integrity
12. Concurrency
13. Session management
14. Audit logging

---

# 9. Risk Register

| ID       | Risk                                                      | Probability | Impact | Score | Level    |
| -------- | --------------------------------------------------------- | ----------: | -----: | ----: | -------- |
| RISK-001 | Incorrect account balance                                 |           4 |      5 |    20 | Critical |
| RISK-002 | Unauthorized customer data access                         |           4 |      5 |    20 | Critical |
| RISK-003 | Duplicate financial transaction                           |           4 |      5 |    20 | Critical |
| RISK-004 | Partial transfer processing                               |           3 |      5 |    15 | High     |
| RISK-005 | Authentication bypass                                     |           3 |      5 |    15 | High     |
| RISK-006 | Privilege escalation                                      |           3 |      5 |    15 | High     |
| RISK-007 | Transfer exceeding available balance                      |           4 |      5 |    20 | Critical |
| RISK-008 | Transfer limit bypass                                     |           3 |      5 |    15 | High     |
| RISK-009 | Frozen account can transact                               |           3 |      5 |    15 | High     |
| RISK-010 | Incorrect fee calculation                                 |           3 |      4 |    12 | High     |
| RISK-011 | Incorrect loan interest calculation                       |           3 |      4 |    12 | High     |
| RISK-012 | Incorrect deposit interest calculation                    |           3 |      4 |    12 | High     |
| RISK-013 | Concurrent transactions corrupt balance                   |           4 |      5 |    20 | Critical |
| RISK-014 | Session remains active after logout                       |           3 |      5 |    15 | High     |
| RISK-015 | Expired session accepted                                  |           3 |      5 |    15 | High     |
| RISK-016 | Account lockout fails                                     |           3 |      4 |    12 | High     |
| RISK-017 | Incorrect beneficiary used in transfer                    |           3 |      5 |    15 | High     |
| RISK-018 | Closed destination account accepts funds incorrectly      |           2 |      5 |    10 | High     |
| RISK-019 | Transaction history does not match balance changes        |           3 |      4 |    12 | High     |
| RISK-020 | Statement contains incorrect transactions                 |           3 |      4 |    12 | High     |
| RISK-021 | Sensitive information exposed                             |           3 |      5 |    15 | High     |
| RISK-022 | Admin action not audited                                  |           3 |      4 |    12 | High     |
| RISK-023 | Unauthorized admin operation                              |           3 |      5 |    15 | High     |
| RISK-024 | Card freeze does not prevent operations                   |           3 |      4 |    12 | High     |
| RISK-025 | Blocked card becomes usable unexpectedly                  |           2 |      5 |    10 | High     |
| RISK-026 | Failed payment modifies balance                           |           3 |      5 |    15 | High     |
| RISK-027 | Duplicate payment                                         |           3 |      5 |    15 | High     |
| RISK-028 | Scheduled transfer executes incorrectly                   |           3 |      4 |    12 | High     |
| RISK-029 | Cancelled transfer still executes                         |           2 |      5 |    10 | High     |
| RISK-030 | API accepts unauthorized request                          |           4 |      5 |    20 | Critical |
| RISK-031 | Invalid database relationships                            |           2 |      4 |     8 | Medium   |
| RISK-032 | Audit log exposes sensitive data                          |           2 |      4 |     8 | Medium   |
| RISK-033 | Transaction reference duplication                         |           2 |      4 |     8 | Medium   |
| RISK-034 | Notification sent for failed transaction as success       |           3 |      3 |     9 | Medium   |
| RISK-035 | Browser-specific critical workflow failure                |           2 |      4 |     8 | Medium   |
| RISK-036 | Responsive UI hides critical action                       |           2 |      3 |     6 | Medium   |
| RISK-037 | Invalid data accepted due to frontend-only validation     |           4 |      4 |    16 | High     |
| RISK-038 | API and UI enforce different business rules               |           3 |      4 |    12 | High     |
| RISK-039 | Database state inconsistent with API response             |           3 |      5 |    15 | High     |
| RISK-040 | Performance degradation under load                        |           4 |      4 |    16 | High     |
| RISK-041 | System unavailable during traffic spike                   |           3 |      5 |    15 | High     |
| RISK-042 | Slow transaction causes duplicate resubmission            |           4 |      4 |    16 | High     |
| RISK-043 | Incorrect timezone causes scheduled transaction failure   |           2 |      4 |     8 | Medium   |
| RISK-044 | OTP reuse accepted                                        |           3 |      5 |    15 | High     |
| RISK-045 | Expired OTP accepted                                      |           3 |      5 |    15 | High     |
| RISK-046 | Password reset token reusable                             |           2 |      5 |    10 | High     |
| RISK-047 | Another customer's resource accessible by ID manipulation |           4 |      5 |    20 | Critical |
| RISK-048 | UI reports success while backend transaction fails        |           3 |      5 |    15 | High     |
| RISK-049 | Database migration corrupts financial records             |           2 |      5 |    10 | High     |
| RISK-050 | Shared test data causes false failures                    |           4 |      3 |    12 | High     |

---

# 10. RISK-001 — Incorrect Account Balance

## Description

The system displays or stores an incorrect balance after a financial operation.

Examples:

* Debit not deducted
* Credit not added
* Fee deducted twice
* Failed transaction affects balance
* Reversal incorrectly calculated

## Probability

4 — Likely

## Impact

5 — Critical

## Risk Score

```text
4 × 5 = 20
```

Risk Level:

```text
CRITICAL
```

## Testing Controls

* Verify starting balance.
* Execute transaction.
* Verify final balance.
* Validate transaction history.
* Validate API result.
* Validate database state.
* Validate statement.
* Test failed transactions.
* Test reversals.
* Test concurrent transactions.

## Release Impact

Any reproducible balance-integrity defect should block release.

---

# 11. RISK-002 — Unauthorized Customer Data Access

## Description

Customer A can access Customer B's resources.

Affected resources may include:

* Accounts
* Transactions
* Cards
* Statements
* Loans
* Beneficiaries
* Profile details

## Probability

4

## Impact

5

## Score

```text
20 — CRITICAL
```

## Testing Controls

Attempt resource access using:

* Modified URL
* Modified API ID
* Another customer's account ID
* Another customer's card ID
* Another customer's transaction ID
* Direct navigation

Expected:

```text
Access denied
```

The response should not expose sensitive information.

---

# 12. RISK-003 — Duplicate Financial Transaction

## Description

A transfer or payment executes more than once because of:

* Double-click
* Retry
* Browser refresh
* API retry
* Network delay
* Duplicate request

## Score

```text
Probability: 4
Impact: 5
Risk Score: 20
Level: Critical
```

## Test Coverage

Test:

* Double-click Submit.
* Rapidly submit transaction twice.
* Refresh during processing.
* Retry same API request.
* Reuse same idempotency key where implemented.
* Network retry behavior.

Expected:

Only the intended transaction should execute.

---

# 13. RISK-004 — Partial Transaction Processing

## Description

A financial transaction completes only partially.

Example:

```text
Source debited
Destination not credited
```

or:

```text
Destination credited
Source not debited
```

This violates transaction atomicity.

## Score

```text
Probability: 3
Impact: 5
Score: 15
Level: High
```

## Testing Controls

Validate:

* Source balance
* Destination balance
* Transaction record
* Failure recovery
* Rollback behavior

---

# 14. RISK-005 — Authentication Bypass

Potential causes:

* Missing authentication check
* Reused session
* Direct URL access
* Incorrect API authentication
* Invalid token accepted

Testing should include:

* Protected endpoint without token
* Invalid token
* Expired token
* Logged-out token
* Modified token
* Direct UI navigation

---

# 15. RISK-006 — Privilege Escalation

A lower-privileged user gains access to higher-level functions.

Examples:

```text
Customer → Admin
Support Admin → Super Admin
```

Testing must validate:

* UI controls
* Direct URLs
* API endpoints
* Role permissions
* Admin resources

UI hiding alone is not sufficient security.

---

# 16. RISK-007 — Spending Beyond Available Balance

A customer may be able to spend more than available funds.

Example:

```text
Available Balance = 1000

Transfer A = 800
Transfer B = 500
```

If overdraft is unsupported, both cannot succeed.

Testing includes:

* Single transaction above balance
* Exact balance
* Concurrent transfers
* Fees causing insufficient funds
* Pending transaction interaction

---

# 17. RISK-008 — Transaction Limit Bypass

Transfer limits may include:

* Per-transaction limit
* Daily limit
* Monthly limit
* Beneficiary limit
* Card limit

Testing must validate:

```text
Limit - 0.01
Limit
Limit + 0.01
```

Also test cumulative daily limits.

---

# 18. RISK-009 — Restricted Account Performs Transaction

Account states such as:

```text
FROZEN
RESTRICTED
SUSPENDED
CLOSED
```

must correctly prevent prohibited actions.

Test attempts should include:

* Transfers
* Payments
* Card operations
* Scheduled transfer creation

---

# 19. RISK-010 — Incorrect Fee Calculation

Fees may depend on:

* Transaction type
* Amount
* Account type
* Destination
* Currency

Validate:

```text
Amount
+ Fee
= Total Debit
```

Boundary values should receive specific coverage.

---

# 20. RISK-011 — Loan Calculation Error

Financial errors may occur in:

* Interest
* Installments
* Remaining principal
* Final payment
* Early repayment

Testing should compare expected calculations against displayed and stored values.

---

# 21. RISK-012 — Deposit Interest Error

Validate:

* Principal
* Interest rate
* Term
* Maturity amount
* Early withdrawal behavior

Incorrect interest calculations have direct financial impact.

---

# 22. RISK-013 — Concurrency Corrupts Balance

This is a critical banking risk.

Example:

```text
Balance = 1000

Transaction A = 800
Transaction B = 500
```

If both read the same starting balance before updates, the system could incorrectly approve both.

Testing should include:

* Parallel browser submissions
* Parallel API requests
* JMeter concurrency tests
* SQL validation after execution

---

# 23. RISK-014 — Session Remains Valid After Logout

After logout:

```text
Old session/token
```

must no longer grant protected access according to the intended architecture.

Test:

1. Login.
2. Capture protected page/API behavior.
3. Logout.
4. Attempt to reuse previous session.
5. Attempt browser Back.
6. Attempt direct resource navigation.

---

# 24. RISK-015 — Expired Session Accepted

Test:

* Session timeout
* Token expiration
* Long idle period
* Request after expiration
* Transaction confirmation after session expiry

Expected:

Reauthentication should occur.

Financial action must not silently proceed with an invalid session.

---

# 25. RISK-016 — Account Lockout Failure

Repeated invalid authentication attempts should follow the business rule.

Test:

```text
Attempt 1
Attempt 2
Attempt 3
...
Configured threshold
```

Then verify:

* Lockout
* Correct messaging
* Valid password cannot bypass active lock
* Unlock flow works
* Audit record exists

---

# 26. RISK-017 — Beneficiary Integrity Failure

Potential defects:

* Wrong beneficiary selected
* Beneficiary account incorrectly mapped
* Removed beneficiary still usable
* Pending beneficiary usable prematurely
* Duplicate beneficiary accepted unexpectedly

High-value transfer workflows must verify beneficiary identity immediately before confirmation.

---

# 27. RISK-018 — Closed Destination Account Receives Transfer

The destination account state must be validated.

Testing should include:

```text
ACTIVE
FROZEN
RESTRICTED
CLOSED
INVALID
```

Expected result depends on defined business rules.

---

# 28. RISK-019 — Transaction History Inconsistency

A completed transfer may:

* Change balance
* But not appear in history

or:

* Appear in history
* But not affect balance

Testing should cross-check multiple representations.

---

# 29. RISK-020 — Incorrect Statement

Statements must accurately represent:

* Transactions
* Dates
* Amounts
* Fees
* Balances
* Transaction references

Test:

* Date ranges
* Boundary dates
* Reversed transactions
* Large transaction histories

---

# 30. RISK-021 — Sensitive Data Exposure

Potential sensitive information:

* Full card numbers
* Passwords
* Authentication tokens
* Private customer information
* Internal stack traces
* Database errors

Testing should inspect:

* UI
* Browser storage
* Network responses
* Error messages
* Logs where accessible

---

# 31. RISK-022 — Missing Audit Record

Critical actions should be traceable.

Examples:

* Admin freezes account
* Customer changes password
* Transfer created
* Loan approved
* Card blocked

Audit validation should verify:

* Actor
* Action
* Timestamp
* Target
* Result

---

# 32. RISK-023 — Unauthorized Administrative Action

A limited administrator must not perform operations outside granted permissions.

Testing should include:

* Hidden controls
* Direct URL
* API request
* Modified request
* Role change scenarios

---

# 33. RISK-024 — Frozen Card Still Usable

After card freeze:

Expected restrictions should apply immediately or according to documented rules.

Validate:

* Card status
* Online operations
* Relevant simulated transactions
* Unfreeze behavior

---

# 34. RISK-025 — Blocked Card Restored Incorrectly

Blocked cards often represent a stronger state than frozen cards.

State testing should verify:

```text
ACTIVE → FROZEN → ACTIVE
```

may be valid.

But:

```text
BLOCKED → ACTIVE
```

must follow explicit business rules.

---

# 35. RISK-026 — Failed Payment Changes Balance

When payment fails:

Expected:

```text
Payment Status = FAILED
Balance = unchanged
```

unless a defined temporary authorization model applies.

Validate database and transaction state.

---

# 36. RISK-027 — Duplicate Payment

Test:

* Submit button double-click
* Request retry
* Refresh
* Repeated bill reference
* Same request payload

Financial systems should detect or safely handle repeated operations.

---

# 37. RISK-028 — Scheduled Transfer Executes Incorrectly

Possible issues:

* Executes early
* Executes late
* Wrong amount
* Wrong beneficiary
* Executes multiple times

Testing should validate schedule, timezone, account balance, and execution history.

---

# 38. RISK-029 — Cancelled Scheduled Transfer Executes

Test:

```text
Create scheduled transfer
→ Cancel transfer
→ Reach scheduled execution time
```

Expected:

No financial transaction occurs.

Cancellation must persist correctly.

---

# 39. RISK-030 — API Authorization Failure

Backend endpoints must independently enforce security.

Critical rule:

```text
UI restriction != security control
```

Even if the UI hides an action, the API must reject unauthorized requests.

---

# 40. RISK-031 — Referential Integrity Failure

Examples:

* Transaction references nonexistent account.
* Card references deleted customer.
* Loan references invalid borrower.
* Beneficiary references missing account.

SQL testing should verify relationships and constraints.

---

# 41. RISK-032 — Sensitive Data in Audit Logs

Audit logs are security-sensitive.

They should not contain:

* Passwords
* OTP values
* Full tokens
* CVVs
* Excessive sensitive personal data

---

# 42. RISK-033 — Duplicate Transaction Reference

Transaction references should normally be unique.

Testing should verify:

* Unique generation
* Database uniqueness
* Search behavior
* API response consistency

---

# 43. RISK-034 — Incorrect Notification Status

Example:

Transfer fails, but user receives:

```text
Transfer successful
```

This creates misleading financial information.

Notifications should reflect final transaction state.

---

# 44. RISK-035 — Browser-Specific Failure

Critical workflows may fail only in one browser because of:

* JavaScript behavior
* Storage handling
* CSS interaction
* Date input
* Browser APIs

Critical flows therefore require cross-browser testing.

---

# 45. RISK-036 — Responsive UI Blocks Critical Action

Examples:

* Confirm button hidden
* Amount truncated
* Warning message off-screen
* Navigation inaccessible

Responsive testing must cover financial confirmations.

---

# 46. RISK-037 — Frontend-Only Validation

A malicious or technically capable user can bypass browser validation.

Example UI rule:

```text
Maximum Transfer = 100,000
```

Even if the frontend blocks 100,001, the backend must also reject it.

Testing should modify API requests directly.

---

# 47. RISK-038 — UI and API Rule Mismatch

Example:

```text
UI maximum = 100,000
API maximum = 200,000
```

This creates inconsistent system behavior.

Manual and API testing should validate the same business rules.

---

# 48. RISK-039 — API / Database Inconsistency

Example:

API returns:

```text
COMPLETED
```

but database shows:

```text
PENDING
```

Cross-layer validation is required for critical operations.

---

# 49. RISK-040 — Performance Degradation

High traffic may increase:

* Login latency
* Dashboard load time
* Transfer response time
* Statement generation time

JMeter will later validate performance thresholds.

---

# 50. RISK-041 — Traffic Spike Causes Outage

Spike tests should simulate sudden increases in activity.

Example:

```text
100 users
→ suddenly 1000 users
```

Observe:

* Availability
* Error rate
* Recovery
* Throughput

---

# 51. RISK-042 — Slow Response Causes Duplicate Submission

A slow transaction may cause a user to click Confirm multiple times.

This risk combines:

* Performance
* UI behavior
* Idempotency

Testing must verify that slow processing does not create duplicate financial activity.

---

# 52. RISK-043 — Timezone Error

Time-sensitive features include:

* Scheduled transfers
* Recurring transfers
* OTP expiry
* Sessions
* Card expiry
* Statements

Testing should verify consistent timezone handling.

---

# 53. RISK-044 — OTP Reuse

After successful use:

```text
OTP should become invalid
```

Attempting the same OTP again should fail.

---

# 54. RISK-045 — Expired OTP Accepted

Test immediately before and after expiry boundary.

Expected:

Expired OTP must not authorize sensitive action.

---

# 55. RISK-046 — Password Reset Token Reuse

Test:

```text
Generate reset token
→ Reset password
→ Reuse same token
```

Expected:

Reuse denied.

---

# 56. RISK-047 — Insecure Direct Object Reference

Example request:

```text
GET /accounts/ACC-001
```

Customer modifies it to:

```text
GET /accounts/ACC-002
```

where ACC-002 belongs to someone else.

Expected:

Authorization failure.

This applies to:

* Accounts
* Cards
* Transactions
* Statements
* Loans
* Beneficiaries

---

# 57. RISK-048 — UI Reports False Success

Potential flow:

```text
User clicks Transfer
Backend fails
UI still displays Success
```

Testing must verify final server state, especially for critical transactions.

---

# 58. RISK-049 — Migration Damages Financial Data

Database schema changes may affect:

* Decimal precision
* Relationships
* Transaction status
* Account balances

Migration validation is required before regression.

---

# 59. RISK-050 — Shared Test Data Contamination

Example:

Tester A uses:

```text
ACC-001
```

and changes balance.

Tester B executes a test expecting the original balance.

Result:

False failure.

Mitigations:

* Isolated accounts
* Data resets
* Dedicated automation users
* Controlled seed process

---

# 60. Risk by Module

## Authentication

Key risks:

* Credential bypass
* Account lockout failure
* Session handling
* OTP misuse
* Password reset misuse

Risk Level:

```text
Critical / High
```

---

# 61. Accounts

Key risks:

* Incorrect balance
* Wrong account ownership
* Incorrect account state
* Account closure issues

Risk Level:

```text
Critical
```

---

# 62. Beneficiaries

Key risks:

* Wrong beneficiary
* Duplicate beneficiary
* Unverified beneficiary use
* Removed beneficiary use

Risk Level:

```text
High
```

---

# 63. Transfers

Key risks:

* Duplicate transfer
* Balance corruption
* Limit bypass
* Atomicity failure
* Concurrency
* Wrong destination

Risk Level:

```text
Critical
```

---

# 64. Payments

Key risks:

* Duplicate payment
* Incorrect debit
* Failed payment debit
* Wrong payee

Risk Level:

```text
High / Critical
```

---

# 65. Cards

Key risks:

* Frozen card still active
* Unauthorized card access
* Incorrect limit enforcement
* Incorrect state transition

Risk Level:

```text
High
```

---

# 66. Loans

Key risks:

* Incorrect eligibility
* Incorrect interest
* Incorrect installment
* Incorrect outstanding balance

Risk Level:

```text
High
```

---

# 67. Deposits

Key risks:

* Incorrect maturity
* Incorrect interest
* Early withdrawal rule violation

Risk Level:

```text
High
```

---

# 68. Statements

Key risks:

* Missing transaction
* Incorrect amount
* Incorrect balance
* Date-filter errors

Risk Level:

```text
High
```

---

# 69. Notifications

Key risks:

* Incorrect transaction status
* Missing security notification
* Wrong recipient

Risk Level:

```text
Medium / High
```

---

# 70. Administration

Key risks:

* Unauthorized action
* Privilege escalation
* Missing audit logs
* Incorrect customer/account modification

Risk Level:

```text
Critical
```

---

# 71. Audit Logging

Key risks:

* Missing audit record
* Wrong actor
* Wrong timestamp
* Sensitive data exposure
* Logs modifiable unexpectedly

Risk Level:

```text
High
```

---

# 72. Risk-Based Test Priority

Testing execution should prioritize:

```text
1. Authentication
2. Authorization
3. Accounts
4. Transfers
5. Balance integrity
6. Transaction processing
7. Admin access
8. Payments
9. Beneficiaries
10. Cards
11. Statements
12. Loans
13. Deposits
14. Audit logging
15. Notifications
16. Profile and low-risk UI behavior
```

---

# 73. Automation Priority by Risk

## Highest Automation Priority

* Login
* Authorization
* Account balances
* Transfers
* Payments
* Transaction history
* Critical card states

## High Automation Priority

* Beneficiaries
* Statements
* Account restrictions
* Loan workflows
* Admin permissions

## Lower Automation Priority

* Cosmetic UI checks
* Rare configuration workflows
* Highly subjective usability checks

---

# 74. API Testing Priority by Risk

Highest API coverage should focus on:

```text
Authentication
Accounts
Transfers
Payments
Cards
Admin APIs
Loans
```

Reason:

Backend services are the primary enforcement layer for financial and authorization rules.

---

# 75. Database Testing Priority by Risk

Critical SQL validation should focus on:

* Balances
* Transactions
* Account ownership
* Transaction status
* Transfers
* Loans
* Audit logs
* Referential integrity

---

# 76. Performance Testing Priority

Performance testing should prioritize high-volume operations:

* Login
* Dashboard/account retrieval
* Transaction history
* Transfers
* Payments
* Statements

Special focus:

```text
Concurrent financial operations
```

---

# 77. Exploratory Testing Priority

Exploratory testing should focus on areas where scripted tests may miss unexpected behavior.

High-value charters include:

* Interrupt transfer workflow.
* Manipulate browser navigation.
* Use multiple tabs.
* Repeat financial operations.
* Change account state during active session.
* Explore role boundaries.
* Explore session expiry during financial transaction.

---

# 78. Security-Oriented Testing Priority

Security-focused manual validation should prioritize:

1. Authorization
2. Authentication
3. Session management
4. Resource ownership
5. Admin permissions
6. Sensitive data exposure
7. Input validation

---

# 79. Release Blocker Risks

Release should be blocked if any of the following are reproducibly possible:

* Incorrect account balance
* Unauthorized money transfer
* Unauthorized customer data access
* Authentication bypass
* Duplicate financial transaction
* Partial financial transaction
* Critical privilege escalation
* Database corruption
* Transfer beyond permitted balance
* Critical financial calculation error

---

# 80. High-Risk Defect Review

High-risk defects should be reviewed based on:

* Severity
* Reproducibility
* Frequency
* Workaround availability
* Customer impact
* Financial exposure
* Security exposure

A workaround does not automatically make a critical financial defect acceptable.

---

# 81. Residual Risk

Residual risk is the risk remaining after testing and mitigation.

Example:

A rare browser layout issue may remain open.

Residual risk may be acceptable if:

* No financial integrity issue exists.
* No security risk exists.
* A workaround exists.
* Business accepts the impact.

---

# 82. Unacceptable Residual Risk

Residual risk is normally unacceptable when related to:

* Incorrect balances
* Unauthorized access
* Unauthorized transfers
* Duplicate transactions
* Financial data loss
* Authentication bypass

---

# 83. Risk Review Frequency

The risk register should be reviewed:

* When major features are added
* When architecture changes
* After serious defects
* Before release
* After production-like testing
* When business rules change

---

# 84. Risk Change Example

Suppose transaction retry functionality is introduced.

Initially:

```text
Duplicate Transaction Risk
Probability = 3
Impact = 5
Score = 15
```

If retry logic proves unstable:

```text
Probability = 5
Impact = 5
Score = 25
```

Testing priority should increase accordingly.

---

# 85. Defect-Based Risk Adjustment

Defects provide evidence about actual risk.

Example:

If several card-state defects are discovered, card functionality may receive higher risk classification and increased regression coverage.

Risk assessment is therefore not static.

---

# 86. Risk Traceability

High-risk requirements should map to:

```text
Risk
   ↓
Requirement
   ↓
Scenario
   ↓
Test Case
   ↓
Automation
   ↓
Execution Result
```

This ensures important risks are actually covered.

---

# 87. Example Risk Traceability

```text
RISK-003
Duplicate Transaction
       ↓
REQ-TRF-015
Duplicate submission must not create multiple transfers
       ↓
TS-TRF-020
Repeated transfer submission
       ↓
TC-TRF-061
Double-click Confirm
       ↓
Playwright / REST Assured / JMeter coverage
```

---

# 88. Risk-Based Regression Selection

When changes are made, regression should consider both:

```text
Changed functionality
+
High-risk related functionality
```

Example:

A transfer fee calculation change should trigger regression for:

* Transfers
* Account balances
* Transaction history
* Statements
* Limits
* Fees
* API transfer behavior
* Database transaction data

---

# 89. Risk Matrix

A simplified matrix:

| Impact ↓ / Probability → |  1 |  2 |  3 |  4 |  5 |
| ------------------------ | -: | -: | -: | -: | -: |
| 5                        |  5 | 10 | 15 | 20 | 25 |
| 4                        |  4 |  8 | 12 | 16 | 20 |
| 3                        |  3 |  6 |  9 | 12 | 15 |
| 2                        |  2 |  4 |  6 |  8 | 10 |
| 1                        |  1 |  2 |  3 |  4 |  5 |

Higher scores receive stronger coverage and greater release attention.

---

# 90. Testing Risk vs Product Risk

Two kinds of risk must be distinguished.

## Product Risk

The application behaves incorrectly.

Example:

```text
Transfer duplicates.
```

## Testing Risk

QA cannot confidently determine product quality.

Example:

```text
QA environment constantly resets balances.
```

Both can affect release confidence.

---

# 91. Major Testing Risks

Testing-specific risks include:

* Unstable environment
* Incomplete requirements
* Missing test data
* Limited database access
* Shared test users
* External dependency failures
* Test automation instability
* Lack of production-like staging

---

# 92. Testing Risk Mitigation

## Unstable Environment

Mitigation:

* Health checks
* Smoke tests
* Deployment tracking

## Incomplete Requirements

Mitigation:

* Requirement review
* Assumptions documentation
* Business-rule clarification

## Shared Test Data

Mitigation:

* Dedicated users
* Reset mechanisms
* Isolated automation accounts

## Flaky Automation

Mitigation:

* Stable selectors
* Explicit state setup
* Avoid fixed sleeps
* Test isolation

---

# 93. Third-Party Dependency Risk

External integrations may be unavailable.

Potential services:

* Email
* SMS
* External payment provider
* Identity verification provider

Mitigation:

* Sandbox
* Mocks
* Stubs
* Failure simulation

---

# 94. Database Risk

Database-related risks include:

* Precision errors
* Missing constraints
* Orphan records
* Duplicate records
* Failed migrations
* Incorrect transaction isolation

These risks justify dedicated SQL validation later in the project.

---

# 95. Automation Risk

Poor automation can create false confidence.

Examples:

* Test checks only success message.
* Test does not validate balance.
* Test uses shared data.
* Test retries flaky failures automatically without investigation.

Automation quality must therefore be reviewed alongside automation quantity.

---

# 96. False Confidence Risk

Example:

Automated transfer test:

```text
Click Transfer
Verify "Success" message
```

This is insufficient.

A stronger test verifies:

```text
Success message
+
Transaction reference
+
Source balance
+
Destination effect where accessible
+
Transaction history
```

API and SQL testing can provide deeper confirmation.

---

# 97. Monitoring Risk After Release

In a real banking system, residual risk would also be managed through monitoring.

Examples:

* Failed transaction rate
* Duplicate transaction detection
* Authentication failures
* API error rate
* Latency
* Unusual administrative activity

Operational monitoring is beyond the initial project scope but should be considered part of real-world quality engineering.

---

# 98. Risk Acceptance

Some non-critical risks may be accepted.

Risk acceptance should document:

* Risk
* Business impact
* Reason for acceptance
* Workaround
* Owner
* Planned future action

Critical banking-security and financial-integrity risks should not be casually accepted.

---

# 99. Risk Assessment Success Criteria

The risk strategy is successful when:

* Critical risks have explicit test coverage.
* High-risk areas receive deeper testing.
* Regression reflects risk.
* Automation reflects risk.
* Release decisions reflect impact rather than raw pass percentage.
* Newly discovered defects can update the risk model.
* Critical financial and authorization risks remain visible throughout the project.

---

# 100. Final Risk Principle

The goal of QA is not to prove that every possible defect has been removed.

The goal is to reduce uncertainty and understand the remaining risk.

For the Banking System, the most important question is not:

```text
How many test cases passed?
```

The more important questions are:

```text
Can money move incorrectly?

Can unauthorized users access protected resources?

Can balances become inconsistent?

Can transactions execute twice?

Can failures leave the system in a partial state?

Can important actions occur without traceability?
```

These risks will drive the depth and priority of all later manual, automated, API, database, performance, BDD, and CI/CD testing.
