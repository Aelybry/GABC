package com.Shopify.testobjects;

import java.awt.Dimension;
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.GABC.core.BaseTest;
import com.GABC.core.Browser;
import com.GABC.webelements.AlertsHandler;
import com.GABC.webelements.Button;
import com.GABC.webelements.Dropdown;
import com.GABC.webelements.Link;
import com.GABC.webelements.RadioButton;
import com.GABC.webelements.TextBox;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TrainingOrderCreationTest extends BaseTest{
	
	String orderNum="";
	String createdOrders="";
	String[] storeRefCode;
	String[] writeOnRow;
	int numRetry = 0;
	int currBOPISOrder = 0;
	int count = 0;
	
	@Test
	public void openLoginPage() {
		ExtentTest logger = extent.startTest("Account Login - Bypass Captcha", "Account Login - Bypass Captcha");
		Instant start = Instant.now();
		
		try {
			navigate(logger);
			String email = inputData.get("Create").get("email");	
			String password = inputData.get("Create").get("password");	
			
			Browser.waitForElementIsClickable(logger, driver, homePage.tf_loginEmail);
			TextBox.getInstance().setText(logger, homePage.tf_loginEmail, email);
			TextBox.getInstance().setText(logger, homePage.tf_loginPassword, password);
			Browser.pause(logger, ".3");
			Browser.clickElementJSExecutor(logger, driver, homePage.btn_signIn);
			Browser.waitForBrowserToLoadCompletely(driver);	
			Browser.pause(logger, ".3");
			
			Instant finish = Instant.now();
			long timeElapsed = Duration.between(start, finish).toSeconds();
			System.out.println("\r\nElapsed Time - Login : " + timeElapsed + " sec\r\n");	
			
			if((driver.findElements(By.xpath(homePage.btn_captcha)).size()>0)||(driver.findElements(By.xpath("//*[contains(text(),'Welcome,')]")).size()!=1)) {
				System.out.println("UNSUCCESSFUL LOGIN or CAPTCHA IS DISPLAYED. WILL RETRY TO LOGIN");
				Browser.pause(logger, ".3");
				driver.close();
				driver.quit();
				openLoginPage();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			driver.close();
			driver.quit();
			openLoginPage();
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	

	//Method to write created orders to excel
	@SuppressWarnings("deprecation")
	public void writeOrders(String cellValue,int rowNum, int cellNum){		
		try {
			File src = new File(homePage.file_TrainingOrderList);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);			
			XSSFSheet sheet = xsf.getSheetAt(0);
			
			//write data
			if(sheet.getRow(rowNum).getCell(cellNum).getCellType()==Cell.CELL_TYPE_BLANK) {
				sheet.getRow(rowNum).getCell(cellNum).setCellValue(cellValue);
			} else {
				sheet.getRow(rowNum).getCell(cellNum).setCellValue(cellValue);
			}
			FileOutputStream fos = new FileOutputStream(src);		
			xsf.write(fos);		
			xsf.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Method to write number of created orders to excel
	@SuppressWarnings("deprecation")
	public void writeNumOfOrdersCreated(String cellValue,int rowNum, int cellNum){		
		try {
			File srce = new File(homePage.file_TrainingOrderCreated);			
			FileInputStream fist = new FileInputStream(srce);		
			XSSFWorkbook hsf = new XSSFWorkbook(fist);			
			XSSFSheet sheet = hsf.getSheetAt(0);
			
			//write data
			if(sheet.getRow(rowNum).getCell(cellNum).getCellType()==Cell.CELL_TYPE_BLANK) {
				sheet.getRow(rowNum).getCell(cellNum).setCellValue(cellValue);
			} else {
				sheet.getRow(rowNum).getCell(cellNum).setCellValue(cellValue);
			}
			FileOutputStream fost = new FileOutputStream(srce);		
			hsf.write(fost);		
			hsf.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	//Method to write number of created orders to excel
	@SuppressWarnings("deprecation")
	public void resetTrainingInputSheet(){		
		ExtentTest logger = extent.startTest("Reset training Input Sheet", "Reset training Input Sheet");
		navigateResetTrainingInputSheet(logger);
		try {
			File srce = new File(homePage.file_TrainingOrderCreated);			
			FileInputStream fist = new FileInputStream(srce);		
			XSSFWorkbook hsf = new XSSFWorkbook(fist);			
			XSSFSheet sheet = hsf.getSheetAt(0);
			
			writeOnRow = homePage.txt_trainingStoresRow.split(",");
			
			//write data
			for (int i = 1; i < writeOnRow.length; i++) {
				int j = Integer.parseInt(writeOnRow[i]);
				if(sheet.getRow(j).getCell(9).getCellType()==Cell.CELL_TYPE_BLANK) {
					sheet.getRow(j).getCell(9).setCellValue("0");
				} else {
					sheet.getRow(j).getCell(9).setCellValue("0");
				}
			}
			FileOutputStream fost = new FileOutputStream(srce);		
			hsf.write(fost);		
			hsf.close();	
			logger.log(LogStatus.INFO, "Training Input Sheet reset completed");
			System.out.println("Training Input Sheet reset completed");
			driver.close();
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	
	@Test
	public void createTrainingOrders() {
		ExtentTest logger = extent.startTest("Create Training Orders", "Create Training Orders");	
		
		int i = Integer.parseInt(inputData.get("Create").get("createdOrders"));
		int numOfOrders = Integer.parseInt(inputData.get("Create").get("ordersToCreate"));
		
		if(i<currBOPISOrder) {
			i=currBOPISOrder;
		} else if(i==numOfOrders) {
			currBOPISOrder = i;	
			System.out.println("Number of BOPIS orders to create is already completed. Set createdOrders column on input sheet to 0 to rerun script.");
			logger.log(LogStatus.PASS, "Number of BOPIS orders to create is already completed. Set createdOrders column on input sheet to 0 to rerun script.");
		}
		
		storeRefCode = homePage.txt_trainingStores.split(",");
		writeOnRow = homePage.txt_trainingStoresRow.split(",");
		
		String refCode = inputData.get("Create").get("refCode");
		Instant startOrderCreate = Instant.now();
		
		try {
			while (i < numOfOrders) {
				switch(i) {
					case 0:
						//BOPIS	Store	Prepaid	Credit Card	Delivered	Return/Refund
						createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"Prepaid",false);
						break;
					case 1:
						//Store	POP	Cash	Delivered
						createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"COD",false);
						break;
					case 2:
						//Store	Prepaid	Credit Card	Failed - Customer did not pick up
						//createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"Prepaid",false);
						break;
					case 3:
						//BOPIS	Warehouse	Prepaid	Credit Card	Delivered
						createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"Prepaid",false);
						break;
					case 4:
						//BOPIS	Warehouse	POP	Cash	Delivered
						createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"COD",false);
						break;
					case 5:
						//BOPIS	Warehouse	POP	Cash	Failed - item not available
						//createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"COD",false);
						break;
					case 6:
						//BOPIS	Warehouse + Store	Prepaid	Credit Card	Delivered
						createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"Prepaid",true);
						break;
					case 7:
						//BOPIS	Warehouse + Store	POP	Cash	Delivered
						createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"COD",true);
						break;
					case 8:
						//BOPIS	Warehouse + Store	POP	Credit Card + Cash	Failed - Customer did not pick up
						//createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"COD",true);
						break;
					case 9:
						//BOPIS	Warehouse + Store	Prepaid	Credit Card	Failed - item not available
						//createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"Prepaid",true);
						break;
					case 10:
						//BOPIS	Store	Prepaid	Credit Card	Partial - Delivered
						//createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"Prepaid",false);
						break;
					case 11:
						//BOPIS	Warehouse	POP	Debit	Partial - Delivered
						//createOrder_BOPIS(i,Integer.parseInt(writeOnRow[getIndexOf(refCode)]),"COD",false);
						break;
				}	
				i++;
			}
			Instant finishOrderCreate = Instant.now();
			long timeElapsedBOSTC = Duration.between(startOrderCreate, finishOrderCreate).toSeconds();
			System.out.println("Total Elapsed Time : " + timeElapsedBOSTC + " sec");	
			System.out.println("===============================================");			
			driver.close();
			driver.quit();
		} catch (Exception e) {
			i--;
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
		} 
	}
	
	
	public void check_stuckLoading() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		if(driver.findElements(By.xpath(homePage.btn_continueLoading)).size()>0) {
			System.out.println("Loading is taking too long. Refreshing the page");
			driver.navigate().refresh();
			Browser.waitForBrowserToLoadCompletely(driver);
			if(driver.findElements(By.xpath(homePage.txt_btn_continue)).size()>0) {
				js.executeScript("arguments[0].click();", homePage.btn_continue);
			}		
		}	
	}
	
	public int getIndexOf(String strToFind) {
		int index = 0;
		for (int i = 0; i < storeRefCode.length; i++) {
			if(strToFind.equals(storeRefCode[i])) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * This method is for checking if carft is empty
	 * 
	 * @param logger
	 */
	public void check_cartEmpty(ExtentTest logger) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		if(driver.findElements(By.xpath(homePage.err_cartEmpty)).size()>0) {
			System.out.println("Cart is Empty. Refreshing the page");
			driver.navigate().refresh();
			Browser.waitForBrowserToLoadCompletely(driver);
			js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_addToCart);
			Browser.waitForElementIsVisible(logger, driver, homePage.btn_addToCart);
			js.executeScript("arguments[0].click();", homePage.btn_addToCart);
			Browser.pause(logger, "1");	
		}
	}
	
	public void selectPickUpStore(ExtentTest logger) throws Exception {
		String pickupStore = inputData.get("Create").get("pickupStore").toLowerCase().replaceAll(" ", "");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		//Populate Contact information and Shipping address	
		Browser.waitForElementIsVisible(logger, driver, homePage.btn_pickUp);
		Browser.clickElementJSExecutor(logger, driver, homePage.btn_pickUp);				
		Browser.pause(logger, "2");
		
		if(numRetry==2) {
			System.out.println("Max reload retries reached. Re-running test");
			numRetry = 0;
			throw new Exception("Max reload retries reached. Re-running test");
		} else if(driver.findElements(By.xpath(homePage.txt_showStoreNearLoc)).size()<=0) {
			System.out.println("Show Store Near Loc is not displayed. Refreshing the page");
			driver.navigate().refresh();
			Browser.pause(logger, "2");
			numRetry++;
			selectPickUpStore(logger);
		} else {
			js.executeScript("arguments[0].click();", homePage.btn_showStoreNearLoc);
			Browser.waitForLoading(logger, driver);
			Browser.pause(logger, "2");
			numRetry = 0;
		}
		
		if(driver.findElements(By.xpath(homePage.txt_genericPickupStoreXpath)).size()<=0) {
			System.out.println("Loading Pickup store is taking too long. Refreshing the page");
			driver.navigate().refresh();
			Browser.pause(logger, "2");
			numRetry++;
			selectPickUpStore(logger);
		} else {
			java.util.List<WebElement> storeToFind = driver.findElements(By.xpath(homePage.txt_genericPickupStoreXpath));
			for(int i=0; i < storeToFind.size(); i++) {
				if(driver.findElements(By.xpath(homePage.txt_genericPickupStoreXpath)).get(i)
				    .getAttribute("data-location-title").replaceAll(" ", "").equalsIgnoreCase(pickupStore)) {
					js.executeScript("arguments[0].scrollIntoView(true);", storeToFind.get(i));	
					Button.getInstance().click(logger, storeToFind.get(i));
				} else if((i==storeToFind.size()-1)&&(driver.findElements(By.xpath("//*[contains(text(),'"+inputData.get("Create").get("pickupStore")+"')]")).size()>0)){
					Browser.clickElementJSExecutor(logger, driver, driver.findElement(By.xpath("//*[contains(text(),'"+inputData.get("Create").get("pickupStore")+"')]")));
				} 
			}	
			Button.getInstance().click(logger, homePage.btn_continue);	
			numRetry = 0;
		}
	}
	
	public void check_ifOrderNumDisplayed(ExtentTest logger) {
		Browser.waitForElementIsVisible(logger, driver, homePage.txt_orderNumber);
		if(homePage.txt_orderNumber.getText().length()==0) {
			driver.navigate().refresh();
			Browser.waitForBrowserToLoadCompletely(driver);
			check_ifOrderNumDisplayed(logger);
		} else {
			System.out.println("Order# generated");
		}
	}
	
//***********************************************************************************************************************************************//	
	
	//Method for creating BOPIS order
	public void createOrder_BOPIS(int numOrderCreated, int writeOnRowCnt,String paymentMode,boolean mixFulfilled) {
		ExtentTest logger = extent.startTest("Create BOPIS order", "Create BOPIS order");
		
		if((numOrderCreated==0)||(numOrderCreated==1)||(numOrderCreated==2)||(numOrderCreated==10)) {
			browser.navigate(logger, inputData.get("navigate").get("SKU").toString());
			System.out.println("Creating order with Store item");
		} else {
			browser.navigate(logger, inputData.get("Create").get("SKU").toString());
			System.out.println("Creating order with WH item");
		}
		
		Browser.waitForElementIsVisible(logger, driver, homePage.txt_quantity);
		Browser.waitForBrowserToLoadCompletely(driver);
		int numOfOrders = Integer.parseInt(inputData.get("Create").get("ordersToCreate"));
		System.out.println("\r\n===============================================\r\nCreating BOPIS - "
							+ paymentMode + "\r\nMixfulfilled: " + mixFulfilled + "\r\n");
		
		try {	
			//Pull test data from excel sheet
			String refCode = inputData.get("Create").get("refCode");
			String custAddress = inputData.get("Create").get("customerAddress");
				
			//Add item to Cart
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Browser.waitForElementIsVisible(logger, driver, homePage.btn_addToCart);
			js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_addToCart);
			js.executeScript("arguments[0].click();", homePage.btn_addToCart);
			Browser.pause(logger, "1");				
			
//			if((numOrderCreated==0)||(numOrderCreated==1)||(numOrderCreated==2)||(numOrderCreated==3)||(numOrderCreated==4)||(numOrderCreated==5)||(numOrderCreated==10)||(numOrderCreated==11)) {	
//				//Add another item to Cart
//				Browser.waitForElementIsVisible(logger, driver, homePage.txt_addquantity);
//				Button.getInstance().click(logger, homePage.txt_addquantity);
//				Browser.pause(logger, "1");	
//			}
			
			if(mixFulfilled==true) {
				//Add mix item to Cart
				browser.navigate(logger, inputData.get("navigate").get("SKU").toString());
				Browser.waitForBrowserToLoadCompletely(driver);
				
				//Add item to Cart
				js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_addToCart);
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_addToCart);
				js.executeScript("arguments[0].click();", homePage.btn_addToCart);
				//Browser.pause(logger, "1");	
			}
				
			Browser.waitForElementIsClickable(logger, driver, homePage.btn_reviewCart);
			check_cartEmpty(logger);
			Button.getInstance().click(logger, homePage.btn_reviewCart);
			Browser.waitForElementIsVisible(logger, driver, homePage.btn_checkOut);
			//Browser.pause(logger, "1");		
				
			js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_checkOut);
			Browser.waitForElementIsClickable(logger, driver, homePage.btn_checkOut);
			Browser.clickElementJSExecutor(logger, driver, homePage.btn_checkOut);

			//Populate Contact information and Shipping address	
			check_stuckLoading();
			
			selectPickUpStore(logger);	
						
			if(paymentMode=="COD") {
			//select COD as payment method
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_cashOnDelivery);
				RadioButton.getInstance().click(logger, homePage.btn_cashOnDelivery);
			} 
			
			//select billing address
			Browser.waitForElementIsVisible(logger, driver, homePage.dd_billingAddress);
			Dropdown.getInstance().selectByPartOfTextIgnoreCase(logger, driver, homePage.dd_billingAddress, custAddress);
			//Browser.pause(logger, "1.3");	
			Browser.waitForElementIsClickable(logger, driver, homePage.btn_continue);
			Button.getInstance().click(logger, homePage.btn_continue);
			Browser.waitForBrowserToLoadCompletely(driver);
			
			if(paymentMode=="Prepaid") {
				System.out.println("Prepaid order waiting for Paymongo page to load");
				payMongo.completePayment(logger,driver);	
			} else {
				System.out.println("Non-prepaid order waiting for order#");
			}
			
			if(driver.findElements(By.xpath(homePage.lbl_orderBeingProcess)).size()< 1) {
				System.out.println("Your order’s being processed IS NOT DISPLAYED ");
				driver.navigate().refresh();
				Browser.waitForBrowserToLoadCompletely(driver);
				check_ifOrderNumDisplayed(logger);
			} else {
				System.out.println("Waiting for Order# ...");
				driver.navigate().refresh();
				Browser.waitForBrowserToLoadCompletely(driver);
				check_ifOrderNumDisplayed(logger);
			}
		
			//get order number
			Browser.waitForElementIsVisible(logger, driver, homePage.txt_orderNumber);
			orderNum = homePage.txt_orderNumber.getText().substring(homePage.txt_orderNumber.getText().indexOf(" ")+1, homePage.txt_orderNumber.getText().length());
			logger.log(LogStatus.PASS, "BOPIS Order created: " + orderNum + "<br>");
			createdOrders = createdOrders + orderNum.replace("#","PX")+"D" + "\r\n";
			Browser.captureScreenShot(logger, driver, orderNum);
			
			numOrderCreated++;
					
			//print created orders in console for monitoring
			System.out.println("\r\nBOPIS - Training Orders Created ("+ numOrderCreated + "/" + numOfOrders + ") : \r\n" + createdOrders);
			
			//write created order in excel	
			
			writeOrders(orderNum.replace("#","PX")+"D",numOrderCreated,getIndexOf(refCode));
			writeNumOfOrdersCreated(String.valueOf(numOrderCreated),writeOnRowCnt,9);	
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			currBOPISOrder = numOrderCreated;
			if(currBOPISOrder!=numOfOrders) {
				driver.close();
				driver.quit();
				System.out.println("Error encountered while creating BOSTC order. Re-running test");
				openLoginPage();
				createTrainingOrders();
			}
		} 
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);	
	}
	// End of method for creating BOPIS order
	
	
}
