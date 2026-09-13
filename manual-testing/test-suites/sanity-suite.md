# Banking System — Sanity Test Suite

## 1. Document Information

| Field      | Value                          |
| ---------- | ------------------------------ |
| Project    | Banking System Testing Project |
| Test Suite | Sanity Testing                 |
| Document   | Sanity Test Suite              |
| Version    | 1.0                            |
| Status     | Draft                          |
| Owner      | QA Engineering                 |

---

# 2. Purpose

The Sanity Test Suite provides focused validation after:

* Bug fixes
* Small feature changes
* Configuration updates
* Minor releases
* Module-specific deployments
* Hotfixes

The purpose is to verify that:

```text id="a8m2kj"
The specific change works.

The reported defect is fixed.

The affected workflow remains functional.

Closely related functionality is not obviously broken.
```

Sanity testing is narrower than smoke testing and significantly smaller than full regression testing.

---

# 3. Sanity Testing Principle

Sanity testing should be:

```text id="doa58h"
Focused

Fast

Risk-based

Change-driven

Repeatable

Limited to affected areas and dependencies
```

The central question is:

```text id="ax91qu"
Does this specific change work correctly
without breaking the immediately related functionality?
```

---

# 4. Sanity vs Smoke vs Regression

## Smoke

Purpose:

```text id="6sulda"
Is the overall build stable enough to test?
```

Coverage:

```text id="7iqenf"
Broad + shallow
```

---

## Sanity

Purpose:

```text id="ivvrqv"
Did this specific change/fix work correctly?
```

Coverage:

```text id="tvii9h"
Narrow + moderately deep
```

---

## Regression

Purpose:

```text id="mggvyk"
Did the change break existing functionality elsewhere?
```

Coverage:

```text id="fqxu8i"
Broad + deep
```

---

# 5. Sanity Test Naming Convention

Sanity tests use:

```text id="x0jsvs"
SAN-XXX
```

Priority:

```text id="998ndd"
P0 = Critical
P1 = High
P2 = Medium
```

---

# 6. Entry Criteria

Sanity testing begins when:

* Build/deployment succeeds.
* Smoke testing passes where required.
* Specific change or defect is identified.
* Acceptance criteria are available.
* Environment is stable.
* Required test data exists.
* Relevant dependencies are reachable.
* Fix/build version is known.

---

# 7. Exit Criteria

Sanity testing passes when:

* Target fix/change works.
* Related high-risk paths pass.
* No new blocker/critical defect is introduced.
* No immediate financial-integrity issue exists.
* No authorization/security regression exists.
* Data/state remains consistent.

If sanity fails, broader regression may be postponed until the defect is fixed or risk is accepted.

---

# 8. Sanity Execution Strategy

For each change:

```text id="fa37p5"
1. Verify the exact change.
2. Reproduce the original defect if applicable.
3. Confirm the defect no longer occurs.
4. Test nearby positive path.
5. Test nearby negative path.
6. Test relevant boundary.
7. Verify affected API/backend behavior.
8. Verify financial/database impact where applicable.
9. Verify related state transitions.
10. Decide whether broader regression is required.
```

---

# 9. Change Impact Classification

## Low Risk

Examples:

* Label change
* Cosmetic issue
* Non-financial display formatting

Recommended sanity:

```text id="jik1vz"
Targeted UI verification
+ nearby navigation
```

---

## Medium Risk

Examples:

* Search/filter fix
* Notification preference update
* Profile validation change

Recommended sanity:

```text id="ciulxf"
Target feature
+ related API
+ nearby workflows
```

---

## High Risk

Examples:

* Transfer fix
* Balance calculation
* Card state handling
* Loan disbursement
* Deposit maturity
* Authorization
* Session handling

Recommended sanity:

```text id="tmysw9"
Target fix
+ API
+ database validation
+ state validation
+ financial reconciliation
+ focused regression
```

---

# 10. Authentication Fix Sanity

## SAN-001 — Validate login fix

**Priority:** P0

Use when change affects login.

Verify:

* Valid login works.
* Invalid password rejected.
* Session created only after successful authentication.
* Redirect correct.
* No authentication bypass introduced.

---

## SAN-002 — Validate account lockout fix

**Priority:** P0

Verify:

* Attempts below threshold.
* Exact threshold.
* Locked account blocked.
* Correct password during lockout does not bypass security.

---

## SAN-003 — Validate MFA fix

**Priority:** P0

Verify:

* Correct code accepted.
* Incorrect code rejected.
* Expired code rejected.
* Reused code rejected.
* Protected resources inaccessible before completion.

---

## SAN-004 — Validate logout/session fix

**Priority:** P0

Verify:

```text id="1ldxbr"
Login
→ Logout
→ Reuse session
→ Access denied
```

Also test browser Back and direct API access.

---

# 11. Password Reset Sanity

## SAN-005 — Password reset fix

**Priority:** P0

Verify:

* Valid token works.
* Expired token rejected.
* Used token rejected.
* New password works.
* Old password fails.
* Reset notification generated where applicable.

---

## SAN-006 — Multiple reset-token handling

**Priority:** P0

Verify latest-token policy and invalidation rules.

---

# 12. Account Fix Sanity

## SAN-007 — Account balance display fix

**Priority:** P0

Verify:

* Dashboard balance.
* Account-detail balance.
* API balance.
* Database balance where checked.

Values must agree.

---

## SAN-008 — Available-balance fix

**Priority:** P0

Verify:

* Pending hold included correctly.
* Settled transaction updates correctly.
* Failed transaction releases hold.

---

## SAN-009 — Account freeze fix

**Priority:** P0

Verify:

```text id="5n0cp7"
ACTIVE
→ Freeze
→ FROZEN
→ Transfer rejected
→ Payment rejected where applicable
→ Unfreeze
→ ACTIVE
```

---

## SAN-010 — Account closure fix

**Priority:** P0

Verify:

* Eligible account closes.
* Non-zero balance prevents closure.
* Pending transaction prevents closure.
* Closed account cannot transact.

---

# 13. Beneficiary Sanity

## SAN-011 — Beneficiary creation fix

**Priority:** P1

Verify:

* Valid beneficiary created.
* Duplicate handling correct.
* Ownership correct.
* Verification state correct.

---

## SAN-012 — Beneficiary activation fix

**Priority:** P0

Verify:

* Pending beneficiary cannot transfer.
* Cooldown enforced.
* Active beneficiary works after valid activation.

---

## SAN-013 — Beneficiary deletion fix

**Priority:** P1

Verify:

* Delete succeeds.
* Deleted beneficiary cannot be used.
* Historical transfers remain intact.

---

# 14. Transfer Fix Sanity

## SAN-014 — Successful transfer after fix

**Priority:** P0

Verify:

```text id="fj2hj6"
Correct source
Correct destination
Correct amount
Correct fee
Correct reference
Correct source debit
Correct destination credit
```

---

## SAN-015 — Insufficient funds fix

**Priority:** P0

Verify:

* Exact valid balance.
* Just-over-balance request.
* Fee included in total debit.
* Rejected attempt causes no balance change.

---

## SAN-016 — Transfer limit fix

**Priority:** P0

Verify:

```text id="kz8qlb"
Below limit
At limit
Above limit
```

Check UI + API.

---

## SAN-017 — Duplicate transfer fix

**Priority:** P0

Reproduce original failure mechanism:

* Double-click
* Retry
* Replay
* Two tabs

Expected:

```text id="0joj4h"
One intended transfer only.
```

---

## SAN-018 — Transfer fee fix

**Priority:** P0

Verify:

* Correct fee rule.
* Correct display.
* Correct debit.
* Correct statement/history representation.

---

## SAN-019 — Transfer status fix

**Priority:** P0

Verify:

```text id="5h734t"
PENDING
PROCESSING
COMPLETED
FAILED
CANCELLED
REVERSED
```

Only relevant transition should occur.

---

# 15. Scheduled Transfer Sanity

## SAN-020 — Scheduled transfer execution fix

**Priority:** P0

Verify:

* Schedule created.
* Executes at valid time.
* Transaction appears once.

---

## SAN-021 — Scheduled transfer cancellation fix

**Priority:** P0

Verify:

```text id="htq7hv"
Schedule
→ Cancel
→ Wait for execution time
→ No transfer occurs
```

---

## SAN-022 — Scheduled transfer balance validation fix

**Priority:** P0

Verify execution-time balance, not only creation-time balance.

---

# 16. Recurring Transfer Sanity

## SAN-023 — Recurring transfer occurrence fix

**Priority:** P1

Verify one expected occurrence.

---

## SAN-024 — Recurring transfer cancellation fix

**Priority:** P0

Verify no future occurrence after cancellation.

---

## SAN-025 — Recurring transfer failed occurrence

**Priority:** P1

Verify failure behavior does not duplicate later executions.

---

# 17. Payment Fix Sanity

## SAN-026 — Successful payment fix

**Priority:** P0

Verify:

* Correct bill/payee.
* Correct amount.
* Correct account debit.
* Correct reference.

---

## SAN-027 — Failed payment balance fix

**Priority:** P0

Verify failed payment does not alter settled balance.

---

## SAN-028 — Duplicate bill payment fix

**Priority:** P0

Re-attempt same unique bill through:

* UI
* API
* concurrent submission

Expected:

One settlement.

---

## SAN-029 — Payment notification fix

**Priority:** P1

Verify status and notification agree.

---

# 18. Card Fix Sanity

## SAN-030 — Card activation fix

**Priority:** P1

Verify:

```text id="3ner9u"
INACTIVE → ACTIVE
```

and active transaction succeeds.

---

## SAN-031 — Card freeze fix

**Priority:** P0

Verify:

```text id="uvd41b"
ACTIVE → FROZEN
```

followed by rejected purchase.

---

## SAN-032 — Card unfreeze fix

**Priority:** P0

Verify:

```text id="ojv5ny"
FROZEN → ACTIVE
```

and valid transaction resumes.

---

## SAN-033 — Card block fix

**Priority:** P0

Verify blocked card cannot transact through UI or API.

---

## SAN-034 — Card limit fix

**Priority:** P0

Verify:

```text id="cw0xl5"
Below
At
Above
```

configured limit.

---

## SAN-035 — Card replacement fix

**Priority:** P1

Verify:

* Replacement created.
* Old card behavior correct.
* Duplicate replacement prevented.

---

# 19. Loan Fix Sanity

## SAN-036 — Loan eligibility fix

**Priority:** P1

Verify:

* Eligible customer accepted.
* Clearly ineligible customer rejected.
* Relevant KYC/customer-state rule enforced.

---

## SAN-037 — Loan approval fix

**Priority:** P0

Verify:

```text id="8jif98"
UNDER_REVIEW → APPROVED
```

and unauthorized role cannot perform approval.

---

## SAN-038 — Loan disbursement fix

**Priority:** P0

Verify:

* Credit exactly once.
* Correct amount.
* Correct account.
* Loan state correct.
* Transaction created.

---

## SAN-039 — Loan repayment fix

**Priority:** P0

Verify:

* Correct debit.
* Principal/interest allocation correct.
* Outstanding balance decreases.

---

## SAN-040 — Final loan repayment fix

**Priority:** P0

Verify:

```text id="7fm5c5"
Outstanding balance → 0
Loan → CLOSED
```

No residual monetary value remains.

---

# 20. Deposit Fix Sanity

## SAN-041 — Deposit opening fix

**Priority:** P0

Verify:

* Funding debit exactly once.
* Principal correct.
* Deposit state correct.

---

## SAN-042 — Deposit maturity fix

**Priority:** P0

Verify:

* Maturity recognized.
* Interest correct.
* Payout correct.
* One settlement only.

---

## SAN-043 — Deposit duplicate payout fix

**Priority:** P0

Repeat maturity processing.

Expected:

No second payout.

---

## SAN-044 — Early withdrawal fix

**Priority:** P0

Verify:

* Eligibility.
* Penalty.
* Interest treatment.
* Net payout.
* final state.

---

# 21. Transaction History Fix Sanity

## SAN-045 — Missing transaction fix

**Priority:** P0

Generate known transaction and verify it appears.

---

## SAN-046 — Duplicate transaction-history row fix

**Priority:** P1

Verify exactly one expected row per financial event.

---

## SAN-047 — Transaction status fix

**Priority:** P0

Verify final authoritative status.

---

## SAN-048 — Reversal-history fix

**Priority:** P0

Verify original transaction remains plus linked reversal.

---

# 22. Statement Fix Sanity

## SAN-049 — Statement transaction inclusion fix

**Priority:** P0

Verify known in-range transactions included exactly once.

---

## SAN-050 — Statement balance fix

**Priority:** P0

Reconcile:

```text id="z2v4sr"
Opening + Credits - Debits = Closing
```

---

## SAN-051 — Statement fee fix

**Priority:** P0

Verify fees affect closing balance correctly.

---

## SAN-052 — Statement authorization fix

**Priority:** P0

Verify another customer's statement remains inaccessible.

---

# 23. Notification Fix Sanity

## SAN-053 — Transaction success notification fix

**Priority:** P1

Verify successful transaction produces correct status.

---

## SAN-054 — False success notification fix

**Priority:** P0

Force transaction failure.

Expected:

No success notification.

---

## SAN-055 — Duplicate notification fix

**Priority:** P1

Repeat/retry event.

Expected:

No unintended duplicate notification.

---

## SAN-056 — Notification ownership fix

**Priority:** P0

Another customer cannot access notification.

---

# 24. Profile / Settings Sanity

## SAN-057 — Profile update fix

**Priority:** P1

Verify valid update persists.

---

## SAN-058 — Email change fix

**Priority:** P0

Verify:

* Verification required.
* New email authoritative after completion.
* old email behavior correct.
* notification created.

---

## SAN-059 — Password change fix

**Priority:** P0

Verify old/new password and session policy.

---

## SAN-060 — MFA settings fix

**Priority:** P0

Verify security verification required.

---

# 25. Security Fix Sanity

## SAN-061 — IDOR authorization fix

**Priority:** P0

Use Customer A and Customer B.

Verify fixed endpoint denies cross-customer access.

Then check nearby related resources.

Example:

```text id="a1gov7"
Fix: Statement authorization

Sanity:
Statement metadata
Statement download
Transaction endpoint
Account endpoint
```

---

## SAN-062 — Admin authorization fix

**Priority:** P0

Verify:

* Target unauthorized role rejected.
* Authorized role still works.
* UI and API consistent.

---

## SAN-063 — Session expiration fix

**Priority:** P0

Verify protected API/action after timeout is rejected.

---

## SAN-064 — Sensitive-data exposure fix

**Priority:** P0

Verify removed data no longer appears in:

* API
* UI
* Errors
* logs available to tester
* URLs

---

# 26. Admin Fix Sanity

## SAN-065 — Customer status change fix

**Priority:** P0

Verify:

* Valid transition.
* Reason.
* Immediate effect.
* Audit record.

---

## SAN-066 — Account freeze admin fix

**Priority:** P0

Verify:

```text id="9el8fq"
Admin freezes
→ Account state changes
→ Customer financial restriction enforced
→ Audit created
```

---

## SAN-067 — KYC review fix

**Priority:** P1

Verify correct reviewer permission and resulting state.

---

## SAN-068 — Transaction reversal fix

**Priority:** P0

Verify:

* Eligible transaction reversed.
* balances correct.
* original preserved.
* second reversal rejected.
* audit generated.

---

# 27. Audit Fix Sanity

## SAN-069 — Missing audit event fix

**Priority:** P0

Repeat target operation and verify audit creation.

---

## SAN-070 — Incorrect audit actor fix

**Priority:** P0

Verify correct authenticated actor.

---

## SAN-071 — Sensitive data redaction fix

**Priority:** P0

Verify secrets no longer appear in audit records.

---

# 28. Search / Filter Sanity

## SAN-072 — Search fix

**Priority:** P2

Verify:

```text id="s6kc3c"
Exact match
Partial match
No match
```

---

## SAN-073 — Filter fix

**Priority:** P2

Verify relevant filter plus reset/default behavior.

---

## SAN-074 — Combined search + filter fix

**Priority:** P2

Verify interaction.

---

# 29. Pagination Sanity

## SAN-075 — Page boundary fix

**Priority:** P2

Verify records around page transition.

---

## SAN-076 — Duplicate/missing row pagination fix

**Priority:** P1

Verify:

```text id="82k4cm"
No duplicate rows
No missing rows
Stable ordering
```

---

# 30. API Fix Sanity

## SAN-077 — API validation fix

**Priority:** P1

Verify:

* originally failing payload
* valid payload
* nearby invalid payload

---

## SAN-078 — API authorization fix

**Priority:** P0

Verify:

```text id="w33gs0"
Authorized credential → allowed
Unauthorized credential → denied
Missing credential → denied
```

---

## SAN-079 — API idempotency fix

**Priority:** P0

Submit same financial request twice.

Expected:

One financial effect.

---

## SAN-080 — API status-code/error fix

**Priority:** P1

Verify correct response while ensuring business state remains correct.

---

# 31. Database Fix Sanity

## SAN-081 — Persistence fix

**Priority:** P0

Verify UI/API success results in correct database state.

---

## SAN-082 — Failed transaction persistence fix

**Priority:** P0

Verify rejected/failed transaction does not create invalid completed records.

---

## SAN-083 — Relationship integrity fix

**Priority:** P0

Verify ownership/foreign-key relationship correct.

---

## SAN-084 — Duplicate record fix

**Priority:** P0

Repeat operation and confirm uniqueness rule.

---

# 32. Concurrency Fix Sanity

## SAN-085 — Concurrent transfer fix

**Priority:** P0

Two debits compete for limited balance.

Expected:

No overspending.

---

## SAN-086 — Concurrent loan disbursement fix

**Priority:** P0

Two requests.

Expected:

One credit.

---

## SAN-087 — Concurrent deposit maturity fix

**Priority:** P0

Two processors.

Expected:

One payout.

---

## SAN-088 — Concurrent reversal fix

**Priority:** P0

Two reversal requests.

Expected:

One reversal.

---

# 33. Time / Scheduling Fix Sanity

## SAN-089 — Timezone scheduling fix

**Priority:** P1

Verify intended execution time in supported timezone.

---

## SAN-090 — Month-end fix

**Priority:** P1

Verify affected recurring/scheduled workflow.

---

## SAN-091 — Leap-date fix

**Priority:** P1

Verify February 29 behavior.

---

## SAN-092 — Daily-limit reset fix

**Priority:** P0

Verify counter immediately before/after reset.

---

# 34. Precision / Rounding Fix Sanity

## SAN-093 — Fee rounding fix

**Priority:** P0

Verify:

```text id="89pxg3"
Below midpoint
At midpoint
Above midpoint
```

---

## SAN-094 — Loan interest rounding fix

**Priority:** P0

Verify installment sum and total repayable reconcile.

---

## SAN-095 — Deposit interest rounding fix

**Priority:** P0

Verify credited amount matches authoritative calculation.

---

# 35. UI Fix Sanity

## SAN-096 — Validation message fix

**Priority:** P2

Verify target validation and adjacent valid input.

---

## SAN-097 — Confirmation dialog fix

**Priority:** P1

For financial action verify:

* correct account
* beneficiary/payee
* amount
* fee
* final total

---

## SAN-098 — Responsive layout fix

**Priority:** P2

Verify changed breakpoint plus nearby viewport.

---

## SAN-099 — Browser-specific fix

**Priority:** P2

Verify:

* originally affected browser
* one unaffected browser as regression check

---

# 36. Example Bug-Fix Sanity — Duplicate Transfer

Original defect:

```text id="1xtyrv"
BUG-001:
Double-clicking transfer confirmation creates two transactions.
```

Sanity suite:

```text id="g2xydo"
SAN-017 — Repeat double-click
SAN-014 — Normal valid transfer
SAN-015 — Insufficient balance
SAN-079 — API duplicate request
SAN-084 — Database uniqueness
SAN-085 — Concurrent transfer
```

This verifies both:

```text id="23oh0g"
The specific bug
and
the surrounding financial workflow.
```

---

# 37. Example Bug-Fix Sanity — Frozen Account

Original defect:

```text id="h85joj"
Transfer succeeds after administrator freezes account.
```

Sanity:

```text id="fwxcbo"
Freeze account
Verify status
Transfer through UI
Transfer through API
Payment attempt
Unfreeze
Valid transfer
Audit record
```

Relevant tests:

```text id="0e4t54"
SAN-009
SAN-014
SAN-066
SAN-078
```

---

# 38. Example Bug-Fix Sanity — Statement Fee

Original defect:

```text id="a60j5e"
Statement excludes transaction fee from closing balance.
```

Sanity:

```text id="wwf338"
Generate payment with fee
Verify transaction history
Generate statement
Verify fee row
Recalculate closing balance
Compare account/API/database
```

Relevant:

```text id="z2i4fu"
SAN-049
SAN-050
SAN-051
SAN-081
```

---

# 39. Example Bug-Fix Sanity — MFA Bypass

Original defect:

```text id="jy97fv"
Protected API accessible after password validation before MFA.
```

Sanity:

```text id="1yq8qx"
Valid credentials
Stop at MFA
Call accounts API
Call transfer API
Open dashboard URL
Complete MFA
Repeat API calls
```

Expected:

```text id="ibqamv"
Before MFA → denied
After MFA → authorized according to permissions
```

---

# 40. Example Feature Sanity — New Transfer Fee Rule

Change:

```text id="5u84go"
New transfer fee rule deployed.
```

Sanity coverage:

```text id="1filze"
No-fee tier
Fixed-fee tier
Percentage-fee tier
Threshold boundary
Insufficient balance including fee
Transaction history
Statement
API
Database
```

This is more focused than running the entire regression suite.

---

# 41. Example Feature Sanity — New Card Limit

Change:

```text id="lk3r7p"
Customers may now change daily card purchase limit.
```

Sanity:

```text id="kzfjys"
View current limit
Change to valid value
Purchase below limit
Purchase at limit
Purchase above limit
Invalid limit change
Frozen card behavior
API validation
Persistence
```

---

# 42. Sanity Test Selection Rules

When selecting sanity tests, include:

```text id="bbbv2j"
Exact changed behavior

Main valid path

Main invalid path

Most important boundary

Immediate upstream dependency

Immediate downstream dependency

Security/authorization impact

Financial impact

Persistence impact
```

Do not automatically execute unrelated modules unless impact analysis indicates risk.

---

# 43. Impact Analysis Questions

Before sanity execution ask:

```text id="3bhdxk"
What code/module changed?

What API changed?

What database table/schema changed?

What business rule changed?

What calls this component?

What does this component call?

Does it touch money?

Does it touch authorization?

Does it change lifecycle state?

Does it affect audit or notifications?

Could retries or concurrency matter?
```

---

# 44. Sanity Execution Template

```text id="uv3sq5"
Sanity Run ID:
Build:
Environment:
Change / Defect:
Affected Module:
Tester:
Date:

Tests Selected:
Tests Passed:
Tests Failed:
Tests Blocked:

Original Issue Reproduced Before Fix:
YES / NO / N/A

Original Issue Fixed:
YES / NO

Related Workflow Stable:
YES / NO

Critical Defects:
High Defects:

Recommendation:
PROCEED / DO NOT PROCEED / BROADER REGRESSION REQUIRED
```

---

# 45. Sanity Result Table Template

| Test ID | Area          | Status  | Defect | Notes |
| ------- | ------------- | ------- | ------ | ----- |
| SAN-XXX | Target fix    | NOT_RUN | —      | —     |
| SAN-XXX | Positive path | NOT_RUN | —      | —     |
| SAN-XXX | Negative path | NOT_RUN | —      | —     |
| SAN-XXX | API           | NOT_RUN | —      | —     |
| SAN-XXX | Database      | NOT_RUN | —      | —     |

---

# 46. Sanity Failure Rules

Sanity should fail immediately if the target change:

```text id="9m3hyw"
Still reproduces the original defect

Creates incorrect balance

Creates duplicate financial effect

Introduces authorization bypass

Breaks main valid workflow

Corrupts database state

Creates invalid lifecycle transition

Produces false success
```

---

# 47. When to Escalate From Sanity to Regression

Run broader regression when:

* Shared financial service changed.
* Authentication/session code changed.
* Authorization framework changed.
* Database schema changed.
* Transaction engine changed.
* Calculation engine changed.
* Common API middleware changed.
* Major dependency upgraded.
* Multiple modules show unexpected behavior.
* Sanity finds an unrelated regression.

---

# 48. When Full Regression May Not Be Necessary

A small targeted change may require sanity only when:

```text id="zsgctx"
Risk is low

Impact is isolated

No shared logic changed

No financial/security behavior changed

Sanity passes

Release policy permits limited regression
```

Example:

```text id="zhkhto"
Text alignment on notification settings screen.
```

---

# 49. Sanity Automation Strategy

Frequently repeated sanity scenarios should be automated.

Suitable tools:

```text id="jgoea3"
Selenium
Cypress
Playwright
Postman
REST Assured
Jest
```

Good automation targets:

* Bug regressions
* Login/MFA
* Transfer validation
* Duplicate prevention
* Card states
* API authorization
* financial calculations

---

# 50. CI/CD Application

For high-risk hotfixes:

```text id="5m4r0p"
Build
→ Deploy
→ Smoke
→ Targeted Sanity
→ Focused Regression
→ Release Decision
```

With:

```text id="2gtt3j"
GitHub Actions
or
Jenkins
```

---

# 51. Sanity and Database Validation

If a changed feature writes data, sanity should verify persistence.

Examples:

```text id="gbjnvg"
Transfer fix
→ transaction + balance

Loan fix
→ loan + repayment records

Deposit fix
→ deposit + payout

Admin fix
→ target state + audit
```

---

# 52. Sanity and API Validation

For UI fixes involving important business logic, verify whether the backend changed.

Example:

```text id="7ze4tq"
UI transfer limit bug fixed
```

Test both:

```text id="aqgsnl"
UI request
Direct API request
```

The backend remains authoritative.

---

# 53. Sanity and Exploratory Testing

A short exploratory session is useful after complex bug fixes.

Example:

```text id="lpp13o"
Duplicate transaction fix
```

Run:

```text id="y8t1p8"
30–60 minute exploratory charter:
refresh
retry
Back
double click
multiple tabs
network timeout
```

before considering the fix fully trusted.

---

# 54. Risk Traceability

Sanity testing commonly validates fixes related to:

```text id="b0q8mq"
RISK-001 — Incorrect balance
RISK-002 — Unauthorized data access
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
RISK-014 — Session after logout
RISK-015 — Expired session
RISK-019 — History mismatch
RISK-020 — Statement mismatch
RISK-024 — Frozen card
RISK-025 — Blocked card
RISK-026 — Failed payment balance impact
RISK-027 — Duplicate payment
RISK-029 — Cancelled scheduled transfer
RISK-030 — Unauthorized API
RISK-034 — Incorrect notification
RISK-037 — Client-only validation
RISK-039 — API/DB inconsistency
RISK-047 — IDOR
RISK-048 — UI/backend mismatch
```

---

# 55. Sanity Coverage Summary

This suite provides targeted sanity coverage for:

* Authentication
* Lockout
* MFA
* Password reset
* Sessions
* Accounts
* Account balances
* Account states
* Beneficiaries
* Transfers
* Transfer limits
* Duplicate transfers
* Fees
* Scheduled transfers
* Recurring transfers
* Payments
* Cards
* Loans
* Deposits
* Transaction history
* Statements
* Notifications
* Profiles
* Security
* Admin
* Audit
* Search
* Pagination
* APIs
* Database persistence
* Concurrency
* Scheduling
* Timezones
* Precision
* UI fixes

---

# 56. Final Sanity Testing Principle

Sanity testing should answer:

```text id="gdx85b"
Was the specific issue fixed?

Does the main valid path still work?

Does the most important invalid path still fail safely?

Did the fix introduce a nearby regression?

Are financial effects correct?

Is authorization still enforced?

Is persistent state correct?

Should we proceed to broader regression?
```

For high-risk banking changes, sanity should never stop at:

```text id="aa0lus"
The UI looks fixed.
```

Instead verify:

```text id="yvff7i"
UI

API

Business rule

Financial effect

Database state

Authorization

Related lifecycle state
```

The core rule is:

```text id="e33ku3"
Verify the exact fix first,
then verify the smallest meaningful circle of risk around it.
```
