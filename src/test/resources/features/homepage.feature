Feature: Ulta homepage

  Scenario: Validate homepage essentials
    Given I open the Ulta homepage
    Then the homepage should load successfully
    And the search bar should be visible and enabled
    And the account icon should be accessible