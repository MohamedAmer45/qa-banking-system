# Banking System — User Acceptance Testing (UAT) Scenarios

## 1. Document Information

| Field     | Value                          |
| --------- | ------------------------------ |
| Project   | Banking System Testing Project |
| Document  | UAT Scenarios                  |
| Version   | 1.0                            |
| Status    | Draft                          |
| Owner     | QA / Business                  |
| Test Type | User Acceptance Testing        |

---

# 2. Purpose

This document defines business-focused User Acceptance Testing scenarios for the Banking System.

UAT verifies that the system supports real business workflows and is suitable for intended users.

UAT is not intended to replace:

* Functional testing
* Security testing
* API testing
* Database testing
* Regression testing
* Performance testing

Instead, UAT answers:

```text
Can customers and banking staff successfully complete the
business workflows the system was designed to support?
```

---

# 3. UAT Objectives

UAT should confirm that:

* Customers can access and manage their banking relationship.
* Customers can safely view and move money.
* Financial information is understandable.
* Banking operations follow expected business rules.
* Administrative users can perform authorized duties.
* Business workflows are usable from beginning to end.
* Critical customer journeys behave consistently.
* Business stakeholders accept the implemented behavior.

---

# 4. UAT Participants

Possible UAT participants include:

```text
Business Analyst

Product Owner

Operations Representative

Customer Service Representative

Loan Operations Representative

KYC / Compliance Representative

Finance / Reconciliation Representative

Selected Business Users

QA Facilitator
```

QA may support UAT but business stakeholders should own the final business acceptance decision.

---

# 5. UAT Scenario Naming Convention

Use:

```text
UAT-XXX
```

Examples:

```text
UAT-001
UAT-002
UAT-003
```

---

# 6. UAT Result Statuses

Use:

```text
NOT_RUN

PASS

FAIL

BLOCKED

ACCEPTED_WITH_OBSERVATION
```

---

# 7. UAT Entry Criteria

UAT should begin when:

* Core development is complete.
* Release candidate is deployed.
* Smoke testing passes.
* Critical regression passes.
* No unresolved Critical/P0 defect blocks UAT.
* Required test data exists.
* UAT environment is stable.
* Business requirements are available.
* Known limitations are documented.
* UAT participants have access.

---

# 8. UAT Exit Criteria

UAT may be considered complete when:

* Required scenarios executed.
* Critical business journeys pass.
* No unresolved UAT blocker remains.
* Business-critical financial workflows are accepted.
* Known issues are documented.
* Residual risks are understood.
* Business signoff decision is recorded.

---

# 9. UAT Test Data

Recommended synthetic personas:

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

KYC:
VERIFIED
```

## Customer Requiring KYC

```text
Customer:
CUST-003

KYC:
PENDING
```

## Operations Administrator

```text
ADMIN-002
Role:
OPERATIONS_ADMIN
```

## KYC Reviewer

```text
ADMIN-003
Role:
KYC_REVIEWER
```

## Loan Officer

```text
ADMIN-004
Role:
LOAN_OFFICER
```

---

# 10. UAT-001 — Customer Login and Account Access

## Business Goal

A valid customer should be able to securely access their banking account.

## Preconditions

```text
Customer:
CUST-001

Status:
ACTIVE

MFA:
ENABLED
```

## Business Steps

1. Open the Banking System.
2. Enter valid credentials.
3. Complete MFA.
4. Open dashboard.
5. View account list.

## Expected Business Outcome

The customer successfully reaches their banking dashboard and sees only their own accounts.

## Acceptance Criteria

```text
Login successful.

MFA successful.

Dashboard understandable.

Owned accounts visible.

No unrelated customer information visible.
```

## Priority

P0

---

# 11. UAT-002 — Customer Reviews Account Balance

## Business Goal

Customer should understand how much money is currently available.

## Steps

1. Login.
2. Open primary account.
3. Review current balance.
4. Review available balance.
5. Review recent transactions.

## Expected Outcome

Balances are visible, understandable, and consistent with recent financial activity.

## Acceptance Criteria

* Current balance clearly displayed.
* Available balance clearly displayed.
* Currency visible.
* Recent transaction information understandable.
* No contradictory financial information.

## Priority

P0

---

# 12. UAT-003 — Add New Beneficiary

## Business Goal

Customer should be able to safely prepare a new recipient for future transfers.

## Steps

1. Login.
2. Open Beneficiaries.
3. Select Add Beneficiary.
4. Enter valid recipient details.
5. Submit.
6. Complete required verification.
7. Review beneficiary status.

## Expected Outcome

Beneficiary is created according to business rules.

## Acceptance Criteria

```text
Recipient details accepted.

Verification completed.

Beneficiary state understandable.

Customer knows whether beneficiary is immediately usable.
```

## Priority

P1

---

# 13. UAT-004 — Transfer to Active Beneficiary

## Business Goal

Customer should be able to move money to an approved beneficiary.

## Preconditions

```text
Source account:
ACTIVE

Beneficiary:
ACTIVE

Balance:
Sufficient
```

## Steps

1. Login.
2. Open Transfers.
3. Select source account.
4. Select beneficiary.
5. Enter amount.
6. Review fee.
7. Review confirmation.
8. Confirm transfer.
9. Review result.

## Expected Outcome

One transfer completes successfully.

## Acceptance Criteria

```text
Correct source account.

Correct recipient.

Correct amount.

Fee visible before confirmation.

Total debit understandable.

One transaction completed.

Confirmation/reference available.

Balances update correctly.
```

## Priority

P0

---

# 14. UAT-005 — Customer Cancels Transfer Before Confirmation

## Business Goal

Customer should be able to safely abandon a transfer before execution.

## Steps

1. Begin valid transfer.
2. Reach confirmation screen.
3. Select Cancel/Back.
4. Return to transfer/dashboard.
5. Check account balance and history.

## Expected Outcome

No transfer occurs.

## Acceptance Criteria

```text
No debit.

No credit.

No completed transaction.

No success notification.
```

## Priority

P1

---

# 15. UAT-006 — Transfer Rejected for Insufficient Funds

## Business Goal

Customer should receive clear feedback when funds are insufficient.

## Steps

1. Select account.
2. Enter transfer amount greater than available balance.
3. Submit.

## Expected Outcome

Transfer is rejected clearly without affecting funds.

## Acceptance Criteria

```text
Reason understandable.

No financial effect.

Balance remains unchanged.

No misleading success state.
```

## Priority

P0

---

# 16. UAT-007 — Transfer Fee Transparency

## Business Goal

Customer should understand the full cost before committing to a transfer.

## Steps

1. Create transfer with applicable fee.
2. Reach confirmation.

## Expected Outcome

The confirmation clearly displays:

```text
Transfer amount

Fee

Total debit
```

## Acceptance Criteria

The customer can make an informed decision before confirming.

## Priority

P0

---

# 17. UAT-008 — Scheduled Transfer

## Business Goal

Customer should be able to schedule a future transfer.

## Steps

1. Create transfer.
2. Select future date.
3. Confirm schedule.
4. Review scheduled transactions.

## Expected Outcome

Scheduled transfer appears with correct date and amount.

## Acceptance Criteria

```text
Schedule date correct.

Source/recipient correct.

Amount correct.

Status understandable.

Customer can identify upcoming transfer.
```

## Priority

P1

---

# 18. UAT-009 — Cancel Scheduled Transfer

## Business Goal

Customer should be able to cancel an eligible future transfer.

## Steps

1. Open scheduled transactions.
2. Select eligible scheduled transfer.
3. Cancel.
4. Confirm cancellation.

## Expected Outcome

Transfer becomes cancelled and should not execute.

## Acceptance Criteria

```text
Cancellation confirmation shown.

Status becomes CANCELLED.

No execution occurs at scheduled time.
```

## Priority

P0

---

# 19. UAT-010 — Recurring Transfer Setup

## Business Goal

Customer should be able to configure recurring money movement.

## Steps

1. Create transfer.
2. Select recurrence.
3. Choose frequency/date.
4. Review schedule.
5. Confirm.

## Expected Outcome

Recurring transfer is created according to selected schedule.

## Acceptance Criteria

* Frequency understandable.
* Next execution date visible.
* Amount visible.
* Recipient visible.
* Customer can review/cancel recurrence.

## Priority

P1

---

# 20. UAT-011 — Successful Bill Payment

## Business Goal

Customer should be able to pay a valid bill.

## Steps

1. Open Payments.
2. Select payee/bill.
3. Enter required reference.
4. Enter amount if applicable.
5. Review payment.
6. Confirm.

## Expected Outcome

Payment completes successfully.

## Acceptance Criteria

```text
Correct bill/payee.

Correct amount.

Fee visible if applicable.

Success confirmation.

Reference available.

History updated.
```

## Priority

P0

---

# 21. UAT-012 — Failed Payment

## Business Goal

Customer should clearly understand a failed payment without losing available funds incorrectly.

## Expected Outcome

```text
Payment failure clearly shown.

No completed debit remains.

No false success notification.
```

## Priority

P0

---

# 22. UAT-013 — Freeze Card

## Business Goal

A customer should be able to quickly stop use of a card.

## Steps

1. Open Cards.
2. Select active card.
3. Choose Freeze.
4. Confirm.

## Expected Outcome

Card becomes frozen immediately according to business rules.

## Acceptance Criteria

```text
Card status clearly changes.

Customer understands card is unavailable.

Unfreeze option available where permitted.
```

## Priority

P0

---

# 23. UAT-014 — Unfreeze Card

## Business Goal

Customer should be able to restore use of an eligible frozen card.

## Expected Outcome

Card returns to active state.

## Priority

P1

---

# 24. UAT-015 — Permanently Block Card

## Business Goal

Customer should be able to report/block a card that should no longer be used.

## Expected Outcome

Card reaches the appropriate irreversible/controlled blocked state.

## Acceptance Criteria

* Warning clearly explains consequence.
* Confirmation required.
* Card status clearly changes.
* Replacement options shown if supported.

## Priority

P0

---

# 25. UAT-016 — Change Card Spending Limit

## Business Goal

Customer should be able to manage spending limits where supported.

## Steps

1. Open card settings.
2. Change supported limit.
3. Confirm.
4. Review updated limit.

## Expected Outcome

Updated valid limit is clearly displayed and applied.

## Priority

P1

---

# 26. UAT-017 — Apply for Loan

## Business Goal

Eligible customer should be able to submit a loan application.

## Steps

1. Open Loans.
2. Select product.
3. Enter amount.
4. Select term.
5. Review estimated repayment.
6. Submit application.

## Expected Outcome

Application is accepted into the correct review state.

## Acceptance Criteria

```text
Requested amount correct.

Term correct.

Interest/repayment information understandable.

Application reference available.

Status visible.
```

## Priority

P1

---

# 27. UAT-018 — Ineligible Loan Application

## Business Goal

Customer should understand when they cannot proceed with a loan.

## Expected Outcome

Application is prevented/rejected with clear business reason where appropriate.

## Priority

P1

---

# 28. UAT-019 — Loan Approval by Authorized Officer

## Business Goal

Loan officer should be able to review and approve eligible applications.

## Actor

```text
ADMIN-004
LOAN_OFFICER
```

## Steps

1. Login as loan officer.
2. Open application.
3. Review details.
4. Approve.
5. Verify new status.

## Expected Outcome

Loan reaches approved state.

## Priority

P0

---

# 29. UAT-020 — Loan Disbursement

## Business Goal

Approved loan should result in one correct disbursement.

## Expected Outcome

```text
Loan amount credited once.

Loan state updated.

Customer can see resulting balance/history.

Repayment schedule available.
```

## Priority

P0

---

# 30. UAT-021 — Loan Repayment

## Business Goal

Customer should be able to repay an installment.

## Steps

1. Open active loan.
2. Review amount due.
3. Submit repayment.
4. Review loan balance.

## Expected Outcome

Outstanding amount decreases correctly.

## Priority

P0

---

# 31. UAT-022 — Open Deposit

## Business Goal

Customer should be able to place eligible funds into a deposit product.

## Steps

1. Open Deposits.
2. Select product.
3. Select funding account.
4. Enter principal.
5. Select term.
6. Review rate/maturity.
7. Confirm.

## Expected Outcome

Deposit created successfully.

## Acceptance Criteria

```text
Principal correct.

Funding debit correct.

Rate visible.

Term visible.

Maturity date visible.
```

## Priority

P0

---

# 32. UAT-023 — Deposit Maturity

## Business Goal

Customer should receive correct matured funds according to selected product.

## Expected Outcome

```text
Principal + applicable interest
is paid according to product rules.

One payout only.
```

## Priority

P0

---

# 33. UAT-024 — Early Deposit Withdrawal

## Business Goal

Customer should understand financial consequences before ending a deposit early.

## Steps

1. Open active deposit.
2. Select early withdrawal.
3. Review penalty.
4. Review expected payout.
5. Confirm.

## Expected Outcome

Customer sees transparent calculation before confirmation.

## Priority

P0

---

# 34. UAT-025 — Transaction History Review

## Business Goal

Customer should be able to understand previous account activity.

## Steps

1. Open Transaction History.
2. Review recent transfer/payment.
3. Open transaction detail.

## Expected Outcome

The customer can identify:

* Transaction type
* Amount
* Date
* Status
* Reference
* Debit/credit direction

## Priority

P1

---

# 35. UAT-026 — Search and Filter Transactions

## Business Goal

Customer should be able to locate a historical transaction.

## Expected Outcome

Search/filter behavior is understandable and returns expected results.

## Priority

P2

---

# 36. UAT-027 — Download Bank Statement

## Business Goal

Customer should be able to obtain a usable account statement.

## Steps

1. Open Statements.
2. Select account.
3. Select period.
4. Generate.
5. Download.

## Expected Outcome

Statement is generated successfully.

## Acceptance Criteria

```text
Correct account.

Correct period.

Opening balance visible.

Transactions understandable.

Fees represented.

Closing balance visible.

Download usable.
```

## Priority

P0

---

# 37. UAT-028 — Statement Reconciliation

## Business Goal

Business/finance representative should confirm that a statement accurately reflects account activity.

## Expected Outcome

```text
Opening
+ Credits
- Debits
- Fees
=
Closing
```

## Priority

P0

---

# 38. UAT-029 — Customer Updates Profile

## Business Goal

Customer should be able to maintain permitted personal details.

## Expected Outcome

Allowed changes save correctly.

Protected banking fields remain non-editable.

## Priority

P1

---

# 39. UAT-030 — Change Password

## Business Goal

Customer should be able to securely update credentials.

## Expected Outcome

* New password accepted.
* Old password no longer works.
* Security confirmation/alert generated according to policy.
* Session behavior follows policy.

## Priority

P0

---

# 40. UAT-031 — Manage MFA

## Business Goal

Customer should be able to configure MFA securely according to bank policy.

## Expected Outcome

Changes require sufficient verification and are reflected clearly.

## Priority

P0

---

# 41. UAT-032 — Notification Review

## Business Goal

Customer should receive understandable notifications for important events.

Review:

```text
Transfer

Payment

Card freeze

Password change

Loan decision

Deposit maturity
```

## Expected Outcome

Notifications accurately reflect authoritative event state.

## Priority

P1

---

# 42. UAT-033 — KYC Review by Authorized Reviewer

## Actor

```text
ADMIN-003
KYC_REVIEWER
```

## Business Goal

Authorized reviewer can process a customer's KYC state.

## Expected Outcome

Decision updates appropriate customer state and is traceable.

## Priority

P0

---

# 43. UAT-034 — Customer Restrictions After KYC Change

## Business Goal

Business rules should restrict products appropriately when KYC status is not sufficient.

## Steps

1. Use customer with restricted KYC state.
2. Attempt restricted banking product/action.

## Expected Outcome

System applies expected business restriction.

## Priority

P0

---

# 44. UAT-035 — Operations Admin Freezes Account

## Actor

```text
OPERATIONS_ADMIN
```

## Business Goal

Authorized bank operations user should be able to freeze an account.

## Expected Outcome

* Account status changes.
* Customer-facing behavior reflects freeze.
* Prohibited financial actions stop.
* Audit record exists.

## Priority

P0

---

# 45. UAT-036 — Operations Admin Unfreezes Account

## Business Goal

Authorized admin should restore an eligible account.

## Expected Outcome

Account returns to permitted state with audit trail.

## Priority

P0

---

# 46. UAT-037 — Read-Only Admin Reviews Customer

## Actor

```text
READ_ONLY_ADMIN
```

## Business Goal

Read-only staff should inspect information without changing state.

## Expected Outcome

Required information visible.

State-changing actions unavailable and rejected.

## Priority

P0

---

# 47. UAT-038 — Admin Reviews Transaction

## Business Goal

Authorized operations staff should investigate transaction details.

## Expected Outcome

Staff can identify:

```text
Customer

Account

Amount

Status

Reference

Timestamp

Related reversal where applicable
```

without exposing unnecessary secrets.

## Priority

P1

---

# 48. UAT-039 — Authorized Transaction Reversal

## Business Goal

Authorized staff should be able to reverse an eligible transaction according to business rules.

## Expected Outcome

```text
Original transaction preserved.

Reversal created separately.

Balances updated correctly.

Audit created.

Customer history reflects reversal.
```

## Priority

P0

---

# 49. UAT-040 — Admin Audit Review

## Business Goal

Authorized audit/operations staff should trace critical administrative activity.

## Expected Outcome

Audit shows:

```text
Actor

Action

Target

Time

Old state

New state

Reason where applicable
```

## Priority

P0

---

# 50. UAT-041 — Customer Attempts Restricted Resource

## Business Goal

A customer should never gain access to another customer's banking resource.

## Scenario

Customer A attempts to access a resource owned by Customer B.

## Expected Outcome

Access denied with no protected information exposed.

## Priority

P0

---

# 51. UAT-042 — Customer Session Timeout

## Business Goal

An inactive session should end safely.

## Expected Outcome

Customer must re-authenticate before accessing protected banking functionality.

No financial action should complete from expired state.

## Priority

P0

---

# 52. UAT-043 — Customer Logs Out

## Business Goal

Customer should be confident that logout ends account access.

## Expected Outcome

Protected banking functions are no longer usable.

## Priority

P0

---

# 53. UAT-044 — Customer Uses Mobile View

## Business Goal

Customer should be able to safely complete critical banking actions from a supported mobile viewport.

Test:

* Login
* Account balance
* Transfer
* Payment
* Card freeze
* Statement

## Acceptance Criteria

Financial values and confirmation information remain readable and actionable.

## Priority

P1

---

# 54. UAT-045 — Arabic / RTL Experience

Where supported.

## Business Goal

Arabic-speaking customers should understand and use critical workflows correctly.

Review:

* Navigation
* Account information
* Transfer confirmation
* Monetary values
* Notifications

## Expected Outcome

RTL rendering does not alter financial meaning or usability.

## Priority

P1

---

# 55. UAT-046 — Customer Handles Transfer Failure

## Business Goal

Customer should understand what happened when a transfer cannot complete.

## Expected Outcome

System clearly indicates failure without implying money was transferred.

Customer can determine whether retry is appropriate.

## Priority

P0

---

# 56. UAT-047 — Customer Handles Payment Failure

## Business Goal

Payment failure should be clear and financially safe.

## Acceptance Criteria

```text
Failure status clear.

No false success.

Balance understandable.

Retry path safe.
```

## Priority

P0

---

# 57. UAT-048 — Account Closure Request

Where supported.

## Business Goal

Customer/admin should understand why an account can or cannot be closed.

Check dependencies such as:

* Remaining balance
* Pending transactions
* Active products
* Holds

## Expected Outcome

Closure rules are understandable and consistently enforced.

## Priority

P1

---

# 58. UAT-049 — Customer Reviews Fees

## Business Goal

Customer should understand applicable banking charges.

Review fees for representative:

* Transfer
* Payment
* Early deposit withdrawal
* Other supported fees

## Expected Outcome

Fees are displayed before commitment where required.

## Priority

P1

---

# 59. UAT-050 — End-to-End Active Customer Journey

## Business Goal

Validate a representative complete banking journey.

## Flow

```text
Login
↓
MFA
↓
Review Account
↓
Add Beneficiary
↓
Transfer Money
↓
Review Transaction
↓
Download Statement
↓
Logout
```

## Expected Outcome

Entire journey is understandable and completes without inconsistent state.

## Priority

P0

---

# 60. UAT-051 — End-to-End Loan Journey

## Flow

```text
Customer Applies
↓
Loan Officer Reviews
↓
Loan Approved
↓
Disbursement
↓
Customer Reviews Schedule
↓
Customer Repays
↓
Loan Balance Updates
```

## Expected Outcome

The full business lifecycle is understandable to both customer and staff.

## Priority

P0

---

# 61. UAT-052 — End-to-End Deposit Journey

## Flow

```text
Customer Opens Deposit
↓
Funding Account Debited
↓
Deposit Active
↓
Maturity
↓
Interest Applied
↓
Payout
↓
Customer Reviews History/Statement
```

## Expected Outcome

Financial results and states are understandable and correct.

## Priority

P0

---

# 62. UAT-053 — Customer Security Journey

## Flow

```text
Login
↓
MFA
↓
Change Password
↓
Security Notification
↓
Logout
↓
Login With New Password
```

## Expected Outcome

The customer can complete the security lifecycle without confusion.

## Priority

P0

---

# 63. UAT-054 — Operations Investigation Journey

## Actor

Operations Admin

## Flow

```text
Search Customer
↓
Open Account
↓
Review Transaction
↓
Review Related Audit
↓
Take Authorized Action
↓
Confirm Audit Entry
```

## Expected Outcome

Operations staff can investigate and act without needing unauthorized workarounds.

## Priority

P1

---

# 64. UAT-055 — Critical Incident: Freeze Account

## Business Goal

Bank staff should be able to respond to suspected account risk.

## Flow

```text
Admin Searches Customer
↓
Freezes Account
↓
Customer Attempts Transfer
↓
Transfer Rejected
↓
Audit Reviewed
```

## Expected Outcome

Freeze has immediate business effect and is traceable.

## Priority

P0

---

# 65. UAT-056 — Critical Incident: Freeze Card

## Flow

```text
Customer/Admin Freezes Card
↓
Purchase Attempt
↓
Purchase Rejected
↓
Card Status Reviewed
```

## Expected Outcome

Freeze protects the customer immediately according to product rules.

## Priority

P0

---

# 66. UAT-057 — Financial Reconciliation Journey

## Actor

Finance / Operations Representative

## Flow

```text
Review Account Opening Balance
↓
Review Transfers/Payments/Fees
↓
Review Closing Balance
↓
Review Statement
```

## Expected Outcome

Financial activity can be reconciled without unexplained difference.

## Priority

P0

---

# 67. UAT-058 — Customer Understands Transaction Status

Review statuses such as:

```text
PENDING
PROCESSING
COMPLETED
FAILED
CANCELLED
REVERSED
```

## Expected Outcome

Status terminology is understandable and does not mislead the customer.

## Priority

P1

---

# 68. UAT-059 — Customer Understands Product Status

Review:

```text
Account ACTIVE / FROZEN / CLOSED

Card ACTIVE / FROZEN / BLOCKED

Loan UNDER_REVIEW / APPROVED / ACTIVE / CLOSED

Deposit ACTIVE / MATURED / CLOSED
```

## Expected Outcome

Status labels and available actions make business sense.

## Priority

P1

---

# 69. UAT-060 — Customer Recovers From Validation Error

## Business Goal

Validation errors should help rather than trap customers.

Example:

```text
Invalid transfer amount
```

Expected:

* Clear explanation
* Previous valid inputs retained where appropriate
* Customer can correct and continue

## Priority

P2

---

# 70. Business Acceptance Criteria

The Banking System should generally be accepted only if business users confirm:

```text
Core journeys are complete.

Financial values are understandable.

Critical controls behave as expected.

Customers receive meaningful feedback.

Operational workflows are usable.

No critical business process requires unsafe workaround.
```

---

# 71. UAT Critical Scenario Set

The minimum critical UAT set should include:

```text
UAT-001 — Login

UAT-002 — Account balance

UAT-004 — Transfer

UAT-006 — Insufficient funds

UAT-007 — Fee transparency

UAT-009 — Cancel scheduled transfer

UAT-011 — Payment

UAT-013 — Freeze card

UAT-015 — Block card

UAT-020 — Loan disbursement

UAT-021 — Loan repayment

UAT-022 — Deposit opening

UAT-023 — Deposit maturity

UAT-027 — Statement

UAT-028 — Statement reconciliation

UAT-030 — Password change

UAT-035 — Admin freeze

UAT-037 — Read-only admin

UAT-039 — Reversal

UAT-041 — Customer isolation

UAT-042 — Session timeout

UAT-043 — Logout

UAT-050 — End-to-end customer

UAT-051 — End-to-end loan

UAT-052 — End-to-end deposit

UAT-055 — Account freeze incident

UAT-057 — Reconciliation
```

---

# 72. UAT Execution Template

| UAT ID  | Business Journey   | Actor                 | Priority | Result  | Defect/Observation |
| ------- | ------------------ | --------------------- | -------- | ------- | ------------------ |
| UAT-001 | Login and accounts | Customer              | P0       | NOT_RUN | —                  |
| UAT-004 | Transfer           | Customer              | P0       | NOT_RUN | —                  |
| UAT-020 | Loan disbursement  | Loan Officer/Customer | P0       | NOT_RUN | —                  |
| UAT-035 | Freeze account     | Operations            | P0       | NOT_RUN | —                  |

---

# 73. Detailed UAT Result Template

```text
## UAT Result

UAT ID:

Scenario:

Business Actor:

Tester:

Environment:

Build:

Date:

Preconditions:

Business Steps:

Expected Business Outcome:

Actual Outcome:

Result:
PASS / FAIL / BLOCKED / ACCEPTED_WITH_OBSERVATION

Defect:

Observation:

Business Impact:

Evidence:

Business Comments:
```

---

# 74. UAT Defect Handling

If UAT reveals a defect:

```text
UAT Scenario
↓
Defect Created
↓
Severity/Priority Review
↓
Fix
↓
QA Retest
↓
Regression
↓
Business UAT Retest
```

Critical UAT defects should not be bypassed simply because technical QA passed.

---

# 75. UAT Observation Handling

Not every business concern is necessarily a defect.

Example:

```text
Business user wants clearer wording on deposit maturity.
```

May become:

* UX improvement
* Requirement clarification
* Product backlog item

Record it rather than losing the feedback.

---

# 76. UAT Failure Examples

Examples of UAT failure:

```text
Customer cannot understand total transfer cost.

Loan officer cannot determine why application is ineligible.

Deposit maturity amount differs from customer expectation.

Read-only admin needs a prohibited workaround to perform daily duties.

Statement cannot be reconciled by finance user.

Mobile customer cannot see required financial confirmation.
```

---

# 77. UAT Release Blocking Examples

Normally release blocking:

```text
Customer cannot login.

Core transfer journey fails.

Wrong financial amount shown/processed.

Customer can access another customer's information.

Loan disbursement incorrect.

Deposit payout incorrect.

Statement does not reconcile.

Operations cannot perform critical incident action.

Critical business workflow unusable.
```

---

# 78. UAT Metrics

Track:

```text
Total scenarios

Executed

Passed

Failed

Blocked

Accepted with observation

P0 failures

Open UAT defects

Business acceptance status
```

---

# 79. UAT Summary Template

```text
## UAT Summary

Release:

Build:

Environment:

Total Planned:

Executed:

Passed:

Failed:

Blocked:

Accepted With Observation:

P0 Failed:

Open Critical Defects:

Open High Defects:

Business Acceptance:
APPROVED / APPROVED_WITH_CONDITIONS / REJECTED

Comments:
```

---

# 80. UAT Traceability

Example:

```text
Business Requirement:
Customer transfers money

↓
Requirement:
REQ-TRF-001

↓
Functional Testing:
transfer-scenarios.md

↓
Regression:
REG-016

↓
UAT:
UAT-004

↓
Business Result:
PASS
```

---

# 81. UAT vs Functional Testing

Functional testing asks:

```text
Does the feature meet detailed requirements?
```

UAT asks:

```text
Does the feature actually satisfy the business/user need?
```

Example:

Functional test:

```text
Transfer API returns COMPLETED
and balances reconcile.
```

UAT:

```text
Can the customer understand the recipient,
amount, fee, total debit,
and confidently complete the transfer?
```

Both matter.

---

# 82. UAT vs Security Testing

UAT may confirm:

```text
Customer cannot see another customer's account.
```

But dedicated security testing should still perform deeper authorization testing.

UAT does not replace adversarial or technical security validation.

---

# 83. UAT vs Performance Testing

A UAT participant may report:

```text
Transfers feel too slow.
```

That is useful business feedback.

Formal performance testing should still measure:

```text
Response time

Latency

Throughput

Error rate

Concurrency
```

---

# 84. Current UAT Status

Current project status:

```text
UAT Scenarios:
DESIGNED

Actual UAT Execution:
PENDING

Business Signoff:
PENDING
```

---

# 85. Readiness for UAT

Before actual UAT begins, complete:

```text
Application implementation

Deployment

Real smoke execution

Critical regression

Required security checks

Stable test data

Business user access
```

---

# 86. Final UAT Principle

User Acceptance Testing should answer:

```text
Can a real customer successfully use the bank?

Can bank staff safely operate the system?

Are financial outcomes understandable?

Do business workflows make sense from end to end?

Can stakeholders accept the remaining risk?
```

The core rule is:

```text
A technically correct Banking System is not complete
until its critical business workflows
are accepted by the people responsible for using
and operating those workflows.
```
