# BUG-UI-002 — Back-office sidebar is not role-filtered

| Field | Value |
|---|---|
| Module | Back office / navigation |
| Severity | Low |
| Priority | Medium |
| Status | **Closed — fixed 2026-09-23** |
| Found by | Selenium suite, 2026-09-21 |

## Summary

Every staff role is shown the full nine-item back-office sidebar, including
"Users & roles", which only `ADMIN` may use. `SUPPORT`, `AUDITOR`, `EMPLOYEE`
and `MANAGER` are offered navigation to modules the server will refuse.

## Cause

`renderShell` picks a nav list by role class, not by permission:

```js
const staff = staffRoles.has(u.role), nav = staff ? adminNav : customerNav;
```

`adminNav` is a flat array with no permission metadata, so every staff role
receives all nine entries.

## Steps to reproduce

1. Sign in as `support@novabank.test` / `Support123!`, MFA `123456`.
2. Observe the sidebar.

## Expected

Navigation offers only modules the signed-in role can use, or the entry is
visibly disabled.

## Actual

All nine back-office entries are offered. Selecting "Users & roles" issues
`GET /api/admin/users`, the server answers `403`, and the view renders an
error.

## This is not an authorization failure

The server enforces the boundary correctly. `ADMIN-005` in the Playwright suite
confirms `SUPPORT` receives `403` from `POST /api/admin/accounts/1/freeze`, and
`RoleAuthorizationTest.readOnlyRoleCannotAdministerUsers` confirms the module
yields an error rather than user data.

`docs/ARCHITECTURE.md` in the application repository states the principle
directly: *"RBAC is enforced server-side; hiding UI controls is not treated as
authorization."* That principle holds. No data leaks.

What is defective is the interface offering actions that always fail. It is a
usability defect, and it also weakens the signal a tester gets from the UI —
a visible control is normally a claim that the action is available.

## Suggested fix

Give `adminNav` a required permission per entry and filter it against the same
`ROLE_PERMISSIONS` table the server uses, so one definition drives both.

## Impact on testing

A test asserting "a read-only role cannot see user administration" will fail
against correct-by-design behaviour. Assert instead that the module yields no
data, which is what `RoleAuthorizationTest` does.

## Fix

As suggested above, and with one definition driving both sides.

`permissionsFor(role)` in `src/lib/access.js` expands the role's entry in
`ROLE_PERMISSIONS` — the same table `requirePermission` checks — and `/api/me`
reports the result. Each `adminNav` entry now carries the permission its own
list endpoint requires, read off the route guards rather than invented for the
client, and the sidebar is filtered against what the server reported.

The two endpoints guarded by `requireAdmin` rather than a permission are marked
`role:ADMIN`, so the nav filter mirrors the guard instead of approximating it.

**The authorization did not change and is not the sidebar.** Every admin route
keeps its own guard, and the principle in `docs/ARCHITECTURE.md` still holds.
What changed is that the interface stopped offering actions that cannot work.

## Verification

Checked across all 45 role/module combinations: every entry a role can see
returns `200`, every entry it cannot see returns `403`.

```text
SUPPORT   6/9 entries   hides fraud, audit, users
AUDITOR   8/9 entries   hides users
EMPLOYEE  6/9 entries   hides fraud, audit, users
MANAGER   8/9 entries   hides users
ADMIN     9/9 entries
```

Regression cover:

- `selenium/.../RoleAuthorizationTest` — a data provider over all five staff
  roles, so a role added to the table becomes a row rather than a forgotten
  case, plus a test that a withheld module still yields nothing when reached
  directly
- `cucumber/.../authorization.feature` — a scenario outline over three roles
- `novabank/tests/access.test.js` — six Jest tests on `permissionsFor`, the
  strongest being that it agrees with `hasPermission` for every role and every
  permission. A disagreement between those two is exactly how this returns.

Both suites now reach user administration through `window.navigate` rather than
the sidebar, since the button they used to click is correctly absent. That is
the better check anyway: it arrives the way a bookmark would, and the server
still refuses it.
