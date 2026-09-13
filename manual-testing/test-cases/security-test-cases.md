# Banking System — Security Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Security                       |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document converts the Security scenario catalog into detailed, execution-ready security-focused test cases.

Coverage includes:

* Authentication security
* Authorization
* IDOR/BOLA
* Role-based access control
* Privilege escalation
* MFA
* OTP lifecycle
* Session security
* Logout/revocation
* Password reset
* Mass assignment
* Financial parameter tampering
* Replay/idempotency
* Rate limiting
* Enumeration resistance
* Sensitive-data protection
* API security
* Input validation
* Injection resistance
* XSS resistance
* CSRF protection where applicable
* File handling
* Security headers
* Browser storage
* Error handling
* Audit/logging
* Fail-closed behavior
* Cross-customer isolation
* Admin security
* Financial transaction security

This is a **defensive QA security test suite**, intended to validate that the Banking System safely enforces its documented security controls.

---

# 3. Test Case ID Convention

Security test cases use:

```text id="902fau"
SEC-TC-XXX
```

Examples:

```text id="dqh12t"
SEC-TC-001
SEC-TC-002
SEC-TC-003
```

---

# 4. Critical Security Invariants

## Invariant 1 — Authentication

Protected banking functionality must require valid authenticated identity.

---

## Invariant 2 — Authorization

Authentication alone is not sufficient.

Every protected action must also verify:

```text id="p78t65"
Who is the user?

What resource are they accessing?

Do they own the resource?

What role do they have?

Is the requested action permitted?
```

---

## Invariant 3 — Server-Side Authority

Security-critical values must never rely solely on client state.

Examples:

```text id="ru7oex"
Role

Account ownership

Balance

Transaction status

Fees

Limits

Beneficiary state

Card state

Loan state

KYC state
```

---

## Invariant 4 — Customer Isolation

```text id="id5f3j"
Customer A
must never gain access to
Customer B's protected resources.
```

---

## Invariant 5 — Least Privilege

Users and administrators must receive only the minimum access required for their role.

---

## Invariant 6 — Financial Integrity

Tampering with requests must never allow the customer to:

```text id="b5sbw8"
Create money

Avoid fees

Exceed balances

Exceed limits

Use unauthorized accounts

Use unauthorized beneficiaries

Alter transaction status

Duplicate financial effects
```

---

## Invariant 7 — Sensitive Data Protection

The system must not expose:

```text id="aq9k5k"
Passwords

Password hashes

CVV

PIN

OTP

MFA secrets

Session tokens

Reusable reset tokens

Full protected card data

Other customers' private data
```

---

## Invariant 8 — Fail Closed

If authentication, authorization, policy, or security-control validation cannot be completed safely:

```text id="t0cnh2"
Deny
```

is preferred over unsafe authorization.

---

# 5. Common Security Test Data

## Standard Customer

```text id="dlffrv"
CUST-001

Role:
CUSTOMER

Status:
ACTIVE

KYC:
VERIFIED
```

## Second Customer

```text id="3ed9z5"
CUST-002

Role:
CUSTOMER

Status:
ACTIVE
```

## Operations Admin

```text id="yhyxdb"
ADMIN-002

Role:
OPERATIONS_ADMIN
```

## KYC Reviewer

```text id="kj53xb"
ADMIN-003

Role:
KYC_REVIEWER
```

## Loan Officer

```text id="hn5gd5"
ADMIN-004

Role:
LOAN_OFFICER
```

## Read-Only Admin

```text id="ycfu0t"
ADMIN-007

Role:
READ_ONLY_ADMIN
```

## Customer A Account

```text id="7j1s5t"
ACC-001
Owner: CUST-001
```

## Customer B Account

```text id="k31nh7"
ACC-002
Owner: CUST-002
```

---

# 6. Common Preconditions

Unless otherwise specified:

```text id="3stkg3"
Application is available.

Authentication service is available.

Required synthetic users/resources exist.

API client is available.

Browser developer tools may be used.

Database/log validation is available where required.

Testing is performed only against authorized project environments.
```

---

# 7. Unauthenticated Access Test Cases

## SEC-TC-001 — Access Customer Dashboard Without Login

**Priority:** P0
**Requirement:** REQ-SEC-001
**Risk:** RISK-005

### Expected Result

Access denied or redirected to authentication.

No customer data exposed.

---

## SEC-TC-002 — Access Account API Without Token

**Priority:** P0

Expected: denied.

---

## SEC-TC-003 — Access Transfer API Without Authentication

**Priority:** P0

Expected: denied with no financial effect.

---

## SEC-TC-004 — Access Admin Portal Without Authentication

**Priority:** P0

Expected: denied.

---

## SEC-TC-005 — Download Statement Without Authentication

**Priority:** P0

Expected: denied.

---

## SEC-TC-006 — Access Card API Without Authentication

**Priority:** P0

Expected: denied.

---

## SEC-TC-007 — Access Loan API Without Authentication

**Priority:** P0

Expected: denied.

---

# 8. Invalid Authentication Test Cases

## SEC-TC-008 — Invalid Credentials

**Priority:** P0

Expected: authentication denied.

---

## SEC-TC-009 — Empty Credentials

**Priority:** P1

Expected: rejected.

---

## SEC-TC-010 — Disabled Customer Login

**Priority:** P0

Expected: denied according to account-status policy.

---

## SEC-TC-011 — Suspended Customer Login

**Priority:** P0

Expected: restricted/denied according to policy.

---

## SEC-TC-012 — Locked Account Login

**Priority:** P0

Expected: denied until legitimate unlock condition.

---

# 9. Authentication Enumeration Test Cases

## SEC-TC-013 — Unknown Email vs Wrong Password

**Priority:** P1
**Requirement:** REQ-SEC-002

### Expected Result

Responses do not unnecessarily reveal whether an account exists.

---

## SEC-TC-014 — Password Reset Unknown Email

**Priority:** P1

Expected: response minimizes account enumeration.

---

## SEC-TC-015 — Registration Existing Email

**Priority:** P1

Expected: behavior balances usability and account-disclosure policy.

---

# 10. Brute-Force / Rate-Limit Test Cases

## SEC-TC-016 — Repeated Failed Login Attempts

**Priority:** P0
**Risk:** RISK-016

Expected: configured rate limiting/lockout/protection activates.

---

## SEC-TC-017 — Login Requests Above Rate Threshold

**Priority:** P0

Expected: excessive attempts throttled.

---

## SEC-TC-018 — Password Reset Request Flood

**Priority:** P1

Expected: abuse controls prevent notification/reset flooding.

---

## SEC-TC-019 — OTP Verification Flood

**Priority:** P0

Expected: attempt limits/rate controls applied.

---

## SEC-TC-020 — OTP Resend Flood

**Priority:** P1

Expected: resend throttled.

---

# 11. MFA Security Test Cases

## SEC-TC-021 — Login Without MFA After Password Success

**Priority:** P0
**Requirement:** REQ-SEC-003
**Risk:** RISK-005

### Preconditions

MFA enabled.

### Expected Result

Protected session is not fully established until MFA succeeds.

---

## SEC-TC-022 — Direct Dashboard Request Before MFA

**Priority:** P0

Expected: access denied.

---

## SEC-TC-023 — Direct API Request Before MFA

**Priority:** P0

Expected: denied.

---

## SEC-TC-024 — Manipulate Client MFA Flag

**Priority:** P0
**Risk:** RISK-037

Expected: backend MFA state remains authoritative.

---

## SEC-TC-025 — Skip MFA Endpoint

**Priority:** P0

Expected: no authenticated banking session granted.

---

# 12. OTP Lifecycle Security Test Cases

## SEC-TC-026 — Valid OTP

**Priority:** P0

Expected: accepted once.

---

## SEC-TC-027 — Invalid OTP

**Priority:** P0

Expected: rejected.

---

## SEC-TC-028 — Expired OTP

**Priority:** P0
**Risk:** RISK-045

Expected: rejected.

---

## SEC-TC-029 — Reuse Consumed OTP

**Priority:** P0
**Risk:** RISK-044

Expected: rejected.

---

## SEC-TC-030 — Old OTP After Resend

**Priority:** P0

Expected: invalid according to policy.

---

## SEC-TC-031 — OTP for Customer A Used on Customer B Session

**Priority:** P0

Expected: rejected.

---

## SEC-TC-032 — OTP for Login Used for Different Sensitive Action

**Priority:** P0

Expected: rejected unless intentionally scoped for both.

---

# 13. Password Reset Security

## SEC-TC-033 — Valid Reset Token

**Priority:** P0
**Requirement:** REQ-SEC-004

Expected: password reset succeeds once.

---

## SEC-TC-034 — Invalid Reset Token

**Priority:** P0

Expected: rejected.

---

## SEC-TC-035 — Expired Reset Token

**Priority:** P0

Expected: rejected.

---

## SEC-TC-036 — Reuse Reset Token

**Priority:** P0
**Risk:** RISK-046

Expected: rejected.

---

## SEC-TC-037 — Old Reset Token After New Reset Requested

**Priority:** P0

Expected: follows invalidation policy.

---

## SEC-TC-038 — Reset Token for Customer A Used Against Customer B

**Priority:** P0

Expected: rejected.

---

# 14. Session Security Test Cases

## SEC-TC-039 — Session Created After Successful Login

**Priority:** P0
**Requirement:** REQ-SEC-005

Expected: valid session associated with correct authenticated identity.

---

## SEC-TC-040 — Session Rotated After Authentication

**Priority:** P0

Expected: session fixation mitigated.

---

## SEC-TC-041 — Session Rotated After Privilege Change

**Priority:** P0

Expected: security context refreshed where required.

---

## SEC-TC-042 — Session Timeout

**Priority:** P0
**Risk:** RISK-015

Expected: expired session denied.

---

## SEC-TC-043 — API Call After Session Expiry

**Priority:** P0

Expected: denied.

---

## SEC-TC-044 — Browser Back After Session Expiry

**Priority:** P0

Expected: cached protected page cannot be used to perform authorized actions.

---

# 15. Logout and Revocation

## SEC-TC-045 — Logout Revokes Session

**Priority:** P0
**Risk:** RISK-014

Expected: session/token cannot access protected resource afterward.

---

## SEC-TC-046 — Logout in One Tab

**Priority:** P0

Expected: other tab follows session revocation behavior.

---

## SEC-TC-047 — API Token Used After Logout

**Priority:** P0

Expected: denied where token/session is revoked.

---

## SEC-TC-048 — Password Change Revokes Required Sessions

**Priority:** P0

Expected: session policy enforced.

---

## SEC-TC-049 — Manual Session Revocation

**Priority:** P0

Expected: revoked session unusable.

---

# 16. Session Fixation / Token Security

## SEC-TC-050 — Pre-Login Session Identifier Reused After Login

**Priority:** P0

Expected: authenticated session identifier rotated/secured.

---

## SEC-TC-051 — Session Token in URL

**Priority:** P0
**Risk:** RISK-021

Expected: reusable authentication token not exposed in URL.

---

## SEC-TC-052 — Session Token in Page Source

**Priority:** P0

Expected: secrets not unnecessarily exposed.

---

## SEC-TC-053 — Session Token in Logs

**Priority:** P0
**Risk:** RISK-032

Expected: token redacted/not logged.

---

# 17. Browser Storage Security

## SEC-TC-054 — Password Stored in Local Storage

**Priority:** P0

Expected: plaintext password absent.

---

## SEC-TC-055 — OTP Stored in Local Storage

**Priority:** P0

Expected: reusable OTP absent.

---

## SEC-TC-056 — Sensitive Card Data Stored in Browser Storage

**Priority:** P0

Expected: CVV/full sensitive data absent.

---

## SEC-TC-057 — Logout Clears/Invalidates Sensitive Client State

**Priority:** P0

Expected: stale local state cannot authorize actions.

---

# 18. Customer Resource Isolation — Accounts

## SEC-TC-058 — Customer A Reads Customer B Account

**Priority:** P0
**Requirement:** REQ-SEC-006
**Risk:** RISK-047

Expected: denied.

---

## SEC-TC-059 — Customer A Updates Customer B Account Alias

**Priority:** P0

Expected: denied.

---

## SEC-TC-060 — Customer A Uses Customer B Account as Transfer Source

**Priority:** P0

Expected: denied with no debit.

---

# 19. Customer Resource Isolation — Beneficiaries

## SEC-TC-061 — Customer A Reads Customer B Beneficiary

**Priority:** P0

Expected: denied.

---

## SEC-TC-062 — Customer A Uses Customer B Beneficiary

**Priority:** P0

Expected: denied.

---

## SEC-TC-063 — Customer A Deletes Customer B Beneficiary

**Priority:** P0

Expected: denied.

---

# 20. Customer Resource Isolation — Transactions

## SEC-TC-064 — Customer A Reads Customer B Transaction

**Priority:** P0

Expected: denied.

---

## SEC-TC-065 — Customer A Searches for Customer B Transaction Reference

**Priority:** P0

Expected: no unauthorized data disclosed.

---

# 21. Customer Resource Isolation — Statements

## SEC-TC-066 — Customer A Downloads Customer B Statement

**Priority:** P0

Expected: denied.

---

## SEC-TC-067 — Direct Statement URL IDOR

**Priority:** P0

Expected: denied.

---

# 22. Customer Resource Isolation — Cards

## SEC-TC-068 — Customer A Views Customer B Card

**Priority:** P0

Expected: denied.

---

## SEC-TC-069 — Customer A Freezes Customer B Card

**Priority:** P0

Expected: denied.

---

## SEC-TC-070 — Customer A Changes Customer B Card Limit

**Priority:** P0

Expected: denied.

---

# 23. Customer Resource Isolation — Loans

## SEC-TC-071 — Customer A Views Customer B Loan

**Priority:** P0

Expected: denied.

---

## SEC-TC-072 — Customer A Repays Customer B Loan Through Manipulated ID

**Priority:** P0

Expected: denied unless explicit authorized third-party payment exists.

---

# 24. Customer Resource Isolation — Deposits

## SEC-TC-073 — Customer A Views Customer B Deposit

**Priority:** P0

Expected: denied.

---

## SEC-TC-074 — Customer A Early-Withdraws Customer B Deposit

**Priority:** P0

Expected: denied.

---

# 25. Customer Resource Isolation — Notifications/Profile

## SEC-TC-075 — Customer A Reads Customer B Notification

**Priority:** P0

Expected: denied.

---

## SEC-TC-076 — Customer A Updates Customer B Profile

**Priority:** P0

Expected: denied.

---

## SEC-TC-077 — Customer A Revokes Customer B Session

**Priority:** P0

Expected: denied.

---

# 26. RBAC Test Cases

## SEC-TC-078 — Customer Accesses Admin Endpoint

**Priority:** P0
**Requirement:** REQ-SEC-007
**Risk:** RISK-006

Expected: denied.

---

## SEC-TC-079 — Read-Only Admin Performs State-Changing Action

**Priority:** P0

Expected: denied.

---

## SEC-TC-080 — KYC Reviewer Performs Loan Approval

**Priority:** P0

Expected: denied unless explicitly granted.

---

## SEC-TC-081 — Loan Officer Changes KYC Status

**Priority:** P0

Expected: denied.

---

## SEC-TC-082 — Operations Admin Accesses Restricted Security Administration Function

**Priority:** P0

Expected: role policy enforced.

---

## SEC-TC-083 — Role Removed During Active Session

**Priority:** P0

Expected: stale permissions cannot remain indefinitely usable.

---

# 27. Horizontal vs Vertical Privilege Escalation

## SEC-TC-084 — Horizontal Escalation Between Customers

**Priority:** P0

Expected: denied.

---

## SEC-TC-085 — Vertical Escalation Customer → Admin

**Priority:** P0

Expected: denied.

---

## SEC-TC-086 — Vertical Escalation Read-Only Admin → Operations Admin

**Priority:** P0

Expected: denied.

---

# 28. Mass Assignment Test Cases

## SEC-TC-087 — Profile Mass Assignment

**Priority:** P0
**Requirement:** REQ-SEC-008
**Risk:** RISK-037

Payload:

```json id="zi53t1"
{
  "firstName": "Updated",
  "role": "SUPER_ADMIN",
  "kycStatus": "VERIFIED",
  "riskScore": 0
}
```

### Expected Result

Only allowed fields may change.

---

## SEC-TC-088 — Account Mass Assignment

**Priority:** P0

Payload attempts:

```text id="9kbgsh"
balance
status
ownerId
currency
```

Expected: protected fields rejected/ignored.

---

## SEC-TC-089 — Transfer Mass Assignment

**Priority:** P0

Attempts:

```text id="9k5cb3"
fee = 0
status = COMPLETED
approved = true
```

Expected: backend ignores/rejects.

---

## SEC-TC-090 — Loan Mass Assignment

**Priority:** P0

Attempts:

```text id="1s9z98"
interestRate = 0
status = APPROVED
approvedAmount = maximum
```

Expected: denied.

---

## SEC-TC-091 — Card Mass Assignment

**Priority:** P0

Attempts:

```text id="0o2dgn"
status = ACTIVE
dailyLimit = unlimited
ownerId = CUST-002
```

Expected: denied.

---

# 29. Financial Parameter Tampering

## SEC-TC-092 — Transfer Amount Modified After Confirmation

**Priority:** P0
**Requirement:** REQ-SEC-009

### Expected Result

Server processes only authoritative validated amount.

---

## SEC-TC-093 — Transfer Fee Modified to Zero

**Priority:** P0
**Risk:** RISK-010

Expected: authoritative fee recalculated.

---

## SEC-TC-094 — Payment Fee Modified

**Priority:** P0

Expected: server fee used.

---

## SEC-TC-095 — Daily Limit Modified Client-Side

**Priority:** P0
**Risk:** RISK-008

Expected: authoritative limit enforced.

---

## SEC-TC-096 — Available Balance Modified in Client

**Priority:** P0

Expected: server balance validation unaffected.

---

## SEC-TC-097 — Beneficiary Destination Modified in Request

**Priority:** P0
**Risk:** RISK-017

Expected: destination validated against authorized beneficiary.

---

# 30. Transaction Status Tampering

## SEC-TC-098 — Customer Submits Transfer as COMPLETED

**Priority:** P0

Expected: rejected/ignored.

---

## SEC-TC-099 — Customer Marks Failed Payment as Success

**Priority:** P0

Expected: rejected.

---

## SEC-TC-100 — Customer Marks Loan as APPROVED

**Priority:** P0

Expected: rejected.

---

## SEC-TC-101 — Customer Marks Deposit as MATURED

**Priority:** P0

Expected: rejected.

---

# 31. Replay / Duplicate Security

## SEC-TC-102 — Replay Completed Transfer Request

**Priority:** P0
**Requirement:** REQ-SEC-010
**Risk:** RISK-003

Expected: no unintended second financial effect.

---

## SEC-TC-103 — Replay Completed Payment Request

**Priority:** P0

Expected: no duplicate debit.

---

## SEC-TC-104 — Replay Loan Disbursement Request

**Priority:** P0

Expected: one disbursement only.

---

## SEC-TC-105 — Replay Deposit Maturity Payout

**Priority:** P0

Expected: one payout only.

---

## SEC-TC-106 — Replay Card Settlement

**Priority:** P0

Expected: no duplicate settlement.

---

# 32. Idempotency-Key Abuse

## SEC-TC-107 — Same Key Same Transfer Payload

**Priority:** P0

Expected: one financial effect.

---

## SEC-TC-108 — Same Key Different Transfer Payload

**Priority:** P0

Expected: conflict rejected.

---

## SEC-TC-109 — Customer A Reuses Customer B Idempotency Key

**Priority:** P0

Expected: no cross-customer collision or data disclosure.

---

## SEC-TC-110 — Very Long Idempotency Key

**Priority:** P1

Expected: safely constrained/validated.

---

# 33. CSRF Test Cases

## SEC-TC-111 — Cross-Site Profile Update Attempt

**Priority:** P0
**Requirement:** REQ-SEC-011

Where cookie-based browser authentication applies.

Expected: request rejected unless valid anti-CSRF/origin protection exists.

---

## SEC-TC-112 — Cross-Site Transfer Submission Attempt

**Priority:** P0

Expected: unauthorized cross-origin action prevented.

---

## SEC-TC-113 — Cross-Site Card Freeze/Unfreeze Attempt

**Priority:** P0

Expected: prevented.

---

# 34. XSS Test Cases

## SEC-TC-114 — Script Payload in Beneficiary Alias

**Priority:** P1

Example:

```text id="o70vq0"
<script>alert(1)</script>
```

Expected: rendered safely; no execution.

---

## SEC-TC-115 — Script Payload in Profile Name

**Priority:** P1

Expected: safely encoded.

---

## SEC-TC-116 — Script Payload in Transfer Description

**Priority:** P1

Expected: safely encoded across:

```text id="87mwma"
Confirmation

History

Statements

Notifications

Admin screens
```

---

## SEC-TC-117 — Stored XSS Through Admin-Visible Field

**Priority:** P0

Expected: content remains safely rendered when viewed by privileged users.

---

# 35. Injection Resistance

## SEC-TC-118 — SQL-Like Login Input

**Priority:** P0

Example:

```text id="v5w0w2"
' OR '1'='1
```

Expected: authentication not bypassed.

---

## SEC-TC-119 — SQL-Like Search Input

**Priority:** P1

Expected: no database-query manipulation.

---

## SEC-TC-120 — SQL-Like Transaction Reference

**Priority:** P1

Expected: safely handled.

---

## SEC-TC-121 — Unexpected Structured Input

**Priority:** P1

Expected: validation rejects unsafe type/format.

---

# 36. Path / File Security

## SEC-TC-122 — Statement File Path Manipulation

**Priority:** P0

Expected: cannot access arbitrary files or another customer's statement.

---

## SEC-TC-123 — Uploaded File Name Path Manipulation

**Priority:** P0

Where file uploads exist.

Expected: unsafe path components ignored/rejected.

---

## SEC-TC-124 — Unsupported File Type Upload

**Priority:** P1

Expected: rejected.

---

## SEC-TC-125 — Oversized File Upload

**Priority:** P1

Expected: size restrictions enforced.

---

## SEC-TC-126 — File Content Does Not Match Extension

**Priority:** P1

Expected: validation based on appropriate content/type controls, not filename alone.

---

# 37. Sensitive Data Exposure — API

## SEC-TC-127 — Profile API Returns Password Field

**Priority:** P0
**Requirement:** REQ-SEC-012
**Risk:** RISK-021

Expected: password absent.

---

## SEC-TC-128 — API Returns Password Hash

**Priority:** P0

Expected: absent.

---

## SEC-TC-129 — Card API Returns Full CVV

**Priority:** P0

Expected: absent.

---

## SEC-TC-130 — Card API Unnecessarily Returns Full PAN

**Priority:** P0

Expected: appropriately masked/minimized.

---

## SEC-TC-131 — Session API Returns Raw Session Token

**Priority:** P0

Expected: absent where unnecessary.

---

## SEC-TC-132 — OTP Returned in Generic API Response

**Priority:** P0

Expected: absent.

---

# 38. Sensitive Data Exposure — UI / Logs

## SEC-TC-133 — Password Visible in Page Source

**Priority:** P0

Expected: absent.

---

## SEC-TC-134 — OTP Exposed in Logs

**Priority:** P0
**Risk:** RISK-032

Expected: absent/redacted.

---

## SEC-TC-135 — Access Token Exposed in Audit Logs

**Priority:** P0

Expected: absent/redacted.

---

## SEC-TC-136 — Full Card Data in Admin Logs

**Priority:** P0

Expected: appropriately masked.

---

# 39. Error Message Security

## SEC-TC-137 — Server Error Reveals Stack Trace

**Priority:** P1
**Requirement:** REQ-SEC-013

Expected: internal stack details hidden from customer.

---

## SEC-TC-138 — Database Error Reveals SQL

**Priority:** P1

Expected: internal query/schema details hidden.

---

## SEC-TC-139 — Authorization Failure Reveals Resource Details

**Priority:** P1

Expected: response does not leak protected resource metadata.

---

## SEC-TC-140 — Login Failure Reveals Password Validation Internals

**Priority:** P2

Expected: unnecessary security implementation details hidden.

---

# 40. Security Header Test Cases

## SEC-TC-141 — Content Security Policy

**Priority:** P1
**Requirement:** REQ-SEC-014

Expected: configured according to application security design.

---

## SEC-TC-142 — Frame Protection

**Priority:** P1

Expected: clickjacking protection present where required.

---

## SEC-TC-143 — MIME Sniffing Protection

**Priority:** P2

Expected: browser content-type protections configured.

---

## SEC-TC-144 — Referrer Policy

**Priority:** P2

Expected: sensitive URL data not leaked unnecessarily.

---

## SEC-TC-145 — Strict Transport Security

**Priority:** P1

For HTTPS deployment.

Expected: secure transport policy configured appropriately.

---

# 41. Cookie Security Test Cases

## SEC-TC-146 — Secure Session Cookie

**Priority:** P0

Where session cookies are used.

Expected: Secure attribute enabled on HTTPS.

---

## SEC-TC-147 — HttpOnly Session Cookie

**Priority:** P0

Expected: client-side script access prevented where appropriate.

---

## SEC-TC-148 — SameSite Cookie Policy

**Priority:** P1

Expected: policy reduces cross-site request risk while supporting application requirements.

---

# 42. API Method Security

## SEC-TC-149 — Unsupported HTTP Method

**Priority:** P1

Expected: rejected.

---

## SEC-TC-150 — GET Does Not Perform Sensitive State Change

**Priority:** P0

Expected: sensitive financial/security mutation not triggered by simple GET.

---

## SEC-TC-151 — Override Method Header Abuse

**Priority:** P1

Expected: unauthorized method override cannot bypass endpoint controls.

---

# 43. API Content-Type / Payload Validation

## SEC-TC-152 — Wrong Content Type

**Priority:** P1

Expected: rejected where endpoint requires specific format.

---

## SEC-TC-153 — Malformed JSON

**Priority:** P1

Expected: safe validation response.

---

## SEC-TC-154 — Deeply Nested Unexpected Payload

**Priority:** P1

Expected: handled safely without resource exhaustion or security bypass.

---

## SEC-TC-155 — Unknown Protected Fields

**Priority:** P0

Expected: ignored/rejected according to strict schema policy.

---

# 44. Admin Portal Security

## SEC-TC-156 — Customer Opens Admin URL

**Priority:** P0
**Requirement:** REQ-SEC-015

Expected: denied.

---

## SEC-TC-157 — Customer Calls Admin API Directly

**Priority:** P0

Expected: denied.

---

## SEC-TC-158 — Read-Only Admin Calls Reversal API

**Priority:** P0
**Risk:** RISK-023

Expected: denied.

---

## SEC-TC-159 — KYC Reviewer Calls Account Freeze API

**Priority:** P0

Expected: denied unless explicitly authorized.

---

## SEC-TC-160 — Loan Officer Calls User-Role Administration API

**Priority:** P0

Expected: denied.

---

# 45. Admin Sensitive Data Minimization

## SEC-TC-161 — Support/Read-Only Role Sees Excess Sensitive Data

**Priority:** P0

Expected: only role-necessary fields visible.

---

## SEC-TC-162 — Admin Search Reveals Full Authentication Data

**Priority:** P0

Expected: no password/hash/token/OTP data.

---

# 46. Financial Authorization Security

## SEC-TC-163 — Transfer From Unauthorized Account

**Priority:** P0

Expected: denied.

---

## SEC-TC-164 — Payment From Unauthorized Account

**Priority:** P0

Expected: denied.

---

## SEC-TC-165 — Deposit Funding From Unauthorized Account

**Priority:** P0

Expected: denied.

---

## SEC-TC-166 — Loan Disbursement to Manipulated Unauthorized Account

**Priority:** P0

Expected: denied.

---

# 47. Account-State Security Enforcement

## SEC-TC-167 — Frozen Account Transfer via API

**Priority:** P0
**Risk:** RISK-009

Expected: rejected.

---

## SEC-TC-168 — Frozen Account Payment via API

**Priority:** P0

Expected: rejected.

---

## SEC-TC-169 — Closed Account Transaction API

**Priority:** P0

Expected: rejected.

---

## SEC-TC-170 — Stale Active UI Against Frozen Backend

**Priority:** P0
**Risk:** RISK-048

Expected: backend state wins.

---

# 48. Card-State Security Enforcement

## SEC-TC-171 — Frozen Card Purchase

**Priority:** P0
**Risk:** RISK-024

Expected: declined.

---

## SEC-TC-172 — Blocked Card Purchase

**Priority:** P0
**Risk:** RISK-025

Expected: declined.

---

## SEC-TC-173 — Old Replaced Card Purchase

**Priority:** P0

Expected: declined.

---

# 49. Beneficiary Security Enforcement

## SEC-TC-174 — Pending Beneficiary Used Through API

**Priority:** P0

Expected: rejected.

---

## SEC-TC-175 — Disabled Beneficiary Used Through API

**Priority:** P0

Expected: rejected.

---

## SEC-TC-176 — Deleted Beneficiary Used Through API

**Priority:** P0

Expected: rejected.

---

# 50. Security Notification Validation

## SEC-TC-177 — Password Change Security Notification

**Priority:** P0

Expected: generated according to policy and contains no secret.

---

## SEC-TC-178 — MFA Disabled Notification

**Priority:** P0

Expected: generated.

---

## SEC-TC-179 — Card Block Notification

**Priority:** P1

Expected: correct.

---

## SEC-TC-180 — Failed Security Action Sends No False Success

**Priority:** P0
**Risk:** RISK-034

Expected: notification reflects authoritative result.

---

# 51. Audit Security Test Cases

## SEC-TC-181 — Privileged Admin Action Audited

**Priority:** P0
**Requirement:** REQ-SEC-016
**Risk:** RISK-022

Expected:

```text id="fglymr"
Actor

Role

Action

Target

Timestamp

Reason where required
```

---

## SEC-TC-182 — Unauthorized Attempt Logged Where Required

**Priority:** P1

Expected: useful security telemetry without exposing secrets.

---

## SEC-TC-183 — Audit Record Cannot Be Customer-Modified

**Priority:** P0

Expected: denied.

---

## SEC-TC-184 — Read-Only Admin Cannot Alter Audit Records

**Priority:** P0

Expected: denied.

---

## SEC-TC-185 — Secrets Redacted From Audit

**Priority:** P0
**Risk:** RISK-032

Expected:

```text id="6ab310"
Password:
Absent

OTP:
Absent

Access Token:
Absent / Redacted

CVV:
Absent
```

---

# 52. Fail-Closed Test Cases

## SEC-TC-186 — Authorization Service Unavailable During Transfer

**Priority:** P0
**Requirement:** REQ-SEC-017

Expected: transfer denied rather than allowed without authorization.

---

## SEC-TC-187 — Role Service Unavailable for Admin Action

**Priority:** P0

Expected: privileged action denied safely.

---

## SEC-TC-188 — Beneficiary Validation Service Unavailable

**Priority:** P0

Expected: transfer not authorized based on unverified stale assumption.

---

## SEC-TC-189 — Card State Service Unavailable

**Priority:** P0

Expected: transaction follows documented secure failure policy.

---

## SEC-TC-190 — Security Policy Configuration Missing

**Priority:** P0

Expected: system does not silently default to insecure unrestricted access.

---

# 53. Stale Authorization Test Cases

## SEC-TC-191 — Admin Role Revoked While Admin Page Open

**Priority:** P0

Expected: later protected action revalidates permission.

---

## SEC-TC-192 — Customer Account Suspended While Transfer Page Open

**Priority:** P0

Expected: final submit denied if current status prohibits transfer.

---

## SEC-TC-193 — Beneficiary Disabled While Transfer Confirmation Open

**Priority:** P0

Expected: final submit denied.

---

## SEC-TC-194 — Card Frozen While Purchase Context Open

**Priority:** P0

Expected: backend current state enforced.

---

# 54. Concurrency Security Test Cases

## SEC-TC-195 — Concurrent Transfer Limit Bypass Attempt

**Priority:** P0
**Risk:** RISK-024

### Example

```text id="un1i18"
Remaining Daily Limit:
1,000.00

Request A:
700.00

Request B:
600.00
```

Expected: successful total cannot exceed limit.

---

## SEC-TC-196 — Concurrent Available-Balance Bypass

**Priority:** P0
**Risk:** RISK-013

Example:

```text id="stf8sf"
Available:
1,000.00

Request A:
800.00

Request B:
500.00
```

Expected: no overspending.

---

## SEC-TC-197 — Concurrent Loan Disbursement

**Priority:** P0

Expected: one disbursement.

---

## SEC-TC-198 — Concurrent Deposit Maturity Payout

**Priority:** P0

Expected: one payout.

---

## SEC-TC-199 — Concurrent Duplicate Payment Callback

**Priority:** P0

Expected: one payment settlement effect.

---

# 55. Security Against Client-Side-Only Controls

## SEC-TC-200 — Hidden Button Access Through Direct API

**Priority:** P0
**Risk:** RISK-037

Expected: backend independently denies action.

---

## SEC-TC-201 — Disabled UI Control Re-enabled in Browser

**Priority:** P0

Expected: server still enforces business/security rule.

---

## SEC-TC-202 — Modified JavaScript Validation

**Priority:** P0

Expected: backend validation prevents bypass.

---

# 56. Cross-Origin / CORS Test Cases

## SEC-TC-203 — Unauthorized Origin API Request

**Priority:** P1

Where browser CORS applies.

Expected: configuration does not expose protected API unnecessarily.

---

## SEC-TC-204 — Credentialed Cross-Origin Request

**Priority:** P0

Expected: only explicitly permitted trusted origin behavior.

---

# 57. Cache / Sensitive Page Test Cases

## SEC-TC-205 — Protected Page After Logout via Browser Back

**Priority:** P0

Expected: sensitive data is not interactively usable after logout.

---

## SEC-TC-206 — Statement Download Cached for Another Session

**Priority:** P0

Expected: cache behavior does not expose protected financial file to unrelated user/session.

---

# 58. Redirect / Link Safety

## SEC-TC-207 — Manipulated Post-Login Redirect

**Priority:** P1

Expected: redirect constrained to allowed destinations.

---

## SEC-TC-208 — Password Reset Redirect Manipulation

**Priority:** P1

Expected: cannot redirect sensitive flow to untrusted destination where disallowed.

---

# 59. Business Logic Abuse Cases

## SEC-TC-209 — Transfer Fee Bypass Through Alternate Endpoint

**Priority:** P0

Expected: consistent fee rules across all transfer paths.

---

## SEC-TC-210 — Limit Bypass Through Scheduled Transfer

**Priority:** P0

Expected: scheduled transfer still respects authoritative limits at appropriate validation/execution stages.

---

## SEC-TC-211 — Frozen Account Bypass Through Scheduled Transaction

**Priority:** P0

Expected: source state revalidated at execution.

---

## SEC-TC-212 — Closed Destination Bypass Through Stored Beneficiary

**Priority:** P0

Expected: destination state revalidated.

---

# 60. End-to-End Authentication Bypass Test

## SEC-TC-213 — Password → MFA → Protected Banking Action

**Priority:** P0

### Steps

1. Authenticate with valid password.
2. Do not complete MFA.
3. Request dashboard.
4. Request account API.
5. Request transfer API.
6. Complete MFA.
7. Repeat requests.

### Expected Result

Before MFA:

```text id="ujgmqj"
Protected Access:
Denied
```

After valid MFA:

```text id="l3gkkj"
Protected Access:
Allowed according to role/ownership
```

---

# 61. End-to-End Customer Isolation Test

## SEC-TC-214 — Customer A Attempts Customer B Resources

**Priority:** P0

Attempt Customer B resources through:

```text id="jujji8"
Accounts

Beneficiaries

Transactions

Statements

Cards

Loans

Deposits

Notifications

Profile

Sessions
```

using:

```text id="taszv6"
UI URLs

Direct IDs

API endpoints

Search

Pagination

Modified payloads
```

### Expected Result

Every unauthorized request is denied.

No protected Customer B data is returned.

---

# 62. End-to-End Role Escalation Test

## SEC-TC-215 — Customer Attempts Admin Operations

**Priority:** P0

Try:

```text id="0bkm7s"
Freeze account

Change KYC

Approve loan

Reverse transfer

Change user role

Read audit logs
```

### Expected Result

Every privileged action denied.

---

# 63. End-to-End Financial Tampering Test

## SEC-TC-216 — Manipulated Transfer Request

**Priority:** P0

### Valid Intended Transfer

```text id="vq080n"
Source:
ACC-001

Amount:
1,000.00

Fee:
10.00
```

### Tampered Request Attempts

```text id="42t3wz"
fee = 0

status = COMPLETED

sourceAccount = Customer B account

limit = unlimited

balance = 999999
```

### Expected Result

Backend authoritative rules prevail.

No unauthorized financial effect occurs.

---

# 64. End-to-End Replay Test

## SEC-TC-217 — Replay Completed Financial Request

**Priority:** P0

### Steps

1. Complete valid transfer.
2. Capture request identity/idempotency context.
3. Replay request multiple times.
4. Inspect account, API, DB, history.

### Expected Result

```text id="cppwp8"
Intended Financial Effects:
1

Unexpected Duplicate Effects:
0
```

---

# 65. End-to-End Session Revocation Test

## SEC-TC-218 — Password Change Invalidates Required Sessions

**Priority:** P0

### Steps

1. Login Session A.
2. Login Session B.
3. Change password in A.
4. Use B to open protected resource.
5. Use B to call API.
6. Attempt old password.
7. Attempt new password.

### Expected Result

Revocation and password policy are enforced consistently.

---

# 66. End-to-End Mass Assignment Test

## SEC-TC-219 — Customer Profile Privilege Escalation Attempt

**Priority:** P0

### Payload

```json id="dozq5t"
{
  "firstName": "Updated",
  "role": "SUPER_ADMIN",
  "kycStatus": "VERIFIED",
  "riskScore": 0,
  "customerStatus": "ACTIVE"
}
```

### Expected Result

```text id="mcp0pz"
Allowed field:
May update

Protected fields:
Must remain unchanged
```

No privilege escalation.

---

# 67. End-to-End Fail-Closed Test

## SEC-TC-220 — Authorization Dependency Failure

**Priority:** P0

### Steps

1. Begin protected financial operation.
2. Simulate authorization-policy dependency failure.
3. Submit operation.

### Expected Result

```text id="kj0uw4"
Financial Operation:
Denied / Safely Deferred

Unauthorized Success:
No
```

---

# 68. Security Risk Mapping

| Risk                                     | Related Test Cases                     |
| ---------------------------------------- | -------------------------------------- |
| RISK-002 Unauthorized customer data      | SEC-TC-058–077, 127–132, 214           |
| RISK-003 Duplicate transaction           | SEC-TC-102–110, 195–199, 217           |
| RISK-005 Authentication bypass           | SEC-TC-001–012, 021–025, 213           |
| RISK-006 Privilege escalation            | SEC-TC-078–091, 156–166, 215, 219      |
| RISK-008 Limit bypass                    | SEC-TC-095, 195, 210                   |
| RISK-009 Frozen account transaction      | SEC-TC-167–170, 211                    |
| RISK-010 Fee calculation/tampering       | SEC-TC-093–094, 209, 216               |
| RISK-013 Concurrency                     | SEC-TC-195–199                         |
| RISK-014 Session remains active          | SEC-TC-045–053, 218                    |
| RISK-015 Expired session                 | SEC-TC-042–044                         |
| RISK-016 Lockout/rate limiting           | SEC-TC-016–020                         |
| RISK-017 Wrong beneficiary               | SEC-TC-097, 174–176                    |
| RISK-018 Closed destination              | SEC-TC-212                             |
| RISK-021 Sensitive exposure              | SEC-TC-051–057, 127–140, 161–162       |
| RISK-022 Missing audit                   | SEC-TC-181–185                         |
| RISK-023 Unauthorized admin              | SEC-TC-078–086, 156–160                |
| RISK-024 Frozen card usable              | SEC-TC-171, 194                        |
| RISK-025 Blocked card usable             | SEC-TC-172                             |
| RISK-030 Unauthorized API                | SEC-TC-002–007, 058–110, 149–176       |
| RISK-032 Sensitive audit/log exposure    | SEC-TC-053, 134–136, 185               |
| RISK-034 False notification              | SEC-TC-177–180                         |
| RISK-037 Frontend-only validation bypass | SEC-TC-024, 087–101, 200–202, 216, 219 |
| RISK-039 API/DB inconsistency            | SEC-TC-102–110, 195–199, 217           |
| RISK-042 Retry duplication               | SEC-TC-102–110, 217                    |
| RISK-044 OTP reuse                       | SEC-TC-029–032                         |
| RISK-045 Expired OTP                     | SEC-TC-028                             |
| RISK-046 Reset token reuse               | SEC-TC-033–038                         |
| RISK-047 IDOR                            | SEC-TC-058–077, 214                    |
| RISK-048 UI/backend mismatch             | SEC-TC-170, 191–194, 200–202           |

---

# 69. Requirements Mapping

| Requirement                                            | Test Cases     |
| ------------------------------------------------------ | -------------- |
| REQ-SEC-001 Protected resources require authentication | SEC-TC-001–012 |
| REQ-SEC-002 Enumeration/rate-limit resistance          | SEC-TC-013–020 |
| REQ-SEC-003 MFA/OTP security                           | SEC-TC-021–032 |
| REQ-SEC-004 Reset-token security                       | SEC-TC-033–038 |
| REQ-SEC-005 Session security                           | SEC-TC-039–057 |
| REQ-SEC-006 Customer isolation / IDOR                  | SEC-TC-058–077 |
| REQ-SEC-007 RBAC / privilege control                   | SEC-TC-078–086 |
| REQ-SEC-008 Mass-assignment protection                 | SEC-TC-087–091 |
| REQ-SEC-009 Financial tamper protection                | SEC-TC-092–101 |
| REQ-SEC-010 Replay/idempotency protection              | SEC-TC-102–110 |
| REQ-SEC-011 Browser/web request security               | SEC-TC-111–126 |
| REQ-SEC-012 Sensitive data protection                  | SEC-TC-127–136 |
| REQ-SEC-013 Safe errors                                | SEC-TC-137–140 |
| REQ-SEC-014 Browser/transport security                 | SEC-TC-141–155 |
| REQ-SEC-015 Admin security                             | SEC-TC-156–162 |
| REQ-SEC-016 Auditability                               | SEC-TC-177–185 |
| REQ-SEC-017 Fail-closed/stale-state security           | SEC-TC-186–220 |

---

# 70. Smoke Candidates

Recommended security smoke coverage:

```text id="69eqd5"
SEC-TC-001
SEC-TC-003
SEC-TC-004
SEC-TC-021
SEC-TC-028
SEC-TC-029
SEC-TC-042
SEC-TC-045
SEC-TC-058
SEC-TC-060
SEC-TC-066
SEC-TC-078
SEC-TC-079
SEC-TC-087
SEC-TC-092
SEC-TC-102
SEC-TC-127
SEC-TC-167
SEC-TC-171
SEC-TC-186
```

---

# 71. Sanity Candidates

After authentication/security changes:

```text id="9i8v84"
SEC-TC-001
SEC-TC-016
SEC-TC-021
SEC-TC-026
SEC-TC-028
SEC-TC-029
SEC-TC-036
SEC-TC-040
SEC-TC-042
SEC-TC-045
SEC-TC-058
SEC-TC-078
SEC-TC-087
SEC-TC-092
SEC-TC-102
SEC-TC-118
SEC-TC-127
SEC-TC-156
SEC-TC-167
SEC-TC-186
```

---

# 72. Critical Regression Candidates

```text id="aa8fk1"
SEC-TC-001–012

SEC-TC-016–057

SEC-TC-058–110

SEC-TC-111–140

SEC-TC-146–220
```

---

# 73. UI Automation Candidates

Best suited for:

```text id="n18gtd"
Playwright

Selenium

Cypress
```

Strong candidates:

```text id="02ks6m"
SEC-TC-001
SEC-TC-004
SEC-TC-008–057
SEC-TC-058–086
SEC-TC-111–117
SEC-TC-141–148
SEC-TC-156–162
SEC-TC-170–180
SEC-TC-191–220
```

Playwright is particularly valuable for:

```text id="wqlnqo"
Multiple browser contexts

Customer A / Customer B isolation

Admin vs customer sessions

Session revocation

Stale-tab testing

MFA state testing
```

---

# 74. API Automation Candidates

Best suited for:

```text id="6qgcr5"
REST Assured

Postman
```

Strong candidates:

```text id="45peec"
SEC-TC-002–007

SEC-TC-021–038

SEC-TC-042–053

SEC-TC-058–110

SEC-TC-118–140

SEC-TC-149–220
```

High-value API-security automation includes:

```text id="8cr027"
IDOR

RBAC

Mass assignment

Financial tampering

Replay

Idempotency

State validation

Unauthenticated access

Protected-field mutation
```

---

# 75. SQL / Database Testing Candidates

Strong SQL candidates:

```text id="l5lbpx"
SEC-TC-087–110

SEC-TC-127–136

SEC-TC-181–199

SEC-TC-214–220
```

Database validation should verify:

```text id="qhglhy"
Ownership

Role assignment

Customer/resource relationships

Protected field integrity

Transaction uniqueness

Idempotency results

Audit records

Session revocation state

Financial state

No unauthorized persistence
```

---

# 76. JMeter / Concurrency Security Candidates

Strong candidates:

```text id="38o2tz"
SEC-TC-016–020

SEC-TC-102–110

SEC-TC-195–199
```

JMeter can validate:

```text id="52o8jd"
Rate limiting

Brute-force protection

Concurrent limit enforcement

Concurrent balance enforcement

Duplicate prevention

Exactly-once financial processing
```

---

# 77. Jest Candidates

Jest is useful for testing security-sensitive business logic such as:

```text id="i6wbfg"
Permission functions

Role matrices

Limit calculations

Fee authority

Input schemas

Protected-field allowlists

State transition rules
```

Potential mappings:

```text id="lqkn6o"
SEC-TC-078–101

SEC-TC-155

SEC-TC-167–176

SEC-TC-200–212
```

---

# 78. Security Evidence Requirements

For critical security tests, capture as applicable:

```text id="74ib1i"
User ID

Role

Target resource ID

Expected authorization result

Actual authorization result

HTTP method

Endpoint

Response status

Safe response body excerpt

Financial state before/after

Database state

Audit event

Session state

Timestamp

Screenshot

Defect ID
```

Never place in evidence:

```text id="fj5vi8"
Passwords

OTP values

CVV

PIN

Raw access tokens

Raw refresh tokens

MFA secret seeds

Production customer secrets
```

---

# 79. Security Defect Examples

Potential Critical/High defects include:

```text id="exz2em"
Unauthenticated transfer API succeeds.

Customer accesses another customer's account.

Customer downloads another customer's statement.

Customer changes own role to admin.

Read-only admin reverses transaction.

MFA can be bypassed through direct API.

Expired OTP remains valid.

Used reset token remains valid.

Logout does not revoke required session.

Customer modifies transfer fee.

Customer modifies account balance.

Customer marks transfer completed.

Replay causes duplicate debit.

Frozen account transacts through API.

Frozen card remains usable.

Admin API exposes password hashes.

Audit logs contain access tokens.

Authorization service failure allows transfer.

Concurrent requests bypass daily transfer limit.

Client-side disabled control can be bypassed through direct endpoint.
```

---

# 80. Security Release Blockers

Production release should normally be blocked by unresolved issues involving:

```text id="8mlb80"
Authentication bypass

MFA bypass

Cross-customer data access

IDOR on financial resources

Privilege escalation

Unauthorized admin operations

Protected-field modification

Financial parameter tampering

Replay causing duplicate money movement

Balance/limit race-condition bypass

Frozen/blocked state bypass

Session revocation failure with material risk

Password-reset token reuse

Sensitive credential exposure

Full protected card-data exposure

Audit secret leakage

Security control failure that defaults to allow
```

---

# 81. Security Exit Criteria

Security testing is acceptable when:

```text id="798hcl"
Protected functionality requires valid authentication.

MFA cannot be bypassed.

OTP/reset-token lifecycles are secure.

Sessions expire/revoke correctly.

Customer isolation is enforced across every resource.

RBAC follows least privilege.

Privilege escalation is blocked.

Mass assignment is blocked.

Financial parameters are server-authoritative.

Replay/idempotency protection works.

Rate limiting is effective.

Input handling prevents unsafe execution.

Sensitive data is minimized and protected.

Browser/session controls meet security requirements.

Admin operations are appropriately restricted.

Security-relevant actions are audited.

Security failures fail closed.

Concurrent requests cannot bypass balance/limit controls.

No unresolved Critical/P0 security defect remains.
```

---

# 82. Final Security Testing Principle

Security testing for the Banking System is not limited to checking:

```text id="213c10"
Can the user log in?
```

It must also prove:

```text id="gdyzc7"
Can the user access only what they own?

Can they perform only actions their role permits?

Can client data be trusted? No.

Can stale state bypass current restrictions? No.

Can repeated requests duplicate money? No.

Can concurrent requests bypass limits? No.

Can sensitive information leak? No.

Can a security dependency failure accidentally grant access? No.
```

The critical security invariant is:

```text id="2zmy6g"
Every protected banking action
must be authenticated,
authorized,
validated against authoritative server state,
and executed only within the user's permitted scope.
```

The core rule is:

```text id="e3ionk"
Never trust the client for identity,
authorization,
financial state,
security state,
or transaction finality.
```
