# Cypress Suite

Front-end and contract coverage for NovaBank. Deliberately **not** a second
copy of the Playwright suite: the two divide the work.

| | Playwright | Cypress |
|---|---|---|
| End-to-end money movement | Yes | No |
| Cross-browser | Chromium, Firefox, WebKit | Chrome/Electron |
| Form validation | Minimal | Primary |
| Network contract assertions | Minimal | Primary |
| Uncaught page exceptions | Opt-in guard | Fails by default |

That last row is not theoretical. Cypress found `BUG-UI-001`, a `TypeError`
thrown on every page load, on its first `cy.visit`, while Playwright had been
driving the same screens throughout the retarget without noticing.

## Running

```bash
npm ci
CYPRESS_BASE_URL=http://localhost:3000 npm run validate
```

Start the application first, from the
[`novabank-banking-system`](https://github.com/MohamedAmer45/novabank-banking-system)
repository:

```bash
npm run db:reset && npm start
```

## Custom commands

| Command | Purpose |
|---|---|
| `cy.byTestId(id)` | Resolve by the application's `data-testid` |
| `cy.login(user?)` | Full credential + MFA handshake through the UI |
| `cy.apiLogin(user?)` | Obtain a session token over HTTP, no UI |
| `cy.openView(name)` | Navigate via the sidebar, dismissing any open modal |
| `cy.waitForView()` | Wait for a view render to settle |

`cy.apiLogin` exists so contract tests do not pay for a browser sign-in they
do not need.

## Conventions

**Sign-in is two steps.** Credentials return an MFA challenge; the challenge is
exchanged for a session. `cy.login` asserts on both responses.

**Waiting.** The application updates the page heading before its view data
arrives. `cy.waitForView()` waits for the placeholder to clear. There are no
fixed waits anywhere in this suite — every `cy.wait` is on a network alias.

**Money assertions use minor units,** read from `data-balance-minor`.

**Modals.** Transfer, beneficiary and account-opening forms are modals.
`cy.openView` dismisses an open one first, since a rejected submission leaves
its modal up where it swallows the next navigation click.
