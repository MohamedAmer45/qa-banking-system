# Banking System — Sample Defects

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Document | Sample Defects                 |
| Version  | 1.0                            |
| Status   | Portfolio Samples              |
| Owner    | QA Engineering                 |

---

# 2. Purpose

This document contains realistic sample defects for the Banking System Testing Project.

The defects demonstrate how QA findings should be documented across:

* Financial transactions
* Authentication
* Authorization
* Accounts
* Cards
* Loans
* Deposits
* Statements
* Notifications
* APIs
* Database consistency
* Concurrency
* Browser compatibility
* Session management
* Admin operations

These are **sample portfolio defects**.

They must not be represented as defects actually discovered in the Banking System until they have been reproduced against the implemented application.

---

# 3. Sample Defect Summary

| ID      | Module              | Summary                                                 | Severity | Priority |
| ------- | ------------------- | ------------------------------------------------------- | -------- | -------- |
| BUG-001 | Transfers           | Double-click creates duplicate transfer                 | Critical | P0       |
| BUG-002 | Transfers           | Fee excluded from available-balance validation          | Critical | P0       |
| BUG-003 | Security            | Customer accesses another customer's statement          | Critical | P0       |
| BUG-004 | Authentication      | Protected API accessible before MFA completion          | Critical | P0       |
| BUG-005 | Accounts            | Transfer succeeds after account is frozen               | Critical | P0       |
| BUG-006 | Payments            | Failed payment still reduces available balance          | Critical | P0       |
| BUG-007 | Cards               | Frozen card remains usable through API                  | Critical | P0       |
| BUG-008 | Loans               | Concurrent requests disburse loan twice                 | Critical | P0       |
| BUG-009 | Deposits            | Deposit maturity payout executes twice                  | Critical | P0       |
| BUG-010 | Statements          | Payment fee excluded from closing balance               | Critical | P0       |
| BUG-011 | Notifications       | Failed payment sends success notification               | High     | P1       |
| BUG-012 | Admin               | Read-only admin can reverse transaction through API     | Critical | P0       |
| BUG-013 | Sessions            | Logged-out session remains usable in another tab        | Critical | P0       |
| BUG-014 | Transactions        | Reversal overwrites original transaction record         | High     | P1       |
| BUG-015 | Transfers           | Timeout retry creates second transfer                   | Critical | P0       |
| BUG-016 | Scheduled Transfers | Cancelled transfer still executes                       | Critical | P0       |
| BUG-017 | Profile/Security    | Password change does not revoke sessions as required    | Critical | P0       |
| BUG-018 | API                 | Customer can modify own KYC status                      | Critical | P0       |
| BUG-019 | Cross-Browser       | Mobile confirmation hides transfer fee and total        | High     | P1       |
| BUG-020 | Database            | Duplicate transaction references generated concurrently | Critical | P0       |

---

# 4. BUG-001 — Duplicate Transfer From Double Submission

## Title

```text
[Transfers] Rapid double-click on confirmation creates two completed transfers
```

## Module

Transfers

## Severity

Critical

## Priority

P0

## Status

OPEN — Sample

## Related Risks

```text
RISK-001 — Incorrect account balance
RISK-003 — Duplicate financial transaction
RISK-013 — Concurrency corruption
```

## Preconditions

```text
Customer: CUST-001
Customer Status: ACTIVE

Source Account: ACC-001
Account Status: ACTIVE
Opening Balance: 10,000.00

Beneficiary: BEN-001
Beneficiary Status: ACTIVE

Transfer Amount: 1,000.00
Fee: 10.00
```

## Steps to Reproduce

1. Login as `CUST-001`.
2. Navigate to Transfers.
3. Select `ACC-001`.
4. Select `BEN-001`.
5. Enter `1,000.00`.
6. Continue to confirmation.
7. Rapidly double-click **Confirm Transfer**.
8. Open Transaction History.

## Expected Result

Only one transfer should be processed.

```text
Expected debit:
1,000.00 + 10.00 fee = 1,010.00

Expected final balance:
10,000.00 - 1,010.00 = 8,990.00
```

One transaction reference should exist.

## Actual Result

Two transfers are created.

```text
Transfer 1 = 1,000.00 + 10.00
Transfer 2 = 1,000.00 + 10.00

Actual final balance:
7,980.00
```

Two transaction references are generated.

## Business Impact

Customers may be charged twice for one intended transaction.

This can cause:

* Incorrect balances
* Financial loss
* Customer complaints
* Manual reconciliation
* Refund operations

## Suggested Regression

```text
REG-021
SEC-SUITE-052
SAN-017
```

---

# 5. BUG-002 — Transfer Fee Excluded From Balance Validation

## Title

```text
[Transfers] Available-balance validation ignores transfer fee
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-001
RISK-007
RISK-010
```

## Preconditions

```text
Available Balance = 5,000.00
Transfer Fee = 10.00
```

## Steps

1. Login.
2. Open transfer form.
3. Enter transfer amount `5,000.00`.
4. Submit.

## Expected

Total debit should be:

```text
5,000.00 + 10.00 = 5,010.00
```

Since available balance is only:

```text
5,000.00
```

the transfer should be rejected.

## Actual

Transfer is accepted because validation appears to check:

```text
transferAmount <= availableBalance
```

instead of:

```text
transferAmount + fee <= availableBalance
```

## Impact

Potential:

* Negative balance
* Invalid financial state
* Reconciliation failure

---

# 6. BUG-003 — Statement IDOR

## Title

```text
[Security] Customer can download another customer's statement by modifying statement ID
```

## Module

Statements / Security

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-002
RISK-021
RISK-047
```

## Preconditions

```text
Authenticated User:
CUST-001

Target Statement:
STAT-002

Owner:
CUST-002
```

## Steps

1. Login as `CUST-001`.
2. Download one owned statement.
3. Observe the statement identifier used in the request.
4. Replace it with `STAT-002`.
5. Submit the request.

## Expected

Access should be denied.

No statement belonging to `CUST-002` should be returned.

## Actual

The statement belonging to `CUST-002` downloads successfully.

## Security Impact

Unauthorized disclosure of:

* Financial transactions
* Account details
* Balances
* Customer banking information

## Suggested Regression

```text
REG-055
SEC-SUITE-017
SEC-SUITE-143
SEC-SUITE-144
```

---

# 7. BUG-004 — MFA Bypass Through API

## Title

```text
[Authentication] Protected account API is accessible before MFA completion
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-005
RISK-030
RISK-044
```

## Preconditions

```text
Customer has MFA enabled.
```

## Steps

1. Submit valid username/password.
2. Stop at MFA challenge.
3. Before entering OTP, call the protected Accounts API using the temporary authenticated session.
4. Observe response.

## Expected

Protected resource access should be denied until MFA is successfully completed.

## Actual

Accounts API returns customer account information.

## Security Impact

Password compromise alone may allow protected account access despite MFA being enabled.

---

# 8. BUG-005 — Account Freeze Uses Stale Transfer State

## Title

```text
[Accounts/Transfers] Transfer succeeds after source account is frozen
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-009
RISK-013
RISK-048
```

## Preconditions

```text
Account: ACC-001
State: ACTIVE
```

## Steps

1. Customer opens transfer confirmation.
2. Admin freezes `ACC-001`.
3. Verify account state becomes `FROZEN`.
4. Customer submits the already-open transfer.

## Expected

Final transfer submission should revalidate account state and reject the transaction.

## Actual

Transfer succeeds because the transfer page appears to rely on the state loaded before the freeze.

## Impact

Frozen-account restrictions can be bypassed through stale workflow state.

---

# 9. BUG-006 — Failed Payment Retains Balance Hold

## Title

```text
[Payments] Failed payment does not release reserved available balance
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-001
RISK-026
RISK-039
```

## Preconditions

```text
Current Balance: 10,000.00
Available Balance: 10,000.00

Payment Amount: 2,000.00
```

## Steps

1. Submit payment.
2. Force provider/system failure.
3. Wait until payment state becomes `FAILED`.
4. Refresh account balances.

## Expected

```text
Current Balance = 10,000.00
Available Balance = 10,000.00
```

Any temporary hold should be released.

## Actual

```text
Current Balance = 10,000.00
Available Balance = 8,000.00
```

The failed payment remains reserved.

## Impact

Customer funds become incorrectly unavailable.

---

# 10. BUG-007 — Frozen Card API Bypass

## Title

```text
[Cards] Frozen card transaction succeeds when submitted directly through API
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-024
RISK-030
RISK-037
```

## Preconditions

```text
Card: CARD-001
State: FROZEN
```

## Steps

1. Freeze `CARD-001`.
2. Verify UI shows `FROZEN`.
3. Submit card transaction request directly through relevant test API/interface.
4. Observe result.

## Expected

Transaction should be declined.

## Actual

Transaction is approved.

## Observation

UI prevents use, but backend does not enforce the card state.

## Impact

Security/business-rule enforcement exists only on the client.

---

# 11. BUG-008 — Duplicate Loan Disbursement

## Title

```text
[Loans] Concurrent disbursement requests credit approved loan twice
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-001
RISK-003
RISK-011
RISK-013
```

## Preconditions

```text
Loan: LOAN-001
State: APPROVED
Approved Amount: 100,000.00

Destination Account Opening Balance:
20,000.00
```

## Steps

1. Send two disbursement requests concurrently.
2. Wait for both responses.
3. Review account balance and loan transactions.

## Expected

Exactly one request should create financial disbursement.

Expected balance:

```text
120,000.00
```

## Actual

Both requests succeed.

Actual balance:

```text
220,000.00
```

## Impact

The system creates `100,000.00` of invalid funds.

This is a release blocker.

---

# 12. BUG-009 — Duplicate Deposit Maturity Payout

## Title

```text
[Deposits] Concurrent maturity workers credit deposit payout twice
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-001
RISK-003
RISK-012
RISK-013
```

## Preconditions

```text
Deposit: DEP-001
Principal: 50,000.00
Interest: 5,000.00

Expected Payout:
55,000.00
```

## Steps

1. Place deposit at maturity.
2. Trigger two maturity-processing requests concurrently.
3. Review settlement account.

## Expected

One payout:

```text
+55,000.00
```

## Actual

Two payouts:

```text
+110,000.00
```

## Impact

Money is created incorrectly.

---

# 13. BUG-010 — Statement Closing Balance Incorrect

## Title

```text
[Statements] Payment fee is excluded from statement closing balance
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-001
RISK-010
RISK-020
```

## Test Data

Opening:

```text
25,000.00
```

Transactions:

```text
Incoming Transfer +5,000.00
Payment -2,000.00
Payment Fee -20.00
Transfer -1,500.00
Refund +500.00
```

Expected:

```text
25,000
+ 5,000
- 2,000
- 20
- 1,500
+ 500
= 26,980.00
```

## Actual

Statement closing balance:

```text
27,000.00
```

Account balance:

```text
26,980.00
```

## Impact

Official statement does not reconcile with account balance.

---

# 14. BUG-011 — False Success Notification

## Title

```text
[Notifications] Failed payment generates payment-success notification
```

## Severity

High

## Priority

P1

## Related Risks

```text
RISK-034
RISK-048
```

## Steps

1. Submit payment.
2. Force provider failure.
3. Verify payment becomes `FAILED`.
4. Review customer notification.

## Expected

Notification should indicate failure, or no success message should be generated.

## Actual

Customer receives:

```text
Payment completed successfully.
```

## Impact

Customer is given false financial information.

---

# 15. BUG-012 — Read-Only Admin Privilege Escalation

## Title

```text
[Admin/Security] Read-only administrator can reverse transfer through direct API request
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-006
RISK-023
RISK-030
```

## Preconditions

```text
Admin:
ADMIN-005

Role:
READ_ONLY_ADMIN
```

## Steps

1. Login as read-only admin.
2. Open completed transfer.
3. Confirm reversal control is hidden in UI.
4. Send reversal API request directly.
5. Observe result.

## Expected

Request denied.

## Actual

Transfer is reversed successfully.

## Impact

UI permissions exist but backend RBAC enforcement is missing.

---

# 16. BUG-013 — Logout Does Not Revoke Other Tab

## Title

```text
[Sessions] Logged-out customer session remains usable in existing browser tab
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-014
RISK-030
```

## Steps

1. Login.
2. Open Account page in Tab A.
3. Open Transfer page in Tab B.
4. Logout from Tab A.
5. Submit transfer from Tab B.

## Expected

Transfer should be rejected because session was logged out.

## Actual

Transfer succeeds.

## Impact

Revoked sessions remain authorized for financial operations.

---

# 17. BUG-014 — Reversal Overwrites Original Transaction

## Title

```text
[Transactions] Reversing transfer replaces original transaction instead of preserving audit history
```

## Severity

High

## Priority

P1

## Related Risks

```text
RISK-019
RISK-022
RISK-039
```

## Steps

1. Complete transfer.
2. Record original reference.
3. Reverse transaction.
4. Open transaction history.

## Expected

History should contain:

```text
Original Transfer
+
Linked Reversal
```

## Actual

Original transfer row is replaced by:

```text
REVERSED
```

and no separate reversal event exists.

## Impact

Financial history and audit trail lose important event information.

---

# 18. BUG-015 — Timeout Retry Duplicates Transfer

## Title

```text
[Transfers] Retrying after client timeout creates second completed transfer
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-003
RISK-042
RISK-048
```

## Steps

1. Submit valid transfer.
2. Delay client response until UI times out.
3. Backend completes original transfer.
4. UI displays generic failure.
5. Customer selects Retry.
6. Review transactions.

## Expected

System should safely determine/reuse result of original transaction.

## Actual

Second transfer is processed.

## Impact

One intended action creates two debits.

---

# 19. BUG-016 — Cancelled Scheduled Transfer Executes

## Title

```text
[Scheduled Transfers] Cancelled scheduled transfer still executes at scheduled time
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-028
RISK-029
RISK-039
```

## Steps

1. Create scheduled transfer.
2. Wait until shortly before execution.
3. Cancel transfer.
4. Verify UI shows `CANCELLED`.
5. Wait until original scheduled time.
6. Review balance/history.

## Expected

No transfer should execute.

## Actual

Scheduled worker processes transfer despite cancellation.

## Impact

Unauthorized/unexpected movement of customer funds.

---

# 20. BUG-017 — Password Change Does Not Revoke Sessions

## Title

```text
[Profile/Security] Existing session remains authorized after password change despite configured revocation policy
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-014
RISK-021
```

## Preconditions

Two active sessions:

```text
Session A
Session B
```

## Steps

1. Login on Session A.
2. Login on Session B.
3. Change password on Session A.
4. Use Session B to submit financial action.

## Expected

If security policy requires session revocation, Session B should be invalidated.

## Actual

Session B remains fully authenticated.

## Security Impact

A previously compromised session may remain usable after customer changes password.

---

# 21. BUG-018 — Customer Can Modify KYC Status

## Title

```text
[API/Security] Customer can modify protected KYC status using profile update payload
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-006
RISK-030
RISK-037
```

## Steps

1. Login as customer with:

```text
KYC = PENDING
```

2. Submit profile update request containing:

```json
{
  "kycStatus": "VERIFIED"
}
```

3. Reload profile.

## Expected

Protected field should be rejected or ignored.

KYC should remain:

```text
PENDING
```

## Actual

KYC becomes:

```text
VERIFIED
```

## Impact

Customer can bypass compliance/product eligibility controls.

---

# 22. BUG-019 — Mobile Transfer Confirmation Hides Fee

## Title

```text
[Responsive UI] Transfer confirmation hides fee and total debit at 360×800 viewport
```

## Severity

High

## Priority

P1

## Related Risks

```text
RISK-036
RISK-048
```

## Environment

```text
Viewport:
360 × 800
```

## Steps

1. Open transfer flow at mobile viewport.
2. Enter valid transfer.
3. Continue to confirmation.
4. Observe confirmation modal.

## Expected

Customer should see:

* Source account
* Beneficiary
* Transfer amount
* Fee
* Total debit
* Confirm button

## Actual

Fee and total are below an inaccessible modal region.

Customer can still reach **Confirm**.

## Impact

Customer can approve a financial transaction without seeing complete charge information.

---

# 23. BUG-020 — Duplicate Transaction Reference

## Title

```text
[Database/Transactions] Concurrent transfers generate duplicate transaction references
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-003
RISK-033
RISK-039
```

## Preconditions

Two customers submit transfers concurrently.

## Steps

1. Prepare two independent valid transfers.
2. Submit at the same instant.
3. Inspect generated references.
4. Query persisted transaction records.

## Expected

Each transaction should have a globally or appropriately unique reference according to system design.

## Actual

Both transactions receive:

```text
TRF-20260913-10001
```

## Impact

Can cause:

* Reconciliation ambiguity
* Incorrect support investigation
* Incorrect transaction lookup
* Duplicate-key issues in downstream systems
* Audit confusion

---

# 24. BUG-021 — Payment Provider Success / Local Failure Mismatch

## Title

```text
[Payments] Provider confirms payment but local transaction remains FAILED
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-004
RISK-026
RISK-039
RISK-048
```

## Scenario

External provider:

```text
SUCCESS
```

Local system:

```text
FAILED
```

## Expected

System should reconcile to one authoritative outcome using the defined provider reconciliation process.

## Actual

Customer receives local failure while provider considers payment completed.

## Impact

Potential:

* Customer retry
* duplicate external settlement
* balance mismatch
* provider reconciliation failure

---

# 25. BUG-022 — Loan Installment Rounding Mismatch

## Title

```text
[Loans] Sum of installment schedule differs from displayed total repayable amount
```

## Severity

High

## Priority

P1

## Related Risks

```text
RISK-011
RISK-039
```

## Test Data

Example:

```text
Displayed Total Repayable:
105,000.00

Sum of Installments:
105,000.03
```

## Expected

Installment schedule should reconcile exactly to the authoritative total according to rounding rules.

## Actual

Difference:

```text
0.03
```

## Impact

Financial calculation inconsistency.

Small discrepancies may accumulate across many accounts.

---

# 26. BUG-023 — Deposit Early-Withdrawal Penalty Applied Twice

## Title

```text
[Deposits] Early-withdrawal penalty deducted twice from payout
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-001
RISK-012
```

## Example

```text
Principal = 50,000.00
Allowed Interest = 2,000.00
Penalty = 1,000.00
```

Expected:

```text
50,000 + 2,000 - 1,000
= 51,000.00
```

Actual:

```text
50,000 + 2,000 - 1,000 - 1,000
= 50,000.00
```

## Impact

Customer receives incorrect settlement amount.

---

# 27. BUG-024 — Daily Transfer Limit Race Condition

## Title

```text
[Transfers] Concurrent requests bypass daily transfer limit
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-008
RISK-013
```

## Preconditions

Example daily remaining limit:

```text
10,000.00
```

Two requests:

```text
Transfer A = 7,000.00
Transfer B = 7,000.00
```

## Expected

At most one should succeed if total allowed remaining limit is `10,000.00`.

## Actual

Both succeed.

Total transferred:

```text
14,000.00
```

## Impact

Server-side cumulative limit is bypassed under concurrency.

---

# 28. BUG-025 — Old OTP Remains Valid After Resend

## Title

```text
[Authentication] Previous MFA OTP remains valid after new OTP is issued
```

## Severity

High

## Priority

P1

## Related Risks

```text
RISK-044
RISK-045
```

## Steps

1. Request OTP A.
2. Select resend.
3. Receive OTP B.
4. Submit OTP A.

## Expected

If resend policy invalidates previous OTP, OTP A should fail.

## Actual

OTP A authenticates successfully.

## Impact

OTP lifecycle is weaker than configured security policy.

---

# 29. BUG-026 — Statement Download Still Accessible After Logout

## Title

```text
[Security/Statements] Previously generated statement URL remains accessible after logout
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-002
RISK-014
RISK-021
```

## Steps

1. Login.
2. Generate statement.
3. Copy download URL.
4. Logout.
5. Open URL directly.

## Expected

Authorization should still be required according to design.

## Actual

Statement downloads successfully without authenticated session.

## Impact

Sensitive financial information may remain exposed through reusable URLs.

---

# 30. BUG-027 — Audit Log Stores Access Token

## Title

```text
[Audit/Security] Authentication access token is stored in audit log request details
```

## Severity

Critical

## Priority

P0

## Related Risks

```text
RISK-021
RISK-032
```

## Steps

1. Perform administrative action.
2. Inspect authorized test-environment audit record.
3. Review stored request metadata.

## Expected

Authentication secrets should be redacted.

## Actual

Full bearer token is stored in audit payload.

## Impact

Any user/service with audit-log access may obtain reusable authentication credentials.

---

# 31. BUG-028 — Search Result Exposes Another Customer

## Title

```text
[Admin] Customer-support role can view fields restricted to higher-privileged administrators
```

## Severity

High

## Priority

P1

## Related Risks

```text
RISK-002
RISK-006
RISK-023
```

## Preconditions

Role:

```text
CUSTOMER_SUPPORT
```

## Expected

Only fields authorized for the role should be shown.

## Actual

Restricted KYC/internal-risk information is returned by the API and displayed.

## Impact

Excessive privilege/data exposure.

---

# 32. BUG-029 — Pagination Duplicates Transactions

## Title

```text
[Transaction History] New transaction inserted during paging causes duplicate and missing rows
```

## Severity

Medium

## Priority

P2

## Steps

1. Load page 1 of transaction history.
2. Create new transaction.
3. Navigate to page 2.
4. Compare results.

## Expected

Stable pagination strategy should avoid duplicates/missing records according to design.

## Actual

One transaction appears on both pages and another disappears from navigation.

## Impact

Customer may receive incomplete or misleading history.

---

# 33. BUG-030 — Arabic Alias Breaks Transfer Confirmation Layout

## Title

```text
[Responsive/Localization] Long Arabic beneficiary alias overlaps transfer amount in confirmation modal
```

## Severity

Medium

## Priority

P2

## Environment

```text
Language: Arabic
Viewport: 390 × 844
```

## Expected

RTL text should wrap safely without hiding financial information.

## Actual

Beneficiary alias overlaps the amount field.

## Impact

Financial confirmation becomes difficult to understand.

---

# 34. Defect Distribution by Category

| Category                | Sample Defects |
| ----------------------- | -------------: |
| Financial Integrity     |             12 |
| Security/Authorization  |              9 |
| Concurrency/Idempotency |              8 |
| API                     |              8 |
| Database/Data Integrity |              7 |
| UI/Responsive           |              2 |
| Notifications           |              1 |
| Browser/Session         |              3 |
| Calculations            |              4 |

A defect may belong to multiple categories.

---

# 35. Critical Sample Defect Set

The most important portfolio examples are:

```text
BUG-001 — Duplicate transfer
BUG-002 — Fee/balance validation
BUG-003 — Statement IDOR
BUG-004 — MFA bypass
BUG-005 — Frozen account stale-state transfer
BUG-006 — Failed payment balance hold
BUG-007 — Frozen card API bypass
BUG-008 — Duplicate loan disbursement
BUG-009 — Duplicate deposit maturity
BUG-010 — Statement reconciliation
BUG-012 — Admin privilege escalation
BUG-013 — Session revocation failure
BUG-015 — Timeout duplicate transfer
BUG-016 — Cancelled scheduled transfer executes
BUG-018 — Customer modifies KYC
BUG-020 — Duplicate transaction reference
BUG-024 — Limit race condition
BUG-026 — Statement accessible after logout
BUG-027 — Token stored in audit log
```

These demonstrate high-risk banking QA coverage beyond simple UI validation.

---

# 36. Mapping Samples to Automation

## UI Automation

Suitable candidates:

```text
BUG-001
BUG-005
BUG-013
BUG-019
BUG-030
```

Tools:

```text
Playwright
Cypress
Selenium
```

---

## API Automation

Suitable:

```text
BUG-002
BUG-003
BUG-004
BUG-007
BUG-012
BUG-015
BUG-018
BUG-025
BUG-026
```

Tools:

```text
REST Assured
Postman
```

---

## Database Validation

Strong candidates:

```text
BUG-001
BUG-008
BUG-009
BUG-010
BUG-014
BUG-020
BUG-023
BUG-024
```

---

## Concurrency / Performance Tooling

Candidates:

```text
BUG-008
BUG-009
BUG-020
BUG-024
```

Possible later tools:

```text
JMeter
REST Assured concurrent execution
custom test harness
```

---

# 37. Mapping Samples to Regression

| Defect  | Regression Focus                 |
| ------- | -------------------------------- |
| BUG-001 | Duplicate transfer/idempotency   |
| BUG-002 | Balance + fee validation         |
| BUG-003 | Resource ownership               |
| BUG-004 | MFA/auth state                   |
| BUG-005 | Account state revalidation       |
| BUG-006 | Failed transaction balance       |
| BUG-007 | Card state backend enforcement   |
| BUG-008 | Loan disbursement idempotency    |
| BUG-009 | Deposit payout idempotency       |
| BUG-010 | Statement reconciliation         |
| BUG-011 | Notification state consistency   |
| BUG-012 | RBAC                             |
| BUG-013 | Session invalidation             |
| BUG-014 | Transaction immutability         |
| BUG-015 | Retry recovery                   |
| BUG-016 | Scheduled state validation       |
| BUG-017 | Session security                 |
| BUG-018 | Protected-field validation       |
| BUG-020 | Transaction reference uniqueness |
| BUG-024 | Limits + concurrency             |
| BUG-026 | Download authorization           |
| BUG-027 | Secret redaction                 |

---

# 38. Sample Defect Retest Example

For:

```text
BUG-001
```

Retest:

1. Use build containing fix.
2. Repeat original double-click scenario.
3. Confirm one transaction.
4. Confirm one debit.
5. Confirm correct balance.
6. Confirm single reference.
7. Check API.
8. Check database.

Then run related regression:

```text
Normal transfer
Timeout retry
Browser refresh
Back navigation
Two-tab submission
API replay
Concurrent submission
```

---

# 39. Sample Security Defect Retest Example

For:

```text
BUG-003
```

Retest:

```text
Customer A → Customer B statement metadata
Customer A → Customer B statement PDF
Customer A → Customer B transaction
Customer A → Customer B account
Customer A → Customer B card
```

Also verify:

```text
Customer A can still access own resources.
```

The fix must not simply block all requests.

---

# 40. Sample Financial Defect Retest Example

For:

```text
BUG-010
```

Verify:

```text
Account balance

Transaction history

Statement opening balance

Statement fee row

Statement closing balance

API totals

Database values
```

Then test:

```text
No fees
One fee
Multiple fees
Reversal
Refund
Month boundary
```

---

# 41. Sample Concurrency Retest Example

For:

```text
BUG-008 — Duplicate loan disbursement
```

Run:

```text
2 concurrent requests
5 concurrent requests
Retry same request
Same idempotency key
Different idempotency keys for same approved loan
Timeout/retry
```

Expected:

```text
One disbursement only.
```

---

# 42. Portfolio Usage

These defects demonstrate experience with:

* Writing reproducible bug reports
* Financial reconciliation
* Security testing
* IDOR/authorization validation
* API testing
* Database validation
* Concurrency
* Idempotency
* Session management
* Cross-browser testing
* State transitions
* Risk-based severity
* Regression planning

Sample defects should always remain clearly labeled as simulated examples until reproduced against the actual system.

---

# 43. Final Sample Defect Principle

Banking defects should not be evaluated only by whether:

```text
A page looks wrong.
```

QA should determine whether the defect affects:

```text
Money

Authorization

Customer privacy

Lifecycle state

Database consistency

Audit history

Retry behavior

Concurrency

Downstream reconciliation
```

A seemingly small symptom can represent a major financial defect.

For example:

```text
"The success page timed out"
```

may actually mean:

```text
Backend completed transaction
+
customer thinks it failed
+
customer retries
+
duplicate debit occurs
```

The core rule is:

```text
Always investigate the authoritative financial
and security state behind the visible symptom.
```
