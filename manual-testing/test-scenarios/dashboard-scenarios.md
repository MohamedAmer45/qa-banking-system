# Banking System — Dashboard Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Dashboard (`DASH`)             |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

## 2. Scope

The customer overview shown immediately after sign-in: `DASH-001` to `DASH-007`
in `requirements/requirements-catalog.md`.

The dashboard reads from accounts, transactions, cards, bill payments,
transfers and notifications. It owns none of that data. That shapes what is
worth testing here: the dashboard is a **view**, so the scenarios below check
that it renders each required panel, agrees with the modules it summarises, and
shows nothing belonging to another customer. Rules about the underlying data —
how a balance changes, when a card activates — belong to those modules' own
scenarios and are not repeated.

---

## 3. History

This module had no scenarios and no automated coverage until 2026-09-23.

The reason was not oversight. Three of its seven requirements — recent
transactions, active cards and upcoming scheduled payments — were never
rendered, so there was nothing to write a scenario against. That is recorded as
`BUG-DASH-001` and has been fixed; these scenarios cover the result.

---

## 4. Scenarios

### DASH-S-01 — Overview after sign-in

**Requirement:** DASH-001

A customer signs in and lands on an overview that lists their accounts without
further navigation.

Checks: the accounts panel is present; it lists at least one account; the
account-count KPI agrees with the panel.

---

### DASH-S-02 — Current and available balances

**Requirement:** DASH-002

Each account on the overview shows both its balance and the amount actually
available to spend.

Checks: both figures are present for every account card; both are integers in
minor units; available never exceeds the balance.

The invariant is the relationship, not the figures. Absolute amounts are stable
only against a freshly seeded database, and every other suite moves money.

---

### DASH-S-03 — Recent transactions

**Requirement:** DASH-003

The overview summarises the customer's most recent ledger activity across their
accounts.

Checks: the panel lists rows or an explicit empty state; each row carries a
direction (`DEBIT` or `CREDIT`) and a positive amount; the panel is a summary
and does not attempt the full ledger.

---

### DASH-S-04 — Active cards

**Requirement:** DASH-004

The overview lists the customer's active cards.

Checks: only `ACTIVE` cards appear; each shows a masked last four; a customer
with no active card gets an explicit empty state rather than a blank area.

---

### DASH-S-05 — Upcoming scheduled payments

**Requirement:** DASH-005

Scheduled bill payments and scheduled transfers that have not yet run are shown
together, soonest first.

Checks: every entry is dated in the future; entries are ordered by date; a
customer with nothing scheduled gets an explicit empty state.

`DASH-005` says "where applicable". The empty state is what makes that
testable: without it, "nothing scheduled" and "the panel failed to render" look
identical.

---

### DASH-S-06 — Unread notifications

**Requirement:** DASH-006

The overview reports how many notifications are unread and marks which ones.

Checks: the count is a non-negative integer; the overview count and the topbar
badge agree; each alert is flagged read or unread.

---

### DASH-S-07 — Customer isolation

**Requirement:** DASH-007

A customer's dashboard contains only their own financial information.

Checks: no other seeded identity's name or email appears anywhere on the page;
signing in as a second customer produces that customer's own dashboard.

This is the one dashboard scenario that is a security assertion rather than a
presentation one, and it is rated accordingly.

---

## 5. Out of scope here

| Concern | Covered by |
|---|---|
| Back-office dashboard | `admin-scenarios.md` |
| Notification delivery and read state | `notification-scenarios.md` |
| Card lifecycle | `card-scenarios.md` |
| Scheduling a payment | `payment-scenarios.md` |
| Balance arithmetic after a transfer | `transfer-scenarios.md` |
