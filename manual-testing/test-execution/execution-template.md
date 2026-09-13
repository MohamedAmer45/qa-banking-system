# Banking System — Test Execution Template

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Document | Test Execution Template        |
| Version  | 1.0                            |
| Status   | Active                         |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines the reusable format for recording manual test execution across the Banking System.

The template is designed to capture:

* What was executed
* Which environment/build was used
* Which test data was used
* Expected vs actual behavior
* Financial validation
* Security validation
* API/database validation where applicable
* Evidence
* Defects
* Retest information
* Overall execution result

The goal is to make test execution:

```text id="y0tq51"
Repeatable

Auditable

Traceable

Evidence-based

Useful for release decisions
```

---

# 3. Execution Naming Convention

Execution runs use:

```text id="xg2v7w"
EXEC-XXX
```

Examples:

```text id="ynfkw5"
EXEC-001
EXEC-002
EXEC-003
```

Suite-specific runs may use:

```text id="f4sppo"
SMK-RUN-XXX
REG-RUN-XXX
SAN-RUN-XXX
SEC-RUN-XXX
UAT-RUN-XXX
```

---

# 4. Test Status Values

Use:

```text id="oqsok6"
NOT_RUN

PASS

FAIL

BLOCKED

SKIPPED

NOT_APPLICABLE
```

Do not use ambiguous statuses such as:

```text id="fx66yc"
Mostly Passed

Looks Fine

Probably Works
```

---

# 5. Execution Result Definitions

## PASS

The observed behavior matches the expected result.

No unexplained data, financial, authorization, or persistence issue exists.

---

## FAIL

The actual result does not match the expected result.

A defect should normally be linked.

---

## BLOCKED

Execution cannot continue because of an external dependency or prerequisite problem.

Examples:

```text id="f862oj"
Environment unavailable

Required API down

Test user unavailable

Required configuration missing

Blocking defect prevents workflow
```

---

## SKIPPED

The test was intentionally not executed.

Reason must be recorded.

---

## NOT_APPLICABLE

The test does not apply to the current build/configuration.

---

# 6. Test Execution Header Template

```text id="4thn9u"
# Test Execution Report

Execution ID:

Execution Type:
Smoke / Sanity / Regression / Security / UAT / Ad Hoc / Other

Suite:

Environment:

Application URL:

Build / Version:

Frontend Version:

Backend Version:

Database Version / Migration:

Feature Flags:

Browser:

Browser Version:

Operating System:

Viewport:

Executed By:

Execution Date:

Start Time:

End Time:

Overall Status:
```

---

# 7. Scope

Use:

```text id="p46ij8"
## Scope

Modules Included:

Modules Excluded:

Execution Tier:

Related Change / Release:

Related Requirement(s):

Related Risk(s):

Related Defect(s):
```

Example:

```text id="3vwo94"
Modules Included:
Authentication
Accounts
Transfers
Statements

Execution Tier:
TIER-1

Related Release:
1.0.0-rc1
```

---

# 8. Entry Criteria

Record whether entry criteria were satisfied.

```text id="ysip4u"
## Entry Criteria

[ ] Deployment completed
[ ] Environment healthy
[ ] Required services available
[ ] Test data prepared
[ ] Required user roles available
[ ] Smoke passed where required
[ ] Known blockers reviewed
[ ] Build/version confirmed
```

If an item is not satisfied, explain why execution can still proceed.

---

# 9. Test Data

Record only synthetic test data.

```text id="ziyy9w"
## Test Data

Customer:
CUST-001

Second Customer:
CUST-002

Source Account:
ACC-001

Destination Account:
ACC-002

Beneficiary:
BEN-001

Card:
CARD-001

Loan:
LOAN-001

Deposit:
DEP-001

Admin:
ADMIN-001

Other:
```

Do not record:

```text id="ljbmf3"
Real passwords

Real OTPs

Access tokens

Real customer financial information
```

---

# 10. Environment Validation

Before execution, verify:

| Check                                | Status  | Notes |
| ------------------------------------ | ------- | ----- |
| Frontend reachable                   | NOT_RUN |       |
| Backend reachable                    | NOT_RUN |       |
| Database reachable                   | NOT_RUN |       |
| Authentication service healthy       | NOT_RUN |       |
| Notification service available       | NOT_RUN |       |
| External test dependencies available | NOT_RUN |       |
| Test data available                  | NOT_RUN |       |
| Feature flags correct                | NOT_RUN |       |

---

# 11. Execution Table

Use the following format:

| Test ID | Test Title       | Priority | Expected        | Actual        | Status  | Defect |
| ------- | ---------------- | -------- | --------------- | ------------- | ------- | ------ |
| TC-XXX  | Test description | P0       | Expected result | Actual result | NOT_RUN | —      |

For large runs, shorten Expected/Actual and place detailed evidence below.

---

# 12. Detailed Test Execution Template

```text id="un8h27"
## Test Execution

Test ID:

Test Title:

Module:

Priority:

Requirement:

Risk:

Preconditions:

Test Data:

### Steps

1.
2.
3.
4.

### Expected Result

### Actual Result

### Status

PASS / FAIL / BLOCKED / SKIPPED

### Defect

BUG-XXX / N/A

### Evidence

Screenshot:
Video:
Request:
Response:
Logs:
Transaction Reference:
Database Evidence:

### Notes
```

---

# 13. Financial Test Execution Template

For any transaction that affects money, add:

```text id="77uqrl"
## Financial Validation

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

Reconciliation Result:
PASS / FAIL
```

---

# 14. Financial Reconciliation Rule

For a simple debit:

```text id="0a7fj7"
Opening Balance
- Amount
- Fee
= Expected Closing Balance
```

For a credit:

```text id="ioovk6"
Opening Balance
+ Credit
= Expected Closing Balance
```

For a mixed period:

```text id="1j1gev"
Opening
+ Credits
- Debits
- Fees
= Closing
```

---

# 15. Example Financial Execution

```text id="ogmah4"
Opening Balance:
10,000.00

Transfer:
1,000.00

Fee:
10.00

Expected Closing:
8,990.00

Actual Closing:
8,990.00

Difference:
0.00

Result:
PASS
```

---

# 16. Financial Failure Example

```text id="qgs1o6"
Opening Balance:
10,000.00

Expected Debit:
1,010.00

Actual Debit:
2,020.00

Expected Closing:
8,990.00

Actual Closing:
7,980.00

Difference:
-1,010.00

Result:
FAIL

Defect:
BUG-001
```

---

# 17. Security Validation Template

Use when test concerns authentication, authorization, roles, or data isolation.

```text id="gztf5v"
## Security Validation

Authenticated User:

Role:

Target Resource:

Resource Owner:

Expected Access:
ALLOW / DENY

Actual Access:
ALLOW / DENY

Authentication Enforced:
YES / NO

Authorization Enforced:
YES / NO

Cross-Customer Isolation:
PASS / FAIL / N/A

Sensitive Data Exposure:
YES / NO

Security Result:
PASS / FAIL
```

---

# 18. Security Failure Example

```text id="oq7eu1"
Authenticated User:
CUST-001

Target Resource:
STAT-002

Resource Owner:
CUST-002

Expected:
DENY

Actual:
ALLOW

Result:
FAIL

Defect:
BUG-003
```

---

# 19. API Validation Template

```text id="dffh9l"
## API Validation

Endpoint:

Method:

Request ID:

Request Parameters:

Request Body:

Expected Status:

Actual Status:

Expected Response:

Actual Response:

Response Time:

Correlation ID:

Result:
PASS / FAIL
```

Redact sensitive headers and tokens.

---

# 20. Database Validation Template

```text id="lg2h75"
## Database Validation

Database:

Schema:

Tables:

Relevant IDs:

Expected Records:

Actual Records:

Expected Status:

Actual Status:

Expected Relationships:

Actual Relationships:

Expected Balance:

Actual Balance:

Duplicate Check:

Integrity Result:
PASS / FAIL
```

---

# 21. Cross-Layer Validation

For high-risk banking tests, validate multiple layers.

Example:

```text id="e3yvwz"
UI:
Transfer shows COMPLETED

API:
status = COMPLETED

Database:
transaction status = COMPLETED

Transaction History:
record exists once

Statement:
transaction represented correctly

Balance:
reconciles
```

Result:

```text id="wy9lpi"
PASS
```

only if all required authoritative layers agree.

---

# 22. Transaction Reference Tracking

For financial tests, record:

```text id="ihisf1"
Transaction Reference:

Payment Reference:

Loan Reference:

Deposit Reference:

Reversal Reference:

Provider Reference:
```

References make execution easier to investigate later.

---

# 23. Timestamp Tracking

For time-sensitive defects/tests record:

```text id="yz7xyu"
Action Started:

Request Sent:

Response Received:

Transaction Created:

Status Updated:

Notification Received:
```

Useful for:

* Race conditions
* Scheduled transfers
* Retry behavior
* Notifications
* Performance observations

---

# 24. Evidence Standards

## PASS Evidence

For critical cases, capture enough evidence to prove:

* Correct output
* Correct financial state
* Correct status
* Correct authorization

Not every trivial test requires screenshots.

---

## FAIL Evidence

For failures capture:

```text id="zh3p81"
Screenshot/video

Exact test data

Request/response

Transaction reference

Timestamp

Logs

Database evidence where relevant
```

---

# 25. Defect Linking

When a test fails:

```text id="d9r4ds"
Test:
REG-021

Status:
FAIL

Defect:
BUG-001
```

If multiple tests fail due to the same root issue, they may link to the same defect.

Do not create unnecessary duplicate defects.

---

# 26. Blocking Issue Template

```text id="ngmrvo"
## Blocker

Blocked Test:

Blocking Reason:

Blocking Defect:

Dependency:

Start Time:

Impact:

Tests Also Affected:

Workaround:

Expected Resolution:
```

Do not enter speculative resolution dates.

---

# 27. Skipped Test Template

```text id="kkt1q5"
## Skipped Test

Test ID:

Reason:

Approved By:

Risk:

Follow-Up Required:
YES / NO
```

---

# 28. Execution Progress Summary

| Status  | Count |
| ------- | ----: |
| Total   |     0 |
| Passed  |     0 |
| Failed  |     0 |
| Blocked |     0 |
| Skipped |     0 |
| Not Run |     0 |

---

# 29. Pass Rate

Formula:

```text id="dj8kru"
Pass Rate
=
Passed
/
Executed
× 100
```

Where:

```text id="umfbnj"
Executed = Passed + Failed
```

Blocked/skipped tests should normally be reported separately.

Example:

```text id="mvhsfe"
Passed = 90
Failed = 10

Pass Rate = 90%
```

---

# 30. Priority Summary

| Priority | Total | Passed | Failed | Blocked |
| -------- | ----: | -----: | -----: | ------: |
| P0       |     0 |      0 |      0 |       0 |
| P1       |     0 |      0 |      0 |       0 |
| P2       |     0 |      0 |      0 |       0 |
| P3       |     0 |      0 |      0 |       0 |

P0 failures should be highlighted separately.

---

# 31. Module Summary

| Module         | Total | Passed | Failed | Blocked |
| -------------- | ----: | -----: | -----: | ------: |
| Authentication |     0 |      0 |      0 |       0 |
| Accounts       |     0 |      0 |      0 |       0 |
| Transfers      |     0 |      0 |      0 |       0 |
| Payments       |     0 |      0 |      0 |       0 |
| Cards          |     0 |      0 |      0 |       0 |
| Loans          |     0 |      0 |      0 |       0 |
| Deposits       |     0 |      0 |      0 |       0 |
| Statements     |     0 |      0 |      0 |       0 |
| Security       |     0 |      0 |      0 |       0 |
| Admin          |     0 |      0 |      0 |       0 |

---

# 32. Defect Summary

| Defect  | Severity | Priority | Module    | Status |
| ------- | -------- | -------- | --------- | ------ |
| BUG-XXX | Critical | P0       | Transfers | OPEN   |

---

# 33. Open Defect Counts

| Severity | Count |
| -------- | ----: |
| Critical |     0 |
| High     |     0 |
| Medium   |     0 |
| Low      |     0 |

---

# 34. Risk Summary

Record risks affected by failed execution.

Example:

```text id="83gc1z"
RISK-003:
Duplicate transaction prevention failed.

RISK-013:
Concurrency coverage incomplete due to environment limitation.
```

---

# 35. Coverage Gaps

Use:

```text id="wf603x"
## Coverage Gaps

Not Executed:

Blocked:

Unsupported Browser:

Missing Test Data:

Environment Limitations:

Deferred Coverage:

Impact:
```

Coverage gaps must be visible in release reporting.

---

# 36. Known Limitations

Examples:

```text id="6g45ug"
External provider sandbox unavailable.

SMS delivery validated only through mock provider.

WebKit execution not available on current environment.

Large-volume data set not yet loaded.

Database access read-only.
```

---

# 37. Retest Section

```text id="eqhivl"
## Retest Summary

Defect:

Original Failed Test:

Fixed Build:

Retest Date:

Original Steps Re-executed:
YES / NO

Retest Result:
PASS / FAIL

Related Regression:
PASS / FAIL / NOT_RUN

Notes:
```

---

# 38. Regression After Retest

For high-risk fixes, include related tests.

Example:

```text id="hqyshe"
BUG-001 fixed.

Retest:
REG-021

Related:
REG-016
REG-017
REG-022
REG-024
SEC-SUITE-052
SEC-SUITE-057
```

---

# 39. Execution Recommendation

Use:

```text id="648a1v"
## Recommendation

PROCEED

PROCEED_WITH_KNOWN_RISK

DO_NOT_PROCEED
```

Then explain why.

---

# 40. Proceed Criteria

Possible when:

* Required suite passed.
* No unacceptable P0 issue remains.
* No financial integrity failure.
* No authentication/authorization bypass.
* Blocked coverage understood.
* Residual risk acceptable.

---

# 41. Proceed With Known Risk

Use only when:

* Known defects are documented.
* No unacceptable Critical defect remains.
* Risk is explicitly understood.
* Release decision owner accepts risk.

Example:

```text id="1lvgap"
One P2 transaction filter defect remains.

No financial or security impact.

Recommendation:
PROCEED_WITH_KNOWN_RISK
```

---

# 42. Do Not Proceed Criteria

Examples:

```text id="oxsg0k"
Incorrect account balance

Duplicate transfer

Authentication bypass

Customer data exposure

Privilege escalation

Duplicate loan disbursement

Duplicate deposit payout

Core transaction workflow unavailable
```

Recommendation:

```text id="1c9ezm"
DO_NOT_PROCEED
```

---

# 43. Final Execution Summary Template

```text id="y0iwzl"
## Execution Summary

Execution ID:

Suite:

Environment:

Build:

Total Planned:

Executed:

Passed:

Failed:

Blocked:

Skipped:

Pass Rate:

P0 Failed:

Critical Defects:

High Defects:

Key Risks:

Coverage Gaps:

Overall Status:

Release Recommendation:
```

---

# 44. Example Smoke Execution

```text id="ql25z8"
Execution ID:
SMK-RUN-001

Environment:
QA

Build:
1.0.0-rc1

Total Planned:
20

Executed:
20

Passed:
19

Failed:
1

Blocked:
0

P0 Failed:
1

Failed Test:
SMK-021

Defect:
BUG-001 — Duplicate transfer

Overall:
FAIL

Recommendation:
DO_NOT_PROCEED
```

---

# 45. Example Regression Execution

```text id="jxq6zp"
Execution ID:
REG-RUN-001

Tier:
TIER-1

Total:
42

Passed:
42

Failed:
0

Blocked:
0

P0 Failed:
0

Critical Defects:
0

Result:
PASS

Recommendation:
PROCEED
```

---

# 46. Example Blocked Execution

```text id="yro5vb"
Test:
REG-191

Status:
BLOCKED

Reason:
Payment provider sandbox unavailable.

Impact:
Payment-provider failure reconciliation not validated.

Risk:
RISK-039

Recommendation:
Do not claim full payment regression coverage.
```

---

# 47. Execution Evidence Folder Structure

Recommended later structure:

```text id="xzq16d"
evidence/
├── screenshots/
├── videos/
├── api/
├── database/
├── logs/
└── reports/
```

Example:

```text id="bgt1f1"
evidence/
└── REG-RUN-001/
    ├── screenshots/
    ├── api/
    └── database/
```

---

# 48. Evidence Naming Convention

Recommended:

```text id="zrhkvm"
<TestID>-<short-description>-<timestamp>
```

Examples:

```text id="3g52x6"
REG-021-duplicate-transfer-001.png

REG-055-statement-auth-denied.png

SEC-SUITE-017-idor-response.json
```

---

# 49. Manual Execution Quality Rules

Do not mark a test PASS only because:

```text id="cudtv0"
No error message appeared.
```

For financial tests, confirm authoritative effect.

Do not mark authorization PASS only because:

```text id="v0e8gd"
The button is hidden.
```

Verify direct request behavior where relevant.

Do not mark persistence PASS only because:

```text id="bn3lec"
UI showed Success.
```

Verify persisted state where appropriate.

---

# 50. Financial Test Quality Rule

For any P0 financial test, ideally validate:

```text id="nzdujn"
Operation status

Balance

Transaction record

Reference

Fee

Destination effect

History

Database/API consistency
```

---

# 51. Authorization Test Quality Rule

For authorization tests:

```text id="2vzr0s"
Authorized user path → works

Unauthorized user path → denied

No protected data returned

No persistent change occurs
```

This verifies both positive and negative permission behavior.

---

# 52. Concurrency Execution Recording

When executing concurrent tests, record:

```text id="x4gc7t"
Initial state

Request A

Request B

Timing

Response A

Response B

Final state

Financial result

Created records
```

Example:

```text id="rzdya5"
Balance:
1,000.00

Request A:
800.00

Request B:
500.00

Expected:
Only one can succeed.

Actual:
Both succeed.

Result:
FAIL
```

---

# 53. State Transition Recording

Record:

```text id="s320s4"
Entity:

Initial State:

Action:

Expected Final State:

Actual Final State:

Result:
```

Example:

```text id="r6y0b7"
Card:
CARD-001

Initial:
ACTIVE

Action:
Freeze

Expected:
FROZEN

Actual:
FROZEN

Result:
PASS
```

---

# 54. Scheduled Operation Recording

For scheduled or recurring operations record:

```text id="kso3nj"
Created At:

Scheduled For:

Timezone:

Cancelled At:

Expected Execution:

Actual Execution:

Final Status:
```

This helps investigate timing defects.

---

# 55. Calculation Validation Template

```text id="jn3yem"
Input:

Formula:

Rounding Rule:

Expected:

Actual:

Difference:

Result:
```

Use for:

* Fees
* Interest
* Penalties
* Installments
* Maturity
* Refunds

---

# 56. Cross-Browser Execution Recording

| Test ID | Chrome  | Edge    | Firefox | WebKit  |
| ------- | ------- | ------- | ------- | ------- |
| CB-005  | NOT_RUN | NOT_RUN | NOT_RUN | NOT_RUN |
| CB-022  | NOT_RUN | NOT_RUN | NOT_RUN | NOT_RUN |
| CB-062  | NOT_RUN | NOT_RUN | NOT_RUN | NOT_RUN |

Record browser versions separately in the run metadata.

---

# 57. Responsive Execution Recording

| Test      | 1920×1080 | 1366×768 | 768×1024 | 390×844 | 360×800 |
| --------- | --------- | -------- | -------- | ------- | ------- |
| Login     | —         | —        | —        | NOT_RUN | NOT_RUN |
| Transfer  | —         | —        | —        | NOT_RUN | NOT_RUN |
| Statement | —         | —        | —        | NOT_RUN | NOT_RUN |

---

# 58. Execution Metrics

Useful metrics:

```text id="j8r07h"
Pass rate

Execution completion %

P0 pass rate

Defects per module

Critical defect count

Blocked test count

Requirement coverage

Risk coverage

Retest success rate
```

---

# 59. Execution Completion Formula

```text id="2e9lkq"
Execution Completion %
=
Executed + Blocked + Skipped
/
Planned
× 100
```

Report individual categories as well.

Do not hide blocked/skipped tests inside a single completion number.

---

# 60. P0 Pass Rate

```text id="lyqld4"
P0 Pass Rate
=
Passed P0
/
Executed P0
× 100
```

For release-critical suites, target should generally be:

```text id="j95cvg"
100%
```

unless explicit risk acceptance exists.

---

# 61. Daily Execution Update Template

```text id="poa2r8"
## Daily Test Update

Build:

Executed Today:

Passed:

Failed:

Blocked:

New Defects:

Critical Issues:

Key Risks:

Coverage Completed:

Next Coverage:
```

Useful during long regression cycles.

---

# 62. Test Execution Review Checklist

Before finalizing a run:

```text id="atbh63"
Correct build recorded?

Environment recorded?

Test data recorded?

Statuses complete?

Failed tests linked to defects?

P0 failures highlighted?

Financial failures reconciled?

Authorization failures validated?

Blocked tests explained?

Evidence linked?

Coverage gaps stated?

Recommendation included?
```

---

# 63. Execution Data Integrity

Do not alter a completed execution result simply because a defect was later fixed.

Keep:

```text id="3rlpns"
Original Run:
FAIL
```

Then create:

```text id="jh98hg"
Retest:
PASS
```

This preserves historical execution integrity.

---

# 64. Execution History

Maintain separate runs such as:

```text id="fj3ktq"
SMK-RUN-001
SMK-RUN-002

REG-RUN-001
REG-RUN-002

SEC-RUN-001
```

Do not overwrite previous release results.

---

# 65. Traceability

Each execution run should connect to:

```text id="is1w7v"
Test Suite
Test Cases
Requirements
Risks
Defects
Release
```

Example:

```text id="qz52eq"
REG-RUN-001
↓
REG-021
↓
REQ-TRF-011
↓
RISK-003
↓
BUG-001
```

---

# 66. Auditability Principle

Another QA engineer should be able to review the execution later and answer:

```text id="vn9nns"
Which build was tested?

Which tests ran?

Which data was used?

Which tests failed?

What evidence exists?

What defects were created?

Which risks remain?

Why was release recommended or rejected?
```

---

# 67. Test Execution Anti-Patterns

Avoid:

```text id="5ytrj5"
Marking unexecuted tests PASS.

Using one result for multiple untested browsers.

Ignoring failed negative cases because happy path works.

Reporting only UI behavior for financial defects.

Removing old failed results after fix.

Hiding blocked coverage.

Using real customer data.

Recording credentials in evidence.
```

---

# 68. Final Test Execution Principle

Execution should not simply record:

```text id="54gkge"
Pass
or
Fail
```

It should establish whether the system behaved correctly across:

```text id="476hbv"
Functionality

Money

Authorization

State

Persistence

Auditability

User experience
```

For Banking System testing, the strongest evidence chain is:

```text id="1rlqj3"
Test Action
↓
Observed UI/API Result
↓
Financial / Security Validation
↓
Persistent State
↓
Evidence
↓
Defect or PASS
```

The core rule is:

```text id="2gv0c5"
A test result is only as trustworthy
as the evidence and validation behind it.
```
