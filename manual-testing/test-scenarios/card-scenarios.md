# Banking System — Card Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Cards                          |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for card management within the Banking System.

Card functionality is a high-risk area because defects may allow:

* Unauthorized card usage
* Incorrect card-state transitions
* Transactions on frozen or blocked cards
* Incorrect spending limits
* Exposure of sensitive card data
* Duplicate replacement cards
* Incorrect card-account mapping
* Continued usage after expiry or cancellation

The scenarios cover card lifecycle, ownership, status, limits, masking, replacement, security, and transaction integration.

---

# 3. Scope

Card testing includes:

* Card issuance
* Card viewing
* Card ownership
* Card activation
* Card freeze
* Card unfreeze
* Card block
* Card cancellation
* Card replacement
* Card expiry
* Card limits
* Card status
* Linked account
* Sensitive data masking
* Card transaction eligibility
* Authorization
* Notifications
* Audit logging
* API consistency
* Database consistency
* Concurrency

---

# 4. Scenario Naming Convention

Card scenarios use:

```text
TS-CARD-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Card Viewing Scenarios

## TS-CARD-001 — Customer views own active card

**Priority:** P1

Expected:

* Correct card is displayed.
* Card belongs to authenticated customer.
* Card status is correct.
* Sensitive card data is masked appropriately.

---

## TS-CARD-002 — Customer views multiple owned cards

**Priority:** P1

Expected:

All owned cards appear without unrelated cards.

---

## TS-CARD-003 — Customer with no cards views cards page

**Priority:** P2

Expected:

Appropriate empty state.

---

## TS-CARD-004 — Customer opens card details

**Priority:** P1

Verify:

* Card alias/type
* Masked PAN
* Expiry date
* Status
* Linked account
* Available limits

---

## TS-CARD-005 — Refresh card details page

**Priority:** P2

Expected:

Latest persisted state remains correct.

---

# 6. Card Ownership and Authorization

## TS-CARD-006 — Customer attempts to view another customer's card

**Priority:** P0

Expected:

Access denied.

---

## TS-CARD-007 — Modify card ID in URL

**Priority:** P0

Expected:

Another customer's card cannot be accessed.

---

## TS-CARD-008 — Modify card ID in API request

**Priority:** P0

Expected:

Backend authorization rejects access.

---

## TS-CARD-009 — Customer attempts to freeze another customer's card

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-010 — Customer attempts to change another customer's card limits

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-011 — Unauthenticated user attempts card access

**Priority:** P0

Expected:

Authentication required.

---

## TS-CARD-012 — Expired session attempts card action

**Priority:** P0

Expected:

Rejected.

---

# 7. Card Issuance Scenarios

Execute where card issuance is supported.

## TS-CARD-013 — Issue card to eligible active customer

**Priority:** P1

Expected:

Card created successfully.

---

## TS-CARD-014 — Issue card linked to valid active account

**Priority:** P1

Expected:

Correct account relationship stored.

---

## TS-CARD-015 — Issue card linked to nonexistent account

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-016 — Issue card linked to another customer's account

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-017 — Issue card for closed account

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-018 — Issue card to disabled customer

**Priority:** P0

Expected:

Rejected where disabled customers are ineligible.

---

## TS-CARD-019 — Duplicate issuance request submitted rapidly

**Priority:** P1

Expected:

Unintended duplicate card is not created.

---

# 8. Card Activation Scenarios

## TS-CARD-020 — Activate valid inactive card

**Priority:** P0

Expected:

Status changes to ACTIVE.

---

## TS-CARD-021 — Attempt transaction before activation

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-022 — Activate already active card

**Priority:** P1

Expected:

Handled safely without invalid duplicate state change.

---

## TS-CARD-023 — Activate blocked card

**Priority:** P0

Expected:

Rejected unless an explicit recovery flow exists.

---

## TS-CARD-024 — Activate expired card

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-025 — Unauthorized activation request

**Priority:** P0

Expected:

Denied.

---

## TS-CARD-026 — Card activation generates audit record

**Priority:** P1

Expected:

State change is traceable.

---

# 9. Freeze Scenarios

## TS-CARD-027 — Customer freezes active card

**Priority:** P0

Expected:

Status becomes FROZEN.

---

## TS-CARD-028 — Frozen card shows updated status immediately

**Priority:** P0

Expected:

UI reflects persisted state.

---

## TS-CARD-029 — Frozen card transaction attempt

**Priority:** P0

Expected:

Rejected according to freeze rules.

---

## TS-CARD-030 — Freeze card while transaction page is open

**Priority:** P0

Expected:

Final card operation uses current card state.

---

## TS-CARD-031 — Freeze same card twice

**Priority:** P1

Expected:

No invalid state or server error.

---

## TS-CARD-032 — Customer freezes card from second browser session

**Priority:** P1

Expected:

Other sessions reflect new card state.

---

# 10. Unfreeze Scenarios

## TS-CARD-033 — Unfreeze frozen card

**Priority:** P0

Expected:

Status becomes ACTIVE.

---

## TS-CARD-034 — Perform valid operation after unfreeze

**Priority:** P1

Expected:

Card becomes usable again.

---

## TS-CARD-035 — Attempt to unfreeze blocked card

**Priority:** P0

Expected:

Rejected unless recovery explicitly permits it.

---

## TS-CARD-036 — Attempt to unfreeze expired card

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-037 — Unfreeze already active card

**Priority:** P1

Expected:

Handled safely.

---

# 11. Card Blocking Scenarios

## TS-CARD-038 — Block active card

**Priority:** P0

Expected:

Card becomes BLOCKED.

---

## TS-CARD-039 — Block frozen card

**Priority:** P0

Expected:

Card transitions to BLOCKED where allowed.

---

## TS-CARD-040 — Blocked card transaction attempt

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-041 — Blocked card cannot be normally unblocked by customer

**Priority:** P0

Expected:

Security policy enforced.

---

## TS-CARD-042 — Blocked card remains blocked after logout/login

**Priority:** P0

Expected:

State persists.

---

## TS-CARD-043 — Block action generates audit record

**Priority:** P0

Expected:

Actor, card, timestamp, and result recorded.

---

# 12. Card Cancellation Scenarios

## TS-CARD-044 — Authorized cancellation of active card

**Priority:** P0

Expected:

Status becomes CANCELLED.

---

## TS-CARD-045 — Cancel frozen card

**Priority:** P1

Expected:

Behavior follows lifecycle rules.

---

## TS-CARD-046 — Cancel blocked card

**Priority:** P1

Expected:

Allowed only according to business rules.

---

## TS-CARD-047 — Cancelled card transaction attempt

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-048 — Cancelled card cannot return to ACTIVE

**Priority:** P0

Expected:

Invalid transition rejected.

---

## TS-CARD-049 — Cancellation preserves historical card transactions

**Priority:** P0

Expected:

Financial history remains intact.

---

# 13. Card Replacement Scenarios

## TS-CARD-050 — Replace lost active card

**Priority:** P1

Expected:

Replacement flow starts successfully.

---

## TS-CARD-051 — Replace stolen card

**Priority:** P0

Expected:

Old card becomes unusable according to security policy.

---

## TS-CARD-052 — Replace expired card

**Priority:** P1

Expected:

New valid card issued.

---

## TS-CARD-053 — Replacement card has unique card identifier

**Priority:** P0

Expected:

No PAN/card-ID duplication.

---

## TS-CARD-054 — Old replaced card cannot be used

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-055 — Replacement card linked to correct account

**Priority:** P0

Expected:

Ownership and account relationship preserved.

---

## TS-CARD-056 — Double-submit replacement request

**Priority:** P0

Expected:

Only intended replacement card created.

---

## TS-CARD-057 — Replacement generates audit record

**Priority:** P1

Expected:

Old and new card relationship is traceable.

---

# 14. Card Expiry Scenarios

## TS-CARD-058 — Active card before expiry date

**Priority:** P1

Expected:

Card usable.

---

## TS-CARD-059 — Card on expiry boundary

**Priority:** P0

Expected:

Behavior follows exact expiration semantics.

---

## TS-CARD-060 — Card after expiry date

**Priority:** P0

Expected:

Card transaction rejected.

---

## TS-CARD-061 — Expired card cannot be activated

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-062 — Expired card still visible in historical card view

**Priority:** P2

Expected:

History remains available if supported.

---

## TS-CARD-063 — Expiry displayed in correct format

**Priority:** P2

Expected:

Customer can understand expiry date.

---

# 15. Card State Transition Scenarios

Possible lifecycle:

```text
INACTIVE
   ↓
ACTIVE
   ↓
FROZEN
   ↓
ACTIVE
   ↓
BLOCKED
```

and terminal states such as:

```text
CANCELLED
EXPIRED
```

---

## TS-CARD-064 — INACTIVE → ACTIVE

**Priority:** P0

Expected:

Valid activation.

---

## TS-CARD-065 — ACTIVE → FROZEN

**Priority:** P0

Expected:

Valid transition.

---

## TS-CARD-066 — FROZEN → ACTIVE

**Priority:** P0

Expected:

Valid unfreeze.

---

## TS-CARD-067 — ACTIVE → BLOCKED

**Priority:** P0

Expected:

Valid security transition.

---

## TS-CARD-068 — FROZEN → BLOCKED

**Priority:** P0

Expected:

Valid where supported.

---

## TS-CARD-069 — BLOCKED → ACTIVE

**Priority:** P0

Expected:

Rejected unless controlled recovery exists.

---

## TS-CARD-070 — CANCELLED → ACTIVE

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-071 — EXPIRED → ACTIVE

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-072 — Invalid transition through API

**Priority:** P0

Expected:

Backend rejects unsupported transition.

---

# 16. Spending Limit Scenarios

Assume example daily purchase limit:

```text
20,000.00
```

---

## TS-CARD-073 — Transaction below card purchase limit

**Priority:** P1

Expected:

Allowed if all other checks pass.

---

## TS-CARD-074 — Transaction exactly at purchase limit

**Priority:** P0

Expected:

Handled according to inclusive limit rule.

---

## TS-CARD-075 — Transaction above purchase limit

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-076 — Multiple purchases cumulatively reach daily limit

**Priority:** P0

Expected:

Correct cumulative limit tracking.

---

## TS-CARD-077 — Additional transaction exceeds cumulative daily limit

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-078 — Failed transactions do not incorrectly consume limit

**Priority:** P0

Expected:

Limit accounting follows business rules.

---

# 17. Withdrawal Limit Scenarios

Where ATM/withdrawal simulation exists.

## TS-CARD-079 — Withdrawal below daily limit

**Priority:** P1

Expected:

Allowed.

---

## TS-CARD-080 — Withdrawal at exact limit

**Priority:** P0

Expected:

Handled correctly.

---

## TS-CARD-081 — Withdrawal above limit

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-082 — Cumulative withdrawals exceed daily limit

**Priority:** P0

Expected:

Limit enforced.

---

# 18. Online / Channel Limit Scenarios

If separate limits exist.

## TS-CARD-083 — Online purchase below online limit

**Priority:** P1

Expected:

Allowed.

---

## TS-CARD-084 — Online purchase above online limit

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-085 — Card-present limit differs from online limit

**Priority:** P1

Expected:

Correct channel-specific rule enforced.

---

# 19. Card Limit Update Scenarios

## TS-CARD-086 — Customer reduces card limit within allowed range

**Priority:** P1

Expected:

New limit saved.

---

## TS-CARD-087 — Customer increases limit within permitted range

**Priority:** P1

Expected:

Allowed according to rules.

---

## TS-CARD-088 — Set limit below minimum allowed

**Priority:** P1

Expected:

Rejected.

---

## TS-CARD-089 — Set limit above maximum allowed

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-090 — Set exact maximum allowed limit

**Priority:** P1

Expected:

Accepted where inclusive.

---

## TS-CARD-091 — Unauthorized card-limit modification through API

**Priority:** P0

Expected:

Denied.

---

## TS-CARD-092 — Limit change while card transaction page is open

**Priority:** P0

Expected:

Current server-side limit enforced.

---

# 20. Linked Account Scenarios

## TS-CARD-093 — Card linked to correct customer account

**Priority:** P0

Expected:

Correct ownership.

---

## TS-CARD-094 — Card transaction debits linked account

**Priority:** P0

Expected:

Correct account balance affected.

---

## TS-CARD-095 — Linked account frozen

**Priority:** P0

Expected:

Card behavior follows account/card rules.

---

## TS-CARD-096 — Linked account closed

**Priority:** P0

Expected:

Card can no longer perform prohibited operations.

---

## TS-CARD-097 — Card linked to wrong account due to manipulated request

**Priority:** P0

Expected:

Backend rejects unauthorized relationship.

---

# 21. Card Transaction Scenarios

## TS-CARD-098 — Successful card purchase creates transaction

**Priority:** P0

Expected:

Correct debit and card transaction record.

---

## TS-CARD-099 — Frozen card purchase rejected

**Priority:** P0

Expected:

No balance change.

---

## TS-CARD-100 — Blocked card purchase rejected

**Priority:** P0

Expected:

No debit.

---

## TS-CARD-101 — Expired card purchase rejected

**Priority:** P0

Expected:

No debit.

---

## TS-CARD-102 — Cancelled card purchase rejected

**Priority:** P0

Expected:

No debit.

---

## TS-CARD-103 — Card purchase above account available balance

**Priority:** P0

Expected:

Rejected unless overdraft explicitly supported.

---

## TS-CARD-104 — Card purchase plus fee exceeds available balance

**Priority:** P0

Expected:

Rejected.

---

# 22. Pending Card Transaction Scenarios

## TS-CARD-105 — Card authorization creates pending hold

**Priority:** P0

Expected:

Available balance decreases according to hold amount.

---

## TS-CARD-106 — Pending card transaction completes

**Priority:** P0

Expected:

Final debit and balance state are correct.

---

## TS-CARD-107 — Pending card transaction expires/releases

**Priority:** P0

Expected:

Hold is released correctly.

---

## TS-CARD-108 — Reversed authorization restores available balance

**Priority:** P0

Expected:

Correct financial restoration.

---

## TS-CARD-109 — Multiple pending card holds calculated correctly

**Priority:** P0

Expected:

Available balance reflects aggregate holds.

---

# 23. Duplicate Card Transaction Scenarios

## TS-CARD-110 — Same merchant transaction submitted twice

**Priority:** P0

Expected:

Duplicate handling follows transaction rules.

---

## TS-CARD-111 — Slow card-processing response causes retry

**Priority:** P0

Expected:

No unintended double debit.

---

## TS-CARD-112 — Identical card transaction request replayed

**Priority:** P0

Expected:

Idempotency or duplicate controls prevent unintended duplicate financial effect.

---

# 24. Card Refund / Reversal Scenarios

## TS-CARD-113 — Valid card refund

**Priority:** P0

Expected:

Correct credit applied.

---

## TS-CARD-114 — Refund exceeds original transaction amount

**Priority:** P0

Expected:

Rejected unless partial/multiple refund rules explicitly permit total within original amount.

---

## TS-CARD-115 — Refund already fully refunded transaction

**Priority:** P0

Expected:

Duplicate over-refund prevented.

---

## TS-CARD-116 — Card refund appears in transaction history

**Priority:** P1

Expected:

Correct credit reference.

---

## TS-CARD-117 — Refund restores balance correctly

**Priority:** P0

Expected:

Financial reconciliation correct.

---

# 25. Concurrency Scenarios

## TS-CARD-118 — Two card purchases compete for same available balance

**Priority:** P0

Example:

```text
Available Balance = 1000
Purchase A = 700
Purchase B = 500
```

Expected:

Combined successful debit cannot exceed allowed balance.

---

## TS-CARD-119 — Card purchase and bank transfer occur concurrently

**Priority:** P0

Expected:

Account financial integrity preserved.

---

## TS-CARD-120 — Card frozen while transaction is pending

**Priority:** P0

Expected:

Outcome follows card-network/business rules and remains auditable.

---

## TS-CARD-121 — Limit changed while transaction is being processed

**Priority:** P0

Expected:

Defined authoritative timing rule applied consistently.

---

## TS-CARD-122 — Two sessions attempt freeze/unfreeze simultaneously

**Priority:** P1

Expected:

Final card state remains valid.

---

# 26. Sensitive Card Data Scenarios

## TS-CARD-123 — Card number masked by default

**Priority:** P0

Expected:

Example:

```text
**** **** **** 1234
```

---

## TS-CARD-124 — CVV not exposed in ordinary card list

**Priority:** P0

Expected:

Sensitive authentication data protected.

---

## TS-CARD-125 — PIN never displayed in plain text

**Priority:** P0

Expected:

No PIN exposure.

---

## TS-CARD-126 — API response does not expose unnecessary full PAN

**Priority:** P0

Expected:

Only necessary masked data returned.

---

## TS-CARD-127 — Card data not included in URL

**Priority:** P0

Expected:

PAN/CVV/PIN not present in query strings or path.

---

## TS-CARD-128 — Card data not exposed in error messages

**Priority:** P0

Expected:

No sensitive leakage.

---

## TS-CARD-129 — Card data not exposed in accessible logs

**Priority:** P0

Expected:

Sensitive values masked/redacted.

---

# 27. Card Details Reveal Scenarios

Where secure temporary reveal is supported.

## TS-CARD-130 — Reveal card details after required reauthentication

**Priority:** P0

Expected:

Sensitive details shown only after security control passes.

---

## TS-CARD-131 — Attempt reveal with expired session

**Priority:** P0

Expected:

Denied.

---

## TS-CARD-132 — Reveal times out and remasks card information

**Priority:** P0

Expected:

Sensitive information is not left exposed indefinitely.

---

## TS-CARD-133 — Browser refresh after reveal

**Priority:** P0

Expected:

Card details return to safe masked state unless secure design states otherwise.

---

# 28. Admin Card Scenarios

## TS-CARD-134 — Authorized admin views customer card

**Priority:** P1

Expected:

Only permitted fields shown.

---

## TS-CARD-135 — Limited admin sees masked card information

**Priority:** P0

Expected:

Sensitive field-level access enforced.

---

## TS-CARD-136 — Authorized admin blocks card

**Priority:** P0

Expected:

Card becomes blocked and audit created.

---

## TS-CARD-137 — Limited admin attempts card replacement without permission

**Priority:** P0

Expected:

Denied.

---

## TS-CARD-138 — Customer attempts admin card API

**Priority:** P0

Expected:

Denied.

---

# 29. Data Consistency Scenarios

## TS-CARD-139 — Card status matches UI and API

**Priority:** P0

Expected:

Consistent state.

---

## TS-CARD-140 — Card status matches API and database

**Priority:** P0

Expected:

Persisted state agrees.

---

## TS-CARD-141 — Card linked account matches UI/API/database

**Priority:** P0

Expected:

Correct account relationship.

---

## TS-CARD-142 — Limit value consistent across UI and API

**Priority:** P1

Expected:

No mismatch.

---

## TS-CARD-143 — Failed card state update leaves database unchanged

**Priority:** P0

Expected:

No partial update.

---

# 30. Transaction History Integration

## TS-CARD-144 — Successful card purchase appears in history

**Priority:** P0

Expected:

Correct card debit entry.

---

## TS-CARD-145 — Pending card transaction appears with correct status

**Priority:** P1

Expected:

Not shown as completed prematurely.

---

## TS-CARD-146 — Card refund appears as credit

**Priority:** P0

Expected:

Correct amount and reference.

---

## TS-CARD-147 — Declined card transaction does not appear as completed debit

**Priority:** P0

Expected:

Financial history remains accurate.

---

# 31. Statement Integration

## TS-CARD-148 — Completed card purchase appears on statement

**Priority:** P0

Expected:

Correct:

* Amount
* Merchant
* Date
* Reference
* Fee where applicable

---

## TS-CARD-149 — Refund appears on statement

**Priority:** P0

Expected:

Correct credit.

---

## TS-CARD-150 — Released authorization does not remain as completed charge

**Priority:** P0

Expected:

Statement reflects final financial state.

---

# 32. Error Handling Scenarios

## TS-CARD-151 — Card service unavailable

**Priority:** P1

Expected:

Safe failure message.

---

## TS-CARD-152 — Network failure during freeze request

**Priority:** P0

Expected:

Final card state can be determined safely after reconnect.

---

## TS-CARD-153 — Network failure during limit update

**Priority:** P1

Expected:

No ambiguous partial configuration.

---

## TS-CARD-154 — Backend error during card replacement

**Priority:** P0

Expected:

No unintended duplicate card issuance.

---

## TS-CARD-155 — Slow freeze request

**Priority:** P0

Expected:

Repeated actions do not produce invalid state.

---

# 33. Notification Scenarios

## TS-CARD-156 — Card activation notification

**Priority:** P1

Expected:

Correct customer notified.

---

## TS-CARD-157 — Card freeze notification

**Priority:** P1

Expected:

Reflects actual frozen state.

---

## TS-CARD-158 — Card unfreeze notification

**Priority:** P1

Expected:

Correct state communicated.

---

## TS-CARD-159 — Card blocked notification

**Priority:** P0

Expected:

Security-sensitive message generated.

---

## TS-CARD-160 — Card replacement notification

**Priority:** P1

Expected:

Customer informed appropriately.

---

## TS-CARD-161 — Failed card-state change does not generate false success notification

**Priority:** P1

Expected:

No misleading notification.

---

# 34. Audit Scenarios

## TS-CARD-162 — Activation audit event

**Priority:** P1

Expected:

Traceable.

---

## TS-CARD-163 — Freeze audit event

**Priority:** P0

Expected:

Actor, card, previous state, new state, timestamp.

---

## TS-CARD-164 — Unfreeze audit event

**Priority:** P1

Expected:

Traceable.

---

## TS-CARD-165 — Block audit event

**Priority:** P0

Expected:

Security action traceable.

---

## TS-CARD-166 — Replacement audit event

**Priority:** P1

Expected:

Old/new relationship traceable.

---

## TS-CARD-167 — Unauthorized card-action attempt logged where required

**Priority:** P2

Expected:

Security event available where implemented.

---

# 35. Security Scenarios

## TS-CARD-168 — Manipulate card owner ID

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-169 — Modify card status directly in client request

**Priority:** P0

Expected:

Backend authorization and state-transition validation enforced.

---

## TS-CARD-170 — Modify card limit beyond allowed maximum

**Priority:** P0

Expected:

Backend rejects manipulated value.

---

## TS-CARD-171 — Replay freeze/unfreeze request

**Priority:** P1

Expected:

No invalid state.

---

## TS-CARD-172 — Access full card data without required reauthentication

**Priority:** P0

Expected:

Denied.

---

# 36. Accessibility and Usability Scenarios

## TS-CARD-173 — Card status clearly visible

**Priority:** P1

Expected:

ACTIVE/FROZEN/BLOCKED state is unambiguous.

---

## TS-CARD-174 — Freeze and unfreeze actions clearly distinguished

**Priority:** P1

Expected:

Customer understands action.

---

## TS-CARD-175 — Destructive block/cancel action requires clear confirmation

**Priority:** P0

Expected:

Accidental permanent action reduced.

---

## TS-CARD-176 — Masked card digits allow customer to distinguish cards

**Priority:** P1

Expected:

Cards remain identifiable.

---

## TS-CARD-177 — Card controls support keyboard navigation

**Priority:** P2

Expected:

Logical accessible flow.

---

# 37. Responsive Scenarios

## TS-CARD-178 — Card list on desktop

**Priority:** P2

Expected:

Readable.

---

## TS-CARD-179 — Card list on tablet

**Priority:** P2

Expected:

Usable.

---

## TS-CARD-180 — Card controls on mobile

**Priority:** P1

Expected:

Freeze/block/limit actions remain accessible.

---

## TS-CARD-181 — Masked card number on mobile

**Priority:** P1

Expected:

Readable without exposing full number.

---

## TS-CARD-182 — Confirmation modal on mobile

**Priority:** P1

Expected:

Full warning and action buttons visible.

---

# 38. Cross-Browser Scenarios

## TS-CARD-183 — Card management in Chrome

**Priority:** P1

Expected:

Works correctly.

---

## TS-CARD-184 — Card management in Edge

**Priority:** P1

Expected:

Works correctly.

---

## TS-CARD-185 — Card management in Firefox

**Priority:** P1

Expected:

Works correctly.

---

## TS-CARD-186 — Card management in WebKit

**Priority:** P2

Expected:

No critical browser-specific issue.

---

# 39. Boundary Scenarios

## TS-CARD-187 — Purchase limit minus 0.01

**Priority:** P1

Expected:

Allowed.

---

## TS-CARD-188 — Purchase exact limit

**Priority:** P0

Expected:

Handled correctly.

---

## TS-CARD-189 — Purchase limit plus 0.01

**Priority:** P0

Expected:

Rejected.

---

## TS-CARD-190 — Available balance minus 0.01 purchase

**Priority:** P1

Expected:

Allowed if fees permit.

---

## TS-CARD-191 — Exact available balance purchase

**Priority:** P0

Expected:

Handled according to hold/fee rules.

---

## TS-CARD-192 — Available balance plus 0.01 purchase

**Priority:** P0

Expected:

Rejected where overdraft is not allowed.

---

## TS-CARD-193 — Card expires one day before boundary

**Priority:** P1

Expected:

Still valid where expiration policy allows.

---

## TS-CARD-194 — Card exactly at expiration boundary

**Priority:** P0

Expected:

Correct policy applied.

---

## TS-CARD-195 — Card immediately after expiration boundary

**Priority:** P0

Expected:

Rejected.

---

# 40. End-to-End Card Scenarios

## TS-CARD-196 — Card activation journey

**Priority:** P0

Flow:

```text
Inactive Card
→ Customer Opens Cards
→ Activates Card
→ Card Becomes ACTIVE
→ Perform Valid Card Operation
→ Verify Transaction
→ Verify Audit
```

---

## TS-CARD-197 — Freeze and unfreeze journey

**Priority:** P0

Flow:

```text
Active Card
→ Freeze
→ Attempt Card Transaction
→ Rejected
→ Unfreeze
→ Retry Valid Transaction
→ Allowed
```

---

## TS-CARD-198 — Lost card replacement journey

**Priority:** P0

Flow:

```text
Active Card
→ Report Lost
→ Block Old Card
→ Request Replacement
→ New Card Issued
→ Verify Old Card Unusable
→ Activate New Card
```

---

## TS-CARD-199 — Expired card journey

**Priority:** P0

Flow:

```text
Active Card
→ Expiry Reached
→ Card Becomes EXPIRED
→ Attempt Transaction
→ Rejected
→ Request/Receive Replacement
```

---

## TS-CARD-200 — Card limit enforcement journey

**Priority:** P0

Flow:

```text
Set Daily Limit
→ Complete Transactions Below Limit
→ Reach Limit
→ Attempt Additional Transaction
→ Rejected
→ Verify Balance Integrity
```

---

## TS-CARD-201 — Concurrent card transactions journey

**Priority:** P0

Flow:

```text
Available Balance = 1000
→ Card Purchase A = 700
→ Card Purchase B = 500
→ Submit Concurrently
→ Verify Combined Successful Debit Does Not Exceed Balance
→ Verify Final Balance
→ Verify Transaction States
```

---

# 41. Critical Smoke Scenarios

Card smoke coverage should include:

```text
TS-CARD-001 — View own card
TS-CARD-006 — Cannot view another customer's card
TS-CARD-020 — Activate card
TS-CARD-027 — Freeze card
TS-CARD-029 — Frozen card transaction rejected
TS-CARD-033 — Unfreeze card
TS-CARD-038 — Block card
TS-CARD-098 — Successful card purchase
```

---

# 42. Critical Regression Scenarios

Always prioritize:

* Card ownership
* Activation
* Freeze/unfreeze
* Block
* Cancel
* Expiry
* Replacement
* Linked account
* Spending limits
* Available balance
* Frozen/blocked transaction rejection
* Pending holds
* Refunds
* Duplicate transaction prevention
* Concurrency
* Sensitive-data masking
* UI/API/database consistency
* Authorization
* Audit logging

---

# 43. Automation Candidates

Strong UI automation candidates:

* View card
* Activate card
* Freeze card
* Unfreeze card
* Block card
* Limit changes
* Replacement flow
* Expiry-state validation
* Masking checks
* Unauthorized access

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 44. API Automation Candidates

Postman and REST Assured should later cover:

* Get cards
* Get card details
* Unauthorized card access
* Activate
* Freeze
* Unfreeze
* Block
* Cancel
* Replace
* Update limits
* Invalid state transitions
* Transaction eligibility
* Card/account ownership

---

# 45. SQL Validation Candidates

Database testing should validate:

* Card owner
* Linked account
* Card status
* Expiry
* Limits
* Replacement relationship
* Card transaction relationships
* Pending holds
* Audit records

Sensitive raw card data should never be included unnecessarily in test evidence.

---

# 46. Performance Testing Candidates

JMeter can later test:

* Card-list retrieval
* Card-status updates
* High-volume simulated card transactions
* Concurrent balance contention
* Limit checks
* Authorization/hold processing

Performance must not compromise financial integrity or card-state enforcement.

---

# 47. BDD Candidates

Example:

```gherkin
Feature: Card freeze

Scenario: Frozen card cannot be used for purchases
  Given the customer has an active card
  And the card is linked to an active account
  When the customer freezes the card
  And a purchase is attempted using the card
  Then the purchase should be rejected
  And the account balance should remain unchanged
```

Replacement example:

```gherkin
Scenario: Replaced card makes the old card unusable
  Given the customer has reported an active card as lost
  When a replacement card is issued
  Then the old card should no longer be usable
  And the replacement card should be linked to the correct customer account
```

---

# 48. Risk Traceability

Major related risks include:

```text
RISK-001 — Incorrect account balance
RISK-002 — Unauthorized customer data access
RISK-013 — Concurrent transactions corrupt balance
RISK-021 — Sensitive information exposure
RISK-023 — Unauthorized admin operation
RISK-024 — Card freeze does not prevent operations
RISK-025 — Blocked card becomes usable unexpectedly
RISK-030 — Unauthorized API request
RISK-039 — API/database inconsistency
RISK-047 — Insecure direct object access
```

---

# 49. Card Coverage Summary

This catalog covers:

* Card viewing
* Ownership
* Issuance
* Activation
* Freeze/unfreeze
* Blocking
* Cancellation
* Replacement
* Expiry
* State transitions
* Spending limits
* Withdrawal limits
* Channel limits
* Linked accounts
* Card transactions
* Pending holds
* Duplicate transactions
* Refunds
* Concurrency
* Sensitive data
* Secure detail reveal
* Admin actions
* Data consistency
* Transaction history
* Statements
* Error handling
* Notifications
* Audit
* Security
* Accessibility
* Responsive behavior
* Cross-browser behavior
* Boundaries
* End-to-end flows

---

# 50. Final Card Testing Principle

Card testing must validate the card's full lifecycle and its connection to financial state.

The most important questions are:

```text
Does the card belong to the authenticated customer?

Is sensitive card data protected?

Can inactive, frozen, blocked, expired, or cancelled cards be used?

Do card-state changes take effect immediately?

Are card limits enforced server-side?

Is the correct bank account debited?

Can duplicate card transactions cause multiple debits?

Are pending holds and reversals reflected correctly?

Can an old replaced card still be used?

Do UI, API, database, transaction history, and statements agree?

Are critical card actions authorized and audited?
```

A card's visual state is not enough; the backend transaction rules must always enforce the same security and financial state.
