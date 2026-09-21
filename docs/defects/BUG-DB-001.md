# BUG-DB-001 — FX and interest rates stored as binary floating point

| Field | Value |
|---|---|
| Module | Database / financial precision |
| Requirements | DB-006, DB-007 |
| Severity | Medium |
| Priority | High |
| Status | **Closed — fixed** |
| Found by | Database suite, 2026-09-21 |
| Fixed | 2026-09-21 |

## Summary

`transfers.fx_rate` and `loans.interest_rate` were declared `REAL`. Both record
the rate actually applied to a money movement, and single-precision binary
floating point cannot represent most decimal rates exactly.

## Evidence

Run against the live database:

```sql
SELECT (1000000 * 52.4::real)::bigint    AS with_real,
       (1000000 * 52.4::numeric)::bigint AS with_numeric;
```

```
 with_real | with_numeric
-----------+--------------
  52400002 |     52400000
```

Converting 10,000.00 at a rate of 52.4 drifts by **2 minor units** from the
recorded rate alone.

## Impact

The application computes FX in JavaScript from its own rate table, so live
conversions were correct. The defect is in what was *recorded*: a statement or
audit recomputed from `fx_rate` would not reconcile with
`credited_amount_minor`.

`DB-006` requires appropriate decimal precision and `DB-007` requires that
financial calculations not rely on floating-point storage. A rate that feeds a
money calculation falls under both.

## Fix

```
fx_rate        REAL -> NUMERIC(18,8)
interest_rate  REAL -> NUMERIC(6,3)
```

No floating-point columns remain. The `pg` NUMERIC type parser was already
registered, so values still arrive as JavaScript numbers and no application
code changed.

## Why the other suites could not find this

Playwright, Cypress, Selenium and Cucumber all observe the application through
its own API, which returns a rounded figure that looks correct. The error is in
the storage type, visible only by reading `information_schema` or by performing
arithmetic in the database.

This is the first defect found by inspecting the schema rather than the
behaviour, and it is the argument for the database layer existing at all.

## Regression check

`ConstraintTest.moneyIsNeverFloatingPoint` and
`ConstraintTest.ratesUseExactNumerics` fail if any money-adjacent column
returns to a floating-point type.
