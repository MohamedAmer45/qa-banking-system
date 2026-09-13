# Banking System — Transfer Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Transfers                      |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Transfer scenario catalog into detailed, execution-ready test cases.

Transfers are one of the highest-risk modules in the Banking System because failures can directly affect customer money.

Coverage includes:

* Own-account transfers
* Same-bank transfers
* Beneficiary transfers
* External transfers
* Transfer amounts
* Fees
* Limits
* Available balance
* Account states
* Beneficiary states
* Destination states
* Scheduled transfers
* Recurring transfers
* Cancellation
* Idempotency
* Duplicate submission
* Timeout and retry
* Atomicity
* Reversals
* Concurrency
* Transaction history
* Statements
* Notifications
* API validation
* Database validation
* Authorization
* Security
* Precision
* Cross-browser behavior
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Transfer test cases use:

```text
TRF-TC-XXX
```

Examples:

```text
TRF-TC-001
TRF-TC-002
TRF-TC-003
```

---

# 4. Critical Transfer Invariants

The Transfer module must preserve the following rules.

## Invariant 1 — Exactly Once

```text
One intended transfer
=
One financial effect
```

---

## Invariant 2 — Atomicity

```text
Source debit
+
Destination credit
```

must either both complete according to transaction design or safely fail/recover.

---

## Invariant 3 — Financial Neutrality on Failure

```text
Failed Transfer
=
No invalid settled debit
+
No invalid destination credit
+
No duplicate fee
```

---

## Invariant 4 — Available Balance

The system must validate:

```text
Available Balance
>=
Transfer Amount + Applicable Fee
```

before authorization.

---

## Invariant 5 — Ownership

A customer may transfer funds only from an account they are authorized to use.

---

## Invariant 6 — Beneficiary Validity

An external/saved beneficiary must be:

```text
Owned by customer
+
Verified
+
Active
+
Eligible
```

at transfer authorization time.

---

## Invariant 7 — Traceability

Each completed transfer must have:

```text
Unique reference

Source transaction

Destination transaction where applicable

Timestamp

Status

Financial amount

Applicable fee

Traceable history
```

---

# 5. Common Test Data

## Customer A

```text
Customer:
CUST-001

Status:
ACTIVE

KYC:
VERIFIED

MFA:
ENABLED
```

## Customer B

```text
Customer:
CUST-002

Status:
ACTIVE
```

## Source Account

```text
Account:
ACC-001

Owner:
CUST-001

Status:
ACTIVE

Current Balance:
100,000.00

Available Balance:
100,000.00

Currency:
EGP
```

## Customer A Secondary Account

```text
Account:
ACC-003

Owner:
CUST-001

Status:
ACTIVE

Balance:
20,000.00

Currency:
EGP
```

## Customer B Destination

```text
Account:
ACC-002

Owner:
CUST-002

Status:
ACTIVE

Currency:
EGP
```

## Active Beneficiary

```text
Beneficiary:
BEN-001

Owner:
CUST-001

Destination:
ACC-002

Status:
ACTIVE
```

## Pending Beneficiary

```text
Beneficiary:
BEN-002

Owner:
CUST-001

Status:
PENDING_ACTIVATION
```

## Disabled Beneficiary

```text
Beneficiary:
BEN-003

Owner:
CUST-001

Status:
DISABLED
```

## Frozen Source Account

```text
Account:
ACC-004

Owner:
CUST-001

Status:
FROZEN
```

## Closed Destination

```text
Account:
ACC-006

Status:
CLOSED
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer is authenticated.

Transfer service is available.

Source account exists.

Required beneficiaries exist.

Synthetic test data is used.

Transaction history is accessible.

Database/API validation is available when required.
```

---

# 7. Own-Account Transfer Test Cases

## TRF-TC-001 — Valid Transfer Between Own Accounts

**Priority:** P0
**Requirement:** REQ-TRF-001
**Risk:** RISK-001
**Automation:** Playwright / REST Assured

### Steps

1. Login as `CUST-001`.
2. Open Transfers.
3. Select `ACC-001` as source.
4. Select `ACC-003` as destination.
5. Enter valid amount.
6. Review confirmation.
7. Confirm.

### Expected Result

* One transfer completes.
* Source is debited once.
* Destination is credited once.
* Fee applied correctly if applicable.
* Both balances reconcile.
* One transfer reference is generated.

---

## TRF-TC-002 — Transfer Entire Available Balance When No Fee Applies

**Priority:** P0
**Type:** Boundary

### Expected Result

Transfer succeeds if:

```text
Amount = Available Balance
```

and no additional fee is required.

Final available balance is correct.

---

## TRF-TC-003 — Transfer Entire Balance When Fee Applies

**Priority:** P0

### Example

```text
Available Balance:
10,000.00

Transfer:
10,000.00

Fee:
10.00
```

### Expected Result

Rejected because total required is:

```text
10,010.00
```

---

## TRF-TC-004 — Same Source and Destination Account

**Priority:** P1

### Expected Result

Rejected if same-account transfer has no valid business purpose.

No financial effect.

---

# 8. Same-Bank Customer Transfer Test Cases

## TRF-TC-005 — Valid Same-Bank Transfer

**Priority:** P0
**Requirement:** REQ-TRF-002

### Expected Result

* Source debit correct.
* Destination credit correct.
* Fee correct.
* Status correct.
* History updated once.

---

## TRF-TC-006 — Transfer to Same-Bank Beneficiary

**Priority:** P0

### Preconditions

`BEN-001` active.

### Expected Result

Transfer succeeds if all validation passes.

---

## TRF-TC-007 — Transfer to Invalid Same-Bank Account

**Priority:** P0

### Expected Result

Transfer rejected.

No source debit.

---

# 9. External Transfer Test Cases

## TRF-TC-008 — Valid External Transfer

**Priority:** P0
**Requirement:** REQ-TRF-003

### Expected Result

Transfer enters correct processing/completed state according to external-transfer design.

Fees are displayed and applied correctly.

---

## TRF-TC-009 — Invalid External Bank Details

**Priority:** P0

### Expected Result

Transfer rejected before financial commitment where validation is possible.

---

## TRF-TC-010 — External Provider Rejects Transfer

**Priority:** P0

### Expected Result

Local transaction state reflects failure accurately.

Customer is not left with an invalid permanent debit.

---

## TRF-TC-011 — External Provider Timeout

**Priority:** P0
**Risk:** RISK-042

### Expected Result

System does not blindly create duplicate transfers on retry.

Transaction enters safe deterministic state.

---

# 10. Transfer Amount Validation

## TRF-TC-012 — Minimum Valid Amount

**Priority:** P1
**Type:** Boundary

### Expected Result

Exact minimum accepted.

---

## TRF-TC-013 — Below Minimum Amount

**Priority:** P1

Expected: rejected.

---

## TRF-TC-014 — Zero Amount

**Priority:** P0

Expected: rejected.

---

## TRF-TC-015 — Negative Amount

**Priority:** P0

Expected: rejected.

---

## TRF-TC-016 — Maximum Valid Transfer Amount

**Priority:** P0

Expected: accepted if balance and other limits permit.

---

## TRF-TC-017 — Maximum Plus Smallest Currency Unit

**Priority:** P0

Expected: rejected.

---

## TRF-TC-018 — Excess Decimal Precision

**Priority:** P1

Example:

```text
100.001
```

when only two decimal places are supported.

### Expected Result

Rejected or rounded according to documented financial rule.

No silent inconsistent calculation.

---

## TRF-TC-019 — Very Large Numeric Input

**Priority:** P1

### Expected Result

Handled safely without numeric overflow or server error.

---

## TRF-TC-020 — Non-Numeric Amount

**Priority:** P2

Expected: validation error.

---

# 11. Balance Validation

## TRF-TC-021 — Sufficient Balance

**Priority:** P0
**Requirement:** REQ-TRF-004

### Expected Result

Transfer proceeds if all rules pass.

---

## TRF-TC-022 — Insufficient Balance

**Priority:** P0
**Risk:** RISK-007

### Expected Result

Transfer rejected.

No debit.

No destination credit.

No completed transaction.

---

## TRF-TC-023 — Balance Equals Amount Plus Fee

**Priority:** P0
**Type:** Boundary

Example:

```text
Available:
1,010.00

Amount:
1,000.00

Fee:
10.00
```

Expected:

```text
Closing:
0.00
```

if zero balance is allowed.

---

## TRF-TC-024 — Balance One Smallest Currency Unit Below Required Total

**Priority:** P0

Example:

```text
Available:
1,009.99

Required:
1,010.00
```

Expected: rejected.

---

## TRF-TC-025 — Current Balance Sufficient but Available Balance Insufficient

**Priority:** P0

### Example

```text
Current:
10,000.00

Available:
5,000.00

Transfer + Fee:
6,000.00
```

### Expected Result

Transfer rejected using available balance.

---

# 12. Fee Test Cases

## TRF-TC-026 — Transfer With No Fee

**Priority:** P1

Expected: no fee deducted.

---

## TRF-TC-027 — Transfer With Fixed Fee

**Priority:** P0
**Risk:** RISK-010

### Expected Result

Fee is correctly calculated and displayed before confirmation.

---

## TRF-TC-028 — Transfer With Percentage Fee

**Priority:** P0

Expected: percentage calculation correct.

---

## TRF-TC-029 — Fee Minimum Boundary

**Priority:** P1

Expected: minimum fee rule applied correctly.

---

## TRF-TC-030 — Fee Maximum Cap

**Priority:** P1

Expected: fee does not exceed configured cap.

---

## TRF-TC-031 — Fee Included in Balance Validation

**Priority:** P0
**Risk:** RISK-007, RISK-010

Expected:

```text
Available >= Amount + Fee
```

must hold.

---

## TRF-TC-032 — Fee Displayed Before Confirmation

**Priority:** P0

Expected: customer sees amount, fee, and total debit before final authorization.

---

## TRF-TC-033 — Fee Applied Exactly Once

**Priority:** P0

Expected: no duplicate fee charge.

---

# 13. Transfer Limit Test Cases

## TRF-TC-034 — Transfer Below Per-Transaction Limit

**Priority:** P1

Expected: allowed if other rules pass.

---

## TRF-TC-035 — Transfer Exactly at Per-Transaction Limit

**Priority:** P0
**Type:** Boundary

Expected: follows configured inclusive boundary.

---

## TRF-TC-036 — Transfer Above Per-Transaction Limit

**Priority:** P0
**Risk:** RISK-008

Expected: rejected.

---

## TRF-TC-037 — Daily Limit Below Threshold

**Priority:** P1

Expected: transfer allowed.

---

## TRF-TC-038 — Transfer Reaches Exact Daily Limit

**Priority:** P0

Expected: succeeds if daily limit is inclusive.

---

## TRF-TC-039 — Transfer Exceeds Remaining Daily Limit

**Priority:** P0

Expected: rejected.

---

## TRF-TC-040 — Monthly Limit Enforcement

**Priority:** P1

Expected: configured monthly aggregate enforced.

---

## TRF-TC-041 — Manipulate Client-Side Transfer Limit

**Priority:** P0
**Risk:** RISK-037

Expected: backend authoritative limit still enforced.

---

# 14. Source Account State Test Cases

## TRF-TC-042 — Active Source Account

**Priority:** P0

Expected: transfer may proceed.

---

## TRF-TC-043 — Frozen Source Account

**Priority:** P0
**Risk:** RISK-009

Expected: rejected.

---

## TRF-TC-044 — Restricted Source Account

**Priority:** P0

Expected: restrictions enforced.

---

## TRF-TC-045 — Closed Source Account

**Priority:** P0

Expected: rejected.

---

## TRF-TC-046 — Source Frozen After Confirmation Page Loaded

**Priority:** P0
**Risk:** RISK-048

### Expected Result

Final submission revalidates current account state.

Transfer rejected.

---

# 15. Destination State Test Cases

## TRF-TC-047 — Active Destination

**Priority:** P0

Expected: transfer can proceed.

---

## TRF-TC-048 — Closed Destination

**Priority:** P0
**Risk:** RISK-018

Expected: rejected.

---

## TRF-TC-049 — Restricted Destination

**Priority:** P0

Expected: current destination rules enforced.

---

## TRF-TC-050 — Destination Closes After Confirmation Loaded

**Priority:** P0

Expected: final submission revalidates destination state.

---

# 16. Beneficiary Validation Test Cases

## TRF-TC-051 — Active Owned Beneficiary

**Priority:** P0

Expected: valid.

---

## TRF-TC-052 — Pending Beneficiary

**Priority:** P0

Expected: rejected.

---

## TRF-TC-053 — Disabled Beneficiary

**Priority:** P0

Expected: rejected.

---

## TRF-TC-054 — Deleted Beneficiary

**Priority:** P0

Expected: rejected.

---

## TRF-TC-055 — Another Customer's Beneficiary

**Priority:** P0
**Risk:** RISK-047

Expected: rejected.

---

## TRF-TC-056 — Beneficiary Disabled After Confirmation Loaded

**Priority:** P0

Expected: backend revalidates before financial authorization.

---

# 17. Authorization Test Cases

## TRF-TC-057 — Transfer From Own Account

**Priority:** P0
**Requirement:** REQ-TRF-005

Expected: allowed if other validations pass.

---

## TRF-TC-058 — Transfer From Another Customer's Account via URL/API Manipulation

**Priority:** P0
**Risk:** RISK-002, RISK-047

Expected:

```text
DENIED
```

No financial effect.

---

## TRF-TC-059 — Customer Changes Source Account ID in Request

**Priority:** P0

Expected: backend ownership validation rejects request.

---

## TRF-TC-060 — Unauthenticated Transfer API

**Priority:** P0

Expected: rejected.

---

## TRF-TC-061 — Pre-MFA Session Attempts Transfer

**Priority:** P0
**Risk:** RISK-005

Expected: rejected.

---

## TRF-TC-062 — Expired Session Attempts Transfer

**Priority:** P0
**Risk:** RISK-015

Expected: rejected with no financial effect.

---

# 18. Transfer Confirmation Test Cases

## TRF-TC-063 — Confirmation Shows Source

**Priority:** P1

Expected: correct source account displayed.

---

## TRF-TC-064 — Confirmation Shows Destination

**Priority:** P0

Expected: authoritative recipient/destination displayed.

---

## TRF-TC-065 — Confirmation Shows Amount

**Priority:** P0

Expected: exact amount shown.

---

## TRF-TC-066 — Confirmation Shows Fee

**Priority:** P0

Expected: fee shown clearly.

---

## TRF-TC-067 — Confirmation Shows Total Debit

**Priority:** P0

Expected:

```text
Amount + Fee
```

displayed accurately.

---

## TRF-TC-068 — Back From Confirmation

**Priority:** P1

Expected: no transfer is created merely by opening confirmation.

---

## TRF-TC-069 — Cancel Before Confirmation

**Priority:** P1

Expected: no financial effect.

---

# 19. Successful Transfer Financial Validation

## TRF-TC-070 — Source Balance Calculation

**Priority:** P0

Example:

```text
Opening:
10,000.00

Amount:
1,000.00

Fee:
10.00
```

Expected:

```text
Closing:
8,990.00
```

---

## TRF-TC-071 — Destination Balance Calculation

**Priority:** P0

Example:

```text
Opening:
5,000.00

Credit:
1,000.00
```

Expected:

```text
Closing:
6,000.00
```

---

## TRF-TC-072 — Combined Financial Reconciliation

**Priority:** P0

Expected:

* Source decreases by amount + fee.
* Destination increases by transfer amount.
* Fee is recorded according to accounting model.
* No unexplained value exists.

---

# 20. Duplicate Submission Test Cases

## TRF-TC-073 — Double-Click Confirm

**Priority:** P0
**Requirement:** REQ-TRF-011
**Risk:** RISK-003

### Steps

Rapidly double-click final confirmation.

### Expected Result

One transfer only.

---

## TRF-TC-074 — Repeated Enter Key Submission

**Priority:** P0

Expected: one financial effect.

---

## TRF-TC-075 — Refresh After Completed Transfer

**Priority:** P0

Expected: completed transfer is not replayed.

---

## TRF-TC-076 — Browser Back and Resubmit

**Priority:** P0

Expected: no unintended duplicate transaction.

---

## TRF-TC-077 — Two Tabs Submit Same Transfer

**Priority:** P0

Expected: duplicate-control behavior follows transaction identity/idempotency rules.

---

# 21. Idempotency Test Cases

## TRF-TC-078 — Same Idempotency Key and Same Payload

**Priority:** P0
**Requirement:** REQ-TRF-012
**Risk:** RISK-003, RISK-042
**Automation:** REST Assured

### Expected Result

One financial effect.

Subsequent request returns existing result or equivalent safe response.

---

## TRF-TC-079 — Same Key With Different Payload

**Priority:** P0

### Expected Result

Conflicting request rejected.

---

## TRF-TC-080 — Different Keys for Independent Transfers

**Priority:** P0

Expected: independent intended transfers may each process.

---

## TRF-TC-081 — Missing Idempotency Key Where Required

**Priority:** P0

Expected: request rejected or protected through equivalent server strategy according to API contract.

---

## TRF-TC-082 — Idempotency Key Reused by Different Customer

**Priority:** P0

Expected: no cross-customer transaction collision or data exposure.

---

# 22. Timeout / Retry Test Cases

## TRF-TC-083 — Backend Completes but Client Times Out

**Priority:** P0
**Risk:** RISK-042

### Steps

1. Submit transfer.
2. Simulate delayed/lost response.
3. Backend completes transaction.
4. Client retries.

### Expected Result

Retry resolves to original transaction.

One financial effect.

---

## TRF-TC-084 — Backend Never Processes Timed-Out Request

**Priority:** P0

Expected: retry may process exactly once.

---

## TRF-TC-085 — Timeout Status Is Initially Unknown

**Priority:** P0

Expected:

System does not prematurely display definitive failure if authoritative state is unknown.

---

## TRF-TC-086 — Retry Through UI After Unknown Status

**Priority:** P0

Expected: safe retry/deduplication prevents double debit.

---

# 23. Atomicity Test Cases

## TRF-TC-087 — Source Debit Succeeds / Destination Credit Fails

**Priority:** P0
**Requirement:** REQ-TRF-013
**Risk:** RISK-004

### Expected Result

System safely rolls back/compensates or enters controlled recoverable state according to transaction architecture.

Customer must not lose money silently.

---

## TRF-TC-088 — Destination Credit Succeeds / Source Debit Fails

**Priority:** P0

Expected: impossible or reconciled safely.

Money must not be created incorrectly.

---

## TRF-TC-089 — Failure Between Ledger Entries

**Priority:** P0

Expected: no final inconsistent financial state.

---

## TRF-TC-090 — Database Failure During Transfer Commit

**Priority:** P0

Expected: transaction preserves atomicity.

---

# 24. Transfer Status Test Cases

## TRF-TC-091 — PENDING Transfer

**Priority:** P1

Expected: no misleading COMPLETED status.

---

## TRF-TC-092 — PROCESSING Transfer

**Priority:** P1

Expected: customer understands transaction is not final.

---

## TRF-TC-093 — COMPLETED Transfer

**Priority:** P0

Expected: all financial effects committed correctly.

---

## TRF-TC-094 — FAILED Transfer

**Priority:** P0

Expected: financial state matches failure semantics.

---

## TRF-TC-095 — CANCELLED Transfer

**Priority:** P0

Expected: cancelled operation does not execute.

---

## TRF-TC-096 — REVERSED Transfer

**Priority:** P0

Expected: original preserved with linked reversal.

---

# 25. Scheduled Transfer Test Cases

## TRF-TC-097 — Schedule Valid Future Transfer

**Priority:** P0
**Requirement:** REQ-TRF-014

### Expected Result

Transfer is scheduled with correct date/time and details.

---

## TRF-TC-098 — Schedule Transfer for Past Date

**Priority:** P1

Expected: rejected.

---

## TRF-TC-099 — Schedule Transfer for Current Boundary

**Priority:** P1

Expected: handled according to scheduling cutoff rules.

---

## TRF-TC-100 — Scheduled Transfer Executes on Due Date

**Priority:** P0

Expected: executes once.

---

## TRF-TC-101 — Scheduled Transfer With Insufficient Funds at Execution

**Priority:** P0

Expected:

* Transfer fails according to policy.
* No invalid debit.
* Customer notified appropriately.

---

## TRF-TC-102 — Scheduled Transfer Source Frozen Before Execution

**Priority:** P0

Expected: execution revalidates current source state and rejects.

---

## TRF-TC-103 — Scheduled Transfer Beneficiary Disabled Before Execution

**Priority:** P0

Expected: current beneficiary state revalidated.

---

## TRF-TC-104 — Scheduled Transfer Destination Closed Before Execution

**Priority:** P0

Expected: rejected according to destination rules.

---

# 26. Scheduled Transfer Cancellation

## TRF-TC-105 — Cancel Eligible Scheduled Transfer

**Priority:** P0
**Requirement:** REQ-TRF-015

### Expected Result

State becomes `CANCELLED`.

No execution on due date.

---

## TRF-TC-106 — Cancel Already Cancelled Transfer

**Priority:** P2

Expected: idempotent safe response.

---

## TRF-TC-107 — Cancel Already Executed Transfer

**Priority:** P0

Expected: cannot retroactively mark completed transaction cancelled.

Use reversal process where appropriate.

---

## TRF-TC-108 — Cancel at Execution Boundary

**Priority:** P0
**Risk:** RISK-029

Expected: deterministic outcome.

Transaction must not both execute and appear cancelled without reconciliation.

---

# 27. Recurring Transfer Test Cases

## TRF-TC-109 — Create Valid Recurring Transfer

**Priority:** P1
**Requirement:** REQ-TRF-016

Expected: recurrence stored correctly.

---

## TRF-TC-110 — Recurring Transfer Executes Once Per Cycle

**Priority:** P0

Expected: no duplicate occurrence.

---

## TRF-TC-111 — Recurring Transfer With Insufficient Funds

**Priority:** P0

Expected: cycle handled according to policy without invalid financial effect.

---

## TRF-TC-112 — Cancel Recurring Transfer

**Priority:** P0

Expected: future occurrences cease.

---

## TRF-TC-113 — Edit Recurring Transfer

**Priority:** P1

Expected: future schedule updates according to business rules.

---

## TRF-TC-114 — Recurrence Timezone Handling

**Priority:** P1
**Risk:** RISK-043

Expected: execution occurs on intended local/business schedule.

---

# 28. Concurrency Test Cases

## TRF-TC-115 — Two Concurrent Transfers Within Balance

**Priority:** P0
**Risk:** RISK-013

### Example

```text
Available:
10,000.00

Transfer A:
2,000.00

Transfer B:
3,000.00
```

### Expected Result

Both may succeed.

Closing balance reflects both once.

---

## TRF-TC-116 — Concurrent Transfers Exceed Balance

**Priority:** P0

Example:

```text
Available:
1,000.00

Transfer A:
800.00

Transfer B:
500.00
```

Expected:

Both cannot succeed.

---

## TRF-TC-117 — Five Concurrent Identical Requests

**Priority:** P0

Expected: duplicate/idempotency strategy prevents unintended five debits.

---

## TRF-TC-118 — Transfer and Payment Compete for Funds

**Priority:** P0

Expected: available-balance control is atomic across transaction types.

---

## TRF-TC-119 — Transfer and Account Freeze Concurrently

**Priority:** P0

Expected: deterministic valid final state.

---

## TRF-TC-120 — Transfer and Account Closure Concurrently

**Priority:** P0

Expected: no invalid completed transaction against closed source/destination state.

---

# 29. Daily Limit Concurrency Test Cases

## TRF-TC-121 — Two Transfers Independently Under Limit but Combined Over Limit

**Priority:** P0
**Risk:** RISK-024

Example:

```text
Remaining Daily Limit:
1,000.00

Request A:
700.00

Request B:
600.00
```

### Expected Result

Both must not complete if combined total violates daily limit.

---

## TRF-TC-122 — Concurrent Transfers Exactly Reach Daily Limit

**Priority:** P0

Expected: valid combined total may succeed according to policy.

---

# 30. Transfer Reversal Test Cases

## TRF-TC-123 — Authorized Reversal of Eligible Transfer

**Priority:** P0
**Requirement:** REQ-TRF-017

### Expected Result

* Original transaction preserved.
* Reversal created separately.
* Balance compensated correctly.
* Audit record created.

---

## TRF-TC-124 — Unauthorized Customer Attempts Reversal API

**Priority:** P0

Expected: denied.

---

## TRF-TC-125 — Read-Only Admin Attempts Reversal

**Priority:** P0
**Risk:** RISK-006

Expected: denied.

---

## TRF-TC-126 — Reverse Same Transfer Twice

**Priority:** P0

Expected: duplicate reversal prevented unless partial/multiple reversals explicitly supported.

---

## TRF-TC-127 — Reversal Amount Correct

**Priority:** P0

Expected: correct compensating amount applied according to fee/refund rules.

---

## TRF-TC-128 — Reversal History Link

**Priority:** P0

Expected: original and reversal references remain linked.

---

# 31. Transfer History Test Cases

## TRF-TC-129 — Completed Transfer Appears Once

**Priority:** P0
**Requirement:** REQ-TRF-018

Expected: one history entry per intended ledger representation.

---

## TRF-TC-130 — Failed Transfer Status in History

**Priority:** P1

Expected: failure clearly represented without appearing completed.

---

## TRF-TC-131 — Scheduled Transfer Appears Correctly

**Priority:** P1

Expected: scheduled state/date shown correctly.

---

## TRF-TC-132 — Reversed Transfer History

**Priority:** P0

Expected: reversal does not overwrite original event.

---

# 32. Statement Integration Test Cases

## TRF-TC-133 — Completed Transfer Included in Statement

**Priority:** P0

Expected: amount and fee included once.

---

## TRF-TC-134 — Failed Transfer Not Included as Settled Debit

**Priority:** P0

Expected: statement remains financially correct.

---

## TRF-TC-135 — Reversal Included Correctly

**Priority:** P0

Expected: both original and reversal represented according to statement rules.

---

# 33. Notification Test Cases

## TRF-TC-136 — Successful Transfer Notification

**Priority:** P1

Expected: notification matches actual completed transaction.

---

## TRF-TC-137 — Failed Transfer Notification

**Priority:** P1

Expected: failure notification, not success.

---

## TRF-TC-138 — No Duplicate Notification for One Transfer

**Priority:** P2

Expected: one expected event notification according to channel design.

---

## TRF-TC-139 — Duplicate Request Does Not Produce Two Success Notifications

**Priority:** P0

Expected: notification behavior matches one financial effect.

---

# 34. API Transfer Test Cases

## TRF-TC-140 — Valid Transfer API

**Priority:** P0
**Automation:** REST Assured / Postman

Expected: correct successful response and financial result.

---

## TRF-TC-141 — Missing Required Field

**Priority:** P1

Expected: validation error.

---

## TRF-TC-142 — Invalid Source Account

**Priority:** P0

Expected: rejected.

---

## TRF-TC-143 — Invalid Beneficiary

**Priority:** P0

Expected: rejected.

---

## TRF-TC-144 — Unauthorized Source Account

**Priority:** P0

Expected: denied.

---

## TRF-TC-145 — Manipulated Fee in Request

**Priority:** P0
**Risk:** RISK-037

### Example

Client sends:

```json
{
  "amount": 1000,
  "fee": 0
}
```

when server fee should be 10.

### Expected Result

Server calculates authoritative fee.

---

## TRF-TC-146 — Manipulated Total Debit

**Priority:** P0

Expected: server does not trust client-calculated total.

---

## TRF-TC-147 — Manipulated Transfer Status

**Priority:** P0

Expected: customer cannot submit `"status": "COMPLETED"` to bypass workflow.

---

# 35. Database Validation Test Cases

## TRF-TC-148 — Transfer Record Exists Once

**Priority:** P0
**Automation:** SQL

Expected: one authoritative transfer record for one intended operation.

---

## TRF-TC-149 — Unique Transfer Reference

**Priority:** P0
**Risk:** RISK-033

Expected: no duplicate reference.

---

## TRF-TC-150 — Source Ledger Entry Correct

**Priority:** P0

Expected: amount/fee/direction/reference correct.

---

## TRF-TC-151 — Destination Ledger Entry Correct

**Priority:** P0

Expected: credit amount/reference correct.

---

## TRF-TC-152 — Failed Transfer Has No Invalid Completed Ledger Entries

**Priority:** P0

Expected: no settled debit/credit inconsistent with failure.

---

## TRF-TC-153 — Transfer Status Persistence

**Priority:** P0

Expected: DB status matches authoritative transaction lifecycle.

---

## TRF-TC-154 — Scheduled Transfer Persistence

**Priority:** P1

Expected: execution time, recurrence/cancellation state correct.

---

# 36. UI/API/Database Reconciliation

## TRF-TC-155 — Cross-Layer Completed Transfer

**Priority:** P0
**Risk:** RISK-039, RISK-048

### Validate

```text
UI Status
=
API Status
=
DB Status
```

and:

```text
UI Amount
=
API Amount
=
DB Amount
```

and balances reconcile.

---

## TRF-TC-156 — Cross-Layer Failed Transfer

**Priority:** P0

Expected: UI/API/DB all reflect safe failure state.

---

# 37. Precision Test Cases

## TRF-TC-157 — Smallest Supported Amount

**Priority:** P1

Expected: processed without rounding corruption.

---

## TRF-TC-158 — Large Amount Near Maximum

**Priority:** P1

Expected: correct precision retained.

---

## TRF-TC-159 — Percentage Fee Rounding

**Priority:** P0

Expected: fee rounding follows documented method.

---

## TRF-TC-160 — Repeated Small Transfers

**Priority:** P0

Expected: cumulative balance remains precise.

---

# 38. Security Input Test Cases

## TRF-TC-161 — Script-Like Transfer Description

**Priority:** P1

Expected: no script execution.

---

## TRF-TC-162 — SQL-Like Reference Input

**Priority:** P1

Expected: safely handled.

---

## TRF-TC-163 — Unexpected Protected Fields

**Priority:** P0

Payload example:

```json
{
  "amount": 1000,
  "status": "COMPLETED",
  "approved": true,
  "fee": 0
}
```

Expected: protected fields ignored/rejected.

---

# 39. Audit Test Cases

## TRF-TC-164 — Completed Transfer Audit/Trace Event

**Priority:** P1

Expected: transaction traceability contains appropriate identifiers without secrets.

---

## TRF-TC-165 — Admin Reversal Audit

**Priority:** P0
**Risk:** RISK-022

Expected audit contains:

```text
Actor
Action
Transfer
Reason
Timestamp
```

---

## TRF-TC-166 — Audit Does Not Expose Authentication Secrets

**Priority:** P0

Expected: no password, OTP, reusable token.

---

# 40. Error Handling Test Cases

## TRF-TC-167 — Transfer Service Unavailable Before Submission

**Priority:** P1

Expected: safe error.

No transaction.

---

## TRF-TC-168 — Transfer Service Fails After Submission

**Priority:** P0

Expected: authoritative outcome recoverable and financial integrity preserved.

---

## TRF-TC-169 — History Service Unavailable After Successful Transfer

**Priority:** P1

Expected: transfer itself remains correct; UI must not re-submit merely because history loading failed.

---

# 41. Cross-Browser Test Cases

## TRF-TC-170 — Transfer in Chrome

**Priority:** P1

Expected: critical flow works.

---

## TRF-TC-171 — Transfer in Edge

**Priority:** P1

Expected: critical flow works.

---

## TRF-TC-172 — Transfer in Firefox

**Priority:** P1

Expected: critical flow works.

---

## TRF-TC-173 — Transfer in WebKit

**Priority:** P1

Expected: critical flow works.

---

# 42. Responsive Test Cases

## TRF-TC-174 — Transfer at 390×844

**Priority:** P0

Expected:

* Source visible.
* Destination visible.
* Amount visible.
* Fee visible.
* Total debit visible.
* Confirm button reachable.

---

## TRF-TC-175 — Transfer at 360×800

**Priority:** P0

Expected: no critical financial value hidden or clipped.

---

## TRF-TC-176 — Mobile Confirmation With Large Amount

**Priority:** P0

Expected: full amount and fee remain readable before confirmation.

---

# 43. Accessibility Test Cases

## TRF-TC-177 — Keyboard-Only Transfer

**Priority:** P1

Expected: complete transfer flow can be navigated using keyboard where accessibility requirements apply.

---

## TRF-TC-178 — Transfer Form Labels

**Priority:** P2

Expected: source, destination, amount, date fields clearly labeled.

---

## TRF-TC-179 — Financial Error Messages Accessible

**Priority:** P1

Expected: insufficient funds/limit errors are clearly communicated.

---

# 44. End-to-End Successful Transfer

## TRF-TC-180 — Full Same-Bank Transfer Reconciliation

**Priority:** P0

### Test Data

```text
Source Opening:
10,000.00

Destination Opening:
5,000.00

Transfer:
1,000.00

Fee:
10.00
```

### Expected

Source:

```text
8,990.00
```

Destination:

```text
6,000.00
```

### Steps

1. Record opening balances.
2. Submit transfer.
3. Record confirmation reference.
4. Validate source balance.
5. Validate destination balance.
6. Validate transaction history.
7. Validate API.
8. Validate database.
9. Generate statement.
10. Validate notification.

### Expected Result

All layers reconcile exactly once.

---

# 45. End-to-End Failed Transfer

## TRF-TC-181 — Insufficient Funds Remains Financially Neutral

**Priority:** P0

### Example

```text
Available:
1,000.00

Amount:
1,000.00

Fee:
10.00
```

### Expected Result

```text
Transfer:
FAILED / REJECTED

Source Debit:
0

Destination Credit:
0

Fee Charged:
0

Success Notification:
No
```

---

# 46. End-to-End Duplicate Prevention

## TRF-TC-182 — Double Submission Across Layers

**Priority:** P0

### Steps

1. Record source balance.
2. Prepare transfer.
3. Rapidly submit twice.
4. Inspect UI.
5. Inspect API activity.
6. Query transfer records.
7. Query ledger.
8. Inspect transaction history.

### Expected Result

```text
Intended Transfers:
1

Financial Effects:
1

Transfer References:
1 authoritative transaction

Unexpected Debit:
0
```

---

# 47. End-to-End Timeout Retry

## TRF-TC-183 — Lost Response Then Retry

**Priority:** P0

### Expected Result

```text
Original backend completion:
1

Client retries:
Yes

Final financial effects:
1
```

Customer eventually receives authoritative transaction state.

---

# 48. End-to-End Atomicity

## TRF-TC-184 — Inject Failure During Transfer Processing

**Priority:** P0

### Expected Result

No state where customer permanently loses source money without correct destination/compensation.

After recovery, balances and ledger reconcile.

---

# 49. End-to-End Scheduled Transfer

## TRF-TC-185 — Schedule → Execute → Reconcile

**Priority:** P0

### Steps

1. Create scheduled transfer.
2. Confirm scheduled state.
3. Reach due time.
4. Verify one execution.
5. Validate balances.
6. Validate history.
7. Validate statement.

### Expected Result

One correctly timed financial execution.

---

# 50. End-to-End Scheduled Cancellation

## TRF-TC-186 — Schedule → Cancel → Due Date

**Priority:** P0

Expected:

```text
Final Scheduled State:
CANCELLED

Execution Count:
0

Financial Effect:
0
```

---

# 51. End-to-End Reversal

## TRF-TC-187 — Complete Transfer → Authorized Reversal

**Priority:** P0

### Expected Result

Original transaction remains.

Separate reversal compensates according to business rules.

Final balances reconcile.

---

# 52. End-to-End Concurrency

## TRF-TC-188 — Concurrent Overspending Protection

**Priority:** P0

### Test Data

```text
Available:
1,000.00

Transfer A:
800.00

Transfer B:
500.00
```

### Expected Result

At most one succeeds.

No negative balance.

No duplicate/missing ledger state.

---

# 53. End-to-End Daily Limit Race

## TRF-TC-189 — Concurrent Daily Limit Enforcement

**Priority:** P0

### Test Data

```text
Remaining Daily Limit:
1,000.00

Transfer A:
700.00

Transfer B:
600.00
```

### Expected Result

Combined successful transfers must not exceed daily limit.

---

# 54. Risk Mapping

| Risk                                           | Related Test Cases                         |
| ---------------------------------------------- | ------------------------------------------ |
| RISK-001 Incorrect balance                     | TRF-TC-021–033, 070–072, 148–160, 180–189  |
| RISK-003 Duplicate transaction                 | TRF-TC-073–086, 117, 139, 148–149, 182–183 |
| RISK-004 Partial transfer                      | TRF-TC-087–090, 184                        |
| RISK-005 Authentication bypass                 | TRF-TC-060–062                             |
| RISK-007 Insufficient funds                    | TRF-TC-022–025                             |
| RISK-008 Limit bypass                          | TRF-TC-034–041, 121–122, 189               |
| RISK-009 Frozen account                        | TRF-TC-043, 046, 102, 119                  |
| RISK-010 Fee calculation                       | TRF-TC-026–033, 159                        |
| RISK-013 Concurrency                           | TRF-TC-115–122, 188–189                    |
| RISK-017 Wrong beneficiary                     | TRF-TC-051–056, 064                        |
| RISK-018 Closed destination                    | TRF-TC-048, 050, 104                       |
| RISK-019 History mismatch                      | TRF-TC-129–135                             |
| RISK-022 Missing audit                         | TRF-TC-164–166                             |
| RISK-024 Daily-limit race                      | TRF-TC-121–122, 189                        |
| RISK-029 Cancelled scheduled transfer executes | TRF-TC-105–108, 186                        |
| RISK-030 Unauthorized API                      | TRF-TC-058–062, 140–147                    |
| RISK-033 Duplicate reference                   | TRF-TC-149                                 |
| RISK-037 Frontend-only validation              | TRF-TC-041, 145–147, 163                   |
| RISK-039 API/DB inconsistency                  | TRF-TC-148–156                             |
| RISK-042 Retry duplication                     | TRF-TC-011, 078–086, 183                   |
| RISK-043 Timezone scheduling                   | TRF-TC-114                                 |
| RISK-047 IDOR                                  | TRF-TC-055, 058–059, 144                   |
| RISK-048 UI/backend mismatch                   | TRF-TC-046, 050, 056, 155–156              |

---

# 55. Requirements Mapping

| Requirement                           | Test Cases              |
| ------------------------------------- | ----------------------- |
| REQ-TRF-001 Own-account transfer      | TRF-TC-001–004          |
| REQ-TRF-002 Same-bank transfer        | TRF-TC-005–007          |
| REQ-TRF-003 External transfer         | TRF-TC-008–011          |
| REQ-TRF-004 Amount/balance validation | TRF-TC-012–025          |
| REQ-TRF-005 Source authorization      | TRF-TC-057–062          |
| REQ-TRF-006 Fee correctness           | TRF-TC-026–033          |
| REQ-TRF-007 Transfer limits           | TRF-TC-034–041          |
| REQ-TRF-008 Source state validation   | TRF-TC-042–046          |
| REQ-TRF-009 Destination validation    | TRF-TC-047–050          |
| REQ-TRF-010 Beneficiary validation    | TRF-TC-051–056          |
| REQ-TRF-011 Duplicate prevention      | TRF-TC-073–077, 182     |
| REQ-TRF-012 Idempotency/retry         | TRF-TC-078–086, 183     |
| REQ-TRF-013 Atomicity                 | TRF-TC-087–090, 184     |
| REQ-TRF-014 Scheduled transfers       | TRF-TC-097–104, 185     |
| REQ-TRF-015 Scheduled cancellation    | TRF-TC-105–108, 186     |
| REQ-TRF-016 Recurring transfer        | TRF-TC-109–114          |
| REQ-TRF-017 Reversal                  | TRF-TC-123–128, 187     |
| REQ-TRF-018 History/traceability      | TRF-TC-129–139          |
| REQ-TRF-019 API integrity             | TRF-TC-140–147          |
| REQ-TRF-020 Persistence               | TRF-TC-148–154          |
| REQ-TRF-021 Cross-layer consistency   | TRF-TC-155–156          |
| REQ-TRF-022 Concurrency safety        | TRF-TC-115–122, 188–189 |

---

# 56. Smoke Candidates

Recommended transfer smoke cases:

```text
TRF-TC-005
TRF-TC-022
TRF-TC-031
TRF-TC-043
TRF-TC-051
TRF-TC-058
TRF-TC-070
TRF-TC-073
TRF-TC-078
TRF-TC-087
TRF-TC-093
TRF-TC-129
TRF-TC-155
```

---

# 57. Sanity Candidates

After transfer changes:

```text
TRF-TC-005
TRF-TC-022
TRF-TC-023
TRF-TC-027
TRF-TC-031
TRF-TC-035
TRF-TC-043
TRF-TC-051
TRF-TC-070
TRF-TC-073
TRF-TC-078
TRF-TC-083
TRF-TC-087
TRF-TC-100
TRF-TC-105
TRF-TC-123
TRF-TC-129
TRF-TC-148
TRF-TC-155
```

---

# 58. Critical Regression Candidates

```text
TRF-TC-001–011

TRF-TC-012–041

TRF-TC-042–062

TRF-TC-070–090

TRF-TC-093–128

TRF-TC-129–160

TRF-TC-163–169

TRF-TC-174–189
```

---

# 59. UI Automation Candidates

Best suited for:

```text
Playwright

Selenium

Cypress
```

Strong candidates:

```text
TRF-TC-001–010
TRF-TC-012–077
TRF-TC-085–086
TRF-TC-091–139
TRF-TC-167–187
```

---

# 60. API Automation Candidates

Best suited for:

```text
REST Assured

Postman
```

Strong candidates:

```text
TRF-TC-005–011
TRF-TC-012–062
TRF-TC-070–169
TRF-TC-180–189
```

---

# 61. SQL / Database Testing Candidates

Strong SQL candidates:

```text
TRF-TC-070–090

TRF-TC-100–128

TRF-TC-148–160

TRF-TC-180–189
```

Database validation should verify:

```text
Transfer row

Unique reference

Source ledger entry

Destination ledger entry

Fee entry

Status

Idempotency record

Scheduled execution record

Reversal relationship

Account balances

Audit relationships
```

---

# 62. Performance / JMeter Candidates

Strong concurrency and load candidates:

```text
TRF-TC-073
TRF-TC-077
TRF-TC-078
TRF-TC-083
TRF-TC-087
TRF-TC-100
TRF-TC-108
TRF-TC-110
TRF-TC-115–122
TRF-TC-188
TRF-TC-189
```

Performance testing must validate financial correctness after load.

Not only:

```text
Response time
```

but also:

```text
Transaction count

Duplicate count

Balance integrity

Limit enforcement

Error rate

Ledger consistency
```

---

# 63. Test Evidence Requirements

For critical transfer tests, capture as applicable:

```text
Customer ID

Source account

Destination / beneficiary

Opening source balance

Opening destination balance

Transfer amount

Fee

Idempotency key

Transfer reference

Transaction status

Closing source balance

Closing destination balance

API request/response

Database rows

Ledger entries

Audit record

Notification

Screenshot

Timestamp

Defect ID
```

Never attach real credentials or real customer financial data.

---

# 64. Transfer Defect Examples

Potential Critical/High defects include:

```text
Duplicate transfer after double click.

Transfer succeeds with insufficient funds.

Transfer fee excluded from balance validation.

Frozen source account transfers successfully.

Closed destination receives transfer.

Customer transfers from another customer's account.

Pending beneficiary can be used.

Client-modified fee is accepted.

Timeout retry creates duplicate transfer.

Scheduled cancelled transfer still executes.

Recurring transfer executes twice.

Source debited but destination not credited.

Daily limit bypassed through concurrency.

Reversal deletes original transaction.

UI says failed while backend completed.

Statement omits completed transfer.
```

---

# 65. Transfer Release Blockers

Release should normally be blocked by unresolved issues involving:

```text
Incorrect debit

Incorrect credit

Duplicate money movement

Unauthorized source account

Wrong beneficiary/destination

Insufficient-funds bypass

Limit bypass

Fee miscalculation with material impact

Frozen-account bypass

Atomicity failure

Retry duplication

Scheduled-transfer execution after cancellation

Concurrent overspending

Critical ledger mismatch

Critical history/statement mismatch
```

---

# 66. Transfer Exit Criteria

Transfer testing is acceptable when:

```text
All P0 transfer cases execute successfully.

Only authorized source accounts can be used.

Only valid destinations/beneficiaries can be used.

Transfer amounts and fees are correct.

Available balance is enforced.

Limits are enforced.

Frozen/closed states are enforced.

Duplicate submission is safe.

Idempotency works.

Timeout retries are safe.

Transfers are atomic.

Scheduled and recurring behavior is correct.

Cancellation works.

Reversals remain traceable.

Concurrency cannot create invalid balances.

UI/API/DB/ledger/history agree.

No unresolved Critical/P0 transfer defect remains.
```

---

# 67. Final Transfer Testing Principle

A transfer cannot be considered successful merely because the UI displays:

```text
Transfer Successful
```

QA must prove that:

```text
The customer was authorized.

The source account was valid.

The destination was valid.

The amount was valid.

The fee was correct.

The balance was sufficient.

The operation executed once.

The source was debited correctly.

The destination was credited correctly.

The database persisted the right state.

The history recorded the transaction.

The statement can reconcile it.

The notification reflects the real result.
```

The most important invariant is:

```text
One authorized customer intent
must produce one correct,
atomic,
traceable financial effect.
```

The core rule is:

```text
A Banking System must never lose,
duplicate,
misroute,
or silently corrupt customer money
during a transfer.
```
