# Banking System — Equivalence Partitioning

## 1. Document Information

| Field                 | Value                          |
| --------------------- | ------------------------------ |
| Project               | Banking System Testing Project |
| Test Design Technique | Equivalence Partitioning       |
| Document              | Test Design                    |
| Version               | 1.0                            |
| Status                | Draft                          |
| Owner                 | QA Engineering                 |

---

# 2. Purpose

This document applies **Equivalence Partitioning (EP)** to the Banking System.

Equivalence Partitioning divides input data and system states into groups that are expected to behave similarly.

Instead of testing every possible value, QA selects representative values from each partition.

Example:

If valid transfer amounts are:

```text id="yd7ovr"
1.00 to 100,000.00
```

the input space can be divided into:

```text id="vl5rcq"
Invalid Low:
Amount < 1.00

Valid:
1.00 <= Amount <= 100,000.00

Invalid High:
Amount > 100,000.00
```

Representative values can then be selected from each class.

---

# 3. Why Equivalence Partitioning Is Important

Banking systems contain large input spaces involving:

* Monetary values
* Customer states
* Account states
* Card states
* Beneficiary states
* Loan states
* Deposit states
* Transaction states
* Dates
* Authentication states
* Roles
* Permissions
* Limits
* Contact information
* Search fields

Without partitioning, test suites can become unnecessarily repetitive.

Equivalence Partitioning helps maximize coverage while reducing redundant test cases.

---

# 4. Partition Types

Partitions are classified as:

```text id="xzsn39"
Valid Equivalence Class
Invalid Equivalence Class
```

Example:

```text id="y5s830"
Field: Transfer Amount

Valid:
1.00–100,000.00

Invalid:
< 1.00
> 100,000.00
Non-numeric
Missing
```

One representative value from each partition may provide baseline coverage, with Boundary Value Analysis used separately around the edges.

---

# 5. Scenario Naming Convention

Equivalence partitions use:

```text id="7umrz9"
EP-XXX
```

Classification:

```text id="hd6d0b"
V = Valid Partition
I = Invalid Partition
```

Priority:

```text id="ji77a6"
P0 = Critical
P1 = High
P2 = Medium
P3 = Low
```

---

# 6. Transfer Amount Partitions

Assume:

```text id="zq9ihv"
Minimum transfer = 1.00
Maximum transfer = 100,000.00
```

| ID     | Partition             | Representative | Expected |
| ------ | --------------------- | -------------: | -------- |
| EP-001 | Invalid below minimum |           0.50 | Reject   |
| EP-002 | Valid transfer amount |      25,000.00 | Accept   |
| EP-003 | Invalid above maximum |     150,000.00 | Reject   |
| EP-004 | Invalid zero          |           0.00 | Reject   |
| EP-005 | Invalid negative      |        -100.00 | Reject   |
| EP-006 | Invalid non-numeric   |          `abc` | Reject   |
| EP-007 | Invalid blank         |          Empty | Reject   |

**Priority:** P0

---

# 7. Transfer Balance Partitions

Possible partitions:

| ID     | Partition                                                     | Representative                                 | Expected                          |
| ------ | ------------------------------------------------------------- | ---------------------------------------------- | --------------------------------- |
| EP-008 | Balance greater than total debit                              | Balance 10,000 / Debit 5,000                   | Accept                            |
| EP-009 | Balance exactly sufficient                                    | Balance 5,000 / Debit 5,000                    | Accept according to reserve rules |
| EP-010 | Balance insufficient                                          | Balance 4,000 / Debit 5,000                    | Reject                            |
| EP-011 | Current balance sufficient but available balance insufficient | Current 10,000 / Available 4,000 / Debit 5,000 | Reject                            |

**Priority:** P0

---

# 8. Transfer Source Account State Partitions

| ID     | State                      | Class               | Expected              |
| ------ | -------------------------- | ------------------- | --------------------- |
| EP-012 | ACTIVE                     | Valid               | Transfer allowed      |
| EP-013 | FROZEN                     | Invalid             | Reject                |
| EP-014 | RESTRICTED                 | Invalid/conditional | Apply restrictions    |
| EP-015 | SUSPENDED                  | Invalid             | Reject                |
| EP-016 | CLOSED                     | Invalid             | Reject                |
| EP-017 | Another customer's account | Invalid             | Authorization failure |

**Priority:** P0

---

# 9. Transfer Destination Partitions

| ID     | Destination            | Class                 | Expected              |
| ------ | ---------------------- | --------------------- | --------------------- |
| EP-018 | Valid active account   | Valid                 | Accept                |
| EP-019 | Nonexistent account    | Invalid               | Reject                |
| EP-020 | Closed account         | Invalid               | Reject                |
| EP-021 | Restricted destination | Conditional           | Follow business rules |
| EP-022 | Same account as source | Invalid               | Reject                |
| EP-023 | Valid external account | Valid where supported | Process               |

---

# 10. Beneficiary State Partitions

| ID     | Beneficiary State           | Class   | Expected              |
| ------ | --------------------------- | ------- | --------------------- |
| EP-024 | ACTIVE                      | Valid   | Transfer allowed      |
| EP-025 | PENDING_VERIFICATION        | Invalid | Reject                |
| EP-026 | PENDING_ACTIVATION          | Invalid | Reject                |
| EP-027 | DISABLED                    | Invalid | Reject                |
| EP-028 | DELETED                     | Invalid | Reject                |
| EP-029 | Belongs to another customer | Invalid | Authorization failure |
| EP-030 | Nonexistent                 | Invalid | Reject                |

**Priority:** P0

---

# 11. Transfer Limit Partitions

For per-transaction and daily limits:

| ID     | Partition                                     | Expected                      |
| ------ | --------------------------------------------- | ----------------------------- |
| EP-031 | Requested amount within limit                 | Accept                        |
| EP-032 | Requested amount exceeds per-transfer limit   | Reject                        |
| EP-033 | Cumulative daily total within limit           | Accept                        |
| EP-034 | Cumulative daily total exceeds limit          | Reject                        |
| EP-035 | Concurrent requests collectively exceed limit | Only permitted total succeeds |

---

# 12. Payment Amount Partitions

Assume:

```text id="o6p7t4"
Minimum = 1.00
Maximum = 50,000.00
```

| ID     | Partition     | Representative | Expected |
| ------ | ------------- | -------------: | -------- |
| EP-036 | Below minimum |           0.50 | Reject   |
| EP-037 | Valid amount  |       1,500.00 | Accept   |
| EP-038 | Above maximum |      60,000.00 | Reject   |
| EP-039 | Zero          |           0.00 | Reject   |
| EP-040 | Negative      |        -100.00 | Reject   |
| EP-041 | Non-numeric   |          `abc` | Reject   |

---

# 13. Bill State Partitions

| ID     | Bill State                             | Class               | Expected        |
| ------ | -------------------------------------- | ------------------- | --------------- |
| EP-042 | Valid unpaid bill                      | Valid               | Payment allowed |
| EP-043 | Already paid bill                      | Invalid             | Reject          |
| EP-044 | Expired bill                           | Conditional/invalid | Follow rules    |
| EP-045 | Nonexistent bill                       | Invalid             | Reject          |
| EP-046 | Bill/payee mismatch                    | Invalid             | Reject          |
| EP-047 | Invalid customer/reference combination | Invalid             | Reject          |

**Priority:** P0

---

# 14. Payee State Partitions

| ID     | State                      | Expected |
| ------ | -------------------------- | -------- |
| EP-048 | Active valid payee         | Allow    |
| EP-049 | Disabled payee             | Reject   |
| EP-050 | Deleted payee              | Reject   |
| EP-051 | Nonexistent payee          | Reject   |
| EP-052 | Unauthorized private payee | Reject   |

---

# 15. Card State Partitions

| ID     | Card State | Class   | Expected Transaction Behavior |
| ------ | ---------- | ------- | ----------------------------- |
| EP-053 | INACTIVE   | Invalid | Reject                        |
| EP-054 | ACTIVE     | Valid   | Allow if other rules pass     |
| EP-055 | FROZEN     | Invalid | Reject                        |
| EP-056 | BLOCKED    | Invalid | Reject                        |
| EP-057 | EXPIRED    | Invalid | Reject                        |
| EP-058 | CANCELLED  | Invalid | Reject                        |

**Priority:** P0

---

# 16. Card Transaction Amount Partitions

| ID     | Partition                                     | Expected |
| ------ | --------------------------------------------- | -------- |
| EP-059 | Within card limit and balance                 | Accept   |
| EP-060 | Above card limit                              | Reject   |
| EP-061 | Within card limit but above available balance | Reject   |
| EP-062 | Above both limit and balance                  | Reject   |
| EP-063 | Zero amount                                   | Reject   |
| EP-064 | Negative amount                               | Reject   |

---

# 17. Card Ownership Partitions

| ID     | Partition                              | Expected                |
| ------ | -------------------------------------- | ----------------------- |
| EP-065 | Card owned by authenticated customer   | Access                  |
| EP-066 | Card owned by another customer         | Deny                    |
| EP-067 | Invalid/nonexistent card               | Reject                  |
| EP-068 | Admin role with authorized card access | Allow according to role |
| EP-069 | Admin role without card permission     | Deny                    |

---

# 18. Loan Amount Partitions

Assume:

```text id="46pmur"
Minimum = 10,000.00
Maximum = 1,000,000.00
```

| ID     | Partition     | Representative | Expected           |
| ------ | ------------- | -------------: | ------------------ |
| EP-070 | Below minimum |          5,000 | Reject             |
| EP-071 | Valid amount  |        250,000 | Accept if eligible |
| EP-072 | Above maximum |      1,500,000 | Reject             |
| EP-073 | Zero          |              0 | Reject             |
| EP-074 | Negative      |        -10,000 | Reject             |
| EP-075 | Non-numeric   |         `loan` | Reject             |

---

# 19. Loan Term Partitions

Assume supported terms are:

```text id="iq23y1"
6–60 months
```

| ID     | Partition                  | Representative | Expected |
| ------ | -------------------------- | -------------: | -------- |
| EP-076 | Below minimum term         |       3 months | Reject   |
| EP-077 | Supported term             |      24 months | Accept   |
| EP-078 | Above maximum term         |      72 months | Reject   |
| EP-079 | Invalid non-supported type |  `24.5 months` | Reject   |

---

# 20. Loan Eligibility Partitions

| ID     | Customer Condition           | Class               | Expected            |
| ------ | ---------------------------- | ------------------- | ------------------- |
| EP-080 | Active + verified + eligible | Valid               | Application allowed |
| EP-081 | Unverified KYC               | Invalid             | Reject/restrict     |
| EP-082 | Restricted customer          | Invalid/conditional | Follow rules        |
| EP-083 | Suspended customer           | Invalid             | Reject              |
| EP-084 | Disabled customer            | Invalid             | Reject              |
| EP-085 | Excess existing debt         | Invalid             | Reject              |
| EP-086 | Does not meet minimum income | Invalid             | Reject              |

---

# 21. Loan Application State Partitions

| ID     | State        | Expected Allowed Actions             |
| ------ | ------------ | ------------------------------------ |
| EP-087 | DRAFT        | Edit/submit                          |
| EP-088 | SUBMITTED    | Await review                         |
| EP-089 | UNDER_REVIEW | Admin decision                       |
| EP-090 | APPROVED     | Eligible for controlled disbursement |
| EP-091 | REJECTED     | No disbursement                      |
| EP-092 | CANCELLED    | No approval/disbursement             |
| EP-093 | ACTIVE       | Repayment operations                 |
| EP-094 | CLOSED       | No further repayment                 |
| EP-095 | DEFAULTED    | Special servicing rules              |

---

# 22. Loan Repayment Partitions

| ID     | Partition                                      | Expected              |
| ------ | ---------------------------------------------- | --------------------- |
| EP-096 | Valid installment amount with sufficient funds | Accept                |
| EP-097 | Valid amount with insufficient funds           | Reject/fail           |
| EP-098 | Partial repayment where supported              | Accept                |
| EP-099 | Partial repayment where unsupported            | Reject                |
| EP-100 | Amount above outstanding balance               | Reject/control payoff |
| EP-101 | Payment after loan closed                      | Reject                |

---

# 23. Deposit Principal Partitions

Assume:

```text id="jbb3h7"
Minimum = 1,000.00
Maximum = 1,000,000.00
```

| ID     | Partition       | Representative | Expected |
| ------ | --------------- | -------------: | -------- |
| EP-102 | Below minimum   |            500 | Reject   |
| EP-103 | Valid principal |         50,000 | Accept   |
| EP-104 | Above maximum   |      2,000,000 | Reject   |
| EP-105 | Zero            |              0 | Reject   |
| EP-106 | Negative        |         -1,000 | Reject   |
| EP-107 | Non-numeric     |      `deposit` | Reject   |

---

# 24. Deposit Funding Account Partitions

| ID     | Account State                            | Expected              |
| ------ | ---------------------------------------- | --------------------- |
| EP-108 | Active owned account with enough balance | Allow                 |
| EP-109 | Active account with insufficient balance | Reject                |
| EP-110 | Frozen account                           | Reject                |
| EP-111 | Restricted account                       | Reject/conditional    |
| EP-112 | Closed account                           | Reject                |
| EP-113 | Another customer's account               | Authorization failure |

---

# 25. Deposit State Partitions

| ID     | State           | Expected                    |
| ------ | --------------- | --------------------------- |
| EP-114 | PENDING         | Await activation            |
| EP-115 | ACTIVE          | Normal deposit behavior     |
| EP-116 | MATURED         | Maturity processing allowed |
| EP-117 | EARLY_WITHDRAWN | No further payout           |
| EP-118 | RENEWED         | New cycle active            |
| EP-119 | CLOSED          | No further withdrawal       |
| EP-120 | CANCELLED       | No activation/payout        |

---

# 26. Deposit Withdrawal Partitions

| ID     | Condition                           | Expected                |
| ------ | ----------------------------------- | ----------------------- |
| EP-121 | Active and early withdrawal allowed | Process with rules      |
| EP-122 | Active but withdrawal prohibited    | Reject                  |
| EP-123 | Matured and unpaid                  | Process maturity payout |
| EP-124 | Closed                              | Reject                  |
| EP-125 | Already early withdrawn             | Reject duplicate payout |

---

# 27. Authentication Credential Partitions

## Username / Email

| ID     | Partition                   | Expected                       |
| ------ | --------------------------- | ------------------------------ |
| EP-126 | Registered valid identifier | Continue credential validation |
| EP-127 | Unregistered identifier     | Reject safely                  |
| EP-128 | Invalid format              | Reject                         |
| EP-129 | Empty                       | Reject                         |

## Password

| ID     | Partition                    | Expected                            |
| ------ | ---------------------------- | ----------------------------------- |
| EP-130 | Correct password             | Authenticate if other controls pass |
| EP-131 | Incorrect password           | Reject                              |
| EP-132 | Empty password               | Reject                              |
| EP-133 | Password for another account | Reject                              |

---

# 28. Customer Account Status Partitions for Login

| ID     | Status    | Expected        |
| ------ | --------- | --------------- |
| EP-134 | ACTIVE    | Login allowed   |
| EP-135 | LOCKED    | Reject          |
| EP-136 | SUSPENDED | Reject/restrict |
| EP-137 | DISABLED  | Reject          |
| EP-138 | CLOSED    | Reject          |

---

# 29. MFA Code Partitions

Assume six-digit numeric OTP.

| ID     | Partition             | Representative   | Expected |
| ------ | --------------------- | ---------------- | -------- |
| EP-139 | Correct active OTP    | Valid code       | Accept   |
| EP-140 | Incorrect numeric OTP | Wrong 6 digits   | Reject   |
| EP-141 | Expired OTP           | Previously valid | Reject   |
| EP-142 | Reused OTP            | Used valid code  | Reject   |
| EP-143 | Too short OTP         | 5 digits         | Reject   |
| EP-144 | Too long OTP          | 7 digits         | Reject   |
| EP-145 | Non-numeric OTP       | `ABCDEF`         | Reject   |
| EP-146 | Blank OTP             | Empty            | Reject   |

---

# 30. Password Reset Token Partitions

| ID     | Token State                            | Expected     |
| ------ | -------------------------------------- | ------------ |
| EP-147 | Valid active token                     | Accept reset |
| EP-148 | Expired token                          | Reject       |
| EP-149 | Previously used token                  | Reject       |
| EP-150 | Invalid/random token                   | Reject       |
| EP-151 | Token for another user/session context | Reject       |
| EP-152 | Missing token                          | Reject       |

---

# 31. Session State Partitions

| ID     | Session State               | Expected                   |
| ------ | --------------------------- | -------------------------- |
| EP-153 | Valid authenticated session | Allow authorized resources |
| EP-154 | Expired session             | Reject                     |
| EP-155 | Revoked session             | Reject                     |
| EP-156 | Logged-out session          | Reject                     |
| EP-157 | Anonymous session           | Reject protected resource  |
| EP-158 | Valid session wrong role    | Deny role-protected action |

---

# 32. User Role Partitions

| ID     | Role                 | Expected                        |
| ------ | -------------------- | ------------------------------- |
| EP-159 | CUSTOMER             | Customer operations only        |
| EP-160 | SUPER_ADMIN          | Authorized full admin functions |
| EP-161 | OPERATIONS_ADMIN     | Operations permissions          |
| EP-162 | KYC_REVIEWER         | KYC permissions only            |
| EP-163 | LOAN_OFFICER         | Loan permissions                |
| EP-164 | AUDITOR              | Audit/read-only permissions     |
| EP-165 | READ_ONLY_ADMIN      | No state-changing actions       |
| EP-166 | Invalid/unknown role | No unauthorized privileges      |

---

# 33. Customer Status Partitions

| ID     | Status     | Expected General Behavior                           |
| ------ | ---------- | --------------------------------------------------- |
| EP-167 | ACTIVE     | Normal functionality                                |
| EP-168 | RESTRICTED | Limited functionality                               |
| EP-169 | SUSPENDED  | Financial operations blocked                        |
| EP-170 | DISABLED   | Access prohibited                                   |
| EP-171 | CLOSED     | Account relationship terminated according to policy |

---

# 34. KYC Status Partitions

| ID     | KYC Status | Expected                            |
| ------ | ---------- | ----------------------------------- |
| EP-172 | VERIFIED   | KYC-dependent functionality allowed |
| EP-173 | PENDING    | Restricted until reviewed           |
| EP-174 | REJECTED   | Restricted                          |
| EP-175 | EXPIRED    | Reverification required             |

---

# 35. Email Address Partitions

| ID     | Partition                | Example              | Expected            |
| ------ | ------------------------ | -------------------- | ------------------- |
| EP-176 | Valid unused email       | `qa1@banktest.local` | Accept              |
| EP-177 | Valid already-used email | Existing address     | Reject where unique |
| EP-178 | Missing `@`              | `userbank.com`       | Reject              |
| EP-179 | Missing domain           | `user@`              | Reject              |
| EP-180 | Empty                    | —                    | Reject if required  |
| EP-181 | Leading/trailing spaces  | `user@test.com`      | Normalize/validate  |
| EP-182 | Valid mixed case         | `User@Test.com`      | Handle consistently |

---

# 36. Phone Number Partitions

| ID     | Partition                 | Expected               |
| ------ | ------------------------- | ---------------------- |
| EP-183 | Valid supported number    | Accept                 |
| EP-184 | Too short                 | Reject                 |
| EP-185 | Too long                  | Reject                 |
| EP-186 | Unsupported characters    | Reject                 |
| EP-187 | Empty                     | Reject where required  |
| EP-188 | Already-associated number | Follow uniqueness rule |

---

# 37. Name Field Partitions

| ID     | Partition           | Example                | Expected            |
| ------ | ------------------- | ---------------------- | ------------------- |
| EP-189 | Valid Latin name    | `Mohamed`              | Accept              |
| EP-190 | Valid Arabic name   | Arabic text            | Accept              |
| EP-191 | Valid accented name | `José`                 | Accept if supported |
| EP-192 | Empty required name | —                      | Reject              |
| EP-193 | Spaces only         | `   `                  | Reject              |
| EP-194 | Too long            | > maximum              | Reject              |
| EP-195 | Script-like input   | `<script>...</script>` | Handle safely       |

---

# 38. Search Input Partitions

Applicable to:

* Transactions
* Customers
* Accounts
* Beneficiaries
* Loans
* Audit logs

| ID     | Partition                   | Expected                         |
| ------ | --------------------------- | -------------------------------- |
| EP-196 | Exact valid search value    | Matching result                  |
| EP-197 | Partial valid value         | Matching results where supported |
| EP-198 | No-match value              | Empty result                     |
| EP-199 | Empty search                | Default/all authorized results   |
| EP-200 | Leading/trailing whitespace | Normalized                       |
| EP-201 | Special characters          | Safe handling                    |
| EP-202 | Script-like input           | No execution                     |
| EP-203 | SQL-like input              | No query manipulation            |
| EP-204 | Over-length input           | Reject/truncate safely           |

---

# 39. Transaction Status Partitions

| ID     | State      | Expected Representation            |
| ------ | ---------- | ---------------------------------- |
| EP-205 | PENDING    | Pending                            |
| EP-206 | PROCESSING | In progress                        |
| EP-207 | COMPLETED  | Final success                      |
| EP-208 | FAILED     | No successful financial completion |
| EP-209 | CANCELLED  | Not completed                      |
| EP-210 | REVERSED   | Reversal relationship shown        |

---

# 40. Transaction Type Partitions

| ID     | Type              |
| ------ | ----------------- |
| EP-211 | Transfer          |
| EP-212 | Payment           |
| EP-213 | Card purchase     |
| EP-214 | Card refund       |
| EP-215 | Loan disbursement |
| EP-216 | Loan repayment    |
| EP-217 | Deposit funding   |
| EP-218 | Deposit interest  |
| EP-219 | Deposit maturity  |
| EP-220 | Fee               |
| EP-221 | Reversal          |

Representative transaction from each class should be checked in:

* Transaction history
* Statements
* API
* Database

---

# 41. Debit / Credit Partitions

| ID     | Class                       | Expected                                      |
| ------ | --------------------------- | --------------------------------------------- |
| EP-222 | Debit transaction           | Decreases financial balance where applicable  |
| EP-223 | Credit transaction          | Increases balance                             |
| EP-224 | Pending hold                | Reduces available balance according to design |
| EP-225 | Non-financial failed record | Does not change balance                       |

---

# 42. Statement Date Partitions

| ID     | Date Range Type        | Expected     |
| ------ | ---------------------- | ------------ |
| EP-226 | Valid historical range | Generate     |
| EP-227 | Current valid range    | Generate     |
| EP-228 | Future-only range      | Empty/reject |
| EP-229 | Start after end        | Reject       |
| EP-230 | Range beyond maximum   | Reject       |
| EP-231 | Valid single-day range | Generate     |

---

# 43. Statement Transaction Partitions

| ID     | Transaction Condition   | Expected Statement Behavior                 |
| ------ | ----------------------- | ------------------------------------------- |
| EP-232 | Completed within range  | Include                                     |
| EP-233 | Completed outside range | Exclude                                     |
| EP-234 | Failed transaction      | No completed financial effect               |
| EP-235 | Cancelled transaction   | No completed financial effect               |
| EP-236 | Reversed transaction    | Represent according to reversal model       |
| EP-237 | Pending transaction     | Include/exclude according to defined policy |

---

# 44. Notification Event Partitions

| ID     | Event Type           | Representative      |
| ------ | -------------------- | ------------------- |
| EP-238 | Financial success    | Successful transfer |
| EP-239 | Financial failure    | Failed payment      |
| EP-240 | Security event       | Password changed    |
| EP-241 | Card event           | Card frozen         |
| EP-242 | Loan event           | Loan approved       |
| EP-243 | Deposit event        | Deposit matured     |
| EP-244 | Profile event        | Email changed       |
| EP-245 | Administrative event | Account restricted  |

---

# 45. Notification Delivery Partitions

| ID     | Delivery Condition               | Expected                    |
| ------ | -------------------------------- | --------------------------- |
| EP-246 | Enabled optional notification    | Deliver                     |
| EP-247 | Disabled optional notification   | Suppress                    |
| EP-248 | Mandatory security notification  | Deliver according to policy |
| EP-249 | Provider temporarily unavailable | Retry/record failure        |
| EP-250 | Permanent delivery failure       | Record according to design  |
| EP-251 | Wrong customer mapping           | Must never deliver          |

---

# 46. Notification Read-State Partitions

| ID     | State     | Expected                                                     |
| ------ | --------- | ------------------------------------------------------------ |
| EP-252 | UNREAD    | Included in unread count                                     |
| EP-253 | READ      | Not counted unread                                           |
| EP-254 | DISMISSED | Hidden according to design                                   |
| EP-255 | DELETED   | Removed from user's active notification view where supported |

---

# 47. File Upload Type Partitions

Where profile/KYC file upload exists.

| ID     | Partition                              | Expected |
| ------ | -------------------------------------- | -------- |
| EP-256 | Allowed image/document type            | Accept   |
| EP-257 | Unsupported extension                  | Reject   |
| EP-258 | Allowed extension with invalid content | Reject   |
| EP-259 | Empty file                             | Reject   |
| EP-260 | Oversized file                         | Reject   |
| EP-261 | Valid file below limit                 | Accept   |

---

# 48. File Size Partitions

Assume maximum:

```text id="rvmr1l"
5 MB
```

| ID     | Partition     | Expected |
| ------ | ------------- | -------- |
| EP-262 | 0 bytes       | Reject   |
| EP-263 | >0 and <=5 MB | Accept   |
| EP-264 | >5 MB         | Reject   |

Boundary values are handled separately by BVA.

---

# 49. Pagination Partitions

Assume page size:

```text id="onvm2a"
25
```

| ID     | Dataset       | Expected       |
| ------ | ------------- | -------------- |
| EP-265 | No records    | Empty          |
| EP-266 | 1–25 records  | One page       |
| EP-267 | 26–50 records | Two pages      |
| EP-268 | >50 records   | Multiple pages |

Verify no record duplication or loss between partitions.

---

# 50. Date / Time Partitions

Generic time-based functionality can be grouped into:

| ID     | Partition                          | Expected                              |
| ------ | ---------------------------------- | ------------------------------------- |
| EP-269 | Past date                          | Usually invalid for future scheduling |
| EP-270 | Current date                       | Conditional                           |
| EP-271 | Valid future date                  | Accept                                |
| EP-272 | Future date beyond allowed horizon | Reject                                |
| EP-273 | Invalid calendar date              | Reject                                |

Applicable to:

* Transfers
* Payments
* Loan schedules
* Deposit maturity
* Statements
* Recurring instructions

---

# 51. Scheduled Transfer Partitions

| ID     | Condition                                             | Expected         |
| ------ | ----------------------------------------------------- | ---------------- |
| EP-274 | Valid future schedule + sufficient funds at execution | Execute          |
| EP-275 | Valid schedule + insufficient funds                   | Fail safely      |
| EP-276 | Valid schedule + source frozen                        | Reject execution |
| EP-277 | Valid schedule + beneficiary disabled                 | Reject           |
| EP-278 | Cancelled schedule                                    | Never execute    |
| EP-279 | Past schedule date                                    | Reject creation  |

---

# 52. Recurring Transaction Partitions

| ID     | Condition                              | Expected             |
| ------ | -------------------------------------- | -------------------- |
| EP-280 | Valid active recurring instruction     | Execute on schedule  |
| EP-281 | Paused recurring instruction           | No execution         |
| EP-282 | Cancelled recurring instruction        | No future execution  |
| EP-283 | End date reached                       | Stop                 |
| EP-284 | Insufficient balance on one occurrence | Apply failure policy |
| EP-285 | Invalid frequency                      | Reject               |

---

# 53. Fee Type Partitions

| ID     | Fee Type                        | Expected                      |
| ------ | ------------------------------- | ----------------------------- |
| EP-286 | No fee                          | No fee debit                  |
| EP-287 | Fixed fee                       | Apply configured fixed amount |
| EP-288 | Percentage fee                  | Calculate percentage          |
| EP-289 | Tiered fee                      | Apply correct tier            |
| EP-290 | Invalid negative configured fee | Reject configuration          |

Representative tests should verify:

* Calculation
* Display
* Account impact
* Statement
* Transaction history
* Database

---

# 54. Interest Rate Partitions

For loan/deposit rates:

| ID     | Partition                         | Expected             |
| ------ | --------------------------------- | -------------------- |
| EP-291 | Valid configured rate             | Use                  |
| EP-292 | Zero rate where permitted         | Process correctly    |
| EP-293 | Negative rate where unsupported   | Reject               |
| EP-294 | Unauthorized client-supplied rate | Ignore/reject        |
| EP-295 | Unsupported excessive rate        | Reject configuration |

---

# 55. Admin Reason Partitions

For privileged actions requiring a reason:

| ID     | Partition            | Expected            |
| ------ | -------------------- | ------------------- |
| EP-296 | Valid reason         | Accept              |
| EP-297 | Empty reason         | Reject              |
| EP-298 | Spaces only          | Reject              |
| EP-299 | Below minimum length | Reject              |
| EP-300 | Above maximum length | Reject              |
| EP-301 | Script-like content  | Store/render safely |

---

# 56. Admin Permission Partitions

Example:

| ID     | Admin Class              | Expected               |
| ------ | ------------------------ | ---------------------- |
| EP-302 | Full permission          | Allow operation        |
| EP-303 | Read-only                | Deny state change      |
| EP-304 | Correct specialized role | Allow scoped operation |
| EP-305 | Wrong specialized role   | Deny                   |
| EP-306 | Disabled admin           | Deny                   |
| EP-307 | Standard customer        | Deny                   |

---

# 57. Account Closure Partitions

| ID     | Condition                           | Expected                   |
| ------ | ----------------------------------- | -------------------------- |
| EP-308 | Zero balance + no dependencies      | Allow                      |
| EP-309 | Positive balance                    | Reject                     |
| EP-310 | Negative balance                    | Reject                     |
| EP-311 | Pending transaction                 | Reject                     |
| EP-312 | Active loan/deposit/card dependency | Reject/resolve             |
| EP-313 | Already closed                      | Reject/idempotent handling |

---

# 58. Card Replacement Partitions

| ID     | Card Condition          | Expected                      |
| ------ | ----------------------- | ----------------------------- |
| EP-314 | Lost active card        | Replacement allowed           |
| EP-315 | Stolen card             | Block + replacement           |
| EP-316 | Expired card            | Replacement allowed           |
| EP-317 | Already replaced card   | Prevent duplicate replacement |
| EP-318 | Another customer's card | Deny                          |

---

# 59. Reversal Eligibility Partitions

| ID     | Transaction Condition    | Expected                                    |
| ------ | ------------------------ | ------------------------------------------- |
| EP-319 | Completed and reversible | Allow authorized reversal                   |
| EP-320 | Failed transaction       | Do not reverse as completed financial event |
| EP-321 | Cancelled transaction    | Reject reversal                             |
| EP-322 | Already reversed         | Reject second reversal                      |
| EP-323 | Unauthorized actor       | Deny                                        |
| EP-324 | Nonexistent transaction  | Reject                                      |

---

# 60. Data Ownership Partitions

For any customer-owned resource:

```text id="yq603a"
Account
Beneficiary
Card
Loan
Deposit
Transaction
Statement
Notification
Session
Device
```

use these partitions:

| ID     | Ownership Class                                   | Expected                       |
| ------ | ------------------------------------------------- | ------------------------------ |
| EP-325 | Resource belongs to authenticated user            | Allow according to permissions |
| EP-326 | Resource belongs to another user                  | Deny                           |
| EP-327 | Resource does not exist                           | Reject                         |
| EP-328 | Resource belongs to user but state forbids action | Reject                         |
| EP-329 | Authorized administrator accesses resource        | Apply role rules               |

**Priority:** P0

---

# 61. API Authentication Partitions

| ID     | Credential State            | Expected                  |
| ------ | --------------------------- | ------------------------- |
| EP-330 | Valid credential            | Authenticate              |
| EP-331 | Missing credential          | Reject                    |
| EP-332 | Invalid credential          | Reject                    |
| EP-333 | Expired credential          | Reject                    |
| EP-334 | Revoked credential          | Reject                    |
| EP-335 | Valid credential wrong role | Deny restricted operation |

---

# 62. API Payload Partitions

| ID     | Payload Class            | Expected                  |
| ------ | ------------------------ | ------------------------- |
| EP-336 | Valid complete payload   | Process                   |
| EP-337 | Missing required field   | Reject                    |
| EP-338 | Invalid field type       | Reject                    |
| EP-339 | Unknown extra field      | Ignore/reject per schema  |
| EP-340 | Protected field supplied | Reject/ignore securely    |
| EP-341 | Malformed JSON           | Reject                    |
| EP-342 | Empty body               | Reject when body required |

---

# 63. HTTP Method Partitions

For a typical resource:

| ID     | Method Class                               | Expected               |
| ------ | ------------------------------------------ | ---------------------- |
| EP-343 | Supported read method                      | Allow authorized read  |
| EP-344 | Supported write method                     | Allow authorized write |
| EP-345 | Unsupported method                         | Reject                 |
| EP-346 | Read method attempting state change        | No modification        |
| EP-347 | Method used without required authorization | Reject                 |

---

# 64. Financial Transaction Submission Partitions

| ID     | Submission Type                 | Expected                             |
| ------ | ------------------------------- | ------------------------------------ |
| EP-348 | First valid submission          | Process                              |
| EP-349 | Duplicate identical submission  | Prevent unintended duplicate         |
| EP-350 | Retry after known failure       | Process according to retry policy    |
| EP-351 | Retry after ambiguous timeout   | Idempotently determine/process state |
| EP-352 | Concurrent duplicate submission | One intended effect                  |

---

# 65. Concurrency Partitions

For financial operations:

| ID     | Concurrent Condition                  | Expected                         |
| ------ | ------------------------------------- | -------------------------------- |
| EP-353 | Combined debit below balance          | Both may succeed                 |
| EP-354 | Combined debit exactly equals balance | Both may succeed if rules permit |
| EP-355 | Combined debit exceeds balance        | Not all can succeed              |
| EP-356 | Same unique bill submitted twice      | One settlement                   |
| EP-357 | Same loan disbursement twice          | One credit                       |
| EP-358 | Same maturity payout twice            | One payout                       |

---

# 66. Transaction Reconciliation Partitions

| ID     | Reconciliation Class                             | Expected |
| ------ | ------------------------------------------------ | -------- |
| EP-359 | UI/API/DB all match                              | Pass     |
| EP-360 | UI differs from API                              | Fail     |
| EP-361 | API differs from DB                              | Fail     |
| EP-362 | Transaction history differs from balance         | Fail     |
| EP-363 | Statement differs from history                   | Fail     |
| EP-364 | Notification differs from final financial status | Fail     |

---

# 67. Error Response Partitions

| ID     | Error Type           | Expected                       |
| ------ | -------------------- | ------------------------------ |
| EP-365 | Validation error     | Safe user-readable validation  |
| EP-366 | Authentication error | No protected data              |
| EP-367 | Authorization error  | No resource leakage            |
| EP-368 | Not-found error      | Safe response                  |
| EP-369 | Conflict/state error | Current state explained safely |
| EP-370 | Server error         | No stack trace/secrets         |
| EP-371 | Dependency timeout   | Safe deterministic handling    |

---

# 68. Browser Session Partitions

| ID     | Browser State                       | Expected                         |
| ------ | ----------------------------------- | -------------------------------- |
| EP-372 | Fresh authenticated session         | Normal use                       |
| EP-373 | Session after logout                | Protected access denied          |
| EP-374 | Expired session                     | Denied                           |
| EP-375 | Session revoked from another device | Denied                           |
| EP-376 | Two valid concurrent sessions       | Follow concurrent-session policy |

---

# 69. Cross-Browser Partitions

Instead of testing every workflow in every browser, browser environments can be partitioned by engine:

| ID     | Browser Engine/Class | Representative           |
| ------ | -------------------- | ------------------------ |
| EP-377 | Chromium             | Chrome                   |
| EP-378 | Chromium secondary   | Edge                     |
| EP-379 | Gecko                | Firefox                  |
| EP-380 | WebKit               | Safari/WebKit automation |

Critical financial workflows should still receive wider coverage than low-risk UI functionality.

---

# 70. Responsive Viewport Partitions

Representative classes:

| ID     | Class                  | Representative |
| ------ | ---------------------- | -------------- |
| EP-381 | Large desktop          | 1920×1080      |
| EP-382 | Laptop/smaller desktop | 1366×768       |
| EP-383 | Tablet                 | 768×1024       |
| EP-384 | Large mobile           | 390×844        |
| EP-385 | Small mobile           | 360×800        |

Important flows should be tested with at least one representative from each required class.

---

# 71. Data Volume Partitions

For lists such as transactions, notifications, customers, and audit records:

| ID     | Data Volume      | Expected                  |
| ------ | ---------------- | ------------------------- |
| EP-386 | Zero records     | Empty state               |
| EP-387 | Small dataset    | Normal rendering          |
| EP-388 | Exactly one page | Correct                   |
| EP-389 | Multiple pages   | Pagination                |
| EP-390 | Large dataset    | Correct scalable behavior |

---

# 72. Input Character Partitions

Text fields should consider:

| ID     | Character Class         | Example          |
| ------ | ----------------------- | ---------------- |
| EP-391 | Latin letters           | `Mohamed`        |
| EP-392 | Arabic/Unicode          | Arabic name/text |
| EP-393 | Digits                  | `12345`          |
| EP-394 | Valid punctuation       | `Apt. 12-B`      |
| EP-395 | Leading/trailing spaces | `text`           |
| EP-396 | Emoji where unsupported | `Test😀`         |
| EP-397 | Script-like syntax      | `<script>`       |
| EP-398 | SQL-like syntax         | `' OR '1'='1`    |

Expected behavior depends on field purpose but must always be safe.

---

# 73. Currency Partitions

Where multiple currencies are supported:

| ID     | Currency Condition                                     | Expected                 |
| ------ | ------------------------------------------------------ | ------------------------ |
| EP-399 | Supported account currency                             | Accept                   |
| EP-400 | Supported different transaction currency               | Convert/process by rules |
| EP-401 | Unsupported currency                                   | Reject                   |
| EP-402 | Currency omitted when required                         | Reject                   |
| EP-403 | Manipulated currency incompatible with account/product | Reject                   |

If the project remains single-currency, unsupported-currency requests should still be rejected server-side.

---

# 74. Date Validity Partitions

| ID     | Date Class                           | Expected |
| ------ | ------------------------------------ | -------- |
| EP-404 | Valid ordinary date                  | Accept   |
| EP-405 | Valid leap date                      | Accept   |
| EP-406 | Invalid February 29 in non-leap year | Reject   |
| EP-407 | Impossible date such as April 31     | Reject   |
| EP-408 | Missing required date                | Reject   |
| EP-409 | Malformed date string                | Reject   |

---

# 75. Account Number / Identifier Partitions

| ID     | Class                                 | Expected                      |
| ------ | ------------------------------------- | ----------------------------- |
| EP-410 | Valid existing owned account          | Process                       |
| EP-411 | Valid existing other-customer account | Deny where ownership required |
| EP-412 | Valid-format nonexistent account      | Reject                        |
| EP-413 | Invalid-format account identifier     | Reject                        |
| EP-414 | Empty identifier                      | Reject                        |
| EP-415 | Over-length identifier                | Reject                        |

---

# 76. Admin Search Result Partitions

| ID     | Result Class                        | Expected                        |
| ------ | ----------------------------------- | ------------------------------- |
| EP-416 | Exact one match                     | Display one                     |
| EP-417 | Multiple authorized matches         | Display result set              |
| EP-418 | No match                            | Empty state                     |
| EP-419 | Match exists but role cannot access | Do not expose unauthorized data |
| EP-420 | Large result set                    | Paginate/filter                 |

---

# 77. Audit Event Partitions

Critical audit classes include:

| ID     | Audit Class                   |
| ------ | ----------------------------- |
| EP-421 | Authentication/security event |
| EP-422 | Customer state change         |
| EP-423 | Account state change          |
| EP-424 | Card state change             |
| EP-425 | Loan decision                 |
| EP-426 | Financial reversal            |
| EP-427 | Limit/fee configuration       |
| EP-428 | Admin permission change       |

For each class verify:

* Actor
* Action
* Target
* Timestamp
* Result
* No secret exposure

---

# 78. Valid vs Invalid State Transition Partitions

Instead of testing every possible pair equally, classify transitions as:

```text id="qxgnh0"
Valid forward transition
Valid recovery transition
Valid terminal transition
Invalid backward transition
Invalid skip transition
Invalid action from terminal state
```

Example for card:

| ID     | Class                     | Example                                     |
| ------ | ------------------------- | ------------------------------------------- |
| EP-429 | Valid forward             | INACTIVE → ACTIVE                           |
| EP-430 | Valid reversible state    | ACTIVE → FROZEN → ACTIVE                    |
| EP-431 | Valid terminal            | ACTIVE → CANCELLED                          |
| EP-432 | Invalid terminal recovery | CANCELLED → ACTIVE                          |
| EP-433 | Invalid blocked recovery  | BLOCKED → ACTIVE without authorized process |

---

# 79. Equivalence Partitioning With Boundary Value Analysis

EP and BVA should be used together.

Example:

```text id="qc039k"
Transfer range:
1.00–100,000.00
```

EP gives:

```text id="v7ld2b"
Invalid Low
Valid
Invalid High
```

Representative EP tests:

```text id="rs225j"
0.50
50,000
150,000
```

BVA adds:

```text id="fijsru"
0.99
1.00
1.01
99,999.99
100,000.00
100,000.01
```

EP provides broad category coverage.

BVA provides precision around the edges.

Both are needed for critical banking rules.

---

# 80. Equivalence Partitioning With State Transition Testing

State fields should not only be partitioned by current state.

They should later also be tested for valid transitions.

Example:

```text id="jii463"
Card States:
ACTIVE
FROZEN
BLOCKED
EXPIRED
CANCELLED
```

EP verifies behavior inside each state.

State Transition Testing verifies which changes between states are valid.

---

# 81. Equivalence Partitioning With Decision Tables

Some functionality depends on combinations of partitions.

Example transfer:

```text id="32jr8b"
Account state
Beneficiary state
Balance class
Transfer limit class
Customer status
```

Testing every combination manually may be excessive.

Decision tables are useful for selecting important combinations of these partitions.

---

# 82. High-Risk EP Regression Set

The minimum equivalence-partition regression set should cover:

* Valid/invalid transfer amounts
* Sufficient/insufficient balances
* All major account states
* All major beneficiary states
* Payment bill states
* All card states
* Valid/invalid loan amounts
* Loan eligibility classes
* Loan lifecycle states
* Deposit states
* Valid/invalid authentication
* Session states
* MFA states
* Customer states
* KYC states
* Roles/permissions
* Transaction statuses
* Statement date classes
* Notification success/failure classes
* Ownership classes
* API authentication states
* Valid/invalid payload types
* Duplicate/replay classes
* Concurrency classes

---

# 83. Automation Candidates

Equivalence partitions are ideal for data-driven tests.

Example transfer data:

```text id="efgl41"
[
  {
    "partition": "INVALID_LOW",
    "amount": 0.50,
    "expected": "REJECTED"
  },
  {
    "partition": "VALID",
    "amount": 25000.00,
    "expected": "ACCEPTED"
  },
  {
    "partition": "INVALID_HIGH",
    "amount": 150000.00,
    "expected": "REJECTED"
  }
]
```

This approach can later be used in:

* Selenium
* Cypress
* Playwright
* Jest
* Postman
* REST Assured
* Cucumber

---

# 84. API Automation Application

For an API field such as:

```text id="c6t7gv"
amount
```

equivalence classes could generate tests for:

```text id="5654vg"
Valid numeric amount
Too-small amount
Too-large amount
Zero
Negative
String
Boolean
Null
Missing
Array/object instead of number
```

This provides broader schema and business-rule validation than testing only valid/invalid numbers.

---

# 85. SQL Validation Application

For each accepted valid partition, validate:

```text id="cildzn"
Correct record persisted
Correct relationship
Correct state
Correct precision
Correct balance effect
```

For rejected invalid partitions:

```text id="plpzdx"
No invalid financial record
No unintended balance update
No orphan record
No invalid state transition
```

---

# 86. BDD Example

```gherkin id="ws9bo6"
Feature: Transfer amount equivalence partitions

Scenario Outline: Transfer amount classes are validated correctly
  Given the customer has sufficient available balance
  And the source account is active
  And the beneficiary is active
  When the customer submits a transfer amount from the <partition> partition
  Then the request should be <result>

Examples:
  | partition            | amount    | result   |
  | below minimum        | 0.50      | rejected |
  | valid amount         | 25000.00  | accepted |
  | above maximum        | 150000.00 | rejected |
  | zero                 | 0.00      | rejected |
  | negative             | -100.00   | rejected |
```

---

# 87. Risk Traceability

Equivalence Partitioning directly supports mitigation of:

```text id="qwmp5e"
RISK-001 — Incorrect account balance
RISK-002 — Unauthorized customer data access
RISK-005 — Authentication bypass
RISK-006 — Privilege escalation
RISK-007 — Transfer exceeding available balance
RISK-008 — Transfer limit bypass
RISK-009 — Frozen account can transact
RISK-011 — Incorrect loan calculations
RISK-012 — Incorrect deposit calculations
RISK-017 — Incorrect beneficiary handling
RISK-018 — Closed destination account accepts funds
RISK-024 — Frozen card remains usable
RISK-025 — Blocked card remains usable
RISK-026 — Failed payment changes balance
RISK-030 — Unauthorized API request
RISK-037 — Frontend-only validation
RISK-039 — API/database inconsistency
RISK-044 — OTP reuse
RISK-045 — Expired OTP accepted
RISK-047 — Unauthorized resource access
```

---

# 88. Equivalence Partition Coverage Summary

This document defines equivalence classes for:

* Transfer amounts
* Balances
* Account states
* Beneficiaries
* Transfer limits
* Payments
* Bills
* Payees
* Cards
* Card transactions
* Loans
* Loan eligibility
* Loan states
* Loan repayment
* Deposits
* Deposit funding
* Deposit states
* Authentication
* Passwords
* MFA
* Reset tokens
* Sessions
* Roles
* Customer status
* KYC
* Emails
* Phone numbers
* Names
* Search
* Transaction status
* Transaction types
* Debit/credit classes
* Statements
* Notifications
* Files
* Pagination
* Dates
* Scheduled operations
* Recurring operations
* Fees
* Interest
* Admin permissions
* Account closure
* Card replacement
* Reversal eligibility
* Data ownership
* API authentication
* API payloads
* HTTP methods
* Duplicate/replay behavior
* Concurrency
* Financial reconciliation
* Errors
* Browser sessions
* Browser engines
* Responsive layouts
* Data volumes
* Character types
* Currency
* Identifiers
* Audit events
* State transitions

---

# 89. Final Equivalence Partitioning Principle

Equivalence Partitioning should answer:

```text id="k2lktj"
What fundamentally different classes of input or system state exist?

Which classes should be accepted?

Which classes should be rejected?

Can one representative from a class adequately test the shared behavior?

Which high-risk classes need multiple representatives anyway?

Are the same partitions enforced by UI, API, and backend rules?

Can invalid partitions still create database or financial side effects?

Does authorization create separate valid and invalid classes?

Does current lifecycle state create separate behavior classes?
```

The goal is not to minimize testing blindly.

The goal is to avoid redundant testing while ensuring every materially different banking condition is represented.

For critical financial and security rules, Equivalence Partitioning should be combined with:

```text id="zpsdoq"
Boundary Value Analysis
Decision Tables
State Transition Testing
Concurrency Testing
Negative Testing
API Validation
Database Validation
```

This creates efficient but comprehensive test coverage.
