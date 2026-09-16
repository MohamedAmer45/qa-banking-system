@authorization
Feature: Role-based authorization
  As NovaBank
  I want customer and administrator capabilities separated
  So that protected administration functions are exposed only to administrators

  @smoke @customer
  Scenario: Customer cannot access Admin navigation
    Given a customer session is active for authorization testing
    Then the Admin navigation should not be visible
    And the Admin navigation should have the hidden CSS class

  @smoke @admin
  Scenario: Administrator can access Admin navigation
    Given an administrator session is active for authorization testing
    Then the Admin navigation should be visible
    And the Admin navigation should not have the hidden CSS class

  @regression
  Scenario: Role information changes with the authenticated session
    Given a customer session is active for authorization testing
    Then the authenticated session should identify the "customer" role
    When the customer logs out and starts an administrator session
    Then the authenticated session should identify the "admin" role
    And the Admin navigation should be visible
