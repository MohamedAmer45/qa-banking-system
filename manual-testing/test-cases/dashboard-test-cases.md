# Banking System — Dashboard Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Dashboard (`DASH`)             |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

## 2. Conventions

Test case ids use `DASH-TC-XXX`.

Requirements are referenced by their catalog id (`DASH-001`), with no `REQ-`
prefix. The catalog in `requirements/requirements-catalog.md` is the source of
truth.

Test data is the seeded `customer@novabank.test` / `Demo123!`, MFA `123456`.

**Balances are asserted as relationships, never as absolute amounts.** Seven
other suites move money through this account, so "available does not exceed the
balance" holds at any time while "the balance is 249,000.00" holds only against
a fresh seed.

---

## 3. Automation

All seven requirements are automated in
`playwright/tests/dashboard/dashboard.spec.ts` (13 tests). The cases below are
the manual definition of the same coverage; the Automation line on each records
where it runs.

---

## DASH-TC-001 — Overview is reached on sign-in

**Priority:** P0
**Requirement:** DASH-001
**Automation:** Playwright

### Steps

1. Sign in as the seeded customer and complete the MFA step.

### Expected Result

The overview loads without further navigation. The "Your accounts" panel is
present and lists at least one account. The account-count KPI is not lower than
the number of cards shown.

---

## DASH-TC-002 — Balance and available balance are both shown

**Priority:** P0
**Requirement:** DASH-002
**Automation:** Playwright

### Steps

1. Sign in and remain on the overview.
2. Read each account card.

### Expected Result

Every card shows a balance and an available balance. Both are integers in minor
units. Available is less than or equal to the balance on every card.

---

## DASH-TC-003 — EGP total is reported in minor units

**Priority:** P1
**Requirement:** DASH-002
**Automation:** Playwright

### Expected Result

The EGP balance KPI exposes an integer minor-unit value, so the figure can be
asserted without parsing a formatted currency string.

---

## DASH-TC-004 — Recent transactions are listed

**Priority:** P1
**Requirement:** DASH-003
**Automation:** Playwright

### Steps

1. Sign in and read the "Recent transactions" panel.

### Expected Result

The panel lists up to five of the customer's most recent transactions across
their accounts, or an explicit empty state. Each row carries a direction of
`DEBIT` or `CREDIT` and a positive amount in minor units.

---

## DASH-TC-005 — Active cards are listed

**Priority:** P2
**Requirement:** DASH-004
**Automation:** Playwright

### Expected Result

Only cards with status `ACTIVE` appear. Each shows a masked last four and an
expiry. A customer with no active card sees an explicit empty state.

---

## DASH-TC-006 — Non-active cards are excluded

**Priority:** P2
**Requirement:** DASH-004
**Automation:** Playwright

### Steps

1. Ensure the customer holds a card that is not `ACTIVE` (for example
   `PENDING_ACTIVATION` or `BLOCKED`) — see `card-test-cases.md`.
2. Read the overview cards panel.

### Expected Result

The non-active card does not appear on the overview. It remains visible in the
Cards module, which shows the full lifecycle.

---

## DASH-TC-007 — Upcoming scheduled payments are listed

**Priority:** P2
**Requirement:** DASH-005
**Automation:** Playwright

### Steps

1. Schedule a bill payment for a future date — see `payment-test-cases.md`.
2. Return to the overview.

### Expected Result

The scheduled payment appears under "Upcoming scheduled payments", dated in the
future. Scheduled transfers appear in the same panel. Entries are ordered
soonest first.

---

## DASH-TC-008 — No scheduled payments

**Priority:** P3
**Requirement:** DASH-005
**Automation:** Playwright

### Expected Result

A customer with nothing scheduled sees the panel with an explicit empty state,
not a blank area. `DASH-005` says "where applicable"; the empty state is what
separates "nothing is scheduled" from "the panel did not render".

---

## DASH-TC-009 — Unread notification count

**Priority:** P2
**Requirement:** DASH-006
**Automation:** Playwright

### Expected Result

The overview reports a non-negative integer unread count, and it matches the
topbar badge. The two are renderings of one fact and must not disagree.

---

## DASH-TC-010 — Alerts are marked read or unread

**Priority:** P2
**Requirement:** DASH-006
**Automation:** Playwright

### Steps

1. Open Notifications and mark one notification as read.
2. Return to the overview.

### Expected Result

That alert is no longer flagged unread, and the unread count has decreased by
one.

---

## DASH-TC-011 — Dashboard shows only the customer's own data

**Priority:** P0
**Requirement:** DASH-007
**Automation:** Playwright

### Steps

1. Sign in as the seeded customer.
2. Read the entire overview.

### Expected Result

The customer's own name appears. No other seeded identity's name or email
appears anywhere on the page.

---

## DASH-TC-012 — A second customer sees their own dashboard

**Priority:** P0
**Requirement:** DASH-007
**Automation:** Playwright

### Steps

1. Sign in as `customer@novabank.test` and note the overview.
2. Sign out, clear the session, and sign in as `receiver@novabank.test`.

### Expected Result

The second customer's own accounts and identity are shown. The first customer's
email does not appear.

---

## DASH-TC-013 — Direct navigation does not bypass ownership

**Priority:** P0
**Requirement:** DASH-007
**Automation:** REST Assured (`DataExposureTest`)

### Steps

1. Authenticate as the customer.
2. Call each endpoint the dashboard reads, requesting another customer's
   identifiers where the endpoint accepts one.

### Expected Result

Each endpoint scopes to the signed-in user. Ownership is enforced server-side;
the dashboard hiding another customer's data is not what protects it.
