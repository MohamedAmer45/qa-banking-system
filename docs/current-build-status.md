# NovaBank Current Build Status

Last synchronized: 2026-09-14

## Live Environment

Production URL:

`https://novabank-qa-proxy.onrender.com`

The current application is a deterministic QA banking sandbox designed for repeatable manual, UI automation, API, validation, authorization, negative, boundary, and regression testing.

## Authentication Model

The current deployed build uses role-based demo sessions instead of real banking credentials.

Available UI sessions:

- Enter as customer
- Enter as admin
- Log out

Browser authentication state is stored in `sessionStorage`.

Current API demo tokens:

- `demo-customer`
- `demo-admin`

No real banking credentials are used.

## Current Functional Status

| Module | Status |
|---|---|
| Customer session | Available |
| Admin session | Available |
| Dashboard | Available |
| Accounts | Available |
| Transfers | Available |
| Transactions | Available |
| Bills | Available |
| Cards | Available |
| Loans | Available |
| Notifications | Available |
| Profile | Available |
| Admin console | Available |
| Logout | Available |
| MFA | Blocked |
| Beneficiaries | Blocked |
| Account creation | Blocked |
| Extended account controls | Blocked |
| Dedicated statements | Blocked |
| Persistent database | Not currently implemented |

## Persistence

Current UI state uses:

- `sessionStorage` for session information
- `localStorage` for deterministic banking state
- Serverless API for application requests

The current build does not use persistent PostgreSQL banking storage.

## QA Treatment of Missing Features

Requirements and tests for unavailable functionality are intentionally preserved.

They must be marked `Blocked` rather than deleted or rewritten to treat the missing implementation as expected behavior.

Current blocked areas include:

- MFA
- Beneficiary management
- Account creation
- Extended account controls
- Dedicated statements
- Persistent database behavior
- SQL persistence verification