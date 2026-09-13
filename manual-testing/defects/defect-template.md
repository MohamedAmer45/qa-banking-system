# Banking System — Defect Report Template

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Document | Defect Report Template         |
| Version  | 1.0                            |
| Status   | Active                         |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines the standard format for reporting defects discovered during Banking System testing.

The objective is to create defect reports that are:

* Reproducible
* Clear
* Evidence-based
* Technically useful
* Risk-aware
* Traceable to requirements and tests
* Appropriate for financial and security defects

A strong defect report should allow a developer or other tester to understand:

```text
What is wrong?

Where did it happen?

How can it be reproduced?

What should have happened?

What actually happened?

What data was affected?

How serious is the impact?

What evidence proves the issue?
```

---

# 3. Defect Naming Convention

Defects use:

```text
BUG-XXX
```

Examples:

```text
BUG-001
BUG-002
BUG-003
```

Recommended title format:

```text
[Module] Short description of actual incorrect behavior
```

Examples:

```text
[Transfers] Double-clicking confirmation creates duplicate transfer

[Statements] Payment fee excluded from closing balance calculation

[Security] Customer can access another customer's statement

[Cards] Frozen card remains usable through direct API

[Loans] Concurrent disbursement requests credit loan twice
```

Avoid vague titles such as:

```text
Transfer issue

Login broken

Statement problem

Something wrong with card
```

---

# 4. Defect Statuses

Recommended defect lifecycle:

```text
NEW
OPEN
IN_PROGRESS
FIXED
READY_FOR_RETEST
REOPENED
DEFERRED
REJECTED
DUPLICATE
CANNOT_REPRODUCE
CLOSED
```

Possible flow:

```text
NEW
↓
OPEN
↓
IN_PROGRESS
↓
FIXED
↓
READY_FOR_RETEST
↓
CLOSED
```

If retest fails:

```text
READY_FOR_RETEST
↓
REOPENED
↓
IN_PROGRESS
```

---

# 5. Severity Levels

Severity describes the technical/business impact of the defect.

## Critical

Examples:

* Money lost
* Money duplicated
* Unauthorized money movement
* Incorrect account balance
* Authentication bypass
* Customer A accesses Customer B data
* Privilege escalation
* Duplicate loan disbursement
* Duplicate deposit payout
* Transaction corruption
* Severe financial reconciliation failure

---

## High

Examples:

* Major banking workflow unavailable
* Frozen card can still transact
* Incorrect statement
* Major calculation error
* Failed transaction displayed as successful
* Important state-transition failure
* Major security control weakened

---

## Medium

Examples:

* Search/filter failure
* Notification issue
* Noncritical validation problem
* Incorrect nonfinancial display
* Workflow has reasonable workaround

---

## Low

Examples:

* Cosmetic issue
* Alignment
* Minor wording issue
* Small visual inconsistency

---

# 6. Priority Levels

Priority describes how urgently the defect should be fixed.

```text
P0 = Immediate / Release blocker
P1 = High
P2 = Medium
P3 = Low
```

Severity and priority are related but not identical.

Example:

```text
High Severity + P0
```

may apply if an issue blocks release.

A cosmetic issue could theoretically have higher priority if required for an immediate regulatory or contractual reason.

---

# 7. Core Defect Report Template

```text
# Defect Report

Defect ID:
Title:

Module:
Feature:
Environment:
Build / Version:

Reported By:
Reported Date:
Assigned To:

Severity:
Priority:
Status:

Related Requirement:
Related Business Rule:
Related Test Scenario:
Related Test Case:
Related Risk:
Related Exploratory Session:

## Summary

Provide a concise explanation of the defect.

## Preconditions

Describe the required system state before reproducing the issue.

## Test Data

Customer:
Account:
Beneficiary:
Card:
Loan:
Deposit:
Admin Role:
Transaction Reference:
Other:

## Steps to Reproduce

1.
2.
3.
4.
5.

## Expected Result

Describe what the system should do.

## Actual Result

Describe exactly what the system does instead.

## Reproducibility

Always / Intermittent / Once

Reproduction Rate:

## Financial Impact

Opening Balance:
Transaction Amount:
Fee:
Expected Closing Balance:
Actual Closing Balance:

Financial Reconciliation:
PASS / FAIL / N/A

## Security Impact

Authentication Impact:
Authorization Impact:
Customer Data Exposure:
Privilege Escalation:
Sensitive Data Exposure:

Security Impact:
YES / NO

## API Evidence

Endpoint:
Method:
Request ID:
Request:
Response Status:
Response:

## Database Evidence

Relevant Tables:
Expected State:
Actual State:
Query / Observation:

## Logs / Technical Evidence

Correlation ID:
Application Logs:
Browser Console:
Network Logs:
Server Logs:

## Attachments

Screenshots:
Videos:
HAR File:
Logs:
Other:

## Workaround

Describe available workaround, if any.

## Business Impact

Describe how the defect affects customers, money, operations, compliance, support, or release risk.

## Notes

Any additional information.

## Retest Result

Build:
Date:
Tester:
Result:
PASS / FAIL

## Regression Result

Related Regression Executed:
YES / NO

Regression Result:
PASS / FAIL / NOT_RUN

## Final Resolution

Resolution:
Closed Date:
```

---

# 8. Required Fields

Every defect should include at minimum:

```text
Defect ID
Title
Module
Environment
Build
Severity
Priority
Status
Preconditions
Steps to Reproduce
Expected Result
Actual Result
Evidence
```

For banking defects, also include relevant:

```text
Account/customer test data
Transaction reference
Financial values
Security context
API evidence
Database evidence
```

---

# 9. Writing a Strong Defect Title

A defect title should include:

```text
Location/Feature
+
Trigger
+
Failure
```

Example:

```text
[Transfers] Retrying after response timeout creates a second completed transfer
```

This is stronger than:

```text
Duplicate transfer bug
```

Another example:

```text
[Cards] Purchase remains authorized after card status changes from ACTIVE to FROZEN
```

---

# 10. Summary Guidance

The summary should explain the problem in 1–3 sentences.

Example:

```text
A customer can submit the same transfer twice by rapidly clicking
the confirmation button. Both requests are processed successfully,
causing the source account to be debited twice.
```

Do not fill the summary with unrelated implementation speculation.

---

# 11. Preconditions Guidance

Preconditions should describe the state needed before execution.

Example:

```text
Customer CUST-001 is ACTIVE.

Source account ACC-001 is ACTIVE.

Available balance = 10,000.00.

Beneficiary BEN-001 is ACTIVE.

Customer is authenticated with completed MFA.
```

---

# 12. Test Data Guidance

Use synthetic identifiers.

Example:

```text
Customer: CUST-001
Source Account: ACC-001
Destination Account: ACC-002
Beneficiary: BEN-001
Amount: 1,000.00
Fee: 10.00
```

Do not include:

```text
Real customer names

Real account numbers

Real passwords

Real OTP values

Real access tokens

Production financial information
```

---

# 13. Steps to Reproduce Guidance

Steps should be:

* Numbered
* Precise
* Minimal
* Reproducible

Good example:

```text
1. Login as CUST-001.
2. Open Transfers.
3. Select ACC-001 as source account.
4. Select BEN-001.
5. Enter 1,000.00.
6. Continue to confirmation.
7. Rapidly double-click Confirm Transfer.
8. Open Transaction History.
```

Poor example:

```text
Login and do a transfer twice.
```

---

# 14. Expected Result Guidance

Expected result should describe the required behavior.

Example:

```text
Only one transfer should be created.

ACC-001 should be debited once.

One transfer reference should exist.

Final balance should be 8,990.00.
```

---

# 15. Actual Result Guidance

Actual result should state observable facts.

Example:

```text
Two COMPLETED transfer records are created.

ACC-001 is debited twice.

Two unique references are generated.

Final balance becomes 7,980.00.
```

Avoid:

```text
The system is broken.
```

---

# 16. Reproducibility

Recommended values:

```text
Always
Frequently
Intermittent
Rare
Once
```

Also record approximate rate:

```text
5/5
4/5
2/10
```

Example:

```text
Reproducibility: Always
Reproduction Rate: 5/5
```

---

# 17. Environment Information

Include:

```text
Environment:
QA / Staging / Local

Application URL:

Frontend Build:

Backend Build:

Database Version / Migration:

Browser:

Browser Version:

Operating System:

Viewport:

Feature Flags:
```

Environment information is especially important for intermittent and browser-specific issues.

---

# 18. Financial Defect Template

For defects affecting money, add:

```text
## Financial Evidence

Account:
Currency:

Opening Current Balance:
Opening Available Balance:

Transaction Amount:
Fee:
Interest:
Penalty:
Refund:

Expected Debit:
Actual Debit:

Expected Credit:
Actual Credit:

Expected Closing Balance:
Actual Closing Balance:

Difference:

Transaction Reference:
Related Reference:

History Correct:
YES / NO

Statement Correct:
YES / NO

API Correct:
YES / NO

Database Correct:
YES / NO
```

---

# 19. Financial Reconciliation Example

Example defect:

```text
Opening Balance = 10,000.00

Transfer = 1,000.00

Fee = 10.00

Expected:
10,000 - 1,000 - 10 = 8,990.00

Actual:
7,980.00
```

Difference:

```text
1,010.00
```

This immediately demonstrates duplicate financial impact.

---

# 20. Financial Severity Rule

Any defect that may cause:

```text
Incorrect balance

Duplicate debit

Duplicate credit

Money creation

Money loss

Incorrect interest

Incorrect fee

Unauthorized transaction

Incorrect final settlement
```

should be treated as potentially P0/Critical until impact is assessed.

---

# 21. Security Defect Template

For security-related defects, include:

```text
## Security Context

Authenticated User:
User Role:

Target Resource:
Target Resource Owner:

Authentication State:
Session State:

Expected Permission:
Actual Permission:

Cross-Customer Access:
YES / NO

Privilege Escalation:
YES / NO

Financial Security Impact:
YES / NO

Sensitive Data Exposed:
YES / NO
```

---

# 22. IDOR Defect Example Structure

Example:

```text
Title:
[Statements] Customer can download another customer's statement by modifying statement ID

Authenticated User:
CUST-001

Resource Owner:
CUST-002

Resource:
Statement STAT-002

Expected:
403/authorization denial with no statement data.

Actual:
Statement PDF for CUST-002 is returned.
```

Additional evidence:

* Endpoint
* Request
* Response
* Safe synthetic IDs
* Screenshot

---

# 23. Authentication Defect Template

Include:

```text
Credential State:
VALID / INVALID

MFA State:
NOT_REQUIRED / PENDING / COMPLETED

Session State:
NONE / ACTIVE / EXPIRED / REVOKED

Target Protected Resource:

Expected Access:
DENIED / ALLOWED

Actual Access:
DENIED / ALLOWED
```

---

# 24. API Defect Template

Add:

```text
## API Details

Endpoint:
Method:

Headers:
Use safe/redacted values only.

Query Parameters:

Path Parameters:

Request Body:

Expected Status:

Actual Status:

Expected Response:

Actual Response:

Correlation ID:

Response Time:
```

Redact:

```text
Authorization tokens
Cookies
Passwords
OTP values
Secrets
```

---

# 25. Example API Evidence

```text
Endpoint:
POST /api/transfers

Expected:
Request rejected because source account is frozen.

Actual:
201/200 success and transfer reaches COMPLETED.
```

Then validate whether financial state changed.

---

# 26. Database Defect Template

Include:

```text
## Database Validation

Database:
Schema:

Tables Involved:

Relevant Record IDs:

Expected Data:

Actual Data:

Relationship Validation:

Duplicate Record Check:

Balance Validation:

Transaction Status Validation:
```

Do not paste production credentials.

---

# 27. Database Evidence Example

For duplicate loan disbursement:

```text
Expected:
1 disbursement transaction linked to LOAN-001.

Actual:
2 completed disbursement transactions exist.

Expected account credit:
100,000.00

Actual account credit:
200,000.00
```

---

# 28. UI Defect Template

For UI-specific issues include:

```text
Page:
Component:

Browser:
Viewport:

Expected Layout:

Actual Layout:

Functional Impact:

Responsive Impact:

Accessibility Impact:
```

Example:

```text
Transfer confirmation button is outside the viewport at 360×800,
preventing mobile customers from submitting the transfer.
```

---

# 29. Cross-Browser Defect Template

Add:

```text
Affected Browsers:

Chrome:
PASS / FAIL

Edge:
PASS / FAIL

Firefox:
PASS / FAIL

WebKit:
PASS / FAIL

Affected Viewports:

Primary Browser Comparison:
```

Example:

```text
Firefox 153:
FAIL

Chrome 153:
PASS

Edge 153:
PASS
```

---

# 30. Concurrency Defect Template

Concurrency defects require stronger evidence.

Include:

```text
## Concurrency Evidence

Initial State:

Initial Balance:

Request A:
Timestamp:
Result:

Request B:
Timestamp:
Result:

Expected Final State:

Actual Final State:

Expected Balance:

Actual Balance:

Created Transaction IDs:

Database State:

Reproduction Method:
```

---

# 31. Concurrency Example

```text
Opening Balance = 1,000.00

Request A = 800.00
Request B = 500.00
```

Expected:

```text
At most one request succeeds if both cannot be funded.
```

Actual:

```text
Both requests succeed.

Final balance = -300.00
```

Severity:

```text
Critical
P0
```

---

# 32. State Transition Defect Template

Include:

```text
Entity:

Previous State:

Event:

Expected New State:

Actual New State:

Actor:

Authorized:
YES / NO

Side Effects:

Audit Record:
YES / NO
```

Example:

```text
Entity: Card CARD-001

Previous State:
FROZEN

Event:
Activate

Expected:
Reject invalid transition

Actual:
State changes to ACTIVE
```

---

# 33. Notification Defect Template

Include:

```text
Underlying Event:

Authoritative Event Status:

Notification Type:

Channel:

Expected Message:

Actual Message:

Recipient:

Duplicate:
YES / NO

Timing:
```

Example:

```text
Payment status:
FAILED

Actual notification:
"Payment completed successfully."
```

---

# 34. Statement Defect Template

Include:

```text
Account:

Statement Period:

Opening Balance:

Credits:

Debits:

Fees:

Expected Closing Balance:

Actual Closing Balance:

Missing Transactions:

Duplicate Transactions:

Incorrect Transactions:
```

---

# 35. Calculation Defect Template

For:

* Fees
* Loan interest
* Deposit interest
* Penalties
* Installments
* Refund calculations

Include:

```text
Input Values:

Formula / Expected Rule:

Expected Result:

Actual Result:

Rounding Rule:

Difference:
```

---

# 36. Calculation Example

```text
Principal:
100,000.00

Expected Interest:
5,000.00

Expected Total:
105,000.00

Actual Total:
105,000.03

Difference:
0.03
```

Even small discrepancies may be material when repeated across many accounts.

---

# 37. Error-Handling Defect Template

Include:

```text
Failure Trigger:

Expected User Message:

Actual User Message:

Backend Result:

Financial Result:

Retry Behavior:

Recovery Behavior:
```

Example:

```text
Backend transfer succeeds.

Client connection times out.

UI shows generic failure.

Customer retries.

Second transfer succeeds.
```

This is not merely a UI-message defect because it can create duplicate financial impact.

---

# 38. Performance-Related Functional Defect Template

For slow behavior causing functional issues include:

```text
Operation:

Expected Response Time:

Observed Response Time:

Concurrency:

Data Volume:

Functional Side Effect:

Timeout:

Retry Behavior:
```

Example:

```text
Transfer takes 25 seconds.

UI times out at 10 seconds.

Customer retries.

Both transfers eventually complete.
```

---

# 39. Accessibility Defect Template

Include:

```text
Page:

Component:

Input Method:
Keyboard / Screen Reader / Other

Expected:

Actual:

WCAG-related observation:

Functional Impact:
```

Avoid claiming a specific WCAG violation unless verified.

---

# 40. Defect Traceability

Defects should link back to existing QA artifacts.

Example:

```text
Defect:
BUG-001

Requirement:
Transfer idempotency

Scenario:
TS-TRF duplicate submission

Test Case:
TC-TRF-XXX

Risk:
RISK-003

Exploratory Session:
ES-001

Regression:
REG-021

Future Automation:
REST Assured / Playwright
```

---

# 41. Defect-to-Risk Mapping

Examples:

| Defect                         | Risk                |
| ------------------------------ | ------------------- |
| Duplicate transfer             | RISK-003            |
| Incorrect balance              | RISK-001            |
| Customer data exposure         | RISK-002 / RISK-047 |
| Authentication bypass          | RISK-005            |
| Limit bypass                   | RISK-008            |
| Frozen card usable             | RISK-024            |
| Failed payment changes balance | RISK-026            |
| Statement mismatch             | RISK-020            |
| Unauthorized admin action      | RISK-023            |
| False success notification     | RISK-034            |

---

# 42. Defect Labels / Categories

Recommended categories:

```text
FUNCTIONAL
FINANCIAL
SECURITY
AUTHENTICATION
AUTHORIZATION
API
DATABASE
UI
CROSS_BROWSER
RESPONSIVE
ACCESSIBILITY
PERFORMANCE
CONCURRENCY
CALCULATION
DATA_INTEGRITY
STATE_TRANSITION
NOTIFICATION
AUDIT
```

A defect may have multiple labels.

Example:

```text
BUG-001

FINANCIAL
API
CONCURRENCY
DATA_INTEGRITY
```

---

# 43. Root Cause Field

QA may add suspected root cause, but should distinguish:

```text
Observation
```

from:

```text
Hypothesis
```

Good:

```text
Suspected cause:
The confirmation endpoint may lack idempotency protection.
Developer confirmation required.
```

Avoid:

```text
Root cause is definitely missing database lock.
```

unless confirmed.

---

# 44. Workaround Field

Possible values:

```text
No workaround

Temporary workaround available

Operational workaround available
```

Example:

```text
Workaround:
Customer must not retry transfer after timeout until transaction history is checked.
```

A workaround does not automatically reduce severity.

---

# 45. Business Impact Guidance

Explain what the defect means outside technical implementation.

Example:

```text
Customers may be charged twice for one intended transfer.
This can cause incorrect balances, support incidents,
financial reconciliation failures, and loss of customer trust.
```

---

# 46. Release Impact

Recommended field:

```text
Release Impact:

BLOCKER
HIGH RISK
ACCEPTABLE WITH APPROVAL
LOW RISK
```

Examples of release blockers:

```text
Authentication bypass

Incorrect account balance

Duplicate financial transaction

Cross-customer data exposure

Privilege escalation

Core transfer unavailable
```

---

# 47. Defect Reproduction Quality

A defect should ideally be reproducible by another tester using only:

```text
Environment

Test data

Preconditions

Steps

Evidence
```

If reproduction requires verbal explanation, the report likely needs improvement.

---

# 48. Evidence Priorities

## Financial Defects

Capture:

1. Balance before
2. Request/action
3. Transaction reference
4. Balance after
5. History
6. API
7. Database where possible

---

## Security Defects

Capture:

1. User identity/role
2. Resource owner
3. Request
4. Response
5. Sensitive result
6. Authorization expectation

---

## UI Defects

Capture:

1. Screenshot/video
2. Browser
3. Viewport
4. Steps
5. Functional impact

---

## Concurrency Defects

Capture:

1. Requests
2. timestamps
3. responses
4. final state
5. database records

---

# 49. Screenshot Guidance

Screenshots should highlight:

* Relevant error
* Incorrect amount
* Incorrect state
* Missing control
* Browser/layout issue

Avoid screenshots containing:

* Passwords
* OTPs
* Authentication tokens
* Real customer data

---

# 50. Video Guidance

Video is useful for:

* Race conditions
* Flickering state
* Browser navigation issues
* Double submission
* Intermittent UI problems
* Responsive behavior

Still provide written steps.

A video should support the defect report, not replace it.

---

# 51. Logs Guidance

Useful identifiers:

```text
Correlation ID

Request ID

Transaction Reference

Job ID

Timestamp
```

Do not attach huge unrelated logs when a smaller relevant extract is available.

---

# 52. Duplicate Defects

Before filing a new defect:

```text
Search existing open defects.

Compare trigger.

Compare root symptom.

Compare affected module.
```

If duplicate:

```text
Status = DUPLICATE
```

and link to original defect.

A different trigger causing the same visible symptom may still deserve separate investigation if root causes differ.

---

# 53. Rejected Defects

A defect may be rejected when:

* Behavior matches requirement.
* Environment misconfiguration caused issue.
* Test data invalid.
* Duplicate defect.
* Requirement changed.

QA should record the reason.

Do not delete the historical record.

---

# 54. Cannot Reproduce

If developer cannot reproduce:

Provide:

```text
Environment

Build

Exact test data

Video

Network request

Logs

Timestamp

Reproduction frequency
```

Retry using the original environment where possible.

---

# 55. Retest Template

```text
## Retest

Defect ID:

Original Build:

Fixed Build:

Retested By:

Date:

Original Reproduction Steps Re-executed:
YES / NO

Expected Result:

Actual Result:

Result:
PASS / FAIL

Regression Required:
YES / NO

Regression Tests:

Notes:
```

---

# 56. Reopened Defect Criteria

Reopen when:

* Original failure still occurs.
* Fix only partially resolves issue.
* Equivalent path still fails and is part of same defect.
* Financial inconsistency remains.

Create a new defect if a completely different issue is discovered during retest.

---

# 57. Regression After Fix

Examples:

## Transfer Bug

Retest:

```text
Exact bug
```

Then regression:

```text
Valid transfer
Invalid transfer
Balance
Fee
History
Statement
Notification
API
Database
Duplicate/retry behavior
```

---

## Authorization Bug

Retest:

```text
Exact unauthorized resource
```

Then regression:

```text
Other endpoints
Other resource types
Authorized path
Admin roles
Downloads/deep links
```

---

# 58. Defect Metrics

Useful metrics:

```text
Total defects

Open defects

Closed defects

Critical defects

High defects

Defects by module

Defects by root cause

Reopened defects

Escaped defects

Defect aging
```

Metrics should support quality decisions rather than individual performance scoring.

---

# 59. Example Complete Defect Report

## Defect ID

```text
BUG-001
```

## Title

```text
[Transfers] Rapid double-click on confirmation creates two completed transfers
```

## Module

```text
Transfers
```

## Environment

```text
QA
```

## Build

```text
1.0.0-rc1
```

## Severity

```text
Critical
```

## Priority

```text
P0
```

## Status

```text
OPEN
```

## Related Risk

```text
RISK-001
RISK-003
```

## Preconditions

```text
Customer CUST-001 is ACTIVE.

ACC-001 is ACTIVE.

ACC-001 available balance = 10,000.00.

BEN-001 is ACTIVE.

Customer is authenticated.
```

## Test Data

```text
Transfer Amount = 1,000.00
Fee = 10.00
```

## Steps to Reproduce

```text
1. Login as CUST-001.
2. Open Transfers.
3. Select ACC-001.
4. Select BEN-001.
5. Enter 1,000.00.
6. Continue to confirmation.
7. Rapidly double-click Confirm.
8. Open transaction history.
```

## Expected Result

```text
One transfer should be completed.

One debit of 1,010.00 should occur.

Expected final balance = 8,990.00.
```

## Actual Result

```text
Two completed transfers are created.

Two debits of 1,010.00 occur.

Actual final balance = 7,980.00.
```

## Reproducibility

```text
5/5
```

## Financial Impact

```text
Opening Balance:
10,000.00

Expected Closing:
8,990.00

Actual Closing:
7,980.00

Unexpected Difference:
1,010.00
```

## Business Impact

```text
A customer may be charged twice for one intended transfer.
This creates incorrect financial balances and requires financial correction.
```

## Evidence

```text
Screenshot:
transfer-history-duplicate.png

References:
TRF-10001
TRF-10002

Timestamp:
2026-09-13 00:00:00 test environment time
```

## Suggested Follow-Up

```text
Validate server-side idempotency.

Validate database uniqueness/transaction handling.

Add REST Assured replay test.

Add Playwright double-submit regression.
```

---

# 60. Example Security Defect

## Defect ID

```text
BUG-002
```

## Title

```text
[Statements] Customer can download another customer's statement by changing statement ID
```

## Severity

```text
Critical
```

## Priority

```text
P0
```

## Test Context

```text
Authenticated User:
CUST-001

Target Statement Owner:
CUST-002
```

## Expected

```text
Access denied.
No statement content returned.
```

## Actual

```text
Statement belonging to CUST-002 is returned to CUST-001.
```

## Impact

```text
Unauthorized disclosure of customer banking data.
```

Relevant risks:

```text
RISK-002
RISK-047
```

---

# 61. Example Calculation Defect

## Defect ID

```text
BUG-003
```

## Title

```text
[Statements] Payment fee is excluded from closing balance calculation
```

## Financial Data

```text
Opening:
25,000.00

Credit:
+5,000.00

Payment:
-2,000.00

Fee:
-20.00

Transfer:
-1,500.00

Refund:
+500.00
```

Expected:

```text
26,980.00
```

Actual:

```text
27,000.00
```

Difference:

```text
20.00
```

---

# 62. Defect Report Completion Checklist

Before filing, verify:

```text
Clear title?

Correct module?

Correct build?

Severity justified?

Priority justified?

Preconditions included?

Test data included?

Steps reproducible?

Expected result specific?

Actual result factual?

Financial impact recorded?

Security impact recorded?

Evidence attached?

Sensitive information removed?

Requirement/risk/test linked?

Workaround included if known?
```

---

# 63. Final Defect Reporting Principle

A defect report should never leave the reader asking:

```text
What happened?

How do I reproduce it?

Why is this important?
```

For Banking System defects, QA must additionally answer:

```text
Did money change?

Did customer data leak?

Was authorization bypassed?

Did the database persist an invalid state?

Can the issue occur twice?

Can concurrency make it worse?

Can this block release?
```

The core rule is:

```text
Report defects with enough evidence
to reproduce the problem,
understand the risk,
fix the cause,
and verify the resolution.
```

For financial and security issues:

```text
Precision matters as much as reproduction.
```
