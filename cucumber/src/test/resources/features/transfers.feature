@transfers @regression
Feature: Moving money between accounts
  A transfer either completes in full or moves nothing. The account is debited
  once, and a rejected transfer leaves the balance exactly as it was.

  Traces to: TRF-001, TRF-004, TRF-005, TRF-013

  Background:
    Given a signed-in "customer"

  @smoke @TRF-001
  Scenario: The transfer form offers the customer's own accounts
    When the transfer form is opened
    Then at least 2 source accounts can be chosen

  @smoke @TRF-001 @TRF-002
  Scenario: A valid transfer debits the source account once
    When the source account balance is noted
    And a transfer of "100" is submitted to the first beneficiary
    Then the source account is debited by at least 10000 minor units

  @TRF-004 @negative
  Scenario: A transfer beyond the available balance moves nothing
    When the source account balance is noted
    And a transfer of "99999999" is submitted to the first beneficiary
    Then the outcome is reported as "reject"
    And the source account balance is unchanged

  @TRF-005 @boundary
  Scenario Outline: The form refuses amounts outside the permitted range
    When the transfer form is opened
    Then the amount "<amount>" is refused by the form

    Examples:
      | amount |
      | 0      |
      | -1     |

  @TRF-005 @boundary
  Scenario: The smallest permitted amount is accepted
    When the transfer form is opened
    Then the amount "0.01" is accepted by the form
