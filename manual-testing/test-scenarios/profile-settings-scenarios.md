# Banking System — Profile & Settings Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Profile & Settings             |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for customer profile and settings functionality within the Banking System.

Profile and settings functionality affects:

* Personal information
* Contact details
* Security settings
* Password management
* MFA configuration
* Notification preferences
* Language and localization
* Session management
* Trusted devices
* Privacy settings
* Display preferences

Although many settings are non-financial, security-related profile changes may directly affect account protection and customer identity.

---

# 3. Scope

Testing includes:

* Profile viewing
* Profile editing
* Personal details
* Contact details
* Email changes
* Phone changes
* Address changes
* Password changes
* MFA settings
* Notification preferences
* Language settings
* Timezone settings
* Currency display preferences
* Session/device management
* Security settings
* Privacy preferences
* Settings persistence
* Authorization
* Validation
* Audit logging
* Notifications
* Error handling

---

# 4. Scenario Naming Convention

Profile/settings scenarios use:

```text
TS-PROF-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Profile Viewing Scenarios

## TS-PROF-001 — Customer views own profile

**Priority:** P1

Expected:

Correct customer information is displayed.

---

## TS-PROF-002 — Customer views account-related profile summary

**Priority:** P2

Expected:

Displayed summary belongs to authenticated customer.

---

## TS-PROF-003 — Customer refreshes profile page

**Priority:** P2

Expected:

Latest persisted profile information remains visible.

---

## TS-PROF-004 — Customer opens profile in multiple tabs

**Priority:** P2

Expected:

Tabs display consistent customer information.

---

## TS-PROF-005 — Customer with incomplete optional profile data

**Priority:** P2

Expected:

Missing optional fields are handled gracefully.

---

# 6. Profile Ownership and Authorization

## TS-PROF-006 — Customer attempts to view another customer's profile

**Priority:** P0

Expected:

Access denied.

---

## TS-PROF-007 — Customer modifies profile customer ID in URL

**Priority:** P0

Expected:

Cannot access another customer's information.

---

## TS-PROF-008 — Customer modifies customer ID in profile API

**Priority:** P0

Expected:

Backend authorization rejects request.

---

## TS-PROF-009 — Unauthenticated user accesses profile endpoint

**Priority:** P0

Expected:

Authentication required.

---

## TS-PROF-010 — Expired session attempts profile update

**Priority:** P0

Expected:

Update rejected.

---

## TS-PROF-011 — Customer attempts administrative settings endpoint

**Priority:** P0

Expected:

Denied.

---

# 7. Personal Information Update Scenarios

## TS-PROF-012 — Update editable personal information with valid data

**Priority:** P1

Expected:

Changes save successfully.

---

## TS-PROF-013 — Cancel personal information changes

**Priority:** P2

Expected:

Original values remain unchanged.

---

## TS-PROF-014 — Submit profile form without changes

**Priority:** P3

Expected:

Handled safely.

---

## TS-PROF-015 — Refresh before saving changes

**Priority:** P2

Expected:

Unsaved data is handled predictably.

---

## TS-PROF-016 — Double-submit profile update

**Priority:** P1

Expected:

No duplicate or inconsistent updates.

---

# 8. Name Validation Scenarios

## TS-PROF-017 — Valid first name

**Priority:** P2

Expected:

Accepted.

---

## TS-PROF-018 — Valid last name

**Priority:** P2

Expected:

Accepted.

---

## TS-PROF-019 — Empty mandatory first name

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-020 — Empty mandatory last name

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-021 — Name at minimum supported length

**Priority:** P2

Expected:

Accepted.

---

## TS-PROF-022 — Name below minimum length

**Priority:** P2

Expected:

Rejected where minimum exists.

---

## TS-PROF-023 — Name at maximum supported length

**Priority:** P2

Expected:

Accepted.

---

## TS-PROF-024 — Name above maximum supported length

**Priority:** P2

Expected:

Rejected.

---

## TS-PROF-025 — Arabic name

**Priority:** P2

Expected:

Unicode stored/displayed correctly.

---

## TS-PROF-026 — Name with accented characters

**Priority:** P2

Expected:

Supported correctly.

---

## TS-PROF-027 — Name containing only spaces

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-028 — Name with leading/trailing whitespace

**Priority:** P2

Expected:

Normalized or handled according to rules.

---

## TS-PROF-029 — Script-like name input

**Priority:** P1

Expected:

Input safely escaped and never executed.

---

# 9. Email Change Scenarios

## TS-PROF-030 — Change email to valid unused address

**Priority:** P0

Expected:

Change follows required verification flow.

---

## TS-PROF-031 — Invalid email format

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-032 — Change to email already used by another customer

**Priority:** P0

Expected:

Rejected where email must be unique.

---

## TS-PROF-033 — Change to current email

**Priority:** P3

Expected:

Handled safely.

---

## TS-PROF-034 — Email with leading/trailing spaces

**Priority:** P1

Expected:

Normalized without bypassing uniqueness validation.

---

## TS-PROF-035 — New email requires verification

**Priority:** P0

Expected:

Unverified new email does not immediately become trusted where verification is required.

---

## TS-PROF-036 — Correct email verification code

**Priority:** P0

Expected:

New email becomes verified.

---

## TS-PROF-037 — Incorrect email verification code

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-038 — Expired email verification code

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-039 — Reused verification code

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-040 — Old email no longer used after verified change

**Priority:** P0

Expected:

New contact information becomes authoritative according to rules.

---

## TS-PROF-041 — Email-change security notification

**Priority:** P0

Expected:

Security alert sent according to policy.

---

# 10. Phone Number Change Scenarios

## TS-PROF-042 — Change to valid phone number

**Priority:** P1

Expected:

Update follows phone verification flow.

---

## TS-PROF-043 — Invalid phone format

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-044 — Phone below minimum supported length

**Priority:** P2

Expected:

Rejected.

---

## TS-PROF-045 — Phone above maximum supported length

**Priority:** P2

Expected:

Rejected.

---

## TS-PROF-046 — Duplicate phone number

**Priority:** P1

Expected:

Behavior follows uniqueness requirements.

---

## TS-PROF-047 — Verify new phone using valid OTP

**Priority:** P0

Expected:

Phone becomes trusted.

---

## TS-PROF-048 — Invalid phone OTP

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-049 — Expired phone OTP

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-050 — Old phone no longer receives protected notifications after successful change

**Priority:** P0

Expected:

Correct trusted phone is used.

---

# 11. Address Settings Scenarios

## TS-PROF-051 — Update valid address

**Priority:** P2

Expected:

Saved successfully.

---

## TS-PROF-052 — Required address field empty

**Priority:** P2

Expected:

Validation displayed.

---

## TS-PROF-053 — Address at maximum length

**Priority:** P2

Expected:

Accepted.

---

## TS-PROF-054 — Address above maximum length

**Priority:** P2

Expected:

Rejected safely.

---

## TS-PROF-055 — Address containing Arabic characters

**Priority:** P2

Expected:

Stored correctly.

---

## TS-PROF-056 — Address containing valid punctuation and numbers

**Priority:** P2

Expected:

Accepted.

---

# 12. Date of Birth Scenarios

## TS-PROF-057 — View stored date of birth

**Priority:** P2

Expected:

Correct.

---

## TS-PROF-058 — Attempt to modify immutable date of birth

**Priority:** P1

Expected:

Rejected if field is protected.

---

## TS-PROF-059 — Invalid future date

**Priority:** P1

Expected:

Rejected if date is editable.

---

## TS-PROF-060 — Invalid calendar date

**Priority:** P1

Expected:

Rejected.

---

# 13. Immutable Identity Fields

Fields may include:

* Customer ID
* National identifier
* Verified date of birth
* KYC identifier

## TS-PROF-061 — Customer attempts to modify immutable customer ID

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-062 — Customer attempts to modify protected identity field through API

**Priority:** P0

Expected:

Backend ignores/rejects unauthorized field.

---

## TS-PROF-063 — Hidden protected field inserted into request payload

**Priority:** P0

Expected:

Mass-assignment protection prevents unauthorized modification.

---

# 14. Password Change Scenarios

## TS-PROF-064 — Change password with correct current password

**Priority:** P0

Expected:

Password changes successfully.

---

## TS-PROF-065 — Incorrect current password

**Priority:** P0

Expected:

Change rejected.

---

## TS-PROF-066 — New password below minimum length

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-067 — New password at minimum length

**Priority:** P1

Expected:

Accepted if composition rules pass.

---

## TS-PROF-068 — New password above maximum supported length

**Priority:** P1

Expected:

Handled according to password rules.

---

## TS-PROF-069 — New password missing uppercase requirement

**Priority:** P1

Expected:

Rejected if required.

---

## TS-PROF-070 — New password missing lowercase requirement

**Priority:** P1

Expected:

Rejected if required.

---

## TS-PROF-071 — New password missing numeric requirement

**Priority:** P1

Expected:

Rejected if required.

---

## TS-PROF-072 — New password missing special-character requirement

**Priority:** P1

Expected:

Rejected if required.

---

## TS-PROF-073 — New password equals current password

**Priority:** P1

Expected:

Rejected where reuse is prohibited.

---

## TS-PROF-074 — Password confirmation mismatch

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-075 — Old password fails after successful change

**Priority:** P0

Expected:

Cannot authenticate.

---

## TS-PROF-076 — New password succeeds after change

**Priority:** P0

Expected:

Authentication succeeds.

---

## TS-PROF-077 — Password-change notification generated

**Priority:** P0

Expected:

Security notification sent.

---

# 15. Password Change Session Handling

## TS-PROF-078 — Current session after password change

**Priority:** P0

Expected:

Behavior follows security policy.

---

## TS-PROF-079 — Other active sessions after password change

**Priority:** P0

Expected:

Sessions invalidated where required.

---

## TS-PROF-080 — Existing API token after password change

**Priority:** P0

Expected:

Token behavior follows security policy.

---

## TS-PROF-081 — Password change while another sensitive operation is open

**Priority:** P0

Expected:

Old session cannot bypass newly applied security state.

---

# 16. MFA Settings Scenarios

## TS-PROF-082 — Enable MFA successfully

**Priority:** P0

Expected:

MFA becomes required according to configured rules.

---

## TS-PROF-083 — Enable MFA requires identity verification

**Priority:** P0

Expected:

Security verification enforced.

---

## TS-PROF-084 — Login after enabling MFA

**Priority:** P0

Expected:

MFA challenge required.

---

## TS-PROF-085 — Disable MFA with valid verification

**Priority:** P0

Expected:

Disabled only where policy permits.

---

## TS-PROF-086 — Disable MFA without required authentication

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-087 — Expired MFA verification code during settings change

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-088 — Reused MFA verification code

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-089 — MFA configuration change generates security notification

**Priority:** P0

Expected:

Customer alerted.

---

## TS-PROF-090 — MFA setting persists after logout/login

**Priority:** P0

Expected:

Security state persists.

---

# 17. Notification Preference Scenarios

## TS-PROF-091 — Enable optional transfer notifications

**Priority:** P2

Expected:

Preference saved.

---

## TS-PROF-092 — Disable optional transfer notifications

**Priority:** P2

Expected:

Preference saved where allowed.

---

## TS-PROF-093 — Enable card notifications

**Priority:** P2

Expected:

Preference saved.

---

## TS-PROF-094 — Enable payment notifications

**Priority:** P2

Expected:

Preference saved.

---

## TS-PROF-095 — Disable marketing notifications

**Priority:** P2

Expected:

Marketing alerts suppressed.

---

## TS-PROF-096 — Mandatory security alerts cannot be disabled

**Priority:** P0

Expected:

Security policy enforced.

---

## TS-PROF-097 — Notification preferences persist after login

**Priority:** P1

Expected:

Saved settings remain.

---

## TS-PROF-098 — Preferences respected by notification system

**Priority:** P1

Expected:

Actual delivery matches stored settings.

---

# 18. Notification Channel Settings

## TS-PROF-099 — Enable email notification channel

**Priority:** P2

Expected:

Enabled.

---

## TS-PROF-100 — Disable optional email channel

**Priority:** P2

Expected:

Preference respected.

---

## TS-PROF-101 — Enable SMS channel

**Priority:** P2

Expected:

Enabled where supported.

---

## TS-PROF-102 — Enable in-app channel

**Priority:** P2

Expected:

Enabled.

---

## TS-PROF-103 — Disable all optional channels

**Priority:** P2

Expected:

Optional notifications suppressed while mandatory security alerts still follow policy.

---

# 19. Language Settings

Where localization is supported.

## TS-PROF-104 — Change language from English to another supported language

**Priority:** P2

Expected:

UI language changes correctly.

---

## TS-PROF-105 — Change language back to English

**Priority:** P2

Expected:

UI returns to English.

---

## TS-PROF-106 — Language persists after logout/login

**Priority:** P2

Expected:

Stored preference remains.

---

## TS-PROF-107 — Unsupported language value through API

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-108 — Financial values remain unchanged after language change

**Priority:** P0

Expected:

Localization must not alter actual monetary values.

---

## TS-PROF-109 — Right-to-left language layout where supported

**Priority:** P2

Expected:

UI remains usable.

---

# 20. Timezone Settings

Where configurable.

## TS-PROF-110 — Select valid timezone

**Priority:** P2

Expected:

Saved successfully.

---

## TS-PROF-111 — Unsupported timezone through API

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-112 — Transaction timestamps update presentation after timezone change

**Priority:** P1

Expected:

Underlying absolute transaction time remains unchanged.

---

## TS-PROF-113 — Scheduled transaction does not shift incorrectly when display timezone changes

**Priority:** P0

Expected:

Execution semantics remain correct.

---

# 21. Currency Display Preference

Where supported.

## TS-PROF-114 — Change preferred display currency

**Priority:** P2

Expected:

Display behavior follows requirements.

---

## TS-PROF-115 — Preferred display currency does not alter account's actual currency

**Priority:** P0

Expected:

No financial data modification.

---

## TS-PROF-116 — Unsupported currency preference

**Priority:** P1

Expected:

Rejected.

---

# 22. Theme / Appearance Settings

Where implemented.

## TS-PROF-117 — Select light theme

**Priority:** P3

Expected:

Preference applied.

---

## TS-PROF-118 — Select dark theme

**Priority:** P3

Expected:

Preference applied.

---

## TS-PROF-119 — Theme persists across sessions

**Priority:** P3

Expected:

Stored correctly.

---

## TS-PROF-120 — Theme change does not hide critical financial information

**Priority:** P1

Expected:

Balances/statuses remain readable.

---

# 23. Session Management Scenarios

Where settings provide active-session management.

## TS-PROF-121 — View active sessions

**Priority:** P0

Expected:

Customer can see authorized session information.

---

## TS-PROF-122 — Current session displayed

**Priority:** P1

Expected:

Current session distinguishable.

---

## TS-PROF-123 — Multiple active sessions displayed

**Priority:** P1

Expected:

Correct list.

---

## TS-PROF-124 — Revoke another active session

**Priority:** P0

Expected:

Selected session becomes invalid.

---

## TS-PROF-125 — Revoked session attempts protected action

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-126 — Revoke all other sessions

**Priority:** P0

Expected:

Only intended current session remains where supported.

---

## TS-PROF-127 — Customer attempts to revoke another customer's session via manipulated ID

**Priority:** P0

Expected:

Denied.

---

# 24. Device Management Scenarios

Where trusted-device functionality exists.

## TS-PROF-128 — View trusted devices

**Priority:** P1

Expected:

Only customer's devices shown.

---

## TS-PROF-129 — Trust new device after required verification

**Priority:** P0

Expected:

Device added securely.

---

## TS-PROF-130 — Remove trusted device

**Priority:** P0

Expected:

Device loses trusted status.

---

## TS-PROF-131 — Removed device attempts trust-dependent login behavior

**Priority:** P0

Expected:

Additional verification required according to policy.

---

## TS-PROF-132 — Customer attempts to manipulate another customer's device ID

**Priority:** P0

Expected:

Denied.

---

# 25. Privacy Settings Scenarios

Where privacy settings exist.

## TS-PROF-133 — View privacy settings

**Priority:** P2

Expected:

Current preferences displayed.

---

## TS-PROF-134 — Update optional privacy preference

**Priority:** P2

Expected:

Preference saved.

---

## TS-PROF-135 — Restricted mandatory data-processing setting cannot be disabled where required

**Priority:** P1

Expected:

Business/legal system rule enforced.

---

## TS-PROF-136 — Privacy settings persist after logout/login

**Priority:** P2

Expected:

Consistent.

---

# 26. Profile Picture Scenarios

Where supported.

## TS-PROF-137 — Upload valid profile image

**Priority:** P3

Expected:

Image updated.

---

## TS-PROF-138 — Upload unsupported file type

**Priority:** P2

Expected:

Rejected.

---

## TS-PROF-139 — Upload file exceeding maximum size

**Priority:** P2

Expected:

Rejected.

---

## TS-PROF-140 — Upload malformed file disguised as image

**Priority:** P1

Expected:

Secure validation rejects unsafe content.

---

## TS-PROF-141 — Remove profile picture

**Priority:** P3

Expected:

Default avatar restored.

---

# 27. Settings Persistence Scenarios

## TS-PROF-142 — Profile changes persist after page refresh

**Priority:** P1

Expected:

Correct stored values.

---

## TS-PROF-143 — Settings persist after logout/login

**Priority:** P1

Expected:

Correct.

---

## TS-PROF-144 — Settings consistent across multiple devices

**Priority:** P1

Expected:

Server-side preferences synchronize where applicable.

---

## TS-PROF-145 — Failed save does not appear persisted after refresh

**Priority:** P0

Expected:

No false success.

---

# 28. Concurrent Update Scenarios

## TS-PROF-146 — Edit profile simultaneously in two tabs

**Priority:** P1

Expected:

Final state follows concurrency design without silent corruption.

---

## TS-PROF-147 — Change email in two sessions concurrently

**Priority:** P0

Expected:

Only one valid trusted email state remains.

---

## TS-PROF-148 — Password changed while another profile update occurs

**Priority:** P0

Expected:

Security controls remain valid.

---

## TS-PROF-149 — Notification settings changed simultaneously on two devices

**Priority:** P2

Expected:

Final state deterministic.

---

## TS-PROF-150 — Session revoked while same session changes settings

**Priority:** P0

Expected:

Revoked session cannot complete unauthorized update.

---

# 29. API Validation Scenarios

## TS-PROF-151 — Retrieve own profile via API

**Priority:** P1

Expected:

Correct owned profile.

---

## TS-PROF-152 — Retrieve another customer's profile via API

**Priority:** P0

Expected:

Denied.

---

## TS-PROF-153 — Update valid profile field through API

**Priority:** P1

Expected:

Saved correctly.

---

## TS-PROF-154 — Update restricted field through API

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-155 — Send unknown fields in profile payload

**Priority:** P1

Expected:

Safely ignored or rejected according to API contract.

---

## TS-PROF-156 — Attempt mass-assignment of role/admin field

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-157 — Change notification preferences through API

**Priority:** P1

Expected:

Authorized update succeeds.

---

# 30. Database Consistency Scenarios

## TS-PROF-158 — Profile update persists correctly in database

**Priority:** P1

Expected:

Stored value matches UI/API.

---

## TS-PROF-159 — Email verification status persists correctly

**Priority:** P0

Expected:

Trusted-contact state accurate.

---

## TS-PROF-160 — Phone verification status persists correctly

**Priority:** P0

Expected:

Accurate.

---

## TS-PROF-161 — Password change updates credential data safely

**Priority:** P0

Expected:

No plaintext password stored.

---

## TS-PROF-162 — Notification preferences persist correctly

**Priority:** P1

Expected:

Database/API/UI agree.

---

## TS-PROF-163 — Revoked session state persists

**Priority:** P0

Expected:

Revoked credentials remain unusable.

---

# 31. Error Handling Scenarios

## TS-PROF-164 — Profile service unavailable

**Priority:** P1

Expected:

Safe error message.

---

## TS-PROF-165 — Server error during profile save

**Priority:** P1

Expected:

No false success.

---

## TS-PROF-166 — Network disconnect during email change

**Priority:** P0

Expected:

Final trusted email state can be determined safely.

---

## TS-PROF-167 — Network disconnect during password change

**Priority:** P0

Expected:

Customer can determine which password is valid without insecure behavior.

---

## TS-PROF-168 — Settings API timeout

**Priority:** P1

Expected:

Previous persisted configuration remains authoritative unless save actually succeeded.

---

# 32. Security Scenarios

## TS-PROF-169 — Manipulate profile owner ID

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-170 — Manipulate account role through profile payload

**Priority:** P0

Expected:

Privilege escalation prevented.

---

## TS-PROF-171 — Manipulate KYC status through profile payload

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-172 — Manipulate customer status through profile payload

**Priority:** P0

Expected:

Rejected.

---

## TS-PROF-173 — Sensitive setting change requires reauthentication where configured

**Priority:** P0

Expected:

Security control enforced.

---

## TS-PROF-174 — CSRF-like unauthorized state-changing request

**Priority:** P0

Expected:

Request protection works according to application architecture.

---

## TS-PROF-175 — Script-like text in editable profile fields

**Priority:** P1

Expected:

No script execution.

---

## TS-PROF-176 — SQL-like input in editable fields

**Priority:** P1

Expected:

Handled safely as data.

---

# 33. Sensitive Data Scenarios

## TS-PROF-177 — Password never displayed in profile API response

**Priority:** P0

Expected:

Not returned.

---

## TS-PROF-178 — MFA secret not exposed

**Priority:** P0

Expected:

Protected.

---

## TS-PROF-179 — Session tokens not displayed in device/session list

**Priority:** P0

Expected:

No raw credential exposure.

---

## TS-PROF-180 — National/customer identifiers masked where required

**Priority:** P1

Expected:

Privacy rules respected.

---

## TS-PROF-181 — Sensitive values not exposed in URLs

**Priority:** P0

Expected:

Passwords, OTPs, tokens, and protected identity values are not placed in query strings.

---

# 34. Audit Scenarios

## TS-PROF-182 — Email change creates audit record

**Priority:** P0

Expected:

Security-sensitive change traceable.

---

## TS-PROF-183 — Phone change audited

**Priority:** P1

Expected:

Traceable.

---

## TS-PROF-184 — Password change audited without logging password value

**Priority:** P0

Expected:

Action is recorded but credential is never exposed.

---

## TS-PROF-185 — MFA configuration change audited

**Priority:** P0

Expected:

Old/new state and actor traceable.

---

## TS-PROF-186 — Session revocation audited

**Priority:** P1

Expected:

Security action traceable.

---

## TS-PROF-187 — Unauthorized profile modification attempt recorded where required

**Priority:** P2

Expected:

Security event traceable.

---

# 35. Notification Scenarios

## TS-PROF-188 — Email change notification sent

**Priority:** P0

Expected:

Correct customer notified.

---

## TS-PROF-189 — Phone change notification sent

**Priority:** P1

Expected:

Correct.

---

## TS-PROF-190 — Password change notification sent

**Priority:** P0

Expected:

Correct security alert.

---

## TS-PROF-191 — MFA change notification sent

**Priority:** P0

Expected:

Correct.

---

## TS-PROF-192 — Failed profile update does not generate successful-change notification

**Priority:** P1

Expected:

No false notification.

---

# 36. Search/Navigation Scenarios

## TS-PROF-193 — Navigate from dashboard to profile

**Priority:** P2

Expected:

Correct page opens.

---

## TS-PROF-194 — Navigate directly to security settings

**Priority:** P2

Expected:

Authenticated customer reaches correct subsection.

---

## TS-PROF-195 — Browser Back after saving settings

**Priority:** P2

Expected:

No stale unsafe state or duplicate save.

---

## TS-PROF-196 — Browser Forward after settings update

**Priority:** P3

Expected:

Correct current data displayed.

---

# 37. Accessibility Scenarios

## TS-PROF-197 — Profile form supports keyboard navigation

**Priority:** P2

Expected:

Logical focus order.

---

## TS-PROF-198 — Settings controls have accessible labels

**Priority:** P2

Expected:

Screen-reader-compatible naming where supported.

---

## TS-PROF-199 — Toggle controls expose current state clearly

**Priority:** P2

Expected:

Enabled/disabled state is understandable.

---

## TS-PROF-200 — Validation errors linked to correct fields

**Priority:** P2

Expected:

Accessible error handling.

---

## TS-PROF-201 — Security warnings do not rely on color alone

**Priority:** P1

Expected:

Text/icon/semantic indication exists.

---

# 38. Responsive Scenarios

## TS-PROF-202 — Profile page on desktop

**Priority:** P2

Expected:

Readable.

---

## TS-PROF-203 — Profile page on tablet

**Priority:** P2

Expected:

Usable.

---

## TS-PROF-204 — Profile page on mobile

**Priority:** P1

Expected:

Critical profile/security controls remain available.

---

## TS-PROF-205 — Password change on mobile

**Priority:** P1

Expected:

All fields and security requirements visible.

---

## TS-PROF-206 — Session/device list on mobile

**Priority:** P1

Expected:

Customer can identify and revoke devices safely.

---

# 39. Cross-Browser Scenarios

## TS-PROF-207 — Profile management in Chrome

**Priority:** P1

Expected:

Works correctly.

---

## TS-PROF-208 — Profile management in Edge

**Priority:** P1

Expected:

Works correctly.

---

## TS-PROF-209 — Profile management in Firefox

**Priority:** P1

Expected:

Works correctly.

---

## TS-PROF-210 — Security-settings flow across supported browsers

**Priority:** P0

Expected:

No critical browser-specific security failure.

---

# 40. Boundary Scenarios

## TS-PROF-211 — Name minimum minus one character

**Priority:** P2

Expected:

Rejected.

---

## TS-PROF-212 — Name exactly at minimum length

**Priority:** P2

Expected:

Accepted.

---

## TS-PROF-213 — Name exactly at maximum length

**Priority:** P2

Expected:

Accepted.

---

## TS-PROF-214 — Name maximum plus one character

**Priority:** P2

Expected:

Rejected.

---

## TS-PROF-215 — Password minimum minus one character

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-216 — Password exactly minimum length

**Priority:** P1

Expected:

Accepted if other rules pass.

---

## TS-PROF-217 — Password maximum boundary

**Priority:** P1

Expected:

Handled according to policy.

---

## TS-PROF-218 — OTP exactly required length

**Priority:** P1

Expected:

Processed.

---

## TS-PROF-219 — OTP one digit below required length

**Priority:** P1

Expected:

Rejected.

---

## TS-PROF-220 — OTP one digit above required length

**Priority:** P1

Expected:

Rejected.

---

# 41. End-to-End Profile and Settings Scenarios

## TS-PROF-221 — Verified email-change journey

**Priority:** P0

Flow:

```text
Login
→ Open Profile
→ Change Email
→ Submit
→ Receive Verification
→ Verify New Email
→ Refresh Profile
→ Verify New Email
→ Verify Security Notification
→ Verify Audit
```

---

## TS-PROF-222 — Password-change journey

**Priority:** P0

Flow:

```text
Login
→ Open Security Settings
→ Enter Current Password
→ Enter Valid New Password
→ Confirm Change
→ Verify Security Notification
→ Logout
→ Verify Old Password Fails
→ Verify New Password Succeeds
```

---

## TS-PROF-223 — Enable MFA journey

**Priority:** P0

Flow:

```text
Login
→ Open Security Settings
→ Enable MFA
→ Complete Verification
→ Logout
→ Login Again
→ Verify MFA Challenge Required
```

---

## TS-PROF-224 — Session revocation journey

**Priority:** P0

Flow:

```text
Login On Device A
→ Login On Device B
→ Open Sessions On Device A
→ Revoke Device B
→ Attempt Protected Action On Device B
→ Verify Access Denied
```

---

## TS-PROF-225 — Notification preference journey

**Priority:** P1

Flow:

```text
Open Settings
→ Disable Optional Payment Email Alert
→ Save
→ Complete Payment
→ Verify In-App Alert As Configured
→ Verify Optional Email Not Sent
→ Re-enable Preference
→ Repeat Payment
→ Verify Email Sent
```

---

## TS-PROF-226 — Unauthorized field manipulation journey

**Priority:** P0

Flow:

```text
Login As Customer
→ Intercept Profile Update
→ Add role = ADMIN
→ Submit Request
→ Verify Request Rejected/Ignored
→ Verify Role Unchanged
→ Verify Audit/Security Logging
```

---

# 42. Critical Smoke Scenarios

Profile/settings smoke coverage should include:

```text
TS-PROF-001 — View own profile
TS-PROF-006 — Cannot view another customer's profile
TS-PROF-012 — Valid profile update
TS-PROF-030 — Valid email change
TS-PROF-064 — Password change
TS-PROF-075 — Old password invalid after change
TS-PROF-082 — Enable MFA
TS-PROF-121 — View sessions
```

---

# 43. Critical Regression Scenarios

Always prioritize:

* Profile ownership
* Protected-field authorization
* Email changes
* Phone changes
* Verification workflows
* Password changes
* Session invalidation
* MFA
* Session revocation
* Trusted devices
* Mandatory security alerts
* Notification preferences
* Settings persistence
* Sensitive-data protection
* Mass-assignment prevention
* UI/API/database consistency
* Audit logging

---

# 44. Automation Candidates

Strong UI automation candidates:

* View profile
* Update basic profile fields
* Email validation
* Phone validation
* Password change
* Notification preferences
* Language setting
* Session revocation
* MFA settings where test environment supports controlled OTP

Suitable for:

* Selenium
* Cypress
* Playwright

---

# 45. API Automation Candidates

Postman and REST Assured should later cover:

* Get profile
* Update profile
* Unauthorized profile access
* Protected-field modification
* Mass-assignment attempts
* Email/phone verification
* Password change
* Notification preferences
* Session management
* Device management
* MFA configuration

---

# 46. SQL Validation Candidates

Database testing should validate:

* Customer profile data
* Email
* Email verification state
* Phone
* Phone verification state
* Address
* Settings
* Notification preferences
* MFA-enabled state
* Session revocation
* Trusted-device relationships
* Audit events

Passwords, tokens, OTPs, or MFA secrets must not be exposed unnecessarily in test evidence.

---

# 47. Performance Testing Candidates

JMeter may later cover:

* Profile retrieval
* Settings retrieval
* Preference updates
* Active-session lookup

These are lower-performance-risk operations than transfers/payments, but authorization and consistency must remain correct under concurrency.

---

# 48. BDD Candidates

Example:

```gherkin
Feature: Password change

Scenario: Customer successfully changes their password
  Given the customer is logged in
  And the customer knows the current password
  When the customer changes to a valid new password
  Then the password change should succeed
  And the old password should no longer authenticate
  And the new password should authenticate successfully
  And a security notification should be generated
```

Authorization example:

```gherkin
Scenario: Customer cannot change their role through profile settings
  Given the customer is logged in as a standard customer
  When the customer submits a profile update containing an administrator role
  Then the role modification should be rejected
  And the customer should remain a standard customer
```

---

# 49. Risk Traceability

Major related risks include:

```text
RISK-002 — Unauthorized customer data access
RISK-005 — Authentication bypass
RISK-006 — Privilege escalation
RISK-014 — Session active after logout/security change
RISK-015 — Expired session accepted
RISK-021 — Sensitive information exposure
RISK-030 — Unauthorized API request
RISK-037 — Frontend-only validation
RISK-044 — OTP reuse
RISK-045 — Expired OTP accepted
RISK-047 — Insecure direct object access
```

---

# 50. Profile & Settings Coverage Summary

This catalog covers:

* Profile viewing
* Ownership
* Personal information
* Name validation
* Email changes
* Phone changes
* Address changes
* Date of birth
* Immutable fields
* Password changes
* Session security
* MFA
* Notification preferences
* Notification channels
* Language
* Timezone
* Currency preferences
* Appearance
* Session management
* Device management
* Privacy
* Profile pictures
* Settings persistence
* Concurrent updates
* API validation
* Database consistency
* Error handling
* Security
* Sensitive data
* Audit
* Notifications
* Navigation
* Accessibility
* Responsive behavior
* Cross-browser behavior
* Boundary testing
* End-to-end workflows

---

# 51. Final Profile & Settings Testing Principle

Profile and settings testing must distinguish between ordinary preferences and security-sensitive identity changes.

For every critical settings workflow, QA should be able to answer:

```text
Does this profile belong to the authenticated customer?

Can the customer modify only permitted fields?

Can protected fields such as role, KYC status, or customer status be manipulated?

Are email and phone changes verified securely?

Are old trusted contact details invalidated correctly?

Does a password change make the old password unusable?

Are active sessions handled correctly after security changes?

Can MFA be disabled without required verification?

Can another customer's session or device be modified?

Are mandatory security notifications still delivered?

Do profile/settings changes persist correctly across UI, API, and database?

Are security-sensitive actions audited without exposing secrets?
```

Profile settings must never provide an indirect path for privilege escalation, account takeover, or weakening of banking security controls.
