@loans
Feature: Customer loan applications
  As a NovaBank customer
  I want to review loan information and apply for a loan
  So that I can choose an amount and repayment term within the supported limits

  Background:
    Given a customer is authenticated in NovaBank
    And the customer opens the Loans page

  @smoke
  Scenario: Customer views existing loan information
    Then the Loans page should be displayed
    And the current personal loan information should be displayed

  @regression
  Scenario: Supported loan terms and amount limits are configured
    Then the supported 12, 24, and 36 month loan terms should be available
    And the loan amount range should be "1000" through "50000"

  @regression
  Scenario: Customer selects a supported loan term
    When the customer selects the "24 months" loan term
    Then the selected loan term should be "24 months"

  @regression @boundary
  Scenario Outline: Boundary loan amounts satisfy validation
    When the customer enters loan amount "<amount>"
    Then the loan amount should be valid

    Examples:
      | amount |
      | 1000   |
      | 50000  |

  @regression @validation
  Scenario Outline: Out-of-range loan amounts fail validation
    When the customer enters loan amount "<amount>"
    Then the loan amount should be invalid

    Examples:
      | amount |
      | 999    |
      | 50001  |

  @regression @boundary
  Scenario Outline: Customer submits a valid loan application
    When the customer applies for a loan of "<amount>" for "<term>"
    Then the loan application should be submitted successfully

    Examples:
      | amount | term      |
      | 5000   | 24 months |
      | 1000   | 12 months |
      | 50000  | 36 months |
