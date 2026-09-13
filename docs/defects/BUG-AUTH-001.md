# BUG-AUTH-001 — Concurrent authenticated requests intermittently return HTTP 401

## Summary

After a successful customer login and MFA verification, the Transfers module
cannot reliably load because multiple authenticated API requests executed in
parallel intermittently return HTTP 401 Unauthorized.

## Module

Authentication / Session Management / Transfers

## Severity

Critical

## Priority

High

## Status

Open

## Environment

- Application: NovaBank QA Lab
- Environment: Production QA deployment
- Browser: Google Chrome
- Automation: Selenium / Java / TestNG

## Preconditions

1. Customer has valid credentials.
2. Customer successfully completes MFA.
3. Customer dashboard loads successfully.
4. A valid authenticated session exists.

## Steps to Reproduce

1. Sign in using a valid customer account.
2. Complete MFA verification.
3. Confirm the authenticated dashboard loads.
4. Navigate to Transfers.
5. Observe the requests triggered by the Transfers renderer.

The frontend performs authenticated requests concurrently:

- GET /api/accounts
- GET /api/beneficiaries
- GET /api/transfers

## Expected Result

All three requests should accept the same valid authenticated session and return
successful responses.

The Transfers page should render normally.

## Actual Result

One or more concurrent authenticated requests intermittently return HTTP 401.

Observed example:

GET /api/accounts
HTTP 200

GET /api/beneficiaries
HTTP 401

GET /api/transfers
HTTP 401

A separate execution produced:

GET /api/accounts
HTTP 401

GET /api/beneficiaries
HTTP 401

GET /api/transfers
HTTP 200

Login and MFA immediately before these requests both returned HTTP 200.

## Impact

The Transfers module cannot reliably load for a successfully authenticated
customer.

The frontend authentication wrapper also treats HTTP 401 as session failure,
which can clear the current session and return the user to authentication.

This can prevent access to financial functionality even though the customer's
session is valid.

## Automation Evidence

Detected by:

TransfersReadOnlyTest

Setup failure:

Transfers page should load successfully.
Expected: true
Actual: false

The functional transfer test cases are skipped because module initialization
fails first.

## Technical Observation

The Transfers frontend loads its dependencies concurrently using a Promise.all
pattern.

The same freshly issued bearer/session token is accepted by some requests while
being rejected by others during the same navigation operation.

This indicates that the failure is not caused by invalid customer credentials or
an expired session.

## Resolution Requirement

A valid authenticated session must support simultaneous API requests.

After successful login and MFA:

1. GET /api/accounts must return 200.
2. GET /api/beneficiaries must return 200.
3. GET /api/transfers must return 200.
4. Parallel requests using the same valid session must not invalidate or race
   with each other.
5. The Transfers module must load consistently.
6. TransfersReadOnlyTest should then be re-enabled.

---

## Additional Affected Module — Cards

The same authentication/session defect also affects the Cards module.

The Cards frontend loads its dependencies concurrently:

- GET /api/cards
- GET /api/accounts

During Selenium execution, the customer successfully authenticated and reached
the dashboard, but GET /api/accounts intermittently returned HTTP 401 while the
Cards module was loading.

Because the frontend treats any authenticated HTTP 401 response as session
failure, the application clears the session and the Cards renderer does not
finish loading.

### Automation Evidence

Detected by:

CardsReadOnlyTest

Setup failure:

Cards page should load successfully.

Expected: true
Actual: false

The five Cards functional tests are skipped because module initialization fails
before they can execute.

### Updated Impact

BUG-AUTH-001 is not limited to Transfers.

Confirmed affected modules currently include:

- Transfers
- Cards

Any module that performs multiple authenticated requests may potentially be
affected until the underlying session handling defect is resolved.

### Updated Resolution Requirement

After successful login and MFA, the same valid authenticated session must
reliably support all API requests, including simultaneous requests from:

- Transfers
- Cards

CardsReadOnlyTest and TransfersReadOnlyTest should be re-enabled after the
session defect is fixed.
