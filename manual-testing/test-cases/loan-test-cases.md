# Banking System — Loan Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Loans                          |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Loan scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Loan products
* Eligibility
* Loan applications
* Amount and term validation
* Interest calculation
* Fees
* Approval
* Rejection
* Role-based decisions
* Disbursement
* Exactly-once disbursement
* Installment schedules
* Repayments
* Partial repayments
* Overpayments
* Early settlement
* Overdue/default states
* Penalties
* Loan closure
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

Loan test cases use:

```text
LOAN-TC-XXX
```

Examples:

```text
LOAN-TC-001
LOAN-TC-002
LOAN-TC-003
```

---

# 4. Critical Loan Invariants

## Invariant 1 — Eligibility

Only customers satisfying authoritative eligibility requirements may proceed with restricted loan products.

---

## Invariant 2 — Authorized Decision

Loan approval or rejection must only be performed by an authorized role.

---

## Invariant 3 — Exactly-Once Disbursement

```text
One approved loan
=
One intended disbursement
```

Repeated requests, retries, callbacks, or concurrent processing must not create duplicate credits.

---

## Invariant 4 — Financial Accuracy

Loan calculations must correctly determine:

```text
Principal

Interest

Fees

Installment Amount

Outstanding Principal

Outstanding Interest

Penalties

Settlement Amount
```

---

## Invariant 5 — Repayment Integrity

One intended repayment must reduce the loan obligation and customer account balance exactly once.

---

## Invariant 6 — State Integrity

A loan must follow valid lifecycle transitions.

Example:

```text
DRAFT
→ SUBMITTED
→ UNDER_REVIEW
→ APPROVED
→ DISBURSED
→ ACTIVE
→ CLOSED
```

Alternative valid branches may include:

```text
REJECTED

CANCELLED

OVERDUE

DEFAULTED
```

---

## Invariant 7 — Traceability

Loan application decisions, disbursements, repayments, penalties, settlements, and administrative actions must remain traceable.

---

# 5. Common Test Data

## Eligible Customer

```text
Customer:
CUST-001

Status:
ACTIVE

KYC:
VERIFIED

Loan Eligibility:
ELIGIBLE
```

## Ineligible Customer

```text
Customer:
CUST-003

Status:
ACTIVE

KYC:
PENDING

Loan Eligibility:
NOT_ELIGIBLE
```

## Loan Officer

```text
Admin:
ADMIN-004

Role:
LOAN_OFFICER
```

## Read-Only Admin

```text
Admin:
ADMIN-007

Role:
READ_ONLY_ADMIN
```

## Funding Account

```text
Account:
ACC-001

Owner:
CUST-001

Status:
ACTIVE
```

## Sample Loan Product

```text
Product:
PERSONAL_LOAN

Minimum Amount:
10,000.00

Maximum Amount:
500,000.00

Minimum Term:
6 months

Maximum Term:
60 months
```

## Approved Loan

```text
Loan:
LOAN-001

Customer:
CUST-001

Status:
APPROVED

Principal:
100,000.00
```

## Active Loan

```text
Loan:
LOAN-002

Customer:
CUST-001

Status:
ACTIVE
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer authentication works.

Loan service is available.

Loan products are configured.

Required synthetic customers exist.

Required accounts exist.

API/DB validation is available when required.

No real lending or customer data is used.
```

---

# 7. Loan Product Test Cases

## LOAN-TC-001 — View Available Loan Products

**Priority:** P1
**Requirement:** REQ-LOAN-001

### Expected Result

Eligible customer sees applicable loan products with correct:

* Product name
* Amount range
* Term range
* Interest information
* Fees
* Eligibility conditions

---

## LOAN-TC-002 — Ineligible Product Hidden or Restricted

**Priority:** P1

### Expected Result

Customer cannot successfully apply for an unavailable/ineligible product.

---

## LOAN-TC-003 — Loan Product Details Match API

**Priority:** P1

### Expected Result

Displayed rates, limits, and terms match authoritative backend configuration.

---

# 8. Eligibility Test Cases

## LOAN-TC-004 — Eligible Customer

**Priority:** P0
**Requirement:** REQ-LOAN-002

### Expected Result

Customer may proceed with loan application.

---

## LOAN-TC-005 — Pending KYC Customer

**Priority:** P0

### Expected Result

Loan application restricted according to business rules.

---

## LOAN-TC-006 — Rejected KYC Customer

**Priority:** P0

Expected: application rejected.

---

## LOAN-TC-007 — Suspended Customer

**Priority:** P0

Expected: application rejected.

---

## LOAN-TC-008 — Closed Customer

**Priority:** P0

Expected: application rejected.

---

## LOAN-TC-009 — Client Manipulates Eligibility Flag

**Priority:** P0
**Risk:** RISK-037

### Expected Result

Backend independently calculates/validates eligibility.

---

# 9. Loan Application Test Cases

## LOAN-TC-010 — Submit Valid Loan Application

**Priority:** P0
**Requirement:** REQ-LOAN-003
**Automation:** Playwright / REST Assured

### Steps

1. Login as eligible customer.
2. Open Loans.
3. Choose product.
4. Enter valid amount.
5. Choose valid term.
6. Review estimated payment.
7. Submit.

### Expected Result

* One application created.
* Correct customer assigned.
* Correct product/amount/term stored.
* Application reaches expected submitted/review state.
* Reference generated.

---

## LOAN-TC-011 — Duplicate Application Submission

**Priority:** P0

### Steps

Rapidly submit the same application twice.

### Expected Result

Duplicate unintended applications are prevented or handled according to documented rules.

---

## LOAN-TC-012 — Application With Missing Required Field

**Priority:** P1

Expected: rejected with validation.

---

## LOAN-TC-013 — Unsupported Loan Product

**Priority:** P1

Expected: rejected.

---

# 10. Loan Amount Boundary Test Cases

## LOAN-TC-014 — Minimum Loan Amount

**Priority:** P1
**Type:** Boundary

Expected: accepted if otherwise eligible.

---

## LOAN-TC-015 — Minimum Minus Smallest Currency Unit

**Priority:** P1

Expected: rejected.

---

## LOAN-TC-016 — Maximum Loan Amount

**Priority:** P0

Expected: accepted if eligibility permits.

---

## LOAN-TC-017 — Maximum Plus Smallest Currency Unit

**Priority:** P0

Expected: rejected.

---

## LOAN-TC-018 — Zero Loan Amount

**Priority:** P0

Expected: rejected.

---

## LOAN-TC-019 — Negative Loan Amount

**Priority:** P0

Expected: rejected.

---

## LOAN-TC-020 — Excess Decimal Precision

**Priority:** P1

Expected: rejected or rounded according to financial rules.

---

# 11. Loan Term Boundary Test Cases

## LOAN-TC-021 — Minimum Term

**Priority:** P1

Expected: accepted.

---

## LOAN-TC-022 — Below Minimum Term

**Priority:** P1

Expected: rejected.

---

## LOAN-TC-023 — Maximum Term

**Priority:** P1

Expected: accepted.

---

## LOAN-TC-024 — Above Maximum Term

**Priority:** P1

Expected: rejected.

---

## LOAN-TC-025 — Unsupported Term Interval

**Priority:** P2

Example:

```text
13 months
```

when only configured term options are valid.

Expected: rejected or mapped according to product rules.

---

# 12. Interest Calculation Test Cases

## LOAN-TC-026 — Basic Interest Calculation

**Priority:** P0
**Requirement:** REQ-LOAN-004
**Risk:** RISK-011

### Expected Result

Displayed/calculated interest matches configured product formula.

---

## LOAN-TC-027 — Interest Rate Boundary

**Priority:** P0

Expected: configured rate used exactly.

---

## LOAN-TC-028 — Interest Changes by Term

**Priority:** P1

Expected: correct product rate/total according to selected term.

---

## LOAN-TC-029 — Interest Changes by Amount Tier

**Priority:** P1

Expected: correct rate tier applied.

---

## LOAN-TC-030 — Client Manipulates Interest Rate

**Priority:** P0
**Risk:** RISK-037

Expected: backend ignores manipulated rate.

---

# 13. Fee Calculation Test Cases

## LOAN-TC-031 — No-Fee Loan Product

**Priority:** P1

Expected: no fee charged.

---

## LOAN-TC-032 — Fixed Origination Fee

**Priority:** P0

Expected: fee correct and visible before commitment.

---

## LOAN-TC-033 — Percentage Origination Fee

**Priority:** P0

Expected: correct calculation.

---

## LOAN-TC-034 — Fee Cap

**Priority:** P1

Expected: configured cap enforced.

---

## LOAN-TC-035 — Client Manipulates Fee

**Priority:** P0

Expected: authoritative backend fee used.

---

# 14. Repayment Estimate Test Cases

## LOAN-TC-036 — Installment Estimate Display

**Priority:** P0

Expected: estimated installment matches authoritative calculation.

---

## LOAN-TC-037 — Total Repayment Display

**Priority:** P0

Expected:

```text
Principal
+
Interest
+
Applicable Fees
```

is accurately represented.

---

## LOAN-TC-038 — Estimate Recalculates After Term Change

**Priority:** P1

Expected: updated values correct.

---

## LOAN-TC-039 — Estimate Recalculates After Amount Change

**Priority:** P1

Expected: updated values correct.

---

# 15. Application State Test Cases

## LOAN-TC-040 — DRAFT Application

**Priority:** P1

Expected: editable if allowed.

---

## LOAN-TC-041 — SUBMITTED Application

**Priority:** P0

Expected: under business review and protected from unauthorized changes.

---

## LOAN-TC-042 — UNDER_REVIEW Application

**Priority:** P0

Expected: authorized reviewer workflow only.

---

## LOAN-TC-043 — APPROVED Application

**Priority:** P0

Expected: eligible for configured disbursement process.

---

## LOAN-TC-044 — REJECTED Application

**Priority:** P0

Expected: cannot disburse.

---

## LOAN-TC-045 — CANCELLED Application

**Priority:** P0

Expected: cannot continue to approval/disbursement unless explicitly reopened through supported workflow.

---

# 16. Loan Decision Authorization Test Cases

## LOAN-TC-046 — Loan Officer Approves Application

**Priority:** P0
**Requirement:** REQ-LOAN-005

### Expected Result

Authorized officer can approve eligible application.

Audit created.

---

## LOAN-TC-047 — Loan Officer Rejects Application

**Priority:** P0

Expected: rejection stored with required reason where applicable.

---

## LOAN-TC-048 — Customer Attempts to Approve Own Loan

**Priority:** P0
**Risk:** RISK-006, RISK-030

Expected: denied.

---

## LOAN-TC-049 — Read-Only Admin Attempts Approval

**Priority:** P0

Expected: denied.

---

## LOAN-TC-050 — Unauthorized Admin Role Attempts Approval

**Priority:** P0
**Risk:** RISK-023

Expected: denied.

---

## LOAN-TC-051 — Client Manipulates Loan Status to APPROVED

**Priority:** P0

Expected: protected status ignored/rejected.

---

# 17. Approval Rule Test Cases

## LOAN-TC-052 — Approve Eligible Application

**Priority:** P0

Expected: succeeds.

---

## LOAN-TC-053 — Approve Ineligible Application

**Priority:** P0

Expected: rejected unless authorized override process exists.

---

## LOAN-TC-054 — Approve Rejected Application Directly

**Priority:** P0

Expected: invalid transition rejected unless formal reconsideration workflow exists.

---

## LOAN-TC-055 — Approve Cancelled Application

**Priority:** P0

Expected: rejected.

---

# 18. Disbursement Test Cases

## LOAN-TC-056 — Disburse Approved Loan

**Priority:** P0
**Requirement:** REQ-LOAN-006
**Risk:** RISK-001

### Test Data

```text
Loan Principal:
100,000.00

Destination Opening Balance:
20,000.00
```

### Expected Result

```text
Destination Closing Balance:
120,000.00
```

and:

* One loan disbursement transaction.
* Loan state updated correctly.
* Unique disbursement reference.
* Repayment schedule created.

---

## LOAN-TC-057 — Disburse Unapproved Loan

**Priority:** P0

Expected: rejected.

---

## LOAN-TC-058 — Disburse Rejected Loan

**Priority:** P0

Expected: rejected.

---

## LOAN-TC-059 — Disburse Cancelled Loan

**Priority:** P0

Expected: rejected.

---

## LOAN-TC-060 — Disbursement to Invalid Account

**Priority:** P0

Expected: rejected safely.

---

## LOAN-TC-061 — Disbursement to Another Customer's Account

**Priority:** P0
**Risk:** RISK-047

Expected: rejected unless explicitly approved business configuration allows otherwise.

---

# 19. Exactly-Once Disbursement Test Cases

## LOAN-TC-062 — Double-Click Disbursement

**Priority:** P0
**Requirement:** REQ-LOAN-007
**Risk:** RISK-005? Use duplicate financial risk: RISK-003

Expected: one credit only.

---

## LOAN-TC-063 — Repeated API Disbursement Request

**Priority:** P0

Expected: one financial effect.

---

## LOAN-TC-064 — Same Idempotency Key / Same Payload

**Priority:** P0

Expected: returns existing result or equivalent safe response.

---

## LOAN-TC-065 — Same Idempotency Key / Different Payload

**Priority:** P0

Expected: conflicting request rejected.

---

## LOAN-TC-066 — Retry After Disbursement Timeout

**Priority:** P0
**Risk:** RISK-042

Expected: retry does not duplicate credit.

---

## LOAN-TC-067 — Duplicate Processing Job

**Priority:** P0

Expected: second job cannot disburse already-disbursed loan.

---

# 20. Concurrent Disbursement Test Cases

## LOAN-TC-068 — Two Concurrent Disbursement Requests

**Priority:** P0
**Risk:** RISK-013

Expected:

```text
Successful Disbursements:
1
```

---

## LOAN-TC-069 — Five Concurrent Disbursement Requests

**Priority:** P0

Expected: one financial credit.

---

## LOAN-TC-070 — Disbursement Concurrent With Loan Cancellation

**Priority:** P0

Expected: deterministic valid outcome.

Loan must not incorrectly appear both cancelled and successfully newly disbursed without reconciliation.

---

# 21. Disbursement Database Validation

## LOAN-TC-071 — One Disbursement Record

**Priority:** P0
**Automation:** SQL

Expected: one authoritative disbursement row.

---

## LOAN-TC-072 — One Account Credit

**Priority:** P0

Expected: destination credited once.

---

## LOAN-TC-073 — Unique Disbursement Reference

**Priority:** P0
**Risk:** RISK-033

Expected: unique reference.

---

## LOAN-TC-074 — Loan State Matches Disbursement

**Priority:** P0

Expected: DB lifecycle state and financial records agree.

---

# 22. Repayment Schedule Test Cases

## LOAN-TC-075 — Schedule Generated After Disbursement

**Priority:** P0
**Requirement:** REQ-LOAN-008

Expected: installment schedule created correctly.

---

## LOAN-TC-076 — Correct Number of Installments

**Priority:** P0

Expected: count matches term/product rules.

---

## LOAN-TC-077 — Installment Due Dates

**Priority:** P0

Expected: dates follow schedule rules.

---

## LOAN-TC-078 — Installment Principal Allocation

**Priority:** P0

Expected: principal allocations sum correctly.

---

## LOAN-TC-079 — Installment Interest Allocation

**Priority:** P0

Expected: interest allocations match calculation model.

---

## LOAN-TC-080 — Final Installment Rounding

**Priority:** P0
**Risk:** RISK-022? Better calculation risk RISK-011

Expected: final installment absorbs permitted rounding difference so total schedule reconciles exactly.

---

# 23. Repayment Test Cases

## LOAN-TC-081 — Valid Installment Repayment

**Priority:** P0
**Requirement:** REQ-LOAN-009

### Expected Result

* Customer account debited once.
* Loan outstanding reduced correctly.
* Installment state updated.
* History updated.

---

## LOAN-TC-082 — Repayment With Insufficient Account Balance

**Priority:** P0

Expected: rejected or failed safely.

No invalid loan reduction.

---

## LOAN-TC-083 — Repayment From Frozen Account

**Priority:** P0

Expected: rejected according to account-state rules.

---

## LOAN-TC-084 — Duplicate Repayment Submission

**Priority:** P0

Expected: one repayment only.

---

## LOAN-TC-085 — Repayment Retry After Timeout

**Priority:** P0

Expected: no duplicate debit or duplicate loan reduction.

---

# 24. Partial Repayment Test Cases

## LOAN-TC-086 — Valid Partial Repayment

**Priority:** P1

Expected: outstanding values recalculate according to product rules.

---

## LOAN-TC-087 — Partial Repayment Below Minimum

**Priority:** P1

Expected: rejected if minimum applies.

---

## LOAN-TC-088 — Partial Repayment Greater Than Outstanding

**Priority:** P0

Expected: rejected or routed through payoff rules.

---

# 25. Repayment Allocation Test Cases

## LOAN-TC-089 — Repayment Allocates to Interest and Principal Correctly

**Priority:** P0

Expected: allocation follows documented waterfall.

---

## LOAN-TC-090 — Repayment With Outstanding Penalty

**Priority:** P0

Expected: allocation follows configured priority such as:

```text
Penalty
→ Interest
→ Principal
```

if that is the business rule.

---

## LOAN-TC-091 — Client Manipulates Allocation

**Priority:** P0

Expected: backend computes authoritative allocation.

---

# 26. Loan Balance Test Cases

## LOAN-TC-092 — Outstanding Principal After Repayment

**Priority:** P0

Expected: exact correct remaining principal.

---

## LOAN-TC-093 — Outstanding Interest

**Priority:** P0

Expected: correct according to repayment/accrual model.

---

## LOAN-TC-094 — Outstanding Total

**Priority:** P0

Expected:

```text
Principal
+
Accrued Interest
+
Applicable Penalties
+
Applicable Fees
```

matches authoritative value.

---

# 27. Overdue Loan Test Cases

## LOAN-TC-095 — Installment Passes Due Date Unpaid

**Priority:** P0
**Requirement:** REQ-LOAN-010

Expected: installment/loan enters overdue state according to policy.

---

## LOAN-TC-096 — Overdue Penalty Applied

**Priority:** P0

Expected: correct penalty once according to rules.

---

## LOAN-TC-097 — Duplicate Penalty Job

**Priority:** P0

Expected: penalty not applied twice unintentionally.

---

## LOAN-TC-098 — Overdue Notification

**Priority:** P1

Expected: correct amount/date/status.

---

# 28. Default Test Cases

## LOAN-TC-099 — Loan Enters Default After Threshold

**Priority:** P0

Expected: correct transition when threshold met.

---

## LOAN-TC-100 — Default Before Threshold

**Priority:** P0

Expected: must not occur prematurely.

---

## LOAN-TC-101 — Defaulted Loan Restricted Actions

**Priority:** P0

Expected: restricted lifecycle/actions enforced.

---

# 29. Early Settlement Test Cases

## LOAN-TC-102 — Request Settlement Quote

**Priority:** P0
**Requirement:** REQ-LOAN-011

Expected: quote includes correct:

* Remaining principal
* Accrued interest
* Settlement fee/penalty
* Total payoff

---

## LOAN-TC-103 — Valid Full Early Settlement

**Priority:** P0

Expected:

```text
Outstanding Loan:
0.00
```

after correct payment.

Loan transitions toward closed state.

---

## LOAN-TC-104 — Settlement Quote Expiry

**Priority:** P1

Expected: expired quote cannot be used if recalculation is required.

---

## LOAN-TC-105 — Client Manipulates Settlement Amount

**Priority:** P0

Expected: backend authoritative payoff amount enforced.

---

## LOAN-TC-106 — Duplicate Settlement Submission

**Priority:** P0

Expected: loan is not overpaid/double-closed.

---

# 30. Loan Closure Test Cases

## LOAN-TC-107 — Final Scheduled Repayment Closes Loan

**Priority:** P0
**Requirement:** REQ-LOAN-012

Expected: loan closes only after obligations reach zero according to rules.

---

## LOAN-TC-108 — Close Loan With Outstanding Principal

**Priority:** P0

Expected: rejected.

---

## LOAN-TC-109 — Close Loan With Outstanding Interest

**Priority:** P0

Expected: rejected unless waived through authorized process.

---

## LOAN-TC-110 — Closed Loan Receives Additional Repayment

**Priority:** P0

Expected: rejected or safely refunded according to design.

---

# 31. State Transition Test Cases

## LOAN-TC-111 — SUBMITTED → UNDER_REVIEW

**Priority:** P1

Expected: valid.

---

## LOAN-TC-112 — UNDER_REVIEW → APPROVED

**Priority:** P0

Expected: authorized transition only.

---

## LOAN-TC-113 — UNDER_REVIEW → REJECTED

**Priority:** P0

Expected: valid authorized transition.

---

## LOAN-TC-114 — APPROVED → DISBURSED

**Priority:** P0

Expected: after valid disbursement.

---

## LOAN-TC-115 — DISBURSED → ACTIVE

**Priority:** P0

Expected: valid lifecycle.

---

## LOAN-TC-116 — ACTIVE → CLOSED

**Priority:** P0

Expected: only after zero obligation.

---

## LOAN-TC-117 — REJECTED → DISBURSED

**Priority:** P0

Expected: rejected as invalid transition.

---

## LOAN-TC-118 — CLOSED → ACTIVE

**Priority:** P0

Expected: rejected if closed is terminal.

---

# 32. Cancellation Test Cases

## LOAN-TC-119 — Customer Cancels Draft Application

**Priority:** P1

Expected: application cancelled safely.

---

## LOAN-TC-120 — Cancel Submitted Application Before Decision

**Priority:** P1

Expected: follows business rules.

---

## LOAN-TC-121 — Cancel Approved Loan Before Disbursement

**Priority:** P0

Expected: permitted only according to business policy.

No disbursement afterward.

---

## LOAN-TC-122 — Cancel Already Disbursed Loan

**Priority:** P0

Expected: rejected; repayment/settlement process required.

---

# 33. Concurrency Test Cases

## LOAN-TC-123 — Two Officers Decide Same Application

**Priority:** P0
**Risk:** RISK-013

### Steps

1. Officer A opens application.
2. Officer B opens same application.
3. A approves.
4. B rejects using stale state.

### Expected Result

Only valid transition is persisted according to concurrency control.

---

## LOAN-TC-124 — Repayment and Penalty Job Concurrently

**Priority:** P0

Expected: correct deterministic financial allocation.

---

## LOAN-TC-125 — Final Repayment and Early Settlement Concurrently

**Priority:** P0

Expected: one valid payoff outcome.

No overpayment/double closure.

---

## LOAN-TC-126 — Loan Closure and Repayment Concurrently

**Priority:** P0

Expected: no invalid final state.

---

# 34. API Loan Test Cases

## LOAN-TC-127 — Get Own Loan API

**Priority:** P0
**Automation:** REST Assured

Expected: authorized loan returned.

---

## LOAN-TC-128 — Get Another Customer's Loan

**Priority:** P0
**Risk:** RISK-047

Expected: denied.

---

## LOAN-TC-129 — Create Application API

**Priority:** P0

Expected: valid application created once.

---

## LOAN-TC-130 — Customer Approval API

**Priority:** P0

Expected: denied.

---

## LOAN-TC-131 — Read-Only Admin Approval API

**Priority:** P0

Expected: denied.

---

## LOAN-TC-132 — Manipulate Interest Rate

**Priority:** P0

Expected: backend ignores client-controlled interest.

---

## LOAN-TC-133 — Manipulate Approved Amount

**Priority:** P0

Expected: unauthorized modification rejected.

---

## LOAN-TC-134 — Manipulate Loan Status

**Priority:** P0

Expected: protected lifecycle cannot be client-controlled.

---

# 35. Database Validation Test Cases

## LOAN-TC-135 — Loan Ownership Relationship

**Priority:** P0
**Automation:** SQL

Expected: loan references correct customer.

---

## LOAN-TC-136 — Application Amount and Term Persistence

**Priority:** P1

Expected: stored values match accepted application.

---

## LOAN-TC-137 — Loan Decision Persistence

**Priority:** P0

Expected: decision/actor/timestamp consistent.

---

## LOAN-TC-138 — Disbursement Uniqueness

**Priority:** P0
**Risk:** RISK-003

Expected:

```text
Disbursement Count:
1
```

for one loan.

---

## LOAN-TC-139 — Repayment Schedule Persistence

**Priority:** P0

Expected: installment totals reconcile.

---

## LOAN-TC-140 — Repayment Persistence

**Priority:** P0

Expected: one record per intended repayment.

---

## LOAN-TC-141 — Outstanding Balance Persistence

**Priority:** P0

Expected: values match calculation.

---

## LOAN-TC-142 — Penalty Persistence

**Priority:** P0

Expected: correct penalty applied once.

---

## LOAN-TC-143 — Closed Loan Outstanding Balance

**Priority:** P0

Expected: no unauthorized remaining obligation when state is properly `CLOSED`.

---

# 36. UI/API/Database Consistency

## LOAN-TC-144 — Loan Status Cross-Layer Validation

**Priority:** P0
**Risk:** RISK-039, RISK-048

Expected:

```text
UI Status
=
API Status
=
DB Status
```

---

## LOAN-TC-145 — Loan Balance Cross-Layer Validation

**Priority:** P0

Expected: outstanding values agree.

---

## LOAN-TC-146 — Schedule Cross-Layer Validation

**Priority:** P0

Expected: installment dates/amounts agree.

---

# 37. Repayment History Test Cases

## LOAN-TC-147 — Successful Repayment Appears Once

**Priority:** P0

Expected: one repayment transaction.

---

## LOAN-TC-148 — Failed Repayment Not Shown as Successful

**Priority:** P0

Expected: correct failure state.

---

## LOAN-TC-149 — Final Settlement History

**Priority:** P0

Expected: settlement clearly traceable.

---

# 38. Statement Integration Test Cases

## LOAN-TC-150 — Loan Disbursement Appears in Account Statement

**Priority:** P0

Expected: one correct credit.

---

## LOAN-TC-151 — Loan Repayment Appears in Statement

**Priority:** P0

Expected: correct debit.

---

## LOAN-TC-152 — Duplicate Disbursement Not Present

**Priority:** P0

Expected: exactly one loan credit.

---

# 39. Notification Test Cases

## LOAN-TC-153 — Application Submitted Notification

**Priority:** P2

Expected: correct application reference/status.

---

## LOAN-TC-154 — Approval Notification

**Priority:** P1

Expected: reflects actual approved state.

---

## LOAN-TC-155 — Rejection Notification

**Priority:** P1

Expected: accurate and contains only appropriate information.

---

## LOAN-TC-156 — Disbursement Notification

**Priority:** P0

Expected: correct amount/reference.

---

## LOAN-TC-157 — Repayment Notification

**Priority:** P1

Expected: correct payment/outstanding information.

---

## LOAN-TC-158 — Failed Repayment Sends No Success Notification

**Priority:** P0

Expected: no false success.

---

# 40. Audit Test Cases

## LOAN-TC-159 — Approval Audit

**Priority:** P0
**Risk:** RISK-022

Expected audit includes:

```text
Actor

Loan

Decision

Previous State

New State

Timestamp

Reason where applicable
```

---

## LOAN-TC-160 — Rejection Audit

**Priority:** P0

Expected: decision traceable.

---

## LOAN-TC-161 — Disbursement Audit/Trace

**Priority:** P0

Expected: disbursement traceable to approved loan.

---

## LOAN-TC-162 — Administrative Settlement Adjustment Audit

**Priority:** P0

Expected: actor/reason/amount recorded.

---

# 41. Precision and Rounding Test Cases

## LOAN-TC-163 — Interest Rounding

**Priority:** P0
**Risk:** RISK-011

Expected: configured rounding method applied consistently.

---

## LOAN-TC-164 — Installment Rounding

**Priority:** P0
**Risk:** RISK-022? Use calculation risk context

Expected: sum of installments reconciles to total obligation.

---

## LOAN-TC-165 — Final Installment Rounding Adjustment

**Priority:** P0

Expected: no unexplained remaining fractional balance.

---

## LOAN-TC-166 — Large Loan Amount Precision

**Priority:** P1

Expected: no floating-point corruption.

---

# 42. Security Input Test Cases

## LOAN-TC-167 — Script-Like Application Text

**Priority:** P1

Expected: safely encoded.

---

## LOAN-TC-168 — SQL-Like Input

**Priority:** P1

Expected: no injection or DB error.

---

## LOAN-TC-169 — Protected Fields in Application Payload

**Priority:** P0

Example:

```json
{
  "status": "APPROVED",
  "approvedAmount": 500000,
  "interestRate": 0,
  "approvedBy": "CUSTOMER"
}
```

Expected: protected values ignored/rejected.

---

# 43. Error Handling Test Cases

## LOAN-TC-170 — Loan Service Unavailable During Application

**Priority:** P1

Expected: no misleading successful submission.

---

## LOAN-TC-171 — Failure During Disbursement

**Priority:** P0

Expected: no partial/duplicate credit.

---

## LOAN-TC-172 — Failure After Debit During Repayment

**Priority:** P0

Expected: system safely reconciles customer account debit and loan reduction.

---

## LOAN-TC-173 — Schedule Generation Failure After Disbursement

**Priority:** P0

Expected: loan cannot remain silently active without valid repayment schedule; failure is recoverable/visible.

---

# 44. Cross-Browser Test Cases

## LOAN-TC-174 — Loan Flow in Chrome

**Priority:** P2

Expected: critical UI flow works.

---

## LOAN-TC-175 — Loan Flow in Edge

**Priority:** P2

Expected: works.

---

## LOAN-TC-176 — Loan Flow in Firefox

**Priority:** P2

Expected: works.

---

## LOAN-TC-177 — Loan Flow in WebKit

**Priority:** P2

Expected: works.

---

# 45. Responsive Test Cases

## LOAN-TC-178 — Loan Application at 390×844

**Priority:** P1

Expected:

* Amount visible.
* Term visible.
* Interest visible.
* Fees visible.
* Installment estimate visible.
* Submit/confirmation accessible.

---

## LOAN-TC-179 — Loan Repayment at 360×800

**Priority:** P1

Expected: due amount and confirmation remain readable.

---

## LOAN-TC-180 — Long Repayment Schedule on Mobile

**Priority:** P2

Expected: schedule remains usable without hiding important financial columns/values.

---

# 46. Accessibility Test Cases

## LOAN-TC-181 — Keyboard Loan Application

**Priority:** P2

Expected: form operable via keyboard.

---

## LOAN-TC-182 — Financial Labels Accessible

**Priority:** P2

Expected: principal, interest, fees, total, installment amounts are clearly labeled.

---

## LOAN-TC-183 — Loan Status Not Communicated by Color Alone

**Priority:** P2

Expected: textual/semantic state available.

---

# 47. End-to-End Loan Application Journey

## LOAN-TC-184 — Application → Review → Approval

**Priority:** P0

### Steps

1. Eligible customer applies.
2. Validate submitted application.
3. Loan officer reviews.
4. Approve.
5. Verify status.
6. Verify audit.
7. Verify notification.

### Expected Result

```text
SUBMITTED
→ UNDER_REVIEW
→ APPROVED
```

with authorized, traceable transitions.

---

# 48. End-to-End Loan Disbursement

## LOAN-TC-185 — Approval → Disbursement → Reconciliation

**Priority:** P0

### Test Data

```text
Loan:
100,000.00

Destination Opening:
20,000.00
```

Expected:

```text
Destination Closing:
120,000.00

Disbursement Count:
1
```

### Validate

* UI
* API
* DB
* Account balance
* History
* Statement
* Schedule
* Notification

---

# 49. End-to-End Duplicate Disbursement Protection

## LOAN-TC-186 — Concurrent and Retry Disbursement

**Priority:** P0

### Steps

1. Approved loan exists.
2. Submit multiple concurrent disbursement requests.
3. Simulate one timeout and retry.
4. Inspect financial state.

### Expected Result

```text
Loan:
One

Disbursement Financial Effects:
One

Account Credits:
One

Disbursement Reference:
One authoritative result
```

---

# 50. End-to-End Repayment Journey

## LOAN-TC-187 — Installment Repayment → Reconciliation

**Priority:** P0

### Validate

```text
Customer Account Debit
=
Repayment Amount
```

and:

```text
Loan Outstanding Before
-
Applied Repayment Components
=
Loan Outstanding After
```

according to allocation rules.

---

# 51. End-to-End Failed Repayment

## LOAN-TC-188 — Insufficient Funds Repayment Failure

**Priority:** P0

Expected:

```text
Loan Balance Reduction:
0

Permanent Customer Debit:
0

Installment Paid State:
No

Success Notification:
No
```

---

# 52. End-to-End Early Settlement

## LOAN-TC-189 — Settlement Quote → Full Payoff → Closure

**Priority:** P0

### Expected Result

* Correct quote.
* One payoff debit.
* Outstanding loan reaches zero.
* Loan transitions to closed.
* Statement/history reconcile.

---

# 53. End-to-End Overdue Journey

## LOAN-TC-190 — Missed Installment → Overdue → Penalty → Payment

**Priority:** P0

### Expected Result

* Overdue state occurs at correct time.
* Penalty applied once.
* Repayment allocation correct.
* Outstanding balance reconciles.

---

# 54. End-to-End Concurrency Test

## LOAN-TC-191 — Final Repayment vs Settlement Race

**Priority:** P0

### Steps

1. Small remaining balance exists.
2. Submit final installment.
3. Concurrently submit early-settlement request/payment.
4. Inspect loan/account state.

### Expected Result

System prevents double collection.

Loan closes once with correct total paid.

---

# 55. Loan Risk Mapping

| Risk                               | Related Test Cases                                 |
| ---------------------------------- | -------------------------------------------------- |
| RISK-001 Incorrect balance         | LOAN-TC-056–074, 081–106, 135–166, 185–191         |
| RISK-003 Duplicate transaction     | LOAN-TC-011, 062–070, 084–085, 097, 106, 138, 186  |
| RISK-006 Privilege escalation      | LOAN-TC-046–051, 130–134                           |
| RISK-011 Loan interest calculation | LOAN-TC-026–039, 075–080, 163–166                  |
| RISK-013 Concurrency corruption    | LOAN-TC-068–070, 123–126, 186, 191                 |
| RISK-019 History inconsistency     | LOAN-TC-147–152                                    |
| RISK-021 Sensitive exposure        | LOAN-TC-127–134, 159–169                           |
| RISK-022 Audit gap                 | LOAN-TC-159–162                                    |
| RISK-023 Unauthorized admin        | LOAN-TC-049–050                                    |
| RISK-030 Unauthorized API          | LOAN-TC-127–134                                    |
| RISK-033 Duplicate reference       | LOAN-TC-073                                        |
| RISK-037 Frontend-only validation  | LOAN-TC-009, 030, 035, 051, 091, 105, 132–134, 169 |
| RISK-039 API/DB inconsistency      | LOAN-TC-135–146                                    |
| RISK-042 Retry duplication         | LOAN-TC-066, 085, 186                              |
| RISK-047 IDOR                      | LOAN-TC-061, 128                                   |
| RISK-048 UI/backend mismatch       | LOAN-TC-144–146                                    |

---

# 56. Requirements Mapping

| Requirement                            | Test Cases               |
| -------------------------------------- | ------------------------ |
| REQ-LOAN-001 Loan products             | LOAN-TC-001–003          |
| REQ-LOAN-002 Eligibility               | LOAN-TC-004–009          |
| REQ-LOAN-003 Application               | LOAN-TC-010–025          |
| REQ-LOAN-004 Financial calculations    | LOAN-TC-026–039          |
| REQ-LOAN-005 Approval/rejection        | LOAN-TC-040–055          |
| REQ-LOAN-006 Disbursement              | LOAN-TC-056–061          |
| REQ-LOAN-007 Exactly-once disbursement | LOAN-TC-062–074          |
| REQ-LOAN-008 Installment schedule      | LOAN-TC-075–080          |
| REQ-LOAN-009 Repayment                 | LOAN-TC-081–094          |
| REQ-LOAN-010 Overdue/default           | LOAN-TC-095–101          |
| REQ-LOAN-011 Early settlement          | LOAN-TC-102–106          |
| REQ-LOAN-012 Closure                   | LOAN-TC-107–118          |
| REQ-LOAN-013 Concurrency/traceability  | LOAN-TC-123–166, 184–191 |

---

# 57. Smoke Candidates

Recommended loan smoke coverage:

```text
LOAN-TC-001
LOAN-TC-004
LOAN-TC-010
LOAN-TC-046
LOAN-TC-056
LOAN-TC-062
LOAN-TC-075
LOAN-TC-081
LOAN-TC-107
LOAN-TC-128
LOAN-TC-144
```

---

# 58. Sanity Candidates

After loan changes:

```text
LOAN-TC-004
LOAN-TC-010
LOAN-TC-016
LOAN-TC-026
LOAN-TC-036
LOAN-TC-046
LOAN-TC-056
LOAN-TC-062
LOAN-TC-066
LOAN-TC-075
LOAN-TC-081
LOAN-TC-089
LOAN-TC-095
LOAN-TC-102
LOAN-TC-107
LOAN-TC-138
LOAN-TC-144
```

---

# 59. Critical Regression Candidates

```text
LOAN-TC-004–011

LOAN-TC-014–039

LOAN-TC-040–074

LOAN-TC-075–126

LOAN-TC-127–173

LOAN-TC-178–191
```

---

# 60. UI Automation Candidates

Best suited for:

```text
Playwright

Selenium

Cypress
```

Strong candidates:

```text
LOAN-TC-001–025

LOAN-TC-036–061

LOAN-TC-075–125

LOAN-TC-147–190
```

---

# 61. API Automation Candidates

Best suited for:

```text
REST Assured

Postman
```

Strong candidates:

```text
LOAN-TC-004–173

LOAN-TC-184–191
```

particularly:

```text
Eligibility

Approval authorization

Status transitions

Disbursement idempotency

Repayment

Settlement

IDOR

Protected-field manipulation
```

---

# 62. SQL / Database Testing Candidates

Strong SQL candidates:

```text
LOAN-TC-010–011

LOAN-TC-040–080

LOAN-TC-081–126

LOAN-TC-135–166

LOAN-TC-184–191
```

Database validation should verify:

```text
Loan ownership

Application values

Decision state

Approved amount

Interest rate

Fees

Disbursement uniqueness

Repayment schedule

Repayments

Outstanding balances

Penalties

Settlement

State transitions

Audit relationships
```

---

# 63. Jest Candidates

Loan business logic is particularly suitable for Jest tests covering:

```text
Interest calculations

Installment calculations

Fee calculations

Rounding

Penalty calculations

Outstanding balance

Early settlement quotes

Allocation waterfalls
```

High-value mappings include:

```text
LOAN-TC-026–039

LOAN-TC-075–080

LOAN-TC-089–094

LOAN-TC-096–106

LOAN-TC-163–166
```

---

# 64. Performance / JMeter Candidates

Strong concurrency/performance candidates:

```text
LOAN-TC-011

LOAN-TC-062–070

LOAN-TC-084–085

LOAN-TC-097

LOAN-TC-106

LOAN-TC-123–126

LOAN-TC-186

LOAN-TC-191
```

Performance testing must verify financial correctness in addition to:

```text
Response time

Throughput

Error rate
```

---

# 65. Test Evidence Requirements

For critical loan tests, capture as applicable:

```text
Customer ID

Loan ID

Application reference

Loan product

Principal

Interest rate

Fees

Term

Installment amount

Outstanding principal

Outstanding interest

Penalty

Disbursement reference

Repayment reference

Settlement reference

Opening account balance

Closing account balance

API request/response

DB rows

Audit record

Notification

Timestamp

Defect ID
```

---

# 66. Loan Defect Examples

Potential Critical/High defects include:

```text
Ineligible customer obtains loan.

Customer approves own loan.

Unauthorized admin approves loan.

Approved amount differs from agreed amount.

Interest calculation incorrect.

Installment schedule does not reconcile.

Loan disbursed twice.

Disbursement credited to wrong account.

Rejected loan is disbursed.

Repayment debits customer but does not reduce loan.

Loan balance decreases without account debit.

Duplicate repayment reduces loan twice.

Penalty applied twice.

Early settlement overcharges customer.

Closed loan still shows outstanding amount.

Concurrent final repayment causes overpayment.

UI/API/DB loan states disagree.
```

---

# 67. Loan Release Blockers

Release should normally be blocked by unresolved issues involving:

```text
Unauthorized loan approval

Incorrect loan principal

Incorrect interest with material impact

Duplicate loan disbursement

Wrong-account disbursement

Rejected/cancelled loan disbursement

Incorrect repayment allocation

Duplicate repayment

Customer debit without loan reduction

Loan reduction without customer debit

Incorrect payoff amount

Duplicate penalty

Incorrect outstanding balance

Critical loan-state inconsistency

Loan IDOR/customer data exposure
```

---

# 68. Loan Exit Criteria

Loan testing is acceptable when:

```text
Eligibility is enforced.

Applications validate correctly.

Loan calculations reconcile.

Only authorized roles approve/reject.

Invalid state transitions are blocked.

Disbursement occurs exactly once.

Disbursement reaches correct account.

Schedules calculate correctly.

Repayments update both account and loan correctly.

Failed repayments remain financially safe.

Overdue/default logic is correct.

Penalties are accurate and applied once.

Early settlement is accurate.

Loans close only when obligations are satisfied.

Concurrency does not create duplicate financial effects.

UI/API/DB states agree.

Critical actions are audited.

No unresolved Critical/P0 loan defect remains.
```

---

# 69. Final Loan Testing Principle

A loan is not simply:

```text
An amount credited to a customer.
```

It is a long-running financial contract containing:

```text
Eligibility

Application

Decision

Principal

Interest

Fees

Disbursement

Installment Schedule

Repayments

Penalties

Outstanding Balance

Settlement

Closure
```

Every stage must remain financially and operationally consistent.

The most important loan invariant is:

```text
One approved loan
must produce one correct disbursement,
and every repayment must reduce
the customer's financial obligation exactly once.
```

The core rule is:

```text
At every point in the loan lifecycle,
the customer's account balance,
loan outstanding balance,
repayment schedule,
transaction history,
API state,
and database state
must describe the same financial reality.
```
