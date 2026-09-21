# BUG-UI-001 — TypeError thrown on every page load

| Field | Value |
|---|---|
| Module | Accounts / application bootstrap |
| Severity | Medium |
| Priority | High |
| Status | **Closed — fixed** |
| Found by | Cypress suite, 2026-09-21 |
| Fixed | 2026-09-21 |

## Summary

Every page load, including the unauthenticated sign-in screen, threw an
uncaught `TypeError` 0 ms after `app.js` evaluated.

```
Uncaught TypeError: Cannot set properties of null (setting 'onsubmit')
    at app.js:99
```

## Cause

`openAccountModal` was written as a single statement followed by a second one:

```js
window.openAccountModal=()=>openModal('Open a new account', `…`);
setTimeout(()=>document.querySelector('#account-form').onsubmit=…, 0);
```

The semicolon terminates the assignment, so the `setTimeout` was never part of
the handler. It ran once at module load, when no modal — and therefore no
`#account-form` — existed. `querySelector` returned `null` and the property
assignment threw.

## Impact

The error was uncaught and asynchronous, so the browser continued and the
application looked healthy. Nothing user-facing broke. It did, however, mean
every session began with a JavaScript exception in the console, and any tooling
that treats uncaught page errors as failures would reject every page.

## Why it took a second framework to find

Playwright had been driving the same screens for the whole retarget without
reporting anything: it does not fail a test on an uncaught page exception
unless the suite explicitly listens for `pageerror`.

Cypress fails the current test on any uncaught exception originating from the
application. The first `cy.visit("/")` surfaced it immediately.

This is the clearest argument in the project for keeping overlapping UI
frameworks with genuinely different defaults, rather than treating the second
one as redundant coverage.

## Fix

The body is braced so the wiring runs when the modal opens. The other fourteen
deferred handler assignments were already inside braced bodies and were
correct, but all fifteen now return early when their form is absent, since a
modal can be dismissed before its timeout fires.

## Regression check

`cypress/e2e/smoke.cy.ts` fails if the exception returns. Consider adding a
`pageerror` listener to the Playwright fixtures so both suites catch this class
of defect.
