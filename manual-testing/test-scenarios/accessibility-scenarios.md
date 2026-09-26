# Banking System — Accessibility Scenarios

## 1. Document Information

| Field    | Value                          |
| -------- | ------------------------------ |
| Project  | Banking System Testing Project |
| Module   | Accessibility (`A11Y`)         |
| Document | Test Scenarios                 |
| Version  | 1.0                            |
| Status   | Execution Ready                |
| Owner    | QA Engineering                 |

---

## 2. Scope

`A11Y-001` to `A11Y-009` in `requirements/requirements-catalog.md`, each mapped
to a WCAG 2.1 AA success criterion.

Accessibility here is treated as a functional obligation, not a preference. This
is a consumer banking interface: a customer who cannot use a pointer must still
be able to move money.

---

## 3. What a machine can decide, and what it cannot

This split shapes every scenario below, and it is the reason the module was
written with nine requirements rather than one.

**Decidable by a scanner.** Whether a control exposes an accessible name, whether
contrast meets a ratio, whether ARIA attributes are valid, whether ids are
unique. A rule either passes on an element or it does not. `A11Y-001` to
`A11Y-006`.

**Not decidable by a scanner.** Whether focus goes where it should, whether a
trap can be escaped, whether an announcement makes sense. A scanner can confirm a
dialog carries `role="dialog"`; it cannot confirm that opening one moves focus
into it. `A11Y-007` to `A11Y-009`.

A green scan therefore means the machine-checkable part is clean. It is a floor,
not a statement that the application is accessible — automated checks find only a
minority of WCAG issues, and none of the judgement-based ones.

---

## 4. Scenarios

### A11Y-S-01 — Every form control is named

**Requirement:** A11Y-001, A11Y-003

Every input, select and textarea a customer meets exposes a name to assistive
technology, including controls presented only as an icon and filter controls with
no visible label.

Checks: a scan of each view reports no `label`, `select-name` or `button-name`
violation; an open modal is scanned too, since its markup does not exist until it
is opened.

---

### A11Y-S-02 — Text meets the contrast minimum

**Requirement:** A11Y-002

Text and interactive controls are legible against their background at the AA
ratio.

Checks: no `color-contrast` violation on any view. The application uses a dark
theme with a lot of muted grey, which is where this fails first.

---

### A11Y-S-03 — Structure and ARIA are valid

**Requirement:** A11Y-004, A11Y-005, A11Y-006

Headings and landmarks describe the page, ARIA is applied to elements that permit
it, and ids used for labelling are unique.

Checks: no `heading-order`, `landmark-*`, `aria-*` or `duplicate-id` violation.

---

### A11Y-S-04 — A dialog behaves like one

**Requirement:** A11Y-007

Opening a modal announces a dialog, moves focus into it, keeps focus inside while
it is open, closes on Escape, and returns focus to the control that opened it.

Checks: `role="dialog"`, `aria-modal`, and a non-empty accessible name; focus is
inside the dialog immediately after opening; focus is still inside after tabbing
well past the number of controls it contains; Escape closes it; the trigger has
focus afterwards.

Tabbing once proves nothing — the next control is inside the dialog either way.
The check has to tab far enough to have escaped an untrapped dialog several times
over.

---

### A11Y-S-05 — Status messages are announced

**Requirement:** A11Y-008

Transfer outcomes and error toasts reach assistive technology without moving
focus.

Checks: the toast container is a live region *before* a message arrives — a
region created at the same moment as its content is not reliably announced — and
raising a message does not change the focused element.

---

### A11Y-S-06 — Everything works without a pointer

**Requirement:** A11Y-009

Any operation a customer can complete by clicking can be completed from the
keyboard.

Checks: sign-in is completed with Tab, typing and Enter only, through both the
credential and MFA steps; every sidebar destination is a real button or link
rather than a clickable `div`; scrolling regions are keyboard reachable.

---

## 5. Out of scope here

| Concern | Why |
|---|---|
| Screen reader output quality | Requires a person with a screen reader. Coherence of an announcement is a judgement, not an assertion |
| Reading order | A scanner reports DOM order, not whether it makes sense |
| Alternative text quality | `alt` presence is checkable; usefulness is not |
| Cognitive load, plain language | Outside what this project can assert |

These are named rather than omitted so the coverage claim stays honest: the
module covers the machine-checkable surface and the interaction behaviour around
it, and nothing beyond that.
