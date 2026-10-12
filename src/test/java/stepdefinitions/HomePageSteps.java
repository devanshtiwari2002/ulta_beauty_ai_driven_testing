package stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.HomePage;
import org.testng.Assert;
import utils.WebDriverProvider;

public class HomePageSteps {
	private HomePage homePage;

	@Given("I open the Ulta homepage")
	public void openUltaHomepage() {
		homePage = new HomePage(WebDriverProvider.getDriver());
		homePage.open();
	}

	@Then("the homepage should load successfully")
	public void homepageShouldLoadSuccessfully() {
		Assert.assertTrue(homePage.isLoaded(), "Ulta homepage did not load successfully.");
	}

	@And("the search bar should be visible and enabled")
	public void searchBarShouldBeVisibleAndEnabled() {
		Assert.assertTrue(homePage.isSearchBarVisibleAndEnabled(),
				"The homepage search control should be visible and enabled.");
	}

	@And("the account icon should be accessible")
	public void accountIconShouldBeAccessible() {
		Assert.assertTrue(homePage.isAccountIconAccessible(),
				"The account icon should be visible and have an accessible name.");
	}
}
