package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductPage {
	private static final By PRODUCT_HEADING = By.cssSelector("main h1");
	private static final By PRODUCT_HERO = By.cssSelector("main .ProductHero__content");
	private static final By SIMILAR_ITEMS_HEADING = By.xpath("//h2[normalize-space()='Similar items for you']");
	private static final By RECOMMENDATION_CAROUSEL = By.cssSelector(
			"div[role='region'][aria-label='Similar items for you Product Carousel']");
	private static final By RECOMMENDATION_CARDS = By.cssSelector("div.ProductCard[aria-label='product']");
	private static final By RECOMMENDATION_PRODUCT_NAME = By.cssSelector(
			".pal-c-ProductCardBody--title p");
	private static final By VARIANT_OPTIONS = By.xpath(
			"//main//div[contains(@class, 'ProductHero__content')]"
					+ "//*[normalize-space()='Size:']/parent::div/following-sibling::div//a[contains(@href, 'sku=')]");
	private static final By PURCHASE_CTA = By.cssSelector(
			"button.AddToBagButton__AddToBag[aria-label='Add for ship']");
	private static final By KEEP_SHOPPING_BUTTON = By.xpath(
			"//button[.//span[normalize-space()='Keep shopping']]");
	private static final By ADD_TO_BAG_DIALOG = By.cssSelector("[role='dialog'][aria-label='add to bag']");
	private static final By ADD_TO_BAG_FLYOUT_CLOSE_BUTTON = By.cssSelector(
			"button.OverlayFlyoutHeader__close[aria-label='Close']");
	private static final By BAG_LINK = By.cssSelector("header a[href*='/bag']");
	private static final By ADDED_TO_BAG_CONFIRMATION = By.xpath(
			"//strong[contains(translate(normalize-space(.), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'IN BAG')]");
	private static final Pattern BAG_ITEM_COUNT = Pattern.compile("\\b(\\d+)\\s+items?\\b", Pattern.CASE_INSENSITIVE);
	private static final Logger LOGGER = Logger.getLogger(ProductPage.class.getName());

	private final WebDriver driver;
	private final WebDriverWait wait;
	private int bagItemCountBeforePurchase = -1;
	private String bagConfirmationBeforePurchase = "";

	public ProductPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	public boolean isLoaded() {
		return wait.until(driver -> driver.getCurrentUrl().contains("/p/")
				&& driver.findElements(PRODUCT_HEADING).stream().anyMatch(WebElement::isDisplayed));
	}

	public String getProductName() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_HEADING))
				.getText().replaceAll("\\s+", " ").trim();
	}

	public List<String> getRecommendationProductNames() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(SIMILAR_ITEMS_HEADING));
		WebElement carousel;

		try {
			carousel = wait.until(ExpectedConditions.presenceOfElementLocated(RECOMMENDATION_CAROUSEL));
		} catch (TimeoutException exception) {
			LOGGER.warning("Recommendation carousel found=false, cards=0, names=[]");
			throw new IllegalStateException("The 'Similar items for you' carousel was not found.", exception);
		}

		new Actions(driver).scrollToElement(carousel).perform();
		carousel = wait.until(ExpectedConditions.visibilityOfElementLocated(RECOMMENDATION_CAROUSEL));
		List<WebElement> recommendationCards = carousel.findElements(RECOMMENDATION_CARDS);
		List<String> recommendationNames = recommendationCards.stream()
				.map(card -> card.findElements(RECOMMENDATION_PRODUCT_NAME).stream()
						.findFirst()
						.map(element -> element.getDomProperty("textContent")
								.replaceAll("\\s+", " ").trim())
						.orElse(""))
				.filter(name -> !name.isBlank())
				.toList();
		LOGGER.info(() -> String.format(Locale.ROOT,
				"Recommendation carousel found=true, cards=%d, names=%s",
				recommendationCards.size(), recommendationNames));
		return recommendationNames;
	}

	public boolean hasAvailableVariants() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_HERO));
		return driver.findElements(VARIANT_OPTIONS).stream().anyMatch(this::isAvailableVariant);
	}

	public void selectAvailableVariant() {
		List<WebElement> variants = driver.findElements(VARIANT_OPTIONS).stream()
				.filter(this::isAvailableVariant)
				.toList();
		if (variants.isEmpty() || variants.stream().anyMatch(this::isSelectedVariant)) {
			return;
		}

		WebElement variant = variants.get(0);
		String variantName = variant.getText().trim();
		wait.until(ExpectedConditions.elementToBeClickable(variant)).click();
		wait.until(driver -> driver.findElements(VARIANT_OPTIONS).stream()
				.anyMatch(option -> isSelectedVariant(option)
						&& variantName.equals(option.getText().trim())));
		wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_HEADING));
	}

	public void clickPurchaseCta() {
		bagItemCountBeforePurchase = getBagItemCount();
		bagConfirmationBeforePurchase = getBagConfirmationText();
		wait.until(ExpectedConditions.elementToBeClickable(PURCHASE_CTA)).click();
	}

	public boolean isProductAddedSuccessfully() {
		return wait.until(driver -> isConfirmationVisible()
				&& !getBagConfirmationText().equals(bagConfirmationBeforePurchase)
				|| (bagItemCountBeforePurchase >= 0 && getBagItemCount() > bagItemCountBeforePurchase));
	}

	public void continueShopping() {
		wait.until(ExpectedConditions.elementToBeClickable(KEEP_SHOPPING_BUTTON)).click();
		wait.until(ExpectedConditions.invisibilityOfElementLocated(ADD_TO_BAG_DIALOG));
	}

	public void closeAddToBagFlyout() {
		if (findVisibleAddToBagDialog() == null) {
			return;
		}

		WebElement closeButton;
		try {
			closeButton = wait.until(driver -> findVisibleEnabledFlyoutCloseButton());
		} catch (TimeoutException exception) {
			if (findVisibleAddToBagDialog() == null) {
				return;
			}
			String diagnostic = getFlyoutCloseDiagnostic();
			LOGGER.warning(diagnostic);
			throw new TimeoutException(diagnostic, exception);
		}

		try {
			closeButton.click();
		} catch (ElementClickInterceptedException exception) {
			LOGGER.info("Flyout Close click was intercepted; retrying with a DOM click.");
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeButton);
		}

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(ADD_TO_BAG_DIALOG));
		} catch (TimeoutException exception) {
			String diagnostic = getFlyoutCloseDiagnostic();
			LOGGER.warning(diagnostic);
			throw new TimeoutException(diagnostic, exception);
		}
	}

	private WebElement findVisibleAddToBagDialog() {
		return driver.findElements(ADD_TO_BAG_DIALOG).stream()
				.filter(WebElement::isDisplayed)
				.findFirst()
				.orElse(null);
	}

	private WebElement findVisibleEnabledFlyoutCloseButton() {
		WebElement dialog = findVisibleAddToBagDialog();
		if (dialog == null) {
			return null;
		}
		return dialog.findElements(ADD_TO_BAG_FLYOUT_CLOSE_BUTTON).stream()
				.filter(WebElement::isDisplayed)
				.filter(WebElement::isEnabled)
				.findFirst()
				.orElse(null);
	}

	private String getFlyoutCloseDiagnostic() {
		List<WebElement> dialogs = driver.findElements(ADD_TO_BAG_DIALOG);
		long visibleDialogs = dialogs.stream().filter(WebElement::isDisplayed).count();
		long scopedCloseButtons = dialogs.stream()
				.filter(WebElement::isDisplayed)
				.flatMap(dialog -> dialog.findElements(ADD_TO_BAG_FLYOUT_CLOSE_BUTTON).stream())
				.count();
		long clickableCloseButtons = dialogs.stream()
				.filter(WebElement::isDisplayed)
				.flatMap(dialog -> dialog.findElements(ADD_TO_BAG_FLYOUT_CLOSE_BUTTON).stream())
				.filter(WebElement::isDisplayed)
				.filter(WebElement::isEnabled)
				.count();
		return String.format(
				"Could not close Add-to-Bag flyout. URL: %s, visible dialogs: %d, "
						+ "scoped Close buttons: %d, visible/enabled Close buttons: %d",
				driver.getCurrentUrl(), visibleDialogs, scopedCloseButtons, clickableCloseButtons);
	}

	private boolean isAvailableVariant(WebElement variant) {
		return variant.isDisplayed()
				&& variant.isEnabled()
				&& !"true".equalsIgnoreCase(variant.getAttribute("aria-disabled"));
	}

	private boolean isSelectedVariant(WebElement variant) {
		if ("true".equalsIgnoreCase(variant.getAttribute("aria-current"))
				|| "true".equalsIgnoreCase(variant.getAttribute("aria-selected"))
				|| "true".equalsIgnoreCase(variant.getAttribute("aria-pressed"))) {
			return true;
		}
		String className = variant.getAttribute("class");
		return className != null && className.toLowerCase(Locale.ROOT).contains("selected");
	}

	private boolean isConfirmationVisible() {
		return driver.findElements(ADDED_TO_BAG_CONFIRMATION).stream()
				.anyMatch(element -> element.isDisplayed() && element.getText().matches("\\d+\\s+IN BAG"));
	}

	private String getBagConfirmationText() {
		return driver.findElements(ADDED_TO_BAG_CONFIRMATION).stream()
				.filter(WebElement::isDisplayed)
				.map(element -> element.getText().trim())
				.filter(text -> text.matches("\\d+\\s+IN BAG"))
				.findFirst()
				.orElse("");
	}

	private int getBagItemCount() {
		List<WebElement> bagLinks = driver.findElements(BAG_LINK);
		if (bagLinks.isEmpty()) {
			return -1;
		}

		WebElement bagLink = bagLinks.get(0);
		Matcher countMatcher = BAG_ITEM_COUNT.matcher(bagLink.getAccessibleName() + " " + bagLink.getText());
		return countMatcher.find() ? Integer.parseInt(countMatcher.group(1)) : -1;
	}
}
