@admin
Feature: Admin console
  As a NovaBank administrator
  I want to review operational banking metrics
  So that I can monitor the current deterministic QA data

  Background:
    Given an authenticated administrator opens the Admin console

  @smoke
  Scenario: Administrator views the admin console
    Then the Admin console page should be displayed
    And at least three admin summary metrics should be displayed

  @regression
  Scenario: Admin summary contains complete banking metrics
    Then every admin summary metric should contain a value
    And all current admin banking metrics should be available

  @regression
  Scenario: Admin summary matches deterministic seeded data
    Then the admin metrics should match the current seeded QA data
