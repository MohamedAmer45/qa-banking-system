# Banking System — Defect Severity & Priority Guide

## 1. Document Information

| Field    | Value                            |
| -------- | -------------------------------- |
| Project  | Banking System Testing Project   |
| Document | Defect Severity & Priority Guide |
| Version  | 1.0                              |
| Status   | Active                           |
| Owner    | QA Engineering                   |

---

# 2. Purpose

This document defines a consistent framework for assigning:

* **Severity** — how serious the defect's impact is.
* **Priority** — how urgently the defect should be fixed.

The guide is tailored for a Banking System where defects may affect:

* Customer money
* Account balances
* Authentication
* Authorization
* Privacy
* Financial calculations
* Transaction integrity
* Auditability
* Regulatory/business processes
* Availability
* User experience

Severity and priority should be assigned based on measurable impact rather than subjective judgment.

---

# 3. Severity vs Priority

Severity answers:

```text
How badly does this defect affect the system or customer?
```

Priority answers:

```text
How urgently should this defect be fixed?
```

They are related but not identical.

Example:

```text
A typo on the production login page:
Severity = Low
Priority = High
```

if it must be corrected immediately for business reasons.

Another example:

```text
Rare duplicate transfer under concurrency:
Severity = Critical
Priority = P0
```

because even low-frequency financial corruption is unacceptable.

---

# 4. Severity Levels

This project uses:

```text
CRITICAL
HIGH
MEDIUM
LOW
```

---

# 5. Critical Severity

## Definition

A Critical defect causes or could cause:

* Incorrect customer balances
* Money loss
* Money duplication
* Unauthorized financial activity
* Authentication bypass
* Major authorization bypass
* Customer-to-customer data exposure
* Privilege escalation
* Financial-data corruption
* Duplicate disbursement
* Duplicate payout
* Irrecoverable data loss
* Major banking-system outage
* Severe audit/integrity failure

A workaround generally does not make a financial or security corruption issue noncritical.

---

# 6. Critical Banking Examples

## Financial Integrity

```text
Transfer debits source account twice.

Deposit maturity pays customer twice.

Loan disbursement credits customer twice.

Failed transfer still reduces balance.

Transfer credits destination without debiting source.

Account balance becomes negative despite overdraft not being supported.
```

---

## Authentication / Authorization

```text
User bypasses MFA.

Customer accesses another customer's account.

Customer can transfer from another customer's account.

Read-only admin can reverse transactions.

Customer can modify own role to ADMIN.
```

---

## Data Exposure

```text
Customer can download another customer's bank statement.

API returns another customer's transactions.

Access tokens are exposed in audit logs.
```

---

## Data Integrity

```text
Transaction records disappear.

Reversal deletes original transaction history.

Database corruption causes account balances to differ from transaction ledger.
```

---

# 7. High Severity

## Definition

A High defect causes major functional impact but does not necessarily produce immediate catastrophic financial/security corruption.

Examples include:

* Major banking workflow unavailable
* Incorrect financial information shown
* Incorrect but recoverable calculation
* Important state enforcement failure
* Significant security weakness
* Major statement defect
* Major notification defect affecting financial confidence
* Critical action unusable for subset of supported customers/browsers

---

# 8. High Severity Examples

```text
Statement displays incorrect closing balance but core ledger is correct.

Customer cannot make payments although transfers still work.

Frozen card state is displayed incorrectly but backend correctly blocks transactions.

Loan installment schedule differs slightly from authoritative total.

Successful transaction appears as pending for an extended period.

Security notification is not generated after password change.

Mobile users cannot see full transfer confirmation information.
```

---

# 9. Medium Severity

## Definition

A Medium defect affects functionality but:

* Does not compromise core financial integrity.
* Has a practical workaround.
* Affects secondary functionality.
* Has limited operational impact.

---

# 10. Medium Severity Examples

```text
Transaction filter does not combine correctly with date filter.

Pagination shows duplicate row while authoritative transaction data remains correct.

Beneficiary search fails for partial aliases.

Notification unread counter is incorrect.

Admin search sorting is incorrect.

Arabic text overlaps a noncritical field.

Profile validation message is misleading.
```

---

# 11. Low Severity

## Definition

A Low defect has minimal functional impact.

Usually includes:

* Cosmetic defects
* Minor alignment issues
* Wording issues
* Noncritical formatting inconsistencies

---

# 12. Low Severity Examples

```text
Button is misaligned by several pixels.

Minor spelling mistake.

Unexpected whitespace.

Icon inconsistent between screens.

Noncritical text wrapping.

Color differs slightly from design specification.
```

---

# 13. Priority Levels

This project uses:

```text
P0 — Immediate / Release Blocker
P1 — High Priority
P2 — Medium Priority
P3 — Low Priority
```

---

# 14. P0 — Immediate / Release Blocker

## Definition

A P0 defect requires immediate investigation and normally blocks release.

Typical P0 categories:

```text
Money corruption

Unauthorized money movement

Authentication bypass

Privilege escalation

Customer data exposure

Incorrect balance

Duplicate financial transaction

Major outage

Core banking functionality unavailable

Severe irreversible data corruption
```

---

# 15. P0 Examples

```text
Customer is charged twice.

Customer A sees Customer B's statement.

MFA can be bypassed.

Frozen account still transfers money.

Loan disburses twice.

Deposit maturity pays out twice.

Payment provider completes payment but bank system loses transaction state.

Transfer engine unavailable for all customers.
```

---

# 16. P1 — High Priority

## Definition

Important defect that should be fixed quickly and normally before release unless risk is explicitly accepted.

Examples:

```text
Incorrect financial display with correct underlying ledger.

Important workflow fails for subset of users.

Incorrect notification for failed transaction.

Loan calculation has small but material rounding problem.

Important browser-specific functional failure.

Security hardening weakness without demonstrated direct exploitation.
```

---

# 17. P2 — Medium Priority

## Definition

Defect should be fixed but does not normally block release.

Examples:

```text
Search filter issue.

Pagination problem.

Noncritical validation error.

Minor admin workflow inconvenience.

Responsive layout issue with workaround.
```

---

# 18. P3 — Low Priority

## Definition

Low urgency.

Examples:

```text
Cosmetic issue.

Minor text inconsistency.

Spacing defect.

Nonfunctional visual difference.
```

---

# 19. Severity / Priority Matrix

| Severity | Normal Priority | Possible Range |
| -------- | --------------- | -------------- |
| Critical | P0              | P0             |
| High     | P1              | P0–P2          |
| Medium   | P2              | P1–P3          |
| Low      | P3              | P2–P3          |

Critical financial/security defects should normally remain `P0`.

---

# 20. Severity Decision Questions

When assigning severity, ask:

```text
Does money move incorrectly?

Is account balance incorrect?

Can a customer access another customer's data?

Can authentication be bypassed?

Can authorization be bypassed?

Can a financial transaction occur twice?

Can a transaction partially complete?

Is authoritative financial data corrupted?

Does this prevent a core banking function?

Can the defect affect many customers?

Can the impact be reversed safely?

Does a workaround exist?
```

---

# 21. Priority Decision Questions

When assigning priority, ask:

```text
Does this block release?

Is it affecting production-like critical flows?

How many users are affected?

Is there a workaround?

How likely is occurrence?

Is the defect security-sensitive?

Does it affect financial reconciliation?

Is the affected feature required for the current release?

Does fixing it reduce a major business risk?
```

---

# 22. Financial Severity Rules

Any defect affecting actual financial state should be assessed carefully.

Potential Critical cases include:

```text
Wrong debit

Wrong credit

Duplicate debit

Duplicate credit

Incorrect fee

Incorrect interest

Incorrect penalty

Incorrect refund

Incorrect final settlement

Money created

Money lost

Balance inconsistent with transaction ledger
```

Small numeric differences can still be serious.

Example:

```text
0.01 discrepancy × millions of accounts
```

may represent material aggregate impact.

---

# 23. Balance Defect Severity

## Critical

```text
Authoritative account balance is wrong.
```

Examples:

```text
Balance decreases after failed transfer.

Two transactions applied for one request.

Balance differs from ledger.
```

---

## High

```text
Displayed balance is wrong but backend authoritative balance is correct.
```

Still important because customers may make incorrect financial decisions.

---

# 24. Transfer Defect Severity

| Defect                                         | Severity | Priority |
| ---------------------------------------------- | -------- | -------- |
| Duplicate transfer                             | Critical | P0       |
| Unauthorized transfer                          | Critical | P0       |
| Transfer > available balance                   | Critical | P0       |
| Frozen account transfer                        | Critical | P0       |
| Incorrect fee affecting balance                | Critical | P0       |
| Scheduled transfer executes after cancellation | Critical | P0       |
| Transfer history label incorrect               | Medium   | P2       |
| Transfer confirmation UI spacing               | Low      | P3       |

---

# 25. Payment Defect Severity

| Defect                          | Severity | Priority |
| ------------------------------- | -------- | -------- |
| Duplicate payment               | Critical | P0       |
| Failed payment changes balance  | Critical | P0       |
| Wrong bill paid                 | Critical | P0       |
| Provider/local state mismatch   | Critical | P0       |
| Failure shown as success        | High     | P1       |
| Payment search filter incorrect | Medium   | P2       |

---

# 26. Card Defect Severity

| Defect                        | Severity | Priority |
| ----------------------------- | -------- | -------- |
| Blocked card usable           | Critical | P0       |
| Frozen card usable            | Critical | P0       |
| Unauthorized card access      | Critical | P0       |
| Card limit bypass             | Critical | P0       |
| Incorrect card status display | High     | P1       |
| Card alignment issue          | Low      | P3       |

---

# 27. Loan Defect Severity

| Defect                        | Severity      | Priority |
| ----------------------------- | ------------- | -------- |
| Duplicate disbursement        | Critical      | P0       |
| Unauthorized approval         | Critical      | P0       |
| Wrong customer credited       | Critical      | P0       |
| Incorrect outstanding balance | Critical      | P0       |
| Interest calculation mismatch | High/Critical | P0–P1    |
| Loan filter incorrect         | Medium        | P2       |

Impact determines whether a calculation defect is High or Critical.

---

# 28. Deposit Defect Severity

| Defect                             | Severity      | Priority |
| ---------------------------------- | ------------- | -------- |
| Duplicate maturity payout          | Critical      | P0       |
| Principal debited twice            | Critical      | P0       |
| Incorrect maturity amount          | Critical      | P0       |
| Incorrect early-withdrawal penalty | Critical/High | P0–P1    |
| Deposit display label wrong        | Low           | P3       |

---

# 29. Statement Defect Severity

| Defect                                      | Severity      | Priority |
| ------------------------------------------- | ------------- | -------- |
| Another customer can access statement       | Critical      | P0       |
| Closing balance incorrect                   | Critical/High | P0       |
| Transaction missing from official statement | High          | P1       |
| PDF formatting minor issue                  | Low           | P3       |
| Statement filename incorrect                | Low           | P3       |

A statement mismatch becomes Critical if it reflects or causes authoritative financial inconsistency.

---

# 30. Authentication Defect Severity

| Defect                        | Severity      | Priority |
| ----------------------------- | ------------- | -------- |
| Authentication bypass         | Critical      | P0       |
| MFA bypass                    | Critical      | P0       |
| Password reset token reusable | Critical/High | P0       |
| Account lockout bypass        | High/Critical | P0–P1    |
| Incorrect login error message | Medium        | P2       |
| Login UI alignment            | Low           | P3       |

---

# 31. Authorization Defect Severity

Authorization failures involving protected banking resources should normally be treated very seriously.

Examples:

```text
Customer accesses another customer's account:
Critical / P0

Customer sees another customer's notification containing sensitive info:
Critical / P0

Customer-support role sees unnecessary internal field:
High / P1

Read-only administrator can modify account:
Critical / P0
```

---

# 32. API Defect Severity

Do not assign severity based only on HTTP status codes.

Example:

```text
API returns 500 instead of 400
```

could be Medium.

But:

```text
API returns 200 and executes unauthorized transfer
```

is Critical.

Assess:

```text
Business effect
Financial effect
Security effect
Persistent state
```

---

# 33. Database Defect Severity

Potential Critical database defects include:

```text
Duplicate financial records

Orphan transaction records

Incorrect account ownership

Broken foreign-key relationship affecting security

Balance/ledger mismatch

Duplicate transaction reference causing reconciliation failure

Transaction state differs from financial state
```

---

# 34. Notification Defect Severity

Notifications should be evaluated according to underlying event.

Example:

```text
Payment failed but customer receives success notification
```

Severity:

```text
High
```

If notification itself triggers another financial action or exposes another customer's sensitive information, severity may become Critical.

---

# 35. Audit Defect Severity

## Critical

Examples:

```text
Audit logs expose access tokens.

Administrator can delete financial audit trail.

Critical actions cannot be traced to actor.

Audit data can be manipulated to hide unauthorized transaction.
```

---

## High

Examples:

```text
Admin reason missing.

Incorrect previous-state value.

Important security event not recorded.
```

---

# 36. Cross-Browser Severity

Severity depends on functionality, not browser alone.

Example:

```text
Transfer cannot be submitted in Firefox
```

High or Critical depending on supported-browser requirements and affected population.

Example:

```text
Firefox uses slightly different border radius
```

Low.

Example:

```text
WebKit submits wrong amount due to decimal parsing
```

Critical.

---

# 37. Responsive Defect Severity

Examples:

```text
Fee hidden but transfer still confirmable:
High

Confirm button inaccessible:
High

Financial amount clipped so user cannot determine transfer amount:
High

Small spacing issue:
Low
```

A responsive defect may become Critical if it causes incorrect financial submission.

---

# 38. Accessibility Severity

Assess based on:

* Critical functionality blocked
* Number of affected users
* Availability of alternative method
* Regulatory/accessibility requirements
* Banking risk

Example:

```text
Keyboard-only user cannot confirm transfer
```

may be High.

---

# 39. Concurrency Defect Severity

Concurrency defects affecting financial integrity should generally be Critical.

Examples:

```text
Concurrent transfers cause negative balance.

Two admins disburse same loan.

Two maturity workers pay same deposit.

Two reversals process same transaction.
```

Severity:

```text
Critical
```

Priority:

```text
P0
```

---

# 40. Idempotency Defect Severity

For financial operations:

```text
Duplicate execution caused by retry
```

should usually be:

```text
Critical / P0
```

Applicable to:

* Transfers
* Payments
* Loan disbursement
* Deposit creation
* Deposit payout
* Refunds
* Reversals

---

# 41. Availability Severity

## Critical

```text
Entire Banking System unavailable.

All customers cannot login.

All transfers fail.

Database unavailable causing banking services to fail.
```

---

## High

```text
Loan module unavailable.

Statements unavailable.

Card-management module unavailable.
```

Exact severity depends on business criticality and duration.

---

# 42. Data-Loss Severity

## Critical

Examples:

```text
Transactions permanently deleted.

Customer balances cannot be reconstructed.

Audit trail destroyed.

Financial records corrupted.
```

---

## High

Examples:

```text
Nonfinancial profile field lost.

Notification history missing.
```

---

# 43. Workaround Impact

A workaround may influence priority but should not automatically reduce severity.

Example:

```text
Customer gets charged twice but support can refund manually.
```

Severity remains:

```text
Critical
```

A workaround exists, but financial corruption still occurred.

---

# 44. Frequency vs Severity

Frequency does not define severity.

Example:

```text
Duplicate loan disbursement occurs only 1 in 10,000 attempts.
```

Still:

```text
Critical
```

because the potential impact is severe.

Frequency instead influences:

* Risk
* Priority discussions
* Investigation strategy
* Release decision

---

# 45. Severity vs Probability

Risk can be represented as:

```text
Risk = Probability × Impact
```

Severity primarily reflects:

```text
Impact
```

Therefore:

```text
Low probability + catastrophic impact
```

can still be:

```text
Critical Severity
```

---

# 46. Release Blocking Rules

Release should normally be blocked for unresolved defects involving:

```text
Incorrect account balance

Unauthorized financial transaction

Duplicate debit/credit

Authentication bypass

MFA bypass

Customer-to-customer data access

Privilege escalation

Core transaction engine failure

Loan double-disbursement

Deposit duplicate payout

Irrecoverable financial data loss

Major reconciliation corruption

Critical audit/security-secret exposure
```

---

# 47. Conditional Release Rules

Conditional release may be considered when:

* No P0 defects remain.
* Remaining defects have understood impact.
* Workarounds exist.
* No security/privacy issue remains.
* No financial-integrity issue remains.
* Product/business accepts residual risk.

Example:

```text
Transaction history filter incorrect for one optional filter.
```

Could be conditionally released.

---

# 48. Non-Blocking Examples

Possible nonblocking defects:

```text
Minor alignment

Noncritical sorting issue

Cosmetic responsive defect

Minor wording

Optional filter bug
```

assuming no hidden financial/security impact.

---

# 49. Severity Escalation Examples

A defect initially appears Medium:

```text
Transaction row displays wrong status.
```

Investigation finds:

```text
Database also stores incorrect final state.
```

Severity may increase to:

```text
High or Critical
```

depending on financial consequences.

---

# 50. Severity Reduction Example

Initial report:

```text
Balance is incorrect.
```

Investigation finds:

```text
Only stale UI cache is wrong.

API/database/ledger values are correct.

Refresh resolves issue.
```

Severity may be:

```text
High
```

rather than Critical.

Still serious because users may make financial decisions using incorrect information.

---

# 51. Priority Escalation Examples

A Medium defect may receive P1 because:

* Major customer demo is imminent.
* Important customer segment affected.
* Regulatory requirement involved.
* Defect blocks UAT.
* Defect causes significant operational burden.

---

# 52. Priority Reduction Examples

A High-severity defect could potentially be deferred only in exceptional circumstances if:

* Feature is disabled.
* Affected functionality will not be released.
* Exposure is impossible in production configuration.
* Formal risk acceptance exists.

Critical financial/security defects should not casually be downgraded.

---

# 53. Example Severity Assessment — Duplicate Transfer

Defect:

```text
Double-click causes two transfers.
```

Questions:

```text
Does money change incorrectly? YES

Can customer lose money? YES

Can this happen during normal interaction? YES

Is authoritative financial state affected? YES
```

Severity:

```text
Critical
```

Priority:

```text
P0
```

Release impact:

```text
Block
```

---

# 54. Example Severity Assessment — False Success Notification

Defect:

```text
Payment fails but notification says success.
```

Questions:

```text
Actual financial state corrupted? NO

Customer receives false financial information? YES

Could customer make poor decisions? YES
```

Severity:

```text
High
```

Priority:

```text
P1
```

Could become P0 if business/release risk warrants.

---

# 55. Example Severity Assessment — Mobile Fee Hidden

Defect:

```text
Fee is hidden on confirmation screen.
```

Financial backend:

```text
Correct
```

User impact:

```text
Customer confirms transaction without seeing full cost.
```

Severity:

```text
High
```

Priority:

```text
P1
```

---

# 56. Example Severity Assessment — Search Filter

Defect:

```text
Transaction status + date filter does not combine correctly.
```

Financial data:

```text
Correct
```

Workaround:

```text
Use one filter at a time.
```

Severity:

```text
Medium
```

Priority:

```text
P2
```

---

# 57. Example Severity Assessment — Typo

Defect:

```text
"Beneficary" instead of "Beneficiary".
```

Severity:

```text
Low
```

Priority:

```text
P3
```

---

# 58. Severity Decision Tree

Use this simplified decision path:

```text
Does it corrupt or incorrectly move money?
    YES → CRITICAL

Does it expose another customer's protected data?
    YES → CRITICAL

Does it bypass authentication/authorization?
    YES → CRITICAL

Does it cause major core banking workflow failure?
    YES → HIGH or CRITICAL

Does it significantly mislead customer about financial state?
    YES → HIGH

Does it affect secondary functionality with workaround?
    YES → MEDIUM

Is impact mostly cosmetic?
    YES → LOW
```

---

# 59. Priority Decision Tree

```text
Does it block release or create unacceptable financial/security risk?
    YES → P0

Must it be fixed before release under normal policy?
    YES → P1

Can it reasonably be scheduled for later iteration?
    YES → P2

Is it minor/cosmetic backlog work?
    YES → P3
```

---

# 60. Severity and Priority Review

Severity and priority may be reviewed during triage by:

* QA
* Development
* Product
* Engineering lead
* Security
* Operations
* Business stakeholders

QA should provide evidence supporting the proposed classification.

---

# 61. Defect Triage Questions

During triage ask:

```text
What is the customer impact?

Can money be affected?

Can data leak?

Can authorization be bypassed?

How many users are affected?

How often can it happen?

Is there a workaround?

Does it block another test?

Could it cause downstream reconciliation issues?

Does it affect production release readiness?
```

---

# 62. Defect Triage Example Table

| Defect                     | Severity | Priority | Release Impact      |
| -------------------------- | -------- | -------- | ------------------- |
| Duplicate Transfer         | Critical | P0       | Block               |
| MFA Bypass                 | Critical | P0       | Block               |
| Statement IDOR             | Critical | P0       | Block               |
| Incorrect Loan Rounding    | High     | P1       | Review/Fix          |
| False Success Notification | High     | P1       | Fix                 |
| Search Filter Failure      | Medium   | P2       | Usually Nonblocking |
| Minor Alignment            | Low      | P3       | Nonblocking         |

---

# 63. Module-Specific Severity Quick Reference

## Authentication

```text
Bypass → Critical

Lockout weakness → High/Critical

Incorrect error message → Medium
```

## Accounts

```text
Wrong balance → Critical

State enforcement failure → Critical

Display formatting → Medium/Low
```

## Transfers

```text
Duplicate/wrong transfer → Critical

Incorrect fee → Critical/High

History label → Medium
```

## Payments

```text
Wrong payment/duplicate → Critical

False status → High/Critical
```

## Cards

```text
Blocked card usable → Critical

Incorrect status display → High
```

## Loans

```text
Duplicate disbursement → Critical

Calculation mismatch → High/Critical
```

## Deposits

```text
Duplicate payout → Critical

Incorrect interest → High/Critical
```

## Statements

```text
Unauthorized access → Critical

Incorrect financial totals → High/Critical
```

---

# 64. Security Quick Reference

```text
Authentication bypass → Critical / P0

Authorization bypass → Critical / P0

IDOR → Critical / P0

Privilege escalation → Critical / P0

Token/password exposure → Critical / P0

Missing secondary header → Medium / P2

Verbose low-risk error → Medium / P2
```

Actual severity depends on exploitability and impact.

---

# 65. Financial Quick Reference

```text
Wrong money movement → Critical

Duplicate money movement → Critical

Missing money movement → Critical

Wrong balance → Critical

Wrong settlement → Critical

Wrong interest/fee applied → High/Critical

Incorrect financial display only → High
```

---

# 66. Severity Documentation

The defect report should explain why severity was chosen.

Example:

```text
Severity: Critical

Justification:
The issue creates two completed transfers from one customer action,
resulting in duplicate debit and incorrect authoritative balance.
```

This is stronger than simply selecting `Critical`.

---

# 67. Priority Documentation

For P0 issues, include release impact.

Example:

```text
Priority: P0

Release Impact:
Release blocker because customer funds can be duplicated.
```

---

# 68. Disagreement Handling

If QA and development disagree about severity:

1. Review reproducible evidence.
2. Separate technical impact from fix urgency.
3. Review business/risk impact.
4. Use project definitions.
5. Escalate to product/engineering/security where needed.

Avoid changing severity simply to improve metrics.

---

# 69. Defect Metrics Warning

Severity and priority should never be manipulated to make dashboards look healthier.

Bad practice:

```text
Downgrade Critical → Medium because release date is near.
```

Instead:

```text
Keep Critical.

Document explicit risk acceptance if release proceeds.
```

---

# 70. Release Readiness Use

Severity/priority contributes to:

```text
Smoke decision

Regression decision

UAT decision

Release-readiness report

Residual risk

Release recommendation
```

These classifications therefore need to remain consistent across the project.

---

# 71. Mapping to Project Risks

Examples:

| Risk                              | Typical Defect Severity |
| --------------------------------- | ----------------------- |
| RISK-001 Incorrect Balance        | Critical                |
| RISK-002 Unauthorized Data Access | Critical                |
| RISK-003 Duplicate Transaction    | Critical                |
| RISK-005 Authentication Bypass    | Critical                |
| RISK-008 Limit Bypass             | Critical                |
| RISK-010 Fee Error                | High/Critical           |
| RISK-011 Loan Calculation         | High/Critical           |
| RISK-012 Deposit Calculation      | High/Critical           |
| RISK-013 Concurrency Corruption   | Critical                |
| RISK-020 Statement Incorrect      | High/Critical           |
| RISK-021 Sensitive Exposure       | High/Critical           |
| RISK-023 Unauthorized Admin       | Critical                |
| RISK-034 False Notification       | High                    |
| RISK-035 Browser Failure          | Medium/High             |
| RISK-036 Responsive Failure       | Medium/High             |
| RISK-047 IDOR                     | Critical                |
| RISK-048 UI/Backend Mismatch      | High/Critical           |

---

# 72. Severity/Priority Checklist

Before assigning classification, verify:

```text
Financial impact assessed?

Security impact assessed?

Authorization impact assessed?

Data-integrity impact assessed?

Affected users known?

Frequency considered?

Workaround identified?

Authoritative backend state checked?

Database impact checked?

Downstream effect considered?

Release impact considered?
```

---

# 73. Final Severity Principle

Severity should represent:

```text
The worst realistic impact of the demonstrated defect.
```

Not:

```text
How difficult the defect is to fix.
```

Not:

```text
How angry someone is about the defect.
```

Not:

```text
How frequently QA happens to reproduce it.
```

---

# 74. Final Priority Principle

Priority should represent:

```text
How urgently the defect should be addressed
given severity, probability, business importance,
release timing, and available mitigations.
```

---

# 75. Banking Quality Rule

For this project:

```text
Any defect that threatens customer money,
customer privacy, authentication,
authorization, financial integrity,
or irreversible banking data
must receive immediate risk evaluation.
```

The core rule is:

```text
Severity measures impact.

Priority measures urgency.

Neither should be assigned without understanding
the authoritative financial, security, and data state.
```
