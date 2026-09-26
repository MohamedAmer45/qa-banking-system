# Banking System — Accessibility Test Cases

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Accessibility (`A11Y`)         |
| Document | Detailed Test Cases            |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

## 2. Conventions

Test case ids use `A11Y-TC-XXX`. Requirements are referenced by catalog id
(`A11Y-001`), with no `REQ-` prefix.

Every case names the WCAG 2.1 AA success criterion behind it, so a disagreement
is settled against the standard rather than an opinion.

---

## 3. Automation

`A11Y-TC-001` to `A11Y-TC-008` are automated in
`playwright/tests/accessibility/accessibility.spec.ts` — 30 tests, scanning 23
views plus an open modal, asserting zero violations rather than a tolerated list.

`A11Y-TC-009` to `A11Y-TC-011` are **manual only** and marked as such. They are
the judgement-based checks no engine settles, and leaving them out of the
automated count is the point rather than an omission.

---

## A11Y-TC-001 — Every form control is named

**Priority:** P0
**Requirement:** A11Y-001, A11Y-003 — WCAG 1.3.1, 4.1.2
**Automation:** Playwright + axe-core

### Steps

1. Scan each customer view and each back-office view.

### Expected Result

No `label`, `select-name` or `button-name` violation. Every input, select and
textarea exposes a name, including filter controls that carry no visible label.

---

## A11Y-TC-002 — An open modal is named and its fields are labelled

**Priority:** P0
**Requirement:** A11Y-001, A11Y-003 — WCAG 4.1.2
**Automation:** Playwright + axe-core

### Steps

1. Sign in, open Transfers, open the transfer form.
2. Scan with the modal still open.

### Expected Result

No violations inside the dialog. Scanning the view with the modal closed is not
sufficient: the dialog's markup does not exist until it is opened, which is how
an unlabelled form inside one survives a green run.

---

## A11Y-TC-003 — Text meets the contrast minimum

**Priority:** P1
**Requirement:** A11Y-002 — WCAG 1.4.3, 1.4.11
**Automation:** Playwright + axe-core

### Expected Result

No `color-contrast` violation on any view. The dark theme's muted greys are where
this fails first; the sidebar section heading was one such failure at 3.73:1.

---

## A11Y-TC-004 — Structure, ARIA and ids are valid

**Priority:** P2
**Requirement:** A11Y-004, A11Y-005, A11Y-006 — WCAG 1.3.1, 4.1.1, 4.1.2
**Automation:** Playwright + axe-core

### Expected Result

No `heading-order`, `landmark-*`, `aria-*` or `duplicate-id` violation.

---

## A11Y-TC-005 — A modal is announced as a dialog

**Priority:** P0
**Requirement:** A11Y-007 — WCAG 4.1.2
**Automation:** Playwright

### Expected Result

The modal carries `role="dialog"`, `aria-modal="true"` and a non-empty accessible
name taken from its heading.

---

## A11Y-TC-006 — Focus enters a modal and cannot leave it

**Priority:** P0
**Requirement:** A11Y-007 — WCAG 2.1.2, 2.4.3
**Automation:** Playwright

### Steps

1. Open the transfer form.
2. Confirm focus is inside the dialog.
3. Press Tab at least 25 times.

### Expected Result

Focus is inside the dialog after opening and still inside after tabbing. One Tab
would prove nothing — the next control is inside the dialog whether or not focus
is trapped, so the check must tab far enough to have escaped an untrapped dialog
several times over.

---

## A11Y-TC-007 — Escape closes a modal and focus returns to its trigger

**Priority:** P0
**Requirement:** A11Y-007 — WCAG 2.1.2, 2.4.3
**Automation:** Playwright

### Steps

1. Focus the "New transfer" button and open the modal.
2. Press Escape.

### Expected Result

The modal closes and focus is back on "New transfer". Returning focus is the half
most implementations skip: without it focus falls back to the document and a
keyboard user is returned to the top of the page, having lost their place.

---

## A11Y-TC-008 — Sign-in is completable with the keyboard alone

**Priority:** P0
**Requirement:** A11Y-009 — WCAG 2.1.1
**Automation:** Playwright

### Steps

1. Complete both the credential and MFA steps using only Tab, typing, and Enter.

### Expected Result

The session reaches the overview. No pointer event is used at any point.

---

## A11Y-TC-009 — Screen reader announces a completed transfer coherently

**Priority:** P1
**Requirement:** A11Y-008 — WCAG 4.1.3
**Automation:** **Manual only**

### Steps

1. With NVDA or VoiceOver running, complete a transfer.

### Expected Result

The outcome is announced, and the announcement is understandable on its own — a
customer who cannot see the screen learns whether their money moved. An automated
check can confirm a live region exists; it cannot confirm the sentence makes
sense.

---

## A11Y-TC-010 — Reading order matches visual order

**Priority:** P2
**Requirement:** A11Y-004 — WCAG 1.3.2
**Automation:** **Manual only**

### Expected Result

Tabbing and screen reader traversal follow the order the page appears to have. A
scanner reports DOM order; whether that order is sensible is a judgement.

---

## A11Y-TC-011 — The application is usable at 200% zoom

**Priority:** P2
**Requirement:** A11Y-002 — WCAG 1.4.4
**Automation:** **Manual only**

### Steps

1. Set browser zoom to 200% and complete a transfer.

### Expected Result

No content is lost or clipped, and no horizontal scrolling is needed to read
text. The layout collapses the sidebar below 820px, which is the behaviour to
check at zoom.
