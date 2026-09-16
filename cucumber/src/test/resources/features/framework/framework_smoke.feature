@ui @framework @smoke
Feature: Cucumber Selenium framework health

  As a QA engineer
  I want the BDD framework to open NovaBank
  So that business scenarios can be automated reliably

  Scenario: NovaBank entry page exposes both demo roles
    Given the NovaBank entry page is open
    Then the customer demo option should be available
    And the admin demo option should be available