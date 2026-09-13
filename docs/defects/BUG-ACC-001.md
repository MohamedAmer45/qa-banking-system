# BUG-ACC-001 — Open Account form does not submit

## Summary

The customer can open the "Open account" modal and select an account type and currency, but submitting the form does not create a new bank account.

## Module

Accounts

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

1. Customer account exists.
2. Customer can authenticate successfully.
3. MFA verification succeeds.
4. Customer has access to the Accounts module.

## Steps to Reproduce

1. Sign in as a customer.
2. Complete MFA verification.
3. Navigate to Accounts.
4. Click "Open account".
5. Select an account type.
6. Select a currency.
7. Click "Open account" in the modal.

## Expected Result

The application should:

1. Submit the account creation request.
2. Send POST /api/accounts.
3. Create the new account.
4. Close the modal.
5. Display the new account.
6. Display an "Account opened" success message.

## Actual Result

The modal opens correctly, but submitting the form does not create an account.

No POST /api/accounts request is produced.

The account count remains unchanged.

## Technical Investigation

Inspection of the deployed frontend indicates that the account form's `onsubmit` handler is not attached when the modal is opened.

The application creates the account form dynamically, but the JavaScript responsible for attaching the submit handler executes when the form does not yet exist.

As a result, the form appears visually but does not execute the intended application account-creation flow.

## Automation Evidence

Detected by:

`AccountCreationTest.customerShouldOpenNewSavingsAccount`

The automated test correctly fails because the account count does not increase after submission.

## Workaround

No valid UI workaround.

Direct API account creation would bypass the defective UI and therefore is not considered a valid workaround for this UI test.

## Resolution Requirement

The frontend implementation must attach the account form submit handler after the modal/form has been created.

Once the application is fixed and redeployed, re-enable:

`AccountCreationTest.customerShouldOpenNewSavingsAccount`
