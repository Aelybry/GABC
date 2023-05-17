package com.GSP.pageobjects;

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
public class GSPPage extends BaseTest {

	/*
	 * Web Elements GSP Page
	 */
	
	@FindBy(name="login_name")
	public WebElement txt_usernameGSP;
	
	@FindBy(name="password")
	public WebElement txt_passwordGSP;
	
	@FindBy(xpath="//button[contains(text(),'Login')]")
	public WebElement btn_signInGSP;
	
	@FindBy(xpath="//*[contains(text(),'Home')]")
	public WebElement txt_homePage;
	
	@FindBy(xpath="//*[contains(text(),'These credentials do not match our records.')]")
	public WebElement err_invalidLogin;
	
	@FindBy(xpath="//*[@class='icon-thumbnail']")
	public WebElement btn_transactions;
	
	@FindBy(xpath="//*[contains(text(),'Transactions')]")
	public WebElement link_transactions;
	
	@FindBy(xpath="//a[contains(text(),'Deliveries')]")
	public WebElement link_deliveries;
	
	@FindBy(xpath="//a[contains(text(),'Misrouted Delivery')]")
	public WebElement link_misroutedDelivery;
	
	@FindBy(xpath="//a[contains(text(),'No Document Delivery')]")
	public WebElement link_noDocumentDelivery;
	
	@FindBy(xpath="//a[contains(text(),'Pullout Request')]")
	public WebElement link_pulloutRequest;
	
	@FindBy(xpath="//a[contains(text(),'Loading')]")
	public WebElement txt_loadingDeliveries;
	
	@FindBy(id="searchDelivery")
	public WebElement btn_searchDelivery;
	public String txt_noResult = "//*[contains(text(),'No data available in table')]";
	 
	@FindBy(id="fromDate")
	public WebElement btn_fromDate;
	
	@FindBy(xpath="//*[@href='/delivery-confirmation']")
	public WebElement btn_deliveryConfirmation;
	
	public String deliveryConfirmationCrumbs = "//*[contains(text(),'Delivery Confirmation')]";
	public String mrdCreateNew = "//*[contains(text(),'New Misrouted Delivery')]";
	public String ndCreateNew = "//*[contains(text(),'New No Document Delivery')]";
	
	@FindBy(xpath="//*[@data-title=\"Actions\"]")
	public WebElement btn_action;
	
	@FindBy(xpath="//*[@data-title=\"Confirm\"]")
	public WebElement btn_actionConfirm;
	
	@FindBy(xpath="//*[@data-title=\"Confirm Request\"]")
	public WebElement btn_actionConfirmRequest;
	
	@FindBy(xpath="//*[@data-title=\"Cancel Request\"]")
	public WebElement btn_actionCancelRequest;
	
	@FindBy(xpath="//*[@data-title='Save Request']")
	public WebElement btn_saveRequest;
	
	//Error and messages
	public String errBlankDeliveryConfirmation = "//*[contains(text(),'Please input required(*) fields.')]";
	public String errInvalidBarCodeDC = "//*[contains(text(),'Barcode does not exist!')]";
	public String msgSuccessfulDeliveryConfirmation = "//*[contains(text(),'Transaction has been updated.')]";
	public String errZeroQuantityDetected = "//*[contains(text(),'Zero Quantity Detected')]";
	
	public String errBlankMRD = "//*[contains(text(),'SSCC should be 18 digit characters.')]";
	public String errMissingRequiredFieldskMRD = "//*[contains(text(),'Please enter required field(s).')]";
	public String errBarcodeEnteredBeforeSSCC = "//*[contains(text(),'Please enter SSCC!')]";
	public String errBarcodeEnteredBeforeDestinationSite = "//*[contains(text(),'Source site not found!')]";
	public String errEmptyBarCode = "//*[contains(text(),'Unable to confirm empty material details.')]";
	public String msgSaveMRDRequest = "//*[@class=\"badge badge-warning\" and contains(text(),'IN-TRANSIT')]";
	public String msgConfirmMRDRequest= "//*[@class=\"badge badge-danger\" and contains(text(),'READY')]";
	public String msgCanceledMRDRequest= "//*[@class=\"badge badge-danger\" and contains(text(),'CANCELLED')]";
	public String msgConfirmMRDCancel= "//*[contains(text(),'Transaction has been saved.')]";
	public String errMRDAlreadyExist= "//*[contains(text(),'Misrouted Delivery already exist.')]";
	public String errNDAlreadyExist= "//*[contains(text(),'No Document Delivery already exist.')]";
	
	@FindBy(id="shippingUnit")
	public WebElement tb_shippingUnit;
	
	@FindBy(id="scannedQty")
	public WebElement tb_scannedQty;
	
	@FindBy(id="barCode")
	public WebElement tb_barCode;
	
	public String lbl_barCodes = "(//*[@class=\" td-barcode\"])";
	
	@FindBy(id="airWayBill")
	public WebElement tb_airWayBill;
			
	@FindBy(xpath="//span[@id='select2-remark-container']")
	public WebElement dd_selectRemarks;
	@FindBy(xpath="//*[@class='select2-search__field']")
	public WebElement tf_selectRemarks;
	
	@FindBy(xpath="//span[@id='select2-forwarder-container']")
	public WebElement dd_selectForwarder;
	@FindBy(xpath="//*[@class='select2-search__field']")
	public WebElement tf_selectForwarder;
	
	@FindBy(xpath="//*[@class=' td-barcode']")
	public WebElement lbl_tdBarcode;
	
	public String msgLoading = "//*[contains(text(),'Loading Delivery details...')]";
	
	@FindBy(xpath="//*[contains(text(),'Yes, confirm it!')]")
	public WebElement btn_confirmSubmit;
	
	@FindBy(id="imgInp")
	public WebElement btn_fileUpload;
	
	@FindBy(xpath="//*[@src='http://150.200.3.72/img/avatar.png']")
	public WebElement btn_profile;
	
	@FindBy(xpath="(//*[contains(text(),'Logout')])[1]")
	public WebElement btn_logOut;
	
	/*
	 * elements for MRD / ND
	 */
	
	@FindBy(id="shippingNumber")
	public WebElement tb_shippingNumber;
	
	@FindBy(xpath="//*[@value='Create New']")
	public WebElement btn_createNew;
	
	@FindBy(id="transaction_number")
	public WebElement tb_deliveryNumber;
	
	@FindBy(id="SSCC")
	public WebElement tb_ssccNumber;
	
	@FindBy(id="select2-destination_site-container")
	public WebElement dd_destinationSite;
	
	@FindBy(xpath="//*[@class='select2-search__field']")
	public WebElement tb_destinationSite;
	
	@FindBy(xpath="//*[contains(text(),'Yes, save it!')]")
	public WebElement btn_saveMRDRequest;
	
	@FindBy(xpath="//*[contains(text(),'Yes, confirm it!')]")
	public WebElement btn_confirmMRDRequest;

	public String btn_viewRequest = "(//*[@class='btn btn-success btn-sm'])";	
	public String lbl_badges ="(//*[contains(@class,'badge badge-')])";
	
	/*
	 * Shared methods across modules
	 */
	public void loginGSP(ExtentTest logger, String userName, String password) {
		try {
			TextBox.getInstance().setText(logger, txt_usernameGSP, userName);
			logger.log(LogStatus.INFO, "HTML", "User name : <b>" + userName + "</b> entered in the username field");		
		
			TextBox.getInstance().setPassword(logger, txt_passwordGSP, password);
			logger.log(LogStatus.INFO, "HTML", "Password : <b>****************</b> entered in the password field");
						
			Button.getInstance().click(logger, btn_signInGSP);
			logger.log(LogStatus.INFO, "<b>Sign In</b> button clicked");
			
		} catch (Exception e) {
			
		}
	}
		
	
}
