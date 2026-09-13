# Banking System — Notification Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Notifications                  |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for notification functionality within the Banking System.

Notifications provide customers and administrators with information about:

* Financial transactions
* Security events
* Account changes
* Card actions
* Loan events
* Deposit events
* Payment events
* Transfer events
* Administrative actions

Incorrect notifications can create serious customer confusion even when the underlying financial transaction is correct.

Critical notification risks include:

* Success notification for failed transaction
* Wrong amount
* Wrong recipient
* Duplicate notification
* Missing security alert
* Exposure of sensitive information
* Incorrect read/unread state
* Notification preferences not respected

---

# 3. Scope

Notification testing includes:

* In-app notifications
* Email notifications
* SMS notifications where simulated
* Security alerts
* Transaction alerts
* Account alerts
* Card alerts
* Loan alerts
* Deposit alerts
* Payment alerts
* Transfer alerts
* Notification preferences
* Read/unread state
* Deletion/dismissal
* Duplicate prevention
* Notification content
* Deep links
* Delivery status
* Error handling
* Authorization
* Data consistency
* Audit integration

---

# 4. Scenario Naming Convention

Notification scenarios use:

```text
TS-NOTIF-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Notification Center Scenarios

## TS-NOTIF-001 — Customer views own notification center

**Priority:** P1

Expected:

Only notifications belonging to the authenticated customer are displayed.

---

## TS-NOTIF-002 — Customer with no notifications views notification center

**Priority:** P2

Expected:

Appropriate empty state is displayed.

---

## TS-NOTIF-003 — Customer with multiple notifications views complete list

**Priority:** P1

Expected:

Notifications appear according to configured ordering and pagination rules.

---

## TS-NOTIF-004 — New notification appears after triggering event

**Priority:** P0

Expected:

New notification becomes available after the underlying event is successfully processed.

---

## TS-NOTIF-005 — Refresh notification center

**Priority:** P2

Expected:

Latest persisted notification state is shown.

---

## TS-NOTIF-006 — Notification counter displays correct unread count

**Priority:** P1

Expected:

Unread badge matches actual unread notifications.

---

# 6. Notification Ownership and Authorization

## TS-NOTIF-007 — Customer attempts to access another customer's notification

**Priority:** P0

Expected:

Access denied.

---

## TS-NOTIF-008 — Modify notification ID in URL

**Priority:** P0

Expected:

Another customer's notification cannot be accessed.

---

## TS-NOTIF-009 — Modify notification ID through API

**Priority:** P0

Expected:

Backend authorization rejects access.

---

## TS-NOTIF-010 — Unauthenticated user accesses notification center

**Priority:** P0

Expected:

Authentication required.

---

## TS-NOTIF-011 — Expired session accesses notification

**Priority:** P0

Expected:

Reauthentication required.

---

## TS-NOTIF-012 — Customer A logs out and Customer B logs in

**Priority:** P0

Expected:

No notifications belonging to Customer A remain visible.

---

# 7. Successful Transfer Notifications

## TS-NOTIF-013 — Successful outgoing transfer generates notification

**Priority:** P0

Expected:

Notification reflects:

* Successful status
* Correct amount
* Correct currency
* Correct destination context
* Correct transaction reference where included

---

## TS-NOTIF-014 — Internal transfer recipient receives credit notification

**Priority:** P1

Where supported.

Expected:

Correct recipient receives correct incoming amount.

---

## TS-NOTIF-015 — Own-account transfer notification

**Priority:** P2

Expected:

Notification accurately reflects movement between customer's accounts.

---

## TS-NOTIF-016 — Transfer notification amount matches transaction

**Priority:** P0

Expected:

Exact match.

---

## TS-NOTIF-017 — Transfer notification reference matches transaction

**Priority:** P1

Expected:

Consistent reference.

---

# 8. Failed Transfer Notifications

## TS-NOTIF-018 — Failed transfer generates failure notification where configured

**Priority:** P1

Expected:

Notification clearly indicates failure.

---

## TS-NOTIF-019 — Failed transfer does not generate success notification

**Priority:** P0

Expected:

No misleading financial confirmation.

---

## TS-NOTIF-020 — Insufficient-funds transfer notification reflects failure

**Priority:** P1

Expected:

Correct status.

---

## TS-NOTIF-021 — Cancelled transfer does not generate completed-transfer notification

**Priority:** P0

Expected:

No false completion message.

---

## TS-NOTIF-022 — Reversed transfer generates reversal notification

**Priority:** P1

Expected:

Customer is informed that the previous financial event was reversed.

---

# 9. Scheduled Transfer Notifications

## TS-NOTIF-023 — Scheduled transfer creation notification

**Priority:** P2

Expected:

Correct schedule and amount.

---

## TS-NOTIF-024 — Scheduled transfer execution notification

**Priority:** P1

Expected:

Reflects actual executed transaction.

---

## TS-NOTIF-025 — Scheduled transfer failure notification

**Priority:** P1

Expected:

Failure state communicated.

---

## TS-NOTIF-026 — Cancelled scheduled transfer notification

**Priority:** P2

Expected:

Cancellation accurately reflected.

---

## TS-NOTIF-027 — Cancelled scheduled transfer does not later send execution-success notification

**Priority:** P0

Expected:

No contradiction.

---

# 10. Payment Notifications

## TS-NOTIF-028 — Successful bill payment notification

**Priority:** P0

Expected:

Correct:

* Payee
* Amount
* Currency
* Reference
* Status

---

## TS-NOTIF-029 — Failed payment notification

**Priority:** P1

Expected:

Correct failure status.

---

## TS-NOTIF-030 — Failed payment does not generate success notification

**Priority:** P0

Expected:

No false financial confirmation.

---

## TS-NOTIF-031 — Payment reversal notification

**Priority:** P1

Expected:

Correct reversal details.

---

## TS-NOTIF-032 — Already-paid bill rejection does not generate another payment-success notification

**Priority:** P0

Expected:

No duplicate misleading confirmation.

---

# 11. Card Notifications

## TS-NOTIF-033 — Card activation notification

**Priority:** P1

Expected:

Correct card identified using masked data.

---

## TS-NOTIF-034 — Card freeze notification

**Priority:** P0

Expected:

Customer notified of actual frozen state.

---

## TS-NOTIF-035 — Card unfreeze notification

**Priority:** P1

Expected:

Correct state communicated.

---

## TS-NOTIF-036 — Card block notification

**Priority:** P0

Expected:

Security-sensitive notification sent.

---

## TS-NOTIF-037 — Card replacement notification

**Priority:** P1

Expected:

Old/new card context safely represented.

---

## TS-NOTIF-038 — Card transaction notification

**Priority:** P1

Expected:

Correct merchant, amount, and masked card identifier.

---

## TS-NOTIF-039 — Declined card transaction notification

**Priority:** P1

Expected:

Must not imply successful debit.

---

## TS-NOTIF-040 — Card refund notification

**Priority:** P1

Expected:

Correct refund amount.

---

# 12. Account Notifications

## TS-NOTIF-041 — Account freeze notification

**Priority:** P0

Expected:

Correct customer informed immediately according to business rules.

---

## TS-NOTIF-042 — Account unfreeze notification

**Priority:** P1

Expected:

Correct status.

---

## TS-NOTIF-043 — Account restriction notification

**Priority:** P0

Expected:

Restriction accurately communicated.

---

## TS-NOTIF-044 — Account closure notification

**Priority:** P1

Expected:

Generated only after successful closure.

---

## TS-NOTIF-045 — Failed account closure does not generate closure-success notification

**Priority:** P0

Expected:

No false status message.

---

# 13. Customer and Profile Notifications

## TS-NOTIF-046 — Email-address change notification

**Priority:** P0

Expected:

Security notification generated according to requirements.

---

## TS-NOTIF-047 — Phone-number change notification

**Priority:** P1

Expected:

Correct security message.

---

## TS-NOTIF-048 — Password-change notification

**Priority:** P0

Expected:

Customer informed of security-sensitive change.

---

## TS-NOTIF-049 — Password-reset notification

**Priority:** P0

Expected:

Correct and secure message.

---

## TS-NOTIF-050 — KYC status update notification

**Priority:** P2

Expected:

Correct verification result.

---

## TS-NOTIF-051 — Customer restriction/suspension notification

**Priority:** P1

Expected:

Actual status accurately communicated.

---

# 14. Authentication and Security Notifications

## TS-NOTIF-052 — New-device login notification

**Priority:** P1

Where supported.

Expected:

Correct login event information.

---

## TS-NOTIF-053 — Suspicious-login notification

**Priority:** P0

Expected:

Customer alerted according to security policy.

---

## TS-NOTIF-054 — Account-lockout notification

**Priority:** P1

Expected:

Customer informed where applicable.

---

## TS-NOTIF-055 — Password reset requested notification

**Priority:** P0

Expected:

Customer receives appropriate security alert without exposing reset token unnecessarily.

---

## TS-NOTIF-056 — MFA/security-setting change notification

**Priority:** P0

Expected:

Security-sensitive configuration change is communicated.

---

## TS-NOTIF-057 — Failed login does not send excessive notifications beyond configured policy

**Priority:** P2

Expected:

Notification throttling follows design.

---

# 15. Loan Notifications

## TS-NOTIF-058 — Loan application submitted notification

**Priority:** P2

Expected:

Application reference/status correct.

---

## TS-NOTIF-059 — Loan approval notification

**Priority:** P0

Expected:

Correct approved amount, term, and status.

---

## TS-NOTIF-060 — Loan rejection notification

**Priority:** P1

Expected:

Correct application status.

---

## TS-NOTIF-061 — Loan disbursement notification

**Priority:** P0

Expected:

Amount equals actual account credit.

---

## TS-NOTIF-062 — Loan repayment notification

**Priority:** P1

Expected:

Correct repayment amount.

---

## TS-NOTIF-063 — Failed loan repayment notification

**Priority:** P1

Expected:

Failure clearly communicated.

---

## TS-NOTIF-064 — Loan closure notification

**Priority:** P1

Expected:

Sent only after outstanding balance reaches valid closed state.

---

# 16. Deposit Notifications

## TS-NOTIF-065 — Deposit creation notification

**Priority:** P1

Expected:

Correct principal, term, rate, and maturity details where included.

---

## TS-NOTIF-066 — Deposit maturity reminder

**Priority:** P2

Expected:

Correct maturity date.

---

## TS-NOTIF-067 — Deposit maturity notification

**Priority:** P1

Expected:

Reflects actual maturity processing.

---

## TS-NOTIF-068 — Deposit payout notification

**Priority:** P0

Expected:

Amount matches actual account credit.

---

## TS-NOTIF-069 — Early-withdrawal notification

**Priority:** P1

Expected:

Penalty and net payout represented correctly where included.

---

## TS-NOTIF-070 — Auto-renewal notification

**Priority:** P1

Expected:

Correct renewed state and terms.

---

# 17. Notification Content Accuracy

## TS-NOTIF-071 — Notification amount matches financial transaction amount

**Priority:** P0

Expected:

Exact amount.

---

## TS-NOTIF-072 — Notification currency matches transaction currency

**Priority:** P0

Expected:

Correct.

---

## TS-NOTIF-073 — Notification status matches final transaction status

**Priority:** P0

Expected:

No stale or contradictory state.

---

## TS-NOTIF-074 — Notification timestamp is correct

**Priority:** P1

Expected:

Matches event timing according to timezone rules.

---

## TS-NOTIF-075 — Notification account/card identifier is correctly masked

**Priority:** P0

Expected:

Enough information for recognition without exposing sensitive data.

---

## TS-NOTIF-076 — Notification recipient name is correct

**Priority:** P1

Expected:

Correct customer.

---

## TS-NOTIF-077 — Notification link/reference points to correct event

**Priority:** P1

Expected:

No cross-transaction mismatch.

---

# 18. Wrong Recipient Prevention

## TS-NOTIF-078 — Customer A event does not notify Customer B

**Priority:** P0

Expected:

Only intended recipient receives notification.

---

## TS-NOTIF-079 — Internal transfer sender and recipient receive their respective notifications

**Priority:** P0

Expected:

Content is appropriate to each role.

---

## TS-NOTIF-080 — Card event notification delivered only to card owner

**Priority:** P0

Expected:

No data leakage.

---

## TS-NOTIF-081 — Loan event notification delivered to correct borrower

**Priority:** P0

Expected:

Correct.

---

## TS-NOTIF-082 — Admin action on Customer A does not notify unrelated customer

**Priority:** P0

Expected:

No cross-customer notification.

---

# 19. Duplicate Notification Prevention

## TS-NOTIF-083 — One completed transfer generates only expected number of notifications

**Priority:** P0

Expected:

No duplicate alerts.

---

## TS-NOTIF-084 — Refreshing success page does not resend notification

**Priority:** P0

Expected:

No duplicate.

---

## TS-NOTIF-085 — Duplicate transfer request blocked also prevents duplicate notification

**Priority:** P0

Expected:

One financial event produces one configured notification set.

---

## TS-NOTIF-086 — Retried backend notification job does not produce duplicate visible notifications

**Priority:** P0

Expected:

Idempotent notification behavior where required.

---

## TS-NOTIF-087 — Reopening notification center does not duplicate stored entries

**Priority:** P1

Expected:

No duplicate rendering/storage.

---

# 20. Missing Notification Scenarios

## TS-NOTIF-088 — Critical account freeze produces required alert

**Priority:** P0

Expected:

Notification exists.

---

## TS-NOTIF-089 — Password change produces required security alert

**Priority:** P0

Expected:

Notification exists.

---

## TS-NOTIF-090 — Successful high-risk transfer generates required alert

**Priority:** P0

Expected:

Notification exists.

---

## TS-NOTIF-091 — Card block produces required alert

**Priority:** P0

Expected:

Notification exists.

---

# 21. Read / Unread State Scenarios

## TS-NOTIF-092 — New notification initially appears unread

**Priority:** P1

Expected:

Unread state displayed correctly.

---

## TS-NOTIF-093 — Open notification marks it read

**Priority:** P1

Expected:

State updates.

---

## TS-NOTIF-094 — Mark notification as read manually

**Priority:** P2

Expected:

Unread count decreases correctly.

---

## TS-NOTIF-095 — Mark notification unread again

**Priority:** P2

Where supported.

Expected:

Unread count increases accordingly.

---

## TS-NOTIF-096 — Mark all notifications as read

**Priority:** P2

Expected:

Unread count becomes zero.

---

## TS-NOTIF-097 — Refresh after marking notification read

**Priority:** P1

Expected:

Read state persists.

---

## TS-NOTIF-098 — Read state consistent across two browser sessions

**Priority:** P1

Expected:

Server-side state eventually synchronized.

---

# 22. Notification Counter Scenarios

## TS-NOTIF-099 — New unread notification increments counter

**Priority:** P1

Expected:

Correct count.

---

## TS-NOTIF-100 — Reading notification decrements counter

**Priority:** P1

Expected:

Correct.

---

## TS-NOTIF-101 — Multiple new notifications increment counter accurately

**Priority:** P1

Expected:

Count matches actual unread records.

---

## TS-NOTIF-102 — Mark-all-read resets counter

**Priority:** P1

Expected:

Counter becomes zero.

---

## TS-NOTIF-103 — Duplicate backend event does not inflate unread counter

**Priority:** P1

Expected:

Count remains accurate.

---

# 23. Notification Deletion / Dismissal

Where supported.

## TS-NOTIF-104 — Customer dismisses notification

**Priority:** P2

Expected:

Notification disappears according to design.

---

## TS-NOTIF-105 — Delete notification

**Priority:** P2

Expected:

Only owned notification affected.

---

## TS-NOTIF-106 — Delete another customer's notification through manipulated ID

**Priority:** P0

Expected:

Denied.

---

## TS-NOTIF-107 — Deleted/dismissed notification does not affect transaction history

**Priority:** P0

Expected:

Financial records remain intact.

---

## TS-NOTIF-108 — Deleting notification does not delete audit record

**Priority:** P1

Expected:

Audit history unaffected.

---

# 24. Notification Preferences

Potential categories:

```text
Transfers
Payments
Cards
Loans
Deposits
Security
Marketing
Account Updates
```

Security-critical notification behavior should follow product requirements and may not be fully disableable.

---

## TS-NOTIF-109 — Enable transfer notifications

**Priority:** P2

Expected:

Preference saved.

---

## TS-NOTIF-110 — Disable optional transfer notification

**Priority:** P2

Expected:

Optional notification suppressed.

---

## TS-NOTIF-111 — Disable optional payment notifications

**Priority:** P2

Expected:

Preference respected.

---

## TS-NOTIF-112 — Security-critical notification cannot be disabled if mandatory

**Priority:** P0

Expected:

System enforces required security alert policy.

---

## TS-NOTIF-113 — Preference persists after logout/login

**Priority:** P1

Expected:

Stored setting remains.

---

## TS-NOTIF-114 — Preference update reflected across sessions

**Priority:** P1

Expected:

Consistent.

---

# 25. Notification Channel Preferences

Where multiple delivery channels exist.

## TS-NOTIF-115 — Enable in-app notifications

**Priority:** P2

Expected:

Channel enabled.

---

## TS-NOTIF-116 — Enable email notifications

**Priority:** P2

Expected:

Channel enabled.

---

## TS-NOTIF-117 — Enable SMS notifications

**Priority:** P2

Expected:

Channel enabled where supported.

---

## TS-NOTIF-118 — Disable optional email notifications

**Priority:** P2

Expected:

Email suppressed while other enabled channels still operate.

---

## TS-NOTIF-119 — Notification sent only through selected optional channels

**Priority:** P1

Expected:

Preferences respected.

---

## TS-NOTIF-120 — Mandatory security alert ignores disabled optional preferences where required

**Priority:** P0

Expected:

Security policy takes precedence.

---

# 26. Email Notification Scenarios

## TS-NOTIF-121 — Email delivered to current verified email address

**Priority:** P1

Expected:

Correct recipient.

---

## TS-NOTIF-122 — Old email is not used after verified email change

**Priority:** P0

Expected:

Notifications use current trusted contact according to design.

---

## TS-NOTIF-123 — Email subject accurately describes event

**Priority:** P2

Expected:

Clear and non-misleading.

---

## TS-NOTIF-124 — Email body contains correct transaction details

**Priority:** P0

Expected:

Correct amount/status/reference as appropriate.

---

## TS-NOTIF-125 — Email does not expose passwords, OTPs, full tokens, CVV, or PIN

**Priority:** P0

Expected:

Sensitive information protected.

---

# 27. SMS Notification Scenarios

Where simulated SMS exists.

## TS-NOTIF-126 — SMS delivered to current verified phone

**Priority:** P1

Expected:

Correct destination.

---

## TS-NOTIF-127 — Old phone is not used after verified phone change

**Priority:** P0

Expected:

Correct contact mapping.

---

## TS-NOTIF-128 — SMS amount and status correct

**Priority:** P0

Expected:

Matches actual financial event.

---

## TS-NOTIF-129 — SMS does not expose full sensitive card/account data

**Priority:** P0

Expected:

Masked identifiers only.

---

# 28. In-App Notification Deep Links

## TS-NOTIF-130 — Transfer notification opens correct transaction

**Priority:** P1

Expected:

Correct owned transaction details.

---

## TS-NOTIF-131 — Card notification opens correct card

**Priority:** P1

Expected:

Correct owned card.

---

## TS-NOTIF-132 — Loan notification opens correct loan

**Priority:** P1

Expected:

Correct loan.

---

## TS-NOTIF-133 — Notification deep link after logout

**Priority:** P0

Expected:

Authentication required before protected content is shown.

---

## TS-NOTIF-134 — Notification deep link to another customer's resource

**Priority:** P0

Expected:

Backend authorization denies access.

---

## TS-NOTIF-135 — Deleted/invalid target resource

**Priority:** P2

Expected:

Safe unavailable-resource state.

---

# 29. Delivery Failure Scenarios

## TS-NOTIF-136 — Email provider unavailable

**Priority:** P1

Expected:

Underlying banking transaction remains correct.

Notification failure is recorded/retried according to design.

---

## TS-NOTIF-137 — SMS provider unavailable

**Priority:** P1

Expected:

Financial operation remains unaffected.

---

## TS-NOTIF-138 — In-app notification storage failure

**Priority:** P1

Expected:

Core financial transaction should not be incorrectly rolled back unless notification is explicitly transactional.

---

## TS-NOTIF-139 — Notification retry succeeds after temporary provider failure

**Priority:** P1

Expected:

Notification eventually delivered without duplication.

---

## TS-NOTIF-140 — Permanent delivery failure

**Priority:** P2

Expected:

Failure status/logging is available where implemented.

---

# 30. Financial Transaction Independence

## TS-NOTIF-141 — Successful transfer remains successful when email delivery fails

**Priority:** P0

Expected:

Financial state remains correct.

---

## TS-NOTIF-142 — Successful payment remains successful when notification service fails

**Priority:** P0

Expected:

No rollback solely because notification could not be delivered unless explicitly designed otherwise.

---

## TS-NOTIF-143 — Notification service delay does not cause transaction resubmission

**Priority:** P0

Expected:

Customer can distinguish transaction processing from notification delivery.

---

# 31. Notification Status Consistency

## TS-NOTIF-144 — COMPLETED transaction produces completed-status content

**Priority:** P0

Expected:

Correct.

---

## TS-NOTIF-145 — FAILED transaction never produces completed-status content

**Priority:** P0

Expected:

Correct.

---

## TS-NOTIF-146 — REVERSED transaction notification does not continue claiming original transaction is final

**Priority:** P0

Expected:

Latest state represented accurately.

---

## TS-NOTIF-147 — PENDING transaction notification clearly indicates pending status

**Priority:** P1

Expected:

No misleading finality.

---

# 32. Timing and Ordering Scenarios

## TS-NOTIF-148 — Notification timestamp corresponds to triggering event

**Priority:** P1

Expected:

Correct.

---

## TS-NOTIF-149 — Newest notification appears first

**Priority:** P2

Where required.

Expected:

Correct chronological ordering.

---

## TS-NOTIF-150 — Multiple events occurring in same second remain distinguishable

**Priority:** P1

Expected:

No missing or overwritten notification.

---

## TS-NOTIF-151 — Reversal notification appears after original transaction notification

**Priority:** P1

Expected:

Logical chronological order.

---

## TS-NOTIF-152 — Timezone displayed correctly

**Priority:** P1

Expected:

Consistent user-facing time.

---

# 33. Concurrency Scenarios

## TS-NOTIF-153 — Multiple simultaneous transfers create separate correct notifications

**Priority:** P0

Expected:

Each actual transaction represented once.

---

## TS-NOTIF-154 — Same transaction processed by retry workers

**Priority:** P0

Expected:

No duplicate customer-facing alert.

---

## TS-NOTIF-155 — Customer reads notification while another notification arrives

**Priority:** P2

Expected:

Unread counter remains accurate.

---

## TS-NOTIF-156 — Notification preference changes while event is processing

**Priority:** P1

Expected:

Behavior follows defined timing semantics consistently.

---

# 34. API Scenarios

## TS-NOTIF-157 — Get own notifications through API

**Priority:** P1

Expected:

Only owned notifications returned.

---

## TS-NOTIF-158 — Get another customer's notification

**Priority:** P0

Expected:

Denied.

---

## TS-NOTIF-159 — Mark owned notification as read through API

**Priority:** P1

Expected:

State persists.

---

## TS-NOTIF-160 — Mark another customer's notification read

**Priority:** P0

Expected:

Denied.

---

## TS-NOTIF-161 — Update notification preference through API

**Priority:** P1

Expected:

Authorized preference saved.

---

## TS-NOTIF-162 — Manipulate customer ID in preference request

**Priority:** P0

Expected:

Ownership enforced.

---

# 35. Database Consistency Scenarios

## TS-NOTIF-163 — Notification stored for correct customer

**Priority:** P0

Expected:

Correct customer relationship.

---

## TS-NOTIF-164 — Notification event reference maps to correct transaction/resource

**Priority:** P0

Expected:

Correct relationship.

---

## TS-NOTIF-165 — Read status persists correctly

**Priority:** P1

Expected:

Database/API/UI agree.

---

## TS-NOTIF-166 — Duplicate notification record not created for same idempotent event

**Priority:** P0

Expected:

No unintended duplicate.

---

## TS-NOTIF-167 — Delivery-channel status stored correctly where applicable

**Priority:** P2

Expected:

Sent/failed/retried state matches actual behavior.

---

# 36. Error Handling Scenarios

## TS-NOTIF-168 — Notification center API unavailable

**Priority:** P1

Expected:

Safe error message.

---

## TS-NOTIF-169 — Notification content malformed

**Priority:** P1

Expected:

UI handles invalid response safely.

---

## TS-NOTIF-170 — Mark-as-read request fails

**Priority:** P2

Expected:

Notification does not falsely appear permanently read if persistence failed.

---

## TS-NOTIF-171 — Preference save fails

**Priority:** P1

Expected:

No false success; previous preference remains authoritative.

---

## TS-NOTIF-172 — Notification list partially loads

**Priority:** P2

Expected:

Application does not incorrectly state that incomplete result is complete.

---

# 37. Security and Sensitive Data Scenarios

## TS-NOTIF-173 — Notification masks account numbers

**Priority:** P0

Expected:

Only approved digits exposed.

---

## TS-NOTIF-174 — Notification masks card numbers

**Priority:** P0

Expected:

No full PAN.

---

## TS-NOTIF-175 — Notification never contains CVV or PIN

**Priority:** P0

Expected:

No exposure.

---

## TS-NOTIF-176 — Notification never contains password

**Priority:** P0

Expected:

No credential exposure.

---

## TS-NOTIF-177 — Notification does not expose authentication token

**Priority:** P0

Expected:

No token leakage.

---

## TS-NOTIF-178 — Notification does not expose excessive recipient/customer data

**Priority:** P1

Expected:

Minimum required information only.

---

# 38. Search and Filtering Scenarios

Where notification-center search/filtering exists.

## TS-NOTIF-179 — Filter unread notifications

**Priority:** P2

Expected:

Only unread items.

---

## TS-NOTIF-180 — Filter read notifications

**Priority:** P2

Expected:

Only read items.

---

## TS-NOTIF-181 — Filter by transaction category

**Priority:** P2

Expected:

Correct category.

---

## TS-NOTIF-182 — Filter security notifications

**Priority:** P2

Expected:

Correct events.

---

## TS-NOTIF-183 — Filter by date

**Priority:** P2

Expected:

Correct range.

---

## TS-NOTIF-184 — Clear notification filters

**Priority:** P3

Expected:

Full authorized list restored.

---

# 39. Pagination and Large Dataset Scenarios

## TS-NOTIF-185 — Notification pagination works correctly

**Priority:** P2

Expected:

No missing or duplicate notifications between pages.

---

## TS-NOTIF-186 — Large number of notifications loads correctly

**Priority:** P2

Expected:

Usable behavior.

---

## TS-NOTIF-187 — Unread count remains accurate with pagination

**Priority:** P1

Expected:

Count represents all unread notifications, not just current page.

---

# 40. Accessibility and Usability Scenarios

## TS-NOTIF-188 — Read/unread state distinguishable without color only

**Priority:** P2

Expected:

Additional indicator available.

---

## TS-NOTIF-189 — Notification text clearly describes event

**Priority:** P1

Expected:

User can understand what happened.

---

## TS-NOTIF-190 — Failed and successful financial notifications clearly distinguishable

**Priority:** P0

Expected:

No ambiguity.

---

## TS-NOTIF-191 — Notification controls keyboard accessible

**Priority:** P2

Expected:

Accessible interaction.

---

## TS-NOTIF-192 — Long notification content does not hide critical amount/status

**Priority:** P1

Expected:

Important information remains visible.

---

# 41. Responsive Scenarios

## TS-NOTIF-193 — Notification center on desktop

**Priority:** P2

Expected:

Readable.

---

## TS-NOTIF-194 — Notification center on tablet

**Priority:** P2

Expected:

Usable.

---

## TS-NOTIF-195 — Notification center on mobile

**Priority:** P1

Expected:

Amount, status, timestamp, and important context remain visible.

---

## TS-NOTIF-196 — Notification deep link from mobile view

**Priority:** P1

Expected:

Correct protected resource opens.

---

# 42. Cross-Browser Scenarios

## TS-NOTIF-197 — Notification center in Chrome

**Priority:** P1

Expected:

Works correctly.

---

## TS-NOTIF-198 — Notification center in Edge

**Priority:** P1

Expected:

Works correctly.

---

## TS-NOTIF-199 — Notification center in Firefox

**Priority:** P1

Expected:

Works correctly.

---

## TS-NOTIF-200 — Notification preference management across supported browsers

**Priority:** P2

Expected:

Consistent.

---

# 43. Boundary Scenarios

## TS-NOTIF-201 — Notification exactly at maximum content length

**Priority:** P2

Expected:

Displayed correctly.

---

## TS-NOTIF-202 — Notification content above maximum supported length

**Priority:** P2

Expected:

Safely truncated or rejected according to design without losing critical financial meaning.

---

## TS-NOTIF-203 — One unread notification

**Priority:** P2

Expected:

Counter shows `1`.

---

## TS-NOTIF-204 — Zero unread notifications

**Priority:** P2

Expected:

Counter shows zero or disappears according to UI design.

---

## TS-NOTIF-205 — Very large unread count

**Priority:** P2

Expected:

Counter remains usable.

---

# 44. End-to-End Notification Scenarios

## TS-NOTIF-206 — Transfer notification journey

**Priority:** P0

Flow:

```text
Login
→ Perform Valid Transfer
→ Verify Transfer Completes
→ Verify Notification Appears
→ Verify Amount
→ Verify Status
→ Verify Reference
→ Open Notification
→ Verify Correct Transaction
```

---

## TS-NOTIF-207 — Failed payment notification journey

**Priority:** P0

Flow:

```text
Attempt Payment With Insufficient Funds
→ Payment Fails
→ Verify Balance Unchanged
→ Verify Failure Notification
→ Verify No Success Notification
```

---

## TS-NOTIF-208 — Card freeze security notification journey

**Priority:** P0

Flow:

```text
Active Card
→ Freeze Card
→ Verify Card FROZEN
→ Verify Security Notification
→ Open Notification
→ Verify Correct Card
```

---

## TS-NOTIF-209 — Password-change security notification journey

**Priority:** P0

Flow:

```text
Login
→ Change Password
→ Verify Change Succeeds
→ Verify Security Notification
→ Logout
→ Verify Old Password Fails
```

---

## TS-NOTIF-210 — Notification preference journey

**Priority:** P1

Flow:

```text
Open Notification Settings
→ Disable Optional Category
→ Trigger Category Event
→ Verify Suppressed Channel
→ Re-enable Preference
→ Trigger Event Again
→ Verify Notification Delivered
```

---

## TS-NOTIF-211 — Notification delivery failure journey

**Priority:** P0

Flow:

```text
Simulate Notification Provider Failure
→ Complete Valid Financial Transaction
→ Verify Transaction Remains Successful
→ Verify Notification Failure Recorded
→ Restore Provider
→ Retry Delivery
→ Verify Notification Delivered Once
```

---

# 45. Critical Smoke Scenarios

Notification smoke coverage should include:

```text
TS-NOTIF-001 — View own notifications
TS-NOTIF-004 — New notification appears
TS-NOTIF-007 — Cannot access another customer's notification
TS-NOTIF-013 — Successful transfer notification
TS-NOTIF-019 — Failed transfer does not generate success notification
TS-NOTIF-034 — Card freeze notification
TS-NOTIF-048 — Password-change notification
TS-NOTIF-092 — New notification unread state
```

---

# 46. Critical Regression Scenarios

Always prioritize:

* Notification ownership
* Wrong-recipient prevention
* Successful transfer notifications
* Failed-transfer notification correctness
* Successful payment notifications
* Failed-payment notification correctness
* Card security events
* Account security events
* Password/security changes
* Amount/status correctness
* Duplicate prevention
* Required security alerts
* Notification preferences
* Read/unread state
* Deep-link authorization
* Sensitive-data masking
* UI/API/database consistency

---

# 47. Automation Candidates

Strong UI automation candidates:

* Notification-center loading
* Unread count
* Mark-as-read
* Mark-all-read
* Notification preferences
* Transfer notification validation
* Payment notification validation
* Card status notification validation
* Deep links
* Unauthorized access

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 48. API Automation Candidates

Postman and REST Assured should later cover:

* Get notifications
* Get notification by ID
* Unauthorized notification access
* Mark read/unread
* Notification preferences
* Category filters
* Pagination
* Notification event references
* Duplicate prevention

---

# 49. SQL Validation Candidates

Database testing should validate:

* Notification owner
* Notification type
* Related event/transaction ID
* Read status
* Created timestamp
* Delivery status
* Channel
* Preference state
* Duplicate-event prevention

---

# 50. Performance Testing Candidates

JMeter may later cover:

* Notification-center retrieval
* Large unread lists
* High-volume transaction notification generation
* Concurrent notification delivery
* Notification preference lookups

Performance load must not create duplicate or wrong-recipient alerts.

---

# 51. BDD Candidates

Example:

```gherkin
Feature: Transfer notifications

Scenario: Successful transfer generates correct notification
  Given the customer has sufficient balance
  And the beneficiary is active
  When the customer completes a valid transfer
  Then the transfer should be completed
  And the customer should receive a transfer notification
  And the notification amount should match the transaction
  And the notification status should indicate success
```

Failure example:

```gherkin
Scenario: Failed payment must not generate a success notification
  Given the customer's account has insufficient available balance
  When the customer attempts to pay a bill
  Then the payment should fail
  And the account balance should remain unchanged
  And no payment-success notification should be generated
```

---

# 52. Risk Traceability

Major related risks include:

```text
RISK-002 — Unauthorized customer data access
RISK-021 — Sensitive information exposure
RISK-034 — Notification sent for failed transaction as success
RISK-039 — API/database inconsistency
RISK-048 — UI reports false success
```

---

# 53. Notification Coverage Summary

This catalog covers:

* Notification center
* Ownership
* Authorization
* Transfers
* Payments
* Cards
* Accounts
* Profile/security events
* Loans
* Deposits
* Content correctness
* Wrong-recipient prevention
* Duplicate prevention
* Missing alerts
* Read/unread states
* Counters
* Dismissal/deletion
* Preferences
* Delivery channels
* Email
* SMS
* In-app notifications
* Deep links
* Provider failures
* Financial-operation independence
* Status consistency
* Timing
* Concurrency
* API behavior
* Database consistency
* Error handling
* Sensitive data
* Search/filtering
* Pagination
* Accessibility
* Responsive behavior
* Cross-browser behavior
* Boundaries
* End-to-end workflows

---

# 54. Final Notification Testing Principle

Notifications must reflect the authoritative banking event; they must never become a competing source of incorrect financial information.

For every critical notification, QA should be able to answer:

```text
Was the correct customer notified?

Was the notification triggered by a real successful or failed event?

Is the amount correct?

Is the currency correct?

Is the final status correct?

Is sensitive information masked?

Was the notification generated exactly once?

Were customer preferences respected?

Were mandatory security alerts still delivered?

Does the notification link to the correct protected resource?

Does notification failure leave the underlying financial transaction unchanged?

Do UI, API, and database notification states agree?
```

The most serious notification defect is not a missing cosmetic alert; it is a notification that tells the customer something financially or security-wise happened when the authoritative system state says otherwise.
