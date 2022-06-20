package com.Shopify.core;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author bryan.adante
 *
 */
public class Browser {
	public WebDriver driver;
	public static String parentHandleer;
	private static final String CHROMEDRIVER_PATH = System.getProperty("user.dir") + "\\drivers\\chromedriver.exe";

	/**
	 * This method will open New Browser based on broswerName Parameter
	 * 
	 * @param browserName
	 * @return WebDriver
	 */
	public WebDriver startBrowser(ExtentTest logger, String browserName) {
		try {
			if (browserName.equalsIgnoreCase("chrome")) {

				System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH);
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);

				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
				chromeOptions.setExperimentalOption("prefs", chromePrefs);
				chromeOptions.addArguments("start-maximized");
				chromeOptions.addArguments("mute-audio");
				chromeOptions.addArguments("disable-extensions");
				chromeOptions.addArguments("disable-gpu");
				chromeOptions.addArguments("window-size=1280x1024");
				chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
				chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
				chromeOptions.setAcceptInsecureCerts(true);
				chromeOptions.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				
				driver = new ChromeDriver(chromeOptions);

				logger.log(LogStatus.PASS, "<b>Chrome </b>browser is started ");
			} else if (browserName.equalsIgnoreCase("firefox")) {
				// To do
				logger.log(LogStatus.PASS, "HTML", "<b>Firefox </b>browser is started ");
			} else if (browserName.equalsIgnoreCase("ie")) {
				// To do
				logger.log(LogStatus.PASS, "HTML", "<b>IE </b>browser is started ");
			}

			driver.manage().window().maximize();
			logger.log(LogStatus.PASS, "Browser is maximized ");
			System.out.println("startBrowser: " + driver);
			return driver;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Failed to lunch browser due to <b style='color:red'>" + e.getClass() + "<br>"
					+ e.getMessage() + "</b>");
			return null;
		}
	}

	/**
	 * This method will used to navigate to given URL
	 * 
	 * @param url
	 */
	public void navigate(ExtentTest logger, String url) {
		try {
			driver.navigate().to(url);
			logger.log(LogStatus.PASS, "Navigating to <b>" + url + "</b>");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Failed to Navigate <b>" + url + "</b> due to <b style='color:red'>"
					+ e.getClass() + "<br>" + e.getMessage() + "</b>");
		}
	}

	/**
	 * This method is for web elements that are clickable
	 * 
	 * @param logger
	 * @param driver
	 * @param element
	 */
	public static void waitForElementIsClickable(ExtentTest logger, WebDriver driver, WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is for web elements that are NOT clickable
	 * 
	 * @param logger
	 * @param driver
	 * @param element
	 */
	public static void waitForElementIsVisible(ExtentTest logger, WebDriver driver, WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is for forcing Java to sleep
	 * 
	 * @param logger
	 * @param waitTime
	 */
	public static void pause(ExtentTest logger, String waitTime) {
		long time = (long) Double.parseDouble(waitTime);
		try {
			Thread.sleep(time * 1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.log(LogStatus.PASS, "Wait <b>" + waitTime + "</b> Seconds");
	}

	/**
	 * This method is for scrolling into view web elements from the browser
	 * 
	 * @param logger
	 * @param driver
	 * @param element
	 */
	public static void scrollElementIntoView(ExtentTest logger, WebDriver driver, WebElement element) {
		if (element == null) {
			logger.log(LogStatus.FAIL, element + " Web Element is NOT displayed");
		} else {
			JavascriptExecutor executr = (JavascriptExecutor) driver;
			executr.executeScript("arguments[0].scrollIntoView(true);", element);
		}
	}

	/**
	 * This method is for scrolling into view web elements from the browser
	 * 
	 * @param logger
	 * @param driver
	 * @param element
	 */
	public static void clickElementJSExecutor(ExtentTest logger, WebDriver driver, WebElement element) {
		if (element == null) {
			logger.log(LogStatus.FAIL, element + " Web Element is NOT displayed");
		} else {
			JavascriptExecutor executr = (JavascriptExecutor) driver;
			executr.executeScript("arguments[0].click();", element);
		}
	}

	/**
	 * This method is for switching frame from the browser
	 * 
	 * @param logger
	 * @param driver
	 * @param element
	 */
	public static void switchFrame(ExtentTest logger, WebDriver driver, WebElement element) {
		if (element == null) {
			logger.log(LogStatus.FAIL, element + " Frame is NOT displayed");
		} else {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
			logger.log(LogStatus.PASS, "Switch to frame " + element);
		}
	}

	/**
	 * This method is for switching frame from the browser
	 * 
	 * @param logger
	 * @param driver
	 * @param element
	 */
	public static void switchDefaultFrame(ExtentTest logger, WebDriver driver) {
		System.out.println("switchDefaultFrame: " + driver);
		driver.switchTo().defaultContent();
	}

	/**
	 * This method will take the screen shot
	 * 
	 * @param logger
	 * @param driver
	 * @param fileName
	 */
	public static void captureScreenShot(ExtentTest logger, WebDriver driver, String fileName) {
		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			logger.log(LogStatus.PASS, "Screen shot taken");

			FileUtils.copyFile(scrFile, new File(fileName));

			logger.log(LogStatus.INFO, "Screen shot Path : <b>" + fileName + "</b>");

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Failed To take Screen shot due to <b style='color:red'>" + e.getClass() + "<br>"
					+ e.getMessage() + "</b>");
		}
	}

	public static void waitForBrowserToLoadCompletely(WebDriver driver) {
		String state = null;
		String oldstate = null;
		try {
			int i = 0;
			while (i < 5) {
				Thread.sleep(1000);
				state = ((JavascriptExecutor) driver).executeScript("return document.readyState;").toString();
				System.out.println("state: " + state);
				System.out.println("i: " + i);
				if (state.equals("interactive") || state.equals("loading"))
					break;
				if (i == 1 && state.equals("complete")) {
					return;
				}
				i++;
			}
			i = 0;
			oldstate = null;
			Thread.sleep(1000);
			while (true) {
				state = ((JavascriptExecutor) driver).executeScript("return document.readyState;").toString();
				System.out.println("state: " + state);
				System.out.println("i: " + i);
				if (state.equals("complete"))
					break;
				if (state.equals(oldstate))
					i++;
				else
					i = 0;
				if (i == 15 && state.equals("loading")) {
					driver.navigate().refresh();
					i = 0;
				} else if (i == 6 && state.equals("interactive")) {
					return;
				}
				Thread.sleep(1000);
				oldstate = state;
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	/**
	 * This method is for web elements that are NOT clickable
	 * 
	 * @param logger
	 * @param driver
	 * @param element
	 */
	public static void waitForDefaultFrameToLoadCompletely(ExtentTest logger, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='mainScrollableDiv']")));
			System.out.println("Default Frame is loaded");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Frame is not loaded completely");
		}
	}

	/**
	 * This method is for web elements that are NOT clickable
	 * 
	 * @param logger
	 * @param driver
	 * @param element
	 */
	public static void waitForFrameToLoadCompletely(ExtentTest logger, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@class='mainScrollableDiv']")));
			System.out.println("Frame is loaded");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Frame is not loaded completely");
		}
	}

	/**
	 * This method is for web elements that are NOT clickable
	 * 
	 * @param logger
	 * @param driver
	 * @param element
	 */
	public static void waitForOpenedFrameToLoadCompletely(ExtentTest logger, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
					By.xpath(".//*[@id='mainScrollableDiv']")));
			System.out.println("Opened iFrame is loaded");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Frame is not loaded completely");
		}
	}

	public static void waitFrame(ExtentTest logger, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(".//*[@id='mainScrollableDiv']")));
			System.out.println("Frame is loaded");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Frame is not loaded completely");
		}
	}

	public static void waitForLoading(ExtentTest logger, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(text(),'MuiCircularProgress')]")));
			System.out.println("waitForLoading is finished");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Frame is not loaded completely");
		}
	}

	public static void waitForAdminPortal(ExtentTest logger, WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(text(),'Back to app')]")));
			System.out.println("waitForAdmninPortal is loaded");
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(LogStatus.FAIL, "Frame is not loaded completely");
		}
	}
	
	/**
	 * This method will return Parent window handler
	 * @author k.narasimha.swamy
	 * @param logger
	 * @param driver
	 * @return
	 */
	public static  String getParentWindowHandle(ExtentTest logger,WebDriver driver)
	{
		try
		{
			parentHandleer = driver.getWindowHandle();
			logger.log(LogStatus.PASS,  "The parent window handler is <b>"+parentHandleer+"</b> Success");
			return parentHandleer;
		}catch(Exception e)
		{
			System.out.println("Error...");
		}
		return "";
	}
	
	public  static void switchToChildWindow(ExtentTest logger,WebDriver driver,String parentHandle)
    {
		try {			
			for (String winHandle : driver.getWindowHandles())
			{	
				if (!winHandle.equals(parentHandleer)) { 
//					driver.switchTo().window(parentHandleer);
					driver.switchTo().window(winHandle);
					logger.log(LogStatus.PASS,  "Swiching to child window is  Success");
				}
			}
		}			
		catch (Exception e) 
		{				
			System.out.println("Error...");
		}
    }
	
	public static  void switchToParentWindowHandle(ExtentTest logger,WebDriver driver,String parentHandle)
	{
		try{	
			//System.out.println("Switching back to parent Window-----------");
			driver.switchTo().window(parentHandle);	
			logger.log(LogStatus.PASS,  "Swiching to parent window <b>"+parentHandle+"</b>is  Success");
			//System.out.println("Switching back to parent Window");
		}catch(Exception e)
		{
			System.out.println("Error...");
		}		
	}
	
	
	public static void attachFileOnSpan(ExtentTest logger, WebDriver driver, WebElement we, String path) {
        // TODO Auto-generated method stub
        //use this method for attach file with span tag
        try{
            //click the attach icon / link
        	Browser.pause(logger, "1");
            we.click();
            Browser.pause(logger, "1");
            
            //Copy file path to clipboard
            StringSelection ss = new StringSelection(path);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
            Robot robot = new Robot();

            //simulate keyboard press "Ctrl + V" to paste clipboard content
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            Browser.pause(logger, "1");
            
            //simulate keyboard Enter
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Browser.pause(logger, "10");
           
        }    catch (Exception e) {
                e.printStackTrace();
        }    
    }
	
}
