package com.Shopify.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.Shopify.core.Browser;
import com.Shopify.webelements.Button;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
/**
 * 
 * @author bryan.s.adante
 *
 */
public class HomePage {
	
    /*Feed Page Elements
	For naming button use btn_nameOfButton
	For naming text field use tf_nameOfTextField
	For naming radio button use rb_nameOfRadioButton
	For naming drop-down use dd_nameOfDropDown
	For naming links use lnk_nameOfLink */
	
	@FindBy(xpath="//*[contains(text(),'No thanks')]")
	public WebElement lnk_closeOffer;
	@FindBy(xpath="//*[@id=\"shopify-section-template--15551142035694__password-header\"]/div/div/header/div/div/div[2]/a")
	public WebElement btn_password;
	@FindBy(id="password")
	public WebElement tf_password;	
	public String txt_password = "omni123";
	
	@FindBy(xpath="//*[contains(@href,'search') and @class='site-nav__link site-nav__link--icon js-search-header js-no-transition']")
	public WebElement icn_Search;
	@FindBy(xpath="//*[@placeholder='Search our store']")
	public WebElement tf_search;
	@FindBy(xpath="//*[@id=\"shopify-section-template--15551142428910__main\"]/div[1]/div/div/div[1]/div/a/div[1]/div[4]/img")
	public WebElement btn_searchSKUThumbnail;
//	public String img_searchResults = "//*[contains(@srcset,'";		
	@FindBy(xpath="//*[@name='add']")
	public WebElement btn_addToCart;
	@FindBy(xpath="//*[contains(text(),'Review Cart')]")
	public WebElement btn_reviewCart;	
	@FindBy(xpath="//*[@name='checkout']")
	public WebElement btn_checkOut;
	
	//Payment Screen
	@FindBy(id="checkout_email")
	public WebElement tf_email;
	@FindBy(id="checkout_shipping_address_first_name")
	public WebElement tf_firstName;
	@FindBy(id="checkout_shipping_address_last_name")
	public WebElement tf_lastName;
	@FindBy(id="checkout_shipping_address_company")
	public WebElement tf_houseNumber;
	@FindBy(id="checkout_shipping_address_province")
	public WebElement tf_province;
	@FindBy(id="custom-city-select")
	public WebElement tf_townOrCity;
	@FindBy(id="custom-bangaray-select")
	public WebElement tf_barangay;
	@FindBy(id="checkout_shipping_address_phone")
	public WebElement tf_phone;
	@FindBy(xpath="//*[@class='btn__content']")
	public WebElement btn_continueToShipping;
	
	@FindBy(xpath="//*[contains(text(),'Continue to payment') and @class=\"btn__content\"]")
	public WebElement btn_continueToPayment;
	
	//select payment method
	@FindBy(xpath="//*[contains(text(),'Cash on Delivery (COD)')]")
	public WebElement btn_cashOnDelivery;
	@FindBy(id="continue_button")
	public WebElement btn_completeOrder;
	
	
	//Order# Screen
	@FindBy(xpath="//*[@class='os-order-number']")
	public WebElement txt_completeOrder;
	
}


