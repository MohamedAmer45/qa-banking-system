# BUG-ACC-001 — Open Account form does not submit

| Field | Value |
|---|---|
| Module | Accounts |
| Severity | High |
| Priority | Medium |
| Status | **Closed — obsolete** |
| Raised against | Pre-port build |
| Re-verified | 2026-09-21 |

## Original summary

The Open Account form did not submit; no account was created and no error was
shown.

## Re-verification

The endpoint behind this form is implemented and enforces its precondition:

```
POST /api/accounts        ->  201 with the new account, when KYC is VERIFIED
POST /api/accounts        ->  403 "Verified KYC is required to open an account."
```

Account opening works. The specific UI failure cannot be re-verified, because
the interface it was raised against has been replaced.

## Closure note

Closed as obsolete rather than fixed: the original defect described a UI that
no longer exists. The requirement it covers (ACC — account opening, and its KYC
precondition) is live and needs a fresh execution against the current UI once
the automation retarget lands. Silent submit failure remains a valid thing to
probe on the new form.
