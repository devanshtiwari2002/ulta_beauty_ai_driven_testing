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
import utils.BeautyCategoryAnalyzer;
import utils.BeautyCategoryAnalyzer.AnalysisResult;
import utils.WebDriverProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BeautyCategorySteps {
	private static final List<String> SEARCH_TERMS = List.of("moisturizer", "mascara");
	private static final Logger LOGGER = Logger.getLogger(BeautyCategorySteps.class.getName());

	private final List<String> cartProductNames = new ArrayList<>();
	private final List<String> collectedRecommendationNames = new ArrayList<>();
	private int productPageRecommendationCount;
	private int cartPageRecommendationCount;

	@Given("the user searches for moisturizer and mascara and adds both to the cart")
	public void userSearchesForProductsAndAddsThemToCart() {
		WebDriver driver = WebDriverProvider.getDriver();
		cartProductNames.clear();
		collectedRecommendationNames.clear();
		productPageRecommendationCount = 0;
		cartPageRecommendationCount = 0;

		for (int index = 0; index < SEARCH_TERMS.size(); index++) {
			String searchTerm = SEARCH_TERMS.get(index);
			ProductPage productPage = openFirstProduct(driver, searchTerm);
			cartProductNames.add(productPage.getProductName());

			List<String> productRecommendations = productPage.getRecommendationProductNames();
			collectedRecommendationNames.addAll(productRecommendations);
			productPageRecommendationCount += productRecommendations.size();
			LOGGER.info(() -> String.format("Product-page recommendations collected for '%s': %d",
					searchTerm, productRecommendations.size()));

			productPage.clickPurchaseCta();
			Assert.assertTrue(productPage.isProductAddedSuccessfully(),
						"The product was not added to the bag for search term: " + searchTerm);
			if (index < SEARCH_TERMS.size() - 1) {
				productPage.closeAddToBagFlyout();
			}
		}
	}

	@When("all recommendations from the product pages and cart page are collected")
	public void collectCartRecommendations() {
		CartPage cartPage = new CartPage(WebDriverProvider.getDriver());
		cartPage.open();

		List<String> actualCartProductNames = cartPage.getProductNamesDisplayedInCart();
		List<String> missingProductNames = cartPage.getMissingProductNames(cartProductNames);
		Assert.assertTrue(missingProductNames.isEmpty(),
				"Expected products were not found in the cart: " + missingProductNames);

		List<String> cartRecommendationNames = cartPage.getRecommendationProductNames();
		collectedRecommendationNames.addAll(cartRecommendationNames);
		cartPageRecommendationCount = cartRecommendationNames.size();
		LOGGER.info(() -> String.format(
				"Cart products verified: %s; cart-page recommendations collected: %d",
				actualCartProductNames, cartRecommendationNames.size()));
	}

	@Then("at least 20 percent of all collected recommendations must be beauty category")
	public void collectedRecommendationsMeetBeautyCategoryThreshold() {
		AnalysisResult analysis = BeautyCategoryAnalyzer.analyze(collectedRecommendationNames);
		double threshold = BeautyCategoryAnalyzer.MINIMUM_CATEGORY_PERCENTAGE;
		LOGGER.info(() -> String.format(
				"AC7 counts: product-page recommendations=%d, cart-page recommendations=%d, "
						+ "total=%d, beauty-category=%d, percentage=%.1f%%",
				productPageRecommendationCount,
				cartPageRecommendationCount,
				analysis.totalRecommendations(),
				analysis.beautyCategoryCount(),
				analysis.beautyCategoryPercentage()));
		Assert.assertTrue(analysis.meetsThreshold(threshold), analysis.diagnosticMessage(threshold));
	}

	private ProductPage openFirstProduct(WebDriver driver, String searchTerm) {
		new HomePage(driver).open();
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
		return productPage;
	}
}