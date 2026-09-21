# BUG-API-001 — Oversized requests dropped the connection instead of returning 413

| Field | Value |
|---|---|
| Module | API / request handling |
| Requirements | SYS-002, SYS-008 |
| Severity | Low |
| Priority | Medium |
| Status | **Closed — fixed** |
| Found by | REST Assured suite, 2026-09-22 |
| Fixed | 2026-09-22 |

## Summary

`docs/api/api-reference.md` documents `413` for a request body over 1 MB. The
application instead called `req.destroy()` the moment the limit was passed,
tearing the socket down before the error handler could write anything. The
client received a connection reset.

## Cause

```js
if (data.length > MAX_BODY_BYTES) {
  reject(Object.assign(new Error('Request body too large.'), { status: 413 }));
  req.destroy();          // ← socket gone before the response is written
}
```

## Why one client saw it and another did not

`curl` reported `HTTP 413` even before the fix, because it tolerates a server
responding mid-upload. REST Assured, on Apache HttpClient, reported
`Connection reset by peer`.

That divergence is the finding, not an inconvenience. A contract that only one
client agrees with is not a contract, and manual verification with a single
tool would have confirmed the documented behaviour incorrectly.

## Fix

A refused request is now drained rather than cut off, so the `413` reaches the
client after the upload finishes. Draining is capped at ten times the limit,
past which the connection is closed, so a client that ignores the refusal
cannot consume memory indefinitely.

## Regression check

`SystemBehaviourTest.oversizedBodyIsRefused` asserts the documented status
rather than accepting any failure, so a return to dropping the connection
fails the suite.
