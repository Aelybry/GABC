 package com.GABC.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import com.GABC.core.Browser;
import com.GABC.webelements.Button;
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
	public String btn_bocuPassword = "//*[@id='password']";
	public String txt_password = "boculifestyle";
	@FindBy(xpath="//*[@class='icon icon-user']")
	public WebElement btn_user;	
	
	@FindBy(xpath="//*[contains(@href,'search') and @class='site-nav__link site-nav__link--icon js-search-header js-no-transition']")
	public WebElement icn_Search;
	@FindBy(xpath="//*[@placeholder='Search our store']")
	public WebElement tf_search;
	@FindBy(xpath="//*[@class='grid__image-contain lazyautosizes lazyloaded']")
	public WebElement lnk_searchSkuThumbnail;
	
	public String txt_containsPrefix = "//*[contains(@data-location-title,'";
	public String txt_containsSuffix = "')]";
	public String txt_addressContainsPrefix = "//*[@data-location-handle='";
	public String txt_addressContainsSuffix = "']";
	public String txt_genericPickupStoreXpath = "//*[contains(@id,'checkout_pickup_option_handle_')]";
	
	

	//login credentials
	@FindBy(id="CustomerEmail")
	public WebElement tf_loginEmail;
	@FindBy(id="CustomerPassword")
	public WebElement tf_loginPassword;
	@FindBy(xpath="//*[@type='submit' and @value='Sign In']")
	public WebElement btn_signIn;	
	
	
	@FindBy(xpath="//label[contains(text(),'Quantity')]")
	public WebElement txt_quantity;	
	@FindBy(xpath="(//*[@class='icon icon-plus'])[1]")
	public WebElement txt_addquantity;	
	@FindBy(xpath="//*[@class='js-qty__adjust js-qty__adjust--plus']")
	public WebElement btn_addquantity;
	@FindBy(xpath="//span[contains(text(),'Add to cart')]")
	public WebElement btn_addToCart;
	public String txt_cartLink = "https://penshoppe-test-store.myshopify.com/cart";
	public String err_cartEmpty = "//*[@class='cart__empty-text text-center']";
	
	@FindBy(xpath="//*[contains(text(),'Review Cart')]")
	public WebElement btn_reviewCart;	
	@FindBy(xpath="//*[contains(text(),'Footer menu')]")
	public WebElement txt_footerMenu;
	@FindBy(xpath="//*[contains(text(),'Check out')]")
	public WebElement btn_checkOut;
	
	//Payment Screen
	@FindBy(id="checkout_email")
	public WebElement tf_email;
	
	//Address finder fields
	@FindBy(id="province")
	public WebElement dd_province;
	@FindBy(id="city")
	public WebElement dd_city;
	@FindBy(id="barangay")
	public WebElement dd_barangay;
	@FindBy(xpath="//*[@class='btn save--btn' and contains(text(),'Verify Address')]")
	public WebElement btn_verifyAddress;
	
	//Ship to Customer web elements
	@FindBy(id="checkout_shipping_address_id")
	public WebElement dd_shippingAddress;
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
	
	//Billing address web elements for user not signed-in
	@FindBy(id="checkout_billing_address_first_name")
	public WebElement tf_firstNameBA;
	@FindBy(id="checkout_billing_address_last_name")
	public WebElement tf_lastNameBA;
	@FindBy(id="checkout_billing_address_company")
	public WebElement tf_houseNumberBA;
	@FindBy(id="checkout_billing_address_city")
	public WebElement tf_provinceBA;
	@FindBy(id="checkout_billing_address_province")
	public WebElement dd_regionBA;
	@FindBy(id="checkout_billing_address_address2")
	public WebElement tf_townOrCityBA;
	@FindBy(id="checkout_billing_address_address1")
	public WebElement tf_barangayBA;
	@FindBy(id="checkout_billing_address_zip")
	public WebElement tf_zipCodeBA;
	@FindBy(id="checkout_billing_address_phone")
	public WebElement tf_phoneBA;
	
	@FindBy(xpath="//*[contains(text(),'Terms of service')]")
	public WebElement txt_termsOfService;
	
	public String btn_captcha = "//*[@class='shopify-challenge__button btn']";
	public String icn_loadingCourier ="//*[contains(@class,'spinner blank')]";
	public String btn_continueLoading = "//*[contains(@class,'--loading')]";
	public String file_OrderList = "C:\\Workspace\\GABC\\src\\test\\java\\com\\dataobjects\\ordersCreated.xlsx";
	public String file_OrderCreated = "C:\\Workspace\\GABC\\src\\test\\java\\com\\dataobjects\\Input.xlsx";
	public String txt_btn_continue = "//*[@id='continue_button']";
	
	public String file_TrainingOrderList = "C:\\Workspace\\GABC\\src\\test\\java\\com\\dataobjects\\trainingOrdersCreated.xlsx";
	public String file_TrainingOrderCreated = "C:\\Workspace\\GABC\\src\\test\\java\\com\\dataobjects\\trainingInput.xlsx";
	
	@FindBy(xpath="//*[contains(text(),'J&T')]")
	public WebElement btn_selectCourierJT;
	
	@FindBy(xpath="//*[@id='continue_button']")
	public WebElement btn_continue;
	
	//select payment method
	@FindBy(xpath="//*[contains(text(),'Pay in store')]")
	public WebElement btn_cashOnDelivery;
	@FindBy(xpath="//*[contains(text(),'Cash on Delivery (COD)')]")
	public WebElement btn_cashOnDeliveryBOSTC;
	
	//Order# Screen
	@FindBy(xpath="//*[@class='os-order-number']")
	public WebElement txt_orderNumber;
	public String lbl_orderNumber = "//*[@class='os-order-number']";
	public String lbl_orderBeingProcess = "//*[contains(text(),'Your order’s being processed.')]";
	
	public String btn_cookies = "(//*[@class='icon icon-close'])[8]";
	
	//Pickup web elements
	@FindBy(xpath="//*[@data-delivery-radio-button=\"pickup\"]")
	public WebElement btn_pickUp;
	
	@FindBy(id="helper-link")
	public WebElement lnk_findYourLocation;
	
	@FindBy(xpath="//button[contains(text(),'Show me stores near this location')]")
	public WebElement btn_showStoreNearLoc;
	public String txt_showStoreNearLoc = "//button[contains(text(),'Show me stores near this location')]";
	
	@FindBy(id="checkout_billing_address_id")
	public WebElement dd_billingAddress;
	public String txt_billingAddress = "MATANDANG BALARA, QUEZON CITY, 1119 Metro Manila, 00, Philippines (Kim Ko, No. 6 Vista Real Avenue)";

	public String txt_trainingPhoneNum = "09123456789";
	public String txt_trainingStores = "Placeholder,P047,P049,P394,P254,P378,P354,P343,P342,P295,P349,P285,P061,P082,P118,P127,P128,P135,P145,P186,P206,P215,P224,P227,P237,P238,P243,P258,P269,P284,P059,P108,P141";
	public String txt_trainingStoresRow = "0,2,6,10,14,18,22,26,30,34,38,42,46,50,54,58,62,66,70,74,78,82,86,90,94,98,102,106,110,114,118,122,126";

	@FindBy(id="searchboxinput")
	public WebElement tf_searchboxinput;
	
	public void check_stuckLoading(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		if(driver.findElements(By.xpath(btn_continueLoading)).size()>0) {
			System.out.println("Loading is taking too long. Refreshing the page");
			driver.navigate().refresh();
			Browser.waitForBrowserToLoadCompletely(driver);
			if(driver.findElements(By.xpath(txt_btn_continue)).size()>0) {
				js.executeScript("arguments[0].click();", btn_continue);
			}		
		}	
	}
	
	//method to check if order # is displayed
	public void check_ifOrderNumDisplayed(ExtentTest logger, WebDriver driver) {
		Browser.waitForElementIsVisible(logger, driver, txt_orderNumber);
		if(txt_orderNumber.getText().length()==0) {
			driver.navigate().refresh();
			Browser.waitForBrowserToLoadCompletely(driver);
			check_ifOrderNumDisplayed(logger, driver);
		} else {
			System.out.println("Order# generated");
		}
	}
	
	/**
	 * This method is for checking if carft is empty
	 * 
	 * @param logger
	 */
	public void check_cartEmpty(ExtentTest logger, WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		if(driver.findElements(By.xpath(err_cartEmpty)).size()>0) {
			System.out.println("Cart is Empty. Refreshing the page");
			driver.navigate().refresh();
			Browser.waitForBrowserToLoadCompletely(driver);
			js.executeScript("arguments[0].scrollIntoView(true);", btn_addToCart);
			Browser.waitForElementIsVisible(logger, driver, btn_addToCart);
			js.executeScript("arguments[0].click();", btn_addToCart);
			Browser.pause(logger, "1");	
		}
	}
	
}


