# Banking System — Loan Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Loans                          |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for loan functionality within the Banking System.

Loan functionality is financially sensitive because defects may lead to:

* Incorrect eligibility decisions
* Incorrect approved loan amounts
* Incorrect interest calculations
* Incorrect installment schedules
* Incorrect outstanding balances
* Duplicate disbursements
* Incorrect repayments
* Incorrect early-settlement values
* Unauthorized loan approval
* Incorrect loan-state transitions

The scenarios cover the complete loan lifecycle from product discovery and application through approval, disbursement, repayment, closure, and historical reporting.

---

# 3. Scope

Loan testing includes:

* Loan products
* Loan eligibility
* Loan application
* Loan amount validation
* Loan term validation
* Interest rates
* Fees
* Application status
* Approval
* Rejection
* Administrative review
* Disbursement
* Installment schedules
* Repayments
* Partial repayments
* Full repayment
* Early repayment
* Overdue installments
* Loan closure
* Loan cancellation
* Loan ownership
* Authorization
* Notifications
* Audit logging
* Data consistency
* Financial calculations

---

# 4. Scenario Naming Convention

Loan scenarios use:

```text
TS-LOAN-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Loan Product Viewing Scenarios

## TS-LOAN-001 — Customer views available loan products

**Priority:** P1

Expected:

Available products are displayed correctly.

---

## TS-LOAN-002 — Loan product displays minimum amount

**Priority:** P1

Expected:

Correct configured minimum is shown.

---

## TS-LOAN-003 — Loan product displays maximum amount

**Priority:** P1

Expected:

Correct configured maximum is shown.

---

## TS-LOAN-004 — Loan product displays available terms

**Priority:** P1

Expected:

Supported repayment periods are clear.

---

## TS-LOAN-005 — Loan product displays interest information

**Priority:** P0

Expected:

Interest/rate information matches configured product rules.

---

## TS-LOAN-006 — Loan product displays fees

**Priority:** P1

Expected:

Applicable charges are visible.

---

## TS-LOAN-007 — Ineligible loan product is hidden or clearly marked

**Priority:** P1

Expected:

Behavior follows product eligibility design.

---

# 6. Loan Ownership and Authorization

## TS-LOAN-008 — Customer views own loan

**Priority:** P0

Expected:

Correct loan details are shown.

---

## TS-LOAN-009 — Customer attempts to view another customer's loan

**Priority:** P0

Expected:

Access denied.

---

## TS-LOAN-010 — Modify loan ID in URL

**Priority:** P0

Expected:

Another customer's loan cannot be accessed.

---

## TS-LOAN-011 — Modify loan ID in API request

**Priority:** P0

Expected:

Backend authorization rejects request.

---

## TS-LOAN-012 — Customer attempts to approve own loan via API

**Priority:** P0

Expected:

Denied.

---

## TS-LOAN-013 — Limited admin attempts unauthorized loan approval

**Priority:** P0

Expected:

Denied.

---

## TS-LOAN-014 — Unauthenticated user attempts loan access

**Priority:** P0

Expected:

Authentication required.

---

# 7. Loan Eligibility Scenarios

Eligibility rules depend on project requirements.

Possible inputs may include:

* Customer status
* KYC status
* Account status
* Income
* Existing loans
* Requested amount
* Risk level

---

## TS-LOAN-015 — Eligible active verified customer applies

**Priority:** P0

Expected:

Application can proceed.

---

## TS-LOAN-016 — Unverified customer attempts application

**Priority:** P0

Expected:

Rejected where KYC verification is mandatory.

---

## TS-LOAN-017 — Restricted customer attempts application

**Priority:** P0

Expected:

Rejected according to restriction rules.

---

## TS-LOAN-018 — Suspended customer attempts application

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-019 — Disabled customer attempts application

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-020 — Customer with excessive existing debt applies

**Priority:** P1

Expected:

Eligibility rules applied correctly.

---

## TS-LOAN-021 — Customer exactly at eligibility boundary

**Priority:** P1

Expected:

Boundary rule enforced accurately.

---

# 8. Loan Application Scenarios

## TS-LOAN-022 — Submit valid loan application

**Priority:** P0

Expected:

Application created successfully.

---

## TS-LOAN-023 — Required field missing

**Priority:** P1

Expected:

Application rejected.

---

## TS-LOAN-024 — Submit all fields empty

**Priority:** P1

Expected:

Required-field validation.

---

## TS-LOAN-025 — Duplicate application submission

**Priority:** P0

Expected:

Unintended duplicate loan application is prevented where business rules require.

---

## TS-LOAN-026 — Double-click application Submit

**Priority:** P0

Expected:

Only one application created.

---

## TS-LOAN-027 — Refresh after application submission

**Priority:** P1

Expected:

No duplicate application.

---

## TS-LOAN-028 — Network interruption after application submission

**Priority:** P1

Expected:

Customer can determine actual application state safely.

---

# 9. Loan Amount Validation

Assume example:

```text
Minimum loan = 10,000.00
Maximum loan = 1,000,000.00
```

---

## TS-LOAN-029 — Amount below minimum

**Priority:** P1

Expected:

Rejected.

---

## TS-LOAN-030 — Exact minimum amount

**Priority:** P1

Expected:

Accepted if otherwise eligible.

---

## TS-LOAN-031 — Minimum plus smallest supported unit

**Priority:** P2

Expected:

Accepted.

---

## TS-LOAN-032 — Maximum minus smallest supported unit

**Priority:** P1

Expected:

Accepted.

---

## TS-LOAN-033 — Exact maximum amount

**Priority:** P0

Expected:

Accepted if maximum inclusive.

---

## TS-LOAN-034 — Amount above maximum

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-035 — Zero loan amount

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-036 — Negative loan amount

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-037 — Non-numeric amount

**Priority:** P1

Expected:

Rejected.

---

## TS-LOAN-038 — Amount with unsupported decimal precision

**Priority:** P1

Expected:

Rejected or normalized according to documented rule.

---

# 10. Loan Term Scenarios

## TS-LOAN-039 — Select valid minimum loan term

**Priority:** P1

Expected:

Accepted.

---

## TS-LOAN-040 — Select valid maximum loan term

**Priority:** P1

Expected:

Accepted.

---

## TS-LOAN-041 — Term shorter than minimum

**Priority:** P1

Expected:

Rejected.

---

## TS-LOAN-042 — Term longer than maximum

**Priority:** P1

Expected:

Rejected.

---

## TS-LOAN-043 — Unsupported term value

**Priority:** P1

Expected:

Rejected server-side even if client manipulated.

---

# 11. Interest Calculation Scenarios

## TS-LOAN-044 — Interest calculated correctly for standard loan

**Priority:** P0

Expected:

Displayed and stored values match configured formula.

---

## TS-LOAN-045 — Interest calculated correctly for minimum amount

**Priority:** P0

Expected:

Correct.

---

## TS-LOAN-046 — Interest calculated correctly for maximum amount

**Priority:** P0

Expected:

Correct.

---

## TS-LOAN-047 — Interest calculation across different terms

**Priority:** P0

Expected:

Correct rate/term relationship.

---

## TS-LOAN-048 — Interest rounding

**Priority:** P0

Expected:

Financial rounding follows defined precision.

---

## TS-LOAN-049 — Total repayable amount

**Priority:** P0

Expected:

```text
Principal
+ Interest
+ Applicable Fees
= Total Repayable
```

---

## TS-LOAN-050 — Interest shown before customer submits application

**Priority:** P1

Expected:

Customer understands expected cost.

---

# 12. Loan Fee Scenarios

## TS-LOAN-051 — Fixed processing fee

**Priority:** P1

Expected:

Correct fee applied.

---

## TS-LOAN-052 — Percentage-based processing fee

**Priority:** P1

Expected:

Correct calculation.

---

## TS-LOAN-053 — Fee at calculation boundary

**Priority:** P1

Expected:

Correct fee tier used.

---

## TS-LOAN-054 — Fee deducted from disbursement where applicable

**Priority:** P0

Expected:

Net disbursement calculated correctly.

---

## TS-LOAN-055 — Fee charged separately

**Priority:** P0

Expected:

Accounting representation correct.

---

# 13. Loan Application Status

Possible statuses:

```text
DRAFT
SUBMITTED
UNDER_REVIEW
APPROVED
REJECTED
CANCELLED
DISBURSED
ACTIVE
CLOSED
DEFAULTED
```

Use only states implemented by the project.

---

## TS-LOAN-056 — Submitted application moves to SUBMITTED

**Priority:** P1

Expected:

Correct state.

---

## TS-LOAN-057 — Application enters UNDER_REVIEW

**Priority:** P1

Expected:

Correct workflow.

---

## TS-LOAN-058 — Approved application becomes APPROVED

**Priority:** P0

Expected:

Authorized approval only.

---

## TS-LOAN-059 — Rejected application becomes REJECTED

**Priority:** P0

Expected:

No disbursement.

---

## TS-LOAN-060 — Cancelled application cannot be approved unexpectedly

**Priority:** P0

Expected:

Invalid state transition prevented.

---

# 14. Administrative Review Scenarios

## TS-LOAN-061 — Authorized loan officer views pending application

**Priority:** P1

Expected:

Permitted application data displayed.

---

## TS-LOAN-062 — Unauthorized admin views restricted loan details

**Priority:** P0

Expected:

Denied or limited according to role.

---

## TS-LOAN-063 — Authorized admin approves eligible application

**Priority:** P0

Expected:

Application approved.

---

## TS-LOAN-064 — Authorized admin rejects application

**Priority:** P1

Expected:

Rejection saved.

---

## TS-LOAN-065 — Approval requires reason/notes where configured

**Priority:** P2

Expected:

Validation enforced.

---

## TS-LOAN-066 — Rejection requires reason

**Priority:** P1

Expected:

Required reason stored.

---

## TS-LOAN-067 — Same application reviewed simultaneously by two admins

**Priority:** P0

Expected:

Conflicting approval/rejection does not create invalid final state.

---

# 15. Loan Approval Scenarios

## TS-LOAN-068 — Approve valid application

**Priority:** P0

Expected:

Loan approved once.

---

## TS-LOAN-069 — Approve already approved application again

**Priority:** P0

Expected:

No duplicate approval/disbursement.

---

## TS-LOAN-070 — Approve rejected application directly

**Priority:** P1

Expected:

Rejected unless explicit reopen workflow exists.

---

## TS-LOAN-071 — Customer status becomes restricted before approval

**Priority:** P0

Expected:

Current eligibility revalidated if required.

---

## TS-LOAN-072 — Approval amount differs from requested amount

**Priority:** P1

Expected:

Customer is shown correct approved amount and acceptance behavior follows rules.

---

# 16. Loan Rejection Scenarios

## TS-LOAN-073 — Reject valid pending application

**Priority:** P1

Expected:

No financial disbursement.

---

## TS-LOAN-074 — Rejected application cannot become active

**Priority:** P0

Expected:

Invalid state transition prevented.

---

## TS-LOAN-075 — Rejection notification sent

**Priority:** P2

Expected:

Correct customer informed.

---

## TS-LOAN-076 — Rejected application remains visible in history

**Priority:** P2

Expected:

Historical record preserved.

---

# 17. Loan Disbursement Scenarios

## TS-LOAN-077 — Approved loan disbursed to correct account

**Priority:** P0

Expected:

Correct customer account receives funds.

---

## TS-LOAN-078 — Disbursement amount equals approved net amount

**Priority:** P0

Expected:

Correct amount credited.

---

## TS-LOAN-079 — Disbursement to wrong account prevented

**Priority:** P0

Expected:

Ownership and destination validation enforced.

---

## TS-LOAN-080 — Disbursement occurs exactly once

**Priority:** P0

Expected:

No duplicate loan credit.

---

## TS-LOAN-081 — Double-submit disbursement request

**Priority:** P0

Expected:

Only one credit.

---

## TS-LOAN-082 — Approved loan destination account becomes closed before disbursement

**Priority:** P0

Expected:

Disbursement blocked or routed according to controlled business process.

---

## TS-LOAN-083 — Database failure during disbursement

**Priority:** P0

Expected:

No partial credit/loan-state corruption.

---

# 18. Installment Schedule Scenarios

## TS-LOAN-084 — Installment schedule generated after activation

**Priority:** P0

Expected:

Schedule exists and matches loan terms.

---

## TS-LOAN-085 — Number of installments matches term

**Priority:** P0

Expected:

Correct count.

---

## TS-LOAN-086 — Installment amount calculated correctly

**Priority:** P0

Expected:

Correct principal/interest allocation according to model.

---

## TS-LOAN-087 — Installment due dates generated correctly

**Priority:** P1

Expected:

Correct dates.

---

## TS-LOAN-088 — Month-end installment schedule

**Priority:** P1

Expected:

Date rules handled correctly.

---

## TS-LOAN-089 — Leap-year installment schedule

**Priority:** P2

Expected:

Valid dates.

---

## TS-LOAN-090 — Sum of installments reconciles with total repayable amount

**Priority:** P0

Expected:

No unexplained discrepancy outside documented rounding adjustment.

---

# 19. Standard Repayment Scenarios

## TS-LOAN-091 — Pay scheduled installment with sufficient funds

**Priority:** P0

Expected:

Payment succeeds.

---

## TS-LOAN-092 — Repayment debits correct source account

**Priority:** P0

Expected:

Correct customer account affected.

---

## TS-LOAN-093 — Outstanding balance decreases correctly

**Priority:** P0

Expected:

Correct new loan balance.

---

## TS-LOAN-094 — Installment marked paid

**Priority:** P0

Expected:

Correct state.

---

## TS-LOAN-095 — Payment transaction created

**Priority:** P0

Expected:

Traceable transaction record.

---

## TS-LOAN-096 — Duplicate installment payment submission

**Priority:** P0

Expected:

No unintended double repayment.

---

# 20. Insufficient Funds Repayment

## TS-LOAN-097 — Repayment with insufficient account balance

**Priority:** P0

Expected:

Payment rejected or handled according to repayment rules.

---

## TS-LOAN-098 — Failed repayment leaves outstanding balance unchanged

**Priority:** P0

Expected:

No false loan reduction.

---

## TS-LOAN-099 — Repayment fee causes insufficient funds

**Priority:** P0

Expected:

Correct behavior according to fee model.

---

# 21. Partial Repayment Scenarios

Where supported.

## TS-LOAN-100 — Partial repayment

**Priority:** P1

Expected:

Outstanding balance decreases by correct amount.

---

## TS-LOAN-101 — Partial repayment below minimum allowed

**Priority:** P1

Expected:

Rejected.

---

## TS-LOAN-102 — Partial repayment above outstanding balance

**Priority:** P0

Expected:

Rejected or treated as payoff according to defined behavior.

---

## TS-LOAN-103 — Partial repayment updates future schedule

**Priority:** P0

Expected:

Schedule changes according to loan model.

---

# 22. Full Repayment Scenarios

## TS-LOAN-104 — Pay exact outstanding balance

**Priority:** P0

Expected:

Loan balance reaches zero.

---

## TS-LOAN-105 — Loan closes after final required payment

**Priority:** P0

Expected:

Status becomes CLOSED.

---

## TS-LOAN-106 — Additional repayment after loan closure

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-107 — Final installment rounding adjustment

**Priority:** P0

Expected:

Loan closes with exactly zero outstanding balance.

---

# 23. Early Repayment Scenarios

## TS-LOAN-108 — Request early repayment quote

**Priority:** P1

Expected:

Correct settlement amount displayed.

---

## TS-LOAN-109 — Early repayment includes applicable fee

**Priority:** P0

Expected:

Correct calculation.

---

## TS-LOAN-110 — Early payoff amount equals outstanding principal plus defined charges

**Priority:** P0

Expected:

Correct financial result.

---

## TS-LOAN-111 — Complete early repayment

**Priority:** P0

Expected:

Loan closes successfully.

---

## TS-LOAN-112 — Duplicate early payoff request

**Priority:** P0

Expected:

No overpayment or duplicate closure.

---

# 24. Overdue Loan Scenarios

Where overdue behavior is implemented.

## TS-LOAN-113 — Installment reaches due date unpaid

**Priority:** P1

Expected:

Status updated according to rules.

---

## TS-LOAN-114 — Loan becomes overdue

**Priority:** P1

Expected:

Correct overdue state.

---

## TS-LOAN-115 — Late fee calculated correctly

**Priority:** P0

Expected:

Correct financial charge.

---

## TS-LOAN-116 — Late payment clears overdue installment

**Priority:** P0

Expected:

Outstanding values update correctly.

---

## TS-LOAN-117 — Multiple overdue installments

**Priority:** P1

Expected:

Accurate aggregate overdue amount.

---

## TS-LOAN-118 — Default-state transition

**Priority:** P0

Expected:

Occurs only according to defined business rules.

---

# 25. Loan Cancellation Scenarios

Where cancellation is supported before disbursement.

## TS-LOAN-119 — Customer cancels submitted application

**Priority:** P1

Expected:

Status becomes CANCELLED where allowed.

---

## TS-LOAN-120 — Cancel approved but not yet disbursed loan

**Priority:** P1

Expected:

Behavior follows cancellation policy.

---

## TS-LOAN-121 — Attempt cancellation after disbursement

**Priority:** P0

Expected:

Rejected; repayment/settlement flow required instead.

---

## TS-LOAN-122 — Cancelled application cannot disburse

**Priority:** P0

Expected:

No funds credited.

---

# 26. Loan State Transition Scenarios

Example:

```text
SUBMITTED
   ↓
UNDER_REVIEW
   ↓
APPROVED
   ↓
DISBURSED
   ↓
ACTIVE
   ↓
CLOSED
```

Alternative:

```text
UNDER_REVIEW
   ↓
REJECTED
```

---

## TS-LOAN-123 — SUBMITTED → UNDER_REVIEW

**Priority:** P1

Expected:

Valid.

---

## TS-LOAN-124 — UNDER_REVIEW → APPROVED

**Priority:** P0

Expected:

Authorized transition.

---

## TS-LOAN-125 — UNDER_REVIEW → REJECTED

**Priority:** P1

Expected:

Valid.

---

## TS-LOAN-126 — APPROVED → DISBURSED

**Priority:** P0

Expected:

Exactly one financial disbursement.

---

## TS-LOAN-127 — DISBURSED → ACTIVE

**Priority:** P0

Expected:

Valid operational loan.

---

## TS-LOAN-128 — ACTIVE → CLOSED

**Priority:** P0

Expected:

Only after required settlement.

---

## TS-LOAN-129 — REJECTED → DISBURSED

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-130 — CLOSED → ACTIVE

**Priority:** P0

Expected:

Rejected.

---

# 27. Concurrency Scenarios

## TS-LOAN-131 — Two admins approve same application simultaneously

**Priority:** P0

Expected:

One valid approval outcome, no duplicate disbursement.

---

## TS-LOAN-132 — Admin approves while another rejects

**Priority:** P0

Expected:

Final state is consistent and auditable.

---

## TS-LOAN-133 — Customer pays installment from two sessions simultaneously

**Priority:** P0

Expected:

Installment not paid twice unintentionally.

---

## TS-LOAN-134 — Final repayment submitted twice

**Priority:** P0

Expected:

No negative outstanding balance.

---

## TS-LOAN-135 — Repayment and early-settlement request occur concurrently

**Priority:** P0

Expected:

Final financial state remains valid.

---

# 28. Financial Precision Scenarios

## TS-LOAN-136 — Interest calculated with supported decimal precision

**Priority:** P0

Expected:

No floating-point drift.

---

## TS-LOAN-137 — Installments sum correctly despite rounding

**Priority:** P0

Expected:

Final schedule reconciles.

---

## TS-LOAN-138 — Outstanding balance never becomes tiny unintended residual

**Priority:** P0

Example invalid result:

```text
Outstanding Balance = 0.0000001
```

Expected:

Financial rounding produces correct settled amount.

---

## TS-LOAN-139 — Large principal calculation

**Priority:** P0

Expected:

No overflow or precision loss.

---

# 29. Transaction History Integration

## TS-LOAN-140 — Loan disbursement appears as credit

**Priority:** P0

Expected:

Correct amount and reference.

---

## TS-LOAN-141 — Loan repayment appears as debit

**Priority:** P0

Expected:

Correct transaction.

---

## TS-LOAN-142 — Loan fee appears correctly

**Priority:** P1

Expected:

Financial representation matches design.

---

## TS-LOAN-143 — Rejected application creates no financial transaction

**Priority:** P0

Expected:

No account impact.

---

# 30. Statement Integration

## TS-LOAN-144 — Disbursement appears on statement

**Priority:** P0

Expected:

Correct credit.

---

## TS-LOAN-145 — Repayment appears on statement

**Priority:** P0

Expected:

Correct debit.

---

## TS-LOAN-146 — Loan-related fee appears on statement

**Priority:** P1

Expected:

Correct.

---

# 31. API Consistency Scenarios

## TS-LOAN-147 — Loan UI status matches API

**Priority:** P0

Expected:

Consistent.

---

## TS-LOAN-148 — API outstanding balance matches UI

**Priority:** P0

Expected:

Same valid financial amount.

---

## TS-LOAN-149 — API status matches database

**Priority:** P0

Expected:

No state disagreement.

---

## TS-LOAN-150 — Repayment API result matches account balance impact

**Priority:** P0

Expected:

Correct.

---

# 32. Database Validation Scenarios

## TS-LOAN-151 — Application record stored correctly

**Priority:** P1

Expected:

Correct customer, requested amount, term, and status.

---

## TS-LOAN-152 — Approved amount stored correctly

**Priority:** P0

Expected:

Correct.

---

## TS-LOAN-153 — Disbursement record stored once

**Priority:** P0

Expected:

No duplicate credit records.

---

## TS-LOAN-154 — Outstanding balance stored correctly after repayment

**Priority:** P0

Expected:

Correct persisted value.

---

## TS-LOAN-155 — Installment records reconcile with loan total

**Priority:** P0

Expected:

No orphan or incorrect schedule rows.

---

## TS-LOAN-156 — Closed loan outstanding balance equals zero

**Priority:** P0

Expected:

Exactly zero according to financial precision.

---

# 33. Error Handling Scenarios

## TS-LOAN-157 — Loan service unavailable during application

**Priority:** P1

Expected:

No false application success.

---

## TS-LOAN-158 — Server failure during approval

**Priority:** P0

Expected:

No partial approval/disbursement.

---

## TS-LOAN-159 — Network disconnect during repayment

**Priority:** P0

Expected:

Customer can determine actual repayment state without unsafe duplicate submission.

---

## TS-LOAN-160 — Slow repayment response

**Priority:** P0

Expected:

Duplicate clicks prevented.

---

## TS-LOAN-161 — Calculation service failure

**Priority:** P0

Expected:

Incorrect estimated financial values are not displayed as authoritative.

---

# 34. Notification Scenarios

## TS-LOAN-162 — Application submitted notification

**Priority:** P2

Expected:

Correct customer informed.

---

## TS-LOAN-163 — Approval notification

**Priority:** P1

Expected:

Correct approved amount and terms.

---

## TS-LOAN-164 — Rejection notification

**Priority:** P1

Expected:

Correct state communicated.

---

## TS-LOAN-165 — Disbursement notification

**Priority:** P0

Expected:

Amount matches actual credit.

---

## TS-LOAN-166 — Repayment notification

**Priority:** P1

Expected:

Correct installment/payment amount.

---

## TS-LOAN-167 — Loan closure notification

**Priority:** P1

Expected:

Customer informed loan is fully settled.

---

## TS-LOAN-168 — Failed repayment does not generate success notification

**Priority:** P0

Expected:

No misleading financial message.

---

# 35. Audit Scenarios

## TS-LOAN-169 — Application creation audited

**Priority:** P1

Expected:

Traceable.

---

## TS-LOAN-170 — Approval audited

**Priority:** P0

Expected:

Actor, loan, decision, timestamp.

---

## TS-LOAN-171 — Rejection audited

**Priority:** P1

Expected:

Actor/reason recorded.

---

## TS-LOAN-172 — Disbursement audited

**Priority:** P0

Expected:

Financial action traceable.

---

## TS-LOAN-173 — Repayment audited

**Priority:** P1

Expected:

Traceable.

---

## TS-LOAN-174 — Early settlement audited

**Priority:** P0

Expected:

Settlement traceable.

---

# 36. Security Scenarios

## TS-LOAN-175 — Manipulate requested amount client-side beyond maximum

**Priority:** P0

Expected:

Backend rejects invalid amount.

---

## TS-LOAN-176 — Manipulate interest rate in request

**Priority:** P0

Expected:

Backend ignores unauthorized client-supplied rate.

---

## TS-LOAN-177 — Manipulate approved amount as customer

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-178 — Manipulate approval status as customer

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-179 — Use another customer's loan ID

**Priority:** P0

Expected:

Authorization failure.

---

## TS-LOAN-180 — Modify repayment amount to invalid value through API

**Priority:** P0

Expected:

Backend validation enforced.

---

# 37. Search and Filtering Scenarios

## TS-LOAN-181 — Customer views active loans

**Priority:** P1

Expected:

Correct loans.

---

## TS-LOAN-182 — Customer views closed loan history

**Priority:** P2

Expected:

Historical loans available.

---

## TS-LOAN-183 — Admin filters applications by status

**Priority:** P2

Expected:

Correct results.

---

## TS-LOAN-184 — Admin searches application by customer

**Priority:** P2

Expected:

Authorized records only.

---

## TS-LOAN-185 — Search loan by reference

**Priority:** P1

Expected:

Correct loan returned.

---

# 38. Cross-Browser Scenarios

## TS-LOAN-186 — Loan application in Chrome

**Priority:** P1

Expected:

Works.

---

## TS-LOAN-187 — Loan application in Edge

**Priority:** P1

Expected:

Works.

---

## TS-LOAN-188 — Loan application in Firefox

**Priority:** P1

Expected:

Works.

---

## TS-LOAN-189 — Repayment workflow across supported browsers

**Priority:** P1

Expected:

No critical browser-specific failure.

---

# 39. Responsive Scenarios

## TS-LOAN-190 — Loan product page on desktop

**Priority:** P2

Expected:

Terms readable.

---

## TS-LOAN-191 — Loan application on tablet

**Priority:** P2

Expected:

Usable.

---

## TS-LOAN-192 — Loan application on mobile

**Priority:** P1

Expected:

Amount, interest, fees, and term remain clear.

---

## TS-LOAN-193 — Installment schedule on mobile

**Priority:** P1

Expected:

Readable without losing financial context.

---

## TS-LOAN-194 — Repayment confirmation on mobile

**Priority:** P0

Expected:

Amount and resulting financial effect clearly visible.

---

# 40. Accessibility and Usability Scenarios

## TS-LOAN-195 — Interest rate clearly labeled

**Priority:** P1

Expected:

No ambiguity.

---

## TS-LOAN-196 — Total repayable amount clearly visible

**Priority:** P0

Expected:

Customer understands total obligation.

---

## TS-LOAN-197 — Installment amount clearly visible before application submission

**Priority:** P0

Expected:

Correct.

---

## TS-LOAN-198 — Loan application supports keyboard navigation

**Priority:** P2

Expected:

Logical focus.

---

## TS-LOAN-199 — Rejection/error messages clear and actionable

**Priority:** P2

Expected:

Customer understands result.

---

# 41. Boundary Scenarios

## TS-LOAN-200 — Loan amount minimum minus smallest unit

**Priority:** P1

Expected:

Rejected.

---

## TS-LOAN-201 — Exact minimum amount

**Priority:** P1

Expected:

Accepted.

---

## TS-LOAN-202 — Minimum plus smallest unit

**Priority:** P2

Expected:

Accepted.

---

## TS-LOAN-203 — Maximum minus smallest unit

**Priority:** P1

Expected:

Accepted.

---

## TS-LOAN-204 — Exact maximum amount

**Priority:** P0

Expected:

Accepted according to rule.

---

## TS-LOAN-205 — Maximum plus smallest unit

**Priority:** P0

Expected:

Rejected.

---

## TS-LOAN-206 — Minimum term minus one

**Priority:** P1

Expected:

Rejected.

---

## TS-LOAN-207 — Exact minimum term

**Priority:** P1

Expected:

Accepted.

---

## TS-LOAN-208 — Exact maximum term

**Priority:** P1

Expected:

Accepted.

---

## TS-LOAN-209 — Maximum term plus one

**Priority:** P1

Expected:

Rejected.

---

# 42. End-to-End Loan Scenarios

## TS-LOAN-210 — Complete approved loan journey

**Priority:** P0

Flow:

```text
Login
→ View Loan Products
→ Select Product
→ Enter Valid Amount and Term
→ Review Interest and Fees
→ Submit Application
→ Admin Reviews
→ Admin Approves
→ Loan Disbursed
→ Verify Account Credit
→ Verify Loan ACTIVE
→ Verify Installment Schedule
→ Verify Notification
```

---

## TS-LOAN-211 — Rejected loan journey

**Priority:** P1

Flow:

```text
Submit Application
→ Admin Reviews
→ Rejects Application
→ Verify REJECTED
→ Verify No Disbursement
→ Verify Notification
→ Verify Audit
```

---

## TS-LOAN-212 — Standard repayment journey

**Priority:** P0

Flow:

```text
Active Loan
→ Installment Due
→ Select Repayment Account
→ Pay Installment
→ Verify Account Debit
→ Verify Installment Paid
→ Verify Outstanding Balance Reduced
→ Verify Transaction History
```

---

## TS-LOAN-213 — Final repayment journey

**Priority:** P0

Flow:

```text
Loan Near Completion
→ Pay Final Installment
→ Verify Outstanding Balance = 0
→ Verify Loan CLOSED
→ Attempt Additional Payment
→ Rejected
```

---

## TS-LOAN-214 — Early repayment journey

**Priority:** P0

Flow:

```text
Active Loan
→ Request Settlement Quote
→ Review Outstanding Principal and Fees
→ Confirm Early Repayment
→ Verify Debit
→ Verify Loan CLOSED
→ Verify Outstanding Balance = 0
```

---

## TS-LOAN-215 — Duplicate disbursement protection

**Priority:** P0

Flow:

```text
Approved Loan
→ Submit Disbursement Twice
→ Verify One Account Credit
→ Verify One Disbursement Record
→ Verify One Active Loan
```

---

# 43. Critical Smoke Scenarios

Loan smoke coverage should include:

```text
TS-LOAN-001 — View loan products
TS-LOAN-008 — View own loan
TS-LOAN-015 — Eligible customer
TS-LOAN-022 — Valid application
TS-LOAN-058 — Approval
TS-LOAN-077 — Correct disbursement
TS-LOAN-084 — Installment schedule
TS-LOAN-091 — Valid repayment
TS-LOAN-093 — Outstanding balance decreases
```

---

# 44. Critical Regression Scenarios

Always prioritize:

* Loan ownership
* Eligibility
* Minimum/maximum amount
* Terms
* Interest calculation
* Fee calculation
* Approval authorization
* Rejection
* Duplicate approval/disbursement
* Correct disbursement account
* Installment schedule
* Repayments
* Duplicate repayment
* Early settlement
* Final closure
* Overdue calculations
* State transitions
* UI/API/database consistency
* Audit

---

# 45. Automation Candidates

Strong UI candidates:

* Loan product viewing
* Valid application
* Boundary validation
* Loan status
* Admin approval/rejection
* Repayment
* Final repayment
* Closed-loan behavior

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 46. API Automation Candidates

Postman and REST Assured should later cover:

* Loan-product endpoints
* Eligibility
* Application creation
* Amount/term validation
* Unauthorized access
* Approval
* Rejection
* Disbursement
* Repayment
* Duplicate repayment
* Early settlement
* State transitions

---

# 47. SQL Validation Candidates

Database testing should validate:

* Loan owner
* Requested amount
* Approved amount
* Principal
* Interest rate
* Fees
* Outstanding balance
* Status
* Disbursement record
* Installments
* Repayment records
* Closure
* Audit records

---

# 48. Performance Testing Candidates

JMeter may later cover:

* Loan product retrieval
* Application submission
* Admin application queues
* Repayment endpoint load
* Concurrent repayment/idempotency behavior

Financial calculations must remain correct under concurrent execution.

---

# 49. BDD Candidates

Example:

```gherkin
Feature: Loan repayment

Scenario: Customer successfully pays a loan installment
  Given the customer has an active loan
  And an unpaid installment is due
  And the repayment account has sufficient balance
  When the customer pays the installment
  Then the repayment should complete successfully
  And the repayment account balance should decrease correctly
  And the loan outstanding balance should decrease
  And the installment should be marked as paid
```

Approval example:

```gherkin
Scenario: A rejected loan is not disbursed
  Given a customer has submitted a loan application
  When an authorized loan officer rejects the application
  Then the application should be marked as rejected
  And no loan funds should be credited to the customer
```

---

# 50. Risk Traceability

Major related risks include:

```text
RISK-001 — Incorrect account balance
RISK-011 — Incorrect loan interest calculation
RISK-013 — Concurrent transactions corrupt balance
RISK-021 — Sensitive information exposure
RISK-023 — Unauthorized administrative action
RISK-030 — Unauthorized API request
RISK-037 — Frontend-only validation
RISK-039 — API/database inconsistency
RISK-048 — UI reports false success
```

---

# 51. Loan Coverage Summary

This catalog covers:

* Loan products
* Ownership
* Authorization
* Eligibility
* Applications
* Amount boundaries
* Terms
* Interest
* Fees
* Statuses
* Administrative review
* Approval
* Rejection
* Disbursement
* Installment schedules
* Repayments
* Partial repayments
* Full repayment
* Early repayment
* Overdue behavior
* Cancellation
* State transitions
* Concurrency
* Financial precision
* Transaction history
* Statements
* API consistency
* Database validation
* Error handling
* Notifications
* Audit
* Security
* Search/filtering
* Responsive behavior
* Cross-browser behavior
* Accessibility
* End-to-end flows

---

# 52. Final Loan Testing Principle

Loan testing must validate the entire financial lifecycle, not merely whether the customer can submit an application.

For every critical loan workflow, QA should be able to answer:

```text
Was the customer eligible?

Was the requested amount valid?

Were interest and fees calculated correctly?

Was approval performed by an authorized actor?

Was the loan disbursed exactly once?

Was the correct account credited?

Is the installment schedule mathematically correct?

Does every repayment reduce the correct balances?

Can repayments be duplicated?

Does early settlement calculate correctly?

Does the loan reach exactly zero outstanding balance when closed?

Do UI, API, database, statements, and transaction history agree?

Are all important decisions and financial actions auditable?
```

Loan testing must prove both workflow correctness and financial reconciliation across the entire loan lifecycle.
