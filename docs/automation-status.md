# NovaBank Automation Status

Last synchronized: 2026-09-14

| Framework / Area | Current Status |
|---|---|
| Selenium Java | Migrated to current UI |
| Cypress TypeScript | Requires current-build synchronization |
| Playwright TypeScript | Requires current-build synchronization |
| Postman | Requires session/API synchronization |
| REST Assured | Requires session/API synchronization |
| Jest | Review against rebuilt backend |
| Cucumber | Review scenarios against current build |
| JMeter | Update authentication/session requests where applicable |
| GitHub Actions | Review after framework migrations |
| Jenkins | Review after framework migrations |

## Selenium Current Coverage

Automated:

- Authentication/session
- Dashboard
- Accounts
- Transactions
- Transfers
- Bills
- Cards
- Loans
- Notifications
- Profile
- Admin
- Authorization
- Session security

Blocked:

- MFA
- Beneficiaries
- Account creation
- Extended account controls
- Dedicated statements