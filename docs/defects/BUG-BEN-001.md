# BUG-BEN-001 � Deleted beneficiary remains after reload

## Summary

Deleting a beneficiary appears to succeed in the UI and the API returns HTTP 200,
but the beneficiary remains available after the application is fully reloaded.

## Module

Beneficiaries

## Severity

High

## Priority

High

## Status

Open

## Environment

- Application: NovaBank QA Lab
- Environment: Production QA deployment
- Browser: Google Chrome
- Automation: Selenium / Java / TestNG

## Preconditions

1. Customer is authenticated.
2. MFA verification is completed.
3. Customer has access to the Beneficiaries module.
4. A beneficiary has been created.
5. The beneficiary has been verified successfully.

## Steps to Reproduce

1. Sign in as the seeded customer.
2. Complete MFA verification.
3. Navigate to Beneficiaries.
4. Create a new beneficiary.
5. Verify the beneficiary using OTP 123456.
6. Click Delete.
7. Confirm the deletion dialog.
8. Observe the "Beneficiary deleted" success notification.
9. Reload the application.
10. Navigate back to Beneficiaries.

## Expected Result

The deleted beneficiary should no longer exist in the beneficiary list.

The API response returned by GET /api/beneficiaries after deletion should not
contain the deleted beneficiary.

## Actual Result

The application reports successful deletion and:

DELETE /api/beneficiaries/{id}

returns HTTP 200.

However, after a complete application refresh and a fresh:

GET /api/beneficiaries

the beneficiary is still displayed.

## Observed API Sequence

POST /api/beneficiaries
HTTP 201

POST /api/beneficiaries/{id}/verify
HTTP 200

DELETE /api/beneficiaries/{id}
HTTP 200

GET /api/beneficiaries
HTTP 200

Application refresh

GET /api/beneficiaries
HTTP 200

Deleted beneficiary is still present.

## Impact

A customer may believe a transfer beneficiary has been removed when it actually
remains associated with the account.

For a banking application, this can create both functional and security concerns,
because a supposedly removed transfer destination may remain usable.

## Automation Evidence

Detected by:

BeneficiaryLifecycleTest.customerShouldCreateVerifyAndDeleteBeneficiary

Final assertion:

Deleted beneficiary should not exist after a fresh application load.

Expected: false
Actual: true

## Test Data Impact

The affected automated lifecycle test must not run repeatedly while this defect
is open because each execution creates another beneficiary that cannot be
reliably deleted.

## Resolution Requirement

The delete operation must persist the beneficiary removal or disablement in the
backend data store.

After deletion:

1. DELETE /api/beneficiaries/{id} should complete successfully.
2. A subsequent GET /api/beneficiaries must not return the deleted beneficiary.
3. Reloading the application must not restore the beneficiary.
4. The lifecycle Selenium test should then be re-enabled.
