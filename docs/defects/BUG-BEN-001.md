# BUG-BEN-001 — Deleted beneficiary is still returned by the API

| Field | Value |
|---|---|
| Module | Beneficiaries |
| Severity | Medium |
| Priority | Medium |
| Status | **Closed — fixed 2026-09-23** |
| Raised against | Pre-port build |
| Re-verified | 2026-09-21 against `novabank-banking-system.vercel.app` |

## Summary

Deleting a beneficiary is a soft delete: the row's status is set to `DELETED`
but it is not removed. `GET /api/beneficiaries` applies no status filter, so
the retired beneficiary is still returned to the client alongside active ones.

## Steps to reproduce

1. Authenticate as `customer@novabank.test` (login, then MFA `123456`).
2. `POST /api/beneficiaries` with any valid body — note the returned `id`.
3. `DELETE /api/beneficiaries/{id}` — returns `200 {"message":"Beneficiary deleted."}`.
4. `GET /api/beneficiaries`.

## Expected

The deleted beneficiary is absent from the list, or the contract explicitly
states that clients must filter on `status`.

## Actual

It is present, with `"status": "DELETED"`.

```json
{ "name": "Defect Probe", "status": "DELETED", ... }
```

Confirmed 2026-09-21. The record is created, deleted, and still returned.

## Assessment

The soft delete itself is correct — historical transfers reference the
beneficiary row, so removing it would break the ledger's referential integrity.
The defect is that the read endpoint does not filter, and the API contract does
not say the client must.

Two acceptable fixes:

1. Filter `status <> 'DELETED'` in `GET /api/beneficiaries`, and expose the
   retired ones through an explicit flag if they are ever needed.
2. Document that the endpoint returns all lifecycle states and that clients
   filter on `status`.

Either resolves it. Fix (1) matches what the endpoint's consumers expect.

## Impact on testing

A beneficiary count assertion after a delete will fail unless it filters on
status. Any test that picks "the first beneficiary" can select a deleted one.

## Fix

Fix (1), as assessed above. `GET /api/beneficiaries` now filters
`status <> 'DELETED'`, and `?includeDeleted=true` returns the full lifecycle
for the cases that need it.

The soft delete is unchanged — the row must survive, because transfers
reference it. What changed is that a client asking for "my beneficiaries" is no
longer handed records it cannot pay.

No payment path changed. A transfer to a non-`ACTIVE` beneficiary was already
refused with 409, and that held throughout the time this defect was open.

## Verification

`rest-assured/.../api/banking/BeneficiaryLifecycleTest.java` — 6 tests:

- a deleted beneficiary disappears from the list
- the list never contains a `DELETED` status, asserted as a property rather
  than against one record
- `?includeDeleted=true` still returns it, with the status intact
- the default list is a strict subset of the included list
- a repeated delete is idempotent and does not resurrect the record
- a deleted beneficiary still cannot be paid — the boundary the filter sits in
  front of

One assumption was wrong and is recorded rather than hidden: a second `DELETE`
returns `200`, not `404`, because the delete handler looks the record up
without a status filter. That is defensible for `DELETE`, which is meant to be
idempotent, so the test asserts the real behaviour and the record staying
retired.
