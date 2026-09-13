# Banking System — Deposit Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Deposits                       |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for deposit functionality within the Banking System.

Deposit products are financially sensitive because defects may cause:

* Incorrect principal handling
* Incorrect interest calculation
* Incorrect maturity values
* Incorrect early-withdrawal penalties
* Duplicate deposit creation
* Duplicate interest credit
* Incorrect maturity processing
* Unauthorized access
* Incorrect closure
* Account-balance inconsistencies

The scenarios cover the full deposit lifecycle from product selection and creation through maturity, renewal, early withdrawal, and closure.

---

# 3. Scope

Deposit testing includes:

* Deposit products
* Product eligibility
* Deposit creation
* Funding account
* Principal validation
* Deposit term
* Interest rate
* Interest calculation
* Maturity amount
* Maturity date
* Deposit status
* Early withdrawal
* Penalties
* Interest credit
* Maturity processing
* Auto-renewal
* Closure
* Deposit ownership
* Authorization
* Notifications
* Audit logging
* Financial reconciliation
* API consistency
* Database validation
* Concurrency

---

# 4. Scenario Naming Convention

Deposit scenarios use:

```text
TS-DEP-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Deposit Product Viewing Scenarios

## TS-DEP-001 — Customer views available deposit products

**Priority:** P1

Expected:

Available products are displayed correctly.

---

## TS-DEP-002 — Deposit product displays minimum principal

**Priority:** P1

Expected:

Correct configured minimum is shown.

---

## TS-DEP-003 — Deposit product displays maximum principal

**Priority:** P1

Expected:

Correct configured maximum is shown where applicable.

---

## TS-DEP-004 — Deposit product displays available terms

**Priority:** P1

Expected:

Supported maturity periods are displayed clearly.

---

## TS-DEP-005 — Deposit product displays interest rate

**Priority:** P0

Expected:

Correct configured rate is shown.

---

## TS-DEP-006 — Deposit product displays early-withdrawal conditions

**Priority:** P1

Expected:

Penalty or forfeiture rules are clearly communicated.

---

## TS-DEP-007 — Deposit product displays maturity behavior

**Priority:** P1

Expected:

Customer can determine whether funds:

* Return to account
* Renew automatically
* Require manual instruction

---

# 6. Deposit Ownership and Authorization

## TS-DEP-008 — Customer views own deposit

**Priority:** P0

Expected:

Correct deposit information displayed.

---

## TS-DEP-009 — Customer attempts to view another customer's deposit

**Priority:** P0

Expected:

Access denied.

---

## TS-DEP-010 — Modify deposit ID in URL

**Priority:** P0

Expected:

Another customer's deposit remains inaccessible.

---

## TS-DEP-011 — Modify deposit ID in API request

**Priority:** P0

Expected:

Backend authorization rejects access.

---

## TS-DEP-012 — Customer attempts to close another customer's deposit

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-013 — Unauthenticated user accesses deposit resource

**Priority:** P0

Expected:

Authentication required.

---

## TS-DEP-014 — Expired session performs deposit action

**Priority:** P0

Expected:

Rejected or reauthentication required.

---

# 7. Deposit Eligibility Scenarios

## TS-DEP-015 — Active verified customer opens deposit

**Priority:** P0

Expected:

Allowed.

---

## TS-DEP-016 — Unverified customer attempts deposit creation

**Priority:** P0

Expected:

Rejected where KYC is required.

---

## TS-DEP-017 — Restricted customer attempts deposit creation

**Priority:** P0

Expected:

Behavior follows customer restrictions.

---

## TS-DEP-018 — Suspended customer attempts deposit creation

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-019 — Disabled customer attempts deposit creation

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-020 — Customer without eligible funding account attempts creation

**Priority:** P0

Expected:

Cannot continue.

---

# 8. Deposit Creation Scenarios

## TS-DEP-021 — Create valid deposit

**Priority:** P0

Expected:

Deposit is created successfully.

---

## TS-DEP-022 — Create deposit using valid active funding account

**Priority:** P0

Expected:

Principal is debited from correct source account.

---

## TS-DEP-023 — Create deposit with required field missing

**Priority:** P1

Expected:

Validation displayed.

---

## TS-DEP-024 — Submit empty deposit application

**Priority:** P1

Expected:

Rejected.

---

## TS-DEP-025 — Double-click Create Deposit

**Priority:** P0

Expected:

Only one deposit is created and principal is debited once.

---

## TS-DEP-026 — Refresh after deposit submission

**Priority:** P0

Expected:

No duplicate creation or duplicate debit.

---

## TS-DEP-027 — Network interruption immediately after creation request

**Priority:** P0

Expected:

Customer can determine actual deposit state before retrying.

---

# 9. Funding Account Scenarios

## TS-DEP-028 — Use active owned account

**Priority:** P0

Expected:

Funding allowed.

---

## TS-DEP-029 — Use frozen funding account

**Priority:** P0

Expected:

Deposit creation rejected.

---

## TS-DEP-030 — Use restricted funding account

**Priority:** P0

Expected:

Rejected where restriction prevents debit.

---

## TS-DEP-031 — Use closed funding account

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-032 — Use another customer's account

**Priority:** P0

Expected:

Authorization failure.

---

## TS-DEP-033 — Funding account freezes before final confirmation

**Priority:** P0

Expected:

Deposit creation rejected.

---

# 10. Principal Amount Validation

Assume example limits:

```text
Minimum principal = 1,000.00
Maximum principal = 1,000,000.00
```

Actual values must follow product rules.

---

## TS-DEP-034 — Principal below minimum

**Priority:** P1

Expected:

Rejected.

---

## TS-DEP-035 — Principal exactly at minimum

**Priority:** P1

Expected:

Accepted.

---

## TS-DEP-036 — Principal minimum plus smallest unit

**Priority:** P2

Expected:

Accepted.

---

## TS-DEP-037 — Principal maximum minus smallest unit

**Priority:** P1

Expected:

Accepted.

---

## TS-DEP-038 — Principal exactly at maximum

**Priority:** P0

Expected:

Accepted if inclusive.

---

## TS-DEP-039 — Principal above maximum

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-040 — Principal equals zero

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-041 — Negative principal

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-042 — Non-numeric principal

**Priority:** P1

Expected:

Rejected.

---

## TS-DEP-043 — Unsupported decimal precision

**Priority:** P1

Expected:

Rejected or normalized strictly according to financial rules.

---

# 11. Available Balance Scenarios

## TS-DEP-044 — Principal below available balance

**Priority:** P0

Expected:

Deposit creation allowed.

---

## TS-DEP-045 — Principal equals available balance

**Priority:** P0

Expected:

Behavior follows minimum-retained-balance and fee rules.

---

## TS-DEP-046 — Principal above available balance

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-047 — Current balance sufficient but available balance insufficient

**Priority:** P0

Expected:

Available balance is authoritative.

---

## TS-DEP-048 — Deposit fee causes total debit to exceed available funds

**Priority:** P0

Expected:

Rejected.

---

# 12. Deposit Term Scenarios

## TS-DEP-049 — Select minimum valid term

**Priority:** P1

Expected:

Accepted.

---

## TS-DEP-050 — Select maximum valid term

**Priority:** P1

Expected:

Accepted.

---

## TS-DEP-051 — Term below supported minimum

**Priority:** P1

Expected:

Rejected.

---

## TS-DEP-052 — Term above supported maximum

**Priority:** P1

Expected:

Rejected.

---

## TS-DEP-053 — Manipulate term to unsupported value through API

**Priority:** P0

Expected:

Backend rejects unsupported term.

---

# 13. Interest Rate Scenarios

## TS-DEP-054 — Correct rate displayed for selected product

**Priority:** P0

Expected:

Configured rate displayed.

---

## TS-DEP-055 — Correct rate applied for selected term

**Priority:** P0

Expected:

Term-specific rate used.

---

## TS-DEP-056 — Customer manipulates interest rate in client request

**Priority:** P0

Expected:

Backend uses authoritative configured rate.

---

## TS-DEP-057 — Product rate changes before final creation

**Priority:** P1

Expected:

Customer receives correct authoritative rate according to locking/business rules.

---

## TS-DEP-058 — Deposit stores agreed interest rate after creation

**Priority:** P0

Expected:

Subsequent unrelated product-rate changes do not silently alter fixed-rate deposit.

---

# 14. Interest Calculation Scenarios

## TS-DEP-059 — Calculate interest for standard deposit

**Priority:** P0

Expected:

Calculation matches defined formula.

---

## TS-DEP-060 — Interest on minimum principal

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-061 — Interest on maximum principal

**Priority:** P0

Expected:

Correct without precision loss.

---

## TS-DEP-062 — Interest for minimum term

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-063 — Interest for maximum term

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-064 — Interest rounding

**Priority:** P0

Expected:

Rounding follows financial precision rules.

---

## TS-DEP-065 — Very small calculated interest value

**Priority:** P1

Expected:

Correct rounding.

---

## TS-DEP-066 — Large calculated interest value

**Priority:** P1

Expected:

No overflow.

---

# 15. Maturity Amount Scenarios

## TS-DEP-067 — Maturity amount calculated correctly

**Priority:** P0

Expected:

```text
Principal + Earned Interest = Maturity Amount
```

minus any explicitly applicable taxes/charges if implemented.

---

## TS-DEP-068 — Maturity value shown before confirmation

**Priority:** P1

Expected:

Customer understands expected financial return.

---

## TS-DEP-069 — Maturity amount matches API result

**Priority:** P0

Expected:

Consistent.

---

## TS-DEP-070 — Maturity amount matches database data/calculation

**Priority:** P0

Expected:

Consistent.

---

# 16. Maturity Date Scenarios

## TS-DEP-071 — Maturity date calculated correctly

**Priority:** P0

Expected:

Correct based on start date and term.

---

## TS-DEP-072 — Month-end maturity calculation

**Priority:** P1

Expected:

Correct calendar behavior.

---

## TS-DEP-073 — Year-end maturity calculation

**Priority:** P1

Expected:

Correct.

---

## TS-DEP-074 — Leap-year maturity date

**Priority:** P1

Expected:

Correct valid date.

---

## TS-DEP-075 — Timezone does not shift maturity date unexpectedly

**Priority:** P0

Expected:

Correct business date.

---

# 17. Deposit Confirmation Scenarios

## TS-DEP-076 — Confirmation displays correct funding account

**Priority:** P0

Expected:

Correct account.

---

## TS-DEP-077 — Confirmation displays correct principal

**Priority:** P0

Expected:

Exact amount.

---

## TS-DEP-078 — Confirmation displays correct term

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-079 — Confirmation displays correct rate

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-080 — Confirmation displays maturity date

**Priority:** P1

Expected:

Correct.

---

## TS-DEP-081 — Confirmation displays maturity amount

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-082 — Cancel before final confirmation

**Priority:** P1

Expected:

No account debit and no deposit creation.

---

# 18. Deposit State Scenarios

Possible states:

```text
PENDING
ACTIVE
MATURED
CLOSED
CANCELLED
```

Optional:

```text
EARLY_WITHDRAWN
RENEWED
```

---

## TS-DEP-083 — New valid deposit becomes ACTIVE

**Priority:** P0

Expected:

Correct state.

---

## TS-DEP-084 — Deposit remains ACTIVE before maturity

**Priority:** P1

Expected:

Correct.

---

## TS-DEP-085 — Deposit reaches MATURITY

**Priority:** P0

Expected:

State transitions correctly.

---

## TS-DEP-086 — Matured deposit becomes CLOSED after payout

**Priority:** P0

Expected:

Correct terminal state.

---

## TS-DEP-087 — Closed deposit cannot be withdrawn again

**Priority:** P0

Expected:

Rejected.

---

# 19. Deposit State Transition Scenarios

Example:

```text
PENDING
   ↓
ACTIVE
   ↓
MATURED
   ↓
CLOSED
```

Early termination:

```text
ACTIVE
   ↓
EARLY_WITHDRAWN
   ↓
CLOSED
```

Renewal:

```text
MATURED
   ↓
RENEWED
   ↓
ACTIVE
```

---

## TS-DEP-088 — PENDING → ACTIVE

**Priority:** P0

Expected:

Occurs once funding succeeds.

---

## TS-DEP-089 — ACTIVE → MATURED

**Priority:** P0

Expected:

Occurs only on valid maturity condition.

---

## TS-DEP-090 — MATURED → CLOSED

**Priority:** P0

Expected:

Valid after payout.

---

## TS-DEP-091 — ACTIVE → CLOSED directly without early-withdrawal process

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-092 — CLOSED → ACTIVE

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-093 — Invalid transition through API

**Priority:** P0

Expected:

Backend rejects.

---

# 20. Principal Debit Scenarios

## TS-DEP-094 — Principal debited exactly once at creation

**Priority:** P0

Expected:

Single debit.

---

## TS-DEP-095 — Account balance decreases by correct principal and applicable fee

**Priority:** P0

Expected:

Correct financial effect.

---

## TS-DEP-096 — Failed creation leaves source balance unchanged

**Priority:** P0

Expected:

No debit.

---

## TS-DEP-097 — Duplicate creation attempt does not duplicate debit

**Priority:** P0

Expected:

Financial integrity preserved.

---

# 21. Maturity Processing Scenarios

## TS-DEP-098 — Deposit matures on expected date

**Priority:** P0

Expected:

Maturity processing executes exactly once.

---

## TS-DEP-099 — Principal returned on maturity

**Priority:** P0

Expected:

Correct principal credit.

---

## TS-DEP-100 — Interest credited on maturity

**Priority:** P0

Expected:

Correct earned interest.

---

## TS-DEP-101 — Principal and interest credited exactly once

**Priority:** P0

Expected:

No duplicate maturity payout.

---

## TS-DEP-102 — Maturity transaction reference generated

**Priority:** P1

Expected:

Traceable.

---

## TS-DEP-103 — Maturity account becomes closed immediately before payout

**Priority:** P0

Expected:

System follows controlled fallback/error workflow without losing funds.

---

# 22. Early Withdrawal Scenarios

Where supported.

## TS-DEP-104 — Request early withdrawal from active deposit

**Priority:** P1

Expected:

Early-withdrawal quote/rules displayed.

---

## TS-DEP-105 — Early withdrawal before minimum holding period

**Priority:** P1

Expected:

Rejected if prohibited.

---

## TS-DEP-106 — Early withdrawal after minimum allowed period

**Priority:** P1

Expected:

Allowed according to rules.

---

## TS-DEP-107 — Early-withdrawal penalty calculated correctly

**Priority:** P0

Expected:

Correct penalty.

---

## TS-DEP-108 — Earned interest reduction calculated correctly

**Priority:** P0

Expected:

Correct forfeiture/recalculation.

---

## TS-DEP-109 — Net early-withdrawal amount correct

**Priority:** P0

Expected:

```text
Principal
+ Allowed Interest
- Penalty
= Net Payout
```

---

## TS-DEP-110 — Early withdrawal payout credited exactly once

**Priority:** P0

Expected:

No duplicate payout.

---

## TS-DEP-111 — Deposit closes after successful early withdrawal

**Priority:** P0

Expected:

Cannot be withdrawn again.

---

# 23. Early Withdrawal Boundary Scenarios

## TS-DEP-112 — Withdrawal one day before minimum eligible date

**Priority:** P1

Expected:

Rejected.

---

## TS-DEP-113 — Withdrawal exactly at eligibility date

**Priority:** P0

Expected:

Handled according to rule.

---

## TS-DEP-114 — Withdrawal one day after eligibility date

**Priority:** P1

Expected:

Allowed.

---

# 24. Auto-Renewal Scenarios

Where supported.

## TS-DEP-115 — Enable auto-renewal

**Priority:** P1

Expected:

Preference saved.

---

## TS-DEP-116 — Disable auto-renewal

**Priority:** P1

Expected:

Preference removed.

---

## TS-DEP-117 — Auto-renew at maturity

**Priority:** P0

Expected:

New deposit/renewal cycle begins according to rules.

---

## TS-DEP-118 — Renewal uses correct principal

**Priority:** P0

Expected:

Configured principal-renewal rule enforced.

---

## TS-DEP-119 — Renewal uses applicable new/current interest rate

**Priority:** P0

Expected:

Correct rate according to renewal policy.

---

## TS-DEP-120 — Auto-renewal executes only once

**Priority:** P0

Expected:

No duplicate renewed deposits.

---

## TS-DEP-121 — Customer disables auto-renewal before maturity

**Priority:** P1

Expected:

Deposit pays out instead of renewing.

---

# 25. Interest Payment Frequency Scenarios

If periodic interest payments are supported.

## TS-DEP-122 — Monthly interest credit

**Priority:** P0

Expected:

Correct monthly interest credited.

---

## TS-DEP-123 — Quarterly interest credit

**Priority:** P0

Expected:

Correct schedule.

---

## TS-DEP-124 — Interest credited to correct account

**Priority:** P0

Expected:

Customer's configured settlement account receives funds.

---

## TS-DEP-125 — Periodic interest not credited twice

**Priority:** P0

Expected:

No duplicate payment.

---

## TS-DEP-126 — Failed interest-credit processing recovers safely

**Priority:** P0

Expected:

No lost or duplicate interest.

---

# 26. Deposit Fees and Penalties

## TS-DEP-127 — Deposit-opening fee calculated correctly

**Priority:** P1

Where applicable.

---

## TS-DEP-128 — Early-withdrawal penalty calculated correctly

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-129 — Penalty boundary rule

**Priority:** P1

Expected:

Correct tier applied.

---

## TS-DEP-130 — Fee displayed before confirmation

**Priority:** P1

Expected:

Customer sees all charges.

---

## TS-DEP-131 — Fee deducted exactly once

**Priority:** P0

Expected:

No duplicate fee.

---

# 27. Deposit Cancellation Scenarios

Where pending creation may be cancellable.

## TS-DEP-132 — Cancel pending deposit before activation

**Priority:** P1

Expected:

No active deposit remains.

---

## TS-DEP-133 — Principal restored after valid cancelled pending creation

**Priority:** P0

Expected:

Correct financial restoration.

---

## TS-DEP-134 — Cancel active deposit using normal cancellation endpoint

**Priority:** P0

Expected:

Rejected if early-withdrawal workflow is required.

---

## TS-DEP-135 — Cancel matured deposit after payout

**Priority:** P1

Expected:

Rejected.

---

# 28. Concurrency Scenarios

## TS-DEP-136 — Two deposit creations compete for same balance

**Priority:** P0

Example:

```text
Available Balance = 10,000
Deposit A = 7,000
Deposit B = 6,000
```

Expected:

Combined successful debit must not exceed available balance.

---

## TS-DEP-137 — Deposit creation and bank transfer occur concurrently

**Priority:** P0

Expected:

Financial integrity preserved.

---

## TS-DEP-138 — Maturity processing triggered concurrently twice

**Priority:** P0

Expected:

Only one payout.

---

## TS-DEP-139 — Early withdrawal submitted simultaneously from two sessions

**Priority:** P0

Expected:

Only one payout and one closure.

---

## TS-DEP-140 — Auto-renewal and manual payout processing race

**Priority:** P0

Expected:

Deposit follows exactly one valid maturity path.

---

# 29. Financial Precision Scenarios

## TS-DEP-141 — Principal precision stored correctly

**Priority:** P0

Expected:

No floating-point errors.

---

## TS-DEP-142 — Interest precision handled correctly

**Priority:** P0

Expected:

Correct rounding.

---

## TS-DEP-143 — Maturity amount reconciles exactly

**Priority:** P0

Expected:

No unexplained fractional residual.

---

## TS-DEP-144 — Repeated periodic interest does not accumulate rounding drift

**Priority:** P0

Expected:

Final amount reconciles according to documented policy.

---

# 30. Transaction History Integration

## TS-DEP-145 — Deposit creation appears as source-account debit

**Priority:** P0

Expected:

Correct principal transaction.

---

## TS-DEP-146 — Maturity payout appears as credit

**Priority:** P0

Expected:

Correct principal/interest representation.

---

## TS-DEP-147 — Periodic interest appears as credit

**Priority:** P1

Expected:

Correct amount and reference.

---

## TS-DEP-148 — Early withdrawal payout appears correctly

**Priority:** P0

Expected:

Net payout and applicable penalty remain traceable.

---

## TS-DEP-149 — Failed deposit creation does not appear as successful debit

**Priority:** P0

Expected:

History remains accurate.

---

# 31. Statement Integration

## TS-DEP-150 — Deposit funding appears on statement

**Priority:** P0

Expected:

Correct debit.

---

## TS-DEP-151 — Deposit interest appears on statement

**Priority:** P1

Expected:

Correct credit.

---

## TS-DEP-152 — Maturity payout appears on statement

**Priority:** P0

Expected:

Correct amount.

---

## TS-DEP-153 — Early-withdrawal penalty appears correctly

**Priority:** P1

Expected:

Accounting representation follows design.

---

# 32. API Consistency Scenarios

## TS-DEP-154 — Deposit UI status matches API

**Priority:** P0

Expected:

Consistent.

---

## TS-DEP-155 — Deposit principal matches API

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-156 — Interest rate matches UI and API

**Priority:** P0

Expected:

Same authoritative value.

---

## TS-DEP-157 — Maturity amount matches API and UI

**Priority:** P0

Expected:

Consistent.

---

## TS-DEP-158 — Deposit API state matches database

**Priority:** P0

Expected:

No disagreement.

---

# 33. Database Validation Scenarios

## TS-DEP-159 — Deposit record stored with correct customer

**Priority:** P0

Expected:

Correct ownership.

---

## TS-DEP-160 — Funding account relationship stored correctly

**Priority:** P0

Expected:

Correct source account.

---

## TS-DEP-161 — Principal stored correctly

**Priority:** P0

Expected:

Correct precision.

---

## TS-DEP-162 — Rate and term stored correctly

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-163 — Maturity date stored correctly

**Priority:** P0

Expected:

Correct.

---

## TS-DEP-164 — Matured/closed deposit reflects correct final status

**Priority:** P0

Expected:

Correct state.

---

## TS-DEP-165 — Interest-credit records reconcile with deposit

**Priority:** P0

Expected:

No missing or duplicate interest records.

---

# 34. Error Handling Scenarios

## TS-DEP-166 — Deposit service unavailable during creation

**Priority:** P0

Expected:

No false success and no debit.

---

## TS-DEP-167 — Database failure during deposit creation

**Priority:** P0

Expected:

No partial debit/deposit record.

---

## TS-DEP-168 — Network interruption after confirmation

**Priority:** P0

Expected:

Actual state can be safely determined before retry.

---

## TS-DEP-169 — Maturity processor fails

**Priority:** P0

Expected:

Deposit remains recoverable and funds are not lost or duplicated.

---

## TS-DEP-170 — Interest calculation service fails

**Priority:** P0

Expected:

Incorrect amount is not presented as authoritative.

---

# 35. Notification Scenarios

## TS-DEP-171 — Deposit-created notification

**Priority:** P1

Expected:

Correct principal, term, and maturity information.

---

## TS-DEP-172 — Maturity reminder notification

**Priority:** P2

Where supported.

Expected:

Correct customer and maturity date.

---

## TS-DEP-173 — Maturity payout notification

**Priority:** P1

Expected:

Correct payout.

---

## TS-DEP-174 — Auto-renewal notification

**Priority:** P1

Expected:

Correct renewal state and terms.

---

## TS-DEP-175 — Early-withdrawal notification

**Priority:** P1

Expected:

Net payout and resulting status correct.

---

## TS-DEP-176 — Failed deposit creation does not generate success notification

**Priority:** P0

Expected:

No misleading message.

---

# 36. Audit Scenarios

## TS-DEP-177 — Deposit creation generates audit record

**Priority:** P1

Expected:

Contains customer, product, principal, timestamp, and result.

---

## TS-DEP-178 — Early withdrawal audited

**Priority:** P0

Expected:

Action and financial result traceable.

---

## TS-DEP-179 — Maturity processing audited

**Priority:** P1

Expected:

Payout or renewal traceable.

---

## TS-DEP-180 — Auto-renewal preference change audited

**Priority:** P2

Expected:

Change recorded where required.

---

## TS-DEP-181 — Administrative deposit action audited

**Priority:** P0

Expected:

Actor and action traceable.

---

# 37. Security Scenarios

## TS-DEP-182 — Manipulate principal beyond product maximum

**Priority:** P0

Expected:

Backend rejects invalid amount.

---

## TS-DEP-183 — Manipulate interest rate

**Priority:** P0

Expected:

Backend ignores unauthorized client rate.

---

## TS-DEP-184 — Manipulate maturity date

**Priority:** P0

Expected:

Authoritative server calculation enforced.

---

## TS-DEP-185 — Manipulate funding account ID

**Priority:** P0

Expected:

Ownership enforced.

---

## TS-DEP-186 — Manipulate deposit owner ID

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-187 — Replay deposit-creation request

**Priority:** P0

Expected:

No duplicate deposit/debit.

---

## TS-DEP-188 — Replay maturity payout request

**Priority:** P0

Expected:

No duplicate credit.

---

# 38. Search and Filtering Scenarios

## TS-DEP-189 — Customer views active deposits

**Priority:** P1

Expected:

Correct list.

---

## TS-DEP-190 — Customer views matured deposits

**Priority:** P2

Expected:

Historical records.

---

## TS-DEP-191 — Customer views closed deposits

**Priority:** P2

Expected:

Correct history.

---

## TS-DEP-192 — Filter by deposit status

**Priority:** P2

Expected:

Matching records only.

---

## TS-DEP-193 — Search by deposit reference

**Priority:** P1

Expected:

Correct deposit returned.

---

# 39. Cross-Browser Scenarios

## TS-DEP-194 — Deposit creation in Chrome

**Priority:** P1

Expected:

Works correctly.

---

## TS-DEP-195 — Deposit creation in Edge

**Priority:** P1

Expected:

Works correctly.

---

## TS-DEP-196 — Deposit creation in Firefox

**Priority:** P1

Expected:

Works correctly.

---

## TS-DEP-197 — Maturity/withdrawal views across supported browsers

**Priority:** P1

Expected:

No critical browser-specific failure.

---

# 40. Responsive Scenarios

## TS-DEP-198 — Deposit product page on desktop

**Priority:** P2

Expected:

Readable.

---

## TS-DEP-199 — Deposit creation on tablet

**Priority:** P2

Expected:

Usable.

---

## TS-DEP-200 — Deposit creation on mobile

**Priority:** P1

Expected:

Principal, rate, term, and maturity value are clear.

---

## TS-DEP-201 — Early-withdrawal quote on mobile

**Priority:** P0

Expected:

Penalty and net payout fully visible.

---

## TS-DEP-202 — Deposit history on mobile

**Priority:** P2

Expected:

Statuses and values remain understandable.

---

# 41. Accessibility and Usability Scenarios

## TS-DEP-203 — Interest rate clearly labeled

**Priority:** P1

Expected:

No ambiguity.

---

## TS-DEP-204 — Maturity amount clearly displayed

**Priority:** P0

Expected:

Customer understands expected payout.

---

## TS-DEP-205 — Early-withdrawal penalty clearly disclosed

**Priority:** P0

Expected:

Customer sees effect before confirming.

---

## TS-DEP-206 — Deposit form supports keyboard navigation

**Priority:** P2

Expected:

Logical focus order.

---

## TS-DEP-207 — Active/matured/closed states clearly distinguished

**Priority:** P1

Expected:

No ambiguity.

---

# 42. Boundary Scenarios

## TS-DEP-208 — Principal minimum minus 0.01

**Priority:** P1

Expected:

Rejected.

---

## TS-DEP-209 — Principal exact minimum

**Priority:** P1

Expected:

Accepted.

---

## TS-DEP-210 — Principal minimum plus 0.01

**Priority:** P2

Expected:

Accepted.

---

## TS-DEP-211 — Principal maximum minus 0.01

**Priority:** P1

Expected:

Accepted.

---

## TS-DEP-212 — Principal exact maximum

**Priority:** P0

Expected:

Accepted according to product rule.

---

## TS-DEP-213 — Principal maximum plus 0.01

**Priority:** P0

Expected:

Rejected.

---

## TS-DEP-214 — Available balance minus 0.01

**Priority:** P1

Expected:

Accepted if fees permit.

---

## TS-DEP-215 — Principal exactly equals available balance

**Priority:** P0

Expected:

Handled according to reserve/fee rules.

---

## TS-DEP-216 — Principal available balance plus 0.01

**Priority:** P0

Expected:

Rejected.

---

# 43. End-to-End Deposit Scenarios

## TS-DEP-217 — Complete deposit opening journey

**Priority:** P0

Flow:

```text
Login
→ View Deposit Products
→ Select Product
→ Select Funding Account
→ Enter Valid Principal
→ Select Term
→ Review Rate
→ Review Maturity Amount
→ Confirm
→ Verify Account Debit
→ Verify Deposit ACTIVE
→ Verify Transaction History
→ Verify Notification
```

---

## TS-DEP-218 — Deposit maturity journey

**Priority:** P0

Flow:

```text
Active Deposit
→ Reach Maturity Date
→ Process Maturity
→ Verify Principal
→ Verify Interest
→ Verify Settlement Account Credit
→ Verify Deposit CLOSED
→ Verify History
→ Verify Audit
```

---

## TS-DEP-219 — Early withdrawal journey

**Priority:** P0

Flow:

```text
Active Deposit
→ Request Early Withdrawal
→ Review Penalty
→ Review Net Payout
→ Confirm
→ Verify Account Credit
→ Verify Deposit CLOSED
→ Verify Penalty
→ Verify Transaction History
```

---

## TS-DEP-220 — Auto-renewal journey

**Priority:** P0

Flow:

```text
Active Deposit
→ Enable Auto-Renewal
→ Reach Maturity
→ Verify Renewal Executes Once
→ Verify New Term
→ Verify New Rate
→ Verify Deposit ACTIVE
→ Verify Notification
```

---

## TS-DEP-221 — Duplicate deposit protection

**Priority:** P0

Flow:

```text
Prepare Valid Deposit
→ Double Click Confirm
→ Verify One Deposit Created
→ Verify One Principal Debit
→ Verify One Deposit Reference
```

---

## TS-DEP-222 — Concurrent funding protection

**Priority:** P0

Flow:

```text
Available Balance = 10,000
→ Deposit Request = 7,000
→ Transfer Request = 6,000
→ Submit Concurrently
→ Verify Combined Successful Debit Does Not Exceed Available Balance
→ Verify Final Account Balance
→ Verify Deposit/Transfer States
```

---

# 44. Critical Smoke Scenarios

Deposit smoke coverage should include:

```text
TS-DEP-001 — View deposit products
TS-DEP-008 — View own deposit
TS-DEP-015 — Eligible customer
TS-DEP-021 — Create valid deposit
TS-DEP-028 — Valid funding account
TS-DEP-054 — Correct interest rate
TS-DEP-067 — Correct maturity amount
TS-DEP-083 — Deposit becomes ACTIVE
TS-DEP-094 — Principal debited once
```

---

# 45. Critical Regression Scenarios

Always prioritize:

* Deposit ownership
* Eligibility
* Funding-account ownership
* Principal boundaries
* Available balance
* Term
* Interest rate
* Interest calculation
* Maturity amount
* Maturity date
* Duplicate creation
* Principal debit
* Maturity payout
* Duplicate maturity prevention
* Early withdrawal
* Penalties
* Auto-renewal
* State transitions
* Concurrency
* UI/API/database consistency
* Audit logging

---

# 46. Automation Candidates

Strong UI candidates:

* View deposit products
* Create deposit
* Principal validation
* Term selection
* Maturity preview
* Deposit status
* Early withdrawal
* Auto-renewal preferences

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 47. API Automation Candidates

Postman and REST Assured should later cover:

* Deposit products
* Create deposit
* Invalid principal
* Invalid term
* Unauthorized account
* Rate validation
* Deposit details
* Early withdrawal
* Maturity
* Renewal
* Invalid state transitions
* Duplicate creation

---

# 48. SQL Validation Candidates

Database testing should validate:

* Deposit owner
* Funding account
* Principal
* Interest rate
* Term
* Maturity date
* Status
* Interest credits
* Payout records
* Early-withdrawal records
* Renewal relationships
* Audit records

---

# 49. Performance Testing Candidates

JMeter may later cover:

* Deposit-product retrieval
* Deposit creation
* Concurrent deposit creation
* Maturity batch processing
* Interest-credit processing

The main requirement is that increased load must never cause duplicate debits, credits, maturity payouts, or renewals.

---

# 50. BDD Candidates

Example:

```gherkin
Feature: Fixed-term deposit

Scenario: Customer successfully opens a deposit
  Given the customer is active and verified
  And the funding account is active
  And the funding account has sufficient available balance
  When the customer opens a deposit with a valid principal and term
  Then the deposit should become active
  And the funding account should be debited exactly once
  And the deposit should contain the correct interest rate
  And the expected maturity amount should be calculated correctly
```

Early-withdrawal example:

```gherkin
Scenario: Early withdrawal applies the correct penalty
  Given the customer has an active deposit
  And early withdrawal is permitted
  When the customer withdraws the deposit before maturity
  Then the configured early-withdrawal penalty should be applied
  And the correct net amount should be credited
  And the deposit should be closed
```

---

# 51. Risk Traceability

Major related risks include:

```text
RISK-001 — Incorrect account balance
RISK-012 — Incorrect deposit interest calculation
RISK-013 — Concurrent transactions corrupt balance
RISK-021 — Sensitive information exposure
RISK-030 — Unauthorized API request
RISK-037 — Frontend-only validation
RISK-039 — API/database inconsistency
RISK-042 — Slow response causes duplicate submission
RISK-048 — UI reports false success
```

---

# 52. Deposit Coverage Summary

This catalog covers:

* Deposit products
* Ownership
* Authorization
* Eligibility
* Creation
* Funding accounts
* Principal boundaries
* Available balance
* Terms
* Rates
* Interest calculations
* Maturity amount
* Maturity date
* Confirmation
* Statuses
* State transitions
* Principal debit
* Maturity processing
* Early withdrawal
* Penalties
* Auto-renewal
* Periodic interest
* Cancellation
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
* End-to-end workflows

---

# 53. Final Deposit Testing Principle

Deposit testing must verify the complete financial lifecycle, not merely whether a deposit record can be created.

For every critical deposit workflow, QA should answer:

```text
Does the deposit belong to the authenticated customer?

Was the funding account authorized?

Was the principal valid?

Was sufficient available balance present?

Was the account debited exactly once?

Was the correct interest rate applied?

Was interest calculated accurately?

Is the maturity date correct?

Is the maturity amount correct?

Can maturity processing happen twice?

Is early-withdrawal penalty correct?

Can a closed deposit be paid again?

Can renewal and payout happen simultaneously?

Do UI, API, database, transaction history, and statements agree?

Are critical financial actions auditable?
```

A deposit should be considered correct only when principal, interest, lifecycle state, account balances, and payout behavior reconcile across every relevant system layer.
