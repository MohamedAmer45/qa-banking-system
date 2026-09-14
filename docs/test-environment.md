<!-- NOVABANK-ENVIRONMENT-SYNC-START -->

## Current Test Environment

Production QA URL:

`https://novabank-banking-system.vercel.app`

### Deployment Characteristics

- Vercel-hosted application
- Single serverless API handler
- Deterministic QA state
- Browser-local banking state
- Demo role authentication
- JSON API responses
- No persistent PostgreSQL banking state in the current build

### Browser Storage

Authentication:

`sessionStorage`

Business state:

`localStorage`

### Automation Targets

Where configured:

- Chrome
- Firefox
- Edge
- Headless execution

### Database Limitation

The current environment does not provide persistent relational-database banking records.

Database and SQL testing remain part of the project but require a future/restored persistent DB-backed test layer.

<!-- NOVABANK-ENVIRONMENT-SYNC-END -->