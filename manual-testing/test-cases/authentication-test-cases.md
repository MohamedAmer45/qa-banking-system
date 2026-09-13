# Banking System — Authentication Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Authentication                 |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Authentication scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Registration
* Login
* Invalid credentials
* Account lockout
* MFA
* OTP lifecycle
* Password reset
* Password change
* Logout
* Session timeout
* Session revocation
* Concurrent sessions
* Remember Me
* Authentication authorization boundaries
* Security behavior
* API validation
* Audit and notifications
* Cross-browser behavior
* Accessibility
* Boundary testing

---

# 3. Test Case ID Convention

Authentication test cases use:

```text
AUTH-TC-XXX
```

Example:

```text
AUTH-TC-001
AUTH-TC-002
AUTH-TC-003
```

---

# 4. Priority Definitions

```text
P0 — Critical banking/security path

P1 — High-value functional/security path

P2 — Secondary functionality

P3 — Cosmetic/low-risk
```

---

# 5. Common Test Data

## Active Customer

```text
Customer:
CUST-001

Status:
ACTIVE

KYC:
VERIFIED

MFA:
ENABLED
```

## Customer Without MFA

```text
Customer:
CUST-002

Status:
ACTIVE

MFA:
DISABLED
```

## Locked Customer

```text
Customer:
CUST-003

Status:
LOCKED
```

## Disabled Customer

```text
Customer:
CUST-004

Status:
DISABLED
```

## Suspended Customer

```text
Customer:
CUST-005

Status:
SUSPENDED
```

## Closed Customer

```text
Customer:
CUST-006

Status:
CLOSED
```

Use synthetic credentials only.

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Authentication service is healthy.

QA environment is accessible.

Customer exists in test data.

Browser cache/session is cleared before isolated authentication tests.

No real customer credentials are used.
```

---

# 7. Registration Test Cases

## AUTH-TC-001 — Register With Valid Data

**Priority:** P1
**Type:** Positive
**Requirement:** REQ-AUTH registration flow
**Automation:** Playwright / Cypress

### Preconditions

User email is not registered.

### Steps

1. Open registration page.
2. Enter valid first name.
3. Enter valid last name.
4. Enter unique valid email.
5. Enter valid password.
6. Confirm password.
7. Accept terms.
8. Submit registration.

### Expected Result

* Registration request succeeds.
* Customer record is created once.
* User receives expected verification/onboarding flow.
* Password is not exposed in UI or response.
* User is not assigned privileged roles.

### Status

```text
NOT_RUN
```

---

## AUTH-TC-002 — Register With Existing Email

**Priority:** P1
**Type:** Negative

### Preconditions

Email already belongs to an existing customer.

### Steps

1. Open registration.
2. Enter valid customer information.
3. Enter already-registered email.
4. Submit.

### Expected Result

* Duplicate account is not created.
* Appropriate error is shown.
* Existing account remains unchanged.
* Response does not expose unnecessary account information.

---

## AUTH-TC-003 — Register With Invalid Email Format

**Priority:** P2
**Type:** Negative / Equivalence Partitioning

### Steps

Test representative invalid values:

```text
user
user@
@example.com
user@example
user example@example.com
```

### Expected Result

Registration is rejected with clear validation.

---

## AUTH-TC-004 — Register With Empty Required Fields

**Priority:** P2
**Type:** Negative

### Steps

Submit registration with required fields empty.

### Expected Result

* Registration does not proceed.
* Required-field errors are shown.
* No customer is created.

---

## AUTH-TC-005 — Password and Confirmation Do Not Match

**Priority:** P1
**Type:** Negative

### Steps

1. Enter valid registration details.
2. Enter valid password.
3. Enter a different confirmation password.
4. Submit.

### Expected Result

Registration is rejected.

---

## AUTH-TC-006 — Terms Not Accepted

**Priority:** P2

### Steps

Submit otherwise-valid registration without accepting required terms.

### Expected Result

Registration does not complete.

---

## AUTH-TC-007 — Duplicate Registration Submission

**Priority:** P1
**Type:** Idempotency

### Steps

1. Complete valid registration.
2. Rapidly submit form twice.

### Expected Result

* One customer account only.
* Duplicate account is not created.
* Duplicate verification workflows are prevented where applicable.

---

# 8. Valid Login Test Cases

## AUTH-TC-008 — Login With Valid Credentials and MFA

**Priority:** P0
**Requirement:** REQ-AUTH-001 / REQ-AUTH-003
**Risk:** RISK-005
**Automation:** Playwright / Selenium / REST Assured

### Preconditions

```text
Customer:
CUST-001

Status:
ACTIVE

MFA:
ENABLED
```

### Steps

1. Open login page.
2. Enter valid username/email.
3. Enter valid password.
4. Submit.
5. Complete valid MFA challenge.

### Expected Result

* Primary credentials accepted.
* MFA challenge required.
* User is authenticated only after MFA succeeds.
* Authenticated session is created.
* User reaches authorized dashboard.

---

## AUTH-TC-009 — Login With Valid Credentials Without MFA

**Priority:** P0

### Preconditions

Customer has MFA disabled and policy permits password-only login.

### Expected Result

Customer authenticates successfully according to configured authentication policy.

---

## AUTH-TC-010 — Login Using Enter Key

**Priority:** P2

### Steps

1. Enter valid credentials.
2. Press Enter instead of clicking Login.

### Expected Result

Login submission behaves consistently with Login button.

---

## AUTH-TC-011 — Login After Previous Logout

**Priority:** P1

### Steps

1. Login successfully.
2. Logout.
3. Return to login page.
4. Login again.

### Expected Result

New authentication succeeds with a new valid session.

---

# 9. Invalid Credential Test Cases

## AUTH-TC-012 — Valid Email / Invalid Password

**Priority:** P0
**Risk:** RISK-005

### Expected Result

* Login denied.
* No authenticated session created.
* Failure count increments according to policy.
* Error does not reveal sensitive details.

---

## AUTH-TC-013 — Invalid Email / Arbitrary Password

**Priority:** P1

### Expected Result

Login denied.

Error behavior should not unnecessarily reveal whether the account exists.

---

## AUTH-TC-014 — Empty Username

**Priority:** P2

### Expected Result

Login not submitted or rejected with validation.

---

## AUTH-TC-015 — Empty Password

**Priority:** P2

### Expected Result

Login rejected.

---

## AUTH-TC-016 — Empty Username and Password

**Priority:** P2

### Expected Result

No authentication request results in a session.

---

## AUTH-TC-017 — Leading/Trailing Whitespace in Email

**Priority:** P2

### Steps

Use:

```text
" user@banktest.local "
```

### Expected Result

Behavior follows defined normalization policy.

If trimming is supported, correct account is resolved safely.

---

## AUTH-TC-018 — Password Case Sensitivity

**Priority:** P1

### Steps

Use correct password with altered letter case.

### Expected Result

Login rejected.

Passwords remain case-sensitive.

---

# 10. Account State Test Cases

## AUTH-TC-019 — Login With Locked Account

**Priority:** P0
**Risk:** RISK-016

### Preconditions

Customer status:

```text
LOCKED
```

### Expected Result

Authentication is denied according to lockout policy.

---

## AUTH-TC-020 — Login With Disabled Account

**Priority:** P0

### Preconditions

```text
Status:
DISABLED
```

### Expected Result

Login denied even with valid credentials.

---

## AUTH-TC-021 — Login With Suspended Account

**Priority:** P0

### Expected Result

Behavior follows defined suspension policy.

If suspended users may not authenticate, login is denied.

---

## AUTH-TC-022 — Login With Closed Account

**Priority:** P0

### Expected Result

Closed customer cannot establish active banking session.

---

## AUTH-TC-023 — Account State Changes During Login

**Priority:** P0
**Type:** Concurrency / State

### Steps

1. Enter valid credentials.
2. Before final authentication completes, admin disables customer.
3. Complete MFA.

### Expected Result

Final authentication revalidates customer status and rejects session if state no longer permits login.

---

# 11. Login Lockout Test Cases

## AUTH-TC-024 — Failed Attempts Below Lockout Threshold

**Priority:** P1
**Risk:** RISK-016

### Preconditions

Lockout threshold configured.

### Steps

Perform invalid login attempts below threshold.

### Expected Result

* Login denied.
* Account remains eligible for valid authentication.
* Failure count updates correctly.

---

## AUTH-TC-025 — Exact Lockout Threshold

**Priority:** P0
**Type:** Boundary Value Analysis

### Steps

Perform exactly the configured number of failed attempts.

### Expected Result

Account becomes locked according to policy.

---

## AUTH-TC-026 — Threshold Minus One

**Priority:** P1
**Type:** BVA

### Expected Result

Account should not yet be locked.

---

## AUTH-TC-027 — Threshold Plus One

**Priority:** P1
**Type:** BVA

### Expected Result

Account remains locked.

Additional attempts should not bypass lockout.

---

## AUTH-TC-028 — Valid Password After Lockout

**Priority:** P0

### Expected Result

Valid credentials do not bypass active lockout.

---

## AUTH-TC-029 — Lockout Expiry

**Priority:** P1

### Preconditions

Temporary lockout policy enabled.

### Steps

1. Trigger lockout.
2. Wait until configured lockout expires.
3. Login with valid credentials.

### Expected Result

User can authenticate after expiration where policy permits.

---

## AUTH-TC-030 — Lockout Audit Event

**Priority:** P1

### Expected Result

Required security/audit event is recorded without exposing password or secret data.

---

# 12. MFA Test Cases

## AUTH-TC-031 — Valid MFA OTP

**Priority:** P0
**Requirement:** REQ-AUTH-003

### Steps

1. Submit valid primary credentials.
2. Enter current valid OTP.

### Expected Result

Authentication completes.

---

## AUTH-TC-032 — Invalid MFA OTP

**Priority:** P0
**Requirement:** REQ-AUTH-004

### Expected Result

* MFA rejected.
* Session remains unauthenticated.
* Protected resources remain inaccessible.

---

## AUTH-TC-033 — Empty MFA OTP

**Priority:** P1

### Expected Result

MFA not accepted.

---

## AUTH-TC-034 — Expired MFA OTP

**Priority:** P0
**Requirement:** REQ-AUTH-005
**Risk:** RISK-045

### Expected Result

Expired code is rejected.

---

## AUTH-TC-035 — Reuse Successful MFA OTP

**Priority:** P0
**Requirement:** REQ-AUTH-006
**Risk:** RISK-044

### Steps

1. Authenticate successfully with OTP.
2. Attempt to reuse same OTP in another authentication attempt.

### Expected Result

Previously consumed OTP is rejected if policy defines single use.

---

## AUTH-TC-036 — Previous OTP After Resend

**Priority:** P0
**Risk:** RISK-044

### Steps

1. Request OTP A.
2. Request resend.
3. Receive OTP B.
4. Submit OTP A.

### Expected Result

Behavior follows OTP invalidation policy.

If resend invalidates previous code, OTP A is rejected.

---

## AUTH-TC-037 — New OTP After Resend

**Priority:** P0

### Expected Result

Newest valid OTP is accepted.

---

## AUTH-TC-038 — MFA Attempts Exceed Allowed Limit

**Priority:** P0

### Steps

Submit invalid OTP repeatedly until policy limit is reached.

### Expected Result

System applies expected protection:

* MFA challenge lock
* Authentication restart
* Temporary restriction
* or equivalent configured behavior

No bypass occurs.

---

## AUTH-TC-039 — MFA Input Boundary

**Priority:** P1
**Type:** BVA

If OTP length is 6 digits, test:

```text
5 digits
6 digits
7 digits
```

### Expected Result

Only structurally valid length accepted for processing.

---

## AUTH-TC-040 — Non-Numeric OTP When Numeric Required

**Priority:** P2

### Expected Result

Invalid input rejected safely.

---

# 13. MFA Bypass / Security Test Cases

## AUTH-TC-041 — Protected UI Before MFA Completion

**Priority:** P0
**Risk:** RISK-005

### Steps

1. Enter valid username/password.
2. Stop at MFA page.
3. Manually navigate to `/accounts`.

### Expected Result

Access denied/redirected to MFA or login.

---

## AUTH-TC-042 — Protected API Before MFA Completion

**Priority:** P0
**Risk:** RISK-005, RISK-030
**Automation:** REST Assured

### Steps

1. Complete password step.
2. Capture temporary/pre-MFA authentication context.
3. Call protected Accounts API.

### Expected Result

Protected API rejects request.

---

## AUTH-TC-043 — Transfer API Before MFA Completion

**Priority:** P0

### Expected Result

Financial endpoint cannot be used prior to completed authentication.

---

## AUTH-TC-044 — Modify Client MFA Flag

**Priority:** P0
**Type:** Client Trust

### Steps

Attempt to manipulate browser/client state indicating MFA success.

### Expected Result

Server-side authentication state remains authoritative.

Protected actions remain denied.

---

# 14. Forgot Password Test Cases

## AUTH-TC-045 — Forgot Password With Registered Email

**Priority:** P1

### Steps

1. Select Forgot Password.
2. Enter registered email.
3. Submit.

### Expected Result

Reset workflow is initiated according to policy.

---

## AUTH-TC-046 — Forgot Password With Unknown Email

**Priority:** P1
**Type:** Enumeration

### Expected Result

Response should avoid unnecessary account-existence disclosure.

---

## AUTH-TC-047 — Empty Forgot Password Email

**Priority:** P2

### Expected Result

Validation prevents submission.

---

## AUTH-TC-048 — Invalid Forgot Password Email Format

**Priority:** P2

### Expected Result

Request rejected with appropriate validation.

---

## AUTH-TC-049 — Repeated Reset Requests

**Priority:** P1

### Expected Result

System handles repeated requests safely according to throttling/token policy.

---

# 15. Password Reset Token Test Cases

## AUTH-TC-050 — Valid Password Reset Token

**Priority:** P0
**Requirement:** REQ-AUTH-009

### Steps

1. Request reset.
2. Use valid reset token.
3. Enter valid new password.
4. Confirm reset.

### Expected Result

* Password changes.
* Token becomes consumed.
* Old password no longer authenticates.
* New password authenticates.

---

## AUTH-TC-051 — Invalid Reset Token

**Priority:** P0

### Expected Result

Reset denied.

---

## AUTH-TC-052 — Expired Reset Token

**Priority:** P0
**Requirement:** REQ-AUTH-010
**Risk:** RISK-046

### Expected Result

Token rejected.

---

## AUTH-TC-053 — Reuse Consumed Reset Token

**Priority:** P0
**Requirement:** REQ-AUTH-011
**Risk:** RISK-046

### Expected Result

Consumed token cannot be reused.

---

## AUTH-TC-054 — New Reset Request Invalidates Older Token

**Priority:** P1

### Steps

1. Request Token A.
2. Request Token B.
3. Attempt reset with Token A.

### Expected Result

Behavior follows defined reset-token invalidation policy.

---

## AUTH-TC-055 — Tampered Reset Token

**Priority:** P0

### Steps

Modify one or more token characters.

### Expected Result

Reset denied securely.

No information about valid token structure should facilitate attack.

---

## AUTH-TC-056 — Reset Password to Same Existing Password

**Priority:** P2

### Expected Result

Behavior follows password policy.

If password reuse is prohibited, reset is rejected.

---

# 16. Password Policy Test Cases

## AUTH-TC-057 — Password Minimum Length Minus One

**Priority:** P1
**Type:** BVA

### Expected Result

Rejected.

---

## AUTH-TC-058 — Password Exactly Minimum Length

**Priority:** P1

### Expected Result

Accepted if all other password rules pass.

---

## AUTH-TC-059 — Password Maximum Length

**Priority:** P2

### Expected Result

Accepted if valid and within supported maximum.

---

## AUTH-TC-060 — Password Maximum Length Plus One

**Priority:** P2

### Expected Result

Rejected or safely constrained according to documented policy.

---

## AUTH-TC-061 — Weak Password

**Priority:** P1

### Expected Result

Rejected if it violates configured complexity/security requirements.

---

## AUTH-TC-062 — Password With Supported Special Characters

**Priority:** P2

### Expected Result

Valid supported password accepted without corruption or unsafe escaping.

---

## AUTH-TC-063 — Unicode Password Handling

**Priority:** P2

### Expected Result

Behavior follows explicitly supported password-character policy.

No encoding corruption.

---

# 17. Change Password Test Cases

## AUTH-TC-064 — Change Password Successfully

**Priority:** P0

### Preconditions

User authenticated.

### Steps

1. Open security settings.
2. Enter current password.
3. Enter valid new password.
4. Confirm.
5. Submit.

### Expected Result

* Password changes.
* Old password fails subsequently.
* New password succeeds.
* Security notification generated where required.

---

## AUTH-TC-065 — Incorrect Current Password

**Priority:** P0

### Expected Result

Password remains unchanged.

---

## AUTH-TC-066 — New Password Confirmation Mismatch

**Priority:** P1

### Expected Result

Change rejected.

---

## AUTH-TC-067 — Existing Session Handling After Password Change

**Priority:** P0
**Risk:** RISK-014

### Preconditions

Sessions A and B are active.

### Steps

1. Change password in Session A.
2. Use Session B.

### Expected Result

Behavior follows session-revocation policy.

If all other sessions must be revoked, Session B is rejected.

---

## AUTH-TC-068 — Old Password After Change

**Priority:** P0

### Expected Result

Old password can no longer authenticate.

---

# 18. Logout Test Cases

## AUTH-TC-069 — Successful Logout

**Priority:** P0
**Requirement:** REQ-AUTH-012
**Risk:** RISK-014

### Steps

1. Login.
2. Logout.
3. Attempt to access protected account page.

### Expected Result

* Session revoked.
* Protected page inaccessible.
* Login required.

---

## AUTH-TC-070 — Browser Back After Logout

**Priority:** P0

### Steps

1. Login.
2. Visit account page.
3. Logout.
4. Press browser Back.

### Expected Result

Protected content must not become usable.

Sensitive content should not remain improperly accessible from cache.

---

## AUTH-TC-071 — Reuse Session Token After Logout

**Priority:** P0
**Automation:** REST Assured

### Steps

1. Authenticate.
2. Capture session/token.
3. Logout.
4. Call protected API using old token.

### Expected Result

Old revoked authentication context is rejected according to token/session design.

---

## AUTH-TC-072 — Logout in Tab A / Use Tab B

**Priority:** P0

### Steps

1. Login.
2. Open Tab A and Tab B.
3. Logout in Tab A.
4. Perform protected action in Tab B.

### Expected Result

Session revocation policy enforced consistently.

If logout revokes shared session, action is denied.

---

# 19. Session Timeout Test Cases

## AUTH-TC-073 — Session Before Timeout

**Priority:** P1
**Type:** Boundary

### Steps

Perform protected action just before configured timeout.

### Expected Result

Session remains valid if inactivity limit has not been reached.

---

## AUTH-TC-074 — Session At/After Timeout

**Priority:** P0
**Requirement:** REQ-AUTH-013
**Risk:** RISK-015

### Steps

1. Login.
2. Remain inactive through timeout.
3. Attempt protected action.

### Expected Result

* Session expired.
* Protected action rejected.
* Re-authentication required.

---

## AUTH-TC-075 — Financial Action After Timeout

**Priority:** P0

### Steps

1. Prepare transfer form.
2. Allow session to expire.
3. Submit transfer.

### Expected Result

Transfer is rejected.

No financial effect occurs.

---

## AUTH-TC-076 — API Request With Expired Session

**Priority:** P0

### Expected Result

Protected API rejects expired credentials/session.

---

## AUTH-TC-077 — Timeout Warning

**Priority:** P2

### Expected Result

If product supports warning, it appears according to policy and does not extend the session unexpectedly without user activity.

---

# 20. Session Revocation Test Cases

## AUTH-TC-078 — Admin Revokes Customer Session

**Priority:** P0

### Steps

1. Customer logs in.
2. Authorized admin revokes session.
3. Customer performs protected action.

### Expected Result

Action denied.

---

## AUTH-TC-079 — Customer Revokes Another Device

**Priority:** P1

### Preconditions

Trusted-device/session management supported.

### Expected Result

Selected target session is revoked while intended current session behaves according to policy.

---

## AUTH-TC-080 — Revoked Session API Use

**Priority:** P0
**Requirement:** REQ-AUTH-014

### Expected Result

Revoked session cannot access protected API.

---

# 21. Concurrent Session Test Cases

## AUTH-TC-081 — Login From Two Browsers

**Priority:** P1

### Steps

1. Login from Browser A.
2. Login from Browser B.

### Expected Result

Behavior follows concurrent-session policy.

If both are allowed, both remain securely isolated.

---

## AUTH-TC-082 — Logout One Concurrent Session

**Priority:** P1

### Expected Result

Behavior matches configured session model:

* Current-session-only logout, or
* Global logout

No ambiguous state.

---

## AUTH-TC-083 — Password Change With Multiple Sessions

**Priority:** P0

### Expected Result

Configured session-security policy is enforced across all active sessions.

---

# 22. Remember Me Test Cases

## AUTH-TC-084 — Remember Me Enabled

**Priority:** P2

### Preconditions

Feature supported.

### Expected Result

Authentication persistence follows configured duration and security rules.

---

## AUTH-TC-085 — Remember Me Disabled

**Priority:** P2

### Expected Result

Session does not persist beyond expected nonpersistent-session behavior.

---

## AUTH-TC-086 — Remembered Session After Password Change

**Priority:** P0

### Expected Result

Remembered session/token follows password-change revocation policy.

---

## AUTH-TC-087 — Remembered Session After Account Disable

**Priority:** P0

### Expected Result

Disabled account cannot continue using stale remembered authentication.

---

# 23. Authentication Authorization Test Cases

## AUTH-TC-088 — Unauthenticated Account Page

**Priority:** P0

### Steps

Navigate directly to protected account URL.

### Expected Result

Denied or redirected to login.

---

## AUTH-TC-089 — Unauthenticated Transfer Page

**Priority:** P0

### Expected Result

No transfer functionality accessible.

---

## AUTH-TC-090 — Unauthenticated Statement Download

**Priority:** P0
**Risk:** RISK-002, RISK-021

### Expected Result

Statement is not returned.

---

## AUTH-TC-091 — Unauthenticated Admin Endpoint

**Priority:** P0

### Expected Result

Denied.

---

## AUTH-TC-092 — Authenticated Customer Calls Admin Endpoint

**Priority:** P0
**Risk:** RISK-006, RISK-030

### Expected Result

Denied.

Authentication alone does not grant admin authorization.

---

# 24. API Authentication Test Cases

## AUTH-TC-093 — API With Valid Authentication

**Priority:** P0
**Automation:** REST Assured / Postman

### Expected Result

Authorized resource request succeeds.

---

## AUTH-TC-094 — API Without Authentication

**Priority:** P0

### Expected Result

Protected API rejects request.

Typical result:

```text
401 Unauthorized
```

according to API specification.

---

## AUTH-TC-095 — API With Invalid Token

**Priority:** P0

### Expected Result

Request rejected.

---

## AUTH-TC-096 — API With Expired Token

**Priority:** P0

### Expected Result

Request rejected.

---

## AUTH-TC-097 — API With Revoked Token

**Priority:** P0

### Expected Result

Request rejected according to token/session architecture.

---

## AUTH-TC-098 — Malformed Authorization Header

**Priority:** P1

### Expected Result

Request rejected safely.

No server error or protected information exposure.

---

# 25. Enumeration / Error Message Test Cases

## AUTH-TC-099 — Existing Email / Wrong Password Error

**Priority:** P1

### Expected Result

Authentication failure message does not expose unnecessary account-existence information.

---

## AUTH-TC-100 — Unknown Email Error

**Priority:** P1

### Expected Result

Response is consistent enough to reduce account enumeration risk.

---

## AUTH-TC-101 — Forgot Password Enumeration

**Priority:** P1

### Steps

Compare reset request for:

```text
Registered Email

Unregistered Email
```

### Expected Result

User-visible response does not reveal unnecessary account existence.

---

## AUTH-TC-102 — Timing Difference Between Existing and Unknown User

**Priority:** P2
**Type:** Security Observation

### Expected Result

No obvious exploitable timing distinction should be intentionally introduced.

This is QA-level observation, not a full side-channel assessment.

---

# 26. Input Security Test Cases

## AUTH-TC-103 — SQL-Like Input in Username

**Priority:** P1

### Test Data

```text
' OR '1'='1
```

### Expected Result

* Authentication not bypassed.
* Input treated safely.
* No database error exposed.

---

## AUTH-TC-104 — Script-Like Input

**Priority:** P1

### Test Data

```text
<script>alert(1)</script>
```

### Expected Result

* No script executes.
* Input safely handled.
* No authentication bypass.

---

## AUTH-TC-105 — Very Long Username Input

**Priority:** P2
**Type:** Boundary / Robustness

### Expected Result

Application handles input safely without crash or unexpected authentication behavior.

---

## AUTH-TC-106 — Very Long Password Input

**Priority:** P2

### Expected Result

Application handles request safely according to documented max length.

---

# 27. Rate Limiting / Abuse Protection

## AUTH-TC-107 — High-Rate Invalid Login Requests

**Priority:** P0

### Expected Result

Configured abuse controls are applied without allowing authentication bypass.

Possible behavior:

* Lockout
* Rate limiting
* Progressive delay
* temporary challenge

according to design.

---

## AUTH-TC-108 — High-Rate Password Reset Requests

**Priority:** P1

### Expected Result

Reset endpoint resists abuse according to rate-limit policy.

---

## AUTH-TC-109 — High-Rate OTP Resend

**Priority:** P1

### Expected Result

OTP resend throttling/business rules enforced.

---

# 28. Audit and Notification Test Cases

## AUTH-TC-110 — Successful Login Audit

**Priority:** P2

### Expected Result

Where required, login event contains appropriate actor/time/context without sensitive secret exposure.

---

## AUTH-TC-111 — Failed Login Audit

**Priority:** P2

### Expected Result

Security event is recorded according to policy.

Passwords must never be logged.

---

## AUTH-TC-112 — Account Lockout Notification

**Priority:** P1

### Expected Result

Required security notification is generated accurately.

---

## AUTH-TC-113 — Password Change Notification

**Priority:** P1

### Expected Result

Customer receives correct security alert according to notification policy.

---

## AUTH-TC-114 — Password Reset Notification

**Priority:** P1

### Expected Result

Expected security notification is created without exposing reset token/password.

---

## AUTH-TC-115 — MFA Change Notification

**Priority:** P1

### Expected Result

Security alert generated where required.

---

# 29. Sensitive Data Test Cases

## AUTH-TC-116 — Password Not Returned by API

**Priority:** P0
**Risk:** RISK-021

### Expected Result

No authentication API response contains plaintext password.

---

## AUTH-TC-117 — OTP Not Exposed in Logs

**Priority:** P0

### Expected Result

OTP value is absent or appropriately redacted in accessible application/audit logs.

---

## AUTH-TC-118 — Access Token Not Exposed in URL

**Priority:** P0

### Expected Result

Sensitive tokens are not placed in browser URL/query parameters where avoidable.

---

## AUTH-TC-119 — Password Not Stored in Browser-Visible Application State

**Priority:** P1

### Expected Result

Application does not unnecessarily retain plaintext password in page state/storage.

---

# 30. Browser Storage Test Cases

## AUTH-TC-120 — Sensitive Authentication Data in Local Storage

**Priority:** P1

### Expected Result

Storage behavior matches secure application design.

Passwords, OTPs, and unnecessary secrets are not stored.

---

## AUTH-TC-121 — Authentication State After Clearing Storage

**Priority:** P2

### Expected Result

System handles cleared client storage gracefully and safely.

---

# 31. Browser Navigation Test Cases

## AUTH-TC-122 — Refresh MFA Page

**Priority:** P2

### Expected Result

MFA challenge remains valid or restarts safely according to design.

No authentication bypass.

---

## AUTH-TC-123 — Back From MFA to Login

**Priority:** P2

### Expected Result

Navigation does not accidentally authenticate user.

---

## AUTH-TC-124 — Refresh After Successful Login

**Priority:** P2

### Expected Result

Valid authenticated session persists correctly.

---

## AUTH-TC-125 — Direct Login Page While Authenticated

**Priority:** P2

### Expected Result

Behavior follows product design without creating additional or broken sessions.

---

# 32. Cross-Browser Test Cases

## AUTH-TC-126 — Login in Chrome

**Priority:** P1

Expected: authentication flow works.

---

## AUTH-TC-127 — Login in Edge

**Priority:** P1

Expected: authentication flow works.

---

## AUTH-TC-128 — Login in Firefox

**Priority:** P1

Expected: authentication flow works.

---

## AUTH-TC-129 — Login in WebKit

**Priority:** P1

Expected: authentication flow works.

---

## AUTH-TC-130 — MFA Across Supported Browsers

**Priority:** P1

### Expected Result

MFA behavior and security semantics remain consistent.

---

# 33. Responsive Test Cases

## AUTH-TC-131 — Login at 390×844

**Priority:** P2

### Expected Result

* Fields visible.
* Login button reachable.
* Errors readable.
* No field overlap.

---

## AUTH-TC-132 — Login at 360×800

**Priority:** P2

### Expected Result

Critical login controls remain usable.

---

## AUTH-TC-133 — MFA at Mobile Viewport

**Priority:** P1

### Expected Result

OTP input and submission remain visible and usable.

---

# 34. Accessibility Test Cases

## AUTH-TC-134 — Keyboard-Only Login

**Priority:** P1

### Steps

Complete login using keyboard only.

### Expected Result

All required controls are reachable in logical order.

---

## AUTH-TC-135 — Visible Focus State

**Priority:** P2

### Expected Result

Focused fields/buttons have perceivable focus indicator.

---

## AUTH-TC-136 — Login Field Labels

**Priority:** P2

### Expected Result

Username/email and password inputs have clear accessible labels.

---

## AUTH-TC-137 — Error Message Association

**Priority:** P2

### Expected Result

Validation errors are clearly associated with affected fields.

---

# 35. Authentication Concurrency Test Cases

## AUTH-TC-138 — Simultaneous Login Attempts With Same Account

**Priority:** P1

### Expected Result

Authentication state remains consistent and follows concurrent-session policy.

---

## AUTH-TC-139 — Valid Login Concurrent With Account Disable

**Priority:** P0

### Expected Result

Final authentication checks authoritative customer status.

Disabled account does not receive usable protected session.

---

## AUTH-TC-140 — Password Reset Concurrent With Login

**Priority:** P1

### Expected Result

Result follows defined credential/session policy without ambiguous or insecure state.

---

## AUTH-TC-141 — Two Password Reset Requests Used Concurrently

**Priority:** P0

### Expected Result

Reset-token lifecycle remains consistent.

Only valid token(s) according to policy can change password.

---

# 36. Session Fixation / Session Rotation Test Cases

## AUTH-TC-142 — Session Identifier Changes After Login

**Priority:** P0

### Expected Result

Authentication does not retain unsafe pre-authenticated session state if session rotation is part of architecture.

---

## AUTH-TC-143 — Session Identifier Changes After Privilege Change

**Priority:** P1

Where applicable.

### Expected Result

Authentication/session state safely reflects role/security changes.

---

# 37. Authentication State Consistency Test Cases

## AUTH-TC-144 — UI Says Logged Out / API Still Authenticated

**Priority:** P0

### Expected Result

Logout state remains consistent across UI and backend authorization.

---

## AUTH-TC-145 — UI Says Logged In / API Session Expired

**Priority:** P1

### Expected Result

Application handles expired backend session gracefully and requires re-authentication.

---

## AUTH-TC-146 — Session State Across Multiple Tabs

**Priority:** P1

### Expected Result

Tabs converge on authoritative session state according to product policy.

---

# 38. Critical Authentication End-to-End Test

## AUTH-TC-147 — Complete Secure Login / Logout Journey

**Priority:** P0
**Automation:** Playwright

### Preconditions

```text
Customer:
CUST-001

MFA:
ENABLED
```

### Steps

1. Open login page.
2. Enter valid credentials.
3. Submit.
4. Verify MFA required.
5. Enter valid MFA code.
6. Verify dashboard access.
7. Open account page.
8. Logout.
9. Revisit account page.
10. Attempt protected API request using old session.

### Expected Result

```text
Credentials accepted.

MFA enforced.

Authenticated session created only after MFA.

Owned account accessible.

Logout succeeds.

Protected UI inaccessible after logout.

Old session cannot perform protected API request.
```

### Status

```text
NOT_RUN
```

---

# 39. Password Reset End-to-End Test

## AUTH-TC-148 — Complete Password Recovery Journey

**Priority:** P0

### Steps

1. Request password reset.
2. Receive valid reset token through test channel.
3. Open reset flow.
4. Set valid new password.
5. Attempt login using old password.
6. Attempt login using new password.
7. Attempt to reuse reset token.

### Expected Result

```text
Reset completes.

Old password rejected.

New password accepted.

Consumed token rejected.

Security notification created where applicable.
```

---

# 40. Lockout End-to-End Test

## AUTH-TC-149 — Failed Attempts → Lockout → Recovery

**Priority:** P0

### Steps

1. Perform invalid login attempts up to threshold.
2. Verify lockout.
3. Attempt valid login while locked.
4. Recover/unlock according to configured process.
5. Login again.

### Expected Result

Lockout and recovery follow business/security policy without bypass.

---

# 41. MFA End-to-End Test

## AUTH-TC-150 — MFA Resend Lifecycle

**Priority:** P0

### Steps

1. Login with valid credentials.
2. Receive OTP A.
3. Request resend.
4. Receive OTP B.
5. Test OTP A.
6. Test OTP B.

### Expected Result

Behavior matches documented OTP lifecycle.

No simultaneous unexpected valid codes beyond allowed policy.

---

# 42. Authentication Risk Mapping

| Risk                                     | Related Test Cases            |
| ---------------------------------------- | ----------------------------- |
| RISK-005 Authentication bypass           | AUTH-TC-008, 041–044, 088–098 |
| RISK-014 Session after logout/revocation | AUTH-TC-067, 069–072, 078–080 |
| RISK-015 Expired session accepted        | AUTH-TC-073–077               |
| RISK-016 Lockout failure                 | AUTH-TC-024–030               |
| RISK-021 Sensitive exposure              | AUTH-TC-090, 116–121          |
| RISK-030 Unauthorized API access         | AUTH-TC-042, 092–098          |
| RISK-044 OTP reuse                       | AUTH-TC-035–038, 150          |
| RISK-045 Expired OTP                     | AUTH-TC-034                   |
| RISK-046 Reset-token reuse               | AUTH-TC-050–055               |
| RISK-037 Frontend-only validation        | AUTH-TC-042–044, 093–106      |

---

# 43. Requirements Mapping

| Requirement                               | Test Cases                |
| ----------------------------------------- | ------------------------- |
| REQ-AUTH-001 Valid login                  | AUTH-TC-008–011           |
| REQ-AUTH-002 Invalid credentials rejected | AUTH-TC-012–018           |
| REQ-AUTH-003 MFA required                 | AUTH-TC-008, 031, 041–044 |
| REQ-AUTH-004 Invalid MFA rejected         | AUTH-TC-032               |
| REQ-AUTH-005 Expired MFA rejected         | AUTH-TC-034               |
| REQ-AUTH-006 MFA reuse prevented          | AUTH-TC-035–038           |
| REQ-AUTH-007 Lockout threshold            | AUTH-TC-024–027           |
| REQ-AUTH-008 Locked login denied          | AUTH-TC-028               |
| REQ-AUTH-009 Valid reset token            | AUTH-TC-050               |
| REQ-AUTH-010 Expired reset token          | AUTH-TC-052               |
| REQ-AUTH-011 Used reset token             | AUTH-TC-053               |
| REQ-AUTH-012 Logout invalidation          | AUTH-TC-069–072           |
| REQ-AUTH-013 Expired session denied       | AUTH-TC-073–077           |
| REQ-AUTH-014 Revoked session denied       | AUTH-TC-078–080           |

---

# 44. Smoke Candidates

Recommended authentication smoke coverage:

```text
AUTH-TC-008
AUTH-TC-012
AUTH-TC-031
AUTH-TC-041
AUTH-TC-069
AUTH-TC-088
```

---

# 45. Sanity Candidates

After authentication changes:

```text
AUTH-TC-008
AUTH-TC-012
AUTH-TC-019
AUTH-TC-025
AUTH-TC-031
AUTH-TC-034
AUTH-TC-050
AUTH-TC-064
AUTH-TC-069
AUTH-TC-074
```

---

# 46. Critical Regression Candidates

```text
AUTH-TC-008
AUTH-TC-012
AUTH-TC-019
AUTH-TC-025
AUTH-TC-028
AUTH-TC-031
AUTH-TC-032
AUTH-TC-034
AUTH-TC-035
AUTH-TC-041
AUTH-TC-042
AUTH-TC-050
AUTH-TC-052
AUTH-TC-053
AUTH-TC-064
AUTH-TC-067
AUTH-TC-069
AUTH-TC-071
AUTH-TC-072
AUTH-TC-074
AUTH-TC-075
AUTH-TC-078
AUTH-TC-080
AUTH-TC-088
AUTH-TC-090
AUTH-TC-092
AUTH-TC-094
AUTH-TC-096
AUTH-TC-097
AUTH-TC-139
AUTH-TC-147
AUTH-TC-148
AUTH-TC-149
AUTH-TC-150
```

---

# 47. UI Automation Candidates

Best suited for:

```text
Playwright
Selenium
Cypress
```

Candidates:

```text
AUTH-TC-008
AUTH-TC-012
AUTH-TC-019
AUTH-TC-025
AUTH-TC-031
AUTH-TC-034
AUTH-TC-041
AUTH-TC-050
AUTH-TC-064
AUTH-TC-069
AUTH-TC-070
AUTH-TC-072
AUTH-TC-074
AUTH-TC-084
AUTH-TC-126–137
AUTH-TC-147–150
```

---

# 48. API Automation Candidates

Best suited for:

```text
REST Assured
Postman
```

Candidates:

```text
AUTH-TC-012
AUTH-TC-019–030
AUTH-TC-031–044
AUTH-TC-045–055
AUTH-TC-067
AUTH-TC-071
AUTH-TC-074–080
AUTH-TC-088–109
AUTH-TC-139–146
```

---

# 49. Database Validation Candidates

Potential SQL validation:

```text
AUTH-TC-001
Customer created once.

AUTH-TC-007
No duplicate customer.

AUTH-TC-025
Lock status/failure count persisted correctly.

AUTH-TC-050
Credential reset state updated.

AUTH-TC-053
Reset token consumed.

AUTH-TC-064
Password credential version/state updated.

AUTH-TC-078
Session/revocation state persisted where applicable.
```

Passwords must never be inspected as plaintext.

---

# 50. Security Suite Mapping

High-value authentication cases should also appear in:

```text
manual-testing/test-suites/security-suite.md
```

Especially:

```text
MFA bypass

Session replay

Logout invalidation

Password reset abuse

OTP reuse

Enumeration

API authentication

Protected endpoint access

Sensitive authentication data
```

---

# 51. Execution Evidence

For real execution, capture appropriate evidence including:

```text
Browser/version

Build/version

Customer ID

Timestamp

Request/response where applicable

Session/auth state

Audit event

Screenshot/video

Defect ID
```

Never attach:

```text
Real password

Real OTP

Reusable token

Sensitive production credentials
```

---

# 52. Authentication Defect Examples

Potential authentication defects include:

```text
MFA bypass

Expired OTP accepted

Used OTP reusable

Logout token remains valid

Expired session remains valid

Reset token reusable

Locked account authenticates

Disabled user authenticates

Admin endpoint accessible to customer

Sensitive token exposed in logs
```

Critical issues should be linked to:

```text
manual-testing/defects/defect-template.md
```

---

# 53. Test Execution Template

For each case:

```text
Test ID:

Build:

Environment:

Browser:

Test Data:

Execution Date:

Expected:

Actual:

Status:

Defect:

Evidence:

Notes:
```

---

# 54. Authentication Exit Criteria

Authentication testing can be considered acceptable when:

```text
All P0 authentication cases executed.

Valid login succeeds.

Invalid login fails.

MFA cannot be bypassed.

OTP lifecycle behaves correctly.

Lockout functions.

Password reset is secure.

Logout invalidates access.

Expired sessions are rejected.

Revoked sessions are rejected.

Unauthorized APIs reject requests.

No critical sensitive-data exposure exists.
```

---

# 55. Release Blockers

Authentication release blockers include:

```text
Authentication bypass

MFA bypass

Locked account can authenticate

Disabled/closed customer gains banking access

Reset-token reuse enabling account takeover

Logout does not revoke critical access as required

Expired session can perform financial action

Customer can access protected admin APIs

Authentication secrets exposed
```

---

# 56. Final Authentication Testing Principle

Authentication testing should not stop after confirming:

```text
Valid username + password works.
```

The real authentication surface includes:

```text
Credentials

MFA

OTP lifecycle

Lockout

Password reset

Password changes

Sessions

Logout

Revocation

Concurrent sessions

API authentication

Customer state

Audit

Sensitive data
```

The most important question is:

```text
At every point in the authentication lifecycle,
can the system accurately determine whether this user
should currently be trusted with protected banking access?
```

The core rule is:

```text
No customer should gain protected banking access
without successfully completing every required
authentication and session-security control.
```
