package com.GABC.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.GABC.core.BaseTest;
import com.GABC.core.Browser;
import com.GABC.webelements.Button;
import com.GABC.webelements.TextBox;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author bryan.s.adante
 *
 */
public class PaymongoPage extends BaseTest {

	/*
	 * Web Elements of Login Page
	 */
	// Welelements for Credit Card
	@FindBy(xpath="//*[contains(text(),'Debit Card')]")
	public WebElement btn_creditDebitCard;
	public String txt_creditDebitCard = "//*[contains(text(),'Debit Card')]";
	
	@FindBy(id="cardNumber") 
	private WebElement tf_cardNumber;
	
	@FindBy(id="expMonth")
	private WebElement tf_expMonth;
	
	@FindBy(id="expYear")
	private WebElement tf_expYear;
	
	@FindBy(id="cvc")
	private WebElement tf_cvc;
	
	@FindBy(xpath="//*[contains(text(),'Complete Payment')]")
	private WebElement btn_completePayment;
	
	@FindBy(xpath="//*[contains(text(),'Redirect back to merchant')]")
	private WebElement btn_redirectBackToMerchant;
	
	public String txt_cardNumber = "4343434343434345";
	public String txt_expMonth = "12";
	public String txt_expYear = "26";
	public String txt_cvc = "789";
	
	//Web elements for Gcash PAyment Method
	@FindBy(xpath="//*[@class='payment-method-content' and contains(text(),'GCash')]")
	public WebElement btn_gCash;
	
	@FindBy(id="email")
	private WebElement tf_gcashEmail;
	public String txt_gcashEmail = "automation.goldenabc@gmail.com";
	
	@FindBy(id="name")
	private WebElement tf_gcashName;
	public String txt_gcashName = "Test Automation";
	
	@FindBy(id="e-wallet-button")
	private WebElement btn_authorizePayment;
	
	@FindBy(id="privacy-policy")
	private WebElement cb_privacyPolicy;
	
	@FindBy(xpath="//*[contains(text(),'Continue')]")
	private WebElement btn_continue;
	
	/**
	 * This method is used to complete payment on  Paymongo Page
	 * 
	 */
	public void completePayment(ExtentTest logger, WebDriver driver){
		try {		
			System.out.println("Paymongo Page - Credit/Debit Payment");
//			Browser.waitForElementIsClickable(logger, driver, btn_creditDebitCard);
//			Button.getInstance().click(logger, btn_creditDebitCard);
			Browser.waitForElementIsClickable(logger, driver, btn_continue);
			Button.getInstance().click(logger, btn_continue);
//			Browser.waitForElementIsClickable(logger, driver, btn_continue);
//			Button.getInstance().click(logger, btn_continue);
			Browser.waitForElementIsVisible(logger, driver, tf_cardNumber);
			TextBox.getInstance().setText(logger, tf_cardNumber, txt_cardNumber);		
			TextBox.getInstance().setText(logger, tf_expMonth, txt_expMonth);
			TextBox.getInstance().setText(logger, tf_expYear, txt_expYear);
			TextBox.getInstance().setText(logger, tf_cvc, txt_cvc);	
			Button.getInstance().click(logger, cb_privacyPolicy);
			Browser.waitForElementIsClickable(logger, driver, btn_completePayment);
			Button.getInstance().click(logger, btn_completePayment);
			Browser.waitForElementIsClickable(logger, driver, btn_redirectBackToMerchant);
			Button.getInstance().click(logger, btn_redirectBackToMerchant);
			Browser.waitForBrowserToLoadCompletely(null);			
		} catch (Exception e) {

		}
	}
}
