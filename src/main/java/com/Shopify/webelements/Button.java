package com.Shopify.webelements;

import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author bryan.adante
 *
 */
public class Button {

	private Button() {

	}

	private static class ButtonSingleton {
		private static Button INSTANCE = new Button();
	}

	public static Button getInstance() {
		System.out.println(ButtonSingleton.INSTANCE);
		return ButtonSingleton.INSTANCE;
	}

	// ****Actions****
	public void click(ExtentTest logger, WebElement we) {
		try {
			we.click();
			logger.log(LogStatus.PASS, we + " Button is clicked");
			System.out.println(" Click: " + we.getText());
		} catch (Exception e) {
			System.out.println(" Faield to click: " + we);
		}
	}

	public String getAttribute(ExtentTest logger, WebElement we, String attribute) {
		String value = null;
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Button is NOT displayed");
		} else {
			value = we.getAttribute(attribute);
		}
		return value;
	}

	// ****Verification****
	public boolean isDisplayed(WebElement we) {
		if (we == null) {
			return false;
		} else {
			return we.isDisplayed();
		}
	}

	public boolean isEnabled(WebElement we) {
		if (we == null) {
			return false;
		} else {
			return we.isEnabled();
		}
	}

	public boolean isTextContains(WebElement we, String text) {
		if (we == null) {
			return false;
		} else {
			return we.getText().contains(text);
		}
	}

	public boolean isTextEquals(WebElement we, String text) {
		if (we == null) {
			return false;
		} else {
			return we.getText().equals(text);
		}
	}

	public void verifyDisplayed(ExtentTest logger, WebElement we) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Button is NOT displayed");
		} else {
			logger.log(LogStatus.PASS, we + " Button is displayed");
		}
	}

	public void verifyNotDisplayed(ExtentTest logger, WebElement we) {
		if (we == null) {
			logger.log(LogStatus.PASS, we + " Button is NOT displayed");
		} else {
			logger.log(LogStatus.FAIL, we + " Button is displayed");
		}
	}

	public void verifyTextContains(ExtentTest logger, WebElement we, String text) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Button is NOT displayed");
		} else {
			if (we.getText().contains(text)) {
				logger.log(LogStatus.PASS, we + " Button text contains <b>" + text + "</b>");
			} else {
				logger.log(LogStatus.FAIL, we + " Button text NOT contains <b>" + text + "</b>");
			}
		}
	}

	public void verifyTextEquals(ExtentTest logger, WebElement we, String text) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Button is NOT displayed");
		} else {
			if (we.getText().equals(text)) {
				logger.log(LogStatus.PASS, we + " Button text is equal to <b>" + text + "</b>");
			} else {
				logger.log(LogStatus.FAIL, we + " Button text is NOT equal to <b>" + text + "</b>");
			}
		}
	}

}
