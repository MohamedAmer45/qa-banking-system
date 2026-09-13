# Banking System — Statement Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Statements                     |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for account-statement functionality within the Banking System.

Statements are formal financial records and must accurately represent account activity for a selected period.

Defects may result in:

* Incorrect opening balance
* Incorrect closing balance
* Missing transactions
* Duplicate transactions
* Incorrect dates
* Incorrect debit or credit classification
* Incorrect fees
* Incorrect account information
* Unauthorized access to another customer's statements
* Inconsistency between statement, transaction history, API, and database

The goal is to ensure statements are complete, accurate, secure, downloadable, and financially reconcilable.

---

# 3. Scope

Statement testing includes:

* Statement generation
* Account selection
* Statement periods
* Custom date ranges
* Opening balance
* Closing balance
* Credits
* Debits
* Fees
* Transfers
* Payments
* Card transactions
* Loan transactions
* Deposit transactions
* Reversals
* Transaction references
* Statement totals
* PDF/download functionality
* Historical statements
* Empty statements
* Large statements
* Authorization
* Data consistency
* Cross-browser behavior
* Responsive behavior
* Auditability

---

# 4. Scenario Naming Convention

Statement scenarios use:

```text
TS-STMT-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Statement Viewing Scenarios

## TS-STMT-001 — Customer views statements for owned account

**Priority:** P0

Expected:

Only statements belonging to the authenticated customer's account are visible.

---

## TS-STMT-002 — Customer with multiple accounts selects one account

**Priority:** P1

Expected:

Statement data corresponds only to the selected account.

---

## TS-STMT-003 — Customer switches between accounts

**Priority:** P0

Expected:

Account details, transactions, and balances change correctly.

---

## TS-STMT-004 — Customer with no historical statements

**Priority:** P2

Expected:

Appropriate empty state displayed.

---

## TS-STMT-005 — Refresh statement page

**Priority:** P2

Expected:

Statement information remains correct.

---

# 6. Statement Ownership and Authorization

## TS-STMT-006 — Customer attempts to access another customer's statement

**Priority:** P0

Expected:

Access denied.

---

## TS-STMT-007 — Modify statement ID in URL

**Priority:** P0

Expected:

Another customer's statement cannot be viewed.

---

## TS-STMT-008 — Modify account ID while requesting statement

**Priority:** P0

Expected:

Backend ownership validation rejects request.

---

## TS-STMT-009 — Request another customer's statement through API

**Priority:** P0

Expected:

Authorization failure.

---

## TS-STMT-010 — Unauthenticated user accesses statement

**Priority:** P0

Expected:

Authentication required.

---

## TS-STMT-011 — Expired session accesses statement

**Priority:** P0

Expected:

Reauthentication required.

---

## TS-STMT-012 — Customer logs out and reopens statement URL

**Priority:** P0

Expected:

Protected statement data is unavailable.

---

# 7. Standard Statement Period Scenarios

Where preset periods exist.

## TS-STMT-013 — Generate current-month statement

**Priority:** P1

Expected:

Correct current-period activity.

---

## TS-STMT-014 — Generate previous-month statement

**Priority:** P1

Expected:

Correct historical activity.

---

## TS-STMT-015 — Generate three-month statement

**Priority:** P1

Expected:

Correct transactions for full period.

---

## TS-STMT-016 — Generate six-month statement

**Priority:** P1

Expected:

Correct period.

---

## TS-STMT-017 — Generate annual statement

**Priority:** P1

Expected:

Correct yearly activity.

---

# 8. Custom Date Range Scenarios

## TS-STMT-018 — Generate statement with valid custom date range

**Priority:** P0

Expected:

Only transactions within the defined period are included.

---

## TS-STMT-019 — Start date equals end date

**Priority:** P1

Expected:

Single-day statement generated correctly.

---

## TS-STMT-020 — Start date before first account transaction

**Priority:** P2

Expected:

Statement begins with correct opening balance for requested period.

---

## TS-STMT-021 — End date after latest transaction

**Priority:** P2

Expected:

No fabricated future activity.

---

## TS-STMT-022 — Start date after end date

**Priority:** P1

Expected:

Validation error.

---

## TS-STMT-023 — Future-only date range

**Priority:** P2

Expected:

Empty result or validation according to requirements.

---

## TS-STMT-024 — Date range exceeds maximum supported period

**Priority:** P1

Expected:

Rejected or handled according to configured limit.

---

# 9. Date Boundary Scenarios

## TS-STMT-025 — Transaction exactly at start-date boundary

**Priority:** P0

Expected:

Included according to defined inclusive/exclusive semantics.

---

## TS-STMT-026 — Transaction exactly at end-date boundary

**Priority:** P0

Expected:

Included according to requirements.

---

## TS-STMT-027 — Transaction one moment before start boundary

**Priority:** P1

Expected:

Excluded.

---

## TS-STMT-028 — Transaction immediately after end boundary

**Priority:** P1

Expected:

Excluded.

---

## TS-STMT-029 — Statement across month boundary

**Priority:** P1

Expected:

Correct activity.

---

## TS-STMT-030 — Statement across year boundary

**Priority:** P1

Expected:

Correct activity.

---

## TS-STMT-031 — Statement period including leap day

**Priority:** P1

Expected:

Correct date handling.

---

# 10. Opening Balance Scenarios

## TS-STMT-032 — Opening balance displayed correctly

**Priority:** P0

Expected:

Opening balance equals account balance immediately before statement period begins.

---

## TS-STMT-033 — Opening balance with no prior transactions

**Priority:** P1

Expected:

Correct initial account balance.

---

## TS-STMT-034 — Opening balance after previous-period activity

**Priority:** P0

Expected:

Prior transactions are incorporated correctly.

---

## TS-STMT-035 — Opening balance matches previous statement closing balance

**Priority:** P0

Expected:

For consecutive periods:

```text
Previous Closing Balance = Current Opening Balance
```

unless defined adjustments exist between statements.

---

# 11. Closing Balance Scenarios

## TS-STMT-036 — Closing balance calculated correctly

**Priority:** P0

Expected:

Correct ending financial value.

---

## TS-STMT-037 — Closing balance equals account state at period end

**Priority:** P0

Expected:

Financial reconciliation succeeds.

---

## TS-STMT-038 — Closing balance for no-transaction period

**Priority:** P1

Expected:

Closing balance equals opening balance unless other valid adjustments exist.

---

## TS-STMT-039 — Latest-period closing balance matches account balance

**Priority:** P0

Expected:

Correct where statement ends at current account state.

---

# 12. Statement Reconciliation

## TS-STMT-040 — Opening balance plus credits minus debits reconciles to closing balance

**Priority:** P0

Expected:

```text
Opening Balance
+ Total Credits
- Total Debits
= Closing Balance
```

when fees are already included as debits.

---

## TS-STMT-041 — Reconcile statement transaction-by-transaction

**Priority:** P0

Expected:

Every balance movement can be explained.

---

## TS-STMT-042 — Reconcile statement with transaction history

**Priority:** P0

Expected:

All qualifying financial transactions appear exactly once.

---

## TS-STMT-043 — Reconcile statement with database

**Priority:** P0

Expected:

Amounts, dates, references, and statuses are consistent.

---

# 13. Credit Scenarios

## TS-STMT-044 — Incoming transfer appears as credit

**Priority:** P0

Expected:

Correct amount and reference.

---

## TS-STMT-045 — Loan disbursement appears as credit

**Priority:** P0

Expected:

Correct amount.

---

## TS-STMT-046 — Deposit maturity payout appears as credit

**Priority:** P0

Expected:

Correct.

---

## TS-STMT-047 — Card refund appears as credit

**Priority:** P0

Expected:

Correct refunded value.

---

## TS-STMT-048 — Transaction reversal credit appears correctly

**Priority:** P0

Expected:

Correct relationship to original debit.

---

# 14. Debit Scenarios

## TS-STMT-049 — Outgoing transfer appears as debit

**Priority:** P0

Expected:

Correct amount.

---

## TS-STMT-050 — Bill payment appears as debit

**Priority:** P0

Expected:

Correct.

---

## TS-STMT-051 — Card purchase appears as debit

**Priority:** P0

Expected:

Correct merchant and amount.

---

## TS-STMT-052 — Loan repayment appears as debit

**Priority:** P0

Expected:

Correct.

---

## TS-STMT-053 — Deposit funding appears as debit

**Priority:** P0

Expected:

Correct principal.

---

# 15. Fee Scenarios

## TS-STMT-054 — Transfer fee appears correctly

**Priority:** P1

Expected:

Correct fee amount.

---

## TS-STMT-055 — Payment fee appears correctly

**Priority:** P1

Expected:

Correct.

---

## TS-STMT-056 — Loan fee appears correctly

**Priority:** P1

Expected:

Correct.

---

## TS-STMT-057 — Deposit penalty appears correctly

**Priority:** P1

Expected:

Correct.

---

## TS-STMT-058 — Fee is not duplicated

**Priority:** P0

Expected:

Fee applied exactly once.

---

# 16. Transaction Inclusion Scenarios

## TS-STMT-059 — Completed transaction included in statement

**Priority:** P0

Expected:

Included.

---

## TS-STMT-060 — Failed transaction not represented as completed financial debit/credit

**Priority:** P0

Expected:

No incorrect financial impact.

---

## TS-STMT-061 — Cancelled transaction does not appear as completed movement

**Priority:** P0

Expected:

Correct.

---

## TS-STMT-062 — Pending transaction inclusion follows statement rules

**Priority:** P0

Expected:

Pending items are either excluded or clearly identified according to design.

---

## TS-STMT-063 — Reversed transaction represented correctly

**Priority:** P0

Expected:

Original and reversal remain auditable.

---

# 17. Missing Transaction Scenarios

## TS-STMT-064 — Completed transfer is present

**Priority:** P0

Expected:

No missing transaction.

---

## TS-STMT-065 — Completed payment is present

**Priority:** P0

Expected:

Present.

---

## TS-STMT-066 — Settled card purchase is present

**Priority:** P0

Expected:

Present.

---

## TS-STMT-067 — Loan repayment is present

**Priority:** P0

Expected:

Present.

---

## TS-STMT-068 — Deposit maturity is present

**Priority:** P0

Expected:

Present.

---

# 18. Duplicate Transaction Scenarios

## TS-STMT-069 — Single transfer appears only once

**Priority:** P0

Expected:

No duplicate row.

---

## TS-STMT-070 — Single payment appears only once

**Priority:** P0

Expected:

No duplication.

---

## TS-STMT-071 — Statement regeneration does not duplicate activity

**Priority:** P0

Expected:

Same correct result.

---

## TS-STMT-072 — Reversal does not incorrectly duplicate original amount

**Priority:** P0

Expected:

Original and reversal represented separately and correctly.

---

# 19. Transaction Reference Scenarios

## TS-STMT-073 — Statement displays transaction reference where required

**Priority:** P1

Expected:

Reference is correct.

---

## TS-STMT-074 — Statement reference matches transaction history

**Priority:** P0

Expected:

Same identifier.

---

## TS-STMT-075 — Statement reference maps to correct database transaction

**Priority:** P0

Expected:

Traceable.

---

# 20. Transaction Description Scenarios

## TS-STMT-076 — Transfer description displayed correctly

**Priority:** P1

Expected:

Correct beneficiary/transfer context according to privacy rules.

---

## TS-STMT-077 — Payment description identifies payee correctly

**Priority:** P1

Expected:

Correct.

---

## TS-STMT-078 — Card transaction description identifies merchant

**Priority:** P1

Expected:

Correct merchant information.

---

## TS-STMT-079 — Loan transaction description clear

**Priority:** P2

Expected:

Customer can identify activity.

---

## TS-STMT-080 — Deposit transaction description clear

**Priority:** P2

Expected:

Correct product context.

---

# 21. Statement Totals Scenarios

Where statements provide totals.

## TS-STMT-081 — Total credits calculated correctly

**Priority:** P0

Expected:

Sum of included credits is exact.

---

## TS-STMT-082 — Total debits calculated correctly

**Priority:** P0

Expected:

Sum of included debits is exact.

---

## TS-STMT-083 — Total fees calculated correctly

**Priority:** P1

Expected:

Correct.

---

## TS-STMT-084 — Total transaction count correct

**Priority:** P1

Expected:

Matches included transactions.

---

# 22. Financial Precision Scenarios

## TS-STMT-085 — Statement values preserve decimal precision

**Priority:** P0

Expected:

No floating-point artifacts.

---

## TS-STMT-086 — Small decimal transaction displayed correctly

**Priority:** P1

Example:

```text
0.01
```

---

## TS-STMT-087 — Large transaction displayed correctly

**Priority:** P1

Expected:

No truncation.

---

## TS-STMT-088 — Reconciliation produces exact financial result

**Priority:** P0

Expected:

No unexplained residual due to rounding.

---

# 23. Historical Statement Scenarios

## TS-STMT-089 — Customer views old statement

**Priority:** P1

Expected:

Historical data remains unchanged.

---

## TS-STMT-090 — Closed account historical statement

**Priority:** P1

Expected:

Accessible where allowed.

---

## TS-STMT-091 — Historical statement remains unchanged after account alias change

**Priority:** P1

Expected:

Financial history preserved.

---

## TS-STMT-092 — Historical statement remains correct after beneficiary deletion

**Priority:** P1

Expected:

Past financial activity remains traceable.

---

# 24. Statement Immutability Scenarios

## TS-STMT-093 — Customer cannot edit statement contents

**Priority:** P0

Expected:

Financial statement is read-only.

---

## TS-STMT-094 — Customer cannot delete historical statement record

**Priority:** P0

Expected:

Rejected.

---

## TS-STMT-095 — Current application changes do not modify previously generated historical financial values

**Priority:** P0

Expected:

Historical accuracy preserved.

---

# 25. PDF / Download Scenarios

Where downloadable statements are supported.

## TS-STMT-096 — Download statement as PDF

**Priority:** P1

Expected:

Valid file generated.

---

## TS-STMT-097 — Downloaded PDF belongs to selected account

**Priority:** P0

Expected:

No cross-account data.

---

## TS-STMT-098 — Downloaded statement period matches requested range

**Priority:** P0

Expected:

Correct period.

---

## TS-STMT-099 — PDF opening balance matches UI

**Priority:** P0

Expected:

Consistent.

---

## TS-STMT-100 — PDF closing balance matches UI

**Priority:** P0

Expected:

Consistent.

---

## TS-STMT-101 — PDF transaction list matches UI

**Priority:** P0

Expected:

No missing or additional transactions.

---

## TS-STMT-102 — PDF transaction amounts and references match

**Priority:** P0

Expected:

Exact consistency.

---

## TS-STMT-103 — PDF filename is meaningful

**Priority:** P3

Example:

```text
statement-ACC-001-2026-08.pdf
```

Sensitive information should not be unnecessarily exposed in the filename.

---

# 26. Export Security Scenarios

## TS-STMT-104 — Download another customer's statement via modified request

**Priority:** P0

Expected:

Denied.

---

## TS-STMT-105 — Statement download link requires authorization

**Priority:** P0

Expected:

Unauthenticated direct download fails.

---

## TS-STMT-106 — Expired session uses old statement download link

**Priority:** P0

Expected:

Behavior follows secure access rules.

---

## TS-STMT-107 — Generated PDF does not expose hidden internal fields

**Priority:** P0

Expected:

No sensitive backend/security metadata.

---

# 27. Large Statement Scenarios

## TS-STMT-108 — Generate statement with hundreds of transactions

**Priority:** P1

Expected:

Complete output.

---

## TS-STMT-109 — Generate statement with thousands of transactions

**Priority:** P1

Expected:

No missing or duplicate transactions.

---

## TS-STMT-110 — Large PDF renders all pages correctly

**Priority:** P1

Expected:

No cut-off financial rows.

---

## TS-STMT-111 — Page breaks do not split financial values incorrectly

**Priority:** P2

Expected:

Statement remains readable.

---

## TS-STMT-112 — Large statement totals remain correct

**Priority:** P0

Expected:

Exact reconciliation.

---

# 28. Empty Statement Scenarios

## TS-STMT-113 — Generate statement for period with no transactions

**Priority:** P1

Expected:

Statement may show:

* Account information
* Opening balance
* Closing balance
* No transaction activity

---

## TS-STMT-114 — Empty statement opening and closing balances match

**Priority:** P0

Expected:

Equal unless valid non-transaction adjustments exist.

---

## TS-STMT-115 — Empty statement does not display fabricated transaction rows

**Priority:** P0

Expected:

No fake data.

---

# 29. Currency Scenarios

## TS-STMT-116 — Statement currency matches account currency

**Priority:** P0

Expected:

Correct.

---

## TS-STMT-117 — Currency shown on all relevant financial totals

**Priority:** P1

Expected:

No ambiguity.

---

## TS-STMT-118 — Multi-currency account/transaction representation

**Priority:** P1

Where supported.

Expected:

Original and converted values follow defined rules.

---

# 30. Sorting Scenarios

## TS-STMT-119 — Transactions ordered by date/time correctly

**Priority:** P1

Expected:

Defined ordering preserved.

---

## TS-STMT-120 — Transactions with same timestamp use stable secondary ordering

**Priority:** P1

Expected:

Deterministic order.

---

## TS-STMT-121 — PDF transaction order matches expected statement order

**Priority:** P1

Expected:

Consistent with UI/requirements.

---

# 31. API Consistency Scenarios

## TS-STMT-122 — Statement API returns selected account data

**Priority:** P0

Expected:

Correct ownership and period.

---

## TS-STMT-123 — Statement API rejects another customer's account

**Priority:** P0

Expected:

Authorization failure.

---

## TS-STMT-124 — API opening balance matches UI

**Priority:** P0

Expected:

Same amount.

---

## TS-STMT-125 — API closing balance matches UI

**Priority:** P0

Expected:

Same amount.

---

## TS-STMT-126 — API transaction set matches statement UI

**Priority:** P0

Expected:

Same financial records.

---

# 32. Database Validation Scenarios

## TS-STMT-127 — Statement transactions match database financial records

**Priority:** P0

Expected:

Correct qualifying records.

---

## TS-STMT-128 — Opening balance can be reconciled using historical account data

**Priority:** P0

Expected:

Correct.

---

## TS-STMT-129 — Closing balance can be reconciled from database activity

**Priority:** P0

Expected:

Correct.

---

## TS-STMT-130 — No transaction included twice because of joins/query logic

**Priority:** P0

Expected:

No duplicate database-driven statement rows.

---

## TS-STMT-131 — Reversal relationships represented correctly from persisted data

**Priority:** P0

Expected:

Correct.

---

# 33. Error Handling Scenarios

## TS-STMT-132 — Statement service unavailable

**Priority:** P1

Expected:

Safe error message.

---

## TS-STMT-133 — Statement generation fails

**Priority:** P1

Expected:

No corrupt or partial statement presented as complete.

---

## TS-STMT-134 — PDF generation service fails

**Priority:** P1

Expected:

Clear failure without incorrect download.

---

## TS-STMT-135 — Network disconnect during statement load

**Priority:** P2

Expected:

Graceful retry/error handling.

---

## TS-STMT-136 — Network disconnect during download

**Priority:** P2

Expected:

Corrupt incomplete download is not presented as successful where detectable.

---

# 34. Sensitive Data Scenarios

## TS-STMT-137 — Card numbers masked in statement

**Priority:** P0

Expected:

No unnecessary full PAN exposure.

---

## TS-STMT-138 — Statement does not expose authentication credentials

**Priority:** P0

Expected:

No tokens, passwords, or OTP data.

---

## TS-STMT-139 — Statement does not expose another customer's private data

**Priority:** P0

Expected:

Only permitted counterparty information appears.

---

## TS-STMT-140 — Statement error does not expose internal database details

**Priority:** P1

Expected:

Safe error.

---

# 35. Audit Scenarios

## TS-STMT-141 — Statement generation audited where required

**Priority:** P2

Expected:

Customer/account/period/action traceable.

---

## TS-STMT-142 — Statement download audited where required

**Priority:** P2

Expected:

Access traceable.

---

## TS-STMT-143 — Administrative statement access audited

**Priority:** P1

Expected:

Actor and customer/account recorded.

---

## TS-STMT-144 — Unauthorized statement access attempt recorded where required

**Priority:** P2

Expected:

Security event traceable.

---

# 36. Admin Statement Scenarios

## TS-STMT-145 — Authorized admin views customer statement

**Priority:** P1

Expected:

Access limited to authorized role.

---

## TS-STMT-146 — Limited admin attempts restricted statement access

**Priority:** P0

Expected:

Denied.

---

## TS-STMT-147 — Admin downloads customer statement

**Priority:** P1

Expected:

Authorization and audit rules enforced.

---

## TS-STMT-148 — Customer attempts admin statement endpoint

**Priority:** P0

Expected:

Denied.

---

# 37. Concurrency Scenarios

## TS-STMT-149 — Generate statement while new transaction is completing

**Priority:** P1

Expected:

Statement uses consistent cutoff semantics.

---

## TS-STMT-150 — Generate same statement simultaneously from two sessions

**Priority:** P2

Expected:

Both outputs are financially identical for same cutoff.

---

## TS-STMT-151 — Transaction reverses during statement generation

**Priority:** P0

Expected:

Statement represents one consistent valid financial state.

---

## TS-STMT-152 — Account closes while statement generation is in progress

**Priority:** P1

Expected:

Historical financial data remains available and valid.

---

# 38. Accessibility and Usability Scenarios

## TS-STMT-153 — Opening and closing balances clearly labeled

**Priority:** P0

Expected:

No ambiguity.

---

## TS-STMT-154 — Debit and credit columns clearly distinguishable

**Priority:** P1

Expected:

Not dependent solely on color.

---

## TS-STMT-155 — Date range clearly shown

**Priority:** P1

Expected:

Customer understands statement period.

---

## TS-STMT-156 — Account identity clearly shown

**Priority:** P1

Expected:

Customer knows which account statement belongs to.

---

## TS-STMT-157 — Download control accessible by keyboard

**Priority:** P2

Expected:

Usable.

---

# 39. Responsive Scenarios

## TS-STMT-158 — Statement page on desktop

**Priority:** P2

Expected:

All relevant fields readable.

---

## TS-STMT-159 — Statement page on tablet

**Priority:** P2

Expected:

Usable.

---

## TS-STMT-160 — Statement view on mobile

**Priority:** P1

Expected:

Balances, dates, and transaction amounts remain accessible.

---

## TS-STMT-161 — Long transaction description on mobile

**Priority:** P2

Expected:

Does not hide amount or status.

---

## TS-STMT-162 — Download action available on mobile

**Priority:** P2

Expected:

Accessible.

---

# 40. Cross-Browser Scenarios

## TS-STMT-163 — Statement generation in Chrome

**Priority:** P1

Expected:

Works correctly.

---

## TS-STMT-164 — Statement generation in Edge

**Priority:** P1

Expected:

Works correctly.

---

## TS-STMT-165 — Statement generation in Firefox

**Priority:** P1

Expected:

Works correctly.

---

## TS-STMT-166 — PDF download across supported browsers

**Priority:** P1

Expected:

Consistent functionality.

---

# 41. Boundary Scenarios

## TS-STMT-167 — Earliest allowed statement date

**Priority:** P1

Expected:

Handled correctly.

---

## TS-STMT-168 — One day before earliest supported statement date

**Priority:** P2

Expected:

Rejected or clamped according to requirements.

---

## TS-STMT-169 — Maximum statement date range exactly

**Priority:** P1

Expected:

Accepted.

---

## TS-STMT-170 — Maximum statement range plus one day

**Priority:** P1

Expected:

Rejected where limit exists.

---

## TS-STMT-171 — Exactly one transaction in statement period

**Priority:** P1

Expected:

Correct opening/transaction/closing reconciliation.

---

## TS-STMT-172 — Exactly one more transaction than single-page PDF capacity

**Priority:** P2

Expected:

Correct page break without missing row.

---

# 42. End-to-End Statement Scenarios

## TS-STMT-173 — Transfer-to-statement reconciliation

**Priority:** P0

Flow:

```text
Record Opening Balance
→ Perform Transfer
→ Verify Transaction History
→ Generate Statement Covering Transfer
→ Verify Transfer Appears Once
→ Verify Amount and Fee
→ Verify Closing Balance
```

---

## TS-STMT-174 — Payment-to-statement reconciliation

**Priority:** P0

Flow:

```text
Complete Payment
→ Verify Account Balance
→ Verify Transaction History
→ Generate Statement
→ Verify Payment Debit
→ Verify Fee
→ Verify Closing Balance
```

---

## TS-STMT-175 — Card purchase and refund statement journey

**Priority:** P0

Flow:

```text
Complete Card Purchase
→ Settle Purchase
→ Process Refund
→ Generate Statement
→ Verify Purchase Debit
→ Verify Refund Credit
→ Verify Final Balance
```

---

## TS-STMT-176 — Loan lifecycle statement journey

**Priority:** P0

Flow:

```text
Disburse Loan
→ Verify Credit
→ Repay Installment
→ Verify Debit
→ Generate Statement
→ Verify Both Transactions
→ Reconcile Closing Balance
```

---

## TS-STMT-177 — Deposit lifecycle statement journey

**Priority:** P0

Flow:

```text
Open Deposit
→ Verify Principal Debit
→ Reach Maturity
→ Verify Payout Credit
→ Generate Statement
→ Verify Deposit Transactions
→ Reconcile Balance
```

---

## TS-STMT-178 — Reversal statement journey

**Priority:** P0

Flow:

```text
Complete Transaction
→ Reverse Transaction
→ Generate Statement
→ Verify Original Transaction
→ Verify Reversal
→ Verify Net Financial Effect
→ Verify Closing Balance
```

---

# 43. Critical Smoke Scenarios

Statement smoke coverage should include:

```text
TS-STMT-001 — View own statement
TS-STMT-006 — Cannot access another customer's statement
TS-STMT-018 — Generate valid date range
TS-STMT-032 — Opening balance correct
TS-STMT-036 — Closing balance correct
TS-STMT-040 — Statement reconciles
TS-STMT-042 — Matches transaction history
TS-STMT-096 — Download statement
```

---

# 44. Critical Regression Scenarios

Always prioritize:

* Statement ownership
* Account selection
* Date ranges
* Opening balance
* Closing balance
* Transaction inclusion
* Missing transactions
* Duplicate transactions
* Debit/credit accuracy
* Fees
* Reversals
* Reference consistency
* Financial reconciliation
* Transaction-history consistency
* PDF/download consistency
* Large statements
* API/database consistency
* Authorization

---

# 45. Automation Candidates

Strong UI automation candidates:

* Generate current-month statement
* Generate custom-range statement
* Validate account selection
* Validate opening/closing balances
* Verify transaction inclusion
* Verify filters/date validation
* Download statement
* Unauthorized statement access

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 46. API Automation Candidates

Postman and REST Assured should later cover:

* Statement generation endpoint
* Custom date ranges
* Invalid ranges
* Account ownership
* Unauthorized access
* Opening balance
* Closing balance
* Transaction list
* Statement download metadata

---

# 47. SQL Validation Candidates

Database testing should validate:

* Statement-period qualifying transactions
* Account ownership
* Opening balance derivation
* Closing balance calculation
* Transaction references
* Reversals
* Fee records
* No duplicates
* Correct timestamps
* Financial reconciliation

---

# 48. Performance Testing Candidates

JMeter may later cover:

* Statement generation under concurrent users
* Large statement generation
* Historical statement retrieval
* PDF generation load
* Statement download throughput

Performance degradation must not lead to incomplete or inconsistent financial statements.

---

# 49. BDD Candidates

Example:

```gherkin
Feature: Account statement

Scenario: Statement closing balance reconciles with account activity
  Given an account has a known opening balance
  And financial transactions occur during the statement period
  When the customer generates the statement
  Then every completed financial transaction in the period should appear exactly once
  And the opening balance should be correct
  And the total credits and debits should be correct
  And the closing balance should reconcile
```

Authorization example:

```gherkin
Scenario: Customer cannot access another customer's statement
  Given Customer A is logged in
  And a statement belongs to Customer B
  When Customer A requests Customer B's statement
  Then access should be denied
```

---

# 50. Risk Traceability

Major related risks include:

```text
RISK-001 — Incorrect account balance
RISK-002 — Unauthorized customer data access
RISK-019 — Transaction history mismatch
RISK-020 — Statement contains incorrect transactions
RISK-021 — Sensitive information exposure
RISK-030 — Unauthorized API request
RISK-039 — API/database inconsistency
RISK-047 — Insecure direct object access
```

---

# 51. Statement Coverage Summary

This catalog covers:

* Statement viewing
* Ownership
* Authorization
* Standard periods
* Custom ranges
* Date boundaries
* Opening balances
* Closing balances
* Financial reconciliation
* Credits
* Debits
* Fees
* Transaction inclusion
* Missing transactions
* Duplicate transactions
* References
* Descriptions
* Totals
* Financial precision
* Historical statements
* Immutability
* PDF/downloads
* Export security
* Large statements
* Empty statements
* Currency
* Sorting
* API consistency
* Database validation
* Error handling
* Sensitive data
* Audit
* Admin access
* Concurrency
* Accessibility
* Responsive behavior
* Cross-browser behavior
* Boundary testing
* End-to-end reconciliation

---

# 52. Final Statement Testing Principle

A bank statement is a formal representation of financial activity and must be fully reconcilable.

For every critical statement, QA should be able to answer:

```text
Does this statement belong to the correct customer and account?

Is the requested period correct?

Is the opening balance correct?

Is every valid financial transaction included exactly once?

Are failed and cancelled transactions represented correctly?

Are debits and credits classified correctly?

Are fees represented correctly?

Are reversals traceable?

Is the closing balance mathematically correct?

Does the statement match transaction history?

Does it match the API and database?

Does the downloaded PDF contain the same financial information?

Can unauthorized users access or download it?
```

A statement should be considered correct only when its full financial contents reconcile with the account's authoritative transaction history and balance.
