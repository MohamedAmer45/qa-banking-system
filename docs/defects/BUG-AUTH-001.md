# BUG-AUTH-001 — Concurrent authenticated requests intermittently return 401

| Field | Value |
|---|---|
| Module | Authentication / session management |
| Severity | Critical |
| Priority | High |
| Status | **Closed — not reproducible** |
| Raised against | Pre-port build (SQLite, single-file server) |
| Re-verified | 2026-09-21 against `novabank-banking-system.vercel.app` |

## Original summary

After a successful login and MFA verification, the Transfers page could not
load reliably: the three authenticated requests it issues in parallel
(`/api/accounts`, `/api/beneficiaries`, `/api/transfers`) intermittently
returned `401` for one or more of the three.

## Re-verification

Twelve concurrent authenticated requests were issued across those same three
endpoints using a single session token.

```
200 200 200 200 200 200 200 200 200 200 200 200
```

Not reproducible.

## Probable original cause

The pre-port build validated sessions against SQLite through a single
synchronous connection. Under concurrent reads that path could return an empty
row rather than blocking, which the handler treated as an invalid session.
Session lookup now runs through a PostgreSQL connection pool, where each
request gets its own connection and a consistent read.

## Closure note

Closed as fixed-by-port rather than deleted, because the failure mode — session
validation degrading under concurrency — is worth keeping as a regression case.
The re-verification above is the regression check.
