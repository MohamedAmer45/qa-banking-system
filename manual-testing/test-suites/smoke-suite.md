# Banking System — Smoke Test Suite

## 1. Document Information

| Field      | Value                          |
| ---------- | ------------------------------ |
| Project    | Banking System Testing Project |
| Test Suite | Smoke Testing                  |
| Document   | Smoke Test Suite               |
| Version    | 1.0                            |
| Status     | Draft                          |
| Owner      | QA Engineering                 |

---

# 2. Purpose

The Smoke Test Suite provides a fast, high-value validation of the Banking System after:

* New deployment
* New build
* Environment refresh
* Infrastructure change
* Major configuration change
* Emergency hotfix
* Backend/API deployment
* Database migration

The objective is to determine whether the application is stable enough for deeper testing.

Smoke testing answers:

```text id="gv78v9"
Can users authenticate?

Can they access their accounts?

Can the system move money correctly?

Are critical financial rules enforced?

Are customer data and permissions protected?

Do the major banking modules load and function?

Is the build stable enough for regression testing?
```

---

# 3. Smoke Testing Principle

Smoke tests should be:

```text id="wm5zdn"
Fast

Stable

Repeatable

High-value

Focused on critical paths

Independent where possible
```

Smoke testing should not attempt to validate every edge case.

Instead, it verifies that the most important workflows are operational.

---

# 4. Smoke Test Scope

The smoke suite covers:

* Application availability
* Authentication
* MFA
* Accounts
* Beneficiaries
* Transfers
* Payments
* Cards
* Loans
* Deposits
* Transaction history
* Statements
* Notifications
* Profile/settings
* Security/authorization
* Admin functionality
* Audit logging

---

# 5. Smoke Suite Naming Convention

Smoke tests use:

```text id="8yfnfm"
SMK-XXX
```

Priority:

```text id="rtsq2d"
P0 = Critical
P1 = High
```

Smoke tests should normally contain only:

```text id="fvk5ms"
P0
and
selected P1
```

coverage.

---

# 6. Entry Criteria

Smoke testing can begin when:

* Deployment completed successfully.
* Application URL is reachable.
* Required services are running.
* Database connection is healthy.
* Test users exist.
* Test accounts contain required balances.
* Required feature flags are configured.
* APIs are reachable.
* Build/version is known.
* Environment health checks pass.

---

# 7. Exit Criteria

Smoke suite passes when:

* All P0 smoke tests pass.
* No critical financial-integrity defect exists.
* No authentication bypass exists.
* No major authorization failure exists.
* No major service/module is unavailable.
* No blocker defect prevents core banking flows.

Smoke should fail when any critical banking workflow cannot be trusted.

---

# 8. Smoke Test Data

Example baseline:

```text id="vk3slk"
Customer A:
CUST-001

Customer B:
CUST-002

Source Account:
ACC-001
Status = ACTIVE
Balance = 100,000.00

Destination Account:
ACC-002
Status = ACTIVE

Beneficiary:
BEN-001
Status = ACTIVE

Card:
CARD-001
Status = ACTIVE

Loan:
LOAN-001

Deposit:
DEP-001

Admin:
ADMIN-001
```

Synthetic test data only.

---

# 9. Application Availability

## SMK-001 — Application loads successfully

**Priority:** P0

Expected:

* Application responds.
* No fatal server error.
* Main login page loads.
* Static resources required for operation load correctly.

---

## SMK-002 — Backend/API health available

**Priority:** P0

Expected:

Critical backend services respond.

---

## SMK-003 — Database-dependent functionality responds

**Priority:** P0

Expected:

Application can retrieve persistent customer/account data.

---

# 10. Authentication Smoke Tests

## SMK-004 — Valid customer login

**Priority:** P0

Preconditions:

```text id="2i14ke"
Customer status = ACTIVE
Valid credentials available
```

Steps:

1. Open login page.
2. Enter valid credentials.
3. Submit.

Expected:

Customer authenticates successfully.

---

## SMK-005 — Invalid password rejected

**Priority:** P0

Expected:

* Login denied.
* No session created.
* Protected pages remain inaccessible.

---

## SMK-006 — Valid MFA authentication

**Priority:** P0

Where MFA is enabled.

Expected:

```text id="5kjtez"
Credentials accepted
→ MFA challenge
→ Valid code accepted
→ Authenticated session created
```

---

## SMK-007 — Protected page blocked before MFA completion

**Priority:** P0

Expected:

Customer cannot bypass MFA by direct navigation.

---

## SMK-008 — Logout invalidates session

**Priority:** P0

Steps:

1. Login.
2. Logout.
3. Attempt protected page/API.

Expected:

Reauthentication required.

---

# 11. Account Smoke Tests

## SMK-009 — Customer views own accounts

**Priority:** P0

Expected:

Only owned accounts are displayed.

---

## SMK-010 — Account balance loads correctly

**Priority:** P0

Expected:

Current and available balance values are returned without error.

---

## SMK-011 — Customer cannot access another customer's account

**Priority:** P0

Expected:

Authorization denied.

---

## SMK-012 — Frozen account rejects prohibited outgoing transaction

**Priority:** P0

Expected:

Backend enforces account status.

---

# 12. Beneficiary Smoke Tests

## SMK-013 — Customer views beneficiaries

**Priority:** P1

Expected:

Only customer's beneficiaries are displayed.

---

## SMK-014 — Active beneficiary can be selected for transfer

**Priority:** P0

Expected:

Active authorized beneficiary available.

---

## SMK-015 — Inactive beneficiary cannot be used

**Priority:** P0

Expected:

Transfer rejected.

---

# 13. Transfer Smoke Tests

## SMK-016 — Successful internal transfer

**Priority:** P0

Preconditions:

```text id="x6vyhn"
Source account = ACTIVE
Beneficiary = ACTIVE
Balance sufficient
Amount within limit
```

Example:

```text id="jv1oq3"
Opening balance = 100,000.00
Transfer = 1,000.00
Fee = 10.00
```

Expected source balance:

```text id="823j20"
98,990.00
```

Expected:

* Transfer status COMPLETED.
* Correct debit.
* Correct credit.
* Correct fee.
* Unique reference.
* Transaction history updated.

---

## SMK-017 — Insufficient funds transfer rejected

**Priority:** P0

Expected:

* Transfer rejected.
* Balance unchanged.
* No completed transfer record.
* No success notification.

---

## SMK-018 — Transfer above configured limit rejected

**Priority:** P0

Expected:

Backend rejects transaction.

---

## SMK-019 — Transfer from frozen account rejected

**Priority:** P0

Expected:

No financial effect.

---

## SMK-020 — Transfer to invalid/inactive beneficiary rejected

**Priority:** P0

Expected:

No debit.

---

## SMK-021 — Duplicate transfer submission does not create duplicate debit

**Priority:** P0

Expected:

Exactly one intended financial effect.

---

# 14. Transfer Reconciliation Smoke

## SMK-022 — Transfer balances reconcile

**Priority:** P0

For successful transfer:

```text id="42az9x"
Source Opening
- Transfer Amount
- Fee
= Source Closing
```

Destination:

```text id="ct46nx"
Destination Opening
+ Transfer Amount
= Destination Closing
```

Expected:

UI/API/database values agree.

---

# 15. Payment Smoke Tests

## SMK-023 — Successful payment

**Priority:** P0

Expected:

* Payment completes.
* Account debit correct.
* Transaction record created.
* Correct payment reference.

---

## SMK-024 — Payment with insufficient balance rejected

**Priority:** P0

Expected:

* No completed payment.
* Balance unchanged.

---

## SMK-025 — Duplicate unique bill payment prevented

**Priority:** P0

Expected:

Same bill cannot be settled twice accidentally.

---

## SMK-026 — Failed payment does not generate success notification

**Priority:** P0

Expected:

Notification state matches financial state.

---

# 16. Card Smoke Tests

## SMK-027 — Customer views active card

**Priority:** P1

Expected:

Correct card displayed with sensitive information masked.

---

## SMK-028 — Active card supports valid operation

**Priority:** P0

Expected:

Allowed transaction/action succeeds according to card rules.

---

## SMK-029 — Freeze active card

**Priority:** P0

Expected:

```text id="yamhez"
ACTIVE → FROZEN
```

---

## SMK-030 — Frozen card rejects purchase

**Priority:** P0

Expected:

Transaction declined.

---

## SMK-031 — Unfreeze card

**Priority:** P0

Expected:

```text id="msdkfz"
FROZEN → ACTIVE
```

---

## SMK-032 — Blocked card remains unusable

**Priority:** P0

Expected:

Protected backend operation rejected.

---

# 17. Loan Smoke Tests

## SMK-033 — Eligible customer can submit loan application

**Priority:** P1

Expected:

Application enters correct submitted/review state.

---

## SMK-034 — Authorized admin can review loan application

**Priority:** P1

Expected:

Application visible to appropriate role.

---

## SMK-035 — Authorized loan approval

**Priority:** P0

Expected:

```text id="8s6ekj"
UNDER_REVIEW → APPROVED
```

---

## SMK-036 — Approved loan disburses exactly once

**Priority:** P0

Expected:

* Customer credited once.
* Loan state updated.
* Transaction created.
* Duplicate disbursement prevented.

---

## SMK-037 — Loan repayment

**Priority:** P0

Expected:

* Correct debit.
* Outstanding amount decreases correctly.
* Repayment transaction appears.

---

# 18. Deposit Smoke Tests

## SMK-038 — Open valid deposit

**Priority:** P0

Expected:

* Funding account debited correctly.
* Deposit created.
* Principal correct.
* State becomes ACTIVE after valid funding.

---

## SMK-039 — Deposit opening with insufficient funds rejected

**Priority:** P0

Expected:

* No deposit activation.
* No invalid debit.

---

## SMK-040 — Deposit maturity payout processed once

**Priority:** P0

Expected:

* Principal + interest correct.
* Settlement account credited once.
* Deposit state updated.

---

## SMK-041 — Duplicate maturity processing prevented

**Priority:** P0

Expected:

No double payout.

---

# 19. Transaction History Smoke Tests

## SMK-042 — Recent successful transfer appears in history

**Priority:** P0

Expected:

Correct:

* Type
* Amount
* Debit/credit
* Status
* Reference
* Timestamp

---

## SMK-043 — Recent payment appears in history

**Priority:** P1

Expected:

Correct financial details.

---

## SMK-044 — Failed transaction does not appear as successful completed debit

**Priority:** P0

Expected:

Financial history reflects authoritative state.

---

## SMK-045 — Reversal preserves original transaction

**Priority:** P0

Expected:

Original + linked reversal remain traceable.

---

# 20. Statement Smoke Tests

## SMK-046 — Generate statement for owned account

**Priority:** P0

Expected:

Statement generated successfully.

---

## SMK-047 — Statement opening balance correct

**Priority:** P0

Expected:

Matches authoritative account history.

---

## SMK-048 — Statement closing balance reconciles

**Priority:** P0

Expected:

```text id="sj7pwd"
Opening
+ Credits
- Debits
= Closing
```

---

## SMK-049 — Another customer's statement inaccessible

**Priority:** P0

Expected:

Authorization denied.

---

## SMK-050 — Statement download/PDF works

**Priority:** P1

Expected:

Download belongs to correct account/customer and contains correct data.

---

# 21. Notification Smoke Tests

## SMK-051 — New notification appears after successful transfer

**Priority:** P1

Expected:

Correct amount/status/reference.

---

## SMK-052 — Failed transfer never produces success notification

**Priority:** P0

Expected:

No false success.

---

## SMK-053 — Password-change security notification

**Priority:** P0

Expected:

Correct customer notified.

---

## SMK-054 — Notification ownership enforced

**Priority:** P0

Expected:

Customer cannot access another customer's notification.

---

# 22. Profile / Settings Smoke Tests

## SMK-055 — Customer views own profile

**Priority:** P1

Expected:

Correct customer data.

---

## SMK-056 — Basic valid profile update

**Priority:** P1

Expected:

Change persists correctly.

---

## SMK-057 — Password change

**Priority:** P0

Expected:

* New password works.
* Old password no longer works.
* Security notification generated.

---

## SMK-058 — Protected profile field cannot be modified

**Priority:** P0

Example:

```text id="50agkb"
role = ADMIN
```

Expected:

Backend rejects/ignores modification.

---

# 23. Security Smoke Tests

## SMK-059 — Customer cannot access another customer's transaction

**Priority:** P0

Expected:

Denied.

---

## SMK-060 — Customer cannot access another customer's card

**Priority:** P0

Expected:

Denied.

---

## SMK-061 — Customer cannot access admin endpoint

**Priority:** P0

Expected:

Denied.

---

## SMK-062 — Expired/revoked session rejected

**Priority:** P0

Expected:

Protected operation cannot continue.

---

## SMK-063 — Protected API requires authentication

**Priority:** P0

Expected:

Unauthenticated request rejected.

---

## SMK-064 — Financial validation enforced server-side

**Priority:** P0

Example:

Submit amount above transfer limit directly through API.

Expected:

Rejected.

---

# 24. Admin Smoke Tests

## SMK-065 — Authorized admin login

**Priority:** P0

Expected:

Admin dashboard accessible.

---

## SMK-066 — Read-only admin cannot modify customer/account

**Priority:** P0

Expected:

Backend denies state-changing request.

---

## SMK-067 — Admin customer search

**Priority:** P1

Expected:

Known customer returned.

---

## SMK-068 — Authorized account freeze

**Priority:** P0

Expected:

```text id="vv8bzd"
ACTIVE → FROZEN
```

Customer can no longer perform prohibited outgoing transaction.

---

## SMK-069 — Account unfreeze

**Priority:** P0

Expected:

Correct state restored.

---

## SMK-070 — Critical admin action creates audit record

**Priority:** P0

Expected:

Audit contains:

* Actor
* Action
* Target
* Previous state
* New state
* Timestamp
* Reason where required

---

# 25. KYC Smoke Tests

## SMK-071 — Authorized KYC reviewer views pending case

**Priority:** P1

Expected:

Correct case.

---

## SMK-072 — Approve KYC

**Priority:** P0

Expected:

```text id="xfrv2s"
PENDING → VERIFIED
```

---

## SMK-073 — Unauthorized role cannot approve KYC

**Priority:** P0

Expected:

Denied.

---

# 26. Audit Smoke Tests

## SMK-074 — Financial transaction audit/event trace exists where required

**Priority:** P1

Expected:

Traceable.

---

## SMK-075 — Admin state change audit exists

**Priority:** P0

Expected:

Correct actor and result.

---

## SMK-076 — Audit record does not expose credentials

**Priority:** P0

Expected:

No:

* Password
* OTP
* Session token
* MFA secret
* CVV/PIN

---

# 27. API Smoke Tests

## SMK-077 — Authentication API works

**Priority:** P0

Expected:

Valid authentication workflow works.

---

## SMK-078 — Accounts API returns authorized accounts

**Priority:** P0

Expected:

Only owned accounts returned.

---

## SMK-079 — Transfer API completes valid transaction

**Priority:** P0

Expected:

Correct financial result.

---

## SMK-080 — Transfer API rejects invalid amount

**Priority:** P0

Expected:

No balance effect.

---

## SMK-081 — Transaction history API returns recent transaction

**Priority:** P1

Expected:

Correct record.

---

## SMK-082 — Statement API rejects unauthorized account

**Priority:** P0

Expected:

Denied.

---

## SMK-083 — Admin API enforces role

**Priority:** P0

Expected:

Customer/limited role cannot perform restricted action.

---

# 28. Database Smoke Validation

After selected financial smoke tests validate database state.

## SMK-084 — Successful transfer persisted correctly

**Priority:** P0

Validate:

```text id="lprq7r"
Transaction exists

Unique reference

Correct status

Correct source

Correct destination

Correct amount

Correct balance effects
```

---

## SMK-085 — Rejected transfer creates no completed financial effect

**Priority:** P0

Expected:

No invalid debit/credit.

---

## SMK-086 — Loan disbursement stored exactly once

**Priority:** P0

Expected:

One credit event.

---

## SMK-087 — Deposit maturity stored exactly once

**Priority:** P0

Expected:

One payout event.

---

# 29. Deployment Smoke Sequence

Recommended deployment smoke order:

```text id="74maxx"
1. Application health
2. Login
3. MFA
4. Account retrieval
5. Authorization isolation
6. Transfer
7. Payment
8. Card state
9. Loan
10. Deposit
11. Transaction history
12. Statement
13. Notification
14. Admin
15. Audit
16. Database reconciliation
```

If early foundational checks fail, deeper smoke execution may be stopped.

---

# 30. Critical P0 Fast-Smoke Subset

For very fast deployment verification, execute:

| ID      | Test                          |
| ------- | ----------------------------- |
| SMK-001 | Application loads             |
| SMK-004 | Customer login                |
| SMK-006 | MFA                           |
| SMK-009 | View accounts                 |
| SMK-011 | Customer isolation            |
| SMK-016 | Valid transfer                |
| SMK-017 | Insufficient funds rejection  |
| SMK-021 | Duplicate transfer prevention |
| SMK-022 | Transfer reconciliation       |
| SMK-023 | Valid payment                 |
| SMK-029 | Freeze card                   |
| SMK-030 | Frozen card rejects purchase  |
| SMK-036 | Loan disbursement once        |
| SMK-038 | Deposit creation              |
| SMK-040 | Deposit maturity payout once  |
| SMK-042 | Transaction history           |
| SMK-048 | Statement reconciliation      |
| SMK-052 | No false success notification |
| SMK-061 | Admin authorization           |
| SMK-070 | Admin audit                   |

This subset provides a quick build-confidence gate.

---

# 31. Extended Smoke Suite

The extended smoke suite should include:

```text id="0g92kv"
SMK-001 through SMK-087
```

Use extended smoke:

* Before full regression
* Before release candidate testing
* After major backend deployment
* After major database migration
* After environment rebuild

---

# 32. Smoke Execution Status

Each test uses:

```text id="jmqvxi"
NOT_RUN
PASS
FAIL
BLOCKED
SKIPPED
```

---

# 33. Smoke Execution Table Template

| Test ID | Test                     | Priority | Status  | Defect | Notes |
| ------- | ------------------------ | -------- | ------- | ------ | ----- |
| SMK-001 | Application loads        | P0       | NOT_RUN | —      | —     |
| SMK-004 | Valid login              | P0       | NOT_RUN | —      | —     |
| SMK-016 | Valid transfer           | P0       | NOT_RUN | —      | —     |
| SMK-023 | Valid payment            | P0       | NOT_RUN | —      | —     |
| SMK-029 | Freeze card              | P0       | NOT_RUN | —      | —     |
| SMK-036 | Loan disbursement        | P0       | NOT_RUN | —      | —     |
| SMK-040 | Deposit maturity         | P0       | NOT_RUN | —      | —     |
| SMK-048 | Statement reconciliation | P0       | NOT_RUN | —      | —     |
| SMK-061 | Admin authorization      | P0       | NOT_RUN | —      | —     |

Actual execution evidence should later be maintained in:

```text id="frs6h3"
manual-testing/test-execution/smoke-execution.md
```

---

# 34. Smoke Failure Rules

Immediate smoke failure should occur for:

```text id="juos6k"
Application unavailable

Customer cannot login

Authentication bypass

MFA bypass

Accounts unavailable

Unauthorized customer data access

Valid transfer unavailable

Duplicate financial transaction

Incorrect balance

Payment critical flow unavailable

Frozen card remains usable

Duplicate loan disbursement

Duplicate deposit maturity payout

Statement financial mismatch

Customer can access admin function

Critical audit failure
```

---

# 35. Smoke Blocking Defect Examples

## Blocker

```text id="n7cglr"
Application cannot start.
```

```text id="r2kf0g"
All customers receive 500 errors after login.
```

## Critical

```text id="l4et5x"
Transfer debits customer twice.
```

```text id="m96b0i"
Customer A can see Customer B's statement.
```

```text id="updz8d"
Frozen account still transfers funds.
```

These failures should prevent progression to full regression until assessed.

---

# 36. Smoke vs Sanity vs Regression

## Smoke

Question:

```text id="a55i03"
Is the build basically stable and testable?
```

Coverage:

```text id="4nwt00"
Broad + shallow + critical
```

---

## Sanity

Question:

```text id="nj2rfr"
Did a specific change/fix work without obvious nearby breakage?
```

Coverage:

```text id="1bb05w"
Narrow + focused
```

---

## Regression

Question:

```text id="qxnepe"
Did recent changes break existing functionality anywhere relevant?
```

Coverage:

```text id="rkyx70"
Broad + deep
```

---

# 37. Smoke Automation Strategy

High-value smoke scenarios should later be automated.

## UI

Use:

```text id="4xi6gv"
Selenium
Cypress
Playwright
```

Automate:

* Login
* Accounts
* Transfer
* Payment
* Card freeze
* Transaction history
* Statement
* Admin access

---

## API

Use:

```text id="r8g30e"
Postman
REST Assured
```

Automate:

* Authentication
* Accounts
* Transfer
* Payment
* Authorization
* Transactions
* Statements
* Admin permissions

---

## Backend / Logic

Use:

```text id="2zd787"
Jest
```

for applicable backend/business logic validation.

---

# 38. CI/CD Smoke Strategy

Smoke tests should later become a release gate.

Recommended pipeline:

```text id="xx1reo"
Build
   ↓
Unit / Jest
   ↓
Deploy Test Environment
   ↓
API Smoke
   ↓
UI Smoke
   ↓
Database Validation
   ↓
PASS → Continue
FAIL → Stop / Flag Deployment
```

Applicable CI tools:

```text id="hlhgug"
GitHub Actions
Jenkins
```

---

# 39. Test Isolation

Smoke tests should minimize interdependency.

Avoid:

```text id="3ce1rf"
SMK-020 requires SMK-019 to create its exact data
```

Prefer:

```text id="2exbd6"
Each smoke test uses known setup/reset data.
```

Financial smoke tests should restore or regenerate state where possible.

---

# 40. Test Data Reset

After smoke execution, restore:

* Account balances where test reset supports it
* Card states
* Customer states
* Beneficiary states
* Loan fixtures
* Deposit fixtures
* Notification state
* Admin test configuration

Do not manually overwrite financial history merely to reset balances.

Prefer controlled seed/reset processes.

---

# 41. Environment Validation

Before executing smoke, record:

```text id="5w6zr4"
Environment

Build/version

Frontend version

Backend version

Database version/migration

Feature flags

Browser

Test-data version
```

This helps identify environment-specific failures.

---

# 42. Smoke Evidence Requirements

For failed P0 tests capture:

```text id="8ferws"
Screenshot

Video when useful

Request

Response

Transaction reference

Timestamp

User/account ID

Balance before

Balance after

Relevant logs

Database evidence
```

---

# 43. Financial Smoke Validation Rule

Every smoke financial transaction should validate more than the UI success message.

For example:

```text id="j2ynh8"
UI says Transfer Successful
```

is not sufficient.

Verify:

```text id="dn6df2"
Transaction status = COMPLETED

Source balance correct

Destination balance correct

Fee correct

History correct

Reference correct

API agrees

Database agrees where checked
```

---

# 44. Security Smoke Validation Rule

Visibility is not authorization.

For example:

```text id="z4l7wn"
Admin menu hidden from customer
```

does not prove security.

Smoke must also test:

```text id="mh85i4"
Customer calls admin API directly
→ Request rejected
```

Similarly:

```text id="rv9yb4"
Customer does not see Customer B account
```

must be supplemented by:

```text id="1nznxd"
Request Customer B account directly
→ Access denied
```

---

# 45. Smoke Report Summary Template

```text id="yfrc3z"
Smoke Execution ID:
Environment:
Build:
Date:
Tester:

Total Tests:
Passed:
Failed:
Blocked:
Skipped:

P0 Passed:
P0 Failed:

Critical Defects:
Blocker Defects:

Overall Result:
PASS / FAIL

Recommendation:
Proceed to Regression / Do Not Proceed
```

---

# 46. Example Smoke Summary

Example only:

```text id="n24ldt"
Smoke Execution ID: SMK-RUN-001
Environment: QA
Build: 1.0.0-rc1

Total: 40
Passed: 39
Failed: 1
Blocked: 0

Failed:
SMK-021 — Duplicate transfer prevention

Defect:
BUG-001 — Double submission creates duplicate debit

Overall Result:
FAIL

Recommendation:
Do not proceed to release regression until duplicate-transfer defect is resolved.
```

---

# 47. Risk Traceability

Smoke coverage primarily mitigates:

```text id="mc09k1"
RISK-001 — Incorrect account balance
RISK-002 — Unauthorized customer data
RISK-003 — Duplicate transaction
RISK-004 — Partial transfer
RISK-005 — Authentication bypass
RISK-007 — Transfer exceeds balance
RISK-008 — Limit bypass
RISK-009 — Frozen account transaction
RISK-013 — Balance corruption
RISK-019 — Transaction history mismatch
RISK-020 — Statement mismatch
RISK-023 — Unauthorized admin
RISK-024 — Frozen card usable
RISK-025 — Blocked card usable
RISK-026 — Failed payment changes balance
RISK-027 — Duplicate payment
RISK-030 — Unauthorized API
RISK-034 — False notification success
RISK-039 — API/database mismatch
RISK-047 — IDOR
RISK-048 — UI/backend result mismatch
```

---

# 48. Smoke Suite Coverage Summary

This suite validates:

* System availability
* Authentication
* MFA
* Logout
* Accounts
* Authorization
* Account status
* Beneficiaries
* Transfers
* Transfer limits
* Transfer balance integrity
* Duplicate prevention
* Payments
* Cards
* Card status
* Loans
* Loan disbursement
* Repayments
* Deposits
* Maturity
* Transaction history
* Reversals
* Statements
* Notifications
* Profile
* Password changes
* Security
* Admin access
* KYC
* Audit logging
* APIs
* Database persistence

---

# 49. Final Smoke Testing Principle

The smoke suite should answer one question:

```text id="nr0jk4"
Is this Banking System build safe and stable enough
to spend more time testing?
```

For a banking application, a build should not be considered smoke-stable unless QA has confidence that:

```text id="gzbl3s"
Users can authenticate.

Customer data remains isolated.

Accounts load correctly.

Money can move through the main valid flow.

Invalid financial operations are rejected.

Balances remain correct.

Duplicate financial effects are prevented.

Critical card/account states are enforced.

Transactions remain traceable.

Statements reconcile.

Admin permissions are enforced.

Critical actions are auditable.
```

The core rule is:

```text id="zw31pg"
If the smoke suite cannot trust authentication,
authorization, or financial integrity,
the build should not proceed to deeper release testing.
```
