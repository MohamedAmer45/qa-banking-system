# Banking System — Customer Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Customers                      |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Customer scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Viewing customer profile
* Ownership and isolation
* Personal information updates
* Protected fields
* KYC states
* Customer account states
* Restrictions
* Suspension
* Closure
* Admin operations
* Search and filtering
* API validation
* Database consistency
* Concurrency
* Audit
* Notifications
* Security
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Customer test cases use:

```text
CUST-TC-XXX
```

Example:

```text
CUST-TC-001
CUST-TC-002
CUST-TC-003
```

---

# 4. Common Test Data

## Customer A

```text
Customer ID:
CUST-001

Status:
ACTIVE

KYC:
VERIFIED
```

## Customer B

```text
Customer ID:
CUST-002

Status:
ACTIVE

KYC:
VERIFIED
```

## Pending KYC Customer

```text
Customer ID:
CUST-003

Status:
ACTIVE

KYC:
PENDING
```

## Rejected KYC Customer

```text
Customer ID:
CUST-004

Status:
ACTIVE

KYC:
REJECTED
```

## Restricted Customer

```text
Customer ID:
CUST-005

Status:
RESTRICTED
```

## Suspended Customer

```text
Customer ID:
CUST-006

Status:
SUSPENDED
```

## Disabled Customer

```text
Customer ID:
CUST-007

Status:
DISABLED
```

## Closed Customer

```text
Customer ID:
CUST-008

Status:
CLOSED
```

---

# 5. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer service is available.

Authentication is functioning.

Required synthetic customer records exist.

Test user is authenticated with appropriate role.

No real customer information is used.
```

---

# 6. View Profile Test Cases

## CUST-TC-001 — Customer Views Own Profile

**Priority:** P0
**Requirement:** REQ-CUST-001
**Automation:** Playwright / Selenium / API

### Steps

1. Login as `CUST-001`.
2. Open Profile.
3. Review displayed customer information.

### Expected Result

* Customer sees only their own profile.
* Allowed personal fields are displayed correctly.
* Protected/internal fields are not improperly exposed.

---

## CUST-TC-002 — Profile Data Matches API

**Priority:** P1

### Steps

1. Open profile.
2. Call profile API using same customer.
3. Compare user-facing values.

### Expected Result

UI and API display consistent allowed profile information.

---

## CUST-TC-003 — Profile Data Matches Database

**Priority:** P1
**Automation:** SQL validation

### Expected Result

Displayed persistent values match expected database state.

---

# 7. Customer Ownership / IDOR Test Cases

## CUST-TC-004 — Customer Cannot View Another Customer's Profile

**Priority:** P0
**Requirement:** REQ-CUST-002
**Risk:** RISK-002, RISK-047

### Preconditions

Authenticated:

```text
CUST-001
```

Target:

```text
CUST-002
```

### Steps

1. Login as Customer A.
2. Attempt direct request for Customer B profile resource.

### Expected Result

* Access denied.
* Customer B data is not returned.
* No sensitive metadata is leaked.

---

## CUST-TC-005 — Manipulate Customer ID in URL

**Priority:** P0

### Steps

1. Open own profile URL.
2. Replace own customer ID with `CUST-002`.
3. Submit.

### Expected Result

Unauthorized access denied.

---

## CUST-TC-006 — Manipulate Customer ID in API Request

**Priority:** P0
**Automation:** REST Assured

### Expected Result

Backend validates ownership independently of client-provided ID.

---

## CUST-TC-007 — Customer Cannot Update Another Customer

**Priority:** P0

### Steps

1. Login as `CUST-001`.
2. Send profile update request targeting `CUST-002`.

### Expected Result

Request denied.

Customer B remains unchanged.

---

# 8. Personal Information Update Test Cases

## CUST-TC-008 — Update Valid First Name

**Priority:** P1

### Steps

1. Login.
2. Open profile edit.
3. Enter valid first name.
4. Save.

### Expected Result

* Update succeeds.
* New value persists.
* Audit/notification occurs if required.

---

## CUST-TC-009 — Update Valid Last Name

**Priority:** P1

### Expected Result

Valid update persists correctly.

---

## CUST-TC-010 — Update Valid Address

**Priority:** P1

### Expected Result

Address is updated according to supported format.

---

## CUST-TC-011 — Update Phone Number

**Priority:** P1

### Expected Result

Phone update follows verification policy where configured.

---

## CUST-TC-012 — Update Email

**Priority:** P0

### Expected Result

Email update follows required verification flow.

Old/new email behavior matches policy.

---

## CUST-TC-013 — Invalid Email Update

**Priority:** P2

### Expected Result

Invalid format rejected.

---

## CUST-TC-014 — Invalid Phone Update

**Priority:** P2

### Expected Result

Invalid value rejected according to supported format.

---

## CUST-TC-015 — Empty Required Name

**Priority:** P2

### Expected Result

Required value cannot be removed.

---

## CUST-TC-016 — Name With Supported Unicode Characters

**Priority:** P2

### Expected Result

Supported international characters persist without corruption.

---

## CUST-TC-017 — Leading and Trailing Whitespace

**Priority:** P2

### Expected Result

Normalization follows defined rules without unexpected persistent whitespace.

---

## CUST-TC-018 — Maximum Name Length

**Priority:** P2
**Type:** Boundary Value Analysis

### Expected Result

Maximum supported length accepted.

---

## CUST-TC-019 — Maximum Name Length Plus One

**Priority:** P2

### Expected Result

Rejected safely.

---

# 9. Protected Field Test Cases

## CUST-TC-020 — Customer Cannot Change Customer ID

**Priority:** P0

### Expected Result

Customer ID is immutable through customer-facing functionality.

---

## CUST-TC-021 — Customer Cannot Modify Role

**Priority:** P0
**Risk:** RISK-006

### Steps

Submit:

```json
{
  "role": "ADMIN"
}
```

### Expected Result

* Protected field ignored or request rejected.
* Role remains `CUSTOMER`.

---

## CUST-TC-022 — Customer Cannot Modify KYC Status

**Priority:** P0
**Risk:** RISK-006, RISK-037

### Steps

Submit:

```json
{
  "kycStatus": "VERIFIED"
}
```

### Expected Result

Customer cannot alter KYC state.

---

## CUST-TC-023 — Customer Cannot Modify Account Status Through Profile

**Priority:** P0

### Expected Result

Protected banking state cannot be changed through general profile API.

---

## CUST-TC-024 — Customer Cannot Modify Internal Risk Score

**Priority:** P0

### Expected Result

Internal-only fields are ignored/rejected and not exposed.

---

## CUST-TC-025 — Mass Assignment With Multiple Protected Fields

**Priority:** P0
**Automation:** REST Assured

### Steps

Submit payload containing:

```json
{
  "role": "SUPER_ADMIN",
  "kycStatus": "VERIFIED",
  "customerStatus": "ACTIVE",
  "riskScore": 0
}
```

### Expected Result

No protected field changes.

---

# 10. KYC Test Cases

## CUST-TC-026 — Verified Customer State

**Priority:** P1
**Requirement:** REQ-CUST-004

### Expected Result

Customer marked `VERIFIED` receives intended access to eligible features.

---

## CUST-TC-027 — Pending KYC Restrictions

**Priority:** P0
**Requirement:** REQ-CUST-008

### Preconditions

```text
KYC:
PENDING
```

### Expected Result

Restricted products/actions are blocked according to business rules.

---

## CUST-TC-028 — Rejected KYC Restrictions

**Priority:** P0

### Expected Result

Relevant restricted banking capabilities are unavailable.

---

## CUST-TC-029 — Expired KYC Restrictions

**Priority:** P0

### Expected Result

Expired KYC state triggers required restrictions/reverification flow.

---

## CUST-TC-030 — Admin Changes KYC Pending to Verified

**Priority:** P0

### Preconditions

Authorized role:

```text
KYC_REVIEWER
```

### Expected Result

* State changes to `VERIFIED`.
* Audit record created.
* Customer-facing eligibility updates correctly.

---

## CUST-TC-031 — Unauthorized Admin Cannot Change KYC

**Priority:** P0
**Risk:** RISK-023

### Expected Result

Role without KYC authority is denied.

---

## CUST-TC-032 — Customer KYC State API/UI Consistency

**Priority:** P1

### Expected Result

UI and API represent same authoritative KYC state.

---

## CUST-TC-033 — KYC State DB Consistency

**Priority:** P1

### Expected Result

Persistent state matches API and expected transition.

---

# 11. Customer Status Test Cases

## CUST-TC-034 — Active Customer Uses Banking Normally

**Priority:** P0

### Expected Result

Permitted banking actions remain available.

---

## CUST-TC-035 — Restricted Customer

**Priority:** P0
**Requirement:** REQ-CUST-005

### Expected Result

Configured restrictions are enforced while allowed read-only functionality remains available where intended.

---

## CUST-TC-036 — Suspended Customer

**Priority:** P0
**Requirement:** REQ-CUST-006

### Expected Result

Suspension rules are enforced consistently.

---

## CUST-TC-037 — Disabled Customer

**Priority:** P0

### Expected Result

Disabled customer cannot perform protected banking operations according to policy.

---

## CUST-TC-038 — Closed Customer

**Priority:** P0
**Requirement:** REQ-CUST-007

### Expected Result

Closed customer cannot perform financial actions.

---

# 12. Customer Status Transition Test Cases

## CUST-TC-039 — ACTIVE → RESTRICTED

**Priority:** P0
**Type:** State Transition

### Expected Result

Transition succeeds only for authorized actor.

Restrictions apply immediately.

---

## CUST-TC-040 — RESTRICTED → ACTIVE

**Priority:** P1

### Expected Result

Customer regains permitted functionality after authorized restoration.

---

## CUST-TC-041 — ACTIVE → SUSPENDED

**Priority:** P0

### Expected Result

Suspension applies correctly and is audited.

---

## CUST-TC-042 — SUSPENDED → ACTIVE

**Priority:** P1

### Expected Result

Only authorized recovery transition succeeds.

---

## CUST-TC-043 — ACTIVE → DISABLED

**Priority:** P0

### Expected Result

Disabled state prevents protected operations.

---

## CUST-TC-044 — ACTIVE → CLOSED

**Priority:** P0

### Expected Result

Closure succeeds only when closure requirements are satisfied.

---

## CUST-TC-045 — CLOSED → ACTIVE Invalid Transition

**Priority:** P0

### Expected Result

Terminal closed state cannot be silently reactivated unless explicit supported process exists.

---

# 13. Customer Closure Test Cases

## CUST-TC-046 — Close Customer With No Dependencies

**Priority:** P1

### Preconditions

Customer meets all closure criteria.

### Expected Result

Customer is closed successfully and audit record exists.

---

## CUST-TC-047 — Close Customer With Positive Account Balance

**Priority:** P0

### Expected Result

Closure rejected if business rules require zero/settled balance.

---

## CUST-TC-048 — Close Customer With Pending Transfer

**Priority:** P0

### Expected Result

Closure rejected or handled according to defined dependency policy.

---

## CUST-TC-049 — Close Customer With Active Loan

**Priority:** P0

### Expected Result

Closure blocked where active loan dependency prevents closure.

---

## CUST-TC-050 — Close Customer With Active Deposit

**Priority:** P0

### Expected Result

Closure blocked or requires defined deposit resolution.

---

## CUST-TC-051 — Customer Attempts Self-Closure Through Unauthorized API

**Priority:** P0

### Expected Result

Request denied if customer self-closure is not supported.

---

# 14. Admin Customer Search Test Cases

## CUST-TC-052 — Search by Customer ID

**Priority:** P1

### Expected Result

Correct customer returned.

---

## CUST-TC-053 — Search by Email

**Priority:** P1

### Expected Result

Matching customer returned according to permissions.

---

## CUST-TC-054 — Search by Name

**Priority:** P2

### Expected Result

Relevant customers returned.

---

## CUST-TC-055 — Search Unknown Customer

**Priority:** P2

### Expected Result

No-result state displayed clearly.

---

## CUST-TC-056 — Admin Search With Partial Match

**Priority:** P2

### Expected Result

Behavior follows search specification.

---

# 15. Admin Customer Filtering Test Cases

## CUST-TC-057 — Filter by Customer Status

**Priority:** P2

### Expected Result

Only selected statuses returned.

---

## CUST-TC-058 — Filter by KYC Status

**Priority:** P2

### Expected Result

Correct filtered population shown.

---

## CUST-TC-059 — Combine Status + KYC Filter

**Priority:** P2

### Expected Result

Intersection of filters returned correctly.

---

## CUST-TC-060 — Clear Filters

**Priority:** P3

### Expected Result

Full allowed result set restored.

---

# 16. Admin Authorization Test Cases

## CUST-TC-061 — Operations Admin Views Customer

**Priority:** P1

### Expected Result

Authorized fields visible.

---

## CUST-TC-062 — Read-Only Admin Views Customer

**Priority:** P1

### Expected Result

Read-only access works without state-changing permission.

---

## CUST-TC-063 — Read-Only Admin Attempts Status Change

**Priority:** P0
**Risk:** RISK-006, RISK-023

### Expected Result

Denied.

No customer state change.

---

## CUST-TC-064 — Customer Support Attempts Restricted KYC Change

**Priority:** P0

### Expected Result

Denied if role lacks KYC authority.

---

## CUST-TC-065 — KYC Reviewer Attempts Unrelated High-Risk Admin Action

**Priority:** P0

### Expected Result

Role boundaries enforced.

---

# 17. Sensitive Data Exposure Test Cases

## CUST-TC-066 — Customer Profile Does Not Expose Internal Fields

**Priority:** P0
**Risk:** RISK-021

### Check for absence of:

```text
Internal risk score

Internal notes

Admin-only flags

Authentication secrets

System-only identifiers where not needed
```

### Expected Result

Only intended customer-facing fields returned.

---

## CUST-TC-067 — Admin Role Sees Only Authorized Sensitive Fields

**Priority:** P0

### Expected Result

Role-based field-level access enforced where applicable.

---

## CUST-TC-068 — Customer Search Does Not Expose Password Data

**Priority:** P0

### Expected Result

No plaintext password or password hash is returned to unauthorized interfaces.

---

## CUST-TC-069 — API Error Does Not Leak Customer Internals

**Priority:** P1

### Expected Result

Errors do not expose stack traces, secrets, or unnecessary internal records.

---

# 18. API Customer Test Cases

## CUST-TC-070 — Get Own Profile API

**Priority:** P0
**Automation:** REST Assured

### Expected Result

Authenticated customer receives own permitted profile fields.

---

## CUST-TC-071 — Get Another Customer Profile API

**Priority:** P0

### Expected Result

Denied.

---

## CUST-TC-072 — Update Own Allowed Field API

**Priority:** P1

### Expected Result

Allowed field updates correctly.

---

## CUST-TC-073 — Update Protected Field API

**Priority:** P0

### Expected Result

Protected field unchanged.

---

## CUST-TC-074 — Unauthenticated Profile API

**Priority:** P0

### Expected Result

Authentication required.

---

## CUST-TC-075 — Invalid Customer ID Format

**Priority:** P2

### Expected Result

Safe validation response.

No server error.

---

# 19. Database Validation Test Cases

## CUST-TC-076 — Customer Ownership Record

**Priority:** P1

### Expected Result

Customer-account relationships reference expected customer.

---

## CUST-TC-077 — Profile Update Persistence

**Priority:** P1

### Expected Result

Updated allowed field persists exactly once.

---

## CUST-TC-078 — Protected Field Remains Unchanged

**Priority:** P0

### Expected Result

Mass-assignment attempt does not alter protected DB columns.

---

## CUST-TC-079 — Customer Status Persistence

**Priority:** P1

### Expected Result

Status transition persists correctly.

---

## CUST-TC-080 — KYC State Persistence

**Priority:** P1

### Expected Result

KYC state matches authorized transition.

---

# 20. Concurrency Test Cases

## CUST-TC-081 — Two Admins Update Same Customer Status

**Priority:** P0
**Risk:** RISK-013

### Steps

1. Admin A loads customer as `ACTIVE`.
2. Admin B loads same customer.
3. Admin A sets `RESTRICTED`.
4. Admin B attempts `SUSPENDED` using stale state.

### Expected Result

System follows defined concurrency strategy.

No unexplained state corruption occurs.

---

## CUST-TC-082 — Concurrent Profile Updates

**Priority:** P1

### Steps

Two sessions update different fields simultaneously.

### Expected Result

Updates are handled according to concurrency design without silent unintended data loss.

---

## CUST-TC-083 — Customer Update While Admin Disables Account

**Priority:** P0

### Expected Result

Final state remains valid.

Protected admin status is not overwritten by stale customer payload.

---

# 21. Stale Data Test Cases

## CUST-TC-084 — Stale Profile Page After Admin Status Change

**Priority:** P1

### Steps

1. Customer opens profile.
2. Admin changes customer state.
3. Customer submits stale profile update.

### Expected Result

Customer cannot accidentally overwrite authoritative status.

---

## CUST-TC-085 — Stale Admin Customer Screen

**Priority:** P1

### Expected Result

Concurrent admin changes are handled safely.

---

# 22. Input Security Test Cases

## CUST-TC-086 — Script-Like Name Input

**Priority:** P1

### Test Data

```text
<script>alert(1)</script>
```

### Expected Result

Input safely handled.

No executable script runs when displayed.

---

## CUST-TC-087 — SQL-Like Input in Address

**Priority:** P1

### Expected Result

No query manipulation or database error.

---

## CUST-TC-088 — Very Long Address

**Priority:** P2

### Expected Result

Boundary validation works safely.

---

## CUST-TC-089 — Unexpected JSON Fields

**Priority:** P1

### Expected Result

Unknown fields handled according to API contract without modifying unintended state.

---

# 23. Audit Test Cases

## CUST-TC-090 — Admin Customer Status Change Audit

**Priority:** P0
**Risk:** RISK-022

### Expected Result

Audit includes:

```text
Actor

Target Customer

Old Status

New Status

Timestamp

Reason where required
```

---

## CUST-TC-091 — KYC Change Audit

**Priority:** P0

### Expected Result

Authorized reviewer decision is traceable.

---

## CUST-TC-092 — Sensitive Data Not Stored in Audit

**Priority:** P0
**Risk:** RISK-032

### Expected Result

Audit does not store passwords, tokens, OTPs, or unnecessary secrets.

---

# 24. Notification Test Cases

## CUST-TC-093 — Customer Status Change Notification

**Priority:** P1

### Expected Result

Notification generated where required and reflects actual new state.

---

## CUST-TC-094 — KYC Approval Notification

**Priority:** P1

### Expected Result

Customer receives accurate approval notification.

---

## CUST-TC-095 — KYC Rejection Notification

**Priority:** P1

### Expected Result

Notification reflects rejection accurately without inappropriate internal data.

---

## CUST-TC-096 — Failed Admin Change Sends No False Success Notification

**Priority:** P1

### Expected Result

No success notification for failed state change.

---

# 25. Search / Pagination Test Cases

## CUST-TC-097 — Customer Search Pagination

**Priority:** P2

### Expected Result

No duplicate/missing customers between pages under stable dataset.

---

## CUST-TC-098 — Sort Customers by Name

**Priority:** P3

### Expected Result

Correct ordering.

---

## CUST-TC-099 — Sort Customers by Created Date

**Priority:** P3

### Expected Result

Correct ordering.

---

## CUST-TC-100 — Filter + Pagination

**Priority:** P2

### Expected Result

Pagination remains consistent within filtered result set.

---

# 26. Cross-Browser Test Cases

## CUST-TC-101 — Profile in Chrome

**Priority:** P2

### Expected Result

Profile behaves correctly.

---

## CUST-TC-102 — Profile in Edge

**Priority:** P2

### Expected Result

Profile behaves correctly.

---

## CUST-TC-103 — Profile in Firefox

**Priority:** P2

### Expected Result

Profile behaves correctly.

---

## CUST-TC-104 — Profile in WebKit

**Priority:** P2

### Expected Result

Profile behaves correctly.

---

# 27. Responsive Test Cases

## CUST-TC-105 — Profile at 390×844

**Priority:** P2

### Expected Result

Important customer information and controls remain usable.

---

## CUST-TC-106 — Profile Edit at 360×800

**Priority:** P2

### Expected Result

Save/cancel controls remain reachable.

---

## CUST-TC-107 — Admin Customer Screen at Tablet View

**Priority:** P2

### Expected Result

Critical operational actions remain accessible without accidental overlap.

---

# 28. Accessibility Test Cases

## CUST-TC-108 — Keyboard-Only Profile Navigation

**Priority:** P2

### Expected Result

Interactive controls accessible in logical order.

---

## CUST-TC-109 — Customer Form Labels

**Priority:** P2

### Expected Result

Fields have meaningful accessible labels.

---

## CUST-TC-110 — Validation Error Accessibility

**Priority:** P2

### Expected Result

Errors can be associated with corresponding fields.

---

# 29. Customer State End-to-End Tests

## CUST-TC-111 — Active → Restricted Journey

**Priority:** P0

### Steps

1. Customer begins in `ACTIVE`.
2. Verify normal banking access.
3. Authorized admin changes to `RESTRICTED`.
4. Customer refreshes.
5. Attempt restricted transaction.
6. Attempt allowed functionality.

### Expected Result

* State changes correctly.
* Restricted functionality denied.
* Allowed functionality remains available according to policy.
* Audit exists.

---

## CUST-TC-112 — Suspended Customer Recovery Journey

**Priority:** P1

### Steps

1. Customer begins `SUSPENDED`.
2. Verify restricted behavior.
3. Authorized admin restores `ACTIVE`.
4. Customer reauthenticates if required.
5. Verify normal access.

### Expected Result

Lifecycle works as defined.

---

## CUST-TC-113 — Customer Closure Journey

**Priority:** P0

### Preconditions

All closure requirements satisfied.

### Steps

1. Admin initiates closure.
2. Verify dependency checks.
3. Confirm closure.
4. Attempt customer login/financial action.
5. Review audit.

### Expected Result

* Customer becomes `CLOSED`.
* Financial actions unavailable.
* Closed state persists.
* Audit created.

---

# 30. KYC End-to-End Test

## CUST-TC-114 — Pending → Verified Journey

**Priority:** P0

### Steps

1. Login as `CUST-003`.
2. Verify restricted feature.
3. Login as KYC reviewer.
4. Approve customer.
5. Verify KYC becomes `VERIFIED`.
6. Customer refreshes/re-authenticates.
7. Verify newly eligible feature.

### Expected Result

Business eligibility updates according to KYC rules.

---

# 31. Mass Assignment End-to-End Test

## CUST-TC-115 — Protected Customer Field Manipulation

**Priority:** P0
**Automation:** REST Assured

### Payload

```json
{
  "firstName": "Updated",
  "role": "SUPER_ADMIN",
  "kycStatus": "VERIFIED",
  "customerStatus": "ACTIVE"
}
```

### Expected Result

```text
Allowed field:
firstName → updated

Protected fields:
role → unchanged
kycStatus → unchanged
customerStatus → unchanged
```

---

# 32. Customer Isolation End-to-End Test

## CUST-TC-116 — Customer A vs Customer B

**Priority:** P0

### Steps

Customer A attempts to access Customer B:

```text
Profile

Accounts

Transactions

Statements

Cards

Notifications
```

### Expected Result

Every protected cross-customer request is denied.

No Customer B sensitive data appears.

---

# 33. Customer Risk Mapping

| Risk                                | Related Test Cases                |
| ----------------------------------- | --------------------------------- |
| RISK-002 Unauthorized customer data | CUST-TC-004–007, 066–071, 116     |
| RISK-006 Privilege escalation       | CUST-TC-021–025, 063–065, 115     |
| RISK-021 Sensitive exposure         | CUST-TC-066–069, 092              |
| RISK-022 Missing audit              | CUST-TC-090–092                   |
| RISK-023 Unauthorized admin         | CUST-TC-030–031, 061–065          |
| RISK-030 Unauthorized API access    | CUST-TC-070–075                   |
| RISK-037 Frontend-only validation   | CUST-TC-021–025, 073, 115         |
| RISK-039 Data inconsistency         | CUST-TC-002–003, 032–033, 076–080 |
| RISK-047 IDOR                       | CUST-TC-004–007, 071, 116         |
| RISK-013 Concurrency                | CUST-TC-081–085                   |

---

# 34. Requirements Mapping

| Requirement                                       | Test Cases                |
| ------------------------------------------------- | ------------------------- |
| REQ-CUST-001 View own profile                     | CUST-TC-001–003           |
| REQ-CUST-002 No other-customer profile access     | CUST-TC-004–007           |
| REQ-CUST-003 Profile validation                   | CUST-TC-008–019           |
| REQ-CUST-004 Customer state affects functionality | CUST-TC-034–045           |
| REQ-CUST-005 Restricted customer rules            | CUST-TC-035, 039–040, 111 |
| REQ-CUST-006 Suspended restrictions               | CUST-TC-036, 041–042, 112 |
| REQ-CUST-007 Closed customer restrictions         | CUST-TC-038, 044–051, 113 |
| REQ-CUST-008 KYC controls eligibility             | CUST-TC-026–033, 114      |

---

# 35. Smoke Candidates

Recommended customer smoke coverage:

```text
CUST-TC-001
CUST-TC-004
CUST-TC-027
CUST-TC-034
CUST-TC-035
CUST-TC-038
CUST-TC-061
CUST-TC-063
```

---

# 36. Sanity Candidates

After customer/profile/KYC changes:

```text
CUST-TC-001
CUST-TC-008
CUST-TC-012
CUST-TC-020
CUST-TC-022
CUST-TC-027
CUST-TC-030
CUST-TC-035
CUST-TC-039
CUST-TC-063
CUST-TC-070
CUST-TC-078
```

---

# 37. Critical Regression Candidates

```text
CUST-TC-001
CUST-TC-004
CUST-TC-006
CUST-TC-007
CUST-TC-012
CUST-TC-020
CUST-TC-021
CUST-TC-022
CUST-TC-025
CUST-TC-027
CUST-TC-028
CUST-TC-029
CUST-TC-030
CUST-TC-031
CUST-TC-035
CUST-TC-036
CUST-TC-037
CUST-TC-038
CUST-TC-039
CUST-TC-041
CUST-TC-043
CUST-TC-044
CUST-TC-045
CUST-TC-047–050
CUST-TC-063
CUST-TC-064
CUST-TC-066
CUST-TC-070
CUST-TC-071
CUST-TC-073
CUST-TC-078
CUST-TC-081
CUST-TC-083
CUST-TC-090
CUST-TC-092
CUST-TC-111
CUST-TC-113
CUST-TC-114
CUST-TC-115
CUST-TC-116
```

---

# 38. UI Automation Candidates

Best candidates for:

```text
Playwright
Selenium
Cypress
```

Include:

```text
CUST-TC-001
CUST-TC-008–019
CUST-TC-027–030
CUST-TC-034–044
CUST-TC-052–060
CUST-TC-101–114
```

---

# 39. API Automation Candidates

Best candidates for:

```text
REST Assured
Postman
```

Include:

```text
CUST-TC-004–007
CUST-TC-020–025
CUST-TC-027–033
CUST-TC-035–051
CUST-TC-061–075
CUST-TC-081–096
CUST-TC-115
CUST-TC-116
```

---

# 40. Database Validation Candidates

Best SQL candidates:

```text
CUST-TC-003

CUST-TC-022

CUST-TC-030

CUST-TC-033

CUST-TC-039–045

CUST-TC-046–050

CUST-TC-076–080

CUST-TC-090–092

CUST-TC-113–115
```

Validate:

* Correct customer row
* Correct ownership
* Status persistence
* KYC persistence
* Protected-field immutability
* Audit records
* No duplicate/unintended rows

---

# 41. Test Evidence Requirements

For real execution, capture when relevant:

```text
Build/version

Environment

Customer ID

Role

Old state

New state

Request/response

Database record

Audit reference

Screenshot

Timestamp

Defect ID
```

Avoid exposing sensitive personal information unnecessarily.

---

# 42. Customer Release Blockers

Examples:

```text
Customer A can view Customer B profile.

Customer can modify own role.

Customer can modify own KYC status.

Customer can overwrite protected account/customer state.

Closed customer can perform financial transactions.

Restricted/suspended customer can bypass controls.

Unauthorized admin can change customer status.

Customer status change is not persisted correctly.

Critical customer-state change has no audit trail.
```

---

# 43. Exit Criteria

Customer-module testing is acceptable when:

```text
Own profile access works.

Cross-customer access is denied.

Allowed profile updates work.

Protected fields cannot be changed.

KYC restrictions work.

Customer-state restrictions work.

State transitions are valid.

Closure dependencies are enforced.

Admin roles are enforced.

UI/API/database state is consistent.

Critical admin changes are audited.

No Critical/P0 customer-security defect remains.
```

---

# 44. Final Customer Testing Principle

Testing a customer module is not only about whether:

```text
A profile can be displayed and edited.
```

The customer record also controls:

```text
Identity

Eligibility

KYC

Authorization

Account access

Product access

Operational restrictions

Lifecycle state
```

A stale or incorrectly authorized customer state can create much larger banking failures.

The core rule is:

```text
A customer must be able to control only the fields
they are allowed to control,
access only the resources they own,
and perform only the banking actions allowed by
their current authoritative customer and KYC state.
```
