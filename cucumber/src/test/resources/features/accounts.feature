@accounts @regression
Feature: Viewing accounts
  A customer sees every account they hold, with an accurate balance.

  Traces to: ACC-001, ACC-003

  Background:
    Given a signed-in "customer"

  @smoke @ACC-001
  Scenario: The customer's accounts are listed
    When the accounts view is opened
    Then at least 3 accounts are listed
