# Banking System — Notification Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Notifications                  |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Notification scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* In-app notifications
* Email notifications
* SMS notifications where supported
* Security notifications
* Financial notifications
* Transaction notifications
* Card notifications
* Loan notifications
* Deposit notifications
* Statement notifications
* Notification preferences
* Read/unread state
* Delivery timing
* Retry behavior
* Duplicate prevention
* False-success prevention
* Event consistency
* Authorization
* Customer isolation
* API validation
* Database validation
* Audit
* Security
* Responsive behavior
* Accessibility

---

# 3. Test Case ID Convention

Notification test cases use:

```text
NOT-TC-XXX
```

Examples:

```text
NOT-TC-001
NOT-TC-002
NOT-TC-003
```

---

# 4. Critical Notification Invariants

## Invariant 1 — Truthfulness

A notification must reflect the authoritative banking event.

```text
Successful Event
→ Success Notification

Failed Event
→ No False Success Notification
```

---

## Invariant 2 — Ownership

A customer must receive only notifications intended for that customer.

---

## Invariant 3 — No Duplicate User Impact

One banking event should not unintentionally create repeated identical notifications.

---

## Invariant 4 — Security Events

Critical security events should generate required notifications according to policy.

Examples:

```text
Password changed

MFA changed

Account locked

Suspicious login

Card blocked

Sensitive profile change
```

---

## Invariant 5 — Sensitive Data Protection

Notifications must not expose unnecessary:

```text
Passwords

OTP values

Full card numbers

Authentication tokens

Sensitive internal data
```

---

# 5. Common Test Data

## Customer A

```text
Customer:
CUST-001

Email:
customer1@banktest.local

Status:
ACTIVE
```

## Customer B

```text
Customer:
CUST-002

Email:
customer2@banktest.local

Status:
ACTIVE
```

## Notification Channels

```text
IN_APP

EMAIL

SMS
```

where supported.

## Sample Preferences

```text
Transaction Notifications:
Enabled

Security Notifications:
Enabled

Marketing Notifications:
Disabled
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer is authenticated.

Notification service is available.

Required synthetic banking event can be triggered.

Test email/SMS channels are used.

API/DB validation is available where required.
```

---

# 7. Notification List Test Cases

## NOT-TC-001 — View Own Notifications

**Priority:** P0
**Requirement:** REQ-NOT-001
**Automation:** Playwright / API

### Steps

1. Login as `CUST-001`.
2. Open Notifications.

### Expected Result

Only notifications belonging to `CUST-001` are displayed.

---

## NOT-TC-002 — Customer With No Notifications

**Priority:** P2

### Expected Result

Clear empty state is displayed.

---

## NOT-TC-003 — Multiple Notification Types

**Priority:** P1

### Expected Result

Notifications from supported categories display correctly:

* Transactions
* Payments
* Cards
* Loans
* Deposits
* Security
* Statements
* Profile/account changes

---

# 8. Notification Ownership / IDOR

## NOT-TC-004 — View Another Customer's Notification

**Priority:** P0
**Risk:** RISK-002, RISK-047

### Expected Result

Access denied.

---

## NOT-TC-005 — Manipulate Notification ID

**Priority:** P0

Expected: unauthorized notification inaccessible.

---

## NOT-TC-006 — Fetch Another Customer's Notification via API

**Priority:** P0
**Automation:** REST Assured

Expected: denied.

---

## NOT-TC-007 — Mark Another Customer's Notification as Read

**Priority:** P0

Expected: denied.

Customer B notification remains unchanged.

---

# 9. Successful Transfer Notification

## NOT-TC-008 — Transfer Success Notification

**Priority:** P0
**Requirement:** REQ-NOT-002

### Preconditions

Valid completed transfer exists.

### Expected Result

Notification reflects:

```text
Correct customer

Correct transaction type

Correct amount

Correct status

Correct timestamp

Correct reference where appropriate
```

---

## NOT-TC-009 — Transfer Notification Amount Matches Transaction

**Priority:** P0

Expected: notification amount equals authoritative transfer amount.

---

## NOT-TC-010 — Transfer Notification Fee Information

**Priority:** P1

Expected: fee shown only where product design requires it and is accurate.

---

# 10. Failed Transfer Notification

## NOT-TC-011 — Failed Transfer Notification

**Priority:** P1

Expected: customer receives failure notification if configured.

---

## NOT-TC-012 — Failed Transfer Must Not Send Success Notification

**Priority:** P0
**Risk:** RISK-034

Expected:

```text
Success Notification:
No
```

---

## NOT-TC-013 — Cancelled Scheduled Transfer Must Not Send Completion Notification

**Priority:** P0

Expected: no false transfer-success notification.

---

# 11. Payment Notification Test Cases

## NOT-TC-014 — Successful Payment Notification

**Priority:** P0

Expected: payment details/status correct.

---

## NOT-TC-015 — Failed Payment Notification

**Priority:** P1

Expected: failure reflected accurately.

---

## NOT-TC-016 — Provider Failure Must Not Generate Success Notification

**Priority:** P0
**Risk:** RISK-034

Expected: authoritative provider/local failure reflected.

---

## NOT-TC-017 — Payment Refund Notification

**Priority:** P1

Expected: correct refund amount/reference.

---

# 12. Card Notification Test Cases

## NOT-TC-018 — Card Freeze Notification

**Priority:** P1

Expected: customer notified according to security policy.

---

## NOT-TC-019 — Card Unfreeze Notification

**Priority:** P1

Expected: correct state.

---

## NOT-TC-020 — Permanent Card Block Notification

**Priority:** P0

Expected: critical security notification sent where required.

---

## NOT-TC-021 — Card Replacement Notification

**Priority:** P1

Expected: correct card event without exposing sensitive data.

---

## NOT-TC-022 — Card Purchase Notification

**Priority:** P1

Expected:

* Correct merchant where appropriate
* Correct amount
* Correct status

---

## NOT-TC-023 — Declined Card Transaction

**Priority:** P1

Expected: decline must not appear as successful purchase.

---

# 13. Loan Notification Test Cases

## NOT-TC-024 — Loan Application Submitted

**Priority:** P2

Expected: correct application reference/status.

---

## NOT-TC-025 — Loan Approved

**Priority:** P1

Expected: notification matches authoritative approval state.

---

## NOT-TC-026 — Loan Rejected

**Priority:** P1

Expected: notification accurately reflects rejection.

---

## NOT-TC-027 — Loan Disbursed

**Priority:** P0

Expected: correct disbursement amount/reference.

---

## NOT-TC-028 — Loan Repayment Success

**Priority:** P1

Expected: correct repayment amount.

---

## NOT-TC-029 — Failed Loan Repayment

**Priority:** P0

Expected: no repayment-success notification.

---

## NOT-TC-030 — Loan Overdue Notification

**Priority:** P1

Expected: accurate due date/amount/status.

---

# 14. Deposit Notification Test Cases

## NOT-TC-031 — Deposit Opened

**Priority:** P1

Expected: correct principal/product.

---

## NOT-TC-032 — Deposit Approaching Maturity

**Priority:** P1

Expected: correct maturity date.

---

## NOT-TC-033 — Deposit Matured

**Priority:** P1

Expected: authoritative maturity state.

---

## NOT-TC-034 — Maturity Payout Notification

**Priority:** P0

Expected: payout amount correct.

---

## NOT-TC-035 — Early Withdrawal Notification

**Priority:** P1

Expected: penalty and payout reflect actual transaction.

---

## NOT-TC-036 — Deposit Renewal Notification

**Priority:** P1

Expected: renewed principal/term/rate accurate.

---

# 15. Security Notification Test Cases

## NOT-TC-037 — Password Changed Notification

**Priority:** P0
**Requirement:** REQ-NOT-003

Expected: security alert sent according to policy.

---

## NOT-TC-038 — Password Reset Notification

**Priority:** P0

Expected: notification confirms event without exposing reset token/password.

---

## NOT-TC-039 — MFA Enabled Notification

**Priority:** P0

Expected: security event accurately communicated.

---

## NOT-TC-040 — MFA Disabled Notification

**Priority:** P0

Expected: security alert generated.

---

## NOT-TC-041 — Account Lockout Notification

**Priority:** P1

Expected: correct security information.

---

## NOT-TC-042 — Suspicious/New Login Notification

**Priority:** P1

Where supported.

Expected: customer receives relevant login context without exposing secrets.

---

# 16. Profile / Account Security Notifications

## NOT-TC-043 — Email Changed Notification

**Priority:** P0

Expected: change communicated according to policy.

---

## NOT-TC-044 — Phone Changed Notification

**Priority:** P1

Expected: correct.

---

## NOT-TC-045 — Account Frozen Notification

**Priority:** P0

Expected: reflects actual state.

---

## NOT-TC-046 — Account Unfrozen Notification

**Priority:** P1

Expected: reflects actual state.

---

## NOT-TC-047 — Customer Status Changed Notification

**Priority:** P1

Expected: generated when required.

---

# 17. Statement Notification Test Cases

## NOT-TC-048 — Statement Ready Notification

**Priority:** P2

Expected: customer receives correct statement-period reference.

---

## NOT-TC-049 — Statement Notification Link Authorization

**Priority:** P0

Expected: link does not bypass statement authentication/ownership controls.

---

# 18. Preference Test Cases

## NOT-TC-050 — View Notification Preferences

**Priority:** P1
**Requirement:** REQ-NOT-004

Expected: current settings displayed correctly.

---

## NOT-TC-051 — Disable Optional Transaction Email Notification

**Priority:** P1

Expected: optional email delivery stops according to policy.

---

## NOT-TC-052 — Enable Optional Transaction Email Notification

**Priority:** P1

Expected: future eligible events delivered.

---

## NOT-TC-053 — Disable Marketing Notifications

**Priority:** P2

Expected: marketing delivery stops.

---

## NOT-TC-054 — Security Notification Cannot Be Disabled Where Mandatory

**Priority:** P0

Expected: customer cannot disable mandatory security alerts.

---

## NOT-TC-055 — Preference Persists

**Priority:** P1

Expected: setting survives logout/login and matches backend.

---

# 19. Preference Authorization Test Cases

## NOT-TC-056 — Customer Changes Own Preferences

**Priority:** P1

Expected: allowed.

---

## NOT-TC-057 — Customer Changes Another Customer's Preferences

**Priority:** P0

Expected: denied.

---

## NOT-TC-058 — Manipulate Preference Owner ID

**Priority:** P0

Expected: backend authenticated identity remains authoritative.

---

# 20. Channel Test Cases

## NOT-TC-059 — In-App Notification

**Priority:** P0

Expected: event appears in correct customer's in-app list.

---

## NOT-TC-060 — Email Notification

**Priority:** P1

Expected: delivered to correct test email address when enabled.

---

## NOT-TC-061 — SMS Notification

**Priority:** P1

Where supported.

Expected: delivered to correct verified number.

---

## NOT-TC-062 — Multi-Channel Notification

**Priority:** P1

Expected: configured channels each receive one appropriate notification.

---

# 21. Channel Failure Test Cases

## NOT-TC-063 — Email Delivery Failure

**Priority:** P1

Expected:

* Banking event remains correctly completed.
* Notification delivery records failure/retry state.
* Event is not rolled back incorrectly.

---

## NOT-TC-064 — SMS Delivery Failure

**Priority:** P1

Expected: safe delivery failure handling.

---

## NOT-TC-065 — In-App Notification Service Failure

**Priority:** P1

Expected: banking transaction remains authoritative and notification can be recovered/retried according to design.

---

# 22. Duplicate Notification Test Cases

## NOT-TC-066 — Duplicate Event Delivery

**Priority:** P0
**Requirement:** REQ-NOT-005

### Steps

Publish/process same event twice.

### Expected Result

Customer should not receive unintended duplicate identical notifications.

---

## NOT-TC-067 — Duplicate Provider Callback

**Priority:** P0

Expected: one payment notification for one payment outcome.

---

## NOT-TC-068 — Transfer Retry Resolves to Existing Transaction

**Priority:** P0

Expected: no duplicate success notification.

---

## NOT-TC-069 — Loan Disbursement Retry

**Priority:** P0

Expected: one disbursement notification.

---

## NOT-TC-070 — Deposit Maturity Job Retries

**Priority:** P0

Expected: one maturity/payout notification for one payout event.

---

# 23. Event Correlation Test Cases

## NOT-TC-071 — Notification Contains Correct Transaction Reference

**Priority:** P1

Expected: correlation to authoritative event is correct.

---

## NOT-TC-072 — Wrong Event Reference Rejected/Detected

**Priority:** P0

Expected: notification must not link one customer's message to another financial event.

---

# 24. Notification Timing Test Cases

## NOT-TC-073 — Notification After Event Completion

**Priority:** P0
**Requirement:** REQ-NOT-006

Expected: success notification is not sent before authoritative success is established where finality is required.

---

## NOT-TC-074 — Pending Event Notification

**Priority:** P1

Where supported.

Expected: clearly indicates pending/nonfinal state.

---

## NOT-TC-075 — Delayed Notification

**Priority:** P1

Expected: delayed delivery still reflects correct event and does not create duplicate user action.

---

## NOT-TC-076 — Out-of-Order Notifications

**Priority:** P0

Example:

```text
Transfer Started

Transfer Completed
```

arrive out of order.

Expected: user-facing state does not become misleading.

---

# 25. Read / Unread Test Cases

## NOT-TC-077 — New Notification Is Unread

**Priority:** P1
**Requirement:** REQ-NOT-007

Expected: unread state correct.

---

## NOT-TC-078 — Mark Notification as Read

**Priority:** P1

Expected: state changes to read.

---

## NOT-TC-079 — Refresh After Marking Read

**Priority:** P1

Expected: read state persists.

---

## NOT-TC-080 — Mark All as Read

**Priority:** P2

Expected: owned unread notifications become read.

---

## NOT-TC-081 — Read State Across Devices/Sessions

**Priority:** P2

Expected: backend-authoritative state synchronizes.

---

# 26. Notification Count Test Cases

## NOT-TC-082 — Unread Badge Count

**Priority:** P1

Expected: equals number of unread notifications according to product design.

---

## NOT-TC-083 — Badge Decrements After Read

**Priority:** P1

Expected: count updates correctly.

---

## NOT-TC-084 — New Notification Increments Badge

**Priority:** P1

Expected: count increments exactly once.

---

## NOT-TC-085 — Duplicate Event Does Not Inflate Badge

**Priority:** P1

Expected: no false unread count increase from duplicate processing.

---

# 27. Notification Ordering

## NOT-TC-086 — Newest First

**Priority:** P2

Expected: correct chronological ordering.

---

## NOT-TC-087 — Equal Timestamp Ordering

**Priority:** P2

Expected: deterministic secondary ordering.

---

# 28. Pagination Test Cases

## NOT-TC-088 — First Notification Page

**Priority:** P2

Expected: configured page size.

---

## NOT-TC-089 — Next Page

**Priority:** P2

Expected: correct continuation.

---

## NOT-TC-090 — No Duplicates Across Pages

**Priority:** P1

Expected: stable dataset has no duplicate notification rows.

---

## NOT-TC-091 — No Missing Notifications Across Pages

**Priority:** P1

Expected: all expected notifications retrievable.

---

# 29. API Notification Test Cases

## NOT-TC-092 — Get Own Notifications API

**Priority:** P0
**Automation:** REST Assured

Expected: authorized list returned.

---

## NOT-TC-093 — Get Another Customer Notification API

**Priority:** P0

Expected: denied.

---

## NOT-TC-094 — Mark Own Notification Read API

**Priority:** P1

Expected: succeeds.

---

## NOT-TC-095 — Mark Another Customer Notification Read API

**Priority:** P0

Expected: denied.

---

## NOT-TC-096 — Unauthenticated Notification API

**Priority:** P0

Expected: denied.

---

## NOT-TC-097 — Invalid Notification ID

**Priority:** P2

Expected: safe error.

---

# 30. Database Validation Test Cases

## NOT-TC-098 — Notification Ownership

**Priority:** P0
**Automation:** SQL

Expected: notification references correct customer.

---

## NOT-TC-099 — Event Reference Persistence

**Priority:** P0

Expected: notification correlates to correct source event.

---

## NOT-TC-100 — Notification Type Persistence

**Priority:** P1

Expected: correct type/category stored.

---

## NOT-TC-101 — Delivery State Persistence

**Priority:** P1

Expected: channel delivery state accurate.

---

## NOT-TC-102 — Read State Persistence

**Priority:** P1

Expected: correct read/unread state.

---

## NOT-TC-103 — Duplicate Event Deduplication

**Priority:** P0

Expected: deduplication/event identity prevents unintended duplicate records where designed.

---

# 31. UI/API/Database Consistency

## NOT-TC-104 — Notification Cross-Layer Validation

**Priority:** P0
**Risk:** RISK-039, RISK-048

Validate:

```text
UI Type
=
API Type
=
DB Type
```

```text
UI Event Reference
=
API Event Reference
=
DB Event Reference
```

```text
UI Read State
=
API Read State
=
DB Read State
```

---

# 32. Financial Event Consistency

## NOT-TC-105 — Transfer Notification vs Transfer Record

**Priority:** P0

Expected: amount/status/reference match.

---

## NOT-TC-106 — Payment Notification vs Payment Record

**Priority:** P0

Expected: matches.

---

## NOT-TC-107 — Card Notification vs Card Transaction

**Priority:** P0

Expected: matches.

---

## NOT-TC-108 — Loan Notification vs Loan State

**Priority:** P0

Expected: matches.

---

## NOT-TC-109 — Deposit Notification vs Deposit State

**Priority:** P0

Expected: matches.

---

# 33. False-Success Prevention

## NOT-TC-110 — UI Success Notification After Backend Failure

**Priority:** P0
**Risk:** RISK-034, RISK-048

Expected: forbidden.

---

## NOT-TC-111 — Success Toast While Persistent Notification Says Failed

**Priority:** P0

Expected: channels must converge on authoritative result.

---

## NOT-TC-112 — Provider Failure With Local Success Event

**Priority:** P0

Expected: notification waits for/reconciles authoritative outcome according to design.

---

# 34. Sensitive Data Test Cases

## NOT-TC-113 — No Password in Notification

**Priority:** P0
**Risk:** RISK-021

Expected: absent.

---

## NOT-TC-114 — No OTP in General Notification History

**Priority:** P0

Expected: OTP not exposed beyond dedicated secure delivery mechanism.

---

## NOT-TC-115 — Card Number Masking

**Priority:** P0

Expected: full PAN not exposed.

---

## NOT-TC-116 — No Access Token in Notification Link

**Priority:** P0

Expected: reusable authentication secrets absent from URL/content.

---

## NOT-TC-117 — Email/SMS Contains Minimum Necessary Data

**Priority:** P1

Expected: no unnecessary account/customer-sensitive fields.

---

# 35. Notification Link Security

## NOT-TC-118 — Transaction Notification Link

**Priority:** P0

Expected: opens only authorized resource.

---

## NOT-TC-119 — Link After Logout

**Priority:** P0

Expected: authentication required.

---

## NOT-TC-120 — Link Shared With Another Customer

**Priority:** P0

Expected: recipient cannot access protected originating resource.

---

## NOT-TC-121 — Tampered Notification Link Identifier

**Priority:** P0

Expected: ownership/authorization enforced.

---

# 36. Concurrency Test Cases

## NOT-TC-122 — Same Event Processed Concurrently

**Priority:** P0
**Risk:** RISK-013

Expected: no unintended duplicate notifications.

---

## NOT-TC-123 — Mark Read Concurrently From Two Devices

**Priority:** P2

Expected: consistent final `READ` state.

---

## NOT-TC-124 — Notification Preference Change During Event Processing

**Priority:** P1

Expected: behavior follows documented event/preference snapshot semantics.

---

# 37. Retry Test Cases

## NOT-TC-125 — Email Retry After Temporary Failure

**Priority:** P1

Expected: eventual one successful delivery without duplicate flood.

---

## NOT-TC-126 — SMS Retry

**Priority:** P1

Expected: safe retry.

---

## NOT-TC-127 — In-App Persistence Retry

**Priority:** P1

Expected: notification eventually stored once.

---

# 38. Timezone Test Cases

## NOT-TC-128 — Notification Timestamp

**Priority:** P1
**Risk:** RISK-043

Expected: displayed according to product timezone rules.

---

## NOT-TC-129 — Midnight Event

**Priority:** P2

Expected: correct date shown.

---

## NOT-TC-130 — Scheduled Event Notification

**Priority:** P1

Expected: notification corresponds to actual scheduled execution time.

---

# 39. Audit Test Cases

## NOT-TC-131 — Mandatory Security Notification Audit

**Priority:** P1
**Risk:** RISK-022

Expected: source event and notification generation traceable where required.

---

## NOT-TC-132 — Delivery Failure Audit/Operational Record

**Priority:** P1

Expected: channel failure/retry traceable.

---

## NOT-TC-133 — No Secrets in Notification Logs

**Priority:** P0
**Risk:** RISK-032

Expected: password, OTP, token, CVV absent from general logs.

---

# 40. Error Handling

## NOT-TC-134 — Notification Service Unavailable During Transfer

**Priority:** P0

### Expected Result

Transfer result remains authoritative.

Notification failure must not roll back or duplicate transfer.

---

## NOT-TC-135 — Notification API Unavailable

**Priority:** P1

Expected: notification screen shows safe error rather than falsely empty history.

---

## NOT-TC-136 — Partial Channel Failure

**Priority:** P1

Example:

```text
In-app:
Delivered

Email:
Failed
```

Expected: each channel state represented/retried independently.

---

# 41. Cross-Browser Test Cases

## NOT-TC-137 — Notifications in Chrome

**Priority:** P2

Expected: works.

---

## NOT-TC-138 — Notifications in Edge

**Priority:** P2

Expected: works.

---

## NOT-TC-139 — Notifications in Firefox

**Priority:** P2

Expected: works.

---

## NOT-TC-140 — Notifications in WebKit

**Priority:** P2

Expected: works.

---

# 42. Responsive Test Cases

## NOT-TC-141 — Notification List at 390×844

**Priority:** P2

Expected:

* Type readable.
* Amount/status readable.
* Timestamp readable.
* Links/actions accessible.

---

## NOT-TC-142 — Notification Detail at 360×800

**Priority:** P2

Expected: content not clipped.

---

## NOT-TC-143 — Long Notification Text

**Priority:** P2

Expected: wraps without hiding critical status or amount.

---

# 43. Accessibility Test Cases

## NOT-TC-144 — Keyboard Notification Navigation

**Priority:** P2

Expected: notifications/actions keyboard accessible.

---

## NOT-TC-145 — Unread State Not Communicated by Color Alone

**Priority:** P2

Expected: semantic/readable state.

---

## NOT-TC-146 — Notification Severity/Status Accessible

**Priority:** P2

Expected: success/failure/security meaning communicated semantically.

---

# 44. End-to-End Successful Transfer Notification

## NOT-TC-147 — Transfer → Notification → Transaction Reconciliation

**Priority:** P0

### Test Data

```text
Transfer:
1,000.00

Fee:
10.00
```

### Steps

1. Execute valid transfer.
2. Capture transfer reference.
3. Open Notifications.
4. Inspect in-app notification.
5. Inspect enabled email/SMS channels.
6. Compare transaction API/database.

### Expected Result

Notification reflects exactly one successful transfer and correct event data.

---

# 45. End-to-End Failed Payment Notification

## NOT-TC-148 — Failed Payment Must Not Produce False Success

**Priority:** P0

### Steps

1. Trigger provider/local failed payment.
2. Inspect UI toast.
3. Inspect notification center.
4. Inspect email/SMS.
5. Inspect payment record.

### Expected Result

```text
Authoritative Payment:
FAILED

Success Notifications:
0

Failure Notification:
According to configured policy
```

---

# 46. End-to-End Security Notification

## NOT-TC-149 — Password Change Alert

**Priority:** P0

### Steps

1. Change password successfully.
2. Inspect notification channels.
3. Review notification content.
4. Verify no password/token is exposed.

### Expected Result

Security alert correctly identifies the event without exposing credentials.

---

# 47. End-to-End Duplicate Prevention

## NOT-TC-150 — Repeated Event Delivery

**Priority:** P0

### Steps

1. Create one completed transaction.
2. Deliver same event multiple times.
3. Retry notification worker.
4. Inspect all notification channels.

### Expected Result

No unintended duplicate notification flood.

---

# 48. End-to-End Customer Isolation

## NOT-TC-151 — Customer A Cannot Access Customer B Notification

**Priority:** P0

Test through:

```text
Notification list

Direct ID

API

Notification link

Mark-read endpoint
```

### Expected Result

Every cross-customer attempt denied.

---

# 49. End-to-End Channel Failure

## NOT-TC-152 — Banking Event Succeeds While Email Fails

**Priority:** P0

### Steps

1. Execute valid transfer.
2. Simulate email provider failure.
3. Verify transfer completed.
4. Verify in-app notification.
5. Inspect email delivery state/retry.

### Expected Result

Financial operation remains correct and email retry does not create another transfer or duplicate financial event.

---

# 50. End-to-End Read State

## NOT-TC-153 — Unread → Read Across Sessions

**Priority:** P1

### Steps

1. Receive notification.
2. Confirm unread.
3. Mark read.
4. Logout.
5. Login again.
6. Open notifications.

### Expected Result

Read state persists.

---

# 51. Notification Risk Mapping

| Risk                                  | Related Test Cases                          |
| ------------------------------------- | ------------------------------------------- |
| RISK-002 Unauthorized customer data   | NOT-TC-004–007, 093–096, 151                |
| RISK-013 Concurrency                  | NOT-TC-066–070, 122–127, 150                |
| RISK-021 Sensitive exposure           | NOT-TC-113–121, 133                         |
| RISK-022 Audit gap                    | NOT-TC-131–133                              |
| RISK-030 Unauthorized API             | NOT-TC-092–097                              |
| RISK-032 Sensitive audit/log exposure | NOT-TC-133                                  |
| RISK-034 False notification           | NOT-TC-011–017, 023, 029, 080, 105–112, 148 |
| RISK-039 API/DB inconsistency         | NOT-TC-098–109                              |
| RISK-043 Timezone issues              | NOT-TC-128–130                              |
| RISK-047 IDOR                         | NOT-TC-004–007, 093–095, 118–121, 151       |
| RISK-048 UI/backend mismatch          | NOT-TC-104–112                              |

---

# 52. Requirements Mapping

| Requirement                           | Test Cases              |
| ------------------------------------- | ----------------------- |
| REQ-NOT-001 View own notifications    | NOT-TC-001–007          |
| REQ-NOT-002 Financial notifications   | NOT-TC-008–036          |
| REQ-NOT-003 Security notifications    | NOT-TC-037–049          |
| REQ-NOT-004 Preferences/channels      | NOT-TC-050–065          |
| REQ-NOT-005 Duplicate prevention      | NOT-TC-066–072          |
| REQ-NOT-006 Timing/event consistency  | NOT-TC-073–076, 105–112 |
| REQ-NOT-007 Read/unread lifecycle     | NOT-TC-077–091          |
| REQ-NOT-008 API/persistence           | NOT-TC-092–109          |
| REQ-NOT-009 Sensitive-data protection | NOT-TC-113–121          |
| REQ-NOT-010 Retry/reliability         | NOT-TC-122–136          |

---

# 53. Smoke Candidates

Recommended notification smoke coverage:

```text
NOT-TC-001
NOT-TC-004
NOT-TC-008
NOT-TC-012
NOT-TC-014
NOT-TC-020
NOT-TC-037
NOT-TC-050
NOT-TC-059
NOT-TC-077
NOT-TC-092
NOT-TC-105
```

---

# 54. Sanity Candidates

After notification changes:

```text
NOT-TC-001
NOT-TC-008
NOT-TC-012
NOT-TC-014
NOT-TC-020
NOT-TC-037
NOT-TC-050
NOT-TC-054
NOT-TC-059
NOT-TC-066
NOT-TC-073
NOT-TC-077
NOT-TC-082
NOT-TC-098
NOT-TC-104
```

---

# 55. Critical Regression Candidates

```text
NOT-TC-001–017

NOT-TC-018–076

NOT-TC-077–136

NOT-TC-141–153
```

---

# 56. UI Automation Candidates

Best suited for:

```text
Playwright

Selenium

Cypress
```

Strong candidates:

```text
NOT-TC-001–005

NOT-TC-008–061

NOT-TC-073–091

NOT-TC-118–121

NOT-TC-135–153
```

---

# 57. API Automation Candidates

Best suited for:

```text
REST Assured

Postman
```

Strong candidates:

```text
NOT-TC-004–007

NOT-TC-050–136

NOT-TC-147–153
```

---

# 58. SQL / Database Testing Candidates

Strong SQL candidates:

```text
NOT-TC-066–091

NOT-TC-098–109

NOT-TC-122–133

NOT-TC-147–153
```

Database validation should verify:

```text
Notification ownership

Event reference

Notification type

Channel

Delivery state

Read state

Created timestamp

Delivered timestamp

Deduplication key

Retry count

Failure state
```

---

# 59. Performance / Reliability Candidates

Strong candidates:

```text
NOT-TC-066
NOT-TC-070
NOT-TC-075
NOT-TC-076
NOT-TC-090
NOT-TC-091
NOT-TC-122
NOT-TC-125–127
NOT-TC-150
```

Later JMeter/event testing should measure:

```text
Notification throughput

Delivery latency

Retry volume

Duplicate rate

Failure rate
```

while ensuring banking events remain correct.

---

# 60. Test Evidence Requirements

For critical notification tests, capture as applicable:

```text
Customer ID

Notification ID

Source event ID

Transaction/payment/card/loan/deposit reference

Notification type

Channel

Expected status

Actual status

Event timestamp

Notification timestamp

Delivery state

Read state

API response

Database row

Test email/SMS evidence

Screenshot

Defect ID
```

Never capture real passwords, OTPs, CVVs, or reusable tokens.

---

# 61. Notification Defect Examples

Potential Critical/High defects include:

```text
Customer receives another customer's notification.

Failed transfer sends success notification.

Failed payment sends success email.

Duplicate provider callback sends duplicate payment alerts.

Loan disbursement notification amount is wrong.

Deposit maturity notification fires although payout failed.

Password-change alert exposes sensitive data.

Notification link allows unauthorized statement access.

Security notification can be disabled despite mandatory policy.

Read state of another customer can be modified.

UI notification says success while backend transaction failed.

Email retry causes repeated notification flood.
```

---

# 62. Notification Release Blockers

Release should normally be blocked by unresolved issues involving:

```text
Cross-customer notification exposure

False financial success notification

Critical security alert missing where mandatory

Sensitive authentication/card data exposure

Notification link authorization bypass

Systematic duplicate critical notifications

Notification event mapped to wrong customer

Critical notification state contradicting authoritative financial state
```

---

# 63. Notification Exit Criteria

Notification testing is acceptable when:

```text
Customers receive only their own notifications.

Financial notifications match authoritative events.

Failed operations never generate false success notifications.

Critical security events generate required alerts.

Preferences work correctly.

Mandatory security alerts cannot be disabled where required.

Channels follow configuration.

Duplicate processing does not create notification floods.

Read/unread state persists correctly.

Notification links preserve authorization.

Sensitive data is protected.

UI/API/DB/event state agrees.

Notification service failures do not corrupt banking transactions.

No unresolved Critical/P0 notification defect remains.
```

---

# 64. Final Notification Testing Principle

A notification is not simply:

```text
A message displayed to the customer.
```

In a banking system, customers may use notifications to decide whether:

```text
Money moved.

A payment succeeded.

A card was used.

A password changed.

A loan was approved.

A deposit matured.

An account may be compromised.
```

Because of that, a notification must be:

```text
Accurate

Authorized

Timely

Traceable

Non-duplicated

Secure
```

The most important notification invariant is:

```text
The notification must never claim
that a financial or security event occurred
unless the authoritative Banking System state
supports that claim.
```

The core rule is:

```text
Notifications must faithfully communicate
banking reality without exposing another customer's data,
leaking sensitive information,
or creating misleading duplicate or false-success messages.
```
