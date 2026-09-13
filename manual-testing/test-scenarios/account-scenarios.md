# Banking System — Account Management Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Account Management             |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for bank-account management within the Banking System.

Account management is a critical-risk area because account state, ownership, and balance information directly affect:

* Transfers
* Payments
* Cards
* Statements
* Loans
* Deposits
* Transaction history
* Customer restrictions
* Administrative operations

The scenarios in this document focus on correctness, ownership, financial integrity, authorization, account lifecycle, and state consistency.

---

# 3. Scope

Account-management testing includes:

* Account creation
* Account retrieval
* Account ownership
* Account types
* Current balance
* Available balance
* Multiple accounts
* Account status
* Account freezing
* Account unfreezing
* Account restriction
* Account suspension
* Account closure
* Account reopening where supported
* Account limits
* Account transaction eligibility
* Administrative account controls
* Balance integrity
* Data consistency
* Audit logging
* Notifications
* Concurrency

---

# 4. Scenario Naming Convention

Account scenarios use:

```text
TS-ACC-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Account Viewing Scenarios

## TS-ACC-001 — Customer views own active account

**Priority:** P0

Expected:

* Correct account is displayed.
* Account belongs to authenticated customer.
* Correct account type and status are shown.

---

## TS-ACC-002 — Customer views account details

**Priority:** P0

Verify:

* Account alias
* Account number or masked account number
* Account type
* Currency
* Current balance
* Available balance
* Status

Expected:

Values match stored account information.

---

## TS-ACC-003 — Customer with multiple accounts views all owned accounts

**Priority:** P1

Expected:

All owned accounts appear and no foreign account appears.

---

## TS-ACC-004 — Customer switches between own accounts

**Priority:** P1

Expected:

Displayed balances, transactions, and account details correspond to the selected account.

---

## TS-ACC-005 — Customer with one account sees only one account

**Priority:** P2

Expected:

No duplicate or unrelated account records.

---

## TS-ACC-006 — Customer with no eligible accounts views dashboard

**Priority:** P2

Expected:

Appropriate empty-state behavior is displayed.

---

## TS-ACC-007 — Refresh account-details page

**Priority:** P1

Expected:

Latest persisted account information remains correct.

---

## TS-ACC-008 — Open same account in multiple browser tabs

**Priority:** P2

Expected:

All tabs eventually reflect the same valid account state.

---

# 6. Account Ownership and Authorization Scenarios

## TS-ACC-009 — Customer attempts to view another customer's account

**Priority:** P0

Expected:

Access denied.

---

## TS-ACC-010 — Modify account ID in URL

**Priority:** P0

Example:

```text
/accounts/ACC-001
```

changed to:

```text
/accounts/ACC-002
```

where `ACC-002` belongs to another customer.

Expected:

Access denied.

---

## TS-ACC-011 — Modify account ID in API request

**Priority:** P0

Expected:

Backend rejects access to another customer's account.

---

## TS-ACC-012 — Customer accesses another customer's account balance endpoint

**Priority:** P0

Expected:

No balance information is returned.

---

## TS-ACC-013 — Customer attempts to modify another customer's account alias

**Priority:** P0

Expected:

Rejected.

---

## TS-ACC-014 — Unauthenticated user attempts account access

**Priority:** P0

Expected:

Authentication required.

---

## TS-ACC-015 — Expired session attempts account access

**Priority:** P0

Expected:

Reauthentication required.

---

## TS-ACC-016 — Logged-out user revisits account URL

**Priority:** P0

Expected:

Protected data cannot be accessed.

---

## TS-ACC-017 — Customer A logs out and Customer B logs in

**Priority:** P0

Expected:

No account information from Customer A remains visible.

---

# 7. Account Creation Scenarios

Execute where account creation is supported.

## TS-ACC-018 — Authorized customer/admin creates valid account

**Priority:** P1

Expected:

Account is created with valid default state.

---

## TS-ACC-019 — Create checking account

**Priority:** P1

Expected:

Account type is stored correctly.

---

## TS-ACC-020 — Create savings account

**Priority:** P1

Expected:

Savings-specific rules apply.

---

## TS-ACC-021 — Create account with supported currency

**Priority:** P1

Expected:

Account is created successfully.

---

## TS-ACC-022 — Create account with unsupported currency

**Priority:** P1

Expected:

Rejected.

---

## TS-ACC-023 — Create account for nonexistent customer

**Priority:** P0

Expected:

Rejected.

---

## TS-ACC-024 — Unauthorized customer attempts administrative account creation

**Priority:** P0

Expected:

Access denied.

---

## TS-ACC-025 — Limited admin attempts account creation without permission

**Priority:** P0

Expected:

Access denied.

---

## TS-ACC-026 — Submit account-creation request twice rapidly

**Priority:** P1

Expected:

Duplicate accounts are not unintentionally created.

---

## TS-ACC-027 — Refresh after account-creation submission

**Priority:** P2

Expected:

Refresh does not create duplicate account.

---

# 8. Account Number Scenarios

## TS-ACC-028 — Newly created account receives unique account number

**Priority:** P0

Expected:

No duplicate account identifier exists.

---

## TS-ACC-029 — Account number displayed in correct format

**Priority:** P2

Expected:

Format follows requirements.

---

## TS-ACC-030 — Account number masked where required

**Priority:** P1

Example:

```text
******1234
```

Expected:

Sensitive information exposure follows business and security requirements.

---

## TS-ACC-031 — Account number remains stable after profile/account updates

**Priority:** P1

Expected:

Immutable identifiers do not change unexpectedly.

---

## TS-ACC-032 — Attempt to manually modify account number

**Priority:** P0

Expected:

Rejected where account number is immutable.

---

# 9. Account Type Scenarios

## TS-ACC-033 — Checking account displays checking-specific behavior

**Priority:** P2

Expected:

Rules correspond to checking account.

---

## TS-ACC-034 — Savings account displays savings-specific behavior

**Priority:** P2

Expected:

Rules correspond to savings account.

---

## TS-ACC-035 — Account type remains unchanged after unrelated profile update

**Priority:** P2

Expected:

No unintended modification.

---

## TS-ACC-036 — Unauthorized account-type change attempt

**Priority:** P0

Expected:

Rejected.

---

# 10. Balance Display Scenarios

## TS-ACC-037 — Display current balance correctly

**Priority:** P0

Expected:

Displayed current balance matches backend/database state.

---

## TS-ACC-038 — Display available balance correctly

**Priority:** P0

Expected:

Available balance reflects pending holds and applicable rules.

---

## TS-ACC-039 — Current balance equals available balance when no holds exist

**Priority:** P1

Expected:

Values match if no pending restrictions apply.

---

## TS-ACC-040 — Available balance differs from current balance when funds are held

**Priority:** P0

Expected:

Difference is correct and explainable.

---

## TS-ACC-041 — Zero balance displays correctly

**Priority:** P1

Expected:

```text
0.00
```

or configured currency equivalent is displayed accurately.

---

## TS-ACC-042 — Very small positive balance displays correctly

**Priority:** P1

Example:

```text
0.01
```

Expected:

Precision is preserved.

---

## TS-ACC-043 — Large balance displays correctly

**Priority:** P1

Expected:

No truncation or precision loss.

---

## TS-ACC-044 — Balance currency displayed correctly

**Priority:** P1

Expected:

Currency matches account configuration.

---

## TS-ACC-045 — Negative balance handling

**Priority:** P0

Execute where overdraft exists or negative balance is otherwise possible.

Expected:

Representation and business rules are correct.

---

# 11. Balance Integrity Scenarios

## TS-ACC-046 — Successful incoming transfer increases balance correctly

**Priority:** P0

Expected:

Destination account increases by correct amount.

---

## TS-ACC-047 — Successful outgoing transfer decreases balance correctly

**Priority:** P0

Expected:

Source account decreases by transfer amount plus applicable fees.

---

## TS-ACC-048 — Failed transfer leaves balance unchanged

**Priority:** P0

Expected:

No incorrect debit or credit.

---

## TS-ACC-049 — Cancelled transfer leaves balance unchanged where not yet executed

**Priority:** P0

Expected:

No completed financial effect.

---

## TS-ACC-050 — Reversed transaction restores balance correctly

**Priority:** P0

Expected:

Balance reflects reversal rules accurately.

---

## TS-ACC-051 — Successful payment decreases balance correctly

**Priority:** P0

Expected:

Correct debit.

---

## TS-ACC-052 — Failed payment leaves balance unchanged

**Priority:** P0

Expected:

No false debit.

---

## TS-ACC-053 — Fee is deducted exactly once

**Priority:** P0

Expected:

No duplicate fee.

---

## TS-ACC-054 — Incoming credit does not accidentally apply fee to destination when not required

**Priority:** P1

Expected:

Destination receives correct amount.

---

## TS-ACC-055 — Multiple sequential transactions produce correct final balance

**Priority:** P0

Example:

```text
Starting Balance = 10,000.00
Transfer = -1,000.00
Fee = -10.00
Incoming Credit = +500.00

Expected Balance = 9,490.00
```

---

# 12. Financial Precision Scenarios

## TS-ACC-056 — Balance supports two-decimal monetary values correctly

**Priority:** P0

Example:

```text
100.55
```

Expected:

No floating-point artifact.

---

## TS-ACC-057 — Add multiple decimal credits

**Priority:** P0

Example:

```text
0.10
+ 0.20
```

Expected:

Financial result remains exact according to defined decimal precision.

---

## TS-ACC-058 — Deduct decimal fee

**Priority:** P0

Expected:

Precise amount is deducted.

---

## TS-ACC-059 — Amount with too many decimal places

**Priority:** P1

Example:

```text
10.999
```

Expected:

Rejected or rounded strictly according to business rules.

---

## TS-ACC-060 — Repeated small-value transactions

**Priority:** P1

Expected:

No cumulative precision drift.

---

# 13. Account Status Scenarios

Potential states:

```text
ACTIVE
FROZEN
RESTRICTED
SUSPENDED
CLOSED
```

---

## TS-ACC-061 — Active account supports allowed transactions

**Priority:** P0

Expected:

Normal financial operations are permitted.

---

## TS-ACC-062 — Frozen account can be viewed

**Priority:** P1

Expected:

Read-only access follows requirements.

---

## TS-ACC-063 — Frozen account attempts outgoing transfer

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-ACC-064 — Frozen account attempts payment

**Priority:** P0

Expected:

Payment rejected where freeze blocks debit operations.

---

## TS-ACC-065 — Frozen account receives incoming funds

**Priority:** P1

Expected:

Behavior follows defined freeze rules.

---

## TS-ACC-066 — Restricted account attempts prohibited operation

**Priority:** P0

Expected:

Rejected.

---

## TS-ACC-067 — Suspended account attempts transaction

**Priority:** P0

Expected:

Rejected according to policy.

---

## TS-ACC-068 — Closed account attempts outgoing transfer

**Priority:** P0

Expected:

Rejected.

---

## TS-ACC-069 — Closed account attempts incoming transfer

**Priority:** P0

Expected:

Behavior follows closure rules.

---

## TS-ACC-070 — Closed account can still display historical statement where permitted

**Priority:** P1

Expected:

Historical data remains available according to business rules.

---

# 14. Freeze and Unfreeze Scenarios

## TS-ACC-071 — Authorized administrator freezes active account

**Priority:** P0

Expected:

Account status becomes FROZEN.

---

## TS-ACC-072 — Frozen account status appears immediately in customer UI

**Priority:** P0

Expected:

Customer sees updated state.

---

## TS-ACC-073 — Frozen account blocks subsequent outgoing transaction

**Priority:** P0

Expected:

Transaction rejected.

---

## TS-ACC-074 — Freeze account while customer is logged in

**Priority:** P0

Expected:

Active session does not bypass updated account state.

---

## TS-ACC-075 — Freeze account while transfer page is open

**Priority:** P0

Expected:

Final transfer submission is rejected.

---

## TS-ACC-076 — Authorized administrator unfreezes account

**Priority:** P1

Expected:

Account returns to valid active state where allowed.

---

## TS-ACC-077 — Customer performs allowed operation after unfreeze

**Priority:** P1

Expected:

Normal behavior restored.

---

## TS-ACC-078 — Limited admin attempts account freeze without permission

**Priority:** P0

Expected:

Access denied.

---

## TS-ACC-079 — Customer attempts to freeze own account through unauthorized administrative endpoint

**Priority:** P0

Expected:

Access denied unless customer self-freeze is intentionally supported.

---

# 15. Account Restriction Scenarios

## TS-ACC-080 — Apply debit restriction

**Priority:** P1

Expected:

Outgoing transactions blocked according to restriction.

---

## TS-ACC-081 — Apply credit restriction

**Priority:** P1

Expected:

Incoming transactions handled according to rules.

---

## TS-ACC-082 — Apply full account restriction

**Priority:** P0

Expected:

All prohibited financial activity is blocked.

---

## TS-ACC-083 — Remove account restriction

**Priority:** P1

Expected:

Permitted functionality resumes.

---

## TS-ACC-084 — Restriction change generates audit record

**Priority:** P1

Expected:

Actor and state change are traceable.

---

# 16. Account Closure Scenarios

## TS-ACC-085 — Close eligible account with zero balance

**Priority:** P1

Expected:

Closure succeeds.

---

## TS-ACC-086 — Attempt closure with positive balance

**Priority:** P0

Expected:

Rejected if zero balance is required.

---

## TS-ACC-087 — Attempt closure with negative balance

**Priority:** P0

Expected:

Rejected where outstanding balance prevents closure.

---

## TS-ACC-088 — Attempt closure with pending transaction

**Priority:** P0

Expected:

Rejected or delayed according to business rules.

---

## TS-ACC-089 — Attempt closure with scheduled transfer

**Priority:** P1

Expected:

Business rule enforced.

---

## TS-ACC-090 — Attempt closure with linked active card

**Priority:** P1

Expected:

Linked-product dependency handled correctly.

---

## TS-ACC-091 — Attempt closure with linked active loan repayment account

**Priority:** P0

Expected:

Closure blocked where account is required for active obligations.

---

## TS-ACC-092 — Closed account disappears from active-account list

**Priority:** P1

Expected:

Displayed according to UI rules.

---

## TS-ACC-093 — Closed account appears in historical/closed-account view

**Priority:** P2

Expected:

History remains accessible if supported.

---

## TS-ACC-094 — Closed account cannot be used as source account

**Priority:** P0

Expected:

Rejected.

---

## TS-ACC-095 — Closed account cannot be selected incorrectly in payment flow

**Priority:** P0

Expected:

Unavailable or blocked.

---

## TS-ACC-096 — Attempt to reopen closed account

**Priority:** P1

Expected:

Rejected unless reopening is explicitly supported.

---

# 17. Account State Transition Scenarios

Example:

```text
ACTIVE
  ↓
FROZEN
  ↓
ACTIVE
```

Possible lifecycle:

```text
ACTIVE
  ↓
RESTRICTED
  ↓
ACTIVE
  ↓
CLOSED
```

---

## TS-ACC-097 — ACTIVE → FROZEN

**Priority:** P0

Expected:

Valid authorized transition.

---

## TS-ACC-098 — FROZEN → ACTIVE

**Priority:** P1

Expected:

Valid authorized transition.

---

## TS-ACC-099 — ACTIVE → RESTRICTED

**Priority:** P0

Expected:

Restriction takes effect immediately.

---

## TS-ACC-100 — RESTRICTED → ACTIVE

**Priority:** P1

Expected:

Restriction removed.

---

## TS-ACC-101 — ACTIVE → CLOSED

**Priority:** P1

Expected:

Allowed only when closure requirements are satisfied.

---

## TS-ACC-102 — CLOSED → ACTIVE

**Priority:** P1

Expected:

Rejected unless business rules explicitly support reopening.

---

## TS-ACC-103 — FROZEN → CLOSED

**Priority:** P1

Expected:

Behavior follows closure policy.

---

## TS-ACC-104 — Invalid state-transition request through API

**Priority:** P0

Expected:

Backend rejects unsupported transition.

---

# 18. Own-Account Transfer Scenarios

## TS-ACC-105 — Transfer from checking to savings owned by same customer

**Priority:** P0

Expected:

* Source decreases.
* Destination increases.
* Transaction recorded.

---

## TS-ACC-106 — Transfer from savings to checking

**Priority:** P0

Expected:

Correct financial movement.

---

## TS-ACC-107 — Transfer to same source account

**Priority:** P1

Expected:

Rejected or prevented.

---

## TS-ACC-108 — Own-account transfer exact available balance

**Priority:** P0

Expected:

Behavior respects fees and minimum-balance rules.

---

## TS-ACC-109 — Own-account transfer exceeds available balance

**Priority:** P0

Expected:

Rejected.

---

## TS-ACC-110 — Own-account transfer where destination becomes frozen before submission

**Priority:** P0

Expected:

Final state validated server-side.

---

# 19. Available Balance and Holds Scenarios

## TS-ACC-111 — Pending transaction reduces available balance if required

**Priority:** P0

Expected:

Available balance reflects reserved funds.

---

## TS-ACC-112 — Failed pending transaction releases held amount

**Priority:** P0

Expected:

Available balance restored.

---

## TS-ACC-113 — Cancelled pending transaction releases hold

**Priority:** P0

Expected:

Reserved funds return correctly.

---

## TS-ACC-114 — Completed pending transaction updates current and available balance

**Priority:** P0

Expected:

Balances become internally consistent.

---

## TS-ACC-115 — Multiple holds calculated correctly

**Priority:** P0

Expected:

Available balance reflects aggregate held value.

---

# 20. Concurrent Balance Scenarios

## TS-ACC-116 — Two transfers use same starting balance concurrently

**Priority:** P0

Example:

```text
Starting Balance = 1,000
Transfer A = 800
Transfer B = 500
```

Expected:

Both cannot succeed if overdraft is not permitted.

---

## TS-ACC-117 — Transfer and payment execute concurrently

**Priority:** P0

Expected:

Financial rules preserve valid final balance.

---

## TS-ACC-118 — Incoming credit and outgoing debit occur concurrently

**Priority:** P0

Expected:

Final balance correctly reflects both operations.

---

## TS-ACC-119 — Account freeze occurs during transaction processing

**Priority:** P0

Expected:

System resolves operation consistently according to transaction state.

---

## TS-ACC-120 — Two admins attempt conflicting account-state changes

**Priority:** P1

Example:

```text
Admin A → Freeze
Admin B → Close
```

Expected:

Final account state remains valid and auditable.

---

# 21. Duplicate and Retry Scenarios

## TS-ACC-121 — Refresh after successful account operation

**Priority:** P1

Expected:

No duplicate financial action occurs.

---

## TS-ACC-122 — Double-click account action button

**Priority:** P1

Expected:

Action executes only once where idempotency is required.

---

## TS-ACC-123 — Retry failed state-change request

**Priority:** P1

Expected:

Account remains consistent.

---

## TS-ACC-124 — Duplicate account-state update request

**Priority:** P1

Expected:

No invalid transition or duplicate audit corruption.

---

# 22. Account Limits Scenarios

Execute where account-specific limits exist.

## TS-ACC-125 — Transaction below account limit

**Priority:** P1

Expected:

Allowed.

---

## TS-ACC-126 — Transaction exactly at account limit

**Priority:** P0

Expected:

Allowed according to inclusive boundary rule.

---

## TS-ACC-127 — Transaction above account limit

**Priority:** P0

Expected:

Rejected.

---

## TS-ACC-128 — Daily cumulative limit below threshold

**Priority:** P1

Expected:

Allowed.

---

## TS-ACC-129 — Daily cumulative limit exactly reached

**Priority:** P0

Expected:

Behavior matches defined rule.

---

## TS-ACC-130 — Daily cumulative limit exceeded by additional transaction

**Priority:** P0

Expected:

Rejected.

---

# 23. Account Alias Scenarios

If customers may name accounts.

## TS-ACC-131 — Set valid account alias

**Priority:** P2

Expected:

Alias saved.

---

## TS-ACC-132 — Empty alias

**Priority:** P3

Expected:

Default behavior follows requirements.

---

## TS-ACC-133 — Alias at maximum length

**Priority:** P2

Expected:

Accepted.

---

## TS-ACC-134 — Alias above maximum length

**Priority:** P2

Expected:

Rejected.

---

## TS-ACC-135 — Alias with Unicode characters

**Priority:** P2

Expected:

Stored correctly where supported.

---

## TS-ACC-136 — Alias with script-like input

**Priority:** P1

Expected:

Safely handled without execution.

---

# 24. Search and Filtering Scenarios

Where customers/admins can search accounts.

## TS-ACC-137 — Search by account number

**Priority:** P1

Expected:

Correct authorized result.

---

## TS-ACC-138 — Search by customer

**Priority:** P1

Admin scenario.

Expected:

Correct account list.

---

## TS-ACC-139 — Filter by ACTIVE status

**Priority:** P2

Expected:

Only active accounts.

---

## TS-ACC-140 — Filter by FROZEN status

**Priority:** P2

Expected:

Only frozen accounts.

---

## TS-ACC-141 — Filter by account type

**Priority:** P2

Expected:

Correct checking/savings results.

---

## TS-ACC-142 — Search nonexistent account

**Priority:** P2

Expected:

Safe empty state.

---

## TS-ACC-143 — Search with malformed input

**Priority:** P1

Expected:

No internal errors or unauthorized information disclosure.

---

# 25. Administrative Account Scenarios

## TS-ACC-144 — Full admin views customer account

**Priority:** P1

Expected:

Authorized access.

---

## TS-ACC-145 — Limited admin views only permitted account fields

**Priority:** P0

Expected:

Field-level permissions enforced.

---

## TS-ACC-146 — Full admin freezes account

**Priority:** P0

Expected:

State updated and audited.

---

## TS-ACC-147 — Full admin unfreezes account

**Priority:** P1

Expected:

State restored correctly.

---

## TS-ACC-148 — Limited admin attempts account closure

**Priority:** P0

Expected:

Denied if permission not granted.

---

## TS-ACC-149 — Customer attempts admin account operation through API

**Priority:** P0

Expected:

Authorization failure.

---

## TS-ACC-150 — Admin modifies account while customer views account

**Priority:** P1

Expected:

Customer receives latest valid state.

---

# 26. Account Data Consistency Scenarios

## TS-ACC-151 — UI balance matches account API

**Priority:** P0

Expected:

Same valid balance.

---

## TS-ACC-152 — API balance matches database

**Priority:** P0

Expected:

Persisted and returned values match.

---

## TS-ACC-153 — Account status matches UI, API, and database

**Priority:** P0

Expected:

No layer disagreement.

---

## TS-ACC-154 — Account currency matches across layers

**Priority:** P1

Expected:

Consistent.

---

## TS-ACC-155 — Account ownership matches database and API authorization

**Priority:** P0

Expected:

Only correct customer has access.

---

## TS-ACC-156 — Failed account update does not partially modify database

**Priority:** P0

Expected:

Atomic update behavior.

---

# 27. Transaction History Integration Scenarios

## TS-ACC-157 — Account balance change corresponds to transaction record

**Priority:** P0

Expected:

Every financial balance movement is traceable.

---

## TS-ACC-158 — Incoming transfer appears as credit

**Priority:** P1

Expected:

Correct type and amount.

---

## TS-ACC-159 — Outgoing transfer appears as debit

**Priority:** P1

Expected:

Correct type and amount.

---

## TS-ACC-160 — Fee appears correctly in transaction history

**Priority:** P1

Expected:

Fee representation matches accounting design.

---

## TS-ACC-161 — Reversal appears correctly

**Priority:** P0

Expected:

History explains corresponding balance restoration.

---

# 28. Statement Integration Scenarios

## TS-ACC-162 — Account statement opening balance is correct

**Priority:** P0

Expected:

Matches account state at period start.

---

## TS-ACC-163 — Account statement closing balance is correct

**Priority:** P0

Expected:

Matches calculated period result.

---

## TS-ACC-164 — Statement transactions reconcile with balance

**Priority:** P0

Expected:

```text
Opening Balance
+ Credits
- Debits
- Fees
= Closing Balance
```

---

## TS-ACC-165 — Closed account historical statement remains correct

**Priority:** P1

Expected:

Closure does not alter historical financial data.

---

# 29. Error Handling Scenarios

## TS-ACC-166 — Account service unavailable

**Priority:** P1

Expected:

Safe error shown.

No false balance is displayed.

---

## TS-ACC-167 — Balance endpoint returns server error

**Priority:** P0

Expected:

Application must not display stale balance as newly verified data without appropriate indication.

---

## TS-ACC-168 — Network disconnect while loading account details

**Priority:** P1

Expected:

Graceful error state.

---

## TS-ACC-169 — Network interruption during account status update

**Priority:** P0

Expected:

Final status can be determined unambiguously after recovery.

---

## TS-ACC-170 — Slow account operation

**Priority:** P1

Expected:

Processing state prevents repeated conflicting actions.

---

# 30. Sensitive Data Scenarios

## TS-ACC-171 — Full account number not exposed where masking is required

**Priority:** P1

Expected:

Appropriate masking.

---

## TS-ACC-172 — Another customer's account details not present in API error

**Priority:** P0

Expected:

No cross-customer leakage.

---

## TS-ACC-173 — Account-related response does not expose unnecessary internal fields

**Priority:** P1

Examples:

* Internal security flags
* Database metadata
* Confidential backend values

---

## TS-ACC-174 — Account identifiers not unnecessarily exposed in public URLs

**Priority:** P2

Expected:

Exposure follows security design.

---

# 31. Audit Scenarios

## TS-ACC-175 — Account creation generates audit record

**Priority:** P1

Expected:

Actor and created account are traceable.

---

## TS-ACC-176 — Account freeze generates audit record

**Priority:** P0

Expected:

Contains:

* Actor
* Account
* Previous state
* New state
* Timestamp

---

## TS-ACC-177 — Account unfreeze generates audit record

**Priority:** P1

Expected:

Traceable.

---

## TS-ACC-178 — Account restriction generates audit record

**Priority:** P0

Expected:

Traceable.

---

## TS-ACC-179 — Account closure generates audit record

**Priority:** P0

Expected:

Closure is fully traceable.

---

## TS-ACC-180 — Unauthorized account-state change attempt logged where required

**Priority:** P2

Expected:

Security event recorded where implemented.

---

# 32. Notification Scenarios

## TS-ACC-181 — Customer receives account-freeze notification

**Priority:** P1

Expected:

Notification reflects actual persisted state.

---

## TS-ACC-182 — Customer receives account-unfreeze notification

**Priority:** P2

Expected:

Correct customer receives notification.

---

## TS-ACC-183 — Customer receives account-restriction notification

**Priority:** P1

Expected:

Status described accurately.

---

## TS-ACC-184 — Customer receives account-closure notification

**Priority:** P1

Expected:

Notification is generated when closure succeeds.

---

## TS-ACC-185 — Failed account-state change does not send success notification

**Priority:** P1

Expected:

No misleading message.

---

# 33. Accessibility and Usability Scenarios

## TS-ACC-186 — Account balances clearly distinguish current and available balance

**Priority:** P1

Expected:

Customer can understand both values.

---

## TS-ACC-187 — Account status clearly visible

**Priority:** P1

Expected:

Frozen/restricted/closed state is not ambiguous.

---

## TS-ACC-188 — Financial amounts display currency clearly

**Priority:** P2

Expected:

Currency context is obvious.

---

## TS-ACC-189 — Account selection usable with keyboard

**Priority:** P2

Expected:

Accessible navigation.

---

## TS-ACC-190 — Account warnings are visible before prohibited action

**Priority:** P1

Expected:

Customer understands restriction.

---

# 34. Responsive Scenarios

## TS-ACC-191 — Account dashboard on desktop

**Priority:** P2

Expected:

All account information readable.

---

## TS-ACC-192 — Account dashboard on tablet

**Priority:** P2

Expected:

No critical information hidden.

---

## TS-ACC-193 — Account dashboard on mobile

**Priority:** P1

Expected:

Balance and status remain clearly visible.

---

## TS-ACC-194 — Transaction controls on mobile for multiple accounts

**Priority:** P1

Expected:

Correct source account can be selected safely.

---

## TS-ACC-195 — Long account alias on mobile

**Priority:** P2

Expected:

Does not obscure balance or critical actions.

---

# 35. Cross-Browser Scenarios

## TS-ACC-196 — Account viewing in Chrome

**Priority:** P1

Expected:

Correct.

---

## TS-ACC-197 — Account viewing in Edge

**Priority:** P1

Expected:

Correct.

---

## TS-ACC-198 — Account viewing in Firefox

**Priority:** P1

Expected:

Correct.

---

## TS-ACC-199 — Account status actions across supported browsers

**Priority:** P1

Expected:

No critical browser-specific failure.

---

# 36. Boundary Scenarios

## TS-ACC-200 — Account balance exactly 0.00

**Priority:** P1

Expected:

Handled correctly.

---

## TS-ACC-201 — Account balance 0.01

**Priority:** P1

Expected:

Correct display and spending behavior.

---

## TS-ACC-202 — Transaction amount equals available balance

**Priority:** P0

Expected:

Business rules around fees and reserves are enforced.

---

## TS-ACC-203 — Transaction amount available balance minus 0.01

**Priority:** P1

Expected:

Allowed if all other rules pass.

---

## TS-ACC-204 — Transaction amount available balance plus 0.01

**Priority:** P0

Expected:

Rejected where overdraft is not allowed.

---

## TS-ACC-205 — Account limit minus 0.01

**Priority:** P1

Expected:

Allowed.

---

## TS-ACC-206 — Account limit exactly

**Priority:** P0

Expected:

Handled according to inclusive/exclusive business rule.

---

## TS-ACC-207 — Account limit plus 0.01

**Priority:** P0

Expected:

Rejected.

---

# 37. End-to-End Account Scenarios

## TS-ACC-208 — New account to first transaction

**Priority:** P1

Flow:

```text
Create Account
→ Verify Account
→ Fund Account
→ View Balance
→ Perform Transaction
→ Verify New Balance
→ Verify Transaction History
```

---

## TS-ACC-209 — Account freeze while customer is active

**Priority:** P0

Flow:

```text
Customer Login
→ View Active Account
→ Admin Freezes Account
→ Customer Attempts Transfer
→ Transfer Rejected
→ Customer Sees Frozen State
→ Verify Audit
```

---

## TS-ACC-210 — Account unfreeze restores functionality

**Priority:** P1

Flow:

```text
Frozen Account
→ Admin Unfreezes
→ Customer Refreshes
→ Account Active
→ Valid Transfer
→ Success
```

---

## TS-ACC-211 — Eligible account closure

**Priority:** P1

Flow:

```text
Account Balance = 0
→ No Pending Transactions
→ Close Account
→ Verify CLOSED
→ Attempt Transaction
→ Rejected
→ Verify Audit
```

---

## TS-ACC-212 — Multi-account customer own-transfer flow

**Priority:** P0

Flow:

```text
Login
→ Select Checking
→ Transfer to Own Savings
→ Verify Checking Debit
→ Verify Savings Credit
→ Verify Both Histories
```

---

## TS-ACC-213 — Concurrent debit protection

**Priority:** P0

Flow:

```text
Starting Balance = 1000
→ Submit 800 Transfer
→ Simultaneously Submit 500 Payment
→ Verify Business Rule Enforcement
→ Verify Valid Final Balance
→ Verify Both Transaction States
```

---

# 38. Critical Smoke Scenarios

Account smoke coverage should include:

```text
TS-ACC-001 — View own account
TS-ACC-002 — Account details
TS-ACC-009 — Cannot view another customer's account
TS-ACC-037 — Current balance correct
TS-ACC-038 — Available balance correct
TS-ACC-046 — Incoming transfer changes balance
TS-ACC-047 — Outgoing transfer changes balance
TS-ACC-061 — Active account functions
```

---

# 39. Critical Regression Scenarios

Always prioritize:

* Account ownership
* Current balance
* Available balance
* Successful debit
* Successful credit
* Failed transaction balance integrity
* Fees
* Account states
* Freeze enforcement
* Closed-account enforcement
* Limits
* Concurrency
* UI/API/database consistency
* Transaction reconciliation
* Statement reconciliation
* Admin authorization

---

# 40. Automation Candidates

Strong UI automation candidates:

* View account
* View balance
* Switch accounts
* Own-account transfer
* Frozen-account behavior
* Restricted-account behavior
* Account-state display
* Account alias
* Admin freeze/unfreeze

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 41. API Automation Candidates

Postman and REST Assured should later cover:

* Get account
* Get account balance
* Get own accounts
* Access unauthorized account
* Create account
* Freeze/unfreeze
* Restrict/unrestrict
* Close account
* Invalid state transitions
* Limits
* Concurrency/idempotency behavior

---

# 42. SQL Validation Candidates

Database testing should validate:

* Account ownership
* Account number uniqueness
* Account type
* Currency
* Current balance
* Available balance where persisted
* Account status
* Transaction relationships
* State changes
* Audit records

---

# 43. Performance Testing Candidates

JMeter should later cover:

* Account-list retrieval
* Account-details retrieval
* Balance retrieval
* Concurrent balance access
* Large numbers of simultaneous dashboard requests

The primary performance goal is to maintain accurate state even under high concurrency.

---

# 44. BDD Candidates

Example:

```gherkin
Feature: Frozen bank account

Scenario: Customer cannot transfer money from a frozen account
  Given the customer has an active account
  And an administrator freezes the account
  When the customer attempts to transfer money from the account
  Then the transfer should be rejected
  And the account balance should remain unchanged
```

---

# 45. Risk Traceability

Major related risks include:

```text
RISK-001 — Incorrect account balance
RISK-002 — Unauthorized customer data access
RISK-004 — Partial transfer processing
RISK-007 — Transfer exceeding available balance
RISK-008 — Transfer limit bypass
RISK-009 — Frozen account can transact
RISK-013 — Concurrent transactions corrupt balance
RISK-019 — Transaction history mismatch
RISK-030 — API authorization failure
RISK-039 — API/database inconsistency
RISK-047 — Insecure direct object access
```

---

# 46. Account Coverage Summary

This catalog covers:

* Account viewing
* Ownership
* Creation
* Account number
* Account type
* Current balance
* Available balance
* Financial integrity
* Precision
* Account states
* Freeze/unfreeze
* Restrictions
* Closure
* State transitions
* Own-account transfers
* Holds
* Limits
* Concurrency
* Duplicate requests
* Admin operations
* Data consistency
* Transaction integration
* Statement reconciliation
* Error handling
* Sensitive data
* Audit
* Notifications
* Accessibility
* Responsive behavior
* Cross-browser behavior
* Boundary testing
* E2E workflows

---

# 47. Final Account Testing Principle

Account testing must treat the balance and account state as critical financial data.

The most important questions are:

```text
Does the account belong to the authenticated customer?

Is the current balance correct?

Is the available balance correct?

Does every debit have a valid reason?

Does every credit have a valid reason?

Can a frozen or closed account still transact?

Can two simultaneous operations spend the same funds?

Can another customer access this account?

Are UI, API, database, transaction history, and statements consistent?

Can failed operations alter the balance?

Are account-state changes authorized and audited?
```

A banking account is the central financial object in the system, so incorrect account behavior can invalidate almost every other banking module.
