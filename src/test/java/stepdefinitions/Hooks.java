package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import utils.WebDriverProvider;

public class Hooks {
	@Before(order = 0)
	public void startWebDriver() {
		WebDriverProvider.startDriver();
	}

	@After
	public void quitWebDriver() {
		WebDriverProvider.quitDriver();
	}
}