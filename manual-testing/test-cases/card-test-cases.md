# Banking System — Card Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Cards                          |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Card scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Card listing
* Card ownership
* Card issuance
* Activation
* Freeze/unfreeze
* Permanent blocking
* Replacement
* Expiry
* Card limits
* Purchase authorization
* Holds
* Settlement
* Failed transactions
* Refunds
* Reversals
* Duplicate processing
* Stale card state
* Sensitive card data
* API validation
* Database validation
* Notifications
* Audit
* Concurrency
* Cross-browser behavior
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Card test cases use:

```text
CARD-TC-XXX
```

Examples:

```text
CARD-TC-001
CARD-TC-002
CARD-TC-003
```

---

# 4. Critical Card Invariants

## Invariant 1 — Ownership

A customer may only view and manage cards they are authorized to access.

---

## Invariant 2 — Card State

Authoritative backend state determines whether the card may be used.

```text
ACTIVE
FROZEN
BLOCKED
EXPIRED
REPLACED
```

must be enforced independently of stale UI state.

---

## Invariant 3 — Financial Integrity

A card transaction must affect the linked account exactly according to its final state.

---

## Invariant 4 — Hold Integrity

Authorization holds must:

```text
Reduce available balance correctly.

Settle correctly.

Release correctly after decline/expiry/reversal.
```

---

## Invariant 5 — Sensitive Data

The application must not unnecessarily expose:

```text
Full PAN

CVV

PIN

Authentication secrets
```

---

# 5. Common Test Data

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
```

## Active Card

```text
Card:
CARD-001

Owner:
CUST-001

Linked Account:
ACC-001

Status:
ACTIVE

Currency:
EGP
```

## Frozen Card

```text
Card:
CARD-002

Owner:
CUST-001

Status:
FROZEN
```

## Blocked Card

```text
Card:
CARD-003

Owner:
CUST-001

Status:
BLOCKED
```

## Expired Card

```text
Card:
CARD-004

Owner:
CUST-001

Status:
EXPIRED
```

## Customer B Card

```text
Card:
CARD-005

Owner:
CUST-002

Status:
ACTIVE
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer is authenticated.

Card service is available.

Linked account is available.

Synthetic card data is used.

API/DB validation is available when required.
```

---

# 7. Card List Test Cases

## CARD-TC-001 — View Own Cards

**Priority:** P0
**Requirement:** REQ-CARD-001
**Automation:** Playwright / API

### Steps

1. Login as `CUST-001`.
2. Open Cards.

### Expected Result

Only cards owned by `CUST-001` are displayed.

---

## CARD-TC-002 — Multiple Cards

**Priority:** P1

### Expected Result

Each owned card appears once with correct:

* Masked number
* Type
* Status
* Expiry
* Linked account

---

## CARD-TC-003 — Customer With No Cards

**Priority:** P2

### Expected Result

Clear empty state.

No other customer's card appears.

---

# 8. Card Ownership / IDOR

## CARD-TC-004 — View Another Customer's Card

**Priority:** P0
**Risk:** RISK-002, RISK-047

### Expected Result

Access denied.

No protected card details returned.

---

## CARD-TC-005 — Modify Card ID in URL

**Priority:** P0

Expected: unauthorized card inaccessible.

---

## CARD-TC-006 — Modify Card ID in API

**Priority:** P0
**Automation:** REST Assured

Expected: backend ownership validation rejects request.

---

## CARD-TC-007 — Attempt State Change on Another Customer's Card

**Priority:** P0

Expected: denied.

---

# 9. Card Issuance Test Cases

## CARD-TC-008 — Issue Valid Card

**Priority:** P1
**Requirement:** REQ-CARD-002

### Expected Result

* One card created.
* Correct customer/account ownership.
* Initial state follows business rules.
* Card identifier unique.

---

## CARD-TC-009 — Ineligible Customer Requests Card

**Priority:** P0

Expected: issuance rejected according to KYC/account/product rules.

---

## CARD-TC-010 — Duplicate Issuance Request

**Priority:** P0

Expected: duplicate unintended card not created.

---

## CARD-TC-011 — Card Linked to Unauthorized Account

**Priority:** P0

Expected: rejected.

---

# 10. Activation Test Cases

## CARD-TC-012 — Activate Eligible Card

**Priority:** P0
**Requirement:** REQ-CARD-003

### Expected Result

```text
INACTIVE
→ ACTIVE
```

with successful verification.

---

## CARD-TC-013 — Invalid Activation Verification

**Priority:** P0

Expected: activation denied.

---

## CARD-TC-014 — Activate Already Active Card

**Priority:** P2

Expected: safe idempotent result.

---

## CARD-TC-015 — Activate Blocked Card

**Priority:** P0

Expected: rejected.

---

## CARD-TC-016 — Activate Expired Card

**Priority:** P0

Expected: rejected.

---

# 11. Freeze Test Cases

## CARD-TC-017 — Freeze Active Card

**Priority:** P0
**Requirement:** REQ-CARD-004

### Expected Result

```text
ACTIVE
→ FROZEN
```

and new prohibited transactions are rejected.

---

## CARD-TC-018 — Freeze Already Frozen Card

**Priority:** P2

Expected: safe idempotent behavior.

---

## CARD-TC-019 — Freeze Blocked Card

**Priority:** P1

Expected: blocked state remains authoritative.

---

## CARD-TC-020 — Unauthorized Freeze API

**Priority:** P0

Expected: denied.

---

# 12. Unfreeze Test Cases

## CARD-TC-021 — Unfreeze Frozen Card

**Priority:** P0
**Requirement:** REQ-CARD-005

### Expected Result

```text
FROZEN
→ ACTIVE
```

when card is eligible.

---

## CARD-TC-022 — Unfreeze Blocked Card

**Priority:** P0

Expected: rejected.

---

## CARD-TC-023 — Unfreeze Expired Card

**Priority:** P0

Expected: rejected.

---

## CARD-TC-024 — Unfreeze Replaced Card

**Priority:** P0

Expected: old replaced card remains unusable.

---

# 13. Permanent Block Test Cases

## CARD-TC-025 — Permanently Block Active Card

**Priority:** P0
**Requirement:** REQ-CARD-006

### Expected Result

```text
ACTIVE
→ BLOCKED
```

Card can no longer authorize purchases.

---

## CARD-TC-026 — Block Frozen Card

**Priority:** P0

Expected:

```text
FROZEN
→ BLOCKED
```

if allowed.

---

## CARD-TC-027 — Unblock Permanently Blocked Card

**Priority:** P0

Expected: rejected if block is terminal.

---

## CARD-TC-028 — Block Card Requires Confirmation

**Priority:** P1

Expected: irreversible consequence clearly communicated.

---

# 14. Card Replacement Test Cases

## CARD-TC-029 — Replace Lost/Stolen Card

**Priority:** P0
**Requirement:** REQ-CARD-007

### Expected Result

* Old card becomes unusable.
* New replacement card created.
* Ownership/account relationship preserved.
* Replacement relationship traceable.

---

## CARD-TC-030 — Old Card After Replacement

**Priority:** P0

Expected: old card transactions rejected.

---

## CARD-TC-031 — Replacement Card Activation

**Priority:** P0

Expected: replacement follows required activation lifecycle.

---

## CARD-TC-032 — Duplicate Replacement Request

**Priority:** P0

Expected: no unintended multiple active replacements.

---

# 15. Expiry Test Cases

## CARD-TC-033 — Active Card Before Expiry

**Priority:** P1

Expected: valid transactions permitted.

---

## CARD-TC-034 — Transaction on Expiry Boundary

**Priority:** P0

Expected: behavior follows documented expiry rule exactly.

---

## CARD-TC-035 — Transaction After Expiry

**Priority:** P0
**Requirement:** REQ-CARD-008

Expected: rejected.

---

## CARD-TC-036 — Expired Card UI State

**Priority:** P1

Expected: clearly shown as expired and unusable.

---

# 16. Card Limit Test Cases

## CARD-TC-037 — Purchase Below Card Limit

**Priority:** P1

Expected: may succeed.

---

## CARD-TC-038 — Purchase Exactly at Limit

**Priority:** P0
**Type:** Boundary

Expected: follows configured inclusive boundary.

---

## CARD-TC-039 — Purchase Above Limit

**Priority:** P0
**Risk:** RISK-008

Expected: declined.

---

## CARD-TC-040 — Change Valid Card Limit

**Priority:** P1
**Requirement:** REQ-CARD-009

Expected: valid limit updated.

---

## CARD-TC-041 — Set Card Limit Above Allowed Maximum

**Priority:** P0

Expected: rejected.

---

## CARD-TC-042 — Manipulate Client-Side Limit

**Priority:** P0
**Risk:** RISK-037

Expected: backend authoritative limit enforced.

---

# 17. Purchase Authorization Test Cases

## CARD-TC-043 — Valid Purchase

**Priority:** P0
**Requirement:** REQ-CARD-010

### Expected Result

* Authorization approved.
* Hold/debit behavior correct.
* Reference generated.
* Available balance updated.

---

## CARD-TC-044 — Purchase With Insufficient Available Balance

**Priority:** P0

Expected: declined.

No invalid settled debit.

---

## CARD-TC-045 — Purchase Using Frozen Card

**Priority:** P0
**Risk:** RISK-024

Expected: declined.

---

## CARD-TC-046 — Purchase Using Blocked Card

**Priority:** P0
**Risk:** RISK-025

Expected: declined.

---

## CARD-TC-047 — Purchase Using Expired Card

**Priority:** P0

Expected: declined.

---

## CARD-TC-048 — Purchase Above Card Limit

**Priority:** P0

Expected: declined.

---

# 18. Stale State Test Cases

## CARD-TC-049 — Card Frozen After Purchase Page Loaded

**Priority:** P0
**Risk:** RISK-048

### Expected Result

Backend current state rejects transaction.

---

## CARD-TC-050 — Card Blocked After Confirmation Loaded

**Priority:** P0

Expected: stale client cannot bypass blocked state.

---

## CARD-TC-051 — Card Replaced While Old Session Remains Open

**Priority:** P0

Expected: old card cannot authorize transaction.

---

# 19. Hold Test Cases

## CARD-TC-052 — Approved Authorization Creates Hold

**Priority:** P0
**Requirement:** REQ-CARD-011

### Example

```text
Current Balance:
10,000.00

Authorization:
2,000.00
```

Expected available balance:

```text
8,000.00
```

while current balance follows ledger design.

---

## CARD-TC-053 — Declined Purchase Creates No Hold

**Priority:** P0

Expected: available balance unchanged.

---

## CARD-TC-054 — Hold Settles Correctly

**Priority:** P0

Expected: settlement converts hold into correct finalized financial transaction.

---

## CARD-TC-055 — Expired Hold Releases

**Priority:** P0

Expected: available balance restored.

---

## CARD-TC-056 — Duplicate Hold Prevention

**Priority:** P0

Expected: retry/duplicate authorization does not create unintended repeated hold.

---

# 20. Settlement Test Cases

## CARD-TC-057 — Authorization and Settlement Match

**Priority:** P0

Expected: settled amount corresponds correctly to authorized transaction.

---

## CARD-TC-058 — Settlement Less Than Authorization

**Priority:** P1

Expected: excess hold released appropriately.

---

## CARD-TC-059 — Settlement Greater Than Authorization

**Priority:** P0

Expected: handled only according to supported rules and limits; otherwise flagged/rejected.

---

## CARD-TC-060 — Duplicate Settlement

**Priority:** P0

Expected: duplicate financial effect prevented.

---

# 21. Failed Card Transaction Neutrality

## CARD-TC-061 — Declined Transaction Financially Neutral

**Priority:** P0

Expected:

```text
Permanent Debit:
0

Invalid Hold:
0
```

---

## CARD-TC-062 — Authorization Error Releases Hold

**Priority:** P0

Expected: no indefinite invalid hold remains.

---

# 22. Refund Test Cases

## CARD-TC-063 — Full Card Refund

**Priority:** P0
**Requirement:** REQ-CARD-012

Expected:

* Original purchase preserved.
* Refund recorded separately.
* Correct amount credited.

---

## CARD-TC-064 — Partial Refund

**Priority:** P1

Expected: correct partial credit where supported.

---

## CARD-TC-065 — Refund Exceeds Purchase

**Priority:** P0

Expected: rejected unless explicitly supported.

---

## CARD-TC-066 — Duplicate Refund

**Priority:** P0

Expected: duplicate credit prevented.

---

## CARD-TC-067 — Refund to Blocked/Replaced Card

**Priority:** P0

Expected: refund reaches correct linked account according to card-network/business rules without reactivating card.

---

# 23. Reversal Test Cases

## CARD-TC-068 — Authorization Reversal

**Priority:** P0

Expected: hold released correctly.

---

## CARD-TC-069 — Duplicate Reversal

**Priority:** P0

Expected: no duplicate financial credit/release.

---

# 24. Concurrent Card Transaction Test Cases

## CARD-TC-070 — Two Purchases Within Available Balance

**Priority:** P0
**Risk:** RISK-013

Expected: both may succeed if balance and limits permit.

---

## CARD-TC-071 — Concurrent Purchases Exceed Available Balance

**Priority:** P0

### Example

```text
Available:
1,000.00

Purchase A:
700.00

Purchase B:
500.00
```

Expected: both must not succeed if overdraft unavailable.

---

## CARD-TC-072 — Concurrent Purchases Exceed Card Daily Limit

**Priority:** P0

Expected: aggregate limit enforced atomically.

---

## CARD-TC-073 — Freeze Concurrent With Purchase

**Priority:** P0

Expected: deterministic valid result according to authorization timing rules.

---

## CARD-TC-074 — Block Concurrent With Purchase

**Priority:** P0

Expected: no contradictory final state.

---

# 25. Duplicate Authorization Test Cases

## CARD-TC-075 — Duplicate Merchant Authorization Request

**Priority:** P0
**Risk:** RISK-003

Expected: network/idempotency strategy prevents unintended duplicate financial effect.

---

## CARD-TC-076 — Retry After Authorization Timeout

**Priority:** P0
**Risk:** RISK-042

Expected: retry does not duplicate hold or charge.

---

# 26. Card Transaction History

## CARD-TC-077 — Completed Purchase Appears in History

**Priority:** P0

Expected: correct amount, merchant, status, reference.

---

## CARD-TC-078 — Declined Purchase Not Shown as Completed

**Priority:** P0

Expected: decline remains distinguishable.

---

## CARD-TC-079 — Refund Linked to Original Purchase

**Priority:** P0

Expected: traceable relationship.

---

# 27. Statement Integration

## CARD-TC-080 — Settled Purchase Appears in Statement

**Priority:** P0

Expected: correct settled amount.

---

## CARD-TC-081 — Authorization Hold Not Misrepresented as Settled Debit

**Priority:** P0

Expected: statement follows authoritative settlement rules.

---

## CARD-TC-082 — Refund Appears in Statement

**Priority:** P0

Expected: correct credit/refund.

---

# 28. Sensitive Card Data Test Cases

## CARD-TC-083 — PAN Masking

**Priority:** P0
**Risk:** RISK-021

Expected: full card number not unnecessarily displayed.

---

## CARD-TC-084 — CVV Not Exposed After Issuance/Display Policy

**Priority:** P0

Expected: CVV unavailable except through explicitly permitted secure flow.

---

## CARD-TC-085 — PIN Not Exposed

**Priority:** P0

Expected: plaintext PIN never returned.

---

## CARD-TC-086 — Card API Does Not Return Unnecessary Sensitive Fields

**Priority:** P0

Expected: response minimized according to authorization.

---

## CARD-TC-087 — Logs Do Not Contain PAN/CVV/PIN

**Priority:** P0

Expected: sensitive values absent or properly masked.

---

# 29. API Card Test Cases

## CARD-TC-088 — Get Own Card API

**Priority:** P0
**Automation:** REST Assured

Expected: permitted masked data returned.

---

## CARD-TC-089 — Get Another Customer's Card

**Priority:** P0

Expected: denied.

---

## CARD-TC-090 — Freeze Own Card API

**Priority:** P0

Expected: allowed where customer self-freeze is supported.

---

## CARD-TC-091 — Block Another Customer's Card API

**Priority:** P0

Expected: denied.

---

## CARD-TC-092 — Manipulate Card Status in Payload

**Priority:** P0

Expected: unauthorized state changes rejected.

---

## CARD-TC-093 — Manipulate Card Limit Above Maximum

**Priority:** P0

Expected: backend rejects.

---

# 30. Database Validation Test Cases

## CARD-TC-094 — Card Ownership Relationship

**Priority:** P0
**Automation:** SQL

Expected: card references correct customer and linked account.

---

## CARD-TC-095 — Unique Card Identifier

**Priority:** P0

Expected: no duplicate card identifier.

---

## CARD-TC-096 — Card State Persistence

**Priority:** P0

Expected: state matches latest valid transition.

---

## CARD-TC-097 — Replacement Relationship

**Priority:** P0

Expected: old/new card relationship traceable.

---

## CARD-TC-098 — Hold Persistence

**Priority:** P0

Expected: active hold amount/state correct.

---

## CARD-TC-099 — Settlement Persistence

**Priority:** P0

Expected: finalized transaction stored once.

---

## CARD-TC-100 — Refund Relationship Persistence

**Priority:** P0

Expected: refund references correct original card transaction.

---

# 31. UI/API/Database Consistency

## CARD-TC-101 — Card Status Cross-Layer Validation

**Priority:** P0
**Risk:** RISK-039, RISK-048

Expected:

```text
UI Status
=
API Status
=
DB Status
```

---

## CARD-TC-102 — Card Limit Cross-Layer Validation

**Priority:** P0

Expected: displayed and enforced limit matches backend state.

---

## CARD-TC-103 — Card Transaction Cross-Layer Validation

**Priority:** P0

Expected: transaction state/amount/reference reconcile across layers.

---

# 32. Audit Test Cases

## CARD-TC-104 — Freeze Audit

**Priority:** P1

Expected:

```text
Actor
Card
Old State
New State
Timestamp
```

---

## CARD-TC-105 — Permanent Block Audit

**Priority:** P0
**Risk:** RISK-022

Expected: security-sensitive block action traceable.

---

## CARD-TC-106 — Limit Change Audit

**Priority:** P1

Expected: old/new limit and actor recorded where required.

---

## CARD-TC-107 — Sensitive Card Data Absent From Audit

**Priority:** P0

Expected: no CVV/PIN/full PAN.

---

# 33. Notification Test Cases

## CARD-TC-108 — Card Freeze Notification

**Priority:** P1

Expected: accurate state notification.

---

## CARD-TC-109 — Card Unfreeze Notification

**Priority:** P1

Expected: accurate notification.

---

## CARD-TC-110 — Card Block Notification

**Priority:** P0

Expected: customer notified where required.

---

## CARD-TC-111 — Card Replacement Notification

**Priority:** P1

Expected: correct replacement event.

---

## CARD-TC-112 — Purchase Notification Matches Transaction

**Priority:** P1

Expected: amount/merchant/status correct.

---

## CARD-TC-113 — Declined Purchase Does Not Generate False Success

**Priority:** P0

Expected: no misleading success notification.

---

# 34. Error Handling

## CARD-TC-114 — Card Service Unavailable

**Priority:** P1

Expected: safe error; stale state must not authorize financial action.

---

## CARD-TC-115 — Freeze Request Times Out

**Priority:** P0

Expected: system retrieves authoritative final state before encouraging unsafe retry.

---

## CARD-TC-116 — Purchase Authorization Service Failure

**Priority:** P0

Expected: fail safely according to business/network policy without unintended debit.

---

# 35. Security Input Tests

## CARD-TC-117 — Script-Like Card Alias

**Priority:** P1

Expected: no script execution.

---

## CARD-TC-118 — Unexpected Protected Fields

**Priority:** P0

Payload example:

```json
{
  "status": "ACTIVE",
  "dailyLimit": 999999999,
  "ownerId": "CUST-002"
}
```

Expected: protected values ignored/rejected.

---

# 36. Cross-Browser Tests

## CARD-TC-119 — Cards in Chrome

**Priority:** P2

Expected: core card-management flows work.

---

## CARD-TC-120 — Cards in Edge

**Priority:** P2

Expected: works.

---

## CARD-TC-121 — Cards in Firefox

**Priority:** P2

Expected: works.

---

## CARD-TC-122 — Cards in WebKit

**Priority:** P2

Expected: works.

---

# 37. Responsive Tests

## CARD-TC-123 — Card Screen at 390×844

**Priority:** P1

Expected:

* Card status visible.
* Masking correct.
* Freeze/block controls reachable.
* No sensitive value exposed.

---

## CARD-TC-124 — Card Screen at 360×800

**Priority:** P1

Expected: critical actions remain usable.

---

## CARD-TC-125 — Limit Change on Mobile

**Priority:** P1

Expected: current/new limits clearly visible.

---

# 38. Accessibility Tests

## CARD-TC-126 — Keyboard Card Management

**Priority:** P2

Expected: card actions keyboard accessible.

---

## CARD-TC-127 — Card Status Not Communicated by Color Alone

**Priority:** P2

Expected: text/semantic state provided.

---

## CARD-TC-128 — Freeze/Block Warnings Accessible

**Priority:** P1

Expected: irreversible/security consequences perceivable.

---

# 39. End-to-End Freeze Journey

## CARD-TC-129 — Active → Freeze → Declined Purchase → Unfreeze

**Priority:** P0

### Steps

1. Confirm `CARD-001` is `ACTIVE`.
2. Perform valid purchase.
3. Freeze card.
4. Attempt purchase.
5. Verify decline.
6. Unfreeze card.
7. Attempt valid purchase again.

### Expected Result

```text
ACTIVE purchase:
Allowed

FROZEN purchase:
Declined

ACTIVE after unfreeze:
Allowed
```

with correct financial history.

---

# 40. End-to-End Block Journey

## CARD-TC-130 — Active → Permanent Block → Purchase Attempt

**Priority:** P0

### Expected Result

Blocked card remains unusable across UI/API/transaction authorization.

---

# 41. End-to-End Replacement Journey

## CARD-TC-131 — Lost Card Replacement

**Priority:** P0

### Steps

1. Start with active old card.
2. Request replacement.
3. Verify old card becomes unusable.
4. Verify replacement created.
5. Activate replacement.
6. Perform valid transaction.
7. Inspect history/audit.

### Expected Result

Only replacement card becomes valid for new transactions.

---

# 42. End-to-End Purchase and Settlement

## CARD-TC-132 — Authorization → Hold → Settlement

**Priority:** P0

### Test Data

```text
Opening Current Balance:
10,000.00

Opening Available:
10,000.00

Purchase:
2,000.00
```

### Expected Flow

```text
Authorization approved
↓
Available Balance = 8,000.00
↓
Settlement completes
↓
Current Balance = 8,000.00
↓
Hold removed
```

No duplicate financial effect.

---

# 43. End-to-End Failed Purchase

## CARD-TC-133 — Decline Remains Financially Neutral

**Priority:** P0

Expected:

```text
Purchase Status:
DECLINED

Permanent Debit:
0

Invalid Persistent Hold:
0

Success Notification:
No
```

---

# 44. End-to-End Refund

## CARD-TC-134 — Purchase → Full Refund

**Priority:** P0

Expected:

* Original purchase preserved.
* Refund stored separately.
* Correct amount credited.
* Statement/history reconcile.

---

# 45. End-to-End Stale Freeze Test

## CARD-TC-135 — Stale ACTIVE UI After Card Freeze

**Priority:** P0

### Steps

1. Open card/payment context while active.
2. Freeze card in separate session.
3. Submit stale purchase.
4. Validate authorization result.

### Expected Result

Backend `FROZEN` state wins.

Purchase declined.

---

# 46. End-to-End Concurrent Spending Test

## CARD-TC-136 — Concurrent Purchases Exceed Available Balance

**Priority:** P0

### Test Data

```text
Available:
1,000.00

Purchase A:
700.00

Purchase B:
500.00
```

### Expected Result

Both cannot successfully settle if overdraft is unsupported.

No invalid negative balance.

---

# 47. End-to-End Daily Limit Race

## CARD-TC-137 — Concurrent Card Limit Enforcement

**Priority:** P0

### Example

```text
Remaining Daily Card Limit:
1,000.00

Purchase A:
700.00

Purchase B:
600.00
```

### Expected Result

Combined successful authorizations/settlements cannot violate authoritative daily limit.

---

# 48. Risk Mapping

| Risk                                | Related Test Cases                  |
| ----------------------------------- | ----------------------------------- |
| RISK-001 Incorrect balance          | CARD-TC-043–081, 098–103, 132–137   |
| RISK-002 Unauthorized customer data | CARD-TC-004–007, 089–091            |
| RISK-003 Duplicate transaction      | CARD-TC-056, 060, 066, 069, 075–076 |
| RISK-008 Limit bypass               | CARD-TC-037–042, 072, 093, 137      |
| RISK-013 Concurrency                | CARD-TC-070–076, 136–137            |
| RISK-021 Sensitive exposure         | CARD-TC-083–087, 107                |
| RISK-022 Audit gap                  | CARD-TC-104–107                     |
| RISK-024 Frozen card usable         | CARD-TC-017–024, 045, 049, 129, 135 |
| RISK-025 Blocked card usable        | CARD-TC-025–032, 046, 050, 130–131  |
| RISK-030 Unauthorized API           | CARD-TC-088–093                     |
| RISK-037 Frontend-only validation   | CARD-TC-042, 049–051, 092–093, 118  |
| RISK-039 API/DB inconsistency       | CARD-TC-094–103                     |
| RISK-042 Retry duplication          | CARD-TC-076, 115                    |
| RISK-047 IDOR                       | CARD-TC-004–007, 089–091            |
| RISK-048 UI/backend mismatch        | CARD-TC-049–051, 101–103, 135       |

---

# 49. Requirements Mapping

| Requirement                   | Test Cases               |
| ----------------------------- | ------------------------ |
| REQ-CARD-001 View own cards   | CARD-TC-001–007          |
| REQ-CARD-002 Card issuance    | CARD-TC-008–011          |
| REQ-CARD-003 Activation       | CARD-TC-012–016          |
| REQ-CARD-004 Freeze           | CARD-TC-017–020          |
| REQ-CARD-005 Unfreeze         | CARD-TC-021–024          |
| REQ-CARD-006 Permanent block  | CARD-TC-025–028          |
| REQ-CARD-007 Replacement      | CARD-TC-029–032, 131     |
| REQ-CARD-008 Expiry           | CARD-TC-033–036          |
| REQ-CARD-009 Limits           | CARD-TC-037–042, 137     |
| REQ-CARD-010 Purchases        | CARD-TC-043–051          |
| REQ-CARD-011 Holds/settlement | CARD-TC-052–062, 132–133 |
| REQ-CARD-012 Refunds          | CARD-TC-063–069, 134     |

---

# 50. Smoke Candidates

Recommended card smoke coverage:

```text
CARD-TC-001
CARD-TC-004
CARD-TC-012
CARD-TC-017
CARD-TC-021
CARD-TC-025
CARD-TC-030
CARD-TC-043
CARD-TC-045
CARD-TC-046
CARD-TC-052
CARD-TC-063
```

---

# 51. Sanity Candidates

After card changes:

```text
CARD-TC-001
CARD-TC-012
CARD-TC-017
CARD-TC-021
CARD-TC-025
CARD-TC-029
CARD-TC-035
CARD-TC-038
CARD-TC-043
CARD-TC-045
CARD-TC-052
CARD-TC-054
CARD-TC-063
CARD-TC-096
CARD-TC-101
```

---

# 52. Critical Regression Candidates

```text
CARD-TC-001–016

CARD-TC-017–076

CARD-TC-077–118

CARD-TC-123–137
```

---

# 53. UI Automation Candidates

Best suited for:

```text
Playwright

Selenium

Cypress
```

Strong candidates:

```text
CARD-TC-001–051

CARD-TC-077–082

CARD-TC-108–135
```

---

# 54. API Automation Candidates

Best suited for:

```text
REST Assured

Postman
```

Strong candidates:

```text
CARD-TC-004–076

CARD-TC-083–118

CARD-TC-129–137
```

---

# 55. SQL / Database Testing Candidates

Strong SQL candidates:

```text
CARD-TC-008–032

CARD-TC-052–081

CARD-TC-094–107

CARD-TC-131–137
```

Database validation should verify:

```text
Card ownership

Linked account

Card state

Replacement relationship

Authorization hold

Settlement

Refund

Unique identifiers

Limits

Audit records
```

---

# 56. Performance / JMeter Candidates

Strong concurrency/load candidates:

```text
CARD-TC-056

CARD-TC-060

CARD-TC-070–076

CARD-TC-115

CARD-TC-136

CARD-TC-137
```

Performance testing should verify:

```text
Authorization throughput

Decline/error rate

Hold uniqueness

Duplicate settlement count

Balance integrity

Limit enforcement
```

---

# 57. Test Evidence Requirements

For critical card tests, capture as applicable:

```text
Customer ID

Card ID

Masked PAN

Linked account

Card state

Opening balance

Available balance

Purchase amount

Hold amount

Settlement amount

Refund amount

Card limit

Transaction reference

API request/response

DB rows

Audit event

Notification

Timestamp

Defect ID
```

Do not capture plaintext:

```text
CVV

PIN

Full production PAN

Authentication secrets
```

---

# 58. Card Defect Examples

Potential Critical/High defects include:

```text
Customer can access another customer's card.

Frozen card authorizes purchases.

Blocked card authorizes purchases.

Expired card authorizes purchases.

Old replaced card remains active.

Card limit can be bypassed.

Declined purchase leaves permanent hold.

Duplicate authorization creates duplicate hold.

Duplicate settlement creates duplicate debit.

Refund credits customer twice.

Concurrent purchases overspend account.

Sensitive PAN/CVV/PIN appears in API/logs.

UI shows frozen but API card remains active.
```

---

# 59. Card Release Blockers

Release should normally be blocked by unresolved issues involving:

```text
Unauthorized card access

Frozen card usable

Blocked card usable

Expired card usable

Old replacement card usable

Duplicate purchase debit

Incorrect hold handling

Incorrect settlement

Duplicate refund

Card-limit bypass

Concurrent overspending

Sensitive card-data exposure

Critical card/account reconciliation failure
```

---

# 60. Card Exit Criteria

Card testing is acceptable when:

```text
Customers can access only their own cards.

Issuance and activation work correctly.

Freeze/unfreeze is enforced server-side.

Permanent block is terminal where required.

Replacement invalidates old card.

Expired cards cannot transact.

Card limits are enforced.

Purchase authorization behaves correctly.

Holds settle/release correctly.

Failed purchases remain financially neutral.

Refunds/reversals remain traceable.

Concurrency cannot overspend balance or limits.

Sensitive card data is protected.

UI/API/DB states agree.

No unresolved Critical/P0 card defect remains.
```

---

# 61. Final Card Testing Principle

A card should never be considered secure simply because the UI displays:

```text
Frozen
```

QA must prove that every transaction channel respects the same authoritative state.

For each card operation, validate:

```text
Who owns the card?

What is its current state?

Is the linked account valid?

Is the amount within limits?

Is sufficient balance available?

Was the transaction authorized once?

Was the hold correct?

Did settlement occur once?

Did any refund/reversal reconcile?

Was sensitive data protected?
```

The critical card rule is:

```text
Card status, account balance, limits,
authorization, holds, settlement,
and refunds must remain consistent
across every layer of the system.
```
