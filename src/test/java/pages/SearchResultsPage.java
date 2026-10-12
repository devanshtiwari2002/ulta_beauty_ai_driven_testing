package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class SearchResultsPage {
	private static final By SEARCH_BUTTON = By.cssSelector("button[aria-label='Product search']");
	private static final By SEARCH_INPUT = By.cssSelector(
			"input[type='search'][name='search'][aria-label='Search Ulta Beauty']");
	private static final By RESULTS_HEADING = By.xpath("//main//h1[normalize-space()='Search Results']");
	private static final By RESULTS_SUMMARY = By.xpath("//main//h2[contains(normalize-space(.), 'results for')]");
	private static final By PRODUCT_LINKS = By.cssSelector("main a[href*='/p/']");
	private static final Logger LOGGER = Logger.getLogger(SearchResultsPage.class.getName());

	private final WebDriver driver;
	private final WebDriverWait wait;
	private String lastSearchTerm = "";

	public SearchResultsPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	public void searchFor(String searchTerm) {
		lastSearchTerm = searchTerm;
		wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BUTTON)).click();

		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
		searchInput.clear();
		searchInput.sendKeys(searchTerm, Keys.ENTER);

		try {
			wait.until(ExpectedConditions.urlContains("/search"));
			wait.until(currentDriver -> hasSearchResultsContent());
		} catch (TimeoutException exception) {
			String diagnostics = String.format(
					"Search results did not load for '%s'. URL: %s, headings: %d, summaries: %d, product links: %d",
					searchTerm,
					driver.getCurrentUrl(),
					driver.findElements(RESULTS_HEADING).size(),
					driver.findElements(RESULTS_SUMMARY).size(),
					driver.findElements(PRODUCT_LINKS).size());
			LOGGER.warning(diagnostics);
			throw new TimeoutException(diagnostics, exception);
		}
	}

	private boolean hasSearchResultsContent() {
		return driver.findElements(RESULTS_HEADING).stream().anyMatch(WebElement::isDisplayed)
				|| hasVisibleProductResults();
	}

	public boolean isSearchResultsDisplayed() {
		return wait.until(currentDriver -> isSearchPageWithVisibleProducts());
	}

	public boolean hasAtLeastOneProduct() {
		return wait.until(currentDriver -> isSearchPageWithVisibleProducts());
	}

	public String getSearchResultsDiagnostics() {
		List<WebElement> productLinks = driver.findElements(PRODUCT_LINKS);
		long visibleProductLinks = productLinks.stream().filter(WebElement::isDisplayed).count();
		return String.format(
				"Search term: '%s'; URL: %s; visible product results: %d; "
						+ "product-link selector (%s) count: %d; results heading count: %d; results summary count: %d",
				lastSearchTerm,
				driver.getCurrentUrl(),
				visibleProductLinks,
				PRODUCT_LINKS,
				productLinks.size(),
				driver.findElements(RESULTS_HEADING).size(),
				driver.findElements(RESULTS_SUMMARY).size());
	}

	private boolean isSearchPageWithVisibleProducts() {
		return driver.getCurrentUrl().contains("/search") && hasVisibleProductResults();
	}

	private boolean hasVisibleProductResults() {
		return driver.findElements(PRODUCT_LINKS).stream().anyMatch(WebElement::isDisplayed);
	}

	public void openFirstProduct() {
		WebElement firstProductLink = wait.until(driver -> driver.findElements(PRODUCT_LINKS).stream()
				.filter(WebElement::isDisplayed)
				.filter(WebElement::isEnabled)
				.findFirst()
				.orElse(null));
		wait.until(ExpectedConditions.elementToBeClickable(firstProductLink)).click();
	}
}
