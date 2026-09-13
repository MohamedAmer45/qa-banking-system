# Banking System — Statement Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Statements                     |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Statement scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Statement generation
* Account ownership
* Date-range validation
* Opening balance
* Closing balance
* Transaction inclusion
* Transaction ordering
* Fees
* Credits and debits
* Reversals
* Refunds
* Pending/failed transactions
* Historical consistency
* PDF/download behavior
* File naming
* Authorization
* IDOR
* Sensitive data
* API validation
* Database reconciliation
* Cross-module reconciliation
* Performance
* Cross-browser behavior
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Statement test cases use:

```text
STMT-TC-XXX
```

Examples:

```text
STMT-TC-001
STMT-TC-002
STMT-TC-003
```

---

# 4. Critical Statement Invariants

## Invariant 1 — Ownership

A customer may generate or download statements only for accounts they are authorized to access.

---

## Invariant 2 — Financial Reconciliation

For a statement period:

```text
Opening Balance
+ Credits
- Debits
- Fees
± Valid Adjustments
=
Closing Balance
```

The statement must mathematically reconcile.

---

## Invariant 3 — Historical Accuracy

A previously generated historical statement must not silently change because current account state changes.

---

## Invariant 4 — Transaction Integrity

Transactions included in the statement must correspond to authoritative settled financial activity for the selected period.

---

## Invariant 5 — Traceability

Statement entries should be traceable to underlying transactions through appropriate references.

---

## Invariant 6 — Secure Delivery

Downloaded statements must not expose another customer's financial information or unnecessary sensitive data.

---

# 5. Common Test Data

## Customer A

```text
Customer:
CUST-001

Status:
ACTIVE
```

## Customer B

```text
Customer:
CUST-002

Status:
ACTIVE
```

## Account A

```text
Account:
ACC-001

Owner:
CUST-001

Currency:
EGP
```

## Account B

```text
Account:
ACC-002

Owner:
CUST-002

Currency:
EGP
```

## Example Statement Period

```text
Start Date:
2026-08-01

End Date:
2026-08-31
```

## Example Reconciliation Data

```text
Opening Balance:
25,000.00

Credits:
5,500.00

Debits:
3,500.00

Fees:
20.00

Expected Closing Balance:
26,980.00
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer is authenticated.

Statement service is available.

Account and transaction data exist.

Synthetic banking data is used.

API/DB validation is available where required.
```

---

# 7. Statement Generation Test Cases

## STMT-TC-001 — Generate Valid Statement

**Priority:** P0
**Requirement:** REQ-STMT-001
**Automation:** Playwright / API

### Steps

1. Login as `CUST-001`.
2. Open Statements.
3. Select `ACC-001`.
4. Select valid date range.
5. Generate statement.

### Expected Result

* Statement is generated successfully.
* Correct account is shown.
* Correct date range is shown.
* Opening/closing balances are present.
* Applicable transactions are included.

---

## STMT-TC-002 — Generate Statement for Current Month

**Priority:** P1

### Expected Result

Current-period statement includes transactions up to the defined cutoff/current date.

---

## STMT-TC-003 — Generate Statement With No Transactions

**Priority:** P1

### Expected Result

Statement still displays:

* Account
* Period
* Opening balance
* Closing balance

with no fabricated transactions.

---

# 8. Statement Ownership / IDOR

## STMT-TC-004 — Customer Generates Own Statement

**Priority:** P0
**Requirement:** REQ-STMT-002

Expected: allowed.

---

## STMT-TC-005 — Generate Another Customer's Statement

**Priority:** P0
**Risk:** RISK-002, RISK-047

### Steps

1. Login as `CUST-001`.
2. Request statement for `ACC-002`.

### Expected Result

Access denied.

No Customer B financial data is exposed.

---

## STMT-TC-006 — Manipulate Account ID in Statement URL

**Priority:** P0

Expected: backend ownership validation denies request.

---

## STMT-TC-007 — Manipulate Statement ID

**Priority:** P0

Expected: unauthorized historical statement cannot be accessed.

---

## STMT-TC-008 — Direct Download URL for Another Customer's Statement

**Priority:** P0
**Risk:** RISK-009? Use RISK-047 / statement IDOR

Expected: denied even if URL is known.

---

# 9. Unauthenticated Access Test Cases

## STMT-TC-009 — Statement Page Without Authentication

**Priority:** P0

Expected: authentication required.

---

## STMT-TC-010 — Download Statement After Logout

**Priority:** P0

Expected: revoked session cannot download protected statement.

---

## STMT-TC-011 — Download Statement After Session Expiry

**Priority:** P0

Expected: re-authentication required.

---

# 10. Date Range Validation

## STMT-TC-012 — Valid Start and End Date

**Priority:** P1
**Requirement:** REQ-STMT-003

Expected: accepted.

---

## STMT-TC-013 — Start Date After End Date

**Priority:** P1

Expected: rejected.

---

## STMT-TC-014 — Same Start and End Date

**Priority:** P1

Expected: single-day statement generated according to inclusive date rules.

---

## STMT-TC-015 — Future Start Date

**Priority:** P1

Expected: rejected or handled according to product rules.

---

## STMT-TC-016 — Future End Date

**Priority:** P1

Expected: future period not falsely populated.

---

## STMT-TC-017 — Maximum Supported Historical Range

**Priority:** P1
**Type:** Boundary

Expected: accepted.

---

## STMT-TC-018 — Range Exceeds Maximum Supported Period

**Priority:** P1

Expected: rejected or divided according to documented rules.

---

# 11. Date Boundary Test Cases

## STMT-TC-019 — Transaction Exactly at Start Boundary

**Priority:** P0

Expected: included if start boundary is inclusive.

---

## STMT-TC-020 — Transaction Exactly at End Boundary

**Priority:** P0

Expected: included according to defined end-boundary rule.

---

## STMT-TC-021 — Transaction Just Before Period

**Priority:** P0

Expected: excluded from activity, but may influence opening balance appropriately.

---

## STMT-TC-022 — Transaction Just After Period

**Priority:** P0

Expected: excluded.

---

# 12. Opening Balance Test Cases

## STMT-TC-023 — Opening Balance Correct

**Priority:** P0
**Requirement:** REQ-STMT-004
**Risk:** RISK-020

### Expected Result

Opening balance equals authoritative account balance immediately before statement period begins.

---

## STMT-TC-024 — Opening Balance With Prior-Day Transaction

**Priority:** P0

Expected: all valid prior activity reflected.

---

## STMT-TC-025 — Zero Opening Balance

**Priority:** P1

Expected:

```text
0.00
```

displayed correctly.

---

# 13. Closing Balance Test Cases

## STMT-TC-026 — Closing Balance Correct

**Priority:** P0
**Requirement:** REQ-STMT-005

Expected: closing value mathematically reconciles.

---

## STMT-TC-027 — Closing Balance Matches Account at Cutoff

**Priority:** P0

Expected: statement closing balance equals authoritative account balance at statement cutoff.

---

## STMT-TC-028 — Zero Closing Balance

**Priority:** P1

Expected: correctly displayed.

---

# 14. Full Reconciliation Test

## STMT-TC-029 — Opening + Credits - Debits - Fees = Closing

**Priority:** P0
**Risk:** RISK-020

### Test Data

```text
Opening:
25,000.00

Credits:
5,500.00

Debits:
3,500.00

Fees:
20.00
```

### Expected Result

```text
Closing:
26,980.00
```

---

# 15. Transaction Inclusion Test Cases

## STMT-TC-030 — Completed Transfer Included

**Priority:** P0

Expected: included once.

---

## STMT-TC-031 — Completed Payment Included

**Priority:** P0

Expected: included once.

---

## STMT-TC-032 — Settled Card Purchase Included

**Priority:** P0

Expected: included correctly.

---

## STMT-TC-033 — Loan Disbursement Included

**Priority:** P0

Expected: correct credit.

---

## STMT-TC-034 — Loan Repayment Included

**Priority:** P0

Expected: correct debit.

---

## STMT-TC-035 — Deposit Funding Included

**Priority:** P0

Expected: correct debit.

---

## STMT-TC-036 — Deposit Payout Included

**Priority:** P0

Expected: correct credit.

---

# 16. Failed / Pending Transaction Handling

## STMT-TC-037 — Failed Transfer Not Included as Settled Debit

**Priority:** P0

Expected: no financial effect in settled statement totals.

---

## STMT-TC-038 — Failed Payment Not Included as Settled Debit

**Priority:** P0

Expected: statement remains correct.

---

## STMT-TC-039 — Pending Card Authorization Handling

**Priority:** P0

Expected: pending authorization is represented according to statement rules and not falsely treated as settled financial activity.

---

## STMT-TC-040 — Cancelled Scheduled Transfer Excluded

**Priority:** P0

Expected: no debit.

---

# 17. Fee Test Cases

## STMT-TC-041 — Transfer Fee Included Once

**Priority:** P0

Expected: exactly one fee entry/amount according to statement model.

---

## STMT-TC-042 — Payment Fee Included Once

**Priority:** P0

Expected: correct.

---

## STMT-TC-043 — Loan Fee Included Correctly

**Priority:** P0

Expected: fee represented according to loan/accounting rules.

---

## STMT-TC-044 — Deposit Penalty Included Correctly

**Priority:** P0

Expected: correct early-withdrawal penalty representation.

---

## STMT-TC-045 — No Duplicate Fee

**Priority:** P0

Expected: no double counting.

---

# 18. Reversal Test Cases

## STMT-TC-046 — Original Transfer Retained After Reversal

**Priority:** P0
**Requirement:** REQ-STMT-006

Expected: original transaction remains visible/traceable.

---

## STMT-TC-047 — Reversal Entry Included

**Priority:** P0

Expected: separate compensating entry appears.

---

## STMT-TC-048 — Reversal Reconciles Closing Balance

**Priority:** P0

Expected: original + reversal produce mathematically correct net result.

---

# 19. Refund Test Cases

## STMT-TC-049 — Full Payment Refund

**Priority:** P0

Expected: original debit and refund credit are represented correctly.

---

## STMT-TC-050 — Partial Refund

**Priority:** P0

Expected: partial credit shown accurately.

---

## STMT-TC-051 — Card Refund

**Priority:** P0

Expected: refund reconciles to card transaction.

---

# 20. Transaction Ordering

## STMT-TC-052 — Transactions Chronological

**Priority:** P1

Expected: ordering follows statement specification.

---

## STMT-TC-053 — Equal Timestamp Ordering

**Priority:** P1

Expected: deterministic ordering using secondary key/reference.

---

## STMT-TC-054 — Running Balance Ordering

**Priority:** P0

Where running balances are shown.

Expected: running balance follows exact transaction order.

---

# 21. Running Balance Test Cases

## STMT-TC-055 — Running Balance After Credit

**Priority:** P0

Expected: prior balance + credit.

---

## STMT-TC-056 — Running Balance After Debit

**Priority:** P0

Expected: prior balance - debit/fee as applicable.

---

## STMT-TC-057 — Running Balance After Reversal

**Priority:** P0

Expected: correct compensating value.

---

## STMT-TC-058 — Final Running Balance Equals Closing Balance

**Priority:** P0

Expected: exact match.

---

# 22. Historical Statement Consistency

## STMT-TC-059 — Historical Statement Reopened Later

**Priority:** P0
**Requirement:** REQ-STMT-007

Expected: historical financial values remain consistent with the authoritative historical record.

---

## STMT-TC-060 — Current Account Balance Changes After Historical Statement

**Priority:** P0

Expected: historical closing balance does not change merely because new transactions occur later.

---

## STMT-TC-061 — Later Reversal of Historical Transaction

**Priority:** P0

Expected: behavior follows accounting/statement correction policy without silently rewriting prior finalized statement history.

---

# 23. Statement Generation Idempotency

## STMT-TC-062 — Generate Same Statement Twice

**Priority:** P1

Expected: both generated copies contain the same financial content for an unchanged finalized period.

---

## STMT-TC-063 — Rapid Double-Click Generate

**Priority:** P2

Expected: no corrupted/partial duplicate output.

---

# 24. Statement Download Test Cases

## STMT-TC-064 — Download Statement PDF

**Priority:** P0
**Requirement:** REQ-STMT-008

Expected:

* File downloads successfully.
* File opens.
* Correct account/period.
* Financial content matches on-screen/API result.

---

## STMT-TC-065 — Downloaded File Is Not Empty

**Priority:** P0

Expected: valid nonzero file content.

---

## STMT-TC-066 — Download Correct MIME/File Type

**Priority:** P1

Expected: correct content type/file extension.

---

## STMT-TC-067 — File Name Contains Useful Context

**Priority:** P2

Example:

```text
statement_ACC-001_2026-08.pdf
```

without exposing unnecessary sensitive information.

---

# 25. PDF Content Test Cases

## STMT-TC-068 — PDF Contains Account Identifier

**Priority:** P1

Expected: appropriately masked/account-identifying information available.

---

## STMT-TC-069 — PDF Contains Statement Period

**Priority:** P0

Expected: correct dates.

---

## STMT-TC-070 — PDF Contains Opening Balance

**Priority:** P0

Expected: correct.

---

## STMT-TC-071 — PDF Contains Closing Balance

**Priority:** P0

Expected: correct.

---

## STMT-TC-072 — PDF Contains Transactions

**Priority:** P0

Expected: complete authorized settled activity.

---

## STMT-TC-073 — PDF Financial Values Match UI/API

**Priority:** P0

Expected: exact consistency.

---

# 26. PDF Layout Test Cases

## STMT-TC-074 — Long Transaction Description

**Priority:** P2

Expected: text wraps without hiding amount/reference.

---

## STMT-TC-075 — Large Number of Transactions

**Priority:** P1

Expected: pagination/page breaks are correct.

---

## STMT-TC-076 — No Truncated Amount Columns

**Priority:** P0

Expected: full monetary values readable.

---

## STMT-TC-077 — Header/Footer on Multiple Pages

**Priority:** P2

Expected: layout remains understandable.

---

# 27. Sensitive Data Test Cases

## STMT-TC-078 — Account Number Masking

**Priority:** P0
**Risk:** RISK-021

Expected: only permitted identifier displayed.

---

## STMT-TC-079 — Card Data Masking

**Priority:** P0

Expected: no full PAN/CVV/PIN.

---

## STMT-TC-080 — No Authentication Secrets

**Priority:** P0

Expected: no password, OTP, token, session identifier.

---

## STMT-TC-081 — Recipient Information Minimization

**Priority:** P1

Expected: only required recipient information displayed.

---

# 28. Direct Link Security Test Cases

## STMT-TC-082 — Statement Link After Logout

**Priority:** P0

Expected: protected statement inaccessible.

---

## STMT-TC-083 — Statement Link Shared With Another Customer

**Priority:** P0

Expected: recipient cannot access without authorization.

---

## STMT-TC-084 — Expired Temporary Download Link

**Priority:** P0

Where signed URLs are used.

Expected: expired link rejected.

---

## STMT-TC-085 — Tampered Download Token

**Priority:** P0

Expected: rejected.

---

# 29. API Statement Test Cases

## STMT-TC-086 — Generate Own Statement API

**Priority:** P0
**Automation:** REST Assured

Expected: succeeds.

---

## STMT-TC-087 — Generate Another Customer's Statement API

**Priority:** P0

Expected: denied.

---

## STMT-TC-088 — Unauthenticated Statement API

**Priority:** P0

Expected: denied.

---

## STMT-TC-089 — Invalid Account ID

**Priority:** P1

Expected: safe error.

---

## STMT-TC-090 — Invalid Date Range API

**Priority:** P1

Expected: validation error.

---

## STMT-TC-091 — Unsupported Output Format

**Priority:** P2

Expected: rejected according to contract.

---

# 30. Database Validation Test Cases

## STMT-TC-092 — Opening Balance Source Data

**Priority:** P0
**Automation:** SQL

Expected: opening balance derivation matches authoritative ledger.

---

## STMT-TC-093 — Closing Balance Source Data

**Priority:** P0

Expected: closing balance derivation correct.

---

## STMT-TC-094 — Statement Transaction Set

**Priority:** P0

Expected: included records match authoritative settled transaction dataset.

---

## STMT-TC-095 — Fee Dataset

**Priority:** P0

Expected: fees included exactly once.

---

## STMT-TC-096 — Reversal Relationship

**Priority:** P0

Expected: original/reversal linkage preserved.

---

## STMT-TC-097 — Statement Metadata Persistence

**Priority:** P1

Where statements are stored.

Expected:

* Customer/account
* Period
* Generation time
* File/reference
* Version/status

are correct.

---

# 31. UI/API/Database Reconciliation

## STMT-TC-098 — Statement Cross-Layer Validation

**Priority:** P0
**Risk:** RISK-039, RISK-048

Validate:

```text
UI Opening
=
API Opening
=
Authoritative Data
```

```text
UI Closing
=
API Closing
=
Authoritative Data
```

and transaction set/fees reconcile.

---

## STMT-TC-099 — PDF/API Reconciliation

**Priority:** P0

Expected: downloaded PDF agrees with statement API.

---

# 32. Account Reconciliation Test Cases

## STMT-TC-100 — Statement Closing Matches Account Cutoff Balance

**Priority:** P0

Expected: exact match at same cutoff.

---

## STMT-TC-101 — Statement Does Not Use Current Balance for Old Period

**Priority:** P0

Expected: historical closing balance remains historical.

---

# 33. Transaction Module Reconciliation

## STMT-TC-102 — Statement Transfer Matches Transaction History

**Priority:** P0

Expected: amount/reference/status/direction consistent.

---

## STMT-TC-103 — Statement Payment Matches Transaction History

**Priority:** P0

Expected: consistent.

---

## STMT-TC-104 — Statement Card Settlement Matches Transaction History

**Priority:** P0

Expected: consistent.

---

## STMT-TC-105 — Statement Loan Activity Matches Transaction History

**Priority:** P0

Expected: consistent.

---

## STMT-TC-106 — Statement Deposit Activity Matches Transaction History

**Priority:** P0

Expected: consistent.

---

# 34. Duplicate / Missing Entry Test Cases

## STMT-TC-107 — No Duplicate Transactions

**Priority:** P0

Expected: each financial event represented exactly according to accounting design.

---

## STMT-TC-108 — No Missing Completed Transaction

**Priority:** P0

Expected: all eligible transactions included.

---

## STMT-TC-109 — Duplicate Reference Detection

**Priority:** P0

Expected: duplicate financial entry is not silently included twice.

---

# 35. Timezone Test Cases

## STMT-TC-110 — Transaction Near Midnight

**Priority:** P1
**Risk:** RISK-043

Expected: included in correct statement date according to business timezone.

---

## STMT-TC-111 — API UTC / UI Local Date

**Priority:** P1

Expected: correct date conversion.

---

## STMT-TC-112 — Month-End Boundary

**Priority:** P0

Expected: transaction belongs to correct monthly statement.

---

## STMT-TC-113 — Year-End Boundary

**Priority:** P0

Expected: transaction belongs to correct annual/monthly period.

---

# 36. Concurrent Generation Test Cases

## STMT-TC-114 — Generate Same Statement Concurrently

**Priority:** P1

Expected: consistent financial content.

---

## STMT-TC-115 — Generate Statement While New Transaction Settles

**Priority:** P0
**Risk:** RISK-013

Expected: statement uses a defined cutoff/snapshot and remains internally consistent.

---

## STMT-TC-116 — Generate Statement During Reversal

**Priority:** P0

Expected: snapshot semantics prevent contradictory totals.

---

# 37. Performance Test Cases

## STMT-TC-117 — Statement With Large Transaction Volume

**Priority:** P1

Expected: generation completes within accepted performance target.

---

## STMT-TC-118 — Large PDF Generation

**Priority:** P1

Expected: complete file generated without missing pages/transactions.

---

## STMT-TC-119 — Concurrent Statement Generation

**Priority:** P1

Expected: service remains stable and authorization remains intact.

---

# 38. Error Handling Test Cases

## STMT-TC-120 — Statement Service Unavailable

**Priority:** P1

Expected: clear failure; no fake empty statement.

---

## STMT-TC-121 — PDF Generation Fails

**Priority:** P1

Expected: user informed; statement data not misrepresented.

---

## STMT-TC-122 — Transaction Data Retrieval Partially Fails

**Priority:** P0

Expected: incomplete financial statement must not be presented as complete.

---

## STMT-TC-123 — Storage Service Fails

**Priority:** P1

Expected: secure failure/retry behavior.

---

# 39. Security Input Test Cases

## STMT-TC-124 — Manipulated Account ID

**Priority:** P0

Expected: ownership enforced.

---

## STMT-TC-125 — SQL-Like Date/Input

**Priority:** P1

Expected: no query manipulation.

---

## STMT-TC-126 — Script-Like Custom Statement Label

**Priority:** P1

Where supported.

Expected: no script execution.

---

## STMT-TC-127 — Path/File Name Manipulation

**Priority:** P0

Expected: customer cannot manipulate generated file path to access unauthorized files.

---

# 40. Audit Test Cases

## STMT-TC-128 — Statement Generation Audit

**Priority:** P1

Where required.

Expected:

```text
Actor
Account
Period
Timestamp
Statement Reference
```

recorded appropriately.

---

## STMT-TC-129 — Admin Statement Access Audit

**Priority:** P0
**Risk:** RISK-022

Expected: privileged access traceable.

---

## STMT-TC-130 — Audit Does Not Contain Sensitive Secrets

**Priority:** P0

Expected: no passwords/tokens/OTP/full card details.

---

# 41. Cross-Browser Test Cases

## STMT-TC-131 — Statement Generation in Chrome

**Priority:** P2

Expected: works.

---

## STMT-TC-132 — Statement Generation in Edge

**Priority:** P2

Expected: works.

---

## STMT-TC-133 — Statement Generation in Firefox

**Priority:** P2

Expected: works.

---

## STMT-TC-134 — Statement Generation in WebKit

**Priority:** P2

Expected: works.

---

# 42. Responsive Test Cases

## STMT-TC-135 — Statement List at 390×844

**Priority:** P1

Expected:

* Account visible.
* Period visible.
* Generate/download controls accessible.

---

## STMT-TC-136 — Statement Details at 360×800

**Priority:** P1

Expected: opening/closing balances remain readable.

---

## STMT-TC-137 — Transaction Table on Mobile

**Priority:** P1

Expected: financial meaning is not lost when layout adapts.

---

# 43. Accessibility Test Cases

## STMT-TC-138 — Keyboard Statement Generation

**Priority:** P2

Expected: account/date/output controls operable via keyboard.

---

## STMT-TC-139 — Statement Form Labels

**Priority:** P2

Expected: account and date fields have accessible labels.

---

## STMT-TC-140 — Download Control Accessible

**Priority:** P2

Expected: download action available to assistive technology.

---

## STMT-TC-141 — Debit/Credit Not Color-Only

**Priority:** P1

Expected: direction conveyed semantically/textually.

---

# 44. End-to-End Statement Reconciliation

## STMT-TC-142 — Complete Financial Statement Reconciliation

**Priority:** P0

### Test Data

```text
Opening Balance:
25,000.00

Credits:
5,500.00

Debits:
3,500.00

Fees:
20.00
```

Expected:

```text
Closing Balance:
26,980.00
```

### Steps

1. Record authoritative opening balance.
2. Identify all settled activity.
3. Generate statement.
4. Sum credits.
5. Sum debits.
6. Sum fees.
7. Calculate expected closing.
8. Compare statement closing.
9. Compare account cutoff balance.
10. Compare API/DB.
11. Download PDF.
12. Compare PDF.

### Expected Result

All representations equal:

```text
26,980.00
```

---

# 45. End-to-End Historical Consistency

## STMT-TC-143 — Historical Statement Remains Stable

**Priority:** P0

### Steps

1. Generate finalized August statement.
2. Save statement reference/hash or evidence.
3. Perform September transactions.
4. Regenerate/reopen August statement.
5. Compare historical values.

### Expected Result

August financial values remain unchanged except where formal correction/versioning rules explicitly require a revised statement.

---

# 46. End-to-End Statement IDOR Test

## STMT-TC-144 — Customer A Attempts Customer B Statement

**Priority:** P0

Attempt using:

```text
Account ID

Statement ID

Direct URL

Download URL

API

Altered query parameter
```

### Expected Result

All unauthorized requests denied.

No Customer B financial data exposed.

---

# 47. End-to-End Reversal Statement Test

## STMT-TC-145 — Transfer → Reversal → Statement

**Priority:** P0

Expected:

* Original transfer retained.
* Separate reversal represented.
* Net balance reconciles.
* References traceable.

---

# 48. End-to-End Failed Transaction Test

## STMT-TC-146 — Failed Payment/Transfer Does Not Distort Statement

**Priority:** P0

Expected:

```text
Invalid Settled Debit:
0

Invalid Fee:
0

Closing Balance:
Correct
```

---

# 49. End-to-End Large Statement Test

## STMT-TC-147 — High-Volume Statement Completeness

**Priority:** P0

### Preconditions

Account contains a large known transaction dataset.

### Expected Result

```text
Missing Eligible Transactions:
0

Unexpected Duplicate Transactions:
0

Financial Difference:
0.00
```

---

# 50. Statement Risk Mapping

| Risk                                | Related Test Cases                         |
| ----------------------------------- | ------------------------------------------ |
| RISK-001 Incorrect balance          | STMT-TC-023–029, 041–058, 092–116, 142–147 |
| RISK-002 Unauthorized customer data | STMT-TC-004–011, 082–088, 144              |
| RISK-013 Concurrency corruption     | STMT-TC-114–116                            |
| RISK-019 History/balance mismatch   | STMT-TC-030–058, 100–109                   |
| RISK-020 Statement incorrect        | STMT-TC-023–077, 092–147                   |
| RISK-021 Sensitive exposure         | STMT-TC-078–085                            |
| RISK-022 Audit gap                  | STMT-TC-128–130                            |
| RISK-030 Unauthorized API           | STMT-TC-086–091                            |
| RISK-033 Duplicate reference        | STMT-TC-107–109                            |
| RISK-037 Frontend-only validation   | STMT-TC-005–008, 124–127                   |
| RISK-039 API/DB inconsistency       | STMT-TC-092–106                            |
| RISK-043 Timezone issue             | STMT-TC-019–022, 110–113                   |
| RISK-047 IDOR                       | STMT-TC-005–008, 087, 124, 144             |
| RISK-048 UI/backend mismatch        | STMT-TC-098–106                            |

---

# 51. Requirements Mapping

| Requirement                                     | Test Cases               |
| ----------------------------------------------- | ------------------------ |
| REQ-STMT-001 Generate statement                 | STMT-TC-001–003          |
| REQ-STMT-002 Authorization/ownership            | STMT-TC-004–011          |
| REQ-STMT-003 Date ranges                        | STMT-TC-012–022          |
| REQ-STMT-004 Opening balance                    | STMT-TC-023–025          |
| REQ-STMT-005 Closing balance/reconciliation     | STMT-TC-026–029, 055–058 |
| REQ-STMT-006 Transaction/fee/reversal inclusion | STMT-TC-030–058          |
| REQ-STMT-007 Historical consistency             | STMT-TC-059–063, 143     |
| REQ-STMT-008 Secure download/output             | STMT-TC-064–091          |

---

# 52. Smoke Candidates

Recommended statement smoke coverage:

```text
STMT-TC-001
STMT-TC-005
STMT-TC-023
STMT-TC-026
STMT-TC-029
STMT-TC-030
STMT-TC-037
STMT-TC-041
STMT-TC-046
STMT-TC-064
STMT-TC-078
STMT-TC-098
```

---

# 53. Sanity Candidates

After statement changes:

```text
STMT-TC-001
STMT-TC-012
STMT-TC-019
STMT-TC-023
STMT-TC-026
STMT-TC-029
STMT-TC-030
STMT-TC-037
STMT-TC-041
STMT-TC-046
STMT-TC-055
STMT-TC-064
STMT-TC-073
STMT-TC-092
STMT-TC-098
```

---

# 54. Critical Regression Candidates

```text
STMT-TC-001–011

STMT-TC-012–061

STMT-TC-064–130

STMT-TC-135–147
```

---

# 55. UI Automation Candidates

Best suited for:

```text
Playwright

Selenium

Cypress
```

Strong candidates:

```text
STMT-TC-001–029

STMT-TC-030–085

STMT-TC-120–147
```

---

# 56. API Automation Candidates

Best suited for:

```text
REST Assured

Postman
```

Strong candidates:

```text
STMT-TC-004–022

STMT-TC-023–130

STMT-TC-142–147
```

---

# 57. SQL / Database Testing Candidates

Strong SQL candidates:

```text
STMT-TC-019–061

STMT-TC-092–119

STMT-TC-142–147
```

Database validation should verify:

```text
Opening balance source

Closing balance source

Transaction set

Fees

Reversals

Refunds

References

Statement metadata

Account ownership

Historical cutoff
```

---

# 58. Performance / JMeter Candidates

Strong performance candidates:

```text
STMT-TC-075

STMT-TC-114–119

STMT-TC-147
```

Measure:

```text
Statement generation time

PDF generation time

API response time

Concurrent generation throughput

Error rate
```

while also validating:

```text
No missing transactions

No duplicate transactions

No financial mismatch

No authorization leakage
```

---

# 59. Test Evidence Requirements

For critical statement testing, capture as applicable:

```text
Customer ID

Account ID

Statement ID

Period

Opening balance

Credit total

Debit total

Fee total

Closing balance

Transaction count

Transaction references

API response

Database query/result

PDF file reference

Generation timestamp

Audit event

Screenshot

Defect ID
```

Do not include unnecessary real customer financial information.

---

# 60. Statement Defect Examples

Potential Critical/High defects include:

```text
Customer downloads another customer's statement.

Opening balance incorrect.

Closing balance incorrect.

Statement fails reconciliation.

Completed transfer missing.

Failed payment shown as settled debit.

Fee included twice.

Reversal overwrites original transaction.

PDF differs from API statement.

Historical statement changes after later transactions.

Direct statement URL works after logout.

Transaction assigned to wrong month because of timezone.

Large statement silently omits transactions.

Concurrent generation produces inconsistent totals.
```

---

# 61. Statement Release Blockers

Release should normally be blocked by unresolved issues involving:

```text
Statement IDOR

Incorrect opening balance

Incorrect closing balance

Material reconciliation difference

Missing settled financial transaction

Duplicate financial transaction

Incorrect fee with material impact

Historical statement corruption

Sensitive financial-data exposure

PDF/API financial mismatch

Wrong-account statement generation

Critical date-period assignment error
```

---

# 62. Statement Exit Criteria

Statement testing is acceptable when:

```text
Customers can generate only authorized statements.

Date ranges behave correctly.

Opening balances are correct.

Closing balances are correct.

Statements reconcile mathematically.

Eligible transactions are complete.

Failed/pending activity is handled correctly.

Fees appear exactly once.

Reversals/refunds remain traceable.

Historical statements remain consistent.

Downloaded PDFs match authoritative data.

Sensitive information is protected.

UI/API/DB/PDF results agree.

Large statements contain no unexplained omissions or duplicates.

No unresolved Critical/P0 statement defect remains.
```

---

# 63. Final Statement Testing Principle

A bank statement is not merely:

```text
A downloadable PDF.
```

It is a formal representation of the financial history of an account for a defined period.

QA must establish that:

```text
The correct customer requested it.

The correct account was used.

The period is correct.

The opening balance is correct.

Every eligible settled transaction is represented.

No ineligible transaction is counted.

Fees are correct.

Reversals/refunds remain traceable.

The closing balance reconciles.

The PDF matches the underlying data.

Historical values remain trustworthy.
```

The critical statement invariant is:

```text
Opening Balance
+ Credits
- Debits
- Fees
± Valid Adjustments
=
Closing Balance
```

The core rule is:

```text
A Banking System statement must provide a complete,
secure, historically reliable, and mathematically
reconcilable representation of customer financial activity.
```
