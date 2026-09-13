# Banking System — Security Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Security                       |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level security-focused manual test scenarios for the Banking System.

The objective is to verify that the application protects:

* Customer identities
* Authentication credentials
* Sessions
* Account data
* Financial transactions
* Personal information
* Administrative functions
* API resources
* Sensitive banking data

Security testing in this project focuses on defensive validation and authorized test-environment behavior.

---

# 3. Scope

Security testing includes:

* Authentication controls
* Authorization
* Object ownership
* Privilege separation
* Session management
* Password security
* MFA
* OTP handling
* Password-reset security
* Sensitive-data protection
* Input validation
* Injection resistance
* Browser security controls
* Cross-site request protection
* Rate limiting
* API authorization
* Replay protection
* Financial-operation security
* Audit logging
* Security notifications
* Admin security
* Information disclosure
* Error handling

---

# 4. Scenario Naming Convention

Security scenarios use:

```text
TS-SEC-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Authentication Security Scenarios

## TS-SEC-001 — Valid customer credentials authenticate successfully

**Priority:** P0

Expected:

Only valid credentials allow access.

---

## TS-SEC-002 — Invalid password rejected

**Priority:** P0

Expected:

Authentication fails without revealing sensitive credential information.

---

## TS-SEC-003 — Nonexistent username/email rejected safely

**Priority:** P1

Expected:

Response does not unnecessarily reveal whether the account exists.

---

## TS-SEC-004 — Disabled customer cannot authenticate

**Priority:** P0

Expected:

Access denied.

---

## TS-SEC-005 — Suspended customer authentication follows security policy

**Priority:** P0

Expected:

Restricted access enforced.

---

## TS-SEC-006 — Locked customer cannot authenticate with correct password

**Priority:** P0

Expected:

Lockout is enforced server-side.

---

## TS-SEC-007 — Customer login cannot authenticate as administrator

**Priority:** P0

Expected:

Role remains authoritative.

---

# 6. User Enumeration Scenarios

## TS-SEC-008 — Login error behavior for valid and invalid usernames

**Priority:** P1

Expected:

Responses should not unnecessarily disclose account existence.

---

## TS-SEC-009 — Forgot-password behavior for registered and unregistered email

**Priority:** P1

Expected:

Response does not expose whether the email exists beyond intended policy.

---

## TS-SEC-010 — Registration duplicate-account validation does not expose unnecessary customer data

**Priority:** P1

Expected:

Only required validation information is returned.

---

# 7. Brute-Force and Lockout Scenarios

## TS-SEC-011 — Repeated invalid passwords trigger configured protection

**Priority:** P0

Expected:

Lockout, throttling, or equivalent control activates according to policy.

---

## TS-SEC-012 — Attempts below lockout threshold

**Priority:** P1

Expected:

Customer remains usable according to configured threshold.

---

## TS-SEC-013 — Exact lockout threshold

**Priority:** P0

Expected:

Configured protection activates correctly.

---

## TS-SEC-014 — Attempts above lockout threshold

**Priority:** P0

Expected:

Additional attempts remain blocked/throttled.

---

## TS-SEC-015 — Distributed repeated failures against same account

**Priority:** P0

Expected:

Protection is not bypassed merely by changing browser session.

---

## TS-SEC-016 — Successful login resets failure counter where required

**Priority:** P1

Expected:

Counter behavior follows policy.

---

# 8. Password Security Scenarios

## TS-SEC-017 — Weak password rejected

**Priority:** P1

Expected:

Password policy enforced.

---

## TS-SEC-018 — Password below minimum length rejected

**Priority:** P1

Expected:

Rejected.

---

## TS-SEC-019 — Password exactly at minimum valid boundary

**Priority:** P1

Expected:

Accepted if all other rules pass.

---

## TS-SEC-020 — Password reuse rejected where history policy exists

**Priority:** P1

Expected:

Previously prohibited password cannot be reused.

---

## TS-SEC-021 — Password never displayed in profile/API response

**Priority:** P0

Expected:

Credential is never returned.

---

## TS-SEC-022 — Password not exposed in URL

**Priority:** P0

Expected:

No password in query string/path.

---

## TS-SEC-023 — Password not exposed in application logs available to normal operators

**Priority:** P0

Expected:

Credential redacted or omitted.

---

## TS-SEC-024 — Password field masks input

**Priority:** P2

Expected:

Password not visible by default.

---

# 9. Password Change Security

## TS-SEC-025 — Password change requires correct current password

**Priority:** P0

Expected:

Unauthorized change prevented.

---

## TS-SEC-026 — Password change invalidates old password

**Priority:** P0

Expected:

Old password no longer authenticates.

---

## TS-SEC-027 — Password change affects existing sessions according to policy

**Priority:** P0

Expected:

Other sessions cannot improperly bypass new security state.

---

## TS-SEC-028 — Password-change notification generated

**Priority:** P0

Expected:

Security alert is produced.

---

# 10. Password Reset Security

## TS-SEC-029 — Valid reset token allows password reset

**Priority:** P0

Expected:

Valid controlled reset succeeds.

---

## TS-SEC-030 — Expired reset token rejected

**Priority:** P0

Expected:

Cannot reset password.

---

## TS-SEC-031 — Already-used reset token rejected

**Priority:** P0

Expected:

Single-use behavior enforced.

---

## TS-SEC-032 — Invalid/malformed reset token rejected

**Priority:** P0

Expected:

No reset.

---

## TS-SEC-033 — Older reset token invalidated after newer reset request where required

**Priority:** P0

Expected:

Only valid latest reset state accepted.

---

## TS-SEC-034 — Reset token not exposed in logs

**Priority:** P0

Expected:

Sensitive token protected.

---

## TS-SEC-035 — Reset token not exposed to another customer

**Priority:** P0

Expected:

Strict ownership maintained.

---

# 11. MFA Security Scenarios

## TS-SEC-036 — Valid MFA code allows authentication

**Priority:** P0

Expected:

Challenge succeeds.

---

## TS-SEC-037 — Invalid MFA code rejected

**Priority:** P0

Expected:

Access denied.

---

## TS-SEC-038 — Expired MFA code rejected

**Priority:** P0

Expected:

Access denied.

---

## TS-SEC-039 — Reused MFA code rejected

**Priority:** P0

Expected:

One-time behavior enforced.

---

## TS-SEC-040 — Previous MFA code invalid after resend where required

**Priority:** P0

Expected:

Old challenge cannot be reused.

---

## TS-SEC-041 — MFA cannot be bypassed by direct navigation to protected page

**Priority:** P0

Expected:

Authentication remains incomplete until MFA succeeds.

---

## TS-SEC-042 — MFA cannot be bypassed through API access

**Priority:** P0

Expected:

Protected API rejects incomplete authentication.

---

## TS-SEC-043 — MFA disabling requires required security verification

**Priority:** P0

Expected:

Cannot weaken account security without authorization.

---

# 12. OTP Rate-Limiting Scenarios

## TS-SEC-044 — Rapid repeated OTP requests

**Priority:** P1

Expected:

Configured rate limit/throttling enforced.

---

## TS-SEC-045 — Repeated invalid OTP submissions

**Priority:** P0

Expected:

Protection activates according to policy.

---

## TS-SEC-046 — OTP request limit applies across multiple browser sessions

**Priority:** P1

Expected:

Basic session switching does not bypass controls.

---

# 13. Session Creation Scenarios

## TS-SEC-047 — Successful login creates valid authenticated session

**Priority:** P0

Expected:

Protected resources become accessible.

---

## TS-SEC-048 — Anonymous session cannot access protected resource

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-049 — Session belongs to correct authenticated customer

**Priority:** P0

Expected:

No cross-customer session confusion.

---

# 14. Session Expiration Scenarios

## TS-SEC-050 — Session expires after configured inactivity period

**Priority:** P0

Expected:

Reauthentication required.

---

## TS-SEC-051 — Expired session cannot call protected API

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-052 — Expired session cannot complete transfer

**Priority:** P0

Expected:

Financial action rejected.

---

## TS-SEC-053 — Expired session cannot modify profile/security settings

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-054 — Expired session in one tab affects related tabs according to session design

**Priority:** P1

Expected:

No stale authorized state.

---

# 15. Logout Security Scenarios

## TS-SEC-055 — Logout terminates current session

**Priority:** P0

Expected:

Protected API/session cannot continue.

---

## TS-SEC-056 — Browser Back after logout

**Priority:** P0

Expected:

Cached protected pages do not expose usable sensitive data.

---

## TS-SEC-057 — Reuse session credential after logout

**Priority:** P0

Expected:

Rejected where session is invalidated.

---

## TS-SEC-058 — Logout from one device behaves according to session policy

**Priority:** P1

Expected:

Only intended sessions remain valid.

---

# 16. Session Revocation Scenarios

## TS-SEC-059 — Customer revokes another owned session

**Priority:** P0

Expected:

Target session becomes unusable.

---

## TS-SEC-060 — Revoked session attempts transfer

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-061 — Revoked session attempts password change

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-062 — Customer cannot revoke another customer's session

**Priority:** P0

Expected:

Ownership enforced.

---

# 17. Authorization / IDOR Scenarios

## TS-SEC-063 — Customer accesses another customer's account by changing account ID

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-064 — Customer accesses another customer's beneficiary by ID manipulation

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-065 — Customer accesses another customer's transaction

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-066 — Customer accesses another customer's statement

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-067 — Customer accesses another customer's card

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-068 — Customer accesses another customer's loan

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-069 — Customer accesses another customer's deposit

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-070 — Customer accesses another customer's notification

**Priority:** P0

Expected:

Denied.

---

# 18. Authorization on State-Changing Operations

## TS-SEC-071 — Customer tries to transfer from another customer's account

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-072 — Customer tries to freeze another customer's card

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-073 — Customer tries to delete another customer's beneficiary

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-074 — Customer tries to change another customer's profile

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-075 — Customer tries to repay another customer's loan using unauthorized request manipulation

**Priority:** P0

Expected:

Denied.

---

# 19. Role-Based Access Control

## TS-SEC-076 — Standard customer cannot access admin dashboard

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-077 — Standard customer cannot call admin API

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-078 — Limited admin cannot perform full-admin operation

**Priority:** P0

Expected:

Role permission enforced.

---

## TS-SEC-079 — Full admin can perform authorized administrative action

**Priority:** P1

Expected:

Allowed.

---

## TS-SEC-080 — Disabled admin cannot authenticate

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-081 — Changing client-side role value does not elevate permissions

**Priority:** P0

Expected:

Backend role remains authoritative.

---

# 20. Privilege Escalation Scenarios

## TS-SEC-082 — Customer inserts `role=ADMIN` into profile-update request

**Priority:** P0

Expected:

Ignored or rejected; role remains unchanged.

---

## TS-SEC-083 — Customer attempts to modify customer status

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-084 — Customer attempts to modify own KYC status

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-085 — Limited admin modifies permissions in request

**Priority:** P0

Expected:

Cannot elevate own role.

---

## TS-SEC-086 — Customer accesses hidden administrative action directly

**Priority:** P0

Expected:

Backend authorization denies action even if UI control is hidden.

---

# 21. Mass Assignment Scenarios

## TS-SEC-087 — Add restricted `role` field to profile payload

**Priority:** P0

Expected:

Protected field not updated.

---

## TS-SEC-088 — Add restricted `customerStatus` field

**Priority:** P0

Expected:

Rejected/ignored.

---

## TS-SEC-089 — Add restricted `balance` field to account update

**Priority:** P0

Expected:

Balance cannot be directly manipulated.

---

## TS-SEC-090 — Add restricted `loanStatus=APPROVED` field

**Priority:** P0

Expected:

Customer cannot approve loan.

---

# 22. Financial Request Tampering Scenarios

## TS-SEC-091 — Change transfer amount after client validation

**Priority:** P0

Expected:

Backend revalidates authoritative amount rules.

---

## TS-SEC-092 — Change transfer source account

**Priority:** P0

Expected:

Ownership enforced.

---

## TS-SEC-093 — Change transfer beneficiary

**Priority:** P0

Expected:

Ownership/status validation enforced.

---

## TS-SEC-094 — Change transfer fee client-side

**Priority:** P0

Expected:

Server-calculated fee remains authoritative.

---

## TS-SEC-095 — Change payment amount client-side

**Priority:** P0

Expected:

Backend validation enforced.

---

## TS-SEC-096 — Change loan interest rate client-side

**Priority:** P0

Expected:

Server configuration remains authoritative.

---

## TS-SEC-097 — Change deposit interest rate client-side

**Priority:** P0

Expected:

Server configuration remains authoritative.

---

# 23. Duplicate / Replay Protection

## TS-SEC-098 — Replay completed transfer request

**Priority:** P0

Expected:

No unintended duplicate financial transaction.

---

## TS-SEC-099 — Replay completed payment request

**Priority:** P0

Expected:

No duplicate payment.

---

## TS-SEC-100 — Replay loan disbursement request

**Priority:** P0

Expected:

No duplicate credit.

---

## TS-SEC-101 — Replay deposit creation request

**Priority:** P0

Expected:

No duplicate debit/deposit.

---

## TS-SEC-102 — Replay maturity payout request

**Priority:** P0

Expected:

No duplicate payout.

---

## TS-SEC-103 — Reuse idempotency key for same financial operation

**Priority:** P0

Expected:

Exactly one intended financial effect.

---

# 24. Client-Side Validation Bypass

## TS-SEC-104 — Remove required field validation in client and submit transfer

**Priority:** P0

Expected:

Backend independently rejects invalid request.

---

## TS-SEC-105 — Submit amount above limit directly to API

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-106 — Submit invalid account state directly to API

**Priority:** P0

Expected:

Business rule enforced server-side.

---

## TS-SEC-107 — Submit unsupported loan term directly to API

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-108 — Submit unsupported card limit directly to API

**Priority:** P0

Expected:

Rejected.

---

# 25. Input Validation Scenarios

## TS-SEC-109 — Script-like input in profile text field

**Priority:** P1

Example controlled test data:

```text
<script>alert('test')</script>
```

Expected:

Input is encoded, escaped, or rejected safely and never executed.

---

## TS-SEC-110 — Script-like input in beneficiary alias

**Priority:** P1

Expected:

No script execution.

---

## TS-SEC-111 — Script-like input in transfer note

**Priority:** P1

Expected:

No executable content.

---

## TS-SEC-112 — SQL-like text in search field

**Priority:** P1

Example controlled input:

```text
' OR '1'='1
```

Expected:

Handled as plain input and causes no database logic change.

---

## TS-SEC-113 — SQL-like text in login field

**Priority:** P0

Expected:

Cannot bypass authentication.

---

## TS-SEC-114 — Extremely long text input

**Priority:** P1

Expected:

Safely rejected/truncated according to field definition without service failure.

---

## TS-SEC-115 — Unicode and special-character input

**Priority:** P2

Expected:

Handled safely without corruption or security bypass.

---

# 26. Stored Content Safety Scenarios

## TS-SEC-116 — Script-like profile data stored then viewed later

**Priority:** P1

Expected:

Stored value is never executed.

---

## TS-SEC-117 — Script-like beneficiary alias shown on transfer page

**Priority:** P1

Expected:

Rendered safely.

---

## TS-SEC-118 — Script-like transaction note shown in history

**Priority:** P1

Expected:

Rendered safely.

---

## TS-SEC-119 — Script-like data included in admin screen

**Priority:** P1

Expected:

Administrative interface remains safe.

---

# 27. Cross-Site Request Protection

Where browser cookie/session architecture requires it.

## TS-SEC-120 — State-changing request without required request-forgery protection

**Priority:** P0

Expected:

Rejected according to application security architecture.

---

## TS-SEC-121 — Invalid request-forgery token

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-122 — Token from another session

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-123 — GET request cannot perform unintended state-changing financial action

**Priority:** P0

Expected:

Sensitive actions require appropriate state-changing methods and controls.

---

# 28. API Authentication Scenarios

## TS-SEC-124 — Protected API without authentication credential

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-125 — Protected API with invalid credential

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-126 — Protected API with expired credential

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-127 — Credential for Customer A used to request Customer B resource

**Priority:** P0

Expected:

Authorization failure.

---

## TS-SEC-128 — Customer credential used on admin API

**Priority:** P0

Expected:

Denied.

---

# 29. API Method Security Scenarios

## TS-SEC-129 — Unsupported HTTP method

**Priority:** P1

Expected:

Rejected safely.

---

## TS-SEC-130 — Attempt state modification using read-only endpoint

**Priority:** P1

Expected:

No modification occurs.

---

## TS-SEC-131 — Attempt to override method through unexpected parameter/header

**Priority:** P1

Expected:

Unauthorized operation not permitted.

---

# 30. API Parameter Tampering Scenarios

## TS-SEC-132 — Negative financial amount sent directly to API

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-133 — Zero financial amount sent directly to API

**Priority:** P0

Expected:

Rejected where nonzero required.

---

## TS-SEC-134 — Amount above maximum limit sent directly

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-135 — Invalid customer/account relationship supplied

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-136 — Hidden administrative parameter added to request

**Priority:** P0

Expected:

Cannot change authorization outcome.

---

# 31. Rate Limiting Scenarios

## TS-SEC-137 — Rapid login attempts

**Priority:** P0

Expected:

Throttled/controlled according to security policy.

---

## TS-SEC-138 — Rapid password-reset requests

**Priority:** P1

Expected:

Rate limiting or abuse protection applied.

---

## TS-SEC-139 — Rapid OTP requests

**Priority:** P1

Expected:

Controlled.

---

## TS-SEC-140 — Rapid beneficiary creation requests

**Priority:** P1

Expected:

Application remains consistent and duplicate protections apply.

---

## TS-SEC-141 — Rapid transfer requests

**Priority:** P0

Expected:

Financial limits/idempotency remain effective.

---

# 32. Sensitive Data Exposure

## TS-SEC-142 — Password not returned by API

**Priority:** P0

Expected:

Never present.

---

## TS-SEC-143 — Password hash not returned by API

**Priority:** P0

Expected:

Never present.

---

## TS-SEC-144 — MFA secret not returned by normal APIs

**Priority:** P0

Expected:

Protected.

---

## TS-SEC-145 — Full payment-card data not exposed unnecessarily

**Priority:** P0

Expected:

Masked/minimized.

---

## TS-SEC-146 — CVV not exposed through card-list/details API beyond explicitly secure workflow

**Priority:** P0

Expected:

Protected.

---

## TS-SEC-147 — PIN not exposed

**Priority:** P0

Expected:

Protected.

---

## TS-SEC-148 — Reset tokens not returned in unrelated API responses

**Priority:** P0

Expected:

Protected.

---

## TS-SEC-149 — Authentication/session tokens not included in application-generated error text

**Priority:** P0

Expected:

No credential leakage.

---

# 33. URL and Browser History Exposure

## TS-SEC-150 — Password absent from URL

**Priority:** P0

Expected:

Not exposed.

---

## TS-SEC-151 — OTP absent from persistent URL where not required

**Priority:** P0

Expected:

Not exposed.

---

## TS-SEC-152 — Full card number absent from URL

**Priority:** P0

Expected:

Not exposed.

---

## TS-SEC-153 — Sensitive customer identity data minimized in URL

**Priority:** P1

Expected:

No unnecessary exposure.

---

# 34. Browser Cache / Back Button Security

## TS-SEC-154 — Logout then browser Back to dashboard

**Priority:** P0

Expected:

Sensitive information is not usable/accessed as authenticated content.

---

## TS-SEC-155 — Logout then browser Back to statement page

**Priority:** P0

Expected:

Protected data remains protected.

---

## TS-SEC-156 — Shared browser login as different customer

**Priority:** P0

Expected:

Previous customer's cached data not exposed.

---

# 35. Error Message Security

## TS-SEC-157 — Invalid login does not reveal password details

**Priority:** P1

Expected:

Generic safe error.

---

## TS-SEC-158 — Invalid database lookup does not reveal SQL/database details

**Priority:** P0

Expected:

No internal query/schema disclosure.

---

## TS-SEC-159 — Server error does not expose stack trace to customer

**Priority:** P0

Expected:

Safe error response.

---

## TS-SEC-160 — Invalid resource ID does not expose internal object metadata

**Priority:** P1

Expected:

Safe response.

---

## TS-SEC-161 — Unauthorized request does not leak resource data in error payload

**Priority:** P0

Expected:

No sensitive data.

---

# 36. Security Headers / Browser Controls

Where applicable to the deployed web application.

## TS-SEC-162 — Application served over HTTPS in deployed environments

**Priority:** P0

Expected:

Sensitive pages use secure transport.

---

## TS-SEC-163 — Secure cookie attributes configured appropriately

**Priority:** P0

Expected:

Authentication cookies follow secure configuration.

---

## TS-SEC-164 — Clickjacking protection present according to deployment policy

**Priority:** P1

Expected:

Sensitive banking UI cannot be embedded unexpectedly where prohibited.

---

## TS-SEC-165 — Content-security controls reduce unintended script execution risk

**Priority:** P1

Expected:

Configured according to application architecture.

---

## TS-SEC-166 — Sensitive authenticated pages use appropriate cache controls

**Priority:** P1

Expected:

Security policy followed.

---

# 37. File Upload Security

Where profile/document uploads exist.

## TS-SEC-167 — Unsupported file type rejected

**Priority:** P1

Expected:

Only permitted formats accepted.

---

## TS-SEC-168 — Oversized upload rejected

**Priority:** P1

Expected:

Configured size limit enforced.

---

## TS-SEC-169 — File extension does not solely determine accepted type

**Priority:** P1

Expected:

Server validates content appropriately.

---

## TS-SEC-170 — Uploaded filename cannot manipulate storage path

**Priority:** P0

Expected:

Filename safely normalized.

---

## TS-SEC-171 — Uploaded file cannot overwrite another customer's resource

**Priority:** P0

Expected:

Ownership and unique storage handling enforced.

---

# 38. Financial Authorization Scenarios

## TS-SEC-172 — Transfer requires valid authenticated customer

**Priority:** P0

Expected:

Anonymous request rejected.

---

## TS-SEC-173 — Transfer validates source-account ownership

**Priority:** P0

Expected:

Correct.

---

## TS-SEC-174 — Transfer validates beneficiary ownership

**Priority:** P0

Expected:

Correct.

---

## TS-SEC-175 — Payment validates source-account ownership

**Priority:** P0

Expected:

Correct.

---

## TS-SEC-176 — Loan repayment validates loan ownership

**Priority:** P0

Expected:

Correct.

---

## TS-SEC-177 — Deposit withdrawal validates deposit ownership

**Priority:** P0

Expected:

Correct.

---

# 39. Financial State Validation Security

## TS-SEC-178 — Frozen account cannot transfer through direct API

**Priority:** P0

Expected:

Server rejects.

---

## TS-SEC-179 — Closed account cannot transfer through direct API

**Priority:** P0

Expected:

Server rejects.

---

## TS-SEC-180 — Blocked card cannot perform transaction through direct API

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-181 — Deleted beneficiary cannot be used through direct API

**Priority:** P0

Expected:

Rejected.

---

## TS-SEC-182 — Rejected loan cannot be disbursed through manipulated request

**Priority:** P0

Expected:

Rejected.

---

# 40. Financial Limit Security

## TS-SEC-183 — Per-transaction transfer limit cannot be bypassed through API

**Priority:** P0

Expected:

Server enforces limit.

---

## TS-SEC-184 — Daily transfer limit cannot be bypassed through multiple sessions

**Priority:** P0

Expected:

Cumulative server-side limit enforced.

---

## TS-SEC-185 — Payment limit cannot be bypassed client-side

**Priority:** P0

Expected:

Server enforces.

---

## TS-SEC-186 — Card spending limit cannot be modified beyond maximum

**Priority:** P0

Expected:

Rejected.

---

# 41. Concurrency and Race-Condition Security

## TS-SEC-187 — Two concurrent transfers cannot overspend account

**Priority:** P0

Expected:

Combined successful debit respects available balance.

---

## TS-SEC-188 — Transfer and payment concurrency cannot create negative balance unexpectedly

**Priority:** P0

Expected:

Financial integrity maintained.

---

## TS-SEC-189 — Two concurrent loan disbursement requests cannot double-credit

**Priority:** P0

Expected:

One disbursement only.

---

## TS-SEC-190 — Two maturity-processing requests cannot double-pay deposit

**Priority:** P0

Expected:

One payout only.

---

## TS-SEC-191 — Two simultaneous bill payments cannot settle same unique bill twice

**Priority:** P0

Expected:

Duplicate settlement prevented.

---

# 42. Admin Security Scenarios

## TS-SEC-192 — Customer cannot access admin UI

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-193 — Customer cannot access admin API

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-194 — Limited admin cannot perform unrestricted account changes

**Priority:** P0

Expected:

Permission enforced.

---

## TS-SEC-195 — Limited admin cannot approve loans without permission

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-196 — Limited admin cannot alter another admin's permissions

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-197 — Disabled admin session cannot continue privileged operations

**Priority:** P0

Expected:

Revoked/disabled state enforced.

---

# 43. Audit Log Security

## TS-SEC-198 — Critical financial operation creates audit entry

**Priority:** P0

Expected:

Traceable.

---

## TS-SEC-199 — Admin action identifies actor

**Priority:** P0

Expected:

Correct administrator recorded.

---

## TS-SEC-200 — Audit entry contains timestamp and result

**Priority:** P1

Expected:

Complete trace.

---

## TS-SEC-201 — Customer cannot edit audit log

**Priority:** P0

Expected:

Denied.

---

## TS-SEC-202 — Normal admin cannot silently delete audit history where prohibited

**Priority:** P0

Expected:

Audit integrity protected.

---

## TS-SEC-203 — Audit log does not store plaintext password

**Priority:** P0

Expected:

No credential exposure.

---

## TS-SEC-204 — Audit log does not store full OTP or authentication secrets

**Priority:** P0

Expected:

Sensitive data redacted.

---

# 44. Security Notification Scenarios

## TS-SEC-205 — Password change generates security notification

**Priority:** P0

Expected:

Customer alerted.

---

## TS-SEC-206 — MFA change generates security notification

**Priority:** P0

Expected:

Customer alerted.

---

## TS-SEC-207 — Email change generates security notification

**Priority:** P0

Expected:

Customer alerted.

---

## TS-SEC-208 — Card block generates security notification

**Priority:** P0

Expected:

Customer alerted.

---

## TS-SEC-209 — Failed financial transaction does not generate success notification

**Priority:** P0

Expected:

Notification reflects authoritative state.

---

# 45. Cross-Customer Data Isolation

## TS-SEC-210 — Customer A account list excludes Customer B accounts

**Priority:** P0

Expected:

Strict isolation.

---

## TS-SEC-211 — Customer A statements exclude Customer B transactions

**Priority:** P0

Expected:

Strict isolation.

---

## TS-SEC-212 — Customer A notification feed excludes Customer B alerts

**Priority:** P0

Expected:

Strict isolation.

---

## TS-SEC-213 — Customer A exports contain no Customer B data

**Priority:** P0

Expected:

Strict isolation.

---

## TS-SEC-214 — Search endpoints do not expose unauthorized customers/resources

**Priority:** P0

Expected:

Results filtered according to authorization.

---

# 46. Data Export Security

Where exports exist.

## TS-SEC-215 — Customer exports only owned transaction data

**Priority:** P0

Expected:

No cross-customer records.

---

## TS-SEC-216 — Statement download requires authorization

**Priority:** P0

Expected:

Protected.

---

## TS-SEC-217 — Old download link cannot bypass current authorization requirements

**Priority:** P0

Expected:

Protected according to signed-link/session policy.

---

## TS-SEC-218 — Export does not contain unnecessary sensitive fields

**Priority:** P0

Expected:

Data minimization enforced.

---

# 47. Security of Notifications

## TS-SEC-219 — Notification contains masked account identifiers

**Priority:** P0

Expected:

No unnecessary full account data.

---

## TS-SEC-220 — Notification contains masked card identifiers

**Priority:** P0

Expected:

No full PAN.

---

## TS-SEC-221 — Notification never contains password

**Priority:** P0

Expected:

No credential exposure.

---

## TS-SEC-222 — Notification deep link still performs authorization

**Priority:** P0

Expected:

Possessing link alone does not grant unauthorized access.

---

# 48. Direct Object Reference Enumeration Resistance

## TS-SEC-223 — Sequential account identifiers do not permit unauthorized discovery

**Priority:** P0

Expected:

Authorization blocks access regardless of predictability.

---

## TS-SEC-224 — Sequential transaction identifiers do not expose records

**Priority:** P0

Expected:

Access denied to unauthorized resources.

---

## TS-SEC-225 — Sequential loan identifiers do not expose loans

**Priority:** P0

Expected:

Access denied.

---

## TS-SEC-226 — Sequential card identifiers do not expose cards

**Priority:** P0

Expected:

Access denied.

---

# 49. Resource Existence Disclosure

## TS-SEC-227 — Unauthorized resource ID does not disclose unnecessary existence details

**Priority:** P1

Expected:

Response follows secure API policy.

---

## TS-SEC-228 — Admin-only resource response to customer does not reveal sensitive metadata

**Priority:** P1

Expected:

Minimal information disclosure.

---

# 50. HTTP Request Validation

## TS-SEC-229 — Invalid content type

**Priority:** P1

Expected:

Rejected safely.

---

## TS-SEC-230 — Malformed JSON body

**Priority:** P1

Expected:

Controlled validation error.

---

## TS-SEC-231 — Missing required request body

**Priority:** P1

Expected:

Rejected.

---

## TS-SEC-232 — Unexpected nested fields

**Priority:** P1

Expected:

Ignored/rejected according to schema.

---

## TS-SEC-233 — Duplicate parameters with conflicting values

**Priority:** P1

Expected:

Deterministic safe handling.

---

# 51. Security Logging Scenarios

## TS-SEC-234 — Repeated failed authentication logged where required

**Priority:** P1

Expected:

Security event available.

---

## TS-SEC-235 — Unauthorized admin access attempt logged

**Priority:** P1

Expected:

Traceable.

---

## TS-SEC-236 — IDOR-style unauthorized access attempt logged where required

**Priority:** P1

Expected:

Traceable without logging sensitive secrets.

---

## TS-SEC-237 — Financial replay/duplicate attempt logged where required

**Priority:** P1

Expected:

Security/transaction evidence preserved.

---

# 52. Information Leakage Through Search

## TS-SEC-238 — Customer search does not expose unauthorized customers

**Priority:** P0

Expected:

Search scope authorized.

---

## TS-SEC-239 — Account search does not reveal another customer's balance

**Priority:** P0

Expected:

No leakage.

---

## TS-SEC-240 — Transaction search does not expose another customer's financial records

**Priority:** P0

Expected:

No leakage.

---

# 53. Frontend Security Consistency

## TS-SEC-241 — Hidden UI action still protected by backend

**Priority:** P0

Expected:

UI visibility is not relied on for authorization.

---

## TS-SEC-242 — Disabled control cannot be bypassed through direct request

**Priority:** P0

Expected:

Server enforces business/security rule.

---

## TS-SEC-243 — Browser developer-tool modification cannot change authoritative financial value

**Priority:** P0

Expected:

Backend ignores manipulated client state.

---

# 54. Environment and Configuration Security

## TS-SEC-244 — Production-like secrets not hardcoded in client bundle

**Priority:** P0

Expected:

No sensitive server secrets exposed.

---

## TS-SEC-245 — Test credentials not exposed in public UI

**Priority:** P0

Expected:

No credential disclosure.

---

## TS-SEC-246 — Debug information disabled in production-like deployment

**Priority:** P1

Expected:

No verbose internal diagnostics exposed.

---

## TS-SEC-247 — Environment-specific configuration does not accidentally point production-like UI to unauthorized backend

**Priority:** P0

Expected:

Correct environment isolation.

---

# 55. Secure Failure Behavior

## TS-SEC-248 — Authorization service unavailable

**Priority:** P0

Expected:

Protected action fails closed rather than allowing access.

---

## TS-SEC-249 — Role/permission lookup failure

**Priority:** P0

Expected:

Privileged operation denied.

---

## TS-SEC-250 — Account-status validation service unavailable before transfer

**Priority:** P0

Expected:

Transfer does not proceed without required validation.

---

## TS-SEC-251 — Beneficiary validation unavailable

**Priority:** P0

Expected:

Transfer does not proceed as if beneficiary were valid.

---

# 56. End-to-End Security Scenarios

## TS-SEC-252 — Cross-customer account access attack simulation

**Priority:** P0

Flow:

```text
Login as Customer A
→ Open Customer A Account
→ Capture/Inspect Account Identifier
→ Replace Identifier With Customer B Account
→ Submit Request
→ Verify Access Denied
→ Verify No Customer B Data Returned
```

---

## TS-SEC-253 — Privilege-escalation protection journey

**Priority:** P0

Flow:

```text
Login as Standard Customer
→ Intercept Editable Profile Request
→ Add Administrator Role Field
→ Submit
→ Verify Role Change Rejected
→ Access Admin Endpoint
→ Verify Access Denied
```

---

## TS-SEC-254 — Financial request tampering journey

**Priority:** P0

Flow:

```text
Prepare Valid Transfer
→ Modify Amount After Client Validation
→ Submit Manipulated Request
→ Verify Server Revalidates Amount
→ Verify Invalid Transfer Rejected
→ Verify Balance Unchanged
```

---

## TS-SEC-255 — Replay protection journey

**Priority:** P0

Flow:

```text
Complete Valid Transfer
→ Capture Same Authorized Request
→ Submit Same Request Again
→ Verify Duplicate Financial Effect Is Prevented
→ Verify Balance Debited Only Once
```

---

## TS-SEC-256 — Session revocation journey

**Priority:** P0

Flow:

```text
Login On Session A
→ Login On Session B
→ Revoke Session B From Session A
→ Attempt Transfer From Session B
→ Verify Rejected
```

---

## TS-SEC-257 — Password reset token lifecycle

**Priority:** P0

Flow:

```text
Request Password Reset
→ Receive Valid Token
→ Reset Password
→ Attempt Reuse Of Same Token
→ Verify Rejected
→ Verify Old Password Fails
→ Verify New Password Succeeds
```

---

## TS-SEC-258 — Security-sensitive profile-change journey

**Priority:** P0

Flow:

```text
Login
→ Change Verified Email
→ Complete Required Verification
→ Verify New Email Stored
→ Verify Old Email No Longer Authoritative
→ Verify Security Notification
→ Verify Audit Record
```

---

## TS-SEC-259 — Concurrent overspending protection

**Priority:** P0

Flow:

```text
Account Balance = 1000
→ Session A Attempts Transfer 800
→ Session B Attempts Payment 500 Concurrently
→ Verify Combined Successful Debit Does Not Exceed Available Funds
→ Verify Final Balance Is Valid
→ Verify Transaction Records
```

---

# 57. Critical Smoke Security Scenarios

Security smoke coverage should include:

```text
TS-SEC-002 — Invalid password rejected
TS-SEC-006 — Locked customer denied
TS-SEC-041 — MFA cannot be bypassed
TS-SEC-050 — Session expires
TS-SEC-055 — Logout invalidates session
TS-SEC-063 — Account ownership enforced
TS-SEC-076 — Customer cannot access admin
TS-SEC-091 — Transfer amount tampering rejected
TS-SEC-098 — Transfer replay prevented
TS-SEC-142 — Password not exposed
```

---

# 58. Critical Security Regression

Always prioritize:

* Authentication
* Lockout
* Password reset
* MFA
* Session expiry
* Logout
* Session revocation
* Account ownership
* Transaction ownership
* Statement ownership
* Card ownership
* Loan ownership
* Deposit ownership
* Admin permissions
* Privilege escalation
* Mass assignment
* Financial request tampering
* Replay protection
* Server-side validation
* Sensitive-data exposure
* Financial state validation
* Concurrency
* Audit integrity

---

# 59. UI Automation Candidates

Security-focused UI regression can include:

* Login lockout
* Session expiry
* MFA enforcement
* Logout protection
* Password change
* Another-account navigation attempt
* Admin-page access control
* Restricted-account transfer
* Frozen-card behavior

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 60. API Automation Candidates

Postman and REST Assured should provide strong security regression coverage for:

* Missing authentication
* Expired authentication
* Resource ownership
* Admin authorization
* Protected-field modification
* Invalid financial values
* State manipulation
* Replay/idempotency
* Unsupported transitions
* Sensitive response fields
* Rate-limit behavior

---

# 61. Database Validation Candidates

SQL validation may verify:

* Customer ownership relationships
* Role assignments
* Account ownership
* Session revocation state
* Transaction uniqueness
* Idempotency references
* Audit records
* Password data is not plaintext
* Financial state unchanged after rejected request

Do not expose passwords, tokens, OTPs, full card data, or similar secrets in test reports.

---

# 62. Performance / Security Interaction

JMeter may later validate:

* Authentication throttling under repeated requests
* Financial integrity during concurrent traffic
* Replay protection under latency
* API authorization under load
* Rate-limiting behavior
* Session validation during high concurrency

Security controls must remain effective under load.

---

# 63. BDD Candidates

Example:

```gherkin
Feature: Account authorization

Scenario: Customer cannot access another customer's account
  Given Customer A is authenticated
  And Account B belongs to Customer B
  When Customer A requests Account B
  Then access should be denied
  And no Account B financial data should be returned
```

Financial tampering example:

```gherkin
Feature: Server-side transfer validation

Scenario: Client-side transfer limit cannot be bypassed
  Given the customer is authenticated
  And the customer's maximum transfer amount is configured
  When the customer submits an API request above that maximum
  Then the transfer should be rejected
  And the account balance should remain unchanged
```

Replay example:

```gherkin
Feature: Financial transaction replay protection

Scenario: Replaying a completed transfer does not duplicate the debit
  Given a transfer has completed successfully
  When the same protected transfer request is submitted again
  Then no second unintended transfer should be created
  And the source account should only contain one corresponding debit
```

---

# 64. Risk Traceability

Major related risks include:

```text
RISK-002 — Unauthorized customer data access
RISK-003 — Duplicate financial transaction
RISK-005 — Authentication bypass
RISK-006 — Privilege escalation
RISK-007 — Transfer exceeding available balance
RISK-008 — Transfer limit bypass
RISK-009 — Frozen account can transact
RISK-013 — Concurrent transactions corrupt balance
RISK-014 — Session active after logout
RISK-015 — Expired session accepted
RISK-016 — Account lockout fails
RISK-021 — Sensitive information exposure
RISK-023 — Unauthorized admin operation
RISK-024 — Frozen card remains usable
RISK-025 — Blocked card becomes usable unexpectedly
RISK-030 — API accepts unauthorized request
RISK-037 — Frontend-only validation bypass
RISK-044 — OTP reuse accepted
RISK-045 — Expired OTP accepted
RISK-046 — Password reset token reusable
RISK-047 — Another customer's resource accessible by ID manipulation
```

---

# 65. Security Coverage Summary

This catalog covers:

* Authentication
* User enumeration
* Brute-force protection
* Password security
* Password reset
* MFA
* OTP abuse
* Sessions
* Logout
* Session revocation
* IDOR/object ownership
* Role-based authorization
* Privilege escalation
* Mass assignment
* Financial request tampering
* Replay attacks
* Client-side validation bypass
* Input validation
* Stored-content safety
* Request-forgery protection
* API authentication
* API methods
* API parameter validation
* Rate limiting
* Sensitive-data exposure
* URL exposure
* Browser caching
* Safe errors
* Security headers
* Upload security
* Financial authorization
* Financial state validation
* Limits
* Concurrency/race conditions
* Admin security
* Audit security
* Security notifications
* Customer-data isolation
* Export security
* Notification security
* Resource enumeration
* Frontend/backend security consistency
* Environment configuration
* Secure failure behavior
* End-to-end security workflows

---

# 66. Final Security Testing Principle

Banking security controls must be enforced by the authoritative backend and must not depend only on frontend visibility, disabled controls, or trusted client input.

For every critical banking workflow, QA should be able to answer:

```text
Is the user authenticated?

Is the session still valid?

Is the user authorized for this exact resource?

Does the resource belong to this customer?

Does the user's role permit this action?

Can client-side controls be bypassed?

Can protected fields be modified?

Can a request be replayed?

Can transaction limits be bypassed?

Can concurrent requests corrupt financial state?

Can another customer's data be accessed?

Are sensitive values protected?

Are important security events audited?

Does the application fail securely when a dependency is unavailable?
```

The key rule is:

```text
Never trust the client.

Authenticate every protected request.

Authorize every protected resource.

Validate every financial operation server-side.

Preserve financial and audit integrity even when requests are malicious,
duplicated, concurrent, malformed, or interrupted.
```
