@ui @cards
Feature: Customer card management

  As a NovaBank customer
  I want to review and control my payment cards
  So that my card information remains secure

  Background:
    Given a customer is authenticated in NovaBank

  @smoke
  Scenario: Customer views issued cards
    When the customer opens the cards workspace
    Then the cards heading should be "Cards"
    And at least 2 customer cards should be displayed
    And these card types should be displayed:
      | Visa Debit   |
      | Virtual Card |

  @regression @security
  Scenario: Sensitive card information is protected
    When the customer opens the cards workspace
    Then every displayed card number should be masked
    And every card should display a valid status

  @regression @controls
  Scenario: Card control action corresponds to card status
    When the customer opens the cards workspace
    Then the action for card "card_1" should correspond to its status

  @regression @controls
  Scenario: Customer toggles and restores a card
    When the customer opens the cards workspace
    And the customer toggles card "card_1"
    Then the controlled card status should change
    And the card notification should describe the new status
    And the controlled card action should change
    When the customer restores the controlled card
    Then the card should return to its original status