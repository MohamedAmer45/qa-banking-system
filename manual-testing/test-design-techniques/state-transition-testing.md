# Banking System — State Transition Testing

## 1. Document Information

| Field                 | Value                          |
| --------------------- | ------------------------------ |
| Project               | Banking System Testing Project |
| Test Design Technique | State Transition Testing       |
| Document              | Test Design                    |
| Version               | 1.0                            |
| Status                | Draft                          |
| Owner                 | QA Engineering                 |

---

# 2. Purpose

This document applies **State Transition Testing** to the Banking System.

Many banking entities behave differently depending on their current lifecycle state.

Examples include:

* Customers
* Accounts
* Beneficiaries
* Transfers
* Payments
* Cards
* Loans
* Deposits
* Sessions
* KYC records
* Notifications

State Transition Testing verifies:

* Valid transitions
* Invalid transitions
* Terminal states
* Reversible states
* Transition side effects
* Authorization around transitions
* Financial integrity during transitions
* Concurrency around state changes

---

# 3. State Transition Testing Principle

A state transition is represented as:

```text
CURRENT STATE
   ↓ EVENT
NEW STATE
```

Example:

```text
ACTIVE
  ↓ Freeze
FROZEN
```

The tests must verify both:

```text
Valid transition:
ACTIVE → FROZEN
```

and:

```text
Invalid transition:
CLOSED → ACTIVE
```

---

# 4. Scenario Naming Convention

State-transition scenarios use:

```text
ST-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Core State Transition Questions

For every stateful banking entity, verify:

```text
What is the initial state?

Which events are allowed from this state?

Which resulting states are valid?

Which transitions are forbidden?

Which states are terminal?

Can the state be reversed?

Who is authorized to trigger the transition?

What side effects must occur?

What financial effects must not occur?

What happens if the transition is submitted twice?

What happens if two conflicting transitions happen concurrently?
```

---

# 6. Customer Lifecycle States

Possible customer states:

```text
PENDING
ACTIVE
RESTRICTED
SUSPENDED
DISABLED
CLOSED
```

Actual implementation may use a subset.

---

# 7. Customer Valid Transitions

| ID     | From       | Event               | To         | Priority |
| ------ | ---------- | ------------------- | ---------- | -------- |
| ST-001 | PENDING    | Complete activation | ACTIVE     | P1       |
| ST-002 | ACTIVE     | Apply restriction   | RESTRICTED | P0       |
| ST-003 | RESTRICTED | Remove restriction  | ACTIVE     | P1       |
| ST-004 | ACTIVE     | Suspend             | SUSPENDED  | P0       |
| ST-005 | SUSPENDED  | Reinstate           | ACTIVE     | P1       |
| ST-006 | ACTIVE     | Disable             | DISABLED   | P0       |
| ST-007 | RESTRICTED | Disable             | DISABLED   | P0       |
| ST-008 | SUSPENDED  | Disable             | DISABLED   | P0       |
| ST-009 | ACTIVE     | Close customer      | CLOSED     | P0       |

---

# 8. Customer Invalid Transitions

| ID     | From     | Attempted To | Expected                                            |
| ------ | -------- | ------------ | --------------------------------------------------- |
| ST-010 | CLOSED   | ACTIVE       | Reject                                              |
| ST-011 | CLOSED   | RESTRICTED   | Reject                                              |
| ST-012 | DISABLED | ACTIVE       | Reject unless explicit reactivation workflow exists |
| ST-013 | PENDING  | CLOSED       | Reject unless explicit cancellation workflow exists |

Critical verification:

* Customer cannot bypass status through direct API.
* Existing sessions revalidate current customer status.
* Audit records reflect transitions.

---

# 9. Customer State Effects

## ST-014 — ACTIVE customer performs financial transaction

**Priority:** P0

Expected:

Allowed if all other business rules pass.

---

## ST-015 — RESTRICTED customer performs prohibited transaction

**Priority:** P0

Expected:

Rejected.

---

## ST-016 — SUSPENDED customer performs financial operation

**Priority:** P0

Expected:

Rejected.

---

## ST-017 — DISABLED customer uses existing session

**Priority:** P0

Expected:

Protected operation rejected.

---

## ST-018 — CLOSED customer attempts login/financial action

**Priority:** P0

Expected:

Denied according to closure policy.

---

# 10. KYC Lifecycle States

Possible states:

```text
PENDING
VERIFIED
REJECTED
EXPIRED
```

---

# 11. KYC Valid Transitions

| ID     | From     | Event                     | To       |
| ------ | -------- | ------------------------- | -------- |
| ST-019 | PENDING  | Approve                   | VERIFIED |
| ST-020 | PENDING  | Reject                    | REJECTED |
| ST-021 | VERIFIED | Expiration reached        | EXPIRED  |
| ST-022 | EXPIRED  | Successful reverification | VERIFIED |
| ST-023 | REJECTED | New valid submission      | PENDING  |

---

# 12. KYC Invalid Transitions

| ID     | From     | Attempted To                         | Expected |
| ------ | -------- | ------------------------------------ | -------- |
| ST-024 | VERIFIED | PENDING without reverification event | Reject   |
| ST-025 | REJECTED | VERIFIED without review              | Reject   |
| ST-026 | EXPIRED  | VERIFIED without verification        | Reject   |

**Priority:** P0 for unauthorized transitions.

---

# 13. Account Lifecycle States

Possible states:

```text
PENDING
ACTIVE
FROZEN
RESTRICTED
SUSPENDED
CLOSED
```

---

# 14. Account Valid Transitions

| ID     | From       | Event                  | To         |
| ------ | ---------- | ---------------------- | ---------- |
| ST-027 | PENDING    | Activate               | ACTIVE     |
| ST-028 | ACTIVE     | Freeze                 | FROZEN     |
| ST-029 | FROZEN     | Unfreeze               | ACTIVE     |
| ST-030 | ACTIVE     | Restrict               | RESTRICTED |
| ST-031 | RESTRICTED | Remove restriction     | ACTIVE     |
| ST-032 | ACTIVE     | Suspend                | SUSPENDED  |
| ST-033 | SUSPENDED  | Reinstate              | ACTIVE     |
| ST-034 | ACTIVE     | Close eligible account | CLOSED     |

---

# 15. Account Invalid Transitions

| ID     | From       | Attempted To                                | Expected |
| ------ | ---------- | ------------------------------------------- | -------- |
| ST-035 | CLOSED     | ACTIVE                                      | Reject   |
| ST-036 | CLOSED     | FROZEN                                      | Reject   |
| ST-037 | FROZEN     | CLOSED with unresolved balance/dependencies | Reject   |
| ST-038 | RESTRICTED | CLOSED with outstanding dependencies        | Reject   |

---

# 16. Account Transition Side Effects

## ST-039 — ACTIVE → FROZEN

**Priority:** P0

Verify:

* Outgoing prohibited transfers fail.
* Payments follow frozen-account rules.
* Card usage follows linked-account rules.
* Account remains viewable where allowed.
* Audit record exists.
* Customer notification generated where required.

---

## ST-040 — FROZEN → ACTIVE

**Priority:** P0

Verify:

* Permitted operations resume.
* No previously failed transaction auto-retries unexpectedly.

---

## ST-041 — ACTIVE → CLOSED

**Priority:** P0

Verify:

* Balance is eligible.
* No pending transaction.
* No unresolved dependencies.
* Closed account cannot transact.
* Historical data remains accessible as required.

---

# 17. Beneficiary Lifecycle States

Possible states:

```text
PENDING_VERIFICATION
PENDING_ACTIVATION
ACTIVE
DISABLED
DELETED
```

---

# 18. Beneficiary Valid Transitions

| ID     | From                 | Event              | To                 |
| ------ | -------------------- | ------------------ | ------------------ |
| ST-042 | PENDING_VERIFICATION | Verify             | PENDING_ACTIVATION |
| ST-043 | PENDING_ACTIVATION   | Cooldown completes | ACTIVE             |
| ST-044 | ACTIVE               | Disable            | DISABLED           |
| ST-045 | DISABLED             | Re-enable          | ACTIVE             |
| ST-046 | ACTIVE               | Delete             | DELETED            |
| ST-047 | DISABLED             | Delete             | DELETED            |

---

# 19. Beneficiary Invalid Transitions

| ID     | From                 | Attempted To           | Expected                                   |
| ------ | -------------------- | ---------------------- | ------------------------------------------ |
| ST-048 | PENDING_VERIFICATION | ACTIVE directly        | Reject                                     |
| ST-049 | PENDING_ACTIVATION   | ACTIVE before cooldown | Reject                                     |
| ST-050 | DELETED              | ACTIVE                 | Reject unless recreated as new beneficiary |
| ST-051 | DELETED              | DISABLED               | Reject                                     |

---

# 20. Beneficiary Transition Effects

## ST-052 — PENDING_ACTIVATION beneficiary used for transfer

**Priority:** P0

Expected:

Rejected.

---

## ST-053 — ACTIVE beneficiary used for transfer

**Priority:** P0

Expected:

Allowed if other transfer rules pass.

---

## ST-054 — Beneficiary disabled during transfer confirmation

**Priority:** P0

Expected:

Final submission revalidates latest state and rejects if no longer eligible.

---

# 21. Transfer Lifecycle States

Possible states:

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

# 22. Transfer Valid Transitions

| ID     | From       | Event                 | To         |
| ------ | ---------- | --------------------- | ---------- |
| ST-055 | CREATED    | Submit                | PENDING    |
| ST-056 | PENDING    | Start processing      | PROCESSING |
| ST-057 | PROCESSING | Successful completion | COMPLETED  |
| ST-058 | PROCESSING | Processing failure    | FAILED     |
| ST-059 | PENDING    | Cancel where allowed  | CANCELLED  |
| ST-060 | COMPLETED  | Authorized reversal   | REVERSED   |

---

# 23. Transfer Invalid Transitions

| ID     | From      | Attempted To                            | Expected                  |
| ------ | --------- | --------------------------------------- | ------------------------- |
| ST-061 | COMPLETED | PROCESSING                              | Reject                    |
| ST-062 | FAILED    | COMPLETED without retry/new transaction | Reject                    |
| ST-063 | CANCELLED | COMPLETED                               | Reject                    |
| ST-064 | REVERSED  | COMPLETED                               | Reject                    |
| ST-065 | REVERSED  | REVERSED again                          | Reject duplicate reversal |

---

# 24. Transfer State Financial Effects

## ST-066 — PENDING transfer

**Priority:** P0

Expected:

Financial handling follows hold/reservation model.

No false final transaction status.

---

## ST-067 — PROCESSING transfer

**Priority:** P0

Expected:

No duplicate submission may create second transaction.

---

## ST-068 — COMPLETED transfer

**Priority:** P0

Expected:

* Source debit correct.
* Destination credit correct.
* Fee correct.
* Transaction history present.
* Reference unique.

---

## ST-069 — FAILED transfer

**Priority:** P0

Expected:

* No completed debit/credit.
* Any temporary hold released.
* No success notification.

---

## ST-070 — CANCELLED transfer

**Priority:** P0

Expected:

No later execution.

---

## ST-071 — REVERSED transfer

**Priority:** P0

Expected:

* Original transaction preserved.
* Separate reversal recorded.
* Financial state reconciles.

---

# 25. Scheduled Transfer States

Possible states:

```text
SCHEDULED
PROCESSING
COMPLETED
FAILED
CANCELLED
```

---

# 26. Scheduled Transfer Transitions

| ID     | From       | Event                  | To         |
| ------ | ---------- | ---------------------- | ---------- |
| ST-072 | SCHEDULED  | Execution time reached | PROCESSING |
| ST-073 | PROCESSING | Success                | COMPLETED  |
| ST-074 | PROCESSING | Failure                | FAILED     |
| ST-075 | SCHEDULED  | Customer cancels       | CANCELLED  |

Invalid:

```text
CANCELLED → PROCESSING
COMPLETED → CANCELLED
FAILED → COMPLETED without explicit retry/new execution
```

IDs:

```text
ST-076
ST-077
ST-078
```

---

# 27. Payment Lifecycle States

Possible states:

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

# 28. Payment Valid Transitions

| ID     | From       | Event                   | To         |
| ------ | ---------- | ----------------------- | ---------- |
| ST-079 | CREATED    | Submit                  | PENDING    |
| ST-080 | PENDING    | Begin processing        | PROCESSING |
| ST-081 | PROCESSING | Provider success        | COMPLETED  |
| ST-082 | PROCESSING | Provider/system failure | FAILED     |
| ST-083 | PENDING    | Cancel where supported  | CANCELLED  |
| ST-084 | COMPLETED  | Authorized reversal     | REVERSED   |

---

# 29. Payment Invalid Transitions

| ID     | From      | Attempted To       | Expected |
| ------ | --------- | ------------------ | -------- |
| ST-085 | FAILED    | COMPLETED directly | Reject   |
| ST-086 | CANCELLED | COMPLETED          | Reject   |
| ST-087 | REVERSED  | COMPLETED          | Reject   |
| ST-088 | REVERSED  | REVERSED again     | Reject   |

---

# 30. Payment State Financial Effects

## ST-089 — FAILED payment

**Priority:** P0

Expected:

Balance unchanged or temporary hold restored.

---

## ST-090 — COMPLETED payment

**Priority:** P0

Expected:

Correct debit and transaction record.

---

## ST-091 — REVERSED payment

**Priority:** P0

Expected:

Separate financial reversal.

---

# 31. Card Lifecycle States

Possible states:

```text
INACTIVE
ACTIVE
FROZEN
BLOCKED
EXPIRED
CANCELLED
```

---

# 32. Card Valid Transitions

| ID     | From     | Event               | To        |
| ------ | -------- | ------------------- | --------- |
| ST-092 | INACTIVE | Activate            | ACTIVE    |
| ST-093 | ACTIVE   | Freeze              | FROZEN    |
| ST-094 | FROZEN   | Unfreeze            | ACTIVE    |
| ST-095 | ACTIVE   | Block               | BLOCKED   |
| ST-096 | FROZEN   | Block               | BLOCKED   |
| ST-097 | ACTIVE   | Expiry date reached | EXPIRED   |
| ST-098 | FROZEN   | Expiry date reached | EXPIRED   |
| ST-099 | ACTIVE   | Cancel              | CANCELLED |
| ST-100 | FROZEN   | Cancel              | CANCELLED |

---

# 33. Card Invalid Transitions

| ID     | From      | Attempted To | Expected                                             |
| ------ | --------- | ------------ | ---------------------------------------------------- |
| ST-101 | BLOCKED   | ACTIVE       | Reject unless controlled replacement/recovery exists |
| ST-102 | EXPIRED   | ACTIVE       | Reject                                               |
| ST-103 | CANCELLED | ACTIVE       | Reject                                               |
| ST-104 | INACTIVE  | FROZEN       | Reject                                               |
| ST-105 | CANCELLED | FROZEN       | Reject                                               |

---

# 34. Card State Transaction Behavior

| State     | Purchase | Withdrawal | Limit Change | Replacement |
| --------- | -------: | ---------: | -----------: | ----------: |
| INACTIVE  |        N |          N |  Conditional | Conditional |
| ACTIVE    |        Y |          Y |            Y |           Y |
| FROZEN    |        N |          N |  Conditional |           Y |
| BLOCKED   |        N |          N |            N |           Y |
| EXPIRED   |        N |          N |            N |           Y |
| CANCELLED |        N |          N |            N |           N |

---

# 35. Card Freeze Race Scenario

## ST-106 — Purchase begins before card freeze

**Priority:** P0

Expected:

Final authorization uses deterministic cutoff rule.

---

## ST-107 — Purchase submitted after freeze completes

**Priority:** P0

Expected:

Declined.

---

# 36. Loan Application Lifecycle States

Possible states:

```text
DRAFT
SUBMITTED
UNDER_REVIEW
APPROVED
REJECTED
CANCELLED
DISBURSED
ACTIVE
PAST_DUE
DEFAULTED
CLOSED
```

---

# 37. Loan Valid Transitions

| ID     | From         | Event                             | To           |
| ------ | ------------ | --------------------------------- | ------------ |
| ST-108 | DRAFT        | Submit                            | SUBMITTED    |
| ST-109 | SUBMITTED    | Begin review                      | UNDER_REVIEW |
| ST-110 | UNDER_REVIEW | Approve                           | APPROVED     |
| ST-111 | UNDER_REVIEW | Reject                            | REJECTED     |
| ST-112 | SUBMITTED    | Cancel where permitted            | CANCELLED    |
| ST-113 | APPROVED     | Disburse                          | DISBURSED    |
| ST-114 | DISBURSED    | Activate                          | ACTIVE       |
| ST-115 | ACTIVE       | Miss required repayment threshold | PAST_DUE     |
| ST-116 | PAST_DUE     | Bring current                     | ACTIVE       |
| ST-117 | PAST_DUE     | Default threshold reached         | DEFAULTED    |
| ST-118 | ACTIVE       | Fully repay                       | CLOSED       |
| ST-119 | PAST_DUE     | Fully settle                      | CLOSED       |

---

# 38. Loan Invalid Transitions

| ID     | From      | Attempted To                                 | Expected |
| ------ | --------- | -------------------------------------------- | -------- |
| ST-120 | REJECTED  | DISBURSED                                    | Reject   |
| ST-121 | CANCELLED | APPROVED                                     | Reject   |
| ST-122 | CLOSED    | ACTIVE                                       | Reject   |
| ST-123 | CLOSED    | PAST_DUE                                     | Reject   |
| ST-124 | APPROVED  | CLOSED without disbursement/settlement model | Reject   |
| ST-125 | DEFAULTED | ACTIVE without defined recovery workflow     | Reject   |

---

# 39. Loan Approval Concurrency

## ST-126 — Two admins approve same UNDER_REVIEW loan

**Priority:** P0

Expected:

Single valid final state.

---

## ST-127 — One admin approves while another rejects

**Priority:** P0

Expected:

Only one transition wins according to concurrency control.

No contradictory final state.

---

# 40. Loan Disbursement Transition Effects

## ST-128 — APPROVED → DISBURSED

**Priority:** P0

Verify:

* Disbursement exactly once.
* Correct account credited.
* Loan record updated.
* Transaction record created.
* Audit event created.

---

## ST-129 — Duplicate disbursement event

**Priority:** P0

Expected:

No second credit.

---

# 41. Loan Repayment State Effects

## ST-130 — ACTIVE loan receives normal repayment

**Priority:** P0

Expected:

Remains ACTIVE until fully settled.

---

## ST-131 — ACTIVE loan final repayment

**Priority:** P0

Expected:

Moves to CLOSED after exact settlement.

---

## ST-132 — PAST_DUE loan receives required catch-up payment

**Priority:** P0

Expected:

May return to ACTIVE according to rules.

---

# 42. Deposit Lifecycle States

Possible states:

```text
PENDING
ACTIVE
MATURED
EARLY_WITHDRAWN
RENEWED
CLOSED
CANCELLED
```

---

# 43. Deposit Valid Transitions

| ID     | From            | Event                    | To              |
| ------ | --------------- | ------------------------ | --------------- |
| ST-133 | PENDING         | Funding succeeds         | ACTIVE          |
| ST-134 | PENDING         | Creation cancelled/fails | CANCELLED       |
| ST-135 | ACTIVE          | Maturity reached         | MATURED         |
| ST-136 | ACTIVE          | Early withdrawal         | EARLY_WITHDRAWN |
| ST-137 | EARLY_WITHDRAWN | Finalize payout          | CLOSED          |
| ST-138 | MATURED         | Pay out                  | CLOSED          |
| ST-139 | MATURED         | Auto-renew               | RENEWED         |
| ST-140 | RENEWED         | Start new term           | ACTIVE          |

---

# 44. Deposit Invalid Transitions

| ID     | From            | Attempted To            | Expected |
| ------ | --------------- | ----------------------- | -------- |
| ST-141 | CLOSED          | ACTIVE                  | Reject   |
| ST-142 | CANCELLED       | ACTIVE                  | Reject   |
| ST-143 | EARLY_WITHDRAWN | ACTIVE                  | Reject   |
| ST-144 | ACTIVE          | RENEWED before maturity | Reject   |
| ST-145 | CLOSED          | MATURED                 | Reject   |

---

# 45. Deposit State Financial Effects

## ST-146 — PENDING → ACTIVE

**Priority:** P0

Expected:

Principal debited exactly once.

---

## ST-147 — ACTIVE → MATURED

**Priority:** P0

Expected:

Interest/maturity amount finalized according to rules.

---

## ST-148 — MATURED → CLOSED

**Priority:** P0

Expected:

Payout exactly once.

---

## ST-149 — MATURED → RENEWED

**Priority:** P0

Expected:

No duplicate payout plus renewal.

---

## ST-150 — ACTIVE → EARLY_WITHDRAWN

**Priority:** P0

Expected:

Penalty/net payout calculated correctly.

---

# 46. Session Lifecycle States

Possible states:

```text
ANONYMOUS
AUTHENTICATED_PENDING_MFA
AUTHENTICATED
EXPIRED
REVOKED
LOGGED_OUT
```

---

# 47. Session Valid Transitions

| ID     | From                      | Event                            | To                        |
| ------ | ------------------------- | -------------------------------- | ------------------------- |
| ST-151 | ANONYMOUS                 | Valid credentials + no MFA       | AUTHENTICATED             |
| ST-152 | ANONYMOUS                 | Valid credentials + MFA required | AUTHENTICATED_PENDING_MFA |
| ST-153 | AUTHENTICATED_PENDING_MFA | Valid MFA                        | AUTHENTICATED             |
| ST-154 | AUTHENTICATED             | Inactivity timeout               | EXPIRED                   |
| ST-155 | AUTHENTICATED             | Revoke                           | REVOKED                   |
| ST-156 | AUTHENTICATED             | Logout                           | LOGGED_OUT                |

---

# 48. Session Invalid Transitions

| ID     | From                      | Attempted To                  | Expected |
| ------ | ------------------------- | ----------------------------- | -------- |
| ST-157 | AUTHENTICATED_PENDING_MFA | AUTHENTICATED without MFA     | Reject   |
| ST-158 | EXPIRED                   | AUTHENTICATED without login   | Reject   |
| ST-159 | REVOKED                   | AUTHENTICATED                 | Reject   |
| ST-160 | LOGGED_OUT                | AUTHENTICATED via old session | Reject   |

---

# 49. Session State Access Effects

| Session State             | Protected Resource               |
| ------------------------- | -------------------------------- |
| ANONYMOUS                 | Deny                             |
| AUTHENTICATED_PENDING_MFA | Deny sensitive access            |
| AUTHENTICATED             | Allow according to authorization |
| EXPIRED                   | Deny                             |
| REVOKED                   | Deny                             |
| LOGGED_OUT                | Deny                             |

---

# 50. Notification Lifecycle States

Possible states:

```text
UNREAD
READ
DISMISSED
DELETED
```

---

# 51. Notification Valid Transitions

| ID     | From   | Event          | To        |
| ------ | ------ | -------------- | --------- |
| ST-161 | UNREAD | Open/mark read | READ      |
| ST-162 | READ   | Mark unread    | UNREAD    |
| ST-163 | UNREAD | Dismiss        | DISMISSED |
| ST-164 | READ   | Dismiss        | DISMISSED |
| ST-165 | READ   | Delete         | DELETED   |
| ST-166 | UNREAD | Delete         | DELETED   |

---

# 52. Notification Invalid Transitions

| ID     | From      | Attempted To | Expected                                           |
| ------ | --------- | ------------ | -------------------------------------------------- |
| ST-167 | DELETED   | READ         | Reject/no effect                                   |
| ST-168 | DELETED   | UNREAD       | Reject/no effect                                   |
| ST-169 | DISMISSED | UNREAD       | Follow design; reject unless restoration supported |

---

# 53. Admin Account Lifecycle States

Possible states:

```text
ACTIVE
LOCKED
DISABLED
```

---

# 54. Admin Transitions

| ID     | From   | Event                  | To       |
| ------ | ------ | ---------------------- | -------- |
| ST-170 | ACTIVE | Failed-login threshold | LOCKED   |
| ST-171 | LOCKED | Authorized unlock      | ACTIVE   |
| ST-172 | ACTIVE | Disable                | DISABLED |

Invalid:

```text
DISABLED → ACTIVE without authorized reactivation
```

ID:

```text
ST-173
```

---

# 55. Transaction Reversal Lifecycle

Possible model:

```text
COMPLETED
   ↓ Authorized reversal
REVERSAL_PENDING
   ↓ Success
REVERSED
```

or direct:

```text
COMPLETED → REVERSED
```

depending on implementation.

---

# 56. Reversal Transition Rules

## ST-174 — COMPLETED → REVERSED

**Priority:** P0

Expected:

Original financial history preserved.

---

## ST-175 — FAILED → REVERSED

**Priority:** P0

Expected:

Rejected.

---

## ST-176 — REVERSED → REVERSED

**Priority:** P0

Expected:

Second reversal rejected.

---

# 57. State Transition and Authorization

A technically valid transition must still fail when the actor is unauthorized.

Example:

```text
ACTIVE ACCOUNT → FROZEN
```

is structurally valid.

But:

```text
Customer without permission attempts freeze
```

must be rejected.

State Transition Testing should therefore cover:

```text
Valid state + authorized actor
Valid state + unauthorized actor
Invalid state + authorized actor
Invalid state + unauthorized actor
```

---

# 58. State Transition and Concurrency

Critical transitions should be tested concurrently.

Examples:

```text
ACTIVE → FROZEN
while transfer is processing

UNDER_REVIEW → APPROVED
while another admin submits REJECTED

MATURED → CLOSED
while another worker submits RENEWED

COMPLETED → REVERSED
while another admin submits same reversal
```

---

# 59. Account Freeze Concurrency Matrix

| Current State | Event A  | Event B       | Expected                         |
| ------------- | -------- | ------------- | -------------------------------- |
| ACTIVE        | Freeze   | Transfer      | Deterministic financial cutoff   |
| ACTIVE        | Freeze   | Payment       | Deterministic cutoff             |
| ACTIVE        | Freeze   | Card purchase | Deterministic cutoff             |
| FROZEN        | Unfreeze | Transfer      | Latest committed state respected |

IDs:

```text
ST-177
ST-178
ST-179
ST-180
```

**Priority:** P0

---

# 60. Card Concurrency Matrix

| Current State | Event A  | Event B  | Expected                         |
| ------------- | -------- | -------- | -------------------------------- |
| ACTIVE        | Freeze   | Purchase | No post-freeze authorization     |
| ACTIVE        | Block    | Purchase | Block state authoritative        |
| FROZEN        | Unfreeze | Block    | One deterministic final state    |
| ACTIVE        | Replace  | Purchase | Old card behavior follows cutoff |

IDs:

```text
ST-181
ST-182
ST-183
ST-184
```

---

# 61. Loan Concurrency Matrix

| Current State | Event A          | Event B           | Expected                  |
| ------------- | ---------------- | ----------------- | ------------------------- |
| UNDER_REVIEW  | Approve          | Reject            | One final decision        |
| APPROVED      | Disburse         | Disburse          | One credit                |
| ACTIVE        | Final payment    | Final payment     | One settlement            |
| PAST_DUE      | Catch-up payment | Default processor | Deterministic final state |

IDs:

```text
ST-185
ST-186
ST-187
ST-188
```

---

# 62. Deposit Concurrency Matrix

| Current State | Event A          | Event B            | Expected              |
| ------------- | ---------------- | ------------------ | --------------------- |
| ACTIVE        | Early withdrawal | Maturity processor | One valid payout path |
| MATURED       | Payout           | Auto-renew         | One valid final path  |
| PENDING       | Activate         | Cancel             | One final state       |
| MATURED       | Payout           | Payout             | One payout only       |

IDs:

```text
ST-189
ST-190
ST-191
ST-192
```

---

# 63. Transition Idempotency

Repeated identical state-change requests should be safe.

Examples:

```text
Freeze frozen account
Block blocked card
Cancel cancelled transfer
Mark already-read notification read
Approve already-approved loan
```

Expected behavior may be:

```text
Return existing state
or
Reject invalid duplicate transition
```

but must never create duplicate financial effects.

IDs:

```text
ST-193
ST-194
ST-195
ST-196
ST-197
```

---

# 64. Terminal States

Terminal states generally should not allow normal re-entry.

Examples:

```text
CLOSED account
CANCELLED card
REVERSED transaction
REJECTED loan application
CLOSED loan
CLOSED deposit
DELETED beneficiary
LOGGED_OUT session
```

Verify any attempted outgoing transition is rejected unless a documented recovery workflow exists.

---

# 65. Historical Integrity After Terminal State

## ST-198 — Closed account history retained

**Priority:** P0

Expected:

Transactions/statements remain accessible according to retention rules.

---

## ST-199 — Deleted beneficiary historical transfers retained

**Priority:** P0

Expected:

Past transactions remain traceable.

---

## ST-200 — Rejected loan application history retained

**Priority:** P1

Expected:

Decision remains auditable.

---

## ST-201 — Closed deposit historical payout remains traceable

**Priority:** P0

Expected:

No history deletion.

---

# 66. Invalid Transition via API

For every major stateful resource, attempt direct API manipulation.

Examples:

```text
CLOSED account → ACTIVE
REJECTED loan → DISBURSED
CANCELLED transfer → COMPLETED
EXPIRED card → ACTIVE
CLOSED deposit → ACTIVE
DELETED beneficiary → ACTIVE
```

IDs:

```text
ST-202
ST-203
ST-204
ST-205
ST-206
ST-207
```

Expected:

Backend rejects invalid transition even if frontend does not expose the action.

---

# 67. State Transition Database Validation

For every successful transition, validate:

```text
Old state
New state
Updated timestamp
Actor where applicable
Reason where required
Version/concurrency field where applicable
Audit record
```

For rejected transition:

```text
State unchanged
No unintended financial record
No unintended audit success record
No duplicate side effect
```

---

# 68. State Transition API Validation

Verify API responses distinguish:

```text
Valid transition
Invalid current state
Unauthorized actor
Stale state/conflict
Already processed/idempotent request
```

Typical outcomes may include:

```text
2xx success
4xx validation/conflict/authorization
```

Exact status code expectations will be defined during API test design.

---

# 69. UI Transition Validation

For UI flows, verify:

* Only valid actions are displayed.
* Invalid actions are disabled/hidden where appropriate.
* Backend still rejects bypass attempts.
* Current state refreshes after action.
* Stale UI does not override newer server state.
* Confirmation reflects target state correctly.

---

# 70. State Transition and Audit

High-risk transitions should generate audit events.

Examples:

```text
Customer ACTIVE → SUSPENDED
Account ACTIVE → FROZEN
Card ACTIVE → BLOCKED
Loan UNDER_REVIEW → APPROVED
Transfer COMPLETED → REVERSED
KYC PENDING → VERIFIED
```

Audit should include:

* Actor
* Target
* Previous state
* New state
* Timestamp
* Reason where required
* Result

---

# 71. State Transition and Notifications

Some transitions should trigger customer notifications.

Examples:

```text
Account → FROZEN
Card → BLOCKED
Loan → APPROVED
Loan → REJECTED
Deposit → MATURED
Password/session security change
```

Verify:

* Correct customer
* Correct state
* No success message if transition failed
* No duplicate notification

---

# 72. State Transition and Transaction History

State changes that create financial effects should be reconcilable.

Examples:

```text
Loan APPROVED → DISBURSED
Deposit ACTIVE → EARLY_WITHDRAWN
Transfer COMPLETED → REVERSED
Payment COMPLETED → REVERSED
```

Verify transaction history reflects financial events without overwriting original records.

---

# 73. High-Risk State Transition Regression Set

At minimum include:

```text
Customer ACTIVE → RESTRICTED
Customer ACTIVE → SUSPENDED
Account ACTIVE → FROZEN
Account FROZEN → ACTIVE
Account ACTIVE → CLOSED
Beneficiary PENDING_ACTIVATION → ACTIVE
Beneficiary ACTIVE → DELETED
Transfer PROCESSING → COMPLETED
Transfer PROCESSING → FAILED
Transfer COMPLETED → REVERSED
Scheduled transfer SCHEDULED → CANCELLED
Payment PROCESSING → COMPLETED
Payment COMPLETED → REVERSED
Card INACTIVE → ACTIVE
Card ACTIVE → FROZEN
Card FROZEN → ACTIVE
Card ACTIVE/FROZEN → BLOCKED
Loan UNDER_REVIEW → APPROVED
Loan UNDER_REVIEW → REJECTED
Loan APPROVED → DISBURSED
Loan ACTIVE → CLOSED
Deposit PENDING → ACTIVE
Deposit ACTIVE → MATURED
Deposit MATURED → CLOSED
Deposit MATURED → RENEWED
Session AUTHENTICATED → EXPIRED
Session AUTHENTICATED → REVOKED
```

---

# 74. Automation Candidates

State transitions are strong candidates for automated regression.

Good targets:

* Account freeze/unfreeze
* Beneficiary lifecycle
* Transfer lifecycle
* Payment lifecycle
* Card lifecycle
* Loan approval/disbursement
* Deposit maturity
* Session expiry/revocation
* Notification read/unread

Suitable for:

* Selenium
* Cypress
* Playwright
* Jest
* Postman
* REST Assured
* Cucumber

---

# 75. Data-Driven Transition Model

Example card transition dataset:

```text
[
  {
    "from": "INACTIVE",
    "event": "ACTIVATE",
    "expected": "ACTIVE"
  },
  {
    "from": "ACTIVE",
    "event": "FREEZE",
    "expected": "FROZEN"
  },
  {
    "from": "FROZEN",
    "event": "UNFREEZE",
    "expected": "ACTIVE"
  },
  {
    "from": "CANCELLED",
    "event": "ACTIVATE",
    "expected": "REJECTED"
  }
]
```

---

# 76. BDD Example — Account State

```gherkin
Feature: Account state transitions

Scenario: Active account is frozen
  Given the customer account is active
  And an authorized administrator is authenticated
  When the administrator freezes the account
  Then the account status should become frozen
  And outgoing prohibited transactions should be rejected
  And the state change should be audited
```

---

# 77. BDD Example — Card State

```gherkin
Feature: Card lifecycle

Scenario: Customer freezes an active card
  Given the card is active
  When the customer freezes the card
  Then the card status should become frozen
  And new card purchases should be rejected
```

---

# 78. BDD Example — Loan State

```gherkin
Feature: Loan application lifecycle

Scenario: Authorized loan officer approves an application
  Given the loan application is under review
  And the loan officer is authorized
  When the loan officer approves the application
  Then the loan status should become approved
  And the decision should be audited
```

---

# 79. BDD Example — Invalid Transition

```gherkin
Feature: Invalid lifecycle transitions

Scenario: Closed account cannot become active
  Given an account is closed
  When an activation request is submitted
  Then the request should be rejected
  And the account should remain closed
```

---

# 80. Risk Traceability

State Transition Testing directly mitigates:

```text
RISK-003 — Duplicate transaction
RISK-004 — Partial transaction processing
RISK-009 — Frozen account can transact
RISK-013 — Concurrency corrupts financial state
RISK-014 — Session active after logout
RISK-015 — Expired session accepted
RISK-017 — Invalid beneficiary behavior
RISK-024 — Frozen card usable
RISK-025 — Blocked card usable
RISK-026 — Failed payment changes balance
RISK-028 — Scheduled transfer executes incorrectly
RISK-029 — Cancelled transfer executes
RISK-039 — API/database state inconsistent
RISK-042 — Duplicate retry caused by slow response
RISK-048 — UI success differs from actual backend state
```

---

# 81. State Transition Coverage Summary

This document covers state models for:

* Customers
* KYC
* Accounts
* Beneficiaries
* Transfers
* Scheduled transfers
* Payments
* Cards
* Loans
* Deposits
* Sessions
* Notifications
* Admin accounts
* Reversals
* Concurrency
* Idempotency
* Terminal states
* API transition validation
* Database consistency
* Audit
* Notifications
* Financial reconciliation

---

# 82. Final State Transition Testing Principle

State Transition Testing should verify more than whether a status label changes.

For every transition, QA should ask:

```text
Was the transition valid from the current state?

Was the actor authorized?

Was the latest state revalidated?

Did the expected next state persist?

Were invalid transitions blocked by the backend?

Did the transition happen exactly once?

Were financial side effects correct?

Were failed transitions financially neutral?

Was the transition audited?

Were notifications accurate?

Can two conflicting transitions race?

Can a terminal state be reopened improperly?

Do UI, API, and database agree on the final state?
```

The core rule is:

```text
A banking entity must only move through explicitly allowed lifecycle states,
and every state-changing action must preserve authorization,
financial integrity, consistency, and auditability.
```
