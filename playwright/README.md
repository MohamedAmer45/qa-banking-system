# Playwright Suite

End-to-end coverage of NovaBank, driven through the `data-testid` attributes
the application exposes for automation.

## Running

```bash
npm ci
npx playwright install --with-deps
npm test
```

That runs against the deployed application at `https://novabank-banking-system.vercel.app` — no local server, database
or seed needed.

### Running against localhost instead

The deployed environment has one shared database that is never reset, so
balance assertions here are relative rather than absolute (`LIM-005`). For an
isolated run, start the application from
[`novabank-banking-system`](https://github.com/MohamedAmer45/novabank-banking-system):

```bash
npm run db:reset && npm start
```

then point the suite at it:

```bash
BASE_URL=http://localhost:3000 npm test
```

## Layout

```
fixtures/testFixtures.ts   Page-object fixtures + an authenticated customerSession
pages/BasePage.ts          testId resolution, navigation, modal and render waits
pages/AuthPage.ts          Credential + MFA handshake
pages/*.ts                 One page object per module
test-data/credentials.ts   The eight seeded identities
tests/                     Specs grouped by module
```

## Conventions

**Selectors.** Everything resolves through `getByTestId`. No CSS class or
visible-text selectors — those broke the previous generation of this suite when
the application changed.

**Sign-in is two steps.** Credentials return an MFA challenge; the challenge is
exchanged for a session. `AuthPage.loginAs()` performs both. There is no
single-step login.

**Waiting for renders.** `navigate()` in the application sets the page heading
before its data arrives, then fills the view. `BasePage.waitForViewReady()`
waits for the placeholder to clear. Without it, a navigation issued straight
after sign-in races the initial render and the late-arriving view wins.

**Money assertions use minor units.** `account-balance` carries
`data-balance-minor`, so tests read the integer the ledger holds rather than
parsing a localised currency string.

**Money tests are serial and relative.** `tests/transfers` runs in serial mode
and asserts deltas against a balance read at the start of each test. Absolute
balances are only safe against a freshly seeded database, which CI provides but
a shared environment does not.

**Modals.** The transfer, beneficiary and account-opening forms are modals. The
page object opens them explicitly (`openForm()`), and `openView()` dismisses any
open modal first, since a rejected submission leaves its modal up and it would
otherwise swallow the next navigation click.
