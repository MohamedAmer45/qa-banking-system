@ui @dashboard
Feature: Customer dashboard overview

  As a NovaBank customer
  I want to view my financial dashboard
  So that I can understand my balances and recent activity

  Background:
    Given a customer is authenticated in NovaBank

  @smoke
  Scenario: Customer views the dashboard summary
    Then the dashboard heading should be "Dashboard"
    And the total balance should display a currency value
    And the dashboard should contain these metrics:
      | Checking     |
      | Savings      |
      | Loan balance |
    And every dashboard metric should display a currency value

  @regression @transactions
  Scenario: Customer views recent account activity
    Then the recent transactions table should be displayed
    And at least one recent transaction should be listed
    And every recent transaction should contain complete currency data