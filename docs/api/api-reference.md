<!-- NOVABANK-API-SYNC-START -->

## Current NovaBank API

Base URL:

`https://novabank-banking-system.vercel.app/api`

### Health

Method:

`GET /health`

Expected service status:

`ok`

### Session

Method:

`POST /session`

Customer body:

    {
      "role": "customer"
    }

Admin body:

    {
      "role": "admin"
    }

Current deterministic tokens:

- `demo-customer`
- `demo-admin`

### Logout

`POST /logout`

### Accounts

`GET /accounts`

Requires authorized demo session.

### Transactions

`GET /transactions`

Requires authorized demo session.

### Transfers

`POST /transfers`

Primary rules:

- amount > 0
- amount <= 10,000

### Bills

`POST /bills/pay`

### Cards

`PATCH /cards`

Current supported states:

- active
- frozen

### Loans

`POST /loans/apply`

Valid amount range:

`1,000 - 50,000`

### Notifications

`GET /notifications`

### Admin Summary

`GET /admin/summary`

Admin role required.

### Authentication Documentation Change

Current API automation must use `/api/session`.

Previous credential-login and MFA API documentation represents the target/legacy banking architecture and remains relevant as blocked intended functionality.

<!-- NOVABANK-API-SYNC-END -->