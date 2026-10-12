Feature: Add product to bag

  Scenario: Add a moisturizer to the bag
    Given I open the Ulta homepage
    When I search for "moisturizer"
    And I open the first product
    And I select an available product variant if required
    And I add the product to the bag
    Then the product should be added successfully

  Scenario: Add three products and verify the cart
    Given the user has added 3 products to the bag
    When the user navigates to the cart
    Then the cart displays at least 3 items
    And each added product is listed with the correct product name

  Scenario Outline: Recommendations are relevant to the viewed product
    Given I open the Ulta homepage
    When I search for "<search_term>"
    And I open the first product
    Then at least 30 percent of recommendations are relevant to the viewed product

    Examples:
      | search_term     |
      | moisturizer     |
      | mascara         |
      | vitamin c serum |