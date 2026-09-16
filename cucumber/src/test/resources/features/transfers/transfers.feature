@ui @transfers
Feature: Customer bank transfers

  As a NovaBank customer
  I want transfers to follow financial business rules
  So that money is moved safely and recorded consistently

  Background:
    Given a customer is authenticated in NovaBank

  @smoke
  Scenario: Customer views transfer options
    When the customer opens the transfers workspace
    Then the transfers heading should be "Transfers"
    And these transfer source accounts should be available:
      | Checking |
      | Savings  |
    And these transfer recipients should be available:
      | Alex Johnson |
      | Sam Lee      |

  @regression @data-integrity
  Scenario: Successful transfer updates the balance and transaction history
    When the customer opens the transfers workspace
    And the current transfer balance and transaction count are recorded
    And the customer sends "100" from Checking to "Alex Johnson"
    Then the transfer notification should be "Transfer completed."
    And the Checking balance should decrease by "100"
    And one transfer transaction should be added

  @boundary
  Scenario Outline: Valid transfer boundaries are accepted
    When the customer opens the transfers workspace
    And the customer sends "<amount>" from Checking to "Sam Lee"
    Then the transfer notification should be "Transfer completed."

    Examples:
      | amount |
      | 0.01   |
      | 10000  |

  @boundary @validation
  Scenario: Amount immediately above the transfer limit is rejected
    When the customer opens the transfers workspace
    And the current transfer balance and transaction count are recorded
    And the customer sends "10000.01" from Checking to "Alex Johnson"
    Then the transfer notification should be "Transfer limit exceeded."
    And the rejected transfer should not change balance or transaction history

  @validation
  Scenario Outline: Non-positive transfer amounts are rejected
    When the customer opens the transfers workspace
    And the current transfer balance and transaction count are recorded
    And the customer bypasses browser minimum validation and sends "<amount>" to "Alex Johnson"
    Then the transfer notification should be "Amount must be positive."
    And the rejected transfer should not change balance or transaction history

    Examples:
      | amount |
      | 0      |
      | -1     |

  @regression @insufficient-funds
  Scenario: Transfer cannot exceed the remaining account balance
    When the customer opens the transfers workspace
    And the customer sends "10000" from Checking to "Alex Johnson"
    Then the transfer notification should be "Transfer completed."
    And the current transfer balance and transaction count are recorded
    When the customer sends "3000" from Checking to "Sam Lee"
    Then the transfer notification should be "Insufficient funds."
    And the rejected transfer should not change balance or transaction history