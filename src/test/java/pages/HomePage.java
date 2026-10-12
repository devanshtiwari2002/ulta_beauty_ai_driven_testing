package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class HomePage {
	private static final String HOMEPAGE_URL = "https://www.ulta.com/";
	private static final By HOMEPAGE_HEADING = By.cssSelector("main h1");
	private static final By SEARCH_CONTROL = By.cssSelector("button[aria-label='Product search']");
	private static final By ACCOUNT_LINK = By.cssSelector("header a[href*='/auth/login']");

	private final WebDriver driver;
	private final WebDriverWait wait;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	public void open() {
		driver.get(HOMEPAGE_URL);
		wait.until(ExpectedConditions.visibilityOfElementLocated(HOMEPAGE_HEADING));
		wait.until(ExpectedConditions.elementToBeClickable(SEARCH_CONTROL));
	}

	public boolean isLoaded() {
		List<WebElement> headings = driver.findElements(HOMEPAGE_HEADING);
		return driver.getTitle().contains("Ulta Beauty")
				&& !headings.isEmpty()
				&& headings.get(0).isDisplayed();
	}

	public boolean isSearchBarVisibleAndEnabled() {
		WebElement searchControl = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_CONTROL));
		return searchControl.isDisplayed() && searchControl.isEnabled();
	}

	public boolean isAccountIconAccessible() {
		List<WebElement> accountLinks = driver.findElements(ACCOUNT_LINK);
		if (accountLinks.isEmpty()) {
			return false;
		}

		WebElement accountLink = accountLinks.get(0);
		return accountLink.isDisplayed()
				&& accountLink.isEnabled()
				&& !accountLink.getAccessibleName().toLowerCase(Locale.ROOT).isBlank();
	}
}