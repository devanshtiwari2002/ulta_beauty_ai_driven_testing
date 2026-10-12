package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public final class WebDriverProvider {
	private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

	private WebDriverProvider() {
	}

	public static void startDriver() {
		if (DRIVER.get() == null) {
			DRIVER.set(new ChromeDriver());
		}
	}

	public static WebDriver getDriver() {
		WebDriver driver = DRIVER.get();
		if (driver == null) {
			throw new IllegalStateException("WebDriver has not been started for this scenario.");
		}
		return driver;
	}

	public static void quitDriver() {
		WebDriver driver = DRIVER.get();
		if (driver != null) {
			try {
				driver.quit();
			} finally {
				DRIVER.remove();
			}
		}
	}
}