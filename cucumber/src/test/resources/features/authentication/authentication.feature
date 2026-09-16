@ui @authentication
Feature: Role-based demo authentication

  As a NovaBank user
  I want to enter a demo session for my assigned role
  So that I receive access to the appropriate banking features

  Background:
    Given the NovaBank entry page is open

  @smoke
  Scenario Outline: User starts a role-based demo session
    When the user starts the "<role>" demo session
    Then the NovaBank application should open for the "<role>" role
    And the admin navigation should be "<admin_navigation>"

    Examples:
      | role     | admin_navigation |
      | customer | hidden           |
      | admin    | visible          |

  @regression @logout
  Scenario: Customer logs out of the demo session
    When the user starts the "customer" demo session
    And the user logs out
    Then the NovaBank entry page should be displayed again