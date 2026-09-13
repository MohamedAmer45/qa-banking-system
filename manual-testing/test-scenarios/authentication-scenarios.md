# Banking System — Authentication Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Authentication                 |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for authentication functionality within the Banking System.

Authentication is considered a critical-risk area because failures may result in:

* Unauthorized account access
* Customer data exposure
* Unauthorized financial transactions
* Session hijacking
* Account lockout bypass
* Multi-factor authentication bypass
* Password reset abuse
* Privilege escalation

The scenarios in this document will later be converted into detailed test cases.

---

# 3. Authentication Scope

The authentication module includes:

* Registration
* Login
* Logout
* Password validation
* Password change
* Forgot password
* Password reset
* Multi-factor authentication
* OTP validation
* Account lockout
* Session management
* Remember-me functionality
* Token/session expiration
* Authentication error handling
* Role-based login behavior

---

# 4. Scenario Naming Convention

Authentication scenarios use:

```text
TS-AUTH-XXX
```

Example:

```text
TS-AUTH-001
TS-AUTH-002
TS-AUTH-003
```

Priority levels:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Registration Scenarios

## TS-AUTH-001 — Register with valid customer information

**Priority:** P1

Verify that a new customer can successfully register using valid required information.

Expected:

* Registration succeeds.
* Customer record is created.
* Appropriate confirmation is displayed.
* Customer enters the expected initial account state.

---

## TS-AUTH-002 — Register with all required fields empty

**Priority:** P1

Verify validation when registration is submitted without entering any required data.

Expected:

* Registration is rejected.
* Required-field validation is displayed.

---

## TS-AUTH-003 — Register with one required field missing

**Priority:** P1

Repeat for each required registration field.

Expected:

Registration is rejected with the correct validation message.

---

## TS-AUTH-004 — Register using an already registered email address

**Priority:** P1

Expected:

Duplicate registration is rejected safely.

The response should not expose unnecessary internal information.

---

## TS-AUTH-005 — Register using an already registered username

**Priority:** P1

Expected:

Duplicate username is rejected where usernames must be unique.

---

## TS-AUTH-006 — Register using invalid email format

**Priority:** P1

Test examples:

```text
user
user@
@banktest.local
user@domain
user..test@example.com
```

Expected:

Invalid email addresses are rejected according to application validation rules.

---

## TS-AUTH-007 — Register using email with leading or trailing spaces

**Priority:** P2

Expected:

The application should normalize or reject the value according to defined requirements.

Duplicate-account rules must not be bypassable with spaces.

---

## TS-AUTH-008 — Register with minimum-length password

**Priority:** P1

Expected:

The minimum valid password should be accepted.

---

## TS-AUTH-009 — Register with password below minimum length

**Priority:** P1

Expected:

Registration is rejected.

---

## TS-AUTH-010 — Register with maximum-length password

**Priority:** P1

Expected:

Maximum supported valid password is accepted.

---

## TS-AUTH-011 — Register with password above maximum length

**Priority:** P1

Expected:

Input is rejected safely.

---

## TS-AUTH-012 — Register with password missing required uppercase character

**Priority:** P2

Execute only when required by password policy.

Expected:

Password is rejected.

---

## TS-AUTH-013 — Register with password missing required lowercase character

**Priority:** P2

Expected:

Password validation is enforced.

---

## TS-AUTH-014 — Register with password missing required number

**Priority:** P2

Expected:

Password validation is enforced.

---

## TS-AUTH-015 — Register with password missing required special character

**Priority:** P2

Expected:

Password validation is enforced.

---

## TS-AUTH-016 — Register with password and confirmation mismatch

**Priority:** P1

Expected:

Registration is rejected.

---

## TS-AUTH-017 — Register using unsupported characters in name fields

**Priority:** P2

Test according to business rules.

Expected:

Invalid values are rejected without system error.

---

## TS-AUTH-018 — Register using valid international characters

**Priority:** P2

Examples:

```text
محمد
José
Müller
François
```

Expected:

Supported Unicode customer names are accepted and displayed correctly.

---

## TS-AUTH-019 — Submit registration twice rapidly

**Priority:** P1

Expected:

Only one customer account should be created.

Duplicate registration should not occur because of:

* Double click
* Slow response
* Rapid resubmission

---

## TS-AUTH-020 — Refresh browser after registration submission

**Priority:** P2

Expected:

Registration should not unintentionally execute twice.

---

# 6. Login Scenarios

## TS-AUTH-021 — Login with valid username and password

**Priority:** P0

Expected:

* Authentication succeeds.
* Customer is redirected to the authenticated area.
* Correct customer account is loaded.

---

## TS-AUTH-022 — Login with valid email and password

**Priority:** P1

Execute when email-based authentication is supported.

Expected:

Login succeeds.

---

## TS-AUTH-023 — Login with valid username and incorrect password

**Priority:** P0

Expected:

* Login fails.
* Generic authentication error is displayed.
* User remains unauthenticated.

---

## TS-AUTH-024 — Login with invalid username and valid-format password

**Priority:** P0

Expected:

Login fails safely.

The response should not unnecessarily confirm whether the username exists.

---

## TS-AUTH-025 — Login with invalid username and invalid password

**Priority:** P1

Expected:

Authentication fails.

---

## TS-AUTH-026 — Login with empty username

**Priority:** P1

Expected:

Required-field validation.

---

## TS-AUTH-027 — Login with empty password

**Priority:** P1

Expected:

Required-field validation.

---

## TS-AUTH-028 — Login with both fields empty

**Priority:** P1

Expected:

Login is rejected.

---

## TS-AUTH-029 — Login with username containing leading spaces

**Priority:** P2

Expected:

Input should be safely normalized or rejected according to requirements.

---

## TS-AUTH-030 — Login with username containing trailing spaces

**Priority:** P2

Expected:

Application handles the input predictably.

---

## TS-AUTH-031 — Login with password containing leading spaces

**Priority:** P2

Expected:

Password should generally be treated exactly as entered unless explicitly defined otherwise.

---

## TS-AUTH-032 — Login with password containing trailing spaces

**Priority:** P2

Expected:

Authentication behavior remains consistent with password policy.

---

## TS-AUTH-033 — Verify password field masks entered characters

**Priority:** P1

Expected:

Password is not displayed as plain text by default.

---

## TS-AUTH-034 — Verify password visibility toggle

**Priority:** P2

Expected:

* Toggle reveals password when selected.
* Toggle hides password when selected again.
* Value remains unchanged.

---

## TS-AUTH-035 — Login using different letter case for username/email

**Priority:** P2

Expected:

Behavior matches defined case-sensitivity rules.

---

## TS-AUTH-036 — Login using different letter case for password

**Priority:** P1

Expected:

Passwords should be case-sensitive.

---

## TS-AUTH-037 — Login as active customer

**Priority:** P0

Expected:

Successful authentication.

---

## TS-AUTH-038 — Login as locked customer

**Priority:** P0

Expected:

Authentication is rejected while lock remains active.

---

## TS-AUTH-039 — Login as disabled customer

**Priority:** P0

Expected:

Login denied.

---

## TS-AUTH-040 — Login as suspended customer

**Priority:** P0

Expected:

Behavior follows account-status business rules.

---

## TS-AUTH-041 — Login as administrator

**Priority:** P0

Expected:

Administrator is authenticated and receives appropriate access.

---

## TS-AUTH-042 — Login as limited administrator

**Priority:** P1

Expected:

Authentication succeeds but permissions remain limited.

---

## TS-AUTH-043 — Customer login must not redirect to admin interface

**Priority:** P0

Expected:

Role separation is enforced.

---

## TS-AUTH-044 — Attempt SQL-like input in username field

**Priority:** P1

Example:

```text
' OR '1'='1
```

Expected:

* Authentication is not bypassed.
* Input is handled safely.
* No database details are exposed.

---

## TS-AUTH-045 — Attempt script-like input in login fields

**Priority:** P1

Example:

```text
<script>alert(1)</script>
```

Expected:

Input is safely handled without script execution.

---

## TS-AUTH-046 — Enter extremely long username

**Priority:** P2

Expected:

Application handles the value safely without crash or excessive delay.

---

## TS-AUTH-047 — Enter extremely long password

**Priority:** P2

Expected:

Input is handled safely.

---

# 7. Failed Login and Account Lockout Scenarios

## TS-AUTH-048 — Single failed login attempt

**Priority:** P1

Expected:

* Login fails.
* User remains active.
* Failed attempt is recorded where applicable.

---

## TS-AUTH-049 — Failed attempts below lockout threshold

**Priority:** P1

Expected:

Account remains unlocked.

---

## TS-AUTH-050 — Failed attempts exactly at lockout threshold

**Priority:** P0

Expected:

Account becomes locked according to business rules.

---

## TS-AUTH-051 — Failed attempts above lockout threshold

**Priority:** P0

Expected:

Account remains locked.

Repeated attempts must not bypass security controls.

---

## TS-AUTH-052 — Correct password after account becomes locked

**Priority:** P0

Expected:

Login is still denied while lock is active.

---

## TS-AUTH-053 — Verify automatic lockout expiration

**Priority:** P1

Execute when temporary lockout is supported.

Expected:

Account becomes usable only after the defined lockout period.

---

## TS-AUTH-054 — Verify administrator unlock

**Priority:** P1

Expected:

Authorized administrator can restore authentication according to business rules.

---

## TS-AUTH-055 — Verify customer unlock process

**Priority:** P1

Execute when self-service unlock is available.

Expected:

Secure unlock process works correctly.

---

## TS-AUTH-056 — Verify failed-login counter reset after successful login

**Priority:** P1

Expected:

Previous failed attempts should reset according to defined requirements.

---

## TS-AUTH-057 — Attempt distributed login failures across multiple sessions

**Priority:** P1

Expected:

Lockout must apply to the account and not merely to one browser session when that is the intended design.

---

# 8. Multi-Factor Authentication Scenarios

## TS-AUTH-058 — Login with valid credentials and valid OTP

**Priority:** P0

Expected:

Authentication completes successfully.

---

## TS-AUTH-059 — Login with valid credentials and invalid OTP

**Priority:** P0

Expected:

Access denied.

---

## TS-AUTH-060 — Login with valid credentials and expired OTP

**Priority:** P0

Expected:

Expired OTP is rejected.

---

## TS-AUTH-061 — Login with empty OTP

**Priority:** P1

Expected:

Validation is displayed.

---

## TS-AUTH-062 — Login with OTP shorter than expected length

**Priority:** P1

Expected:

OTP is rejected.

---

## TS-AUTH-063 — Login with OTP longer than expected length

**Priority:** P1

Expected:

OTP is rejected.

---

## TS-AUTH-064 — Login with non-numeric OTP

**Priority:** P2

Execute when OTP format is numeric-only.

Expected:

Invalid value is rejected.

---

## TS-AUTH-065 — Reuse successfully consumed OTP

**Priority:** P0

Expected:

OTP cannot be reused.

---

## TS-AUTH-066 — Use old OTP after requesting a new OTP

**Priority:** P0

Expected:

Previous OTP should become invalid where required by design.

---

## TS-AUTH-067 — Use newest OTP after requesting multiple OTPs

**Priority:** P1

Expected:

Latest valid OTP works according to application rules.

---

## TS-AUTH-068 — Request multiple OTPs rapidly

**Priority:** P1

Expected:

System enforces rate limits or safe handling.

---

## TS-AUTH-069 — Enter incorrect OTP repeatedly

**Priority:** P0

Expected:

Retry limits are enforced according to requirements.

---

## TS-AUTH-070 — OTP expires exactly at configured expiry boundary

**Priority:** P1

Expected:

Behavior is consistent and predictable at the time boundary.

---

## TS-AUTH-071 — Refresh page during OTP entry

**Priority:** P2

Expected:

Authentication state remains secure.

---

## TS-AUTH-072 — Navigate backward during MFA flow

**Priority:** P1

Expected:

User cannot bypass MFA using browser navigation.

---

## TS-AUTH-073 — Directly navigate to authenticated page before completing MFA

**Priority:** P0

Expected:

Access denied.

---

## TS-AUTH-074 — Complete MFA in one tab and use second pre-authentication tab

**Priority:** P1

Expected:

Session behavior remains secure and consistent.

---

# 9. Forgot Password Scenarios

## TS-AUTH-075 — Request password reset using registered email

**Priority:** P1

Expected:

Password reset flow begins.

---

## TS-AUTH-076 — Request password reset using unregistered email

**Priority:** P1

Expected:

Response should avoid unnecessary user enumeration.

Example safe behavior:

```text
If an account exists, reset instructions have been sent.
```

---

## TS-AUTH-077 — Submit empty email in forgot-password form

**Priority:** P2

Expected:

Required validation.

---

## TS-AUTH-078 — Submit invalid email format

**Priority:** P2

Expected:

Invalid format rejected.

---

## TS-AUTH-079 — Request password reset repeatedly

**Priority:** P1

Expected:

Rate limiting or safe request handling occurs.

---

## TS-AUTH-080 — Password reset request for locked user

**Priority:** P1

Expected:

Behavior follows business rules without unintentionally bypassing account restrictions.

---

## TS-AUTH-081 — Password reset request for disabled user

**Priority:** P1

Expected:

Disabled account restrictions remain enforced.

---

# 10. Password Reset Scenarios

## TS-AUTH-082 — Reset password using valid reset token

**Priority:** P0

Expected:

Password changes successfully.

---

## TS-AUTH-083 — Reset password using expired token

**Priority:** P0

Expected:

Reset denied.

---

## TS-AUTH-084 — Reset password using malformed token

**Priority:** P1

Expected:

Reset denied safely.

---

## TS-AUTH-085 — Reset password using already consumed token

**Priority:** P0

Expected:

Token reuse rejected.

---

## TS-AUTH-086 — Reset password using latest token after generating several tokens

**Priority:** P1

Expected:

Behavior follows configured token policy.

---

## TS-AUTH-087 — Attempt reset with weak new password

**Priority:** P1

Expected:

Password policy is enforced.

---

## TS-AUTH-088 — Reset using same password as existing password

**Priority:** P2

Expected:

Behavior follows password-history rules.

---

## TS-AUTH-089 — Reset using recently used password

**Priority:** P2

Execute if password-history restrictions exist.

Expected:

Reused password rejected.

---

## TS-AUTH-090 — New password and confirmation do not match

**Priority:** P1

Expected:

Password is not changed.

---

## TS-AUTH-091 — Login using old password after successful reset

**Priority:** P0

Expected:

Old password no longer works.

---

## TS-AUTH-092 — Login using new password after successful reset

**Priority:** P0

Expected:

New password works.

---

## TS-AUTH-093 — Existing authenticated sessions after password reset

**Priority:** P0

Verify whether existing sessions are revoked according to defined security requirements.

---

# 11. Password Change Scenarios

## TS-AUTH-094 — Change password using correct current password

**Priority:** P1

Expected:

Password changes successfully.

---

## TS-AUTH-095 — Change password using incorrect current password

**Priority:** P0

Expected:

Change rejected.

---

## TS-AUTH-096 — Change password with weak new password

**Priority:** P1

Expected:

Password-policy validation is enforced.

---

## TS-AUTH-097 — Change password where new password matches confirmation

**Priority:** P1

Expected:

Change succeeds if all other requirements are valid.

---

## TS-AUTH-098 — Change password where new password does not match confirmation

**Priority:** P1

Expected:

Change rejected.

---

## TS-AUTH-099 — Verify old password after password change

**Priority:** P0

Expected:

Old password fails.

---

## TS-AUTH-100 — Verify new password after password change

**Priority:** P0

Expected:

New password succeeds.

---

## TS-AUTH-101 — Verify active sessions after password change

**Priority:** P0

Expected:

Existing sessions are handled according to security requirements.

---

## TS-AUTH-102 — Verify security notification after password change

**Priority:** P1

Expected:

A notification is generated when required.

---

## TS-AUTH-103 — Verify audit record after password change

**Priority:** P1

Expected:

Security-sensitive action is recorded appropriately.

---

# 12. Logout Scenarios

## TS-AUTH-104 — Logout from active customer session

**Priority:** P0

Expected:

* Customer becomes unauthenticated.
* Authenticated resources are no longer accessible.

---

## TS-AUTH-105 — Logout from administrator session

**Priority:** P0

Expected:

Administrator session ends.

---

## TS-AUTH-106 — Use browser Back after logout

**Priority:** P0

Expected:

Protected information must not become usable again.

Cached content must not provide functional authenticated access.

---

## TS-AUTH-107 — Directly navigate to protected URL after logout

**Priority:** P0

Expected:

Redirect to login or access denied.

---

## TS-AUTH-108 — Reuse API token/session after logout

**Priority:** P0

Expected:

Behavior follows session/token revocation design.

If tokens are expected to be invalidated, reuse must fail.

---

## TS-AUTH-109 — Logout in one browser tab while another authenticated tab remains open

**Priority:** P0

Expected:

Session state should synchronize according to application architecture.

Protected actions must not remain incorrectly available.

---

## TS-AUTH-110 — Logout during financial workflow

**Priority:** P1

Expected:

Incomplete financial operation must not execute unexpectedly.

---

## TS-AUTH-111 — Repeated logout request

**Priority:** P2

Expected:

Application handles repeated logout safely without server error.

---

# 13. Session Management Scenarios

## TS-AUTH-112 — Verify session is created after successful authentication

**Priority:** P0

Expected:

Valid authenticated session begins.

---

## TS-AUTH-113 — Verify anonymous user cannot access protected page

**Priority:** P0

Expected:

Access denied or redirected.

---

## TS-AUTH-114 — Verify session timeout after configured inactivity period

**Priority:** P0

Expected:

Session expires according to requirement.

---

## TS-AUTH-115 — Perform action immediately before session timeout

**Priority:** P1

Expected:

Session behavior follows configured activity-reset rules.

---

## TS-AUTH-116 — Perform action immediately after session timeout

**Priority:** P0

Expected:

User must reauthenticate.

---

## TS-AUTH-117 — Session expires while viewing account information

**Priority:** P1

Expected:

Next protected request is rejected.

---

## TS-AUTH-118 — Session expires while entering transfer details

**Priority:** P0

Expected:

Transfer must not execute without valid authentication.

---

## TS-AUTH-119 — Session expires on transfer confirmation screen

**Priority:** P0

Expected:

Confirmation after expiry requires appropriate reauthentication or is rejected.

---

## TS-AUTH-120 — Session expires while changing password

**Priority:** P1

Expected:

Sensitive action should not complete under an invalid session.

---

## TS-AUTH-121 — Session expires in one of multiple tabs

**Priority:** P1

Expected:

All tabs should eventually respect the invalid session.

---

## TS-AUTH-122 — Verify session behavior after browser close

**Priority:** P1

Expected behavior depends on session persistence and remember-me settings.

---

## TS-AUTH-123 — Copy authenticated URL into separate unauthenticated browser

**Priority:** P0

Expected:

URL alone must not grant access.

---

## TS-AUTH-124 — Attempt to reuse session cookie from another account

**Priority:** P0

Expected:

Unauthorized resource access must not occur.

---

# 14. Remember Me Scenarios

## TS-AUTH-125 — Login with Remember Me disabled

**Priority:** P2

Expected:

Session persistence follows normal session rules.

---

## TS-AUTH-126 — Login with Remember Me enabled

**Priority:** P2

Expected:

Authentication persistence follows defined requirements.

---

## TS-AUTH-127 — Close and reopen browser with Remember Me enabled

**Priority:** P2

Expected:

User remains authenticated only for the allowed persistence duration.

---

## TS-AUTH-128 — Close and reopen browser with Remember Me disabled

**Priority:** P2

Expected:

Session behavior follows normal browser-session rules.

---

## TS-AUTH-129 — Logout after using Remember Me

**Priority:** P0

Expected:

Persistent authentication must be invalidated where required.

---

## TS-AUTH-130 — Change password after using Remember Me

**Priority:** P0

Expected:

Previously issued persistent authentication should be handled according to security policy.

---

# 15. Concurrent and Multiple Session Scenarios

## TS-AUTH-131 — Login to the same account in two browser tabs

**Priority:** P2

Expected:

Both tabs follow the same valid session where applicable.

---

## TS-AUTH-132 — Login to the same account from two different browsers

**Priority:** P1

Expected:

Multiple-session behavior follows business rules.

---

## TS-AUTH-133 — Login from second device while first session is active

**Priority:** P1

Expected:

System follows defined multi-device session policy.

---

## TS-AUTH-134 — Logout from one session while another session is active

**Priority:** P1

Verify whether logout affects:

* Current session only
* All sessions

according to requirements.

---

## TS-AUTH-135 — Change password from one session while another remains active

**Priority:** P0

Expected:

Other session is handled according to security requirements.

---

## TS-AUTH-136 — Lock user while active sessions exist

**Priority:** P0

Expected:

Existing access must respect updated security state.

---

## TS-AUTH-137 — Administrator disables customer while customer is logged in

**Priority:** P0

Expected:

Subsequent protected actions are denied according to system rules.

---

# 16. Role and Authorization Authentication Scenarios

## TS-AUTH-138 — Customer attempts to access admin login-protected route

**Priority:** P0

Expected:

Access denied.

---

## TS-AUTH-139 — Limited administrator attempts full-admin route

**Priority:** P0

Expected:

Access denied.

---

## TS-AUTH-140 — Full administrator accesses authorized admin route

**Priority:** P0

Expected:

Access allowed.

---

## TS-AUTH-141 — Customer manipulates authenticated URL to administrative path

**Priority:** P0

Expected:

Authorization prevents access.

---

## TS-AUTH-142 — Customer calls administrative API after normal customer login

**Priority:** P0

Expected:

API returns authorization failure.

---

## TS-AUTH-143 — Authentication token for one role used against another role's restricted endpoint

**Priority:** P0

Expected:

Authorization enforced by backend.

---

# 17. Authentication Security Scenarios

## TS-AUTH-144 — Verify login error does not reveal whether username exists

**Priority:** P1

Expected:

Responses should minimize account-enumeration information.

---

## TS-AUTH-145 — Verify forgot-password response does not reveal account existence

**Priority:** P1

Expected:

Generic safe response where required.

---

## TS-AUTH-146 — Verify passwords are never displayed in application logs visible to user

**Priority:** P0

Expected:

No plaintext credential leakage.

---

## TS-AUTH-147 — Verify password is not included in URL

**Priority:** P0

Expected:

Credentials must not appear in:

```text
query string
URL path
browser history
```

---

## TS-AUTH-148 — Verify OTP is not exposed unnecessarily after submission

**Priority:** P1

Expected:

Sensitive authentication data is handled safely.

---

## TS-AUTH-149 — Verify authentication token is not displayed in UI

**Priority:** P1

Expected:

Tokens remain hidden from normal user-facing screens.

---

## TS-AUTH-150 — Verify protected page does not expose other user's cached data after account switch

**Priority:** P0

Scenario:

```text
Customer A logs in
→ Logout
→ Customer B logs in
```

Expected:

No Customer A data remains visible.

---

## TS-AUTH-151 — Rapid repeated login requests

**Priority:** P1

Expected:

Application remains stable and security controls apply.

---

## TS-AUTH-152 — Rapid repeated password-reset requests

**Priority:** P1

Expected:

Safe throttling/rate limiting behavior.

---

## TS-AUTH-153 — Rapid repeated OTP requests

**Priority:** P1

Expected:

System prevents abuse according to configured rules.

---

## TS-AUTH-154 — Authentication endpoint with malformed request

**Priority:** P1

Expected:

* Request rejected.
* Safe client error returned.
* No internal stack trace exposed.

---

## TS-AUTH-155 — Authentication endpoint with missing required fields

**Priority:** P1

Expected:

Validation error, not internal server failure.

---

# 18. Navigation and Usability Scenarios

## TS-AUTH-156 — Verify login page clearly identifies username/email and password fields

**Priority:** P2

Expected:

Fields and labels are understandable.

---

## TS-AUTH-157 — Verify keyboard navigation through login page

**Priority:** P2

Expected:

Logical focus order.

---

## TS-AUTH-158 — Submit login using Enter key

**Priority:** P2

Expected:

Form submits correctly.

---

## TS-AUTH-159 — Verify visible keyboard focus indicators

**Priority:** P2

Expected:

Focused controls are visually identifiable.

---

## TS-AUTH-160 — Verify error message displayed near relevant login field

**Priority:** P2

Expected:

Error is clear and actionable.

---

## TS-AUTH-161 — Verify failed login does not clear username unnecessarily

**Priority:** P3

Expected behavior depends on usability requirements.

Password should normally not be exposed.

---

## TS-AUTH-162 — Verify Forgot Password link works

**Priority:** P1

Expected:

Correct password-recovery page opens.

---

## TS-AUTH-163 — Verify registration link works

**Priority:** P2

Expected:

Correct registration page opens.

---

## TS-AUTH-164 — Verify logout control is accessible from authenticated area

**Priority:** P1

Expected:

Customer can intentionally end session.

---

# 19. Cross-Browser Authentication Scenarios

## TS-AUTH-165 — Login using Chrome

**Priority:** P1

Expected:

Login workflow works correctly.

---

## TS-AUTH-166 — Login using Microsoft Edge

**Priority:** P1

Expected:

Login workflow works correctly.

---

## TS-AUTH-167 — Login using Firefox

**Priority:** P1

Expected:

Login workflow works correctly.

---

## TS-AUTH-168 — MFA flow across supported browsers

**Priority:** P1

Expected:

No browser-specific authentication failure.

---

## TS-AUTH-169 — Password reset across supported browsers

**Priority:** P2

Expected:

Reset flow remains functional.

---

## TS-AUTH-170 — Logout and session invalidation across supported browsers

**Priority:** P1

Expected:

Consistent secure behavior.

---

# 20. Responsive Authentication Scenarios

## TS-AUTH-171 — Login from desktop viewport

**Priority:** P2

Expected:

Login form displays correctly.

---

## TS-AUTH-172 — Login from tablet viewport

**Priority:** P2

Expected:

All login controls remain visible and usable.

---

## TS-AUTH-173 — Login from mobile viewport

**Priority:** P1

Expected:

Authentication controls remain accessible.

---

## TS-AUTH-174 — MFA input on mobile viewport

**Priority:** P1

Expected:

OTP entry remains usable.

---

## TS-AUTH-175 — Password-reset flow on mobile viewport

**Priority:** P2

Expected:

No hidden buttons or truncated validation messages.

---

# 21. Error Handling Scenarios

## TS-AUTH-176 — Authentication API unavailable during login

**Priority:** P1

Expected:

User receives safe, understandable failure message.

No false success should be displayed.

---

## TS-AUTH-177 — Authentication API returns server error

**Priority:** P1

Expected:

Application handles failure gracefully.

No internal server details should be exposed.

---

## TS-AUTH-178 — Network disconnect during login

**Priority:** P1

Expected:

Login does not enter inconsistent authenticated state.

---

## TS-AUTH-179 — Network disconnect during OTP verification

**Priority:** P1

Expected:

MFA state remains safe.

---

## TS-AUTH-180 — Network interruption during password reset submission

**Priority:** P1

Expected:

Application prevents ambiguous security state where possible.

---

## TS-AUTH-181 — Slow login response

**Priority:** P2

Expected:

* User receives processing feedback.
* Repeated submission does not cause abnormal behavior.

---

# 22. Audit and Notification Scenarios

## TS-AUTH-182 — Successful login generates expected audit event

**Priority:** P1

Verify when login events are audited.

---

## TS-AUTH-183 — Failed login generates expected security event

**Priority:** P1

Expected:

Relevant failed authentication activity is traceable.

---

## TS-AUTH-184 — Account lockout generates audit/security record

**Priority:** P1

Expected:

Lockout is recorded.

---

## TS-AUTH-185 — Password reset generates expected audit event

**Priority:** P1

Expected:

Security-sensitive change is traceable.

---

## TS-AUTH-186 — Password change generates notification

**Priority:** P1

Expected:

Customer receives security notification where required.

---

## TS-AUTH-187 — Suspicious or new-device login generates notification

**Priority:** P2

Execute where this functionality exists.

---

## TS-AUTH-188 — Logout generates audit record where required

**Priority:** P2

Expected:

Audit behavior matches requirements.

---

# 23. State Transition Scenarios

Authentication states may include:

```text
UNAUTHENTICATED
      ↓
CREDENTIALS_VALIDATED
      ↓
MFA_PENDING
      ↓
AUTHENTICATED
      ↓
SESSION_EXPIRED
```

Possible account-security states:

```text
ACTIVE
  ↓
LOCKED
  ↓
ACTIVE
```

or:

```text
ACTIVE
  ↓
DISABLED
```

---

## TS-AUTH-189 — Valid transition from unauthenticated to authenticated

**Priority:** P0

Expected:

Correct credentials and required MFA result in authenticated state.

---

## TS-AUTH-190 — Invalid transition from unauthenticated directly to authenticated page

**Priority:** P0

Expected:

Access denied.

---

## TS-AUTH-191 — MFA_PENDING user accesses fully authenticated resource

**Priority:** P0

Expected:

Access denied until MFA completion.

---

## TS-AUTH-192 — Session expires from authenticated state

**Priority:** P0

Expected:

Protected requests require reauthentication.

---

## TS-AUTH-193 — Locked account transitions back to active after valid unlock

**Priority:** P1

Expected:

Authentication becomes available only after correct unlock process.

---

## TS-AUTH-194 — Disabled account attempts normal unlock path

**Priority:** P0

Expected:

Disabled status cannot be bypassed by standard lockout recovery.

---

# 24. Boundary Scenarios

## TS-AUTH-195 — Password length minimum minus one

**Priority:** P1

Expected:

Rejected.

---

## TS-AUTH-196 — Password at minimum length

**Priority:** P1

Expected:

Accepted if all other password rules are satisfied.

---

## TS-AUTH-197 — Password minimum plus one

**Priority:** P2

Expected:

Accepted.

---

## TS-AUTH-198 — Password maximum minus one

**Priority:** P2

Expected:

Accepted.

---

## TS-AUTH-199 — Password at maximum length

**Priority:** P1

Expected:

Accepted.

---

## TS-AUTH-200 — Password maximum plus one

**Priority:** P1

Expected:

Rejected safely.

---

## TS-AUTH-201 — Failed login threshold minus one

**Priority:** P1

Expected:

Account remains usable.

---

## TS-AUTH-202 — Failed login exactly at threshold

**Priority:** P0

Expected:

Lockout activates.

---

## TS-AUTH-203 — Failed login threshold plus one

**Priority:** P0

Expected:

Account remains protected.

---

## TS-AUTH-204 — OTP one second before expiry

**Priority:** P1

Expected:

Valid according to exact timing requirements.

---

## TS-AUTH-205 — OTP exactly at expiry boundary

**Priority:** P1

Expected:

Behavior matches defined expiry semantics.

---

## TS-AUTH-206 — OTP immediately after expiry

**Priority:** P0

Expected:

Rejected.

---

# 25. Authentication End-to-End Scenarios

## TS-AUTH-207 — New customer registration to successful login

**Priority:** P1

Flow:

```text
Register
→ Complete required verification
→ Login
→ Complete MFA
→ Dashboard
```

---

## TS-AUTH-208 — Failed login to account lockout

**Priority:** P0

Flow:

```text
Invalid Login Attempts
→ Threshold Reached
→ Account Locked
→ Correct Password Attempt
→ Access Denied
```

---

## TS-AUTH-209 — Forgot password to successful login with new password

**Priority:** P0

Flow:

```text
Forgot Password
→ Reset Request
→ Valid Reset Token
→ Set New Password
→ Login
```

---

## TS-AUTH-210 — Password change to old-password rejection

**Priority:** P0

Flow:

```text
Login
→ Change Password
→ Logout
→ Attempt Old Password
→ Rejected
→ Login With New Password
```

---

## TS-AUTH-211 — Successful login to session timeout

**Priority:** P0

Flow:

```text
Login
→ Use Application
→ Become Idle
→ Session Expires
→ Protected Action
→ Reauthentication Required
```

---

## TS-AUTH-212 — Login to logout and session reuse attempt

**Priority:** P0

Flow:

```text
Login
→ Access Protected Resource
→ Logout
→ Reuse Old URL/Session
→ Access Denied
```

---

# 26. Critical Authentication Smoke Scenarios

The authentication smoke suite should include at minimum:

```text
TS-AUTH-021 — Valid login
TS-AUTH-023 — Invalid password rejected
TS-AUTH-037 — Active customer login
TS-AUTH-058 — Valid MFA
TS-AUTH-104 — Logout
TS-AUTH-107 — Protected URL after logout
TS-AUTH-113 — Anonymous protected access
```

---

# 27. Critical Authentication Regression Scenarios

Critical regression should prioritize:

```text
Valid login
Invalid login
Account lockout
MFA
OTP expiry
OTP reuse
Forgot password
Password reset
Password change
Logout
Session timeout
Post-logout access
Customer/admin role separation
Expired authentication
Account status enforcement
```

---

# 28. Automation Candidates

Strong candidates for Selenium, Cypress, and Playwright:

* Valid login
* Invalid login
* Required-field validation
* Password visibility
* Customer login
* Admin login
* Lockout UI behavior
* MFA flow
* Logout
* Browser Back after logout
* Password reset
* Password change
* Session timeout where practical
* Role-based navigation

---

# 29. API Automation Candidates

Strong candidates for Postman and REST Assured:

* Valid authentication
* Invalid authentication
* Missing credentials
* Invalid token
* Expired token
* Unauthorized endpoint
* Role authorization
* Lockout
* Password reset
* Token reuse
* MFA verification
* Rate-limit validation

---

# 30. Database Validation Candidates

Database validation may include:

* Customer authentication status
* Failed-login counter
* Locked-state persistence
* Password-reset token state
* Audit records
* Session records where stored
* Security-event records

Sensitive password values must never be retrieved or documented in plaintext.

---

# 31. Risk Traceability

Major related risks include:

```text
RISK-005  Authentication bypass
RISK-014  Session remains valid after logout
RISK-015  Expired session accepted
RISK-016  Account lockout fails
RISK-021  Sensitive information exposure
RISK-030  Unauthorized API request
RISK-044  OTP reuse
RISK-045  Expired OTP accepted
RISK-046  Password reset token reuse
```

These scenarios therefore receive high regression and automation priority.

---

# 32. Authentication Coverage Summary

This scenario set covers:

* Registration
* Valid login
* Invalid login
* Input validation
* Account states
* Lockout
* MFA
* OTP
* Forgot password
* Password reset
* Password change
* Logout
* Sessions
* Remember Me
* Multiple sessions
* Role authorization
* Security behavior
* Usability
* Cross-browser
* Responsive behavior
* Error handling
* Audit logging
* State transitions
* Boundaries
* End-to-end flows

---

# 33. Final Authentication Testing Principle

Authentication testing must validate more than whether a valid customer can log in.

The critical questions are:

```text
Can an invalid user get in?

Can a locked user get in?

Can MFA be bypassed?

Can an expired credential still be used?

Can a customer access admin functionality?

Does logout actually terminate access?

Can an old session be reused?

Can a password-reset token be reused?

Can another user's session or resources be accessed?
```

Authentication is the first security boundary of the Banking System and should therefore receive extensive manual, API, automation, and regression coverage.
