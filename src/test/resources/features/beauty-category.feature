Feature: Beauty category recommendations

  Scenario: Validate beauty category recommendations across products and cart
    Given the user searches for moisturizer and mascara and adds both to the cart
    When all recommendations from the product pages and cart page are collected
    Then at least 20 percent of all collected recommendations must be beauty category