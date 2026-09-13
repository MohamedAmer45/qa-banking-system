# Banking System — Customer Management Test Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Customer Management            |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Draft                          |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document defines high-level manual test scenarios for customer management functionality within the Banking System.

Customer management includes the lifecycle and maintenance of customer information and customer status.

The scenarios cover both:

* Customer self-service functionality
* Administrative customer-management functionality

Customer data is security-sensitive and must be protected against unauthorized access, incorrect modification, and exposure.

---

# 3. Customer Management Scope

The module includes:

* Customer profile creation
* Personal information
* Contact information
* Address information
* Identity information
* Customer status
* KYC status
* Customer restrictions
* Profile updates
* Customer search
* Customer viewing
* Administrative updates
* Customer activation
* Customer suspension
* Customer restriction
* Customer disabling
* Customer data ownership
* Audit logging
* Notifications

---

# 4. Scenario Naming Convention

Customer scenarios use:

```text
TS-CUST-XXX
```

Examples:

```text
TS-CUST-001
TS-CUST-002
TS-CUST-003
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Customer Profile Viewing Scenarios

## TS-CUST-001 — Active customer views own profile

**Priority:** P1

Expected:

* Correct customer information is displayed.
* Data belongs only to the authenticated customer.

---

## TS-CUST-002 — Customer views personal information section

**Priority:** P1

Verify fields such as:

* First name
* Last name
* Date of birth
* Contact details
* Address
* Customer ID where displayed

Expected:

Stored customer information is displayed accurately.

---

## TS-CUST-003 — Customer views contact information

**Priority:** P2

Expected:

Correct email and phone information is displayed.

---

## TS-CUST-004 — Customer views KYC status

**Priority:** P1

Expected:

The correct verification status is displayed.

Possible statuses:

```text
PENDING
VERIFIED
REJECTED
EXPIRED
```

Only statuses implemented by the application should be retained.

---

## TS-CUST-005 — Customer views account status

**Priority:** P1

Expected:

Correct customer status is displayed where appropriate.

---

## TS-CUST-006 — Customer profile loads after login

**Priority:** P1

Expected:

Profile can be accessed from authenticated navigation.

---

## TS-CUST-007 — Refresh customer profile page

**Priority:** P2

Expected:

* Profile remains accessible while authenticated.
* No incorrect or stale customer data appears.

---

## TS-CUST-008 — Customer opens profile in multiple browser tabs

**Priority:** P2

Expected:

All tabs display the same valid customer data.

---

# 6. Customer Ownership and Authorization Scenarios

## TS-CUST-009 — Customer attempts to view another customer's profile

**Priority:** P0

Expected:

Access denied.

---

## TS-CUST-010 — Customer modifies customer ID in URL

**Priority:** P0

Example:

```text
/customers/CUST-001
```

changed to:

```text
/customers/CUST-002
```

Expected:

Customer must not access another customer's information.

---

## TS-CUST-011 — Customer modifies customer ID in API request

**Priority:** P0

Expected:

Backend authorization rejects the request.

---

## TS-CUST-012 — Customer attempts direct navigation to administrative customer page

**Priority:** P0

Expected:

Access denied.

---

## TS-CUST-013 — Unauthenticated user attempts to access customer profile

**Priority:** P0

Expected:

Authentication required.

---

## TS-CUST-014 — Expired session attempts to access profile

**Priority:** P0

Expected:

Request rejected or reauthentication required.

---

## TS-CUST-015 — Customer logs out and reopens profile URL

**Priority:** P0

Expected:

Profile data cannot be accessed.

---

## TS-CUST-016 — Customer A logs out and Customer B logs in

**Priority:** P0

Expected:

No information from Customer A is displayed to Customer B.

---

# 7. Personal Information Update Scenarios

## TS-CUST-017 — Update editable personal information with valid values

**Priority:** P1

Expected:

* Update succeeds.
* Saved values appear correctly after refresh.

---

## TS-CUST-018 — Update first name with valid value

**Priority:** P2

Expected:

Change succeeds if the field is editable.

---

## TS-CUST-019 — Update last name with valid value

**Priority:** P2

Expected:

Change succeeds if allowed.

---

## TS-CUST-020 — Update personal information and cancel before saving

**Priority:** P2

Expected:

Original data remains unchanged.

---

## TS-CUST-021 — Refresh page after editing but before saving

**Priority:** P2

Expected:

Unsaved changes are handled predictably.

---

## TS-CUST-022 — Save profile with no actual changes

**Priority:** P3

Expected:

Application handles the request safely.

---

## TS-CUST-023 — Submit profile update twice rapidly

**Priority:** P2

Expected:

Duplicate requests do not create inconsistent customer state.

---

# 8. Name Validation Scenarios

## TS-CUST-024 — First name left empty

**Priority:** P1

Expected:

Rejected when the field is mandatory.

---

## TS-CUST-025 — Last name left empty

**Priority:** P1

Expected:

Rejected when required.

---

## TS-CUST-026 — Name at minimum supported length

**Priority:** P2

Expected:

Accepted.

---

## TS-CUST-027 — Name below minimum supported length

**Priority:** P2

Expected:

Rejected where a minimum exists.

---

## TS-CUST-028 — Name at maximum supported length

**Priority:** P2

Expected:

Accepted.

---

## TS-CUST-029 — Name above maximum supported length

**Priority:** P2

Expected:

Rejected safely.

---

## TS-CUST-030 — Name contains hyphen

**Priority:** P2

Example:

```text
Anne-Marie
```

Expected:

Accepted where valid names permit hyphens.

---

## TS-CUST-031 — Name contains apostrophe

**Priority:** P2

Example:

```text
O'Connor
```

Expected:

Handled correctly according to rules.

---

## TS-CUST-032 — Name contains Arabic characters

**Priority:** P2

Example:

```text
محمد أحمد
```

Expected:

Supported Unicode characters are stored and displayed correctly.

---

## TS-CUST-033 — Name contains accented Latin characters

**Priority:** P2

Examples:

```text
José
François
Müller
```

Expected:

Characters are preserved correctly.

---

## TS-CUST-034 — Name contains only spaces

**Priority:** P1

Expected:

Rejected.

---

## TS-CUST-035 — Name contains leading and trailing spaces

**Priority:** P2

Expected:

Input is normalized or handled according to requirements.

---

## TS-CUST-036 — Name contains unsupported numeric characters

**Priority:** P2

Expected:

Rejected when numbers are not permitted.

---

## TS-CUST-037 — Name contains script-like input

**Priority:** P1

Example:

```text
<script>alert(1)</script>
```

Expected:

Input is safely handled and never executed.

---

# 9. Email Update Scenarios

## TS-CUST-038 — Update email with valid unused email

**Priority:** P1

Expected:

Update succeeds according to verification requirements.

---

## TS-CUST-039 — Update email with invalid format

**Priority:** P1

Expected:

Rejected.

---

## TS-CUST-040 — Update email using another customer's registered email

**Priority:** P1

Expected:

Duplicate email is rejected.

---

## TS-CUST-041 — Update email with same current email

**Priority:** P3

Expected:

Application handles the request safely.

---

## TS-CUST-042 — Update email with leading spaces

**Priority:** P2

Expected:

Normalized or rejected consistently.

---

## TS-CUST-043 — Update email with trailing spaces

**Priority:** P2

Expected:

Duplicate checks cannot be bypassed using whitespace.

---

## TS-CUST-044 — Change email and verify re-verification requirement

**Priority:** P1

Expected:

If required, new email must be verified before becoming trusted.

---

## TS-CUST-045 — Verify old email after successful email change

**Priority:** P1

Expected:

Old email should no longer function as the current customer contact identifier where applicable.

---

## TS-CUST-046 — Security notification generated after email change

**Priority:** P1

Expected:

Notification is generated if required by business rules.

---

# 10. Phone Number Update Scenarios

## TS-CUST-047 — Update phone number using valid format

**Priority:** P1

Expected:

Update succeeds.

---

## TS-CUST-048 — Update phone with invalid characters

**Priority:** P2

Expected:

Rejected.

---

## TS-CUST-049 — Update phone below minimum supported length

**Priority:** P2

Expected:

Rejected.

---

## TS-CUST-050 — Update phone above maximum supported length

**Priority:** P2

Expected:

Rejected.

---

## TS-CUST-051 — Update phone with duplicate number

**Priority:** P1

Expected:

Behavior follows uniqueness requirements.

---

## TS-CUST-052 — Update phone using country code format

**Priority:** P2

Example:

```text
+201000000001
```

Expected:

Valid supported format is accepted.

---

## TS-CUST-053 — Phone verification required after change

**Priority:** P1

Expected:

New number follows required verification workflow.

---

## TS-CUST-054 — OTP verification for new phone fails

**Priority:** P1

Expected:

Phone change is not fully confirmed.

---

## TS-CUST-055 — OTP verification for new phone succeeds

**Priority:** P1

Expected:

Updated phone becomes verified.

---

# 11. Address Update Scenarios

## TS-CUST-056 — Update address with valid data

**Priority:** P2

Expected:

Address updates successfully.

---

## TS-CUST-057 — Leave required address field empty

**Priority:** P2

Expected:

Validation displayed.

---

## TS-CUST-058 — Address at maximum supported length

**Priority:** P2

Expected:

Accepted.

---

## TS-CUST-059 — Address above maximum supported length

**Priority:** P2

Expected:

Rejected safely.

---

## TS-CUST-060 — Address contains numbers and punctuation

**Priority:** P2

Expected:

Normal address characters are accepted.

---

## TS-CUST-061 — Address contains Arabic text

**Priority:** P2

Expected:

Unicode data is stored and displayed correctly.

---

## TS-CUST-062 — Address contains multiline input

**Priority:** P3

Expected:

Handled according to field definition.

---

# 12. Date of Birth Scenarios

## TS-CUST-063 — Valid date of birth

**Priority:** P1

Expected:

Accepted.

---

## TS-CUST-064 — Future date of birth

**Priority:** P1

Expected:

Rejected.

---

## TS-CUST-065 — Invalid calendar date

**Priority:** P1

Example:

```text
2025-02-29
```

Expected:

Rejected.

---

## TS-CUST-066 — Valid leap-day birth date

**Priority:** P2

Example:

```text
2000-02-29
```

Expected:

Accepted.

---

## TS-CUST-067 — Customer below minimum banking age

**Priority:** P1

Expected:

Behavior follows age eligibility rules.

---

## TS-CUST-068 — Customer exactly at minimum required age

**Priority:** P1

Expected:

Boundary behavior follows business rules.

---

## TS-CUST-069 — Extremely old but valid birth date

**Priority:** P2

Expected:

Handled safely according to reasonable limits.

---

# 13. Identity and KYC Scenarios

## TS-CUST-070 — Customer with VERIFIED KYC accesses unrestricted supported services

**Priority:** P0

Expected:

KYC-dependent services are available.

---

## TS-CUST-071 — Customer with PENDING KYC attempts restricted transaction

**Priority:** P0

Expected:

Restricted operation is blocked according to requirements.

---

## TS-CUST-072 — Customer with REJECTED KYC attempts restricted transaction

**Priority:** P0

Expected:

Access denied.

---

## TS-CUST-073 — Customer with EXPIRED KYC attempts restricted transaction

**Priority:** P0

Expected:

Behavior follows re-verification rules.

---

## TS-CUST-074 — Administrator changes KYC status from PENDING to VERIFIED

**Priority:** P1

Expected:

New status persists correctly.

---

## TS-CUST-075 — Administrator rejects KYC

**Priority:** P1

Expected:

Customer status and permitted functionality update correctly.

---

## TS-CUST-076 — Unauthorized admin attempts KYC modification

**Priority:** P0

Expected:

Access denied.

---

## TS-CUST-077 — Customer attempts to manually modify own KYC status

**Priority:** P0

Expected:

Not allowed.

---

## TS-CUST-078 — KYC status change generates audit record

**Priority:** P1

Expected:

Audit captures:

* Actor
* Customer
* Previous status
* New status
* Timestamp

---

## TS-CUST-079 — Customer receives KYC status notification

**Priority:** P2

Expected:

Appropriate notification where implemented.

---

# 14. Customer Status Scenarios

Potential customer statuses may include:

```text
ACTIVE
RESTRICTED
SUSPENDED
DISABLED
CLOSED
```

Only implemented states should be retained.

---

## TS-CUST-080 — Active customer uses normal banking functionality

**Priority:** P0

Expected:

Allowed functionality works.

---

## TS-CUST-081 — Restricted customer logs in

**Priority:** P1

Expected:

Login and permitted read-only behavior follow requirements.

---

## TS-CUST-082 — Restricted customer attempts prohibited transfer

**Priority:** P0

Expected:

Transfer rejected.

---

## TS-CUST-083 — Suspended customer attempts login

**Priority:** P0

Expected:

Authentication behavior follows suspension policy.

---

## TS-CUST-084 — Disabled customer attempts login

**Priority:** P0

Expected:

Access denied.

---

## TS-CUST-085 — Administrator restricts active customer

**Priority:** P0

Expected:

Customer status changes successfully if administrator is authorized.

---

## TS-CUST-086 — Administrator suspends active customer

**Priority:** P0

Expected:

Suspension takes effect according to requirements.

---

## TS-CUST-087 — Administrator disables customer

**Priority:** P0

Expected:

Customer can no longer access prohibited services.

---

## TS-CUST-088 — Administrator restores restricted customer to active

**Priority:** P1

Expected:

Permitted functionality is restored.

---

## TS-CUST-089 — Limited administrator attempts to disable customer without permission

**Priority:** P0

Expected:

Access denied.

---

# 15. Customer State Transition Scenarios

Example state model:

```text
ACTIVE
  ↓
RESTRICTED
  ↓
ACTIVE
```

Possible stronger transitions:

```text
ACTIVE
  ↓
SUSPENDED
  ↓
ACTIVE
```

and:

```text
ACTIVE
  ↓
DISABLED
```

---

## TS-CUST-090 — Valid ACTIVE → RESTRICTED transition

**Priority:** P1

Expected:

Transition succeeds for authorized actor.

---

## TS-CUST-091 — Valid RESTRICTED → ACTIVE transition

**Priority:** P1

Expected:

Customer is restored.

---

## TS-CUST-092 — Valid ACTIVE → SUSPENDED transition

**Priority:** P1

Expected:

Suspension persists and restrictions apply.

---

## TS-CUST-093 — Valid SUSPENDED → ACTIVE transition

**Priority:** P1

Expected:

Customer functionality returns according to business rules.

---

## TS-CUST-094 — Attempt unsupported transition

**Priority:** P1

Example:

```text
CLOSED → ACTIVE
```

Expected:

Rejected unless explicitly supported.

---

## TS-CUST-095 — Customer status changed while customer is logged in

**Priority:** P0

Expected:

Existing session respects the updated status.

---

## TS-CUST-096 — Customer restricted while transfer page is open

**Priority:** P0

Expected:

Transfer submission is rejected after status change.

---

## TS-CUST-097 — Customer disabled while active session exists

**Priority:** P0

Expected:

Protected operations are denied and session handling follows security requirements.

---

# 16. Admin Customer Search Scenarios

## TS-CUST-098 — Administrator searches customer by customer ID

**Priority:** P1

Expected:

Correct customer appears.

---

## TS-CUST-099 — Search customer by email

**Priority:** P1

Expected:

Correct record returned.

---

## TS-CUST-100 — Search customer by phone number

**Priority:** P2

Expected:

Correct customer returned where supported.

---

## TS-CUST-101 — Search using partial customer name

**Priority:** P2

Expected:

Relevant results returned according to search behavior.

---

## TS-CUST-102 — Search for nonexistent customer

**Priority:** P2

Expected:

Empty-result state displayed.

---

## TS-CUST-103 — Search using empty search term

**Priority:** P2

Expected:

Application follows defined behavior.

---

## TS-CUST-104 — Search using special characters

**Priority:** P1

Expected:

Handled safely.

---

## TS-CUST-105 — Search using script-like input

**Priority:** P1

Expected:

No script execution.

---

## TS-CUST-106 — Search using SQL-like input

**Priority:** P1

Expected:

No database manipulation or internal error.

---

# 17. Admin Customer List Scenarios

## TS-CUST-107 — Administrator views customer list

**Priority:** P1

Expected:

Authorized administrator can view expected customer data.

---

## TS-CUST-108 — Limited administrator views permitted customer fields only

**Priority:** P0

Expected:

Restricted data is not exposed.

---

## TS-CUST-109 — Customer list pagination

**Priority:** P2

Expected:

Records are paginated correctly.

---

## TS-CUST-110 — Move between customer list pages

**Priority:** P2

Expected:

No duplicate or missing records because of paging logic.

---

## TS-CUST-111 — Sort customer list by name

**Priority:** P2

Expected:

Correct ordering.

---

## TS-CUST-112 — Sort customer list by status

**Priority:** P2

Expected:

Correct ordering.

---

## TS-CUST-113 — Filter customers by status

**Priority:** P1

Expected:

Only matching statuses appear.

---

## TS-CUST-114 — Filter customers by KYC status

**Priority:** P1

Expected:

Filtering is accurate.

---

## TS-CUST-115 — Combine search and status filter

**Priority:** P2

Expected:

Results satisfy both conditions.

---

# 18. Administrative Customer Update Scenarios

## TS-CUST-116 — Authorized admin updates allowed customer field

**Priority:** P1

Expected:

Update succeeds and is audited.

---

## TS-CUST-117 — Admin attempts update of immutable customer identifier

**Priority:** P0

Expected:

Rejected where identifiers are immutable.

---

## TS-CUST-118 — Limited admin modifies restricted customer field

**Priority:** P0

Expected:

Permission denied.

---

## TS-CUST-119 — Admin saves invalid customer data

**Priority:** P1

Expected:

Business validation remains enforced.

Admin access must not bypass data integrity rules.

---

## TS-CUST-120 — Admin and customer update profile simultaneously

**Priority:** P1

Expected:

System handles conflicting updates consistently.

No silent data corruption should occur.

---

## TS-CUST-121 — Two admins update same customer simultaneously

**Priority:** P1

Expected:

Concurrency behavior follows application design.

Potential mechanisms may include:

* Last-write-wins
* Optimistic locking
* Conflict warning

No partial record corruption is acceptable.

---

# 19. Customer Closure Scenarios

Execute where customer-level closure exists.

## TS-CUST-122 — Customer requests closure with eligible state

**Priority:** P1

Expected:

Closure workflow begins according to requirements.

---

## TS-CUST-123 — Attempt closure with positive account balance

**Priority:** P0

Expected:

Closure blocked if zero balance is required.

---

## TS-CUST-124 — Attempt closure with active loan

**Priority:** P0

Expected:

Closure blocked where outstanding obligations exist.

---

## TS-CUST-125 — Attempt closure with pending transfer

**Priority:** P0

Expected:

Behavior follows business rules.

---

## TS-CUST-126 — Authorized administrator closes eligible customer

**Priority:** P1

Expected:

Customer enters correct closed state.

---

## TS-CUST-127 — Closed customer attempts login

**Priority:** P0

Expected:

Behavior follows closure rules.

---

## TS-CUST-128 — Closed customer attempts financial API request

**Priority:** P0

Expected:

Operation denied.

---

## TS-CUST-129 — Customer closure generates audit event

**Priority:** P1

Expected:

Closure is fully traceable.

---

# 20. Data Consistency Scenarios

## TS-CUST-130 — Updated profile matches API response

**Priority:** P1

Expected:

UI and API represent the same customer data.

---

## TS-CUST-131 — Updated profile matches database record

**Priority:** P1

Expected:

Stored data matches the successful update.

---

## TS-CUST-132 — Failed profile update does not modify database

**Priority:** P0

Expected:

Invalid update leaves existing data unchanged.

---

## TS-CUST-133 — Customer status consistent across UI and API

**Priority:** P1

Expected:

No inconsistent state.

---

## TS-CUST-134 — Customer status consistent across API and database

**Priority:** P1

Expected:

Same state represented across layers.

---

## TS-CUST-135 — Customer KYC status consistent across profile and admin view

**Priority:** P1

Expected:

Both interfaces represent the current state.

---

# 21. Concurrent Update Scenarios

## TS-CUST-136 — Profile edited in two browser tabs

**Priority:** P2

Flow:

```text
Tab A opens profile
Tab B opens profile
Tab A changes phone
Tab B changes address
```

Expected:

Final state follows defined concurrency rules without unintended data loss.

---

## TS-CUST-137 — Same field edited in two tabs

**Priority:** P1

Expected:

Conflict is resolved predictably.

---

## TS-CUST-138 — Admin restricts customer while customer edits profile

**Priority:** P1

Expected:

Update handling follows restriction rules.

---

## TS-CUST-139 — Customer disabled during profile save

**Priority:** P0

Expected:

Sensitive modification does not bypass newly applied restriction.

---

# 22. Error Handling Scenarios

## TS-CUST-140 — Profile API unavailable

**Priority:** P1

Expected:

* Safe error displayed.
* Existing customer information is not incorrectly overwritten.

---

## TS-CUST-141 — Server error during profile update

**Priority:** P1

Expected:

Update does not show false success.

---

## TS-CUST-142 — Network disconnect during profile save

**Priority:** P1

Expected:

Application does not leave ambiguous customer state.

---

## TS-CUST-143 — Slow profile update

**Priority:** P2

Expected:

Processing state is visible and repeated submission does not cause corruption.

---

## TS-CUST-144 — Malformed customer response from backend

**Priority:** P1

Expected:

UI handles failure without exposing internal exception information.

---

# 23. Security and Sensitive Data Scenarios

## TS-CUST-145 — Customer password is not displayed in profile response

**Priority:** P0

Expected:

Password or equivalent secret never appears.

---

## TS-CUST-146 — Sensitive customer fields are masked where required

**Priority:** P1

Examples may include:

* Identity number
* Sensitive account identifiers
* Contact details in certain admin roles

Expected:

Masking follows requirements.

---

## TS-CUST-147 — Limited administrator cannot view highly sensitive customer data

**Priority:** P0

Expected:

Field-level authorization is enforced.

---

## TS-CUST-148 — Customer data is not exposed in URL query parameters unnecessarily

**Priority:** P1

Expected:

Sensitive values should not appear in navigational URLs.

---

## TS-CUST-149 — Customer data is not leaked in error message

**Priority:** P1

Expected:

Errors do not expose another customer's data or internal database information.

---

## TS-CUST-150 — Customer profile response does not expose internal security fields

**Priority:** P0

Examples:

* Password hashes
* MFA secrets
* Internal security flags not intended for the user

Expected:

Sensitive internal fields are omitted.

---

# 24. Audit Scenarios

## TS-CUST-151 — Customer profile update creates audit record

**Priority:** P1

Expected:

Record contains appropriate actor and event details.

---

## TS-CUST-152 — Email change creates audit record

**Priority:** P1

Expected:

Previous and new state are traceable according to audit policy.

---

## TS-CUST-153 — Phone change creates audit record

**Priority:** P1

Expected:

Security-sensitive contact update is recorded.

---

## TS-CUST-154 — Admin restriction creates audit record

**Priority:** P0

Expected:

Audit captures:

```text
Actor
Customer
Action
Reason where applicable
Timestamp
Result
```

---

## TS-CUST-155 — Admin customer activation generates audit record

**Priority:** P1

Expected:

State transition is traceable.

---

## TS-CUST-156 — Unauthorized update attempt is recorded where required

**Priority:** P2

Expected:

Security monitoring can identify relevant prohibited action attempts.

---

# 25. Notification Scenarios

## TS-CUST-157 — Email change sends security notification

**Priority:** P1

Expected:

Notification is sent where required.

---

## TS-CUST-158 — Phone change sends security notification

**Priority:** P1

Expected:

Notification is generated appropriately.

---

## TS-CUST-159 — Customer restriction notification

**Priority:** P1

Expected:

Customer receives appropriate status notification where implemented.

---

## TS-CUST-160 — Customer suspension notification

**Priority:** P1

Expected:

Notification content matches actual status.

---

## TS-CUST-161 — Customer restored to active status notification

**Priority:** P2

Expected:

Correct customer receives notification.

---

## TS-CUST-162 — Failed status change does not generate success notification

**Priority:** P1

Expected:

Notifications reflect final persisted state.

---

# 26. Accessibility and Usability Scenarios

## TS-CUST-163 — Profile form supports keyboard navigation

**Priority:** P2

Expected:

Logical tab order.

---

## TS-CUST-164 — Profile fields have visible labels

**Priority:** P2

Expected:

Fields are understandable and accessible.

---

## TS-CUST-165 — Required fields clearly identified

**Priority:** P2

Expected:

Required status is clear.

---

## TS-CUST-166 — Validation messages clearly identify invalid fields

**Priority:** P2

Expected:

Customer can understand how to correct the input.

---

## TS-CUST-167 — Save and Cancel controls clearly distinguish their purpose

**Priority:** P3

Expected:

No ambiguity.

---

## TS-CUST-168 — Long profile information does not break layout

**Priority:** P2

Expected:

Content remains usable.

---

# 27. Responsive Scenarios

## TS-CUST-169 — View profile on desktop

**Priority:** P2

Expected:

Profile renders correctly.

---

## TS-CUST-170 — View profile on tablet

**Priority:** P2

Expected:

Fields and actions remain accessible.

---

## TS-CUST-171 — View profile on mobile

**Priority:** P1

Expected:

No essential profile data or action is inaccessible.

---

## TS-CUST-172 — Edit profile on mobile

**Priority:** P2

Expected:

Form is usable without horizontal layout problems.

---

## TS-CUST-173 — Validation errors on mobile

**Priority:** P2

Expected:

Messages are fully visible and associated with fields.

---

# 28. Cross-Browser Scenarios

## TS-CUST-174 — Profile workflow in Chrome

**Priority:** P1

Expected:

View and edit functions work.

---

## TS-CUST-175 — Profile workflow in Edge

**Priority:** P1

Expected:

Consistent behavior.

---

## TS-CUST-176 — Profile workflow in Firefox

**Priority:** P1

Expected:

Consistent behavior.

---

## TS-CUST-177 — Administrative customer management across supported browsers

**Priority:** P2

Expected:

No major browser-specific failure.

---

# 29. Boundary Scenarios

## TS-CUST-178 — First name minimum minus one

**Priority:** P2

Expected:

Rejected.

---

## TS-CUST-179 — First name at minimum

**Priority:** P2

Expected:

Accepted.

---

## TS-CUST-180 — First name maximum

**Priority:** P2

Expected:

Accepted.

---

## TS-CUST-181 — First name maximum plus one

**Priority:** P2

Expected:

Rejected.

---

## TS-CUST-182 — Address maximum length

**Priority:** P2

Expected:

Accepted.

---

## TS-CUST-183 — Address maximum plus one

**Priority:** P2

Expected:

Rejected safely.

---

## TS-CUST-184 — Customer age one day below eligibility threshold

**Priority:** P1

Expected:

Rejected where eligibility applies.

---

## TS-CUST-185 — Customer exactly at eligibility threshold

**Priority:** P1

Expected:

Accepted according to rules.

---

# 30. End-to-End Customer Scenarios

## TS-CUST-186 — Registration to verified customer profile

**Priority:** P1

Flow:

```text
Register
→ Complete verification
→ Login
→ Open profile
→ Verify personal information
→ Verify KYC status
```

---

## TS-CUST-187 — Customer changes contact information

**Priority:** P1

Flow:

```text
Login
→ Open Profile
→ Change Email
→ Complete Verification
→ Save
→ Verify Updated Profile
→ Verify Notification
→ Verify Audit Record
```

---

## TS-CUST-188 — Administrator restricts customer

**Priority:** P0

Flow:

```text
Customer ACTIVE
→ Admin Restricts Customer
→ Customer Attempts Transfer
→ Transfer Rejected
→ Verify Customer Status
→ Verify Audit
```

---

## TS-CUST-189 — Administrator suspends logged-in customer

**Priority:** P0

Flow:

```text
Customer Login
→ Admin Suspends Customer
→ Customer Attempts Protected Action
→ Access Restricted
```

---

## TS-CUST-190 — Customer KYC moves from pending to verified

**Priority:** P1

Flow:

```text
KYC PENDING
→ Restricted Feature Attempt
→ Rejected
→ Admin Verifies KYC
→ Feature Retried
→ Allowed
```

---

# 31. Critical Smoke Scenarios

Customer-management smoke coverage should include:

```text
TS-CUST-001 — View own profile
TS-CUST-009 — Cannot view another customer's profile
TS-CUST-017 — Valid profile update
TS-CUST-070 — Verified KYC customer behavior
TS-CUST-080 — Active customer behavior
TS-CUST-085 — Admin restricts customer
```

---

# 32. Regression Priority

## P0 Regression

Prioritize:

* Customer ownership
* Unauthorized access
* Customer restrictions
* Suspension
* Disabled status
* KYC restrictions
* Admin authorization
* Active-session status changes
* Sensitive information exposure

## P1 Regression

Include:

* Profile viewing
* Profile updates
* Email changes
* Phone changes
* Search
* Filtering
* Audit logging
* Notifications

## P2/P3 Regression

Include periodically:

* Layout
* Minor usability
* Secondary filters
* Cosmetic behavior

---

# 33. Automation Candidates

Strong UI automation candidates include:

* View profile
* Edit profile
* Required-field validation
* Customer ownership
* KYC status display
* Status restriction behavior
* Admin customer search
* Admin customer restriction
* Customer status filtering

Frameworks:

* Selenium
* Cypress
* Playwright

---

# 34. API Automation Candidates

Postman and REST Assured should later cover:

* Get own customer profile
* Update own profile
* Retrieve another customer's profile
* Customer search
* Customer status updates
* KYC updates
* Unauthorized updates
* Role restrictions
* Invalid request validation

---

# 35. Database Validation Candidates

SQL testing should later verify:

* Customer record creation
* Profile updates
* Customer status
* KYC status
* Email and phone changes
* Audit records
* Data uniqueness
* Failed-update rollback

---

# 36. BDD Candidates

Example:

```gherkin
Feature: Customer restrictions

Scenario: Restricted customer cannot transfer money
  Given an active customer is logged in
  And an administrator restricts the customer
  When the customer attempts to create a transfer
  Then the transfer should be rejected
  And the customer status should remain restricted
```

---

# 37. Risk Traceability

Major related risks include:

```text
RISK-002 — Unauthorized customer data access
RISK-006 — Privilege escalation
RISK-009 — Restricted account can transact
RISK-021 — Sensitive information exposure
RISK-023 — Unauthorized admin operation
RISK-030 — API accepts unauthorized request
RISK-037 — Frontend-only validation
RISK-047 — Another customer's resource accessible by ID manipulation
RISK-050 — Shared test data causes false failures
```

---

# 38. Customer Coverage Summary

This scenario catalog covers:

* Profile viewing
* Profile editing
* Names
* Email
* Phone
* Address
* Date of birth
* KYC
* Customer status
* State transitions
* Restrictions
* Admin management
* Search
* Filtering
* Customer closure
* Authorization
* Sensitive information
* Concurrency
* Error handling
* Audit logging
* Notifications
* Accessibility
* Responsive behavior
* Cross-browser behavior
* Boundary testing
* End-to-end workflows

---

# 39. Final Customer Management Testing Principle

Customer-management testing must validate both data correctness and access control.

The most important questions are:

```text
Can a customer see another customer's information?

Can a customer modify restricted information?

Can an unauthorized administrator modify a customer?

Are customer restrictions immediately enforced?

Does a KYC status actually control restricted functionality?

Can stale sessions bypass a newly applied customer restriction?

Are customer updates consistent across UI, API, and database?

Are sensitive customer fields protected?

Are important customer-state changes audited?
```

Customer identity and status influence almost every banking workflow, so customer-management defects can propagate into transfers, payments, cards, loans, accounts, security, and administration.
