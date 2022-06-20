package com.Shopify.webelements;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.Shopify.core.Browser;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author bryan.adante
 *
 */
public class TextBox {
	
	String requestId = "";
	
	private TextBox() {

	}

	private static class TextBoxSingleton {
		private static TextBox INSTANCE = new TextBox();
	}

	public static TextBox getInstance() {
		System.out.println(TextBoxSingleton.INSTANCE);
		return TextBoxSingleton.INSTANCE;
	}

	//****Actions****
	public void setPassword(ExtentTest logger, WebElement we, String data) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we+" TextBox is NOT displayed.");
		} else {
			we.clear();
			we.sendKeys(data);
			logger.log(LogStatus.PASS, we + " TextBox is populated with '********'");
		}
	}
	
	public void setText(ExtentTest logger, WebElement we, String data) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we+" TextBox is NOT displayed.");
		} else {
			we.clear();
			we.click();
			we.sendKeys(data);
			logger.log(LogStatus.PASS, we + " TextBox is populated with '" + data + "'");
		}
	}

	/**
	 * 
	 * @param logger
	 * @param we webelement
	 * @param data test data from excel
	 */
	public void setTextAndEnter(ExtentTest logger, WebElement we, String data) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we+" TextBox is NOT displayed.");
		} else {
			we.clear();
			we.click();
			we.sendKeys(data);
			Browser.pause(logger, "1");
			we.sendKeys(Keys.ENTER);
			logger.log(LogStatus.PASS, we + " TextBox is populated with '" + data + "'");
		}
	}
	
	public void setTextAndTab(ExtentTest logger, WebElement we, String data) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we+" TextBox is NOT displayed.");
		} else {
			we.clear();
			we.click();
			we.sendKeys(data);
			Browser.pause(logger, "1");
			we.sendKeys(Keys.TAB);
			logger.log(LogStatus.PASS, we + " TextBox is populated with '" + data + "'");
		}
	}
	
	public String getAttribute(ExtentTest logger, WebElement we, String attribute) {
		String value = null;
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " TextBox is NOT displayed");
		} else {
			value = we.getAttribute(attribute);
		}
		return value;
	}
	
	//****Verification****
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

	public boolean isTextContains(WebElement we, String text) {
		if (we == null) {
			return false;
		} else {
			return we.getText().contains(text);
		}
	}

	public boolean isTextEquals(WebElement we, String text) {
		if (we == null) {
			return false;
		} else {
			return we.getText().equals(text);
		}
	}

	public void verifyDisplayed(ExtentTest logger, WebElement we) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we+" TextBox is NOT displayed");
		} else {
			logger.log(LogStatus.PASS, we+" TextBox is displayed");
		}
	}

	public void verifyNotDisplayed(ExtentTest logger, WebElement we) {
		if (we == null) {
			logger.log(LogStatus.PASS, we+" TextBox is NOT displayed");
		} else {
			logger.log(LogStatus.FAIL, we+" TextBox is displayed");
		}
	}

	public void verifyTextContains(ExtentTest logger, WebElement we, String text) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we+" TextBox is NOT displayed");
		} else {
			if(we.getText().contains(text)) {
				logger.log(LogStatus.PASS, we+" TextBox text contains <b>"+text+"</b>");
			}else {
				logger.log(LogStatus.FAIL, we+" TextBox text NOT contains <b>"+text+"</b>");
			}
			
		}
	}

	public void verifyTextEquals(ExtentTest logger, WebElement we, String text) {
		if (we == null) {
			logger.log(LogStatus.FAIL, we + " TextBox is NOT displayed");
		} else {
			if (we.getAttribute("value").equals(text)) {
				logger.log(LogStatus.PASS, we + " TextBox text is equal to <b>" + text + "</b>");
			} else {
				logger.log(LogStatus.FAIL, we + " TextBox text is NOT equal to <b>" + text + "</b>");
			}
		}
	}
	
	 public boolean verifyThatTextFieldIsFilled(ExtentTest logger, WebDriver driver, WebElement we){
	    	
	    	try{
	    		String actual=getElementValue(logger, we);
	    		if(actual==null){
	    			logger.log(LogStatus.FAIL,"Expected text field is not filled-"+we);
	    			return false;
	    		}else{
	    			logger.log(LogStatus.PASS,"Expected text field-"+we+" is not left unfilled. The value present in it is-"+actual);
	    			return true;
	    		}
	    		
	    	}catch (Exception e)
		   {
					e.printStackTrace();
					logger.log(LogStatus.FAIL,  "Unable to verifyThatTextFieldIsFilled due to <b style='color:red'>"+e.getClass()+"<br>"+e.getMessage()+"</b>");
					return false;
	      }
	    	
	    }
	 
		/**
		 * This method will return specified element value 
		 * @param logger
		 * @param element
		 * @return
		 */
	  public String getElementValue(ExtentTest logger,WebElement element)
	    {                                                         
	        try 
	        {	  
	             requestId = element.getAttribute("value"); 
	             logger.log(LogStatus.PASS,  "Element value taken : <b>"+requestId+"</b");
	             System.out.println(requestId);
	        } catch (Exception e)
	        {                                                                      
				e.printStackTrace();
				logger.log(LogStatus.FAIL,  "Failed to take Element <b>"+element+"</b>Value due to <b style='color:red'>"+e.getClass()+"<br>"+e.getMessage()+"</b>");
	        } 
	      return requestId;	
	    } 

}
