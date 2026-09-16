@profile
Feature: Customer profile
  As a NovaBank customer
  I want to review my profile
  So that I can confirm my displayed identity and role information

  Background:
    Given a customer is authenticated in NovaBank
    And the customer opens the Profile page

  @smoke
  Scenario: Customer views populated profile information
    Then the Profile page should be displayed
    And the customer profile identity fields should be populated

  @regression
  Scenario: Customer email has the expected basic format
    Then the displayed profile email should have a valid basic format

  @regression @authorization
  Scenario: Profile displays the correct customer role
    Then the profile role should identify the user as a customer
