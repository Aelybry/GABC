package com.Shopify.core;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.Shopify.pageobjects.HomePage;
import com.Shopify.pageobjects.LoginPage;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;

/**
 * 
 * @author bryan.s.adante
 *
 */
public class BaseTest {

	public WebDriver driver;
	public Browser browser;

	// Variables for taking Data from Excel
	public Map<String, Map<String, String>> inputData;
	private Xls_Reader xl;
	private int startRow = 1;
	private int headerRow = 1;
	private int endRow = 2;

	// Variable for reports
	public ExtentReports extent;
	public DateFormat dateFormat;
	public Date date;
	static String currentPath = "";
	public static String currentDataObjectsPath = "";
	static String suiteFolderPath = "";

	// Variables for PageObjects
	public LoginPage login;
	public HomePage homePage;

	// Constructor

	public BaseTest() {
		date = new Date();
		dateFormat = new SimpleDateFormat("ddMMyyyy-HH_mm_ss");

		// Current Project Path
		currentPath = System.getProperty("user.dir");

		//Current DataObjects Path
//		currentDataObjectsPath = System.getProperty("user.dir")+"\\src\\test\\java\\com\\Shopify\\dataobjects\\";
//		
		// Creating Map object for storing the Excel data
		inputData = new HashMap<String, Map<String, String>>();

		// Initialize Browser class
		browser = new Browser();
	}

	/**
	 * This is method for Getting Suite Name form XML
	 * 
	 * @param context
	 */
	@BeforeSuite
	public void getSuiteName(ITestContext context) {
		String suiteName = context.getCurrentXmlTest().getSuite().getName();

		suiteConfig(suiteName);
		// Print suite Name
		System.out.println("@@@ Suite Name : " + suiteName);
	}

	public void getinputsheetname(String inputsheet) {
		xl = new Xls_Reader(currentPath + "\\src\\test\\java\\com\\Shopify\\dataobjects\\" + inputsheet);
	}

	@Parameters({ "inputsheetname" })
	@BeforeTest
	public void setInputSheetName(String testsheetName) {
		getinputsheetname(testsheetName);

		System.out.println("@@@ Test Sheet Name: " + testsheetName);
	}

	@Parameters({ "testcaseName" })
	@BeforeTest
	public void setUp(String testcaseName, ITestContext context) {
		// Reading Data from Excel
		getInputDataFromExcel(testcaseName);

		// Getting Test name from XML
		String testName = context.getName();

		// Reports configuration
		reportsConfig(testName);

		System.out.println("@@@ Test Case Name: " + testcaseName);
		System.out.println("@@@ Test  Name: " + testName);

	}

	/**
	 * This method for creating a new folder with Suite name
	 * 
	 * @param suiteName
	 */
	public void suiteConfig(String suiteName) {
		suiteFolderPath = currentPath + "//Reports//" + suiteName + "_" + dateFormat.format(date);
		// Create new Directory for Reports with SuiteName
		new File(suiteFolderPath).mkdir();

	}

	public void getInputDataFromExcel(String testcaseName) {
		System.out.println("----------------- This is " + testcaseName + " Data Info -----------------");
		int rowCount = xl.getRowCount("Testdata");
		int colCount = xl.getColumnCount("Testdata");
		System.out.println("rowcount  :" + rowCount + "  colCount  :" + colCount);
		for (int r = 0; r <= rowCount; r++) {

			if (xl.getCellData("Testdata", 0, r).toString().equals(testcaseName)) {
				startRow = r + 1;
				headerRow = r;
				System.out.println("StartRow  : " + startRow);
				break;
			}
		}

		for (int e = startRow; e <= rowCount; e++) {
			if (xl.getCellData("Testdata", 0, e).toString().equals("End")) {
				endRow = e - 1;
				System.out.println("EndRow  :" + endRow);
				break;
			}
		}

		Map<String, String> testCaseValues;

		for (int row = startRow; row <= endRow; row++) {

			testCaseValues = new HashMap<String, String>();

			for (int colValue = 0; colValue < colCount; colValue++) {
				if (!(xl.getCellData("Testdata", colValue, row).isEmpty())) {
					testCaseValues.put(xl.getCellData("Testdata", colValue, headerRow),
							xl.getCellData("Testdata", colValue, row));
				} else {
					testCaseValues.put(xl.getCellData("Testdata", colValue, headerRow),
							xl.getCellData("Testdata", colValue, row));
				}

				inputData.put(xl.getCellData("Testdata", 0, row), testCaseValues);
			}

		}

		System.out.println("Getting input data from  " + "Testdata" + " sheet is SUCCESS");
		System.out.println();
		printMap(testcaseName);
	}

	/**
	 * This method for creating reports file and reports related configurations
	 * 
	 * @param testName
	 */
	public void reportsConfig(String testName) {
		try {
			// Extent reports creation for every test
			extent = new ExtentReports(
					suiteFolderPath + "//" + testName + "_Report_" + dateFormat.format(date) + ".html",
					NetworkMode.ONLINE);
			extent.addSystemInfo("Selenium Version", "3.14.0");
			extent.addSystemInfo("Environment", " ");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method for displaying inputData map values
	 * 
	 * @param testcaseName
	 */
	public void printMap(String testcaseName) {
		System.out.println("*************************************** " + testcaseName
				+ "  test case INPUT DATA *************************************** ");

		for (Map.Entry entry : inputData.entrySet()) {
			System.out.println(entry.getKey() + ", " + entry.getValue());
			System.out.println();
		}

		System.out.println("*************************************** " + testcaseName
				+ "  test case INPUT DATA *************************************** ");
	}

	/**
	 * Opening New Browser and redirecting to Specified URL = login.do
	 */
	public void navigate(ExtentTest logger) {
		driver = browser.startBrowser(logger, inputData.get("navigate").get("Browser").toString());
		browser.navigate(logger, inputData.get("navigate").get("URL").toString());
		driver.manage().timeouts().pageLoadTimeout(1000, TimeUnit.SECONDS);
		Browser.pause(logger, "3");
		Browser.waitForBrowserToLoadCompletely(driver);
		initializePageFactory();
	}

	/**
	 * Redirecting to Specified URL = login.do
	 */
	public void navigateToLogin(ExtentTest logger, WebDriver driver) {
		browser.navigate(logger, inputData.get("navigate").get("URL").toString());
		driver.manage().timeouts().pageLoadTimeout(1000, TimeUnit.SECONDS);
		Browser.waitForBrowserToLoadCompletely(driver);
	}

	/**
	 * This method is where you initialize PageFactory objects
	 */
	public void initializePageFactory() {
		login = PageFactory.initElements(driver, LoginPage.class);
		homePage = PageFactory.initElements(driver, HomePage.class);
		
	}
	
//	@AfterTest
	public void closeBrower() {
		driver.close();
		driver.quit();
	}
}
