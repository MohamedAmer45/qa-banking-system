@ui @transactions
Feature: Customer transaction history

  As a NovaBank customer
  I want to review my transaction history
  So that I can verify account activity and transaction records

  Background:
    Given a customer is authenticated in NovaBank

  @smoke
  Scenario: Customer opens transaction history
    When the customer opens transaction history
    Then the transaction-history heading should be "Transactions"
    And at least one transaction should be displayed

  @regression @data-quality
  Scenario: Transaction records contain complete financial data
    When the customer opens transaction history
    Then every transaction should display a date
    And every transaction should display a description
    And every transaction should display a status
    And every transaction should display a currency amount
    And all transaction columns should contain matching record counts

  @regression
  Scenario: Transaction history contains expected activity
    When the customer opens transaction history
    Then transaction history should contain credit and debit activity
    And every seeded transaction should have completed status