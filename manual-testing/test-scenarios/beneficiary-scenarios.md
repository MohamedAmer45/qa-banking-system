# Banking System — Beneficiary Management Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Beneficiary Management         |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for beneficiary management within the Banking System.

Beneficiary management is a high-risk banking area because incorrect beneficiary handling may result in:

* Transfers to the wrong recipient
* Unauthorized access to saved beneficiaries
* Duplicate beneficiaries
* Transfers to inactive or invalid accounts
* Bypassing beneficiary verification
* Using removed beneficiaries
* Bypassing activation or cooldown periods
* Customer data leakage

The scenarios cover beneficiary creation, validation, activation, modification, deletion, ownership, security, and transfer integration.

---

# 3. Scope

Beneficiary-management testing includes:

* View beneficiaries
* Add beneficiary
* Validate beneficiary account
* Duplicate prevention
* Beneficiary ownership
* Beneficiary activation
* Beneficiary verification
* Cooldown periods
* Beneficiary status
* Edit beneficiary
* Delete beneficiary
* Beneficiary search
* Beneficiary selection
* Account validation
* Customer authorization
* Transfer integration
* Error handling
* Audit logging
* Notifications
* Concurrent updates

---

# 4. Scenario Naming Convention

Beneficiary scenarios use:

```text
TS-BEN-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Beneficiary Viewing Scenarios

## TS-BEN-001 — Customer views own beneficiary list

**Priority:** P1

Expected:

* Correct beneficiaries are displayed.
* Only beneficiaries belonging to the authenticated customer are visible.

---

## TS-BEN-002 — Customer with no beneficiaries views beneficiary page

**Priority:** P2

Expected:

Appropriate empty state is displayed.

---

## TS-BEN-003 — Customer with one beneficiary views beneficiary list

**Priority:** P2

Expected:

Correct beneficiary appears once.

---

## TS-BEN-004 — Customer with multiple beneficiaries views complete list

**Priority:** P1

Expected:

All owned beneficiaries are displayed without duplicates.

---

## TS-BEN-005 — Refresh beneficiary list

**Priority:** P2

Expected:

Latest persisted state is displayed.

---

## TS-BEN-006 — Open beneficiary list in multiple browser tabs

**Priority:** P2

Expected:

All tabs eventually reflect the same valid beneficiary state.

---

# 6. Beneficiary Ownership and Authorization

## TS-BEN-007 — Customer attempts to view another customer's beneficiary

**Priority:** P0

Expected:

Access denied.

---

## TS-BEN-008 — Modify beneficiary ID in URL

**Priority:** P0

Example:

```text
/beneficiaries/BEN-001
```

changed to:

```text
/beneficiaries/BEN-002
```

where `BEN-002` belongs to another customer.

Expected:

Access denied.

---

## TS-BEN-009 — Modify beneficiary ID in API request

**Priority:** P0

Expected:

Backend authorization rejects access.

---

## TS-BEN-010 — Customer attempts to delete another customer's beneficiary

**Priority:** P0

Expected:

Rejected.

---

## TS-BEN-011 — Customer attempts to edit another customer's beneficiary

**Priority:** P0

Expected:

Rejected.

---

## TS-BEN-012 — Unauthenticated user attempts beneficiary access

**Priority:** P0

Expected:

Authentication required.

---

## TS-BEN-013 — Expired session attempts beneficiary operation

**Priority:** P0

Expected:

Operation rejected or reauthentication required.

---

## TS-BEN-014 — Customer logs out and revisits beneficiary URL

**Priority:** P0

Expected:

No beneficiary data is accessible.

---

# 7. Add Beneficiary Scenarios

## TS-BEN-015 — Add valid beneficiary with valid account information

**Priority:** P0

Expected:

* Beneficiary is created.
* Correct owner is assigned.
* Appropriate initial status is applied.

---

## TS-BEN-016 — Add beneficiary using valid account number

**Priority:** P0

Expected:

Destination account is validated successfully.

---

## TS-BEN-017 — Add beneficiary using nonexistent account number

**Priority:** P0

Expected:

Beneficiary creation is rejected.

---

## TS-BEN-018 — Add beneficiary using malformed account number

**Priority:** P1

Expected:

Validation error displayed.

---

## TS-BEN-019 — Add beneficiary with account number left empty

**Priority:** P1

Expected:

Required-field validation.

---

## TS-BEN-020 — Add beneficiary with name left empty

**Priority:** P2

Expected:

Rejected if beneficiary name is required.

---

## TS-BEN-021 — Add beneficiary using own account

**Priority:** P1

Expected:

Behavior follows business rules.

If own accounts must use a separate own-transfer flow, beneficiary creation should be rejected.

---

## TS-BEN-022 — Add beneficiary whose destination account is closed

**Priority:** P0

Expected:

Rejected where closed accounts cannot become beneficiaries.

---

## TS-BEN-023 — Add beneficiary whose destination account is frozen

**Priority:** P1

Expected:

Behavior follows beneficiary/account business rules.

---

## TS-BEN-024 — Add beneficiary whose destination account is restricted

**Priority:** P1

Expected:

Behavior follows restrictions.

---

## TS-BEN-025 — Submit beneficiary creation twice rapidly

**Priority:** P0

Expected:

Only one beneficiary is created.

---

## TS-BEN-026 — Refresh browser immediately after beneficiary creation

**Priority:** P1

Expected:

No duplicate beneficiary is created.

---

## TS-BEN-027 — Browser Back after beneficiary creation

**Priority:** P2

Expected:

Beneficiary is not accidentally created again.

---

# 8. Duplicate Beneficiary Scenarios

## TS-BEN-028 — Add same destination account twice

**Priority:** P0

Expected:

Duplicate beneficiary is rejected where duplicates are not allowed.

---

## TS-BEN-029 — Add same account with different beneficiary alias

**Priority:** P0

Expected:

Alias difference must not bypass duplicate-account rules.

---

## TS-BEN-030 — Add same account with spaces around input

**Priority:** P1

Expected:

Whitespace normalization must not bypass duplicate validation.

---

## TS-BEN-031 — Add same account using different case where identifiers are case-insensitive

**Priority:** P1

Expected:

Duplicate detection remains effective.

---

## TS-BEN-032 — Add beneficiary twice from separate browser tabs

**Priority:** P0

Expected:

Only one valid beneficiary record should exist if duplicates are prohibited.

---

## TS-BEN-033 — Duplicate beneficiary creation through simultaneous API requests

**Priority:** P0

Expected:

Backend prevents duplicate records.

---

# 9. Beneficiary Name/Alias Scenarios

## TS-BEN-034 — Create beneficiary with valid alias

**Priority:** P2

Expected:

Alias saved successfully.

---

## TS-BEN-035 — Beneficiary alias at minimum length

**Priority:** P2

Expected:

Accepted.

---

## TS-BEN-036 — Beneficiary alias below minimum length

**Priority:** P2

Expected:

Rejected if a minimum exists.

---

## TS-BEN-037 — Beneficiary alias at maximum length

**Priority:** P2

Expected:

Accepted.

---

## TS-BEN-038 — Beneficiary alias above maximum length

**Priority:** P2

Expected:

Rejected safely.

---

## TS-BEN-039 — Beneficiary alias with Arabic characters

**Priority:** P2

Expected:

Stored and displayed correctly.

---

## TS-BEN-040 — Beneficiary alias with accented characters

**Priority:** P2

Expected:

Unicode handled correctly.

---

## TS-BEN-041 — Beneficiary alias containing script-like input

**Priority:** P1

Example:

```text
<script>alert(1)</script>
```

Expected:

Safely escaped and never executed.

---

## TS-BEN-042 — Beneficiary alias containing only spaces

**Priority:** P2

Expected:

Rejected or normalized according to rules.

---

# 10. Beneficiary Account Validation Scenarios

## TS-BEN-043 — Validate active destination account

**Priority:** P0

Expected:

Account is accepted.

---

## TS-BEN-044 — Validate nonexistent destination account

**Priority:** P0

Expected:

Rejected.

---

## TS-BEN-045 — Validate closed destination account

**Priority:** P0

Expected:

Rejected or marked unusable according to requirements.

---

## TS-BEN-046 — Validate suspended destination account

**Priority:** P0

Expected:

Behavior follows account-state rules.

---

## TS-BEN-047 — Destination account becomes closed after beneficiary creation

**Priority:** P0

Expected:

Transfer-time validation prevents invalid transfer.

---

## TS-BEN-048 — Destination account becomes frozen after beneficiary creation

**Priority:** P0

Expected:

Transfer behavior follows current destination-account rules, not stale beneficiary state.

---

## TS-BEN-049 — Destination account number changed or invalidated

**Priority:** P0

Expected:

Beneficiary cannot silently point to an unintended account.

---

# 11. Beneficiary Verification Scenarios

Where beneficiary verification is required.

## TS-BEN-050 — Add beneficiary requiring verification

**Priority:** P0

Expected:

Beneficiary enters pending verification/activation state.

---

## TS-BEN-051 — Verify beneficiary with valid OTP

**Priority:** P0

Expected:

Beneficiary becomes verified.

---

## TS-BEN-052 — Verify beneficiary with invalid OTP

**Priority:** P0

Expected:

Verification rejected.

---

## TS-BEN-053 — Verify beneficiary with expired OTP

**Priority:** P0

Expected:

Verification rejected.

---

## TS-BEN-054 — Reuse beneficiary verification OTP

**Priority:** P0

Expected:

Reused OTP rejected.

---

## TS-BEN-055 — Request new beneficiary verification OTP

**Priority:** P1

Expected:

Latest valid OTP follows system policy.

---

## TS-BEN-056 — Enter OTP below required length

**Priority:** P2

Expected:

Rejected.

---

## TS-BEN-057 — Enter OTP above required length

**Priority:** P2

Expected:

Rejected.

---

## TS-BEN-058 — Leave verification OTP empty

**Priority:** P1

Expected:

Required validation.

---

## TS-BEN-059 — Refresh verification page

**Priority:** P1

Expected:

Verification state remains secure and consistent.

---

## TS-BEN-060 — Navigate directly to transfer before beneficiary verification completes

**Priority:** P0

Expected:

Unverified beneficiary cannot be used where verification is mandatory.

---

# 12. Beneficiary Activation and Cooldown Scenarios

Where newly added beneficiaries require activation or cooling-off period.

## TS-BEN-061 — Newly created beneficiary enters PENDING_ACTIVATION

**Priority:** P0

Expected:

Status is correct.

---

## TS-BEN-062 — Attempt transfer before cooldown expires

**Priority:** P0

Expected:

Transfer blocked.

---

## TS-BEN-063 — Attempt transfer one second before activation time

**Priority:** P1

Expected:

Blocked if activation time has not yet been reached.

---

## TS-BEN-064 — Attempt transfer exactly at activation boundary

**Priority:** P1

Expected:

Behavior follows exact business rule.

---

## TS-BEN-065 — Transfer immediately after activation period completes

**Priority:** P0

Expected:

Beneficiary can be used.

---

## TS-BEN-066 — Beneficiary activation timestamp displayed correctly

**Priority:** P2

Expected:

Customer can understand when beneficiary becomes usable.

---

## TS-BEN-067 — Timezone handling for beneficiary activation

**Priority:** P1

Expected:

Activation occurs at correct absolute time.

---

## TS-BEN-068 — Manual status manipulation attempt bypasses cooldown

**Priority:** P0

Expected:

Backend rejects unauthorized activation.

---

# 13. Beneficiary Status Scenarios

Possible states:

```text
PENDING
PENDING_VERIFICATION
PENDING_ACTIVATION
ACTIVE
DISABLED
DELETED
```

Only states supported by the implementation should remain.

---

## TS-BEN-069 — ACTIVE beneficiary can be selected for transfer

**Priority:** P0

Expected:

Available for permitted transactions.

---

## TS-BEN-070 — PENDING beneficiary cannot be used

**Priority:** P0

Expected:

Blocked.

---

## TS-BEN-071 — DISABLED beneficiary cannot be used

**Priority:** P0

Expected:

Transfer blocked.

---

## TS-BEN-072 — DELETED beneficiary cannot be used

**Priority:** P0

Expected:

No new transfer may reference deleted beneficiary.

---

## TS-BEN-073 — Beneficiary status updates correctly after verification

**Priority:** P1

Expected:

State transition persists.

---

## TS-BEN-074 — Beneficiary status consistent across UI and API

**Priority:** P1

Expected:

Same state displayed.

---

## TS-BEN-075 — Beneficiary status consistent across API and database

**Priority:** P1

Expected:

Persisted state matches API result.

---

# 14. Beneficiary State Transition Scenarios

Example lifecycle:

```text
CREATED
   ↓
PENDING_VERIFICATION
   ↓
PENDING_ACTIVATION
   ↓
ACTIVE
   ↓
DISABLED
```

Deletion may transition from certain states:

```text
ACTIVE
   ↓
DELETED
```

---

## TS-BEN-076 — PENDING_VERIFICATION → PENDING_ACTIVATION

**Priority:** P1

Expected:

Valid after successful verification.

---

## TS-BEN-077 — PENDING_ACTIVATION → ACTIVE

**Priority:** P0

Expected:

Occurs only when activation conditions are met.

---

## TS-BEN-078 — ACTIVE → DISABLED

**Priority:** P1

Expected:

Valid authorized transition.

---

## TS-BEN-079 — DISABLED → ACTIVE

**Priority:** P1

Expected:

Allowed only according to business rules.

---

## TS-BEN-080 — ACTIVE → DELETED

**Priority:** P1

Expected:

Beneficiary becomes unavailable for new transactions.

---

## TS-BEN-081 — DELETED → ACTIVE

**Priority:** P1

Expected:

Rejected unless restoration is explicitly supported.

---

## TS-BEN-082 — Invalid state transition through API

**Priority:** P0

Expected:

Backend rejects unsupported transition.

---

# 15. Edit Beneficiary Scenarios

## TS-BEN-083 — Edit beneficiary alias

**Priority:** P2

Expected:

Alias updates successfully.

---

## TS-BEN-084 — Edit beneficiary account number

**Priority:** P0

Expected:

Behavior follows business rules.

Safer expected behavior is often to require creating a new beneficiary rather than silently changing the destination account.

---

## TS-BEN-085 — Edit beneficiary to another valid account

**Priority:** P0

If supported:

Expected:

New account must be fully revalidated and reverified.

---

## TS-BEN-086 — Edit beneficiary to invalid account

**Priority:** P0

Expected:

Rejected.

---

## TS-BEN-087 — Edit beneficiary currently pending activation

**Priority:** P1

Expected:

Activation/verification state is recalculated appropriately.

---

## TS-BEN-088 — Edit beneficiary and cancel

**Priority:** P2

Expected:

Original values remain unchanged.

---

## TS-BEN-089 — Save beneficiary edit twice rapidly

**Priority:** P1

Expected:

Consistent final state without duplicate records.

---

# 16. Delete Beneficiary Scenarios

## TS-BEN-090 — Delete active beneficiary

**Priority:** P1

Expected:

Beneficiary is removed or marked deleted according to design.

---

## TS-BEN-091 — Cancel beneficiary deletion confirmation

**Priority:** P2

Expected:

Beneficiary remains unchanged.

---

## TS-BEN-092 — Delete pending beneficiary

**Priority:** P2

Expected:

Removed successfully where allowed.

---

## TS-BEN-093 — Delete disabled beneficiary

**Priority:** P2

Expected:

Handled according to business rules.

---

## TS-BEN-094 — Delete beneficiary twice

**Priority:** P1

Expected:

Second request does not cause server error or inconsistent state.

---

## TS-BEN-095 — Deleted beneficiary disappears from active list

**Priority:** P1

Expected:

No longer selectable.

---

## TS-BEN-096 — Deleted beneficiary historical transfers remain visible

**Priority:** P0

Expected:

Historical transaction records remain intact.

---

## TS-BEN-097 — Deleted beneficiary cannot be reused through old transfer page

**Priority:** P0

Expected:

Server-side validation rejects transfer.

---

# 17. Deletion During Transfer Scenarios

## TS-BEN-098 — Beneficiary deleted before transfer form submission

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-BEN-099 — Beneficiary deleted after transfer page loads but before confirmation

**Priority:** P0

Expected:

Final submission validates current beneficiary state.

---

## TS-BEN-100 — Beneficiary disabled during transfer confirmation

**Priority:** P0

Expected:

Transaction follows latest valid state rules.

---

## TS-BEN-101 — Beneficiary account closes during transfer flow

**Priority:** P0

Expected:

Transfer rejected if destination is no longer valid.

---

# 18. Search and Filtering Scenarios

## TS-BEN-102 — Search beneficiary by alias

**Priority:** P2

Expected:

Correct matching beneficiaries displayed.

---

## TS-BEN-103 — Search beneficiary by account identifier where supported

**Priority:** P2

Expected:

Correct authorized match returned.

---

## TS-BEN-104 — Search using partial beneficiary name

**Priority:** P2

Expected:

Relevant results.

---

## TS-BEN-105 — Search nonexistent beneficiary

**Priority:** P2

Expected:

Empty state.

---

## TS-BEN-106 — Search with special characters

**Priority:** P1

Expected:

Handled safely.

---

## TS-BEN-107 — Filter beneficiaries by status

**Priority:** P2

Expected:

Only matching status displayed.

---

# 19. Beneficiary Selection in Transfer Flow

## TS-BEN-108 — Select active beneficiary for transfer

**Priority:** P0

Expected:

Correct destination beneficiary selected.

---

## TS-BEN-109 — Beneficiary selection displays enough information to distinguish recipients

**Priority:** P0

Expected:

User can identify intended beneficiary clearly.

Useful information may include:

* Alias
* Beneficiary name
* Masked account number
* Bank information where applicable

---

## TS-BEN-110 — Select beneficiary with similar name to another beneficiary

**Priority:** P0

Expected:

UI provides sufficient distinction to prevent user error.

---

## TS-BEN-111 — Transfer confirmation displays selected beneficiary accurately

**Priority:** P0

Expected:

Confirmation shows the exact beneficiary that will receive funds.

---

## TS-BEN-112 — Change beneficiary before transfer confirmation

**Priority:** P1

Expected:

Confirmation reflects new selected beneficiary only.

---

## TS-BEN-113 — Browser Back changes beneficiary selection

**Priority:** P1

Expected:

No stale beneficiary is submitted unexpectedly.

---

# 20. Wrong-Destination Prevention Scenarios

## TS-BEN-114 — Verify backend uses beneficiary's current mapped account

**Priority:** P0

Expected:

Transfer cannot silently target an unrelated account.

---

## TS-BEN-115 — Modify destination account in client request after selecting beneficiary

**Priority:** P0

Expected:

Backend validates beneficiary ownership and destination consistency.

---

## TS-BEN-116 — Modify beneficiary ID and destination account to inconsistent pair

**Priority:** P0

Expected:

Request rejected.

---

## TS-BEN-117 — Use another customer's beneficiary ID during transfer

**Priority:** P0

Expected:

Authorization failure.

---

# 21. Limit Scenarios

If beneficiary-specific limits exist.

## TS-BEN-118 — Transfer below beneficiary limit

**Priority:** P1

Expected:

Allowed.

---

## TS-BEN-119 — Transfer exactly at beneficiary limit

**Priority:** P0

Expected:

Handled according to inclusive limit rules.

---

## TS-BEN-120 — Transfer above beneficiary limit

**Priority:** P0

Expected:

Rejected.

---

## TS-BEN-121 — Daily beneficiary limit reached through multiple transfers

**Priority:** P0

Expected:

Further transfers are blocked.

---

## TS-BEN-122 — Beneficiary limit updated while transfer is open

**Priority:** P0

Expected:

Final submission uses current limit.

---

# 22. Concurrency Scenarios

## TS-BEN-123 — Beneficiary created simultaneously in two browser tabs

**Priority:** P0

Expected:

Duplicate protection remains effective.

---

## TS-BEN-124 — Beneficiary deleted in one tab while edited in another

**Priority:** P1

Expected:

Deleted state cannot be unintentionally overwritten.

---

## TS-BEN-125 — Beneficiary edited simultaneously in two sessions

**Priority:** P1

Expected:

Conflict handled predictably.

---

## TS-BEN-126 — Beneficiary activated while another session still shows pending state

**Priority:** P1

Expected:

Final transaction request evaluates current server-side state.

---

## TS-BEN-127 — Admin disables beneficiary-related destination account during transfer

**Priority:** P0

Expected:

Transaction does not bypass updated account state.

---

# 23. API Validation Scenarios

## TS-BEN-128 — Get own beneficiary list through API

**Priority:** P1

Expected:

Only owned beneficiaries returned.

---

## TS-BEN-129 — Get another customer's beneficiary through API

**Priority:** P0

Expected:

Authorization failure.

---

## TS-BEN-130 — Create beneficiary through API with valid data

**Priority:** P1

Expected:

Valid response and persisted record.

---

## TS-BEN-131 — Create beneficiary with missing required field through API

**Priority:** P1

Expected:

Validation error.

---

## TS-BEN-132 — Create duplicate beneficiary through API

**Priority:** P0

Expected:

Rejected according to duplicate rules.

---

## TS-BEN-133 — Delete beneficiary through unauthorized API request

**Priority:** P0

Expected:

Denied.

---

## TS-BEN-134 — Manipulate owner/customer ID in beneficiary API payload

**Priority:** P0

Expected:

Customer cannot assign beneficiary to another customer or manipulate ownership.

---

# 24. Data Consistency Scenarios

## TS-BEN-135 — Newly created beneficiary appears in UI and API

**Priority:** P1

Expected:

Consistent representation.

---

## TS-BEN-136 — Beneficiary record matches database state

**Priority:** P1

Expected:

Owner, account mapping, status, and alias are correct.

---

## TS-BEN-137 — Failed beneficiary creation creates no partial record

**Priority:** P0

Expected:

No orphan or invalid beneficiary exists.

---

## TS-BEN-138 — Deleted beneficiary state matches UI, API, and database

**Priority:** P1

Expected:

Consistent state.

---

## TS-BEN-139 — Beneficiary ownership stored correctly

**Priority:** P0

Expected:

Correct customer relationship.

---

## TS-BEN-140 — Beneficiary destination account mapping stored correctly

**Priority:** P0

Expected:

Correct destination account.

---

# 25. Transaction History Integration Scenarios

## TS-BEN-141 — Transfer through beneficiary records correct beneficiary information

**Priority:** P0

Expected:

Transaction history identifies correct destination.

---

## TS-BEN-142 — Historical transfer remains understandable after beneficiary alias changes

**Priority:** P1

Expected:

Transaction history remains accurate.

---

## TS-BEN-143 — Historical transfer remains intact after beneficiary deletion

**Priority:** P0

Expected:

Deleting saved beneficiary does not delete financial history.

---

## TS-BEN-144 — Transaction reference maps to correct beneficiary/destination

**Priority:** P0

Expected:

Auditability remains intact.

---

# 26. Error Handling Scenarios

## TS-BEN-145 — Beneficiary service unavailable while loading list

**Priority:** P1

Expected:

Safe error displayed.

---

## TS-BEN-146 — Server error during beneficiary creation

**Priority:** P1

Expected:

No false success.

---

## TS-BEN-147 — Network disconnect during beneficiary creation

**Priority:** P1

Expected:

Final state can be determined safely after reconnect.

---

## TS-BEN-148 — Network disconnect during beneficiary verification

**Priority:** P1

Expected:

Verification does not enter inconsistent state.

---

## TS-BEN-149 — Slow beneficiary creation response

**Priority:** P1

Expected:

Processing feedback shown and duplicate submission prevented.

---

## TS-BEN-150 — Destination-account validation service unavailable

**Priority:** P0

Expected:

Beneficiary must not be treated as valid without required validation.

---

# 27. Security Scenarios

## TS-BEN-151 — Customer attempts to modify beneficiary owner ID

**Priority:** P0

Expected:

Rejected.

---

## TS-BEN-152 — Customer attempts IDOR on beneficiary resource

**Priority:** P0

Expected:

Another customer's beneficiary remains inaccessible.

---

## TS-BEN-153 — Beneficiary API response does not expose unnecessary sensitive destination data

**Priority:** P1

Expected:

Only required account information is returned.

---

## TS-BEN-154 — Beneficiary account number masked where required

**Priority:** P1

Expected:

Appropriate masking.

---

## TS-BEN-155 — SQL-like input in beneficiary alias

**Priority:** P1

Example:

```text
' OR '1'='1
```

Expected:

Handled as data, not executable query logic.

---

## TS-BEN-156 — Script-like input in alias

**Priority:** P1

Expected:

No script execution.

---

# 28. Audit Scenarios

## TS-BEN-157 — Beneficiary creation generates audit record

**Priority:** P1

Expected:

Audit includes:

* Customer
* Beneficiary
* Action
* Timestamp
* Result

---

## TS-BEN-158 — Beneficiary activation generates audit record

**Priority:** P1

Expected:

State change traceable.

---

## TS-BEN-159 — Beneficiary edit generates audit record

**Priority:** P2

Expected:

Changes traceable according to policy.

---

## TS-BEN-160 — Beneficiary deletion generates audit record

**Priority:** P1

Expected:

Deletion traceable.

---

## TS-BEN-161 — Failed unauthorized beneficiary access recorded where required

**Priority:** P2

Expected:

Relevant security event traceable.

---

# 29. Notification Scenarios

## TS-BEN-162 — Beneficiary creation sends notification

**Priority:** P1

Where required.

Expected:

Correct customer notified.

---

## TS-BEN-163 — Beneficiary activation sends notification

**Priority:** P1

Expected:

Notification reflects actual ACTIVE state.

---

## TS-BEN-164 — Beneficiary deletion sends notification

**Priority:** P2

Expected:

Correct customer receives message.

---

## TS-BEN-165 — Failed creation does not send success notification

**Priority:** P1

Expected:

No misleading notification.

---

## TS-BEN-166 — Unauthorized beneficiary change does not generate customer-facing success message

**Priority:** P1

Expected:

No false success.

---

# 30. Accessibility and Usability Scenarios

## TS-BEN-167 — Beneficiary list clearly distinguishes saved recipients

**Priority:** P1

Expected:

Customer can identify intended recipient.

---

## TS-BEN-168 — Account number is sufficiently masked but still distinguishable

**Priority:** P1

Expected:

Last digits or equivalent identifier allow safe differentiation.

---

## TS-BEN-169 — Add-beneficiary form supports keyboard navigation

**Priority:** P2

Expected:

Logical focus order.

---

## TS-BEN-170 — Beneficiary validation errors are clear

**Priority:** P2

Expected:

User understands why creation failed.

---

## TS-BEN-171 — Beneficiary activation/cooldown state clearly communicated

**Priority:** P1

Expected:

Customer understands why transfer is unavailable.

---

## TS-BEN-172 — Delete beneficiary requires clear confirmation

**Priority:** P1

Expected:

Accidental deletion is reduced.

---

# 31. Responsive Scenarios

## TS-BEN-173 — Beneficiary list on desktop

**Priority:** P2

Expected:

Readable and usable.

---

## TS-BEN-174 — Beneficiary list on tablet

**Priority:** P2

Expected:

Controls remain accessible.

---

## TS-BEN-175 — Beneficiary list on mobile

**Priority:** P1

Expected:

Recipient identity remains clear.

---

## TS-BEN-176 — Add beneficiary on mobile

**Priority:** P2

Expected:

All fields and validation messages accessible.

---

## TS-BEN-177 — Transfer beneficiary selector on mobile

**Priority:** P0

Expected:

Customer can safely distinguish intended recipient.

---

# 32. Cross-Browser Scenarios

## TS-BEN-178 — Beneficiary flow in Chrome

**Priority:** P1

Expected:

Works correctly.

---

## TS-BEN-179 — Beneficiary flow in Edge

**Priority:** P1

Expected:

Works correctly.

---

## TS-BEN-180 — Beneficiary flow in Firefox

**Priority:** P1

Expected:

Works correctly.

---

## TS-BEN-181 — Verification and activation flows across supported browsers

**Priority:** P1

Expected:

Consistent behavior.

---

# 33. Boundary Scenarios

## TS-BEN-182 — Alias minimum minus one

**Priority:** P2

Expected:

Rejected if minimum exists.

---

## TS-BEN-183 — Alias exactly at minimum

**Priority:** P2

Expected:

Accepted.

---

## TS-BEN-184 — Alias exactly at maximum

**Priority:** P2

Expected:

Accepted.

---

## TS-BEN-185 — Alias maximum plus one

**Priority:** P2

Expected:

Rejected.

---

## TS-BEN-186 — Account identifier minimum valid format

**Priority:** P1

Expected:

Accepted.

---

## TS-BEN-187 — Account identifier one character below required length

**Priority:** P1

Expected:

Rejected.

---

## TS-BEN-188 — Account identifier above supported length

**Priority:** P1

Expected:

Rejected safely.

---

## TS-BEN-189 — Maximum allowed number of beneficiaries minus one

**Priority:** P1

Expected:

Another beneficiary may be added.

---

## TS-BEN-190 — Exactly maximum number of beneficiaries

**Priority:** P1

Expected:

List remains valid.

---

## TS-BEN-191 — Attempt to exceed maximum beneficiary count

**Priority:** P1

Expected:

Rejected according to limit.

---

# 34. End-to-End Beneficiary Scenarios

## TS-BEN-192 — Add beneficiary and perform first successful transfer

**Priority:** P0

Flow:

```text
Login
→ Add Beneficiary
→ Validate Destination Account
→ Verify Beneficiary
→ Wait/Complete Activation
→ Select Beneficiary
→ Transfer Funds
→ Confirm Recipient
→ Verify Success
→ Verify Transaction History
```

---

## TS-BEN-193 — Add duplicate beneficiary

**Priority:** P0

Flow:

```text
Existing Beneficiary
→ Add Same Destination Account
→ Submit
→ Duplicate Rejected
→ Verify Only One Beneficiary Exists
```

---

## TS-BEN-194 — Delete beneficiary and attempt transfer using stale page

**Priority:** P0

Flow:

```text
Open Transfer Page
→ Select Beneficiary
→ Delete Beneficiary in Another Tab
→ Return to Transfer
→ Submit
→ Transfer Rejected
```

---

## TS-BEN-195 — Beneficiary destination account closes before transfer

**Priority:** P0

Flow:

```text
Active Beneficiary
→ Open Transfer
→ Destination Account Closed
→ Submit Transfer
→ Rejected
→ Source Balance Unchanged
```

---

## TS-BEN-196 — Beneficiary activation after cooldown

**Priority:** P0

Flow:

```text
Create Beneficiary
→ Verify
→ Beneficiary Pending Activation
→ Attempt Early Transfer
→ Rejected
→ Activation Period Ends
→ Retry Transfer
→ Allowed
```

---

# 35. Critical Smoke Scenarios

Beneficiary smoke coverage should include:

```text
TS-BEN-001 — View beneficiary list
TS-BEN-007 — Cannot access another customer's beneficiary
TS-BEN-015 — Add valid beneficiary
TS-BEN-028 — Duplicate beneficiary rejected
TS-BEN-069 — Active beneficiary selectable
TS-BEN-108 — Select active beneficiary for transfer
TS-BEN-111 — Correct beneficiary shown on confirmation
```

---

# 36. Critical Regression Scenarios

Always prioritize:

* Beneficiary ownership
* Add valid beneficiary
* Invalid account rejection
* Duplicate prevention
* Verification
* Cooldown/activation
* Status enforcement
* Deletion
* Deleted beneficiary cannot transfer
* Destination account-state validation
* Transfer confirmation
* ID manipulation
* Concurrency
* UI/API/database consistency

---

# 37. Automation Candidates

Strong UI automation candidates:

* View beneficiary list
* Add beneficiary
* Required-field validation
* Duplicate beneficiary
* Beneficiary verification
* Activation status
* Edit alias
* Delete beneficiary
* Select beneficiary during transfer
* Deleted beneficiary validation

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 38. API Automation Candidates

Postman and REST Assured should later cover:

* Get beneficiaries
* Create beneficiary
* Duplicate beneficiary
* Invalid account
* Unauthorized beneficiary access
* Verify beneficiary
* Activate beneficiary
* Update beneficiary
* Delete beneficiary
* Invalid state transition
* Transfer using invalid/deleted beneficiary

---

# 39. SQL Validation Candidates

Database testing should validate:

* Beneficiary owner
* Destination account mapping
* Alias
* Beneficiary status
* Verification status
* Activation timestamp
* Uniqueness rules
* Deleted status
* Audit records

---

# 40. Performance Testing Candidates

Potential JMeter coverage:

* Beneficiary-list retrieval under load
* Beneficiary creation under moderate concurrency
* Concurrent duplicate-beneficiary attempts
* Beneficiary lookup during high-volume transfer traffic

Performance testing must preserve ownership and consistency under concurrency.

---

# 41. BDD Candidates

Example:

```gherkin
Feature: Beneficiary activation

Scenario: Customer cannot transfer to a beneficiary before activation
  Given the customer has added a new beneficiary
  And the beneficiary has been verified
  And the activation period has not completed
  When the customer attempts to transfer money to the beneficiary
  Then the transfer should be rejected
  And the source account balance should remain unchanged
```

---

# 42. Risk Traceability

Major related risks include:

```text
RISK-002 — Unauthorized customer data access
RISK-003 — Duplicate financial transaction
RISK-017 — Incorrect beneficiary used in transfer
RISK-018 — Closed destination account accepts funds incorrectly
RISK-030 — API authorization failure
RISK-037 — Frontend-only validation
RISK-047 — Insecure direct object access
RISK-048 — UI reports false success
```

---

# 43. Beneficiary Coverage Summary

This catalog covers:

* Viewing
* Ownership
* Authorization
* Creation
* Account validation
* Duplicate prevention
* Alias validation
* Verification
* OTP
* Activation
* Cooldown
* Statuses
* State transitions
* Editing
* Deletion
* Transfer integration
* Wrong-recipient prevention
* Limits
* Concurrency
* API behavior
* Database consistency
* Error handling
* Security
* Audit
* Notifications
* Accessibility
* Responsive behavior
* Cross-browser behavior
* Boundary testing
* E2E workflows

---

# 44. Final Beneficiary Testing Principle

Beneficiary testing must ensure that the recipient of a financial transaction is always correctly identified, authorized, valid, and current.

The most important questions are:

```text
Does this beneficiary belong to the authenticated customer?

Is the destination account valid?

Can duplicate beneficiaries be created?

Can an unverified beneficiary be used?

Can cooldown rules be bypassed?

Can a deleted or disabled beneficiary still receive a transfer?

Can another customer's beneficiary ID be used?

Does the transfer confirmation clearly identify the recipient?

Does the backend validate the beneficiary again at submission time?

Do historical transactions remain accurate after beneficiary changes?
```

A beneficiary is directly connected to where money is sent, so beneficiary validation must be treated as a high-risk part of the transfer process.
