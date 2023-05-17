package com.GABC.webelements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 
 * @author bryan.adante
 *
 */
public class AlertsHandler {

	private AlertsHandler() {

	}

	private static class AlertsHandlerSingleton {
		private static AlertsHandler INSTANCE = new AlertsHandler();
	}

	public static AlertsHandler getInstance() {
		System.out.println(AlertsHandlerSingleton.INSTANCE);
		return AlertsHandlerSingleton.INSTANCE;
	}

	// ****Actions****
	public void acceptAlert(WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);

			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				driver.switchTo().alert().accept();
			} else {
				System.out.println("No Alert");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
