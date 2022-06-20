package com.Shopify.testobjects;

import java.awt.Dimension;
import java.awt.List;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.Shopify.core.BaseTest;
import com.Shopify.core.Browser;
import com.Shopify.webelements.AlertsHandler;
import com.Shopify.webelements.Button;
import com.Shopify.webelements.Dropdown;
import com.Shopify.webelements.Link;
import com.Shopify.webelements.RadioButton;
import com.Shopify.webelements.TextBox;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ShopifyTest extends BaseTest{
	
	@Test
	public void openHomePage() {
		ExtentTest logger = extent.startTest("Opening Homepage", "Opening Homepage");
		try {
			navigate(logger);
			Browser.waitForBrowserToLoadCompletely(driver);
			Link.getInstance().click(logger, homePage.lnk_closeOffer);
			Browser.pause(logger, "3");
			Button.getInstance().click(logger,homePage.btn_password);
			Browser.pause(logger, "3");
			TextBox.getInstance().setTextAndEnter(logger, homePage.tf_password, homePage.txt_password);
			Browser.pause(logger, "3");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
		
	

	@Test
	//Check search functionality with results found
	public void createOrder_BOSTC_Store() {
		ExtentTest logger = extent.startTest("Create BOSTC order fulfilled by Store", "Create BOSTC order fulfilled by Store");
		try {			
			System.out.println("Create BOSTC order fulfilled by Store");
			String sKU = inputData.get("Create").get("searchSKU");	
			Browser.waitForElementIsClickable(logger, driver, homePage.icn_Search);
			Button.getInstance().click(logger, homePage.icn_Search);
			Browser.pause(logger, "1");
			TextBox.getInstance().setTextAndEnter(logger, homePage.tf_search, sKU);
			Browser.pause(logger, "1");
			//Button.getInstance().click(logger, driver.findElement(By.xpath(homePage.img_searchResults + sKU.substring(0, 6) + "')][1]")));
			Button.getInstance().click(logger, homePage.btn_searchSKUThumbnail);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
}
