Feature: Cart recommendations

  Scenario: Validate cart recommendations are relevant to cart items
    Given the user has moisturizer and mascara in the cart
    When the user views cart recommendations
    Then at least 25 percent of recommendations should be relevant to the cart items