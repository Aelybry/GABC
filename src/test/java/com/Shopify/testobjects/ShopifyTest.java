package com.Shopify.testobjects;

import java.awt.Dimension;
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.openqa.selenium.NoSuchSessionException;
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
	String orderNum="";
	int currBOSTCOrder = 0;
	int currBOPISOrder = 0;
	int numRetry = 0;
	Instant startBOSTC;
	Instant startBOPIS;
	long timeElapsedBOSTC;
	long timeElapsedBOPIS;
	boolean isOxyItem = false;
	boolean isPenItem = false;
	boolean isBocuItem = false;
	
	@Test
	public void openLoginPage() {
		ExtentTest logger = extent.startTest("Account Login - Bypass Captcha", "Account Login - Bypass Captcha");
		Instant start = Instant.now();
		int j=0;
		
		try {
			navigate(logger);
			String email = inputData.get("Create").get("email");	
			String password = inputData.get("Create").get("password");	
			
			if((driver.findElements(By.xpath(homePage.btn_bocuPassword)).size()>0)) {
				Browser.waitForElementIsClickable(logger, driver, homePage.tf_password);
				TextBox.getInstance().setTextAndEnter(logger, homePage.tf_password, homePage.txt_password);
				Browser.pause(logger, ".3");
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_user);
				Button.getInstance().click(logger, homePage.btn_user);
			}
			
			check_brand();
			
			Browser.waitForElementIsClickable(logger, driver, homePage.tf_loginEmail);
			TextBox.getInstance().setText(logger, homePage.tf_loginEmail, email);
			TextBox.getInstance().setText(logger, homePage.tf_loginPassword, password);
				
			Browser.pause(logger, ".3");
			Button.getInstance().click(logger, homePage.btn_signIn);
			Browser.waitForBrowserToLoadCompletely(driver);	
			Browser.pause(logger, ".3");
			
			Instant finish = Instant.now();
			long timeElapsed = Duration.between(start, finish).toSeconds();
			System.out.println("\r\nElapsed Time - Login : " + timeElapsed + " sec\r\n");	
			
			if((driver.findElements(By.xpath(homePage.btn_captcha)).size()>0)
					||(driver.findElements(By.xpath("//*[contains(text(),'My Dashboard')]")).size()!=1)) {
				System.out.println("CAPTCHA IS DISPLAYED. WILL RETRY TO LOGIN");
				Browser.pause(logger, ".3");
				driver.close();
				driver.quit();
				openLoginPage();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	
	//Method to write created orders to excel
	@SuppressWarnings("deprecation")
	public void writeOrders(String cellValue,int rowNum, int cellNum){		
		try {
			File src = new File(homePage.file_OrderList);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);	
			XSSFSheet sheet;
			if(isPenItem==true) {
				sheet = xsf.getSheetAt(0);
			} else if(isOxyItem==true){
				sheet = xsf.getSheetAt(1);
			} else {
				sheet = xsf.getSheetAt(2);
			}
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
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void writeOrdersCreationTime(long cellValue,int rowNum, int cellNum){		
		try {
			File src = new File(homePage.file_OrderList);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);			
			XSSFSheet sheet;
			if(isPenItem==true) {
				sheet = xsf.getSheetAt(0);
			} else if(isOxyItem==true){
				sheet = xsf.getSheetAt(1);
			} else {
				sheet = xsf.getSheetAt(2);
			}
			
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
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//Method to write number of created orders to excel
	@SuppressWarnings("deprecation")
	public void writeNumOfOrdersCreated(String cellValue,int rowNum, int cellNum){		
		try {
			File srce = new File(homePage.file_OrderCreated);			
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
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
/*	@Test
	//Test BOSTC order fulfilled by Store
	public void createOrder_BOSTC_Store() {
			int ordersToCreate = Integer.parseInt(inputData.get("Create").get("ordersToCreate"));
			createOrder_BOSTC("Store",ordersToCreate);	
	}
	// End of method for Create BOSTC order fulfilled by Store
*/	
	@Test
	//Test BOSTC order fulfilled by WH
	public void createOrder_BOSTC_WH() {
			int ordersToCreate = Integer.parseInt(inputData.get("Create").get("ordersToCreate"));
			createOrder_BOSTC("WH",ordersToCreate);
	}
	// End of method for Create BOSTC order fulfilled by Store
	
	@Test
	//Test BOPIS order fulfilled by Store
	public void createOrder_BOPIS_Store() {
			int ordersToCreate = Integer.parseInt(inputData.get("Create").get("ordersToCreate"));
			createOrder_BOPIS("Store",ordersToCreate);			
	}
	// End of method for Create BOSTC order fulfilled by Store
	
	@Test
	//Test BOPIS order fulfilled by WH
	public void createOrder_BOPIS_WH() {
			int ordersToCreate = Integer.parseInt(inputData.get("Create").get("ordersToCreate"));
			createOrder_BOPIS("WH",ordersToCreate);
	}
	// End of method for Create BOPIS order fulfilled by Store
	
	public void check_brand() {
		if(inputData.get("navigate").get("SKU").contains("penshoppe")) {
			isPenItem=true;
			System.out.println("Penshoppe item");
		}	else if(inputData.get("navigate").get("SKU").contains("penshoppe")) {
			isOxyItem=true;
			System.out.println("Oxygn item");
		}   else {
			isBocuItem=true;
			System.out.println("BOCU item");
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
	
	public void selectPickUpStore(ExtentTest logger) throws Exception {
		String pickupStore = inputData.get("Create").get("pickupStore");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		//Populate Contact information and Shipping address	
		Browser.waitForElementIsVisible(logger, driver, homePage.btn_pickUp);
		Button.getInstance().click(logger, homePage.btn_pickUp);				
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
		
		if(driver.findElements(By.xpath(homePage.txt_containsPrefix + pickupStore + homePage.txt_containsSuffix)).size()<=0) {
			System.out.println("Loading Pickup store is taking too long. Refreshing the page");
			driver.navigate().refresh();
			Browser.pause(logger, "2");
			numRetry++;
			selectPickUpStore(logger);
		} else {
			WebElement storeToFind = driver.findElement(By.xpath(homePage.txt_containsPrefix + pickupStore + homePage.txt_containsSuffix));
			js.executeScript("arguments[0].scrollIntoView(true);", storeToFind);	
			Button.getInstance().click(logger, storeToFind);	
			Button.getInstance().click(logger, homePage.btn_continue);	
			numRetry = 0;
		}
	}
	
	
	//Method for creating BOSTC orders
	public void createOrder_BOSTC(String fulfiller, int numOfOrders) {
		ExtentTest logger = extent.startTest("Creating BOSTC order fulfilled by "+ fulfiller, "Creating BOSTC order fulfilled by "+ fulfiller);
		startBOSTC = Instant.now();
		try {
			int i = Integer.parseInt(inputData.get("Create").get("createdOrders"));
			String billingAddress = inputData.get("Create").get("address");
			String rndQty = inputData.get("Create").get("rndQty");
			//Pull test data from excel sheet
			String createdOrders = "";
			if(i<currBOSTCOrder) {
				i=currBOSTCOrder;
			} else if(i==numOfOrders) {
				currBOSTCOrder = i;
				System.out.println("Number of BOSTC orders to create is already completed");
				driver.close();
				driver.quit();
				logger.log(LogStatus.PASS, "Number of BOSTC orders to create is already completed");
			}
			while (i < numOfOrders) {
				Instant startCreateBOSTC = Instant.now();
				browser.navigate(logger, inputData.get("navigate").get("SKU").toString());	
				
				JavascriptExecutor js = (JavascriptExecutor) driver;
				
				//randomize quantity
				if(rndQty.equalsIgnoreCase("Yes")) {
					int min_num = 1;
					int max_num = 10;
					int rand_num = (int) (Math.random() * ( max_num - min_num ));
					if(rand_num>1) {
						for(int q=1;q<rand_num;q++)
							js.executeScript("arguments[0].click();", homePage.btn_addquantity);
					}
				}
				
				//Add item to Cart
				js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_addToCart);
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_addToCart);
				js.executeScript("arguments[0].click();", homePage.btn_addToCart);
				Browser.pause(logger, "1");	
				
				check_cartEmpty(logger);
				
				//Increase item quantity to itemQty set on input sheet
				int itemQty = Integer.parseInt(inputData.get("Create").get("itemQty"));
				for(int q=1;q<itemQty;q++) {
					Browser.waitForElementIsVisible(logger, driver, homePage.txt_addquantity);
					Button.getInstance().click(logger, homePage.txt_addquantity);
					Browser.pause(logger, "1");	
				}
		
				
				Browser.waitForElementIsClickable(logger, driver, homePage.btn_reviewCart);
				Button.getInstance().click(logger, homePage.btn_reviewCart);
				Browser.pause(logger, "1");	
				
				js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_checkOut);
				Browser.waitForElementIsClickable(logger, driver, homePage.btn_checkOut);
				js.executeScript("arguments[0].click();", homePage.btn_checkOut);
				
				Browser.waitForElementIsVisible(logger, driver, homePage.dd_shippingAddress);
				Dropdown.getInstance().selectByVisibleText(logger, homePage.dd_shippingAddress, billingAddress);
				Browser.pause(logger, "1");	
				
				check_stuckLoading();	
				
				//Click Continue to Shipping
				js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_continue);
				Button.getInstance().click(logger, homePage.btn_continue);
				Browser.pause(logger, "2");	
							
				//Click Continue to Payment on Order Summary Screen
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_selectCourierJT);
				if(driver.findElements(By.xpath(homePage.icn_loadingCourier)).size()>0) {
					System.out.println("Stuck loading list of courier will refesh the page");
					driver.navigate().refresh();
					Browser.pause(logger, "2");
				} 
				Button.getInstance().click(logger, homePage.btn_selectCourierJT);
				Browser.pause(logger, "1");	
				Button.getInstance().click(logger, homePage.btn_continue);
				
				check_stuckLoading();	

				//select COD as payment method
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_cashOnDeliveryBOSTC);
				RadioButton.getInstance().click(logger, homePage.btn_cashOnDeliveryBOSTC);
				Button.getInstance().click(logger, homePage.btn_continue);
				Browser.pause(logger, "2");
				
				//get order number			
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
				//get date and time  created
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
				LocalDateTime now = LocalDateTime.now();  
				
				String orderNum = homePage.txt_orderNumber.getText().substring(homePage.txt_orderNumber.getText().indexOf(" ")+1, homePage.txt_orderNumber.getText().length());
				logger.log(LogStatus.PASS, "BOTSC Order created: " + orderNum + "<br>");
				createdOrders = createdOrders + orderNum + "\r\n";
				Browser.captureScreenShot(logger, driver, orderNum);
				
				//increment until number of orders to be created is completed
				i++;
				
				//print created orders in console for monitoring
				System.out.println("\r\nBOSTC - " + fulfiller + " Orders Created ("+ i + "/" + numOfOrders + ") : \r\n" + createdOrders);
					
				/*check current session time     
				*SHOPIFY auto logs out account after an 1 hour even when the account have activities
				*/
				Instant currRunningTimeBOSTC = Instant.now();
				long currElapsedBOSTC = Duration.between(startBOSTC, currRunningTimeBOSTC).toSeconds();
				long orderCreationTimeBOSTC = Duration.between(startCreateBOSTC, currRunningTimeBOSTC).toSeconds();
				System.out.println("Current Session Elapsed Time : " + currElapsedBOSTC + " sec\r\n");
				logger.log(LogStatus.PASS, "Order creation time : " + orderCreationTimeBOSTC + "<br>");
				
				//write created order in excel
				if(fulfiller=="Store") {	
					//write order number
					writeOrders(orderNum,i,0);
					//write order creation time
					writeOrdersCreationTime(orderCreationTimeBOSTC,i,1);
					//Write date and time created 
					writeOrders(dtf.format(now),i,15);
					//write current number or orders already created
					writeNumOfOrdersCreated(String.valueOf(i),2,7);
				} else {
					//write order number
					writeOrders(orderNum,i,2);
					//write order creation time
					writeOrdersCreationTime(orderCreationTimeBOSTC,i,3);
					//Write date and time created 
					writeOrders(dtf.format(now),i,16);
					//write c   urrent number or orders already created
					if(isOxyItem==true) {
						writeNumOfOrdersCreated(String.valueOf(i),18,7);
					} else {
						writeNumOfOrdersCreated(String.valueOf(i),6,7);
					}
				}
				
				currBOSTCOrder = i;
				
				if(currElapsedBOSTC>=3600&&i!=numOfOrders) {
					System.out.println("Shopify Logs-out session after 1 hour. Will close current chromedriver session and relauch");
					driver.close();
					driver.quit();
					timeElapsedBOSTC = timeElapsedBOSTC + currElapsedBOSTC;
					openLoginPage();
					createOrder_BOSTC(fulfiller,numOfOrders);
					break;
				}
				//end of checking current session time
				
				if(i==numOfOrders) {
					System.out.println("===============================================");
					System.out.println("ORDER CREATION COMPLETED ");
					driver.close();
					driver.quit();
					Instant finishBOSTC = Instant.now();
					timeElapsedBOSTC = timeElapsedBOSTC + Duration.between(startBOSTC, finishBOSTC).toSeconds();
					System.out.println("Total Elapsed Time : " + timeElapsedBOSTC + " sec");
					System.out.println("===============================================");
					break;
				}
			}		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
		} finally {
			if(currBOSTCOrder!=numOfOrders) {
				driver.close();
				driver.quit();
				System.out.println("Error encountered while creating BOSTC order. Re-running test");
				openLoginPage();
				createOrder_BOSTC(fulfiller,numOfOrders);
			} else {
				extent.endTest(logger);
				extent.flush();
				Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);	
			}
		}
	}
	//End of Method for creating BOSTC orders
	

//***********************************************************************************************************************************************//	
	
	//Method for creating BOPIS order
	public void createOrder_BOPIS(String fulfiller, int numOfOrders) {
		ExtentTest logger = extent.startTest("Create BOPIS order fulfilled by " + fulfiller, "Create BOPIS order fulfilled by " + fulfiller);
		startBOPIS = Instant.now();
		try {	
			int i = Integer.parseInt(inputData.get("Create").get("createdOrders"));
			String custAddress = inputData.get("Create").get("address");
			//Pull test data from excel sheet
			String createdOrders = "";
			if(i<currBOPISOrder) {
				i=currBOPISOrder;
			} else if(i==numOfOrders) {
				currBOPISOrder = i;	
				System.out.println("Number of BOPIS orders to create is already completed");
				driver.close();
				driver.quit();
				logger.log(LogStatus.PASS, "Number of BOPIS orders to create is already completed");
			}
			while (i < numOfOrders) {	
				Instant startCreateBOPIS = Instant.now();
				browser.navigate(logger, inputData.get("navigate").get("SKU").toString());
				Browser.waitForElementIsVisible(logger, driver, homePage.txt_quantity);
				
				//Add item to Cart
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_addToCart);
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_addToCart);
				js.executeScript("arguments[0].click();", homePage.btn_addToCart);
				Browser.pause(logger, "1");	
				
							
				Browser.waitForElementIsClickable(logger, driver, homePage.btn_reviewCart);
				check_cartEmpty(logger);
				
				//Increase item quantity to itemQty set on input sheet
				int itemQty = Integer.parseInt(inputData.get("Create").get("itemQty"));
				for(int q=1;q<itemQty;q++) {
					Browser.waitForElementIsVisible(logger, driver, homePage.txt_addquantity);
					Button.getInstance().click(logger, homePage.txt_addquantity);
					Browser.pause(logger, "1");	
				}
				
				Button.getInstance().click(logger, homePage.btn_reviewCart);
				Browser.pause(logger, "1");	
				
				js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_checkOut);
				Browser.waitForElementIsClickable(logger, driver, homePage.btn_checkOut);
				js.executeScript("arguments[0].click();", homePage.btn_checkOut);
				
				check_stuckLoading();
				
				selectPickUpStore(logger);		
						
				//select COD as payment method
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_cashOnDelivery);
				RadioButton.getInstance().click(logger, homePage.btn_cashOnDelivery);
				
				//select billing address
				Browser.waitForElementIsVisible(logger, driver, homePage.dd_billingAddress);
				Dropdown.getInstance().selectByVisibleText(logger, homePage.dd_billingAddress, custAddress);
				Browser.pause(logger, "1.3");	
				Button.getInstance().click(logger, homePage.btn_continue);
				Browser.pause(logger, "2");
	
				//get order number
				
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
				createdOrders = createdOrders + orderNum + "\r\n";
				Browser.captureScreenShot(logger, driver, orderNum);
				
				//increment until number of orders to be created is completed
				i++;
				
				//print created orders in console for monitoring
				System.out.println("\r\nBOPIS - " + fulfiller + " Orders Created ("+ i + "/" + numOfOrders + ") : \r\n" + createdOrders);
			
				/*check current session time
				*Shopify auto logs out account after an 1 hour even when the account have activities
				*/
				Instant currRunningTimeBOPIS = Instant.now();
				long currElapsedBOPIS = Duration.between(startBOPIS, currRunningTimeBOPIS).toSeconds();
				long orderCreationTimeBOPIS = Duration.between(startCreateBOPIS, currRunningTimeBOPIS).toSeconds();
				System.out.println("Current Session Elapsed Time : " + currElapsedBOPIS + " sec\r\n");
				logger.log(LogStatus.PASS, "Order creation time : " + orderCreationTimeBOPIS + "<br>");
				
				//write created order in excel
				if(fulfiller=="Store") {	
					//write order number on excel
					writeOrders(orderNum,i,4);
					//write order creation time
					writeOrdersCreationTime(orderCreationTimeBOPIS,i,5);
					//write current number or orders already created
					if(isOxyItem==true) {
						writeNumOfOrdersCreated(String.valueOf(i),22,8);
					} else {
						writeNumOfOrdersCreated(String.valueOf(i),10,8);
					}
				} else {
					writeOrders(orderNum,i,7);
					//write order creation time
					writeOrdersCreationTime(orderCreationTimeBOPIS,i,8);
					//write current number or orders already created
					if(isOxyItem==true) {
						writeNumOfOrdersCreated(String.valueOf(i),26,8);
					} else if(isPenItem==true){
						writeNumOfOrdersCreated(String.valueOf(i),14,8);
					} else {
						writeNumOfOrdersCreated(String.valueOf(i),30,8);
					}
				}	
				
				currBOPISOrder = i;				
				
				if(currElapsedBOPIS>=3600&&i!=numOfOrders) {
					System.out.println("Shopify Logs-out session after 1 hour. Will close current chromedriver session and relauch");
					driver.close();
					driver.quit();
					timeElapsedBOPIS = timeElapsedBOPIS + currElapsedBOPIS;
					openLoginPage();
					createOrder_BOPIS(fulfiller,numOfOrders);
					break;
				}
				//end of checking current session time
				
				if(i==numOfOrders) {
					System.out.println("===============================================");
					System.out.println("ORDER CREATION COMPLETED ");
					driver.close();
					driver.quit();
					Instant finishBOPIS = Instant.now();
					timeElapsedBOPIS = timeElapsedBOPIS + Duration.between(startBOPIS, finishBOPIS).toSeconds();
					System.out.println("Total Elapsed Time : " + timeElapsedBOPIS + " sec");
					System.out.println("===============================================");
					break;
				}				
			}		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
		} finally {
			if(currBOPISOrder!=numOfOrders) {
				System.out.println("Error encountered while creating BOPIS order. Re-running test");
				driver.close();
				driver.quit();
				openLoginPage();
				createOrder_BOPIS(fulfiller,numOfOrders);
			} else {
				extent.endTest(logger);
				extent.flush();
				Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);	
			}
		}			
	}
	// End of method for creating BOPIS order
	

	
}
