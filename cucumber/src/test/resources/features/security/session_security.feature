@security
Feature: Session security
  As NovaBank
  I want authenticated session state removed when access ends
  So that logged-out or invalid sessions cannot retain protected access

  Background:
    Given a customer session is active for security testing

  @regression
  Scenario: Logging out clears the active demo session
    Then the active demo session token should exist
    When the customer logs out of the secure session
    Then the active demo session token should be removed
    And the session selection page should be displayed

  @regression
  Scenario: Clearing browser session storage ends authenticated access
    Then the active demo session token should exist
    When the customer clears browser session storage and refreshes the page
    Then the active demo session token should be removed
    And the session selection page should be displayed

  @regression @authorization
  Scenario: Customer session does not expose administrator access
    Then the customer session should not expose Admin navigation
