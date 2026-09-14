# NovaBank Test Execution Status

Last synchronized: 2026-09-14

## Executable Current-Build Areas

- Customer session
- Admin session
- Logout
- Dashboard
- Accounts
- Transactions
- Transfers
- Bills
- Cards
- Loans
- Notifications
- Profile
- Admin console
- Authorization
- Session-security behavior

## Blocked Areas

| Area | Reason |
|---|---|
| MFA | Not exposed in current build |
| Beneficiaries | Module not exposed |
| Account creation | Not exposed |
| Extended account controls | Not exposed |
| Dedicated statements | Not exposed |
| SQL persistence verification | No persistent DB-backed banking state |

## Result Classification

Use:

- Passed
- Failed
- Blocked
- Not Run

Do not mark blocked functionality as Passed solely because the feature is absent.