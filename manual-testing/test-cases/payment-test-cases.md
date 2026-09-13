# Banking System — Payment Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Payments                       |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Payment scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Bill payments
* Merchant payments
* Saved payees
* Payment references
* Payment amounts
* Fees
* Limits
* Available balance
* Scheduled payments
* Recurring payments
* Provider integration
* Provider/local-state consistency
* Failed-payment financial neutrality
* Duplicate prevention
* Idempotency
* Timeout and retry
* Cancellation
* Reversal/refund
* Concurrency
* Notifications
* Transaction history
* Statements
* API validation
* Database validation
* Security
* Cross-browser behavior
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Payment test cases use:

```text
PAY-TC-XXX
```

Examples:

```text
PAY-TC-001
PAY-TC-002
PAY-TC-003
```

---

# 4. Critical Payment Invariants

## Invariant 1 — Exactly Once

```text
One intended payment
=
One financial effect
```

---

## Invariant 2 — Financial Neutrality on Failure

```text
FAILED PAYMENT
=
No incorrect settled debit
+
No duplicate fee
+
No false completed state
```

---

## Invariant 3 — Provider Consistency

The system must not permanently report:

```text
SUCCESS locally
while provider failed
```

or:

```text
FAILED locally
while provider successfully collected money
```

without reconciliation.

---

## Invariant 4 — Balance Validation

```text
Available Balance
>=
Payment Amount + Applicable Fee
```

must be enforced before financial authorization.

---

## Invariant 5 — Idempotency

Retries, refreshes, duplicate clicks, and provider callbacks must not create duplicate payment effects.

---

## Invariant 6 — Traceability

Every completed payment must have:

```text
Unique payment reference

Payee/provider reference

Amount

Fee

Status

Timestamp

Financial transaction relationship
```

---

# 5. Common Test Data

## Customer

```text
Customer:
CUST-001

Status:
ACTIVE

KYC:
VERIFIED
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
50,000.00

Available Balance:
50,000.00

Currency:
EGP
```

## Saved Bill Payee

```text
Payee:
PAYEE-001

Type:
UTILITY

Status:
ACTIVE
```

## Merchant Payee

```text
Payee:
PAYEE-002

Type:
MERCHANT

Status:
ACTIVE
```

## Disabled Payee

```text
Payee:
PAYEE-003

Status:
DISABLED
```

## Sample Bill

```text
Bill Reference:
BILL-001

Amount:
1,000.00

Status:
UNPAID
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer is authenticated.

Payment service is available.

Source account exists.

Synthetic payees/providers exist.

Provider sandbox/mock is available.

API/DB validation is available when required.
```

---

# 7. Bill Payment Test Cases

## PAY-TC-001 — Valid Bill Payment

**Priority:** P0
**Requirement:** REQ-PAY-001
**Automation:** Playwright / REST Assured

### Steps

1. Login as `CUST-001`.
2. Open Payments.
3. Select biller.
4. Enter valid customer/reference number.
5. Enter valid amount where applicable.
6. Review fee.
7. Confirm payment.

### Expected Result

* Payment completes once.
* Source account debited correctly.
* Fee applied correctly.
* Provider receives one valid payment.
* Payment reference generated.
* History updated.
* Success notification reflects authoritative status.

---

## PAY-TC-002 — Pay Exact Outstanding Bill Amount

**Priority:** P0

### Expected Result

Payment succeeds and bill reaches correct paid state.

---

## PAY-TC-003 — Pay Less Than Required Bill Amount

**Priority:** P1

### Expected Result

Behavior follows biller rules:

* Partial payment accepted, or
* Rejected with clear message.

---

## PAY-TC-004 — Pay More Than Allowed Bill Amount

**Priority:** P1

### Expected Result

Rejected if overpayment is not allowed.

---

## PAY-TC-005 — Pay Already Paid Bill

**Priority:** P0

### Expected Result

Duplicate payment is prevented where provider/business rules indicate the bill is already settled.

---

# 8. Merchant Payment Test Cases

## PAY-TC-006 — Valid Merchant Payment

**Priority:** P0
**Requirement:** REQ-PAY-002

### Expected Result

Merchant payment completes exactly once with correct financial effect.

---

## PAY-TC-007 — Invalid Merchant Identifier

**Priority:** P0

### Expected Result

Payment rejected.

No debit.

---

## PAY-TC-008 — Disabled Merchant

**Priority:** P0

### Expected Result

Payment cannot be initiated to disabled merchant.

---

# 9. Saved Payee Test Cases

## PAY-TC-009 — View Saved Payees

**Priority:** P1

### Expected Result

Only customer-owned/authorized saved payees are visible.

---

## PAY-TC-010 — Select Active Saved Payee

**Priority:** P1

Expected: correct payee details loaded.

---

## PAY-TC-011 — Select Disabled Saved Payee

**Priority:** P0

Expected: payment cannot proceed.

---

## PAY-TC-012 — Manipulate Payee ID

**Priority:** P0
**Risk:** RISK-047

### Expected Result

Customer cannot use unauthorized/non-owned payee reference if ownership applies.

---

# 10. Payment Reference Validation

## PAY-TC-013 — Valid Bill Reference

**Priority:** P1

Expected: accepted.

---

## PAY-TC-014 — Invalid Reference Format

**Priority:** P1

Expected: rejected.

---

## PAY-TC-015 — Empty Required Reference

**Priority:** P1

Expected: validation error.

---

## PAY-TC-016 — Unknown Reference

**Priority:** P0

Expected: provider/local validation rejects invalid reference safely.

---

## PAY-TC-017 — Very Long Reference

**Priority:** P2

Expected: safely rejected or constrained.

---

# 11. Payment Amount Validation

## PAY-TC-018 — Minimum Valid Amount

**Priority:** P1
**Type:** Boundary

Expected: accepted if other rules pass.

---

## PAY-TC-019 — Amount Below Minimum

**Priority:** P1

Expected: rejected.

---

## PAY-TC-020 — Zero Amount

**Priority:** P0

Expected: rejected.

---

## PAY-TC-021 — Negative Amount

**Priority:** P0

Expected: rejected.

---

## PAY-TC-022 — Maximum Valid Amount

**Priority:** P0

Expected: accepted when limits/balance permit.

---

## PAY-TC-023 — Maximum Plus Smallest Unit

**Priority:** P0

Expected: rejected.

---

## PAY-TC-024 — Excess Decimal Precision

**Priority:** P1

Expected: handled according to documented currency precision.

---

# 12. Balance Validation Test Cases

## PAY-TC-025 — Sufficient Available Balance

**Priority:** P0
**Requirement:** REQ-PAY-003

Expected: payment may proceed.

---

## PAY-TC-026 — Insufficient Available Balance

**Priority:** P0
**Risk:** RISK-026

Expected:

```text
Payment rejected.

No permanent debit.

No provider settlement.

No success notification.
```

---

## PAY-TC-027 — Available Balance Equals Amount Plus Fee

**Priority:** P0
**Type:** Boundary

Expected: succeeds if zero remaining balance is allowed.

---

## PAY-TC-028 — Available Balance One Unit Below Required Total

**Priority:** P0

Expected: rejected.

---

## PAY-TC-029 — Current Balance Sufficient / Available Balance Insufficient

**Priority:** P0

Expected: available balance is authoritative for payment eligibility.

---

# 13. Fee Test Cases

## PAY-TC-030 — Payment Without Fee

**Priority:** P1

Expected: no fee charged.

---

## PAY-TC-031 — Fixed Payment Fee

**Priority:** P0
**Risk:** RISK-010

Expected: fee correctly displayed and charged once.

---

## PAY-TC-032 — Percentage Payment Fee

**Priority:** P0

Expected: calculation correct.

---

## PAY-TC-033 — Minimum Fee Boundary

**Priority:** P1

Expected: minimum rule applied.

---

## PAY-TC-034 — Maximum Fee Cap

**Priority:** P1

Expected: cap applied correctly.

---

## PAY-TC-035 — Fee Included in Balance Validation

**Priority:** P0

Expected:

```text
Available Balance
>=
Amount + Fee
```

---

## PAY-TC-036 — Manipulate Fee Client-Side

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

### Expected Result

Backend recalculates authoritative fee.

---

# 14. Payment Limit Test Cases

## PAY-TC-037 — Below Per-Payment Limit

**Priority:** P1

Expected: allowed.

---

## PAY-TC-038 — Exact Per-Payment Limit

**Priority:** P0

Expected: follows configured boundary.

---

## PAY-TC-039 — Above Per-Payment Limit

**Priority:** P0
**Risk:** RISK-008

Expected: rejected.

---

## PAY-TC-040 — Daily Payment Limit Reached Exactly

**Priority:** P0

Expected: succeeds if boundary is inclusive.

---

## PAY-TC-041 — Daily Payment Limit Exceeded

**Priority:** P0

Expected: rejected.

---

## PAY-TC-042 — Manipulate Client-Side Limit

**Priority:** P0

Expected: backend limit remains authoritative.

---

# 15. Source Account State Test Cases

## PAY-TC-043 — Active Source Account

**Priority:** P0

Expected: payment may proceed.

---

## PAY-TC-044 — Frozen Source Account

**Priority:** P0
**Risk:** RISK-009

Expected: rejected.

---

## PAY-TC-045 — Restricted Source Account

**Priority:** P0

Expected: state restrictions enforced.

---

## PAY-TC-046 — Closed Source Account

**Priority:** P0

Expected: rejected.

---

## PAY-TC-047 — Account Frozen After Confirmation Loaded

**Priority:** P0
**Risk:** RISK-048

Expected: backend revalidates and rejects payment.

---

# 16. Payment Confirmation Test Cases

## PAY-TC-048 — Confirmation Shows Payee

**Priority:** P0

Expected: correct payee displayed.

---

## PAY-TC-049 — Confirmation Shows Payment Reference

**Priority:** P0

Expected: exact intended reference displayed.

---

## PAY-TC-050 — Confirmation Shows Amount

**Priority:** P0

Expected: correct amount.

---

## PAY-TC-051 — Confirmation Shows Fee

**Priority:** P0

Expected: correct fee.

---

## PAY-TC-052 — Confirmation Shows Total Debit

**Priority:** P0

Expected:

```text
Amount + Fee
```

---

## PAY-TC-053 — Cancel Before Confirmation

**Priority:** P1

Expected: no financial effect.

---

# 17. Successful Payment Financial Validation

## PAY-TC-054 — Source Balance Debited Correctly

**Priority:** P0

Example:

```text
Opening:
10,000.00

Payment:
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

## PAY-TC-055 — Payment Fee Charged Once

**Priority:** P0

Expected: one fee only.

---

## PAY-TC-056 — Payment Transaction Appears Once

**Priority:** P0

Expected: no duplicate history/ledger entry.

---

# 18. Duplicate Submission Test Cases

## PAY-TC-057 — Double-Click Confirm Payment

**Priority:** P0
**Requirement:** REQ-PAY-004
**Risk:** RISK-027

Expected: one payment only.

---

## PAY-TC-058 — Repeated Enter Submission

**Priority:** P0

Expected: one financial effect.

---

## PAY-TC-059 — Refresh After Payment Completion

**Priority:** P0

Expected: refresh does not resubmit payment.

---

## PAY-TC-060 — Browser Back and Resubmit

**Priority:** P0

Expected: duplicate payment prevented.

---

## PAY-TC-061 — Two Tabs Submit Same Payment

**Priority:** P0

Expected: duplicate controls prevent unintended double settlement.

---

# 19. Idempotency Test Cases

## PAY-TC-062 — Same Idempotency Key / Same Payload

**Priority:** P0
**Requirement:** REQ-PAY-005
**Automation:** REST Assured

Expected: one payment effect.

---

## PAY-TC-063 — Same Key / Different Payload

**Priority:** P0

Expected: conflicting request rejected.

---

## PAY-TC-064 — Different Keys for Independent Payments

**Priority:** P0

Expected: separate intended payments may process independently.

---

## PAY-TC-065 — Provider Callback Repeated

**Priority:** P0
**Risk:** RISK-027

Expected: repeated identical provider callback does not create duplicate local financial effects.

---

# 20. Timeout / Retry Test Cases

## PAY-TC-066 — Backend Completes / Client Times Out

**Priority:** P0
**Risk:** RISK-042

### Expected Result

Retry returns/reconciles original payment.

One debit only.

---

## PAY-TC-067 — Provider Completes / Local Response Lost

**Priority:** P0

Expected: reconciliation identifies provider success and avoids duplicate resubmission.

---

## PAY-TC-068 — Provider Timeout Before Confirmation

**Priority:** P0

Expected: payment enters safe unknown/pending state until authoritative result is known.

---

## PAY-TC-069 — Retry Unknown Payment

**Priority:** P0

Expected: safe retry does not create duplicate provider payment.

---

# 21. Provider Integration Test Cases

## PAY-TC-070 — Provider Success / Local Success

**Priority:** P0
**Requirement:** REQ-PAY-006

Expected: consistent completed state.

---

## PAY-TC-071 — Provider Failure / Local Failure

**Priority:** P0

Expected: financially neutral failure.

---

## PAY-TC-072 — Provider Success / Local Failure

**Priority:** P0
**Risk:** RISK-021? No; use provider mismatch risk context

### Expected Result

System detects mismatch and reconciles safely.

Must not permanently leave customer with contradictory status.

---

## PAY-TC-073 — Provider Failure / Local Success

**Priority:** P0

Expected: mismatch detected.

Payment cannot remain incorrectly completed.

---

## PAY-TC-074 — Provider Duplicate Success Callback

**Priority:** P0

Expected: exactly-once local financial effect.

---

## PAY-TC-075 — Provider Returns Unknown Status

**Priority:** P0

Expected: local state remains pending/unknown according to design until reconciled.

---

# 22. Failed Payment Neutrality Test Cases

## PAY-TC-076 — Provider Rejects Before Debit

**Priority:** P0
**Requirement:** REQ-PAY-007

Expected: no financial effect.

---

## PAY-TC-077 — Failed Payment Releases Hold

**Priority:** P0
**Risk:** RISK-026

Expected: temporary hold released when failure becomes final.

---

## PAY-TC-078 — Failed Payment Does Not Charge Fee

**Priority:** P0

Expected: no invalid fee unless explicitly defined by business rules.

---

## PAY-TC-079 — Failed Payment Does Not Count as Successful Limit Consumption

**Priority:** P0

Expected: limit usage follows documented policy.

---

## PAY-TC-080 — Failed Payment Sends No Success Notification

**Priority:** P0

Expected: authoritative failure reflected.

---

# 23. Payment Status Test Cases

## PAY-TC-081 — PENDING Payment

**Priority:** P1

Expected: not displayed as completed.

---

## PAY-TC-082 — PROCESSING Payment

**Priority:** P1

Expected: clear nonfinal status.

---

## PAY-TC-083 — COMPLETED Payment

**Priority:** P0

Expected: all financial effects committed correctly.

---

## PAY-TC-084 — FAILED Payment

**Priority:** P0

Expected: failure semantics consistent across all layers.

---

## PAY-TC-085 — CANCELLED Payment

**Priority:** P0

Expected: no execution after cancellation.

---

## PAY-TC-086 — REVERSED / REFUNDED Payment

**Priority:** P0

Expected: original retained and correction traceable.

---

# 24. Scheduled Payment Test Cases

## PAY-TC-087 — Schedule Valid Future Payment

**Priority:** P0
**Requirement:** REQ-PAY-008

Expected: payment scheduled with correct date/details.

---

## PAY-TC-088 — Schedule Payment in Past

**Priority:** P1

Expected: rejected.

---

## PAY-TC-089 — Scheduled Payment Executes Once

**Priority:** P0

Expected: one execution at scheduled time.

---

## PAY-TC-090 — Insufficient Funds at Scheduled Execution

**Priority:** P0

Expected: safe failure with no invalid permanent debit.

---

## PAY-TC-091 — Account Frozen Before Execution

**Priority:** P0

Expected: current account state revalidated.

---

## PAY-TC-092 — Payee Disabled Before Execution

**Priority:** P0

Expected: current payee state revalidated.

---

# 25. Scheduled Payment Cancellation

## PAY-TC-093 — Cancel Scheduled Payment

**Priority:** P0

Expected:

```text
Status:
CANCELLED

Execution Count:
0
```

---

## PAY-TC-094 — Cancel at Execution Boundary

**Priority:** P0

Expected: deterministic result with no state showing both cancelled and successfully executed incorrectly.

---

## PAY-TC-095 — Cancel Already Completed Payment

**Priority:** P0

Expected: rejected; refund/reversal flow should be used where applicable.

---

# 26. Recurring Payment Test Cases

## PAY-TC-096 — Create Recurring Payment

**Priority:** P1
**Requirement:** REQ-PAY-009

Expected: schedule stored correctly.

---

## PAY-TC-097 — Recurring Payment Executes Once Per Cycle

**Priority:** P0

Expected: no duplicate occurrence.

---

## PAY-TC-098 — Recurring Payment With Insufficient Funds

**Priority:** P0

Expected: cycle handled safely.

---

## PAY-TC-099 — Cancel Recurring Payment

**Priority:** P0

Expected: future occurrences stop.

---

## PAY-TC-100 — Recurring Payment Timezone Handling

**Priority:** P1
**Risk:** RISK-043

Expected: execution occurs on intended schedule.

---

# 27. Refund / Reversal Test Cases

## PAY-TC-101 — Full Refund

**Priority:** P0
**Requirement:** REQ-PAY-010

Expected:

* Original payment preserved.
* Refund recorded separately.
* Correct amount credited.
* History updated.

---

## PAY-TC-102 — Partial Refund

**Priority:** P1

Expected: valid partial refund correctly credited where supported.

---

## PAY-TC-103 — Refund Exceeds Original Payment

**Priority:** P0

Expected: rejected.

---

## PAY-TC-104 — Duplicate Refund

**Priority:** P0

Expected: duplicate refund prevented.

---

## PAY-TC-105 — Unauthorized Refund Attempt

**Priority:** P0

Expected: denied.

---

## PAY-TC-106 — Refund Fee Handling

**Priority:** P1

Expected: fee refund behavior matches documented business rules.

---

# 28. Payment History Test Cases

## PAY-TC-107 — Completed Payment Appears Once

**Priority:** P0

Expected: one completed payment entry.

---

## PAY-TC-108 — Failed Payment Shown as Failed

**Priority:** P1

Expected: not misrepresented as successful.

---

## PAY-TC-109 — Refunded Payment Shows Relationship

**Priority:** P0

Expected: original and refund remain traceable.

---

# 29. Statement Integration Test Cases

## PAY-TC-110 — Completed Payment Appears in Statement

**Priority:** P0

Expected: amount and fee represented correctly.

---

## PAY-TC-111 — Failed Payment Not Included as Settled Debit

**Priority:** P0

Expected: statement remains accurate.

---

## PAY-TC-112 — Refund Appears Correctly

**Priority:** P0

Expected: credit/refund included according to statement rules.

---

# 30. Notification Test Cases

## PAY-TC-113 — Successful Payment Notification

**Priority:** P1

Expected: matches authoritative completed state.

---

## PAY-TC-114 — Failed Payment Notification

**Priority:** P1

Expected: clearly indicates failure.

---

## PAY-TC-115 — Refund Notification

**Priority:** P1

Expected: correct amount/reference.

---

## PAY-TC-116 — Duplicate Callback Does Not Duplicate Notification

**Priority:** P1

Expected: notification deduplicated according to event design.

---

# 31. API Payment Test Cases

## PAY-TC-117 — Valid Payment API

**Priority:** P0
**Automation:** REST Assured / Postman

Expected: successful payment with correct response.

---

## PAY-TC-118 — Unauthenticated Payment API

**Priority:** P0

Expected: denied.

---

## PAY-TC-119 — Unauthorized Source Account

**Priority:** P0
**Risk:** RISK-047

Expected: denied.

---

## PAY-TC-120 — Invalid Payee ID

**Priority:** P0

Expected: rejected.

---

## PAY-TC-121 — Invalid Amount

**Priority:** P0

Expected: rejected.

---

## PAY-TC-122 — Protected Status Field Manipulation

**Priority:** P0

Payload:

```json
{
  "status": "COMPLETED"
}
```

Expected: ignored/rejected.

---

## PAY-TC-123 — Manipulated Provider Result

**Priority:** P0

Expected: customer/client cannot mark provider payment successful.

---

# 32. Database Validation Test Cases

## PAY-TC-124 — Payment Record Exists Once

**Priority:** P0
**Automation:** SQL

Expected: one authoritative payment record.

---

## PAY-TC-125 — Unique Payment Reference

**Priority:** P0
**Risk:** RISK-033

Expected: unique reference.

---

## PAY-TC-126 — Debit Ledger Entry Correct

**Priority:** P0

Expected: amount/fee/reference correct.

---

## PAY-TC-127 — Failed Payment Has No Invalid Completed Debit

**Priority:** P0

Expected: no settled ledger inconsistency.

---

## PAY-TC-128 — Provider Reference Persisted

**Priority:** P0

Expected: provider/local correlation available.

---

## PAY-TC-129 — Refund Relationship Persisted

**Priority:** P0

Expected: refund references original payment.

---

## PAY-TC-130 — Scheduled Payment Persistence

**Priority:** P1

Expected: schedule, next execution, state correct.

---

# 33. UI/API/Database Consistency

## PAY-TC-131 — Completed Payment Cross-Layer Validation

**Priority:** P0
**Risk:** RISK-039, RISK-048

Validate:

```text
UI Status
=
API Status
=
DB Status
```

and amount/fee/reference agree.

---

## PAY-TC-132 — Failed Payment Cross-Layer Validation

**Priority:** P0

Expected: all layers agree on safe failure.

---

## PAY-TC-133 — Provider/Local Reconciliation

**Priority:** P0

Expected:

```text
Provider Result
↔
Local Payment
↔
Ledger
↔
Customer Balance
```

reconcile.

---

# 34. Concurrency Test Cases

## PAY-TC-134 — Two Concurrent Payments Within Balance

**Priority:** P0
**Risk:** RISK-013

Expected: both may succeed if funds and limits support both.

---

## PAY-TC-135 — Concurrent Payments Exceed Balance

**Priority:** P0

Example:

```text
Available:
1,000.00

Payment A:
700.00

Payment B:
500.00
```

Expected: both cannot succeed.

---

## PAY-TC-136 — Payment and Transfer Compete for Funds

**Priority:** P0

Expected: atomic available-balance enforcement.

---

## PAY-TC-137 — Duplicate Provider Callbacks Concurrently

**Priority:** P0

Expected: one local financial effect.

---

## PAY-TC-138 — Concurrent Daily Limit Consumption

**Priority:** P0

Expected: aggregate limit cannot be bypassed through race condition.

---

# 35. Precision and Rounding Test Cases

## PAY-TC-139 — Smallest Supported Amount

**Priority:** P1

Expected: correct precision.

---

## PAY-TC-140 — Percentage Fee Rounding

**Priority:** P0

Expected: documented rounding method used.

---

## PAY-TC-141 — Repeated Small Payments

**Priority:** P0

Expected: no cumulative balance drift.

---

# 36. Error Handling Test Cases

## PAY-TC-142 — Payment Service Unavailable Before Submission

**Priority:** P1

Expected: safe error with no financial effect.

---

## PAY-TC-143 — Provider Unavailable

**Priority:** P0

Expected: local system does not falsely mark payment completed.

---

## PAY-TC-144 — History Service Unavailable After Payment

**Priority:** P1

Expected: successful payment remains correct; UI must not re-submit due to missing history.

---

## PAY-TC-145 — Notification Service Fails After Payment

**Priority:** P1

Expected: notification failure does not roll back completed financial transaction incorrectly.

---

# 37. Security Input Test Cases

## PAY-TC-146 — Script-Like Reference Input

**Priority:** P1

Expected: safely rendered.

---

## PAY-TC-147 — SQL-Like Input

**Priority:** P1

Expected: no injection or DB error.

---

## PAY-TC-148 — Unexpected Protected Fields

**Priority:** P0

Payload:

```json
{
  "amount": 1000,
  "fee": 0,
  "status": "COMPLETED",
  "providerStatus": "SUCCESS"
}
```

Expected: protected values cannot override backend authority.

---

# 38. Audit Test Cases

## PAY-TC-149 — Completed Payment Trace Record

**Priority:** P1

Expected: sufficient identifiers for investigation.

---

## PAY-TC-150 — Admin Refund Audit

**Priority:** P0
**Risk:** RISK-022

Expected audit includes:

```text
Actor
Payment
Refund Amount
Reason
Timestamp
```

---

## PAY-TC-151 — No Authentication Secrets in Audit

**Priority:** P0

Expected: passwords/tokens/OTP absent.

---

# 39. Cross-Browser Test Cases

## PAY-TC-152 — Payment in Chrome

**Priority:** P1

Expected: critical flow works.

---

## PAY-TC-153 — Payment in Edge

**Priority:** P1

Expected: critical flow works.

---

## PAY-TC-154 — Payment in Firefox

**Priority:** P1

Expected: critical flow works.

---

## PAY-TC-155 — Payment in WebKit

**Priority:** P1

Expected: critical flow works.

---

# 40. Responsive Test Cases

## PAY-TC-156 — Payment at 390×844

**Priority:** P0

Expected:

* Payee visible.
* Amount visible.
* Fee visible.
* Total debit visible.
* Confirm control accessible.

---

## PAY-TC-157 — Payment at 360×800

**Priority:** P0

Expected: no critical payment detail clipped.

---

## PAY-TC-158 — Large Payment Amount on Mobile Confirmation

**Priority:** P0

Expected: complete value remains readable.

---

# 41. Accessibility Test Cases

## PAY-TC-159 — Keyboard-Only Payment

**Priority:** P1

Expected: full flow operable using keyboard where accessibility support applies.

---

## PAY-TC-160 — Payment Form Labels

**Priority:** P2

Expected: payee/reference/amount fields correctly labeled.

---

## PAY-TC-161 — Payment Error Accessibility

**Priority:** P1

Expected: errors such as insufficient funds are clearly perceivable and associated with relevant context.

---

# 42. End-to-End Successful Payment

## PAY-TC-162 — Complete Payment Reconciliation

**Priority:** P0

### Test Data

```text
Opening Balance:
10,000.00

Payment:
1,000.00

Fee:
10.00
```

Expected closing:

```text
8,990.00
```

### Steps

1. Record opening balance.
2. Submit payment.
3. Capture local reference.
4. Capture provider reference.
5. Validate balance.
6. Validate history.
7. Validate API.
8. Validate DB.
9. Validate statement.
10. Validate notification.

### Expected Result

Every layer reflects one successful payment and reconciles.

---

# 43. End-to-End Failed Payment

## PAY-TC-163 — Provider Failure Remains Financially Neutral

**Priority:** P0

Expected:

```text
Provider:
FAILED

Local Payment:
FAILED

Permanent Debit:
0

Invalid Fee:
0

Success Notification:
No
```

---

# 44. End-to-End Duplicate Prevention

## PAY-TC-164 — Double Submit + Provider Callback

**Priority:** P0

### Steps

1. Submit payment twice rapidly.
2. Provider sends repeated success callback.
3. Validate UI/API/DB/ledger.

### Expected Result

```text
Customer Intention:
1

Provider Settlement:
1

Local Financial Effect:
1
```

---

# 45. End-to-End Timeout Reconciliation

## PAY-TC-165 — Provider Success After Client Timeout

**Priority:** P0

Expected:

* Customer eventually receives authoritative successful status.
* Retry does not create second payment.
* Balance reflects one payment only.

---

# 46. End-to-End Scheduled Payment

## PAY-TC-166 — Schedule → Execute → Reconcile

**Priority:** P0

Expected:

```text
Schedule:
Created

Execution:
1

Debit:
Correct

Provider:
Correct

History:
Correct
```

---

# 47. End-to-End Scheduled Cancellation

## PAY-TC-167 — Schedule → Cancel → Due Date

**Priority:** P0

Expected:

```text
Status:
CANCELLED

Execution Count:
0

Financial Effect:
0
```

---

# 48. End-to-End Refund

## PAY-TC-168 — Payment → Full Refund → Reconcile

**Priority:** P0

Expected:

* Original payment retained.
* Refund recorded separately.
* Correct credit applied.
* History and statement reconcile.

---

# 49. End-to-End Concurrency

## PAY-TC-169 — Concurrent Overspending Protection

**Priority:** P0

### Test Data

```text
Available:
1,000.00

Payment A:
700.00

Payment B:
500.00
```

Expected: at most one succeeds if no overdraft allowed.

---

# 50. Provider Mismatch End-to-End Test

## PAY-TC-170 — Provider Success / Local Failure Recovery

**Priority:** P0
**Risk:** RISK-021? More accurately provider mismatch/business integrity

### Steps

1. Provider successfully processes payment.
2. Simulate local persistence/update failure.
3. Run reconciliation/recovery.
4. Review customer balance and final status.

### Expected Result

System eventually converges on one correct authoritative outcome.

No duplicate debit.

No unexplained money loss.

---

# 51. Risk Mapping

| Risk                                     | Related Test Cases                        |
| ---------------------------------------- | ----------------------------------------- |
| RISK-001 Incorrect balance               | PAY-TC-025–036, 054–056, 124–141, 162–170 |
| RISK-003 Duplicate transaction           | PAY-TC-057–069, 074, 104, 137, 164–165    |
| RISK-007 Insufficient funds              | PAY-TC-026–029                            |
| RISK-008 Limit bypass                    | PAY-TC-037–042, 138                       |
| RISK-009 Frozen account                  | PAY-TC-044, 047, 091                      |
| RISK-010 Fee calculation                 | PAY-TC-030–036, 140                       |
| RISK-013 Concurrency                     | PAY-TC-134–138, 169                       |
| RISK-019 History inconsistency           | PAY-TC-107–112                            |
| RISK-022 Audit gap                       | PAY-TC-149–151                            |
| RISK-026 Failed payment changes balance  | PAY-TC-026, 071, 076–080, 127, 163        |
| RISK-027 Duplicate payment               | PAY-TC-057–069, 074, 104, 137, 164        |
| RISK-030 Unauthorized API                | PAY-TC-117–123                            |
| RISK-033 Duplicate reference             | PAY-TC-125                                |
| RISK-037 Frontend-only validation        | PAY-TC-036, 042, 122–123, 148             |
| RISK-039 API/DB inconsistency            | PAY-TC-124–133                            |
| RISK-042 Retry duplication               | PAY-TC-066–069, 165                       |
| RISK-043 Timezone scheduling             | PAY-TC-100                                |
| RISK-047 IDOR                            | PAY-TC-012, 119                           |
| RISK-048 UI/backend mismatch             | PAY-TC-047, 131–133                       |
| RISK-021/Provider reconciliation concern | PAY-TC-070–075, 133, 170                  |

---

# 52. Requirements Mapping

| Requirement                           | Test Cases               |
| ------------------------------------- | ------------------------ |
| REQ-PAY-001 Bill payment              | PAY-TC-001–005           |
| REQ-PAY-002 Merchant payment          | PAY-TC-006–008           |
| REQ-PAY-003 Balance validation        | PAY-TC-025–029           |
| REQ-PAY-004 Duplicate prevention      | PAY-TC-057–061           |
| REQ-PAY-005 Idempotency               | PAY-TC-062–069           |
| REQ-PAY-006 Provider consistency      | PAY-TC-070–075, 133, 170 |
| REQ-PAY-007 Failed-payment neutrality | PAY-TC-076–080, 163      |
| REQ-PAY-008 Scheduled payments        | PAY-TC-087–095, 166–167  |
| REQ-PAY-009 Recurring payments        | PAY-TC-096–100           |
| REQ-PAY-010 Refund/reversal           | PAY-TC-101–106, 168      |

---

# 53. Smoke Candidates

Recommended payment smoke cases:

```text
PAY-TC-001
PAY-TC-006
PAY-TC-026
PAY-TC-035
PAY-TC-044
PAY-TC-054
PAY-TC-057
PAY-TC-062
PAY-TC-071
PAY-TC-076
PAY-TC-107
PAY-TC-131
```

---

# 54. Sanity Candidates

After payment changes:

```text
PAY-TC-001
PAY-TC-026
PAY-TC-027
PAY-TC-031
PAY-TC-035
PAY-TC-044
PAY-TC-054
PAY-TC-057
PAY-TC-062
PAY-TC-066
PAY-TC-070
PAY-TC-071
PAY-TC-077
PAY-TC-083
PAY-TC-087
PAY-TC-093
PAY-TC-101
PAY-TC-124
PAY-TC-131
```

---

# 55. Critical Regression Candidates

```text
PAY-TC-001–008

PAY-TC-012–016

PAY-TC-018–042

PAY-TC-043–080

PAY-TC-083–106

PAY-TC-107–151

PAY-TC-156–170
```

---

# 56. UI Automation Candidates

Best suited for:

```text
Playwright

Selenium

Cypress
```

Strong candidates:

```text
PAY-TC-001–061

PAY-TC-068–116

PAY-TC-142–168
```

---

# 57. API Automation Candidates

Best suited for:

```text
REST Assured

Postman
```

Strong candidates:

```text
PAY-TC-001–008

PAY-TC-012–080

PAY-TC-087–151

PAY-TC-162–170
```

---

# 58. SQL / Database Testing Candidates

Strong SQL candidates:

```text
PAY-TC-054–080

PAY-TC-089–106

PAY-TC-124–141

PAY-TC-162–170
```

Database validation should verify:

```text
Payment row

Unique reference

Provider reference

Debit ledger entry

Fee entry

Status

Idempotency state

Scheduled/recurring record

Refund relationship

Account balance

Audit relationship
```

---

# 59. Performance / JMeter Candidates

Strong candidates:

```text
PAY-TC-057

PAY-TC-061

PAY-TC-062

PAY-TC-065

PAY-TC-066

PAY-TC-074

PAY-TC-089

PAY-TC-097

PAY-TC-134–138

PAY-TC-164

PAY-TC-169
```

Performance tests must validate:

```text
Duplicate count

Provider call count

Financial effect count

Balance integrity

Limit enforcement

Response time

Throughput

Error rate
```

---

# 60. Test Evidence Requirements

For critical payment tests, capture as applicable:

```text
Customer ID

Source account

Payee / provider

Bill reference

Opening balance

Payment amount

Fee

Total debit

Local payment reference

Provider reference

Idempotency key

Status

Closing balance

API request/response

Database rows

Ledger records

Notification

Audit record

Screenshot

Timestamp

Defect ID
```

Never store real credentials, OTPs, or production payment data.

---

# 61. Payment Defect Examples

Potential Critical/High defects include:

```text
Duplicate payment after double click.

Provider success but local payment remains failed.

Local success while provider rejected.

Failed payment permanently reduces balance.

Failed payment leaves unreleased hold.

Fee excluded from available-balance validation.

Frozen account can pay bill.

Daily payment limit bypassed through concurrency.

Duplicate provider callback creates duplicate debit.

Scheduled cancelled payment still executes.

Recurring payment executes twice.

Refund exceeds original payment.

Customer can pay using another customer's source account.

UI says payment failed while provider successfully collected funds.
```

---

# 62. Payment Release Blockers

Release should normally be blocked by unresolved issues involving:

```text
Duplicate customer payment

Incorrect debit

Incorrect fee

Failed-payment financial loss

Provider/local unreconciled financial mismatch

Unauthorized source account

Frozen-account payment bypass

Limit bypass

Duplicate refund

Scheduled payment after cancellation

Recurring payment duplicate execution

Critical statement/history mismatch

Critical payment-state inconsistency
```

---

# 63. Payment Exit Criteria

Payment testing is acceptable when:

```text
All P0 payment cases execute successfully.

Valid bill and merchant payments work.

Invalid payees/references are rejected.

Available balance is enforced.

Fees are correct.

Limits are enforced.

Frozen/closed accounts are enforced.

Duplicate submission is safe.

Idempotency works.

Timeout/retry behavior is safe.

Provider/local states reconcile.

Failed payments are financially neutral.

Scheduled/recurring payments behave correctly.

Refunds are traceable.

Concurrency cannot create invalid balance or limit state.

UI/API/DB/provider state agrees.

No unresolved Critical/P0 payment defect remains.
```

---

# 64. Final Payment Testing Principle

A payment is not proven correct simply because the screen displays:

```text
Payment Successful
```

QA must establish that:

```text
The customer was authorized.

The source account was valid.

The payee/reference was valid.

The amount was valid.

The fee was correct.

The available balance was sufficient.

The provider processed the intended payment.

The local system recorded the same result.

The customer was debited exactly once.

A retry cannot create a second payment.

History and statements reconcile.

Notifications reflect the authoritative result.
```

The most important payment invariant is:

```text
One authorized customer payment intent
must result in one correct and traceable settlement outcome.
```

The core rule is:

```text
The Banking System and the payment provider
must eventually agree on exactly one authoritative financial outcome,
without duplicating, losing, or silently misrepresenting customer money.
```
