package com.Shopify.testobjects;

import java.awt.Dimension;
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
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
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.GABC.core.BaseTest;
import com.GABC.core.Browser;
import com.GABC.pageobjects.SiteManagerPage;
import com.GABC.webelements.AlertsHandler;
import com.GABC.webelements.Button;
import com.GABC.webelements.Dropdown;
import com.GABC.webelements.Link;
import com.GABC.webelements.RadioButton;
import com.GABC.webelements.TextBox;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class SiteManagerTest extends BaseTest{
	String orderNum="";
	String createdOrders="";
	String[] storeRefCode;
	String[] writeOnRow;
	int numRetry = 0;
	int currBOPISOrder = 0;
	int count = 0;
	int ordersToCheck = 0;
	
	public void loginSiteManager(){  
		ExtentTest logger = extent.startTest("Log-in to Site Manager", "Log-in to Site Manager");
		try {			
			// open site manager
			navigateSM(logger);
			// pull login credentisla from inputsheet
			String username = inputData.get("Create").get("Username");
			String password = inputData.get("Create").get("Password");
			siteManagerPage.loginSM(logger, username, password);
			
		} catch (Exception e) {
			Browser.captureScreenShot(logger, driver, "Error");
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			e.printStackTrace();
			closeBrowser();
			loginSiteManager();
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);	
	} 	
	
	//Test check shipping orders - EOM order cleanup
	@Test
	public void eomCleanupCount() {
		ExtentTest logger = extent.startTest("EOM Cleanup Count Shipping Orders", "EOM Cleanup Count Shipping Orders");
		try {
			loginSiteManager();
			int numOrdersCleanedUp = Integer.parseInt(inputData.get("Create").get("ordersCleanedUp"));
			File src = new File(siteManagerPage.eomCleanupCountDir);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);	
			XSSFSheet sheet = xsf.getSheetAt(0);
			ordersToCheck = sheet.getLastRowNum();
			System.out.println("Initial orderstocheck value "  + ordersToCheck);
			getLastRow(sheet, 0, ordersToCheck);
			FileOutputStream fos = new FileOutputStream(src);
			xsf.write(fos);
			xsf.close();
			
			System.out.println("After Getlastrow orderstocheck value "  + ordersToCheck);
			Link.getInstance().click(logger, siteManagerPage.lnk_Commerce);
			Link.getInstance().click(logger, siteManagerPage.lnk_CommerceOrders);
			Link.getInstance().click(logger, siteManagerPage.lnk_CommerceOrdersManageExisting);

			
			for(int i=numOrdersCleanedUp+1;i<=ordersToCheck;i++) {
				String orderNum = String.valueOf(sheet.getRow(i).getCell(0).getStringCellValue());
				TextBox.getInstance().setTextAndEnter(logger, siteManagerPage.tf_orderID, orderNum);
				
				if(driver.findElements(By.xpath("//*[contains(text(),'"+ orderNum +"')]")).size()>0) {
					Link.getInstance().click(logger, siteManagerPage.lnk_orderStatus);
					Browser.waitForBrowserToLoadCompletely(driver);
					
					if(siteManagerPage.lnk_orderStatus.getText().equals("Order Complete")) {
						System.out.println("Order already completed. Will skip order " + orderNum);
						writeOnExcelSheet("Skipped", i, 1,siteManagerPage.eomCleanupCountDir);
						writeOnExcelSheet(String.valueOf(i), 2, 21,siteManagerPage.siteManagerDir);
						continue;
					} else if(driver.findElements(By.xpath("//*[contains(text(),'Ordered')]")).size()>0
							&&driver.findElements(By.xpath("//*[contains(text(),'Cancelled ')]")).size()>0
							&&driver.findElements(By.xpath("//*[contains(text(),'Fulfilled ')]")).size()>0){
						System.out.println(orderNum + " Order is already fulfilled / cancelled");
						writeOnExcelSheet("Skipped", i, 1,siteManagerPage.eomCleanupCountDir);
						writeOnExcelSheet(String.valueOf(i), 2, 21,siteManagerPage.siteManagerDir);
					} else {
						System.out.println("WILL UPDATE ORDER SHIP ORDER DETAILS " + orderNum);
						Button.getInstance().click(logger, driver.findElement(By.xpath("//*[contains(text(),'"+ orderNum +"')]")));
						Browser.waitForBrowserToLoadCompletely(driver);
						driver.switchTo().frame("manageIframe");
						// Add additional (if else) Shipping and Payment Method combinations / condition as needed
						if(driver.findElements(By.xpath("//*[contains(text(),'Local Pickup')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Credit Card")){
							 updateShipOrderDetails(i,"PREPAID","INHOUSE Delivery");		
						} else if(driver.findElements(By.xpath("//*[contains(text(),'LBC EXPRESS INC. -PRE')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Credit Card")){
							 updateShipOrderDetails(i,"PREPAID","LBC EXPRESS INC. -PRE");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'FASTCARGO LOGISTICS CORPORATION -PRE')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Credit Card")){
							 updateShipOrderDetails(i,"PREPAID","FASTCARGO LOGISTICS CORPORATION -PRE");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'FAST FORWARD FREIGHT INC -PRE')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Credit Card")){
							 updateShipOrderDetails(i,"PREPAID","FAST FORWARD FREIGHT INC -PRE");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'WSI-PRE')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Credit Card")){
							 updateShipOrderDetails(i,"PREPAID","WSI-PRE");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'J&T-PRE')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Credit Card")){
							 updateShipOrderDetails(i,"PREPAID","J&T-PRE");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'ENTREGO FULFILLMENT SOLUTIONS INC. -PRE')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Credit Card")){
							 updateShipOrderDetails(i,"PREPAID","ENTREGO FULFILLMENT SOLUTIONS INC. -PRE");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'LBC EXPRESS INC. -COD')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Cash On Delivery")){
							 updateShipOrderDetails(i,"COD","LBC EXPRESS INC. -COD");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'FASTCARGO LOGISTICS CORPORATION -COD')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Cash On Delivery")){
							 updateShipOrderDetails(i,"COD","FASTCARGO LOGISTICS CORPORATION -COD");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'FAST FORWARD FREIGHT INC -COD')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Cash On Delivery")){
							 updateShipOrderDetails(i,"COD","FAST FORWARD FREIGHT INC -COD");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'WSI-COD')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Cash On Delivery")){
							 updateShipOrderDetails(i,"COD","WSI-COD");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'J&T-COD')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Cash On Delivery")){
							 updateShipOrderDetails(i,"COD","J&T-COD");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'ENTREGO FULFILLMENT SOLUTIONS INC. -COD')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Cash On Delivery")){
							 updateShipOrderDetails(i,"COD","ENTREGO FULFILLMENT SOLUTIONS INC. -COD");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'Local Pickup')]")).size()>0
								&&siteManagerPage.text_paymentMethod.getText().equals("Cash On Delivery")){
							 updateShipOrderDetails(i,"COD","INHOUSE Delivery");							 
						} else if(driver.findElements(By.xpath("//*[contains(text(),'Customer Pickup')]")).size()>0){
							 updateShipOrderDetails(i,"PICKUP IN STORE","Customer Pickup");							 
						} else {
							 System.out.println("UNHANDLED SHIPPING AND PAYMENT METHOD");
						}
					}
				} else {
					System.out.println("ORDER NOT FOUND");
				}
			}	
			closeBrowser();
		} catch (Exception e) {
			Browser.captureScreenShot(logger, driver, "Error");
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			e.printStackTrace();
			closeBrowser();
			eomCleanupCount();
		}
	}
	
	// method to update Ship Details for EOM order cleanup
	public void updateShipOrderDetails(int currOrdersChk, String carrier, String service){  
		ExtentTest logger = extent.startTest("Update Ship order Details", "Update Ship order Details");
		
		try {				
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.btn_shipOrder);
			Browser.waitForBrowserToLoadCompletely(driver);
			
			//item details
			Browser.waitForElementIsVisible(logger, driver, siteManagerPage.cb_chkAllItem);
			Browser.scrollElementIntoView(logger, driver, siteManagerPage.cb_chkAllItem);
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.cb_chkAllItem);
			Browser.waitForBrowserToLoadCompletely(driver);
			
			//Options
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.cb_genLabelsAndTracking);
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.cb_emailCustomer);
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.cb_sendReviewRequest);
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.cb_captureFunds);	
			Browser.waitForBrowserToLoadCompletely(driver);
			
			//Shipping
			Dropdown.getInstance().selectByVisibleText(logger, siteManagerPage.dd_carrier, carrier);
			Browser.waitForBrowserToLoadCompletely(driver);
			Dropdown.getInstance().selectByVisibleText(logger, siteManagerPage.dd_service, service);
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_HHmmss");  
		    Date date = new Date();  
			TextBox.getInstance().setText(logger, siteManagerPage.tf_trackingNumber, "A_" + formatter.format(date));
			
			//check if order have multiple items - will copy shipping details to all items if true
			if(siteManagerPage.lnk_copyToAll.isDisplayed()) {
				System.out.println("MULTIPLE ITEM ORDER - COPY SHIPPING DETAILS TO ALL ITEMS");
				Link.getInstance().click(logger, siteManagerPage.lnk_copyToAll);
			} else {
				System.out.println("SINGLE ITEM ORDER");
			}	
			
			Browser.scrollElementIntoView(logger, driver, siteManagerPage.btn_packAndSHipItems);
			Browser.pause(logger, "1");
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.btn_packAndSHipItems);
			Browser.waitForBrowserToLoadCompletely(driver);
			
			Browser.switchDefaultFrame(logger, driver);
			
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.btn_return);
			Browser.waitForBrowserToLoadCompletely(driver);
			
			checkiforderisCompleted(logger,currOrdersChk);
			writeOnExcelSheet("Done", currOrdersChk, 1,siteManagerPage.eomCleanupCountDir);
		} catch (Exception e) {
			Browser.captureScreenShot(logger, driver, "Error");
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			e.printStackTrace();
			closeBrowser();
			eomCleanupCount();
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);	
	} 
	
	// method to update Ship Details for EOM order cleanup
	public void checkiforderisCompleted(ExtentTest logger, int currOrdersChk){  

			Button.getInstance().click(logger, siteManagerPage.btn_searchOrderNumber);
			Browser.waitForBrowserToLoadCompletely(driver);
			if(siteManagerPage.lnk_orderStatus.getText().equals("Order Complete")) {
				System.out.println("Order "+ orderNum + "Status Updated");
				logger.log(LogStatus.PASS, "Order Status Updated");
				writeOnExcelSheet(String.valueOf(currOrdersChk), 2, 21, siteManagerPage.siteManagerDir);
			} else {
				System.out.println("ORDER STATUS WAS NOT UPDATED");
				logger.log(LogStatus.FAIL, "ORDER STATUS WAS NOT UPDATED");
				checkiforderisCompleted(logger,currOrdersChk);
			}
	} 


	public void takeInventoryScreenshot(int row,String invStatus){
		ExtentTest logger = extent.startTest("Take inventory screenshot " + invStatus, "Take inventory screenshot " + invStatus);
		
		try {	
			File src = new File(siteManagerPage.smokeTestDir);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);	
			XSSFSheet sheet = xsf.getSheetAt(0);
			String supplierID1 = String.valueOf(sheet.getRow(row).getCell(0).getNumericCellValue());		
			
			if(invStatus.equals("BeforeOrderPlacementWH")||invStatus.equals("WhileRemorsePeriodWH")||invStatus.equals("AfterRemorsePeriodWH")) {
				supplierID1 = siteManagerPage.warehouseSiteID;
			} else {
				supplierID1 = supplierID1.substring(0, supplierID1.length()-2);
			}
			
			String sku = String.valueOf(sheet.getRow(row).getCell(1).getNumericCellValue());
			sku = sku.substring(0, sku.length()-2).replaceAll("[.]", "");

			//navigate to Site Manager > Content >  Add/Edit >
			Link.getInstance().click(logger, siteManagerPage.lnk_content);
			Link.getInstance().click(logger, siteManagerPage.lnk_addEditProduct);
			TextBox.getInstance().setTextAndEnter(logger, siteManagerPage.txt_searachSKU, sku.substring(0, sku.length()-3));
			Browser.waitForElementIsClickable(logger, driver, siteManagerPage.lnk_inventoryInfo);
			Browser.clickElementJSExecutor(logger, driver,siteManagerPage.lnk_inventoryInfo);
			
			Browser.waitForLoading(logger, driver, siteManagerPage.icon_loadinggraphic);
			
			driver.switchTo().frame(siteManagerPage.iframe_inventory);
			Browser.waitForElementIsVisible(logger, driver, siteManagerPage.lbl_parentStyle);
			
			Browser.waitForElementIsClickable(logger, driver, siteManagerPage.btn_inventoryBySupplier);
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.btn_inventoryBySupplier);
			Browser.waitForElementIsClickable(logger, driver, siteManagerPage.btn_showDetails);
			Browser.clickElementJSExecutor(logger, driver, siteManagerPage.btn_showDetails);
			
			TextBox.getInstance().setTextAndEnter(logger, siteManagerPage.tf_agSupplierID, supplierID1);
			TextBox.getInstance().setTextAndEnter(logger, siteManagerPage.tf_agSKU, sku);
			 
			Browser.pause(logger, "1");
			
			if(driver.findElements(By.xpath(siteManagerPage.inventoryInfoEmptyCell)).size()>0) {
				Browser.waitForLoading(logger, driver, (WebElement) driver.findElement(By.xpath(siteManagerPage.inventoryInfoEmptyCell)));
			} else {
				System.out.println("Table is populated. Taking screenshot and write inv values on monitoring sheet");
			}
			
			Browser.captureScreenShot(logger, driver, supplierID1 + "_" + sku + "_" + invStatus + (row-1));	
			System.out.println(supplierID1 + "_" + sku + "_" + invStatus + (row-1) + ".jpg screenshot taken");
				
			writeSKUInventory(row, invStatus);
			
			//close Inventory info screen
			Browser.switchDefaultFrame(logger, driver);
			Browser.waitForElementIsClickable(logger, driver, siteManagerPage.btn_closeSM);
			Button.getInstance().click(logger, siteManagerPage.btn_closeSM);
				
			xsf.close();
			
		} catch (Exception e) {
			Browser.captureScreenShot(logger, driver, "Error");
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			e.printStackTrace();
			takeInventoryScreenshot(row, invStatus);
			//closeBrowser();	
		}
		logger.log(LogStatus.PASS, "Screenshot taken successfully" + "<br>");
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
		//closeBrowser();	
	}
	
	public void writeSKUInventory(int row, String invStatus){
		
		try {	
			File src = new File(siteManagerPage.smokeTestDir);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);	
			XSSFSheet sheet = xsf.getSheetAt(0);
			
			switch(invStatus) {
				case "BeforeOrderPlacement":
					//write sku inventory BeforeOrderPlacement on excel tracking sheet
					CellUtil.getCell(sheet.getRow(row), 4);
					CellUtil.getCell(sheet.getRow(row), 5);
					CellUtil.getCell(sheet.getRow(row), 6);
					CellUtil.getCell(sheet.getRow(row), 7);
					sheet.getRow(row).getCell(4).setCellValue(Integer.parseInt(siteManagerPage.text_onhandInventory.getText()));
					sheet.getRow(row).getCell(5).setCellValue(Integer.parseInt(siteManagerPage.text_reservedInventory.getText()));
					sheet.getRow(row).getCell(6).setCellValue(Integer.parseInt(siteManagerPage.text_externalReserveQty.getText()));
					sheet.getRow(row).getCell(7).setCellValue(Integer.parseInt(siteManagerPage.text_availableInventory.getText()));
					break;
					
				case "BeforeOrderPlacementWH":
					//write sku inventory BeforeOrderPlacement on excel tracking sheet
					CellUtil.getCell(sheet.getRow(row), 8);
					CellUtil.getCell(sheet.getRow(row), 9);
					CellUtil.getCell(sheet.getRow(row), 10);
					CellUtil.getCell(sheet.getRow(row), 11);
					sheet.getRow(row).getCell(8).setCellValue(Integer.parseInt(siteManagerPage.text_onhandInventory.getText()));
					sheet.getRow(row).getCell(9).setCellValue(Integer.parseInt(siteManagerPage.text_reservedInventory.getText()));
					sheet.getRow(row).getCell(10).setCellValue(Integer.parseInt(siteManagerPage.text_externalReserveQty.getText()));
					sheet.getRow(row).getCell(11).setCellValue(Integer.parseInt(siteManagerPage.text_availableInventory.getText()));
					break;
					
				case "WhileRemorsePeriod":
					//write sku inventory WhileRemorsePeriod on excel tracking sheet
					CellUtil.getCell(sheet.getRow(row), 12);
					CellUtil.getCell(sheet.getRow(row), 13);
					CellUtil.getCell(sheet.getRow(row), 14);
					CellUtil.getCell(sheet.getRow(row), 15);
					CellUtil.getCell(sheet.getRow(row), 16);

					sheet.getRow(row).getCell(12).setCellValue(Integer.parseInt(siteManagerPage.text_onhandInventory.getText()));
					sheet.getRow(row).getCell(13).setCellValue(Integer.parseInt(siteManagerPage.text_reservedInventory.getText()));
					sheet.getRow(row).getCell(14).setCellValue(Integer.parseInt(siteManagerPage.text_externalReserveQty.getText()));
					sheet.getRow(row).getCell(15).setCellValue(Integer.parseInt(siteManagerPage.text_availableInventory.getText()));
						
					//compare before order placement STORE inventory vs STORE inv while in remorse period.
					//There should be no change in store inventory yet
					if(sheet.getRow(row).getCell(12).getNumericCellValue()==sheet.getRow(row).getCell(4).getNumericCellValue()&&
					   sheet.getRow(row).getCell(13).getNumericCellValue()==sheet.getRow(row).getCell(5).getNumericCellValue()&&	
					   sheet.getRow(row).getCell(14).getNumericCellValue()==sheet.getRow(row).getCell(6).getNumericCellValue()&&
					   sheet.getRow(row).getCell(15).getNumericCellValue()==sheet.getRow(row).getCell(7).getNumericCellValue()){
						//if values match set test status to pass
						sheet.getRow(row).getCell(16).setCellValue("PASS");
					} else {
						sheet.getRow(row).getCell(16).setCellValue("FAIL");
					}
					break;
					
				case "WhileRemorsePeriodWH":
					//write sku inventory WhileRemorsePeriod on excel tracking sheet
					CellUtil.getCell(sheet.getRow(row), 17);
					CellUtil.getCell(sheet.getRow(row), 18);
					CellUtil.getCell(sheet.getRow(row), 19);
					CellUtil.getCell(sheet.getRow(row), 20);
					CellUtil.getCell(sheet.getRow(row), 21);

					sheet.getRow(row).getCell(17).setCellValue(Integer.parseInt(siteManagerPage.text_onhandInventory.getText()));
					sheet.getRow(row).getCell(18).setCellValue(Integer.parseInt(siteManagerPage.text_reservedInventory.getText()));
					sheet.getRow(row).getCell(19).setCellValue(Integer.parseInt(siteManagerPage.text_externalReserveQty.getText()));
					sheet.getRow(row).getCell(20).setCellValue(Integer.parseInt(siteManagerPage.text_availableInventory.getText()));
						
					//compare WH inventory before order placement vs WH inventory while in remorse period
					//Unavailable inventory should be increased 
					//Avaialble inventory be decreased
					if(sheet.getRow(row).getCell(17).getNumericCellValue()==sheet.getRow(row).getCell(8).getNumericCellValue()&&
					   sheet.getRow(row).getCell(18).getNumericCellValue()==sheet.getRow(row).getCell(9).getNumericCellValue()&&	
					   sheet.getRow(row).getCell(19).getNumericCellValue()==(sheet.getRow(row).getCell(10).getNumericCellValue()+1)&&
					   sheet.getRow(row).getCell(20).getNumericCellValue()==(sheet.getRow(row).getCell(11).getNumericCellValue()-1)){
						//if values match set test status to pass
						sheet.getRow(row).getCell(21).setCellValue("PASS");
					} else {
						sheet.getRow(row).getCell(21).setCellValue("FAIL");
					}
					break;
			
				case "AfterRemorsePeriod":
					//write sku inventory AfterRemorsePeriod on excel tracking sheet
					CellUtil.getCell(sheet.getRow(row), 3);
					CellUtil.getCell(sheet.getRow(row), 22);
					CellUtil.getCell(sheet.getRow(row), 23);
					CellUtil.getCell(sheet.getRow(row), 24);
					CellUtil.getCell(sheet.getRow(row), 25);
					CellUtil.getCell(sheet.getRow(row), 26);
					
					System.out.println("Writing iNVENTORY DATA . . . ");
					sheet.getRow(row).getCell(22).setCellValue(Integer.parseInt(siteManagerPage.text_onhandInventory.getText()));
					sheet.getRow(row).getCell(23).setCellValue(Integer.parseInt(siteManagerPage.text_reservedInventory.getText()));
					sheet.getRow(row).getCell(24).setCellValue(Integer.parseInt(siteManagerPage.text_externalReserveQty.getText()));
					sheet.getRow(row).getCell(25).setCellValue(Integer.parseInt(siteManagerPage.text_availableInventory.getText()));
					
					if(sheet.getRow(row).getCell(3).getStringCellValue().equalsIgnoreCase("Ordered")||
					   sheet.getRow(row).getCell(3).getStringCellValue().equalsIgnoreCase("Processed - Ready for Pickup")) {
						//compare before order placement inventory vs while remorse period inventory
						if(sheet.getRow(row).getCell(22).getNumericCellValue()==sheet.getRow(row).getCell(12).getNumericCellValue()&&
							sheet.getRow(row).getCell(23).getNumericCellValue()==(sheet.getRow(row).getCell(13).getNumericCellValue()+1)&&	
						   	sheet.getRow(row).getCell(24).getNumericCellValue()==sheet.getRow(row).getCell(14).getNumericCellValue()&&
						   	sheet.getRow(row).getCell(25).getNumericCellValue()==sheet.getRow(row).getCell(15).getNumericCellValue()){
							//if values match set test status to pass
							sheet.getRow(row).getCell(26).setCellValue("PASS");
						} else {
							sheet.getRow(row).getCell(26).setCellValue("FAIL");
						}
					} else {
						sheet.getRow(row).getCell(26).setCellValue("Recheck Later");
						System.out.println("----- ORDER STATUS IS " + sheet.getRow(row).getCell(3).getStringCellValue() 
								           + ". EXPECTING ORDERED or READY FOR PICKUP STATUS -----");
					}
					break;
					
				case "AfterRemorsePeriodWH":
					//write sku inventory AfterRemorsePeriod on excel tracking sheet
					CellUtil.getCell(sheet.getRow(row), 27);
					CellUtil.getCell(sheet.getRow(row), 28);
					CellUtil.getCell(sheet.getRow(row), 29);
					CellUtil.getCell(sheet.getRow(row), 30);
					CellUtil.getCell(sheet.getRow(row), 31);
					
					System.out.println("Writing iNVENTORY DATA . . . ");
					sheet.getRow(row).getCell(27).setCellValue(Integer.parseInt(siteManagerPage.text_onhandInventory.getText()));
					sheet.getRow(row).getCell(28).setCellValue(Integer.parseInt(siteManagerPage.text_reservedInventory.getText()));
					sheet.getRow(row).getCell(29).setCellValue(Integer.parseInt(siteManagerPage.text_externalReserveQty.getText()));
					sheet.getRow(row).getCell(30).setCellValue(Integer.parseInt(siteManagerPage.text_availableInventory.getText()));
					
					if(sheet.getRow(row).getCell(3).getStringCellValue().equalsIgnoreCase("Ordered")||
							sheet.getRow(row).getCell(3).getStringCellValue().equalsIgnoreCase("Processed - Ready for Pickup")) {
						//compare before order placement inventory vs while remorse period inventory
						if(sheet.getRow(row).getCell(27).getNumericCellValue()==sheet.getRow(row).getCell(17).getNumericCellValue()&&
							sheet.getRow(row).getCell(28).getNumericCellValue()==sheet.getRow(row).getCell(18).getNumericCellValue()&&	
						   	sheet.getRow(row).getCell(29).getNumericCellValue()==(sheet.getRow(row).getCell(19).getNumericCellValue()-1)&&
						   	sheet.getRow(row).getCell(30).getNumericCellValue()==sheet.getRow(row).getCell(20).getNumericCellValue()){
							//if values match set test status to pass
							sheet.getRow(row).getCell(31).setCellValue("PASS");
						} else {
							sheet.getRow(row).getCell(31).setCellValue("FAIL");
						}
					} else {
						sheet.getRow(row).getCell(31).setCellValue("Recheck Later");
						System.out.println("----- ORDER STATUS IS " + sheet.getRow(row).getCell(3).getStringCellValue() 
								           + ". EXPECTING ORDERED or READY FOR PICKUP STATUS -----");
					}
					break;
					
				case "OrderCompletedOrCanceled":
					//write sku inventory AfterRemorsePeriod on excel tracking sheet
					CellUtil.getCell(sheet.getRow(row), 3);
					CellUtil.getCell(sheet.getRow(row), 32);
					CellUtil.getCell(sheet.getRow(row), 33);
					CellUtil.getCell(sheet.getRow(row), 34);
					CellUtil.getCell(sheet.getRow(row), 35);
					CellUtil.getCell(sheet.getRow(row), 36);
					
					System.out.println("Writing iNVENTORY DATA . . . ");
					sheet.getRow(row).getCell(32).setCellValue(Integer.parseInt(siteManagerPage.text_onhandInventory.getText()));
					sheet.getRow(row).getCell(33).setCellValue(Integer.parseInt(siteManagerPage.text_reservedInventory.getText()));
					sheet.getRow(row).getCell(34).setCellValue(Integer.parseInt(siteManagerPage.text_externalReserveQty.getText()));
					sheet.getRow(row).getCell(35).setCellValue(Integer.parseInt(siteManagerPage.text_availableInventory.getText()));
						
					//compare before order placement inventory vs while remorse period inventory
					if(sheet.getRow(row).getCell(3).getStringCellValue().equalsIgnoreCase("Order Complete")) {
						if(sheet.getRow(row).getCell(32).getNumericCellValue()==sheet.getRow(row).getCell(22).getNumericCellValue()&&
							sheet.getRow(row).getCell(33).getNumericCellValue()==(sheet.getRow(row).getCell(23).getNumericCellValue()-1)&&	
						   	sheet.getRow(row).getCell(34).getNumericCellValue()==sheet.getRow(row).getCell(24).getNumericCellValue()&&
						   	sheet.getRow(row).getCell(35).getNumericCellValue()==sheet.getRow(row).getCell(25).getNumericCellValue()){
							//if values match set test status to pass
							sheet.getRow(row).getCell(36).setCellValue("PASS");
						} else {
							sheet.getRow(row).getCell(36).setCellValue("FAIL");
						}
					} else if(sheet.getRow(row).getCell(3).getStringCellValue().equalsIgnoreCase("Order Cancelled")) {
						if(sheet.getRow(row).getCell(32).getNumericCellValue()==(sheet.getRow(row).getCell(22).getNumericCellValue()+1)&&
							sheet.getRow(row).getCell(33).getNumericCellValue()==(sheet.getRow(row).getCell(23).getNumericCellValue()-1)&&	
							sheet.getRow(row).getCell(34).getNumericCellValue()==sheet.getRow(row).getCell(24).getNumericCellValue()&&
							sheet.getRow(row).getCell(35).getNumericCellValue()==(sheet.getRow(row).getCell(25).getNumericCellValue()+1)){
						    //if values match set test status to pass
							sheet.getRow(row).getCell(36).setCellValue("PASS");
						} else {
							sheet.getRow(row).getCell(36).setCellValue("FAIL");
						}
					} else {
						sheet.getRow(row).getCell(36).setCellValue("Recheck Later");
						System.out.println("----- ORDER STATUS IS " + sheet.getRow(row).getCell(3).getStringCellValue() 
								           + ". EXPECTING COMPLETE OR CANCELED STATUS-----");
					}
					break;
			}

			FileOutputStream fos = new FileOutputStream(src);	
			xsf.write(fos);		
			xsf.close();	
			
		} catch (Exception e) {
			e.printStackTrace(); 
			//closeBrowser();	
		}
	}
	
	 public void checkOrdersStatus(){
		 ExtentTest logger = extent.startTest("Check Order Status", "Check Order Status");
	 
		 try {
			 File src = new File(siteManagerPage.smokeTestDir);
			 FileInputStream fis = new FileInputStream(src);
			 XSSFWorkbook xsf = new XSSFWorkbook(fis);
			 XSSFSheet sheet = xsf.getSheetAt(0);
			 ordersToCheck = sheet.getLastRowNum();
				
			 getLastRow(sheet, 2, ordersToCheck);
				
			 System.out.println("Check status of " + ordersToCheck + " orders");
			
			 Link.getInstance().click(logger, siteManagerPage.lnk_Commerce);
			 Link.getInstance().click(logger, siteManagerPage.lnk_CommerceOrders);
			 Link.getInstance().click(logger, siteManagerPage.lnk_CommerceOrdersManageExisting);
				
			
			 for(int i=2;i<=(ordersToCheck+1);i++) {
				 //Pull test data from excel sheet
				 CellUtil.getCell(sheet.getRow(i), 2);
				 CellUtil.getCell(sheet.getRow(i), 3);
				 System.out.println("Checking ( " + (i-1) + "/" + ordersToCheck + " )" );
					
				 String orderNum = sheet.getRow(i).getCell(2).getStringCellValue();
				 TextBox.getInstance().setTextAndEnter(logger, siteManagerPage.tf_orderID, orderNum);
					
				 Browser.waitForBrowserToLoadCompletely(driver);
				
				 Browser.captureScreenShot(logger, driver, "OrderStatus_" + orderNum + "_" + System.currentTimeMillis());
				 
				 if(driver.findElements(By.xpath("//*[contains(text(),'Search has returned no results')]")).size()>0) {
					 sheet.getRow(i).getCell(3).setCellValue("Order not found");
				 } else if(driver.findElements(By.xpath("//*[contains(text(),'"+ orderNum +"')]")).size()>0) {
					 System.out.println(orderNum + " found in EOM. Getting Order Status . . .");
					 sheet.getRow(i).getCell(3).setCellValue(siteManagerPage.lnk_orderStatus.getText());
				 } else {
					 System.out.println("Nothing to check");
				 }
			}
		    //write created order in excel
			FileOutputStream fos = new FileOutputStream(src);
			xsf.write(fos);
			xsf.close();
			
		 } catch (Exception e) {
			 Browser.captureScreenShot(logger, driver, "Error");
			 logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			 e.printStackTrace();
			 closeBrowser();
		 }
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
		//closeBrowser();
	}
	
	public void getLastRow(XSSFSheet sheet,int col,int lastRow) {
		for(int c=2;c<lastRow;c++) {
			CellUtil.getCell(sheet.getRow(c), col);
			if(sheet.getRow(c).getCell(col).getStringCellValue()== "") {
				ordersToCheck = c-2;
				break;
			} else {
				continue;
			}
		}
	}
	
	
	//Test BOSTC order fulfilled by WH
	@Test
	public void inventoryBeforeOrderPlacement() {
		ExtentTest logger = extent.startTest("Take inventory Before Order Placement screenshot and order creation", "Take inventory Before Order Placement screenshot and order creation");
		try {
			loginSiteManager();
			int numOrderCreated = Integer.parseInt(inputData.get("Create").get("ordersCreated"))+2;
			File src = new File(siteManagerPage.smokeTestDir);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);	
			XSSFSheet sheet = xsf.getSheetAt(0);
			ordersToCheck = sheet.getLastRowNum();
			System.out.println("Initial orderstocheck value "  + ordersToCheck);
			getLastRow(sheet, 37, ordersToCheck);
			System.out.println("After Getlastrow orderstocheck value "  + ordersToCheck);
			for(int i=1;i<=ordersToCheck;i++) {
//				System.out.println("Checking inv " + i + " / " + ordersToCheck);
//				takeInventoryScreenshot((i+1),"BeforeOrderPlacement");
//				takeInventoryScreenshot((i+1),"BeforeOrderPlacementWH");
				createOrder_BOPIS(numOrderCreated,i+1,"POP",false);
			}	
			xsf.close();
		} catch (Exception e) {
			Browser.captureScreenShot(logger, driver, "Error");
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			e.printStackTrace();
			//closeBrowser();	
		}
	}
		
	@Test
	public void inventoryWhileRemorsePeriod() {
		ExtentTest logger = extent.startTest("Take Inventory while Remorse Period", "Take Inventory while Remorse Period");
		try {
			loginSiteManager();
			File src = new File(siteManagerPage.smokeTestDir);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);	
			XSSFSheet sheet = xsf.getSheetAt(0);
			ordersToCheck = sheet.getLastRowNum();
			getLastRow(sheet, 37, ordersToCheck);
			for(int i=1;i<=ordersToCheck;i++) {
				takeInventoryScreenshot((i+1),"WhileRemorsePeriod");
				takeInventoryScreenshot((i+1),"WhileRemorsePeriodWH");
			}	
			xsf.close();
		} catch (Exception e) {
			Browser.captureScreenShot(logger, driver, "Error");
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			e.printStackTrace();
		}
		closeBrowser();	
	}
	
	@Test
	public void inventoryAfterRemorsePeriod() {
		ExtentTest logger = extent.startTest("Take Inventory After Remorse Period", "Take Inventory After Remorse Period");
		try {
			loginSiteManager();
			File src = new File(siteManagerPage.smokeTestDir);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);	
			XSSFSheet sheet = xsf.getSheetAt(0);
			ordersToCheck = sheet.getLastRowNum();
			getLastRow(sheet, 37, ordersToCheck);
			checkOrdersStatus();
			for(int i=1;i<=ordersToCheck;i++) {
				takeInventoryScreenshot((i+1),"AfterRemorsePeriod");
				takeInventoryScreenshot((i+1),"AfterRemorsePeriodWH");
			}	
			xsf.close();
		} catch (Exception e) {
			Browser.captureScreenShot(logger, driver, "Error");
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			e.printStackTrace();	
		}
		closeBrowser();
	}
	
	@Test
	public void inventoryOrderCompletedOrCanceled() {
		ExtentTest logger = extent.startTest("Take Inventory Screenshot after order is comppleted ro cancelled", "Take Inventory Screenshot after order is comppleted ro cancelled");
		try {
			loginSiteManager();
			File src = new File(siteManagerPage.smokeTestDir);			
			FileInputStream fis = new FileInputStream(src);		
			XSSFWorkbook xsf = new XSSFWorkbook(fis);	
			XSSFSheet sheet = xsf.getSheetAt(0);
			ordersToCheck = sheet.getLastRowNum();
			getLastRow(sheet, 37, ordersToCheck);
			checkOrdersStatus();
			for(int i=1;i<=ordersToCheck;i++) {
				takeInventoryScreenshot((i+1),"OrderCompletedOrCanceled");
			}	
			xsf.close();
		} catch (Exception e) {
			Browser.captureScreenShot(logger, driver, "Error");
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			e.printStackTrace();	
		}
		closeBrowser();
	}
	

	//Method for creating BOPIS order
	public void createOrder_BOPIS(int numOrderCreated, int writeOnRowCnt,String paymentMode,boolean mixFulfilled) {
			ExtentTest logger = extent.startTest("Create BOPIS order", "Create BOPIS order");
			int numOfOrders = ordersToCheck;
			try {	
				//browser.navigate(logger, inputData.get("navigate").get("penURL").toString());
				
				File src = new File(siteManagerPage.smokeTestDir);			
				FileInputStream fis = new FileInputStream(src);		
				XSSFWorkbook xsf = new XSSFWorkbook(fis);	
				XSSFSheet sheet = xsf.getSheetAt(0);
				
				//Pull test data from excel sheet
				CellUtil.getCell(sheet.getRow(writeOnRowCnt), 0);
				CellUtil.getCell(sheet.getRow(writeOnRowCnt), 1);
				CellUtil.getCell(sheet.getRow(writeOnRowCnt), 2);
				CellUtil.getCell(sheet.getRow(writeOnRowCnt), 37);
				CellUtil.getCell(sheet.getRow(writeOnRowCnt), 39);
				CellUtil.getCell(sheet.getRow(writeOnRowCnt), 40);
				CellUtil.getCell(sheet.getRow(writeOnRowCnt), 41);
				CellUtil.getCell(sheet.getRow(writeOnRowCnt), 42);  
				
				System.out.println("#"+sheet.getRow(writeOnRowCnt).getCell(0).getNumericCellValue()+"#");
				int siteID = (int) sheet.getRow(writeOnRowCnt).getCell(0).getNumericCellValue();
				int skuStore = (int) sheet.getRow(writeOnRowCnt).getCell(1).getNumericCellValue();
//				int skuWH = (int) sheet.getRow(numOrderCreated).getCell(2).getNumericCellValue();
				
				//Pull data for web elements for Address finder
				String refCode = sheet.getRow(writeOnRowCnt).getCell(37).getStringCellValue();
				String addressProvince = sheet.getRow(writeOnRowCnt).getCell(39).getStringCellValue().replaceFirst("\\s++$", "");
				String addressCity = sheet.getRow(writeOnRowCnt).getCell(40).getStringCellValue().replaceFirst("\\s++$", "");
				String addressBarangay = sheet.getRow(writeOnRowCnt).getCell(41).getStringCellValue().replaceFirst("\\s++$", "");
				String addressLine1 = sheet.getRow(writeOnRowCnt).getCell(42).getStringCellValue().replaceFirst("\\s++$", "");
				
				//web elements for billing address
				String firstNameBA = inputData.get("Create").get("firstNameBA");
				String lastNameBA = inputData.get("Create").get("lastNameBA");
				String houseNumberBA = inputData.get("Create").get("houseNumberBA");
				String provinceBA = inputData.get("Create").get("provinceBA");
				String townOrCityBA = inputData.get("Create").get("townOrCityBA");
				String barangayBA = inputData.get("Create").get("barangayBA");
				String zipCodeBA = inputData.get("Create").get("zipCodeBA");
				String phoneBA = inputData.get("Create").get("phoneBA");
				
				if(refCode.substring(0, 1).equalsIgnoreCase("P")) { 
					browser.navigate(logger, inputData.get("navigate").get("penURL").toString());
					System.out.println("Creating PENSHOPPE Order");
				} else {
					browser.navigate(logger, inputData.get("navigate").get("oxyURL").toString());
					System.out.println("Creating OXYGEN Order");
				}			
				
				Button.getInstance().click(logger, homePage.icn_Search);
				Browser.waitForElementIsClickable(logger, driver, homePage.tf_search);
				TextBox.getInstance().setTextAndEnter(logger, homePage.tf_search, String.valueOf(skuStore));
				//Browser.waitForElementIsClickable(logger, driver, homePage.lnk_searchSkuThumbnail);
				Browser.clickElementJSExecutor(logger, driver, homePage.lnk_searchSkuThumbnail);
				
				Browser.waitForElementIsVisible(logger, driver, homePage.txt_quantity);
				Browser.waitForBrowserToLoadCompletely(driver);
						
					//Add item to Cart
				Browser.scrollElementIntoView(logger, driver, homePage.btn_addToCart);
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_addToCart);
				Browser.clickElementJSExecutor(logger, driver, homePage.btn_addToCart);
				Browser.pause(logger, "1");			
								
				if(mixFulfilled==true) {
					//Add mix item to Cart
					JavascriptExecutor js = (JavascriptExecutor) driver;
					browser.navigate(logger, "https://uat.penshoppe.com/products/track-pants-draw-cord-22?_pos=1&_sid=55c278935&_ss=r&variant=41904533340332");
					Browser.waitForBrowserToLoadCompletely(driver);
					
					//Add item to Cart
					js.executeScript("arguments[0].scrollIntoView(true);", homePage.btn_addToCart);
					Browser.waitForElementIsVisible(logger, driver, homePage.btn_addToCart);
					js.executeScript("arguments[0].click();", homePage.btn_addToCart);
					//Browser.pause(logger, "1");	
				}
			
				Browser.waitForElementIsClickable(logger, driver, homePage.btn_reviewCart);
				homePage.check_cartEmpty(logger,driver);
				Button.getInstance().click(logger, homePage.btn_reviewCart);
				Browser.waitForElementIsVisible(logger, driver, homePage.btn_checkOut);
				Browser.pause(logger, "1");		
					
				Browser.scrollElementIntoView(logger, driver, homePage.btn_checkOut);
				Browser.waitForElementIsClickable(logger, driver, homePage.btn_checkOut);
				Browser.clickElementJSExecutor(logger, driver, homePage.btn_checkOut);
	
				//Populate Contact information and Shipping address	
				homePage.check_stuckLoading(driver);
				
				Browser.waitForBrowserToLoadCompletely(driver);
				System.out.println("Enter email address");
				Browser.waitForElementIsClickable(logger, driver, homePage.tf_email);
				TextBox.getInstance().setText(logger, homePage.tf_email, siteManagerPage.automationEmail);
				
				selectPickUpStore(logger,String.valueOf(siteID), addressProvince, addressCity, addressBarangay, addressLine1);	
								
				if(paymentMode=="POP") {
				//select COD as payment method
					Browser.waitForElementIsVisible(logger, driver, homePage.btn_cashOnDelivery);
					RadioButton.getInstance().click(logger, homePage.btn_cashOnDelivery);
				} 
					
				//select billing address
//				Browser.waitForElementIsVisible(logger, driver, homePage.dd_billingAddress);
				TextBox.getInstance().setTextAndTab(logger, homePage.tf_firstNameBA, firstNameBA);
				TextBox.getInstance().setTextAndTab(logger, homePage.tf_lastNameBA, lastNameBA);
				TextBox.getInstance().setTextAndTab(logger, homePage.tf_houseNumberBA, houseNumberBA);
				TextBox.getInstance().setTextAndTab(logger, homePage.tf_provinceBA, provinceBA);
				TextBox.getInstance().setTextAndTab(logger, homePage.tf_townOrCityBA, townOrCityBA);
				TextBox.getInstance().setTextAndTab(logger, homePage.tf_barangayBA, barangayBA);
				TextBox.getInstance().setTextAndTab(logger, homePage.tf_zipCodeBA, zipCodeBA);	
				Dropdown.getInstance().selectByPartOfTextIgnoreCase(logger, driver, homePage.dd_regionBA, provinceBA);
				TextBox.getInstance().setTextAndTab(logger, homePage.tf_phoneBA, phoneBA);	
				
//				Dropdown.getInstance().selectByVisibleText(logger, homePage.dd_billingAddress, custAddress);
//				Browser.pause(logger, "1.3");	
				Browser.waitForElementIsClickable(logger, driver, homePage.btn_continue);
				Button.getInstance().click(logger, homePage.btn_continue);
				Browser.waitForBrowserToLoadCompletely(driver);
					
				if(driver.findElements(By.xpath(homePage.lbl_orderBeingProcess)).size()< 1) {
					System.out.println("Your order’s being processed IS NOT DISPLAYED ");
					driver.navigate().refresh();
					Browser.waitForBrowserToLoadCompletely(driver);
					homePage.check_ifOrderNumDisplayed(logger,driver);
				} else {
					System.out.println("Waiting for Order# ...");
					driver.navigate().refresh();
					Browser.waitForBrowserToLoadCompletely(driver);
					homePage.check_ifOrderNumDisplayed(logger,driver);
				}
				
				//get order number
				Browser.waitForElementIsVisible(logger, driver, homePage.txt_orderNumber);
				orderNum = homePage.txt_orderNumber.getText().substring(homePage.txt_orderNumber.getText().indexOf(" ")+1, homePage.txt_orderNumber.getText().length());
				logger.log(LogStatus.PASS, "BOPIS Order created: " + orderNum + "<br>");
				createdOrders = createdOrders + orderNum + "\r\n";
				Browser.captureScreenShot(logger, driver, orderNum);
					
				numOrderCreated++;
							
				//print created orders in console for monitoring
				System.out.println("\r\nBOPIS - Training Orders Created ("+ writeOnRowCnt + "/" + numOfOrders + ") : \r\n" + createdOrders);
					
				//write created order in excel		
				xsf.close();	
				writeOnExcelSheet(orderNum,writeOnRowCnt,2,siteManagerPage.smokeTestDir);
				writeOnExcelSheet("New",writeOnRowCnt,3,siteManagerPage.smokeTestDir);
				writeOnExcelSheet(String.valueOf(numOrderCreated),2,10,siteManagerPage.siteManagerDir);	
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
				currBOPISOrder = numOrderCreated;
				if(currBOPISOrder!=numOfOrders) {
					driver.close();
					driver.quit();
					System.out.println("Error encountered while creating BOSTC order. Re-running test");
					createOrder_BOPIS(numOrderCreated, writeOnRowCnt,paymentMode, mixFulfilled);
				}
			} 
			extent.endTest(logger);
			extent.flush();
			Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);	
		}
		// End of method for creating BOPIS order
		
		
		//method for selecting pickup store
		public void selectPickUpStore(ExtentTest logger, String pickupStore, String prvnc, String cty, String brgy, String add1) throws Exception {
			//Populate Contact information and Shipping address	
			if(homePage.tf_email.getText().equals("")) {
				TextBox.getInstance().setText(logger, homePage.tf_email, siteManagerPage.automationEmail);
			}
			
			Browser.waitForElementIsVisible(logger, driver, homePage.btn_pickUp);
			Button.getInstance().click(logger, homePage.btn_pickUp);				
			Browser.pause(logger, "2");
			
			System.out.println("Will select : " + prvnc + " / " + cty + " / " + brgy + " from address finder");
			Link.getInstance().click(logger, homePage.lnk_findYourLocation);
			Dropdown.getInstance().selectByPartOfTextIgnoreCase(logger, driver, homePage.dd_province, prvnc);
			Browser.pause(logger, "1");
			Dropdown.getInstance().selectByPartOfTextIgnoreCase(logger, driver, homePage.dd_city, cty);
			Browser.pause(logger, "1");
			Dropdown.getInstance().selectByPartOfTextIgnoreCase(logger, driver, homePage.dd_barangay, brgy);
			Browser.pause(logger, "1");
			Button.getInstance().click(logger, homePage.btn_verifyAddress);
			
			if(numRetry==2) {
				System.out.println("Max reload retries reached. Re-running test");
				numRetry = 0;
				throw new Exception("Max reload retries reached. Re-running test");
			} else if(driver.findElements(By.xpath(homePage.txt_showStoreNearLoc)).size()<=0) {
				System.out.println("Show Store Near Loc is not displayed. Refreshing the page");
				driver.navigate().refresh();
				Browser.pause(logger, "2");
				numRetry++;
				selectPickUpStore(logger,pickupStore, prvnc, cty, brgy, add1);
			} else {
				Browser.clickElementJSExecutor(logger, driver, homePage.btn_showStoreNearLoc);
				Browser.waitForLoading(logger, driver, homePage.btn_showStoreNearLoc);
				numRetry = 0;
			}
			
			if(driver.findElements(By.xpath(homePage.txt_addressContainsPrefix + pickupStore + homePage.txt_addressContainsSuffix)).size()<=0) {
				System.out.println("Loading Pickup store is taking too long. Refreshing the page");
				driver.navigate().refresh();
				Browser.pause(logger, "2");
				numRetry++;
				selectPickUpStore(logger,pickupStore, prvnc, cty, brgy, add1);
			} else {
				WebElement storeToFind = driver.findElement(By.xpath(homePage.txt_addressContainsPrefix + pickupStore + homePage.txt_addressContainsSuffix));
				Browser.scrollElementIntoView(logger, driver, storeToFind);	
				Button.getInstance().click(logger, storeToFind);	
				Button.getInstance().click(logger, homePage.btn_continue);	
				numRetry = 0;
			}
		}
		
		
		//Method to write created orders to excel
		@SuppressWarnings("deprecation")
		public void writeOnExcelSheet(String cellValue,int rowNum, int cellNum, String filePath){		
			try {
				File src = new File(filePath);			
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
		
}
