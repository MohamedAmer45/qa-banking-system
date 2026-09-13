# Banking System — Admin Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Admin / Operations             |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Admin scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Admin authentication
* Admin authorization
* Role-based access control
* Customer search
* Customer review
* KYC operations
* Customer status management
* Account freeze/unfreeze
* Account restrictions
* Transaction investigation
* Transfer reversal
* Payment investigation
* Card administration
* Loan operations
* Deposit operations
* Limit and fee administration
* Audit-log access
* Sensitive-data minimization
* Administrative reason requirements
* Four-eyes/dual-control concepts where applicable
* Concurrency
* Stale-state protection
* API validation
* Database validation
* Auditability
* Notifications
* Security
* Cross-browser behavior
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Admin test cases use:

```text
ADM-TC-XXX
```

Examples:

```text
ADM-TC-001
ADM-TC-002
ADM-TC-003
```

---

# 4. Critical Admin Invariants

## Invariant 1 — Least Privilege

Every admin role must be limited to explicitly permitted operations.

```text
Read-only admin
≠
Operations admin
≠
KYC reviewer
≠
Loan officer
≠
Security administrator
```

---

## Invariant 2 — Server-Side RBAC

Hiding an admin button is not sufficient.

Every privileged operation must verify authorization on the server.

---

## Invariant 3 — Administrative Traceability

Critical admin actions must record:

```text
Actor

Role

Action

Target

Previous state

New state

Reason where required

Timestamp
```

---

## Invariant 4 — Financial Safety

An administrator must not be able to silently create, destroy, duplicate, or redirect customer money.

Financial corrections must use controlled workflows such as:

```text
Reversal

Refund

Adjustment

Approved administrative operation
```

rather than directly editing settled financial history.

---

## Invariant 5 — Customer Isolation and Data Minimization

Admins may see only the customer information required by their role.

---

## Invariant 6 — Stale-State Protection

Administrative decisions must use authoritative current state rather than stale UI data.

---

## Invariant 7 — No Silent Audit Bypass

A privileged operation must not succeed without required audit evidence.

---

# 5. Common Test Data

## Operations Admin

```text
Admin:
ADMIN-002

Role:
OPERATIONS_ADMIN

Status:
ACTIVE
```

## KYC Reviewer

```text
Admin:
ADMIN-003

Role:
KYC_REVIEWER

Status:
ACTIVE
```

## Loan Officer

```text
Admin:
ADMIN-004

Role:
LOAN_OFFICER

Status:
ACTIVE
```

## Read-Only Admin

```text
Admin:
ADMIN-007

Role:
READ_ONLY_ADMIN

Status:
ACTIVE
```

## Standard Customer

```text
Customer:
CUST-001

Status:
ACTIVE

KYC:
VERIFIED
```

## Pending-KYC Customer

```text
Customer:
CUST-003

Status:
ACTIVE

KYC:
PENDING
```

## Active Account

```text
Account:
ACC-001

Owner:
CUST-001

Status:
ACTIVE
```

## Active Loan

```text
Loan:
LOAN-002

Customer:
CUST-001

Status:
ACTIVE
```

## Active Deposit

```text
Deposit:
DEP-001

Customer:
CUST-001

Status:
ACTIVE
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Admin portal is available.

Required admin accounts exist.

Required synthetic customer/financial data exists.

API and database validation are available where required.

Audit logging is enabled.

No production customer data is used.
```

---

# 7. Admin Authentication Test Cases

## ADM-TC-001 — Valid Admin Login

**Priority:** P0
**Requirement:** REQ-ADM-001

### Steps

1. Open admin portal.
2. Enter valid admin credentials.
3. Complete MFA if required.

### Expected Result

Admin is authenticated with the correct role and permissions.

---

## ADM-TC-002 — Invalid Admin Password

**Priority:** P0

Expected: denied.

---

## ADM-TC-003 — Disabled Admin Login

**Priority:** P0

Expected: denied.

---

## ADM-TC-004 — Locked Admin Login

**Priority:** P0

Expected: denied according to lockout policy.

---

## ADM-TC-005 — Admin Login Without Required MFA

**Priority:** P0
**Risk:** RISK-005

Expected: privileged session is not established.

---

## ADM-TC-006 — Customer Credentials on Admin Portal

**Priority:** P0

Expected: admin access denied.

---

# 8. Admin Session Test Cases

## ADM-TC-007 — Admin Session Timeout

**Priority:** P0

Expected: expired admin session cannot perform privileged action.

---

## ADM-TC-008 — Admin Logout

**Priority:** P0

Expected: session revoked.

---

## ADM-TC-009 — Privileged API After Logout

**Priority:** P0

Expected: denied.

---

## ADM-TC-010 — Admin Role Removed During Active Session

**Priority:** P0
**Risk:** RISK-023

Expected: later privileged actions revalidate permissions.

---

# 9. Admin Role-Based Access Control

## ADM-TC-011 — Operations Admin Accesses Allowed Operations

**Priority:** P0
**Requirement:** REQ-ADM-002

Expected: explicitly permitted operations succeed.

---

## ADM-TC-012 — Read-Only Admin Views Customer

**Priority:** P1

Expected: read permitted.

---

## ADM-TC-013 — Read-Only Admin Modifies Customer

**Priority:** P0
**Risk:** RISK-006

Expected: denied.

---

## ADM-TC-014 — Read-Only Admin Freezes Account

**Priority:** P0

Expected: denied.

---

## ADM-TC-015 — Read-Only Admin Reverses Transaction

**Priority:** P0

Expected: denied.

---

## ADM-TC-016 — KYC Reviewer Updates KYC

**Priority:** P0

Expected: allowed when policy permits.

---

## ADM-TC-017 — KYC Reviewer Approves Loan

**Priority:** P0

Expected: denied unless role explicitly includes loan approval.

---

## ADM-TC-018 — Loan Officer Approves Loan

**Priority:** P0

Expected: permitted for eligible application.

---

## ADM-TC-019 — Loan Officer Changes Customer KYC

**Priority:** P0

Expected: denied.

---

## ADM-TC-020 — Operations Admin Changes User Role Without Permission

**Priority:** P0

Expected: denied unless role-management permission explicitly granted.

---

# 10. Direct Admin API Authorization

## ADM-TC-021 — Customer Calls Admin API

**Priority:** P0
**Risk:** RISK-030

Expected: denied.

---

## ADM-TC-022 — Read-Only Admin Calls Write API Directly

**Priority:** P0

Expected: denied even if UI control is hidden.

---

## ADM-TC-023 — KYC Reviewer Calls Loan Approval API

**Priority:** P0

Expected: denied.

---

## ADM-TC-024 — Loan Officer Calls Account Freeze API

**Priority:** P0

Expected: denied unless authorized.

---

## ADM-TC-025 — Manipulate Role Value in Client

**Priority:** P0
**Risk:** RISK-037

Expected: server-authoritative role remains unchanged.

---

# 11. Customer Search Test Cases

## ADM-TC-026 — Search Customer by Customer ID

**Priority:** P1
**Requirement:** REQ-ADM-003

Expected: matching permitted customer returned.

---

## ADM-TC-027 — Search by Email

**Priority:** P1

Expected: correct customer returned according to role access.

---

## ADM-TC-028 — Search by Phone

**Priority:** P1

Expected: correct result.

---

## ADM-TC-029 — Search Unknown Customer

**Priority:** P2

Expected: clear empty result.

---

## ADM-TC-030 — Partial Customer Search

**Priority:** P2

Expected: behavior follows search specification.

---

# 12. Customer Search Security

## ADM-TC-031 — Read-Only Role Sees Only Authorized Fields

**Priority:** P0
**Risk:** RISK-021

Expected: sensitive/internal fields minimized.

---

## ADM-TC-032 — Search Result Does Not Expose Password Hash

**Priority:** P0

Expected: absent.

---

## ADM-TC-033 — Search Does Not Expose Authentication Tokens

**Priority:** P0

Expected: absent.

---

## ADM-TC-034 — Search Does Not Expose OTP/MFA Secret

**Priority:** P0

Expected: absent.

---

# 13. Customer Detail Test Cases

## ADM-TC-035 — View Customer Summary

**Priority:** P1

Expected: correct profile/status/KYC information for role.

---

## ADM-TC-036 — View Customer Accounts

**Priority:** P1

Expected: account list belongs to selected customer.

---

## ADM-TC-037 — View Customer Transactions

**Priority:** P1

Expected: correct authorized history.

---

## ADM-TC-038 — View Customer Cards

**Priority:** P1

Expected: sensitive card data masked.

---

## ADM-TC-039 — View Customer Loans

**Priority:** P1

Expected: correct loan relationships.

---

## ADM-TC-040 — View Customer Deposits

**Priority:** P1

Expected: correct deposit relationships.

---

# 14. KYC Review Test Cases

## ADM-TC-041 — KYC Reviewer Opens Pending Case

**Priority:** P0
**Requirement:** REQ-ADM-004

Expected: relevant KYC evidence/status visible.

---

## ADM-TC-042 — Approve Valid KYC

**Priority:** P0

Expected:

```text
PENDING
→ VERIFIED
```

with audit trail.

---

## ADM-TC-043 — Reject KYC

**Priority:** P0

Expected:

```text
PENDING
→ REJECTED
```

with required reason.

---

## ADM-TC-044 — KYC Approval Without Required Evidence

**Priority:** P0

Expected: rejected according to policy.

---

## ADM-TC-045 — Read-Only Admin Approves KYC

**Priority:** P0

Expected: denied.

---

## ADM-TC-046 — Customer Calls KYC Approval API

**Priority:** P0

Expected: denied.

---

# 15. KYC State Transition Tests

## ADM-TC-047 — PENDING → VERIFIED

**Priority:** P0

Expected: valid authorized transition.

---

## ADM-TC-048 — PENDING → REJECTED

**Priority:** P0

Expected: valid authorized transition.

---

## ADM-TC-049 — VERIFIED → PENDING

**Priority:** P1

Expected: allowed only through defined review/reverification process.

---

## ADM-TC-050 — Client Forces Invalid KYC State

**Priority:** P0

Expected: rejected.

---

# 16. Customer Status Administration

## ADM-TC-051 — Suspend Active Customer

**Priority:** P0
**Requirement:** REQ-ADM-005

Expected:

```text
ACTIVE
→ SUSPENDED
```

with reason and audit.

---

## ADM-TC-052 — Restore Suspended Customer

**Priority:** P0

Expected: authorized transition only.

---

## ADM-TC-053 — Disable Customer

**Priority:** P0

Expected: security/business restrictions immediately apply.

---

## ADM-TC-054 — Close Customer Record

**Priority:** P0

Expected: closure dependencies enforced.

---

## ADM-TC-055 — Customer Status Change Without Reason

**Priority:** P1

Where reason mandatory.

Expected: rejected.

---

## ADM-TC-056 — Unauthorized Role Changes Customer Status

**Priority:** P0

Expected: denied.

---

# 17. Customer Status Stale-State Tests

## ADM-TC-057 — Customer Transaction While Suspension Occurs

**Priority:** P0
**Risk:** RISK-013

Expected: authoritative timing/state rules prevent invalid transaction.

---

## ADM-TC-058 — Two Admins Change Customer State Concurrently

**Priority:** P0

Expected: valid deterministic final state.

---

# 18. Account Freeze Administration

## ADM-TC-059 — Authorized Admin Freezes Active Account

**Priority:** P0
**Requirement:** REQ-ADM-006

### Expected Result

```text
ACTIVE
→ FROZEN
```

with audit record.

---

## ADM-TC-060 — Freeze Already Frozen Account

**Priority:** P2

Expected: safe idempotent behavior.

---

## ADM-TC-061 — Unfreeze Eligible Account

**Priority:** P0

Expected:

```text
FROZEN
→ ACTIVE
```

with authorization/audit.

---

## ADM-TC-062 — Unfreeze Closed Account

**Priority:** P0

Expected: rejected.

---

## ADM-TC-063 — Freeze Another Account Through Modified ID

**Priority:** P0

Expected: target-specific authorization/validation applied.

---

# 19. Account Restriction Administration

## ADM-TC-064 — Apply Account Restriction

**Priority:** P0

Expected: configured restrictions become authoritative.

---

## ADM-TC-065 — Remove Account Restriction

**Priority:** P0

Expected: authorized removal only.

---

## ADM-TC-066 — Read-Only Admin Changes Restriction

**Priority:** P0

Expected: denied.

---

# 20. Freeze Stale-State Protection

## ADM-TC-067 — Customer Has Transfer Confirmation Open Before Freeze

**Priority:** P0
**Risk:** RISK-009, RISK-048

### Steps

1. Customer prepares transfer.
2. Admin freezes source account.
3. Customer submits transfer.

### Expected Result

Transfer rejected based on current state.

---

## ADM-TC-068 — Payment Submission After Freeze

**Priority:** P0

Expected: rejected.

---

# 21. Transaction Investigation Test Cases

## ADM-TC-069 — Search Transaction by Reference

**Priority:** P0
**Requirement:** REQ-ADM-007

Expected: correct transaction returned.

---

## ADM-TC-070 — Search by Customer

**Priority:** P1

Expected: correct authorized transaction set.

---

## ADM-TC-071 — View Transaction Lifecycle

**Priority:** P0

Expected: status history/references sufficient for investigation.

---

## ADM-TC-072 — View Related Source/Destination Records

**Priority:** P0

Expected: correct relationships.

---

## ADM-TC-073 — View Failed Transaction Diagnostics

**Priority:** P1

Expected: operational information available without exposing secrets.

---

# 22. Transfer Reversal Test Cases

## ADM-TC-074 — Authorized Reversal of Eligible Transfer

**Priority:** P0
**Requirement:** REQ-ADM-008

### Expected Result

* Original transaction preserved.
* Separate reversal created.
* Correct financial compensation.
* Audit record created.

---

## ADM-TC-075 — Read-Only Admin Reversal

**Priority:** P0
**Risk:** RISK-006

Expected: denied.

---

## ADM-TC-076 — Reverse Already Reversed Transfer

**Priority:** P0

Expected: duplicate reversal prevented.

---

## ADM-TC-077 — Reverse Failed Transfer

**Priority:** P0

Expected: rejected if no settled value exists to reverse.

---

## ADM-TC-078 — Reverse Another Transfer by Manipulated ID

**Priority:** P0

Expected: target and eligibility are validated.

---

# 23. Reversal Financial Validation

## ADM-TC-079 — Reversal Restores Correct Amount

**Priority:** P0

Expected: correct compensating value.

---

## ADM-TC-080 — Reversal Fee Handling

**Priority:** P0

Expected: fee treatment follows business rules.

---

## ADM-TC-081 — Reversal Does Not Delete Original

**Priority:** P0

Expected: immutable transaction history preserved.

---

## ADM-TC-082 — Reversal Statement Reconciliation

**Priority:** P0

Expected: statement/history balances reconcile.

---

# 24. Concurrent Reversal Test Cases

## ADM-TC-083 — Two Admins Reverse Same Transfer

**Priority:** P0
**Risk:** RISK-013

Expected:

```text
Successful Reversals:
1
```

unless controlled partial/multiple reversal model explicitly supports otherwise.

---

## ADM-TC-084 — Reversal Request Retried After Timeout

**Priority:** P0
**Risk:** RISK-042

Expected: no duplicate compensation.

---

# 25. Payment Investigation

## ADM-TC-085 — Inspect Payment Status

**Priority:** P1

Expected: local/provider correlation available.

---

## ADM-TC-086 — Provider/Local Status Mismatch

**Priority:** P0

Expected: admin can identify mismatch without manually falsifying settlement state.

---

## ADM-TC-087 — Authorized Payment Refund

**Priority:** P0

Expected: controlled refund process creates traceable financial record.

---

## ADM-TC-088 — Duplicate Payment Refund Attempt

**Priority:** P0

Expected: duplicate credit prevented.

---

# 26. Card Administration

## ADM-TC-089 — Admin Blocks Card

**Priority:** P0

Expected: authorized block applied immediately.

---

## ADM-TC-090 — Admin Replaces Card

**Priority:** P0

Expected: old card invalidated and replacement traceable.

---

## ADM-TC-091 — Read-Only Admin Blocks Card

**Priority:** P0

Expected: denied.

---

## ADM-TC-092 — Admin Views Full CVV

**Priority:** P0
**Risk:** RISK-021

Expected: CVV unavailable.

---

## ADM-TC-093 — Admin View Uses Masked PAN

**Priority:** P0

Expected: masking follows role/data-minimization requirements.

---

# 27. Loan Administration

## ADM-TC-094 — Loan Officer Views Pending Application

**Priority:** P0

Expected: permitted.

---

## ADM-TC-095 — Loan Officer Approves Eligible Application

**Priority:** P0

Expected: approval succeeds with audit.

---

## ADM-TC-096 — Loan Officer Rejects Application

**Priority:** P0

Expected: rejection/reason recorded.

---

## ADM-TC-097 — Read-Only Admin Approves Loan

**Priority:** P0

Expected: denied.

---

## ADM-TC-098 — Operations Admin Approves Loan Without Permission

**Priority:** P0

Expected: denied.

---

## ADM-TC-099 — Approve Rejected/Cancelled Loan Through Direct API

**Priority:** P0

Expected: state transition rules enforced.

---

# 28. Loan Disbursement Administration

## ADM-TC-100 — Disburse Approved Loan

**Priority:** P0

Expected: exactly one correct disbursement.

---

## ADM-TC-101 — Duplicate Admin Disbursement

**Priority:** P0

Expected: one financial effect.

---

## ADM-TC-102 — Disbursement to Manipulated Destination Account

**Priority:** P0

Expected: unauthorized destination rejected.

---

# 29. Deposit Administration

## ADM-TC-103 — View Customer Deposit

**Priority:** P1

Expected: authorized financial details shown.

---

## ADM-TC-104 — Inspect Maturity Status

**Priority:** P1

Expected: correct current lifecycle.

---

## ADM-TC-105 — Manual Maturity Payout After Automatic Payout

**Priority:** P0

Expected: duplicate payout rejected.

---

## ADM-TC-106 — Administrative Deposit Adjustment

**Priority:** P0

Expected: controlled, authorized, fully audited adjustment only.

---

# 30. Limit Administration

## ADM-TC-107 — View Customer Transfer Limit

**Priority:** P1
**Requirement:** REQ-ADM-009

Expected: correct configured/effective limit.

---

## ADM-TC-108 — Authorized Limit Change

**Priority:** P0

Expected: new limit validated, persisted, audited.

---

## ADM-TC-109 — Limit Above Policy Maximum

**Priority:** P0

Expected: rejected unless authorized override workflow exists.

---

## ADM-TC-110 — Read-Only Admin Changes Limit

**Priority:** P0

Expected: denied.

---

## ADM-TC-111 — Customer Limit Manipulation Does Not Affect Admin Value

**Priority:** P0

Expected: authoritative server configuration preserved.

---

# 31. Fee Administration

## ADM-TC-112 — View Fee Configuration

**Priority:** P1

Expected: authorized role sees applicable fee configuration.

---

## ADM-TC-113 — Authorized Fee Change

**Priority:** P0

Expected: validation/audit applied.

---

## ADM-TC-114 — Negative Fee Configuration

**Priority:** P0

Expected: rejected unless explicitly valid product behavior.

---

## ADM-TC-115 — Excessive Fee Outside Allowed Policy

**Priority:** P0

Expected: rejected or requires governed override.

---

## ADM-TC-116 — Unauthorized Role Changes Fee

**Priority:** P0

Expected: denied.

---

# 32. Configuration Effective-Date Tests

## ADM-TC-117 — Future-Dated Limit Change

**Priority:** P1

Expected: current value remains active until effective date.

---

## ADM-TC-118 — Future-Dated Fee Change

**Priority:** P1

Expected: old/new values applied according to effective timestamps.

---

## ADM-TC-119 — Past-Dated Configuration Change

**Priority:** P0

Expected: restricted according to governance rules.

---

# 33. Audit Log Access

## ADM-TC-120 — Authorized Admin Views Audit Logs

**Priority:** P0
**Requirement:** REQ-ADM-010

Expected: permitted according to role.

---

## ADM-TC-121 — Customer Accesses Audit Logs

**Priority:** P0

Expected: denied.

---

## ADM-TC-122 — Unauthorized Admin Role Accesses Sensitive Audit Logs

**Priority:** P0

Expected: denied/minimized according to policy.

---

## ADM-TC-123 — Filter Audit by Actor

**Priority:** P1

Expected: correct results.

---

## ADM-TC-124 — Filter Audit by Action

**Priority:** P1

Expected: correct.

---

## ADM-TC-125 — Filter Audit by Target

**Priority:** P1

Expected: correct.

---

## ADM-TC-126 — Filter Audit by Date Range

**Priority:** P1

Expected: correct.

---

# 34. Audit Record Integrity

## ADM-TC-127 — Admin Cannot Edit Audit Record

**Priority:** P0
**Risk:** RISK-022

Expected: denied.

---

## ADM-TC-128 — Read-Only Admin Cannot Delete Audit Record

**Priority:** P0

Expected: denied.

---

## ADM-TC-129 — Critical Action Generates Audit Entry

**Priority:** P0

Expected: event present.

---

## ADM-TC-130 — Audit Contains Previous/New State

**Priority:** P1

Expected: enough context for investigation.

---

## ADM-TC-131 — Audit Contains Required Reason

**Priority:** P1

Expected: reason stored when mandatory.

---

# 35. Audit Sensitive Data

## ADM-TC-132 — Audit Does Not Contain Password

**Priority:** P0
**Risk:** RISK-032

Expected: absent.

---

## ADM-TC-133 — Audit Does Not Contain OTP

**Priority:** P0

Expected: absent.

---

## ADM-TC-134 — Audit Does Not Contain Raw Session Token

**Priority:** P0

Expected: absent/redacted.

---

## ADM-TC-135 — Audit Does Not Contain CVV

**Priority:** P0

Expected: absent.

---

# 36. Administrative Reason Validation

## ADM-TC-136 — Freeze Account With Valid Reason

**Priority:** P1

Expected: accepted.

---

## ADM-TC-137 — Freeze Account Without Required Reason

**Priority:** P0

Expected: rejected.

---

## ADM-TC-138 — Reversal Without Required Reason

**Priority:** P0

Expected: rejected.

---

## ADM-TC-139 — KYC Rejection Without Reason

**Priority:** P0

Expected: rejected if reason mandatory.

---

## ADM-TC-140 — Excessively Long Reason

**Priority:** P2

Expected: input limit enforced safely.

---

# 37. Four-Eyes / Dual-Control Test Cases

Where high-risk administrative changes require dual approval.

## ADM-TC-141 — Initiator Cannot Self-Approve Restricted Action

**Priority:** P0

Expected: second independent authorized approver required.

---

## ADM-TC-142 — Different Authorized Admin Approves

**Priority:** P0

Expected: action proceeds after valid second approval.

---

## ADM-TC-143 — Unauthorized Approver

**Priority:** P0

Expected: approval denied.

---

## ADM-TC-144 — Approval After Request Expiry

**Priority:** P0

Expected: expired approval request rejected.

---

# 38. Bulk Operations Test Cases

Where bulk administration is supported.

## ADM-TC-145 — Valid Bulk Customer Review

**Priority:** P1

Expected: each item processed according to authorization/rules.

---

## ADM-TC-146 — Bulk Operation Contains Unauthorized Item

**Priority:** P0

Expected: unauthorized item does not bypass validation because it is inside bulk request.

---

## ADM-TC-147 — Partial Bulk Failure

**Priority:** P0

Expected: result clearly identifies each success/failure; no hidden inconsistent state.

---

# 39. Stale Admin Data Test Cases

## ADM-TC-148 — Two Admins Open Same Customer

**Priority:** P1

Expected: latest authoritative state preserved.

---

## ADM-TC-149 — KYC State Changes Before Second Admin Submits

**Priority:** P0

Expected: stale decision cannot blindly overwrite newer state.

---

## ADM-TC-150 — Loan State Changes Before Approval Submit

**Priority:** P0

Expected: backend revalidates current state.

---

## ADM-TC-151 — Account Already Frozen Before Second Freeze

**Priority:** P1

Expected: safe idempotent result.

---

# 40. Admin Concurrency Test Cases

## ADM-TC-152 — Two Admins Reverse Same Transfer

**Priority:** P0
**Risk:** RISK-013

Expected: one reversal financial effect.

---

## ADM-TC-153 — Two Admins Approve Same Loan

**Priority:** P0

Expected: one valid decision/disbursement path.

---

## ADM-TC-154 — Two Admins Change Same Limit

**Priority:** P0

Expected: deterministic conflict/version handling.

---

## ADM-TC-155 — Freeze and Unfreeze Concurrently

**Priority:** P0

Expected: valid final state with traceable ordering.

---

# 41. Admin API Test Cases

## ADM-TC-156 — Valid Customer Search API

**Priority:** P1
**Automation:** REST Assured

Expected: authorized results only.

---

## ADM-TC-157 — Valid Account Freeze API

**Priority:** P0

Expected: correct state transition.

---

## ADM-TC-158 — Read-Only Admin Freeze API

**Priority:** P0

Expected: denied.

---

## ADM-TC-159 — Valid KYC Decision API

**Priority:** P0

Expected: role/state rules enforced.

---

## ADM-TC-160 — Valid Loan Decision API

**Priority:** P0

Expected: role/state rules enforced.

---

## ADM-TC-161 — Valid Reversal API

**Priority:** P0

Expected: controlled exactly-once reversal.

---

# 42. Protected Field Manipulation

## ADM-TC-162 — Admin Request Includes Unauthorized Role Field

**Priority:** P0
**Risk:** RISK-037

Expected: only fields explicitly allowed for that admin role may change.

---

## ADM-TC-163 — Admin Directly Changes Account Balance

**Priority:** P0

Payload attempts:

```json
{
  "balance": 999999999
}
```

Expected: rejected.

Financial corrections require approved ledger/adjustment workflow.

---

## ADM-TC-164 — Admin Directly Changes Transaction Amount

**Priority:** P0

Expected: rejected.

---

## ADM-TC-165 — Admin Directly Changes Completed Transaction to Failed

**Priority:** P0

Expected: rejected; reversal/adjustment workflow required.

---

## ADM-TC-166 — Admin Directly Changes Loan Principal

**Priority:** P0

Expected: unauthorized direct mutation rejected.

---

# 43. Database Validation Test Cases

## ADM-TC-167 — Customer Status Persistence

**Priority:** P0
**Automation:** SQL

Expected: latest authorized state stored.

---

## ADM-TC-168 — KYC Decision Persistence

**Priority:** P0

Expected: correct status/reviewer/timestamp.

---

## ADM-TC-169 — Account Freeze Persistence

**Priority:** P0

Expected: DB state matches API/UI.

---

## ADM-TC-170 — Reversal Persistence

**Priority:** P0

Expected: original plus separate reversal relationship.

---

## ADM-TC-171 — Limit Configuration Persistence

**Priority:** P0

Expected: correct value/effective time.

---

## ADM-TC-172 — Fee Configuration Persistence

**Priority:** P0

Expected: correct.

---

## ADM-TC-173 — Audit Entry Persistence

**Priority:** P0

Expected: critical operation is traceable.

---

# 44. UI/API/Database Consistency

## ADM-TC-174 — Customer Status Cross-Layer Validation

**Priority:** P0
**Risk:** RISK-039, RISK-048

Expected:

```text
Admin UI
=
API
=
DB
```

---

## ADM-TC-175 — KYC Status Cross-Layer Validation

**Priority:** P0

Expected: all layers agree.

---

## ADM-TC-176 — Account State Cross-Layer Validation

**Priority:** P0

Expected: all layers agree.

---

## ADM-TC-177 — Reversal Cross-Layer Validation

**Priority:** P0

Expected: UI/API/DB/ledger/history reconcile.

---

## ADM-TC-178 — Limit/Fee Cross-Layer Validation

**Priority:** P0

Expected: displayed, API, DB, and transaction enforcement values agree.

---

# 45. Notification Test Cases

## ADM-TC-179 — Customer Suspended Notification

**Priority:** P1

Expected: sent according to policy and matches actual state.

---

## ADM-TC-180 — Account Frozen Notification

**Priority:** P0

Expected: accurate.

---

## ADM-TC-181 — Account Unfrozen Notification

**Priority:** P1

Expected: accurate.

---

## ADM-TC-182 — KYC Decision Notification

**Priority:** P1

Expected: matches authoritative decision.

---

## ADM-TC-183 — Loan Approval/Rejection Notification

**Priority:** P1

Expected: accurate.

---

## ADM-TC-184 — Failed Admin Action Sends No False Success Notification

**Priority:** P0
**Risk:** RISK-034

Expected: no false customer communication.

---

# 46. Admin Data Minimization

## ADM-TC-185 — Read-Only Admin Cannot View Password Hash

**Priority:** P0
**Risk:** RISK-021

Expected: absent.

---

## ADM-TC-186 — Admin Cannot View Customer OTP

**Priority:** P0

Expected: absent.

---

## ADM-TC-187 — Admin Cannot View Raw Session Token

**Priority:** P0

Expected: absent.

---

## ADM-TC-188 — Card Data Is Masked

**Priority:** P0

Expected: no unnecessary full PAN/CVV/PIN.

---

# 47. Admin Search Input Security

## ADM-TC-189 — SQL-Like Search Input

**Priority:** P1

Expected: no injection/query manipulation.

---

## ADM-TC-190 — Script-Like Reason/Input

**Priority:** P1

Expected: safely encoded.

---

## ADM-TC-191 — Very Long Search Input

**Priority:** P2

Expected: handled safely.

---

# 48. Admin Error Handling

## ADM-TC-192 — Admin Service Unavailable

**Priority:** P1

Expected: safe error; no partial privileged state change.

---

## ADM-TC-193 — Authorization Service Failure

**Priority:** P0

Expected: privileged operation fails closed.

---

## ADM-TC-194 — Audit Service Failure During Critical Action

**Priority:** P0

Expected: behavior follows governance policy.

For actions requiring mandatory audit, operation must not silently succeed without traceability.

---

## ADM-TC-195 — Database Failure During Reversal

**Priority:** P0

Expected: no partial financial correction.

---

# 49. Cross-Browser Test Cases

## ADM-TC-196 — Admin Portal in Chrome

**Priority:** P2

Expected: critical operations work.

---

## ADM-TC-197 — Admin Portal in Edge

**Priority:** P2

Expected: works.

---

## ADM-TC-198 — Admin Portal in Firefox

**Priority:** P2

Expected: works.

---

## ADM-TC-199 — Admin Portal in WebKit

**Priority:** P2

Expected: works where supported.

---

# 50. Responsive Test Cases

## ADM-TC-200 — Admin Customer View at 1024×768

**Priority:** P2

Expected: key information/actions usable.

---

## ADM-TC-201 — Admin Portal Narrow View

**Priority:** P2

Expected: critical state/action information is not hidden misleadingly.

---

## ADM-TC-202 — Confirmation Dialog on Smaller Viewport

**Priority:** P1

Expected: target customer/account, impact, and reason remain readable.

---

# 51. Accessibility Test Cases

## ADM-TC-203 — Keyboard Admin Navigation

**Priority:** P2

Expected: critical controls keyboard accessible.

---

## ADM-TC-204 — Administrative Forms Have Labels

**Priority:** P2

Expected: accessible labels present.

---

## ADM-TC-205 — Status Not Communicated by Color Alone

**Priority:** P2

Expected: semantic/textual status available.

---

## ADM-TC-206 — Destructive Action Warning Accessible

**Priority:** P1

Expected: freeze/block/reversal/closure consequences clearly perceivable.

---

# 52. End-to-End KYC Administration

## ADM-TC-207 — Pending KYC → Review → Approval

**Priority:** P0

### Steps

1. Login as `ADMIN-003`.
2. Search `CUST-003`.
3. Open pending KYC.
4. Review required evidence.
5. Approve.
6. Verify customer state.
7. Verify DB.
8. Verify audit.
9. Verify notification.

### Expected Result

```text
KYC:
PENDING → VERIFIED
```

with authorized traceable change.

---

# 53. End-to-End Account Freeze

## ADM-TC-208 — Freeze Customer Account and Verify Enforcement

**Priority:** P0

### Steps

1. Confirm `ACC-001` is active.
2. Admin freezes account with reason.
3. Customer attempts transfer.
4. Customer attempts payment.
5. Query account API.
6. Query DB.
7. Inspect audit.
8. Inspect customer notification.

### Expected Result

All prohibited transactions are rejected while account state is consistently `FROZEN`.

---

# 54. End-to-End Account Unfreeze

## ADM-TC-209 — Frozen → Unfreeze → Transaction Restored

**Priority:** P0

Expected:

* Authorized unfreeze succeeds.
* Audit created.
* Notification accurate.
* Valid transaction becomes possible again if no other restriction exists.

---

# 55. End-to-End Transfer Investigation and Reversal

## ADM-TC-210 — Investigate → Reverse → Reconcile

**Priority:** P0

### Test Data

```text
Original Transfer:
1,000.00

Fee:
10.00
```

### Steps

1. Search transaction reference.
2. Verify original completed state.
3. Review account/history.
4. Perform authorized reversal.
5. Validate source/destination financial state.
6. Validate transaction history.
7. Validate statement.
8. Validate API/DB.
9. Validate audit.

### Expected Result

Original remains traceable.

One correct reversal occurs.

No duplicate compensation.

---

# 56. End-to-End Read-Only Admin Restriction

## ADM-TC-211 — Read-Only Admin Attempts Privileged Actions

**Priority:** P0

Attempt:

```text
Suspend customer

Approve KYC

Freeze account

Reverse transfer

Approve loan

Change limit

Change fee
```

### Expected Result

Every state-changing action denied.

Read-only views remain available according to role.

---

# 57. End-to-End Loan Administration

## ADM-TC-212 — Loan Review → Approval → Disbursement

**Priority:** P0

### Steps

1. Loan officer reviews eligible application.
2. Approves loan.
3. Authorized disbursement occurs.
4. Validate account credit.
5. Validate loan state.
6. Validate repayment schedule.
7. Validate DB/audit/notification.

### Expected Result

One approved loan results in one correct disbursement.

---

# 58. End-to-End Duplicate Disbursement Protection

## ADM-TC-213 — Two Admin Sessions Disburse Same Loan

**Priority:** P0

### Expected Result

```text
Loan Disbursement Count:
1

Account Credits:
1
```

---

# 59. End-to-End Deposit Payout Protection

## ADM-TC-214 — Manual Action After Automatic Deposit Payout

**Priority:** P0

### Expected Result

Second payout attempt rejected.

Customer account receives one maturity payout only.

---

# 60. End-to-End Limit Change

## ADM-TC-215 — Change Transfer Limit → Enforce New Limit

**Priority:** P0

### Steps

1. Record current transfer limit.
2. Authorized admin changes limit.
3. Verify audit.
4. Customer performs transaction within new limit.
5. Customer attempts transaction above new limit.
6. Validate API/DB.

### Expected Result

New limit becomes authoritative at intended effective time.

---

# 61. End-to-End Stale-State Protection

## ADM-TC-216 — Two Admins Operate on Same Record

**Priority:** P0

### Steps

1. Admin A and Admin B open same customer/account.
2. Admin A changes state.
3. Admin B submits stale conflicting action.
4. Inspect final state.

### Expected Result

Backend validates current state and prevents invalid overwrite.

---

# 62. End-to-End Audit Traceability

## ADM-TC-217 — Critical Admin Action Investigation

**Priority:** P0

### Steps

1. Perform authorized account freeze.
2. Search audit by actor.
3. Search by account.
4. Search by action.
5. Inspect event.

### Expected Result

Audit clearly identifies:

```text
Who

What

Target

When

Previous state

New state

Reason
```

without exposing authentication secrets.

---

# 63. End-to-End Fail-Closed Authorization

## ADM-TC-218 — Authorization Dependency Failure

**Priority:** P0

### Steps

1. Begin privileged operation.
2. Simulate authorization dependency failure.
3. Submit.

### Expected Result

```text
Privileged Action:
Denied / Safely Deferred

Unauthorized State Change:
No
```

---

# 64. End-to-End Audit Dependency Failure

## ADM-TC-219 — Mandatory Audit Cannot Be Written

**Priority:** P0

### Expected Result

For operations requiring guaranteed auditability, the system does not silently perform an untraceable privileged action.

---

# 65. End-to-End Privilege Escalation Attempt

## ADM-TC-220 — Read-Only Admin Manipulates Role/Endpoint

**Priority:** P0

### Steps

1. Login as `ADMIN-007`.
2. Re-enable hidden UI actions.
3. Modify request role field.
4. Call operations endpoints directly.
5. Attempt reversal/freeze/approval.

### Expected Result

All unauthorized mutations denied by backend.

---

# 66. Admin Risk Mapping

| Risk                                     | Related Test Cases                                         |
| ---------------------------------------- | ---------------------------------------------------------- |
| RISK-001 Incorrect financial balance     | ADM-TC-074–106, 163–178, 210–215                           |
| RISK-003 Duplicate financial transaction | ADM-TC-076, 083–084, 088, 101, 105, 152–153, 210, 213–214  |
| RISK-005 Authentication bypass           | ADM-TC-001–010                                             |
| RISK-006 Privilege escalation            | ADM-TC-011–025, 045, 056, 075, 091, 097–099, 110, 116, 220 |
| RISK-010 Fee calculation/configuration   | ADM-TC-112–119, 178, 215                                   |
| RISK-013 Concurrency                     | ADM-TC-057–058, 083, 148–155, 213, 216                     |
| RISK-021 Sensitive exposure              | ADM-TC-031–034, 092–093, 132–135, 185–188                  |
| RISK-022 Audit gap                       | ADM-TC-120–144, 173, 181–184, 217–219                      |
| RISK-023 Unauthorized admin              | ADM-TC-011–025, 045, 056, 075, 091, 097–099, 110, 116, 122 |
| RISK-024 Frozen card usable              | ADM-TC-089–090                                             |
| RISK-030 Unauthorized API                | ADM-TC-021–025, 156–166                                    |
| RISK-032 Sensitive audit/log exposure    | ADM-TC-132–135                                             |
| RISK-034 False notification              | ADM-TC-179–184                                             |
| RISK-037 Frontend-only validation        | ADM-TC-021–025, 162–166, 220                               |
| RISK-039 API/DB inconsistency            | ADM-TC-167–178                                             |
| RISK-042 Retry duplication               | ADM-TC-084, 101, 105                                       |
| RISK-048 UI/backend mismatch             | ADM-TC-067–068, 148–151, 174–178, 216                      |

---

# 67. Requirements Mapping

| Requirement                                      | Test Cases     |
| ------------------------------------------------ | -------------- |
| REQ-ADM-001 Admin authentication/session         | ADM-TC-001–010 |
| REQ-ADM-002 RBAC / least privilege               | ADM-TC-011–025 |
| REQ-ADM-003 Customer search/review               | ADM-TC-026–040 |
| REQ-ADM-004 KYC operations                       | ADM-TC-041–050 |
| REQ-ADM-005 Customer status management           | ADM-TC-051–058 |
| REQ-ADM-006 Account controls                     | ADM-TC-059–068 |
| REQ-ADM-007 Transaction investigation            | ADM-TC-069–073 |
| REQ-ADM-008 Reversal/refund controls             | ADM-TC-074–088 |
| REQ-ADM-009 Limits/fees/configuration            | ADM-TC-107–119 |
| REQ-ADM-010 Audit access/integrity               | ADM-TC-120–144 |
| REQ-ADM-011 Administrative integrity/concurrency | ADM-TC-145–220 |

---

# 68. Smoke Candidates

Recommended admin smoke coverage:

```text
ADM-TC-001
ADM-TC-005
ADM-TC-011
ADM-TC-013
ADM-TC-021
ADM-TC-026
ADM-TC-041
ADM-TC-042
ADM-TC-051
ADM-TC-059
ADM-TC-061
ADM-TC-069
ADM-TC-074
ADM-TC-095
ADM-TC-120
ADM-TC-129
```

---

# 69. Sanity Candidates

After admin functionality changes:

```text
ADM-TC-001
ADM-TC-011
ADM-TC-013
ADM-TC-026
ADM-TC-041
ADM-TC-042
ADM-TC-051
ADM-TC-059
ADM-TC-061
ADM-TC-067
ADM-TC-069
ADM-TC-074
ADM-TC-075
ADM-TC-083
ADM-TC-095
ADM-TC-108
ADM-TC-120
ADM-TC-129
ADM-TC-167
ADM-TC-174
```

---

# 70. Critical Regression Candidates

```text
ADM-TC-001–025

ADM-TC-031–068

ADM-TC-069–119

ADM-TC-120–178

ADM-TC-179–195

ADM-TC-207–220
```

---

# 71. UI Automation Candidates

Best suited for:

```text
Playwright

Selenium

Cypress
```

Strong candidates:

```text
ADM-TC-001–020

ADM-TC-026–155

ADM-TC-179–220
```

Playwright is particularly useful for:

```text
Admin vs customer browser contexts

Two-admin concurrency

Read-only vs operations-admin sessions

Stale state

Role revocation

Admin/customer end-to-end workflows
```

---

# 72. API Automation Candidates

Best suited for:

```text
REST Assured

Postman
```

Strong candidates:

```text
ADM-TC-005–025

ADM-TC-041–178

ADM-TC-192–220
```

High-value API automation includes:

```text
RBAC

Protected endpoints

KYC decisions

Customer-state changes

Freeze/unfreeze

Reversal

Loan decisions

Configuration changes

Audit authorization

Mass assignment

Concurrency
```

---

# 73. SQL / Database Testing Candidates

Strong SQL candidates:

```text
ADM-TC-041–068

ADM-TC-074–119

ADM-TC-120–178

ADM-TC-207–219
```

Database validation should verify:

```text
Customer status

KYC state

Account state

Transaction/reversal relationships

Loan decisions

Disbursement uniqueness

Deposit payout uniqueness

Limits

Fees

Effective dates

Audit events

Actor relationships
```

---

# 74. Jest Candidates

Jest is suitable for admin business-rule logic such as:

```text
RBAC permission matrices

Allowed state transitions

Required-reason rules

Limit boundaries

Fee validation

Dual-control rules

Admin field allowlists
```

Strong mappings include:

```text
ADM-TC-011–025

ADM-TC-041–068

ADM-TC-107–144

ADM-TC-162–166
```

---

# 75. Performance / JMeter Candidates

Strong candidates:

```text
ADM-TC-026–030

ADM-TC-069–073

ADM-TC-083

ADM-TC-145–155

ADM-TC-213
```

Performance testing may evaluate:

```text
Customer search latency

Transaction investigation latency

Audit search/filter performance

Concurrent admin actions

Bulk processing
```

Correct authorization must remain enforced under load.

---

# 76. Test Evidence Requirements

For critical admin tests, capture as applicable:

```text
Admin ID

Admin role

Customer ID

Target resource ID

Action

Previous state

Requested new state

Actual new state

Reason

Financial amount where applicable

Transaction/reference ID

API request/response

Database state

Audit record

Customer notification

Timestamp

Screenshot

Defect ID
```

Never include:

```text
Passwords

OTP values

Raw session tokens

MFA secrets

CVV

PIN
```

---

# 77. Admin Defect Examples

Potential Critical/High defects include:

```text
Read-only admin reverses a transfer.

Customer can call an admin endpoint.

KYC reviewer approves loans without permission.

Loan officer changes customer KYC state.

Admin directly modifies settled transaction amount.

Admin directly modifies account balance.

Two admins reverse the same transaction twice.

Two admins disburse the same loan twice.

Manual deposit payout runs after automatic payout.

Account freeze succeeds but customer can still transfer.

Admin change is not audited.

Audit record can be modified.

Audit log contains session token.

Admin search exposes password hash.

Stale admin page overwrites newer customer state.

Authorization service failure allows privileged action.

Failed admin action sends false success notification.
```

---

# 78. Admin Release Blockers

Production release should normally be blocked by unresolved issues involving:

```text
Admin authentication bypass

Privilege escalation

RBAC bypass

Customer access to admin APIs

Read-only role performing mutations

Unauthorized KYC/customer-state change

Unauthorized account freeze/unfreeze

Duplicate financial reversal

Duplicate loan disbursement

Duplicate deposit payout

Direct account-balance manipulation

Direct settled-transaction modification

Sensitive credential exposure

Missing audit for critical actions

Audit tampering

Fail-open privileged authorization

Critical admin/UI/API/DB state mismatch
```

---

# 79. Admin Exit Criteria

Admin testing is acceptable when:

```text
Admin authentication is secure.

Admin sessions expire/revoke correctly.

RBAC follows least privilege.

Direct APIs enforce the same permissions as the UI.

Customer search exposes only role-appropriate information.

KYC changes are authorized and traceable.

Customer status changes are authorized and traceable.

Account controls are enforced immediately.

Reversals preserve financial integrity and occur once.

Loan/disbursement operations are controlled.

Deposit payout operations cannot duplicate funds.

Limits and fees are validated and audited.

Audit records cannot be silently altered.

Sensitive secrets are never exposed.

Concurrency produces valid deterministic state.

Stale admin actions cannot overwrite authoritative state incorrectly.

UI/API/DB states agree.

No unresolved Critical/P0 admin defect remains.
```

---

# 80. Final Admin Testing Principle

The admin portal is one of the highest-risk areas of the Banking System because administrators can affect:

```text
Customer identity

KYC state

Account availability

Financial transactions

Cards

Loans

Deposits

Limits

Fees

Audit records
```

A privileged UI control is not itself a security control.

QA must verify:

```text
Who is the administrator?

What role do they currently have?

Is the requested operation permitted?

Is the target resource valid?

Is the current state still valid?

Is financial impact correct?

Was the action executed exactly once?

Was the action audited?

Was the customer notified correctly?

Did UI, API, and database converge on the same result?
```

The critical administrative invariant is:

```text
Every privileged banking operation
must be explicitly authorized,
validated against current authoritative state,
financially safe,
and fully traceable.
```

The core rule is:

```text
Administrative power must never bypass
the same financial integrity,
authorization,
auditability,
and security guarantees
that protect the rest of the Banking System.
```
