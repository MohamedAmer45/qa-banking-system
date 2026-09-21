@authentication @regression
Feature: Signing in to NovaBank
  Access to banking requires both a password and a one-time code. Neither is
  sufficient on its own, and a session exists only once both are accepted.

  Traces to: AUTH-001 through AUTH-008

  Background:
    Given the NovaBank sign-in page is open

  @smoke @AUTH-001 @AUTH-003
  Scenario: A password alone does not grant access
    When the "customer" submits valid credentials
    Then a one-time code is requested
    And no session is established

  @smoke @AUTH-003
  Scenario: Completing both steps grants access
    When the "customer" submits valid credentials
    And the correct one-time code is submitted
    Then the session is established
    And the signed-in role is "CUSTOMER"

  @AUTH-002 @negative
  Scenario: An incorrect password is refused
    When the "customer" submits an incorrect password
    Then no session is established
    And the failure is reported as "invalid"

  @AUTH-004 @negative
  Scenario: An incorrect one-time code is refused
    When the "customer" submits valid credentials
    And the one-time code "000000" is submitted
    Then no session is established
    And the failure is reported as "invalid"

  @AUTH-008
  Scenario: Signing out ends the session
    When the "customer" submits valid credentials
    And the correct one-time code is submitted
    And the customer signs out
    Then the sign-in page is shown again
    And no session is established

  @AUTH-001
  Scenario Outline: Each role signs in under its own identity
    When the "<role>" submits valid credentials
    And the correct one-time code is submitted
    Then the signed-in role is "<expected>"

    Examples:
      | role          | expected |
      | customer      | CUSTOMER |
      | administrator | ADMIN    |
      | manager       | MANAGER  |
      | support agent | SUPPORT  |
      | auditor       | AUDITOR  |
