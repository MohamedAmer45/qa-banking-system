@ui @bills
Feature: Customer bill payments

  As a NovaBank customer
  I want bill payments to follow financial rules
  So that payments are processed safely and accurately

  Background:
    Given a customer is authenticated in NovaBank

  @smoke
  Scenario: Customer views available billers
    When the customer opens the bills workspace
    Then the bills heading should be "Bills"
    And these billers should be available:
      | Electricity |
      | Water       |
      | Internet    |
      | Mobile      |

  @regression
  Scenario: Customer completes a valid bill payment
    When the customer opens the bills workspace
    And the customer pays "50" to the "Electricity" biller
    Then the bill-payment notification should be "Bill paid."

  @boundary
  Scenario: Minimum valid bill amount is accepted
    When the customer opens the bills workspace
    And the customer pays "0.01" to the "Mobile" biller
    Then the bill-payment notification should be "Bill paid."

  @validation
  Scenario Outline: Non-positive bill amounts are rejected
    When the customer opens the bills workspace
    And the customer bypasses browser bill minimum validation and pays "<amount>" to "<biller>"
    Then the bill-payment notification should be "Invalid bill amount."

    Examples:
      | amount | biller      |
      | 0      | Electricity |
      | -10    | Water       |

  @validation @insufficient-funds
  Scenario: Bill payment above the available balance is rejected
    When the customer opens the bills workspace
    And the customer pays "999999" to the "Internet" biller
    Then the bill-payment notification should be "Invalid bill amount."