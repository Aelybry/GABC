package com.GABC.pageobjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

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
public class LoginPage {

	/*
	 * Web Elements of Login Page
	 */
	
	@FindBy(id="CustomerEmail")
	private WebElement tf_userName;
	
	@FindBy(id="CustomerPassword")
	private WebElement tf_password;
	
	@FindBy(xpath="//*[@value='Sign In']")
	private WebElement btn_signIn;
	
	/**
	 * This method is used to login in login.do
	 * 
	 * @param userName
	 * @param password
	 */
	public void login(ExtentTest logger, String userName, String password) {
		try {
			TextBox.getInstance().setText(logger, tf_userName, userName);
			logger.log(LogStatus.INFO, "HTML", "User name : <b>" + userName + "</b> entered in the username field");		
		
			TextBox.getInstance().setPassword(logger, tf_password, password);
			logger.log(LogStatus.INFO, "HTML", "Password : <b>****************</b> entered in the password field");
						
			Button.getInstance().click(logger, btn_signIn);
			logger.log(LogStatus.INFO, "<b>Sign In</b> button cliked");
			
		} catch (Exception e) {

		}
	}
}
