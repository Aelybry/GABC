package com.GABC.webelements;

import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author bryan.adante
 *
 */
public class RadioButton {

	private RadioButton() {

	}

	private static class RadioButtonSingleton {
		private static RadioButton INSTANCE = new RadioButton();
	}

	public static RadioButton getInstance() {
		System.out.println(RadioButtonSingleton.INSTANCE);
		return RadioButtonSingleton.INSTANCE;
	}

	// ****Actions****
	public void click(ExtentTest logger, WebElement we) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " RadioButton is NOT displayed");
		} else {
			we.click();
			logger.log(LogStatus.PASS, we + " RadioButton is clicked");
		}
	}
	
	public String getAttribute(ExtentTest logger, WebElement we, String attribute) {
		String value = null;
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " RadioButton is NOT displayed");
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

	public boolean isSelected(WebElement we) {
		if (we == null) {
			return false;
		} else {
			return we.isSelected();
		}
	}

	public void verifyDisplayed(ExtentTest logger, WebElement we) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " RadioButton is NOT displayed");
		} else {
			logger.log(LogStatus.PASS, we + " RadioButton is displayed");
		}
	}

	public void verifyNotDisplayed(ExtentTest logger, WebElement we) {
		if (we == null) {
			logger.log(LogStatus.PASS, we + " RadioButton is NOT displayed");
		} else {
			logger.log(LogStatus.FAIL, we + " RadioButton is displayed");
		}
	}

}
