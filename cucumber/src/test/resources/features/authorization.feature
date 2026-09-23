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

  # The sidebar is filtered on the permissions the server reports, so a
  # read-only role is no longer offered user administration (BUG-UI-002,
  # fixed). Hiding the entry is not the authorization, so the scenario below
  # still reaches the module directly and asserts it yields nothing.
  @SEC-003 @BUG-UI-002
  Scenario: A read-only role cannot obtain user administration data
    Given a signed-in "support agent"
    Then the "admin-users" module is not offered in the sidebar
    When user administration is opened directly
    Then no user data is shown

  @SEC-003 @BUG-UI-002
  Scenario Outline: The sidebar offers a staff role only what it may open
    Given a signed-in "<role>"
    Then the "<offered>" module is offered in the sidebar
    And the "<withheld>" module is not offered in the sidebar

    Examples:
      | role          | offered          | withheld    |
      | support agent | admin-customers  | admin-audit |
      | auditor       | admin-audit      | admin-users |
      | manager       | admin-fraud      | admin-users |
