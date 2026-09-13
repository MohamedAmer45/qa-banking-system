# Banking System — Profile & Settings Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Profile & Settings             |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Profile & Settings scenario catalog into detailed, execution-ready test cases.

Coverage includes:

* Profile viewing
* Personal information updates
* Email changes
* Phone changes
* Contact verification
* Address changes
* Password changes
* MFA settings
* Notification preferences
* Language and locale
* Session management
* Trusted devices
* Security settings
* Account preferences
* Sensitive/protected fields
* Authorization
* Customer isolation
* Validation
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

Profile and Settings test cases use:

```text
PROF-TC-XXX
```

Examples:

```text
PROF-TC-001
PROF-TC-002
PROF-TC-003
```

---

# 4. Critical Profile & Settings Invariants

## Invariant 1 — Ownership

A customer may access and modify only their own profile and settings.

---

## Invariant 2 — Protected Fields

Customer-editable profile functionality must not allow modification of protected values such as:

```text
Customer ID

Role

KYC status

Risk score

Account state

Administrative flags

Internal notes
```

---

## Invariant 3 — Sensitive Changes Require Verification

Security-sensitive changes may require:

```text
Current password

MFA

OTP

Email verification

Phone verification

Re-authentication
```

according to business rules.

---

## Invariant 4 — Session Security

Security-sensitive changes must apply required session revocation behavior consistently.

---

## Invariant 5 — Auditability

Important profile and security changes must remain traceable without logging sensitive secrets.

---

# 5. Common Test Data

## Primary Customer

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

## Secondary Customer

```text
Customer:
CUST-002

Status:
ACTIVE
```

## Current Email

```text
customer1@banktest.local
```

## New Valid Email

```text
customer1.updated@banktest.local
```

## Current Phone

```text
+201000000001
```

## New Test Phone

```text
+201000000099
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text
Application is available.

Customer authentication works.

Profile/settings service is available.

Synthetic customer data is used.

Verification test channels are available.

API/DB validation is available where required.
```

---

# 7. View Profile Test Cases

## PROF-TC-001 — View Own Profile

**Priority:** P0
**Requirement:** REQ-PROF-001
**Automation:** Playwright / API

### Steps

1. Login as `CUST-001`.
2. Open Profile.

### Expected Result

Customer sees their own permitted profile information.

---

## PROF-TC-002 — Profile Data Matches API

**Priority:** P1

### Expected Result

Visible profile data matches authoritative API values.

---

## PROF-TC-003 — Profile Data Matches Database

**Priority:** P1

### Expected Result

Persisted profile data matches expected database state.

---

# 8. Profile Ownership / IDOR

## PROF-TC-004 — View Another Customer's Profile

**Priority:** P0
**Risk:** RISK-002, RISK-047

### Expected Result

Access denied.

---

## PROF-TC-005 — Manipulate Customer ID in URL

**Priority:** P0

Expected: unauthorized profile inaccessible.

---

## PROF-TC-006 — Manipulate Customer ID Through API

**Priority:** P0
**Automation:** REST Assured

Expected: backend identity/ownership validation enforced.

---

## PROF-TC-007 — Modify Another Customer's Profile

**Priority:** P0

Expected: denied and Customer B remains unchanged.

---

# 9. Name Update Test Cases

## PROF-TC-008 — Update Valid First Name

**Priority:** P1
**Requirement:** REQ-PROF-002

Expected: update persists successfully.

---

## PROF-TC-009 — Update Valid Last Name

**Priority:** P1

Expected: persists.

---

## PROF-TC-010 — Empty Required First Name

**Priority:** P2

Expected: rejected.

---

## PROF-TC-011 — Name Minimum Length

**Priority:** P2
**Type:** Boundary

Expected: valid minimum accepted.

---

## PROF-TC-012 — Name Maximum Length

**Priority:** P2

Expected: valid maximum accepted.

---

## PROF-TC-013 — Name Maximum Plus One

**Priority:** P2

Expected: rejected.

---

## PROF-TC-014 — Unicode Name

**Priority:** P2

Examples:

```text
أحمد
José
Müller
```

Expected: supported Unicode preserved.

---

## PROF-TC-015 — Leading and Trailing Spaces

**Priority:** P2

Expected: normalization follows defined rules.

---

# 10. Address Update Test Cases

## PROF-TC-016 — Update Valid Address

**Priority:** P1

Expected: address persists correctly.

---

## PROF-TC-017 — Empty Required Address Field

**Priority:** P2

Expected: validation enforced.

---

## PROF-TC-018 — Very Long Address

**Priority:** P2

Expected: maximum length enforced safely.

---

## PROF-TC-019 — Unicode Address

**Priority:** P2

Expected: supported international text preserved.

---

# 11. Email Change Test Cases

## PROF-TC-020 — Change Email to Valid New Address

**Priority:** P0
**Requirement:** REQ-PROF-003

### Expected Result

Email enters verification workflow according to policy.

---

## PROF-TC-021 — Change Email to Existing Customer Email

**Priority:** P0

Expected: rejected.

No account collision.

---

## PROF-TC-022 — Invalid Email Format

**Priority:** P1

Expected: rejected.

---

## PROF-TC-023 — Empty Email

**Priority:** P1

Expected: rejected if email mandatory.

---

## PROF-TC-024 — Same Existing Email

**Priority:** P2

Expected: no unnecessary destructive state change.

---

## PROF-TC-025 — Email Change Requires Reauthentication

**Priority:** P0

Where configured.

Expected: customer must satisfy required security verification.

---

# 12. Email Verification Test Cases

## PROF-TC-026 — Valid Email Verification Token

**Priority:** P0

Expected: new email becomes verified and active according to policy.

---

## PROF-TC-027 — Invalid Verification Token

**Priority:** P0

Expected: change not completed.

---

## PROF-TC-028 — Expired Verification Token

**Priority:** P0

Expected: rejected.

---

## PROF-TC-029 — Reused Verification Token

**Priority:** P0

Expected: consumed token cannot be reused.

---

## PROF-TC-030 — Old Token After Resend

**Priority:** P0

Expected: behavior follows token invalidation policy.

---

## PROF-TC-031 — New Email Cannot Be Used Before Verification

**Priority:** P0

Where verification is required before activation.

Expected: authoritative login/contact identity remains according to policy.

---

# 13. Phone Change Test Cases

## PROF-TC-032 — Change to Valid Phone Number

**Priority:** P0
**Requirement:** REQ-PROF-004

Expected: verification workflow initiated where required.

---

## PROF-TC-033 — Invalid Phone Format

**Priority:** P1

Expected: rejected.

---

## PROF-TC-034 — Existing Customer Phone Number

**Priority:** P0

Expected: uniqueness/business rules enforced.

---

## PROF-TC-035 — Unsupported Country Code

**Priority:** P1

Expected: handled according to supported regions.

---

# 14. Phone Verification Test Cases

## PROF-TC-036 — Valid OTP

**Priority:** P0

Expected: new phone becomes verified.

---

## PROF-TC-037 — Invalid OTP

**Priority:** P0

Expected: phone change remains incomplete.

---

## PROF-TC-038 — Expired OTP

**Priority:** P0
**Risk:** RISK-045

Expected: rejected.

---

## PROF-TC-039 — Reused OTP

**Priority:** P0
**Risk:** RISK-044

Expected: rejected.

---

## PROF-TC-040 — Old OTP After Resend

**Priority:** P0

Expected: invalid according to OTP lifecycle policy.

---

# 15. Protected Profile Fields

## PROF-TC-041 — Customer Attempts to Change Customer ID

**Priority:** P0

Expected: immutable.

---

## PROF-TC-042 — Customer Attempts to Change Role

**Priority:** P0
**Risk:** RISK-006

Expected: denied.

---

## PROF-TC-043 — Customer Attempts to Change KYC Status

**Priority:** P0

Expected: denied.

---

## PROF-TC-044 — Customer Attempts to Change Customer Status

**Priority:** P0

Expected: denied.

---

## PROF-TC-045 — Customer Attempts to Change Risk Score

**Priority:** P0

Expected: denied.

---

## PROF-TC-046 — Mass Assignment Attack

**Priority:** P0
**Risk:** RISK-037

Payload example:

```json
{
  "firstName": "Updated",
  "role": "SUPER_ADMIN",
  "kycStatus": "VERIFIED",
  "riskScore": 0,
  "customerStatus": "ACTIVE"
}
```

### Expected Result

Only explicitly allowed fields change.

---

# 16. Password Change Test Cases

## PROF-TC-047 — Valid Password Change

**Priority:** P0
**Requirement:** REQ-PROF-005

### Steps

1. Open Security Settings.
2. Enter current password.
3. Enter valid new password.
4. Confirm new password.
5. Submit.

### Expected Result

* Password changes.
* Old password no longer authenticates.
* New password authenticates.
* Security notification generated where required.

---

## PROF-TC-048 — Incorrect Current Password

**Priority:** P0

Expected: password remains unchanged.

---

## PROF-TC-049 — New Password Confirmation Mismatch

**Priority:** P1

Expected: rejected.

---

## PROF-TC-050 — New Password Below Minimum Length

**Priority:** P1

Expected: rejected.

---

## PROF-TC-051 — New Password Exactly Minimum Length

**Priority:** P1

Expected: accepted if all other rules pass.

---

## PROF-TC-052 — Weak New Password

**Priority:** P1

Expected: rejected according to password policy.

---

## PROF-TC-053 — Reuse Old Password

**Priority:** P1

Expected: follows configured password-history policy.

---

# 17. Password Change Session Security

## PROF-TC-054 — Current Session After Password Change

**Priority:** P0

Expected: behavior follows configured session policy.

---

## PROF-TC-055 — Other Browser Session After Password Change

**Priority:** P0
**Risk:** RISK-014

### Expected Result

Other active session is revoked if policy requires global revocation.

---

## PROF-TC-056 — Remembered Session After Password Change

**Priority:** P0

Expected: stale remembered authentication follows revocation policy.

---

# 18. MFA Enable Test Cases

## PROF-TC-057 — Enable MFA Successfully

**Priority:** P0
**Requirement:** REQ-PROF-006

### Expected Result

* Required verification completes.
* MFA status becomes enabled.
* Future authentication requires MFA.
* Security notification/audit event generated.

---

## PROF-TC-058 — Enable MFA With Invalid Verification

**Priority:** P0

Expected: MFA remains disabled.

---

## PROF-TC-059 — MFA Setup Interrupted

**Priority:** P0

Expected: partial setup does not incorrectly mark MFA enabled.

---

# 19. MFA Disable Test Cases

## PROF-TC-060 — Disable MFA With Valid Reauthentication

**Priority:** P0

Expected: follows required security policy.

---

## PROF-TC-061 — Disable MFA Without Required Password/OTP

**Priority:** P0
**Risk:** RISK-005

Expected: denied.

---

## PROF-TC-062 — Client Manipulates MFA Enabled Flag

**Priority:** P0
**Risk:** RISK-037

Expected: backend authoritative security flow required.

---

## PROF-TC-063 — Security Notification After MFA Disable

**Priority:** P0

Expected: notification generated where mandatory.

---

# 20. Notification Preference Test Cases

## PROF-TC-064 — View Notification Preferences

**Priority:** P1
**Requirement:** REQ-PROF-007

Expected: current preferences displayed correctly.

---

## PROF-TC-065 — Enable Optional Email Notifications

**Priority:** P1

Expected: preference persists.

---

## PROF-TC-066 — Disable Optional Email Notifications

**Priority:** P1

Expected: future optional notifications follow preference.

---

## PROF-TC-067 — Disable Marketing Notifications

**Priority:** P2

Expected: persists.

---

## PROF-TC-068 — Attempt to Disable Mandatory Security Notification

**Priority:** P0

Expected: denied where security alerts are mandatory.

---

## PROF-TC-069 — Preferences Persist After Login

**Priority:** P1

Expected: values remain after new session.

---

# 21. Language / Locale Test Cases

## PROF-TC-070 — Change Language to English

**Priority:** P2
**Requirement:** REQ-PROF-008

Expected: supported UI changes appropriately.

---

## PROF-TC-071 — Change Language to Arabic

**Priority:** P1

Expected:

* Arabic content displayed.
* RTL behavior correct where applicable.
* Financial values remain understandable.

---

## PROF-TC-072 — Language Preference Persists

**Priority:** P2

Expected: persists across sessions.

---

## PROF-TC-073 — Unsupported Locale Through API

**Priority:** P2

Expected: rejected/fallback according to contract.

---

# 22. Currency / Display Preference Test Cases

## PROF-TC-074 — Change Supported Display Preference

**Priority:** P2

Expected: preference affects presentation only and does not alter authoritative account currency or monetary values.

---

## PROF-TC-075 — Client Attempts to Change Account Currency Through Settings

**Priority:** P0

Expected: denied.

---

# 23. Session List Test Cases

## PROF-TC-076 — View Active Sessions

**Priority:** P1
**Requirement:** REQ-PROF-009

Expected: customer sees their authorized active sessions/device information according to design.

---

## PROF-TC-077 — Session Information Minimization

**Priority:** P1

Expected: no session token/secret exposed.

---

## PROF-TC-078 — Session Last-Active Timestamp

**Priority:** P2

Expected: accurate within expected update semantics.

---

# 24. Session Revocation Test Cases

## PROF-TC-079 — Revoke Other Session

**Priority:** P0

Expected: selected session becomes unusable.

---

## PROF-TC-080 — Revoke Current Session

**Priority:** P1

Expected: customer is logged out safely if supported.

---

## PROF-TC-081 — Revoke All Other Sessions

**Priority:** P0

Expected: current session behavior follows policy; all targeted sessions revoked.

---

## PROF-TC-082 — Use Revoked Session API Token

**Priority:** P0

Expected: denied.

---

## PROF-TC-083 — Revoke Another Customer's Session

**Priority:** P0

Expected: denied.

---

# 25. Trusted Device Test Cases

## PROF-TC-084 — View Trusted Devices

**Priority:** P1

Where supported.

Expected: only customer's devices visible.

---

## PROF-TC-085 — Remove Trusted Device

**Priority:** P0

Expected: future authentication requires normal security checks.

---

## PROF-TC-086 — Remove Another Customer's Device

**Priority:** P0

Expected: denied.

---

## PROF-TC-087 — Stale Trusted Device After Password Change

**Priority:** P0

Expected: follows session/device revocation policy.

---

# 26. Security Preference Test Cases

## PROF-TC-088 — Configure Login Alerts

**Priority:** P1

Expected: preference persists where configurable.

---

## PROF-TC-089 — Mandatory Security Control Cannot Be Disabled Through Client Manipulation

**Priority:** P0

Expected: backend policy enforced.

---

# 27. Save / Cancel Behavior

## PROF-TC-090 — Save Valid Profile Edit

**Priority:** P1

Expected: changes persist.

---

## PROF-TC-091 — Cancel Profile Edit

**Priority:** P2

Expected: unsaved changes discarded.

---

## PROF-TC-092 — Navigate Away With Unsaved Changes

**Priority:** P2

Expected: behavior follows UX requirement, such as warning or discard.

---

## PROF-TC-093 — Double-Click Save

**Priority:** P1

Expected: no duplicate side effect.

---

# 28. Concurrency Test Cases

## PROF-TC-094 — Two Sessions Edit Same Profile Field

**Priority:** P1
**Risk:** RISK-013

Expected: conflict handling follows defined concurrency strategy.

---

## PROF-TC-095 — Customer Edits Profile While Admin Suspends Customer

**Priority:** P0

Expected: stale profile save cannot overwrite protected customer status.

---

## PROF-TC-096 — Email Change From Two Sessions

**Priority:** P0

Expected: deterministic valid final identity/contact state.

---

## PROF-TC-097 — Password Change and Session Revocation Concurrently

**Priority:** P0

Expected: no stale session remains unintentionally authorized.

---

# 29. Stale Data Test Cases

## PROF-TC-098 — Profile Page Open Before Admin Update

**Priority:** P1

Expected: customer update cannot overwrite admin-only fields with stale payload.

---

## PROF-TC-099 — Security Settings Page Open Before MFA State Change

**Priority:** P0

Expected: backend current security state remains authoritative.

---

# 30. API Profile Test Cases

## PROF-TC-100 — Get Own Profile API

**Priority:** P0
**Automation:** REST Assured

Expected: permitted profile data returned.

---

## PROF-TC-101 — Get Another Customer Profile API

**Priority:** P0

Expected: denied.

---

## PROF-TC-102 — Update Allowed Profile Field API

**Priority:** P1

Expected: persists.

---

## PROF-TC-103 — Update Protected Profile Field API

**Priority:** P0

Expected: rejected/ignored securely.

---

## PROF-TC-104 — Unauthenticated Profile API

**Priority:** P0

Expected: denied.

---

## PROF-TC-105 — Invalid Field Type

**Priority:** P1

Expected: validation error.

---

## PROF-TC-106 — Unknown JSON Fields

**Priority:** P1

Expected: handled according to API contract without unintended state changes.

---

# 31. API Security Settings Test Cases

## PROF-TC-107 — Password Change API

**Priority:** P0

Expected: current-password/security verification required.

---

## PROF-TC-108 — MFA Enable API

**Priority:** P0

Expected: correct multi-step verification enforced.

---

## PROF-TC-109 — MFA Disable API

**Priority:** P0

Expected: cannot be disabled through a simple unauthorized flag update.

---

## PROF-TC-110 — Session Revoke API

**Priority:** P0

Expected: only owned session may be targeted.

---

# 32. Database Validation Test Cases

## PROF-TC-111 — Profile Update Persistence

**Priority:** P1
**Automation:** SQL

Expected: allowed fields persist correctly.

---

## PROF-TC-112 — Protected Fields Remain Unchanged

**Priority:** P0

Expected: mass-assignment attempts do not alter protected columns.

---

## PROF-TC-113 — Email Verification State Persistence

**Priority:** P0

Expected: email/value/verification state consistent.

---

## PROF-TC-114 — Phone Verification State Persistence

**Priority:** P0

Expected: correct.

---

## PROF-TC-115 — MFA State Persistence

**Priority:** P0

Expected: matches successfully completed security workflow.

---

## PROF-TC-116 — Notification Preference Persistence

**Priority:** P1

Expected: correct.

---

## PROF-TC-117 — Session Revocation Persistence

**Priority:** P0

Expected: revoked session cannot become valid again unintentionally.

---

# 33. UI/API/Database Consistency

## PROF-TC-118 — Profile Cross-Layer Validation

**Priority:** P0
**Risk:** RISK-039, RISK-048

Expected:

```text
UI Allowed Profile Fields
=
API Allowed Profile Fields
=
DB Expected Values
```

---

## PROF-TC-119 — Security Setting Cross-Layer Validation

**Priority:** P0

Expected:

```text
UI MFA State
=
API MFA State
=
DB Authoritative State
```

---

## PROF-TC-120 — Preferences Cross-Layer Validation

**Priority:** P1

Expected: UI/API/DB values agree.

---

# 34. Input Security Test Cases

## PROF-TC-121 — Script-Like Profile Name

**Priority:** P1

Example:

```text
<script>alert(1)</script>
```

Expected: no script execution.

---

## PROF-TC-122 — Script-Like Address

**Priority:** P1

Expected: safely encoded.

---

## PROF-TC-123 — SQL-Like Input

**Priority:** P1

Expected: no query manipulation/database error.

---

## PROF-TC-124 — Extremely Long Payload

**Priority:** P2

Expected: size/field limits enforced safely.

---

# 35. Sensitive Data Test Cases

## PROF-TC-125 — Profile API Does Not Return Password

**Priority:** P0
**Risk:** RISK-021

Expected: plaintext password absent.

---

## PROF-TC-126 — Password Hash Not Returned to Customer

**Priority:** P0

Expected: absent.

---

## PROF-TC-127 — MFA Secrets Not Exposed

**Priority:** P0

Expected: reusable MFA secrets/recovery information only exposed through explicitly secure designed workflow.

---

## PROF-TC-128 — Session Tokens Not Displayed

**Priority:** P0

Expected: absent from session-management UI/API response where unnecessary.

---

# 36. Audit Test Cases

## PROF-TC-129 — Email Change Audit

**Priority:** P1
**Risk:** RISK-022

Expected:

```text
Actor

Change Type

Old Value where policy permits

New Value where policy permits

Timestamp
```

with sensitive values handled appropriately.

---

## PROF-TC-130 — Phone Change Audit

**Priority:** P1

Expected: traceable.

---

## PROF-TC-131 — Password Change Audit

**Priority:** P0

Expected: event recorded without old/new password.

---

## PROF-TC-132 — MFA State Change Audit

**Priority:** P0

Expected: enable/disable action traceable.

---

## PROF-TC-133 — Session Revocation Audit

**Priority:** P1

Expected: action traceable without token disclosure.

---

## PROF-TC-134 — No Authentication Secrets in Audit

**Priority:** P0
**Risk:** RISK-032

Expected: no password, OTP, reusable token, secret seed.

---

# 37. Notification Test Cases

## PROF-TC-135 — Email Change Notification

**Priority:** P0

Expected: required security/contact-change notification generated.

---

## PROF-TC-136 — Phone Change Notification

**Priority:** P1

Expected: accurate.

---

## PROF-TC-137 — Password Change Notification

**Priority:** P0

Expected: generated without password data.

---

## PROF-TC-138 — MFA Enabled Notification

**Priority:** P0

Expected: accurate.

---

## PROF-TC-139 — MFA Disabled Notification

**Priority:** P0

Expected: accurate.

---

## PROF-TC-140 — Failed Change Sends No False Success Notification

**Priority:** P0
**Risk:** RISK-034

Expected: notification reflects authoritative result.

---

# 38. Error Handling Test Cases

## PROF-TC-141 — Profile Service Unavailable

**Priority:** P1

Expected: safe error; existing profile not overwritten.

---

## PROF-TC-142 — Email Verification Service Failure

**Priority:** P1

Expected: change remains safely pending/unverified.

---

## PROF-TC-143 — Phone Verification Service Failure

**Priority:** P1

Expected: current verified phone remains authoritative until completion.

---

## PROF-TC-144 — Session Revocation Service Failure

**Priority:** P0

Expected: system does not falsely claim session has been revoked when it remains active.

---

# 39. Cross-Browser Test Cases

## PROF-TC-145 — Profile in Chrome

**Priority:** P2

Expected: core functionality works.

---

## PROF-TC-146 — Profile in Edge

**Priority:** P2

Expected: works.

---

## PROF-TC-147 — Profile in Firefox

**Priority:** P2

Expected: works.

---

## PROF-TC-148 — Profile in WebKit

**Priority:** P2

Expected: works.

---

# 40. Responsive Test Cases

## PROF-TC-149 — Profile at 390×844

**Priority:** P1

Expected:

* Fields readable.
* Save/cancel controls reachable.
* Sensitive settings clearly separated.

---

## PROF-TC-150 — Security Settings at 360×800

**Priority:** P1

Expected: password/MFA/session controls remain accessible.

---

## PROF-TC-151 — Arabic RTL Profile Layout

**Priority:** P1

Expected: layout remains usable and field/value relationships remain clear.

---

# 41. Accessibility Test Cases

## PROF-TC-152 — Keyboard Profile Editing

**Priority:** P2

Expected: complete edit flow keyboard accessible.

---

## PROF-TC-153 — Form Labels

**Priority:** P2

Expected: all profile/security inputs have meaningful accessible labels.

---

## PROF-TC-154 — Validation Error Association

**Priority:** P2

Expected: errors associated with relevant inputs.

---

## PROF-TC-155 — Security Warnings Accessible

**Priority:** P1

Expected: MFA disable, session revoke, and password change warnings perceivable.

---

# 42. End-to-End Email Change Journey

## PROF-TC-156 — Email Change → Verify → Reauthenticate

**Priority:** P0

### Steps

1. Login as `CUST-001`.
2. Request email change.
3. Enter new valid email.
4. Complete required reauthentication.
5. Receive verification token.
6. Verify new email.
7. Logout.
8. Login according to updated identity rules.
9. Validate profile/API/DB.
10. Inspect notification/audit.

### Expected Result

One authorized verified email change is completed and traceable.

---

# 43. End-to-End Phone Change Journey

## PROF-TC-157 — Phone Change → OTP Verification

**Priority:** P0

### Expected Result

New phone becomes authoritative only after required verification.

Old/new values remain consistent across UI/API/DB.

---

# 44. End-to-End Password Change Journey

## PROF-TC-158 — Password Change + Session Revocation

**Priority:** P0

### Steps

1. Open Session A.
2. Open Session B.
3. Change password in Session A.
4. Attempt action in Session B.
5. Logout/login using old password.
6. Login using new password.

### Expected Result

```text
Old Password:
Rejected

New Password:
Accepted

Other Session:
Handled according to revocation policy
```

---

# 45. End-to-End MFA Enable Journey

## PROF-TC-159 — Enable MFA → Next Login

**Priority:** P0

### Steps

1. Start with MFA disabled.
2. Complete valid enable workflow.
3. Logout.
4. Login again with valid password.
5. Attempt to bypass MFA.
6. Complete MFA.

### Expected Result

MFA becomes mandatory according to security policy and cannot be bypassed.

---

# 46. End-to-End MFA Disable Journey

## PROF-TC-160 — Secure MFA Disable

**Priority:** P0

### Expected Result

MFA may be disabled only after all required verification.

Security notification and audit exist.

---

# 47. End-to-End Session Revocation

## PROF-TC-161 — Revoke Another Device

**Priority:** P0

### Steps

1. Login from Device/Browser A.
2. Login from Device/Browser B.
3. From A, revoke B.
4. From B, call protected page/API.

### Expected Result

Session B denied.

Session A behaves according to intended policy.

---

# 48. End-to-End Protected Field Attack

## PROF-TC-162 — Allowed + Protected Fields in Same Update

**Priority:** P0

### Payload

```json
{
  "firstName": "Mohamed",
  "role": "SUPER_ADMIN",
  "kycStatus": "VERIFIED",
  "customerStatus": "ACTIVE",
  "riskScore": 0
}
```

### Expected Result

```text
firstName:
Updated if valid

role:
Unchanged

kycStatus:
Unchanged

customerStatus:
Unchanged

riskScore:
Unchanged
```

---

# 49. End-to-End Customer Isolation

## PROF-TC-163 — Customer A vs Customer B Settings

**Priority:** P0

Customer A attempts to access/modify Customer B:

```text
Profile

Email

Phone

Notification preferences

Sessions

Trusted devices

MFA state
```

### Expected Result

Every unauthorized request denied.

---

# 50. End-to-End Stale State Protection

## PROF-TC-164 — Stale Profile Save After Administrative Restriction

**Priority:** P0

### Steps

1. Customer opens profile.
2. Admin changes protected customer state.
3. Customer submits stale profile form.
4. Inspect DB/API.

### Expected Result

Customer-editable fields may update according to policy, but protected administrative state remains unchanged.

---

# 51. Profile & Settings Risk Mapping

| Risk                                | Related Test Cases                       |
| ----------------------------------- | ---------------------------------------- |
| RISK-002 Unauthorized customer data | PROF-TC-004–007, 083, 086, 101, 163      |
| RISK-005 Authentication/MFA bypass  | PROF-TC-057–063, 159–160                 |
| RISK-006 Privilege escalation       | PROF-TC-041–046, 103, 162                |
| RISK-013 Concurrency                | PROF-TC-094–099, 164                     |
| RISK-014 Session remains active     | PROF-TC-054–056, 079–087, 158, 161       |
| RISK-021 Sensitive exposure         | PROF-TC-125–128                          |
| RISK-022 Audit gap                  | PROF-TC-129–134                          |
| RISK-032 Sensitive audit exposure   | PROF-TC-134                              |
| RISK-034 False notification         | PROF-TC-135–140                          |
| RISK-037 Frontend-only validation   | PROF-TC-041–046, 062, 089, 103, 106, 162 |
| RISK-039 API/DB inconsistency       | PROF-TC-111–120                          |
| RISK-044 OTP reuse                  | PROF-TC-039–040                          |
| RISK-045 Expired OTP                | PROF-TC-038                              |
| RISK-047 IDOR                       | PROF-TC-004–007, 083, 086, 101, 163      |
| RISK-048 UI/backend mismatch        | PROF-TC-098–099, 118–120, 140, 144       |

---

# 52. Requirements Mapping

| Requirement                              | Test Cases      |
| ---------------------------------------- | --------------- |
| REQ-PROF-001 View own profile            | PROF-TC-001–007 |
| REQ-PROF-002 Edit allowed profile fields | PROF-TC-008–019 |
| REQ-PROF-003 Email changes               | PROF-TC-020–031 |
| REQ-PROF-004 Phone changes               | PROF-TC-032–040 |
| REQ-PROF-005 Password changes            | PROF-TC-047–056 |
| REQ-PROF-006 MFA settings                | PROF-TC-057–063 |
| REQ-PROF-007 Notification preferences    | PROF-TC-064–069 |
| REQ-PROF-008 Locale/settings             | PROF-TC-070–075 |
| REQ-PROF-009 Sessions/devices            | PROF-TC-076–089 |

---

# 53. Smoke Candidates

Recommended profile/settings smoke coverage:

```text
PROF-TC-001
PROF-TC-004
PROF-TC-008
PROF-TC-020
PROF-TC-032
PROF-TC-041
PROF-TC-047
PROF-TC-057
PROF-TC-064
PROF-TC-076
PROF-TC-079
PROF-TC-100
```

---

# 54. Sanity Candidates

After profile/settings changes:

```text
PROF-TC-001
PROF-TC-008
PROF-TC-020
PROF-TC-026
PROF-TC-032
PROF-TC-036
PROF-TC-041
PROF-TC-047
PROF-TC-055
PROF-TC-057
PROF-TC-060
PROF-TC-064
PROF-TC-069
PROF-TC-079
PROF-TC-111
PROF-TC-118
```

---

# 55. Critical Regression Candidates

```text
PROF-TC-001–007

PROF-TC-020–063

PROF-TC-068

PROF-TC-076–120

PROF-TC-125–144

PROF-TC-149–164
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
PROF-TC-001–040

PROF-TC-047–099

PROF-TC-135–164
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
PROF-TC-004–007

PROF-TC-020–069

PROF-TC-076–144

PROF-TC-156–164
```

---

# 58. SQL / Database Testing Candidates

Strong SQL candidates:

```text
PROF-TC-020–040

PROF-TC-041–069

PROF-TC-076–120

PROF-TC-129–164
```

Database validation should verify:

```text
Customer ownership

Allowed profile fields

Protected fields

Email verification state

Phone verification state

MFA state

Preferences

Session revocation state

Trusted devices

Audit records
```

---

# 59. Test Evidence Requirements

For critical profile/settings tests, capture as applicable:

```text
Customer ID

Field changed

Previous allowed value

New allowed value

Verification state

MFA state

Session ID/reference without token

Device reference

API request/response

DB value

Audit event

Notification

Timestamp

Screenshot

Defect ID
```

Never capture:

```text
Plaintext password

OTP

MFA secret seed

Session token

Reusable authentication token
```

---

# 60. Profile & Settings Defect Examples

Potential Critical/High defects include:

```text
Customer can edit another customer's profile.

Customer can change own role.

Customer can change own KYC status.

Mass assignment modifies protected fields.

Email changes without required verification.

Phone changes without required verification.

Used OTP remains reusable.

Password change leaves prohibited sessions active.

MFA can be disabled without required verification.

Customer can revoke another customer's session.

Mandatory security notifications can be disabled.

Profile save overwrites admin-set customer status.

Session API exposes authentication token.

UI shows MFA enabled while backend has MFA disabled.
```

---

# 61. Profile & Settings Release Blockers

Release should normally be blocked by unresolved issues involving:

```text
Profile IDOR

Privilege escalation

Protected field modification

Unauthorized KYC/customer-status modification

Email takeover through verification bypass

Phone takeover through verification bypass

Password-change security failure

MFA enable/disable bypass

Session revocation failure with security impact

Cross-customer session/device access

Sensitive authentication-secret exposure

Critical profile/security-state inconsistency
```

---

# 62. Exit Criteria

Profile & Settings testing is acceptable when:

```text
Customers can access only their own profile/settings.

Allowed fields update correctly.

Protected fields remain immutable to customers.

Email and phone changes follow verification rules.

Password changes follow security policy.

MFA enable/disable flows are secure.

Session revocation works.

Trusted-device controls are authorized.

Notification preferences persist.

Mandatory security controls cannot be disabled improperly.

Locale/preferences behave correctly.

Sensitive authentication data is protected.

Critical changes are audited.

Security notifications match actual changes.

UI/API/DB states agree.

No unresolved Critical/P0 profile/settings defect remains.
```

---

# 63. Final Profile & Settings Testing Principle

Profile and Settings functionality may appear less financially critical than transfers or payments, but it controls the customer's:

```text
Identity

Authentication

Recovery channels

Security settings

Sessions

Notification channels
```

A weakness here can become an account-takeover path.

QA must therefore verify both:

```text
Can the customer change what they are allowed to change?
```

and:

```text
Can the customer NOT change what they are not allowed to change?
```

The critical invariant is:

```text
Only the authenticated customer may change
their permitted settings,
and sensitive identity/security changes
must complete every required verification step
before becoming authoritative.
```

The core rule is:

```text
Profile and Settings must give customers control
without allowing them to bypass identity,
authorization,
security,
or administrative banking controls.
```
