@ui @accounts
Feature: Customer account overview

  As a NovaBank customer
  I want to review my bank accounts
  So that I can see their types, balances, and protected identifiers

  Background:
    Given a customer is authenticated in NovaBank

  @smoke
  Scenario: Customer views available accounts
    When the customer opens the account overview
    Then the accounts heading should be "Accounts"
    And at least one customer account should be displayed
    And the account overview should contain these account types:
      | Checking |
      | Savings  |
    And every account should display a currency balance

  @regression @security
  Scenario: Account identifiers are protected
    When the customer opens the account overview
    Then every account identifier should be masked