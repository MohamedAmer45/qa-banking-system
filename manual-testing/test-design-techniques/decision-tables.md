# Banking System — Decision Tables

## 1. Document Information

| Field                 | Value                          |
| --------------------- | ------------------------------ |
| Project               | Banking System Testing Project |
| Test Design Technique | Decision Table Testing         |
| Document              | Test Design                    |
| Version               | 1.0                            |
| Status                | Draft                          |
| Owner                 | QA Engineering                 |

---

# 2. Purpose

This document applies **Decision Table Testing** to complex Banking System business rules.

Decision tables are useful when system behavior depends on several conditions at the same time.

Banking examples include:

* Transfer authorization
* Balance validation
* Account status
* Beneficiary status
* KYC status
* Transfer limits
* Card eligibility
* Loan decisions
* Account closure
* Deposit withdrawal
* Administrative permissions

A decision table helps verify that combinations of conditions produce the correct action.

---

# 3. Decision Table Structure

A typical decision table contains:

```text
Conditions
↓
Rules / combinations
↓
Expected actions
```

Example:

| Condition / Action  | R1 | R2 |
| ------------------- | -: | -: |
| Account active?     |  Y |  N |
| Sufficient balance? |  Y |  Y |
| Transfer allowed?   |  Y |  N |

Each column represents one rule or combination.

---

# 4. Notation

Use:

```text
Y = Yes / True
N = No / False
- = Condition does not matter
```

Actions use:

```text
X = Perform action
- = Do not perform action
```

---

# 5. Scenario Naming Convention

Decision-table rules use:

```text
DT-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 6. Transfer Authorization Decision Table

Conditions:

* Customer authenticated
* Customer active
* Source account owned
* Source account active
* Beneficiary valid and active
* Sufficient available balance
* Amount within transfer limit

| Condition / Action   | R1 | R2 | R3 | R4 | R5 | R6 | R7 |
| -------------------- | -: | -: | -: | -: | -: | -: | -: |
| Authenticated?       |  Y |  N |  Y |  Y |  Y |  Y |  Y |
| Customer active?     |  Y |  - |  N |  Y |  Y |  Y |  Y |
| Own source account?  |  Y |  - |  - |  N |  Y |  Y |  Y |
| Source active?       |  Y |  - |  - |  - |  N |  Y |  Y |
| Beneficiary active?  |  Y |  - |  - |  - |  - |  N |  Y |
| Sufficient balance?  |  Y |  - |  - |  - |  - |  - |  N |
| Within limit?        |  Y |  - |  - |  - |  - |  - |  Y |
| **Execute transfer** |  X |  - |  - |  - |  - |  - |  - |
| **Reject transfer**  |  - |  X |  X |  X |  X |  X |  X |

Additional rule:

| Condition / Action  | R8 |
| ------------------- | -: |
| Authenticated?      |  Y |
| Customer active?    |  Y |
| Own source account? |  Y |
| Source active?      |  Y |
| Beneficiary active? |  Y |
| Sufficient balance? |  Y |
| Within limit?       |  N |
| Execute transfer    |  - |
| Reject transfer     |  X |

IDs:

```text
DT-001 to DT-008
```

**Priority:** P0

---

# 7. Transfer Balance + Fee Decision Table

Conditions:

* Transfer amount <= available balance
* Fee applicable
* Amount + fee <= available balance

| Condition / Action          | R1 | R2 | R3 | R4 |
| --------------------------- | -: | -: | -: | -: |
| Transfer amount <= balance? |  Y |  Y |  Y |  N |
| Fee applies?                |  N |  Y |  Y |  - |
| Amount + fee <= balance?    |  - |  Y |  N |  - |
| Accept transfer             |  X |  X |  - |  - |
| Reject insufficient funds   |  - |  - |  X |  X |

IDs:

```text
DT-009 to DT-012
```

This table prevents a common defect where the system checks only:

```text
Transfer Amount <= Balance
```

instead of:

```text
Transfer Amount + Fee <= Available Balance
```

---

# 8. Transfer Limit Decision Table

Conditions:

* Amount within per-transfer limit
* Daily cumulative amount within daily limit
* Monthly cumulative amount within monthly limit

| Condition / Action        | R1 | R2 | R3 | R4 |
| ------------------------- | -: | -: | -: | -: |
| Per-transfer limit valid? |  Y |  N |  Y |  Y |
| Daily limit valid?        |  Y |  - |  N |  Y |
| Monthly limit valid?      |  Y |  - |  - |  N |
| Allow                     |  X |  - |  - |  - |
| Reject per-transfer limit |  - |  X |  - |  - |
| Reject daily limit        |  - |  - |  X |  - |
| Reject monthly limit      |  - |  - |  - |  X |

IDs:

```text
DT-013 to DT-016
```

**Priority:** P0

---

# 9. Beneficiary Transfer Decision Table

Conditions:

* Beneficiary exists
* Beneficiary belongs to customer
* Beneficiary verified
* Activation cooldown completed
* Beneficiary active

| Condition / Action | R1 | R2 | R3 | R4 | R5 |
| ------------------ | -: | -: | -: | -: | -: |
| Exists?            |  Y |  N |  Y |  Y |  Y |
| Owned by customer? |  Y |  - |  N |  Y |  Y |
| Verified?          |  Y |  - |  - |  N |  Y |
| Cooldown complete? |  Y |  - |  - |  - |  N |
| Active?            |  Y |  - |  - |  - |  Y |
| Allow transfer     |  X |  - |  - |  - |  - |
| Reject             |  - |  X |  X |  X |  X |

Additional inactive rule:

| Condition / Action | R6 |
| ------------------ | -: |
| Exists?            |  Y |
| Owned by customer? |  Y |
| Verified?          |  Y |
| Cooldown complete? |  Y |
| Active?            |  N |
| Allow transfer     |  - |
| Reject             |  X |

IDs:

```text
DT-017 to DT-022
```

---

# 10. Account Status Transaction Decision Table

Possible account states:

```text
ACTIVE
FROZEN
RESTRICTED
SUSPENDED
CLOSED
```

Operations:

* View
* Receive funds
* Transfer
* Payment
* Card transaction

Example policy:

| Account State |            View |     Receive |    Transfer |     Payment | Card Transaction |
| ------------- | --------------: | ----------: | ----------: | ----------: | ---------------: |
| ACTIVE        |               Y |           Y |           Y |           Y |                Y |
| FROZEN        |               Y | Conditional |           N |           N |                N |
| RESTRICTED    |               Y | Conditional | Conditional | Conditional |      Conditional |
| SUSPENDED     |               Y | Conditional |           N |           N |                N |
| CLOSED        | Historical only |           N |           N |           N |                N |

IDs:

```text
DT-023 to DT-027
```

Actual behavior must match final business rules.

---

# 11. Customer Status Decision Table

| Customer Status |       Login |             View Data | Financial Actions | Profile Changes |
| --------------- | ----------: | --------------------: | ----------------: | --------------: |
| ACTIVE          |           Y |                     Y |                 Y |               Y |
| RESTRICTED      |           Y |                     Y |       Conditional |     Conditional |
| SUSPENDED       | Conditional |           Conditional |                 N |     Conditional |
| DISABLED        |           N |                     N |                 N |               N |
| CLOSED          |           N | Historical/controlled |                 N |               N |

IDs:

```text
DT-028 to DT-032
```

**Priority:** P0

---

# 12. KYC Decision Table

Conditions:

* Customer active
* KYC verified
* Product requires KYC

| Condition / Action    | R1 | R2 | R3 | R4 |
| --------------------- | -: | -: | -: | -: |
| Customer active?      |  Y |  Y |  Y |  N |
| KYC verified?         |  Y |  N |  N |  - |
| Product requires KYC? |  Y |  Y |  N |  - |
| Allow product         |  X |  - |  X |  - |
| Reject/restrict       |  - |  X |  - |  X |

IDs:

```text
DT-033 to DT-036
```

---

# 13. Login Decision Table

Conditions:

* Username exists
* Password correct
* Account locked
* Account enabled
* MFA enabled

| Condition / Action | R1 | R2 | R3 | R4 | R5 |
| ------------------ | -: | -: | -: | -: | -: |
| User exists?       |  Y |  N |  Y |  Y |  Y |
| Password correct?  |  Y |  - |  N |  Y |  Y |
| Locked?            |  N |  - |  - |  Y |  N |
| Enabled?           |  Y |  - |  - |  - |  N |
| MFA enabled?       |  N |  - |  - |  - |  - |
| Login success      |  X |  - |  - |  - |  - |
| Reject credentials |  - |  X |  X |  - |  - |
| Reject locked      |  - |  - |  - |  X |  - |
| Reject disabled    |  - |  - |  - |  - |  X |

MFA path:

| Condition / Action  | R6 |
| ------------------- | -: |
| User exists?        |  Y |
| Password correct?   |  Y |
| Locked?             |  N |
| Enabled?            |  Y |
| MFA enabled?        |  Y |
| Start MFA challenge |  X |

IDs:

```text
DT-037 to DT-042
```

---

# 14. MFA Verification Decision Table

Conditions:

* OTP present
* OTP correct
* OTP not expired
* OTP unused
* Attempt count below threshold

| Condition / Action | R1 | R2 | R3 | R4 | R5 |
| ------------------ | -: | -: | -: | -: | -: |
| OTP present?       |  Y |  N |  Y |  Y |  Y |
| Correct?           |  Y |  - |  N |  Y |  Y |
| Not expired?       |  Y |  - |  - |  N |  Y |
| Unused?            |  Y |  - |  - |  - |  N |
| Attempts allowed?  |  Y |  - |  Y |  Y |  Y |
| Authenticate       |  X |  - |  - |  - |  - |
| Reject             |  - |  X |  X |  X |  X |

Rate-limit rule:

| Condition / Action | R6 |
| ------------------ | -: |
| Attempts allowed?  |  N |
| Authenticate       |  - |
| Block/throttle     |  X |

IDs:

```text
DT-043 to DT-048
```

---

# 15. Password Reset Decision Table

Conditions:

* Reset token exists
* Token valid
* Token unexpired
* Token unused
* New password valid

| Condition / Action | R1 | R2 | R3 | R4 | R5 |
| ------------------ | -: | -: | -: | -: | -: |
| Token exists?      |  Y |  N |  Y |  Y |  Y |
| Valid token?       |  Y |  - |  N |  Y |  Y |
| Unexpired?         |  Y |  - |  - |  N |  Y |
| Unused?            |  Y |  - |  - |  - |  N |
| Password valid?    |  Y |  - |  - |  - |  - |
| Reset password     |  X |  - |  - |  - |  - |
| Reject             |  - |  X |  X |  X |  X |

Additional password invalid rule:

| Condition / Action            | R6 |
| ----------------------------- | -: |
| Token valid/unexpired/unused? |  Y |
| Password valid?               |  N |
| Reset password                |  - |
| Reject                        |  X |

IDs:

```text
DT-049 to DT-054
```

---

# 16. Payment Decision Table

Conditions:

* Account active
* Bill valid
* Bill unpaid
* Payee active
* Sufficient balance
* Payment within limit

| Condition / Action | R1 | R2 | R3 | R4 | R5 | R6 |
| ------------------ | -: | -: | -: | -: | -: | -: |
| Account active?    |  Y |  N |  Y |  Y |  Y |  Y |
| Bill valid?        |  Y |  - |  N |  Y |  Y |  Y |
| Bill unpaid?       |  Y |  - |  - |  N |  Y |  Y |
| Payee active?      |  Y |  - |  - |  - |  N |  Y |
| Sufficient funds?  |  Y |  - |  - |  - |  - |  N |
| Within limit?      |  Y |  - |  - |  - |  - |  Y |
| Process payment    |  X |  - |  - |  - |  - |  - |
| Reject payment     |  - |  X |  X |  X |  X |  X |

Additional limit rule:

| Condition / Action            | R7 |
| ----------------------------- | -: |
| All previous conditions valid |  Y |
| Within limit?                 |  N |
| Process                       |  - |
| Reject                        |  X |

IDs:

```text
DT-055 to DT-061
```

---

# 17. Duplicate Bill Payment Decision Table

Conditions:

* Bill exists
* Bill unique
* Bill already paid
* Current payment already processing

| Condition / Action            | R1 | R2 | R3 | R4 |
| ----------------------------- | -: | -: | -: | -: |
| Bill exists?                  |  Y |  N |  Y |  Y |
| Bill already paid?            |  N |  - |  Y |  N |
| Payment currently processing? |  N |  - |  - |  Y |
| Process payment               |  X |  - |  - |  - |
| Reject invalid bill           |  - |  X |  - |  - |
| Reject duplicate paid bill    |  - |  - |  X |  - |
| Prevent concurrent duplicate  |  - |  - |  - |  X |

IDs:

```text
DT-062 to DT-065
```

---

# 18. Card Transaction Decision Table

Conditions:

* Card active
* Card unexpired
* Linked account active
* Sufficient balance
* Within card limit

| Condition / Action  | R1 | R2 | R3 | R4 | R5 |
| ------------------- | -: | -: | -: | -: | -: |
| Card active?        |  Y |  N |  Y |  Y |  Y |
| Not expired?        |  Y |  - |  N |  Y |  Y |
| Account active?     |  Y |  - |  - |  N |  Y |
| Sufficient funds?   |  Y |  - |  - |  - |  N |
| Within limit?       |  Y |  - |  - |  - |  Y |
| Approve transaction |  X |  - |  - |  - |  - |
| Decline             |  - |  X |  X |  X |  X |

Limit-exceeded rule:

| Condition / Action         | R6 |
| -------------------------- | -: |
| All other conditions valid |  Y |
| Within limit?              |  N |
| Approve                    |  - |
| Decline                    |  X |

IDs:

```text
DT-066 to DT-071
```

---

# 19. Card Lifecycle Decision Table

| Current State |     Activate |       Freeze | Unfreeze |        Block |      Cancel |
| ------------- | -----------: | -----------: | -------: | -----------: | ----------: |
| INACTIVE      |            Y |            N |        N |            Y |           Y |
| ACTIVE        | N/Idempotent |            Y |        N |            Y |           Y |
| FROZEN        |            N | N/Idempotent |        Y |            Y |           Y |
| BLOCKED       |            N |            N |        N | N/Idempotent | Conditional |
| EXPIRED       |            N |            N |        N |            N | Conditional |
| CANCELLED     |            N |            N |        N |            N |           N |

IDs:

```text
DT-072 to DT-077
```

---

# 20. Loan Eligibility Decision Table

Conditions:

* Customer active
* KYC verified
* Minimum income met
* Debt/risk condition acceptable
* Requested amount within limits

| Condition / Action    | R1 | R2 | R3 | R4 | R5 |
| --------------------- | -: | -: | -: | -: | -: |
| Customer active?      |  Y |  N |  Y |  Y |  Y |
| KYC verified?         |  Y |  - |  N |  Y |  Y |
| Income sufficient?    |  Y |  - |  - |  N |  Y |
| Risk/debt acceptable? |  Y |  - |  - |  - |  N |
| Amount valid?         |  Y |  - |  - |  - |  Y |
| Eligible              |  X |  - |  - |  - |  - |
| Reject                |  - |  X |  X |  X |  X |

Additional invalid amount:

| Condition / Action               | R6 |
| -------------------------------- | -: |
| All eligibility conditions valid |  Y |
| Amount valid?                    |  N |
| Eligible                         |  - |
| Reject                           |  X |

IDs:

```text
DT-078 to DT-083
```

---

# 21. Loan Approval Decision Table

Conditions:

* Application submitted
* Eligibility passed
* Reviewer authorized
* Application not already decided

| Condition / Action   | R1 | R2 | R3 | R4 |
| -------------------- | -: | -: | -: | -: |
| Submitted?           |  Y |  N |  Y |  Y |
| Eligible?            |  Y |  - |  N |  Y |
| Reviewer authorized? |  Y |  - |  - |  N |
| Already decided?     |  N |  - |  - |  - |
| Approve possible     |  X |  - |  - |  - |
| Reject operation     |  - |  X |  X |  X |

Already decided rule:

| Condition / Action            | R5 |
| ----------------------------- | -: |
| Submitted/eligible/authorized |  Y |
| Already decided?              |  Y |
| Approve possible              |  - |
| Reject duplicate decision     |  X |

IDs:

```text
DT-084 to DT-088
```

---

# 22. Loan Disbursement Decision Table

Conditions:

* Loan approved
* Not previously disbursed
* Destination account active
* Destination belongs to borrower

| Condition / Action  | R1 | R2 | R3 | R4 |
| ------------------- | -: | -: | -: | -: |
| Approved?           |  Y |  N |  Y |  Y |
| Already disbursed?  |  N |  - |  Y |  N |
| Destination active? |  Y |  - |  - |  N |
| Destination owned?  |  Y |  - |  - |  Y |
| Disburse            |  X |  - |  - |  - |
| Reject              |  - |  X |  X |  X |

Unauthorized destination rule:

| Condition / Action  | R5 |
| ------------------- | -: |
| Approved?           |  Y |
| Already disbursed?  |  N |
| Destination active? |  Y |
| Destination owned?  |  N |
| Disburse            |  - |
| Reject              |  X |

IDs:

```text
DT-089 to DT-093
```

---

# 23. Loan Repayment Decision Table

Conditions:

* Loan active
* Repayment account owned
* Repayment account active
* Sufficient balance
* Payment amount valid

| Condition / Action | R1 | R2 | R3 | R4 | R5 |
| ------------------ | -: | -: | -: | -: | -: |
| Loan active?       |  Y |  N |  Y |  Y |  Y |
| Account owned?     |  Y |  - |  N |  Y |  Y |
| Account active?    |  Y |  - |  - |  N |  Y |
| Sufficient funds?  |  Y |  - |  - |  - |  N |
| Amount valid?      |  Y |  - |  - |  - |  Y |
| Process repayment  |  X |  - |  - |  - |  - |
| Reject             |  - |  X |  X |  X |  X |

Invalid amount rule:

| Condition / Action     | R6 |
| ---------------------- | -: |
| Other conditions valid |  Y |
| Amount valid?          |  N |
| Process                |  - |
| Reject                 |  X |

IDs:

```text
DT-094 to DT-099
```

---

# 24. Loan Closure Decision Table

Conditions:

* Outstanding principal = 0
* Interest settled
* Fees settled
* No pending repayment

| Condition / Action    | R1 | R2 | R3 | R4 |
| --------------------- | -: | -: | -: | -: |
| Principal zero?       |  Y |  N |  Y |  Y |
| Interest settled?     |  Y |  - |  N |  Y |
| Fees settled?         |  Y |  - |  - |  N |
| No pending repayment? |  Y |  - |  - |  - |
| Close loan            |  X |  - |  - |  - |
| Keep active           |  - |  X |  X |  X |

Pending repayment rule:

| Condition / Action    | R5 |
| --------------------- | -: |
| All balances settled? |  Y |
| Pending repayment?    |  Y |
| Close                 |  - |
| Wait/reconcile        |  X |

IDs:

```text
DT-100 to DT-104
```

---

# 25. Deposit Creation Decision Table

Conditions:

* Customer eligible
* Funding account owned
* Account active
* Principal within range
* Sufficient available balance
* Term valid

| Condition / Action     | R1 | R2 | R3 | R4 | R5 | R6 |
| ---------------------- | -: | -: | -: | -: | -: | -: |
| Eligible customer?     |  Y |  N |  Y |  Y |  Y |  Y |
| Funding account owned? |  Y |  - |  N |  Y |  Y |  Y |
| Account active?        |  Y |  - |  - |  N |  Y |  Y |
| Principal valid?       |  Y |  - |  - |  - |  N |  Y |
| Sufficient balance?    |  Y |  - |  - |  - |  - |  N |
| Term valid?            |  Y |  - |  - |  - |  - |  Y |
| Create deposit         |  X |  - |  - |  - |  - |  - |
| Reject                 |  - |  X |  X |  X |  X |  X |

Invalid term rule:

| Condition / Action             | R7 |
| ------------------------------ | -: |
| All preceding conditions valid |  Y |
| Term valid?                    |  N |
| Create                         |  - |
| Reject                         |  X |

IDs:

```text
DT-105 to DT-111
```

---

# 26. Deposit Early Withdrawal Decision Table

Conditions:

* Deposit active
* Early withdrawal permitted
* Minimum holding period met
* Customer authorized

| Condition / Action        | R1 | R2 | R3 | R4 |
| ------------------------- | -: | -: | -: | -: |
| Deposit active?           |  Y |  N |  Y |  Y |
| Early withdrawal allowed? |  Y |  - |  N |  Y |
| Holding period met?       |  Y |  - |  - |  N |
| Customer authorized?      |  Y |  - |  - |  Y |
| Calculate payout/penalty  |  X |  - |  - |  - |
| Reject                    |  - |  X |  X |  X |

Unauthorized rule:

| Condition / Action      | R5 |
| ----------------------- | -: |
| Deposit active/eligible |  Y |
| Customer authorized?    |  N |
| Process                 |  - |
| Reject                  |  X |

IDs:

```text
DT-112 to DT-116
```

---

# 27. Deposit Maturity Decision Table

Conditions:

* Deposit active
* Maturity date reached
* Auto-renew enabled
* Settlement account valid

| Condition / Action        | R1 | R2 | R3 | R4 |
| ------------------------- | -: | -: | -: | -: |
| Deposit active?           |  Y |  N |  Y |  Y |
| Maturity reached?         |  Y |  - |  N |  Y |
| Auto-renew?               |  N |  - |  - |  Y |
| Settlement account valid? |  Y |  - |  - |  - |
| Pay principal + interest  |  X |  - |  - |  - |
| Renew deposit             |  - |  - |  - |  X |
| No maturity action        |  - |  X |  X |  - |

Invalid settlement account rule:

| Condition / Action            | R5 |
| ----------------------------- | -: |
| Active?                       |  Y |
| Maturity reached?             |  Y |
| Auto-renew?                   |  N |
| Settlement account valid?     |  N |
| Normal payout                 |  - |
| Controlled exception/recovery |  X |

IDs:

```text
DT-117 to DT-121
```

---

# 28. Account Closure Decision Table

Conditions:

* Balance zero
* No pending transactions
* No active loan dependency
* No active deposit dependency
* No unresolved card dependency
* Authorized actor

| Condition / Action       | R1 | R2 | R3 | R4 | R5 | R6 |
| ------------------------ | -: | -: | -: | -: | -: | -: |
| Balance zero?            |  Y |  N |  Y |  Y |  Y |  Y |
| No pending transactions? |  Y |  - |  N |  Y |  Y |  Y |
| No loan dependency?      |  Y |  - |  - |  N |  Y |  Y |
| No deposit dependency?   |  Y |  - |  - |  - |  N |  Y |
| No card dependency?      |  Y |  - |  - |  - |  - |  N |
| Authorized?              |  Y |  - |  - |  - |  - |  Y |
| Close account            |  X |  - |  - |  - |  - |  - |
| Reject closure           |  - |  X |  X |  X |  X |  X |

Unauthorized rule:

| Condition / Action             | R7 |
| ------------------------------ | -: |
| All financial conditions valid |  Y |
| Authorized?                    |  N |
| Close                          |  - |
| Reject                         |  X |

IDs:

```text
DT-122 to DT-128
```

---

# 29. Card Replacement Decision Table

Conditions:

* Card belongs to customer
* Card exists
* Replacement reason valid
* Card not already replaced
* Replacement permitted for state

| Condition / Action        | R1 | R2 | R3 | R4 | R5 |
| ------------------------- | -: | -: | -: | -: | -: |
| Owned?                    |  Y |  N |  Y |  Y |  Y |
| Exists?                   |  Y |  - |  N |  Y |  Y |
| Valid reason?             |  Y |  - |  - |  N |  Y |
| Already replaced?         |  N |  - |  - |  - |  Y |
| State allows replacement? |  Y |  - |  - |  - |  Y |
| Create replacement        |  X |  - |  - |  - |  - |
| Reject                    |  - |  X |  X |  X |  X |

IDs:

```text
DT-129 to DT-133
```

---

# 30. Notification Delivery Decision Table

Conditions:

* Event qualifies for notification
* Category enabled
* Notification mandatory
* Channel enabled
* Contact verified

| Condition / Action             | R1 | R2 | R3 | R4 | R5 |
| ------------------------------ | -: | -: | -: | -: | -: |
| Qualifying event?              |  Y |  N |  Y |  Y |  Y |
| Category enabled?              |  Y |  - |  N |  N |  Y |
| Mandatory security alert?      |  N |  - |  N |  Y |  N |
| Channel enabled?               |  Y |  - |  - |  Y |  N |
| Contact verified?              |  Y |  - |  - |  Y |  - |
| Send notification              |  X |  - |  - |  X |  - |
| Suppress optional notification |  - |  X |  X |  - |  X |

Mandatory but unverified contact requires channel-specific fallback policy.

IDs:

```text
DT-134 to DT-138
```

---

# 31. Notification Status Decision Table

Conditions:

* Underlying transaction completed
* Failed
* Pending
* Reversed

| Transaction State | Success Message | Failure Message | Pending Message | Reversal Message |
| ----------------- | --------------: | --------------: | --------------: | ---------------: |
| COMPLETED         |               Y |               N |               N |                N |
| FAILED            |               N |               Y |               N |                N |
| PENDING           |               N |               N |               Y |                N |
| REVERSED          |               N |               N |               N |                Y |

IDs:

```text
DT-139 to DT-142
```

This is critical for preventing:

```text
Failed transaction → Success notification
```

---

# 32. Statement Transaction Inclusion Decision Table

Conditions:

* Transaction belongs to account
* Transaction date within statement range
* Transaction financially completed/relevant
* Authorization valid

| Condition / Action       | R1 | R2 | R3 | R4 |
| ------------------------ | -: | -: | -: | -: |
| Belongs to account?      |  Y |  N |  Y |  Y |
| In date range?           |  Y |  - |  N |  Y |
| Qualifies for statement? |  Y |  - |  - |  N |
| Authorized?              |  Y |  - |  - |  Y |
| Include                  |  X |  - |  - |  - |
| Exclude                  |  - |  X |  X |  X |

Unauthorized statement request:

| Condition / Action              | R5 |
| ------------------------------- | -: |
| Transaction otherwise qualifies |  Y |
| Authorized?                     |  N |
| Include                         |  - |
| Deny statement access           |  X |

IDs:

```text
DT-143 to DT-147
```

---

# 33. Transaction Reversal Decision Table

Conditions:

* Transaction exists
* Transaction completed
* Reversible
* Not already reversed
* Actor authorized

| Condition / Action | R1 | R2 | R3 | R4 | R5 |
| ------------------ | -: | -: | -: | -: | -: |
| Exists?            |  Y |  N |  Y |  Y |  Y |
| Completed?         |  Y |  - |  N |  Y |  Y |
| Reversible?        |  Y |  - |  - |  N |  Y |
| Already reversed?  |  N |  - |  - |  - |  Y |
| Authorized?        |  Y |  - |  - |  - |  Y |
| Reverse            |  X |  - |  - |  - |  - |
| Reject             |  - |  X |  X |  X |  X |

Unauthorized rule:

| Condition / Action               | R6 |
| -------------------------------- | -: |
| All transaction conditions valid |  Y |
| Authorized?                      |  N |
| Reverse                          |  - |
| Reject                           |  X |

IDs:

```text
DT-148 to DT-153
```

---

# 34. Admin Permission Decision Table

Example operation:

```text
Freeze customer account
```

Conditions:

* Admin authenticated
* Admin active
* Permission exists
* Target valid
* Reason supplied

| Condition / Action | R1 | R2 | R3 | R4 | R5 |
| ------------------ | -: | -: | -: | -: | -: |
| Authenticated?     |  Y |  N |  Y |  Y |  Y |
| Active admin?      |  Y |  - |  N |  Y |  Y |
| Has permission?    |  Y |  - |  - |  N |  Y |
| Valid target?      |  Y |  - |  - |  - |  N |
| Reason valid?      |  Y |  - |  - |  - |  Y |
| Perform action     |  X |  - |  - |  - |  - |
| Reject             |  - |  X |  X |  X |  X |

Missing reason:

| Condition / Action     | R6 |
| ---------------------- | -: |
| Other conditions valid |  Y |
| Reason valid?          |  N |
| Perform                |  - |
| Reject                 |  X |

IDs:

```text
DT-154 to DT-159
```

---

# 35. KYC Admin Decision Table

Conditions:

* Reviewer authenticated
* Reviewer authorized
* Case pending
* Evidence sufficient
* Decision provided

| Condition / Action   | R1 | R2 | R3 | R4 |
| -------------------- | -: | -: | -: | -: |
| Authenticated?       |  Y |  N |  Y |  Y |
| Authorized reviewer? |  Y |  - |  N |  Y |
| Case pending?        |  Y |  - |  - |  N |
| Evidence sufficient? |  Y |  - |  - |  - |
| Mark VERIFIED        |  X |  - |  - |  - |

Rejection path:

| Condition / Action               | R5 |
| -------------------------------- | -: |
| Authenticated/authorized/pending |  Y |
| Evidence sufficient?             |  N |
| Rejection reason supplied?       |  Y |
| Mark REJECTED                    |  X |

Missing rejection reason:

| Condition / Action         | R6 |
| -------------------------- | -: |
| Evidence insufficient?     |  Y |
| Reason supplied?           |  N |
| Reject decision submission |  X |

IDs:

```text
DT-160 to DT-165
```

---

# 36. Profile Email Change Decision Table

Conditions:

* Customer authenticated
* Current session valid
* New email valid
* Email unused
* Verification completed

| Condition / Action     | R1 | R2 | R3 | R4 | R5 |
| ---------------------- | -: | -: | -: | -: | -: |
| Authenticated?         |  Y |  N |  Y |  Y |  Y |
| Session valid?         |  Y |  - |  N |  Y |  Y |
| New email valid?       |  Y |  - |  - |  N |  Y |
| Email available?       |  Y |  - |  - |  - |  N |
| Verification complete? |  Y |  - |  - |  - |  Y |
| Commit change          |  X |  - |  - |  - |  - |
| Reject                 |  - |  X |  X |  X |  X |

Pending verification path:

| Condition / Action         | R6 |
| -------------------------- | -: |
| All prior conditions valid |  Y |
| Verification complete?     |  N |
| Commit trusted email       |  - |
| Keep change pending        |  X |

IDs:

```text
DT-166 to DT-171
```

---

# 37. Session Access Decision Table

Conditions:

* Session exists
* Session authenticated
* Session unexpired
* Session not revoked
* User enabled

| Condition / Action     | R1 | R2 | R3 | R4 | R5 |
| ---------------------- | -: | -: | -: | -: | -: |
| Exists?                |  Y |  N |  Y |  Y |  Y |
| Authenticated?         |  Y |  - |  N |  Y |  Y |
| Unexpired?             |  Y |  - |  - |  N |  Y |
| Not revoked?           |  Y |  - |  - |  - |  N |
| User enabled?          |  Y |  - |  - |  - |  Y |
| Allow protected access |  X |  - |  - |  - |  - |
| Reject                 |  - |  X |  X |  X |  X |

Disabled-user rule:

| Condition / Action      | R6 |
| ----------------------- | -: |
| Session otherwise valid |  Y |
| User enabled?           |  N |
| Allow                   |  - |
| Reject                  |  X |

IDs:

```text
DT-172 to DT-177
```

---

# 38. Financial Idempotency Decision Table

Conditions:

* Request valid
* Idempotency key present
* Key seen before
* Existing request payload matches previous payload
* Previous operation completed

| Condition / Action               | R1 | R2 |          R3 | R4 |
| -------------------------------- | -: | -: | ----------: | -: |
| Request valid?                   |  Y |  N |           Y |  Y |
| Key present?                     |  Y |  - |           N |  Y |
| Key seen before?                 |  N |  - |           - |  Y |
| Payload matches previous?        |  - |  - |           - |  Y |
| Previous completed?              |  - |  - |           - |  Y |
| Execute new financial action     |  X |  - | Conditional |  - |
| Reject invalid request           |  - |  X |           - |  - |
| Return prior result/no duplicate |  - |  - |           - |  X |

Conflicting key reuse:

| Condition / Action        | R5 |
| ------------------------- | -: |
| Key seen before?          |  Y |
| Payload matches previous? |  N |
| Execute                   |  - |
| Reject conflict           |  X |

IDs:

```text
DT-178 to DT-182
```

**Priority:** P0

---

# 39. Scheduled Transfer Execution Decision Table

Conditions at execution time:

* Schedule active
* Execution time reached
* Source account active
* Beneficiary active
* Sufficient funds
* Limits available

| Condition / Action  | R1 | R2 | R3 | R4 | R5 | R6 |
| ------------------- | -: | -: | -: | -: | -: | -: |
| Schedule active?    |  Y |  N |  Y |  Y |  Y |  Y |
| Time reached?       |  Y |  - |  N |  Y |  Y |  Y |
| Source active?      |  Y |  - |  - |  N |  Y |  Y |
| Beneficiary active? |  Y |  - |  - |  - |  N |  Y |
| Funds sufficient?   |  Y |  - |  - |  - |  - |  N |
| Limits valid?       |  Y |  - |  - |  - |  - |  Y |
| Execute             |  X |  - |  - |  - |  - |  - |
| Do not execute/fail |  - |  X |  X |  X |  X |  X |

Limit invalid rule:

| Condition / Action         | R7 |
| -------------------------- | -: |
| All prior conditions valid |  Y |
| Limits valid?              |  N |
| Execute                    |  - |
| Fail/reject                |  X |

IDs:

```text
DT-183 to DT-189
```

---

# 40. Recurring Payment Decision Table

Conditions:

* Recurrence active
* Occurrence due
* End date not passed
* Source account valid
* Funds available
* Bill/payee valid

| Condition / Action | R1 | R2 | R3 | R4 | R5 | R6 |
| ------------------ | -: | -: | -: | -: | -: | -: |
| Recurrence active? |  Y |  N |  Y |  Y |  Y |  Y |
| Due?               |  Y |  - |  N |  Y |  Y |  Y |
| Before end?        |  Y |  - |  - |  N |  Y |  Y |
| Source valid?      |  Y |  - |  - |  - |  N |  Y |
| Funds available?   |  Y |  - |  - |  - |  - |  N |
| Payee/bill valid?  |  Y |  - |  - |  - |  - |  Y |
| Execute occurrence |  X |  - |  - |  - |  - |  - |
| Skip/fail/stop     |  - |  X |  X |  X |  X |  X |

Invalid payee rule:

| Condition / Action     | R7 |
| ---------------------- | -: |
| Other conditions valid |  Y |
| Payee/bill valid?      |  N |
| Execute                |  - |
| Fail occurrence        |  X |

IDs:

```text
DT-190 to DT-196
```

---

# 41. Notification Preference Decision Table

Conditions:

* Event category optional
* Category enabled
* Channel enabled
* Mandatory security event

| Condition / Action  | R1 | R2 | R3 | R4 |
| ------------------- | -: | -: | -: | -: |
| Optional event?     |  Y |  Y |  Y |  N |
| Category enabled?   |  Y |  N |  Y |  - |
| Channel enabled?    |  Y |  - |  N |  Y |
| Mandatory security? |  N |  N |  N |  Y |
| Deliver             |  X |  - |  - |  X |
| Suppress            |  - |  X |  X |  - |

IDs:

```text
DT-197 to DT-200
```

---

# 42. Transaction History Display Decision Table

Conditions:

* Transaction belongs to account
* User owns account
* Transaction record exists
* Status visible according to requirements

| Condition / Action  | R1 | R2 | R3 | R4 |
| ------------------- | -: | -: | -: | -: |
| Belongs to account? |  Y |  N |  Y |  Y |
| User owns account?  |  Y |  - |  N |  Y |
| Record exists?      |  Y |  - |  - |  N |
| Display transaction |  X |  - |  - |  - |
| Hide/deny           |  - |  X |  X |  X |

IDs:

```text
DT-201 to DT-204
```

---

# 43. Account Freeze During Transaction Decision Table

Conditions:

* Transaction started before freeze
* Transaction committed before freeze
* Freeze completed before final authorization

| Condition / Action                        | R1 | R2 | R3 |
| ----------------------------------------- | -: | -: | -: |
| Started before freeze?                    |  Y |  Y |  N |
| Committed before freeze?                  |  Y |  N |  N |
| Freeze active before authorization?       |  N |  Y |  Y |
| Keep valid completed transaction          |  X |  - |  - |
| Reject/rollback according to atomic rules |  - |  X |  X |

IDs:

```text
DT-205 to DT-207
```

The exact transaction cutoff must be explicitly defined.

---

# 44. Concurrent Balance Decision Table

Assume:

```text
Available balance = 1,000
```

Two concurrent operations:

```text
Operation A
Operation B
```

| Condition / Action                                      | R1 | R2 | R3 |
| ------------------------------------------------------- | -: | -: | -: |
| A + B <= available balance?                             |  Y |  N |  N |
| A individually affordable?                              |  Y |  Y |  N |
| B individually affordable?                              |  Y |  Y |  Y |
| Allow both                                              |  X |  - |  - |
| Allow only valid combination according to locking order |  - |  X |  - |
| Reject unaffordable operation                           |  - |  - |  X |

IDs:

```text
DT-208 to DT-210
```

**Priority:** P0

---

# 45. Card Refund Decision Table

Conditions:

* Original transaction exists
* Original transaction completed
* Refund amount <= refundable amount
* Not already fully refunded
* Authorized actor

| Condition / Action      | R1 | R2 | R3 | R4 | R5 |
| ----------------------- | -: | -: | -: | -: | -: |
| Original exists?        |  Y |  N |  Y |  Y |  Y |
| Completed?              |  Y |  - |  N |  Y |  Y |
| Refund valid?           |  Y |  - |  - |  N |  Y |
| Already fully refunded? |  N |  - |  - |  - |  Y |
| Authorized?             |  Y |  - |  - |  - |  Y |
| Refund                  |  X |  - |  - |  - |  - |
| Reject                  |  - |  X |  X |  X |  X |

Unauthorized rule:

| Condition / Action          | R6 |
| --------------------------- | -: |
| All transaction rules valid |  Y |
| Authorized?                 |  N |
| Refund                      |  - |
| Reject                      |  X |

IDs:

```text
DT-211 to DT-216
```

---

# 46. Fee Calculation Decision Table

Example tiered transfer fee:

```text
Amount <= 1,000      → 0
1,000.01–10,000      → 10
> 10,000             → 0.2%
```

| Rule   | Amount Range    | Fee  |
| ------ | --------------- | ---- |
| DT-217 | <= 1,000        | 0    |
| DT-218 | 1,000.01–10,000 | 10   |
| DT-219 | > 10,000        | 0.2% |

Additional conditions should include:

* Fee cap
* Fee waiver
* Customer tier
* Transfer type

when implemented.

---

# 47. Example Complex Fee Decision Table

Conditions:

* Internal transfer
* Premium customer
* Amount > free threshold

| Condition / Action      | R1 | R2 | R3 | R4 |
| ----------------------- | -: | -: | -: | -: |
| Internal transfer?      |  Y |  Y |  N |  N |
| Premium customer?       |  Y |  N |  Y |  N |
| Above fee threshold?    |  Y |  Y |  Y |  N |
| Waive fee               |  X |  - |  X |  - |
| Charge standard fee     |  - |  X |  - |  - |
| No fee due to threshold |  - |  - |  - |  X |

IDs:

```text
DT-220 to DT-223
```

---

# 48. Audit Requirement Decision Table

Conditions:

* State-changing action
* Financial action
* Privileged admin action
* Security-sensitive action

| Condition / Action  | R1 | R2 | R3 | R4 |          R5 |
| ------------------- | -: | -: | -: | -: | ----------: |
| State-changing?     |  Y |  N |  Y |  Y |           N |
| Financial?          |  N |  Y |  Y |  N |           N |
| Privileged admin?   |  N |  N |  Y |  Y |           N |
| Security-sensitive? |  N |  N |  N |  Y |           N |
| Create audit event  |  X |  X |  X |  X | Conditional |

IDs:

```text
DT-224 to DT-228
```

---

# 49. File Upload Decision Table

Where profile/KYC uploads exist.

Conditions:

* File type supported
* File size valid
* File content valid
* User authorized

| Condition / Action | R1 | R2 | R3 | R4 |
| ------------------ | -: | -: | -: | -: |
| Supported type?    |  Y |  N |  Y |  Y |
| Valid size?        |  Y |  - |  N |  Y |
| Content valid?     |  Y |  - |  - |  N |
| Authorized?        |  Y |  - |  - |  Y |
| Accept upload      |  X |  - |  - |  - |
| Reject             |  - |  X |  X |  X |

Unauthorized rule:

| Condition / Action | R5 |
| ------------------ | -: |
| File valid         |  Y |
| Authorized?        |  N |
| Accept             |  - |
| Reject             |  X |

IDs:

```text
DT-229 to DT-233
```

---

# 50. Statement Generation Decision Table

Conditions:

* Customer owns account
* Date range valid
* Date range within maximum
* Account exists
* Session valid

| Condition / Action | R1 | R2 | R3 | R4 | R5 |
| ------------------ | -: | -: | -: | -: | -: |
| Owns account?      |  Y |  N |  Y |  Y |  Y |
| Date range valid?  |  Y |  - |  N |  Y |  Y |
| Within max range?  |  Y |  - |  - |  N |  Y |
| Account exists?    |  Y |  - |  - |  - |  N |
| Session valid?     |  Y |  - |  - |  - |  Y |
| Generate statement |  X |  - |  - |  - |  - |
| Reject             |  - |  X |  X |  X |  X |

Expired session:

| Condition / Action     | R6 |
| ---------------------- | -: |
| All request data valid |  Y |
| Session valid?         |  N |
| Generate               |  - |
| Reject/authenticate    |  X |

IDs:

```text
DT-234 to DT-239
```

---

# 51. Admin Transaction Reversal Decision Table

Conditions:

* Admin authenticated
* Role permits reversal
* Transaction reversible
* Reason valid
* Not previously reversed

| Condition / Action | R1 | R2 | R3 | R4 | R5 |
| ------------------ | -: | -: | -: | -: | -: |
| Authenticated?     |  Y |  N |  Y |  Y |  Y |
| Authorized role?   |  Y |  - |  N |  Y |  Y |
| Reversible?        |  Y |  - |  - |  N |  Y |
| Reason valid?      |  Y |  - |  - |  - |  N |
| Already reversed?  |  N |  - |  - |  - |  N |
| Execute reversal   |  X |  - |  - |  - |  - |
| Reject             |  - |  X |  X |  X |  X |

Already reversed:

| Condition / Action     | R6 |
| ---------------------- | -: |
| Other conditions valid |  Y |
| Already reversed?      |  Y |
| Execute                |  - |
| Reject                 |  X |

IDs:

```text
DT-240 to DT-245
```

---

# 52. Decision Table for Server-Side Validation

Conditions:

* UI validation passes
* API/backend validation passes

| Rule   | UI Validation | Backend Validation | Expected                                                 |
| ------ | ------------: | -----------------: | -------------------------------------------------------- |
| DT-246 |             Y |                  Y | Process valid request                                    |
| DT-247 |             N |                  Y | UI blocks; direct API still must be evaluated by backend |
| DT-248 |             Y |                  N | Backend rejects                                          |
| DT-249 |             N |                  N | Reject                                                   |

Important principle:

```text
Backend validation is authoritative.
```

UI validation alone is never sufficient.

---

# 53. Decision Table for UI/API/DB Consistency

|        UI |       API |        DB | Result                 |
| --------: | --------: | --------: | ---------------------- |
|   Correct |   Correct |   Correct | Pass                   |
| Incorrect |   Correct |   Correct | UI defect              |
|   Correct | Incorrect |   Correct | API defect             |
|   Correct |   Correct | Incorrect | Persistence defect     |
| Incorrect | Incorrect |   Correct | Integration defect     |
|   Correct | Incorrect | Incorrect | Backend defect         |
| Incorrect |   Correct | Incorrect | Cross-layer defect     |
| Incorrect | Incorrect | Incorrect | Critical system defect |

IDs:

```text
DT-250 to DT-257
```

---

# 54. Decision Table for Financial Success

A financial operation should be considered successfully completed only when:

* Business validation passes
* Authorization passes
* Persistence succeeds
* Required balance changes succeed
* Transaction record exists

| Validation | Authorization | Balance/Persistence | Transaction Record | Result                 |
| ---------: | ------------: | ------------------: | -----------------: | ---------------------- |
|          Y |             Y |                   Y |                  Y | SUCCESS                |
|          N |             - |                   - |                  - | REJECT                 |
|          Y |             N |                   - |                  - | REJECT                 |
|          Y |             Y |                   N |                  - | FAIL/ROLLBACK          |
|          Y |             Y |                   Y |                  N | FAIL/RECOVERY REQUIRED |

IDs:

```text
DT-258 to DT-262
```

**Priority:** P0

---

# 55. Decision Table for Transaction Notification

Conditions:

* Financial transaction actually successful
* Notification creation successful
* Delivery provider available

| Financial Transaction | Notification Created | Provider | Expected Financial State | Notification                |
| --------------------- | -------------------: | -------: | ------------------------ | --------------------------- |
| Success               |                    Y |        Y | Success                  | Delivered                   |
| Success               |                    Y |        N | Success                  | Retry/failed delivery       |
| Success               |                    N |        - | Success                  | Record notification failure |
| Failed                |                    Y |        Y | Failed                   | Failure notification only   |
| Failed                |                    N |        - | Failed                   | No false success            |

IDs:

```text
DT-263 to DT-267
```

Critical rule:

```text
Notification failure must not silently change the financial result.

Financial failure must never produce a success notification.
```

---

# 56. Decision Table for Transaction Retry

Conditions:

* Previous request failed before processing
* Previous request completed
* Previous result unknown due to timeout
* Same idempotency key

| Previous State           | Same Key? | Expected                                                               |
| ------------------------ | --------: | ---------------------------------------------------------------------- |
| Definitely not processed |         Y | Retry safely                                                           |
| Completed                |         Y | Return prior result, no duplicate                                      |
| Unknown                  |         Y | Resolve prior state before duplicate execution                         |
| Completed                |         N | New operation only if user intentionally requested another transaction |

IDs:

```text
DT-268 to DT-271
```

---

# 57. Decision Table for Concurrent Financial Operations

Conditions:

* Operation A valid
* Operation B valid
* Combined financial impact affordable
* Same uniqueness constraint affected

| A Valid | B Valid | Combined Affordable | Same Unique Resource | Expected                                   |
| ------: | ------: | ------------------: | -------------------: | ------------------------------------------ |
|       Y |       Y |                   Y |                    N | Both may succeed                           |
|       Y |       Y |                   N |                    N | Only affordable valid combination succeeds |
|       Y |       Y |                   Y |                    Y | Only one where uniqueness requires         |
|       Y |       N |                   - |                    - | A may succeed, B rejects                   |
|       N |       N |                   - |                    - | Both reject                                |

IDs:

```text
DT-272 to DT-276
```

---

# 58. Decision Table for Customer Data Access

Conditions:

* Authenticated
* Customer owns resource
* Admin access
* Admin permission

| Authenticated | Own Resource | Admin | Permission | Expected               |
| ------------: | -----------: | ----: | ---------: | ---------------------- |
|             Y |            Y |     N |          - | Allow                  |
|             Y |            N |     N |          - | Deny                   |
|             Y |            N |     Y |          Y | Allow authorized admin |
|             Y |            N |     Y |          N | Deny                   |
|             N |            - |     - |          - | Deny                   |

IDs:

```text
DT-277 to DT-281
```

---

# 59. Decision Table for Destructive Admin Action

Example:

```text
Close account
Block card
Suspend customer
Reverse transaction
```

Conditions:

* Permission valid
* Target state eligible
* Reason supplied
* Confirmation completed

| Permission | Eligible State | Reason | Confirmed | Expected  |
| ---------: | -------------: | -----: | --------: | --------- |
|          Y |              Y |      Y |         Y | Execute   |
|          N |              - |      - |         - | Deny      |
|          Y |              N |      - |         - | Reject    |
|          Y |              Y |      N |         - | Reject    |
|          Y |              Y |      Y |         N | No action |

IDs:

```text
DT-282 to DT-286
```

---

# 60. Decision Table for Security-Sensitive Setting Change

Example:

```text
Password
Email
Phone
MFA
```

Conditions:

* Session valid
* Reauthentication passed
* New value valid
* Verification passed where required

| Session | Reauth | Value Valid | Verification | Expected            |
| ------: | -----: | ----------: | -----------: | ------------------- |
|       Y |      Y |           Y |            Y | Apply               |
|       N |      - |           - |            - | Reject              |
|       Y |      N |           - |            - | Reject              |
|       Y |      Y |           N |            - | Reject              |
|       Y |      Y |           Y |            N | Keep pending/reject |

IDs:

```text
DT-287 to DT-291
```

---

# 61. Decision Table for Lockout

Assume:

```text
Maximum failed attempts = 5
```

Conditions:

* Credentials correct
* Failed count below threshold
* Failed count reaches threshold
* Account locked

| Rule   | Correct Password | Failed Count       | Locked | Expected           |
| ------ | ---------------: | ------------------ | -----: | ------------------ |
| DT-292 |                Y | Below threshold    |      N | Login              |
| DT-293 |                N | Below threshold    |      N | Reject + increment |
| DT-294 |                N | Reaches threshold  |      N | Reject + lock      |
| DT-295 |                Y | Threshold reached  |      Y | Reject due to lock |
| DT-296 |                N | Threshold exceeded |      Y | Reject             |

---

# 62. Decision Table for OTP Resend

Conditions:

* Original OTP active
* Resend allowed
* Rate limit exceeded

| Original OTP | Resend Allowed | Rate Limited | Expected                                     |
| -----------: | -------------: | -----------: | -------------------------------------------- |
|       Active |              Y |            N | Issue new OTP according to invalidation rule |
|       Active |              N |            N | Reject premature resend                      |
|          Any |              - |            Y | Reject/throttle                              |
|      Expired |              Y |            N | Issue new OTP                                |

IDs:

```text
DT-297 to DT-300
```

---

# 63. Decision Table Coverage Reduction

A full combination table with many binary conditions can grow rapidly.

For example:

```text
7 Boolean conditions
```

can produce:

```text
2^7 = 128 combinations
```

Testing every combination may be unnecessary.

Decision tables should therefore:

1. Remove impossible combinations.
2. Collapse conditions using `-` where irrelevant.
3. Prioritize high-risk combinations.
4. Include each failure cause at least once.
5. Include important multi-failure combinations.
6. Use pairwise testing later for broader interaction coverage.

---

# 64. High-Risk Rule Selection

For critical financial operations, at minimum test:

```text
All conditions valid

Each individual critical condition invalid once

Key combinations of multiple invalid conditions

Concurrency-related combinations

Authorization failures

Boundary-related combinations
```

Example transfer:

```text
All valid
Insufficient balance only
Limit exceeded only
Frozen account only
Inactive beneficiary only
Unauthorized account only
Expired session only
Concurrent overspending
```

---

# 65. Decision Tables + Boundary Value Analysis

Decision tables define combinations.

Boundary Value Analysis provides precise values inside a condition.

Example condition:

```text
Amount within transfer limit?
```

Decision table gives:

```text
Y / N
```

BVA supplies concrete data:

```text
99,999.99
100,000.00
100,000.01
```

Combining both gives stronger test coverage.

---

# 66. Decision Tables + Equivalence Partitioning

Equivalence Partitioning defines categories such as:

```text
ACTIVE ACCOUNT
FROZEN ACCOUNT
CLOSED ACCOUNT
```

Decision tables combine those categories with other conditions:

```text
Account state
Balance state
Beneficiary state
Customer state
```

This allows representative but comprehensive rule coverage.

---

# 67. Decision Tables + State Transition Testing

Decision tables answer:

```text
Should this action happen?
```

State Transition Testing answers:

```text
Is this transition from state A to state B valid?
```

Example:

Decision table:

```text
Can this card be blocked?
```

State transition:

```text
ACTIVE → BLOCKED
FROZEN → BLOCKED
CANCELLED → BLOCKED
```

Both techniques should be used together.

---

# 68. UI Automation Candidates

Decision-table rules can drive data-driven UI automation.

Example:

```text
Source state
Beneficiary state
Balance state
Amount
Expected result
```

can be executed through:

* Selenium
* Cypress
* Playwright

Critical UI rules should be backed by API testing as well.

---

# 69. API Automation Candidates

Decision tables are especially useful for API automation.

Example REST Assured dataset:

```text
Rule:
Authenticated = true
Source Owned = true
Account Active = false
Balance Sufficient = true
Beneficiary Active = true

Expected:
HTTP/business rejection
No transaction created
No balance change
```

Postman and REST Assured can iterate through these combinations.

---

# 70. SQL Validation Candidates

For every rejected financial rule, validate:

```text
No unexpected account debit
No unexpected account credit
No completed transaction record
No incorrect limit consumption
No orphan financial record
```

For accepted rules:

```text
Correct balance effect
Correct transaction record
Correct status
Correct reference
Correct audit record
```

---

# 71. Performance Testing Application

Decision tables can also define valid performance-test traffic mixes.

Example:

```text
70% valid transfers
10% insufficient funds
5% limit exceeded
5% frozen account
5% invalid beneficiary
5% duplicate/retry traffic
```

JMeter can use these conditions to simulate more realistic production-like behavior.

---

# 72. BDD Example — Transfer Decision Table

```gherkin
Feature: Transfer authorization

Scenario Outline: Transfer decision depends on account and beneficiary rules
  Given the customer authentication state is <authenticated>
  And the source account state is <accountState>
  And the beneficiary state is <beneficiaryState>
  And available funds are <funds>
  And the transfer limit condition is <limit>
  When the customer attempts a transfer
  Then the transfer should be <result>

Examples:
  | authenticated | accountState | beneficiaryState | funds        | limit    | result   |
  | yes           | active       | active           | sufficient   | valid    | accepted |
  | no            | active       | active           | sufficient   | valid    | rejected |
  | yes           | frozen       | active           | sufficient   | valid    | rejected |
  | yes           | active       | disabled         | sufficient   | valid    | rejected |
  | yes           | active       | active           | insufficient | valid    | rejected |
  | yes           | active       | active           | sufficient   | exceeded | rejected |
```

---

# 73. BDD Example — Card Usage

```gherkin
Feature: Card transaction authorization

Scenario Outline: Card transaction eligibility
  Given the card state is <cardState>
  And the linked account state is <accountState>
  And available funds are <funds>
  And the transaction is <limitState>
  When a card purchase is attempted
  Then the transaction should be <result>

Examples:
  | cardState | accountState | funds        | limitState | result   |
  | active    | active       | sufficient   | within     | accepted |
  | frozen    | active       | sufficient   | within     | rejected |
  | blocked   | active       | sufficient   | within     | rejected |
  | expired   | active       | sufficient   | within     | rejected |
  | active    | frozen       | sufficient   | within     | rejected |
  | active    | active       | insufficient | within     | rejected |
  | active    | active       | sufficient   | exceeded   | rejected |
```

---

# 74. BDD Example — Loan Approval

```gherkin
Feature: Loan eligibility

Scenario Outline: Loan eligibility decision
  Given the customer status is <customerStatus>
  And the KYC status is <kyc>
  And the customer's income is <income>
  And the debt assessment is <debt>
  And the requested amount is <amount>
  When eligibility is evaluated
  Then the application should be <result>

Examples:
  | customerStatus | kyc      | income       | debt       | amount  | result     |
  | active         | verified | sufficient   | acceptable | valid   | eligible   |
  | suspended      | verified | sufficient   | acceptable | valid   | ineligible |
  | active         | pending  | sufficient   | acceptable | valid   | ineligible |
  | active         | verified | insufficient | acceptable | valid   | ineligible |
  | active         | verified | sufficient   | excessive  | valid   | ineligible |
  | active         | verified | sufficient   | acceptable | invalid | ineligible |
```

---

# 75. Risk Traceability

Decision Table Testing directly supports mitigation of:

```text
RISK-001 — Incorrect account balance
RISK-003 — Duplicate financial transaction
RISK-004 — Partial transfer
RISK-005 — Authentication bypass
RISK-006 — Privilege escalation
RISK-007 — Transfer exceeds available balance
RISK-008 — Limit bypass
RISK-009 — Frozen account can transact
RISK-010 — Incorrect fee
RISK-011 — Incorrect loan rules/calculations
RISK-012 — Incorrect deposit rules/calculations
RISK-013 — Concurrent transaction corruption
RISK-017 — Wrong/invalid beneficiary
RISK-018 — Invalid destination account
RISK-023 — Unauthorized admin operation
RISK-024 — Frozen card usable
RISK-025 — Blocked card usable
RISK-026 — Failed payment changes balance
RISK-027 — Duplicate payment
RISK-028 — Scheduled transfer incorrect
RISK-029 — Cancelled transfer executes
RISK-030 — Unauthorized API request
RISK-037 — Frontend-only validation
RISK-038 — UI/API business rules disagree
RISK-039 — Database/API state inconsistent
RISK-047 — Unauthorized resource access
RISK-048 — False UI success
```

---

# 76. Decision Table Coverage Summary

This document applies decision tables to:

* Transfer authorization
* Balance and fees
* Transfer limits
* Beneficiaries
* Account states
* Customer status
* KYC
* Login
* MFA
* Password reset
* Payments
* Duplicate bill payments
* Cards
* Card lifecycle
* Loan eligibility
* Loan approval
* Loan disbursement
* Loan repayments
* Loan closure
* Deposits
* Deposit withdrawal
* Deposit maturity
* Account closure
* Card replacement
* Notifications
* Statements
* Reversals
* Admin permissions
* KYC review
* Profile security changes
* Sessions
* Idempotency
* Scheduled transfers
* Recurring payments
* Transaction history
* Concurrency
* Refunds
* Fees
* Audit requirements
* File uploads
* Server-side validation
* Cross-layer consistency
* Financial completion
* Retries
* Data access
* Destructive admin actions
* Lockout
* OTP resend

---

# 77. Final Decision Table Testing Principle

Decision Table Testing should be used whenever the answer to a business rule depends on several conditions simultaneously.

For every complex banking rule, QA should ask:

```text
What conditions determine the result?

Which conditions are mandatory?

Which conditions become irrelevant after another condition fails?

What happens when every condition is valid?

What happens when each critical condition fails individually?

Which combinations could produce dangerous financial behavior?

Can authorization failure be combined with otherwise valid business data?

Can concurrency change the result?

Does the UI make the same decision as the API?

Does the database state reflect the decision correctly?
```

The most important decision-table areas in this project are:

```text
Financial authorization
Balance validation
Limits
Lifecycle state
Customer/KYC status
Admin permissions
Idempotency
Concurrency
Security
```

Decision tables ensure that the system is tested not only for individual rules, but for the interaction between those rules.
