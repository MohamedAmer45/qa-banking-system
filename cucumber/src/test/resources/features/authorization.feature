@authorization @security @regression
Feature: Role boundaries
  Permissions are enforced by the server. A role that may not perform an action
  never obtains the data behind it, whatever the interface offers.

  Traces to: ADMIN-001, ADMIN-002, SEC-003, AUDIT-001

  @smoke @ADMIN-001
  Scenario: An administrator reaches the customer directory
    Given a signed-in "administrator"
    When the customer directory is opened
    Then the directory lists "customer@novabank.test"

  @AUDIT-001
  Scenario: Signing in is recorded in the audit trail
    Given a signed-in "administrator"
    When the audit trail is opened
    Then the audit trail records a sign-in

  @SEC-003
  Scenario: A customer is not offered the back office
    Given a signed-in "customer"
    Then banking navigation is available
    And back-office navigation is not available

  # The sidebar is not role-filtered, so a read-only role is offered user
  # administration it cannot use — recorded as BUG-UI-002. What matters, and
  # what this asserts, is that the module yields no user data.
  @SEC-003 @BUG-UI-002
  Scenario: A read-only role cannot obtain user administration data
    Given a signed-in "support agent"
    When user administration is opened
    Then no user data is shown
