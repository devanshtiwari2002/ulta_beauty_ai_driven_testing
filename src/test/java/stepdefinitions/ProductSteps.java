package stepdefinitions;

import io.cucumber.java.en.And;
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

public class ProductSteps {
	private static final List<String> AC4_SEARCH_TERMS = List.of("moisturizer", "mascara", "vitamin c serum");

	private ProductPage productPage;
	private CartPage cartPage;
	private final List<String> addedProductNames = new ArrayList<>();

	@Given("the user has added 3 products to the bag")
	public void userHasAddedThreeProductsToBag() {
		WebDriver driver = WebDriverProvider.getDriver();
		addedProductNames.clear();
		new HomePage(driver).open();

		for (int index = 0; index < AC4_SEARCH_TERMS.size(); index++) {
			productPage = addProductForSearchTerm(driver, AC4_SEARCH_TERMS.get(index));
			if (index < AC4_SEARCH_TERMS.size() - 1) {
				productPage.closeAddToBagFlyout();
			}
		}
	}

	@When("the user navigates to the cart")
	public void userNavigatesToCart() {
		cartPage = new CartPage(WebDriverProvider.getDriver());
		cartPage.open();
	}

	@Then("the cart displays at least 3 items")
	public void cartDisplaysAtLeastThreeItems() {
		Assert.assertTrue(cartPage.hasAtLeastItems(3),
				"The cart should display at least 3 items, but displayed " + cartPage.getNumberOfItems() + ".");
	}

	@And("each added product is listed with the correct product name")
	public void eachAddedProductIsListedWithCorrectName() {
		List<String> missingProductNames = cartPage.getMissingProductNames(addedProductNames);
		Assert.assertTrue(missingProductNames.isEmpty(),
				"These added products were not found in the cart: " + missingProductNames);
	}

	@Then("at least 30 percent of recommendations are relevant to the viewed product")
	public void recommendationsAreRelevantToViewedProduct() {
		ProductPage viewedProductPage = getProductPage();
		String viewedProductName = viewedProductPage.getProductName();
		List<String> recommendationNames = viewedProductPage.getRecommendationProductNames();
		RecommendationAnalyzer.AnalysisResult analysis = RecommendationAnalyzer.analyze(
				viewedProductName, recommendationNames);
		double minimumPercentage = RecommendationAnalyzer.MINIMUM_RELEVANCE_PERCENTAGE;

		Assert.assertTrue(analysis.meetsThreshold(minimumPercentage),
				analysis.diagnosticMessage(minimumPercentage));
	}

	@And("I open the first product")
	public void openFirstProduct() {
		WebDriver driver = WebDriverProvider.getDriver();
		SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
		Assert.assertTrue(searchResultsPage.hasAtLeastOneProduct(),
				"Search results should contain at least one product before opening a product.");
		searchResultsPage.openFirstProduct();

		productPage = new ProductPage(driver);
		Assert.assertTrue(productPage.isLoaded(), "The first product page did not load successfully.");
	}

	@And("I select an available product variant if required")
	public void selectAvailableProductVariantIfRequired() {
		ProductPage productPage = getProductPage();
		if (productPage.hasAvailableVariants()) {
			productPage.selectAvailableVariant();
		}
	}

	@And("I add the product to the bag")
	public void addProductToBag() {
		getProductPage().clickPurchaseCta();
	}

	@Then("the product should be added successfully")
	public void productShouldBeAddedSuccessfully() {
		Assert.assertTrue(getProductPage().isProductAddedSuccessfully(),
				"The selected product was not added to the bag successfully.");
	}

	private ProductPage addProductForSearchTerm(WebDriver driver, String searchTerm) {
		SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
		searchResultsPage.searchFor(searchTerm);
		Assert.assertTrue(searchResultsPage.hasAtLeastOneProduct(),
				"Search results should contain a product for: " + searchTerm);
		searchResultsPage.openFirstProduct();

		ProductPage currentProductPage = new ProductPage(driver);
		Assert.assertTrue(currentProductPage.isLoaded(),
				"The first product page did not load for search term: " + searchTerm);
		if (currentProductPage.hasAvailableVariants()) {
			currentProductPage.selectAvailableVariant();
		}

		String productName = currentProductPage.getProductName();
		Assert.assertFalse(productName.isBlank(),
				"The product name should be available for search term: " + searchTerm);
		currentProductPage.clickPurchaseCta();
		Assert.assertTrue(currentProductPage.isProductAddedSuccessfully(),
				"The product was not added to the bag for search term: " + searchTerm);
		addedProductNames.add(productName);
		return currentProductPage;
	}

	private ProductPage getProductPage() {
		if (productPage == null) {
			productPage = new ProductPage(WebDriverProvider.getDriver());
		}
		return productPage;
	}
}