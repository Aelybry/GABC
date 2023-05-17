package com.GABC.webelements;

import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author bryan.adante
 *
 */
public class Link {

	private Link() {

	}

	private static class LinkSingleton {
		private static Link INSTANCE = new Link();
	}

	public static Link getInstance() {
		System.out.println(LinkSingleton.INSTANCE);
		return LinkSingleton.INSTANCE;
	}

	// ****Actions****
	public void click(ExtentTest logger, WebElement we) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Link is NOT displayed");
		} else {
			we.click();
			logger.log(LogStatus.PASS, we + " Link is clicked");
		}
	}
	
	public String getAttribute(ExtentTest logger, WebElement we, String attribute) {
		String value = null;
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Link is NOT displayed");
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
			logger.log(LogStatus.FAIL, we + " Link is NOT displayed");
		} else {
			logger.log(LogStatus.PASS, we + " Link is displayed");
		}
	}

	public void verifyNotDisplayed(ExtentTest logger, WebElement we) {
		if (we == null) {
			logger.log(LogStatus.PASS, we + " Link is NOT displayed");
		} else {
			logger.log(LogStatus.FAIL, we + " Link is displayed");
		}
	}

	public void verifyTextContains(ExtentTest logger, WebElement we, String text) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Link is NOT displayed");
		} else {
			if (we.getText().contains(text)) {
				logger.log(LogStatus.PASS, we + " Link text contains <b>" + text + "</b>");
			} else {
				logger.log(LogStatus.FAIL, we + " Link text NOT contains <b>" + text + "</b>");
			}
		}
	}

	public void verifyTextEquals(ExtentTest logger, WebElement we, String text) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Link is NOT displayed");
		} else {
			if (we.getText().equals(text)) {
				logger.log(LogStatus.PASS, we + " Link text is equal to <b>" + text + "</b>");
			} else {
				logger.log(LogStatus.FAIL, we + " Link text is NOT equal to <b>" + text + "</b>");
			}
		}
	}

}
