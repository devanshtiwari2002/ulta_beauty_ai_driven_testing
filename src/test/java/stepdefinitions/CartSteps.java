package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;
import pages.SearchResultsPage;
import utils.RecommendationAnalyzer;
import utils.WebDriverProvider;

import java.util.ArrayList;
import java.util.List;

public class CartSteps {
	private final List<String> addedProductNames = new ArrayList<>();
	private List<String> cartProductNames = List.of();
	private List<String> recommendationNames = List.of();

	@Given("the user has moisturizer and mascara in the cart")
	public void userHasMoisturizerAndMascaraInCart() {
		WebDriver driver = WebDriverProvider.getDriver();
		addedProductNames.clear();
		new HomePage(driver).open();

		addProductForSearchTerm(driver, "moisturizer");
		ProductPage moisturizerPage = new ProductPage(driver);
		moisturizerPage.closeAddToBagFlyout();

		addProductForSearchTerm(driver, "mascara");
	}

	@When("the user views cart recommendations")
	public void userViewsCartRecommendations() {
		CartPage cartPage = new CartPage(WebDriverProvider.getDriver());
		cartPage.open();

		cartProductNames = cartPage.getProductNamesDisplayedInCart();
		List<String> missingAddedProducts = cartPage.getMissingProductNames(addedProductNames);
		Assert.assertTrue(missingAddedProducts.isEmpty(),
				"Added products are missing from the cart: " + missingAddedProducts);
		Assert.assertTrue(cartPage.containsProductCategory("moisturizer"),
				"The cart should contain a moisturizer product: " + cartProductNames);
		Assert.assertTrue(cartPage.containsProductCategory("mascara"),
				"The cart should contain a mascara product: " + cartProductNames);

		recommendationNames = cartPage.getRecommendationProductNames();
	}

	@Then("at least 25 percent of recommendations should be relevant to the cart items")
	public void recommendationsShouldBeRelevantToCartItems() {
		RecommendationAnalyzer.AnalysisResult analysis = RecommendationAnalyzer.analyze(
				cartProductNames, recommendationNames);
		Assert.assertTrue(analysis.meetsThreshold(25.0), analysis.diagnosticMessage(25.0));
	}

	private void addProductForSearchTerm(WebDriver driver, String searchTerm) {
		SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
		searchResultsPage.searchFor(searchTerm);
		Assert.assertTrue(searchResultsPage.hasAtLeastOneProduct(),
				"Search results should contain a product for: " + searchTerm);
		searchResultsPage.openFirstProduct();

		ProductPage productPage = new ProductPage(driver);
		Assert.assertTrue(productPage.isLoaded(),
				"The first product page did not load for search term: " + searchTerm);
		if (productPage.hasAvailableVariants()) {
			productPage.selectAvailableVariant();
		}

		String productName = productPage.getProductName();
		Assert.assertFalse(productName.isBlank(),
				"The product name should be available for search term: " + searchTerm);
		productPage.clickPurchaseCta();
		Assert.assertTrue(productPage.isProductAddedSuccessfully(),
				"The product was not added to the bag for search term: " + searchTerm);
		addedProductNames.add(productName);
	}
}