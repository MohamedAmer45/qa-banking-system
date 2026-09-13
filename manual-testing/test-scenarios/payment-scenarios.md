# Banking System — Payment Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Payments                       |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for payment functionality within the Banking System.

Payments are a high-risk financial area because defects may cause:

* Incorrect account debits
* Duplicate payments
* Payments to wrong payees
* Incorrect fees
* Failed-payment balance changes
* Limit bypasses
* Incorrect payment status
* Incorrect scheduling
* Incorrect reversals
* Misleading notifications
* Inconsistent transaction history

The scenarios cover immediate, scheduled, and recurring payments, along with payee validation, financial integrity, authorization, idempotency, and reconciliation.

---

# 3. Scope

Payment testing includes:

* Bill payments
* Merchant payments
* Saved payees
* One-time payees
* Payment amounts
* Payment references
* Source accounts
* Payee validation
* Payment limits
* Fees
* Immediate payments
* Scheduled payments
* Recurring payments
* Pending payments
* Failed payments
* Cancelled payments
* Reversed payments
* Duplicate prevention
* Idempotency
* Account balance impact
* Transaction history
* Statements
* Notifications
* Audit logging
* Authorization
* Concurrency

---

# 4. Scenario Naming Convention

Payment scenarios use:

```text
TS-PAY-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Valid Payment Scenarios

## TS-PAY-001 — Pay valid bill using active account

**Priority:** P0

Expected:

* Payment succeeds.
* Source account is debited correctly.
* Payment record is created.
* Correct payment status is displayed.
* Transaction reference is generated.

---

## TS-PAY-002 — Pay valid merchant

**Priority:** P0

Expected:

Payment completes successfully according to merchant-payment rules.

---

## TS-PAY-003 — Pay saved payee

**Priority:** P1

Expected:

Correct payee is used.

---

## TS-PAY-004 — Pay one-time payee

**Priority:** P1

Where supported.

Expected:

Payment completes without incorrectly adding payee to saved list unless requested.

---

## TS-PAY-005 — Make minimum valid payment

**Priority:** P1

Expected:

Accepted.

---

## TS-PAY-006 — Make normal payment within balance and limits

**Priority:** P0

Expected:

Payment completes correctly.

---

## TS-PAY-007 — Make payment exactly at maximum transaction limit

**Priority:** P0

Expected:

Accepted where maximum is inclusive.

---

# 6. Source Account Scenarios

## TS-PAY-008 — Pay from active account

**Priority:** P0

Expected:

Allowed.

---

## TS-PAY-009 — Pay from frozen account

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-010 — Pay from restricted account

**Priority:** P0

Expected:

Rejected when restriction blocks payments.

---

## TS-PAY-011 — Pay from suspended account

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-012 — Pay from closed account

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-013 — Customer attempts payment from another customer's account

**Priority:** P0

Expected:

Authorization failure.

---

## TS-PAY-014 — Source account becomes frozen before confirmation

**Priority:** P0

Expected:

Payment rejected at final submission.

---

# 7. Payee Validation Scenarios

## TS-PAY-015 — Pay active valid payee

**Priority:** P0

Expected:

Allowed.

---

## TS-PAY-016 — Pay nonexistent payee

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-017 — Pay disabled payee

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-018 — Pay deleted payee using stale page

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-019 — Customer attempts another customer's private saved payee

**Priority:** P0

Expected:

Authorization failure where payees are customer-owned.

---

## TS-PAY-020 — Payee becomes disabled before final confirmation

**Priority:** P0

Expected:

Latest payee state is validated before payment execution.

---

# 8. Bill Validation Scenarios

## TS-PAY-021 — Pay valid outstanding bill

**Priority:** P0

Expected:

Payment succeeds.

---

## TS-PAY-022 — Pay nonexistent bill reference

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-023 — Pay already-paid bill

**Priority:** P0

Expected:

Duplicate settlement prevented.

---

## TS-PAY-024 — Pay expired bill

**Priority:** P1

Expected:

Behavior follows bill rules.

---

## TS-PAY-025 — Pay bill with invalid customer/reference combination

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-026 — Bill amount displayed correctly before payment

**Priority:** P0

Expected:

Customer sees correct amount due.

---

## TS-PAY-027 — Bill status changes after successful payment

**Priority:** P0

Expected:

Bill marked paid or equivalent.

---

## TS-PAY-028 — Failed payment leaves bill unpaid

**Priority:** P0

Expected:

Bill state remains outstanding.

---

# 9. Payment Amount Validation

Assume example limits:

```text
Minimum payment = 1.00
Maximum payment = 50,000.00
```

Actual limits must follow business rules.

---

## TS-PAY-029 — Payment below minimum

**Priority:** P1

Expected:

Rejected.

---

## TS-PAY-030 — Payment exactly at minimum

**Priority:** P1

Expected:

Accepted.

---

## TS-PAY-031 — Payment minimum plus smallest currency unit

**Priority:** P2

Expected:

Accepted.

---

## TS-PAY-032 — Payment maximum minus smallest currency unit

**Priority:** P1

Expected:

Accepted.

---

## TS-PAY-033 — Payment exactly at maximum

**Priority:** P0

Expected:

Accepted if inclusive.

---

## TS-PAY-034 — Payment above maximum

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-035 — Zero payment

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-036 — Negative payment

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-037 — Blank payment amount

**Priority:** P1

Expected:

Required validation.

---

## TS-PAY-038 — Non-numeric payment amount

**Priority:** P1

Expected:

Rejected.

---

## TS-PAY-039 — Payment with too many decimal places

**Priority:** P1

Expected:

Rejected or rounded strictly according to documented financial rules.

---

## TS-PAY-040 — Extremely large numeric input

**Priority:** P1

Expected:

Safely rejected without overflow or system error.

---

# 10. Insufficient Funds Scenarios

## TS-PAY-041 — Payment below available balance

**Priority:** P0

Expected:

Allowed.

---

## TS-PAY-042 — Payment equals available balance with no fee

**Priority:** P0

Expected:

Allowed where no minimum retained balance exists.

---

## TS-PAY-043 — Payment equals available balance but fee applies

**Priority:** P0

Expected:

Rejected if total debit exceeds available funds.

---

## TS-PAY-044 — Payment exceeds available balance

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-045 — Current balance sufficient but available balance insufficient

**Priority:** P0

Expected:

Payment follows available-balance rules.

---

## TS-PAY-046 — Pending hold causes insufficient available balance

**Priority:** P0

Expected:

Payment rejected.

---

# 11. Payment Fee Scenarios

## TS-PAY-047 — Payment with zero fee

**Priority:** P1

Expected:

Only payment amount is debited.

---

## TS-PAY-048 — Payment with fixed fee

**Priority:** P0

Expected:

Correct fee applied exactly once.

---

## TS-PAY-049 — Payment with percentage fee

**Priority:** P0

Expected:

Correct calculated fee.

---

## TS-PAY-050 — Payment at fee-tier boundary

**Priority:** P0

Expected:

Correct tier is applied.

---

## TS-PAY-051 — Fee causes payment to exceed available balance

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-052 — Payment fee displayed before confirmation

**Priority:** P1

Expected:

Customer can see financial effect before submission.

---

## TS-PAY-053 — Total debit calculated correctly

**Priority:** P0

Expected:

```text
Payment Amount + Fee = Total Debit
```

---

## TS-PAY-054 — Fee deducted more than once

**Priority:** P0

Expected:

Must never occur.

---

# 12. Payment Confirmation Scenarios

## TS-PAY-055 — Confirmation displays correct source account

**Priority:** P0

Expected:

Correct account shown.

---

## TS-PAY-056 — Confirmation displays correct payee

**Priority:** P0

Expected:

Correct recipient or service provider shown.

---

## TS-PAY-057 — Confirmation displays correct bill reference

**Priority:** P0

Expected:

Exact bill/payment reference displayed.

---

## TS-PAY-058 — Confirmation displays correct amount

**Priority:** P0

Expected:

Exact value shown.

---

## TS-PAY-059 — Confirmation displays correct fee

**Priority:** P0

Expected:

Correct.

---

## TS-PAY-060 — Confirmation displays total debit

**Priority:** P0

Expected:

Correct financial total.

---

## TS-PAY-061 — Cancel from confirmation screen

**Priority:** P1

Expected:

No financial transaction occurs.

---

## TS-PAY-062 — Edit payment before confirmation

**Priority:** P1

Expected:

Updated details are shown before final submission.

---

# 13. Authorization Scenarios

## TS-PAY-063 — Unauthenticated user attempts payment

**Priority:** P0

Expected:

Authentication required.

---

## TS-PAY-064 — Expired session submits payment

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-065 — Session expires on confirmation screen

**Priority:** P0

Expected:

Payment does not execute without valid authentication.

---

## TS-PAY-066 — Customer modifies source account ID

**Priority:** P0

Expected:

Backend ownership validation prevents unauthorized debit.

---

## TS-PAY-067 — Customer modifies payee ID

**Priority:** P0

Expected:

Authorization and payee validation enforced.

---

## TS-PAY-068 — Customer modifies bill reference in request

**Priority:** P0

Expected:

Backend validates correct bill/payee relationship.

---

## TS-PAY-069 — Customer attempts restricted administrative payment action

**Priority:** P0

Expected:

Denied.

---

# 14. Duplicate Payment and Idempotency Scenarios

## TS-PAY-070 — Double-click Confirm

**Priority:** P0

Expected:

Only one payment executes.

---

## TS-PAY-071 — Repeated clicks during slow payment response

**Priority:** P0

Expected:

No duplicate debit.

---

## TS-PAY-072 — Refresh after payment submission

**Priority:** P0

Expected:

Payment is not executed again.

---

## TS-PAY-073 — Browser retry after network error

**Priority:** P0

Expected:

Duplicate payment prevented.

---

## TS-PAY-074 — Submit identical payment API request twice

**Priority:** P0

Expected:

Idempotency rules prevent unintended duplicate payment.

---

## TS-PAY-075 — Pay same bill twice using separate sessions

**Priority:** P0

Expected:

Already-paid bill cannot be settled twice.

---

## TS-PAY-076 — Two users attempt payment for same unique bill concurrently

**Priority:** P0

Where logically possible.

Expected:

Only valid permitted settlement occurs.

---

# 15. Payment Reference Scenarios

## TS-PAY-077 — Successful payment generates transaction/payment reference

**Priority:** P0

Expected:

Unique traceable reference.

---

## TS-PAY-078 — Payment reference unique across different payments

**Priority:** P0

Expected:

No duplication.

---

## TS-PAY-079 — Reference appears in payment history

**Priority:** P1

Expected:

Same reference displayed.

---

## TS-PAY-080 — API response reference matches UI reference

**Priority:** P1

Expected:

Consistent.

---

## TS-PAY-081 — Payment reference maps to correct database record

**Priority:** P0

Expected:

Traceability maintained.

---

# 16. Payment Status Scenarios

Possible statuses:

```text
CREATED
PENDING
PROCESSING
COMPLETED
FAILED
CANCELLED
REVERSED
```

---

## TS-PAY-082 — Immediate successful payment becomes COMPLETED

**Priority:** P0

Expected:

Correct final state.

---

## TS-PAY-083 — Delayed payment becomes PENDING

**Priority:** P1

Expected:

Displayed as pending, not completed.

---

## TS-PAY-084 — PENDING payment transitions to COMPLETED

**Priority:** P0

Expected:

Financial state updates correctly.

---

## TS-PAY-085 — PENDING payment transitions to FAILED

**Priority:** P0

Expected:

No incorrect final debit.

---

## TS-PAY-086 — CANCELLED payment cannot later complete

**Priority:** P0

Expected:

Invalid transition prevented.

---

## TS-PAY-087 — COMPLETED payment reverses only through controlled reversal process

**Priority:** P0

Expected:

Original payment remains historically traceable.

---

# 17. Failed Payment Scenarios

## TS-PAY-088 — Payment fails due to insufficient funds

**Priority:** P0

Expected:

Balance unchanged.

---

## TS-PAY-089 — Payment fails due to invalid payee

**Priority:** P0

Expected:

No financial effect.

---

## TS-PAY-090 — Payment fails because source account frozen

**Priority:** P0

Expected:

No debit.

---

## TS-PAY-091 — Payment provider returns rejection

**Priority:** P0

Expected:

Payment marked failed and financial state remains valid.

---

## TS-PAY-092 — Payment service returns server error

**Priority:** P0

Expected:

No false success.

---

## TS-PAY-093 — Database error during payment

**Priority:** P0

Expected:

Atomic rollback or safe recovery.

---

## TS-PAY-094 — Third-party provider timeout

**Priority:** P0

Expected:

System avoids ambiguous duplicate-prone outcome.

---

# 18. Payment Atomicity Scenarios

## TS-PAY-095 — Account debit occurs only if payment processing succeeds according to settlement model

**Priority:** P0

Expected:

No orphan debit.

---

## TS-PAY-096 — Provider success but local transaction persistence fails

**Priority:** P0

Expected:

System uses recovery/reconciliation mechanism and preserves traceability.

---

## TS-PAY-097 — Local debit succeeds but provider rejects payment

**Priority:** P0

Expected:

Funds are restored or appropriate reversal occurs.

---

## TS-PAY-098 — Payment record creation fails

**Priority:** P0

Expected:

No untraceable financial change.

---

## TS-PAY-099 — Notification fails after successful payment

**Priority:** P1

Expected:

Financial state remains correct.

---

# 19. Concurrency Scenarios

## TS-PAY-100 — Two simultaneous payments compete for same balance

**Priority:** P0

Example:

```text
Starting Balance = 1000
Payment A = 700
Payment B = 500
```

Expected:

Both cannot succeed if overdraft is not allowed.

---

## TS-PAY-101 — Payment and transfer occur concurrently

**Priority:** P0

Expected:

Available-balance rules remain correct.

---

## TS-PAY-102 — Same bill submitted from two tabs simultaneously

**Priority:** P0

Expected:

Bill not paid twice.

---

## TS-PAY-103 — Same payment submitted from separate devices

**Priority:** P0

Expected:

Duplicate protection remains effective.

---

## TS-PAY-104 — Account frozen during payment processing

**Priority:** P0

Expected:

Final outcome remains consistent and auditable.

---

# 20. Daily Payment Limit Scenarios

## TS-PAY-105 — Daily payment total below limit

**Priority:** P1

Expected:

Allowed.

---

## TS-PAY-106 — Daily payment total reaches exact limit

**Priority:** P0

Expected:

Handled according to boundary rule.

---

## TS-PAY-107 — New payment exceeds daily limit

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-108 — Multiple payments cumulatively exceed daily limit

**Priority:** P0

Expected:

Limit enforced cumulatively.

---

## TS-PAY-109 — Failed payment does not incorrectly consume daily limit

**Priority:** P0

Expected:

Limit accounting follows business rules.

---

# 21. Scheduled Payment Scenarios

## TS-PAY-110 — Schedule valid future payment

**Priority:** P1

Expected:

Payment saved with scheduled status.

---

## TS-PAY-111 — Schedule payment for past date

**Priority:** P1

Expected:

Rejected.

---

## TS-PAY-112 — Schedule payment for current date

**Priority:** P1

Expected:

Behavior follows business rules.

---

## TS-PAY-113 — Scheduled payment executes at correct time

**Priority:** P0

Expected:

Executes once.

---

## TS-PAY-114 — Scheduled payment has insufficient funds at execution time

**Priority:** P0

Expected:

Fails safely.

---

## TS-PAY-115 — Source account frozen before scheduled execution

**Priority:** P0

Expected:

Execution blocked.

---

## TS-PAY-116 — Payee disabled before scheduled execution

**Priority:** P0

Expected:

Payment rejected.

---

## TS-PAY-117 — Bill already paid before scheduled payment executes

**Priority:** P0

Expected:

Duplicate settlement prevented.

---

# 22. Scheduled Payment Cancellation Scenarios

## TS-PAY-118 — Cancel scheduled payment before execution

**Priority:** P1

Expected:

Status becomes cancelled.

---

## TS-PAY-119 — Cancel scheduled payment after processing starts

**Priority:** P0

Expected:

Behavior follows current transaction-state rules.

---

## TS-PAY-120 — Cancelled payment reaches scheduled time

**Priority:** P0

Expected:

No debit occurs.

---

## TS-PAY-121 — Cancellation generates audit record

**Priority:** P1

Expected:

Traceable.

---

# 23. Recurring Payment Scenarios

## TS-PAY-122 — Create valid recurring payment

**Priority:** P1

Expected:

Schedule stored correctly.

---

## TS-PAY-123 — Weekly recurring payment

**Priority:** P1

Expected:

Correct interval.

---

## TS-PAY-124 — Monthly recurring payment

**Priority:** P1

Expected:

Correct interval.

---

## TS-PAY-125 — Recurring payment with end date

**Priority:** P1

Expected:

Stops after configured end.

---

## TS-PAY-126 — Pause recurring payment

**Priority:** P1

Expected:

No payment while paused.

---

## TS-PAY-127 — Resume recurring payment

**Priority:** P1

Expected:

Future executions resume.

---

## TS-PAY-128 — Cancel recurring payment

**Priority:** P1

Expected:

Future payments stop.

---

## TS-PAY-129 — One recurring occurrence fails due to insufficient funds

**Priority:** P0

Expected:

Failure does not corrupt future schedule.

---

## TS-PAY-130 — Recurring payment executes twice for one cycle

**Priority:** P0

Expected:

Must never occur.

---

# 24. Time and Date Scenarios

## TS-PAY-131 — Scheduled payment at month end

**Priority:** P1

Expected:

Correct execution date.

---

## TS-PAY-132 — Scheduled payment across year boundary

**Priority:** P1

Expected:

Correct execution.

---

## TS-PAY-133 — Payment on leap day

**Priority:** P2

Expected:

Valid leap date handled correctly.

---

## TS-PAY-134 — Invalid leap date

**Priority:** P2

Expected:

Rejected.

---

## TS-PAY-135 — Timezone does not shift scheduled payment incorrectly

**Priority:** P0

Expected:

Correct absolute execution time.

---

# 25. Payment Reversal Scenarios

## TS-PAY-136 — Authorized payment reversal

**Priority:** P0

Expected:

Reversal follows business rules.

---

## TS-PAY-137 — Reversal restores account balance correctly

**Priority:** P0

Expected:

Correct financial restoration.

---

## TS-PAY-138 — Payment cannot be reversed twice

**Priority:** P0

Expected:

Duplicate reversal blocked.

---

## TS-PAY-139 — Unauthorized user attempts reversal

**Priority:** P0

Expected:

Denied.

---

## TS-PAY-140 — Reversal creates separate traceable transaction

**Priority:** P0

Expected:

Original payment remains in historical record.

---

## TS-PAY-141 — Reversal notification reflects actual reversal

**Priority:** P1

Expected:

Correct status communicated.

---

# 26. Transaction History Integration

## TS-PAY-142 — Successful payment appears in transaction history

**Priority:** P0

Expected:

Correct debit record.

---

## TS-PAY-143 — Pending payment appears with PENDING status

**Priority:** P1

Expected:

Not incorrectly displayed as completed.

---

## TS-PAY-144 — Failed payment appears correctly where failure history is retained

**Priority:** P1

Expected:

No false financial debit.

---

## TS-PAY-145 — Reversed payment appears with reversal entry

**Priority:** P0

Expected:

History remains auditable.

---

## TS-PAY-146 — Payment reference searchable in history

**Priority:** P1

Expected:

Correct payment returned.

---

# 27. Statement Integration Scenarios

## TS-PAY-147 — Completed payment appears on account statement

**Priority:** P0

Expected:

Correct:

* Amount
* Date
* Payee
* Reference
* Fee
* Balance effect

---

## TS-PAY-148 — Failed payment does not appear as completed debit

**Priority:** P0

Expected:

Statement remains financially accurate.

---

## TS-PAY-149 — Payment fee appears correctly on statement

**Priority:** P1

Expected:

Matches accounting model.

---

## TS-PAY-150 — Reversed payment represented correctly

**Priority:** P0

Expected:

Original and reversal are traceable.

---

# 28. API Consistency Scenarios

## TS-PAY-151 — Payment API result matches UI result

**Priority:** P0

Expected:

Status, amount, reference, and fee agree.

---

## TS-PAY-152 — Payment API status matches database

**Priority:** P0

Expected:

Consistent state.

---

## TS-PAY-153 — Balance API reflects successful payment

**Priority:** P0

Expected:

Correct updated balance.

---

## TS-PAY-154 — Failed payment API leaves account balance unchanged

**Priority:** P0

Expected:

No hidden debit.

---

# 29. Database Validation Scenarios

## TS-PAY-155 — Successful payment creates correct payment record

**Priority:** P0

Verify:

* Source account
* Payee
* Bill reference
* Amount
* Fee
* Status
* Transaction reference
* Timestamp

---

## TS-PAY-156 — Source balance updated correctly

**Priority:** P0

Expected:

Correct persisted balance.

---

## TS-PAY-157 — Failed payment does not persist invalid debit

**Priority:** P0

Expected:

Balance unchanged or valid reversal exists.

---

## TS-PAY-158 — Bill state updated correctly after successful payment

**Priority:** P0

Expected:

Settled state persisted.

---

## TS-PAY-159 — Referential integrity maintained

**Priority:** P0

Expected:

No orphan payee, payment, or transaction relationships.

---

# 30. Error Handling Scenarios

## TS-PAY-160 — Payment service unavailable

**Priority:** P0

Expected:

Safe failure message and no debit.

---

## TS-PAY-161 — Payee lookup service unavailable

**Priority:** P1

Expected:

Payment cannot proceed without required validation.

---

## TS-PAY-162 — Network disconnect before confirmation

**Priority:** P1

Expected:

No payment.

---

## TS-PAY-163 — Network disconnect immediately after confirmation

**Priority:** P0

Expected:

Actual payment state can be determined without unsafe blind resubmission.

---

## TS-PAY-164 — Slow provider response

**Priority:** P0

Expected:

Duplicate clicks prevented and processing status shown.

---

## TS-PAY-165 — Malformed backend response

**Priority:** P1

Expected:

No false success.

---

# 31. Notification Scenarios

## TS-PAY-166 — Successful payment notification

**Priority:** P1

Expected:

Correct amount, payee, and status.

---

## TS-PAY-167 — Failed payment notification

**Priority:** P1

Expected:

Clearly indicates failure.

---

## TS-PAY-168 — Scheduled payment execution notification

**Priority:** P2

Expected:

Notification matches final state.

---

## TS-PAY-169 — Recurring payment execution notification

**Priority:** P2

Expected:

Correct occurrence represented.

---

## TS-PAY-170 — Reversal notification

**Priority:** P1

Expected:

Correct reversal details.

---

## TS-PAY-171 — Failed payment does not generate success notification

**Priority:** P0

Expected:

No misleading confirmation.

---

# 32. Audit Scenarios

## TS-PAY-172 — Successful payment generates audit record

**Priority:** P0

Expected:

Record includes:

* Customer
* Account
* Payee
* Amount
* Reference
* Timestamp
* Result

---

## TS-PAY-173 — Failed payment is auditable where required

**Priority:** P1

Expected:

Failure traceable.

---

## TS-PAY-174 — Scheduled payment creation audited

**Priority:** P1

Expected:

Traceable.

---

## TS-PAY-175 — Scheduled payment cancellation audited

**Priority:** P1

Expected:

Traceable.

---

## TS-PAY-176 — Payment reversal audited

**Priority:** P0

Expected:

Original payment, reversing actor, reason, and result recorded.

---

# 33. Security Scenarios

## TS-PAY-177 — Manipulate payment amount client-side

**Priority:** P0

Expected:

Backend independently validates amount.

---

## TS-PAY-178 — Manipulate payment fee client-side

**Priority:** P0

Expected:

Backend calculates authoritative fee.

---

## TS-PAY-179 — Manipulate source account ID

**Priority:** P0

Expected:

Ownership enforced.

---

## TS-PAY-180 — Manipulate payee ID

**Priority:** P0

Expected:

Payee validation/authorization enforced.

---

## TS-PAY-181 — Manipulate bill reference after loading valid bill

**Priority:** P0

Expected:

Backend validates exact requested bill.

---

## TS-PAY-182 — Replay completed payment request

**Priority:** P0

Expected:

No duplicate payment.

---

## TS-PAY-183 — SQL-like input in payment description/reference

**Priority:** P1

Expected:

Safely handled.

---

## TS-PAY-184 — Script-like input in payment notes

**Priority:** P1

Expected:

No script execution.

---

# 34. Payment Notes and Descriptions

## TS-PAY-185 — Valid payment note

**Priority:** P2

Expected:

Saved correctly where supported.

---

## TS-PAY-186 — Empty optional note

**Priority:** P3

Expected:

Payment can proceed.

---

## TS-PAY-187 — Note at maximum length

**Priority:** P2

Expected:

Accepted.

---

## TS-PAY-188 — Note over maximum length

**Priority:** P2

Expected:

Rejected.

---

## TS-PAY-189 — Arabic payment note

**Priority:** P2

Expected:

Unicode retained correctly.

---

# 35. Search and Filtering Scenarios

## TS-PAY-190 — Search payment by reference

**Priority:** P1

Expected:

Correct payment returned.

---

## TS-PAY-191 — Filter payments by status

**Priority:** P2

Expected:

Matching records only.

---

## TS-PAY-192 — Filter payments by date range

**Priority:** P1

Expected:

Correct results.

---

## TS-PAY-193 — Filter payments by payee

**Priority:** P2

Expected:

Correct results.

---

## TS-PAY-194 — Filter by amount range

**Priority:** P2

Expected:

Correct payments shown.

---

# 36. Cross-Browser Scenarios

## TS-PAY-195 — Payment flow in Chrome

**Priority:** P0

Expected:

Works correctly.

---

## TS-PAY-196 — Payment flow in Edge

**Priority:** P0

Expected:

Works correctly.

---

## TS-PAY-197 — Payment flow in Firefox

**Priority:** P0

Expected:

Works correctly.

---

## TS-PAY-198 — Payment confirmation using WebKit

**Priority:** P1

Expected:

No critical browser-specific issue.

---

# 37. Responsive Scenarios

## TS-PAY-199 — Payment flow on desktop

**Priority:** P1

Expected:

All important payment data visible.

---

## TS-PAY-200 — Payment flow on tablet

**Priority:** P1

Expected:

Usable layout.

---

## TS-PAY-201 — Payment flow on mobile

**Priority:** P0

Expected:

Source, payee, amount, fee, bill reference, and confirmation remain clearly visible.

---

## TS-PAY-202 — Long payee name on mobile

**Priority:** P1

Expected:

Payee remains distinguishable.

---

## TS-PAY-203 — Validation errors on mobile

**Priority:** P1

Expected:

Fully visible.

---

# 38. Accessibility and Usability Scenarios

## TS-PAY-204 — Payment form supports keyboard navigation

**Priority:** P2

Expected:

Logical focus order.

---

## TS-PAY-205 — Amount field clearly displays currency

**Priority:** P1

Expected:

No financial ambiguity.

---

## TS-PAY-206 — Payee identity clearly visible on confirmation

**Priority:** P0

Expected:

Wrong-payee risk minimized.

---

## TS-PAY-207 — Fee and total debit clearly distinguished

**Priority:** P0

Expected:

Customer understands exact charge.

---

## TS-PAY-208 — Success page shows payment reference

**Priority:** P1

Expected:

Traceable reference visible.

---

## TS-PAY-209 — Failure page explicitly indicates payment did not complete

**Priority:** P0

Expected:

No ambiguity about whether money moved.

---

# 39. Boundary Scenarios

## TS-PAY-210 — Minimum amount minus 0.01

**Priority:** P1

Expected:

Rejected.

---

## TS-PAY-211 — Exact minimum

**Priority:** P1

Expected:

Accepted.

---

## TS-PAY-212 — Minimum plus 0.01

**Priority:** P2

Expected:

Accepted.

---

## TS-PAY-213 — Maximum minus 0.01

**Priority:** P1

Expected:

Accepted.

---

## TS-PAY-214 — Exact maximum

**Priority:** P0

Expected:

Handled according to limit rule.

---

## TS-PAY-215 — Maximum plus 0.01

**Priority:** P0

Expected:

Rejected.

---

## TS-PAY-216 — Available balance minus 0.01

**Priority:** P1

Expected:

Accepted if fees allow.

---

## TS-PAY-217 — Exact available balance

**Priority:** P0

Expected:

Handled according to fee and reserve rules.

---

## TS-PAY-218 — Available balance plus 0.01

**Priority:** P0

Expected:

Rejected.

---

# 40. End-to-End Payment Scenarios

## TS-PAY-219 — Complete bill-payment journey

**Priority:** P0

Flow:

```text
Login
→ Select Payments
→ Select Source Account
→ Select Payee
→ Enter Bill Reference
→ Retrieve Bill
→ Verify Amount
→ Review Fee
→ Confirm Payment
→ Verify Success
→ Verify Account Balance
→ Verify Transaction History
→ Verify Bill Status
→ Verify Notification
```

---

## TS-PAY-220 — Insufficient funds payment journey

**Priority:** P0

Flow:

```text
Login
→ Select Low-Balance Account
→ Select Valid Bill
→ Attempt Payment Above Available Balance
→ Payment Rejected
→ Verify Balance Unchanged
→ Verify Bill Remains Unpaid
```

---

## TS-PAY-221 — Duplicate-payment prevention journey

**Priority:** P0

Flow:

```text
Prepare Valid Payment
→ Double Click Confirm
→ Wait for Completion
→ Verify One Payment
→ Verify One Debit
→ Verify Bill Paid Once
```

---

## TS-PAY-222 — Scheduled payment journey

**Priority:** P0

Flow:

```text
Create Scheduled Payment
→ Verify Scheduled State
→ Reach Execution Time
→ Verify Payment Executes
→ Verify Balance
→ Verify Bill State
→ Verify History
```

---

## TS-PAY-223 — Cancelled scheduled payment journey

**Priority:** P0

Flow:

```text
Create Scheduled Payment
→ Cancel
→ Verify CANCELLED
→ Reach Scheduled Time
→ Verify No Debit
→ Verify Bill Remains Unpaid
```

---

## TS-PAY-224 — Payment reversal journey

**Priority:** P0

Flow:

```text
Complete Payment
→ Authorized Reversal
→ Verify Reversal Transaction
→ Verify Balance Restoration
→ Verify Original History Preserved
→ Verify Audit Record
```

---

## TS-PAY-225 — Concurrent payment protection

**Priority:** P0

Flow:

```text
Balance = 1000
→ Payment A = 700
→ Payment B = 500
→ Submit Concurrently
→ Verify Combined Successful Debit Does Not Exceed Balance
→ Verify Final Balance
→ Verify Payment States
```

---

# 41. Critical Smoke Scenarios

Payment smoke coverage should include:

```text
TS-PAY-001 — Valid bill payment
TS-PAY-008 — Active source account
TS-PAY-015 — Valid payee
TS-PAY-021 — Valid bill
TS-PAY-041 — Sufficient funds
TS-PAY-055 — Correct confirmation
TS-PAY-077 — Reference generated
TS-PAY-082 — Completed status
TS-PAY-142 — Payment appears in history
```

---

# 42. Critical Regression Scenarios

Always prioritize:

* Valid bill payment
* Merchant payment
* Account ownership
* Payee validation
* Bill validation
* Insufficient funds
* Fees
* Limits
* Frozen/restricted accounts
* Duplicate payment
* Idempotency
* Atomicity
* Provider failure
* Concurrency
* Scheduled payments
* Recurring payments
* Cancellation
* Reversal
* Bill status
* Transaction history
* Statements
* UI/API/database consistency
* Authorization

---

# 43. Automation Candidates

Strong UI candidates for Selenium, Cypress, and Playwright:

* Valid payment
* Invalid bill
* Already-paid bill
* Insufficient funds
* Payment limits
* Frozen source account
* Confirmation details
* Duplicate submission
* Scheduled payment
* Cancellation
* Transaction-history validation

---

# 44. API Automation Candidates

Postman and REST Assured should later cover:

* Create payment
* Invalid payee
* Invalid bill
* Already-paid bill
* Insufficient funds
* Minimum/maximum payment
* Fees
* Authorization
* Duplicate request
* Idempotency
* Scheduled payments
* Cancellation
* Status transitions
* Reversal
* Concurrent submissions

---

# 45. SQL Validation Candidates

Database testing should validate:

* Source account balance
* Payment amount
* Payment fee
* Payee relationship
* Bill status
* Payment status
* Transaction reference
* Scheduled-payment state
* Recurring-payment state
* Reversal records
* Audit records

---

# 46. Performance Testing Candidates

JMeter should later cover:

* High-volume payment submission
* Bill lookup under load
* Concurrent payments
* Duplicate-payment protection under latency
* Payment throughput
* Payment response times
* Provider timeout handling
* Error rate

Financial integrity must remain correct under load.

---

# 47. BDD Candidates

Example:

```gherkin
Feature: Bill payment

Scenario: Customer successfully pays a valid outstanding bill
  Given the customer is logged in
  And the source account is active
  And the source account has sufficient available balance
  And the bill is valid and unpaid
  When the customer pays the bill
  Then the payment should complete successfully
  And the source account balance should decrease correctly
  And the bill should be marked as paid
  And a completed payment transaction should be recorded
```

Duplicate-protection example:

```gherkin
Scenario: The same bill cannot be paid twice
  Given a bill has already been paid successfully
  When the customer attempts to pay the same bill again
  Then the payment should be rejected
  And the source account should not be debited again
```

---

# 48. Risk Traceability

Major related risks include:

```text
RISK-001 — Incorrect account balance
RISK-003 — Duplicate financial transaction
RISK-009 — Frozen account can transact
RISK-010 — Incorrect fee calculation
RISK-013 — Concurrent transactions corrupt balance
RISK-026 — Failed payment modifies balance
RISK-027 — Duplicate payment
RISK-030 — Unauthorized API request
RISK-037 — Frontend-only validation
RISK-039 — API/database inconsistency
RISK-042 — Slow response causes duplicate submission
RISK-048 — UI reports false success
```

---

# 49. Payment Coverage Summary

This catalog covers:

* Bill payments
* Merchant payments
* Payees
* Bill validation
* Source accounts
* Amount boundaries
* Available balance
* Fees
* Confirmation
* Authorization
* Duplicate prevention
* Idempotency
* References
* Status transitions
* Failures
* Atomicity
* Concurrency
* Limits
* Scheduled payments
* Recurring payments
* Cancellation
* Timezones
* Reversals
* Transaction history
* Statements
* API consistency
* Database consistency
* Error handling
* Notifications
* Audit
* Security
* Search/filtering
* Responsive behavior
* Cross-browser behavior
* Accessibility
* End-to-end workflows

---

# 50. Final Payment Testing Principle

A payment should not be considered successful merely because the UI displays a success message.

For each critical payment, QA should verify:

```text
Was the customer authorized?

Was the source account valid?

Was the payee valid?

Was the bill valid and unpaid?

Was the amount valid?

Was sufficient balance available?

Was the fee correct?

Did exactly one payment execute?

Was the source balance updated correctly?

Was the bill state updated correctly?

Did failure leave financial state unchanged?

Do UI, API, database, transaction history, and statements agree?

Is the payment fully traceable and auditable?
```

Payment testing must verify the entire financial result, not just the frontend workflow.
