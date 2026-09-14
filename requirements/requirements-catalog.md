# Banking System Requirements Catalog

## 1. Purpose

This document defines the functional requirements of the Banking System used by the QA Banking System Testing Project.

Each requirement has a unique ID that will later be referenced by:

- Test scenarios
- Test cases
- Selenium tests
- Cypress tests
- Playwright tests
- Jest tests
- Postman collections
- REST Assured tests
- Database tests
- JMeter performance tests
- Cucumber scenarios
- CI/CD pipelines

---

# 2. Authentication

| ID       | Requirement                                                                                  | Priority |
| -------- | -------------------------------------------------------------------------------------------- | -------- |
| AUTH-001 | The system shall allow registered customers to log in using valid credentials.               | Critical |
| AUTH-002 | The system shall reject invalid login credentials.                                           | Critical |
| AUTH-003 | Login error messages shall not reveal whether an email address exists.                       | High     |
| AUTH-004 | Email addresses used for login shall be case-insensitive.                                    | Medium   |
| AUTH-005 | Password validation shall be case-sensitive.                                                 | Critical |
| AUTH-006 | The system shall support multi-factor authentication using OTP.                              | Critical |
| AUTH-007 | OTP codes shall expire after a configurable period.                                          | High     |
| AUTH-008 | Previously used or expired OTP codes shall be rejected.                                      | High     |
| AUTH-009 | Accounts shall be temporarily locked after repeated failed login attempts.                   | Critical |
| AUTH-010 | Locked accounts shall not be able to authenticate until unlocked or the lock period expires. | Critical |
| AUTH-011 | Customers shall be able to request password-reset instructions.                              | High     |
| AUTH-012 | Password-reset tokens shall expire after a configurable period.                              | High     |
| AUTH-013 | Password-reset tokens shall only be usable once.                                             | High     |
| AUTH-014 | Customers shall be able to log out.                                                          | Critical |
| AUTH-015 | Logging out shall invalidate the active session.                                             | Critical |
| AUTH-016 | User sessions shall expire after a configurable inactivity period.                           | High     |
| AUTH-017 | Authenticated users shall not be able to access another user's session.                      | Critical |

---

# 3. Customer Registration and KYC

| ID      | Requirement                                                                                     | Priority |
| ------- | ----------------------------------------------------------------------------------------------- | -------- |
| KYC-001 | New customers shall be able to register for online banking.                                     | Critical |
| KYC-002 | Registration shall require mandatory personal information.                                      | High     |
| KYC-003 | Email addresses shall be unique per customer account.                                           | High     |
| KYC-004 | Phone numbers shall be validated before account activation.                                     | High     |
| KYC-005 | Customers shall be able to submit identity information.                                         | Critical |
| KYC-006 | Customers shall be able to upload required identity documents.                                  | Critical |
| KYC-007 | The system shall maintain a KYC status for each customer.                                       | Critical |
| KYC-008 | Supported KYC statuses shall include Pending, Approved, Rejected and Review Required.           | High     |
| KYC-009 | Restricted banking functionality shall remain unavailable until KYC requirements are satisfied. | Critical |
| KYC-010 | Authorized employees shall be able to approve or reject KYC submissions.                        | Critical |
| KYC-011 | Customers shall be able to update permitted profile information.                                | Medium   |
| KYC-012 | Changes to sensitive customer information shall be recorded in the audit log.                   | High     |

---

# 4. Dashboard

| ID       | Requirement                                                               | Priority |
| -------- | ------------------------------------------------------------------------- | -------- |
| DASH-001 | Customers shall see an overview of their accounts after login.            | Critical |
| DASH-002 | The dashboard shall display current and available balances.               | Critical |
| DASH-003 | The dashboard shall display recent transactions.                          | High     |
| DASH-004 | The dashboard shall display the customer's active cards.                  | Medium   |
| DASH-005 | The dashboard shall display upcoming scheduled payments where applicable. | Medium   |
| DASH-006 | The dashboard shall display unread notifications.                         | Medium   |
| DASH-007 | Customers shall only see financial information belonging to them.         | Critical |

---

# 5. Bank Accounts

| ID      | Requirement                                                                     | Priority |
| ------- | ------------------------------------------------------------------------------- | -------- |
| ACC-001 | A customer may own one or more bank accounts.                                   | Critical |
| ACC-002 | Supported account types shall include Current and Savings accounts.             | High     |
| ACC-003 | Every account shall have a unique account identifier.                           | Critical |
| ACC-004 | Customers shall be able to view account details.                                | Critical |
| ACC-005 | Customers shall be able to view their current balance.                          | Critical |
| ACC-006 | Customers shall be able to view their available balance.                        | Critical |
| ACC-007 | The system shall maintain account statuses.                                     | Critical |
| ACC-008 | Supported statuses shall include Active, Frozen, Suspended and Closed.          | High     |
| ACC-009 | Frozen, suspended or closed accounts shall not initiate financial transactions. | Critical |
| ACC-010 | Customers shall be able to retrieve account statements.                         | High     |
| ACC-011 | Account balances shall be updated after successful transactions.                | Critical |
| ACC-012 | Customers shall never be able to access accounts belonging to another customer. | Critical |

---

# 6. Beneficiaries

| ID      | Requirement                                                                                            | Priority |
| ------- | ------------------------------------------------------------------------------------------------------ | -------- |
| BEN-001 | Customers shall be able to add transfer beneficiaries.                                                 | High     |
| BEN-002 | Beneficiary account details shall be validated before creation.                                        | High     |
| BEN-003 | Duplicate beneficiaries shall be handled according to configured business rules.                       | Medium   |
| BEN-004 | Beneficiaries may require OTP verification before activation.                                          | High     |
| BEN-005 | Customers shall be able to view their beneficiaries.                                                   | Medium   |
| BEN-006 | Customers shall be able to edit permitted beneficiary information.                                     | Medium   |
| BEN-007 | Customers shall be able to delete beneficiaries.                                                       | Medium   |
| BEN-008 | Customers shall not access beneficiaries belonging to another customer.                                | Critical |
| BEN-009 | Transfers shall only be permitted to eligible beneficiaries when beneficiary verification is required. | Critical |

---

# 7. Transfers

| ID      | Requirement                                                                                  | Priority |
| ------- | -------------------------------------------------------------------------------------------- | -------- |
| TRF-001 | Customers shall be able to transfer money between their own eligible accounts.               | Critical |
| TRF-002 | Customers shall be able to transfer money to another customer of the same bank.              | Critical |
| TRF-003 | Customers shall be able to initiate transfers to supported external banks.                   | Critical |
| TRF-004 | Transfer amounts shall be greater than zero.                                                 | Critical |
| TRF-005 | A transfer shall not exceed the sender's available balance unless overdraft is supported.    | Critical |
| TRF-006 | Transfers shall respect configured per-transaction limits.                                   | Critical |
| TRF-007 | Transfers shall respect configured daily limits.                                             | Critical |
| TRF-008 | Applicable fees shall be displayed before transfer confirmation.                             | High     |
| TRF-009 | Customers shall confirm transfer details before submission.                                  | High     |
| TRF-010 | Sensitive transfers may require OTP confirmation.                                            | Critical |
| TRF-011 | Every transfer shall receive a unique transaction reference.                                 | Critical |
| TRF-012 | Successful transfers shall debit the source account correctly.                               | Critical |
| TRF-013 | Successful transfers shall credit the destination account correctly where applicable.        | Critical |
| TRF-014 | A transfer shall either complete fully or fail without partial balance modification.         | Critical |
| TRF-015 | Failed transfers shall not incorrectly modify account balances.                              | Critical |
| TRF-016 | Duplicate transfer submissions shall not create unintended duplicate financial transactions. | Critical |
| TRF-017 | Customers shall be able to schedule transfers for a future date.                             | High     |
| TRF-018 | Customers shall be able to create recurring transfers.                                       | High     |
| TRF-019 | Customers shall be able to cancel eligible scheduled transfers before execution.             | High     |
| TRF-020 | Completed transfers shall appear in transaction history.                                     | Critical |
| TRF-021 | Failed transfers shall have an appropriate failure status and reason.                        | High     |
| TRF-022 | Financial transfers shall generate audit records.                                            | Critical |

---

# 8. Payments

| ID      | Requirement                                                                  | Priority |
| ------- | ---------------------------------------------------------------------------- | -------- |
| PAY-001 | Customers shall be able to pay supported bills.                              | Critical |
| PAY-002 | Customers shall be able to select the funding account for a payment.         | High     |
| PAY-003 | Customers shall be able to save supported billers.                           | Medium   |
| PAY-004 | Payment amounts shall be validated before submission.                        | Critical |
| PAY-005 | Payments shall not exceed available funds unless permitted by account rules. | Critical |
| PAY-006 | Customers shall be able to schedule eligible payments.                       | High     |
| PAY-007 | Customers shall be able to create recurring payments.                        | High     |
| PAY-008 | Completed payments shall appear in payment and transaction history.          | High     |
| PAY-009 | Failed payments shall not incorrectly deduct funds.                          | Critical |
| PAY-010 | Each payment shall receive a unique reference number.                        | High     |

---

# 9. Cards

| ID       | Requirement                                                                | Priority |
| -------- | -------------------------------------------------------------------------- | -------- |
| CARD-001 | Customers shall be able to view cards associated with their accounts.      | High     |
| CARD-002 | Sensitive card data shall be masked in the user interface.                 | Critical |
| CARD-003 | Customers shall be able to freeze an active card.                          | Critical |
| CARD-004 | Customers shall be able to unfreeze an eligible frozen card.               | Critical |
| CARD-005 | Frozen cards shall not permit new card transactions.                       | Critical |
| CARD-006 | Customers shall be able to enable or disable online payments.              | High     |
| CARD-007 | Customers shall be able to enable or disable international usage.          | High     |
| CARD-008 | Customers shall be able to configure supported spending limits.            | High     |
| CARD-009 | Cards shall maintain statuses such as Active, Frozen, Blocked and Expired. | High     |
| CARD-010 | Unauthorized customers shall not access cards belonging to other users.    | Critical |

---

# 10. Transaction History

| ID      | Requirement                                                                                           | Priority |
| ------- | ----------------------------------------------------------------------------------------------------- | -------- |
| TXN-001 | Customers shall be able to view their transaction history.                                            | Critical |
| TXN-002 | Transaction records shall display a unique reference.                                                 | High     |
| TXN-003 | Transaction records shall display the transaction amount.                                             | High     |
| TXN-004 | Transaction records shall display the date and time.                                                  | High     |
| TXN-005 | Transaction records shall display the transaction status.                                             | High     |
| TXN-006 | Supported statuses shall include Pending, Completed, Failed, Cancelled and Reversed where applicable. | High     |
| TXN-007 | Customers shall be able to filter transactions by date.                                               | Medium   |
| TXN-008 | Customers shall be able to filter transactions by type.                                               | Medium   |
| TXN-009 | Customers shall be able to filter transactions by status.                                             | Medium   |
| TXN-010 | Customers shall be able to search transactions using supported criteria.                              | Medium   |
| TXN-011 | Customers shall be able to view transaction details.                                                  | High     |
| TXN-012 | Customers shall only see transactions associated with their own accounts.                             | Critical |
| TXN-013 | Transaction records shall remain consistent with the corresponding account balance changes.           | Critical |

---

# 11. Loans

| ID       | Requirement                                                            | Priority |
| -------- | ---------------------------------------------------------------------- | -------- |
| LOAN-001 | Customers shall be able to view available loan products.               | Medium   |
| LOAN-002 | Eligible customers shall be able to apply for a loan.                  | High     |
| LOAN-003 | Loan applications shall validate mandatory applicant information.      | High     |
| LOAN-004 | The system shall evaluate configured eligibility rules.                | High     |
| LOAN-005 | Loan applications shall maintain a processing status.                  | High     |
| LOAN-006 | Authorized staff shall be able to approve or reject loan applications. | Critical |
| LOAN-007 | Approved loans shall create the appropriate loan account or record.    | Critical |
| LOAN-008 | Customers shall be able to view outstanding loan balances.             | High     |
| LOAN-009 | Customers shall be able to view repayment schedules.                   | High     |
| LOAN-010 | Loan repayments shall reduce the outstanding loan balance correctly.   | Critical |
| LOAN-011 | Loan transactions shall be recorded in transaction history.            | High     |

---

# 12. Notifications

| ID        | Requirement                                                            | Priority |
| --------- | ---------------------------------------------------------------------- | -------- |
| NOTIF-001 | Customers shall receive notifications for configured financial events. | Medium   |
| NOTIF-002 | Successful transfers may generate notifications.                       | Medium   |
| NOTIF-003 | Failed transactions may generate notifications.                        | High     |
| NOTIF-004 | Security-sensitive account events shall generate notifications.        | High     |
| NOTIF-005 | Customers shall be able to view notifications.                         | Medium   |
| NOTIF-006 | Customers shall be able to mark notifications as read.                 | Low      |
| NOTIF-007 | The system shall maintain read and unread notification states.         | Low      |

---

# 13. Security Settings

| ID      | Requirement                                                                  | Priority |
| ------- | ---------------------------------------------------------------------------- | -------- |
| SEC-001 | Customers shall be able to change their password after authentication.       | High     |
| SEC-002 | Passwords shall comply with configured password policies.                    | Critical |
| SEC-003 | Customers shall be able to enable supported MFA methods.                     | Critical |
| SEC-004 | Customers shall be able to view active sessions.                             | High     |
| SEC-005 | Customers shall be able to terminate supported active sessions.              | High     |
| SEC-006 | Customers shall be able to view recognized devices where supported.          | Medium   |
| SEC-007 | Sensitive security changes shall require additional verification.            | Critical |
| SEC-008 | Security changes shall generate audit entries.                               | High     |
| SEC-009 | Authentication credentials shall never be returned through application APIs. | Critical |
| SEC-010 | Access-controlled resources shall enforce server-side authorization.         | Critical |

---

# 14. Administration and Back Office

| ID        | Requirement                                                                              | Priority |
| --------- | ---------------------------------------------------------------------------------------- | -------- |
| ADMIN-001 | Authorized employees shall be able to search customer records.                           | Critical |
| ADMIN-002 | Authorized employees shall be able to view permitted customer information.               | Critical |
| ADMIN-003 | Administrators shall be able to manage customer account status.                          | Critical |
| ADMIN-004 | Authorized users shall be able to freeze eligible accounts.                              | Critical |
| ADMIN-005 | Authorized users shall be able to unfreeze eligible accounts.                            | Critical |
| ADMIN-006 | Authorized employees shall be able to review KYC submissions.                            | Critical |
| ADMIN-007 | Authorized employees shall be able to approve or reject KYC submissions.                 | Critical |
| ADMIN-008 | Authorized employees shall be able to review flagged transactions.                       | High     |
| ADMIN-009 | Authorized employees shall be able to review loan applications.                          | High     |
| ADMIN-010 | Administrative functionality shall require an authorized employee or administrator role. | Critical |
| ADMIN-011 | Customers shall not be able to access administrative APIs or pages.                      | Critical |
| ADMIN-012 | Sensitive administrative actions shall be recorded in the audit log.                     | Critical |

---

# 15. Audit Logging

| ID        | Requirement                                                   | Priority |
| --------- | ------------------------------------------------------------- | -------- |
| AUDIT-001 | Authentication attempts shall be auditable.                   | High     |
| AUDIT-002 | Financial transfers shall generate audit events.              | Critical |
| AUDIT-003 | Payments shall generate audit events.                         | Critical |
| AUDIT-004 | Sensitive profile changes shall generate audit events.        | High     |
| AUDIT-005 | Security-setting changes shall generate audit events.         | Critical |
| AUDIT-006 | Administrative actions shall generate audit events.           | Critical |
| AUDIT-007 | Audit records shall contain the acting user where applicable. | High     |
| AUDIT-008 | Audit records shall contain timestamps.                       | High     |
| AUDIT-009 | Audit records shall identify the action performed.            | High     |
| AUDIT-010 | Audit records shall not be modifiable by normal customers.    | Critical |

---

# 16. Database Integrity

| ID     | Requirement                                                                                                     | Priority |
| ------ | --------------------------------------------------------------------------------------------------------------- | -------- |
| DB-001 | Every customer shall have a unique database identifier.                                                         | Critical |
| DB-002 | Every bank account shall have a unique database identifier.                                                     | Critical |
| DB-003 | Account ownership relationships shall maintain referential integrity.                                           | Critical |
| DB-004 | Every financial transaction shall have a unique transaction identifier.                                         | Critical |
| DB-005 | Transaction records shall reference valid accounts.                                                             | Critical |
| DB-006 | Financial amounts shall use appropriate decimal precision.                                                      | Critical |
| DB-007 | Financial calculations shall not rely on floating-point storage.                                                | Critical |
| DB-008 | Successful transfers shall persist corresponding transaction records.                                           | Critical |
| DB-009 | Failed transfers shall not leave partial balance changes.                                                       | Critical |
| DB-010 | Concurrent financial operations shall maintain correct balances.                                                | Critical |
| DB-011 | Duplicate API requests shall not result in duplicate financial transactions when idempotency protections apply. | Critical |
| DB-012 | Deleted or disabled entities shall preserve required financial history.                                         | High     |
| DB-013 | Foreign-key relationships shall prevent orphan financial records.                                               | Critical |
| DB-014 | Audit records shall reference valid entities where applicable.                                                  | High     |
| DB-015 | Database transactions shall rollback correctly when financial operations fail.                                  | Critical |

---

# 17. General System Requirements

| ID      | Requirement                                                                                            | Priority |
| ------- | ------------------------------------------------------------------------------------------------------ | -------- |
| SYS-001 | The application shall provide consistent validation between UI and API layers.                         | High     |
| SYS-002 | API responses shall return appropriate HTTP status codes.                                              | High     |
| SYS-003 | Validation errors shall provide meaningful responses without exposing sensitive internal information.  | High     |
| SYS-004 | Users shall only access resources permitted by their assigned roles.                                   | Critical |
| SYS-005 | Sensitive information shall not be exposed through URLs, logs or client-side responses.                | Critical |
| SYS-006 | Financial operations shall be traceable through unique references.                                     | Critical |
| SYS-007 | Application timestamps shall be stored and handled consistently.                                       | High     |
| SYS-008 | The system shall handle unexpected failures without corrupting financial data.                         | Critical |
| SYS-009 | Critical operations shall maintain consistency across UI, API and database layers.                     | Critical |
| SYS-010 | Application functionality shall support automated testing through stable APIs and testable interfaces. | Medium   |

---

# 18. Requirement Traceability

The following naming convention shall be used throughout the project:

```text
Requirement:
TRF-001

Test Scenario:
TS-TRF-001

Test Case:
TC-TRF-001

Cucumber Scenario:
TRF-001

UI Automation:
TRF-001

API Test:
TRF-001

Database Validation:
TRF-001
```

This allows every test result to be traced back to the original system requirement.

---

# 19. Requirement Categories

| Prefix | Module                      |
| ------ | --------------------------- |
| AUTH   | Authentication              |
| KYC    | Customer Registration / KYC |
| DASH   | Dashboard                   |
| ACC    | Bank Accounts               |
| BEN    | Beneficiaries               |
| TRF    | Transfers                   |
| PAY    | Payments                    |
| CARD   | Cards                       |
| TXN    | Transaction History         |
| LOAN   | Loans                       |
| NOTIF  | Notifications               |
| SEC    | Security                    |
| ADMIN  | Administration              |
| AUDIT  | Audit Logging               |
| DB     | Database Integrity          |
| SYS    | General System Requirements |

<!-- NOVABANK-REQUIREMENTS-SYNC-START -->

## Current Build Requirements Synchronization

The original banking-system requirements remain the intended product specification.

A missing implementation does not automatically remove or redefine a requirement.

### Implemented in Current Build

| Requirement Area | Status |
|---|---|
| Customer demo session | Implemented |
| Admin demo session | Implemented |
| Logout | Implemented |
| Role-based navigation | Implemented |
| Dashboard | Implemented |
| Account display | Implemented |
| Masked account numbers | Implemented |
| Transactions | Implemented |
| Transfers | Implemented |
| Bills | Implemented |
| Cards | Implemented |
| Card freeze/unfreeze | Implemented |
| Loans | Implemented |
| Notifications | Implemented |
| Profile | Implemented |
| Admin summary | Implemented |

### Current Implementation Gaps

| Requirement Area | Status |
|---|---|
| Email/password authentication | Build gap |
| MFA | Build gap |
| Beneficiary management | Build gap |
| Account creation | Build gap |
| Extended account controls | Build gap |
| Dedicated statements | Build gap |
| Persistent database records | Build gap |

### Authentication Deviation

Target/full banking requirement:

`Credentials -> authentication -> MFA where required -> banking session`

Current QA implementation:

`Role selection -> deterministic customer/admin session`

The current demo-session mechanism is a QA-environment implementation choice. It does not permanently remove authentication-security requirements from the intended banking-system scope.

<!-- NOVABANK-REQUIREMENTS-SYNC-END -->