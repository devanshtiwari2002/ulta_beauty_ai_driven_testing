Feature: Ulta product search

	Scenario Outline: Search for beauty products
		Given I open the Ulta homepage
		When I search for "<search_term>"
		Then search results should be displayed
		And at least one product should be present in the results

		Examples:
			| search_term     |
			| moisturizer     |
			| mascara         |
			| vitamin c serum |
