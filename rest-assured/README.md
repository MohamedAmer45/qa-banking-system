# REST Assured Suite

API regression for NovaBank. 113 tests.

Its job in the stack is the **contract**: response shape, status-code
semantics, the authorization grid, and what must never appear in a response.
The UI suites assert on rendered values and the database suite on stored rows;
neither notices when a field changes type or a body starts carrying a password
hash.

## Running

```bash
mvn clean test
```

That runs against the deployed application at `https://novabank-banking-system.vercel.app` — no local server needed.

### Running against localhost instead

The deployed environment has one shared database that is never reset, so
balance assertions here are relative rather than absolute (`LIM-005`). For an
isolated run, start the application from
[`novabank-banking-system`](https://github.com/MohamedAmer45/novabank-banking-system):

```bash
npm run db:reset && npm start
```

then point the suite at it:

```bash
mvn clean test -Dapi.base.url=http://localhost:3000
```

## Coverage

| Class | Tests | Requirements |
|---|---:|---|
| `AuthorizationMatrixTest` | 63 | SYS-004, SEC-001, SEC-003 |
| `DataExposureTest` | 15 | SYS-003, SYS-005, SEC-002 |
| `SystemBehaviourTest` | 13 | SYS-002, 006, 007, 008, 009, 010 |
| `AuditTrailTest` | 9 | AUDIT-001 to AUDIT-010 |
| `SchemaTest` | 7 | SYS-002, SYS-010, DB-006, ACC-001, TRF-001 |

This closes `SYS` (10) and `AUDIT` (10) — the two modules that had no coverage
anywhere in the project.

## Why a matrix for authorization

`AuthorizationMatrixTest` is a data provider, not 63 hand-written tests. A
permission table is only correct if *every* cell is, and the failure mode worth
catching is an accidental widening — a role gaining access it should not have.
A matrix shows that as one failing cell. Individually written tests show it as
a test nobody thought to write.

The grid covers all six roles against the back-office endpoints, plus the
negative direction: every protected endpoint refuses an anonymous caller and a
malformed token.

## Two conventions the assertions depend on

**Ownership failures return `404`, not `403`.** The API deliberately refuses to
confirm that another customer's resource exists. `DataExposureTest` asserts
`404` for cross-customer reads; asserting `403` would fail against correct
behaviour.

**Money is integer minor units.** `SchemaTest` pins the type in the schema and
again at the value level, because a float would still render correctly in the
UI and still store correctly, while breaking any consumer that assumes minor
units.

## Audit tests prove a known action was recorded

Each one performs an action and then looks for *that* action in the trail —
the transfer it just made, by reference; the sign-in it just performed, by
actor. Asserting only that the trail is non-empty would pass against a log
that records nothing real.

## Schema files

`src/test/resources/schemas/` holds draft-07 schemas used by
`matchesJsonSchemaInClasspath`. They carry `not`/`anyOf` clauses forbidding
credential fields, so a response that starts leaking `password_hash` fails the
contract rather than needing a separate test.

Note that a value under `properties` must itself be a schema object. A bare
string there makes the whole document invalid and the validator refuses to run
at all, which is how the first version of `account.json` failed.

## Relationship to Postman

Postman covers manual exploration, collections and request chaining. This suite
is the automated regression layer: matrices, schemas and assertions that are
awkward to express in a collection and need to run on every push.
