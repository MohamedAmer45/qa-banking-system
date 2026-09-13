# Banking System — Regression Test Suite

## 1. Document Information

| Field      | Value                          |
| ---------- | ------------------------------ |
| Project    | Banking System Testing Project |
| Test Suite | Regression Testing             |
| Document   | Regression Test Suite          |
| Version    | 1.0                            |
| Status     | Draft                          |
| Owner      | QA Engineering                 |

---

# 2. Purpose

The Regression Test Suite validates that new changes, fixes, releases, infrastructure updates, and configuration changes have not broken existing Banking System functionality.

Regression testing provides broad and deep coverage across:

* Functional behavior
* Financial integrity
* Authentication
* Authorization
* Account state
* Transfers
* Payments
* Cards
* Loans
* Deposits
* Transaction history
* Statements
* Notifications
* Profile/settings
* Admin operations
* Auditability
* APIs
* Database consistency
* Cross-browser behavior
* Error handling
* Concurrency

Regression testing should provide confidence that previously working banking behavior still works correctly after change.

---

# 3. Regression Testing Principle

Regression testing should answer:

```text
What changed?

What could that change affect directly?

What could it affect indirectly?

Which existing banking rules must still hold?

Did the change introduce any financial, security, or data-integrity regression?
```

A banking regression suite should emphasize:

```text
Money
Authorization
State
Persistence
Auditability
Idempotency
Concurrency
```

---

# 4. Regression Suite Naming Convention

Regression tests use:

```text
REG-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

Regression tier:

```text
TIER-1 = Critical regression
TIER-2 = Core regression
TIER-3 = Extended regression
TIER-4 = Full compatibility / exhaustive regression
```

---

# 5. Regression Tiers

## TIER-1 — Critical Regression

Use for:

* Hotfix validation
* Critical banking changes
* Pre-release gates
* High-risk deployments

Covers:

```text
Authentication
Authorization
Balances
Transfers
Payments
Cards
Loan disbursement
Deposit payout
Statements
Critical admin permissions
Audit
```

---

## TIER-2 — Core Regression

Includes TIER-1 plus:

* Beneficiaries
* Profile/settings
* Notifications
* KYC
* Loan lifecycle
* Deposit lifecycle
* Search/filter
* Session management
* Scheduled operations

---

## TIER-3 — Extended Regression

Includes TIER-2 plus:

* Boundary cases
* Additional negative testing
* Browser/device combinations
* Accessibility
* Large datasets
* More concurrency
* Error recovery
* Localization where applicable

---

## TIER-4 — Full Regression

Includes all planned manual and automated regression coverage.

Run before:

* Major production release
* Major architecture change
* Major database migration
* Authentication redesign
* Transaction-engine changes
* Financial-calculation changes

---

# 6. Regression Entry Criteria

Regression can begin when:

* Smoke suite passes.
* Target build is deployed.
* Environment is stable.
* Test data is prepared.
* Critical blockers are resolved or formally accepted.
* Requirements for changes are available.
* Changed modules are identified.
* Known defects are documented.

---

# 7. Regression Exit Criteria

Regression is complete when:

* Required regression tier executed.
* All P0 tests pass.
* No unresolved blocker defects.
* No unresolved critical financial-integrity defects.
* Security and authorization remain acceptable.
* Failed tests are reviewed.
* Regression summary completed.
* Release risk is understood.

---

# 8. Regression Test Selection Strategy

Select tests based on:

```text
Changed module
Shared services
Database changes
API contracts
Business rules
Security impact
Financial impact
State transitions
Dependencies
Previous defect history
Risk assessment
```

Example:

```text
Change:
Transfer fee calculation

Direct coverage:
Transfers
Fees
Balances

Indirect coverage:
Statements
Transaction history
Notifications
Daily limits
Scheduled transfers
Database reconciliation
```

---

# 9. TIER-1 Critical Regression Suite

## Authentication

### REG-001 — Valid customer login

**Priority:** P0

Expected:

Authentication succeeds.

---

### REG-002 — Invalid credentials rejected

**Priority:** P0

Expected:

No authenticated session.

---

### REG-003 — MFA required where enabled

**Priority:** P0

Expected:

Protected access unavailable before MFA completion.

---

### REG-004 — Expired session rejected

**Priority:** P0

---

### REG-005 — Logout invalidates session

**Priority:** P0

---

# 10. Authorization Critical Regression

### REG-006 — Customer cannot access another customer's account

**Priority:** P0

---

### REG-007 — Customer cannot access another customer's transaction

**Priority:** P0

---

### REG-008 — Customer cannot access another customer's statement

**Priority:** P0

---

### REG-009 — Customer cannot access another customer's card

**Priority:** P0

---

### REG-010 — Customer cannot access admin functionality

**Priority:** P0

---

# 11. Account Critical Regression

### REG-011 — View own accounts

**Priority:** P0

---

### REG-012 — Current balance correct

**Priority:** P0

---

### REG-013 — Available balance correct

**Priority:** P0

---

### REG-014 — Frozen account rejects prohibited outgoing transaction

**Priority:** P0

---

### REG-015 — Closed account cannot transact

**Priority:** P0

---

# 12. Transfer Critical Regression

### REG-016 — Valid internal transfer

**Priority:** P0

Verify:

* Source debit
* Destination credit
* Fee
* reference
* history
* final status

---

### REG-017 — Insufficient balance rejected

**Priority:** P0

Expected:

No completed financial effect.

---

### REG-018 — Transfer above limit rejected

**Priority:** P0

---

### REG-019 — Inactive beneficiary rejected

**Priority:** P0

---

### REG-020 — Frozen source account rejected

**Priority:** P0

---

### REG-021 — Duplicate transfer prevented

**Priority:** P0

---

### REG-022 — Transfer retry after timeout is idempotent

**Priority:** P0

---

### REG-023 — Transfer reversal preserves original transaction

**Priority:** P0

---

### REG-024 — Transfer balance reconciliation

**Priority:** P0

Expected:

```text
Opening
- transfer
- fee
= closing
```

---

# 13. Payment Critical Regression

### REG-025 — Successful payment

**Priority:** P0

---

### REG-026 — Insufficient funds payment rejected

**Priority:** P0

---

### REG-027 — Duplicate bill payment prevented

**Priority:** P0

---

### REG-028 — Failed payment leaves balance unchanged

**Priority:** P0

---

### REG-029 — Failed payment does not produce success notification

**Priority:** P0

---

# 14. Card Critical Regression

### REG-030 — Active card usable

**Priority:** P0

---

### REG-031 — Freeze active card

**Priority:** P0

---

### REG-032 — Frozen card rejected

**Priority:** P0

---

### REG-033 — Unfreeze restores valid behavior

**Priority:** P0

---

### REG-034 — Blocked card remains unusable

**Priority:** P0

---

### REG-035 — Card limit enforced

**Priority:** P0

---

# 15. Loan Critical Regression

### REG-036 — Eligible loan application

**Priority:** P1

---

### REG-037 — Authorized loan approval

**Priority:** P0

---

### REG-038 — Unauthorized loan approval rejected

**Priority:** P0

---

### REG-039 — Loan disbursement exactly once

**Priority:** P0

---

### REG-040 — Loan repayment updates outstanding balance

**Priority:** P0

---

### REG-041 — Final loan repayment closes loan correctly

**Priority:** P0

---

# 16. Deposit Critical Regression

### REG-042 — Valid deposit opening

**Priority:** P0

---

### REG-043 — Deposit with insufficient funding rejected

**Priority:** P0

---

### REG-044 — Deposit maturity calculation correct

**Priority:** P0

---

### REG-045 — Deposit maturity payout exactly once

**Priority:** P0

---

### REG-046 — Early withdrawal calculation correct

**Priority:** P0

---

# 17. Transaction History Critical Regression

### REG-047 — Transfer appears exactly once

**Priority:** P0

---

### REG-048 — Payment appears correctly

**Priority:** P1

---

### REG-049 — Reversal linked to original transaction

**Priority:** P0

---

### REG-050 — Failed transaction not shown as successful debit

**Priority:** P0

---

# 18. Statement Critical Regression

### REG-051 — Generate statement

**Priority:** P0

---

### REG-052 — Statement opening balance correct

**Priority:** P0

---

### REG-053 — Statement closing balance reconciles

**Priority:** P0

---

### REG-054 — Fees included correctly

**Priority:** P0

---

### REG-055 — Unauthorized statement access denied

**Priority:** P0

---

# 19. Admin Critical Regression

### REG-056 — Valid admin login

**Priority:** P0

---

### REG-057 — Read-only admin cannot modify state

**Priority:** P0

---

### REG-058 — Authorized account freeze

**Priority:** P0

---

### REG-059 — Unauthorized account freeze rejected

**Priority:** P0

---

### REG-060 — Critical admin action audited

**Priority:** P0

---

# 20. Audit Critical Regression

### REG-061 — Critical financial action traceable

**Priority:** P1

---

### REG-062 — Admin action actor recorded

**Priority:** P0

---

### REG-063 — Audit record does not expose secrets

**Priority:** P0

---

# 21. TIER-2 Authentication Regression

### REG-064 — Account lockout threshold

**Priority:** P0

---

### REG-065 — Correct password rejected while account locked

**Priority:** P0

---

### REG-066 — Password reset valid token

**Priority:** P1

---

### REG-067 — Expired reset token rejected

**Priority:** P0

---

### REG-068 — Used reset token rejected

**Priority:** P0

---

### REG-069 — OTP resend behavior

**Priority:** P1

---

### REG-070 — Reused OTP rejected

**Priority:** P0

---

# 22. Session Regression

### REG-071 — Multiple active sessions behave according to policy

**Priority:** P1

---

### REG-072 — Session revocation works

**Priority:** P0

---

### REG-073 — Password change applies required session policy

**Priority:** P0

---

### REG-074 — Browser Back after logout does not restore authenticated functionality

**Priority:** P0

---

# 23. Beneficiary Regression

### REG-075 — Add valid beneficiary

**Priority:** P1

---

### REG-076 — Duplicate beneficiary handled correctly

**Priority:** P1

---

### REG-077 — Beneficiary verification

**Priority:** P1

---

### REG-078 — Beneficiary cooldown enforced

**Priority:** P0

---

### REG-079 — Disabled beneficiary unusable

**Priority:** P0

---

### REG-080 — Deleted beneficiary unusable

**Priority:** P0

---

### REG-081 — Beneficiary ownership enforced

**Priority:** P0

---

# 24. Transfer Extended Core Regression

### REG-082 — Own-account transfer

**Priority:** P1

---

### REG-083 — Same-bank beneficiary transfer

**Priority:** P1

---

### REG-084 — External transfer where supported

**Priority:** P1

---

### REG-085 — Transfer note persists safely

**Priority:** P2

---

### REG-086 — Daily transfer limit

**Priority:** P0

---

### REG-087 — Monthly transfer limit

**Priority:** P1

---

### REG-088 — Transfer fee tier calculation

**Priority:** P0

---

### REG-089 — Scheduled transfer creation

**Priority:** P1

---

### REG-090 — Scheduled transfer execution

**Priority:** P0

---

### REG-091 — Scheduled transfer cancellation

**Priority:** P0

---

### REG-092 — Recurring transfer execution

**Priority:** P1

---

### REG-093 — Recurring transfer cancellation

**Priority:** P0

---

# 25. Payment Core Regression

### REG-094 — Bill payment

**Priority:** P1

---

### REG-095 — Merchant payment

**Priority:** P1

---

### REG-096 — Scheduled payment

**Priority:** P1

---

### REG-097 — Recurring payment

**Priority:** P1

---

### REG-098 — Payment reversal

**Priority:** P0

---

### REG-099 — Payment fee

**Priority:** P1

---

# 26. Card Core Regression

### REG-100 — Card activation

**Priority:** P1

---

### REG-101 — Card replacement

**Priority:** P1

---

### REG-102 — Expired card rejected

**Priority:** P0

---

### REG-103 — Cancelled card rejected

**Priority:** P0

---

### REG-104 — Purchase at exact limit

**Priority:** P0

---

### REG-105 — Purchase above limit rejected

**Priority:** P0

---

### REG-106 — Refund processing

**Priority:** P1

---

# 27. Loan Core Regression

### REG-107 — Loan amount minimum boundary

**Priority:** P1

---

### REG-108 — Loan amount maximum boundary

**Priority:** P1

---

### REG-109 — Loan term validation

**Priority:** P1

---

### REG-110 — Ineligible KYC loan request

**Priority:** P0

---

### REG-111 — Rejected loan cannot disburse

**Priority:** P0

---

### REG-112 — Partial repayment

**Priority:** P1

---

### REG-113 — Early settlement

**Priority:** P1

---

### REG-114 — Past-due transition

**Priority:** P1

---

# 28. Deposit Core Regression

### REG-115 — Deposit minimum amount

**Priority:** P1

---

### REG-116 — Deposit maximum amount

**Priority:** P1

---

### REG-117 — Deposit term validation

**Priority:** P1

---

### REG-118 — Early-withdrawal eligibility

**Priority:** P1

---

### REG-119 — Auto-renewal

**Priority:** P1

---

### REG-120 — Renewal and payout mutually exclusive

**Priority:** P0

---

# 29. Profile Regression

### REG-121 — View own profile

**Priority:** P1

---

### REG-122 — Valid name/address update

**Priority:** P2

---

### REG-123 — Email change verification

**Priority:** P0

---

### REG-124 — Phone change verification

**Priority:** P0

---

### REG-125 — Password change

**Priority:** P0

---

### REG-126 — MFA enable/disable

**Priority:** P0

---

### REG-127 — Protected fields cannot be modified

**Priority:** P0

---

# 30. Notification Regression

### REG-128 — Transfer success notification

**Priority:** P1

---

### REG-129 — Payment failure notification

**Priority:** P1

---

### REG-130 — Card-state notification

**Priority:** P1

---

### REG-131 — Loan decision notification

**Priority:** P1

---

### REG-132 — Deposit maturity notification

**Priority:** P1

---

### REG-133 — Password-change security alert

**Priority:** P0

---

### REG-134 — Notification ownership

**Priority:** P0

---

### REG-135 — Notification preferences respected

**Priority:** P2

---

# 31. KYC Regression

### REG-136 — Pending KYC displayed

**Priority:** P1

---

### REG-137 — Approve KYC

**Priority:** P0

---

### REG-138 — Reject KYC

**Priority:** P1

---

### REG-139 — Unauthorized KYC decision rejected

**Priority:** P0

---

### REG-140 — Expired KYC affects restricted products

**Priority:** P0

---

# 32. Admin Core Regression

### REG-141 — Customer search

**Priority:** P1

---

### REG-142 — Account search

**Priority:** P1

---

### REG-143 — Restrict customer

**Priority:** P0

---

### REG-144 — Suspend customer

**Priority:** P0

---

### REG-145 — Unfreeze account

**Priority:** P0

---

### REG-146 — Block card

**Priority:** P0

---

### REG-147 — Loan decision

**Priority:** P0

---

### REG-148 — Transaction reversal

**Priority:** P0

---

### REG-149 — Limit configuration authorization

**Priority:** P0

---

### REG-150 — Fee configuration authorization

**Priority:** P0

---

# 33. API Regression

### REG-151 — Authentication endpoint

**Priority:** P0

---

### REG-152 — Accounts endpoint authorization

**Priority:** P0

---

### REG-153 — Beneficiary endpoint ownership

**Priority:** P0

---

### REG-154 — Transfer endpoint validation

**Priority:** P0

---

### REG-155 — Transfer endpoint idempotency

**Priority:** P0

---

### REG-156 — Payment endpoint validation

**Priority:** P0

---

### REG-157 — Card endpoint state enforcement

**Priority:** P0

---

### REG-158 — Loan endpoint role enforcement

**Priority:** P0

---

### REG-159 — Deposit endpoint validation

**Priority:** P0

---

### REG-160 — Statement endpoint ownership

**Priority:** P0

---

### REG-161 — Admin endpoint role enforcement

**Priority:** P0

---

# 34. Database Regression

### REG-162 — Account ownership relationships intact

**Priority:** P0

---

### REG-163 — Transaction reference uniqueness

**Priority:** P0

---

### REG-164 — Transfer balances reconcile in persistence layer

**Priority:** P0

---

### REG-165 — Failed transfer does not create invalid completed transaction

**Priority:** P0

---

### REG-166 — Loan disbursement unique

**Priority:** P0

---

### REG-167 — Deposit payout unique

**Priority:** P0

---

### REG-168 — Audit relationships intact

**Priority:** P1

---

# 35. TIER-3 Boundary Regression

Include key BVA scenarios for:

```text
Transfer min/max
Payment min/max
Card limit
Loan min/max
Loan term
Deposit min/max
Password length
OTP expiry
Session timeout
Beneficiary cooldown
Statement range
Account closure zero balance
Daily limit reset
```

Suggested IDs:

```text
REG-169 through REG-181
```

---

# 36. Negative Input Regression

### REG-182 — Invalid transfer amount type

**Priority:** P1

---

### REG-183 — Negative financial amount

**Priority:** P0

---

### REG-184 — Missing required payload fields

**Priority:** P1

---

### REG-185 — Unsupported enum/state

**Priority:** P1

---

### REG-186 — Over-length input

**Priority:** P2

---

### REG-187 — Script-like content rendered safely

**Priority:** P1

---

### REG-188 — SQL-like input handled safely

**Priority:** P1

---

# 37. Error Handling Regression

### REG-189 — Network failure during transfer

**Priority:** P0

---

### REG-190 — Transfer timeout retry

**Priority:** P0

---

### REG-191 — Payment provider failure

**Priority:** P0

---

### REG-192 — Notification provider failure does not alter financial status

**Priority:** P0

---

### REG-193 — Statement generation failure handled safely

**Priority:** P1

---

### REG-194 — Server error does not expose stack trace

**Priority:** P1

---

# 38. Concurrency Regression

### REG-195 — Concurrent transfers with enough balance

**Priority:** P0

---

### REG-196 — Concurrent transfers exceeding balance

**Priority:** P0

---

### REG-197 — Transfer + payment competing for same balance

**Priority:** P0

---

### REG-198 — Loan double-disbursement prevention

**Priority:** P0

---

### REG-199 — Deposit double-payout prevention

**Priority:** P0

---

### REG-200 — Concurrent transfer reversal

**Priority:** P0

---

### REG-201 — Admin approve/reject race

**Priority:** P0

---

# 39. State Transition Regression

Validate major transitions:

```text
ACTIVE → FROZEN
FROZEN → ACTIVE
ACTIVE → CLOSED

PENDING_ACTIVATION → ACTIVE
ACTIVE → DELETED

PROCESSING → COMPLETED
PROCESSING → FAILED
COMPLETED → REVERSED

INACTIVE → ACTIVE
ACTIVE → FROZEN
FROZEN → ACTIVE
ACTIVE → BLOCKED

UNDER_REVIEW → APPROVED
UNDER_REVIEW → REJECTED
APPROVED → DISBURSED
ACTIVE → CLOSED

PENDING → ACTIVE
ACTIVE → MATURED
MATURED → CLOSED
MATURED → RENEWED
```

Suggested IDs:

```text
REG-202 through REG-219
```

---

# 40. Search / Filter Regression

### REG-220 — Transaction search

**Priority:** P2

---

### REG-221 — Transaction filter

**Priority:** P2

---

### REG-222 — Customer admin search

**Priority:** P2

---

### REG-223 — Combined filters

**Priority:** P2

---

### REG-224 — Empty result

**Priority:** P2

---

# 41. Pagination Regression

### REG-225 — One-page dataset

**Priority:** P2

---

### REG-226 — Multi-page dataset

**Priority:** P2

---

### REG-227 — No duplicates across pages

**Priority:** P1

---

### REG-228 — No missing rows across pages

**Priority:** P1

---

### REG-229 — Stable sort during pagination

**Priority:** P1

---

# 42. Cross-Browser Regression

Critical flows should be run on supported browsers.

## Chrome

```text
Login
Accounts
Transfer
Payment
Cards
Statements
Admin
```

## Edge

```text
Login
Transfer
Payment
Statements
```

## Firefox

```text
Login
Transfer
Cards
Statements
```

## WebKit/Safari where applicable

```text
Login
Transfer
Statement
```

Suggested IDs:

```text
REG-230 through REG-245
```

---

# 43. Responsive Regression

Representative viewports:

```text
1920x1080
1366x768
768x1024
390x844
360x800
```

Validate:

* Login
* Dashboard
* Transfer
* Payment
* Cards
* Statements
* Profile

Suggested IDs:

```text
REG-246 through REG-260
```

---

# 44. Accessibility Regression

### REG-261 — Keyboard navigation

**Priority:** P2

---

### REG-262 — Focus visibility

**Priority:** P2

---

### REG-263 — Form labels

**Priority:** P2

---

### REG-264 — Validation error association

**Priority:** P2

---

### REG-265 — Critical statuses not color-only

**Priority:** P2

---

# 45. Large Dataset Regression

### REG-266 — Large transaction history

**Priority:** P1

---

### REG-267 — Large notification list

**Priority:** P2

---

### REG-268 — Large admin customer list

**Priority:** P2

---

### REG-269 — Large audit log

**Priority:** P1

---

# 46. Temporal Regression

### REG-270 — Midnight/daily limit transition

**Priority:** P0

---

### REG-271 — Month-end recurrence

**Priority:** P1

---

### REG-272 — Year-end scheduled transfer

**Priority:** P1

---

### REG-273 — Leap-day processing

**Priority:** P1

---

### REG-274 — OTP expiry boundary

**Priority:** P0

---

### REG-275 — Session timeout boundary

**Priority:** P0

---

# 47. Precision Regression

### REG-276 — Transfer fee rounding

**Priority:** P0

---

### REG-277 — Loan-interest rounding

**Priority:** P0

---

### REG-278 — Deposit-interest rounding

**Priority:** P0

---

### REG-279 — Statement decimal reconciliation

**Priority:** P0

---

# 48. Security Regression

### REG-280 — Role field injection cannot elevate customer

**Priority:** P0

---

### REG-281 — Customer status cannot be modified by customer

**Priority:** P0

---

### REG-282 — KYC status cannot be self-modified

**Priority:** P0

---

### REG-283 — Direct admin API denied to customer

**Priority:** P0

---

### REG-284 — Another customer's resource ID denied

**Priority:** P0

---

### REG-285 — Expired authentication rejected

**Priority:** P0

---

### REG-286 — Revoked authentication rejected

**Priority:** P0

---

### REG-287 — Sensitive fields absent from normal API responses

**Priority:** P0

---

### REG-288 — Audit logs do not expose secrets

**Priority:** P0

---

# 49. TIER-1 Recommended Execution Set

A compact TIER-1 release regression should include at minimum:

```text
REG-001
REG-003
REG-005
REG-006
REG-008
REG-010
REG-012
REG-014
REG-016
REG-017
REG-021
REG-022
REG-024
REG-025
REG-027
REG-028
REG-031
REG-032
REG-034
REG-039
REG-041
REG-042
REG-045
REG-047
REG-049
REG-053
REG-055
REG-057
REG-058
REG-060
REG-062
REG-063
REG-155
REG-164
REG-166
REG-167
REG-196
REG-198
REG-199
REG-280
REG-284
```

---

# 50. Change-Based Regression Examples

## Transfer Engine Change

Run:

```text
Authentication
Accounts
Beneficiaries
Transfers
Limits
Fees
History
Statements
Notifications
API
Database
Concurrency
Scheduled/recurring transfers
```

---

## Authentication Change

Run:

```text
Login
MFA
Lockout
Password reset
Session
Profile security
Customer/admin authorization
API authentication
```

---

## Database Migration

Run:

```text
Accounts
Balances
Transactions
Statements
Loans
Deposits
Audit logs
Ownership relationships
Critical read/write APIs
```

---

## Card Service Change

Run:

```text
Card lifecycle
Card transactions
Limits
Linked accounts
Refunds
Notifications
History
Statements
```

---

# 51. Regression Execution Status

Use:

```text
NOT_RUN
PASS
FAIL
BLOCKED
SKIPPED
```

---

# 52. Regression Execution Template

| Test ID | Module         | Priority | Tier   | Status  | Defect | Notes |
| ------- | -------------- | -------- | ------ | ------- | ------ | ----- |
| REG-001 | Authentication | P0       | TIER-1 | NOT_RUN | —      | —     |
| REG-016 | Transfers      | P0       | TIER-1 | NOT_RUN | —      | —     |
| REG-025 | Payments       | P0       | TIER-1 | NOT_RUN | —      | —     |
| REG-039 | Loans          | P0       | TIER-1 | NOT_RUN | —      | —     |
| REG-045 | Deposits       | P0       | TIER-1 | NOT_RUN | —      | —     |

Actual execution results will later be maintained in:

```text
manual-testing/test-execution/regression-execution.md
```

---

# 53. Regression Failure Rules

A release should normally be blocked or escalated when regression finds:

```text
Incorrect customer balance

Unauthorized customer data access

Authentication bypass

Duplicate financial transaction

Partial money movement

Frozen/blocked resource still usable

Incorrect loan disbursement

Duplicate deposit payout

Statement mismatch

Critical audit failure

Privilege escalation

Broken core transaction flow
```

---

# 54. Regression Defect Retesting

When a regression defect is fixed:

```text
1. Retest exact defect.
2. Execute related sanity tests.
3. Re-run failed regression case.
4. Re-run immediate dependencies.
5. Run broader regression if shared component changed.
```

Do not mark regression defect closed only because the original UI symptom disappeared.

---

# 55. Regression Automation Strategy

The highest-value regression scenarios should become automated.

## Selenium

Focus:

* Core customer UI
* Admin UI
* Cross-browser flow

## Cypress

Focus:

* Frontend workflows
* API-assisted UI setup
* Fast browser regression

## Playwright

Focus:

* End-to-end workflows
* Cross-browser
* Session/context testing
* Parallel execution
* Network controls

## Jest

Focus:

* Backend/business logic
* Financial calculations
* Utility rules

## Postman / REST Assured

Focus:

* APIs
* Authorization
* Validation
* Idempotency
* Negative scenarios

---

# 56. CI/CD Regression Strategy

Example pipeline:

```text
Build
↓
Jest / Unit
↓
API Smoke
↓
UI Smoke
↓
TIER-1 Regression
↓
Deploy Staging
↓
TIER-2 Regression
↓
Release Candidate
↓
TIER-3 / Full Regression where required
```

Using:

```text
GitHub Actions
Jenkins
```

---

# 57. Parallelization Strategy

Regression can be parallelized by:

```text
Module

Browser

Test data

Automation framework

Risk tier
```

Example:

```text
Worker 1 → Authentication + Accounts
Worker 2 → Transfers + Payments
Worker 3 → Cards + Loans
Worker 4 → Deposits + Statements
Worker 5 → Admin + Security
```

Financial tests must use isolated data to prevent cross-test interference.

---

# 58. Regression Data Strategy

Use:

* Dedicated customers
* Dedicated accounts
* Resettable fixtures
* Deterministic balances
* Unique references
* Isolated loans/deposits
* Separate admin roles

Avoid shared mutable financial data wherever possible.

---

# 59. Regression Metrics

Track:

```text
Total planned

Executed

Passed

Failed

Blocked

Skipped

P0 failures

P1 failures

Defect count

Defect reopen rate

Execution duration

Automation coverage
```

---

# 60. Suggested Regression Dashboard

| Metric                 | Value |
| ---------------------- | ----- |
| Planned Tests          | TBD   |
| Executed               | TBD   |
| Passed                 | TBD   |
| Failed                 | TBD   |
| Blocked                | TBD   |
| P0 Failures            | TBD   |
| P1 Failures            | TBD   |
| Open Critical Defects  | TBD   |
| Automated Regression % | TBD   |
| Overall Status         | TBD   |

---

# 61. Release Recommendation Rules

## Recommend Release

When:

* All P0 regression passes.
* No blocker defect.
* No unacceptable critical defect.
* Financial reconciliation passes.
* Security posture acceptable.
* Residual risks documented.

---

## Conditional Release

Possible when:

* Only accepted low/medium issues remain.
* No financial-integrity risk.
* No security/privacy risk.
* Product/business owner accepts residual risk.

---

## Do Not Release

When:

```text
Money can be lost, duplicated, or miscalculated.

Customer data isolation fails.

Authentication/authorization can be bypassed.

Critical banking workflow is unavailable.

Auditability is materially broken.
```

---

# 62. Regression Traceability

Regression cases should link to:

```text
Requirements
Business rules
Risk IDs
Test scenarios
Defects
Automation cases
```

Example:

```text
REG-021
Duplicate Transfer Prevention

Requirement:
Transaction idempotency

Risk:
RISK-003

Related:
TS-TRF duplicate scenarios

Automation:
REST Assured / Playwright
```

---

# 63. Risk Traceability

Regression heavily mitigates:

```text
RISK-001 — Incorrect balance
RISK-002 — Unauthorized data
RISK-003 — Duplicate transaction
RISK-004 — Partial processing
RISK-005 — Authentication bypass
RISK-006 — Privilege escalation
RISK-007 — Overspending
RISK-008 — Limit bypass
RISK-009 — Frozen account transaction
RISK-010 — Fee calculation
RISK-011 — Loan calculation
RISK-012 — Deposit calculation
RISK-013 — Concurrency
RISK-014 — Session remains active
RISK-015 — Expired session accepted
RISK-016 — Lockout failure
RISK-017 — Beneficiary issue
RISK-018 — Invalid destination
RISK-019 — History mismatch
RISK-020 — Statement mismatch
RISK-021 — Sensitive-data exposure
RISK-022 — Missing audit
RISK-023 — Unauthorized admin
RISK-024 — Frozen card usable
RISK-025 — Blocked card usable
RISK-026 — Failed payment modifies balance
RISK-027 — Duplicate payment
RISK-028 — Scheduled transfer issue
RISK-029 — Cancelled transfer executes
RISK-030 — Unauthorized API
RISK-033 — Duplicate transaction reference
RISK-034 — Incorrect notification
RISK-035 — Browser failure
RISK-036 — Responsive failure
RISK-037 — Frontend-only validation
RISK-038 — UI/API rule mismatch
RISK-039 — API/DB inconsistency
RISK-042 — Retry duplication
RISK-043 — Timezone defects
RISK-044 — OTP reuse
RISK-045 — Expired OTP
RISK-046 — Reset token reuse
RISK-047 — IDOR
RISK-048 — UI/backend mismatch
```

---

# 64. Regression Coverage Summary

The regression suite covers:

* Authentication
* MFA
* Sessions
* Authorization
* Accounts
* Beneficiaries
* Transfers
* Payments
* Cards
* Loans
* Deposits
* Transactions
* Statements
* Notifications
* Profile/settings
* KYC
* Admin
* Audit
* APIs
* Database
* Boundaries
* Negative testing
* Concurrency
* Error recovery
* State transitions
* Search/filtering
* Pagination
* Cross-browser
* Responsive
* Accessibility
* Large datasets
* Dates/timezones
* Precision
* Security

---

# 65. Final Regression Testing Principle

Regression testing should not be treated as:

```text
Run everything every time.
```

It should be:

```text
Risk-based
Change-aware
Traceable
Layered
Automated where valuable
Financially rigorous
```

The key questions are:

```text
Does the old functionality still work?

Did the change alter financial calculations?

Did it weaken authorization?

Did it introduce duplicate processing?

Did it alter lifecycle states?

Did it affect persistence?

Did it break another module?

Do UI, API, and database still agree?

Can the system still be trusted with customer money and data?
```

The core rule is:

```text
Regression testing protects previously working banking behavior
from unintended change.
```
