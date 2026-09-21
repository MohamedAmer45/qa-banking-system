# BUG-UI-002 — Back-office sidebar is not role-filtered

| Field | Value |
|---|---|
| Module | Back office / navigation |
| Severity | Low |
| Priority | Medium |
| Status | **Open — confirmed on the current build** |
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

Deliberately left open: a real finding, carried through the defect workflow
rather than quietly patched.
