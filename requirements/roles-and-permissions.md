# Banking System Roles and Permissions

## 1. Purpose

This document defines the user roles, permissions, authorization boundaries, ownership rules, and access-control expectations for the Banking System.

The purpose of this document is to ensure that:

- Customers can access only their own banking information.
- Bank employees can perform approved operational activities.
- Administrators can access privileged administrative functionality.
- Unauthorized users cannot access protected functionality.
- Authorization is enforced on the backend and not only through the UI.
- Sensitive actions are recorded in audit logs.
- Role-based access control can be tested consistently across UI, API, and database layers.

The initial Banking System supports the following roles:

```text
CUSTOMER
BANK_EMPLOYEE
ADMIN
```

---

# 2. General Authorization Principles

## RP-001 — Authentication Required

Protected banking functionality shall require authentication.

Unauthenticated users shall not access:

```text
Customer dashboard
Bank accounts
Transfers
Payments
Cards
Beneficiaries
Loans
Transaction history
Security settings
Employee functions
Administrative functions
```

---

## RP-002 — Server-Side Authorization

Authorization shall be enforced by the backend.

Removing, hiding, or disabling UI controls alone shall not be considered sufficient authorization.

---

## RP-003 — Default Deny

Access shall be denied unless the authenticated role is explicitly authorized to perform the requested operation.

---

## RP-004 — Resource Ownership

Customers shall only access resources belonging to their own customer identity.

This includes:

```text
Accounts
Cards
Beneficiaries
Transfers
Payments
Transactions
Loans
Notifications
Profile information
Security settings
Sessions
Devices
```

---

## RP-005 — Cross-Customer Access

Changing an identifier in a URL, request path, query parameter, or API body must not allow one customer to access another customer's data.

Example:

```text
/api/accounts/1001
```

If account `1001` belongs to another customer, access shall be rejected.

---

## RP-006 — Unauthenticated API Access

Requests to protected API endpoints without valid authentication should normally return:

```text
401 Unauthorized
```

---

## RP-007 — Unauthorized Role Access

An authenticated user attempting an operation outside their permissions should normally receive:

```text
403 Forbidden
```

---

## RP-008 — Audit Requirement

Sensitive employee and administrative operations shall generate audit records.

---

# 3. CUSTOMER Role

## Role

```text
CUSTOMER
```

## Purpose

Represents a normal banking customer using the customer-facing banking application.

A customer may perform operations related to their own accounts and banking products but shall not perform employee or administrative operations.

---

# 4. CUSTOMER Permissions

## 4.1 Authentication

A CUSTOMER may:

```text
Register
Log in
Log out
Complete MFA/OTP verification
Request password reset
Reset password
Change password
View own active sessions
Terminate eligible own sessions
Manage supported security settings
```

A CUSTOMER may not:

```text
Access another customer's authentication information
View password hashes
View OTP secrets
Manage another customer's sessions
Unlock their own account through administrator functionality
```

---

## 4.2 Customer Profile

A CUSTOMER may:

```text
View own profile
Update permitted own profile fields
Submit KYC information
Upload own KYC documents
View own KYC status
```

A CUSTOMER may not:

```text
View another customer's profile
Change own KYC approval status
Approve KYC
Reject KYC
Modify protected identity fields without required verification
```

---

## 4.3 Accounts

A CUSTOMER may:

```text
View own accounts
View own account balances
View own account details
View own statements
View own account status
```

A CUSTOMER may not:

```text
View another customer's account
Modify account balances directly
Change their own account status directly
Freeze an entire bank account through administrative controls
Close an account through an unauthorized API
Change account ownership
```

---

## 4.4 Beneficiaries

A CUSTOMER may:

```text
Create own beneficiaries
View own beneficiaries
Edit permitted beneficiary information
Delete own beneficiaries
Verify own beneficiaries
```

A CUSTOMER may not:

```text
View another customer's beneficiaries
Modify another customer's beneficiaries
Delete another customer's beneficiaries
Use unverified beneficiaries when verification is required
```

---

## 4.5 Transfers

A CUSTOMER may:

```text
Transfer between eligible own accounts
Transfer to eligible same-bank beneficiaries
Transfer to supported external beneficiaries
Create scheduled transfers
Create recurring transfers
Cancel eligible scheduled transfers
View own transfer history
```

A CUSTOMER may not:

```text
Transfer funds from another customer's account
Bypass transfer limits
Bypass insufficient-funds validation
Modify completed transfer records
Cancel completed transfers
Directly change transfer status
Bypass required OTP confirmation
```

---

## 4.6 Payments

A CUSTOMER may:

```text
Pay supported bills
Use eligible own accounts for payment
Save own billers
Schedule payments
Create recurring payments
View own payment history
```

A CUSTOMER may not:

```text
Use another customer's account
Modify another customer's saved billers
Change completed payment records
Bypass payment limits or balance validation
```

---

## 4.7 Cards

A CUSTOMER may:

```text
View own cards
Freeze eligible own cards
Unfreeze eligible own cards
Enable or disable online transactions
Enable or disable international usage
Manage permitted spending limits
View supported card information
```

A CUSTOMER may not:

```text
View another customer's card
Modify another customer's card
View unrestricted sensitive card data
Unblock administratively blocked cards
Change card ownership
Directly change protected card database fields
```

---

## 4.8 Transactions

A CUSTOMER may:

```text
View own transaction history
Search own transactions
Filter own transactions
View own transaction details
Download supported statements
```

A CUSTOMER may not:

```text
View another customer's transactions
Modify transaction history
Delete financial transactions
Change transaction status
Change transaction amount
```

---

## 4.9 Loans

A CUSTOMER may:

```text
View available loan products
Submit eligible loan applications
View own loan applications
View own loan balances
View own repayment schedules
Make supported loan repayments
```

A CUSTOMER may not:

```text
View another customer's loans
Approve own loan application
Reject loan applications
Change loan application status directly
Modify loan balance directly
```

---

## 4.10 Notifications

A CUSTOMER may:

```text
View own notifications
Mark own notifications as read
Manage permitted notification settings
```

A CUSTOMER may not:

```text
View another customer's notifications
Change system-generated notification ownership
```

---

## 4.11 Administration

A CUSTOMER shall not access:

```text
Administrative dashboard
Customer administration
KYC approval tools
Account freeze controls
Account unfreeze controls
Loan approval tools
Transaction review tools
System configuration
Audit management
Employee-management functionality
```

---

# 5. BANK_EMPLOYEE Role

## Role

```text
BANK_EMPLOYEE
```

## Purpose

Represents an authorized bank employee who performs operational and back-office activities.

The employee role shall have more privileges than a customer but fewer privileges than an administrator.

---

# 6. BANK_EMPLOYEE Permissions

## 6.1 Employee Authentication

A BANK_EMPLOYEE may:

```text
Log in through employee authentication
Use required MFA
Log out
Change own password
Manage permitted own security settings
```

A BANK_EMPLOYEE may not:

```text
Access another employee's credentials
Disable security controls without permission
Modify own role
Promote themselves to ADMIN
```

---

## 6.2 Customer Search

A BANK_EMPLOYEE may:

```text
Search customers
View permitted customer information
View customer account status
View customer KYC status
View permitted transaction information
```

Access shall be limited to information necessary for employee responsibilities.

---

## 6.3 KYC Operations

A BANK_EMPLOYEE may:

```text
Review KYC applications
View submitted KYC information
View permitted KYC documents
Approve eligible KYC submissions
Reject eligible KYC submissions
Mark submissions for additional review
```

KYC decisions must generate audit records.

---

## 6.4 Account Operations

A BANK_EMPLOYEE may, when authorized:

```text
View customer account status
Freeze eligible accounts
Unfreeze eligible accounts
Review account restrictions
```

A BANK_EMPLOYEE may not:

```text
Directly change account balances
Delete financial history
Transfer customer funds for personal use
Change account ownership without an approved process
```

---

## 6.5 Transaction Review

A BANK_EMPLOYEE may:

```text
Search permitted transactions
Review flagged transactions
View transaction details needed for investigation
View transaction status
```

A BANK_EMPLOYEE may not:

```text
Delete completed transaction history
Directly edit transaction amounts
Directly manipulate balances
Create unauthorized reversal records
```

---

## 6.6 Loan Operations

A BANK_EMPLOYEE may, when authorized:

```text
Review loan applications
Approve eligible loan applications
Reject loan applications
View loan application details
View repayment information
```

Loan decisions shall generate audit records.

---

## 6.7 Customer Security Assistance

A BANK_EMPLOYEE may perform only specifically authorized support operations.

Potentially permitted operations may include:

```text
Review account lock status
Assist with customer verification
Escalate security issues
```

Sensitive authentication operations shall remain restricted.

---

## 6.8 Employee Restrictions

A BANK_EMPLOYEE shall not:

```text
Create ADMIN users
Change system-wide configuration unless explicitly authorized
Modify audit records
Delete financial history
Change their own permissions
Change their own role
Access database credentials
View customer passwords
View password hashes
View OTP secrets
Access unrestricted card secrets
```

---

# 7. ADMIN Role

## Role

```text
ADMIN
```

## Purpose

Represents a privileged administrative user responsible for managing higher-risk operational and system-level functionality.

ADMIN access shall be protected by strong authentication and auditing.

---

# 8. ADMIN Permissions

## 8.1 Administrative Access

An ADMIN may access:

```text
Administrative dashboard
Customer management
Account management
Employee management where implemented
KYC management
Loan administration
Transaction review
Security administration
Audit views
System configuration
```

---

## 8.2 Customer Management

An ADMIN may:

```text
Search customers
View permitted customer details
Review account status
Manage supported customer status fields
Investigate account issues
```

An ADMIN shall still not have access to customer plaintext passwords or restricted authentication secrets.

---

## 8.3 Account Administration

An ADMIN may:

```text
Freeze eligible accounts
Unfreeze eligible accounts
Suspend eligible accounts
Review account status
Manage supported operational restrictions
```

Changes shall generate audit records.

---

## 8.4 KYC Administration

An ADMIN may:

```text
Review KYC submissions
Approve KYC submissions
Reject KYC submissions
Request additional review
View KYC decision history
```

---

## 8.5 Loan Administration

An ADMIN may:

```text
Review loan applications
Approve eligible applications
Reject applications
Review repayment status
Review loan records
```

---

## 8.6 Transaction Administration

An ADMIN may:

```text
Review transactions
Search transactions
Investigate flagged transactions
Review failed transactions
Review reversed transactions
```

Direct arbitrary modification of completed financial history shall not be permitted.

---

## 8.7 System Configuration

An ADMIN may manage configured values where system functionality supports it.

Examples include:

```text
Transfer limits
Authentication thresholds
OTP configuration
Session timeout
Operational feature settings
Supported limits
```

Configuration changes must be audited.

---

## 8.8 Audit Access

An ADMIN may:

```text
Search audit records
View audit records
Filter audit records
Review security events
Review administrative activity
```

An ADMIN should not normally:

```text
Edit existing audit records
Delete audit records
Rewrite audit history
```

---

## 8.9 Security Administration

An ADMIN may perform specifically authorized security operations such as:

```text
Unlock eligible user accounts
Review suspicious login activity
Terminate compromised sessions
Review security events
Manage supported user-status controls
```

All sensitive security actions shall be audited.

---

# 9. Role Permission Matrix

Legend:

```text
✓ = Allowed
△ = Allowed with restrictions / authorization
✗ = Not allowed
```

| Function                             | CUSTOMER | BANK_EMPLOYEE | ADMIN |
| ------------------------------------ | :------: | :-----------: | :---: |
| Log in                               |    ✓     |       ✓       |   ✓   |
| Manage own password                  |    ✓     |       ✓       |   ✓   |
| View own profile                     |    ✓     |       ✓       |   ✓   |
| View another customer's profile      |    ✗     |       △       |   ✓   |
| Update own permitted profile         |    ✓     |       △       |   ✓   |
| Submit KYC                           |    ✓     |       ✗       |   ✗   |
| Approve KYC                          |    ✗     |       ✓       |   ✓   |
| Reject KYC                           |    ✗     |       ✓       |   ✓   |
| View own accounts                    |    ✓     |       ✓       |   ✓   |
| View another customer's accounts     |    ✗     |       △       |   ✓   |
| Directly change balances             |    ✗     |       ✗       |   ✗   |
| Freeze own card                      |    ✓     |       ✗       |   △   |
| Freeze customer bank account         |    ✗     |       △       |   ✓   |
| Unfreeze customer bank account       |    ✗     |       △       |   ✓   |
| Create beneficiary                   |    ✓     |       ✗       |   ✗   |
| Transfer own funds                   |    ✓     |       ✗       |   ✗   |
| Bypass transfer limit                |    ✗     |       ✗       |   ✗   |
| View own transaction history         |    ✓     |       ✓       |   ✓   |
| View customer transaction history    |    ✗     |       △       |   ✓   |
| Modify completed transaction history |    ✗     |       ✗       |   ✗   |
| Apply for loan                       |    ✓     |       ✗       |   ✗   |
| Approve loan                         |    ✗     |       △       |   ✓   |
| Reject loan                          |    ✗     |       △       |   ✓   |
| View own notifications               |    ✓     |       ✓       |   ✓   |
| Access admin dashboard               |    ✗     |       ✗       |   ✓   |
| Search customers                     |    ✗     |       ✓       |   ✓   |
| View audit logs                      |    ✗     |       △       |   ✓   |
| Modify audit logs                    |    ✗     |       ✗       |   ✗   |
| Change system configuration          |    ✗     |       ✗       |   ✓   |
| Change own role                      |    ✗     |       ✗       |   ✗   |
| Create ADMIN users                   |    ✗     |       ✗       |   △   |
| View passwords                       |    ✗     |       ✗       |   ✗   |
| View password hashes                 |    ✗     |       ✗       |   ✗   |
| View OTP secrets                     |    ✗     |       ✗       |   ✗   |

---

# 10. Resource Ownership Rules

## RP-OWN-001 — Accounts

A customer may access an account only when:

```text
account.customer_id = authenticated_customer.id
```

---

## RP-OWN-002 — Beneficiaries

A customer may access a beneficiary only when:

```text
beneficiary.customer_id = authenticated_customer.id
```

---

## RP-OWN-003 — Cards

A customer may access a card only when the card belongs to an account owned by that customer.

---

## RP-OWN-004 — Transactions

A customer may access a transaction only when the transaction is associated with an account they are authorized to access.

---

## RP-OWN-005 — Loans

A customer may access a loan only when:

```text
loan.customer_id = authenticated_customer.id
```

---

## RP-OWN-006 — Notifications

A customer may access a notification only when:

```text
notification.user_id = authenticated_user.id
```

---

## RP-OWN-007 — Sessions

A customer may manage only sessions associated with their own authenticated identity.

---

# 11. API Authorization Expectations

## RP-API-001 — No Authentication

Example:

```http
GET /api/accounts
```

Without authentication:

```text
Expected: 401 Unauthorized
```

---

## RP-API-002 — Customer Accessing Own Resource

Example:

```http
GET /api/accounts/{ownAccountId}
```

Expected:

```text
200 OK
```

when the account belongs to the authenticated customer.

---

## RP-API-003 — Customer Accessing Another Customer's Resource

Example:

```http
GET /api/accounts/{anotherCustomersAccountId}
```

Expected:

```text
403 Forbidden
```

or:

```text
404 Not Found
```

depending on the security design.

The API must never return another customer's account data.

---

## RP-API-004 — Customer Accessing Admin Endpoint

Example:

```http
GET /api/admin/customers
```

Authenticated as:

```text
CUSTOMER
```

Expected:

```text
403 Forbidden
```

---

## RP-API-005 — Employee Accessing Permitted Endpoint

Example:

```http
GET /api/admin/customers/{customerId}
```

Authenticated as an authorized:

```text
BANK_EMPLOYEE
```

Expected:

```text
200 OK
```

if the employee has the required permission.

---

## RP-API-006 — Employee Accessing Admin-Only Endpoint

Example:

```http
PUT /api/admin/system-config
```

Authenticated as:

```text
BANK_EMPLOYEE
```

Expected:

```text
403 Forbidden
```

---

## RP-API-007 — Admin Access

Authenticated ADMIN users shall access admin functionality permitted to their role.

---

# 12. UI Authorization Expectations

## RP-UI-001 — Customer Navigation

A customer shall not see privileged administrative navigation items.

---

## RP-UI-002 — Employee Navigation

A bank employee shall see only employee functionality permitted to their role.

---

## RP-UI-003 — Admin Navigation

An administrator may see authorized administrative navigation.

---

## RP-UI-004 — Direct URL Protection

Even if a CUSTOMER manually navigates to:

```text
/admin
/admin/customers
/admin/audit
```

the backend and frontend shall prevent unauthorized access.

---

## RP-UI-005 — Hidden Button Bypass

Removing client-side restrictions through browser developer tools must not allow unauthorized actions.

---

# 13. Privilege Escalation Rules

## RP-PRIV-001 — Role Manipulation

A user shall not change their role by modifying a request.

Example malicious request:

```json
{
  "role": "ADMIN"
}
```

shall not promote a CUSTOMER to ADMIN.

---

## RP-PRIV-002 — Token Manipulation

Modifying role information in client-controlled data must not grant additional privileges.

---

## RP-PRIV-003 — Employee Self-Promotion

A BANK_EMPLOYEE shall not promote their own account to ADMIN.

---

## RP-PRIV-004 — Client-Side Trust

The backend shall not trust client-supplied role or ownership information without server-side validation.

---

# 14. Sensitive Data Restrictions

No supported role shall receive plaintext:

```text
Passwords
Password hashes
OTP secrets
Private authentication secrets
Full unrestricted security credentials
Database credentials
Private encryption keys
```

Sensitive card data shall be masked or protected according to system design.

---

# 15. Administrative Audit Requirements

The following operations shall generate audit records where applicable:

```text
KYC approval
KYC rejection
Account freeze
Account unfreeze
Account suspension
Customer-status changes
Loan approval
Loan rejection
Security actions
Configuration changes
Administrative login
Administrative logout
Privilege changes
Employee-management actions
```

Audit records should include:

```text
Actor
Role
Action
Target resource
Timestamp
Result
Relevant reference ID
```

---

# 16. Test Coverage Expectations

Role and permission testing shall include:

```text
Positive authorization testing
Negative authorization testing
Unauthenticated access testing
Horizontal privilege escalation
Vertical privilege escalation
Direct URL access
Direct API access
Resource-ID manipulation
Role manipulation
Token/session authorization
Cross-customer access
Cross-role access
Audit verification
UI restriction verification
Backend enforcement verification
```

---

# 17. Horizontal Privilege Escalation

Horizontal privilege escalation occurs when one user accesses another user at the same privilege level.

Example:

```text
Customer A → Customer B's account
```

This must be prevented.

Example test:

```http
GET /api/accounts/customer-b-account-id
Authorization: Customer-A-Token
```

Expected:

```text
Access denied
No Customer B data returned
```

---

# 18. Vertical Privilege Escalation

Vertical privilege escalation occurs when a lower-privileged user accesses functionality belonging to a higher role.

Example:

```text
CUSTOMER → ADMIN functionality
```

This must be prevented.

Example:

```http
POST /api/admin/accounts/{id}/freeze
Authorization: Customer-Token
```

Expected:

```text
403 Forbidden
```

---

# 19. Role Hierarchy

The initial conceptual hierarchy is:

```text
ADMIN
  ↑
BANK_EMPLOYEE
  ↑
CUSTOMER
```

However, higher roles shall not automatically bypass all business rules.

For example:

```text
ADMIN cannot directly rewrite completed financial history.
ADMIN cannot read plaintext passwords.
ADMIN cannot bypass financial consistency constraints.
ADMIN cannot delete required audit history.
```

---

# 20. Role-Based Test Accounts

The test environment shall eventually provide dedicated accounts for:

```text
customer.standard
customer.kyc_pending
customer.locked
customer.frozen_account
customer.multiple_accounts

employee.standard
employee.kyc_reviewer
employee.loan_reviewer

admin.standard
```

These identities shall use test-only credentials and data.

Secrets must not be committed to GitHub.

---

# 21. Permission Traceability

Permissions shall be traceable to requirements, business rules, and test cases.

Example:

```text
Requirement:
ADMIN-011

Business Rule:
BR-AUTHZ-003

Permission Rule:
RP-API-004

Test Scenario:
TS-AUTHZ-001

Test Case:
TC-AUTHZ-001
```

---

# 22. Role Summary

## CUSTOMER

Primary purpose:

```text
Manage their own banking activity.
```

Allowed scope:

```text
Own profile
Own accounts
Own cards
Own beneficiaries
Own transfers
Own payments
Own loans
Own transactions
Own notifications
Own security settings
```

---

## BANK_EMPLOYEE

Primary purpose:

```text
Perform authorized operational banking work.
```

Allowed scope may include:

```text
Customer lookup
KYC review
Account review
Account restriction operations
Transaction investigation
Loan review
Operational support
```

---

## ADMIN

Primary purpose:

```text
Perform privileged banking administration and system management.
```

Allowed scope may include:

```text
Administrative customer management
Account administration
KYC administration
Loan administration
Security administration
Audit access
System configuration
Administrative investigations
```

---

# 23. Final Authorization Principle

Every protected operation must answer all of the following questions before it is executed:

```text
1. Is the user authenticated?
2. Is the session valid?
3. Does the user's role permit this operation?
4. Does the user own the requested resource when ownership applies?
5. Is the resource in a state that permits the operation?
6. Are additional security checks required?
7. Should the operation create an audit record?
```

If any required authorization condition fails, the operation must not be executed.
