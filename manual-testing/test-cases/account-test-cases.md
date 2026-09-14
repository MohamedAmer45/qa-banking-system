# Banking System — Account Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Accounts                       |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Account scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Account listing
* Account ownership
* Account details
* Account creation
* Account numbers
* Current balance
* Available balance
* Holds
* Account states
* Freeze/unfreeze
* Restriction/suspension
* Account closure
* Balance integrity
* Financial reconciliation
* Own-account transfers
* Limits
* Concurrency
* Duplicate processing
* API validation
* Database validation
* Transaction-history consistency
* Statement consistency
* Audit
* Notifications
* Security
* Cross-browser behavior
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Account test cases use:

```text
ACC-TC-XXX
```

Examples:

```text
ACC-TC-001
ACC-TC-002
ACC-TC-003
```

---

# 4. Common Test Data

## Customer A

```text
Customer:
CUST-001

Status:
ACTIVE

KYC:
VERIFIED
```

## Customer B

```text
Customer:
CUST-002

Status:
ACTIVE

KYC:
VERIFIED
```

## Primary Account

```text
Account:
ACC-001

Owner:
CUST-001

Type:
CHECKING

Status:
ACTIVE

Current Balance:
10,000.00

Available Balance:
10,000.00

Currency:
EGP
```

## Secondary Customer A Account

```text
Account:
ACC-003

Owner:
CUST-001

Type:
SAVINGS

Status:
ACTIVE

Current Balance:
5,000.00

Available Balance:
5,000.00

Currency:
EGP
```

## Customer B Account

```text
Account:
ACC-002

Owner:
CUST-002

Status:
ACTIVE
```

## Frozen Account

```text
Account:
ACC-004

Owner:
CUST-001

Status:
FROZEN
```

## Restricted Account

```text
Account:
ACC-005

Owner:
CUST-001

Status:
RESTRICTED
```

## Closed Account

```text
Account:
ACC-006

Owner:
CUST-001

Status:
CLOSED
```

---

# 5. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Account service is available.

Customer is authenticated.

Synthetic test accounts exist.

Required balances are prepared.

Database access is available when DB validation is required.

No real customer financial data is used.
```

---

# 6. Account List Test Cases

## ACC-TC-001 — View Own Accounts

**Priority:** P0
**Requirement:** REQ-ACC-001
**Automation:** Playwright / Selenium / API

### Steps

1. Login as `CUST-001`.
2. Open Accounts.
3. Review account list.

### Expected Result

Only accounts owned by `CUST-001` are displayed.

Each account shows applicable summary information such as:

* Account name/type
* Masked account number
* Currency
* Current balance
* Available balance
* Status

---

## ACC-TC-002 — Customer With Multiple Accounts

**Priority:** P1

### Expected Result

All Customer A accounts appear once.

No duplicate account cards/rows are displayed.

---

## ACC-TC-003 — Customer With No Accounts

**Priority:** P2

### Expected Result

A clear empty state is displayed.

No unrelated customer account is shown.

---

## ACC-TC-004 — Account List API Matches UI

**Priority:** P1

### Expected Result

UI accounts match authorized accounts returned by API.

---

# 7. Account Ownership / IDOR Test Cases

## ACC-TC-005 — Customer Cannot View Another Customer's Account

**Priority:** P0
**Requirement:** REQ-ACC-002
**Risk:** RISK-002, RISK-047

### Preconditions

Authenticated:

```text
CUST-001
```

Target:

```text
ACC-002
Owner: CUST-002
```

### Steps

1. Login as Customer A.
2. Attempt direct access to Customer B's account.

### Expected Result

* Access denied.
* No balance or account details returned.
* No sensitive metadata exposed.

---

## ACC-TC-006 — Modify Account ID in URL

**Priority:** P0

### Steps

1. Open `ACC-001`.
2. Replace the account ID with `ACC-002`.
3. Submit request.

### Expected Result

Customer B's account remains inaccessible.

---

## ACC-TC-007 — Modify Account ID in API

**Priority:** P0
**Automation:** REST Assured

### Expected Result

Backend performs ownership validation independently of UI.

---

## ACC-TC-008 — Customer Cannot Modify Another Customer's Account

**Priority:** P0

### Expected Result

Any state-changing request targeting `ACC-002` is denied.

---

# 8. Account Detail Test Cases

## ACC-TC-009 — View Active Account Details

**Priority:** P0

### Expected Result

Correct account information is shown.

---

## ACC-TC-010 — Account Number Is Correctly Masked

**Priority:** P1
**Risk:** RISK-021

### Expected Result

Only permitted account-number portions are displayed where masking is required.

---

## ACC-TC-011 — Account Number Remains Stable

**Priority:** P1

### Expected Result

Account number does not unexpectedly change between sessions.

---

## ACC-TC-012 — Account Currency Display

**Priority:** P1

### Expected Result

Correct currency is displayed consistently.

---

## ACC-TC-013 — Account Type Display

**Priority:** P2

### Expected Result

Account type matches authoritative account record.

---

# 9. Account Creation Test Cases

## ACC-TC-014 — Create Valid Account

**Priority:** P1

### Preconditions

Customer is eligible for requested account type.

### Steps

1. Open account creation flow.
2. Select valid product/account type.
3. Submit required information.
4. Confirm.

### Expected Result

* One account is created.
* Account belongs to authenticated customer.
* Unique account number is generated.
* Initial status follows product rules.

---

## ACC-TC-015 — Duplicate Account Submission

**Priority:** P1
**Type:** Idempotency

### Steps

Rapidly submit valid account-creation request twice.

### Expected Result

Duplicate unintended account creation is prevented according to design.

---

## ACC-TC-016 — Ineligible Customer Creates Account

**Priority:** P0

### Expected Result

Account creation is denied according to eligibility/KYC rules.

---

## ACC-TC-017 — Unsupported Account Type

**Priority:** P2

### Expected Result

Invalid account type rejected.

---

# 10. Account Number Test Cases

## ACC-TC-018 — New Account Number Is Unique

**Priority:** P0
**Automation:** SQL

### Expected Result

New account number does not duplicate existing account numbers.

---

## ACC-TC-019 — Concurrent Account Creation Generates Unique Numbers

**Priority:** P0
**Risk:** RISK-013

### Steps

Create multiple accounts concurrently.

### Expected Result

Every generated account identifier/number remains unique.

---

## ACC-TC-020 — Customer Cannot Edit Account Number

**Priority:** P0

### Expected Result

Account number is immutable through customer-facing update APIs.

---

# 11. Current Balance Test Cases

## ACC-TC-021 — Current Balance Display

**Priority:** P0
**Requirement:** REQ-ACC-003
**Risk:** RISK-001

### Expected Result

Displayed current balance matches authoritative backend value.

---

## ACC-TC-022 — Zero Current Balance

**Priority:** P1

### Expected Result

```text
0.00
```

is displayed correctly without negative-zero or blank representation.

---

## ACC-TC-023 — Large Current Balance

**Priority:** P1

### Expected Result

Large monetary value is displayed without truncation or precision loss.

---

## ACC-TC-024 — Decimal Balance Formatting

**Priority:** P1

### Expected Result

Configured currency precision is applied consistently.

---

## ACC-TC-025 — Negative Balance When Product Does Not Support Overdraft

**Priority:** P0

### Expected Result

Account should not reach unauthorized negative state through normal transaction flows.

If DB contains such state unexpectedly, defect should be investigated.

---

# 12. Available Balance Test Cases

## ACC-TC-026 — Available Balance Equals Current Balance With No Holds

**Priority:** P0
**Requirement:** REQ-ACC-004

### Expected Result

```text
Available Balance = Current Balance
```

when no holds/reservations apply.

---

## ACC-TC-027 — Available Balance With Hold

**Priority:** P0

### Test Data

```text
Current Balance:
10,000.00

Hold:
2,000.00
```

### Expected Result

```text
Available Balance:
8,000.00
```

according to business rules.

---

## ACC-TC-028 — Multiple Holds

**Priority:** P0

### Expected Result

All valid active holds are reflected correctly.

---

## ACC-TC-029 — Released Hold Restores Available Balance

**Priority:** P0

### Expected Result

When a hold is legitimately released, available balance increases by correct amount.

---

## ACC-TC-030 — Failed Transaction Does Not Leave Invalid Hold

**Priority:** P0
**Risk:** RISK-001

### Expected Result

Failed transaction does not permanently reduce available balance.

---

# 13. Balance Integrity Test Cases

## ACC-TC-031 — Successful Debit Updates Balance Correctly

**Priority:** P0

### Test Data

```text
Opening:
10,000.00

Debit:
1,000.00

Fee:
10.00
```

### Expected Result

```text
Closing:
8,990.00
```

---

## ACC-TC-032 — Successful Credit Updates Balance Correctly

**Priority:** P0

### Test Data

```text
Opening:
10,000.00

Credit:
2,500.00
```

### Expected Result

```text
Closing:
12,500.00
```

---

## ACC-TC-033 — Failed Debit Leaves Settled Balance Unchanged

**Priority:** P0

### Expected Result

No settled debit is applied.

---

## ACC-TC-034 — Reversal Restores Correct Financial Value

**Priority:** P0

### Expected Result

Reversal produces correct compensating financial effect according to rules.

---

## ACC-TC-035 — Multiple Sequential Transactions Reconcile

**Priority:** P0

### Example

```text
Opening:
20,000.00

Credit:
+5,000.00

Transfer:
-3,000.00

Fee:
-20.00

Payment:
-2,000.00
```

Expected:

```text
Closing:
19,980.00
```

---

# 14. Account State Test Cases

## ACC-TC-036 — Active Account

**Priority:** P0

### Expected Result

Permitted transactions operate normally.

---

## ACC-TC-037 — Frozen Account

**Priority:** P0
**Requirement:** REQ-ACC-005
**Risk:** RISK-009

### Expected Result

Prohibited financial actions are rejected.

Read-only functions may remain accessible according to rules.

---

## ACC-TC-038 — Restricted Account

**Priority:** P0

### Expected Result

Only permitted subset of actions remains available.

---

## ACC-TC-039 — Suspended Account

**Priority:** P0

### Expected Result

Suspension restrictions are enforced consistently.

---

## ACC-TC-040 — Closed Account

**Priority:** P0
**Requirement:** REQ-ACC-006

### Expected Result

Closed account cannot participate in prohibited transactions.

---

# 15. Freeze / Unfreeze Test Cases

## ACC-TC-041 — Authorized Admin Freezes Account

**Priority:** P0

### Preconditions

Account:

```text
ACTIVE
```

Authorized admin role available.

### Expected Result

```text
ACTIVE
→ FROZEN
```

Audit record created.

---

## ACC-TC-042 — Customer Attempts Transaction After Freeze

**Priority:** P0

### Expected Result

Transaction rejected.

No financial effect.

---

## ACC-TC-043 — Freeze While Customer Has Transfer Confirmation Open

**Priority:** P0
**Risk:** RISK-009, RISK-013

### Steps

1. Customer opens transfer confirmation.
2. Admin freezes source account.
3. Customer submits transfer.

### Expected Result

Backend revalidates current account state.

Transfer rejected.

---

## ACC-TC-044 — Authorized Admin Unfreezes Account

**Priority:** P0

### Expected Result

```text
FROZEN
→ ACTIVE
```

only when allowed.

---

## ACC-TC-045 — Unauthorized Role Attempts Freeze

**Priority:** P0
**Risk:** RISK-023

### Expected Result

Denied.

No state change.

---

## ACC-TC-046 — Customer Calls Freeze Admin API

**Priority:** P0

### Expected Result

Denied unless customer-driven self-freeze is explicitly supported by business design.

---

# 16. Account State Transition Test Cases

## ACC-TC-047 — ACTIVE → FROZEN

**Priority:** P0
**Type:** State Transition

Expected: allowed for authorized actor.

---

## ACC-TC-048 — FROZEN → ACTIVE

**Priority:** P0

Expected: allowed only for authorized recovery path.

---

## ACC-TC-049 — ACTIVE → RESTRICTED

**Priority:** P0

Expected: permitted according to admin/business rules.

---

## ACC-TC-050 — ACTIVE → CLOSED

**Priority:** P0

Expected: only when closure requirements satisfied.

---

## ACC-TC-051 — CLOSED → ACTIVE

**Priority:** P0

Expected: rejected if CLOSED is terminal.

---

## ACC-TC-052 — Invalid State Transition Through API

**Priority:** P0

### Expected Result

Backend rejects unsupported transition regardless of client payload.

---

# 17. Account Closure Test Cases

## ACC-TC-053 — Close Eligible Zero-Balance Account

**Priority:** P1
**Requirement:** REQ-ACC-008

### Preconditions

```text
Balance:
0.00

No active holds.

No pending transactions.

No blocking dependencies.
```

### Expected Result

Account closes successfully.

---

## ACC-TC-054 — Close Account With Positive Balance

**Priority:** P0

### Expected Result

Closure rejected if balance must be zero.

---

## ACC-TC-055 — Close Account With Pending Transfer

**Priority:** P0

### Expected Result

Closure rejected or safely deferred according to business rules.

---

## ACC-TC-056 — Close Account With Active Hold

**Priority:** P0

### Expected Result

Closure denied until hold is resolved where required.

---

## ACC-TC-057 — Close Account Linked to Active Card

**Priority:** P1

### Expected Result

Dependency policy is enforced.

---

## ACC-TC-058 — Closed Account Receives New Transfer

**Priority:** P0
**Risk:** RISK-018

### Expected Result

Incoming transfer is rejected/handled according to closed-account rules.

---

## ACC-TC-059 — Closed Account Sends Transfer

**Priority:** P0

### Expected Result

Transaction rejected.

---

# 18. Own-Account Transfer Test Cases

## ACC-TC-060 — Transfer Between Customer's Own Accounts

**Priority:** P0

### Preconditions

```text
Source:
ACC-001

Destination:
ACC-003
```

### Expected Result

* Source debited once.
* Destination credited once.
* Applicable fee applied.
* Both balances reconcile.

---

## ACC-TC-061 — Own Transfer Same Source and Destination

**Priority:** P1

### Expected Result

Rejected if self-to-same-account transfer has no valid business meaning.

---

## ACC-TC-062 — Own Transfer With Frozen Source

**Priority:** P0

Expected: rejected.

---

## ACC-TC-063 — Own Transfer With Closed Destination

**Priority:** P0

Expected: rejected according to destination-state rule.

---

# 19. Balance vs Transaction History Test Cases

## ACC-TC-064 — Completed Debit Appears in History

**Priority:** P0
**Requirement:** REQ-ACC-009

### Expected Result

Balance change has matching transaction-history record.

---

## ACC-TC-065 — Completed Credit Appears in History

**Priority:** P0

### Expected Result

Credit appears once with correct reference.

---

## ACC-TC-066 — Failed Transaction Does Not Appear as Completed

**Priority:** P0

### Expected Result

Failed operation is not represented as successful settled debit/credit.

---

## ACC-TC-067 — Reversal Appears Separately

**Priority:** P0

### Expected Result

Original transaction remains traceable with linked reversal.

---

# 20. Statement Reconciliation Test Cases

## ACC-TC-068 — Account Balance Matches Statement Closing Balance

**Priority:** P0

### Expected Result

For same authoritative cutoff:

```text
Account Closing Balance
=
Statement Closing Balance
```

---

## ACC-TC-069 — Statement Opening + Activity = Closing

**Priority:** P0

### Expected Result

Statement reconciles mathematically.

---

## ACC-TC-070 — Fees Included in Reconciliation

**Priority:** P0

### Expected Result

Applicable fees are included exactly once.

---

# 21. Concurrent Transaction Test Cases

## ACC-TC-071 — Two Concurrent Debits Within Balance

**Priority:** P0
**Requirement:** REQ-ACC-010
**Risk:** RISK-013

### Test Data

```text
Opening:
10,000.00

Debit A:
2,000.00

Debit B:
3,000.00
```

### Expected Result

Both may succeed.

Expected closing balance reflects both exactly once.

---

## ACC-TC-072 — Concurrent Debits Exceed Balance

**Priority:** P0

### Test Data

```text
Available:
1,000.00

Request A:
800.00

Request B:
500.00
```

### Expected Result

Both must not complete if overdraft is not allowed.

No negative balance.

---

## ACC-TC-073 — Transfer and Payment Compete for Same Funds

**Priority:** P0

### Expected Result

Available-balance enforcement remains atomic across transaction types.

---

## ACC-TC-074 — Debit and Account Freeze Concurrently

**Priority:** P0

### Expected Result

Final outcome follows transaction/state locking rules without invalid intermediate state.

---

## ACC-TC-075 — Account Closure Concurrent With Incoming Transaction

**Priority:** P0

### Expected Result

System resolves race consistently without losing money or leaving contradictory state.

---

# 22. Duplicate Processing Test Cases

## ACC-TC-076 — Duplicate Debit Request

**Priority:** P0
**Risk:** RISK-003

### Expected Result

One intended operation produces one debit.

---

## ACC-TC-077 — Retry After Timeout

**Priority:** P0
**Risk:** RISK-042

### Expected Result

Retry does not duplicate financial effect.

---

## ACC-TC-078 — Browser Refresh After Financial Submission

**Priority:** P0

### Expected Result

Refresh does not replay completed financial action.

---

## ACC-TC-079 — Browser Back Then Resubmit

**Priority:** P0

### Expected Result

Previously completed action is not unintentionally duplicated.

---

# 23. Limits Test Cases

## ACC-TC-080 — Transaction Within Account Limit

**Priority:** P1

Expected: succeeds if all other rules pass.

---

## ACC-TC-081 — Transaction At Exact Limit

**Priority:** P0
**Type:** Boundary

Expected: follows inclusive/exclusive limit rule exactly.

---

## ACC-TC-082 — Transaction Above Limit

**Priority:** P0
**Risk:** RISK-008

Expected: rejected.

---

## ACC-TC-083 — Client Modifies Limit Value

**Priority:** P0
**Risk:** RISK-037

### Expected Result

Backend ignores manipulated client-side limit and enforces authoritative rule.

---

# 24. Precision and Rounding Test Cases

## ACC-TC-084 — Balance With Smallest Supported Currency Unit

**Priority:** P1

### Expected Result

Smallest supported amount is stored and displayed correctly.

---

## ACC-TC-085 — Unsupported Excess Decimal Precision

**Priority:** P1

### Expected Result

Handled according to documented rounding/rejection rule.

---

## ACC-TC-086 — Repeated Decimal Operations

**Priority:** P0

### Expected Result

No cumulative floating-point drift beyond permitted currency precision.

---

## ACC-TC-087 — Negative Zero Display

**Priority:** P2

### Expected Result

System should not display:

```text
-0.00
```

for effectively zero balance unless explicitly required.

---

# 25. API Account Test Cases

## ACC-TC-088 — Get Own Account API

**Priority:** P0
**Automation:** REST Assured

### Expected Result

Authorized customer receives account.

---

## ACC-TC-089 — Get Another Customer's Account API

**Priority:** P0

Expected: denied.

---

## ACC-TC-090 — Get Account Without Authentication

**Priority:** P0

Expected: authentication required.

---

## ACC-TC-091 — Invalid Account ID

**Priority:** P2

Expected: safe error response.

---

## ACC-TC-092 — API Balance Matches UI

**Priority:** P0

Expected: values consistent.

---

## ACC-TC-093 — API Account Status Matches UI

**Priority:** P0

Expected: authoritative status consistent.

---

## ACC-TC-094 — API Prevents Unauthorized Status Update

**Priority:** P0

Expected: customer cannot mutate protected account state.

---

# 26. Database Validation Test Cases

## ACC-TC-095 — Account Owner Relationship

**Priority:** P0
**Automation:** SQL

### Expected Result

Account row references expected customer.

---

## ACC-TC-096 — Unique Account Number

**Priority:** P0

### Expected Result

No duplicate account number exists.

---

## ACC-TC-097 — Current Balance Persistence

**Priority:** P0

### Expected Result

Persistent balance matches authoritative expected result.

---

## ACC-TC-098 — Available Balance Persistence

**Priority:** P0

### Expected Result

Available balance reflects active holds/transactions correctly.

---

## ACC-TC-099 — Account Status Persistence

**Priority:** P0

Expected: state matches latest valid transition.

---

## ACC-TC-100 — Closed Account State Remains Terminal

**Priority:** P0

Expected: no unintended active state is restored.

---

# 27. Database Integrity Test Cases

## ACC-TC-101 — No Orphan Account

**Priority:** P0

### Expected Result

Every account references a valid customer.

---

## ACC-TC-102 — No Duplicate Primary Identifier

**Priority:** P0

Expected: account IDs are unique.

---

## ACC-TC-103 — Account Currency Is Valid

**Priority:** P1

Expected: account references supported currency.

---

## ACC-TC-104 — Balance Fields Use Expected Precision

**Priority:** P0

Expected: financial DB types preserve configured decimal precision.

---

# 28. UI/API/Database Consistency Test

## ACC-TC-105 — Cross-Layer Account Validation

**Priority:** P0

### Validate

```text
UI account status
=
API account status
=
DB account status
```

and:

```text
UI current balance
=
API current balance
=
authoritative DB balance
```

### Expected Result

All required layers agree.

---

# 29. Error Handling Test Cases

## ACC-TC-106 — Account Service Unavailable

**Priority:** P1

### Expected Result

User receives safe failure state.

No stale data is represented as newly confirmed financial state.

---

## ACC-TC-107 — Account API Returns Server Error

**Priority:** P1

### Expected Result

UI handles error gracefully without exposing stack traces.

---

## ACC-TC-108 — Partial Dashboard Load

**Priority:** P1

### Expected Result

Account failure does not cause misleading zero balances.

---

# 30. Cache / Refresh Test Cases

## ACC-TC-109 — Balance Refresh After Transaction

**Priority:** P0

### Expected Result

Fresh account state is displayed after completed transaction.

---

## ACC-TC-110 — Multi-Tab Balance Refresh

**Priority:** P1

### Steps

1. Open account in Tab A and Tab B.
2. Complete transaction in Tab A.
3. Refresh/use Tab B.

### Expected Result

Tab B eventually reflects authoritative updated balance.

---

## ACC-TC-111 — Cached Account After Freeze

**Priority:** P0

### Expected Result

Cached `ACTIVE` UI cannot bypass backend `FROZEN` state.

---

# 31. Audit Test Cases

## ACC-TC-112 — Freeze Audit Record

**Priority:** P0
**Risk:** RISK-022

### Expected Result

Audit records:

```text
Actor

Account

Previous State

New State

Timestamp

Reason
```

---

## ACC-TC-113 — Unfreeze Audit Record

**Priority:** P1

Expected: transition is traceable.

---

## ACC-TC-114 — Account Closure Audit

**Priority:** P0

Expected: closure action and actor are recorded.

---

## ACC-TC-115 — Audit Contains No Sensitive Secrets

**Priority:** P0
**Risk:** RISK-032

Expected: no passwords/tokens/OTP data.

---

# 32. Notification Test Cases

## ACC-TC-116 — Account Freeze Notification

**Priority:** P1

Expected: customer receives accurate notification where required.

---

## ACC-TC-117 — Account Unfreeze Notification

**Priority:** P1

Expected: accurate state notification.

---

## ACC-TC-118 — Account Closure Notification

**Priority:** P1

Expected: customer receives correct closure communication.

---

## ACC-TC-119 — Failed Freeze Does Not Send Success Notification

**Priority:** P1

Expected: notification reflects actual authoritative state.

---

# 33. Search / Filtering Test Cases

## ACC-TC-120 — Admin Search by Account Number

**Priority:** P2

Expected: correct permitted account returned.

---

## ACC-TC-121 — Filter by Account Status

**Priority:** P2

Expected: correct filtered results.

---

## ACC-TC-122 — Filter by Account Type

**Priority:** P2

Expected: correct account types returned.

---

## ACC-TC-123 — Combine Type and Status Filters

**Priority:** P2

Expected: correct intersection.

---

# 34. Security Input Test Cases

## ACC-TC-124 — Invalid Account Identifier Injection-Like Input

**Priority:** P1

### Example

```text
' OR '1'='1
```

### Expected Result

No query manipulation.

Safe error.

---

## ACC-TC-125 — Script-Like Account Alias

**Priority:** P1

### Expected Result

No executable content.

---

## ACC-TC-126 — Unexpected API Fields in Account Update

**Priority:** P0

### Example

```json
{
  "status": "ACTIVE",
  "balance": 999999999
}
```

### Expected Result

Customer cannot mutate authoritative account state/balance.

---

# 35. Cross-Browser Test Cases

## ACC-TC-127 — Accounts in Chrome

**Priority:** P2

Expected: correct functionality.

---

## ACC-TC-128 — Accounts in Edge

**Priority:** P2

Expected: correct functionality.

---

## ACC-TC-129 — Accounts in Firefox

**Priority:** P2

Expected: correct functionality.

---

## ACC-TC-130 — Accounts in WebKit

**Priority:** P2

Expected: correct functionality.

---

# 36. Responsive Test Cases

## ACC-TC-131 — Account Dashboard at 390×844

**Priority:** P1

### Expected Result

Balance, currency, status, and actions remain readable.

---

## ACC-TC-132 — Account Details at 360×800

**Priority:** P1

Expected: critical financial values are not clipped.

---

## ACC-TC-133 — Large Balance on Mobile

**Priority:** P1

Expected: full amount remains understandable and does not overlap controls.

---

# 37. Accessibility Test Cases

## ACC-TC-134 — Keyboard Account Navigation

**Priority:** P2

Expected: account cards/actions reachable using keyboard.

---

## ACC-TC-135 — Account Status Accessible Label

**Priority:** P2

Expected: state is communicated beyond color alone.

---

## ACC-TC-136 — Balance Screen Reader Label

**Priority:** P2

Expected: balance/currency relationship is understandable.

---

# 38. End-to-End Balance Test

## ACC-TC-137 — Complete Account Reconciliation Journey

**Priority:** P0

### Test Data

```text
Opening Balance:
20,000.00
```

### Actions

```text
Incoming Credit:
+5,000.00

Transfer:
-3,000.00

Transfer Fee:
-20.00

Payment:
-2,000.00

Refund:
+500.00
```

Expected:

```text
20,000
+5,000
-3,000
-20
-2,000
+500
=
20,480.00
```

### Steps

1. Record opening balance.
2. Execute each operation.
3. Review account balance.
4. Review transaction history.
5. Generate statement.
6. Query API.
7. Validate DB.

### Expected Result

```text
UI Balance:
20,480.00

API Balance:
20,480.00

DB Balance:
20,480.00

Statement Closing:
20,480.00
```

All transactions represented exactly once.

---

# 39. End-to-End Freeze Test

## ACC-TC-138 — Freeze Enforcement Across Channels

**Priority:** P0

### Steps

1. Confirm account is `ACTIVE`.
2. Perform valid transaction.
3. Admin freezes account.
4. Attempt transfer through UI.
5. Attempt payment through API.
6. Attempt stale transfer submission.
7. Review account status.
8. Review audit.

### Expected Result

All prohibited financial operations after freeze are rejected regardless of channel.

---

# 40. End-to-End Closure Test

## ACC-TC-139 — Account Closure Lifecycle

**Priority:** P0

### Preconditions

Account satisfies closure rules.

### Steps

1. Verify dependencies.
2. Close account.
3. Refresh UI.
4. Query API.
5. Query DB.
6. Attempt debit.
7. Attempt incoming transfer where prohibited.
8. Review audit.

### Expected Result

Account remains `CLOSED` consistently and cannot violate terminal-state rules.

---

# 41. Concurrency End-to-End Test

## ACC-TC-140 — Concurrent Balance Protection

**Priority:** P0

### Test Data

```text
Opening Available Balance:
1,000.00
```

Concurrent operations:

```text
Transfer:
800.00

Payment:
500.00
```

### Expected Result

At most one operation succeeds if total funds cannot cover both.

Final balance must never become invalid.

History, API, and DB must reconcile with the accepted operation.

---

# 42. Account Risk Mapping

| Risk                                      | Related Test Cases                         |
| ----------------------------------------- | ------------------------------------------ |
| RISK-001 Incorrect balance                | ACC-TC-021–035, 064–070, 097–105, 137, 140 |
| RISK-002 Unauthorized customer data       | ACC-TC-005–008, 089                        |
| RISK-003 Duplicate transaction            | ACC-TC-076–079                             |
| RISK-008 Limit bypass                     | ACC-TC-080–083                             |
| RISK-009 Frozen account transaction       | ACC-TC-037, 041–046, 111, 138              |
| RISK-013 Concurrency corruption           | ACC-TC-019, 043, 071–075, 140              |
| RISK-018 Closed destination accepts funds | ACC-TC-058, 063                            |
| RISK-019 History/balance mismatch         | ACC-TC-064–070                             |
| RISK-021 Sensitive exposure               | ACC-TC-010, 005–007                        |
| RISK-022 Audit gap                        | ACC-TC-112–115                             |
| RISK-023 Unauthorized admin               | ACC-TC-045–046                             |
| RISK-030 Unauthorized API                 | ACC-TC-088–094                             |
| RISK-037 Frontend-only validation         | ACC-TC-043, 083, 094, 126                  |
| RISK-039 API/DB inconsistency             | ACC-TC-092–105                             |
| RISK-042 Retry duplication                | ACC-TC-077                                 |
| RISK-047 IDOR                             | ACC-TC-005–008, 089                        |
| RISK-048 UI/backend mismatch              | ACC-TC-105, 109–111                        |

---

# 43. Requirements Mapping

| Requirement                                | Test Cases               |
| ------------------------------------------ | ------------------------ |
| REQ-ACC-001 View own accounts              | ACC-TC-001–004           |
| REQ-ACC-002 Account ownership              | ACC-TC-005–008           |
| REQ-ACC-003 Current balance accurate       | ACC-TC-021–025, 031–035  |
| REQ-ACC-004 Available balance accurate     | ACC-TC-026–030           |
| REQ-ACC-005 Frozen account enforcement     | ACC-TC-037, 041–046, 138 |
| REQ-ACC-006 Closed account restrictions    | ACC-TC-040, 050–059, 139 |
| REQ-ACC-007 Valid state lifecycle          | ACC-TC-036–052           |
| REQ-ACC-008 Closure dependencies           | ACC-TC-053–059           |
| REQ-ACC-009 Balance/history reconciliation | ACC-TC-064–070, 137      |
| REQ-ACC-010 Concurrency safety             | ACC-TC-071–075, 140      |

---

# 44. Smoke Candidates

Recommended account smoke cases:

```text
ACC-TC-001
ACC-TC-005
ACC-TC-021
ACC-TC-026
ACC-TC-031
ACC-TC-037
ACC-TC-042
ACC-TC-064
ACC-TC-068
```

---

# 45. Sanity Candidates

After account/balance/state changes:

```text
ACC-TC-001
ACC-TC-021
ACC-TC-026
ACC-TC-027
ACC-TC-031
ACC-TC-033
ACC-TC-037
ACC-TC-041
ACC-TC-042
ACC-TC-044
ACC-TC-064
ACC-TC-068
ACC-TC-092
ACC-TC-097
```

---

# 46. Critical Regression Candidates

```text
ACC-TC-001
ACC-TC-005
ACC-TC-007
ACC-TC-018
ACC-TC-019
ACC-TC-021
ACC-TC-025
ACC-TC-026–035
ACC-TC-037–059
ACC-TC-060
ACC-TC-062
ACC-TC-063
ACC-TC-064–083
ACC-TC-086
ACC-TC-088–105
ACC-TC-109–115
ACC-TC-126
ACC-TC-137
ACC-TC-138
ACC-TC-139
ACC-TC-140
```

---

# 47. UI Automation Candidates

Strong candidates for:

```text
Playwright

Selenium

Cypress
```

Include:

```text
ACC-TC-001–013
ACC-TC-021–030
ACC-TC-036–046
ACC-TC-053–070
ACC-TC-109–123
ACC-TC-127–139
```

---

# 48. API Automation Candidates

Strong candidates for:

```text
REST Assured

Postman
```

Include:

```text
ACC-TC-005–008
ACC-TC-014–020
ACC-TC-021–035
ACC-TC-037–083
ACC-TC-088–108
ACC-TC-126
ACC-TC-138–140
```

---

# 49. SQL / Database Testing Candidates

Strong database cases:

```text
ACC-TC-018
ACC-TC-019
ACC-TC-021–035
ACC-TC-041–059
ACC-TC-064–079
ACC-TC-095–105
ACC-TC-112–115
ACC-TC-137–140
```

SQL validation should verify:

* Ownership
* Account-number uniqueness
* Status
* Current balance
* Available balance
* Holds
* Financial records
* Transaction references
* Audit records
* Referential integrity

---

# 50. Performance / Concurrency Candidates

Later JMeter/API concurrency coverage should include:

```text
ACC-TC-019

ACC-TC-071

ACC-TC-072

ACC-TC-073

ACC-TC-074

ACC-TC-075

ACC-TC-076

ACC-TC-077

ACC-TC-140
```

Performance execution must verify financial integrity after the load, not only response times.

---

# 51. Test Evidence Requirements

For critical account tests, capture as applicable:

```text
Customer ID

Account ID

Opening balance

Available balance

Transaction amount

Fee

Hold amount

Closing balance

Transaction reference

Account state

Request/response

Database record

Audit record

Screenshot

Timestamp
```

---

# 52. Account Defect Examples

Potential defects include:

```text
Customer sees another customer's account.

Current balance incorrect.

Available balance ignores hold.

Failed transaction leaves hold.

Frozen account still transfers money.

Closed account accepts transaction.

Duplicate request debits account twice.

Concurrent transactions create negative balance.

UI/API/DB balances disagree.

Account state not persisted.

Account number duplicated.

Statement closing balance differs from account.
```

---

# 53. Account Release Blockers

Release blockers include:

```text
Incorrect authoritative balance

Unauthorized account access

Negative balance caused by race when unsupported

Frozen account can perform prohibited transaction

Closed account performs financial operation

Duplicate debit

Missing credit/debit

Balance/history mismatch

Critical account-state inconsistency

Account ownership corruption
```

---

# 54. Account Exit Criteria

Account testing is acceptable when:

```text
Customers see only owned accounts.

Current balances are correct.

Available balances are correct.

Holds are handled correctly.

Failed operations remain financially neutral.

Frozen/restricted/closed states are enforced.

Closure dependencies work.

Transaction history reconciles.

Statements reconcile.

Concurrent operations do not corrupt balance.

API/UI/DB states agree.

Critical state changes are audited.

No Critical/P0 account defect remains.
```

---

# 55. Final Account Testing Principle

Account testing is not complete when the UI merely displays:

```text
Balance: 10,000.00
```

QA must establish that the number is trustworthy.

That means verifying:

```text
Opening balance

Credits

Debits

Fees

Holds

Failed operations

Reversals

Concurrent operations

History

Statements

API state

Database state
```

The critical account invariant is:

```text
Authoritative Balance
=
Previous Balance
+ Valid Credits
- Valid Debits
- Applicable Fees
± Valid Adjustments
```

and every financial effect must be traceable.

The core rule is:

```text
An account balance is not just a value displayed on a page.

It is the result of every financial event affecting the account,
and every layer of the Banking System must agree on that result.
```

<!-- NOVABANK-TEST-CASES-SYNC-START -->

## Current Build Test Cases

### CB-TC-001 - Customer Session

Precondition: NovaBank session page is open.

Steps:

1. Click `Enter as customer`.
2. Observe the dashboard.

Expected:

- Dashboard loads.
- Customer role is shown.
- Admin navigation is hidden.

Status: Executable.

### CB-TC-002 - Admin Session

Steps:

1. Open NovaBank.
2. Click `Enter as admin`.

Expected:

- Dashboard loads.
- Admin role is shown.
- Admin navigation becomes visible.

Status: Executable.

### CB-TC-003 - Logout

Precondition: User has an active session.

Steps:

1. Click `Log out`.

Expected:

- Session is cleared.
- Session-selection page is displayed.

Status: Executable.

### CB-TC-004 - Account Display

Expected:

- Checking account is displayed.
- Savings account is displayed.
- Account numbers are masked.
- Balances use USD.

Status: Executable.

### CB-TC-005 - Valid Transfer

Data:

`$100`

Expected:

- Transfer succeeds.
- Confirmation appears.
- Source balance is reduced.
- Transaction activity is generated.

Status: Executable.

### CB-TC-006 - Transfer Maximum Boundary

Data:

`$10,000`

Expected:

Transfer succeeds.

Status: Executable.

### CB-TC-007 - Transfer Above Maximum

Data:

`$10,000.01`

Expected:

Transfer is rejected with transfer-limit validation.

Status: Executable.

### CB-TC-008 - Invalid Transfer Amount

Data:

`0`

`-1`

Expected:

Transfer is rejected.

Status: Executable.

### CB-TC-009 - Valid Bill Payment

Data:

Electricity, `$50`

Expected:

Payment succeeds.

Status: Executable.

### CB-TC-010 - Minimum Bill Payment

Data:

`$0.01`

Expected:

Payment is accepted when sufficient funds exist.

Status: Executable.

### CB-TC-011 - Invalid Bill Payment

Data:

`0`, negative amount, or amount greater than available balance.

Expected:

Payment is rejected.

Status: Executable.

### CB-TC-012 - Freeze Card

Expected:

- Active card becomes frozen.
- Action changes to Unfreeze.

Status: Executable.

### CB-TC-013 - Unfreeze Card

Expected:

Frozen card becomes active.

Status: Executable.

### CB-TC-014 - Minimum Loan

Data:

`$1,000`

Expected:

Amount is valid.

Status: Executable.

### CB-TC-015 - Maximum Loan

Data:

`$50,000`

Expected:

Amount is valid.

Status: Executable.

### CB-TC-016 - Invalid Loan Boundaries

Data:

`$999`

`$50,001`

Expected:

Values are invalid.

Status: Executable.

### CB-TC-017 - Valid Loan Application

Data:

`$5,000`, 24 months.

Expected:

Loan application is submitted successfully.

Status: Executable.

### CB-TC-018 - Customer Authorization

Expected:

Customer must not see Admin navigation.

Status: Executable.

### CB-TC-019 - Admin Authorization

Expected:

Admin can access Admin console.

Status: Executable.

### CB-TC-020 - Session Storage Removal

Steps:

1. Start customer session.
2. Remove session storage.
3. Refresh page.

Expected:

Application returns to session-selection state.

Status: Executable.

## Blocked Existing Cases

Existing cases related to the following functionality remain valid but currently use the status `Blocked`:

- Credential login
- MFA
- Beneficiary CRUD
- Account creation
- Extended account controls
- Dedicated statements
- Persistent database verification
- SQL data validation

Blocked reason:

`Required functionality is unavailable in the current deployed build.`

These cases must not be deleted simply to produce a passing test suite.

<!-- NOVABANK-TEST-CASES-SYNC-END -->