# Banking System — Admin & Operations Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Admin & Operations             |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for administrative and operational functionality within the Banking System.

Administrative functions are high risk because they may allow privileged users to:

* View customer information
* Modify customer status
* Freeze or restrict accounts
* Manage cards
* Review KYC
* Approve or reject loans
* Inspect transactions
* Perform reversals
* Manage limits
* View audit logs
* Perform operational actions

Incorrect administrative authorization or processing may result in privilege escalation, unauthorized financial changes, privacy violations, or loss of auditability.

---

# 3. Scope

Admin and operations testing includes:

* Admin authentication
* Admin roles
* Permissions
* Customer management
* Customer status changes
* KYC review
* Account administration
* Freeze/unfreeze operations
* Restrictions
* Card administration
* Beneficiary support
* Transfer oversight
* Payment oversight
* Loan administration
* Deposit administration
* Transaction investigation
* Reversals
* Limits
* Fees
* Audit logs
* Search/filtering
* Operational dashboards
* Notifications
* Data consistency
* Concurrency
* Privilege separation

---

# 4. Scenario Naming Convention

Admin scenarios use:

```text
TS-ADM-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Admin Authentication Scenarios

## TS-ADM-001 — Valid admin login

**Priority:** P0

Expected:

Authorized administrator successfully authenticates.

---

## TS-ADM-002 — Invalid admin password

**Priority:** P0

Expected:

Authentication rejected.

---

## TS-ADM-003 — Disabled admin login

**Priority:** P0

Expected:

Access denied.

---

## TS-ADM-004 — Locked admin account login

**Priority:** P0

Expected:

Lockout enforced.

---

## TS-ADM-005 — Admin session expires

**Priority:** P0

Expected:

Protected administrative actions require reauthentication.

---

## TS-ADM-006 — Admin logout

**Priority:** P0

Expected:

Session becomes unusable.

---

# 6. Admin Role Scenarios

Example administrative roles may include:

```text
SUPER_ADMIN
OPERATIONS_ADMIN
CUSTOMER_SUPPORT
KYC_REVIEWER
LOAN_OFFICER
AUDITOR
READ_ONLY_ADMIN
```

Actual roles must follow project implementation.

---

## TS-ADM-007 — Super admin accesses permitted administrative features

**Priority:** P0

Expected:

Authorized functions available.

---

## TS-ADM-008 — Read-only admin cannot perform state-changing operation

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-009 — KYC reviewer accesses KYC functions

**Priority:** P1

Expected:

Allowed.

---

## TS-ADM-010 — KYC reviewer attempts loan approval

**Priority:** P0

Expected:

Denied unless role explicitly permits it.

---

## TS-ADM-011 — Loan officer accesses loan-review functions

**Priority:** P1

Expected:

Allowed.

---

## TS-ADM-012 — Loan officer attempts system-admin configuration

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-013 — Auditor accesses audit logs

**Priority:** P1

Expected:

Allowed as read-only according to policy.

---

## TS-ADM-014 — Customer-support role attempts privileged financial reversal

**Priority:** P0

Expected:

Denied unless explicitly authorized.

---

# 7. Privilege Escalation Scenarios

## TS-ADM-015 — Limited admin modifies role field in request

**Priority:** P0

Expected:

Cannot elevate own privileges.

---

## TS-ADM-016 — Limited admin calls super-admin API directly

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-017 — Customer attempts admin endpoint

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-018 — Client-side hidden admin button manually enabled

**Priority:** P0

Expected:

Backend authorization still blocks unauthorized action.

---

## TS-ADM-019 — Admin attempts to modify own permissions without authority

**Priority:** P0

Expected:

Denied.

---

# 8. Admin Dashboard Scenarios

## TS-ADM-020 — Authorized admin opens dashboard

**Priority:** P1

Expected:

Operational metrics load correctly.

---

## TS-ADM-021 — Dashboard displays customer count

**Priority:** P2

Expected:

Matches authoritative data.

---

## TS-ADM-022 — Dashboard displays account count

**Priority:** P2

Expected:

Correct.

---

## TS-ADM-023 — Dashboard displays transaction summary

**Priority:** P1

Expected:

Correct aggregation.

---

## TS-ADM-024 — Dashboard displays pending administrative tasks

**Priority:** P1

Expected:

Correct counts.

---

## TS-ADM-025 — Dashboard data refreshes

**Priority:** P2

Expected:

Updated values appear without duplication.

---

# 9. Customer Search Scenarios

## TS-ADM-026 — Search customer by customer ID

**Priority:** P1

Expected:

Correct customer returned.

---

## TS-ADM-027 — Search customer by email

**Priority:** P1

Expected:

Correct customer returned.

---

## TS-ADM-028 — Search customer by phone number

**Priority:** P2

Expected:

Correct result where permitted.

---

## TS-ADM-029 — Search customer by name

**Priority:** P2

Expected:

Relevant results.

---

## TS-ADM-030 — Search nonexistent customer

**Priority:** P2

Expected:

Empty result.

---

## TS-ADM-031 — Search with leading/trailing spaces

**Priority:** P2

Expected:

Handled predictably.

---

## TS-ADM-032 — Search with special characters

**Priority:** P1

Expected:

Handled safely.

---

# 10. Customer Detail Scenarios

## TS-ADM-033 — Authorized admin views customer profile

**Priority:** P1

Expected:

Permitted customer data displayed.

---

## TS-ADM-034 — Limited admin sees only allowed customer fields

**Priority:** P0

Expected:

Field-level access restrictions enforced.

---

## TS-ADM-035 — Sensitive information masked

**Priority:** P0

Expected:

Protected values are not unnecessarily exposed.

---

## TS-ADM-036 — Admin views customer's account list

**Priority:** P1

Expected:

Correct accounts only.

---

## TS-ADM-037 — Admin views customer's cards

**Priority:** P1

Expected:

Correct cards with appropriate masking.

---

## TS-ADM-038 — Admin views customer's loans

**Priority:** P1

Expected:

Correct loan relationships.

---

## TS-ADM-039 — Admin views customer's deposits

**Priority:** P1

Expected:

Correct deposits.

---

# 11. Customer Status Administration

Possible statuses:

```text
ACTIVE
RESTRICTED
SUSPENDED
DISABLED
CLOSED
```

---

## TS-ADM-040 — Authorized admin restricts active customer

**Priority:** P0

Expected:

Customer state changes correctly.

---

## TS-ADM-041 — Authorized admin suspends customer

**Priority:** P0

Expected:

Status persists.

---

## TS-ADM-042 — Authorized admin disables customer

**Priority:** P0

Expected:

Customer can no longer access prohibited functions.

---

## TS-ADM-043 — Reactivate eligible restricted customer

**Priority:** P1

Expected:

Valid transition succeeds.

---

## TS-ADM-044 — Invalid customer status transition

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-045 — Customer status change requires reason

**Priority:** P1

Expected:

Reason captured where required.

---

## TS-ADM-046 — Customer status change audited

**Priority:** P0

Expected:

Actor, previous state, new state, reason, and timestamp recorded.

---

# 12. Customer Status Effect Scenarios

## TS-ADM-047 — Restricted customer transfer behavior updates immediately

**Priority:** P0

Expected:

New restriction enforced.

---

## TS-ADM-048 — Suspended customer active session attempts financial action

**Priority:** P0

Expected:

Current status revalidated and action rejected.

---

## TS-ADM-049 — Disabled customer existing session attempts protected API

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-050 — Reactivated customer regains permitted functionality

**Priority:** P1

Expected:

Behavior matches restored status.

---

# 13. KYC Administration Scenarios

Possible states:

```text
PENDING
VERIFIED
REJECTED
EXPIRED
```

---

## TS-ADM-051 — KYC reviewer views pending KYC case

**Priority:** P1

Expected:

Correct customer and submitted data displayed.

---

## TS-ADM-052 — Approve valid KYC case

**Priority:** P0

Expected:

Status becomes VERIFIED.

---

## TS-ADM-053 — Reject KYC case

**Priority:** P0

Expected:

Status becomes REJECTED.

---

## TS-ADM-054 — KYC rejection requires reason

**Priority:** P1

Expected:

Reason stored.

---

## TS-ADM-055 — Unauthorized admin attempts KYC approval

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-056 — Customer attempts to self-approve KYC

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-057 — Same KYC case approved twice

**Priority:** P0

Expected:

No duplicate state transition.

---

## TS-ADM-058 — Two reviewers process same KYC case concurrently

**Priority:** P0

Expected:

Single deterministic final state.

---

# 14. KYC Effect Scenarios

## TS-ADM-059 — Verified customer gains KYC-dependent functionality

**Priority:** P1

Expected:

Allowed after successful verification.

---

## TS-ADM-060 — Rejected customer remains blocked from restricted product

**Priority:** P0

Expected:

Business rule enforced.

---

## TS-ADM-061 — Expired KYC restricts configured financial actions

**Priority:** P0

Expected:

Current KYC state enforced.

---

# 15. Account Administration

## TS-ADM-062 — Admin views account details

**Priority:** P1

Expected:

Correct ownership, balance, status, and metadata.

---

## TS-ADM-063 — Admin searches account by account number

**Priority:** P1

Expected:

Correct account returned.

---

## TS-ADM-064 — Admin searches nonexistent account

**Priority:** P2

Expected:

No result.

---

## TS-ADM-065 — Limited admin attempts prohibited account update

**Priority:** P0

Expected:

Denied.

---

# 16. Account Freeze / Unfreeze

## TS-ADM-066 — Authorized admin freezes active account

**Priority:** P0

Expected:

Status becomes FROZEN.

---

## TS-ADM-067 — Frozen account cannot perform prohibited transfer

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-068 — Authorized admin unfreezes account

**Priority:** P0

Expected:

Status restored according to rules.

---

## TS-ADM-069 — Unauthorized admin attempts freeze

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-070 — Freeze action requires reason

**Priority:** P1

Expected:

Reason recorded.

---

## TS-ADM-071 — Freeze action audited

**Priority:** P0

Expected:

Complete trace.

---

# 17. Account Restriction Scenarios

## TS-ADM-072 — Apply transfer restriction

**Priority:** P0

Expected:

Transfers blocked while permitted operations remain available according to rule.

---

## TS-ADM-073 — Apply payment restriction

**Priority:** P0

Expected:

Payments blocked.

---

## TS-ADM-074 — Apply multiple restrictions

**Priority:** P0

Expected:

All selected restrictions enforced.

---

## TS-ADM-075 — Remove restriction

**Priority:** P1

Expected:

Permitted functionality restored.

---

## TS-ADM-076 — Restriction change while customer session active

**Priority:** P0

Expected:

Server enforces newest restriction state.

---

# 18. Account Closure Administration

## TS-ADM-077 — Authorized admin closes eligible zero-balance account

**Priority:** P0

Expected:

Account becomes CLOSED.

---

## TS-ADM-078 — Attempt closure with nonzero balance

**Priority:** P0

Expected:

Rejected unless special controlled process exists.

---

## TS-ADM-079 — Attempt closure with pending transaction

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-080 — Attempt closure with linked active financial dependencies

**Priority:** P0

Expected:

Business rules enforced.

---

## TS-ADM-081 — Closed account cannot transact

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-082 — Closure audited

**Priority:** P0

Expected:

Traceable.

---

# 19. Balance Administration Security

## TS-ADM-083 — Normal admin cannot directly edit balance

**Priority:** P0

Expected:

No arbitrary balance modification.

---

## TS-ADM-084 — Manipulate balance value in admin request

**Priority:** P0

Expected:

Rejected unless operation uses controlled accounting adjustment workflow.

---

## TS-ADM-085 — Authorized adjustment uses explicit transaction record

**Priority:** P0

Expected:

Financial correction is traceable rather than silent balance overwrite.

---

## TS-ADM-086 — Adjustment maintains account reconciliation

**Priority:** P0

Expected:

History and balance remain consistent.

---

# 20. Card Administration

## TS-ADM-087 — Authorized admin views customer's card

**Priority:** P1

Expected:

Sensitive information masked according to role.

---

## TS-ADM-088 — Authorized admin blocks card

**Priority:** P0

Expected:

Card becomes BLOCKED.

---

## TS-ADM-089 — Blocked card cannot transact

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-090 — Authorized admin initiates card replacement

**Priority:** P1

Expected:

Replacement workflow follows card rules.

---

## TS-ADM-091 — Limited admin attempts card replacement

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-092 — Card block/replacement audited

**Priority:** P0

Expected:

Traceable.

---

# 21. Beneficiary Support Scenarios

## TS-ADM-093 — Authorized support admin views beneficiary relationship

**Priority:** P2

Expected:

Only permitted data shown.

---

## TS-ADM-094 — Admin disables beneficiary where policy permits

**Priority:** P1

Expected:

Beneficiary becomes unusable.

---

## TS-ADM-095 — Disabled beneficiary cannot be used by customer

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-ADM-096 — Unauthorized admin attempts beneficiary change

**Priority:** P0

Expected:

Denied.

---

# 22. Transfer Oversight Scenarios

## TS-ADM-097 — Admin searches transfer by reference

**Priority:** P1

Expected:

Correct transfer returned.

---

## TS-ADM-098 — Admin views transfer details

**Priority:** P1

Expected:

Correct source, destination, amount, fee, status, and timestamps.

---

## TS-ADM-099 — Limited admin cannot modify transfer state directly

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-100 — Admin views failed transfer reason

**Priority:** P1

Expected:

Operational diagnostic information available without exposing secrets.

---

## TS-ADM-101 — Admin views pending transfer

**Priority:** P1

Expected:

Correct current state.

---

# 23. Transfer Reversal Administration

Where administrative reversals are supported.

## TS-ADM-102 — Authorized admin reverses eligible completed transfer

**Priority:** P0

Expected:

Controlled reversal succeeds.

---

## TS-ADM-103 — Unauthorized admin attempts reversal

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-104 — Reversal requires reason

**Priority:** P0

Expected:

Reason mandatory.

---

## TS-ADM-105 — Transfer cannot be reversed twice

**Priority:** P0

Expected:

Duplicate reversal prevented.

---

## TS-ADM-106 — Reversal creates separate transaction

**Priority:** P0

Expected:

Original history preserved.

---

## TS-ADM-107 — Reversal balances reconcile

**Priority:** P0

Expected:

Correct source/destination financial state.

---

## TS-ADM-108 — Reversal audited

**Priority:** P0

Expected:

Administrator, reason, original reference, reversal reference, timestamp recorded.

---

# 24. Payment Oversight Scenarios

## TS-ADM-109 — Search payment by reference

**Priority:** P1

Expected:

Correct payment returned.

---

## TS-ADM-110 — View payment status

**Priority:** P1

Expected:

Correct state.

---

## TS-ADM-111 — View failed payment reason

**Priority:** P1

Expected:

Operational details available.

---

## TS-ADM-112 — Unauthorized status modification

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-113 — Authorized payment reversal where supported

**Priority:** P0

Expected:

Controlled reversal.

---

# 25. Loan Administration

## TS-ADM-114 — Loan officer views pending loan applications

**Priority:** P1

Expected:

Correct queue.

---

## TS-ADM-115 — Loan officer opens application

**Priority:** P1

Expected:

Correct borrower/application details.

---

## TS-ADM-116 — Authorized loan approval

**Priority:** P0

Expected:

Application becomes APPROVED.

---

## TS-ADM-117 — Authorized loan rejection

**Priority:** P1

Expected:

Application becomes REJECTED.

---

## TS-ADM-118 — Unauthorized admin attempts approval

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-119 — Customer attempts loan approval endpoint

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-120 — Approved loan disbursed once

**Priority:** P0

Expected:

Exactly one financial credit.

---

## TS-ADM-121 — Approval and disbursement audited

**Priority:** P0

Expected:

Complete traceability.

---

# 26. Loan Concurrent Review Scenarios

## TS-ADM-122 — Two admins approve same loan simultaneously

**Priority:** P0

Expected:

One valid approval outcome.

---

## TS-ADM-123 — One admin approves while another rejects

**Priority:** P0

Expected:

No conflicting final state.

---

## TS-ADM-124 — Loan changes status while admin review page is stale

**Priority:** P0

Expected:

Submission revalidates current state.

---

# 27. Deposit Administration

## TS-ADM-125 — Admin views customer's deposit

**Priority:** P1

Expected:

Correct deposit details.

---

## TS-ADM-126 — Admin views maturity status

**Priority:** P1

Expected:

Correct state.

---

## TS-ADM-127 — Unauthorized admin attempts manual maturity payout

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-128 — Administrative early closure follows controlled rules

**Priority:** P0

Expected:

Penalty and payout calculated correctly.

---

## TS-ADM-129 — Deposit administrative action audited

**Priority:** P0

Expected:

Traceable.

---

# 28. Transaction Investigation

## TS-ADM-130 — Admin searches transaction by reference

**Priority:** P1

Expected:

Correct transaction.

---

## TS-ADM-131 — Admin searches by account

**Priority:** P1

Expected:

Authorized transaction history.

---

## TS-ADM-132 — Admin filters transactions by status

**Priority:** P2

Expected:

Correct records.

---

## TS-ADM-133 — Admin filters transactions by type

**Priority:** P2

Expected:

Correct.

---

## TS-ADM-134 — Admin filters transactions by date range

**Priority:** P2

Expected:

Correct results.

---

## TS-ADM-135 — Admin views original/reversal relationship

**Priority:** P0

Expected:

Traceable.

---

# 29. Transaction Immutability Administration

## TS-ADM-136 — Standard admin attempts to edit completed transaction amount

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-137 — Standard admin attempts to delete completed transaction

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-138 — Financial correction uses reversal/adjustment

**Priority:** P0

Expected:

Original history preserved.

---

# 30. Limit Administration

Where operational configuration permits.

## TS-ADM-139 — Authorized admin views configured transfer limits

**Priority:** P1

Expected:

Correct values.

---

## TS-ADM-140 — Authorized admin changes permitted limit

**Priority:** P0

Expected:

New value stored according to governance rules.

---

## TS-ADM-141 — Limit below permitted minimum

**Priority:** P1

Expected:

Rejected.

---

## TS-ADM-142 — Limit above permitted maximum

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-143 — Unauthorized admin changes limit

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-144 — Limit change audited

**Priority:** P0

Expected:

Old/new values and actor recorded.

---

## TS-ADM-145 — Customer transaction uses updated limit

**Priority:** P0

Expected:

Latest authoritative configuration enforced.

---

# 31. Fee Administration

Where fee configuration is supported.

## TS-ADM-146 — Authorized admin views fee configuration

**Priority:** P1

Expected:

Correct fee rules.

---

## TS-ADM-147 — Authorized admin updates permitted fee

**Priority:** P0

Expected:

New rule stored.

---

## TS-ADM-148 — Invalid negative fee

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-149 — Unsupported fee precision

**Priority:** P1

Expected:

Rejected.

---

## TS-ADM-150 — Fee change audited

**Priority:** P0

Expected:

Old/new configuration recorded.

---

## TS-ADM-151 — New transactions use updated fee according to effective-time rules

**Priority:** P0

Expected:

Correct fee applied.

---

# 32. Audit Log Viewing

## TS-ADM-152 — Authorized auditor views audit log

**Priority:** P0

Expected:

Correct records available.

---

## TS-ADM-153 — Unauthorized admin accesses audit log

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-154 — Customer accesses audit-log API

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-155 — Audit log includes actor

**Priority:** P0

Expected:

Correct user/admin identity.

---

## TS-ADM-156 — Audit log includes action

**Priority:** P0

Expected:

Correct operation.

---

## TS-ADM-157 — Audit log includes timestamp

**Priority:** P1

Expected:

Correct.

---

## TS-ADM-158 — Audit log includes result

**Priority:** P1

Expected:

Success/failure clearly represented.

---

# 33. Audit Log Integrity

## TS-ADM-159 — Admin cannot edit existing audit entry

**Priority:** P0

Expected:

Audit history immutable.

---

## TS-ADM-160 — Admin cannot delete audit history without special governed process

**Priority:** P0

Expected:

Integrity protected.

---

## TS-ADM-161 — Failed admin action creates audit/security event where required

**Priority:** P1

Expected:

Attempt traceable.

---

## TS-ADM-162 — Audit log does not expose password

**Priority:** P0

Expected:

No credentials stored.

---

## TS-ADM-163 — Audit log does not expose OTP or authentication token

**Priority:** P0

Expected:

Secrets redacted.

---

## TS-ADM-164 — Card information masked in audit records

**Priority:** P0

Expected:

No unnecessary full PAN.

---

# 34. Admin Search and Filtering

## TS-ADM-165 — Filter customers by status

**Priority:** P2

Expected:

Correct results.

---

## TS-ADM-166 — Filter KYC cases by status

**Priority:** P2

Expected:

Correct queue.

---

## TS-ADM-167 — Filter loans by application status

**Priority:** P2

Expected:

Correct.

---

## TS-ADM-168 — Filter transfers by status

**Priority:** P2

Expected:

Correct.

---

## TS-ADM-169 — Filter audit log by actor

**Priority:** P2

Expected:

Correct records.

---

## TS-ADM-170 — Filter audit log by date

**Priority:** P2

Expected:

Correct.

---

## TS-ADM-171 — Combine multiple filters

**Priority:** P2

Expected:

Correct intersection.

---

# 35. Pagination and Large Dataset Scenarios

## TS-ADM-172 — Customer list pagination

**Priority:** P2

Expected:

No missing or duplicate customers.

---

## TS-ADM-173 — Transaction list pagination

**Priority:** P2

Expected:

Correct.

---

## TS-ADM-174 — Audit-log pagination

**Priority:** P2

Expected:

Correct.

---

## TS-ADM-175 — Large customer dataset

**Priority:** P1

Expected:

Admin interface remains usable.

---

## TS-ADM-176 — Large audit dataset

**Priority:** P1

Expected:

Search/filtering remains correct.

---

# 36. Admin Data Export Scenarios

Where export functionality exists.

## TS-ADM-177 — Authorized admin exports permitted report

**Priority:** P1

Expected:

Correct authorized data.

---

## TS-ADM-178 — Limited admin attempts restricted export

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-179 — Export respects current filters

**Priority:** P1

Expected:

Correct dataset.

---

## TS-ADM-180 — Export contains no unauthorized sensitive fields

**Priority:** P0

Expected:

Data minimization enforced.

---

## TS-ADM-181 — Export action audited

**Priority:** P1

Expected:

Traceable.

---

# 37. Notification Scenarios

## TS-ADM-182 — Customer notified after account freeze

**Priority:** P0

Expected:

Correct customer receives accurate notification.

---

## TS-ADM-183 — Customer notified after account unfreeze

**Priority:** P1

Expected:

Correct.

---

## TS-ADM-184 — Customer notified after KYC decision

**Priority:** P1

Expected:

Status accurately reflected.

---

## TS-ADM-185 — Customer notified after loan decision

**Priority:** P1

Expected:

Correct.

---

## TS-ADM-186 — Failed admin action does not generate false success notification

**Priority:** P0

Expected:

No misleading message.

---

# 38. API Authorization Scenarios

## TS-ADM-187 — Admin API without authentication

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-188 — Customer authentication token used on admin API

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-189 — Limited admin accesses prohibited admin API

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-190 — Expired admin session calls API

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-191 — Admin modifies role/permission parameters

**Priority:** P0

Expected:

Backend permission model remains authoritative.

---

# 39. API Data Validation

## TS-ADM-192 — Invalid customer status submitted

**Priority:** P1

Expected:

Rejected.

---

## TS-ADM-193 — Invalid account status submitted

**Priority:** P1

Expected:

Rejected.

---

## TS-ADM-194 — Invalid loan decision state

**Priority:** P1

Expected:

Rejected.

---

## TS-ADM-195 — Restricted field included in request

**Priority:** P0

Expected:

Ignored or rejected.

---

## TS-ADM-196 — Missing mandatory administrative reason

**Priority:** P1

Expected:

Rejected where required.

---

# 40. Database Consistency Scenarios

## TS-ADM-197 — Customer status update persists correctly

**Priority:** P0

Expected:

UI/API/database agree.

---

## TS-ADM-198 — Account freeze persists correctly

**Priority:** P0

Expected:

Status consistent.

---

## TS-ADM-199 — KYC decision persists correctly

**Priority:** P0

Expected:

Correct state.

---

## TS-ADM-200 — Loan approval persists correctly

**Priority:** P0

Expected:

Correct state and decision metadata.

---

## TS-ADM-201 — Reversal records persist correctly

**Priority:** P0

Expected:

Original and reversal relationships intact.

---

## TS-ADM-202 — Audit records match administrative actions

**Priority:** P0

Expected:

Full traceability.

---

# 41. Concurrency Scenarios

## TS-ADM-203 — Two admins update same customer status simultaneously

**Priority:** P0

Expected:

No invalid final state.

---

## TS-ADM-204 — Two admins freeze/unfreeze same account concurrently

**Priority:** P0

Expected:

Final state deterministic.

---

## TS-ADM-205 — Admin closes account while transfer is processing

**Priority:** P0

Expected:

Financial operation and account state remain consistent.

---

## TS-ADM-206 — Two admins reverse same transfer simultaneously

**Priority:** P0

Expected:

Only one reversal succeeds.

---

## TS-ADM-207 — Two admins disburse same loan concurrently

**Priority:** P0

Expected:

Only one credit.

---

## TS-ADM-208 — Configuration updated by two admins simultaneously

**Priority:** P1

Expected:

No silent corruption; conflict behavior follows design.

---

# 42. Stale Data Scenarios

## TS-ADM-209 — Customer status changes while admin page remains open

**Priority:** P0

Expected:

Final action revalidates latest state.

---

## TS-ADM-210 — Loan application changes state before admin decision submission

**Priority:** P0

Expected:

Stale decision cannot overwrite newer state improperly.

---

## TS-ADM-211 — Transfer already reversed while admin reversal page remains open

**Priority:** P0

Expected:

Second reversal rejected.

---

## TS-ADM-212 — Account balance changes before administrative closure

**Priority:** P0

Expected:

Closure eligibility revalidated.

---

# 43. Error Handling Scenarios

## TS-ADM-213 — Admin service unavailable

**Priority:** P1

Expected:

Safe error shown.

---

## TS-ADM-214 — Customer-status update fails

**Priority:** P0

Expected:

No false success.

---

## TS-ADM-215 — Account freeze request times out

**Priority:** P0

Expected:

Final state can be determined safely before retry.

---

## TS-ADM-216 — Loan approval request times out

**Priority:** P0

Expected:

No duplicate approval/disbursement.

---

## TS-ADM-217 — Reversal service fails

**Priority:** P0

Expected:

No partial financial correction.

---

## TS-ADM-218 — Audit service failure during privileged action

**Priority:** P0

Expected:

Behavior follows governance rules; privileged action must not become untraceable.

---

# 44. Sensitive Data Scenarios

## TS-ADM-219 — Admin list does not expose customer passwords

**Priority:** P0

Expected:

Never visible.

---

## TS-ADM-220 — Password hashes not exposed

**Priority:** P0

Expected:

Protected.

---

## TS-ADM-221 — MFA secrets not exposed

**Priority:** P0

Expected:

Protected.

---

## TS-ADM-222 — Card PAN masked according to admin role

**Priority:** P0

Expected:

Only permitted digits shown.

---

## TS-ADM-223 — CVV/PIN never exposed

**Priority:** P0

Expected:

Protected.

---

## TS-ADM-224 — Authentication/session tokens not displayed

**Priority:** P0

Expected:

Protected.

---

# 45. Operational Dashboard Accuracy

## TS-ADM-225 — Pending loan count matches actual pending loans

**Priority:** P1

Expected:

Correct aggregation.

---

## TS-ADM-226 — Pending KYC count matches actual cases

**Priority:** P1

Expected:

Correct.

---

## TS-ADM-227 — Failed transaction count matches selected period

**Priority:** P1

Expected:

Correct.

---

## TS-ADM-228 — Dashboard totals use correct date/time boundaries

**Priority:** P1

Expected:

Accurate.

---

## TS-ADM-229 — Dashboard refresh does not double-count events

**Priority:** P1

Expected:

Correct aggregation.

---

# 46. Audit Trail End-to-End Scenarios

## TS-ADM-230 — Account freeze audit journey

**Priority:** P0

Flow:

```text
Admin Login
→ Search Customer
→ Open Account
→ Freeze Account
→ Enter Reason
→ Verify Account FROZEN
→ Verify Customer Restricted From Transfer
→ Verify Audit Record
→ Verify Customer Notification
```

---

## TS-ADM-231 — KYC decision audit journey

**Priority:** P0

Flow:

```text
KYC Reviewer Login
→ Open Pending Case
→ Review Data
→ Approve
→ Verify VERIFIED
→ Verify Customer Functionality
→ Verify Audit
→ Verify Notification
```

---

## TS-ADM-232 — Loan approval audit journey

**Priority:** P0

Flow:

```text
Loan Officer Login
→ Open Pending Application
→ Approve
→ Verify Application APPROVED
→ Disburse Loan
→ Verify Customer Account Credit
→ Verify Loan ACTIVE
→ Verify Audit Records
```

---

## TS-ADM-233 — Transfer reversal journey

**Priority:** P0

Flow:

```text
Authorized Admin Login
→ Search Completed Transfer
→ Review Transfer
→ Enter Reversal Reason
→ Confirm Reversal
→ Verify Reversal Transaction
→ Verify Balances
→ Verify Audit
→ Verify Customer Notification
```

---

# 47. Privilege Separation End-to-End

## TS-ADM-234 — Read-only admin cannot modify customer

**Priority:** P0

Flow:

```text
Login As Read-Only Admin
→ Open Customer
→ Attempt Status Change Through UI
→ Verify Action Not Available
→ Submit Direct API Request
→ Verify Denied
```

---

## TS-ADM-235 — KYC reviewer cannot approve loan

**Priority:** P0

Expected:

UI and API both deny unauthorized action.

---

## TS-ADM-236 — Loan officer cannot modify admin roles

**Priority:** P0

Expected:

Denied.

---

## TS-ADM-237 — Customer-support agent cannot reverse transaction without required permission

**Priority:** P0

Expected:

Denied.

---

# 48. High-Risk Financial Administration

## TS-ADM-238 — Admin cannot silently modify completed transaction amount

**Priority:** P0

Expected:

Financial history immutable.

---

## TS-ADM-239 — Admin cannot silently modify account balance

**Priority:** P0

Expected:

Accounting adjustment workflow required.

---

## TS-ADM-240 — Admin cannot delete financial transaction

**Priority:** P0

Expected:

Rejected.

---

## TS-ADM-241 — Financial correction preserves original transaction

**Priority:** P0

Expected:

Adjustment/reversal is separate and traceable.

---

# 49. Accessibility Scenarios

## TS-ADM-242 — Admin forms support keyboard navigation

**Priority:** P2

Expected:

Logical focus order.

---

## TS-ADM-243 — Destructive actions clearly identified

**Priority:** P1

Expected:

Freeze, close, reject, reverse, disable actions are unambiguous.

---

## TS-ADM-244 — Confirmation dialogs describe target resource

**Priority:** P0

Expected:

Admin understands which customer/account/transaction will be affected.

---

## TS-ADM-245 — Statuses not distinguished by color alone

**Priority:** P2

Expected:

Text/semantic indicators available.

---

# 50. Responsive Scenarios

## TS-ADM-246 — Admin dashboard on standard desktop

**Priority:** P1

Expected:

Fully usable.

---

## TS-ADM-247 — Admin interface on smaller desktop/laptop viewport

**Priority:** P1

Expected:

Critical controls remain accessible.

---

## TS-ADM-248 — Administrative confirmation modal at reduced viewport

**Priority:** P1

Expected:

Reason fields and action buttons visible.

---

# 51. Cross-Browser Scenarios

## TS-ADM-249 — Admin interface in Chrome

**Priority:** P1

Expected:

Works correctly.

---

## TS-ADM-250 — Admin interface in Edge

**Priority:** P1

Expected:

Works correctly.

---

## TS-ADM-251 — Admin interface in Firefox

**Priority:** P1

Expected:

Works correctly.

---

## TS-ADM-252 — Critical privileged flows across supported browsers

**Priority:** P0

Expected:

No browser-specific authorization/state defect.

---

# 52. Boundary Scenarios

## TS-ADM-253 — Administrative reason at minimum length

**Priority:** P2

Expected:

Accepted.

---

## TS-ADM-254 — Administrative reason below minimum length

**Priority:** P2

Expected:

Rejected where minimum exists.

---

## TS-ADM-255 — Administrative reason at maximum length

**Priority:** P2

Expected:

Accepted.

---

## TS-ADM-256 — Administrative reason above maximum

**Priority:** P2

Expected:

Rejected.

---

## TS-ADM-257 — Limit exact maximum permitted value

**Priority:** P0

Expected:

Accepted where inclusive.

---

## TS-ADM-258 — Limit maximum plus smallest unit

**Priority:** P0

Expected:

Rejected.

---

# 53. Critical Smoke Scenarios

Admin smoke coverage should include:

```text
TS-ADM-001 — Valid admin login
TS-ADM-008 — Read-only admin cannot modify data
TS-ADM-033 — View customer
TS-ADM-040 — Restrict customer
TS-ADM-066 — Freeze account
TS-ADM-088 — Block card
TS-ADM-116 — Approve loan
TS-ADM-152 — View audit log
TS-ADM-187 — Admin API requires authentication
```

---

# 54. Critical Regression Scenarios

Always prioritize:

* Admin authentication
* Role permissions
* Privilege escalation
* Customer status changes
* KYC decisions
* Account freeze/unfreeze
* Account restrictions
* Account closure
* Balance immutability
* Card administration
* Transfer reversal
* Loan approval/disbursement
* Deposit administration
* Transaction immutability
* Limit and fee configuration
* Audit-log integrity
* Sensitive-data protection
* API authorization
* Concurrent privileged actions
* Stale-data handling

---

# 55. UI Automation Candidates

Strong admin UI automation candidates include:

* Admin login
* Customer search
* Customer status change
* KYC approval/rejection
* Account freeze/unfreeze
* Card blocking
* Loan approval/rejection
* Transaction search
* Audit-log search/filtering
* Role-based access checks

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 56. API Automation Candidates

Postman and REST Assured should later cover:

* Admin authentication
* Role authorization
* Customer search
* Customer status change
* KYC decisions
* Account freeze/unfreeze
* Card blocking
* Loan approval/rejection
* Transaction reversal
* Limit configuration
* Fee configuration
* Audit-log access
* Restricted-field manipulation
* Unauthorized admin operations

---

# 57. SQL Validation Candidates

Database testing should validate:

* Admin role assignments
* Customer status
* KYC status
* Account status
* Card status
* Loan decisions
* Disbursement records
* Reversal records
* Limits
* Fees
* Audit entries
* Actor IDs
* Reason fields
* State timestamps

---

# 58. Performance Testing Candidates

JMeter may later cover:

* Admin customer search
* Transaction search
* Audit-log retrieval
* Large operational queues
* Dashboard metrics
* Concurrent administrative actions

Performance testing must confirm that slow responses do not cause duplicate privileged operations.

---

# 59. BDD Candidates

Example:

```gherkin
Feature: Administrative account freeze

Scenario: Authorized operations admin freezes a customer account
  Given an operations admin is authenticated
  And the customer account is active
  When the admin freezes the account with a valid reason
  Then the account should become frozen
  And the customer should no longer be able to perform prohibited transactions
  And the action should be recorded in the audit log
```

Role-separation example:

```gherkin
Scenario: Read-only admin cannot freeze a customer account
  Given a read-only administrator is authenticated
  When the administrator attempts to freeze a customer account
  Then the request should be denied
  And the account status should remain unchanged
```

Reversal example:

```gherkin
Scenario: A completed transfer can only be reversed once
  Given an authorized administrator has reversed a completed transfer
  When another reversal is attempted for the same transfer
  Then the second reversal should be rejected
  And no additional financial adjustment should occur
```

---

# 60. Risk Traceability

Major related risks include:

```text
RISK-001 — Incorrect account balance
RISK-002 — Unauthorized customer data access
RISK-003 — Duplicate financial transaction
RISK-004 — Partial transaction processing
RISK-006 — Privilege escalation
RISK-009 — Frozen account can transact
RISK-011 — Incorrect loan calculations
RISK-013 — Concurrent operations corrupt financial state
RISK-021 — Sensitive information exposure
RISK-022 — Administrative action not audited
RISK-023 — Unauthorized admin operation
RISK-024 — Frozen card remains usable
RISK-025 — Blocked card remains usable
RISK-030 — Unauthorized API request
RISK-039 — API/database inconsistency
RISK-047 — Unauthorized resource access
```

---

# 61. Admin & Operations Coverage Summary

This catalog covers:

* Admin authentication
* Roles
* Permissions
* Privilege escalation
* Dashboard
* Customer search
* Customer details
* Customer status
* KYC
* Account administration
* Freeze/unfreeze
* Restrictions
* Closure
* Balance integrity
* Cards
* Beneficiaries
* Transfers
* Reversals
* Payments
* Loans
* Deposits
* Transaction investigation
* Transaction immutability
* Limits
* Fees
* Audit logs
* Search/filtering
* Pagination
* Data exports
* Notifications
* API authorization
* Database consistency
* Concurrency
* Stale data
* Error handling
* Sensitive data
* Operational metrics
* Privilege separation
* Accessibility
* Responsive behavior
* Cross-browser behavior
* Boundary testing
* End-to-end workflows

---

# 62. Final Admin Testing Principle

Administrative functionality must follow stricter controls than ordinary customer functionality because administrative actions can affect many customers and financial records.

For every privileged operation, QA should be able to answer:

```text
Is the administrator authenticated?

Does this administrator's exact role permit the action?

Is the target customer/account/resource correct?

Is the latest resource state being validated?

Is a reason required?

Can the operation be performed twice accidentally?

Can a limited admin bypass UI restrictions through the API?

Does the action preserve financial history?

Does it create an appropriate audit record?

Does the audit record identify the actor?

Are sensitive values protected?

Does the customer receive the correct notification where required?

Do UI, API, and database states agree?
```

The core administrative rule is:

```text
Privileged access must be explicit.

Permissions must be enforced server-side.

Financial history must never be silently overwritten.

Every critical administrative action must be attributable and auditable.
```
