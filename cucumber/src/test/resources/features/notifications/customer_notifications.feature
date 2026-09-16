@notifications
Feature: Customer notifications
  As a NovaBank customer
  I want to review my notifications
  So that I remain informed about statements and card activity

  Background:
    Given a customer is authenticated in NovaBank
    And the customer opens the Notifications page

  @smoke
  Scenario: Customer views available notifications
    Then the Notifications page should be displayed
    And at least one customer notification should be displayed

  @regression
  Scenario: Notification records contain consistent readable content
    Then every displayed notification should contain readable content
    And the displayed notification count should match the notification list

  @regression
  Scenario Outline: Important notification categories are available
    Then a notification containing "<keyword>" should be displayed

    Examples:
      | keyword   |
      | statement |
      | card      |
