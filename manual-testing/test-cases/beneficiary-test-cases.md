# Banking System — Beneficiary Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Beneficiaries                  |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Beneficiary scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Beneficiary listing
* Beneficiary ownership
* Beneficiary creation
* Destination validation
* Duplicate prevention
* Verification
* OTP lifecycle
* Activation cooldown
* Beneficiary states
* Editing
* Re-verification
* Deletion
* Search/filtering
* Transfer integration
* Destination account state changes
* Concurrency
* API validation
* Database validation
* Audit
* Notifications
* Security
* Cross-browser behavior
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Beneficiary test cases use:

```text
BEN-TC-XXX
```

Examples:

```text
BEN-TC-001
BEN-TC-002
BEN-TC-003
```

---

# 4. Common Test Data

## Customer A

```text
Customer:
CUST-001

Status:
ACTIVE

KYC:
VERIFIED
```

## Customer B

```text
Customer:
CUST-002

Status:
ACTIVE

KYC:
VERIFIED
```

## Active Beneficiary

```text
Beneficiary:
BEN-001

Owner:
CUST-001

Destination:
ACC-002

Status:
ACTIVE
```

## Pending Beneficiary

```text
Beneficiary:
BEN-002

Owner:
CUST-001

Destination:
ACC-007

Status:
PENDING_ACTIVATION
```

## Disabled Beneficiary

```text
Beneficiary:
BEN-003

Owner:
CUST-001

Status:
DISABLED
```

## Customer B Beneficiary

```text
Beneficiary:
BEN-004

Owner:
CUST-002

Status:
ACTIVE
```

## Closed Destination Beneficiary

```text
Beneficiary:
BEN-005

Owner:
CUST-001

Destination Account Status:
CLOSED
```

---

# 5. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer is authenticated.

Beneficiary service is available.

Required destination accounts exist.

Synthetic banking data is used.

OTP/verification test channel is available where required.
```

---

# 6. Beneficiary List Test Cases

## BEN-TC-001 — View Own Beneficiaries

**Priority:** P0
**Requirement:** REQ-BEN-001
**Automation:** Playwright / Selenium / API

### Steps

1. Login as `CUST-001`.
2. Open Beneficiaries.
3. Review list.

### Expected Result

Only beneficiaries belonging to `CUST-001` are displayed.

---

## BEN-TC-002 — Customer With Multiple Beneficiaries

**Priority:** P1

### Expected Result

All owned beneficiaries appear once with correct:

* Alias
* Destination
* Status
* Bank information where applicable

---

## BEN-TC-003 — Customer With No Beneficiaries

**Priority:** P2

### Expected Result

Clear empty state is displayed.

No other customer's beneficiary appears.

---

## BEN-TC-004 — Beneficiary List Matches API

**Priority:** P1

### Expected Result

UI and API return the same authorized beneficiary set.

---

# 7. Ownership / IDOR Test Cases

## BEN-TC-005 — View Another Customer's Beneficiary

**Priority:** P0
**Requirement:** REQ-BEN-002
**Risk:** RISK-047

### Preconditions

Authenticated:

```text
CUST-001
```

Target:

```text
BEN-004
Owner: CUST-002
```

### Expected Result

Access denied.

No destination or beneficiary information is exposed.

---

## BEN-TC-006 — Manipulate Beneficiary ID in URL

**Priority:** P0

### Steps

1. Open own beneficiary.
2. Replace identifier with `BEN-004`.
3. Submit.

### Expected Result

Access denied.

---

## BEN-TC-007 — Manipulate Beneficiary ID Through API

**Priority:** P0
**Automation:** REST Assured

### Expected Result

Backend independently validates ownership.

---

## BEN-TC-008 — Delete Another Customer's Beneficiary

**Priority:** P0

### Expected Result

Request denied.

Customer B's beneficiary remains unchanged.

---

## BEN-TC-009 — Edit Another Customer's Beneficiary

**Priority:** P0

### Expected Result

Request denied.

---

# 8. Add Beneficiary Test Cases

## BEN-TC-010 — Add Valid Beneficiary

**Priority:** P0
**Requirement:** REQ-BEN-001

### Steps

1. Login.
2. Open Beneficiaries.
3. Select Add Beneficiary.
4. Enter valid destination details.
5. Enter alias.
6. Submit.

### Expected Result

* Beneficiary created once.
* Ownership assigned to authenticated customer.
* Destination is correct.
* Initial state follows verification policy.

---

## BEN-TC-011 — Add Beneficiary With Empty Required Fields

**Priority:** P2

### Expected Result

Creation rejected with clear validation.

---

## BEN-TC-012 — Add Beneficiary With Invalid Destination Format

**Priority:** P1

### Expected Result

Invalid destination rejected.

---

## BEN-TC-013 — Add Nonexistent Destination

**Priority:** P0
**Risk:** RISK-017

### Expected Result

Beneficiary is not activated for an invalid nonexistent destination.

---

## BEN-TC-014 — Add Closed Destination Account

**Priority:** P0
**Risk:** RISK-018

### Expected Result

Creation or activation is rejected according to business rules.

---

## BEN-TC-015 — Add Frozen Destination Account

**Priority:** P1

### Expected Result

Behavior follows destination acceptance rules.

If frozen accounts may receive funds, beneficiary creation may be allowed.

If prohibited, request is rejected.

---

## BEN-TC-016 — Add Restricted Destination

**Priority:** P1

### Expected Result

Business rules are enforced consistently.

---

# 9. Own Account as Beneficiary Test Cases

## BEN-TC-017 — Add Own Account as External Beneficiary

**Priority:** P1

### Expected Result

Behavior follows product design.

If own-account transfers have a separate flow, duplicate beneficiary creation may be rejected.

---

## BEN-TC-018 — Add Same Source Account as Beneficiary

**Priority:** P1

### Expected Result

Invalid meaningless configuration is rejected where applicable.

---

# 10. Duplicate Beneficiary Test Cases

## BEN-TC-019 — Add Exact Duplicate Beneficiary

**Priority:** P0
**Requirement:** REQ-BEN-003

### Preconditions

`BEN-001` already exists for same destination.

### Steps

Attempt to create the same beneficiary again.

### Expected Result

Duplicate is prevented or safely identified according to product rules.

---

## BEN-TC-020 — Duplicate Destination With Different Alias

**Priority:** P1

### Expected Result

Duplicate detection uses destination identity, not only alias, if business rules prohibit duplicate destination entries.

---

## BEN-TC-021 — Duplicate Alias With Different Destination

**Priority:** P2

### Expected Result

Behavior follows alias uniqueness policy.

If duplicate aliases are allowed, destination remains clearly distinguishable.

---

## BEN-TC-022 — Duplicate Submission by Double-Click

**Priority:** P0
**Type:** Idempotency

### Expected Result

One beneficiary is created.

---

## BEN-TC-023 — Retry Creation After Timeout

**Priority:** P0

### Expected Result

Retry does not unintentionally create duplicate beneficiary records.

---

# 11. Alias Validation Test Cases

## BEN-TC-024 — Valid Alias

**Priority:** P2

### Expected Result

Alias persists correctly.

---

## BEN-TC-025 — Empty Optional Alias

**Priority:** P2

### Expected Result

Allowed if alias is optional.

System uses appropriate destination display name.

---

## BEN-TC-026 — Alias Minimum Length

**Priority:** P2
**Type:** Boundary

### Expected Result

Minimum valid value accepted.

---

## BEN-TC-027 — Alias Maximum Length

**Priority:** P2

### Expected Result

Maximum supported length accepted.

---

## BEN-TC-028 — Alias Maximum Plus One

**Priority:** P2

### Expected Result

Rejected safely.

---

## BEN-TC-029 — Unicode Alias

**Priority:** P2

### Example

```text
أحمد
José
Müller
```

### Expected Result

Supported Unicode characters persist correctly.

---

## BEN-TC-030 — Script-Like Alias

**Priority:** P1

### Example

```text
<script>alert(1)</script>
```

### Expected Result

No script execution.

Alias safely encoded/rendered.

---

# 12. Beneficiary Verification Test Cases

## BEN-TC-031 — Beneficiary Requires Verification

**Priority:** P0
**Requirement:** REQ-BEN-004

### Expected Result

New beneficiary remains unusable until required verification succeeds.

---

## BEN-TC-032 — Valid Verification OTP

**Priority:** P0

### Expected Result

OTP accepted.

Beneficiary progresses according to lifecycle.

---

## BEN-TC-033 — Invalid Verification OTP

**Priority:** P0

### Expected Result

Beneficiary remains unverified.

---

## BEN-TC-034 — Expired Verification OTP

**Priority:** P0

### Expected Result

Expired code rejected.

---

## BEN-TC-035 — Reuse Consumed OTP

**Priority:** P0

### Expected Result

Used OTP cannot be reused.

---

## BEN-TC-036 — Old OTP After Resend

**Priority:** P0

### Steps

1. Receive OTP A.
2. Request resend.
3. Receive OTP B.
4. Submit OTP A.

### Expected Result

Behavior follows OTP invalidation policy.

---

## BEN-TC-037 — New OTP After Resend

**Priority:** P0

### Expected Result

Latest valid OTP succeeds.

---

## BEN-TC-038 — Verification Attempt Limit

**Priority:** P1

### Expected Result

Repeated invalid OTP attempts trigger configured protective behavior.

---

# 13. Activation Cooldown Test Cases

## BEN-TC-039 — Newly Verified Beneficiary Enters Cooldown

**Priority:** P0
**Requirement:** REQ-BEN-005

### Expected Result

Beneficiary remains:

```text
PENDING_ACTIVATION
```

until configured cooldown completes.

---

## BEN-TC-040 — Transfer During Cooldown

**Priority:** P0

### Expected Result

Transfer to beneficiary is rejected.

No debit occurs.

---

## BEN-TC-041 — Transfer Immediately Before Cooldown Expiry

**Priority:** P0
**Type:** Boundary

### Expected Result

Still rejected.

---

## BEN-TC-042 — Transfer At/After Cooldown Expiry

**Priority:** P0

### Expected Result

Beneficiary becomes usable according to exact activation rule.

---

## BEN-TC-043 — Manipulate Client Activation Time

**Priority:** P0
**Risk:** RISK-037

### Expected Result

Backend authoritative timestamp determines eligibility.

---

# 14. Beneficiary State Test Cases

## BEN-TC-044 — ACTIVE Beneficiary

**Priority:** P0

### Expected Result

Eligible for valid transfer.

---

## BEN-TC-045 — PENDING_ACTIVATION Beneficiary

**Priority:** P0

### Expected Result

Cannot be used until activation requirements complete.

---

## BEN-TC-046 — DISABLED Beneficiary

**Priority:** P0
**Requirement:** REQ-BEN-006

### Expected Result

Cannot be used for transfer.

---

## BEN-TC-047 — DELETED Beneficiary

**Priority:** P0
**Requirement:** REQ-BEN-007

### Expected Result

No new transfer may use deleted beneficiary.

---

## BEN-TC-048 — Invalid Beneficiary State Sent Through API

**Priority:** P0

### Expected Result

Unsupported state rejected.

---

# 15. State Transition Test Cases

## BEN-TC-049 — CREATED → PENDING_VERIFICATION

**Priority:** P1

Expected: valid where defined.

---

## BEN-TC-050 — PENDING_VERIFICATION → PENDING_ACTIVATION

**Priority:** P0

Expected: occurs after successful verification.

---

## BEN-TC-051 — PENDING_ACTIVATION → ACTIVE

**Priority:** P0

Expected: occurs only after cooldown/activation conditions.

---

## BEN-TC-052 — ACTIVE → DISABLED

**Priority:** P0

Expected: authorized action only.

---

## BEN-TC-053 — DISABLED → ACTIVE

**Priority:** P1

Expected: only if supported and properly authorized.

---

## BEN-TC-054 — ACTIVE → DELETED

**Priority:** P0

Expected: deletion succeeds according to business rules.

---

## BEN-TC-055 — DELETED → ACTIVE

**Priority:** P0

Expected: rejected if deletion is terminal.

---

# 16. Edit Beneficiary Test Cases

## BEN-TC-056 — Edit Alias Only

**Priority:** P1

### Expected Result

Alias updates successfully without changing destination.

---

## BEN-TC-057 — Edit Destination Account

**Priority:** P0

### Expected Result

If destination editing is allowed, beneficiary must re-enter required verification/activation process.

If not supported, update is rejected.

---

## BEN-TC-058 — Edit Destination Bank

**Priority:** P0

### Expected Result

Security-sensitive destination change follows full validation/reverification policy.

---

## BEN-TC-059 — Invalid Destination Edit

**Priority:** P0

### Expected Result

Update rejected.

Original destination remains unchanged.

---

## BEN-TC-060 — Edit Beneficiary While Pending

**Priority:** P1

### Expected Result

Lifecycle remains valid according to business policy.

---

## BEN-TC-061 — Edit Deleted Beneficiary

**Priority:** P0

### Expected Result

Rejected.

---

# 17. Reverification Test Cases

## BEN-TC-062 — Sensitive Edit Requires Reverification

**Priority:** P0

### Expected Result

Beneficiary cannot remain immediately `ACTIVE` if sensitive destination fields changed and policy requires verification.

---

## BEN-TC-063 — Transfer Before Reverification Completes

**Priority:** P0

### Expected Result

Rejected.

---

## BEN-TC-064 — Reverification Completes Successfully

**Priority:** P0

### Expected Result

Beneficiary returns to appropriate activation lifecycle.

---

# 18. Delete Beneficiary Test Cases

## BEN-TC-065 — Delete Active Beneficiary

**Priority:** P1

### Expected Result

Beneficiary becomes unavailable for new transfers.

---

## BEN-TC-066 — Delete Pending Beneficiary

**Priority:** P1

### Expected Result

Pending verification/activation workflow terminates safely.

---

## BEN-TC-067 — Delete Already Deleted Beneficiary

**Priority:** P2

### Expected Result

No duplicate side effect.

Safe idempotent response.

---

## BEN-TC-068 — Delete Beneficiary With Historical Transactions

**Priority:** P0
**Requirement:** REQ-BEN-009

### Expected Result

Historical transactions remain intact and traceable.

---

## BEN-TC-069 — Deleted Beneficiary Appears in Old Transaction Details

**Priority:** P1

### Expected Result

Historical recipient information remains meaningful.

---

# 19. Transfer Integration Test Cases

## BEN-TC-070 — Transfer to Active Beneficiary

**Priority:** P0

### Expected Result

Transfer may proceed if all financial rules pass.

---

## BEN-TC-071 — Transfer to Pending Beneficiary

**Priority:** P0

Expected: rejected.

---

## BEN-TC-072 — Transfer to Disabled Beneficiary

**Priority:** P0

Expected: rejected.

---

## BEN-TC-073 — Transfer to Deleted Beneficiary

**Priority:** P0

Expected: rejected.

---

## BEN-TC-074 — Transfer to Another Customer's Beneficiary ID

**Priority:** P0
**Risk:** RISK-047

### Expected Result

Denied.

Customer cannot use a beneficiary they do not own.

---

## BEN-TC-075 — Direct API Transfer Using Unauthorized Beneficiary

**Priority:** P0

### Expected Result

Backend rejects ownership mismatch.

---

# 20. Destination State Change Test Cases

## BEN-TC-076 — Destination Closed After Beneficiary Activation

**Priority:** P0
**Requirement:** REQ-BEN-008
**Risk:** RISK-018

### Steps

1. Beneficiary is `ACTIVE`.
2. Destination account becomes `CLOSED`.
3. Customer attempts transfer.

### Expected Result

Transfer endpoint revalidates destination state and rejects according to business rules.

---

## BEN-TC-077 — Destination Frozen After Beneficiary Activation

**Priority:** P0

### Expected Result

Transfer follows latest destination-state rules, not stale beneficiary state.

---

## BEN-TC-078 — Destination Restricted After Beneficiary Activation

**Priority:** P0

### Expected Result

Current destination restrictions are enforced.

---

## BEN-TC-079 — Destination Reopened/Restored

**Priority:** P1

### Expected Result

Beneficiary usability follows authoritative destination state after refresh/revalidation.

---

# 21. Stale UI Test Cases

## BEN-TC-080 — Beneficiary Disabled While Transfer Page Is Open

**Priority:** P0
**Risk:** RISK-048

### Steps

1. Customer selects active beneficiary.
2. Admin/system disables beneficiary.
3. Customer submits existing transfer confirmation.

### Expected Result

Backend revalidates beneficiary state.

Transfer rejected.

---

## BEN-TC-081 — Beneficiary Deleted in Another Tab

**Priority:** P0

### Expected Result

Stale page cannot submit transaction using deleted beneficiary.

---

## BEN-TC-082 — Destination Closed While Confirmation Is Open

**Priority:** P0

### Expected Result

Final transfer submission revalidates destination.

---

# 22. Search Test Cases

## BEN-TC-083 — Search Beneficiary by Alias

**Priority:** P2

### Expected Result

Matching owned beneficiaries returned.

---

## BEN-TC-084 — Partial Alias Search

**Priority:** P2

### Expected Result

Behavior follows search requirements.

---

## BEN-TC-085 — Search by Destination Identifier

**Priority:** P2

### Expected Result

Authorized matching beneficiary returned where supported.

---

## BEN-TC-086 — Search Unknown Beneficiary

**Priority:** P3

### Expected Result

Clear empty result.

---

# 23. Filter and Sort Test Cases

## BEN-TC-087 — Filter by Active Status

**Priority:** P2

Expected: only active beneficiaries.

---

## BEN-TC-088 — Filter by Pending Status

**Priority:** P2

Expected: only pending beneficiaries.

---

## BEN-TC-089 — Sort by Alias

**Priority:** P3

Expected: correct ordering.

---

## BEN-TC-090 — Search + Status Filter

**Priority:** P2

Expected: correct intersection.

---

# 24. Pagination Test Cases

## BEN-TC-091 — Beneficiary List Pagination

**Priority:** P2

### Expected Result

No duplicates or missing records across stable pages.

---

## BEN-TC-092 — Add Beneficiary During Pagination

**Priority:** P2

### Expected Result

Pagination remains predictable according to sorting strategy.

---

# 25. API Test Cases

## BEN-TC-093 — Get Own Beneficiary

**Priority:** P0
**Automation:** REST Assured

Expected: succeeds.

---

## BEN-TC-094 — Get Other Customer Beneficiary

**Priority:** P0

Expected: denied.

---

## BEN-TC-095 — Create Valid Beneficiary API

**Priority:** P0

Expected: one beneficiary created with correct owner.

---

## BEN-TC-096 — Create Beneficiary Without Authentication

**Priority:** P0

Expected: denied.

---

## BEN-TC-097 — Create Beneficiary for Another Customer Through Payload

**Priority:** P0

### Example

```json
{
  "customerId": "CUST-002",
  "destinationAccount": "ACC-002"
}
```

### Expected Result

Authenticated identity remains authoritative.

Customer cannot assign beneficiary to another customer.

---

## BEN-TC-098 — Update Protected Owner Field

**Priority:** P0

### Expected Result

Owner cannot be reassigned by customer.

---

## BEN-TC-099 — Delete Beneficiary API

**Priority:** P1

Expected: only owner/authorized actor may delete.

---

# 26. Database Validation Test Cases

## BEN-TC-100 — Beneficiary Ownership Persistence

**Priority:** P0
**Automation:** SQL

### Expected Result

Beneficiary references correct customer.

---

## BEN-TC-101 — Destination Relationship Persistence

**Priority:** P0

### Expected Result

Stored destination is correct.

---

## BEN-TC-102 — Duplicate Beneficiary Constraint

**Priority:** P0

### Expected Result

Database/application prevents prohibited duplicate destination relationships.

---

## BEN-TC-103 — Beneficiary State Persistence

**Priority:** P0

Expected: current state matches valid lifecycle.

---

## BEN-TC-104 — Verification Timestamp Persistence

**Priority:** P1

Expected: verification metadata stored correctly where applicable.

---

## BEN-TC-105 — Activation Timestamp Persistence

**Priority:** P1

Expected: cooldown/activation timestamp consistent.

---

## BEN-TC-106 — Deleted Beneficiary Historical References

**Priority:** P0

### Expected Result

Deletion does not orphan or destroy historical transaction relationships.

---

# 27. Concurrency Test Cases

## BEN-TC-107 — Two Concurrent Beneficiary Creation Requests

**Priority:** P0
**Risk:** RISK-013

### Expected Result

No prohibited duplicate beneficiaries are created.

---

## BEN-TC-108 — Verify and Delete Concurrently

**Priority:** P0

### Expected Result

Final beneficiary state is valid and deterministic.

Deleted beneficiary must not become active afterward.

---

## BEN-TC-109 — Activate and Disable Concurrently

**Priority:** P0

### Expected Result

Final state follows concurrency rules without contradictory status.

---

## BEN-TC-110 — Edit and Transfer Concurrently

**Priority:** P0

### Expected Result

Transfer uses a valid authoritative beneficiary destination/state.

No transfer goes to an unintended old/new destination due to race.

---

# 28. Beneficiary Destination Integrity Test

## BEN-TC-111 — Destination Cannot Silently Change

**Priority:** P0
**Risk:** RISK-017

### Steps

1. Create beneficiary pointing to `ACC-002`.
2. Verify destination.
3. Perform unrelated alias edit.
4. Reopen beneficiary.
5. Initiate transfer.

### Expected Result

Destination remains `ACC-002`.

No unrelated edit changes financial destination.

---

# 29. Wrong Destination Prevention Test

## BEN-TC-112 — Alias Collision Must Not Route to Wrong Account

**Priority:** P0

### Preconditions

Two beneficiaries have similar aliases.

### Expected Result

Selection and confirmation show authoritative destination clearly enough to prevent wrong-recipient routing.

---

## BEN-TC-113 — Search Result Selection Maps to Correct Beneficiary ID

**Priority:** P0

### Expected Result

Selected UI row maps to correct backend beneficiary/destination.

---

# 30. Verification Security Test Cases

## BEN-TC-114 — Modify Beneficiary State Client-Side

**Priority:** P0
**Risk:** RISK-037

### Expected Result

Changing client state to `ACTIVE` does not bypass server verification.

---

## BEN-TC-115 — Direct Transfer Before Verification Through API

**Priority:** P0

### Expected Result

Rejected.

---

## BEN-TC-116 — Guess Beneficiary Verification Token

**Priority:** P1

### Expected Result

Invalid verification token/code rejected without exposing valid data.

---

# 31. Sensitive Data Test Cases

## BEN-TC-117 — Beneficiary Response Masks Sensitive Destination Data

**Priority:** P1
**Risk:** RISK-021

### Expected Result

Only necessary recipient information is exposed.

---

## BEN-TC-118 — Beneficiary Logs Do Not Expose Secrets

**Priority:** P1

### Expected Result

OTP/authentication secrets are not logged.

---

# 32. Audit Test Cases

## BEN-TC-119 — Beneficiary Creation Audit

**Priority:** P1

### Expected Result

Creation event recorded where required.

---

## BEN-TC-120 — Beneficiary Verification Audit

**Priority:** P1

Expected: verification event traceable without storing OTP value.

---

## BEN-TC-121 — Beneficiary Destination Change Audit

**Priority:** P0
**Risk:** RISK-022

### Expected Result

Audit contains:

```text
Actor

Beneficiary

Old Destination

New Destination

Timestamp

Verification/Reactivation State
```

---

## BEN-TC-122 — Beneficiary Deletion Audit

**Priority:** P1

Expected: deletion traceable.

---

# 33. Notification Test Cases

## BEN-TC-123 — Beneficiary Added Notification

**Priority:** P1

Expected: generated where policy requires.

---

## BEN-TC-124 — Beneficiary Activated Notification

**Priority:** P1

Expected: accurate activation state.

---

## BEN-TC-125 — Beneficiary Changed Notification

**Priority:** P1

Expected: security-sensitive change notification generated where required.

---

## BEN-TC-126 — Failed Beneficiary Update Sends No False Success

**Priority:** P1

Expected: no success message/notification for rejected change.

---

# 34. Error Handling Test Cases

## BEN-TC-127 — Beneficiary Service Failure During Creation

**Priority:** P1

### Expected Result

* No misleading success.
* No partial invalid beneficiary state.
* Retry can be performed safely.

---

## BEN-TC-128 — Verification Service Failure

**Priority:** P1

### Expected Result

Beneficiary remains in a safe unverified state.

---

## BEN-TC-129 — Timeout During Beneficiary Creation

**Priority:** P0

### Expected Result

Retry is safe and does not create duplicate beneficiary.

---

# 35. Cross-Browser Test Cases

## BEN-TC-130 — Beneficiaries in Chrome

**Priority:** P2

Expected: core flow works.

---

## BEN-TC-131 — Beneficiaries in Edge

**Priority:** P2

Expected: core flow works.

---

## BEN-TC-132 — Beneficiaries in Firefox

**Priority:** P2

Expected: core flow works.

---

## BEN-TC-133 — Beneficiaries in WebKit

**Priority:** P2

Expected: core flow works.

---

# 36. Responsive Test Cases

## BEN-TC-134 — Beneficiary List at 390×844

**Priority:** P2

### Expected Result

Beneficiary identity/status remain readable.

---

## BEN-TC-135 — Add Beneficiary at 360×800

**Priority:** P2

Expected: required inputs and confirmation remain accessible.

---

## BEN-TC-136 — Verification OTP on Mobile

**Priority:** P1

Expected: code entry and submission are usable.

---

# 37. Accessibility Test Cases

## BEN-TC-137 — Keyboard Beneficiary Navigation

**Priority:** P2

Expected: list/actions/forms accessible via keyboard.

---

## BEN-TC-138 — Beneficiary Form Labels

**Priority:** P2

Expected: inputs have clear accessible labels.

---

## BEN-TC-139 — Beneficiary Status Not Communicated by Color Alone

**Priority:** P2

Expected: status has textual/semantic representation.

---

# 38. End-to-End Beneficiary Creation Test

## BEN-TC-140 — Complete Beneficiary Activation Journey

**Priority:** P0

### Steps

1. Login as `CUST-001`.
2. Add valid beneficiary.
3. Verify ownership.
4. Complete OTP verification.
5. Confirm beneficiary enters pending activation if cooldown applies.
6. Attempt transfer before activation.
7. Wait/reach activation condition.
8. Confirm beneficiary becomes active.
9. Perform valid transfer.
10. Review transaction.

### Expected Result

```text
Create
→ Verify
→ Cooldown
→ Activate
→ Transfer
```

works according to business rules.

No early transfer is allowed.

---

# 39. End-to-End Sensitive Edit Test

## BEN-TC-141 — Change Destination and Reverify

**Priority:** P0

### Steps

1. Begin with active beneficiary.
2. Change destination.
3. Confirm beneficiary leaves immediately usable state where required.
4. Attempt transfer before reverification.
5. Complete verification.
6. Complete activation requirements.
7. Transfer.
8. Validate destination.

### Expected Result

No transfer can be sent to modified destination before required security controls complete.

---

# 40. End-to-End Deletion Test

## BEN-TC-142 — Delete Beneficiary With Historical Transactions

**Priority:** P0

### Steps

1. Use beneficiary for valid transfer.
2. Record transaction.
3. Delete beneficiary.
4. Attempt new transfer.
5. Open old transaction history.
6. Generate statement.

### Expected Result

* New transfer cannot use deleted beneficiary.
* Old transaction remains traceable.
* Statement/history remain intact.

---

# 41. Destination State E2E Test

## BEN-TC-143 — Active Beneficiary Destination Becomes Closed

**Priority:** P0

### Steps

1. Verify beneficiary is active.
2. Close destination account.
3. Open transfer.
4. Submit transfer.
5. Review source balance/history.

### Expected Result

* Transfer rejected.
* Source balance unchanged.
* No completed debit.
* No false success notification.

---

# 42. Concurrency End-to-End Test

## BEN-TC-144 — Beneficiary Update vs Transfer Race

**Priority:** P0

### Steps

1. Customer opens transfer using `BEN-001`.
2. In another session, sensitive beneficiary destination is changed/disabled.
3. Submit first transfer.
4. Inspect final destination and state.

### Expected Result

Transfer either:

* Uses a still-valid authoritative beneficiary snapshot according to documented transaction design, or
* Is rejected due to changed beneficiary state.

It must never silently route to an unintended destination.

---

# 43. Beneficiary Risk Mapping

| Risk                                | Related Test Cases               |
| ----------------------------------- | -------------------------------- |
| RISK-002 Unauthorized customer data | BEN-TC-005–009, 094              |
| RISK-013 Concurrency corruption     | BEN-TC-107–110, 144              |
| RISK-017 Wrong beneficiary          | BEN-TC-010–023, 070–082, 111–113 |
| RISK-018 Closed destination         | BEN-TC-014, 076, 143             |
| RISK-019 History inconsistency      | BEN-TC-068–069, 106, 142         |
| RISK-021 Sensitive exposure         | BEN-TC-117–118                   |
| RISK-022 Audit gap                  | BEN-TC-119–122                   |
| RISK-030 Unauthorized API           | BEN-TC-093–099                   |
| RISK-037 Frontend-only validation   | BEN-TC-043, 097–098, 114–115     |
| RISK-039 State/data inconsistency   | BEN-TC-100–106                   |
| RISK-042 Retry duplication          | BEN-TC-023, 129                  |
| RISK-047 IDOR                       | BEN-TC-005–009, 074–075, 094     |
| RISK-048 UI/backend mismatch        | BEN-TC-080–082                   |

---

# 44. Requirements Mapping

| Requirement                                   | Test Cases                   |
| --------------------------------------------- | ---------------------------- |
| REQ-BEN-001 Add valid beneficiary             | BEN-TC-010–018               |
| REQ-BEN-002 Beneficiary ownership             | BEN-TC-005–009, 074–075      |
| REQ-BEN-003 Duplicate prevention              | BEN-TC-019–023               |
| REQ-BEN-004 Verification required             | BEN-TC-031–038               |
| REQ-BEN-005 Activation cooldown               | BEN-TC-039–043               |
| REQ-BEN-006 Disabled beneficiary unusable     | BEN-TC-046, 072              |
| REQ-BEN-007 Deleted beneficiary unusable      | BEN-TC-047, 065–069, 073     |
| REQ-BEN-008 Invalid/closed destination        | BEN-TC-013–016, 076–079, 143 |
| REQ-BEN-009 Historical transactions preserved | BEN-TC-068–069, 106, 142     |

---

# 45. Smoke Candidates

Recommended beneficiary smoke coverage:

```text
BEN-TC-001
BEN-TC-005
BEN-TC-010
BEN-TC-019
BEN-TC-031
BEN-TC-040
BEN-TC-044
BEN-TC-046
BEN-TC-070
BEN-TC-076
```

---

# 46. Sanity Candidates

After beneficiary changes:

```text
BEN-TC-010
BEN-TC-019
BEN-TC-022
BEN-TC-031
BEN-TC-032
BEN-TC-034
BEN-TC-039
BEN-TC-040
BEN-TC-042
BEN-TC-056
BEN-TC-062
BEN-TC-065
BEN-TC-070
BEN-TC-072
BEN-TC-076
```

---

# 47. Critical Regression Candidates

```text
BEN-TC-001
BEN-TC-005–010
BEN-TC-013–023
BEN-TC-031–043
BEN-TC-044–055
BEN-TC-057–079
BEN-TC-080–082
BEN-TC-093–110
BEN-TC-111–115
BEN-TC-119–129
BEN-TC-140–144
```

---

# 48. UI Automation Candidates

Best candidates for:

```text
Playwright

Selenium

Cypress
```

Include:

```text
BEN-TC-001–004
BEN-TC-010–043
BEN-TC-044–090
BEN-TC-123–142
```

---

# 49. API Automation Candidates

Best candidates for:

```text
REST Assured

Postman
```

Include:

```text
BEN-TC-005–023
BEN-TC-031–082
BEN-TC-093–129
BEN-TC-141–144
```

---

# 50. SQL / Database Testing Candidates

Strong SQL candidates:

```text
BEN-TC-019–023
BEN-TC-031–043
BEN-TC-049–069
BEN-TC-100–110
BEN-TC-119–122
BEN-TC-140–144
```

Database validation should verify:

* Correct owner
* Correct destination
* Duplicate prevention
* Verification state
* Activation timestamps
* State transitions
* Deletion state
* Historical transaction relationships
* Audit events

---

# 51. Performance / Concurrency Candidates

Later concurrency/performance testing can include:

```text
BEN-TC-022
BEN-TC-023
BEN-TC-107
BEN-TC-108
BEN-TC-109
BEN-TC-110
BEN-TC-129
BEN-TC-144
```

The primary concern is consistency and duplicate prevention rather than raw throughput.

---

# 52. Test Evidence Requirements

For critical beneficiary tests, capture:

```text
Customer ID

Beneficiary ID

Destination account

Initial state

Final state

Verification timestamp

Activation timestamp

Transaction reference

Request/response

Database record

Audit record

Screenshot

Defect ID
```

Never include real OTPs or sensitive production account details.

---

# 53. Beneficiary Defect Examples

Potential defects include:

```text
Customer can access another customer's beneficiary.

Duplicate beneficiary created.

Invalid destination accepted.

Old OTP remains valid unexpectedly.

Beneficiary usable before activation cooldown expires.

Disabled beneficiary still transfers money.

Deleted beneficiary still transfers money.

Destination change does not trigger reverification.

Historical transactions disappear after beneficiary deletion.

Closed destination still receives transfer.

Stale transfer page uses disabled beneficiary.

Alias/search maps to wrong destination.

Concurrent update routes money incorrectly.
```

---

# 54. Beneficiary Release Blockers

Release-blocking issues include:

```text
Wrong recipient receives transfer.

Customer can use another customer's beneficiary.

Unverified beneficiary can receive transfer.

Cooldown can be bypassed.

Deleted/disabled beneficiary can still be used.

Destination changes without required reverification.

Closed invalid destination accepts transfer.

Beneficiary race causes transfer to unintended destination.

Beneficiary ownership corruption.
```

---

# 55. Beneficiary Exit Criteria

Beneficiary testing is acceptable when:

```text
Customers see only their own beneficiaries.

Valid beneficiaries can be created.

Invalid destinations are rejected.

Duplicates are prevented.

Verification works.

OTP lifecycle works.

Activation cooldown is enforced.

Disabled/deleted beneficiaries cannot be used.

Sensitive edits trigger required reverification.

Destination-state changes are revalidated.

Historical transfers remain intact after deletion.

API and database states agree.

Concurrency does not cause wrong-recipient routing.

No Critical/P0 beneficiary defect remains.
```

---

# 56. Final Beneficiary Testing Principle

A beneficiary is not merely:

```text
A saved name in an address book.
```

It represents an authorized destination for customer money.

Therefore QA must establish that:

```text
The beneficiary belongs to the correct customer.

The destination is correct.

The destination has been validated.

Required verification was completed.

Activation rules were satisfied.

The beneficiary is currently usable.

The destination has not become invalid.

A stale client cannot bypass current state.
```

The most important beneficiary invariant is:

```text
The destination confirmed by the customer
must be the destination that receives the money.
```

The core rule is:

```text
No transfer should ever be routed using a beneficiary
whose ownership, verification, state, or destination
is no longer valid at the time the financial operation is authorized.
```
