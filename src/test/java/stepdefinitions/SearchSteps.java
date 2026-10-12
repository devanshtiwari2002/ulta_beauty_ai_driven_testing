package stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.SearchResultsPage;
import utils.WebDriverProvider;

public class SearchSteps {
	private SearchResultsPage searchResultsPage;

	@Before
	public void setUp() {
		searchResultsPage = new SearchResultsPage(WebDriverProvider.getDriver());
	}

	@When("I search for {string}")
	public void searchFor(String searchTerm) {
		searchResultsPage.searchFor(searchTerm);
	}

	@Then("search results should be displayed")
	public void searchResultsShouldBeDisplayed() {
		Assert.assertTrue(searchResultsPage.isSearchResultsDisplayed(),
				"Search results should contain at least one visible product. "
						+ searchResultsPage.getSearchResultsDiagnostics());
	}

	@And("at least one product should be present in the results")
	public void atLeastOneProductShouldBePresentInResults() {
		Assert.assertTrue(searchResultsPage.hasAtLeastOneProduct(),
				"At least one visible product should be present in the search results. "
						+ searchResultsPage.getSearchResultsDiagnostics());
	}
}
