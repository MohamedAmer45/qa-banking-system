# Banking System — Exploratory Testing Charters

## 1. Document Information

| Field        | Value                          |
| ------------ | ------------------------------ |
| Project      | Banking System Testing Project |
| Testing Type | Exploratory Testing            |
| Document     | Exploratory Testing Charters   |
| Version      | 1.0                            |
| Status       | Draft                          |
| Owner        | QA Engineering                 |

---

# 2. Purpose

This document defines structured exploratory testing charters for the Banking System.

Exploratory testing is used to uncover defects that may not be discovered by scripted test cases alone.

The tester simultaneously:

```text
Learns the system
Designs tests
Executes tests
Observes behavior
Adjusts investigation
Documents findings
```

Exploratory testing is especially useful for:

* Unexpected user behavior
* Workflow interactions
* State inconsistencies
* Financial edge cases
* Usability defects
* Security weaknesses
* Error recovery
* Concurrency issues
* Cross-module defects
* Data inconsistencies

---

# 3. Exploratory Testing Principle

Each exploratory session should have:

```text
Mission
Scope
Timebox
Test data
Risks
Ideas to explore
Observations
Defects
Questions
Evidence
Conclusion
```

Exploratory testing should not mean random clicking.

It should remain:

```text
Purposeful
Risk-driven
Time-boxed
Documented
Repeatable enough to investigate findings
```

---

# 4. Charter Naming Convention

Charters use:

```text
ETC-XXX
```

Example:

```text
ETC-001 — Explore login behavior under unusual user interactions
```

---

# 5. Session Timeboxes

Recommended:

| Session Type       |   Duration |
| ------------------ | ---------: |
| Focused            | 30 minutes |
| Standard           | 60 minutes |
| Deep investigation | 90 minutes |

A session should normally focus on one specific mission.

---

# 6. Session Reporting

Each executed charter should later be recorded in:

```text
manual-testing/exploratory-testing/session-reports.md
```

Recommended session record:

```text
Session ID:
Charter ID:
Tester:
Date:
Environment:
Build:
Start Time:
End Time:
Test Data:
Areas Covered:
Observations:
Defects:
Questions:
Risks:
Evidence:
Conclusion:
Follow-up:
```

---

# 7. Heuristics Used

The exploratory sessions may use:

## CRUD

```text
Create
Read
Update
Delete
```

## SFDIPOT

```text
Structure
Function
Data
Interfaces
Platform
Operations
Time
```

## FEW HICCUPS

```text
Familiarity
Explainability
World
History
Image
Comparable Products
Claims
User Expectations
Product
Purpose
Standards
```

## User-behavior heuristics

```text
Repeat
Interrupt
Refresh
Navigate Back
Open Multiple Tabs
Double Click
Submit Quickly
Wait
Switch Accounts
Change State Mid-Flow
Disconnect Network
Retry
```

---

# 8. Authentication Exploratory Charters

## ETC-001 — Login Interaction Exploration

**Mission**

Explore login behavior using unusual but realistic customer interactions.

**Timebox:** 60 minutes

Explore:

* Valid login
* Invalid login
* Rapid repeated login
* Empty fields
* Spaces
* Upper/lower case
* Browser Back
* Refresh
* Multiple tabs
* Multiple login attempts
* Login after logout
* Expired session

Look for:

* Incorrect authentication
* Stale session
* Wrong error messages
* Account enumeration
* UI inconsistency
* Unexpected redirect loops

---

## ETC-002 — Lockout Exploration

**Mission**

Explore account-lockout behavior around failed authentication attempts.

**Timebox:** 45 minutes

Explore:

* Failures just below threshold
* Exact threshold
* Above threshold
* Correct password during lockout
* New browser after lockout
* Different device/session
* Password reset during lockout
* Unlock behavior

Look for:

* Lockout bypass
* Incorrect counters
* Missing notifications
* Incorrect error messages

---

## ETC-003 — MFA Exploration

**Mission**

Explore MFA challenge behavior and ways the user may interrupt or repeat it.

**Timebox:** 60 minutes

Explore:

* Valid OTP
* Invalid OTP
* Expired OTP
* Reused OTP
* Resend
* Multiple resend requests
* Browser refresh
* Browser Back
* New tab
* Direct dashboard URL
* API request before completing MFA

Look for:

* MFA bypass
* OTP reuse
* Old OTP still valid
* Incorrect attempt counting
* Session authenticated before verification

---

## ETC-004 — Password Reset Exploration

**Mission**

Explore password-reset token lifecycle and recovery behavior.

**Timebox:** 60 minutes

Explore:

* Multiple reset requests
* Older token vs latest token
* Expired token
* Reused token
* Refresh reset page
* Back/forward navigation
* Password policy
* Reset while logged in elsewhere
* Concurrent reset attempts

Look for:

* Reusable token
* Token leakage
* Old password still valid
* Existing sessions remaining valid unexpectedly

---

# 9. Session Management Exploratory Charters

## ETC-005 — Session Expiration Exploration

**Mission**

Explore how the application behaves when an authenticated session expires during different workflows.

**Timebox:** 60 minutes

Try expiration during:

* Dashboard
* Transfer form
* Payment form
* Statement generation
* Password change
* Card freeze
* Loan repayment

Look for:

* Action succeeding after expiration
* Partial transactions
* Data loss
* Incorrect success messages

---

## ETC-006 — Multi-Session Exploration

**Mission**

Explore the same customer using multiple browser sessions simultaneously.

**Timebox:** 60 minutes

Explore:

* Change profile in one session
* Transfer in another
* Freeze card in one session
* Purchase from another
* Logout one session
* Revoke one session
* Password change
* MFA change

Look for:

* Stale authorization
* Unexpected session survival
* State inconsistency

---

# 10. Customer Profile Exploratory Charters

## ETC-007 — Profile Data Exploration

**Mission**

Explore profile fields using unusual but valid and invalid data.

**Timebox:** 60 minutes

Try:

* Long names
* Arabic
* Accented characters
* Leading/trailing spaces
* Empty fields
* Special characters
* Multiple saves
* Refresh before save
* Simultaneous updates

Look for:

* Data corruption
* Incorrect validation
* Encoding problems
* Lost updates

---

## ETC-008 — Security-Sensitive Profile Changes

**Mission**

Explore email, phone, password, and MFA changes.

**Timebox:** 60 minutes

Focus on:

* Verification
* Old contact information
* Notifications
* Concurrent sessions
* Reauthentication
* Replay of verification codes

Look for:

* Account takeover paths
* Unverified contact becoming trusted
* Missing security notifications

---

# 11. Account Exploratory Charters

## ETC-009 — Account State Exploration

**Mission**

Explore how account behavior changes across:

```text
ACTIVE
FROZEN
RESTRICTED
SUSPENDED
CLOSED
```

**Timebox:** 90 minutes

Try:

* View
* Transfer
* Payment
* Card transaction
* Statement
* Deposit
* Loan repayment

Look for:

* Operations permitted from invalid states
* UI/backend disagreement
* State changes not taking effect immediately

---

## ETC-010 — Balance Integrity Exploration

**Mission**

Explore whether account balances remain financially correct after unusual sequences.

**Timebox:** 90 minutes

Try sequences such as:

```text
Transfer
→ Payment
→ Refund
→ Loan repayment
→ Incoming transfer
→ Reversal
```

Compare:

* Current balance
* Available balance
* Transaction history
* Statement
* API
* Database if available

Look for:

* Missing debit
* Duplicate debit
* Incorrect available balance
* Rounding drift

---

# 12. Beneficiary Exploratory Charters

## ETC-011 — Beneficiary Lifecycle Exploration

**Mission**

Explore beneficiary creation through deletion.

**Timebox:** 60 minutes

Explore:

```text
Create
Verify
Cooldown
Activate
Edit
Disable
Re-enable
Delete
```

Try using beneficiary at each state.

Look for:

* Activation bypass
* Deleted beneficiary usage
* Cooldown bypass
* Duplicate beneficiary creation

---

## ETC-012 — Beneficiary Manipulation Exploration

**Mission**

Explore how beneficiary identifiers and destination data are validated.

**Timebox:** 60 minutes

Try:

* Changing beneficiary ID
* Changing destination account
* Switching account after confirmation
* Using another customer's beneficiary
* Replaying old request

Look for:

* Wrong recipient transfer
* IDOR
* stale-data acceptance

---

# 13. Transfer Exploratory Charters

## ETC-013 — Transfer Happy-Path Variation

**Mission**

Explore valid transfers using many combinations of valid data.

**Timebox:** 60 minutes

Vary:

* Source accounts
* Beneficiaries
* Amounts
* Notes
* Transfer types
* Fees
* Timing

Look for inconsistent behavior despite valid combinations.

---

## ETC-014 — Insufficient Funds Exploration

**Mission**

Explore transfer behavior around available-balance exhaustion.

**Timebox:** 60 minutes

Try:

* Exact balance
* Just below
* Just above
* Balance including fee
* Existing holds
* Concurrent transactions

Look for:

* Negative balances
* Fee-related overspending
* inconsistent UI/API validation

---

## ETC-015 — Duplicate Transfer Exploration

**Mission**

Attempt to accidentally or intentionally create duplicate transfers.

**Timebox:** 90 minutes

Try:

* Double click
* Refresh confirmation
* Browser Back + resubmit
* Network timeout + retry
* Same request in two tabs
* Replay same API request
* Concurrent submission

Look for:

* Duplicate debit
* Duplicate credit
* Duplicate notification
* Multiple references for one intended action

---

## ETC-016 — Transfer Interruption Exploration

**Mission**

Explore interruptions during money movement.

**Timebox:** 90 minutes

Interrupt at:

```text
Before confirmation
During submission
While processing
After debit
Before UI confirmation
```

Use:

* Network disconnect
* Refresh
* Tab close
* Browser crash simulation
* API timeout

Look for:

* Partial transfer
* ambiguous state
* incorrect retry handling

---

## ETC-017 — Transfer State Change Exploration

**Mission**

Change related resource states while transfer is underway.

**Timebox:** 90 minutes

Examples:

```text
Freeze source account
Disable beneficiary
Suspend customer
Hit daily limit from another session
Delete beneficiary
```

Look for:

* stale validation
* transfer completing despite invalid current state

---

## ETC-018 — Scheduled Transfer Exploration

**Mission**

Explore scheduled-transfer creation, execution, and cancellation.

**Timebox:** 90 minutes

Focus on:

* Date/time boundaries
* Timezones
* Insufficient execution-time balance
* Source freeze before execution
* Beneficiary change
* Cancellation near execution time

---

## ETC-019 — Recurring Transfer Exploration

**Mission**

Explore recurring instructions over multiple occurrences.

**Timebox:** 90 minutes

Try:

* End dates
* Pausing
* Cancellation
* Failed occurrence
* Later recovery
* Account state changes
* Duplicate scheduled workers

Look for:

* extra occurrence
* missed occurrence
* recurrence after cancellation

---

# 14. Payment Exploratory Charters

## ETC-020 — Payment Processing Exploration

**Mission**

Explore successful and unsuccessful payment processing.

**Timebox:** 60 minutes

Try:

* Different payees
* Bill references
* Exact balance
* Fee
* Multiple payments
* Refresh/retry

Look for:

* duplicate payment
* incorrect payee
* balance mismatch

---

## ETC-021 — Duplicate Bill Payment Exploration

**Mission**

Investigate whether the same unique bill can be paid more than once unintentionally.

**Timebox:** 60 minutes

Try:

* Two tabs
* Two devices
* Double submit
* Timeout retry
* Concurrent API calls

Look for:

* duplicate settlement
* duplicate debit

---

## ETC-022 — Provider Failure Exploration

**Mission**

Explore behavior when external payment provider responses are delayed, failed, or ambiguous.

**Timebox:** 90 minutes

Look for:

* Local debit + external rejection
* External success + local failure
* Retry causing duplicate provider payment
* Incorrect status reconciliation

---

# 15. Card Exploratory Charters

## ETC-023 — Card Lifecycle Exploration

**Mission**

Explore:

```text
INACTIVE
ACTIVE
FROZEN
BLOCKED
EXPIRED
CANCELLED
```

**Timebox:** 90 minutes

Try actions at each state:

* Purchase
* Freeze
* Unfreeze
* Block
* Replace
* Change limit

Look for illegal transitions.

---

## ETC-024 — Card Freeze Race Exploration

**Mission**

Explore card transactions while freeze/block operations occur.

**Timebox:** 60 minutes

Try:

```text
Purchase immediately before freeze
Purchase during freeze
Purchase immediately after freeze
```

Look for:

* post-freeze approvals
* inconsistent UI/API state

---

## ETC-025 — Card Limit Exploration

**Mission**

Explore daily/card/channel limits with multiple transactions.

**Timebox:** 60 minutes

Try:

* Exact limit
* Repeated smaller purchases
* Concurrent purchases
* Limit change while purchase is processing

Look for:

* cumulative limit bypass

---

## ETC-026 — Card Replacement Exploration

**Mission**

Explore lost/stolen/expired card replacement.

**Timebox:** 60 minutes

Check:

* Old card behavior
* New card
* Pending transactions
* Refunds to old card
* Duplicate replacement

---

# 16. Loan Exploratory Charters

## ETC-027 — Loan Application Exploration

**Mission**

Explore loan application using varied customer states and application data.

**Timebox:** 90 minutes

Vary:

* Amount
* Term
* Income
* KYC
* Customer state
* Existing debt

Look for inconsistent eligibility decisions.

---

## ETC-028 — Loan Review Race Exploration

**Mission**

Explore simultaneous administrative decisions.

**Timebox:** 60 minutes

Try:

```text
Approve vs approve
Approve vs reject
Cancel vs approve
```

Look for:

* conflicting final state
* duplicate disbursement

---

## ETC-029 — Loan Disbursement Exploration

**Mission**

Explore exactly-once loan disbursement.

**Timebox:** 60 minutes

Try:

* Double approve
* Double disburse
* Timeout
* Refresh
* Closed destination account
* Customer restricted after approval

Look for duplicate credits.

---

## ETC-030 — Loan Repayment Exploration

**Mission**

Explore repayment sequences.

**Timebox:** 90 minutes

Try:

* Normal installment
* Partial repayment
* Early settlement
* Exact final payment
* Overpayment
* Concurrent final payments

Look for:

* residual balances
* incorrect closure
* duplicate repayment

---

## ETC-031 — Overdue Loan Exploration

**Mission**

Explore transition to past-due/default states and recovery.

**Timebox:** 60 minutes

Look for:

* incorrect penalty
* incorrect state transition
* payment not restoring eligible status

---

# 17. Deposit Exploratory Charters

## ETC-032 — Deposit Opening Exploration

**Mission**

Explore deposit creation with varied funding conditions.

**Timebox:** 60 minutes

Try:

* Exact available balance
* Amount limits
* Funding account state changes
* Duplicate submission
* Network interruption

Look for:

* duplicate principal debit
* deposit without funding

---

## ETC-033 — Deposit Maturity Exploration

**Mission**

Explore maturity processing.

**Timebox:** 90 minutes

Try:

* Normal maturity
* Delayed worker
* Duplicate worker
* Closed settlement account
* Maturity at month/year boundaries

Look for:

* duplicate payout
* missing interest
* maturity-state inconsistencies

---

## ETC-034 — Early Withdrawal Exploration

**Mission**

Explore early withdrawal and penalty calculations.

**Timebox:** 60 minutes

Try:

* Before minimum holding period
* Exact boundary
* After boundary
* Concurrent early-withdrawal requests

Look for:

* duplicate payout
* incorrect penalty
* incorrect interest

---

## ETC-035 — Renewal vs Payout Race

**Mission**

Explore auto-renewal and maturity payout occurring concurrently.

**Timebox:** 60 minutes

Look for:

```text
Deposit renewed and paid out simultaneously
Duplicate principal
Invalid final state
```

---

# 18. Transaction History Exploratory Charters

## ETC-036 — Transaction History Integrity Exploration

**Mission**

Explore transaction history after complex customer activity.

**Timebox:** 90 minutes

Generate:

* Transfers
* Payments
* Fees
* Reversals
* Card transactions
* Loan events
* Deposit events

Look for:

* Missing rows
* Duplicate rows
* Incorrect debit/credit direction
* Wrong status
* Wrong references

---

## ETC-037 — Search / Filter / Pagination Exploration

**Mission**

Explore transaction-history navigation with changing datasets.

**Timebox:** 60 minutes

Try:

* Filters
* Search
* Pagination
* New transactions during paging
* Same timestamp transactions
* Large dataset

Look for gaps and duplicates.

---

## ETC-038 — Transaction Reversal Exploration

**Mission**

Explore history before and after reversals.

**Timebox:** 60 minutes

Look for:

* Original row overwritten
* Reversal not linked
* balance/history mismatch

---

# 19. Statement Exploratory Charters

## ETC-039 — Statement Reconciliation Exploration

**Mission**

Generate statement after varied financial activity and manually reconcile.

**Timebox:** 90 minutes

Verify:

```text
Opening Balance
+ Credits
- Debits
= Closing Balance
```

Compare with:

* Account balance
* Transaction history
* API
* Database if available

---

## ETC-040 — Statement Boundary Exploration

**Mission**

Explore unusual date periods.

**Timebox:** 60 minutes

Try:

* Single day
* Month end
* Year end
* Leap day
* Empty period
* Maximum range
* Future range

---

## ETC-041 — Statement Download Exploration

**Mission**

Explore PDF/download behavior.

**Timebox:** 60 minutes

Try:

* Large statements
* Multiple downloads
* Session expiry
* Another account
* Old download URL
* Mobile browser

Look for authorization or data consistency issues.

---

# 20. Notification Exploratory Charters

## ETC-042 — Notification Accuracy Exploration

**Mission**

Generate many banking events and compare notifications with authoritative state.

**Timebox:** 90 minutes

Look for:

* Wrong amount
* Wrong status
* Wrong account
* Wrong customer
* Missing notification
* Duplicate notification

---

## ETC-043 — Notification Failure Exploration

**Mission**

Simulate notification provider failures.

**Timebox:** 60 minutes

Explore:

* Email failure
* SMS failure
* Retry
* Permanent failure

Verify financial transaction remains independent.

---

## ETC-044 — Notification Preference Exploration

**Mission**

Change preferences while events occur.

**Timebox:** 60 minutes

Look for:

* Preferences ignored
* Mandatory security alerts suppressed
* inconsistent channel delivery

---

# 21. Security Exploratory Charters

## ETC-045 — Authorization Exploration

**Mission**

Explore whether customer-owned resources can be accessed across customers.

**Timebox:** 90 minutes

Target:

* Accounts
* Beneficiaries
* Cards
* Transactions
* Statements
* Loans
* Deposits
* Notifications

Try manipulating resource identifiers.

Expected:

Strict authorization.

---

## ETC-046 — Client-Side Trust Exploration

**Mission**

Explore whether browser-controlled values can override server business rules.

**Timebox:** 90 minutes

Try modifying:

* Amount
* Fee
* Limit
* Customer ID
* Role
* Account ID
* Beneficiary
* Interest rate
* Status

Look for backend trust of client data.

---

## ETC-047 — Input Safety Exploration

**Mission**

Explore text and search inputs with unusual content.

**Timebox:** 60 minutes

Use controlled test strings containing:

* HTML-like syntax
* Script-like syntax
* SQL-like syntax
* Unicode
* Emoji
* Very long strings
* Null-like values

Expected:

Safe handling.

---

## ETC-048 — Sensitive Data Exposure Exploration

**Mission**

Search UI, APIs, errors, logs available to tester, URLs, exports, and notifications for excessive sensitive data.

**Timebox:** 60 minutes

Look for:

* Password
* Password hash
* Tokens
* OTP
* MFA secret
* Full PAN
* CVV
* PIN
* Internal stack traces

---

## ETC-049 — Replay Exploration

**Mission**

Explore repeated submission of sensitive financial requests.

**Timebox:** 90 minutes

Target:

* Transfer
* Payment
* Loan disbursement
* Deposit creation
* Deposit maturity
* Reversal

Look for duplicate financial effects.

---

# 22. Admin Exploratory Charters

## ETC-050 — Role Permission Exploration

**Mission**

Explore administrative functionality using different admin roles.

**Timebox:** 90 minutes

Try each role against:

* Customer changes
* KYC
* Accounts
* Cards
* Loans
* Reversals
* Audit logs
* Limits

Look for privilege escalation.

---

## ETC-051 — Admin Stale Data Exploration

**Mission**

Keep administrative pages open while resource states change elsewhere.

**Timebox:** 60 minutes

Examples:

* Loan decision
* Account balance
* Customer state
* Transaction reversal

Look for stale action overwriting current state.

---

## ETC-052 — Admin Destructive Action Exploration

**Mission**

Explore freeze, close, suspend, block, reject, and reverse actions.

**Timebox:** 90 minutes

Look for:

* Missing confirmation
* Wrong target
* missing reason
* duplicate action
* missing audit

---

## ETC-053 — Audit Trail Exploration

**Mission**

Perform critical operations and investigate resulting audit records.

**Timebox:** 60 minutes

Check:

* Actor
* Target
* Old state
* New state
* Timestamp
* Reason
* Result
* Sensitive-data redaction

---

# 23. Concurrency Exploratory Charters

## ETC-054 — Balance Race Exploration

**Mission**

Explore multiple simultaneous debits against limited balance.

**Timebox:** 90 minutes

Examples:

```text
Transfer + Transfer
Transfer + Payment
Payment + Card Purchase
Deposit Opening + Transfer
Loan Repayment + Payment
```

Look for:

* Negative balance
* double spending
* missing transaction

---

## ETC-055 — State Change Race Exploration

**Mission**

Explore financial operations at the same time as state changes.

**Timebox:** 90 minutes

Examples:

```text
Transfer + Account Freeze
Purchase + Card Block
Loan Disbursement + Customer Suspension
Deposit Maturity + Account Closure
```

---

## ETC-056 — Duplicate Worker Exploration

**Mission**

Explore backend-style duplicate processing.

**Timebox:** 60 minutes

Simulate/retry:

* Scheduled transfer worker
* Payment worker
* Loan disbursement worker
* Maturity worker
* Notification worker

Look for duplicate effects.

---

# 24. Error Recovery Exploratory Charters

## ETC-057 — Network Failure Exploration

**Mission**

Interrupt network communication during critical workflows.

**Timebox:** 90 minutes

Apply to:

* Login
* Transfer
* Payment
* Card change
* Loan repayment
* Deposit creation
* Statement download

Observe:

* UI state
* retry behavior
* backend state
* duplicate prevention

---

## ETC-058 — Server Error Exploration

**Mission**

Explore handling of controlled server-side failures.

**Timebox:** 60 minutes

Look for:

* false success
* stack traces
* duplicate retry
* unrecoverable stale UI

---

## ETC-059 — Dependency Failure Exploration

**Mission**

Explore behavior when dependent services are unavailable.

**Timebox:** 90 minutes

Examples:

* Notification provider
* Payment provider
* statement generator
* authorization service
* loan service

Critical principle:

```text
Financial operations must fail safely and deterministically.
```

---

# 25. Cross-Browser Exploratory Charter

## ETC-060 — Browser Difference Exploration

**Mission**

Explore critical customer journeys across supported browsers without following a rigid script.

**Timebox:** 90 minutes

Browsers:

```text
Chrome
Edge
Firefox
WebKit/Safari where supported
```

Focus on:

* Forms
* Modals
* Dates
* File downloads
* Navigation
* Validation
* Responsive layout

---

# 26. Responsive Exploratory Charter

## ETC-061 — Mobile and Tablet Exploration

**Mission**

Explore banking functionality using smaller viewports.

**Timebox:** 90 minutes

Focus on:

* Transfers
* Payments
* Statements
* Cards
* Profile
* Notifications

Look for:

* Hidden buttons
* clipped amounts
* overlapping fields
* confirmation data not visible
* horizontal scrolling

---

# 27. Accessibility Exploratory Charter

## ETC-062 — Keyboard and Accessibility Exploration

**Mission**

Explore primary banking journeys without relying on mouse interactions.

**Timebox:** 60 minutes

Check:

* Tab order
* Focus
* Labels
* Error association
* Modals
* Status indicators
* Keyboard activation

Critical information should not rely only on color.

---

# 28. Usability Exploratory Charter

## ETC-063 — Financial Confidence Exploration

**Mission**

Explore whether a normal customer can confidently understand what will happen before and after a financial operation.

**Timebox:** 60 minutes

Ask:

```text
Which account am I using?

Who receives the money?

How much will leave my account?

What fee applies?

When will it happen?

Did it succeed?

What is the reference?

Can I verify it later?
```

Look for confusing or ambiguous UX.

---

# 29. End-to-End Banking Journey Charters

## ETC-064 — New Customer Journey

**Mission**

Explore the experience from customer registration to first successful transfer.

**Timebox:** 90 minutes

Flow:

```text
Register
→ Verify identity/contact
→ Login
→ Complete MFA
→ View account
→ Add beneficiary
→ Wait/activate beneficiary
→ Transfer
→ Verify history
→ Verify statement
→ Verify notification
```

---

## ETC-065 — Active Banking Customer Journey

**Mission**

Explore a realistic multi-operation day.

**Timebox:** 90 minutes

Example:

```text
Login
→ View Accounts
→ Transfer Money
→ Pay Bill
→ Freeze Card
→ Review Transactions
→ Download Statement
→ Change Notification Setting
→ Logout
```

---

## ETC-066 — Loan Customer Journey

**Mission**

Explore the full loan experience.

**Timebox:** 90 minutes

```text
Apply
→ Review
→ Approve
→ Disburse
→ Verify Account Credit
→ Generate Installments
→ Repay
→ View Statement
→ Final Settlement
→ Close Loan
```

---

## ETC-067 — Deposit Customer Journey

**Mission**

Explore full deposit lifecycle.

**Timebox:** 90 minutes

```text
Open Deposit
→ Verify Funding Debit
→ View Deposit
→ Reach Maturity
→ Process Payout/Renewal
→ Verify Transaction History
→ Verify Statement
```

---

# 30. Financial Integrity Exploratory Charter

## ETC-068 — Cross-Module Reconciliation

**Mission**

Perform multiple financial activities and determine whether every layer reconciles.

**Timebox:** 90 minutes

Use:

```text
Opening balance = known value
```

Then perform:

```text
Transfer
Payment
Fee
Card transaction
Loan repayment
Incoming credit
Reversal
```

Calculate expected final balance manually.

Compare:

```text
Dashboard
Account detail
Transaction history
Statement
API
Database
```

Any unexplained financial difference is a P0 investigation.

---

# 31. Time and Date Exploratory Charter

## ETC-069 — Temporal Behavior Exploration

**Mission**

Explore banking functions around important date/time boundaries.

**Timebox:** 90 minutes

Focus on:

* Midnight
* Month end
* Year end
* Leap year
* Session timeout
* OTP expiry
* Beneficiary cooldown
* Scheduled transfers
* Deposit maturity
* Loan due dates

Look for off-by-one-time defects.

---

# 32. Precision Exploratory Charter

## ETC-070 — Financial Precision and Rounding Exploration

**Mission**

Explore calculations producing fractional monetary values.

**Timebox:** 60 minutes

Target:

* Fees
* Loan interest
* Deposit interest
* Penalties
* Refunds
* Totals

Look for:

```text
Floating-point artifacts
Rounding differences
0.01 discrepancies
UI/API/DB disagreement
```

---

# 33. Data Persistence Exploratory Charter

## ETC-071 — Persistence and Refresh Exploration

**Mission**

Explore whether successful and failed changes persist correctly.

**Timebox:** 60 minutes

After actions:

```text
Refresh
Logout/Login
Use second device
Call API
Check database
```

Look for:

* false success
* lost data
* stale data
* inconsistent state

---

# 34. Browser Navigation Exploratory Charter

## ETC-072 — Back / Forward / Refresh Exploration

**Mission**

Aggressively use browser navigation during critical workflows.

**Timebox:** 60 minutes

Target:

* Login
* Transfer
* Payment
* Beneficiary
* Card replacement
* Loan application
* Deposit creation

Look for:

* duplicate form submission
* replay
* stale confirmation
* unauthorized cached data

---

# 35. Large Dataset Exploratory Charter

## ETC-073 — High-Volume Data Exploration

**Mission**

Explore usability and correctness using accounts with many records.

**Timebox:** 90 minutes

Use:

* Thousands of transactions
* Hundreds of notifications
* Many beneficiaries
* Many audit events

Explore:

* Search
* Filters
* Sorting
* Pagination
* Export

Look for:

* duplicates
* missing rows
* slow unusable behavior
* inconsistent counts

---

# 36. Data Isolation Exploratory Charter

## ETC-074 — Customer Isolation Exploration

**Mission**

Use two test customers extensively and attempt to cause cross-customer data exposure.

**Timebox:** 90 minutes

Explore:

* Shared browser
* Two tabs
* changed IDs
* deep links
* cached pages
* downloads
* notifications
* API calls

Critical expected result:

```text
Customer A never receives or accesses Customer B's protected banking data.
```

---

# 37. Role Isolation Exploratory Charter

## ETC-075 — Admin Role Isolation Exploration

**Mission**

Switch between admin roles and investigate cached/hidden privileged actions.

**Timebox:** 90 minutes

Look for:

* role permissions cached from prior admin
* hidden UI action still callable
* API permissions inconsistent with UI

---

# 38. Exploratory Charter Priorities

## P0 Charters

Execute frequently:

```text
ETC-003 MFA
ETC-005 Session Expiration
ETC-009 Account State
ETC-010 Balance Integrity
ETC-014 Insufficient Funds
ETC-015 Duplicate Transfer
ETC-016 Transfer Interruption
ETC-017 Transfer State Changes
ETC-021 Duplicate Payment
ETC-024 Card Freeze Race
ETC-029 Loan Disbursement
ETC-033 Deposit Maturity
ETC-045 Authorization
ETC-046 Client-Side Trust
ETC-049 Replay
ETC-054 Balance Race
ETC-055 State Change Race
ETC-068 Financial Reconciliation
ETC-074 Customer Isolation
```

---

# 39. Regression Exploratory Sessions

Exploratory testing should be repeated after major changes affecting:

```text
Authentication
Transfers
Payments
Balances
Cards
Loans
Deposits
Authorization
Admin
Database
Transaction processing
```

Recommended:

```text
2–4 targeted exploratory sessions
```

after significant high-risk releases.

---

# 40. Exploratory Bug Evidence

When a defect is found, record:

```text
Charter ID
Session ID
Build
Environment
Customer/account data IDs
Exact sequence
Actual behavior
Expected behavior
Screenshots/video
Request/response where relevant
Logs
Transaction references
Timestamps
Balance before
Balance after
```

Financial defects require especially strong evidence.

---

# 41. Exploratory Severity Guidance

## Critical

Examples:

```text
Unauthorized account access
Duplicate debit
Incorrect balance
Money created/lost
Authentication bypass
Duplicate loan disbursement
Duplicate maturity payout
```

## High

Examples:

```text
Card freeze ineffective
Incorrect statement
Failed transaction displayed as success
Major workflow unavailable
```

## Medium

Examples:

```text
Incorrect filter
Notification delay
Poor error message
```

## Low

Examples:

```text
Cosmetic alignment
Minor formatting issue
```

---

# 42. Questions to Ask During Exploration

Continuously ask:

```text
What if I do this twice?

What if I refresh?

What if the network fails?

What if another session changes the data?

What if this account becomes frozen?

What if this request is delayed?

What if I submit the same request again?

What if I change the identifier?

What if another customer owns this object?

What if the balance is exactly enough?

What if the state changes between review and confirmation?

What if the backend succeeds but the UI fails?

What if the UI succeeds but the backend fails?

What if the notification is delayed?

What if the user comes back tomorrow?
```

---

# 43. Exploratory Testing and Automation

Exploratory findings should feed automation.

When exploration discovers:

```text
Important bug
High-risk edge case
Repeatable regression condition
```

create automated coverage using:

* Selenium
* Cypress
* Playwright
* Jest
* Postman
* REST Assured

Example:

```text
Exploration discovers double-click duplicates transfer
```

Follow-up:

```text
Create regression UI/API automated test for duplicate submission.
```

---

# 44. Exploratory Testing and Requirements

If exploration reveals undefined behavior, record it as a question rather than automatically treating it as a defect.

Examples:

```text
Should frozen accounts receive incoming transfers?

What happens to scheduled transfer when beneficiary is disabled?

Should MFA resend invalidate the previous code?

Does password change invalidate all sessions?

What happens when maturity settlement account is closed?
```

These should feed:

```text
Requirements clarification
Business rule update
Test-case update
Automation update
```

---

# 45. Exploratory Testing and Risk Assessment

New risks discovered during exploration should be added to:

```text
manual-testing/test-planning/risk-assessment.md
```

Example:

```text
Exploration finds transaction retry after gateway timeout can produce duplicate debit
```

This should become:

```text
New risk
New regression case
New automated coverage
Potential new monitoring requirement
```

---

# 46. Exit Criteria for an Exploratory Session

A session can end when:

* Timebox expires.
* Charter mission has been sufficiently explored.
* A major defect blocks further investigation.
* A new focused charter is needed.
* Required data/environment becomes unavailable.

Always record:

```text
What was covered
What was not covered
What was found
What needs follow-up
```

---

# 47. Exploratory Testing Coverage Summary

These charters cover:

* Authentication
* Lockout
* MFA
* Password reset
* Sessions
* Profile
* Accounts
* Balance integrity
* Beneficiaries
* Transfers
* Scheduled transfers
* Recurring transfers
* Payments
* Provider failures
* Cards
* Card states
* Limits
* Replacement
* Loans
* Disbursement
* Repayment
* Deposits
* Maturity
* Early withdrawal
* Transaction history
* Statements
* Notifications
* Security
* Authorization
* Replay
* Admin
* Audit
* Concurrency
* Error recovery
* Browsers
* Responsive UI
* Accessibility
* Usability
* Financial precision
* Date/time
* Large datasets
* Data isolation
* Role isolation
* End-to-end journeys

---

# 48. Final Exploratory Testing Principle

Exploratory testing in a banking system should focus heavily on interactions between:

```text
Money
State
Time
Authorization
Concurrency
Failures
Retries
```

A workflow that behaves correctly during a clean scripted test may fail when:

```text
The user refreshes

The request times out

A second session changes the state

A transaction is submitted twice

The account is frozen

The balance changes

The beneficiary is disabled

A service becomes unavailable

The user navigates backward

The same request is replayed
```

The core exploratory principle for this project is:

```text
Explore what happens when normal banking workflows
are interrupted, repeated, combined, delayed,
manipulated, or executed from unexpected states.
```

The objective is not simply to find UI defects.

The objective is to uncover conditions that could compromise:

```text
Financial integrity
Customer security
Data privacy
System consistency
Auditability
Customer trust
```
