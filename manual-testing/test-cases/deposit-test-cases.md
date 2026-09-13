# Banking System — Deposit Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Deposits                       |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Deposit scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Deposit products
* Eligibility
* Deposit opening
* Funding
* Minimum/maximum principal
* Terms
* Interest rates
* Interest calculations
* Maturity dates
* Maturity payout
* Exactly-once payout
* Early withdrawal
* Penalties
* Renewal
* Auto-renewal
* Cancellation
* State transitions
* Concurrency
* Idempotency
* API validation
* Database validation
* Notifications
* Audit
* Authorization
* Precision and rounding
* Cross-browser behavior
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Deposit test cases use:

```text
DEP-TC-XXX
```

Examples:

```text
DEP-TC-001
DEP-TC-002
DEP-TC-003
```

---

# 4. Critical Deposit Invariants

## Invariant 1 — Principal Integrity

Opening a deposit must debit the correct funding account by the exact accepted principal.

```text
Funding Account Debit
=
Deposit Principal
+
Applicable Opening Fee
```

where applicable.

---

## Invariant 2 — Interest Accuracy

Interest must follow the configured product:

```text
Principal
+
Rate
+
Term
+
Compounding Method
+
Day Count / Product Rules
```

---

## Invariant 3 — Exactly-Once Maturity Payout

```text
One matured deposit
=
One intended maturity payout
```

Retries, jobs, callbacks, or concurrent processing must not create duplicate credits.

---

## Invariant 4 — Early Withdrawal Accuracy

Early withdrawal must calculate:

```text
Principal
+
Earned Interest
-
Penalty
-
Applicable Fees
=
Final Payout
```

according to product rules.

---

## Invariant 5 — State Integrity

A deposit must follow valid lifecycle states.

Example:

```text
PENDING_FUNDING
→ ACTIVE
→ MATURED
→ PAID_OUT
→ CLOSED
```

Possible alternate branches:

```text
CANCELLED
EARLY_WITHDRAWN
RENEWED
```

---

## Invariant 6 — Traceability

Funding, accrual, maturity, renewal, withdrawal, penalty, and payout must remain traceable.

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

## Funding Account

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

## Secondary Account

```text
Account:
ACC-003

Owner:
CUST-001

Status:
ACTIVE
```

## Frozen Funding Account

```text
Account:
ACC-004

Owner:
CUST-001

Status:
FROZEN
```

## Example Deposit Product

```text
Product:
TERM_DEPOSIT_12M

Minimum Principal:
10,000.00

Maximum Principal:
1,000,000.00

Term:
12 months

Interest Rate:
Configured by product

Early Withdrawal:
Supported with penalty

Auto-Renewal:
Supported
```

## Active Deposit

```text
Deposit:
DEP-001

Customer:
CUST-001

Status:
ACTIVE

Principal:
50,000.00
```

## Matured Deposit

```text
Deposit:
DEP-002

Customer:
CUST-001

Status:
MATURED
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer authentication works.

Deposit service is available.

Deposit products are configured.

Funding account exists.

Synthetic financial data is used.

API/DB validation is available when required.
```

---

# 7. Deposit Product Test Cases

## DEP-TC-001 — View Available Deposit Products

**Priority:** P1
**Requirement:** REQ-DEP-001

### Expected Result

Customer sees eligible deposit products with correct:

* Product name
* Minimum principal
* Maximum principal
* Term
* Interest rate
* Maturity behavior
* Early withdrawal conditions
* Renewal options

---

## DEP-TC-002 — Deposit Product Details Match API

**Priority:** P1

### Expected Result

UI values match authoritative product configuration.

---

## DEP-TC-003 — Ineligible Product

**Priority:** P1

### Expected Result

Customer cannot successfully open a product for which they are ineligible.

---

# 8. Deposit Eligibility Test Cases

## DEP-TC-004 — Eligible Customer

**Priority:** P0
**Requirement:** REQ-DEP-002

### Expected Result

Customer may proceed with deposit opening.

---

## DEP-TC-005 — Pending KYC Customer

**Priority:** P0

### Expected Result

Restricted according to business rules.

---

## DEP-TC-006 — Suspended Customer

**Priority:** P0

Expected: deposit opening denied.

---

## DEP-TC-007 — Closed Customer

**Priority:** P0

Expected: denied.

---

## DEP-TC-008 — Client Manipulates Eligibility

**Priority:** P0
**Risk:** RISK-037

Expected: backend authoritative eligibility remains enforced.

---

# 9. Deposit Opening Test Cases

## DEP-TC-009 — Open Valid Deposit

**Priority:** P0
**Requirement:** REQ-DEP-003
**Automation:** Playwright / REST Assured

### Steps

1. Login.
2. Open Deposits.
3. Select product.
4. Select funding account.
5. Enter valid principal.
6. Review rate.
7. Review term.
8. Review maturity date.
9. Confirm.

### Expected Result

* One deposit created.
* Correct customer ownership.
* Correct funding account.
* Correct principal.
* Correct product/rate/term.
* Funding processed once.
* Deposit state becomes appropriate active state.

---

## DEP-TC-010 — Duplicate Opening Submission

**Priority:** P0

### Expected Result

No unintended duplicate deposit or duplicate funding debit.

---

## DEP-TC-011 — Missing Required Field

**Priority:** P1

Expected: rejected.

---

## DEP-TC-012 — Unsupported Product

**Priority:** P1

Expected: rejected.

---

# 10. Principal Boundary Test Cases

## DEP-TC-013 — Minimum Principal

**Priority:** P1
**Type:** Boundary

Expected: accepted.

---

## DEP-TC-014 — Minimum Minus Smallest Currency Unit

**Priority:** P1

Expected: rejected.

---

## DEP-TC-015 — Maximum Principal

**Priority:** P0

Expected: accepted if funding balance permits.

---

## DEP-TC-016 — Maximum Plus Smallest Currency Unit

**Priority:** P0

Expected: rejected.

---

## DEP-TC-017 — Zero Principal

**Priority:** P0

Expected: rejected.

---

## DEP-TC-018 — Negative Principal

**Priority:** P0

Expected: rejected.

---

## DEP-TC-019 — Excess Decimal Precision

**Priority:** P1

Expected: handled according to currency precision policy.

---

# 11. Funding Balance Test Cases

## DEP-TC-020 — Sufficient Available Balance

**Priority:** P0
**Requirement:** REQ-DEP-004

Expected: funding succeeds.

---

## DEP-TC-021 — Insufficient Available Balance

**Priority:** P0

Expected:

```text
Deposit not opened.

No invalid debit.

No active deposit created.
```

---

## DEP-TC-022 — Available Balance Equals Principal

**Priority:** P0

Expected: succeeds if no additional fee and zero remaining balance is allowed.

---

## DEP-TC-023 — Available Balance Less Than Principal by Smallest Unit

**Priority:** P0

Expected: rejected.

---

## DEP-TC-024 — Current Balance Sufficient but Available Balance Insufficient

**Priority:** P0

Expected: available balance is authoritative.

---

# 12. Funding Account State Test Cases

## DEP-TC-025 — Active Funding Account

**Priority:** P0

Expected: may fund deposit.

---

## DEP-TC-026 — Frozen Funding Account

**Priority:** P0

Expected: funding rejected.

---

## DEP-TC-027 — Restricted Funding Account

**Priority:** P0

Expected: restrictions enforced.

---

## DEP-TC-028 — Closed Funding Account

**Priority:** P0

Expected: rejected.

---

## DEP-TC-029 — Account Frozen After Confirmation Loaded

**Priority:** P0
**Risk:** RISK-048

Expected: backend revalidates account state before debit.

---

# 13. Funding Authorization Test Cases

## DEP-TC-030 — Fund From Own Account

**Priority:** P0

Expected: allowed.

---

## DEP-TC-031 — Fund From Another Customer's Account

**Priority:** P0
**Risk:** RISK-047

Expected: denied.

---

## DEP-TC-032 — Manipulate Funding Account ID

**Priority:** P0

Expected: backend ownership validation rejects request.

---

## DEP-TC-033 — Unauthenticated Deposit Opening API

**Priority:** P0

Expected: denied.

---

# 14. Funding Financial Validation

## DEP-TC-034 — Funding Debit Correct

**Priority:** P0
**Risk:** RISK-001

### Example

```text
Opening Balance:
100,000.00

Principal:
50,000.00
```

Expected:

```text
Closing Balance:
50,000.00
```

when no fee applies.

---

## DEP-TC-035 — Opening Fee Applied Once

**Priority:** P0

Expected: fee handled exactly according to product rules.

---

## DEP-TC-036 — Failed Deposit Opening Financially Neutral

**Priority:** P0

Expected:

```text
Invalid Debit:
0

Active Deposit:
No
```

---

# 15. Interest Rate Test Cases

## DEP-TC-037 — Correct Product Interest Rate

**Priority:** P0
**Requirement:** REQ-DEP-005
**Risk:** RISK-012

Expected: authoritative configured rate used.

---

## DEP-TC-038 — Rate Changes by Term

**Priority:** P1

Expected: correct rate selected.

---

## DEP-TC-039 — Rate Changes by Principal Tier

**Priority:** P1

Expected: correct tier applied.

---

## DEP-TC-040 — Client Manipulates Interest Rate

**Priority:** P0
**Risk:** RISK-037

Expected: backend ignores client-supplied rate.

---

# 16. Interest Calculation Test Cases

## DEP-TC-041 — Basic Interest Calculation

**Priority:** P0

Expected: calculated according to product rules.

---

## DEP-TC-042 — Interest on Minimum Principal

**Priority:** P1

Expected: correct.

---

## DEP-TC-043 — Interest on Maximum Principal

**Priority:** P1

Expected: correct.

---

## DEP-TC-044 — Interest for Shortest Term

**Priority:** P1

Expected: correct.

---

## DEP-TC-045 — Interest for Longest Term

**Priority:** P1

Expected: correct.

---

## DEP-TC-046 — Compounded Interest

**Priority:** P0

Where supported.

Expected: correct compounding frequency/formula.

---

## DEP-TC-047 — Simple Interest Product

**Priority:** P0

Where supported.

Expected: correct simple interest.

---

# 17. Interest Precision and Rounding

## DEP-TC-048 — Interest Rounding

**Priority:** P0
**Risk:** RISK-012

Expected: configured rounding method used.

---

## DEP-TC-049 — Fractional Currency Interest

**Priority:** P0

Expected: no floating-point corruption.

---

## DEP-TC-050 — Repeated Accrual Periods

**Priority:** P0

Expected: cumulative interest reconciles exactly.

---

## DEP-TC-051 — Final Interest Adjustment

**Priority:** P0

Expected: no unexplained fractional remainder at maturity.

---

# 18. Term and Maturity Date Test Cases

## DEP-TC-052 — Correct Maturity Date

**Priority:** P0
**Requirement:** REQ-DEP-006

Expected: maturity date follows product term.

---

## DEP-TC-053 — Leap-Year Maturity

**Priority:** P1

Expected: calculated according to business date rules.

---

## DEP-TC-054 — Month-End Opening

**Priority:** P1

Expected: maturity date follows configured month-end convention.

---

## DEP-TC-055 — Weekend/Holiday Maturity

**Priority:** P1

Expected: payout date follows configured business-calendar rule.

---

## DEP-TC-056 — Timezone Handling

**Priority:** P1
**Risk:** RISK-043

Expected: maturity occurs at intended business date/time.

---

# 19. Deposit State Test Cases

## DEP-TC-057 — PENDING_FUNDING

**Priority:** P1

Expected: deposit not yet earning/active according to product rules.

---

## DEP-TC-058 — ACTIVE

**Priority:** P0

Expected: accrual and permitted actions available.

---

## DEP-TC-059 — MATURED

**Priority:** P0

Expected: deposit eligible for payout/renewal according to instructions.

---

## DEP-TC-060 — PAID_OUT

**Priority:** P0

Expected: no second payout.

---

## DEP-TC-061 — EARLY_WITHDRAWN

**Priority:** P0

Expected: no maturity payout later.

---

## DEP-TC-062 — CANCELLED

**Priority:** P0

Expected: cannot later activate unless valid supported process exists.

---

## DEP-TC-063 — CLOSED

**Priority:** P0

Expected: terminal state where designed.

---

# 20. State Transition Test Cases

## DEP-TC-064 — PENDING_FUNDING → ACTIVE

**Priority:** P0

Expected: only after successful funding.

---

## DEP-TC-065 — ACTIVE → MATURED

**Priority:** P0

Expected: only when maturity condition reached.

---

## DEP-TC-066 — MATURED → PAID_OUT

**Priority:** P0

Expected: after successful payout.

---

## DEP-TC-067 — ACTIVE → EARLY_WITHDRAWN

**Priority:** P0

Expected: only through valid early withdrawal.

---

## DEP-TC-068 — PAID_OUT → ACTIVE

**Priority:** P0

Expected: rejected unless a new renewed deposit entity is created.

---

## DEP-TC-069 — CLOSED → ACTIVE

**Priority:** P0

Expected: rejected.

---

# 21. Maturity Payout Test Cases

## DEP-TC-070 — Valid Maturity Payout

**Priority:** P0
**Requirement:** REQ-DEP-007

### Example

```text
Principal:
50,000.00

Maturity Interest:
5,000.00
```

Expected payout:

```text
55,000.00
```

subject to applicable tax/fees/product rules.

---

## DEP-TC-071 — Payout to Correct Destination Account

**Priority:** P0

Expected: payout credited only to authorized configured destination.

---

## DEP-TC-072 — Payout Amount Correct

**Priority:** P0

Expected:

```text
Principal
+
Earned Interest
-
Applicable Deductions
=
Payout
```

---

## DEP-TC-073 — Maturity Notification

**Priority:** P1

Expected: customer receives accurate maturity/payout information.

---

# 22. Exactly-Once Maturity Payout

## DEP-TC-074 — Duplicate Maturity Job

**Priority:** P0
**Requirement:** REQ-DEP-008
**Risk:** RISK-006? Use duplicate financial risk RISK-003 / RISK-009 project context

Expected:

```text
Payout Count:
1
```

---

## DEP-TC-075 — Retry Payout After Timeout

**Priority:** P0
**Risk:** RISK-042

Expected: no duplicate credit.

---

## DEP-TC-076 — Same Idempotency Key for Payout

**Priority:** P0

Expected: one financial effect.

---

## DEP-TC-077 — Concurrent Maturity Workers

**Priority:** P0
**Risk:** RISK-013

Expected: one payout only.

---

## DEP-TC-078 — Manually Trigger Payout After Automatic Payout

**Priority:** P0

Expected: rejected.

---

# 23. Maturity Database Validation

## DEP-TC-079 — One Payout Record

**Priority:** P0
**Automation:** SQL

Expected: one authoritative payout.

---

## DEP-TC-080 — One Account Credit

**Priority:** P0

Expected: account credited once.

---

## DEP-TC-081 — Unique Payout Reference

**Priority:** P0
**Risk:** RISK-033

Expected: unique reference.

---

## DEP-TC-082 — Deposit State Matches Payout

**Priority:** P0

Expected: `PAID_OUT`/`CLOSED` state consistent with actual credit.

---

# 24. Early Withdrawal Test Cases

## DEP-TC-083 — Request Early Withdrawal

**Priority:** P0
**Requirement:** REQ-DEP-009

Expected: customer receives clear calculation before confirmation.

---

## DEP-TC-084 — Early Withdrawal Principal Return

**Priority:** P0

Expected: principal treatment matches product rules.

---

## DEP-TC-085 — Early Withdrawal Earned Interest

**Priority:** P0

Expected: earned/reduced interest calculated correctly.

---

## DEP-TC-086 — Early Withdrawal Penalty

**Priority:** P0
**Risk:** RISK-012

Expected: penalty calculated exactly once.

---

## DEP-TC-087 — Early Withdrawal Fee

**Priority:** P1

Expected: applicable fee displayed before confirmation.

---

## DEP-TC-088 — Customer Cancels Early Withdrawal Before Confirmation

**Priority:** P1

Expected: deposit remains unchanged.

---

## DEP-TC-089 — Early Withdrawal Completed

**Priority:** P0

Expected:

* Correct payout.
* Deposit enters correct terminal state.
* No maturity payout later.

---

# 25. Early Withdrawal Boundary Test Cases

## DEP-TC-090 — Withdrawal Immediately After Opening

**Priority:** P1

Expected: follows minimum holding/penalty rules.

---

## DEP-TC-091 — Withdrawal One Day Before Maturity

**Priority:** P0

Expected: correct near-maturity penalty/interest rules.

---

## DEP-TC-092 — Withdrawal At Maturity Boundary

**Priority:** P0

Expected: deterministic classification as early withdrawal or maturity payout according to authoritative time.

---

# 26. Duplicate Early Withdrawal Test Cases

## DEP-TC-093 — Double-Click Early Withdrawal Confirmation

**Priority:** P0

Expected: one payout only.

---

## DEP-TC-094 — Retry After Early Withdrawal Timeout

**Priority:** P0

Expected: no duplicate payout.

---

## DEP-TC-095 — Two Concurrent Early Withdrawals

**Priority:** P0

Expected: at most one succeeds.

---

# 27. Early Withdrawal Penalty Validation

## DEP-TC-096 — Fixed Penalty

**Priority:** P0

Expected: correct.

---

## DEP-TC-097 — Percentage Penalty

**Priority:** P0

Expected: correct.

---

## DEP-TC-098 — Penalty Cap

**Priority:** P1

Expected: cap applied correctly.

---

## DEP-TC-099 — Penalty Applied Twice

**Priority:** P0

Expected: duplicate penalty prevented.

---

## DEP-TC-100 — Client Manipulates Penalty

**Priority:** P0

Expected: backend calculates authoritative value.

---

# 28. Renewal Test Cases

## DEP-TC-101 — Enable Auto-Renewal

**Priority:** P1
**Requirement:** REQ-DEP-010

Expected: renewal instruction saved.

---

## DEP-TC-102 — Disable Auto-Renewal

**Priority:** P1

Expected: maturity will follow payout rules instead.

---

## DEP-TC-103 — Auto-Renew at Maturity

**Priority:** P0

Expected:

* Original deposit lifecycle completed.
* New renewed term created or state updated according to design.
* Principal handling correct.
* Rate uses current renewal rules.

---

## DEP-TC-104 — Renew Principal Only

**Priority:** P0

Where supported.

Expected: interest paid out separately and principal renewed.

---

## DEP-TC-105 — Renew Principal + Interest

**Priority:** P0

Expected: renewed principal equals correct matured amount.

---

## DEP-TC-106 — Auto-Renewal Disabled Before Maturity

**Priority:** P0

Expected: no renewal occurs.

---

# 29. Renewal Rate Test Cases

## DEP-TC-107 — Renewal Uses Current Rate

**Priority:** P0

If product rule specifies current rate.

Expected: old rate is not incorrectly reused.

---

## DEP-TC-108 — Renewal Uses Guaranteed Rate

**Priority:** P0

If product guarantees renewal rate.

Expected: contract rule honored.

---

## DEP-TC-109 — Client Manipulates Renewal Rate

**Priority:** P0

Expected: backend authoritative rate enforced.

---

# 30. Renewal Concurrency Test Cases

## DEP-TC-110 — Renewal and Payout Trigger Concurrently

**Priority:** P0
**Risk:** RISK-013

Expected: exactly one valid maturity path occurs.

System must not both renew principal and fully pay it out incorrectly.

---

## DEP-TC-111 — Two Renewal Jobs Run Concurrently

**Priority:** P0

Expected: one renewal only.

---

# 31. Deposit Cancellation Test Cases

## DEP-TC-112 — Cancel Before Funding

**Priority:** P1

Expected: no financial debit and state becomes cancelled.

---

## DEP-TC-113 — Cancel After Funding / Before Activation

**Priority:** P0

Expected: handled according to business rules with full reconciliation.

---

## DEP-TC-114 — Cancel Active Deposit Through Unsupported Flow

**Priority:** P0

Expected: rejected; early withdrawal flow required.

---

## DEP-TC-115 — Cancel Matured/Paid Deposit

**Priority:** P0

Expected: rejected.

---

# 32. Concurrent Funding Test Cases

## DEP-TC-116 — Two Deposit Openings Compete for Same Balance

**Priority:** P0
**Risk:** RISK-013

Example:

```text
Available:
60,000.00

Deposit A:
40,000.00

Deposit B:
30,000.00
```

Expected: both cannot be funded if overdraft is unsupported.

---

## DEP-TC-117 — Deposit Funding and Transfer Compete for Funds

**Priority:** P0

Expected: available balance enforced atomically.

---

## DEP-TC-118 — Deposit Funding and Payment Compete for Funds

**Priority:** P0

Expected: no overspending.

---

# 33. API Deposit Test Cases

## DEP-TC-119 — Get Own Deposit API

**Priority:** P0
**Automation:** REST Assured

Expected: succeeds.

---

## DEP-TC-120 — Get Another Customer's Deposit

**Priority:** P0
**Risk:** RISK-047

Expected: denied.

---

## DEP-TC-121 — Create Deposit API

**Priority:** P0

Expected: one valid deposit created/funded.

---

## DEP-TC-122 — Unauthorized Funding Account

**Priority:** P0

Expected: denied.

---

## DEP-TC-123 — Manipulate Interest Rate

**Priority:** P0

Expected: ignored/rejected.

---

## DEP-TC-124 — Manipulate Maturity Amount

**Priority:** P0

Expected: server calculates authoritative value.

---

## DEP-TC-125 — Manipulate Deposit Status

**Priority:** P0

Expected: lifecycle cannot be client-controlled.

---

## DEP-TC-126 — Manipulate Penalty

**Priority:** P0

Expected: ignored/rejected.

---

# 34. Database Validation Test Cases

## DEP-TC-127 — Deposit Ownership

**Priority:** P0
**Automation:** SQL

Expected: correct customer relationship.

---

## DEP-TC-128 — Funding Account Relationship

**Priority:** P0

Expected: correct account reference.

---

## DEP-TC-129 — Principal Persistence

**Priority:** P0

Expected: exact accepted principal.

---

## DEP-TC-130 — Interest Rate Persistence

**Priority:** P0

Expected: authoritative rate stored.

---

## DEP-TC-131 — Maturity Date Persistence

**Priority:** P0

Expected: correct date.

---

## DEP-TC-132 — State Persistence

**Priority:** P0

Expected: latest valid lifecycle state.

---

## DEP-TC-133 — Payout Uniqueness

**Priority:** P0

Expected: one maturity/early-withdrawal payout per valid event.

---

## DEP-TC-134 — Renewal Relationship

**Priority:** P0

Expected: original and renewed deposit traceable.

---

# 35. UI/API/Database Consistency

## DEP-TC-135 — Deposit Status Cross-Layer Validation

**Priority:** P0
**Risk:** RISK-039, RISK-048

Expected:

```text
UI
=
API
=
DB
```

for status.

---

## DEP-TC-136 — Principal Cross-Layer Validation

**Priority:** P0

Expected: principal matches.

---

## DEP-TC-137 — Rate Cross-Layer Validation

**Priority:** P0

Expected: rate matches.

---

## DEP-TC-138 — Maturity/Payout Cross-Layer Validation

**Priority:** P0

Expected: maturity result, payout amount, and account balance reconcile.

---

# 36. Transaction History Test Cases

## DEP-TC-139 — Deposit Funding Appears in History

**Priority:** P0

Expected: correct debit/reference.

---

## DEP-TC-140 — Maturity Payout Appears in History

**Priority:** P0

Expected: correct credit/reference.

---

## DEP-TC-141 — Early Withdrawal Appears in History

**Priority:** P0

Expected: payout and penalty representation correct.

---

## DEP-TC-142 — Renewal Appears Correctly

**Priority:** P1

Expected: transaction/history representation follows product design.

---

# 37. Statement Integration Test Cases

## DEP-TC-143 — Deposit Funding Appears in Statement

**Priority:** P0

Expected: correct debit.

---

## DEP-TC-144 — Maturity Payout Appears in Statement

**Priority:** P0

Expected: correct credit.

---

## DEP-TC-145 — Early Withdrawal Penalty Appears Correctly

**Priority:** P0

Expected: statement reconciles.

---

## DEP-TC-146 — Renewal Payout/Reinvestment Reconciles

**Priority:** P0

Expected: no unexplained duplication or missing amount.

---

# 38. Notification Test Cases

## DEP-TC-147 — Deposit Opened Notification

**Priority:** P1

Expected: correct principal/term/product.

---

## DEP-TC-148 — Upcoming Maturity Notification

**Priority:** P1

Expected: correct maturity date.

---

## DEP-TC-149 — Maturity Payout Notification

**Priority:** P1

Expected: correct payout amount.

---

## DEP-TC-150 — Early Withdrawal Notification

**Priority:** P1

Expected: reflects penalty and final payout accurately.

---

## DEP-TC-151 — Renewal Notification

**Priority:** P1

Expected: correct renewed amount/term/rate.

---

## DEP-TC-152 — Failed Deposit Opening Sends No Success Notification

**Priority:** P0

Expected: no false success.

---

# 39. Audit Test Cases

## DEP-TC-153 — Deposit Creation Trace

**Priority:** P1

Expected: ownership/product/principal traceable.

---

## DEP-TC-154 — Early Withdrawal Audit

**Priority:** P0
**Risk:** RISK-022

Expected audit includes:

```text
Actor

Deposit

Principal

Interest

Penalty

Payout

Timestamp
```

---

## DEP-TC-155 — Renewal Instruction Change Audit

**Priority:** P1

Expected: old/new instruction traceable.

---

## DEP-TC-156 — Manual Administrative Adjustment Audit

**Priority:** P0

Expected: actor/reason/amount recorded.

---

# 40. Error Handling Test Cases

## DEP-TC-157 — Deposit Service Fails Before Funding

**Priority:** P1

Expected: no debit and no active deposit.

---

## DEP-TC-158 — Funding Debit Succeeds but Deposit Creation Fails

**Priority:** P0

Expected: system rolls back/compensates safely.

Customer must not lose money without valid deposit.

---

## DEP-TC-159 — Deposit Created but Funding Debit Fails

**Priority:** P0

Expected: deposit cannot remain falsely active/funded.

---

## DEP-TC-160 — Maturity Payout Service Failure

**Priority:** P0

Expected: payout remains recoverable without duplicate credit.

---

## DEP-TC-161 — Renewal Job Failure

**Priority:** P0

Expected: customer funds remain accounted for and final state recoverable.

---

# 41. Security Input Test Cases

## DEP-TC-162 — Script-Like Deposit Alias

**Priority:** P1

Expected: safely encoded.

---

## DEP-TC-163 — SQL-Like Input

**Priority:** P1

Expected: no injection or database error.

---

## DEP-TC-164 — Protected Fields in Opening Payload

**Priority:** P0

Example:

```json
{
  "principal": 50000,
  "interestRate": 99,
  "status": "MATURED",
  "maturityAmount": 999999
}
```

Expected: protected fields ignored/rejected.

---

# 42. Cross-Browser Test Cases

## DEP-TC-165 — Deposit Flow in Chrome

**Priority:** P2

Expected: works.

---

## DEP-TC-166 — Deposit Flow in Edge

**Priority:** P2

Expected: works.

---

## DEP-TC-167 — Deposit Flow in Firefox

**Priority:** P2

Expected: works.

---

## DEP-TC-168 — Deposit Flow in WebKit

**Priority:** P2

Expected: works.

---

# 43. Responsive Test Cases

## DEP-TC-169 — Open Deposit at 390×844

**Priority:** P1

Expected:

* Principal visible.
* Rate visible.
* Term visible.
* Maturity date visible.
* Confirmation accessible.

---

## DEP-TC-170 — Early Withdrawal at 360×800

**Priority:** P0

Expected:

* Principal visible.
* Earned interest visible.
* Penalty visible.
* Final payout visible.
* Confirm button reachable.

---

## DEP-TC-171 — Deposit Schedule/Details on Mobile

**Priority:** P2

Expected: financial values remain readable.

---

# 44. Accessibility Test Cases

## DEP-TC-172 — Keyboard Deposit Opening

**Priority:** P2

Expected: form operable via keyboard.

---

## DEP-TC-173 — Financial Labels Accessible

**Priority:** P2

Expected: principal/rate/term/interest/payout clearly labeled.

---

## DEP-TC-174 — Deposit State Not Communicated by Color Alone

**Priority:** P2

Expected: semantic/textual state available.

---

# 45. End-to-End Deposit Opening Journey

## DEP-TC-175 — Product → Funding → Active Deposit

**Priority:** P0

### Test Data

```text
Funding Account Opening:
100,000.00

Deposit Principal:
50,000.00
```

### Expected

```text
Funding Account Closing:
50,000.00

Deposit Principal:
50,000.00

Deposit State:
ACTIVE
```

### Validate

* UI
* API
* DB
* Account history
* Statement
* Notification

---

# 46. End-to-End Failed Opening

## DEP-TC-176 — Insufficient Funding Balance

**Priority:** P0

Expected:

```text
Deposit Created as Active:
No

Permanent Debit:
0

Success Notification:
No
```

---

# 47. End-to-End Maturity Journey

## DEP-TC-177 — Active → Matured → Payout → Closed

**Priority:** P0

### Steps

1. Start with active deposit.
2. Reach maturity condition.
3. Calculate interest.
4. Process payout.
5. Validate destination account.
6. Validate history.
7. Validate statement.
8. Validate DB state.

### Expected Result

Exactly one correct payout.

---

# 48. End-to-End Duplicate Maturity Protection

## DEP-TC-178 — Concurrent Maturity Jobs + Retry

**Priority:** P0

### Steps

1. Mature deposit.
2. Trigger multiple payout workers.
3. Simulate timeout.
4. Retry.
5. Inspect account and database.

### Expected Result

```text
Maturity Payout Count:
1

Account Credits:
1

Deposit Final State:
PAID_OUT / CLOSED
```

---

# 49. End-to-End Early Withdrawal

## DEP-TC-179 — Active → Early Withdrawal → Reconcile

**Priority:** P0

### Validate

```text
Principal
+
Allowed Interest
-
Penalty
-
Fee
=
Final Payout
```

Expected:

* One payout.
* Correct penalty.
* Deposit cannot mature again later.
* History and statement reconcile.

---

# 50. End-to-End Renewal

## DEP-TC-180 — Active → Matured → Auto-Renewed

**Priority:** P0

Expected:

* Original term traceable.
* Renewal instruction respected.
* Correct renewed principal.
* Correct new rate.
* Correct new maturity date.
* No unintended full payout if reinvestment selected.

---

# 51. End-to-End Maturity vs Early Withdrawal Race

## DEP-TC-181 — Early Withdrawal at Maturity Boundary

**Priority:** P0
**Risk:** RISK-013, RISK-043

### Steps

1. Deposit approaches maturity.
2. Submit early withdrawal near maturity boundary.
3. Automatic maturity job runs concurrently.
4. Inspect final state.

### Expected Result

Only one financial completion path occurs.

The system must not:

```text
Early-withdraw
AND
Pay maturity proceeds
```

for the same principal.

---

# 52. End-to-End Renewal vs Payout Race

## DEP-TC-182 — Auto-Renew and Payout Concurrently

**Priority:** P0

### Expected Result

Only one valid configured maturity instruction is executed.

No duplicate use of principal.

---

# 53. Deposit Risk Mapping

| Risk                                  | Related Test Cases                                  |
| ------------------------------------- | --------------------------------------------------- |
| RISK-001 Incorrect balance            | DEP-TC-020–036, 070–100, 127–161, 175–182           |
| RISK-003 Duplicate transaction        | DEP-TC-010, 074–078, 093–095, 110–111, 133, 178–182 |
| RISK-012 Deposit interest calculation | DEP-TC-037–051, 083–100                             |
| RISK-013 Concurrency corruption       | DEP-TC-077, 095, 110–111, 116–118, 181–182          |
| RISK-019 History inconsistency        | DEP-TC-139–146                                      |
| RISK-021 Sensitive exposure           | DEP-TC-119–126, 162–164                             |
| RISK-022 Audit gap                    | DEP-TC-153–156                                      |
| RISK-030 Unauthorized API             | DEP-TC-119–126                                      |
| RISK-033 Duplicate reference          | DEP-TC-081                                          |
| RISK-037 Frontend-only validation     | DEP-TC-008, 040, 100, 109, 123–126, 164             |
| RISK-039 API/DB inconsistency         | DEP-TC-127–138                                      |
| RISK-042 Retry duplication            | DEP-TC-075, 094, 160, 178                           |
| RISK-043 Timezone/date issue          | DEP-TC-052–056, 181                                 |
| RISK-047 IDOR                         | DEP-TC-031–032, 120, 122                            |
| RISK-048 UI/backend mismatch          | DEP-TC-029, 135–138                                 |

---

# 54. Requirements Mapping

| Requirement                                  | Test Cases     |
| -------------------------------------------- | -------------- |
| REQ-DEP-001 Deposit products                 | DEP-TC-001–003 |
| REQ-DEP-002 Eligibility                      | DEP-TC-004–008 |
| REQ-DEP-003 Deposit opening                  | DEP-TC-009–019 |
| REQ-DEP-004 Funding                          | DEP-TC-020–036 |
| REQ-DEP-005 Interest/rate                    | DEP-TC-037–051 |
| REQ-DEP-006 Maturity date                    | DEP-TC-052–056 |
| REQ-DEP-007 Maturity payout                  | DEP-TC-057–073 |
| REQ-DEP-008 Exactly-once payout              | DEP-TC-074–082 |
| REQ-DEP-009 Early withdrawal                 | DEP-TC-083–100 |
| REQ-DEP-010 Renewal                          | DEP-TC-101–111 |
| REQ-DEP-011 State/concurrency/reconciliation | DEP-TC-112–182 |

---

# 55. Smoke Candidates

Recommended deposit smoke coverage:

```text
DEP-TC-001
DEP-TC-004
DEP-TC-009
DEP-TC-020
DEP-TC-026
DEP-TC-037
DEP-TC-052
DEP-TC-070
DEP-TC-074
DEP-TC-083
DEP-TC-103
DEP-TC-119
DEP-TC-135
```

---

# 56. Sanity Candidates

After deposit changes:

```text
DEP-TC-009
DEP-TC-013
DEP-TC-020
DEP-TC-021
DEP-TC-026
DEP-TC-034
DEP-TC-037
DEP-TC-041
DEP-TC-048
DEP-TC-052
DEP-TC-070
DEP-TC-074
DEP-TC-083
DEP-TC-086
DEP-TC-089
DEP-TC-103
DEP-TC-127
DEP-TC-135
```

---

# 57. Critical Regression Candidates

```text
DEP-TC-004–012

DEP-TC-013–051

DEP-TC-052–111

DEP-TC-112–164

DEP-TC-169–182
```

---

# 58. UI Automation Candidates

Best suited for:

```text
Playwright

Selenium

Cypress
```

Strong candidates:

```text
DEP-TC-001–029

DEP-TC-037–115

DEP-TC-139–180
```

---

# 59. API Automation Candidates

Best suited for:

```text
REST Assured

Postman
```

Strong candidates:

```text
DEP-TC-004–164

DEP-TC-175–182
```

particularly:

```text
Eligibility

Funding authorization

Interest-rate integrity

State transitions

Maturity idempotency

Early withdrawal

Renewal

IDOR

Protected-field manipulation
```

---

# 60. SQL / Database Testing Candidates

Strong SQL candidates:

```text
DEP-TC-009–010

DEP-TC-020–036

DEP-TC-037–111

DEP-TC-127–156

DEP-TC-175–182
```

Database validation should verify:

```text
Deposit ownership

Funding account

Principal

Interest rate

Interest accrual

Maturity date

Deposit state

Payout uniqueness

Early withdrawal

Penalty

Renewal relationship

Transaction references

Audit records
```

---

# 61. Jest Candidates

Deposit calculation logic is highly suitable for Jest tests covering:

```text
Interest calculations

Rate tiers

Compounding

Rounding

Maturity amount

Early-withdrawal penalty

Final payout

Renewal amount
```

High-value mappings:

```text
DEP-TC-037–051

DEP-TC-070–072

DEP-TC-083–100

DEP-TC-103–109
```

---

# 62. Performance / JMeter Candidates

Strong concurrency/performance candidates:

```text
DEP-TC-010

DEP-TC-074–078

DEP-TC-093–095

DEP-TC-110–111

DEP-TC-116–118

DEP-TC-160

DEP-TC-178

DEP-TC-181

DEP-TC-182
```

Performance testing must validate more than latency.

Also verify:

```text
Funding debit count

Maturity payout count

Renewal count

Duplicate count

Balance integrity

Deposit state consistency
```

---

# 63. Test Evidence Requirements

For critical deposit tests, capture as applicable:

```text
Customer ID

Deposit ID

Product

Funding account

Principal

Interest rate

Term

Opening account balance

Funding debit

Accrued interest

Penalty

Maturity amount

Payout amount

Renewal amount

Deposit state

Maturity date

Transaction reference

Idempotency key

API request/response

DB rows

Audit record

Notification

Timestamp

Defect ID
```

---

# 64. Deposit Defect Examples

Potential Critical/High defects include:

```text
Deposit opens without sufficient funds.

Funding account debited but deposit not created.

Deposit created without funding debit.

Incorrect interest rate applied.

Interest calculation incorrect.

Maturity payout occurs twice.

Early withdrawal penalty charged twice.

Early withdrawal also receives maturity payout later.

Renewal and payout both execute.

Deposit funded from another customer's account.

Maturity payout goes to wrong account.

Deposit state differs between UI/API/DB.

Cancelled deposit becomes active.

Paid-out deposit pays again.

Timezone issue causes early/late maturity.

Concurrent openings overspend source account.
```

---

# 65. Deposit Release Blockers

Release should normally be blocked by unresolved issues involving:

```text
Incorrect principal debit

Deposit created without corresponding funding

Funding debit without valid deposit

Incorrect interest with material impact

Duplicate maturity payout

Wrong maturity payout amount

Wrong payout destination

Duplicate early-withdrawal payout

Incorrect early-withdrawal penalty

Renewal/payout duplication

Unauthorized funding account

Concurrent overspending

Critical state inconsistency

Deposit IDOR/customer data exposure
```

---

# 66. Deposit Exit Criteria

Deposit testing is acceptable when:

```text
Eligibility is enforced.

Deposit opening validates correctly.

Funding is authorized and exact.

Insufficient funds are rejected.

Interest rates are authoritative.

Interest calculations reconcile.

Maturity dates are correct.

Maturity payout occurs exactly once.

Early withdrawal calculates correctly.

Penalties apply exactly once.

Renewal behaves correctly.

Invalid state transitions are blocked.

Concurrency cannot duplicate payout or overspend accounts.

History and statements reconcile.

UI/API/DB state agrees.

Critical actions are audited.

No unresolved Critical/P0 deposit defect remains.
```

---

# 67. Final Deposit Testing Principle

A deposit is not simply:

```text
Money placed into a savings product.
```

It is a time-dependent financial contract involving:

```text
Principal

Rate

Term

Interest Accrual

Maturity

Payout

Penalty

Withdrawal

Renewal
```

QA must ensure that every stage describes the same financial reality.

The most important deposit invariant is:

```text
One funded deposit
must result in one correct lifecycle
and one correct final disposition of its principal.
```

At maturity, the system must never:

```text
Pay the deposit twice.

Renew and fully pay it out unintentionally.

Lose the customer's principal.

Apply the wrong interest.

Apply a penalty twice.
```

The core rule is:

```text
From funding through maturity, withdrawal, or renewal,
the principal and every financial adjustment
must remain accurate, traceable, and processed exactly once.
```
