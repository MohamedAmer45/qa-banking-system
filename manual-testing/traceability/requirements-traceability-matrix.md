# Banking System — Requirements Traceability Matrix

## 1. Document Information

| Field    | Value                            |
| -------- | -------------------------------- |
| Project  | Banking System Testing Project   |
| Document | Requirements Traceability Matrix |
| Version  | 1.0                              |
| Status   | Draft                            |
| Owner    | QA Engineering                   |

---

# 2. Purpose

The Requirements Traceability Matrix (RTM) links Banking System requirements and business rules to their corresponding:

* Test scenarios
* Test cases
* Test suites
* Risks
* Test design techniques
* Planned automation
* API coverage
* Database coverage
* Performance coverage

The purpose is to ensure that every important requirement has test coverage and that no major requirement is unintentionally omitted.

The RTM also supports:

* Coverage analysis
* Regression selection
* Release readiness
* Defect impact analysis
* Auditability
* Automation planning

---

# 3. Traceability Flow

The expected traceability chain is:

```text
Requirement
    ↓
Business Rule
    ↓
Risk
    ↓
Test Scenario
    ↓
Detailed Test Case
    ↓
Test Suite
    ↓
Execution Result
    ↓
Defect
    ↓
Automation
```

For high-risk banking functionality, additional traceability may include:

```text
API Test
Database Validation
Performance Test
Security Test
BDD Scenario
```

---

# 4. Requirement ID Convention

For this traceability matrix, requirements are grouped using:

```text
REQ-AUTH-XXX   Authentication
REQ-CUST-XXX   Customers
REQ-ACC-XXX    Accounts
REQ-BEN-XXX    Beneficiaries
REQ-TRF-XXX    Transfers
REQ-PAY-XXX    Payments
REQ-CARD-XXX   Cards
REQ-LOAN-XXX   Loans
REQ-DEP-XXX    Deposits
REQ-TXN-XXX    Transactions
REQ-STMT-XXX   Statements
REQ-NOT-XXX    Notifications
REQ-PROF-XXX   Profile / Settings
REQ-SEC-XXX    Security
REQ-ADM-XXX    Admin / Operations
REQ-AUD-XXX    Audit
REQ-LIM-XXX    Limits
REQ-FEE-XXX    Fees
REQ-NFR-XXX    Non-functional
```

These IDs provide stable QA traceability identifiers for this project.

If the authoritative requirements catalog uses different identifiers later, this matrix should be updated to map them directly.

---

# 5. Coverage Status

Use:

```text
COVERED
PARTIAL
PLANNED
NOT_COVERED
NOT_APPLICABLE
```

---

# 6. Automation Status

Use:

```text
MANUAL
PLANNED_UI
PLANNED_API
PLANNED_DB
PLANNED_PERFORMANCE
PLANNED_BDD
AUTOMATED
```

Multiple values may apply.

---

# 7. Authentication Traceability

| Requirement  | Requirement Summary                                | Scenario File               | Test Case File               | Risk     | Regression    | Automation |
| ------------ | -------------------------------------------------- | --------------------------- | ---------------------------- | -------- | ------------- | ---------- |
| REQ-AUTH-001 | Customer can login with valid credentials          | authentication-scenarios.md | authentication-test-cases.md | —        | REG-001       | UI + API   |
| REQ-AUTH-002 | Invalid credentials must be rejected               | authentication-scenarios.md | authentication-test-cases.md | RISK-005 | REG-002       | UI + API   |
| REQ-AUTH-003 | MFA must be completed where enabled                | authentication-scenarios.md | authentication-test-cases.md | RISK-005 | REG-003       | UI + API   |
| REQ-AUTH-004 | Invalid MFA must be rejected                       | authentication-scenarios.md | authentication-test-cases.md | RISK-044 | SEC-SUITE-006 | API        |
| REQ-AUTH-005 | Expired MFA code must be rejected                  | authentication-scenarios.md | authentication-test-cases.md | RISK-045 | SEC-SUITE-007 | API        |
| REQ-AUTH-006 | MFA code must not be reusable                      | authentication-scenarios.md | authentication-test-cases.md | RISK-044 | SEC-SUITE-008 | API        |
| REQ-AUTH-007 | Failed login threshold must trigger lockout        | authentication-scenarios.md | authentication-test-cases.md | RISK-016 | REG-064       | UI + API   |
| REQ-AUTH-008 | Locked user cannot authenticate normally           | authentication-scenarios.md | authentication-test-cases.md | RISK-016 | REG-065       | UI + API   |
| REQ-AUTH-009 | Password reset must require valid token            | authentication-scenarios.md | authentication-test-cases.md | RISK-046 | REG-066       | UI + API   |
| REQ-AUTH-010 | Expired reset token must fail                      | authentication-scenarios.md | authentication-test-cases.md | RISK-046 | REG-067       | API        |
| REQ-AUTH-011 | Used reset token must fail                         | authentication-scenarios.md | authentication-test-cases.md | RISK-046 | REG-068       | API        |
| REQ-AUTH-012 | Logout invalidates session                         | authentication-scenarios.md | authentication-test-cases.md | RISK-014 | REG-005       | UI + API   |
| REQ-AUTH-013 | Expired sessions cannot access protected resources | authentication-scenarios.md | authentication-test-cases.md | RISK-015 | REG-004       | UI + API   |
| REQ-AUTH-014 | Revoked sessions cannot access protected resources | authentication-scenarios.md | authentication-test-cases.md | RISK-014 | REG-072       | API        |

Coverage:

```text
COVERED
```

---

# 8. Customer Traceability

| Requirement  | Summary                                               | Scenario              | Test Cases             | Risk               | Regression             |
| ------------ | ----------------------------------------------------- | --------------------- | ---------------------- | ------------------ | ---------------------- |
| REQ-CUST-001 | Customer can view own profile                         | customer-scenarios.md | customer-test-cases.md | RISK-002           | REG-121                |
| REQ-CUST-002 | Customer cannot view another customer's profile       | customer-scenarios.md | customer-test-cases.md | RISK-002, RISK-047 | SEC-SUITE-026          |
| REQ-CUST-003 | Customer data updates must be validated               | customer-scenarios.md | customer-test-cases.md | RISK-037           | REG-122                |
| REQ-CUST-004 | Customer state must affect allowed functionality      | customer-scenarios.md | customer-test-cases.md | RISK-039           | State-transition suite |
| REQ-CUST-005 | Restricted customer rules must be enforced            | customer-scenarios.md | customer-test-cases.md | RISK-009           | REG-143                |
| REQ-CUST-006 | Suspended customer restrictions must be enforced      | customer-scenarios.md | customer-test-cases.md | RISK-009           | REG-144                |
| REQ-CUST-007 | Closed customer must not perform financial actions    | customer-scenarios.md | customer-test-cases.md | RISK-039           | Extended regression    |
| REQ-CUST-008 | KYC state must control applicable product eligibility | customer-scenarios.md | customer-test-cases.md | RISK-037           | REG-140                |

---

# 9. Account Traceability

| Requirement | Summary                                                   | Risk               | Regression         | Security      |
| ----------- | --------------------------------------------------------- | ------------------ | ------------------ | ------------- |
| REQ-ACC-001 | Customer can view owned accounts                          | RISK-002           | REG-011            | SEC-SUITE-015 |
| REQ-ACC-002 | Customer cannot view another customer's account           | RISK-002, RISK-047 | REG-006            | SEC-SUITE-015 |
| REQ-ACC-003 | Current balance must be accurate                          | RISK-001           | REG-012            | —             |
| REQ-ACC-004 | Available balance must be accurate                        | RISK-001           | REG-013            | —             |
| REQ-ACC-005 | Frozen accounts must reject prohibited transactions       | RISK-009           | REG-014            | SEC-SUITE-048 |
| REQ-ACC-006 | Closed accounts cannot transact                           | RISK-009           | REG-015            | SEC-SUITE-049 |
| REQ-ACC-007 | Account state transitions must follow allowed lifecycle   | RISK-039           | REG-202+           | —             |
| REQ-ACC-008 | Account closure requires applicable dependencies resolved | RISK-001           | Account test cases | —             |
| REQ-ACC-009 | Balance must reconcile with financial history             | RISK-001, RISK-019 | REG-164            | —             |
| REQ-ACC-010 | Concurrent operations must not corrupt account balance    | RISK-013           | REG-196, REG-197   | SEC-SUITE-059 |

---

# 10. Beneficiary Traceability

| Requirement | Summary                                                   | Risk     | Regression          |
| ----------- | --------------------------------------------------------- | -------- | ------------------- |
| REQ-BEN-001 | Customer can add valid beneficiary                        | RISK-017 | REG-075             |
| REQ-BEN-002 | Beneficiary must belong to authenticated customer         | RISK-047 | REG-081             |
| REQ-BEN-003 | Duplicate beneficiaries handled correctly                 | RISK-017 | REG-076             |
| REQ-BEN-004 | Beneficiary verification required where configured        | RISK-017 | REG-077             |
| REQ-BEN-005 | Activation cooldown enforced                              | RISK-017 | REG-078             |
| REQ-BEN-006 | Disabled beneficiary cannot be used                       | RISK-017 | REG-079             |
| REQ-BEN-007 | Deleted beneficiary cannot be used                        | RISK-017 | REG-080             |
| REQ-BEN-008 | Invalid/closed destination rejected                       | RISK-018 | Transfer regression |
| REQ-BEN-009 | Historical transactions remain after beneficiary deletion | RISK-019 | Extended regression |

---

# 11. Transfer Traceability

| Requirement | Summary                                                | Risk               | Regression       | Security      | Planned Automation |
| ----------- | ------------------------------------------------------ | ------------------ | ---------------- | ------------- | ------------------ |
| REQ-TRF-001 | Valid transfer completes successfully                  | RISK-001           | REG-016          | —             | UI + API           |
| REQ-TRF-002 | Source debit must be correct                           | RISK-001           | REG-016, REG-024 | —             | API + DB           |
| REQ-TRF-003 | Destination credit must be correct                     | RISK-001           | REG-016, REG-024 | —             | API + DB           |
| REQ-TRF-004 | Insufficient balance rejected                          | RISK-007           | REG-017          | SEC-SUITE-047 | API                |
| REQ-TRF-005 | Per-transfer limit enforced                            | RISK-008           | REG-018          | SEC-SUITE-046 | API                |
| REQ-TRF-006 | Daily limit enforced                                   | RISK-008           | REG-086          | SEC-SUITE-059 | API + DB           |
| REQ-TRF-007 | Monthly limit enforced                                 | RISK-008           | REG-087          | —             | API                |
| REQ-TRF-008 | Frozen source rejected                                 | RISK-009           | REG-020          | SEC-SUITE-048 | UI + API           |
| REQ-TRF-009 | Invalid beneficiary rejected                           | RISK-017           | REG-019          | SEC-SUITE-051 | API                |
| REQ-TRF-010 | Closed destination handled correctly                   | RISK-018           | Transfer cases   | —             | API                |
| REQ-TRF-011 | Duplicate transfer prevented                           | RISK-003           | REG-021          | SEC-SUITE-052 | UI + API           |
| REQ-TRF-012 | Retry must not create duplicate transfer               | RISK-003, RISK-042 | REG-022          | SEC-SUITE-057 | API                |
| REQ-TRF-013 | Transfer must be atomic                                | RISK-004           | REG-024          | —             | API + DB           |
| REQ-TRF-014 | Transfer status lifecycle valid                        | RISK-039           | REG-202+         | —             | API                |
| REQ-TRF-015 | Completed transfer can be reversed only where eligible | RISK-004           | REG-023          | SEC-SUITE-056 | API                |
| REQ-TRF-016 | Original transaction preserved after reversal          | RISK-019           | REG-023          | —             | API + DB           |
| REQ-TRF-017 | Scheduled transfer executes correctly                  | RISK-028           | REG-090          | —             | API                |
| REQ-TRF-018 | Cancelled scheduled transfer must never execute        | RISK-029           | REG-091          | SEC-SUITE-151 | API                |
| REQ-TRF-019 | Recurring transfer executes according to schedule      | RISK-028           | REG-092          | —             | API                |
| REQ-TRF-020 | Cancelled recurrence must stop future executions       | RISK-029           | REG-093          | —             | API                |
| REQ-TRF-021 | Transfer reference must be unique                      | RISK-033           | REG-163          | —             | DB                 |
| REQ-TRF-022 | Transfer must remain safe under concurrency            | RISK-013           | REG-195–197      | SEC-SUITE-059 | API + JMeter       |

---

# 12. Payment Traceability

| Requirement | Summary                                              | Risk     | Regression | Automation |
| ----------- | ---------------------------------------------------- | -------- | ---------- | ---------- |
| REQ-PAY-001 | Valid payment succeeds                               | RISK-001 | REG-025    | UI + API   |
| REQ-PAY-002 | Insufficient balance rejected                        | RISK-026 | REG-026    | API        |
| REQ-PAY-003 | Failed payment cannot change settled balance         | RISK-026 | REG-028    | API + DB   |
| REQ-PAY-004 | Duplicate bill payment prevented                     | RISK-027 | REG-027    | API        |
| REQ-PAY-005 | Payment status reflects provider outcome             | RISK-039 | REG-191    | API        |
| REQ-PAY-006 | Payment reversal handled correctly                   | RISK-004 | REG-098    | API + DB   |
| REQ-PAY-007 | Payment fees calculated correctly                    | RISK-010 | REG-099    | Jest + API |
| REQ-PAY-008 | Failed payment must not produce success notification | RISK-034 | REG-029    | API        |
| REQ-PAY-009 | Scheduled payment executes according to schedule     | RISK-028 | REG-096    | API        |
| REQ-PAY-010 | Recurring payment follows recurrence rules           | RISK-028 | REG-097    | API        |

---

# 13. Card Traceability

| Requirement  | Summary                                   | Risk     | Regression                | Security      |
| ------------ | ----------------------------------------- | -------- | ------------------------- | ------------- |
| REQ-CARD-001 | Valid card can be activated               | —        | REG-100                   | —             |
| REQ-CARD-002 | Active card can perform valid operations  | —        | REG-030                   | —             |
| REQ-CARD-003 | Card can be frozen                        | RISK-024 | REG-031                   | —             |
| REQ-CARD-004 | Frozen card cannot transact               | RISK-024 | REG-032                   | SEC-SUITE-148 |
| REQ-CARD-005 | Card can be unfrozen where permitted      | RISK-024 | REG-033                   | —             |
| REQ-CARD-006 | Blocked card cannot transact              | RISK-025 | REG-034                   | SEC-SUITE-148 |
| REQ-CARD-007 | Expired card cannot transact              | RISK-025 | REG-102                   | —             |
| REQ-CARD-008 | Cancelled card cannot transact            | RISK-025 | REG-103                   | —             |
| REQ-CARD-009 | Card limits enforced                      | RISK-008 | REG-035, REG-104, REG-105 | —             |
| REQ-CARD-010 | Card details must be appropriately masked | RISK-021 | CB-039                    | SEC-SUITE-069 |
| REQ-CARD-011 | Card refund correctly reconciles          | RISK-001 | REG-106                   | —             |
| REQ-CARD-012 | Card ownership enforced                   | RISK-047 | REG-009                   | SEC-SUITE-018 |

---

# 14. Loan Traceability

| Requirement  | Summary                                      | Risk               | Regression           | Automation    |
| ------------ | -------------------------------------------- | ------------------ | -------------------- | ------------- |
| REQ-LOAN-001 | Eligible customer can apply                  | RISK-011           | REG-036              | UI + API      |
| REQ-LOAN-002 | Loan amount limits enforced                  | RISK-011           | REG-107, REG-108     | API           |
| REQ-LOAN-003 | Loan term validation enforced                | RISK-011           | REG-109              | API           |
| REQ-LOAN-004 | KYC/eligibility rules enforced               | RISK-011           | REG-110              | API           |
| REQ-LOAN-005 | Authorized role can approve                  | RISK-023           | REG-037              | API           |
| REQ-LOAN-006 | Unauthorized role cannot approve             | RISK-023           | REG-038              | SEC-SUITE-030 |
| REQ-LOAN-007 | Rejected loan cannot disburse                | RISK-011           | REG-111              | SEC-SUITE-149 |
| REQ-LOAN-008 | Loan disbursement exactly once               | RISK-001, RISK-013 | REG-039, REG-198     | SEC-SUITE-054 |
| REQ-LOAN-009 | Repayment reduces outstanding amount         | RISK-011           | REG-040              | API + DB      |
| REQ-LOAN-010 | Final repayment closes loan correctly        | RISK-011           | REG-041              | API + DB      |
| REQ-LOAN-011 | Interest calculation accurate                | RISK-011           | REG-277              | Jest + API    |
| REQ-LOAN-012 | Installment schedule reconciles              | RISK-011           | Precision regression | Jest          |
| REQ-LOAN-013 | Concurrent decisions produce one final state | RISK-013           | REG-201              | API           |

---

# 15. Deposit Traceability

| Requirement | Summary                                     | Risk               | Regression          | Automation    |
| ----------- | ------------------------------------------- | ------------------ | ------------------- | ------------- |
| REQ-DEP-001 | Valid deposit can be opened                 | RISK-012           | REG-042             | UI + API      |
| REQ-DEP-002 | Insufficient funds prevents deposit funding | RISK-001           | REG-043             | API           |
| REQ-DEP-003 | Principal limits enforced                   | RISK-012           | REG-115, REG-116    | API           |
| REQ-DEP-004 | Deposit term validated                      | RISK-012           | REG-117             | API           |
| REQ-DEP-005 | Interest calculation correct                | RISK-012           | REG-278             | Jest          |
| REQ-DEP-006 | Maturity calculation correct                | RISK-012           | REG-044             | API + DB      |
| REQ-DEP-007 | Maturity payout occurs exactly once         | RISK-001, RISK-013 | REG-045, REG-199    | SEC-SUITE-055 |
| REQ-DEP-008 | Early withdrawal uses correct rules         | RISK-012           | REG-046, REG-118    | API           |
| REQ-DEP-009 | Auto-renew follows configuration            | RISK-012           | REG-119             | API           |
| REQ-DEP-010 | Renewal and payout must not both occur      | RISK-013           | REG-120             | API + DB      |
| REQ-DEP-011 | Closed deposit cannot pay again             | RISK-001           | Extended regression | SEC-SUITE-150 |

---

# 16. Transaction History Traceability

| Requirement | Summary                                                | Risk     | Regression             |
| ----------- | ------------------------------------------------------ | -------- | ---------------------- |
| REQ-TXN-001 | Customer sees own transaction history                  | RISK-002 | Transaction regression |
| REQ-TXN-002 | Other customer's history inaccessible                  | RISK-047 | REG-007                |
| REQ-TXN-003 | Completed transfer appears exactly once                | RISK-019 | REG-047                |
| REQ-TXN-004 | Payment history accurate                               | RISK-019 | REG-048                |
| REQ-TXN-005 | Failed transaction not represented as successful debit | RISK-019 | REG-050                |
| REQ-TXN-006 | Reversal linked to original                            | RISK-019 | REG-049                |
| REQ-TXN-007 | References unique                                      | RISK-033 | REG-163                |
| REQ-TXN-008 | Search/filter work correctly                           | —        | REG-220–224            |
| REQ-TXN-009 | Pagination has no missing/duplicate records            | —        | REG-225–229            |
| REQ-TXN-010 | Financial history immutable to customer                | RISK-019 | Security regression    |

---

# 17. Statement Traceability

| Requirement  | Summary                                 | Risk               | Regression           | Security      |
| ------------ | --------------------------------------- | ------------------ | -------------------- | ------------- |
| REQ-STMT-001 | Customer can generate own statement     | RISK-020           | REG-051              | —             |
| REQ-STMT-002 | Other customer's statement inaccessible | RISK-002, RISK-047 | REG-055              | SEC-SUITE-017 |
| REQ-STMT-003 | Opening balance correct                 | RISK-020           | REG-052              | —             |
| REQ-STMT-004 | Closing balance reconciles              | RISK-020           | REG-053              | —             |
| REQ-STMT-005 | Fees represented correctly              | RISK-010, RISK-020 | REG-054              | —             |
| REQ-STMT-006 | Transactions included exactly once      | RISK-019           | Statement regression | —             |
| REQ-STMT-007 | PDF/download protected by authorization | RISK-021           | CB-134               | SEC-SUITE-143 |
| REQ-STMT-008 | Historical statement remains immutable  | RISK-020           | Extended regression  | —             |

---

# 18. Notification Traceability

| Requirement | Summary                                                     | Risk     | Regression     |
| ----------- | ----------------------------------------------------------- | -------- | -------------- |
| REQ-NOT-001 | Successful transfer notification accurate                   | RISK-034 | REG-128        |
| REQ-NOT-002 | Failed payment notification accurate                        | RISK-034 | REG-129        |
| REQ-NOT-003 | Card state notification accurate                            | RISK-034 | REG-130        |
| REQ-NOT-004 | Loan decision notification accurate                         | RISK-034 | REG-131        |
| REQ-NOT-005 | Deposit maturity notification accurate                      | RISK-034 | REG-132        |
| REQ-NOT-006 | Password change security alert                              | RISK-021 | REG-133        |
| REQ-NOT-007 | Notification ownership enforced                             | RISK-002 | REG-134        |
| REQ-NOT-008 | User preferences respected                                  | —        | REG-135        |
| REQ-NOT-009 | Mandatory security alerts cannot be improperly suppressed   | RISK-021 | Security suite |
| REQ-NOT-010 | Notification delivery failure cannot alter financial result | RISK-034 | REG-192        |

---

# 19. Profile / Settings Traceability

| Requirement  | Summary                                              | Risk     | Regression    |
| ------------ | ---------------------------------------------------- | -------- | ------------- |
| REQ-PROF-001 | Customer can view own profile                        | RISK-002 | REG-121       |
| REQ-PROF-002 | Valid profile changes persist                        | RISK-039 | REG-122       |
| REQ-PROF-003 | Email change requires verification                   | RISK-021 | REG-123       |
| REQ-PROF-004 | Phone change requires verification                   | RISK-021 | REG-124       |
| REQ-PROF-005 | Password change securely updates credentials         | RISK-014 | REG-125       |
| REQ-PROF-006 | MFA setting changes require appropriate verification | RISK-005 | REG-126       |
| REQ-PROF-007 | Customer cannot modify protected fields              | RISK-006 | REG-127       |
| REQ-PROF-008 | Customer cannot modify role                          | RISK-006 | SEC-SUITE-033 |
| REQ-PROF-009 | Customer cannot modify KYC state                     | RISK-006 | SEC-SUITE-035 |

---

# 20. Security Traceability

| Requirement | Summary                                    | Primary Risk       | Security Suite    |
| ----------- | ------------------------------------------ | ------------------ | ----------------- |
| REQ-SEC-001 | Protected endpoints require authentication | RISK-030           | SEC-SUITE-097     |
| REQ-SEC-002 | Resource ownership enforced                | RISK-047           | SEC-SUITE-015–026 |
| REQ-SEC-003 | Role permissions enforced backend-side     | RISK-006           | SEC-SUITE-027–037 |
| REQ-SEC-004 | Client cannot override financial rules     | RISK-037           | SEC-SUITE-038–043 |
| REQ-SEC-005 | Replay cannot duplicate financial action   | RISK-003           | SEC-SUITE-052–058 |
| REQ-SEC-006 | Sessions expire/revoke correctly           | RISK-014, RISK-015 | SEC-SUITE-010–014 |
| REQ-SEC-007 | Sensitive credentials not exposed          | RISK-021           | SEC-SUITE-063–069 |
| REQ-SEC-008 | Inputs handled safely                      | RISK-021           | SEC-SUITE-091–096 |
| REQ-SEC-009 | Error messages do not leak protected data  | RISK-021           | SEC-SUITE-124–127 |
| REQ-SEC-010 | Protected downloads require authorization  | RISK-047           | SEC-SUITE-143–145 |
| REQ-SEC-011 | Authorization failures must fail closed    | RISK-030           | SEC-SUITE-161–164 |

---

# 21. Admin Traceability

| Requirement | Summary                                          | Risk     | Regression       |
| ----------- | ------------------------------------------------ | -------- | ---------------- |
| REQ-ADM-001 | Authorized admin can login                       | RISK-023 | REG-056          |
| REQ-ADM-002 | Roles determine admin permissions                | RISK-006 | REG-057          |
| REQ-ADM-003 | Admin can search customers                       | —        | REG-141          |
| REQ-ADM-004 | Authorized role can freeze account               | RISK-023 | REG-058          |
| REQ-ADM-005 | Unauthorized role cannot freeze account          | RISK-023 | REG-059          |
| REQ-ADM-006 | Authorized role can restrict/suspend customer    | RISK-023 | REG-143, REG-144 |
| REQ-ADM-007 | Authorized role can block card                   | RISK-023 | REG-146          |
| REQ-ADM-008 | Authorized loan role can approve/reject          | RISK-023 | REG-147          |
| REQ-ADM-009 | Authorized role can reverse eligible transaction | RISK-023 | REG-148          |
| REQ-ADM-010 | Read-only role cannot change state               | RISK-006 | REG-057          |
| REQ-ADM-011 | Admin high-risk actions require audit            | RISK-022 | REG-060          |

---

# 22. Audit Traceability

| Requirement | Summary                                           | Risk     | Regression     |
| ----------- | ------------------------------------------------- | -------- | -------------- |
| REQ-AUD-001 | Critical financial actions traceable              | RISK-022 | REG-061        |
| REQ-AUD-002 | Admin actor recorded correctly                    | RISK-022 | REG-062        |
| REQ-AUD-003 | Old/new state recorded where required             | RISK-022 | Security suite |
| REQ-AUD-004 | Audit does not expose secrets                     | RISK-032 | REG-063        |
| REQ-AUD-005 | Standard user cannot modify audit records         | RISK-022 | SEC-SUITE-131  |
| REQ-AUD-006 | Critical audit history cannot be silently deleted | RISK-022 | SEC-SUITE-132  |

---

# 23. Limits Traceability

| Requirement | Summary                                             | Risk               | Tests                       |
| ----------- | --------------------------------------------------- | ------------------ | --------------------------- |
| REQ-LIM-001 | Transfer per-transaction limit enforced             | RISK-008           | REG-018                     |
| REQ-LIM-002 | Daily transfer limit enforced                       | RISK-008           | REG-086                     |
| REQ-LIM-003 | Monthly transfer limit enforced                     | RISK-008           | REG-087                     |
| REQ-LIM-004 | Card purchase limit enforced                        | RISK-008           | REG-035                     |
| REQ-LIM-005 | Limit boundaries handled correctly                  | RISK-008           | boundary-value-analysis.md  |
| REQ-LIM-006 | Limits cannot be bypassed through API               | RISK-008, RISK-037 | SEC-SUITE-046               |
| REQ-LIM-007 | Concurrent requests cannot bypass cumulative limits | RISK-008, RISK-013 | REG-196 / concurrency tests |
| REQ-LIM-008 | Limit reset timing correct                          | RISK-043           | REG-270                     |

---

# 24. Fee Traceability

| Requirement | Summary                                      | Risk               | Test Coverage     |
| ----------- | -------------------------------------------- | ------------------ | ----------------- |
| REQ-FEE-001 | Applicable transfer fee calculated correctly | RISK-010           | REG-088           |
| REQ-FEE-002 | Fee included in available-balance validation | RISK-007, RISK-010 | SAN-015 / BVA     |
| REQ-FEE-003 | Fee recorded in history correctly            | RISK-019           | Transaction tests |
| REQ-FEE-004 | Fee included in statement reconciliation     | RISK-020           | REG-054           |
| REQ-FEE-005 | Client cannot manipulate authoritative fee   | RISK-037           | SEC-SUITE-041     |
| REQ-FEE-006 | Rounding rules applied consistently          | RISK-010           | REG-276           |

---

# 25. Non-Functional Traceability

| Requirement | Summary                                                     | Risk         | Coverage                 |
| ----------- | ----------------------------------------------------------- | ------------ | ------------------------ |
| REQ-NFR-001 | Supported browsers function correctly                       | RISK-035     | cross-browser-suite.md   |
| REQ-NFR-002 | Critical workflows usable at supported viewports            | RISK-036     | cross-browser-suite.md   |
| REQ-NFR-003 | Application handles expected data volume                    | RISK-040     | Large dataset regression |
| REQ-NFR-004 | Transaction response remains acceptable under expected load | RISK-040     | Planned JMeter           |
| REQ-NFR-005 | System remains stable under traffic spike                   | RISK-041     | Planned JMeter           |
| REQ-NFR-006 | System handles long-duration workload                       | RISK-040     | Planned endurance test   |
| REQ-NFR-007 | Financial retries remain idempotent under latency           | RISK-042     | JMeter + API             |
| REQ-NFR-008 | Critical flows are keyboard accessible                      | —            | Accessibility regression |
| REQ-NFR-009 | Timezone/date behavior consistent                           | RISK-043     | REG-270–275              |
| REQ-NFR-010 | Monetary precision consistent                               | RISK-010–012 | REG-276–279              |

---

# 26. Test Design Technique Traceability

| Requirement Area | BVA | EP | Decision Table | State Transition | Pairwise |
| ---------------- | --: | -: | -------------: | ---------------: | -------: |
| Authentication   |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Accounts         |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Beneficiaries    |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Transfers        |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Payments         |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Cards            |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Loans            |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Deposits         |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Transactions     |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Statements       |   ✓ |  ✓ |              ✓ |                — |        ✓ |
| Notifications    |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |
| Admin            |   ✓ |  ✓ |              ✓ |                ✓ |        ✓ |

---

# 27. Exploratory Testing Traceability

High-risk requirements also map to exploratory charters.

| Requirement Area    | Charter Examples |
| ------------------- | ---------------- |
| Authentication      | ETC-001–004      |
| Sessions            | ETC-005–006      |
| Accounts            | ETC-009–010      |
| Beneficiaries       | ETC-011–012      |
| Transfers           | ETC-013–019      |
| Payments            | ETC-020–022      |
| Cards               | ETC-023–026      |
| Loans               | ETC-027–031      |
| Deposits            | ETC-032–035      |
| Transactions        | ETC-036–038      |
| Statements          | ETC-039–041      |
| Notifications       | ETC-042–044      |
| Security            | ETC-045–049      |
| Admin               | ETC-050–053      |
| Concurrency         | ETC-054–056      |
| Error Recovery      | ETC-057–059      |
| Browser/Responsive  | ETC-060–061      |
| Financial Integrity | ETC-068          |
| Precision           | ETC-070          |
| Customer Isolation  | ETC-074          |
| Role Isolation      | ETC-075          |

---

# 28. Smoke Suite Traceability

Critical requirements covered by smoke testing include:

```text
REQ-AUTH-001
REQ-AUTH-003
REQ-AUTH-012

REQ-ACC-001
REQ-ACC-003
REQ-ACC-005

REQ-TRF-001
REQ-TRF-004
REQ-TRF-005
REQ-TRF-008
REQ-TRF-011
REQ-TRF-013

REQ-PAY-001
REQ-PAY-002
REQ-PAY-003

REQ-CARD-003
REQ-CARD-004
REQ-CARD-006

REQ-LOAN-005
REQ-LOAN-008

REQ-DEP-001
REQ-DEP-007

REQ-STMT-001
REQ-STMT-004

REQ-SEC-001
REQ-SEC-002
REQ-SEC-003

REQ-ADM-004
REQ-AUD-002
```

---

# 29. Security Suite Traceability

Security suite strongly covers:

```text
REQ-AUTH-002 through REQ-AUTH-014

REQ-ACC-002
REQ-ACC-005
REQ-ACC-006

REQ-BEN-002
REQ-BEN-006
REQ-BEN-007

REQ-TRF-004 through REQ-TRF-013

REQ-CARD-004
REQ-CARD-006
REQ-CARD-010
REQ-CARD-012

REQ-LOAN-006
REQ-LOAN-007
REQ-LOAN-008

REQ-DEP-007
REQ-DEP-011

REQ-STMT-002
REQ-STMT-007

REQ-PROF-007 through REQ-PROF-009

REQ-SEC-001 through REQ-SEC-011

REQ-ADM-002
REQ-ADM-005
REQ-ADM-010

REQ-AUD-004 through REQ-AUD-006
```

---

# 30. UI Automation Traceability

## Selenium Java

Planned primary coverage:

```text
Login
Accounts
Transfers
Payments
Cards
Transactions
Statements
Admin
```

Mapped requirements include:

```text
REQ-AUTH-001
REQ-ACC-001
REQ-TRF-001
REQ-PAY-001
REQ-CARD-003
REQ-TXN-001
REQ-STMT-001
REQ-ADM-003
```

---

# 31. Playwright TypeScript Traceability

Strong candidates:

```text
Authentication
Multi-session tests
Transfers
Duplicate submission
Payments
Cards
Statements
Cross-browser
Responsive
Security state
```

Mapped to:

```text
REQ-AUTH-001–014
REQ-TRF-001–022
REQ-PAY-001–010
REQ-CARD-001–012
REQ-STMT-001–008
REQ-SEC-001–011
```

---

# 32. Cypress TypeScript Traceability

Planned coverage:

```text
Frontend workflows
Network validation
Transfers
Payments
Cards
Profile
Notifications
```

---

# 33. Jest Traceability

Jest will focus on logic-level validation.

Candidates:

```text
Fee calculations
Loan calculations
Deposit calculations
Limit functions
Validation functions
Permission helpers
Financial rounding
```

Relevant requirements:

```text
REQ-FEE-001
REQ-FEE-006
REQ-LOAN-011
REQ-LOAN-012
REQ-DEP-005
REQ-LIM-001–008
```

---

# 34. Postman Traceability

Postman collection should cover:

```text
Authentication
Accounts
Beneficiaries
Transfers
Payments
Cards
Loans
Deposits
Transactions
Statements
Admin
```

Especially:

```text
Positive requests
Negative requests
Authorization
Request chaining
Environment variables
Schema validation
Idempotency
```

---

# 35. REST Assured Traceability

REST Assured is planned as the primary automated API regression framework.

High-value mappings:

```text
REQ-AUTH-002–014
REQ-ACC-002–010
REQ-BEN-002–009
REQ-TRF-001–022
REQ-PAY-001–010
REQ-CARD-004–012
REQ-LOAN-005–013
REQ-DEP-006–011
REQ-STMT-002–008
REQ-SEC-001–011
REQ-ADM-002–011
```

---

# 36. Database Testing Traceability

Database validation is especially important for:

```text
REQ-ACC-003
REQ-ACC-004
REQ-ACC-009

REQ-TRF-002
REQ-TRF-003
REQ-TRF-011
REQ-TRF-013
REQ-TRF-016
REQ-TRF-021

REQ-PAY-003
REQ-PAY-004
REQ-PAY-006

REQ-LOAN-008
REQ-LOAN-009
REQ-LOAN-010

REQ-DEP-006
REQ-DEP-007
REQ-DEP-010

REQ-TXN-003–010

REQ-STMT-003–006

REQ-AUD-001–006
```

Database testing should validate:

* Referential integrity
* Uniqueness
* Ownership
* Transaction status
* Balance consistency
* Duplicate prevention
* Audit relationships

---

# 37. JMeter Traceability

Performance and concurrency coverage will focus on:

```text
REQ-TRF-022
REQ-LIM-007

REQ-NFR-003
REQ-NFR-004
REQ-NFR-005
REQ-NFR-006
REQ-NFR-007
```

Planned workloads:

* Transfers
* Payments
* Authentication
* Account retrieval
* Transaction history
* Mixed workload
* Spike
* Stress
* Endurance
* Financial concurrency

---

# 38. Cucumber / BDD Traceability

BDD scenarios should cover business-readable workflows including:

```text
Authentication

Transfer authorization

Transfer limits

Card state

Loan eligibility

Deposit lifecycle

Admin permissions

Financial reconciliation
```

Examples already appear in:

* decision-tables.md
* boundary-value-analysis.md
* pairwise-testing.md
* state-transition-testing.md

---

# 39. CI/CD Traceability

Critical requirements will later be mapped into CI stages.

Example:

```text
Pull Request
    ↓
Jest / Unit
    ↓
API Checks
    ↓
UI Smoke
    ↓
Security Gate
    ↓
Regression Tier 1
```

Tools:

```text
GitHub Actions
Jenkins
```

Critical CI requirements should include:

```text
REQ-AUTH-001
REQ-ACC-003
REQ-TRF-001
REQ-TRF-004
REQ-TRF-011
REQ-PAY-001
REQ-CARD-004
REQ-SEC-001
REQ-SEC-002
REQ-SEC-003
```

---

# 40. Risk-to-Requirement Traceability

## RISK-001 — Incorrect Balance

Mapped requirements:

```text
REQ-ACC-003
REQ-ACC-004
REQ-ACC-009
REQ-TRF-002
REQ-TRF-003
REQ-PAY-003
REQ-LOAN-008
REQ-DEP-007
REQ-STMT-004
```

---

## RISK-002 — Unauthorized Customer Data

Mapped:

```text
REQ-ACC-002
REQ-TXN-002
REQ-STMT-002
REQ-NOT-007
REQ-SEC-002
```

---

## RISK-003 — Duplicate Transaction

Mapped:

```text
REQ-TRF-011
REQ-TRF-012
REQ-PAY-004
REQ-LOAN-008
REQ-DEP-007
REQ-SEC-005
```

---

## RISK-005 — Authentication Bypass

Mapped:

```text
REQ-AUTH-002
REQ-AUTH-003
REQ-AUTH-004
REQ-AUTH-005
REQ-AUTH-006
REQ-SEC-001
```

---

## RISK-006 — Privilege Escalation

Mapped:

```text
REQ-PROF-007
REQ-PROF-008
REQ-PROF-009
REQ-ADM-002
REQ-ADM-010
REQ-SEC-003
```

---

## RISK-013 — Concurrency Corruption

Mapped:

```text
REQ-ACC-010
REQ-TRF-022
REQ-LOAN-008
REQ-LOAN-013
REQ-DEP-007
REQ-DEP-010
REQ-LIM-007
```

---

## RISK-047 — IDOR

Mapped:

```text
REQ-ACC-002
REQ-BEN-002
REQ-CARD-012
REQ-TXN-002
REQ-STMT-002
REQ-NOT-007
REQ-SEC-002
REQ-SEC-010
```

---

# 41. Requirement-to-Defect Examples

Sample mappings:

| Requirement                                   | Defect  |
| --------------------------------------------- | ------- |
| REQ-TRF-011 Duplicate prevention              | BUG-001 |
| REQ-TRF-004 Balance validation                | BUG-002 |
| REQ-STMT-002 Statement authorization          | BUG-003 |
| REQ-AUTH-003 MFA required                     | BUG-004 |
| REQ-ACC-005 Frozen account enforcement        | BUG-005 |
| REQ-PAY-003 Failed payment balance neutrality | BUG-006 |
| REQ-CARD-004 Frozen card enforcement          | BUG-007 |
| REQ-LOAN-008 Disbursement exactly once        | BUG-008 |
| REQ-DEP-007 Maturity payout exactly once      | BUG-009 |
| REQ-STMT-004 Closing balance                  | BUG-010 |
| REQ-NOT-002 Failure notification              | BUG-011 |
| REQ-ADM-010 Read-only restrictions            | BUG-012 |
| REQ-AUTH-012 Session invalidation             | BUG-013 |
| REQ-TRF-016 Preserve original on reversal     | BUG-014 |
| REQ-TRF-012 Retry idempotency                 | BUG-015 |
| REQ-TRF-018 Cancelled schedule                | BUG-016 |
| REQ-PROF-005 Password change                  | BUG-017 |
| REQ-PROF-009 Protected KYC status             | BUG-018 |
| REQ-TRF-021 Unique transaction reference      | BUG-020 |

These are sample defect mappings until defects are reproduced against the implemented Banking System.

---

# 42. Coverage Completeness Checklist

For every requirement verify:

```text
Requirement defined?

Business rule identified?

Risk mapped?

Scenario exists?

Detailed test case exists/planned?

Positive case covered?

Negative case covered?

Boundary covered where relevant?

Authorization covered?

API covered?

Database covered where relevant?

Regression case exists?

Automation planned?

Execution result available?

Defect linked if failed?
```

---

# 43. Coverage Gaps

A requirement should be flagged when:

```text
No test exists

Only happy path exists

No negative coverage

No authorization coverage

Financial state is not validated

Database state is not checked where needed

Critical requirement has no regression case

High-value repetitive requirement has no automation plan
```

Status:

```text
PARTIAL
```

or:

```text
NOT_COVERED
```

---

# 44. Traceability Review Before Release

Before release, review especially:

```text
P0 requirements

Critical risks

Financial rules

Authentication

Authorization

Balances

Transfers

Payments

Cards

Loan disbursement

Deposit payout

Statements

Admin permissions

Audit
```

All critical requirements should have:

```text
Test Coverage
+
Execution Result
+
Known Defect Status
```

---

# 45. Requirement Coverage Metrics

Useful metrics:

```text
Total requirements

Requirements covered

Requirements partially covered

Requirements not covered

P0 requirements covered

Requirements automated

Requirements with API tests

Requirements with database tests

Requirements with open defects
```

---

# 46. Example Coverage Calculation

Formula:

```text
Requirement Coverage %
=
Covered Requirements
/
Total Testable Requirements
× 100
```

Example:

```text
120 covered
/
125 total
× 100
=
96%
```

Coverage percentage alone must not determine release readiness.

Five uncovered P0 requirements could still make a 96% coverage result unacceptable.

---

# 47. Risk Coverage Metric

Formula:

```text
Critical Risk Coverage %
=
Critical Risks With Tests
/
Total Critical Risks
× 100
```

For this project, the target should be:

```text
100% of identified Critical risks have planned test coverage.
```

Execution status will later determine whether those risks were actually validated.

---

# 48. Automation Coverage Metric

Possible metric:

```text
Automated Regression Coverage %
=
Automated Regression Cases
/
Automation-Eligible Regression Cases
× 100
```

Do not calculate against all manual tests because many exploratory/usability cases are intentionally manual.

---

# 49. Requirements Change Management

When a requirement changes:

```text
Requirement Change
    ↓
Update Business Rule
    ↓
Review Risk
    ↓
Update Test Scenario
    ↓
Update Test Case
    ↓
Update RTM
    ↓
Update Regression
    ↓
Update Automation
```

This prevents outdated tests.

---

# 50. Defect Impact Analysis Using RTM

When a defect is discovered:

Example:

```text
BUG-001
Duplicate Transfer
```

Trace to:

```text
REQ-TRF-011
REQ-TRF-012
REQ-TRF-013
REQ-ACC-003
REQ-TXN-003
REQ-STMT-004
```

This identifies the regression surface.

---

# 51. Example Change Impact Analysis

Change:

```text
Transfer fee calculation updated.
```

RTM shows related requirements:

```text
REQ-FEE-001
REQ-FEE-002
REQ-FEE-003
REQ-FEE-004
REQ-FEE-006
REQ-TRF-002
REQ-TRF-004
REQ-ACC-003
REQ-STMT-004
```

Therefore regression should include:

* Transfer
* Balance
* Fee
* History
* Statement
* Precision

---

# 52. Release Traceability Requirement

A release candidate should eventually provide a chain such as:

```text
REQ-TRF-011
Duplicate Transfer Prevention
        ↓
Transfer Scenario
        ↓
Detailed Transfer Test Case
        ↓
REG-021
        ↓
REST Assured Test
        ↓
Playwright Test
        ↓
Regression Run
        ↓
PASS
```

This represents complete end-to-end traceability.

---

# 53. RTM Maintenance Rules

Update this document when:

* Requirement added
* Requirement changed
* Requirement removed
* Risk changes
* Test case created
* Regression case added
* Automation implemented
* Defect discovered
* Release execution completed

The RTM should evolve with the system.

---

# 54. RTM Ownership

QA owns maintenance of testing traceability.

Inputs may come from:

* Product requirements
* Business rules
* Development/API specifications
* Security requirements
* Architecture
* Defect history
* Production incidents
* Risk assessment

---

# 55. High-Risk Coverage Summary

The most important requirement groups requiring full traceability are:

```text
Authentication

Authorization

Customer isolation

Account balances

Transfer atomicity

Transfer idempotency

Payment integrity

Card state enforcement

Loan disbursement

Deposit maturity

Transaction history

Statement reconciliation

Admin authorization

Audit integrity

Concurrency
```

---

# 56. Current Project Traceability Status

| Area           | Manual Scenarios | Test Design |   Smoke | Regression | Security | Automation |
| -------------- | ---------------: | ----------: | ------: | ---------: | -------: | ---------: |
| Authentication |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Customers      |              Yes |         Yes | Partial |        Yes |      Yes |    Planned |
| Accounts       |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Beneficiaries  |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Transfers      |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Payments       |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Cards          |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Loans          |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Deposits       |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Transactions   |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Statements     |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Notifications  |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Profile        |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Security       |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Admin          |              Yes |         Yes |     Yes |        Yes |      Yes |    Planned |
| Database       |          Planned |     Partial | Partial |        Yes |  Partial |    Planned |
| Performance    |          Planned |     Partial |       — |    Partial |  Partial |    Planned |

---

# 57. Traceability Gaps Remaining

At the current manual-testing stage, major remaining items include:

```text
Detailed test-case documents

Actual execution results

Actual defect linkage

SQL/database test implementation

API automation

UI automation

Performance scripts

BDD implementation

CI/CD integration
```

These will be linked into the RTM as later project phases are completed.

---

# 58. Final Traceability Principle

The RTM should answer:

```text
What requirement are we testing?

Why is it important?

What risk does it address?

Where is it tested?

Has it been executed?

Did it pass?

If it failed, what defect was created?

Is it covered by regression?

Should it be automated?
```

For high-risk banking requirements, the traceability chain should eventually include:

```text
Requirement
+
Business Rule
+
Risk
+
Manual Test
+
API Test
+
Database Validation
+
Regression
+
Automation
+
Execution Result
```

The core rule is:

```text
No critical banking requirement should exist
without visible, traceable test coverage.
```
