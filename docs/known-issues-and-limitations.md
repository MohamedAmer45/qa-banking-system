<!-- NOVABANK-KNOWN-GAPS-SYNC-START -->

## Current Build Gaps and Limitations

| ID | Area | Limitation | QA Status |
|---|---|---|---|
| GAP-001 | Authentication | Real credential login unavailable | Blocked |
| GAP-002 | Authentication | MFA unavailable | Blocked |
| GAP-003 | Beneficiaries | Beneficiary module unavailable | Blocked |
| GAP-004 | Accounts | Account creation unavailable | Blocked |
| GAP-005 | Accounts | Extended account controls unavailable | Blocked |
| GAP-006 | Statements | Dedicated statements unavailable | Blocked |
| GAP-007 | Database | Persistent PostgreSQL banking state unavailable | Blocked |
| GAP-008 | SQL | End-to-end persisted-data SQL validation unavailable | Blocked |

### QA Handling

These gaps do not invalidate their associated requirements.

Existing scenarios, cases, and automation must remain documented.

Execution status should be `Blocked` until functionality is restored.

Tests must not be changed to treat missing product functionality as successful behavior.

<!-- NOVABANK-KNOWN-GAPS-SYNC-END -->