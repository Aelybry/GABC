package com.Shopify.testobjects;

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

public class GetBarangayLongLatTest extends BaseTest{
	int currNumOfBrgyChecked = 1;
	int endNumOfBrgyToChecked = 2;
	String cellValue = "";
	String brgy = "";
	String url = "";
	
	@SuppressWarnings("deprecation")
	@Test
	public void getLongLat() {
		ExtentTest logger = extent.startTest("Get barangay Long Lat", "Get barangay Long Lat");
		navigate(logger);
		
		try {
			for(int i=currNumOfBrgyChecked;i<endNumOfBrgyToChecked;i++) {
				File src = new File("C:\\Workspace\\Shopify\\src\\test\\java\\com\\Shopify\\dataobjects\\BrgyLngLat.xlsx");			
				FileInputStream fis = new FileInputStream(src);		
				XSSFWorkbook xsf = new XSSFWorkbook(fis);	
				XSSFSheet sheet = xsf.getSheetAt(0);
				//write data
				if(sheet.getRow(i).getCell(0).getCellType()==Cell.CELL_TYPE_BLANK) {
					System.out.println("No more Barangay data to check");
				} else {
					brgy = sheet.getRow(i).getCell(0).getStringCellValue();
					System.out.println("======================== Getting long lat of: " + brgy + " ========================");
					TextBox.getInstance().setTextAndEnter(logger, homePage.tf_searchboxinput, brgy);
					Browser.waitForBrowserToLoadCompletely(driver);
					url = driver.getCurrentUrl();
					check_LonglatLoadedCompletely(logger,url);
					int beginIndex = url.indexOf("@")+1;
					cellValue = url.substring(beginIndex,url.indexOf("z",beginIndex)-3);
					CellUtil.getCell(sheet.getRow(i), 1);
					sheet.getRow(i).getCell(1).setCellValue(cellValue);
					currNumOfBrgyChecked = i+1;
				}
				FileOutputStream fos = new FileOutputStream(src);	
				xsf.write(fos);		
				xsf.close();	
				Browser.captureScreenShot(logger, driver, brgy);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Exception encountered due to: <b style='color:red'>" + e.getClass() + "<br>");
			getLongLat();
		}
		driver.close();
		driver.quit();
		extent.endTest(logger);
		extent.flush();
		Assert.assertEquals(logger.getRunStatus(), LogStatus.PASS);
	}
	
	public void check_LonglatLoadedCompletely(ExtentTest logger,String currUrl) {
		if(currUrl.contains("data=!3m1!4b1!")){
			System.out.println("Google maps loaded long lat completely");
			url = driver.getCurrentUrl();
		} else {
			Browser.pause(logger, ".1");
			System.out.println("Waiting for google maps to load long lat completely");
			check_LonglatLoadedCompletely(logger,driver.getCurrentUrl());
		}
	}

}
