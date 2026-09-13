# Banking System — Exploratory Testing Session Reports

## 1. Document Information

| Field        | Value                          |
| ------------ | ------------------------------ |
| Project      | Banking System Testing Project |
| Testing Type | Exploratory Testing            |
| Document     | Session Reports                |
| Version      | 1.0                            |
| Status       | Draft                          |
| Owner        | QA Engineering                 |

---

# 2. Purpose

This document provides:

* A reusable exploratory testing session template
* Guidelines for documenting exploratory work
* Sample session reports
* Example observations
* Example defect discoveries
* Example follow-up actions

Exploratory sessions are based on the charters defined in:

```text
manual-testing/exploratory-testing/exploratory-charters.md
```

The reports below are **sample portfolio examples** until the actual Banking System is implemented and the exploratory sessions are executed.

Actual execution results should replace or supplement the sample data later.

---

# 3. Session Naming Convention

Exploratory sessions use:

```text
ES-XXX
```

Example:

```text
ES-001
```

Each session references a charter:

```text
Charter: ETC-015
```

---

# 4. Session Status

Possible statuses:

```text
PLANNED
IN_PROGRESS
COMPLETED
BLOCKED
FOLLOW_UP_REQUIRED
```

---

# 5. Session Outcome

Recommended outcomes:

```text
PASS
PASS_WITH_OBSERVATIONS
DEFECTS_FOUND
BLOCKED
INCONCLUSIVE
```

Exploratory testing should not be treated as simple pass/fail execution.

A session may successfully achieve its mission while discovering multiple defects.

---

# 6. Reusable Exploratory Session Template

```text
# Exploratory Session Report

Session ID:
Charter ID:
Charter Title:

Tester:
Date:
Start Time:
End Time:
Duration:

Environment:
Application URL:
Build / Version:
Browser:
Device / Viewport:

Session Status:
Session Outcome:

## Mission

Describe the purpose of this exploratory session.

## Scope

List the features, workflows, and risks explored.

## Test Data

Customer:
Account:
Beneficiary:
Card:
Loan:
Deposit:
Other Data:

## Preconditions

Describe the required system state before exploration begins.

## Exploration Performed

1.
2.
3.
4.
5.

## Observations

- Observation 1
- Observation 2
- Observation 3

## Defects Found

| Defect ID | Summary | Severity | Priority | Status |
|---|---|---|---|---|

## Financial Validation

Opening Balance:

Expected Closing Balance:

Actual Closing Balance:

Reconciled:
YES / NO / N/A

## Security Validation

Authorization checked:
YES / NO

Cross-customer isolation checked:
YES / NO

Sensitive data exposure checked:
YES / NO

## Evidence

Screenshots:
Videos:
Requests/Responses:
Logs:
Transaction References:
Timestamps:

## Questions / Requirements Clarifications

-

## Risks Identified

-

## Areas Not Covered

-

## Follow-Up Actions

-

## Session Conclusion

Summarize the result and whether more testing is required.
```

---

# 7. Session Report Guidance

A strong exploratory report should allow another tester to understand:

```text
What was explored?

Why was it explored?

What data was used?

What unusual actions were attempted?

What happened?

What defects were discovered?

What remains uncertain?

What should happen next?
```

For financial sessions, also record:

```text
Balance before

Balance after

Expected financial impact

Actual financial impact

Transaction reference

Related account IDs

Timestamp
```

---

# 8. Sample Session ES-001 — Duplicate Transfer Exploration

## Session Information

| Field      | Value                                    |
| ---------- | ---------------------------------------- |
| Session ID | ES-001                                   |
| Charter    | ETC-015 — Duplicate Transfer Exploration |
| Status     | SAMPLE                                   |
| Timebox    | 90 minutes                               |
| Outcome    | DEFECTS_FOUND                            |
| Priority   | P0                                       |

---

## Mission

Investigate whether repeated submission, browser behavior, network retry, or concurrent activity could create multiple financial transfers from a single intended customer action.

---

## Scope

Explored:

* Double-click submission
* Browser refresh
* Browser Back
* Two-tab submission
* Request replay
* Timeout retry
* Idempotency behavior

---

## Test Data

```text
Customer: CUST-001
Source Account: ACC-001
Beneficiary: BEN-001
Opening Balance: 10,000.00
Transfer Amount: 1,000.00
Transfer Fee: 10.00
```

Expected balance after one successful transfer:

```text
10,000.00
- 1,000.00
- 10.00
= 8,990.00
```

---

## Exploration Performed

1. Submitted normal transfer once.
2. Repeated test using rapid double-click on confirmation.
3. Refreshed confirmation page after successful submission.
4. Navigated backward and attempted resubmission.
5. Opened same transfer flow in two browser tabs.
6. Simulated delayed response and retried request.
7. Replayed same API request with same transaction context.

---

## Sample Observations

* Confirmation button should become unavailable immediately after submission.
* Refresh should not repeat the financial action.
* Browser Back should not recreate a completed transfer.
* Same idempotency context should return prior result.
* Transaction history should contain only one intended debit.

---

## Sample Defect

| Defect ID | Summary                                                                 | Severity | Priority | Status      |
| --------- | ----------------------------------------------------------------------- | -------- | -------- | ----------- |
| BUG-001   | Rapid double-click creates two transfers before button becomes disabled | Critical | P0       | Sample/Open |

### Expected

One intended transfer:

```text
Debit = 1,000.00
Fee = 10.00
Final Balance = 8,990.00
```

### Sample Actual

Two transfers processed:

```text
Debit 1 = 1,000.00
Fee 1 = 10.00

Debit 2 = 1,000.00
Fee 2 = 10.00

Actual Final Balance = 7,980.00
```

### Risk

Duplicate financial transaction and incorrect account balance.

Relevant risks:

```text
RISK-001
RISK-003
RISK-042
```

---

## Follow-Up

* Create dedicated regression case.
* Add API idempotency test.
* Add UI double-submit automation.
* Verify unique transaction/idempotency constraint.
* Add concurrency test.

---

## Conclusion

This sample session demonstrates why duplicate-submission behavior requires testing beyond standard scripted happy-path transfer cases.

---

# 9. Sample Session ES-002 — Balance Integrity Exploration

## Session Information

| Field      | Value                                   |
| ---------- | --------------------------------------- |
| Session ID | ES-002                                  |
| Charter    | ETC-010 — Balance Integrity Exploration |
| Status     | SAMPLE                                  |
| Timebox    | 90 minutes                              |
| Outcome    | PASS_WITH_OBSERVATIONS                  |
| Priority   | P0                                      |

---

## Mission

Explore account balance behavior after multiple different financial operations and validate reconciliation.

---

## Test Data

```text
Customer: CUST-001
Account: ACC-001
Opening Balance: 20,000.00
```

Operations:

```text
Transfer = -2,000.00
Transfer Fee = -20.00
Payment = -500.00
Incoming Transfer = +3,000.00
Card Purchase = -1,000.00
Refund = +250.00
```

Expected final balance:

```text
20,000
- 2,000
- 20
- 500
+ 3,000
- 1,000
+ 250
= 19,730.00
```

---

## Exploration Performed

1. Recorded opening current and available balances.
2. Completed transfer.
3. Completed payment.
4. Received incoming transfer.
5. Completed card purchase.
6. Processed partial refund.
7. Compared final balance.
8. Compared transaction history.
9. Compared generated statement.
10. Compared API/database values where available.

---

## Validation Checklist

```text
Dashboard balance = 19,730.00
Account details = 19,730.00
Transaction history reconciles = YES
Statement reconciles = YES
API balance = 19,730.00
Database balance = 19,730.00
```

---

## Observations

* All transactions must have matching references where applicable.
* Fees should be independently identifiable.
* Refund should not overwrite original purchase.
* Current and available balances may temporarily differ during pending operations.

---

## Follow-Up

Repeat with:

* Pending transactions
* Reversals
* Concurrent operations
* Exact-balance scenarios
* Very small decimal values

---

# 10. Sample Session ES-003 — Insufficient Funds Exploration

## Session Information

| Field      | Value                                    |
| ---------- | ---------------------------------------- |
| Session ID | ES-003                                   |
| Charter    | ETC-014 — Insufficient Funds Exploration |
| Status     | SAMPLE                                   |
| Timebox    | 60 minutes                               |
| Outcome    | DEFECTS_FOUND                            |
| Priority   | P0                                       |

---

## Mission

Explore transfer behavior near the exact available balance, especially where transfer fees are applied.

---

## Test Data

```text
Available Balance = 5,000.00
Transfer Fee = 10.00
```

---

## Explored Values

```text
Transfer = 4,989.99
Total Debit = 4,999.99

Transfer = 4,990.00
Total Debit = 5,000.00

Transfer = 4,990.01
Total Debit = 5,000.01
```

---

## Expected

```text
4,989.99 → ACCEPT
4,990.00 → ACCEPT
4,990.01 → REJECT
```

---

## Sample Defect

| Defect ID | Summary                                                            | Severity | Priority |
| --------- | ------------------------------------------------------------------ | -------- | -------- |
| BUG-002   | Transfer validation ignores fee when calculating available balance | Critical | P0       |

### Sample Actual

Transfer amount:

```text
5,000.00
```

was accepted because:

```text
Transfer Amount <= Available Balance
```

but the fee later produced:

```text
Total Debit = 5,010.00
```

creating an invalid account state.

---

## Root Cause Hypothesis

Client/backend validation checks:

```text
amount <= availableBalance
```

instead of:

```text
amount + fee <= availableBalance
```

---

## Follow-Up

Add tests to:

* UI
* REST Assured
* Postman
* Database validation
* Concurrency suite

---

# 11. Sample Session ES-004 — Account Freeze Race

## Session Information

| Field      | Value                                   |
| ---------- | --------------------------------------- |
| Session ID | ES-004                                  |
| Charter    | ETC-055 — State Change Race Exploration |
| Status     | SAMPLE                                  |
| Timebox    | 90 minutes                              |
| Outcome    | DEFECTS_FOUND                           |
| Priority   | P0                                      |

---

## Mission

Explore interaction between a financial transaction and an administrative account freeze occurring at nearly the same time.

---

## Preconditions

```text
Customer: ACTIVE
Account: ACTIVE
Balance: 10,000
Beneficiary: ACTIVE
```

---

## Exploration

Session A:

```text
Prepare transfer
→ Reach confirmation
```

Session B:

```text
Admin freezes account
```

Session A:

```text
Submit transfer
```

Repeat with different timing.

---

## Expected

The system must use a deterministic transaction cutoff.

After freeze becomes authoritative:

```text
New prohibited transactions must be rejected.
```

---

## Sample Defect

| Defect ID | Summary                                                                          | Severity | Priority |
| --------- | -------------------------------------------------------------------------------- | -------- | -------- |
| BUG-003   | Transfer confirmation uses stale account state and succeeds after account freeze | Critical | P0       |

---

## Observation

The transfer page loaded account state before confirmation but final submission did not revalidate account status.

---

## Follow-Up

Add:

```text
Final-state validation before debit
API regression
Concurrent state-change test
Admin/account integration test
```

---

# 12. Sample Session ES-005 — MFA Bypass Exploration

## Session Information

| Field      | Value                     |
| ---------- | ------------------------- |
| Session ID | ES-005                    |
| Charter    | ETC-003 — MFA Exploration |
| Status     | SAMPLE                    |
| Timebox    | 60 minutes                |
| Outcome    | DEFECTS_FOUND             |
| Priority   | P0                        |

---

## Mission

Explore whether MFA can be bypassed through navigation, stale sessions, or direct protected-resource access.

---

## Exploration

1. Submit valid username/password.
2. Stop at MFA challenge.
3. Open dashboard URL manually.
4. Call protected account API.
5. Open transfer URL.
6. Refresh MFA page.
7. Open a second tab.
8. Use expired OTP.
9. Reuse successful OTP.

---

## Expected

Before MFA completion:

```text
Authentication state = incomplete
Protected resources = denied
```

---

## Sample Defect

| Defect ID | Summary                                                                    | Severity | Priority |
| --------- | -------------------------------------------------------------------------- | -------- | -------- |
| BUG-004   | Account API accessible after password validation but before MFA completion | Critical | P0       |

---

## Risk

Authentication bypass.

Related:

```text
RISK-005
RISK-030
```

---

## Follow-Up

Automate protected API checks for:

```text
Anonymous
Password-only
MFA-complete
Expired session
Revoked session
```

---

# 13. Sample Session ES-006 — Card Freeze Exploration

## Session Information

| Field      | Value                                  |
| ---------- | -------------------------------------- |
| Session ID | ES-006                                 |
| Charter    | ETC-024 — Card Freeze Race Exploration |
| Status     | SAMPLE                                 |
| Timebox    | 60 minutes                             |
| Outcome    | PASS_WITH_OBSERVATIONS                 |
| Priority   | P0                                     |

---

## Mission

Explore transaction authorization around card freeze timing.

---

## Sequence

```text
Card ACTIVE
→ Perform successful test purchase
→ Freeze card
→ Verify state FROZEN
→ Attempt new purchase
→ Attempt API-based purchase
→ Unfreeze card
→ Attempt purchase again
```

---

## Expected

```text
ACTIVE → purchase permitted
FROZEN → purchase rejected
ACTIVE after valid unfreeze → purchase permitted
```

---

## Additional Exploration

* Freeze from second browser.
* Purchase immediately during freeze.
* Refresh stale card page.
* Change limit while frozen.
* Block frozen card.

---

## Observation

Backend card status must remain authoritative even if another browser shows stale `ACTIVE` UI state.

---

# 14. Sample Session ES-007 — Duplicate Loan Disbursement

## Session Information

| Field      | Value                                   |
| ---------- | --------------------------------------- |
| Session ID | ES-007                                  |
| Charter    | ETC-029 — Loan Disbursement Exploration |
| Status     | SAMPLE                                  |
| Timebox    | 60 minutes                              |
| Outcome    | DEFECTS_FOUND                           |
| Priority   | P0                                      |

---

## Mission

Determine whether an approved loan can be credited more than once due to duplicate requests or concurrency.

---

## Preconditions

```text
Loan Status = APPROVED
Approved Amount = 100,000.00
Destination Account Balance = 20,000.00
```

Expected after disbursement:

```text
Account Balance = 120,000.00
Loan Status = DISBURSED / ACTIVE
```

---

## Exploration

* Double-click disbursement.
* Two admins disburse simultaneously.
* Retry after timeout.
* Replay API request.
* Refresh administrative page and resubmit.

---

## Sample Defect

| Defect ID | Summary                                            | Severity | Priority |
| --------- | -------------------------------------------------- | -------- | -------- |
| BUG-005   | Concurrent disbursement requests credit loan twice | Critical | P0       |

Sample actual balance:

```text
220,000.00
```

instead of:

```text
120,000.00
```

---

## Required Follow-Up

* Database uniqueness/locking
* Transaction-level atomicity
* Idempotency
* REST Assured concurrency test
* SQL validation
* Audit verification

---

# 15. Sample Session ES-008 — Deposit Maturity Exploration

## Session Information

| Field      | Value                                  |
| ---------- | -------------------------------------- |
| Session ID | ES-008                                 |
| Charter    | ETC-033 — Deposit Maturity Exploration |
| Status     | SAMPLE                                 |
| Timebox    | 90 minutes                             |
| Outcome    | DEFECTS_FOUND                          |
| Priority   | P0                                     |

---

## Mission

Explore maturity processing under retries and concurrent worker execution.

---

## Preconditions

```text
Principal = 50,000.00
Interest = 5,000.00
Maturity Payout = 55,000.00
Deposit Status = ACTIVE
```

---

## Expected

At maturity:

```text
Account receives exactly 55,000.00 once.
```

---

## Exploration

* Normal maturity worker.
* Same worker retried.
* Two workers simultaneously.
* Maturity + manual payout.
* Auto-renew + payout race.

---

## Sample Defect

| Defect ID | Summary                                                     | Severity | Priority |
| --------- | ----------------------------------------------------------- | -------- | -------- |
| BUG-006   | Duplicate maturity processing produces two customer payouts | Critical | P0       |

---

## Financial Impact

Expected:

```text
+55,000
```

Sample actual:

```text
+110,000
```

---

## Follow-Up

Add dedicated test for:

```text
Maturity idempotency
Distributed locking
Unique payout reference
Database constraint
Auto-renew vs payout race
```

---

# 16. Sample Session ES-009 — Transaction History Integrity

## Session Information

| Field      | Value                                               |
| ---------- | --------------------------------------------------- |
| Session ID | ES-009                                              |
| Charter    | ETC-036 — Transaction History Integrity Exploration |
| Status     | SAMPLE                                              |
| Timebox    | 90 minutes                                          |
| Outcome    | DEFECTS_FOUND                                       |
| Priority   | P0                                                  |

---

## Mission

Explore whether transaction history accurately reflects financial activity.

---

## Activity Generated

```text
Outgoing transfer
Incoming transfer
Payment
Card purchase
Fee
Refund
Loan repayment
Transfer reversal
```

---

## Expected

Each valid financial event should be represented exactly once with:

* Correct type
* Correct amount
* Correct direction
* Correct reference
* Correct status
* Correct timestamp

---

## Sample Defect

| Defect ID | Summary                                                                           | Severity | Priority |
| --------- | --------------------------------------------------------------------------------- | -------- | -------- |
| BUG-007   | Reversal replaces original transaction instead of creating linked reversal record | High     | P1       |

---

## Risk

Historical financial audit trail is lost.

---

## Follow-Up

Verify:

* Original transaction immutability
* Reversal relationship
* Statement behavior
* API response
* Database records

---

# 17. Sample Session ES-010 — Statement Reconciliation

## Session Information

| Field      | Value                                          |
| ---------- | ---------------------------------------------- |
| Session ID | ES-010                                         |
| Charter    | ETC-039 — Statement Reconciliation Exploration |
| Status     | SAMPLE                                         |
| Timebox    | 90 minutes                                     |
| Outcome    | DEFECTS_FOUND                                  |
| Priority   | P0                                             |

---

## Mission

Reconcile an account statement against transaction history and calculated balances.

---

## Financial Activity

Opening balance:

```text
25,000.00
```

Activity:

```text
Incoming Transfer +5,000.00
Payment -2,000.00
Payment Fee -20.00
Transfer -1,500.00
Refund +500.00
```

Expected closing:

```text
25,000
+ 5,000
- 2,000
- 20
- 1,500
+ 500
= 26,980.00
```

---

## Sample Actual

```text
Statement Closing Balance = 27,000.00
Account Balance = 26,980.00
```

---

## Sample Defect

| Defect ID | Summary                                                         | Severity | Priority |
| --------- | --------------------------------------------------------------- | -------- | -------- |
| BUG-008   | Statement calculation excludes payment fee from closing balance | Critical | P0       |

---

## Follow-Up

Validate fee representation across:

```text
Transaction history
Statement
API
Database
Account balance
```

---

# 18. Sample Session ES-011 — Customer Isolation Exploration

## Session Information

| Field      | Value                                    |
| ---------- | ---------------------------------------- |
| Session ID | ES-011                                   |
| Charter    | ETC-074 — Customer Isolation Exploration |
| Status     | SAMPLE                                   |
| Timebox    | 90 minutes                               |
| Outcome    | DEFECTS_FOUND                            |
| Priority   | P0                                       |

---

## Mission

Determine whether Customer A can access Customer B's banking resources.

---

## Test Users

```text
Customer A = CUST-001
Customer B = CUST-002
```

Resources tested:

* Accounts
* Beneficiaries
* Transactions
* Statements
* Cards
* Loans
* Deposits
* Notifications

---

## Exploration

1. Login as Customer A.
2. Open owned resource.
3. Change resource identifier to Customer B's identifier.
4. Repeat through API.
5. Test deep links.
6. Test download endpoints.
7. Test cached browser pages.

---

## Sample Defect

| Defect ID | Summary                                                                     | Severity | Priority |
| --------- | --------------------------------------------------------------------------- | -------- | -------- |
| BUG-009   | Customer can download another customer's statement by changing statement ID | Critical | P0       |

---

## Impact

Unauthorized financial-data disclosure.

---

## Follow-Up

Add authorization checks to:

```text
Statement metadata endpoint
Statement download endpoint
Generated-file storage authorization
```

Add automated IDOR regression.

---

# 19. Sample Session ES-012 — Notification Accuracy

## Session Information

| Field      | Value                                       |
| ---------- | ------------------------------------------- |
| Session ID | ES-012                                      |
| Charter    | ETC-042 — Notification Accuracy Exploration |
| Status     | SAMPLE                                      |
| Timebox    | 90 minutes                                  |
| Outcome    | DEFECTS_FOUND                               |
| Priority   | P0                                          |

---

## Mission

Compare customer notifications with authoritative transaction state.

---

## Events

* Successful transfer
* Failed transfer
* Successful payment
* Failed payment
* Card freeze
* Password change
* Deposit maturity
* Loan approval

---

## Sample Defect

| Defect ID | Summary                                               | Severity | Priority |
| --------- | ----------------------------------------------------- | -------- | -------- |
| BUG-010   | Failed payment generates payment-success notification | High     | P1       |

---

## Expected

```text
Payment Status = FAILED
Notification = Failure or no success notification
Balance = unchanged
```

Sample actual:

```text
Payment Status = FAILED
Notification = "Payment completed successfully"
```

---

## Related Risk

```text
RISK-034
RISK-048
```

---

# 20. Sample Session ES-013 — Admin Privilege Exploration

## Session Information

| Field      | Value                                 |
| ---------- | ------------------------------------- |
| Session ID | ES-013                                |
| Charter    | ETC-050 — Role Permission Exploration |
| Status     | SAMPLE                                |
| Timebox    | 90 minutes                            |
| Outcome    | DEFECTS_FOUND                         |
| Priority   | P0                                    |

---

## Mission

Explore whether limited administrative roles can access privileged functions.

---

## Roles

```text
SUPER_ADMIN
OPERATIONS_ADMIN
KYC_REVIEWER
LOAN_OFFICER
READ_ONLY_ADMIN
```

---

## Tested Actions

* Freeze account
* Approve KYC
* Approve loan
* Reverse transaction
* View audit
* Modify limit

---

## Sample Defect

| Defect ID | Summary                                                         | Severity | Priority |
| --------- | --------------------------------------------------------------- | -------- | -------- |
| BUG-011   | Read-only admin can reverse transfer through direct API request | Critical | P0       |

---

## Observation

UI correctly hides the button, but API permission validation is missing.

---

## Follow-Up

Add role-based API tests for every privileged endpoint.

---

# 21. Sample Session ES-014 — Financial Precision

## Session Information

| Field      | Value                                                  |
| ---------- | ------------------------------------------------------ |
| Session ID | ES-014                                                 |
| Charter    | ETC-070 — Financial Precision and Rounding Exploration |
| Status     | SAMPLE                                                 |
| Timebox    | 60 minutes                                             |
| Outcome    | DEFECTS_FOUND                                          |
| Priority   | P0                                                     |

---

## Mission

Investigate monetary calculations around rounding boundaries.

---

## Values Tested

```text
10.004
10.005
10.006
0.01
0.10
999999.99
```

Areas:

* Loan interest
* Deposit interest
* Fees
* Refunds
* Statement totals

---

## Sample Defect

| Defect ID | Summary                                                                 | Severity | Priority |
| --------- | ----------------------------------------------------------------------- | -------- | -------- |
| BUG-012   | Loan schedule rounds each calculation differently from final loan total | High     | P1       |

---

## Observation

Installment schedule total differed from total repayable by:

```text
0.03
```

---

## Follow-Up

Define one authoritative monetary rounding rule and use consistently in:

```text
Backend calculations
Database persistence
UI
Statements
Tests
```

---

# 22. Sample Session ES-015 — Network Failure During Transfer

## Session Information

| Field      | Value                                 |
| ---------- | ------------------------------------- |
| Session ID | ES-015                                |
| Charter    | ETC-057 — Network Failure Exploration |
| Status     | SAMPLE                                |
| Timebox    | 90 minutes                            |
| Outcome    | DEFECTS_FOUND                         |
| Priority   | P0                                    |

---

## Mission

Explore customer behavior when network connectivity disappears during transfer processing.

---

## Exploration

1. Start valid transfer.
2. Submit transfer.
3. Disconnect network before response.
4. Reconnect.
5. Observe page.
6. Check transaction history.
7. Check balance.
8. Retry transfer.

---

## Expected

Application should allow customer to determine whether the first transfer succeeded before creating another financial effect.

---

## Sample Defect

| Defect ID | Summary                                                                                     | Severity | Priority |
| --------- | ------------------------------------------------------------------------------------------- | -------- | -------- |
| BUG-013   | Timeout displays generic failure even though transfer completed, leading to duplicate retry | Critical | P0       |

---

## Sample Sequence

```text
Transfer #1 backend = COMPLETED

UI = "Something went wrong"

Customer retries

Transfer #2 = COMPLETED
```

Result:

```text
Two debits for one intended transfer
```

---

## Follow-Up

Implement or validate:

* Idempotency
* transaction-status recovery
* clear ambiguous-state messaging
* safe retry behavior

---

# 23. Sample Session ES-016 — Large Transaction History

## Session Information

| Field      | Value                                  |
| ---------- | -------------------------------------- |
| Session ID | ES-016                                 |
| Charter    | ETC-073 — High-Volume Data Exploration |
| Status     | SAMPLE                                 |
| Timebox    | 90 minutes                             |
| Outcome    | PASS_WITH_OBSERVATIONS                 |
| Priority   | P1                                     |

---

## Mission

Explore transaction-history behavior with a large dataset.

---

## Test Data

```text
Account transactions: 5,000+
```

---

## Areas Explored

* Initial load
* Pagination
* Search
* Date filtering
* Type filtering
* Status filtering
* Sorting
* New transaction while paging
* Export

---

## Observations

Verify:

```text
No duplicated rows
No missing page-boundary records
Stable sort
Correct pagination metadata
Filters apply to entire dataset
```

---

## Follow-Up

Use JMeter later for dedicated performance testing.

---

# 24. Sample Session ES-017 — Browser Navigation Replay

## Session Information

| Field      | Value                                          |
| ---------- | ---------------------------------------------- |
| Session ID | ES-017                                         |
| Charter    | ETC-072 — Back / Forward / Refresh Exploration |
| Status     | SAMPLE                                         |
| Timebox    | 60 minutes                                     |
| Outcome    | DEFECTS_FOUND                                  |
| Priority   | P1                                             |

---

## Mission

Explore whether browser navigation can accidentally repeat state-changing operations.

---

## Target Workflows

* Transfer
* Payment
* Beneficiary creation
* Deposit opening
* Loan application

---

## Sample Defect

| Defect ID | Summary                                                                 | Severity | Priority |
| --------- | ----------------------------------------------------------------------- | -------- | -------- |
| BUG-014   | Refreshing deposit confirmation page resubmits deposit creation request | Critical | P0       |

---

## Impact

Possible:

```text
Duplicate deposit
Duplicate principal debit
```

---

# 25. Sample Session ES-018 — Scheduled Transfer Timing

## Session Information

| Field      | Value                                    |
| ---------- | ---------------------------------------- |
| Session ID | ES-018                                   |
| Charter    | ETC-018 — Scheduled Transfer Exploration |
| Status     | SAMPLE                                   |
| Timebox    | 90 minutes                               |
| Outcome    | DEFECTS_FOUND                            |
| Priority   | P1                                       |

---

## Mission

Explore scheduled-transfer execution around time, status, and account changes.

---

## Cases Explored

* Normal execution
* Cancel before execution
* Freeze source before execution
* Disable beneficiary before execution
* Insufficient funds
* Month boundary
* Timezone changes

---

## Sample Defect

| Defect ID | Summary                                                                                | Severity | Priority |
| --------- | -------------------------------------------------------------------------------------- | -------- | -------- |
| BUG-015   | Cancelled scheduled transfer still executes because worker loaded stale schedule state | Critical | P0       |

---

## Related Risk

```text
RISK-029
```

---

# 26. Sample Session ES-019 — Password Change and Sessions

## Session Information

| Field      | Value                                        |
| ---------- | -------------------------------------------- |
| Session ID | ES-019                                       |
| Charter    | ETC-008 — Security-Sensitive Profile Changes |
| Status     | SAMPLE                                       |
| Timebox    | 60 minutes                                   |
| Outcome    | DEFECTS_FOUND                                |
| Priority   | P0                                           |

---

## Mission

Explore how password changes affect existing sessions.

---

## Exploration

```text
Login Device A
Login Device B
Change password on Device A
Attempt transfer from Device B
```

---

## Sample Defect

| Defect ID | Summary                                                                                           | Severity | Priority |
| --------- | ------------------------------------------------------------------------------------------------- | -------- | -------- |
| BUG-016   | Old session remains authorized for financial operations after security policy requires revocation | Critical | P0       |

---

## Follow-Up

Verify session strategy for:

* Password changes
* MFA changes
* Account disable
* User suspension
* Explicit session revocation

---

# 27. Sample Session ES-020 — Deposit Renewal / Payout Race

## Session Information

| Field      | Value                            |
| ---------- | -------------------------------- |
| Session ID | ES-020                           |
| Charter    | ETC-035 — Renewal vs Payout Race |
| Status     | SAMPLE                           |
| Timebox    | 60 minutes                       |
| Outcome    | DEFECTS_FOUND                    |
| Priority   | P0                               |

---

## Mission

Explore concurrency between auto-renewal and maturity payout.

---

## Expected

Only one maturity outcome:

```text
PAYOUT
or
RENEWAL
```

according to the customer's valid configuration and authoritative processing decision.

Never both.

---

## Sample Defect

| Defect ID | Summary                                                         | Severity | Priority |
| --------- | --------------------------------------------------------------- | -------- | -------- |
| BUG-017   | Deposit auto-renews while maturity payout also credits customer | Critical | P0       |

---

## Impact

Customer receives principal while also retaining renewed deposit principal.

This creates money incorrectly.

---

# 28. Sample Session Summary Table

| Session | Charter | Area                | Sample Outcome       | Defects |
| ------- | ------- | ------------------- | -------------------- | ------: |
| ES-001  | ETC-015 | Duplicate Transfer  | Defects Found        |       1 |
| ES-002  | ETC-010 | Balance Integrity   | Pass w/ Observations |       0 |
| ES-003  | ETC-014 | Insufficient Funds  | Defects Found        |       1 |
| ES-004  | ETC-055 | Freeze Race         | Defects Found        |       1 |
| ES-005  | ETC-003 | MFA                 | Defects Found        |       1 |
| ES-006  | ETC-024 | Card Freeze         | Pass w/ Observations |       0 |
| ES-007  | ETC-029 | Loan Disbursement   | Defects Found        |       1 |
| ES-008  | ETC-033 | Deposit Maturity    | Defects Found        |       1 |
| ES-009  | ETC-036 | Transaction History | Defects Found        |       1 |
| ES-010  | ETC-039 | Statements          | Defects Found        |       1 |
| ES-011  | ETC-074 | Customer Isolation  | Defects Found        |       1 |
| ES-012  | ETC-042 | Notifications       | Defects Found        |       1 |
| ES-013  | ETC-050 | Admin Permissions   | Defects Found        |       1 |
| ES-014  | ETC-070 | Precision           | Defects Found        |       1 |
| ES-015  | ETC-057 | Network Failure     | Defects Found        |       1 |
| ES-016  | ETC-073 | Large Dataset       | Pass w/ Observations |       0 |
| ES-017  | ETC-072 | Browser Replay      | Defects Found        |       1 |
| ES-018  | ETC-018 | Scheduled Transfer  | Defects Found        |       1 |
| ES-019  | ETC-008 | Password/Sessions   | Defects Found        |       1 |
| ES-020  | ETC-035 | Deposit Race        | Defects Found        |       1 |

These are sample portfolio findings and must not be represented as actual executed defects until verified against the implemented Banking System.

---

# 29. Example Follow-Up Workflow

When exploratory testing discovers an important issue:

```text
Exploratory Session
        ↓
Defect Report
        ↓
Risk Assessment Update
        ↓
Scripted Regression Test
        ↓
Automated Regression Test
        ↓
Retest
        ↓
Regression Execution
```

Example:

```text
ES-001
↓
BUG-001 Duplicate Transfer
↓
Add RISK-003 coverage
↓
Create detailed transfer test case
↓
Add Playwright/API idempotency regression
↓
Retest fix
```

---

# 30. Converting Exploratory Findings Into Test Cases

Exploration:

```text
Customer refreshes after transfer timeout and retries.
```

Finding:

```text
Duplicate transfer created.
```

Convert into structured test case:

```text
Given a transfer request is successfully processed
But the client does not receive the response
When the same operation is retried
Then the system should not create a second financial transaction.
```

This should later become:

* Manual regression test case
* API automated test
* UI automated test where relevant
* Possibly JMeter concurrency/retry scenario

---

# 31. Session Metrics

Useful exploratory metrics include:

```text
Number of sessions executed

Total exploratory testing time

Defects discovered

Critical defects

High defects

Charters completed

Charters requiring follow-up

New risks identified

New regression tests created
```

Metrics should support decisions rather than measure tester productivity blindly.

---

# 32. Suggested Execution Dashboard

| Metric               | Value |
| -------------------- | ----- |
| Planned Charters     | TBD   |
| Completed Charters   | TBD   |
| Total Session Hours  | TBD   |
| Critical Defects     | TBD   |
| High Defects         | TBD   |
| Medium Defects       | TBD   |
| Low Defects          | TBD   |
| Follow-Up Sessions   | TBD   |
| New Regression Cases | TBD   |

Update after real execution.

---

# 33. Evidence Standards for Financial Defects

For any issue involving money, include:

```text
Customer ID / Test User
Account ID
Opening Balance
Available Balance
Transaction Amount
Fee
Expected Balance
Actual Balance
Transaction Reference
Related Transaction Reference
Timestamp
Request
Response
Database Evidence
Screenshot
```

Never include real customer secrets or production financial data.

---

# 34. Evidence Standards for Authorization Defects

Include:

```text
Authenticated User A
Target Resource Owner B
Resource Type
Resource ID or safe test identifier
Endpoint/Workflow
Expected Authorization Result
Actual Result
Returned Data
Response Code
Screenshot/Response Evidence
```

Use synthetic test data only.

---

# 35. Evidence Standards for Concurrency Defects

Record:

```text
Initial State
Initial Balance
Request A
Request B
Request A Timestamp
Request B Timestamp
Response A
Response B
Final State
Final Balance
Created Transactions
Database State
```

This helps developers reproduce timing-dependent defects.

---

# 36. Session Report Quality Checklist

Before completing a session report, verify:

```text
Charter referenced?

Mission clear?

Environment recorded?

Build recorded?

Test data recorded?

Important actions documented?

Observations separated from assumptions?

Defects linked?

Financial impact recorded where relevant?

Evidence captured?

Uncovered areas listed?

Follow-up defined?

Conclusion written?
```

---

# 37. Exploratory Session Exit Criteria

A session may be closed when:

* The timebox is complete.
* The charter mission has been sufficiently explored.
* Important observations are documented.
* Defects are recorded.
* Questions are captured.
* Follow-up testing is identified.
* Evidence is stored appropriately.

A session marked `BLOCKED` should identify:

```text
Why testing stopped
What dependency is missing
What coverage remains
```

---

# 38. Exploratory Reporting Principles

Do not write:

```text
Tested transfers. Everything looks fine.
```

Prefer:

```text
Explored transfer duplicate-submission behavior using double-click,
browser refresh, Back navigation, two concurrent tabs, and retry after
simulated timeout. Standard submission and Back navigation did not
produce duplicate financial effects. The timeout/retry path requires
additional API-level investigation.
```

Good reports explain both:

```text
Coverage
and
Remaining uncertainty
```

---

# 39. Portfolio Usage

This document demonstrates the ability to:

* Plan exploratory testing
* Execute risk-based sessions
* Investigate financial workflows
* Capture reproducible findings
* Link defects to business risk
* Convert discoveries into regression coverage
* Work beyond scripted test cases

When the actual Banking System is available, sample session reports should be replaced or clearly supplemented with real execution evidence.

---

# 40. Final Exploratory Session Reporting Principle

An exploratory testing report should answer:

```text
What did I investigate?

Why was it important?

What variations did I try?

What did the system actually do?

Did money, state, authorization, and data remain correct?

What defects did I discover?

What remains untested?

What should we test next?
```

The core rule is:

```text
Exploratory testing creates value only when learning is captured
and converted into actionable quality information.
```

For this Banking System, every high-risk discovery should feed back into:

```text
Requirements
Risk assessment
Test scenarios
Detailed test cases
Regression suites
Automation
API testing
Database testing
Performance testing
```

so that an exploratory discovery becomes permanent regression protection.
