# Banking System — Pairwise Testing

## 1. Document Information

| Field                 | Value                          |
| --------------------- | ------------------------------ |
| Project               | Banking System Testing Project |
| Test Design Technique | Pairwise Testing               |
| Document              | Test Design                    |
| Version               | 1.0                            |
| Status                | Draft                          |
| Owner                 | QA Engineering                 |

---

# 2. Purpose

This document applies **Pairwise Testing** to the Banking System.

Pairwise testing is useful when a feature contains many parameters and testing every possible combination would create an excessively large test suite.

The goal is to select a smaller set of test combinations so that every pair of parameter values appears together at least once.

Pairwise testing is particularly useful for:

* Browsers
* Viewports
* Customer states
* Account states
* Transaction types
* Payment methods
* Card states
* User roles
* Notification channels
* Loan products
* Deposit products
* API request combinations
* Device/session combinations

Pairwise testing supplements, but does not replace:

* Risk-based testing
* Boundary Value Analysis
* Equivalence Partitioning
* Decision Tables
* State Transition Testing
* Security testing
* Critical end-to-end testing

---

# 3. Why Pairwise Testing Is Useful

Suppose a transfer feature has:

```text
4 browsers
5 account states
4 beneficiary states
3 balance conditions
3 transaction types
```

Exhaustive combinations would require:

```text
4 × 5 × 4 × 3 × 3 = 720 combinations
```

Many of those combinations provide limited additional value.

Pairwise testing creates a smaller representative set while ensuring that every pair of values interacts at least once.

---

# 4. Pairwise Testing Principle

If parameters are:

```text
Browser:
Chrome
Edge
Firefox

Account State:
Active
Frozen
Restricted

Balance:
Sufficient
Insufficient
```

Pairwise testing ensures combinations such as:

```text
Chrome + Active
Chrome + Frozen
Chrome + Restricted

Edge + Active
Edge + Frozen
Edge + Restricted

Firefox + Active
Firefox + Frozen
Firefox + Restricted

Active + Sufficient
Active + Insufficient

Frozen + Sufficient
Frozen + Insufficient
```

are represented without necessarily testing every complete three-way combination.

---

# 5. Scenario Naming Convention

Pairwise scenarios use:

```text
PW-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 6. When Pairwise Testing Should Not Be Used Alone

Pairwise testing should not be the only technique used for:

```text
Authentication bypass
Authorization
Money movement
Balance integrity
Idempotency
Concurrency
Transaction atomicity
Financial calculation boundaries
State-transition rules
Audit requirements
```

These high-risk behaviors need dedicated tests even if pairwise generation does not select them.

---

# 7. Browser + Viewport Pairwise Model

Parameters:

### Browser

```text
Chrome
Edge
Firefox
WebKit/Safari
```

### Viewport

```text
1920x1080
1366x768
768x1024
390x844
360x800
```

Representative pairwise matrix:

| ID     | Browser | Viewport  |
| ------ | ------- | --------- |
| PW-001 | Chrome  | 1920x1080 |
| PW-002 | Chrome  | 390x844   |
| PW-003 | Edge    | 1366x768  |
| PW-004 | Edge    | 360x800   |
| PW-005 | Firefox | 768x1024  |
| PW-006 | Firefox | 1920x1080 |
| PW-007 | WebKit  | 390x844   |
| PW-008 | WebKit  | 1366x768  |
| PW-009 | Chrome  | 768x1024  |
| PW-010 | Firefox | 360x800   |

Expected:

Critical screens remain usable and functional across representative combinations.

---

# 8. Browser + Module Pairwise Model

Parameters:

### Browser

```text
Chrome
Edge
Firefox
WebKit
```

### Module

```text
Authentication
Accounts
Transfers
Payments
Cards
Loans
Deposits
Statements
Admin
```

Representative coverage:

| ID     | Browser | Module         |
| ------ | ------- | -------------- |
| PW-011 | Chrome  | Authentication |
| PW-012 | Edge    | Accounts       |
| PW-013 | Firefox | Transfers      |
| PW-014 | WebKit  | Payments       |
| PW-015 | Chrome  | Cards          |
| PW-016 | Edge    | Loans          |
| PW-017 | Firefox | Deposits       |
| PW-018 | WebKit  | Statements     |
| PW-019 | Chrome  | Admin          |
| PW-020 | Edge    | Transfers      |
| PW-021 | Firefox | Authentication |
| PW-022 | WebKit  | Accounts       |

Critical modules should still receive dedicated multi-browser regression beyond this reduced set.

---

# 9. Customer Status + KYC Status Pairwise Model

Customer Status:

```text
ACTIVE
RESTRICTED
SUSPENDED
DISABLED
```

KYC Status:

```text
VERIFIED
PENDING
REJECTED
EXPIRED
```

Representative combinations:

| ID     | Customer Status | KYC Status | Expected Focus                   |
| ------ | --------------- | ---------- | -------------------------------- |
| PW-023 | ACTIVE          | VERIFIED   | Full functionality               |
| PW-024 | ACTIVE          | PENDING    | KYC restrictions                 |
| PW-025 | ACTIVE          | REJECTED   | Product restrictions             |
| PW-026 | ACTIVE          | EXPIRED    | Reverification                   |
| PW-027 | RESTRICTED      | VERIFIED   | Customer restrictions            |
| PW-028 | RESTRICTED      | PENDING    | Combined restrictions            |
| PW-029 | SUSPENDED       | VERIFIED   | Financial actions blocked        |
| PW-030 | SUSPENDED       | EXPIRED    | Suspension remains authoritative |
| PW-031 | DISABLED        | VERIFIED   | Access denied                    |
| PW-032 | DISABLED        | REJECTED   | Access denied                    |

---

# 10. Customer Status + Operation Pairwise Model

Operations:

```text
Login
View Account
Transfer
Payment
Card Management
Loan Application
Deposit Opening
Profile Update
```

Customer States:

```text
ACTIVE
RESTRICTED
SUSPENDED
DISABLED
```

Representative pairwise coverage:

| ID     | Customer State | Operation        |
| ------ | -------------- | ---------------- |
| PW-033 | ACTIVE         | Login            |
| PW-034 | ACTIVE         | Transfer         |
| PW-035 | ACTIVE         | Loan Application |
| PW-036 | RESTRICTED     | Payment          |
| PW-037 | RESTRICTED     | Deposit Opening  |
| PW-038 | RESTRICTED     | Profile Update   |
| PW-039 | SUSPENDED      | Transfer         |
| PW-040 | SUSPENDED      | Card Management  |
| PW-041 | SUSPENDED      | View Account     |
| PW-042 | DISABLED       | Login            |
| PW-043 | DISABLED       | Transfer         |
| PW-044 | DISABLED       | Profile Update   |

---

# 11. Account State + Transaction Type Pairwise Model

Account States:

```text
ACTIVE
FROZEN
RESTRICTED
SUSPENDED
CLOSED
```

Transaction Types:

```text
Transfer
Payment
Card Purchase
Loan Repayment
Deposit Funding
```

Representative matrix:

| ID     | Account State | Transaction Type |
| ------ | ------------- | ---------------- |
| PW-045 | ACTIVE        | Transfer         |
| PW-046 | ACTIVE        | Payment          |
| PW-047 | ACTIVE        | Card Purchase    |
| PW-048 | FROZEN        | Transfer         |
| PW-049 | FROZEN        | Loan Repayment   |
| PW-050 | FROZEN        | Deposit Funding  |
| PW-051 | RESTRICTED    | Payment          |
| PW-052 | RESTRICTED    | Card Purchase    |
| PW-053 | RESTRICTED    | Transfer         |
| PW-054 | SUSPENDED     | Transfer         |
| PW-055 | SUSPENDED     | Payment          |
| PW-056 | CLOSED        | Deposit Funding  |
| PW-057 | CLOSED        | Loan Repayment   |

High-risk rule:

```text
Any account state that should block a transaction must also be tested directly through API.
```

---

# 12. Transfer Pairwise Model

Parameters:

### Transfer Type

```text
Own Account
Internal Beneficiary
External Beneficiary
Scheduled
Recurring
```

### Source Account State

```text
ACTIVE
FROZEN
RESTRICTED
```

### Beneficiary State

```text
ACTIVE
PENDING_ACTIVATION
DISABLED
```

### Balance

```text
SUFFICIENT
EXACT
INSUFFICIENT
```

### Limit State

```text
WITHIN_LIMIT
AT_LIMIT
OVER_LIMIT
```

Representative scenarios:

| ID     | Type        | Account    | Beneficiary        | Balance      | Limit        |
| ------ | ----------- | ---------- | ------------------ | ------------ | ------------ |
| PW-058 | Internal    | ACTIVE     | ACTIVE             | SUFFICIENT   | WITHIN_LIMIT |
| PW-059 | External    | ACTIVE     | ACTIVE             | EXACT        | AT_LIMIT     |
| PW-060 | Scheduled   | ACTIVE     | PENDING_ACTIVATION | SUFFICIENT   | WITHIN_LIMIT |
| PW-061 | Recurring   | FROZEN     | ACTIVE             | SUFFICIENT   | WITHIN_LIMIT |
| PW-062 | Internal    | RESTRICTED | ACTIVE             | INSUFFICIENT | WITHIN_LIMIT |
| PW-063 | External    | ACTIVE     | DISABLED           | SUFFICIENT   | OVER_LIMIT   |
| PW-064 | Own Account | ACTIVE     | N/A                | EXACT        | WITHIN_LIMIT |
| PW-065 | Scheduled   | FROZEN     | ACTIVE             | INSUFFICIENT | AT_LIMIT     |
| PW-066 | Recurring   | ACTIVE     | ACTIVE             | SUFFICIENT   | OVER_LIMIT   |
| PW-067 | Internal    | ACTIVE     | PENDING_ACTIVATION | EXACT        | AT_LIMIT     |

These scenarios are pairwise representatives only.

Mandatory dedicated transfer tests must still separately cover:

* Atomicity
* Duplicate submission
* Concurrency
* Authorization
* Balance reconciliation

---

# 13. Transfer Channel + Device Pairwise Model

Channels:

```text
Desktop Web
Tablet Web
Mobile Web
API
```

Transfer Types:

```text
Immediate
Scheduled
Recurring
```

Representative:

| ID     | Channel     | Transfer Type |
| ------ | ----------- | ------------- |
| PW-068 | Desktop Web | Immediate     |
| PW-069 | Desktop Web | Scheduled     |
| PW-070 | Tablet Web  | Recurring     |
| PW-071 | Mobile Web  | Immediate     |
| PW-072 | Mobile Web  | Scheduled     |
| PW-073 | API         | Recurring     |
| PW-074 | API         | Immediate     |

---

# 14. Payment Pairwise Model

Parameters:

### Payment Type

```text
Bill Payment
Merchant Payment
Scheduled Payment
Recurring Payment
```

### Account State

```text
ACTIVE
FROZEN
RESTRICTED
```

### Bill State

```text
UNPAID
PAID
EXPIRED
```

### Balance

```text
SUFFICIENT
INSUFFICIENT
```

Representative:

| ID     | Payment Type | Account    | Bill    | Balance      |
| ------ | ------------ | ---------- | ------- | ------------ |
| PW-075 | Bill Payment | ACTIVE     | UNPAID  | SUFFICIENT   |
| PW-076 | Merchant     | ACTIVE     | PAID    | SUFFICIENT   |
| PW-077 | Scheduled    | ACTIVE     | EXPIRED | INSUFFICIENT |
| PW-078 | Recurring    | FROZEN     | UNPAID  | SUFFICIENT   |
| PW-079 | Bill Payment | RESTRICTED | PAID    | INSUFFICIENT |
| PW-080 | Scheduled    | FROZEN     | EXPIRED | SUFFICIENT   |
| PW-081 | Recurring    | ACTIVE     | UNPAID  | INSUFFICIENT |

---

# 15. Card State + Operation Pairwise Model

Card States:

```text
INACTIVE
ACTIVE
FROZEN
BLOCKED
EXPIRED
CANCELLED
```

Operations:

```text
Activate
Freeze
Unfreeze
Purchase
Change Limit
Replace
```

Representative:

| ID     | Card State | Operation    |
| ------ | ---------- | ------------ |
| PW-082 | INACTIVE   | Activate     |
| PW-083 | INACTIVE   | Purchase     |
| PW-084 | ACTIVE     | Freeze       |
| PW-085 | ACTIVE     | Purchase     |
| PW-086 | ACTIVE     | Change Limit |
| PW-087 | FROZEN     | Unfreeze     |
| PW-088 | FROZEN     | Purchase     |
| PW-089 | FROZEN     | Replace      |
| PW-090 | BLOCKED    | Purchase     |
| PW-091 | BLOCKED    | Replace      |
| PW-092 | EXPIRED    | Replace      |
| PW-093 | EXPIRED    | Purchase     |
| PW-094 | CANCELLED  | Activate     |
| PW-095 | CANCELLED  | Replace      |

---

# 16. Card + Account State Pairwise Model

Card States:

```text
ACTIVE
FROZEN
BLOCKED
```

Account States:

```text
ACTIVE
FROZEN
RESTRICTED
CLOSED
```

Representative:

| ID     | Card State | Account State | Expected Focus         |
| ------ | ---------- | ------------- | ---------------------- |
| PW-096 | ACTIVE     | ACTIVE        | Normal transaction     |
| PW-097 | ACTIVE     | FROZEN        | Account restriction    |
| PW-098 | ACTIVE     | CLOSED        | Linked-account invalid |
| PW-099 | FROZEN     | ACTIVE        | Card freeze            |
| PW-100 | FROZEN     | RESTRICTED    | Multiple restrictions  |
| PW-101 | BLOCKED    | ACTIVE        | Block authoritative    |
| PW-102 | BLOCKED    | CLOSED        | Both invalid           |

---

# 17. Loan Pairwise Model

Parameters:

### Customer Status

```text
ACTIVE
RESTRICTED
SUSPENDED
```

### KYC

```text
VERIFIED
PENDING
EXPIRED
```

### Amount

```text
VALID
BELOW_MIN
ABOVE_MAX
```

### Income

```text
ELIGIBLE
INELIGIBLE
```

### Existing Debt

```text
ACCEPTABLE
EXCESSIVE
```

Representative:

| ID     | Customer   | KYC      | Amount    | Income     | Debt       |
| ------ | ---------- | -------- | --------- | ---------- | ---------- |
| PW-103 | ACTIVE     | VERIFIED | VALID     | ELIGIBLE   | ACCEPTABLE |
| PW-104 | ACTIVE     | PENDING  | BELOW_MIN | ELIGIBLE   | EXCESSIVE  |
| PW-105 | ACTIVE     | EXPIRED  | ABOVE_MAX | INELIGIBLE | ACCEPTABLE |
| PW-106 | RESTRICTED | VERIFIED | ABOVE_MAX | ELIGIBLE   | ACCEPTABLE |
| PW-107 | RESTRICTED | PENDING  | VALID     | INELIGIBLE | ACCEPTABLE |
| PW-108 | SUSPENDED  | VERIFIED | BELOW_MIN | INELIGIBLE | EXCESSIVE  |
| PW-109 | SUSPENDED  | EXPIRED  | VALID     | ELIGIBLE   | EXCESSIVE  |

Dedicated loan tests still separately cover:

* Interest calculation
* Installment calculation
* Disbursement idempotency
* Repayment reconciliation
* State transitions

---

# 18. Loan State + Admin Role Pairwise Model

Loan States:

```text
SUBMITTED
UNDER_REVIEW
APPROVED
REJECTED
ACTIVE
CLOSED
```

Admin Roles:

```text
LOAN_OFFICER
OPERATIONS_ADMIN
READ_ONLY_ADMIN
AUDITOR
```

Representative:

| ID     | Loan State   | Admin Role       | Operation            |
| ------ | ------------ | ---------------- | -------------------- |
| PW-110 | SUBMITTED    | LOAN_OFFICER     | Review               |
| PW-111 | UNDER_REVIEW | LOAN_OFFICER     | Approve              |
| PW-112 | APPROVED     | OPERATIONS_ADMIN | Disburse             |
| PW-113 | REJECTED     | READ_ONLY_ADMIN  | View                 |
| PW-114 | ACTIVE       | AUDITOR          | View                 |
| PW-115 | CLOSED       | LOAN_OFFICER     | Attempt modification |
| PW-116 | UNDER_REVIEW | READ_ONLY_ADMIN  | Attempt approval     |

---

# 19. Deposit Pairwise Model

Parameters:

### Deposit State

```text
PENDING
ACTIVE
MATURED
CLOSED
```

### Funding Account

```text
ACTIVE
FROZEN
INSUFFICIENT_BALANCE
```

### Principal

```text
VALID
BELOW_MIN
ABOVE_MAX
```

### Auto-Renew

```text
ENABLED
DISABLED
```

Representative:

| ID     | Deposit State | Funding Account      | Principal | Auto-Renew |
| ------ | ------------- | -------------------- | --------- | ---------- |
| PW-117 | PENDING       | ACTIVE               | VALID     | ENABLED    |
| PW-118 | PENDING       | FROZEN               | BELOW_MIN | DISABLED   |
| PW-119 | ACTIVE        | ACTIVE               | ABOVE_MAX | DISABLED   |
| PW-120 | ACTIVE        | INSUFFICIENT_BALANCE | VALID     | ENABLED    |
| PW-121 | MATURED       | ACTIVE               | VALID     | ENABLED    |
| PW-122 | MATURED       | FROZEN               | ABOVE_MAX | DISABLED   |
| PW-123 | CLOSED        | ACTIVE               | VALID     | DISABLED   |

---

# 20. Deposit Maturity Pairwise Model

Parameters:

```text
Maturity Reached:
YES / NO

Auto Renew:
YES / NO

Settlement Account:
VALID / CLOSED

Payout Processor:
AVAILABLE / UNAVAILABLE
```

Representative:

| ID     | Maturity | Auto Renew | Settlement Account | Processor   |
| ------ | -------- | ---------- | ------------------ | ----------- |
| PW-124 | YES      | NO         | VALID              | AVAILABLE   |
| PW-125 | YES      | YES        | VALID              | AVAILABLE   |
| PW-126 | YES      | NO         | CLOSED             | AVAILABLE   |
| PW-127 | NO       | YES        | VALID              | AVAILABLE   |
| PW-128 | YES      | NO         | VALID              | UNAVAILABLE |
| PW-129 | NO       | NO         | CLOSED             | UNAVAILABLE |

---

# 21. Authentication Pairwise Model

Parameters:

### Username

```text
VALID
INVALID
EMPTY
```

### Password

```text
CORRECT
INCORRECT
EMPTY
```

### Account State

```text
ACTIVE
LOCKED
DISABLED
```

### MFA

```text
ENABLED
DISABLED
```

Representative:

| ID     | Username | Password  | Account State | MFA      |
| ------ | -------- | --------- | ------------- | -------- |
| PW-130 | VALID    | CORRECT   | ACTIVE        | DISABLED |
| PW-131 | VALID    | CORRECT   | ACTIVE        | ENABLED  |
| PW-132 | VALID    | INCORRECT | LOCKED        | ENABLED  |
| PW-133 | VALID    | EMPTY     | DISABLED      | DISABLED |
| PW-134 | INVALID  | CORRECT   | ACTIVE        | ENABLED  |
| PW-135 | INVALID  | INCORRECT | DISABLED      | DISABLED |
| PW-136 | EMPTY    | EMPTY     | ACTIVE        | ENABLED  |

---

# 22. MFA Pairwise Model

Parameters:

```text
Code:
CORRECT
INCORRECT
MALFORMED

Expiration:
VALID
EXPIRED

Reuse:
UNUSED
USED

Attempt State:
BELOW_LIMIT
AT_LIMIT
```

Representative:

| ID     | Code      | Expiration | Reuse  | Attempts    |
| ------ | --------- | ---------- | ------ | ----------- |
| PW-137 | CORRECT   | VALID      | UNUSED | BELOW_LIMIT |
| PW-138 | CORRECT   | EXPIRED    | UNUSED | BELOW_LIMIT |
| PW-139 | INCORRECT | VALID      | UNUSED | AT_LIMIT    |
| PW-140 | CORRECT   | VALID      | USED   | AT_LIMIT    |
| PW-141 | MALFORMED | EXPIRED    | USED   | BELOW_LIMIT |
| PW-142 | INCORRECT | EXPIRED    | USED   | AT_LIMIT    |

---

# 23. Session Pairwise Model

Session States:

```text
VALID
EXPIRED
REVOKED
LOGGED_OUT
```

Operations:

```text
View Account
Transfer
Change Password
Download Statement
Admin Action
```

Representative:

| ID     | Session    | Operation          |
| ------ | ---------- | ------------------ |
| PW-143 | VALID      | View Account       |
| PW-144 | VALID      | Transfer           |
| PW-145 | EXPIRED    | Change Password    |
| PW-146 | EXPIRED    | Download Statement |
| PW-147 | REVOKED    | Transfer           |
| PW-148 | REVOKED    | View Account       |
| PW-149 | LOGGED_OUT | Download Statement |
| PW-150 | LOGGED_OUT | Change Password    |

---

# 24. Notification Pairwise Model

Parameters:

### Notification Category

```text
TRANSFER
PAYMENT
SECURITY
CARD
LOAN
DEPOSIT
```

### Preference

```text
ENABLED
DISABLED
```

### Channel

```text
IN_APP
EMAIL
SMS
```

### Event Outcome

```text
SUCCESS
FAILURE
```

Representative:

| ID     | Category | Preference | Channel | Outcome |
| ------ | -------- | ---------- | ------- | ------- |
| PW-151 | TRANSFER | ENABLED    | IN_APP  | SUCCESS |
| PW-152 | TRANSFER | DISABLED   | EMAIL   | FAILURE |
| PW-153 | PAYMENT  | ENABLED    | SMS     | FAILURE |
| PW-154 | SECURITY | DISABLED   | EMAIL   | SUCCESS |
| PW-155 | CARD     | ENABLED    | IN_APP  | FAILURE |
| PW-156 | LOAN     | DISABLED   | SMS     | SUCCESS |
| PW-157 | DEPOSIT  | ENABLED    | EMAIL   | SUCCESS |

Mandatory security alerts should receive dedicated tests beyond pairwise selection.

---

# 25. Profile Settings Pairwise Model

Parameters:

```text
Setting:
EMAIL
PHONE
PASSWORD
MFA
NOTIFICATION
LANGUAGE

Session:
VALID
EXPIRED

Verification:
REQUIRED_PASSED
REQUIRED_FAILED
NOT_REQUIRED
```

Representative:

| ID     | Setting      | Session | Verification    |
| ------ | ------------ | ------- | --------------- |
| PW-158 | EMAIL        | VALID   | REQUIRED_PASSED |
| PW-159 | PHONE        | VALID   | REQUIRED_FAILED |
| PW-160 | PASSWORD     | EXPIRED | REQUIRED_PASSED |
| PW-161 | MFA          | VALID   | REQUIRED_PASSED |
| PW-162 | NOTIFICATION | EXPIRED | NOT_REQUIRED    |
| PW-163 | LANGUAGE     | VALID   | NOT_REQUIRED    |

---

# 26. Admin Role + Operation Pairwise Model

Roles:

```text
SUPER_ADMIN
OPERATIONS_ADMIN
KYC_REVIEWER
LOAN_OFFICER
AUDITOR
READ_ONLY_ADMIN
```

Operations:

```text
Freeze Account
Approve KYC
Approve Loan
Reverse Transaction
View Audit
Change Limit
```

Representative:

| ID     | Role             | Operation           |
| ------ | ---------------- | ------------------- |
| PW-164 | SUPER_ADMIN      | Change Limit        |
| PW-165 | OPERATIONS_ADMIN | Freeze Account      |
| PW-166 | KYC_REVIEWER     | Approve KYC         |
| PW-167 | LOAN_OFFICER     | Approve Loan        |
| PW-168 | AUDITOR          | View Audit          |
| PW-169 | READ_ONLY_ADMIN  | Reverse Transaction |
| PW-170 | KYC_REVIEWER     | Freeze Account      |
| PW-171 | LOAN_OFFICER     | View Audit          |
| PW-172 | OPERATIONS_ADMIN | Reverse Transaction |

Expected:

Each role is restricted to defined permissions.

---

# 27. Admin State + Session Pairwise Model

Parameters:

```text
Admin Status:
ACTIVE
LOCKED
DISABLED

Session:
VALID
EXPIRED
REVOKED

Operation:
READ
WRITE
FINANCIAL
```

Representative:

| ID     | Admin Status | Session | Operation |
| ------ | ------------ | ------- | --------- |
| PW-173 | ACTIVE       | VALID   | READ      |
| PW-174 | ACTIVE       | VALID   | FINANCIAL |
| PW-175 | ACTIVE       | EXPIRED | WRITE     |
| PW-176 | LOCKED       | VALID   | READ      |
| PW-177 | LOCKED       | REVOKED | FINANCIAL |
| PW-178 | DISABLED     | VALID   | WRITE     |
| PW-179 | DISABLED     | EXPIRED | READ      |

---

# 28. API Authentication Pairwise Model

Parameters:

```text
Credential:
VALID
MISSING
INVALID
EXPIRED
REVOKED

Role:
CUSTOMER
ADMIN
LIMITED_ADMIN

Endpoint:
CUSTOMER
ADMIN
FINANCIAL
```

Representative:

| ID     | Credential | Role          | Endpoint  |
| ------ | ---------- | ------------- | --------- |
| PW-180 | VALID      | CUSTOMER      | CUSTOMER  |
| PW-181 | VALID      | CUSTOMER      | FINANCIAL |
| PW-182 | VALID      | CUSTOMER      | ADMIN     |
| PW-183 | VALID      | ADMIN         | ADMIN     |
| PW-184 | VALID      | LIMITED_ADMIN | ADMIN     |
| PW-185 | MISSING    | CUSTOMER      | CUSTOMER  |
| PW-186 | INVALID    | ADMIN         | FINANCIAL |
| PW-187 | EXPIRED    | CUSTOMER      | FINANCIAL |
| PW-188 | REVOKED    | ADMIN         | ADMIN     |

---

# 29. API Payload Pairwise Model

Parameters:

```text
Required Fields:
PRESENT
MISSING

Type:
VALID
INVALID

Protected Fields:
ABSENT
PRESENT

Amount:
VALID
NEGATIVE
OVER_LIMIT
```

Representative:

| ID     | Required Fields | Type    | Protected Fields | Amount     |
| ------ | --------------- | ------- | ---------------- | ---------- |
| PW-189 | PRESENT         | VALID   | ABSENT           | VALID      |
| PW-190 | PRESENT         | INVALID | PRESENT          | NEGATIVE   |
| PW-191 | MISSING         | VALID   | ABSENT           | OVER_LIMIT |
| PW-192 | MISSING         | INVALID | PRESENT          | VALID      |
| PW-193 | PRESENT         | VALID   | PRESENT          | OVER_LIMIT |
| PW-194 | MISSING         | VALID   | PRESENT          | NEGATIVE   |

---

# 30. HTTP Method + Endpoint Pairwise Model

Methods:

```text
GET
POST
PUT
PATCH
DELETE
```

Endpoints:

```text
Accounts
Transfers
Beneficiaries
Cards
Loans
Admin
```

Representative:

| ID     | Method | Endpoint      |
| ------ | ------ | ------------- |
| PW-195 | GET    | Accounts      |
| PW-196 | POST   | Transfers     |
| PW-197 | PUT    | Beneficiaries |
| PW-198 | PATCH  | Cards         |
| PW-199 | DELETE | Beneficiaries |
| PW-200 | GET    | Loans         |
| PW-201 | POST   | Admin         |
| PW-202 | DELETE | Cards         |
| PW-203 | PATCH  | Accounts      |

Unsupported method/endpoint combinations should be rejected safely.

---

# 31. Statement Pairwise Model

Parameters:

```text
Account:
OWNED
OTHER_CUSTOMER

Date Range:
VALID
INVALID
OVER_MAXIMUM

Transactions:
NONE
FEW
MANY

Format:
UI
PDF
```

Representative:

| ID     | Account        | Date Range   | Transactions | Format |
| ------ | -------------- | ------------ | ------------ | ------ |
| PW-204 | OWNED          | VALID        | NONE         | UI     |
| PW-205 | OWNED          | VALID        | MANY         | PDF    |
| PW-206 | OWNED          | OVER_MAXIMUM | FEW          | UI     |
| PW-207 | OTHER_CUSTOMER | VALID        | MANY         | PDF    |
| PW-208 | OTHER_CUSTOMER | INVALID      | NONE         | UI     |
| PW-209 | OWNED          | INVALID      | FEW          | PDF    |

---

# 32. Transaction History Pairwise Model

Parameters:

```text
Transaction Type:
TRANSFER
PAYMENT
CARD
LOAN
DEPOSIT

Status:
PENDING
COMPLETED
FAILED
REVERSED

Dataset:
SMALL
LARGE
```

Representative:

| ID     | Type     | Status    | Dataset |
| ------ | -------- | --------- | ------- |
| PW-210 | TRANSFER | COMPLETED | SMALL   |
| PW-211 | PAYMENT  | FAILED    | LARGE   |
| PW-212 | CARD     | PENDING   | SMALL   |
| PW-213 | LOAN     | REVERSED  | LARGE   |
| PW-214 | DEPOSIT  | COMPLETED | LARGE   |
| PW-215 | TRANSFER | PENDING   | LARGE   |
| PW-216 | CARD     | FAILED    | SMALL   |

---

# 33. Search + Filter Pairwise Model

Parameters:

```text
Search:
EMPTY
EXACT_MATCH
PARTIAL
NO_MATCH

Filter:
NONE
STATUS
DATE
TYPE

Dataset:
EMPTY
SMALL
LARGE
```

Representative:

| ID     | Search      | Filter | Dataset |
| ------ | ----------- | ------ | ------- |
| PW-217 | EMPTY       | NONE   | EMPTY   |
| PW-218 | EXACT_MATCH | STATUS | SMALL   |
| PW-219 | PARTIAL     | DATE   | LARGE   |
| PW-220 | NO_MATCH    | TYPE   | SMALL   |
| PW-221 | EMPTY       | DATE   | LARGE   |
| PW-222 | EXACT_MATCH | TYPE   | LARGE   |
| PW-223 | PARTIAL     | NONE   | SMALL   |

---

# 34. Pagination Pairwise Model

Parameters:

```text
Page Size:
10
25
50

Dataset:
0
1
EXACT_PAGE
PAGE_PLUS_ONE
LARGE

Sort:
ASC
DESC
```

Representative:

| ID     | Page Size | Dataset       | Sort |
| ------ | --------: | ------------- | ---- |
| PW-224 |        10 | 0             | ASC  |
| PW-225 |        10 | PAGE_PLUS_ONE | DESC |
| PW-226 |        25 | EXACT_PAGE    | ASC  |
| PW-227 |        25 | LARGE         | DESC |
| PW-228 |        50 | 1             | DESC |
| PW-229 |        50 | LARGE         | ASC  |

---

# 35. File Upload Pairwise Model

Where applicable.

Parameters:

```text
Type:
VALID
INVALID

Size:
BELOW_LIMIT
AT_LIMIT
ABOVE_LIMIT

Content:
VALID
MALFORMED

Authorization:
AUTHORIZED
UNAUTHORIZED
```

Representative:

| ID     | Type    | Size        | Content   | Authorization |
| ------ | ------- | ----------- | --------- | ------------- |
| PW-230 | VALID   | BELOW_LIMIT | VALID     | AUTHORIZED    |
| PW-231 | VALID   | AT_LIMIT    | MALFORMED | AUTHORIZED    |
| PW-232 | VALID   | ABOVE_LIMIT | VALID     | UNAUTHORIZED  |
| PW-233 | INVALID | BELOW_LIMIT | VALID     | AUTHORIZED    |
| PW-234 | INVALID | AT_LIMIT    | MALFORMED | UNAUTHORIZED  |
| PW-235 | VALID   | BELOW_LIMIT | MALFORMED | UNAUTHORIZED  |

---

# 36. Notification Channel + Contact State

Parameters:

```text
Channel:
EMAIL
SMS
IN_APP

Contact:
VERIFIED
UNVERIFIED
MISSING

Event:
MANDATORY
OPTIONAL
```

Representative:

| ID     | Channel | Contact    | Event     |
| ------ | ------- | ---------- | --------- |
| PW-236 | EMAIL   | VERIFIED   | OPTIONAL  |
| PW-237 | EMAIL   | UNVERIFIED | MANDATORY |
| PW-238 | SMS     | VERIFIED   | MANDATORY |
| PW-239 | SMS     | MISSING    | OPTIONAL  |
| PW-240 | IN_APP  | VERIFIED   | MANDATORY |
| PW-241 | IN_APP  | MISSING    | OPTIONAL  |

---

# 37. Transaction Retry Pairwise Model

Parameters:

```text
Previous Outcome:
SUCCESS
FAILURE
TIMEOUT_UNKNOWN

Same Idempotency Key:
YES
NO

Payload:
SAME
DIFFERENT
```

Representative:

| ID     | Previous Outcome | Same Key | Payload   |
| ------ | ---------------- | -------- | --------- |
| PW-242 | SUCCESS          | YES      | SAME      |
| PW-243 | SUCCESS          | YES      | DIFFERENT |
| PW-244 | FAILURE          | YES      | SAME      |
| PW-245 | TIMEOUT_UNKNOWN  | YES      | SAME      |
| PW-246 | TIMEOUT_UNKNOWN  | NO       | DIFFERENT |
| PW-247 | FAILURE          | NO       | DIFFERENT |

**Priority:** P0

---

# 38. Concurrency Pairwise Model

Parameters:

```text
Operation A:
TRANSFER
PAYMENT
LOAN_REPAYMENT
DEPOSIT_OPEN

Operation B:
TRANSFER
PAYMENT
CARD_PURCHASE
ACCOUNT_FREEZE

Balance State:
ENOUGH_FOR_BOTH
ENOUGH_FOR_ONE
ENOUGH_FOR_NONE
```

Representative:

| ID     | Operation A    | Operation B    | Balance State   |
| ------ | -------------- | -------------- | --------------- |
| PW-248 | TRANSFER       | PAYMENT        | ENOUGH_FOR_BOTH |
| PW-249 | TRANSFER       | CARD_PURCHASE  | ENOUGH_FOR_ONE  |
| PW-250 | PAYMENT        | TRANSFER       | ENOUGH_FOR_NONE |
| PW-251 | LOAN_REPAYMENT | ACCOUNT_FREEZE | ENOUGH_FOR_BOTH |
| PW-252 | DEPOSIT_OPEN   | PAYMENT        | ENOUGH_FOR_ONE  |
| PW-253 | LOAN_REPAYMENT | CARD_PURCHASE  | ENOUGH_FOR_NONE |

Pairwise coverage here supplements dedicated race-condition testing.

---

# 39. Timezone + Scheduling Pairwise Model

Parameters:

```text
Operation:
SCHEDULED_TRANSFER
RECURRING_PAYMENT
LOAN_INSTALLMENT
DEPOSIT_MATURITY

Date Type:
NORMAL_DAY
MONTH_END
YEAR_END
LEAP_DAY

Timezone:
UTC
LOCAL
OTHER_SUPPORTED
```

Representative:

| ID     | Operation          | Date Type  | Timezone        |
| ------ | ------------------ | ---------- | --------------- |
| PW-254 | SCHEDULED_TRANSFER | NORMAL_DAY | LOCAL           |
| PW-255 | SCHEDULED_TRANSFER | YEAR_END   | UTC             |
| PW-256 | RECURRING_PAYMENT  | MONTH_END  | OTHER_SUPPORTED |
| PW-257 | LOAN_INSTALLMENT   | LEAP_DAY   | LOCAL           |
| PW-258 | DEPOSIT_MATURITY   | YEAR_END   | OTHER_SUPPORTED |
| PW-259 | DEPOSIT_MATURITY   | MONTH_END  | UTC             |

---

# 40. Language + Browser Pairwise Model

Where localization exists.

Languages:

```text
English
Arabic
Other Supported Language
```

Browsers:

```text
Chrome
Edge
Firefox
```

Representative:

| ID     | Language | Browser |
| ------ | -------- | ------- |
| PW-260 | English  | Chrome  |
| PW-261 | English  | Firefox |
| PW-262 | Arabic   | Edge    |
| PW-263 | Arabic   | Chrome  |
| PW-264 | Other    | Firefox |
| PW-265 | Other    | Edge    |

Validate:

* RTL behavior
* Text overflow
* Financial values
* Dates
* Currency
* Form validation

---

# 41. Currency + Transaction Type Pairwise Model

Where multiple currencies are supported.

Currencies:

```text
BASE
SUPPORTED_FOREIGN
UNSUPPORTED
```

Transaction Types:

```text
TRANSFER
PAYMENT
CARD
LOAN
DEPOSIT
```

Representative:

| ID     | Currency          | Transaction Type |
| ------ | ----------------- | ---------------- |
| PW-266 | BASE              | TRANSFER         |
| PW-267 | BASE              | LOAN             |
| PW-268 | SUPPORTED_FOREIGN | PAYMENT          |
| PW-269 | SUPPORTED_FOREIGN | CARD             |
| PW-270 | UNSUPPORTED       | TRANSFER         |
| PW-271 | UNSUPPORTED       | DEPOSIT          |

---

# 42. Audit Pairwise Model

Parameters:

```text
Actor:
CUSTOMER
ADMIN
SYSTEM

Action:
SECURITY
FINANCIAL
STATE_CHANGE
READ_ONLY

Outcome:
SUCCESS
FAILURE
```

Representative:

| ID     | Actor    | Action       | Outcome |
| ------ | -------- | ------------ | ------- |
| PW-272 | CUSTOMER | SECURITY     | SUCCESS |
| PW-273 | CUSTOMER | FINANCIAL    | FAILURE |
| PW-274 | ADMIN    | STATE_CHANGE | SUCCESS |
| PW-275 | ADMIN    | FINANCIAL    | FAILURE |
| PW-276 | SYSTEM   | FINANCIAL    | SUCCESS |
| PW-277 | SYSTEM   | STATE_CHANGE | FAILURE |
| PW-278 | ADMIN    | READ_ONLY    | SUCCESS |

---

# 43. Security Pairwise Model

Parameters:

```text
Authentication:
VALID
INVALID
EXPIRED

Authorization:
AUTHORIZED
UNAUTHORIZED

Resource:
OWNED
OTHER_CUSTOMER

Operation:
READ
WRITE
FINANCIAL
```

Representative:

| ID     | Authentication | Authorization | Resource       | Operation |
| ------ | -------------- | ------------- | -------------- | --------- |
| PW-279 | VALID          | AUTHORIZED    | OWNED          | READ      |
| PW-280 | VALID          | AUTHORIZED    | OWNED          | FINANCIAL |
| PW-281 | VALID          | UNAUTHORIZED  | OTHER_CUSTOMER | READ      |
| PW-282 | VALID          | UNAUTHORIZED  | OTHER_CUSTOMER | WRITE     |
| PW-283 | INVALID        | UNAUTHORIZED  | OWNED          | FINANCIAL |
| PW-284 | EXPIRED        | AUTHORIZED    | OWNED          | WRITE     |
| PW-285 | EXPIRED        | UNAUTHORIZED  | OTHER_CUSTOMER | FINANCIAL |

Dedicated IDOR/authentication tests remain mandatory.

---

# 44. Error Handling Pairwise Model

Parameters:

```text
Failure:
NETWORK
API_4XX
API_5XX
DATABASE
DEPENDENCY_TIMEOUT

Operation:
READ
FINANCIAL_WRITE
PROFILE_WRITE
ADMIN_WRITE

Retry:
YES
NO
```

Representative:

| ID     | Failure            | Operation       | Retry |
| ------ | ------------------ | --------------- | ----- |
| PW-286 | NETWORK            | READ            | YES   |
| PW-287 | NETWORK            | FINANCIAL_WRITE | YES   |
| PW-288 | API_4XX            | PROFILE_WRITE   | NO    |
| PW-289 | API_5XX            | ADMIN_WRITE     | YES   |
| PW-290 | DATABASE           | FINANCIAL_WRITE | NO    |
| PW-291 | DEPENDENCY_TIMEOUT | FINANCIAL_WRITE | YES   |
| PW-292 | API_5XX            | READ            | NO    |

Financial retry cases must additionally validate idempotency.

---

# 45. Test Data Volume + Module Pairwise Model

Data Volumes:

```text
EMPTY
SMALL
MEDIUM
LARGE
```

Modules:

```text
Transactions
Notifications
Customers
Audit Logs
Loans
```

Representative:

| ID     | Volume | Module        |
| ------ | ------ | ------------- |
| PW-293 | EMPTY  | Transactions  |
| PW-294 | SMALL  | Notifications |
| PW-295 | MEDIUM | Customers     |
| PW-296 | LARGE  | Audit Logs    |
| PW-297 | LARGE  | Transactions  |
| PW-298 | EMPTY  | Loans         |
| PW-299 | MEDIUM | Notifications |

---

# 46. Pairwise Testing for UI Automation

Pairwise combinations are useful for UI automation when exhaustive cross-browser execution would be expensive.

Example:

```text
Chrome + Desktop + Transfer
Edge + Mobile + Payment
Firefox + Tablet + Card
WebKit + Mobile + Statement
```

Critical banking flows can then receive full coverage independently.

Good automation candidates:

* Browser × viewport
* Browser × module
* Customer state × operation
* Account state × transaction
* Card state × operation
* Role × admin function

---

# 47. Pairwise Testing for API Automation

API parameters often have many interacting classes.

Example transfer API dimensions:

```text
Authentication
Source ownership
Account state
Beneficiary state
Amount class
Limit state
Balance state
Idempotency state
```

Exhaustive combinations can become very large.

Pairwise-generated datasets can be executed in:

* Postman
* REST Assured
* Jest where applicable

Critical security and financial cases must still be added manually.

---

# 48. Pairwise Testing for Database Validation

For pairwise scenarios that cause a successful financial operation, validate:

```text
Correct transaction record
Correct account relationship
Correct amount
Correct state
Correct reference
Correct balance change
```

For rejected scenarios:

```text
No unintended financial record
No balance modification
No orphan record
No invalid lifecycle state
```

---

# 49. Pairwise Testing With Boundary Value Analysis

Pairwise testing chooses combinations.

Boundary Value Analysis chooses precise values.

Example pairwise combination:

```text
Account = ACTIVE
Beneficiary = ACTIVE
Balance = SUFFICIENT
Limit = AT_LIMIT
```

Then BVA may provide:

```text
Transfer = 99,999.99
Transfer = 100,000.00
Transfer = 100,000.01
```

Combining techniques significantly improves coverage.

---

# 50. Pairwise Testing With Equivalence Partitioning

Equivalence Partitioning defines representative classes.

Example:

```text
Balance:
Sufficient
Exact
Insufficient

Account:
Active
Frozen
Restricted
```

Pairwise testing chooses combinations of those classes.

Therefore:

```text
EP defines the values/classes.

Pairwise defines which classes should interact in selected tests.
```

---

# 51. Pairwise Testing With Decision Tables

Decision tables should remain the primary technique for strict business-rule outcomes.

Pairwise is better suited for reducing environmental/configuration combinations.

Example:

Decision table:

```text
Can the transfer proceed?
```

Pairwise:

```text
Which browser/account state/beneficiary state/balance combination should be exercised?
```

---

# 52. Pairwise Testing With State Transition Testing

State Transition Testing determines valid transitions.

Pairwise can vary environmental factors around those transitions.

Example:

```text
Card ACTIVE → FROZEN
```

Pairwise dimensions:

```text
Browser
Device
Customer type
Concurrent transaction state
```

---

# 53. Constraints in Pairwise Generation

Not every generated combination is logically possible.

Examples:

```text
Own-account transfer + external beneficiary
```

may be invalid structurally.

Or:

```text
CLOSED account + ACTIVE card purchase success
```

may be impossible.

Pairwise generation should therefore include constraints.

Example constraints:

```text
IF TransferType = OWN_ACCOUNT
THEN BeneficiaryState = N/A

IF UserRole = CUSTOMER
THEN AdminOperation != AUTHORIZED

IF AccountState = CLOSED
THEN ExpectedFinancialSuccess = FALSE
```

---

# 54. Risk Overrides

Pairwise reduction must never remove a scenario identified as critical by risk analysis.

Mandatory overrides include:

```text
Unauthorized financial access
Another customer's resource
Transfer exceeding available balance
Duplicate transfer
Concurrent overspending
Failed transaction changing balance
Frozen account transaction
Blocked card transaction
Duplicate loan disbursement
Duplicate deposit payout
Expired OTP
Reused reset token
Privilege escalation
```

These should always exist as explicit dedicated tests.

---

# 55. Example Pairwise Dataset Format

A reusable CSV could look like:

```text
id,browser,viewport,accountState,beneficiaryState,balanceState,limitState,expected
PW-001,Chrome,Desktop,ACTIVE,ACTIVE,SUFFICIENT,WITHIN_LIMIT,SUCCESS
PW-002,Edge,Mobile,FROZEN,ACTIVE,SUFFICIENT,WITHIN_LIMIT,REJECT
PW-003,Firefox,Tablet,ACTIVE,DISABLED,EXACT,AT_LIMIT,REJECT
PW-004,WebKit,Mobile,RESTRICTED,ACTIVE,INSUFFICIENT,OVER_LIMIT,REJECT
```

This can later be consumed by automation frameworks.

---

# 56. Example JSON Dataset

```json
[
  {
    "id": "PW-058",
    "transferType": "INTERNAL",
    "accountState": "ACTIVE",
    "beneficiaryState": "ACTIVE",
    "balanceState": "SUFFICIENT",
    "limitState": "WITHIN_LIMIT",
    "expected": "SUCCESS"
  },
  {
    "id": "PW-061",
    "transferType": "RECURRING",
    "accountState": "FROZEN",
    "beneficiaryState": "ACTIVE",
    "balanceState": "SUFFICIENT",
    "limitState": "WITHIN_LIMIT",
    "expected": "REJECT"
  }
]
```

---

# 57. Pairwise Execution Strategy

Recommended execution order:

```text
1. P0 risk-based dedicated cases
2. Smoke tests
3. Pairwise combinations
4. Boundary tests
5. Negative/security cases
6. Full regression where required
```

Pairwise tests should not replace critical smoke or financial integrity tests.

---

# 58. Pairwise Regression Levels

## Level 1 — Smoke Pairwise

Use a minimal set covering:

```text
Major browsers
Major account states
Major customer states
Primary transaction types
```

---

## Level 2 — Standard Regression Pairwise

Add:

```text
Devices
KYC states
Limits
Balances
Notification channels
Roles
```

---

## Level 3 — Extended Pairwise

Add:

```text
Concurrency states
Error conditions
Timezones
Languages
Data volume
API payload classes
```

---

# 59. Pairwise Automation Candidates

Strong candidates:

* Cross-browser testing
* Responsive testing
* Transfer condition combinations
* Card state × operation
* Loan eligibility classes
* Deposit states
* Authentication combinations
* MFA combinations
* Role × admin action
* API payload combinations
* Search/filter combinations

---

# 60. Performance Testing Use

Pairwise selection can help define JMeter traffic mixes.

Parameters:

```text
Transaction Type
Customer State
Account Balance
Request Outcome
Concurrency Level
```

Example mix:

```text
Valid transfer + sufficient funds
Payment + insufficient funds
Card purchase + limit exceeded
Loan repayment + valid balance
Deposit creation + insufficient balance
```

This helps performance testing include realistic functional diversity.

---

# 61. BDD Example

```gherkin
Feature: Pairwise transfer coverage

Scenario Outline: Representative transfer combinations
  Given the source account state is <accountState>
  And the beneficiary state is <beneficiaryState>
  And the balance state is <balanceState>
  And the transfer limit state is <limitState>
  When the customer submits the transfer
  Then the result should be <expected>

Examples:
  | accountState | beneficiaryState  | balanceState | limitState   | expected |
  | ACTIVE       | ACTIVE            | SUFFICIENT   | WITHIN_LIMIT | success  |
  | FROZEN       | ACTIVE            | SUFFICIENT   | WITHIN_LIMIT | rejected |
  | ACTIVE       | DISABLED          | EXACT        | AT_LIMIT     | rejected |
  | RESTRICTED   | ACTIVE            | INSUFFICIENT | OVER_LIMIT   | rejected |
```

---

# 62. Pairwise Review Checklist

Before accepting a pairwise test set, verify:

```text
Are all parameter values represented?

Does every pair appear at least once?

Are impossible combinations removed?

Are critical financial scenarios added manually?

Are authorization cases included?

Are negative states represented?

Are boundary values still tested separately?

Are concurrency risks separately covered?

Are generated expected outcomes reviewed manually?

Can the dataset be reused for automation?
```

---

# 63. Risks Addressed

Pairwise testing supports coverage for:

```text
RISK-008 — Limit behavior interactions
RISK-009 — Account-state interaction failures
RISK-013 — Concurrent behavior interactions
RISK-017 — Beneficiary-state interactions
RISK-024 — Card state behavior
RISK-025 — Blocked card behavior
RISK-035 — Cross-browser failures
RISK-036 — Responsive failures
RISK-037 — Frontend/backend validation differences
RISK-038 — UI/API business-rule differences
RISK-043 — Timezone/date interaction defects
```

Pairwise coverage supplements the direct P0 tests mapped to these risks.

---

# 64. Pairwise Coverage Summary

This document applies pairwise coverage to:

* Browsers
* Viewports
* Banking modules
* Customer states
* KYC
* Account states
* Transaction types
* Transfers
* Payments
* Cards
* Loans
* Deposits
* Authentication
* MFA
* Sessions
* Notifications
* Profile settings
* Admin roles
* APIs
* Payloads
* HTTP methods
* Statements
* Transaction history
* Search/filtering
* Pagination
* File uploads
* Retry behavior
* Concurrency
* Scheduling
* Timezones
* Languages
* Currency
* Audit events
* Security
* Error handling
* Data volumes

---

# 65. Final Pairwise Testing Principle

Pairwise Testing should answer:

```text
Which parameters can interact?

What values can each parameter have?

Can we cover every pair without executing every possible combination?

Which combinations are impossible?

Which high-risk cases must always be added manually?

Can the same pairwise data drive UI and API automation?
```

The core rule is:

```text
Use pairwise testing to reduce redundant combinations,
not to reduce critical banking coverage.
```

For this project:

```text
Pairwise Testing
+ Boundary Value Analysis
+ Equivalence Partitioning
+ Decision Tables
+ State Transition Testing
+ Risk-Based Testing
```

together provide a strong and realistic manual test-design approach.
