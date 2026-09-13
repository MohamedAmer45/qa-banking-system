# Banking System — Boundary Value Analysis

## 1. Document Information

| Field                 | Value                          |
| --------------------- | ------------------------------ |
| Project               | Banking System Testing Project |
| Test Design Technique | Boundary Value Analysis        |
| Document              | Test Design                    |
| Version               | 1.0                            |
| Status                | Draft                          |
| Owner                 | QA Engineering                 |

---

# 2. Purpose

This document applies **Boundary Value Analysis (BVA)** to the Banking System.

Boundary Value Analysis focuses testing around the edges of valid and invalid ranges because defects frequently occur at:

* Minimum values
* Maximum values
* Just below minimum
* Just above minimum
* Just below maximum
* Just above maximum
* Exact dates
* Exact counters
* Exact financial limits
* Exact string lengths
* Exact timeouts
* Exact lifecycle thresholds

For banking applications, boundary defects are especially important because they can affect:

* Transfer limits
* Account balances
* Fees
* Loan amounts
* Deposit amounts
* Password rules
* OTP rules
* Daily limits
* Card limits
* Scheduled transactions
* Interest calculations

---

# 3. Boundary Value Analysis Principle

For a valid range:

```text
Minimum <= Value <= Maximum
```

Typical boundary values are:

```text
Minimum - 1
Minimum
Minimum + 1

Maximum - 1
Maximum
Maximum + 1
```

For financial decimal values, the smallest meaningful unit is usually:

```text
0.01
```

Therefore:

```text
Minimum - 0.01
Minimum
Minimum + 0.01

Maximum - 0.01
Maximum
Maximum + 0.01
```

For integer fields:

```text
Min - 1
Min
Min + 1
Max - 1
Max
Max + 1
```

---

# 4. Scenario Naming Convention

Boundary-analysis scenarios use:

```text
BVA-XXX
```

Priority:

```text
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 5. Transfer Amount Boundary Analysis

Assume example business rule:

```text
Minimum transfer = 1.00
Maximum transfer = 100,000.00
```

| ID      | Test Value | Expected Result |
| ------- | ---------: | --------------- |
| BVA-001 |       0.99 | Reject          |
| BVA-002 |       1.00 | Accept          |
| BVA-003 |       1.01 | Accept          |
| BVA-004 |  99,999.99 | Accept          |
| BVA-005 | 100,000.00 | Accept          |
| BVA-006 | 100,000.01 | Reject          |

**Priority:** P0

Also verify:

* Backend enforces same boundary as UI.
* API cannot bypass the maximum.
* Database stores accepted amount precisely.
* Fee calculation remains correct at the boundary.

---

# 6. Transfer Available Balance Boundary

Assume:

```text
Available balance = 5,000.00
Transfer fee = 0
```

| ID      | Transfer Amount | Expected |
| ------- | --------------: | -------- |
| BVA-007 |        4,999.99 | Accept   |
| BVA-008 |        5,000.00 | Accept   |
| BVA-009 |        5,000.01 | Reject   |

When a fee applies:

```text
Available balance = 5,000.00
Fee = 10.00
```

| ID      | Transfer Amount | Total Debit | Expected |
| ------- | --------------: | ----------: | -------- |
| BVA-010 |        4,989.99 |    4,999.99 | Accept   |
| BVA-011 |        4,990.00 |    5,000.00 | Accept   |
| BVA-012 |        4,990.01 |    5,000.01 | Reject   |

**Priority:** P0

---

# 7. Transfer Daily Limit Boundary

Assume:

```text
Daily transfer limit = 200,000.00
Already transferred today = 150,000.00
Remaining limit = 50,000.00
```

| ID      | New Transfer | Daily Total | Expected |
| ------- | -----------: | ----------: | -------- |
| BVA-013 |    49,999.99 |  199,999.99 | Accept   |
| BVA-014 |    50,000.00 |  200,000.00 | Accept   |
| BVA-015 |    50,000.01 |  200,000.01 | Reject   |

**Priority:** P0

Verify concurrent requests cannot both pass by evaluating an outdated remaining-limit value.

---

# 8. Payment Amount Boundary Analysis

Assume:

```text
Minimum payment = 1.00
Maximum payment = 50,000.00
```

| ID      |     Value | Expected |
| ------- | --------: | -------- |
| BVA-016 |      0.99 | Reject   |
| BVA-017 |      1.00 | Accept   |
| BVA-018 |      1.01 | Accept   |
| BVA-019 | 49,999.99 | Accept   |
| BVA-020 | 50,000.00 | Accept   |
| BVA-021 | 50,000.01 | Reject   |

**Priority:** P0

---

# 9. Card Purchase Limit Boundary

Assume:

```text
Daily card purchase limit = 20,000.00
```

| ID      |  Purchase | Expected |
| ------- | --------: | -------- |
| BVA-022 | 19,999.99 | Accept   |
| BVA-023 | 20,000.00 | Accept   |
| BVA-024 | 20,000.01 | Reject   |

If earlier daily purchases total:

```text
15,000.00
```

Remaining allowance:

```text
5,000.00
```

| ID      | New Purchase | Expected |
| ------- | -----------: | -------- |
| BVA-025 |     4,999.99 | Accept   |
| BVA-026 |     5,000.00 | Accept   |
| BVA-027 |     5,000.01 | Reject   |

**Priority:** P0

---

# 10. Card Withdrawal Limit Boundary

Assume:

```text
Daily withdrawal limit = 10,000.00
```

| ID      |     Value | Expected |
| ------- | --------: | -------- |
| BVA-028 |  9,999.99 | Accept   |
| BVA-029 | 10,000.00 | Accept   |
| BVA-030 | 10,000.01 | Reject   |

**Priority:** P0

---

# 11. Card Limit Configuration Boundary

Assume:

```text
Minimum configurable limit = 500.00
Maximum configurable limit = 50,000.00
```

| ID      |     Limit | Expected |
| ------- | --------: | -------- |
| BVA-031 |    499.99 | Reject   |
| BVA-032 |    500.00 | Accept   |
| BVA-033 |    500.01 | Accept   |
| BVA-034 | 49,999.99 | Accept   |
| BVA-035 | 50,000.00 | Accept   |
| BVA-036 | 50,000.01 | Reject   |

---

# 12. Loan Amount Boundary Analysis

Assume:

```text
Minimum loan = 10,000.00
Maximum loan = 1,000,000.00
```

| ID      |  Loan Amount | Expected |
| ------- | -----------: | -------- |
| BVA-037 |     9,999.99 | Reject   |
| BVA-038 |    10,000.00 | Accept   |
| BVA-039 |    10,000.01 | Accept   |
| BVA-040 |   999,999.99 | Accept   |
| BVA-041 | 1,000,000.00 | Accept   |
| BVA-042 | 1,000,000.01 | Reject   |

**Priority:** P0

At each accepted boundary verify:

* Interest calculation
* Fee calculation
* Installment schedule
* Total repayable amount
* Database precision

---

# 13. Loan Term Boundary Analysis

Assume:

```text
Minimum term = 6 months
Maximum term = 60 months
```

| ID      |      Term | Expected |
| ------- | --------: | -------- |
| BVA-043 |  5 months | Reject   |
| BVA-044 |  6 months | Accept   |
| BVA-045 |  7 months | Accept   |
| BVA-046 | 59 months | Accept   |
| BVA-047 | 60 months | Accept   |
| BVA-048 | 61 months | Reject   |

---

# 14. Loan Final Repayment Boundary

Assume:

```text
Outstanding balance = 10,000.00
```

| ID      | Repayment | Expected                             |
| ------- | --------: | ------------------------------------ |
| BVA-049 |  9,999.99 | Partial balance remains              |
| BVA-050 | 10,000.00 | Loan reaches zero                    |
| BVA-051 | 10,000.01 | Reject or controlled payoff handling |

**Priority:** P0

After exact settlement:

```text
Outstanding Balance = 0.00
Loan Status = CLOSED
```

No residual values such as:

```text
0.000001
```

should remain.

---

# 15. Deposit Principal Boundary

Assume:

```text
Minimum deposit = 1,000.00
Maximum deposit = 1,000,000.00
```

| ID      |    Principal | Expected |
| ------- | -----------: | -------- |
| BVA-052 |       999.99 | Reject   |
| BVA-053 |     1,000.00 | Accept   |
| BVA-054 |     1,000.01 | Accept   |
| BVA-055 |   999,999.99 | Accept   |
| BVA-056 | 1,000,000.00 | Accept   |
| BVA-057 | 1,000,000.01 | Reject   |

**Priority:** P0

---

# 16. Deposit Available Balance Boundary

Assume:

```text
Available balance = 20,000.00
```

| ID      | Deposit Principal | Expected                           |
| ------- | ----------------: | ---------------------------------- |
| BVA-058 |         19,999.99 | Accept                             |
| BVA-059 |         20,000.00 | Accept if no retained-balance rule |
| BVA-060 |         20,000.01 | Reject                             |

If deposit fee exists, total debit must also be boundary tested.

---

# 17. Deposit Early Withdrawal Date Boundary

Assume early withdrawal becomes permitted after:

```text
30 days
```

| ID      | Withdrawal Time | Expected                     |
| ------- | --------------- | ---------------------------- |
| BVA-061 | Day 29          | Reject                       |
| BVA-062 | Day 30          | Apply exact eligibility rule |
| BVA-063 | Day 31          | Accept                       |

**Priority:** P1

---

# 18. Account Balance Boundary Analysis

Typical account balance conditions:

```text
0.00
Smallest positive unit
Maximum supported balance
```

| ID      | Balance Condition               | Expected |
| ------- | ------------------------------- | -------- |
| BVA-064 | -0.01 where overdraft forbidden | Invalid  |
| BVA-065 | 0.00                            | Valid    |
| BVA-066 | 0.01                            | Valid    |

**Priority:** P0

Financial operations must not accidentally produce:

```text
-0.00
0.0000001
NaN
Infinity
```

---

# 19. Beneficiary Alias Length Boundary

Assume:

```text
Minimum alias length = 1
Maximum alias length = 50
```

| ID      | Length | Expected           |
| ------- | -----: | ------------------ |
| BVA-067 |      0 | Reject if required |
| BVA-068 |      1 | Accept             |
| BVA-069 |      2 | Accept             |
| BVA-070 |     49 | Accept             |
| BVA-071 |     50 | Accept             |
| BVA-072 |     51 | Reject             |

Also repeat using Unicode/Arabic characters.

---

# 20. Transfer Note Length Boundary

Assume:

```text
Maximum note length = 250 characters
```

| ID      | Length | Expected           |
| ------- | -----: | ------------------ |
| BVA-073 |      0 | Accept if optional |
| BVA-074 |      1 | Accept             |
| BVA-075 |    249 | Accept             |
| BVA-076 |    250 | Accept             |
| BVA-077 |    251 | Reject             |

---

# 21. Name Length Boundary

Assume:

```text
Minimum name length = 2
Maximum name length = 50
```

| ID      | Length | Expected |
| ------- | -----: | -------- |
| BVA-078 |      1 | Reject   |
| BVA-079 |      2 | Accept   |
| BVA-080 |      3 | Accept   |
| BVA-081 |     49 | Accept   |
| BVA-082 |     50 | Accept   |
| BVA-083 |     51 | Reject   |

Perform for:

* First name
* Last name
* Beneficiary alias where appropriate
* Admin reason fields

---

# 22. Password Length Boundary

Assume:

```text
Minimum password length = 12
Maximum password length = 128
```

| ID      | Length | Expected                    |
| ------- | -----: | --------------------------- |
| BVA-084 |     11 | Reject                      |
| BVA-085 |     12 | Accept if composition valid |
| BVA-086 |     13 | Accept                      |
| BVA-087 |    127 | Accept                      |
| BVA-088 |    128 | Accept                      |
| BVA-089 |    129 | Handle according to policy  |

Security note:

The application must avoid unsafe silent truncation.

---

# 23. OTP Length Boundary

Assume:

```text
OTP length = exactly 6 digits
```

| ID      |   Length | Expected |
| ------- | -------: | -------- |
| BVA-090 | 5 digits | Reject   |
| BVA-091 | 6 digits | Process  |
| BVA-092 | 7 digits | Reject   |

Also test:

```text
000000
000001
999998
999999
```

where technically valid according to generation rules.

---

# 24. OTP Expiration Boundary

Assume:

```text
OTP lifetime = 5 minutes
```

| ID      | Time          | Expected                          |
| ------- | ------------- | --------------------------------- |
| BVA-093 | 4 min 59 sec  | Valid                             |
| BVA-094 | Exactly 5 min | Follow exact expiration semantics |
| BVA-095 | 5 min 1 sec   | Expired                           |

**Priority:** P0

The system must use server time, not browser time.

---

# 25. Password Reset Token Expiration

Assume:

```text
Reset token validity = 15 minutes
```

| ID      | Time  | Expected                  |
| ------- | ----- | ------------------------- |
| BVA-096 | 14:59 | Valid                     |
| BVA-097 | 15:00 | Defined boundary behavior |
| BVA-098 | 15:01 | Reject                    |

**Priority:** P0

---

# 26. Login Failure Threshold

Assume:

```text
Lockout threshold = 5 failed attempts
```

| ID      | Failed Attempts | Expected                     |
| ------- | --------------: | ---------------------------- |
| BVA-099 |               4 | Not locked                   |
| BVA-100 |               5 | Lockout/protection activates |
| BVA-101 |               6 | Remains blocked/protected    |

**Priority:** P0

---

# 27. Session Timeout Boundary

Assume:

```text
Session inactivity timeout = 15 minutes
```

| ID      | Inactivity | Expected                       |
| ------- | ---------- | ------------------------------ |
| BVA-102 | 14:59      | Session valid                  |
| BVA-103 | 15:00      | Follow exact timeout semantics |
| BVA-104 | 15:01      | Session expired                |

**Priority:** P0

Test both:

* UI navigation
* Direct API calls

---

# 28. Beneficiary Activation Cooldown Boundary

Assume:

```text
Beneficiary activation delay = 24 hours
```

| ID      | Time Since Creation | Expected                               |
| ------- | ------------------- | -------------------------------------- |
| BVA-105 | 23:59:59            | Transfer rejected                      |
| BVA-106 | Exactly 24 hours    | Transfer allowed if boundary inclusive |
| BVA-107 | 24:00:01            | Transfer allowed                       |

**Priority:** P0

---

# 29. Scheduled Transfer Date Boundary

Assume earliest allowed schedule date is:

```text
Today + 1 day
```

| ID      | Date               | Expected                                  |
| ------- | ------------------ | ----------------------------------------- |
| BVA-108 | Today              | Reject if same-day scheduling unsupported |
| BVA-109 | Tomorrow           | Accept                                    |
| BVA-110 | Day after tomorrow | Accept                                    |

If maximum scheduling horizon exists, e.g.:

```text
365 days
```

then:

| ID      | Date    | Expected |
| ------- | ------- | -------- |
| BVA-111 | Day 364 | Accept   |
| BVA-112 | Day 365 | Accept   |
| BVA-113 | Day 366 | Reject   |

---

# 30. Scheduled Payment Boundary

Apply the same date analysis to scheduled payments:

```text
Past date
Exact earliest valid date
One day after earliest
Exact latest allowed date
One day after latest
```

IDs:

```text
BVA-114
BVA-115
BVA-116
BVA-117
BVA-118
```

Expected:

Business scheduling rules are enforced both in UI and API.

---

# 31. Recurring Transaction Occurrence Boundary

Assume recurrence count must be:

```text
1 to 100 occurrences
```

| ID      | Occurrences | Expected |
| ------- | ----------: | -------- |
| BVA-119 |           0 | Reject   |
| BVA-120 |           1 | Accept   |
| BVA-121 |           2 | Accept   |
| BVA-122 |          99 | Accept   |
| BVA-123 |         100 | Accept   |
| BVA-124 |         101 | Reject   |

---

# 32. Statement Date Range Boundary

Assume maximum statement range:

```text
365 days
```

| ID      |    Range | Expected |
| ------- | -------: | -------- |
| BVA-125 | 364 days | Accept   |
| BVA-126 | 365 days | Accept   |
| BVA-127 | 366 days | Reject   |

Also test:

| ID      | Condition   | Expected                   |
| ------- | ----------- | -------------------------- |
| BVA-128 | Start = End | Valid single-day statement |
| BVA-129 | Start > End | Reject                     |
| BVA-130 | Start < End | Accept                     |

---

# 33. Statement Period Transaction Boundary

For a statement covering:

```text
2026-08-01 00:00:00
through
2026-08-31 23:59:59
```

Test:

| ID      | Transaction Time         | Expected                 |
| ------- | ------------------------ | ------------------------ |
| BVA-131 | Immediately before start | Excluded                 |
| BVA-132 | Exact start              | Included                 |
| BVA-133 | Immediately after start  | Included                 |
| BVA-134 | Immediately before end   | Included                 |
| BVA-135 | Exact end boundary       | Follow defined semantics |
| BVA-136 | Immediately after end    | Excluded                 |

**Priority:** P0

---

# 34. Transaction Pagination Boundary

Assume:

```text
Page size = 25
```

Test datasets containing:

| ID      | Number of Transactions | Expected              |
| ------- | ---------------------: | --------------------- |
| BVA-137 |                      0 | Empty state           |
| BVA-138 |                      1 | One row               |
| BVA-139 |                     24 | One page              |
| BVA-140 |                     25 | Exactly one full page |
| BVA-141 |                     26 | Two pages             |
| BVA-142 |                     50 | Two full pages        |
| BVA-143 |                     51 | Three pages           |

Verify:

* No duplication
* No missing rows
* Stable ordering

---

# 35. Notification Content Boundary

Assume:

```text
Maximum notification text = 500 characters
```

| ID      | Length | Expected                                      |
| ------- | -----: | --------------------------------------------- |
| BVA-144 |    499 | Accept/display                                |
| BVA-145 |    500 | Accept/display                                |
| BVA-146 |    501 | Safe rejection/truncation according to design |

Critical financial meaning must never be silently removed.

---

# 36. Notification Unread Counter Boundary

Test:

| ID      | Unread Count | Expected                         |
| ------- | -----------: | -------------------------------- |
| BVA-147 |            0 | No unread indicator or zero      |
| BVA-148 |            1 | Displays 1                       |
| BVA-149 |           99 | Displays accurately              |
| BVA-150 |         100+ | UI handles large count correctly |

If UI uses a display such as:

```text
99+
```

verify actual backend count remains accurate.

---

# 37. Admin Reason Length Boundary

Assume:

```text
Minimum reason = 10 characters
Maximum reason = 500 characters
```

| ID      | Length | Expected |
| ------- | -----: | -------- |
| BVA-151 |      9 | Reject   |
| BVA-152 |     10 | Accept   |
| BVA-153 |     11 | Accept   |
| BVA-154 |    499 | Accept   |
| BVA-155 |    500 | Accept   |
| BVA-156 |    501 | Reject   |

Applicable actions may include:

* Account freeze
* Customer suspension
* Loan rejection
* Transaction reversal
* Card block
* Financial adjustment

---

# 38. Loan Interest Precision Boundary

Assume interest values are stored to:

```text
2 decimal places
```

Test calculations producing:

```text
10.004
10.005
10.006
```

| ID      | Raw Result | Expected                          |
| ------- | ---------: | --------------------------------- |
| BVA-157 |     10.004 | Round according to defined rule   |
| BVA-158 |     10.005 | Exact half-boundary rule verified |
| BVA-159 |     10.006 | Round according to defined rule   |

**Priority:** P0

The rounding rule must be explicitly documented and used consistently across:

* UI
* API
* Database
* Statements

---

# 39. Fee Precision Boundary

Apply equivalent precision tests to:

* Transfer fees
* Payment fees
* Loan fees
* Deposit penalties
* Card fees

Representative tests:

```text
BVA-160
BVA-161
BVA-162
```

Validate values immediately:

```text
Below rounding midpoint
At rounding midpoint
Above rounding midpoint
```

---

# 40. Transaction Reference Length Boundary

Assume reference format has:

```text
Maximum length = 64 characters
```

Validate generated and searched references at:

```text
63
64
65
```

IDs:

```text
BVA-163
BVA-164
BVA-165
```

Generated system references must never exceed database/API constraints.

---

# 41. Search Input Boundary

Assume generic search maximum:

```text
100 characters
```

| ID      | Length | Expected                            |
| ------- | -----: | ----------------------------------- |
| BVA-166 |      0 | Empty search/default behavior       |
| BVA-167 |      1 | Process                             |
| BVA-168 |     99 | Process                             |
| BVA-169 |    100 | Process                             |
| BVA-170 |    101 | Reject/truncate according to design |

Repeat for:

* Transaction search
* Customer search
* Beneficiary search
* Admin search

---

# 42. File Upload Size Boundary

Where profile/KYC uploads exist.

Assume:

```text
Maximum size = 5 MB
```

| ID      | Size            | Expected |
| ------- | --------------- | -------- |
| BVA-171 | Just below 5 MB | Accept   |
| BVA-172 | Exactly 5 MB    | Accept   |
| BVA-173 | Just above 5 MB | Reject   |

Also test zero-byte files:

```text
BVA-174 — 0 bytes → Reject
```

---

# 43. File Count Boundary

If multiple documents are allowed, assume:

```text
Maximum files = 5
```

| ID      | Count | Expected |
| ------- | ----: | -------- |
| BVA-175 |     4 | Accept   |
| BVA-176 |     5 | Accept   |
| BVA-177 |     6 | Reject   |

---

# 44. Age Eligibility Boundary

If a product requires:

```text
Minimum age = 18
```

Test:

| ID      |               Age | Expected   |
| ------- | ----------------: | ---------- |
| BVA-178 | 17 years 364 days | Ineligible |
| BVA-179 |  Exactly 18 years | Eligible   |
| BVA-180 |  18 years + 1 day | Eligible   |

The calculation should use date of birth and authoritative server/business date.

---

# 45. Loan Eligibility Income Boundary

Example:

```text
Minimum monthly income = 10,000
```

| ID      |    Income | Expected   |
| ------- | --------: | ---------- |
| BVA-181 |  9,999.99 | Ineligible |
| BVA-182 | 10,000.00 | Eligible   |
| BVA-183 | 10,000.01 | Eligible   |

Actual rule depends on project requirements.

---

# 46. Account Closure Balance Boundary

For normal account closure:

```text
Required balance = 0.00
```

| ID      | Balance | Expected                    |
| ------- | ------: | --------------------------- |
| BVA-184 |   -0.01 | Reject / resolve balance    |
| BVA-185 |    0.00 | Eligible if no dependencies |
| BVA-186 |    0.01 | Reject                      |

**Priority:** P0

---

# 47. Card Expiry Boundary

If a card expires at the end of its expiration month, test:

```text
One moment before expiry
Exact expiry cutoff
One moment after expiry
```

IDs:

```text
BVA-187
BVA-188
BVA-189
```

Expected:

The exact expiration policy is consistently enforced by:

* UI
* API
* Transaction authorization

---

# 48. KYC Expiration Boundary

If KYC expires at a configured date/time:

| ID      | Time                      | Expected            |
| ------- | ------------------------- | ------------------- |
| BVA-190 | Immediately before expiry | VERIFIED/valid      |
| BVA-191 | Exact expiry moment       | Follow defined rule |
| BVA-192 | Immediately after expiry  | EXPIRED/restricted  |

---

# 49. Transaction Concurrency Boundary

For an account with:

```text
Available Balance = 1,000.00
```

Submit concurrently:

```text
Transfer A = 500.00
Transfer B = 500.00
```

Expected:

Both may succeed if no fees and total equals available balance.

Then test:

```text
Transfer A = 500.01
Transfer B = 500.00
```

Expected:

Combined successful debit must not exceed:

```text
1,000.00
```

IDs:

```text
BVA-193
BVA-194
```

**Priority:** P0

---

# 50. Daily Limit Reset Boundary

Assume limit resets at banking-day boundary.

Test:

```text
One second before reset
Exact reset time
One second after reset
```

IDs:

```text
BVA-195
BVA-196
BVA-197
```

Verify:

* Daily usage
* New available limit
* Timezone handling
* Concurrent transactions around reset

---

# 51. Monthly Limit Reset Boundary

Test:

```text
Last moment of current month
Exact month transition
First moment of new month
```

IDs:

```text
BVA-198
BVA-199
BVA-200
```

Expected:

Monthly counters transition correctly without double-counting or premature reset.

---

# 52. Interest Accrual Date Boundary

For loans/deposits with daily accrual:

Test transactions/events occurring:

```text
Immediately before daily accrual cutoff
Exactly at cutoff
Immediately after cutoff
```

IDs:

```text
BVA-201
BVA-202
BVA-203
```

Expected:

Interest applies to the correct business date exactly once.

---

# 53. Leap-Year Boundaries

Validate:

```text
February 28
February 29
March 1
```

for leap years.

IDs:

```text
BVA-204
BVA-205
BVA-206
```

Apply to:

* Loan installment dates
* Deposit maturity
* Scheduled transfers
* Scheduled payments
* Recurring payments
* Statements

For non-leap years, February 29 should be rejected or normalized only according to explicit business rules.

---

# 54. Month-End Boundaries

Validate months containing:

```text
28 days
29 days
30 days
31 days
```

Test recurring operations scheduled on:

```text
28th
29th
30th
31st
```

Important cases:

* January 31 → February
* March 31 → April
* August 31 → September

System behavior must follow defined recurrence rules.

---

# 55. Year-End Boundaries

Test:

```text
December 31 23:59:59
January 1 00:00:00
```

Apply to:

* Daily limits
* Monthly limits
* Statements
* Scheduled transfers
* Interest processing
* Audit timestamps
* Transaction history

IDs:

```text
BVA-207
BVA-208
```

---

# 56. Boundary Analysis for State Transitions

Boundaries are not always numeric.

State-transition boundaries are also important.

Example:

```text
ACTIVE → FROZEN
```

Test:

```text
Transaction immediately before freeze
Transaction during freeze operation
Transaction immediately after freeze
```

IDs:

```text
BVA-209
BVA-210
BVA-211
```

Expected:

The authoritative transaction/state cutoff is deterministic.

Apply the same technique to:

* Account closure
* Card blocking
* Customer suspension
* Beneficiary deletion
* Loan approval
* Deposit maturity
* Session revocation

---

# 57. Boundary Analysis for Idempotency

For actions that should occur once, the boundary is often:

```text
First submission
Second identical submission
```

Test:

```text
Submission #1 → accepted
Submission #2 → no duplicate financial effect
```

Apply to:

* Transfer
* Payment
* Loan disbursement
* Deposit creation
* Deposit maturity
* Reversal
* Card replacement

IDs:

```text
BVA-212
BVA-213
```

---

# 58. Boundary Analysis for Attempts and Retries

For systems allowing:

```text
Maximum N retries
```

Always test:

```text
N - 1
N
N + 1
```

Examples:

* OTP attempts
* Login attempts
* Password-reset attempts
* Verification-code resend attempts
* Failed-payment retries

---

# 59. Boundary Analysis Checklist

For every numeric or time-based requirement, verify whether the following exist:

```text
Minimum
Maximum
Minimum - smallest unit
Minimum + smallest unit
Maximum - smallest unit
Maximum + smallest unit
Zero
Negative value
Very large value
Decimal precision
Exact date/time cutoff
Counter threshold
Concurrency at the threshold
```

---

# 60. UI / API / Database Boundary Consistency

Each important boundary should be tested at multiple layers.

Example:

```text
Maximum transfer = 100,000.00
```

Validate:

```text
UI:
100,000.00 accepted
100,000.01 rejected

API:
100,000.00 accepted
100,000.01 rejected

Database:
No transaction > 100,000.00 persists unless explicitly permitted
```

This prevents a common defect:

```text
Frontend rejects invalid value,
but direct API request accepts it.
```

---

# 61. High-Risk Boundary Regression Set

The minimum critical BVA regression set should include:

* Transfer minimum/maximum
* Available balance
* Transfer daily limit
* Payment maximum
* Card spending limit
* Loan minimum/maximum
* Loan term minimum/maximum
* Final loan repayment
* Deposit minimum/maximum
* OTP expiration
* OTP attempts
* Password length
* Login lockout
* Session timeout
* Beneficiary cooldown
* Scheduled transaction dates
* Statement date range
* Account closure zero balance
* Card expiry
* KYC expiry
* Daily/monthly limit reset
* Concurrent balance exhaustion

---

# 62. Automation Candidates

Boundary scenarios are excellent candidates for data-driven automation.

Example dataset:

```text
[
  { "amount": 0.99, "expected": "REJECT" },
  { "amount": 1.00, "expected": "ACCEPT" },
  { "amount": 1.01, "expected": "ACCEPT" },
  { "amount": 99999.99, "expected": "ACCEPT" },
  { "amount": 100000.00, "expected": "ACCEPT" },
  { "amount": 100000.01, "expected": "REJECT" }
]
```

These datasets can later be reused in:

* Selenium
* Cypress
* Playwright
* Jest
* Postman
* REST Assured
* Cucumber

---

# 63. SQL Validation Candidates

For accepted and rejected boundaries, SQL validation should verify:

Accepted boundary:

```text
Correct value persisted
Correct precision
Correct financial relationship
```

Rejected boundary:

```text
No invalid transaction persisted
No balance modification
No orphan record
```

---

# 64. BDD Example

```gherkin
Feature: Transfer amount boundaries

Scenario Outline: Transfer amount is validated against configured limits
  Given the customer has sufficient available balance
  And the beneficiary is active
  When the customer attempts to transfer <amount>
  Then the transfer should be <result>

Examples:
  | amount    | result   |
  | 0.99      | rejected |
  | 1.00      | accepted |
  | 1.01      | accepted |
  | 99999.99  | accepted |
  | 100000.00 | accepted |
  | 100000.01 | rejected |
```

---

# 65. Risk Traceability

Boundary Value Analysis directly helps mitigate:

```text
RISK-001 — Incorrect account balance
RISK-007 — Transfer exceeding available balance
RISK-008 — Transfer limit bypass
RISK-010 — Incorrect fee calculations
RISK-011 — Incorrect loan interest calculations
RISK-012 — Incorrect deposit interest calculations
RISK-013 — Concurrent transactions corrupt balance
RISK-016 — Account lockout failure
RISK-028 — Scheduled transfer executes incorrectly
RISK-037 — Frontend-only validation
RISK-043 — Timezone/date scheduling defects
RISK-045 — Expired OTP accepted
```

---

# 66. Boundary Coverage Summary

This analysis covers boundaries for:

* Transfers
* Payments
* Cards
* Loans
* Deposits
* Account balances
* Beneficiaries
* Notes
* Names
* Passwords
* OTPs
* Reset tokens
* Login attempts
* Sessions
* Activation delays
* Scheduled transactions
* Recurrence
* Statements
* Pagination
* Notifications
* Admin reasons
* Interest precision
* Fees
* References
* Search
* File uploads
* Eligibility
* Account closure
* Expiry
* Concurrency
* Daily/monthly resets
* Interest accrual
* Leap years
* Month/year boundaries
* State transitions
* Idempotency
* Retry counts

---

# 67. Final Boundary Value Analysis Principle

Boundary testing should not be limited to entering a minimum and maximum value into a form.

For each important boundary, QA should ask:

```text
What happens immediately before the boundary?

What happens exactly at the boundary?

What happens immediately after the boundary?

Does the UI enforce it?

Does the API enforce it?

Does the database preserve the correct state?

Does concurrency allow the boundary to be bypassed?

Does the rule change across a date/time boundary?

Does rounding alter the result?

Does failure leave financial state unchanged?
```

The most important banking boundaries are those involving:

```text
Money
Time
Authorization
Limits
Lifecycle state
Retries
Concurrency
```

These boundaries should receive continuous regression coverage because a one-cent, one-second, one-request, or one-state difference can materially change the correctness of a banking transaction.
