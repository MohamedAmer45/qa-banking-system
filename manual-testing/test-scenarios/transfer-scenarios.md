# Banking System — Transfer Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Transfers                      |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for money-transfer functionality within the Banking System.

Transfers are one of the most critical areas of the application because defects may cause:

* Incorrect movement of money
* Duplicate transactions
* Incorrect balances
* Unauthorized transfers
* Transfers to wrong beneficiaries
* Limit violations
* Fee-calculation errors
* Partial transaction completion
* Concurrency defects
* Incorrect scheduled or recurring execution
* Failed-transaction balance corruption

Transfer testing must therefore validate both the user workflow and the resulting financial state.

---

# 3. Scope

Transfer testing includes:

* Own-account transfers
* Transfers to another customer
* Beneficiary transfers
* External transfers
* Immediate transfers
* Scheduled transfers
* Recurring transfers
* Transfer limits
* Available-balance validation
* Fees
* Transfer confirmation
* Transaction references
* Duplicate prevention
* Idempotency
* Pending transfers
* Failed transfers
* Cancelled transfers
* Reversed transfers
* Account-state validation
* Beneficiary-state validation
* Concurrency
* Authorization
* Audit logging
* Notifications
* Data consistency

---

# 4. Scenario Naming Convention

Transfer scenarios use:

```text
TS-TRF-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Valid Transfer Scenarios

## TS-TRF-001 — Transfer valid amount to active beneficiary

**Priority:** P0

Expected:

* Transfer succeeds.
* Source account is debited correctly.
* Destination is credited correctly.
* Correct transaction record is created.
* Transfer reference is generated.

---

## TS-TRF-002 — Transfer between customer's own accounts

**Priority:** P0

Expected:

Source balance decreases and destination balance increases by the correct amount.

---

## TS-TRF-003 — Transfer to another customer within same bank

**Priority:** P0

Expected:

Internal transfer completes successfully.

---

## TS-TRF-004 — Transfer to valid external beneficiary

**Priority:** P0

Execute where external transfers exist.

Expected:

Transfer follows external-transfer rules and enters correct status.

---

## TS-TRF-005 — Transfer smallest valid amount

**Priority:** P1

Expected:

Transfer accepted if all other rules pass.

---

## TS-TRF-006 — Transfer normal mid-range amount

**Priority:** P1

Expected:

Transfer completes successfully.

---

## TS-TRF-007 — Transfer exact maximum per-transaction amount

**Priority:** P0

Expected:

Accepted if maximum is inclusive.

---

## TS-TRF-008 — Transfer using source account with high balance

**Priority:** P1

Expected:

Transfer succeeds without financial precision issues.

---

# 6. Source Account Scenarios

## TS-TRF-009 — Select valid active source account

**Priority:** P0

Expected:

Account can be used.

---

## TS-TRF-010 — Use frozen source account

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-TRF-011 — Use restricted source account

**Priority:** P0

Expected:

Transfer rejected when restriction blocks transfers.

---

## TS-TRF-012 — Use suspended source account

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-TRF-013 — Use closed source account

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-TRF-014 — Customer attempts transfer from another customer's account

**Priority:** P0

Expected:

Authorization failure.

---

## TS-TRF-015 — Source account becomes frozen before final confirmation

**Priority:** P0

Expected:

Final submission revalidates account state and rejects transfer.

---

## TS-TRF-016 — Source account closes while transfer page is open

**Priority:** P0

Expected:

Transfer rejected.

---

# 7. Destination Account Scenarios

## TS-TRF-017 — Transfer to valid active destination account

**Priority:** P0

Expected:

Accepted.

---

## TS-TRF-018 — Transfer to nonexistent destination account

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-019 — Transfer to closed destination account

**Priority:** P0

Expected:

Rejected where closed accounts cannot receive funds.

---

## TS-TRF-020 — Transfer to frozen destination account

**Priority:** P1

Expected:

Behavior follows destination-account rules.

---

## TS-TRF-021 — Transfer to restricted destination account

**Priority:** P1

Expected:

Behavior follows restriction rules.

---

## TS-TRF-022 — Destination account becomes invalid before submission

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-TRF-023 — Transfer to same account as source

**Priority:** P1

Expected:

Rejected or prevented.

---

# 8. Beneficiary Scenarios

## TS-TRF-024 — Transfer to active verified beneficiary

**Priority:** P0

Expected:

Transfer allowed.

---

## TS-TRF-025 — Transfer to unverified beneficiary

**Priority:** P0

Expected:

Rejected where verification is required.

---

## TS-TRF-026 — Transfer to beneficiary pending activation

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-027 — Transfer to disabled beneficiary

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-028 — Transfer to deleted beneficiary using stale page

**Priority:** P0

Expected:

Server rejects transfer.

---

## TS-TRF-029 — Use another customer's beneficiary ID

**Priority:** P0

Expected:

Authorization failure.

---

## TS-TRF-030 — Beneficiary deleted before transfer confirmation

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-TRF-031 — Beneficiary account changed or invalidated before confirmation

**Priority:** P0

Expected:

Current state is validated server-side.

---

# 9. Transfer Amount Validation Scenarios

Assume example limits:

```text
Minimum transfer = 1.00
Maximum transfer = 100,000.00
```

Actual values should follow business rules.

---

## TS-TRF-032 — Transfer amount below minimum

**Priority:** P1

Example:

```text
0.99
```

Expected:

Rejected.

---

## TS-TRF-033 — Transfer exact minimum

**Priority:** P1

Expected:

Accepted.

---

## TS-TRF-034 — Transfer minimum plus one smallest currency unit

**Priority:** P2

Expected:

Accepted.

---

## TS-TRF-035 — Transfer maximum minus 0.01

**Priority:** P1

Expected:

Accepted.

---

## TS-TRF-036 — Transfer exact maximum

**Priority:** P0

Expected:

Accepted if inclusive.

---

## TS-TRF-037 — Transfer maximum plus 0.01

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-038 — Transfer amount zero

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-039 — Transfer negative amount

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-040 — Transfer non-numeric amount

**Priority:** P1

Expected:

Rejected.

---

## TS-TRF-041 — Transfer with blank amount

**Priority:** P1

Expected:

Required-field validation.

---

## TS-TRF-042 — Transfer amount with spaces only

**Priority:** P1

Expected:

Rejected.

---

## TS-TRF-043 — Transfer amount with more decimal places than supported

**Priority:** P1

Example:

```text
100.999
```

Expected:

Rejected or rounded strictly according to documented rules.

---

## TS-TRF-044 — Transfer extremely large numeric value

**Priority:** P1

Expected:

Safely rejected without overflow.

---

# 10. Available Balance Scenarios

## TS-TRF-045 — Transfer below available balance

**Priority:** P0

Expected:

Accepted.

---

## TS-TRF-046 — Transfer equal to available balance with no fee

**Priority:** P0

Expected:

Accepted where no minimum retained balance applies.

---

## TS-TRF-047 — Transfer equal to available balance when fee applies

**Priority:** P0

Expected:

Rejected if total debit exceeds available funds.

---

## TS-TRF-048 — Transfer amount greater than available balance

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-049 — Transfer amount plus fee exceeds available balance

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-050 — Current balance sufficient but available balance insufficient

**Priority:** P0

Expected:

Transfer should follow available-balance rules.

---

## TS-TRF-051 — Pending hold reduces available balance below requested transfer

**Priority:** P0

Expected:

Transfer rejected.

---

# 11. Fee Scenarios

## TS-TRF-052 — Transfer with no fee

**Priority:** P1

Expected:

Only transfer amount is debited.

---

## TS-TRF-053 — Transfer with fixed fee

**Priority:** P0

Expected:

Correct fixed fee applied once.

---

## TS-TRF-054 — Transfer with percentage fee

**Priority:** P0

Expected:

Correct percentage calculation.

---

## TS-TRF-055 — Transfer at fee-tier boundary

**Priority:** P0

Expected:

Correct fee tier used.

---

## TS-TRF-056 — Fee causes insufficient funds

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-TRF-057 — Fee deducted twice

**Priority:** P0

Expected:

Must never occur.

---

## TS-TRF-058 — Fee shown on confirmation screen

**Priority:** P1

Expected:

Customer sees correct fee before final submission.

---

## TS-TRF-059 — Total debit shown correctly

**Priority:** P0

Expected:

```text
Transfer Amount + Fee = Total Debit
```

---

# 12. Transfer Confirmation Scenarios

## TS-TRF-060 — Confirmation screen displays correct source account

**Priority:** P0

Expected:

Correct source identified.

---

## TS-TRF-061 — Confirmation screen displays correct beneficiary

**Priority:** P0

Expected:

Correct recipient shown clearly.

---

## TS-TRF-062 — Confirmation screen displays correct transfer amount

**Priority:** P0

Expected:

Exact amount shown.

---

## TS-TRF-063 — Confirmation screen displays correct fee

**Priority:** P0

Expected:

Fee matches business rules.

---

## TS-TRF-064 — Confirmation screen displays total debit

**Priority:** P0

Expected:

Correct total shown.

---

## TS-TRF-065 — Cancel transfer from confirmation screen

**Priority:** P1

Expected:

No financial transaction occurs.

---

## TS-TRF-066 — Modify transfer before final confirmation

**Priority:** P1

Expected:

Confirmation refreshes to show updated values.

---

## TS-TRF-067 — Browser Back from confirmation screen

**Priority:** P1

Expected:

No duplicate or unintended transfer.

---

# 13. Transfer Authentication / Authorization

## TS-TRF-068 — Unauthenticated user attempts transfer

**Priority:** P0

Expected:

Authentication required.

---

## TS-TRF-069 — Expired session submits transfer

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-TRF-070 — Session expires on confirmation page

**Priority:** P0

Expected:

Transfer cannot proceed without valid authentication.

---

## TS-TRF-071 — Customer modifies source account ID in request

**Priority:** P0

Expected:

Backend verifies account ownership.

---

## TS-TRF-072 — Customer modifies beneficiary ID in request

**Priority:** P0

Expected:

Authorization failure if beneficiary is not owned.

---

## TS-TRF-073 — Customer modifies destination account ID in request

**Priority:** P0

Expected:

Backend validates destination consistency.

---

## TS-TRF-074 — Customer attempts admin-only transfer action

**Priority:** P0

Expected:

Denied.

---

# 14. Duplicate Submission and Idempotency

## TS-TRF-075 — Double-click Confirm once transfer is submitted

**Priority:** P0

Expected:

Only one transfer executes.

---

## TS-TRF-076 — Click Confirm repeatedly during slow response

**Priority:** P0

Expected:

No duplicate transfer.

---

## TS-TRF-077 — Refresh page after submitting transfer

**Priority:** P0

Expected:

Transfer is not executed again.

---

## TS-TRF-078 — Browser retry after network interruption

**Priority:** P0

Expected:

Duplicate processing prevented.

---

## TS-TRF-079 — Submit identical API request twice

**Priority:** P0

Expected:

Behavior follows idempotency design and prevents unintended duplication.

---

## TS-TRF-080 — Submit same idempotency key twice

**Priority:** P0

Where implemented.

Expected:

Same financial operation must not execute twice.

---

## TS-TRF-081 — Submit same payload with different idempotency key

**Priority:** P1

Expected:

System behavior follows defined transaction rules.

---

# 15. Transaction Reference Scenarios

## TS-TRF-082 — Successful transfer generates reference ID

**Priority:** P0

Expected:

Reference is present and traceable.

---

## TS-TRF-083 — Transaction reference is unique

**Priority:** P0

Expected:

No duplicate references across distinct transfers.

---

## TS-TRF-084 — Transfer reference appears in transaction history

**Priority:** P1

Expected:

Same reference appears consistently.

---

## TS-TRF-085 — Reference returned by API matches UI

**Priority:** P1

Expected:

Consistent identifier.

---

## TS-TRF-086 — Reference maps to correct database transaction

**Priority:** P0

Expected:

Correct financial record.

---

# 16. Transaction Status Scenarios

Possible states may include:

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

## TS-TRF-087 — Successful immediate transfer reaches COMPLETED

**Priority:** P0

Expected:

Final completed state.

---

## TS-TRF-088 — Delayed transfer enters PENDING

**Priority:** P1

Expected:

Pending state displayed correctly.

---

## TS-TRF-089 — PENDING transfer transitions to COMPLETED

**Priority:** P0

Expected:

Balances update according to design.

---

## TS-TRF-090 — PENDING transfer transitions to FAILED

**Priority:** P0

Expected:

Financial effect is correctly rolled back or never finalized.

---

## TS-TRF-091 — FAILED transfer cannot later appear as successful without valid retry/recovery process

**Priority:** P0

Expected:

Status history remains valid.

---

## TS-TRF-092 — CANCELLED transfer cannot transition to COMPLETED

**Priority:** P0

Expected:

Invalid state transition rejected.

---

## TS-TRF-093 — COMPLETED transfer transitions to REVERSED only through valid reversal process

**Priority:** P0

Expected:

Controlled state transition.

---

# 17. Failed Transfer Scenarios

## TS-TRF-094 — Transfer fails because of insufficient funds

**Priority:** P0

Expected:

Source balance unchanged.

---

## TS-TRF-095 — Transfer fails because beneficiary is invalid

**Priority:** P0

Expected:

No financial effect.

---

## TS-TRF-096 — Transfer fails because source account is frozen

**Priority:** P0

Expected:

No debit.

---

## TS-TRF-097 — Transfer fails because destination account is closed

**Priority:** P0

Expected:

No debit.

---

## TS-TRF-098 — Transfer backend returns server error

**Priority:** P0

Expected:

No false success and no partial financial effect.

---

## TS-TRF-099 — Database error during transfer

**Priority:** P0

Expected:

Transaction either completes fully or rolls back fully.

---

## TS-TRF-100 — External service timeout during transfer

**Priority:** P0

Expected:

Final state must remain deterministic and recoverable.

---

# 18. Transaction Atomicity Scenarios

## TS-TRF-101 — Source debited only if destination is successfully credited

**Priority:** P0

Expected:

No partial transfer.

---

## TS-TRF-102 — Destination credit fails after debit attempt

**Priority:** P0

Expected:

Source debit rolls back or transfer is recovered safely.

---

## TS-TRF-103 — Transaction record creation fails

**Priority:** P0

Expected:

Financial state must not become untraceable.

---

## TS-TRF-104 — Audit logging failure during transfer

**Priority:** P0

Expected:

Behavior follows banking integrity rules; critical financial action should remain traceable.

---

## TS-TRF-105 — Notification failure after successful transfer

**Priority:** P1

Expected:

Financial transaction remains correct even if notification fails.

---

# 19. Concurrency Scenarios

## TS-TRF-106 — Two simultaneous transfers use same balance

**Priority:** P0

Example:

```text
Starting Balance = 1000
Transfer A = 800
Transfer B = 500
```

Expected:

Both cannot succeed where overdraft is not allowed.

---

## TS-TRF-107 — Transfer and payment use same available balance concurrently

**Priority:** P0

Expected:

Financial integrity preserved.

---

## TS-TRF-108 — Two tabs submit different transfers simultaneously

**Priority:** P0

Expected:

Correct available-balance validation.

---

## TS-TRF-109 — Same transfer submitted simultaneously from two sessions

**Priority:** P0

Expected:

Duplicate prevention and idempotency rules hold.

---

## TS-TRF-110 — Incoming credit arrives while outgoing transfer is processed

**Priority:** P0

Expected:

Final balance accurately reflects both operations.

---

## TS-TRF-111 — Account freeze occurs during transfer processing

**Priority:** P0

Expected:

Transaction outcome is consistent and auditable.

---

# 20. Daily Limit Scenarios

Assume a configured daily limit.

## TS-TRF-112 — Daily total below limit

**Priority:** P1

Expected:

Transfer allowed.

---

## TS-TRF-113 — Daily total exactly reaches limit

**Priority:** P0

Expected:

Handled according to inclusive business rule.

---

## TS-TRF-114 — New transfer would exceed daily limit

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-115 — Multiple small transfers cumulatively exceed daily limit

**Priority:** P0

Expected:

Limit enforced on cumulative total.

---

## TS-TRF-116 — Failed transfers do not incorrectly consume daily limit

**Priority:** P0

Expected:

Only qualifying transactions count according to business rules.

---

## TS-TRF-117 — Reversed transfer effect on daily limit

**Priority:** P1

Expected:

Behavior follows defined accounting rules.

---

# 21. Monthly Limit Scenarios

## TS-TRF-118 — Monthly total below limit

**Priority:** P1

Expected:

Allowed.

---

## TS-TRF-119 — Monthly total exactly at limit

**Priority:** P1

Expected:

Handled according to policy.

---

## TS-TRF-120 — Transfer exceeds monthly limit

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-121 — Month boundary resets monthly limit correctly

**Priority:** P1

Expected:

Correct period calculation.

---

# 22. Scheduled Transfer Scenarios

## TS-TRF-122 — Create valid future scheduled transfer

**Priority:** P1

Expected:

Transfer enters SCHEDULED state.

---

## TS-TRF-123 — Schedule transfer for today

**Priority:** P1

Expected:

Behavior follows scheduling rules.

---

## TS-TRF-124 — Schedule transfer for past date

**Priority:** P1

Expected:

Rejected.

---

## TS-TRF-125 — Schedule transfer far in future

**Priority:** P2

Expected:

Accepted if within allowed range.

---

## TS-TRF-126 — Scheduled transfer executes at correct date/time

**Priority:** P0

Expected:

Executes once at configured time.

---

## TS-TRF-127 — Scheduled transfer with insufficient funds at execution time

**Priority:** P0

Expected:

Fails safely according to business rules.

---

## TS-TRF-128 — Scheduled transfer source account frozen before execution

**Priority:** P0

Expected:

Execution blocked.

---

## TS-TRF-129 — Scheduled transfer beneficiary deleted before execution

**Priority:** P0

Expected:

Execution blocked.

---

## TS-TRF-130 — Scheduled transfer destination account closed before execution

**Priority:** P0

Expected:

Execution blocked.

---

# 23. Scheduled Transfer Cancellation

## TS-TRF-131 — Cancel future scheduled transfer

**Priority:** P1

Expected:

Status becomes CANCELLED.

---

## TS-TRF-132 — Cancel scheduled transfer before execution cutoff

**Priority:** P1

Expected:

Cancelled successfully.

---

## TS-TRF-133 — Cancel transfer after execution has started

**Priority:** P0

Expected:

Behavior follows transaction state rules.

---

## TS-TRF-134 — Cancelled scheduled transfer reaches scheduled date

**Priority:** P0

Expected:

No financial transaction occurs.

---

## TS-TRF-135 — Cancellation generates audit record

**Priority:** P1

Expected:

Cancellation traceable.

---

# 24. Recurring Transfer Scenarios

## TS-TRF-136 — Create valid recurring transfer

**Priority:** P1

Expected:

Schedule saved correctly.

---

## TS-TRF-137 — Monthly recurring transfer

**Priority:** P1

Expected:

Executes once per scheduled cycle.

---

## TS-TRF-138 — Weekly recurring transfer

**Priority:** P1

Expected:

Correct recurrence interval.

---

## TS-TRF-139 — Recurring transfer with end date

**Priority:** P1

Expected:

Stops after configured end.

---

## TS-TRF-140 — Recurring transfer without end date

**Priority:** P2

Expected:

Continues until manually stopped where supported.

---

## TS-TRF-141 — Pause recurring transfer

**Priority:** P1

Expected:

No execution while paused.

---

## TS-TRF-142 — Resume paused recurring transfer

**Priority:** P1

Expected:

Future executions resume correctly.

---

## TS-TRF-143 — Cancel recurring transfer

**Priority:** P1

Expected:

No future executions.

---

## TS-TRF-144 — Insufficient funds during one recurring execution

**Priority:** P0

Expected:

That occurrence follows failure policy without corrupting future schedule.

---

## TS-TRF-145 — Duplicate recurring execution

**Priority:** P0

Expected:

Must not occur.

---

# 25. Time and Timezone Scenarios

## TS-TRF-146 — Scheduled transfer around midnight

**Priority:** P1

Expected:

Correct execution date.

---

## TS-TRF-147 — Scheduled transfer across month boundary

**Priority:** P1

Expected:

Correct date calculation.

---

## TS-TRF-148 — Scheduled transfer across year boundary

**Priority:** P1

Expected:

Correct date calculation.

---

## TS-TRF-149 — Leap-day scheduled transfer

**Priority:** P2

Expected:

Valid leap date handled correctly.

---

## TS-TRF-150 — Invalid leap date

**Priority:** P2

Expected:

Rejected.

---

## TS-TRF-151 — Timezone difference does not execute scheduled transfer at wrong absolute time

**Priority:** P0

Expected:

Consistent timezone handling.

---

# 26. Reversal Scenarios

## TS-TRF-152 — Authorized reversal of completed transfer

**Priority:** P0

Expected:

Reversal follows defined business rules.

---

## TS-TRF-153 — Reversal restores source funds correctly

**Priority:** P0

Expected:

Correct financial restoration.

---

## TS-TRF-154 — Destination balance adjusted correctly after reversal

**Priority:** P0

Expected:

Accounting remains balanced.

---

## TS-TRF-155 — Transfer cannot be reversed twice

**Priority:** P0

Expected:

Duplicate reversal prevented.

---

## TS-TRF-156 — Unauthorized user attempts reversal

**Priority:** P0

Expected:

Denied.

---

## TS-TRF-157 — Reversal generates separate traceable transaction record

**Priority:** P0

Expected:

Original financial history remains intact.

---

# 27. Transaction History Integration

## TS-TRF-158 — Successful transfer appears in source transaction history

**Priority:** P0

Expected:

Correct debit entry.

---

## TS-TRF-159 — Successful internal transfer appears in destination history

**Priority:** P0

Expected:

Correct credit entry.

---

## TS-TRF-160 — Failed transfer appears with correct status where required

**Priority:** P1

Expected:

No misleading completed status.

---

## TS-TRF-161 — Scheduled transfer appears with correct scheduled status

**Priority:** P1

Expected:

Correct representation.

---

## TS-TRF-162 — Reversed transfer displays original and reversal correctly

**Priority:** P0

Expected:

History remains auditable.

---

# 28. Statement Integration

## TS-TRF-163 — Completed outgoing transfer appears on statement

**Priority:** P0

Expected:

Correct date, amount, reference, and balance impact.

---

## TS-TRF-164 — Incoming transfer appears as credit

**Priority:** P0

Expected:

Correct amount.

---

## TS-TRF-165 — Transfer fee appears correctly on statement

**Priority:** P1

Expected:

Accounting representation matches design.

---

## TS-TRF-166 — Failed transfer does not incorrectly appear as completed financial debit

**Priority:** P0

Expected:

Statement remains accurate.

---

# 29. Database Consistency Scenarios

## TS-TRF-167 — Completed transfer creates correct transaction record

**Priority:** P0

Verify:

* Source
* Destination
* Amount
* Fee
* Status
* Reference
* Timestamp

---

## TS-TRF-168 — Source balance matches expected database value

**Priority:** P0

Expected:

Correct persisted balance.

---

## TS-TRF-169 — Destination balance matches expected database value

**Priority:** P0

Expected:

Correct persisted balance.

---

## TS-TRF-170 — Failed transfer does not persist invalid financial changes

**Priority:** P0

Expected:

No unintended debit or credit.

---

## TS-TRF-171 — Transfer relationships preserve referential integrity

**Priority:** P0

Expected:

No orphan transaction records.

---

# 30. API Consistency Scenarios

## TS-TRF-172 — Transfer API response matches UI result

**Priority:** P0

Expected:

Same status, amount, reference.

---

## TS-TRF-173 — Transfer API status matches database state

**Priority:** P0

Expected:

No state mismatch.

---

## TS-TRF-174 — Balance API reflects successful transfer

**Priority:** P0

Expected:

Correct updated balance.

---

## TS-TRF-175 — Failed transfer API does not cause hidden balance modification

**Priority:** P0

Expected:

Balance unchanged.

---

# 31. Error Handling Scenarios

## TS-TRF-176 — Transfer service unavailable

**Priority:** P0

Expected:

Safe failure message.

No money moves.

---

## TS-TRF-177 — Network disconnect before submission

**Priority:** P1

Expected:

No transfer occurs.

---

## TS-TRF-178 — Network disconnect immediately after submission

**Priority:** P0

Expected:

Customer can determine actual transaction state without blindly resubmitting.

---

## TS-TRF-179 — Slow transfer response

**Priority:** P0

Expected:

Processing state prevents duplicate submission.

---

## TS-TRF-180 — Malformed backend response

**Priority:** P1

Expected:

No false success displayed.

---

# 32. Notification Scenarios

## TS-TRF-181 — Successful transfer generates sender notification

**Priority:** P1

Expected:

Correct amount and transfer status.

---

## TS-TRF-182 — Internal recipient receives credit notification

**Priority:** P1

Where supported.

---

## TS-TRF-183 — Failed transfer notification reflects failure

**Priority:** P1

Expected:

No success notification.

---

## TS-TRF-184 — Scheduled transfer execution generates notification

**Priority:** P2

Expected:

Actual execution state communicated.

---

## TS-TRF-185 — Reversed transfer generates notification

**Priority:** P1

Expected:

Customer informed correctly.

---

# 33. Audit Scenarios

## TS-TRF-186 — Successful transfer generates audit record

**Priority:** P0

Expected:

Contains appropriate:

* Customer
* Source account
* Destination
* Amount
* Reference
* Timestamp
* Result

---

## TS-TRF-187 — Failed transfer creates audit/security record where required

**Priority:** P1

Expected:

Failure traceable.

---

## TS-TRF-188 — Scheduled transfer creation audited

**Priority:** P1

Expected:

Schedule creation traceable.

---

## TS-TRF-189 — Scheduled transfer cancellation audited

**Priority:** P1

Expected:

Cancellation traceable.

---

## TS-TRF-190 — Transfer reversal audited

**Priority:** P0

Expected:

Actor, reason, original transaction, and reversal recorded.

---

# 34. Security Scenarios

## TS-TRF-191 — Manipulate transfer amount client-side

**Priority:** P0

Expected:

Backend validates amount independently.

---

## TS-TRF-192 — Manipulate fee client-side

**Priority:** P0

Expected:

Backend calculates authoritative fee.

---

## TS-TRF-193 — Manipulate source account

**Priority:** P0

Expected:

Ownership enforced.

---

## TS-TRF-194 — Manipulate beneficiary

**Priority:** P0

Expected:

Beneficiary ownership and status enforced.

---

## TS-TRF-195 — Manipulate destination account while retaining valid beneficiary ID

**Priority:** P0

Expected:

Rejected if mapping is inconsistent.

---

## TS-TRF-196 — Replay completed transfer request

**Priority:** P0

Expected:

No unintended duplicate transfer.

---

## TS-TRF-197 — SQL-like input in transfer description/reference field

**Priority:** P1

Expected:

Safely handled as data.

---

## TS-TRF-198 — Script-like input in transfer note

**Priority:** P1

Expected:

No script execution.

---

# 35. Transfer Description / Note Scenarios

Where optional transaction notes exist.

## TS-TRF-199 — Transfer with valid note

**Priority:** P2

Expected:

Note stored and displayed correctly.

---

## TS-TRF-200 — Transfer with empty optional note

**Priority:** P3

Expected:

Transfer succeeds.

---

## TS-TRF-201 — Note at maximum length

**Priority:** P2

Expected:

Accepted.

---

## TS-TRF-202 — Note above maximum length

**Priority:** P2

Expected:

Rejected.

---

## TS-TRF-203 — Note with Arabic text

**Priority:** P2

Expected:

Unicode preserved.

---

# 36. Search and Filtering Scenarios

## TS-TRF-204 — Search transfer by reference

**Priority:** P1

Expected:

Correct transaction found.

---

## TS-TRF-205 — Filter transfers by status

**Priority:** P2

Expected:

Correct records.

---

## TS-TRF-206 — Filter by date range

**Priority:** P1

Expected:

Only matching transfers.

---

## TS-TRF-207 — Filter by beneficiary

**Priority:** P2

Expected:

Matching transfer records.

---

## TS-TRF-208 — Filter by amount range

**Priority:** P2

Expected:

Correct results.

---

# 37. Cross-Browser Scenarios

## TS-TRF-209 — Transfer flow in Chrome

**Priority:** P0

Expected:

Critical flow works.

---

## TS-TRF-210 — Transfer flow in Edge

**Priority:** P0

Expected:

Critical flow works.

---

## TS-TRF-211 — Transfer flow in Firefox

**Priority:** P0

Expected:

Critical flow works.

---

## TS-TRF-212 — Transfer confirmation in WebKit

**Priority:** P1

Expected:

No browser-specific failure.

---

# 38. Responsive Scenarios

## TS-TRF-213 — Transfer flow on desktop

**Priority:** P1

Expected:

All critical information visible.

---

## TS-TRF-214 — Transfer flow on tablet

**Priority:** P1

Expected:

Usable and clear.

---

## TS-TRF-215 — Transfer flow on mobile

**Priority:** P0

Expected:

Source, destination, amount, fee, and confirmation remain clearly visible.

---

## TS-TRF-216 — Long beneficiary name on mobile transfer screen

**Priority:** P1

Expected:

Recipient identity remains understandable.

---

## TS-TRF-217 — Error message on mobile transfer form

**Priority:** P1

Expected:

Fully visible and actionable.

---

# 39. Accessibility and Usability Scenarios

## TS-TRF-218 — Transfer form supports keyboard navigation

**Priority:** P2

Expected:

Logical order.

---

## TS-TRF-219 — Amount field clearly indicates currency

**Priority:** P1

Expected:

No ambiguity.

---

## TS-TRF-220 — Confirmation clearly distinguishes amount and fee

**Priority:** P0

Expected:

Customer understands exact financial effect.

---

## TS-TRF-221 — Recipient information clearly visible before confirmation

**Priority:** P0

Expected:

Wrong-recipient risk minimized.

---

## TS-TRF-222 — Success page displays transfer reference

**Priority:** P1

Expected:

Customer can identify transaction.

---

## TS-TRF-223 — Failure page clearly explains that money was not transferred

**Priority:** P0

Expected:

No ambiguity about financial state.

---

# 40. Boundary Scenarios

## TS-TRF-224 — Minimum amount minus smallest unit

**Priority:** P1

Expected:

Rejected.

---

## TS-TRF-225 — Minimum exact

**Priority:** P1

Expected:

Accepted.

---

## TS-TRF-226 — Minimum plus smallest unit

**Priority:** P2

Expected:

Accepted.

---

## TS-TRF-227 — Maximum minus smallest unit

**Priority:** P1

Expected:

Accepted.

---

## TS-TRF-228 — Maximum exact

**Priority:** P0

Expected:

Handled correctly.

---

## TS-TRF-229 — Maximum plus smallest unit

**Priority:** P0

Expected:

Rejected.

---

## TS-TRF-230 — Available balance minus 0.01

**Priority:** P1

Expected:

Accepted if fees allow.

---

## TS-TRF-231 — Exact available balance

**Priority:** P0

Expected:

Handled according to fee/minimum-balance rules.

---

## TS-TRF-232 — Available balance plus 0.01

**Priority:** P0

Expected:

Rejected.

---

# 41. End-to-End Transfer Scenarios

## TS-TRF-233 — Complete beneficiary transfer journey

**Priority:** P0

Flow:

```text
Login
→ View Account
→ Select Beneficiary
→ Enter Amount
→ Review Fee
→ Confirm Transfer
→ Transfer Completes
→ Verify Source Balance
→ Verify Transaction History
→ Verify Notification
→ Verify Statement
```

---

## TS-TRF-234 — Own-account transfer journey

**Priority:** P0

Flow:

```text
Login
→ Select Checking
→ Transfer to Own Savings
→ Confirm
→ Verify Checking Debit
→ Verify Savings Credit
→ Verify Both Transaction Histories
```

---

## TS-TRF-235 — Insufficient-funds journey

**Priority:** P0

Flow:

```text
Login
→ Select Low-Balance Account
→ Enter Amount Above Available Balance
→ Submit
→ Transfer Rejected
→ Verify Balance Unchanged
→ Verify No Completed Transaction
```

---

## TS-TRF-236 — Duplicate-submission protection journey

**Priority:** P0

Flow:

```text
Prepare Valid Transfer
→ Double Click Confirm
→ Wait for Completion
→ Verify One Transfer Only
→ Verify One Debit Only
→ Verify One Transaction Reference
```

---

## TS-TRF-237 — Concurrent-transfer protection journey

**Priority:** P0

Flow:

```text
Balance = 1000
→ Session A submits 800
→ Session B submits 500 concurrently
→ Verify Business Rule Enforcement
→ Verify Final Balance
→ Verify Transaction States
```

---

## TS-TRF-238 — Scheduled transfer journey

**Priority:** P0

Flow:

```text
Create Future Transfer
→ Verify SCHEDULED
→ Wait/Advance to Execution Time
→ Verify Execution
→ Verify Balance
→ Verify Transaction
→ Verify Notification
```

---

## TS-TRF-239 — Cancel scheduled transfer journey

**Priority:** P0

Flow:

```text
Create Scheduled Transfer
→ Cancel Transfer
→ Verify CANCELLED
→ Reach Execution Time
→ Verify No Debit
→ Verify No Completed Transfer
```

---

## TS-TRF-240 — Reversal journey

**Priority:** P0

Flow:

```text
Complete Transfer
→ Authorized Reversal
→ Verify Reversal Transaction
→ Verify Balances
→ Verify Original History Preserved
→ Verify Audit
```

---

# 42. Critical Smoke Scenarios

Transfer smoke coverage should include:

```text
TS-TRF-001 — Valid beneficiary transfer
TS-TRF-002 — Own-account transfer
TS-TRF-009 — Valid source account
TS-TRF-024 — Active beneficiary
TS-TRF-045 — Amount below available balance
TS-TRF-060 — Correct confirmation
TS-TRF-082 — Transaction reference generated
TS-TRF-087 — Completed status
TS-TRF-158 — Transaction history updated
```

---

# 43. Critical Regression Scenarios

Always prioritize:

* Valid internal transfer
* Valid own-account transfer
* Account ownership
* Beneficiary ownership
* Insufficient funds
* Fees
* Limits
* Frozen/restricted accounts
* Destination account status
* Duplicate submission
* Idempotency
* Atomicity
* Concurrency
* Failed-transfer rollback
* Scheduled transfers
* Recurring transfers
* Cancellation
* Reversal
* Transaction history
* Statements
* UI/API/database consistency
* Authorization

---

# 44. Automation Candidates

Strong UI candidates for Selenium, Cypress, and Playwright:

* Valid transfer
* Own-account transfer
* Insufficient funds
* Invalid beneficiary
* Frozen account
* Transfer limits
* Transfer confirmation
* Duplicate submission
* Scheduled transfer
* Transfer cancellation
* Transaction-history validation

---

# 45. API Automation Candidates

Postman and REST Assured should later cover:

* Create valid transfer
* Invalid source account
* Invalid beneficiary
* Insufficient funds
* Minimum/maximum amount
* Daily limit
* Monthly limit
* Fees
* Authorization
* Idempotency
* Duplicate requests
* Scheduled transfers
* Cancellation
* Status transitions
* Reversal
* Concurrent submissions

---

# 46. SQL Validation Candidates

SQL testing should validate:

* Source balance
* Destination balance
* Transfer amount
* Fee
* Transaction status
* Transaction reference
* Source/destination relationships
* Scheduled transfer state
* Recurring transfer state
* Reversal records
* Audit records

---

# 47. Performance Testing Candidates

JMeter should later cover:

* Concurrent valid transfers
* High-volume transfer creation
* Mixed successful/failed transfers
* Duplicate-request behavior
* Balance contention
* Transfer throughput
* Transfer response time
* Error rate under load

Special attention should be paid to:

```text
Concurrent financial integrity
```

High throughput is useless if balances become incorrect.

---

# 48. BDD Candidates

Example:

```gherkin
Feature: Bank transfer

Scenario: Successful transfer to an active beneficiary
  Given the customer is logged in
  And the source account is active
  And the account has sufficient available balance
  And the beneficiary is active
  When the customer transfers a valid amount
  Then the transfer should complete successfully
  And the source account balance should decrease correctly
  And a completed transaction should be recorded
```

Another high-risk scenario:

```gherkin
Scenario: Two concurrent transfers cannot overspend the account
  Given an account has a balance of 1000 EGP
  When a transfer of 800 EGP and a transfer of 500 EGP are submitted concurrently
  Then the combined successful debit must not exceed the available balance
  And the final account balance must remain valid
```

---

# 49. Risk Traceability

Major related risks include:

```text
RISK-001 — Incorrect account balance
RISK-003 — Duplicate financial transaction
RISK-004 — Partial transfer processing
RISK-007 — Transfer exceeding available balance
RISK-008 — Transfer limit bypass
RISK-009 — Frozen account can transact
RISK-013 — Concurrent transactions corrupt balance
RISK-017 — Incorrect beneficiary
RISK-018 — Closed destination account
RISK-019 — Transaction history mismatch
RISK-028 — Scheduled transfer executes incorrectly
RISK-029 — Cancelled transfer still executes
RISK-030 — Unauthorized API request
RISK-037 — Frontend-only validation
RISK-039 — API/database inconsistency
RISK-042 — Slow response causes duplicate submission
RISK-047 — Insecure direct object access
RISK-048 — UI false success
```

---

# 50. Transfer Coverage Summary

This catalog covers:

* Internal transfers
* Own-account transfers
* Beneficiary transfers
* External transfers
* Source-account validation
* Destination-account validation
* Beneficiary validation
* Amount boundaries
* Available balance
* Fees
* Confirmation
* Authorization
* Idempotency
* Duplicate requests
* References
* Status transitions
* Failures
* Atomicity
* Concurrency
* Daily limits
* Monthly limits
* Scheduled transfers
* Recurring transfers
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
* Notes
* Search/filtering
* Responsive behavior
* Cross-browser behavior
* Accessibility
* End-to-end flows

---

# 51. Final Transfer Testing Principle

Transfer testing must verify more than whether the application displays a success message.

For every high-risk transfer, QA should be able to answer:

```text
Was the customer authorized?

Was the source account valid?

Was the beneficiary valid?

Was the transfer amount valid?

Was sufficient money available?

Was the fee correct?

Did exactly one transaction execute?

Was the source debited correctly?

Was the destination credited correctly?

Was the operation atomic?

Was the transaction recorded?

Do UI, API, database, history, and statements agree?

Can the transaction be traced and audited?
```

A transfer should be considered successful only when the entire financial state is correct and consistent across all relevant layers.
