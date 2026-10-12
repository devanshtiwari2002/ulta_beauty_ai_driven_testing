package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartPage {
	private static final By VIEW_BAG_LINK = By.xpath(
			"//a[contains(@href, '/bag')][.//*[normalize-space()='View bag']]");
	private static final By BAG_COUNT_LINK = By.cssSelector("header a[href*='/bag']");
	private static final By CART_PRODUCT_LINKS = By.xpath(
			"//main//a[contains(@href, '/p/') and not(ancestor::*[@role='region' and "
					+ "contains(translate(@aria-label, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'carousel')])]" );
	private static final By BEAUTY_UNDER_20_HEADING = By.xpath("//h2[normalize-space()='Beauty under $20']");
	private static final By BEAUTY_UNDER_20_CAROUSEL = By.cssSelector(
			"div[role='region'][aria-label='Beauty under $20 Product Carousel']");
	private static final By RECOMMENDATION_CARDS = By.cssSelector("div.ProductCard[aria-label='product']");
	private static final By RECOMMENDATION_PRODUCT_NAME = By.cssSelector(
			".pal-c-ProductCardBody--title p");
	private static final By EMPTY_BAG_HEADING = By.xpath(
			"//main//*[self::h1 or self::h2][contains(translate(normalize-space(.), "
					+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'your bag is empty')]");
	private static final Pattern ITEM_COUNT = Pattern.compile(
			"\\b(\\d+)\\s+items?\\s+in\\s+bag\\b", Pattern.CASE_INSENSITIVE);

	private final WebDriver driver;
	private final WebDriverWait wait;

	public CartPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	public void open() {
		wait.until(ExpectedConditions.elementToBeClickable(VIEW_BAG_LINK)).click();
		wait.until(ExpectedConditions.urlContains("/bag"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(BAG_COUNT_LINK));
		wait.until(currentDriver -> readItemCount() >= 0);
	}

	public int getNumberOfItems() {
		return wait.until(currentDriver -> {
			int itemCount = readItemCount();
			return itemCount >= 0 ? itemCount : null;
		});
	}

	public boolean hasAtLeastItems(int minimumItemCount) {
		return getNumberOfItems() >= minimumItemCount;
	}

	public List<String> getProductNamesDisplayedInCart() {
		return wait.until(currentDriver -> {
			List<String> productNames = readProductNames();
			return !productNames.isEmpty() || isEmptyBagDisplayed() ? productNames : null;
		});
	}

	public boolean containsProductCategory(String categoryKeyword) {
		String normalizedCategory = normalize(categoryKeyword);
		return getProductNamesDisplayedInCart().stream()
				.map(this::normalize)
				.anyMatch(productName -> productName.contains(normalizedCategory));
	}

	public List<String> getRecommendationProductNames() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(BEAUTY_UNDER_20_HEADING));
		WebElement carousel = wait.until(ExpectedConditions.presenceOfElementLocated(BEAUTY_UNDER_20_CAROUSEL));
		new Actions(driver).scrollToElement(carousel).perform();
		carousel = wait.until(ExpectedConditions.visibilityOfElementLocated(BEAUTY_UNDER_20_CAROUSEL));

		return carousel.findElements(RECOMMENDATION_CARDS).stream()
				.map(card -> card.findElements(RECOMMENDATION_PRODUCT_NAME).stream()
						.findFirst()
						.map(element -> element.getDomProperty("textContent")
								.replaceAll("\\s+", " ").trim())
						.orElse(""))
				.filter(name -> !name.isBlank())
				.toList();
	}

	public List<String> getMissingProductNames(List<String> expectedProductNames) {
		List<String> actualNames = getProductNamesDisplayedInCart().stream()
				.map(this::normalize)
				.toList();
		return expectedProductNames.stream()
				.filter(expectedName -> !actualNames.contains(normalize(expectedName)))
				.toList();
	}

	private int readItemCount() {
		List<WebElement> bagLinks = driver.findElements(BAG_COUNT_LINK);
		if (bagLinks.isEmpty()) {
			return -1;
		}

		String accessibleName = bagLinks.get(0).getAccessibleName();
		Matcher countMatcher = ITEM_COUNT.matcher(accessibleName + " " + bagLinks.get(0).getText());
		if (countMatcher.find()) {
			return Integer.parseInt(countMatcher.group(1));
		}
		return isEmptyBagDisplayed() ? 0 : -1;
	}

	private List<String> readProductNames() {
		return driver.findElements(CART_PRODUCT_LINKS).stream()
				.filter(WebElement::isDisplayed)
				.map(element -> element.getText().replaceAll("\\s+", " ").trim())
				.filter(name -> !name.isBlank())
				.toList();
	}

	private boolean isEmptyBagDisplayed() {
		return driver.findElements(EMPTY_BAG_HEADING).stream().anyMatch(WebElement::isDisplayed);
	}

	private String normalize(String productName) {
		return productName.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
	}
}