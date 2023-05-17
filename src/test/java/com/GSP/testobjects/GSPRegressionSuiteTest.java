package com.GSP.testobjects;

import java.awt.Dimension;
import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import com.GABC.webelements.AlertsHandler;
import com.GABC.webelements.Button;
import com.GABC.webelements.Dropdown;
import com.GABC.webelements.Link;
import com.GABC.webelements.RadioButton;
import com.GABC.webelements.TextBox;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class GSPRegressionSuiteTest extends BaseTest{
	String url = "";
	String sscc = "";	
	String qty = "";
	String frmDate = "";
	String barCode = "";
	String deliveryNum = "";
	String ssccNum = "";
	String destinationSite = "";
	
	/**
	 * This is methods is for Transactions > Deliveries / Delivery COnfirmation
	 * 
	 * 
	 */
	@Test
	public void deliveriesSearchNoResult() {
		ExtentTest logger = extent.startTest("Test Deliveries Search Functionality - No result", "Test Deliveries Search Functionality - No result");
		
		try {
			navToTrans(logger,"Deliveries");	
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_searchDelivery);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_searchDelivery);
			Browser.pause(logger, "1");
			if((driver.findElements(By.xpath(gspPage.txt_noResult)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - " + gspPage.txt_noResult + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - " + gspPage.txt_noResult + " is NOT displayed");
			}
			Browser.captureScreenShot(logger, driver, "Deliveries Search No Result " + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - deliveriesSearchNoResult " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void deliveriesSearchWithResult() {
		ExtentTest logger = extent.startTest("Test Deliveries Search Functionality - with result", "Test Deliveries Search Functionality - with result");
		
		try {
			navToTrans(logger,"Deliveries");	
			frmDate = inputData.get("testdata").get("fromDate");
			gspPage.btn_fromDate.sendKeys(frmDate);
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_searchDelivery);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_searchDelivery);
			Browser.pause(logger, "1");
			if((driver.findElements(By.xpath(gspPage.txt_noResult)).size()>0)) {
				logger.log(LogStatus.FAIL, "Test FAILED - " + gspPage.txt_noResult + " is displayed");
			} else {
				logger.log(LogStatus.PASS, "Test PASSED - " + gspPage.txt_noResult + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "Deliveries Search With Result " + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - deliveriesSearchWithResult " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}	
	
	@Test
	public void openDeliveryConfirmationScreen() {
		ExtentTest logger = extent.startTest("Test open delivery confirmation screen", "Test Deliveries Search Functionality - with result");
		
		try {
			navToTrans(logger,"Deliveries");	
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_deliveryConfirmation);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_deliveryConfirmation);
			Browser.pause(logger, "1");
			url = driver.getCurrentUrl();
			if(url.contains("delivery-confirmation") && (driver.findElements(By.xpath(gspPage.deliveryConfirmationCrumbs)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Delivery Confirmation Page loaded - " + gspPage.deliveryConfirmationCrumbs + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Delivery Confirmation Page NOT loaded - " + gspPage.deliveryConfirmationCrumbs + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "Deliveries Confirmation Screen");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - openDeliveryConfirmationScreen " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_blankAllFields() {
		ExtentTest logger = extent.startTest("Test Submit Blank Delivery Confirmation", "Test Submit Blank Delivery Confirmation");
		
		try {
			openDeliveryConfirmationScreen();
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit Blank Delivery Confirmation " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBlankDeliveryConfirmation)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBlankDeliveryConfirmation + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBlankDeliveryConfirmation + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_blankAllFields " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_invalidbarCode() {
		ExtentTest logger = extent.startTest("Test Submit DC with Invalid Barcode", "Test Submit DC with Invalid Barcode");
		
		try {
			openDeliveryConfirmationScreen();	
			
			sscc = inputData.get("testdata").get("ssccGood");	
			qty = inputData.get("testdata").get("qtyGood");	
			barCode = inputData.get("testdata").get("bcGood");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode+ Instant.now().getEpochSecond());
			Browser.captureScreenShot(logger, driver, "Submit DC with Invalid Barcode " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.errInvalidBarCodeDC)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errInvalidBarCodeDC + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errInvalidBarCodeDC + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_invalidbarCode" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_blankAWB() {
		ExtentTest logger = extent.startTest("Test Submit delivery confirmation blank AWB", "Test Submit delivery confirmation blank AWB");
		
		try {
			openDeliveryConfirmationScreen();
			
			sscc = inputData.get("testdata").get("ssccGood");	
			qty = inputData.get("testdata").get("qtyGood");	
			barCode = inputData.get("testdata").get("bcGood");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));
			
			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_barCodes)).size() ;i++) {
				TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, "1");
				TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
				System.out.println("Scanned: " + driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
			}
			
			Button.getInstance().click(logger, gspPage.dd_selectRemarks);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectRemarks, "Good");
			
			Button.getInstance().click(logger, gspPage.dd_selectForwarder);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectForwarder, "LBC");
				
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit DC with blank AWB " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.errBlankDeliveryConfirmation)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Error message displayed" + gspPage.errBlankDeliveryConfirmation + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Error message is NOT displayed" + gspPage.errBlankDeliveryConfirmation + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_blankAWB " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_blankRemarks() {
		ExtentTest logger = extent.startTest("Test Submit Delivery Confirmation with Blank Remarks", "Test Submit Delivery Confirmation with Blank Remarks");
		
		try {
			openDeliveryConfirmationScreen();
			
			sscc = inputData.get("testdata").get("ssccGood");	
			qty = inputData.get("testdata").get("qtyGood");	
			barCode = inputData.get("testdata").get("bcGood");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));

			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_barCodes)).size() ;i++) {
				TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, "1");
				TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
				System.out.println("Scanned: " + driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
			}
			
			TextBox.getInstance().setText(logger, gspPage.tb_airWayBill, "AT" + Instant.now().getEpochSecond());
			
			Button.getInstance().click(logger, gspPage.dd_selectForwarder);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectForwarder, "LBC");
				
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit DC with blank Remarks " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.errBlankDeliveryConfirmation)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Error message displayed" + gspPage.errBlankDeliveryConfirmation + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Error message is NOT displayed" + gspPage.errBlankDeliveryConfirmation + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_blankRemarks " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_blankForwarder() {
		ExtentTest logger = extent.startTest("Test Submit Delivery Confirmation with Blank Forwarder", "Test Submit Delivery Confirmation with Blank Forwarder");
		
		try {
			openDeliveryConfirmationScreen();
			
			sscc = inputData.get("testdata").get("ssccGood");	
			qty = inputData.get("testdata").get("qtyGood");	
			barCode = inputData.get("testdata").get("bcGood");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));

			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_barCodes)).size() ;i++) {
				TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, "1");
				TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
				System.out.println("Scanned: " + driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
			}
			
			TextBox.getInstance().setText(logger, gspPage.tb_airWayBill, "AT" + Instant.now().getEpochSecond());
			
			Button.getInstance().click(logger, gspPage.dd_selectRemarks);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectRemarks, "Good");
				
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit DC with blank forwarder " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.errBlankDeliveryConfirmation)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Error message displayed" + gspPage.errBlankDeliveryConfirmation + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Error message is NOT displayed" + gspPage.errBlankDeliveryConfirmation + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_blankForwarder " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_zeroQuantity() {
		ExtentTest logger = extent.startTest("Test submit delivery confirmation with multiple barcodes and one or more barCode have 0 qty", 
				"Test submit delivery confirmation with multiple barcodes and one or more barCode have 0 qty");
		
		try {
			openDeliveryConfirmationScreen();
			
			sscc = inputData.get("testdata").get("ssccGood");	
			qty = inputData.get("testdata").get("qtyGood");	
			barCode = inputData.get("testdata").get("bcGood");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));
			
			for(int i=1; i< driver.findElements(By.xpath(gspPage.lbl_barCodes)).size() ;i++) {
				TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, "1");
				TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
				System.out.println("Scanned: " + driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
			}
			
			Browser.pause(logger, "1");
			TextBox.getInstance().setText(logger, gspPage.tb_airWayBill, "AT" + Instant.now().getEpochSecond());
			
			Button.getInstance().click(logger, gspPage.dd_selectRemarks);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectRemarks, "Good");
			
			Button.getInstance().click(logger, gspPage.dd_selectForwarder);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectForwarder, "LBC");
					
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);	
			
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit Delivery Confirmation Zero Quantity " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.errZeroQuantityDetected)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED- Success message displayed" + gspPage.errZeroQuantityDetected + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Success message is NOT displayed" + gspPage.errZeroQuantityDetected + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_zeroQuantity" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_good() {
		ExtentTest logger = extent.startTest("Test submit delivery confirmation - Good", "Test submit delivery confirmation - Good");
		
		try {
			openDeliveryConfirmationScreen();
			
			sscc = inputData.get("testdata").get("ssccGood");	
			qty = inputData.get("testdata").get("qtyGood");	
			barCode = inputData.get("testdata").get("bcGood");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));
			
			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_barCodes)).size() ;i++) {
				TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, "1");
				TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
				System.out.println("Scanned: " + driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
			}
			
			Browser.pause(logger, "1");
			TextBox.getInstance().setText(logger, gspPage.tb_airWayBill, "AT" + Instant.now().getEpochSecond());
			
			Button.getInstance().click(logger, gspPage.dd_selectRemarks);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectRemarks, "Good");
			
			Button.getInstance().click(logger, gspPage.dd_selectForwarder);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectForwarder, "LBC");
					
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_confirmSubmit);		
			
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit Good Delivery Confirmation " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.msgSuccessfulDeliveryConfirmation)).size()>0)) {
				logger.log(LogStatus.FAIL, "Test FAILED- Success message displayed" + gspPage.errBlankDeliveryConfirmation + " is displayed");
			} else {
				logger.log(LogStatus.PASS, "Test PASSED - Success message is NOT displayed" + gspPage.errBlankDeliveryConfirmation + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_good" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_tampered() {
		ExtentTest logger = extent.startTest("Test submit delivery confirmation - Tampered", "Test submit delivery confirmation - Tampered");
		
		try {
			openDeliveryConfirmationScreen();
			
			sscc = inputData.get("testdata").get("ssccTampered");	
			qty = inputData.get("testdata").get("qtyTampered");	
			barCode = inputData.get("testdata").get("bcTampered");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));
			
			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_barCodes)).size() ;i++) {
				TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, "1");
				TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
				System.out.println("Scanned: " + driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
			}
			
			TextBox.getInstance().setText(logger, gspPage.tb_airWayBill, "AT" + Instant.now().getEpochSecond());
			
			Button.getInstance().click(logger, gspPage.dd_selectRemarks);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectRemarks, "Tampered");
			
			gspPage.btn_fileUpload.sendKeys(System.getProperty("user.dir")+ "\\src\\test\\java\\com\\dataobjects\\" + "sample_image.jpg");
			
			Button.getInstance().click(logger, gspPage.dd_selectForwarder);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectForwarder, "LBC");
					
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_confirmSubmit);
			
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit Tampered Delivery Confirmation " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.msgSuccessfulDeliveryConfirmation)).size()>0)) {
				logger.log(LogStatus.FAIL, "Test FAILED- Success message displayed" + gspPage.errBlankDeliveryConfirmation + " is displayed");
			} else {
				logger.log(LogStatus.PASS, "Test PASSED - Success message is NOT displayed" + gspPage.errBlankDeliveryConfirmation + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_tampered " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_damaged() {
		ExtentTest logger = extent.startTest("Test submit delivery confirmation - Damaged", "Test submit delivery confirmation - Damaged");
		
		try {
			openDeliveryConfirmationScreen();
			
			sscc = inputData.get("testdata").get("ssccDamaged");	
			qty = inputData.get("testdata").get("qtyDamaged");	
			barCode = inputData.get("testdata").get("bcDamaged");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));
			
			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_barCodes)).size() ;i++) {
				TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, "1");
				TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
				System.out.println("Scanned: " + driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
			}
			
			TextBox.getInstance().setText(logger, gspPage.tb_airWayBill, "AT" + Instant.now().getEpochSecond());
			
			Button.getInstance().click(logger, gspPage.dd_selectRemarks);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectRemarks, "Damaged");
			
			gspPage.btn_fileUpload.sendKeys(System.getProperty("user.dir")+ "\\src\\test\\java\\com\\dataobjects\\" + "sample_image.jpg");
			
			Button.getInstance().click(logger, gspPage.dd_selectForwarder);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectForwarder, "LBC");
					
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_confirmSubmit);
			
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit Damaged Delivery Confirmation " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.msgSuccessfulDeliveryConfirmation)).size()>0)) {
				logger.log(LogStatus.FAIL, "Test FAILED- Success message displayed" + gspPage.errBlankDeliveryConfirmation + " is displayed");
			} else {
				logger.log(LogStatus.PASS, "Test PASSED - Success message is NOT displayed" + gspPage.errBlankDeliveryConfirmation + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_damaged " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_soiled() {
		ExtentTest logger = extent.startTest("Test submit delivery confirmation - Soiled", "Test submit delivery confirmation - Soiled");
		
		try {
			openDeliveryConfirmationScreen();
			
			sscc = inputData.get("testdata").get("ssccSoiled");	
			qty = inputData.get("testdata").get("qtySoiled");	
			barCode = inputData.get("testdata").get("bcSoiled");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));
			
			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_barCodes)).size() ;i++) {
				TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, "1");
				TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
				System.out.println("Scanned: " + driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
			}
			
			TextBox.getInstance().setText(logger, gspPage.tb_airWayBill, "AT" + Instant.now().getEpochSecond());
			
			Button.getInstance().click(logger, gspPage.dd_selectRemarks);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectRemarks, "Soiled");
			
			gspPage.btn_fileUpload.sendKeys(System.getProperty("user.dir")+ "\\src\\test\\java\\com\\dataobjects\\" + "sample_image.jpg");
			
			Button.getInstance().click(logger, gspPage.dd_selectForwarder);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectForwarder, "LBC");
					
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_confirmSubmit);
					
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit Soiled Delivery Confirmation " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.msgSuccessfulDeliveryConfirmation)).size()>0)) {
				logger.log(LogStatus.FAIL, "Test FAILED- Success message displayed" + gspPage.errBlankDeliveryConfirmation + " is displayed");
			} else {
				logger.log(LogStatus.PASS, "Test PASSED - Success message is NOT displayed" + gspPage.errBlankDeliveryConfirmation + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_soiled " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitDeliveryConfirmation_undelivered() {
		ExtentTest logger = extent.startTest("Test submit delivery confirmation - Undelivered", "Test submit delivery confirmation - Undelivered");
		
		try {
			openDeliveryConfirmationScreen();
			
			sscc = inputData.get("testdata").get("ssccUndelivered");	
			qty = inputData.get("testdata").get("qtyUndelivered");	
			barCode = inputData.get("testdata").get("bcUndelivered");
			
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingUnit, sscc);
			Browser.waitForLoading(logger, driver, driver.findElement(By.xpath(gspPage.msgLoading)));
			
			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_barCodes)).size() ;i++) {
				TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, "1");
				TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
				System.out.println("Scanned: " + driver.findElement(By.xpath(gspPage.lbl_barCodes+"["+i+"]")).getText());
			}
			
			TextBox.getInstance().setText(logger, gspPage.tb_airWayBill, "AT" + Instant.now().getEpochSecond());
			
			Button.getInstance().click(logger, gspPage.dd_selectRemarks);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectRemarks, "Undelivered");
			
			gspPage.btn_fileUpload.sendKeys(System.getProperty("user.dir")+ "\\src\\test\\java\\com\\dataobjects\\" + "sample_image.jpg");
			
			Button.getInstance().click(logger, gspPage.dd_selectForwarder);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tf_selectForwarder, "LBC");
					
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirm);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_confirmSubmit);
			
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit Undelivered Delivery Confirmation " + Instant.now().getEpochSecond());
			
			if((driver.findElements(By.xpath(gspPage.msgSuccessfulDeliveryConfirmation)).size()>0)) {
				logger.log(LogStatus.FAIL, "Test FAILED- Success message displayed" + gspPage.errBlankDeliveryConfirmation + " is displayed");
			} else {
				logger.log(LogStatus.PASS, "Test PASSED - Success message is NOT displayed" + gspPage.errBlankDeliveryConfirmation + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitDeliveryConfirmation_undelivered " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}

	/**
	 *  
	 * 
	 * This is test methods is for MRD
	 * 
	 * 
	 */
	@Test
	public void mrdSearchNoResult() {
		ExtentTest logger = extent.startTest("Test MRD Search Functionality - No result", "Test MRD Search Functionality - No result");
		
		try {
			navToTrans(logger,"Misrouted Delivery");	
			Browser.waitForBrowserToLoadCompletely(driver);		
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingNumber, "1234567890");
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_searchDelivery);
			Browser.pause(logger, "2");
			if((driver.findElements(By.xpath(gspPage.txt_noResult)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - " + gspPage.txt_noResult + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - " + gspPage.txt_noResult + " is NOT displayed");
			}
			Browser.captureScreenShot(logger, driver, "MRD Search No Result " + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - mrdSearchNoResult " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
	}
	
	@Test
	public void mrdSearchWithResult() {
		ExtentTest logger = extent.startTest("Test MRD Search Functionality - with result", "Test MRD Search Functionality - with result");
		
		try {
			navToTrans(logger,"Misrouted Delivery");	

			frmDate = inputData.get("testdata").get("fromDate");
			gspPage.btn_fromDate.sendKeys(frmDate);
			
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_searchDelivery);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_searchDelivery);
			Browser.pause(logger, "1");
			if((driver.findElements(By.xpath(gspPage.txt_noResult)).size()>0)) {
				logger.log(LogStatus.FAIL, "Test FAILED - " + gspPage.txt_noResult + " is displayed");
			} else {
				logger.log(LogStatus.PASS, "Test PASSED - " + gspPage.txt_noResult + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "MRD Search With Result " + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - mrdSearchWithResult " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	
	@Test
	public void openCreateNewMRD() {
		ExtentTest logger = extent.startTest("Test open MRD - Create New screen", "Test open MRD - Create New screen");
		
		try {
			navToTrans(logger,"Misrouted Delivery");	
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_createNew);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_createNew);
			Browser.pause(logger, "1");
			url = driver.getCurrentUrl();
			if(url.contains("misrouted_delivery/create") && (driver.findElements(By.xpath(gspPage.mrdCreateNew)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - MRD Create New Page loaded - " + gspPage.mrdCreateNew + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - MRD Create New Page NOT loaded - " + gspPage.mrdCreateNew + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "MRD Create New Screen");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - openCreateNewMRD " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	} 
	
	@Test
	public void submitMRD_blankAllFields() {
		ExtentTest logger = extent.startTest("Test Submit Blank MRD requests", "Test Submit Blank MRD requests");
		
		try {
			openCreateNewMRD();
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit Blank MRD requests " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBlankMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBlankMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBlankMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_blankAllFields " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_blankDeliveryNumber() {
		ExtentTest logger = extent.startTest("Test Submit MRD with blank Delivery Number", "Test Submit MRD with blank Delivery Number");
		
		try {
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit MRD blank Delivery Number " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errMissingRequiredFieldskMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errMissingRequiredFieldskMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errMissingRequiredFieldskMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_blankDeliveryNumber" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_blankSSCCNumber() {
		ExtentTest logger = extent.startTest("Test Submit MRD with blank SSCC Number", "Test Submit MRD with blank SSCC Number");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setText(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit MRD blank SSCC Number " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBlankMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBlankMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBlankMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_blankSSCCNumber" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_blankDestinationSite() {
		ExtentTest logger = extent.startTest("Test Submit MRD with blank Destination Site", "Test Submit MRD with blank Destination Site");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setText(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit MRD blank Destination Site " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errMissingRequiredFieldskMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errMissingRequiredFieldskMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errMissingRequiredFieldskMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_blankDestinationSite" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_emptyBarCode() {
		ExtentTest logger = extent.startTest("Test Submit MRD with empty BarCode", "Test Submit MRD with empty BarCode");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit MRD empty BarCode " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errEmptyBarCode)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errEmptyBarCode + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errEmptyBarCode + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_emptyBarCode" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_invalidSSCCLessThan18Digit() {
		ExtentTest logger = extent.startTest("Test Submit MRD with Invalid SSCC - Less than 18 digits", "Test Submit MRD with Invalid SSCC - Less than 18 digits");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, "" + Instant.now().getEpochSecond());
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit MRD Invalid SSCC Less than 18 Digit " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBlankMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBlankMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBlankMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_invalidSSCCLess18Digit" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_invalidSSCCMoreThan18Digit() {
		ExtentTest logger = extent.startTest("Test Submit MRD with Invalid SSCC - More than 18 digits", "Test Submit MRD with Invalid SSCC - More than 18 digits");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum + Instant.now().getEpochSecond());
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit MRD Invalid SSCC More than 18 Digit " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBlankMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBlankMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBlankMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_invalidSSCCMoreThan18Digit" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_mrdAlreadyExist() {
		ExtentTest logger = extent.startTest("Test Submit MRD - Already Exist", "Test Submit MRD - Already Exist");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_saveMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_saveMRDRequest);		
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit MRD Already Exist " + Instant.now().getEpochSecond());
			Browser.pause(logger, "1");
			if((driver.findElements(By.xpath(gspPage.errMRDAlreadyExist)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errMRDAlreadyExist + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errMRDAlreadyExist + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_mrdAlreadyExist" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_saveConfirmRequest() {
		ExtentTest logger = extent.startTest("Test Submit MRD - Request Saved and Confirm", "Test Submit MRD - Request Saved and Confirm");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "2");
			
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_saveMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_saveMRDRequest);		
			Browser.pause(logger, "2");
			
			if((driver.findElements(By.xpath(gspPage.msgSaveMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgSaveMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgSaveMRDRequest + " is NOT displayed");	
			}
			
			Browser.captureScreenShot(logger, driver, "Submit MRD Request Saved " + Instant.now().getEpochSecond());
			Browser.pause(logger, "2");
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);	
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirmRequest);	
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_confirmMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_confirmMRDRequest);		
			Browser.pause(logger, "2");
			
			if((driver.findElements(By.xpath(gspPage.msgConfirmMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgConfirmMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgConfirmMRDRequest + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "Submit MRD Request Confirmed " + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_aaveConfirmRequest" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void cancelMRDRequest() {
		ExtentTest logger = extent.startTest("Test Cancel MRD Request", "Test Cancel MRD Request");
		
		try {
			navToTrans(logger,"Misrouted Delivery");
			Browser.waitForBrowserToLoadCompletely(driver);
			
			ssccNum = inputData.get("testdata").get("ssccNum");	
			
			TextBox.getInstance().setText(logger, gspPage.tb_shippingNumber, ssccNum);			
			Button.getInstance().click(logger, gspPage.btn_searchDelivery);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_badges)).size() ;i++) {
				if(driver.findElement(By.xpath(gspPage.lbl_badges+"["+i+"]")).getText().equalsIgnoreCase("Ready")) {
					System.out.println("Clicking Request @ Index : " + ((i+1)/2));
					driver.findElement(By.xpath(gspPage.btn_viewRequest+"["+((i+1)/2)+"]")).click();;
				} else {
					continue;
				}
			}
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.pause(logger, "2");
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionCancelRequest);
			Browser.pause(logger, "2");
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveMRDRequest);
			Browser.pause(logger, "2");

			Browser.captureScreenShot(logger, driver, "Cancel MRD Request " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.msgCanceledMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgCanceledMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgCanceledMRDRequest + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - cancelMRDRequest" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_reuseCanceledSSCC() {
		ExtentTest logger = extent.startTest("Test Submit MRD - Reuse canceled SSCC", "Test Submit MRD - Reuse canceled SSCC");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "2");
			
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_saveMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_saveMRDRequest);		
			Browser.pause(logger, "2");
			
			if((driver.findElements(By.xpath(gspPage.msgSaveMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgSaveMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgSaveMRDRequest + " is NOT displayed");	
			}
			
			Browser.captureScreenShot(logger, driver, "Submit MRD Request Saved Re-Use SSCC" + Instant.now().getEpochSecond());
			Browser.pause(logger, "2");
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);	
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirmRequest);	
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_confirmMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_confirmMRDRequest);		
			Browser.pause(logger, "2");
			
			if((driver.findElements(By.xpath(gspPage.msgConfirmMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgConfirmMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgConfirmMRDRequest + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "Submit MRD Request Confirmed Re-Use SSCC" + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_reuseCanceledSSCC" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_BarcodeEnteredBeforeSSCC() {
		ExtentTest logger = extent.startTest("Test MRD validation - Barcode Entered Before SSCC", "Test MRD validation - Barcode Entered Before SSCC");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
					
			Browser.captureScreenShot(logger, driver, "Barcode Entered Before SSCC " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBarcodeEnteredBeforeSSCC)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBarcodeEnteredBeforeSSCC + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBarcodeEnteredBeforeSSCC + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_BarcodeEnteredBeforeSSCC" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitMRD_BarcodeEnteredBeforeDestinationSite() {
		ExtentTest logger = extent.startTest("Test MRD validation - Barcode Entered Before Destination Site", "Test MRD validation - Barcode Entered Before Destination Site");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");	
					
			openCreateNewMRD();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
					
			Browser.captureScreenShot(logger, driver, "Barcode Entered Before Destination Site " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBarcodeEnteredBeforeDestinationSite)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBarcodeEnteredBeforeDestinationSite + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBarcodeEnteredBeforeDestinationSite + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitMRD_BarcodeEnteredBeforeDestinationSite" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	/**
	 *  
	 * 
	 * This is test methods is for ND
	 * 
	 * 
	 */
	@Test
	public void ndSearchNoResult() {
		ExtentTest logger = extent.startTest("Test ND Search Functionality - No result", "Test ND Search Functionality - No result");
		
		try {
			navToTrans(logger,"No Document Delivery");	
			Browser.waitForBrowserToLoadCompletely(driver);		
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_shippingNumber, "1234567890");
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_searchDelivery);
			Browser.pause(logger, "2");
			if((driver.findElements(By.xpath(gspPage.txt_noResult)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - " + gspPage.txt_noResult + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - " + gspPage.txt_noResult + " is NOT displayed");
			}
			Browser.captureScreenShot(logger, driver, "ND Search No Result " + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - NDSearchNoResult " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
	}
	
	@Test
	public void ndSearchWithResult() {
		ExtentTest logger = extent.startTest("Test ND Search Functionality - with result", "Test ND Search Functionality - with result");
		
		try {
			navToTrans(logger,"No Document Delivery");	

			frmDate = inputData.get("testdata").get("fromDate");
			gspPage.btn_fromDate.sendKeys(frmDate);
			
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_searchDelivery);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_searchDelivery);
			Browser.pause(logger, "1");
			if((driver.findElements(By.xpath(gspPage.txt_noResult)).size()>0)) {
				logger.log(LogStatus.FAIL, "Test FAILED - " + gspPage.txt_noResult + " is displayed");
			} else {
				logger.log(LogStatus.PASS, "Test PASSED - " + gspPage.txt_noResult + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "ND Search With Result " + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - NDSearchWithResult " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void openCreateNewND() {
		ExtentTest logger = extent.startTest("Test open ND - Create New screen", "Test open ND - Create New screen");
		
		try {
			navToTrans(logger,"No Document Delivery");	
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_createNew);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_createNew);
			Browser.pause(logger, "1");
			url = driver.getCurrentUrl();
			if(url.contains("nodocument_delivery/create") && (driver.findElements(By.xpath(gspPage.ndCreateNew)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - ND Create New Page loaded - " + gspPage.ndCreateNew + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - ND Create New Page NOT loaded - " + gspPage.ndCreateNew + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "ND Create New Screen");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - openCreateNewND " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	} 
	
	@Test
	public void submitND_blankAllFields() {
		ExtentTest logger = extent.startTest("Test Submit Blank ND requests", "Test Submit Blank ND requests");
		
		try {
			openCreateNewND();
			Browser.waitForElementIsClickable(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			Browser.captureScreenShot(logger, driver, "Submit Blank ND requests " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBlankMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBlankMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBlankMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_blankAllFields " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_blankDeliveryNumber() {
		ExtentTest logger = extent.startTest("Test Submit ND with blank Delivery Number", "Test Submit ND with blank Delivery Number");
		
		try {
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit ND blank Delivery Number " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errMissingRequiredFieldskMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errMissingRequiredFieldskMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errMissingRequiredFieldskMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_blankDeliveryNumber" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_blankSSCCNumber() {
		ExtentTest logger = extent.startTest("Test Submit ND with blank SSCC Number", "Test Submit ND with blank SSCC Number");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setText(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit ND blank SSCC Number " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBlankMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBlankMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBlankMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_blankSSCCNumber" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_blankDestinationSite() {
		ExtentTest logger = extent.startTest("Test Submit ND with blank Destination Site", "Test Submit ND with blank Destination Site");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setText(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit ND blank Destination Site " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errMissingRequiredFieldskMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errMissingRequiredFieldskMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errMissingRequiredFieldskMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_blankDestinationSite" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_emptyBarCode() {
		ExtentTest logger = extent.startTest("Test Submit ND with empty BarCode", "Test Submit ND with empty BarCode");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit ND empty BarCode " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errEmptyBarCode)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errEmptyBarCode + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errEmptyBarCode + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_emptyBarCode" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_invalidSSCCLessThan18Digit() {
		ExtentTest logger = extent.startTest("Test Submit ND with Invalid SSCC - Less than 18 digits", "Test Submit ND with Invalid SSCC - Less than 18 digits");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, "" + Instant.now().getEpochSecond());
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit ND Invalid SSCC Less than 18 Digit " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBlankMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBlankMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBlankMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_invalidSSCCLess18Digit" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_invalidSSCCMoreThan18Digit() {
		ExtentTest logger = extent.startTest("Test Submit ND with Invalid SSCC - More than 18 digits", "Test Submit ND with Invalid SSCC - More than 18 digits");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum + Instant.now().getEpochSecond());
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit ND Invalid SSCC More than 18 Digit " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBlankMRD)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBlankMRD + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBlankMRD + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_invalidSSCCMoreThan18Digit" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_NDAlreadyExist() {
		ExtentTest logger = extent.startTest("Test Submit ND - Already Exist", "Test Submit ND - Already Exist");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "1");
			
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_saveMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_saveMRDRequest);		
			Browser.pause(logger, "1");
			
			Browser.captureScreenShot(logger, driver, "Submit ND Already Exist " + Instant.now().getEpochSecond());
			Browser.pause(logger, "1");
			if((driver.findElements(By.xpath(gspPage.errNDAlreadyExist)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errNDAlreadyExist + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errNDAlreadyExist + " is NOT displayed");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_NDAlreadyExist" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_saveConfirmRequest() {
		ExtentTest logger = extent.startTest("Test Submit ND - Request Saved and Confirm", "Test Submit ND - Request Saved and Confirm");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "2");
			
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_saveMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_saveMRDRequest);		
			Browser.pause(logger, "2");
			
			if((driver.findElements(By.xpath(gspPage.msgSaveMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgSaveMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgSaveMRDRequest + " is NOT displayed");	
			}
			
			Browser.captureScreenShot(logger, driver, "Submit ND Request Saved " + Instant.now().getEpochSecond());
			Browser.pause(logger, "2");
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);	
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirmRequest);	
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_confirmMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_confirmMRDRequest);		
			Browser.pause(logger, "2");
			
			if((driver.findElements(By.xpath(gspPage.msgConfirmMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgConfirmMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgConfirmMRDRequest + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "Submit ND Request Confirmed " + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_aaveConfirmRequest" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void cancelNDRequest() {
		ExtentTest logger = extent.startTest("Test Cancel ND Request", "Test Cancel ND Request");
		
		try {
			navToTrans(logger,"No Document Delivery");
			Browser.waitForBrowserToLoadCompletely(driver);
			
			ssccNum = inputData.get("testdata").get("ssccNum");	
			
			TextBox.getInstance().setText(logger, gspPage.tb_shippingNumber, ssccNum);			
			Button.getInstance().click(logger, gspPage.btn_searchDelivery);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			// find index of view button to click
			for(int i=1; i<= driver.findElements(By.xpath(gspPage.lbl_badges)).size() ;i++) {
				if(driver.findElement(By.xpath(gspPage.lbl_badges+"["+i+"]")).getText().equalsIgnoreCase("Ready")) {
					System.out.println("Clicking Request @ Index : " + ((i+1)/2));
					driver.findElement(By.xpath(gspPage.btn_viewRequest+"["+((i+1)/2)+"]")).click();;
				} else {
					continue;
				}
			}
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.pause(logger, "2");
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionCancelRequest);
			Browser.pause(logger, "2");
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveMRDRequest);
			Browser.pause(logger, "2");

			Browser.captureScreenShot(logger, driver, "Cancel ND Request " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.msgCanceledMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgCanceledMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgCanceledMRDRequest + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - cancelNDRequest" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_reuseCanceledSSCC() {
		ExtentTest logger = extent.startTest("Test Submit ND - Reuse canceled SSCC", "Test Submit ND - Reuse canceled SSCC");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_saveRequest);
			Browser.pause(logger, "2");
			
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_saveMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_saveMRDRequest);		
			Browser.pause(logger, "2");
			
			if((driver.findElements(By.xpath(gspPage.msgSaveMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgSaveMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgSaveMRDRequest + " is NOT displayed");	
			}
			
			Browser.captureScreenShot(logger, driver, "Submit ND Request Saved Re-Use SSCC" + Instant.now().getEpochSecond());
			Browser.pause(logger, "2");
			
			Browser.waitForBrowserToLoadCompletely(driver);
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_action);	
			Browser.clickElementJSExecutor(logger, driver, gspPage.btn_actionConfirmRequest);	
			Browser.waitForElementIsVisible(logger, driver, gspPage.btn_confirmMRDRequest);
			Button.getInstance().click(logger, gspPage.btn_confirmMRDRequest);		
			Browser.pause(logger, "2");
			
			if((driver.findElements(By.xpath(gspPage.msgConfirmMRDRequest)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.msgConfirmMRDRequest + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.msgConfirmMRDRequest + " is NOT displayed");	
			}
			Browser.captureScreenShot(logger, driver, "Submit ND Request Confirmed Re-Use SSCC" + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_reuseCanceledSSCC" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_BarcodeEnteredBeforeSSCC() {
		ExtentTest logger = extent.startTest("Test ND validation - Barcode Entered Before SSCC", "Test ND validation - Barcode Entered Before SSCC");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");
			destinationSite = inputData.get("testdata").get("destinationSite");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			Button.getInstance().click(logger, gspPage.dd_destinationSite);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_destinationSite, destinationSite);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
					
			Browser.captureScreenShot(logger, driver, "Barcode Entered Before SSCC " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBarcodeEnteredBeforeSSCC)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBarcodeEnteredBeforeSSCC + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBarcodeEnteredBeforeSSCC + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_BarcodeEnteredBeforeSSCC" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void submitND_BarcodeEnteredBeforeDestinationSite() {
		ExtentTest logger = extent.startTest("Test ND validation - Barcode Entered Before Destination Site", "Test ND validation - Barcode Entered Before Destination Site");
		
		try {
			deliveryNum = inputData.get("testdata").get("deliveryNum");	
			ssccNum = inputData.get("testdata").get("ssccNum");	
			barCode = inputData.get("testdata").get("barCode");
			qty = inputData.get("testdata").get("qty");	
					
			openCreateNewND();
			
			TextBox.getInstance().setText(logger, gspPage.tb_deliveryNumber, deliveryNum);
			TextBox.getInstance().setText(logger, gspPage.tb_ssccNumber, ssccNum);
			TextBox.getInstance().setText(logger, gspPage.tb_scannedQty, qty);
			TextBox.getInstance().setTextAndEnter(logger, gspPage.tb_barCode, barCode);
					
			Browser.captureScreenShot(logger, driver, "Barcode Entered Before Destination Site " + Instant.now().getEpochSecond());
			if((driver.findElements(By.xpath(gspPage.errBarcodeEnteredBeforeDestinationSite)).size()>0)) {
				logger.log(LogStatus.PASS, "Test PASSED - Expected Error message displayed" + gspPage.errBarcodeEnteredBeforeDestinationSite + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Expected Error message is NOT displayed" + gspPage.errBarcodeEnteredBeforeDestinationSite + " is NOT displayed");	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - submitND_BarcodeEnteredBeforeDestinationSite" + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	/**
	 * 
	 * 
	 * This is methods are shared by multiple modules
	 * 
	 * 
	 */
	
	@Test
	public void invalidPasswordLogin() {
		ExtentTest logger = extent.startTest("Test Log in with Invalid Password", "Test Log in with Invalid Password");
		navigate(logger);
		try {
			String username = inputData.get("testdata").get("username");	
			String password = "1nc0rr3ctP4$5w0rd";	
			gspPage.loginGSP(logger, username, password);
			
			if(gspPage.err_invalidLogin.isDisplayed()) {
				logger.log(LogStatus.PASS, "Test PASSED " + gspPage.err_invalidLogin.getText() + " is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - Invalid Password not catched");
			}
			Browser.captureScreenShot(logger, driver, "GSP invalid Password Login" + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - invalidPasswordLogin " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void successfulLogin() {
		ExtentTest logger = extent.startTest("Test Successful Log in", "Test Successful Log in");
		
		try {
			String username = inputData.get("testdata").get("username");	
			String password = inputData.get("testdata").get("password");	
			gspPage.loginGSP(logger, username, password);
			Browser.waitForBrowserToLoadCompletely(driver);
			if(gspPage.txt_homePage.isDisplayed()) {
				logger.log(LogStatus.PASS, "Test PASSED - Home Page is displayed");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED");
			}
			Browser.captureScreenShot(logger, driver, "GSP Successful Login" + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - successfulLogin " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void logOutGSP() {
		ExtentTest logger = extent.startTest("Test Log out of GSP Page", "Test Log out of GSP Page");
		try {
			while(!gspPage.btn_logOut.isDisplayed()) {
				Button.getInstance().click(logger, gspPage.btn_profile);
				Browser.pause(logger, "1");
			}
			Button.getInstance().click(logger, gspPage.btn_logOut);
			Browser.waitForBrowserToLoadCompletely(driver);
			
			url = driver.getCurrentUrl();
			if(url.contains("login")) {
				logger.log(LogStatus.PASS, "Test PASSED - logged out of GSP Successfully");
			} else {
				logger.log(LogStatus.FAIL, "Test FAILED - unable to out of GSP");	
			}
			Browser.captureScreenShot(logger, driver, "GSP Log Out" + Instant.now().getEpochSecond());
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - logOutGSP " + Instant.now().getEpochSecond());
		}
		driver.close();
		driver.quit();
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void openTransDeliveries() {
		ExtentTest logger = extent.startTest("Test Opening Transactions > Deliveries module", "Test Opening Transactions > Deliveries module");
		
		try {
			navToTrans(logger,"Deliveries");	
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "FE - openTransDeliveries " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void openTransMisroutedDelivery() {
		ExtentTest logger = extent.startTest("Test Opening Transactions > Misrouted Delivery module", "Test Opening Transactions > Misrouted Delivery module");
		
		try {
			navToTrans(logger,"Misrouted Delivery");	
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "Fail " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	@Test
	public void openTransNoDocumentDelivery() {
		ExtentTest logger = extent.startTest("Test Opening Transactions > No Document Delivery module", "Test Opening Transactions > No Document Delivery module");
		
		try {
			navToTrans(logger,"No Document Delivery");	
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			Browser.captureScreenShot(logger, driver, "Fail " + Instant.now().getEpochSecond());
		}
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}

	public void navToTrans(ExtentTest logger,String transModule) {
		driver.navigate().refresh();
		Button.getInstance().click(logger, gspPage.btn_transactions);
		Browser.waitForElementIsClickable(logger, driver, gspPage.link_transactions);
		Link.getInstance().click(logger, gspPage.link_transactions);
		Browser.pause(logger, "1");
		switch(transModule) {
			case "Deliveries":
				Link.getInstance().click(logger, gspPage.link_deliveries);
				break;
			case "Misrouted Delivery":
				Link.getInstance().click(logger, gspPage.link_misroutedDelivery);
				break;
			case "No Document Delivery":
				Link.getInstance().click(logger, gspPage.link_noDocumentDelivery);
				break;
		}
		Browser.captureScreenShot(logger, driver, transModule);
		Browser.waitForBrowserToLoadCompletely(driver);
		
	}

}