package com.GABC.webelements;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author bryan.adante
 *
 */
public class Dropdown {

	private Select select;

	private Dropdown() {

	}

	private static class DropdownSingleton {
		private static Dropdown INSTANCE = new Dropdown();
	}

	public static Dropdown getInstance() {
		System.out.println(DropdownSingleton.INSTANCE);
		return DropdownSingleton.INSTANCE;
	}

	// ****Actions****
	public void selectByIndex(ExtentTest logger, WebElement we, int index) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Dropdown is NOT displayed");
		} else {
			select = new Select(we);
			select.selectByIndex(index);
			logger.log(LogStatus.PASS, "Selected index <b>" + index + "</b> from " + we + " dropdown");
		}
	}

	public void selectByVisibleText(ExtentTest logger, WebElement we, String text) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Dropdown is NOT displayed");
		} else {
			select = new Select(we);
			select.selectByVisibleText(text);
			logger.log(LogStatus.PASS, "Selected value <b>" + text + "</b> from " + we + " dropdown");
		}
	}
	
	public void selectByPartOfTextIgnoreCase(ExtentTest logger, WebDriver driver, WebElement we, String fieldValue) {
		//Select dropDown = new Select(driver.findElement(fieldId));
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Dropdown is NOT displayed");
		} else {
			int index = 0;
			select = new Select(we);
		    for (WebElement option : select.getOptions()) {
		        if (option.getText().toLowerCase().contains(fieldValue.toLowerCase())) {
		        	System.out.println(option.getText().toLowerCase() + " ✓ \'" + fieldValue.toLowerCase() + "\'");
		            break;
		        } else {
		        	//System.out.println(option.getText().toLowerCase() + " x \'" + fieldValue.toLowerCase() + "\'");
		        	index++;
		        }
		    }
		    select.selectByIndex(index);
		    logger.log(LogStatus.PASS, "Selected value <b>" + fieldValue + "</b> from " + we + " dropdown");
		}
	}
	
	public String getFirstSelectedValue(ExtentTest logger, WebElement we) {
		String text = null;
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Dropdown is NOT displayed");
		} else {
			select = new Select(we);
			text = select.getFirstSelectedOption().getText();
		}
		return text;
	}
	
	public String getAttribute(ExtentTest logger, WebElement we, String attribute) {
		String value = null;
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " Dropdown is NOT displayed");
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
	
	public boolean verifyDropdownValues(ExtentTest logger, WebElement object, String optionsList) {
		boolean status = false;
		boolean dropStatus = true;
		try {
			List<WebElement> droplistContents = object.findElements(By.tagName("option"));

			String valuesList[] = optionsList.split("|");

			if (valuesList.length > droplistContents.size()) {
//				logger.log(LogStatus.FAIL,"Failed to verify dropdown list values due to <b>Dropdown list size and Options size are different</b><br>"+ valuesList.length + ">" + droplistContents.size());
				return false;
			}
			for (int i = 0; i < valuesList.length; i++) {
				for (int j = 0; j < droplistContents.size(); j++) {
					if (droplistContents.get(j).getText().contains(valuesList[i])) {
						logger.log(LogStatus.PASS,droplistContents.get(j).getText() + " : Option avaliable in Dropdown list");
//						return false;
						status = true;
						break;
					}
				}
				if (status == false) {
					dropStatus = false;
					logger.log(LogStatus.FAIL, "<b>" + valuesList[i] + "</b> :option not avaliable in Dropdown list");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Failed:  to verify dropdown list values due to <b style='color:red'>"
					+ e.getClass() + "<br>" + e.getMessage() + "</b>");
			return false;
		}
		return dropStatus;
	}

}
