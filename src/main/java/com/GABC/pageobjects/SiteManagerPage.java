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
public class SiteManagerPage extends BaseTest {

	/*
	 * Web Elements of Aptos Site Manager Page
	 */
	
	@FindBy(id="TextBox1")
	public WebElement txt_usernameSM;
	
	@FindBy(id="Textbox2")
	public WebElement txt_passwordSM;
	
	@FindBy(id="Button1")
	public WebElement btn_signInSM;
	
	public void loginSM(ExtentTest logger, String userName, String password) {
		try {
			TextBox.getInstance().setText(logger, txt_usernameSM, userName);
			logger.log(LogStatus.INFO, "HTML", "User name : <b>" + userName + "</b> entered in the username field");		
		
			TextBox.getInstance().setPassword(logger, txt_passwordSM, password);
			logger.log(LogStatus.INFO, "HTML", "Password : <b>****************</b> entered in the password field");
						
			Button.getInstance().click(logger, btn_signInSM);
			logger.log(LogStatus.INFO, "<b>Sign In</b> button cliked");
			
		} catch (Exception e) {
			
		}
	}
	
	@FindBy(id="ctl00_liContent")
	public WebElement lnk_content;
	
	@FindBy(xpath="//*[contains(text(),'Add/Edit Products')]")
	public WebElement lnk_addEditProduct;
	
	@FindBy(id="ctl00_mainContent_tCode")
	public WebElement txt_searachSKU;
	
	@FindBy(id="ctl00_mainContent_DataGrid1_ctl03_affiliateGroupInventoryLink")
	public WebElement lnk_inventoryInfo;
	
	@FindBy(xpath="//*[contains(text(),'Inventory by Supplier')]")
	public WebElement btn_inventoryBySupplier;
	
	@FindBy(id="cboxLoadingGraphic")
	public WebElement icon_loadinggraphic;
	
	@FindBy(id="loadingDiv")
	public WebElement icon_loadingDiv;
	
	@FindBy(xpath="//*[contains(text(),'Parent style: ')]")
	public WebElement lbl_parentStyle;
	
	@FindBy(xpath="(//*[@class=\"ag-icon ag-icon-expanded\"])[2]")
	public WebElement btn_showDetails;

	@FindBy(className="cboxIframe")
	public WebElement iframe_inventory;
	
	@FindBy(xpath="(//*[@class='ag-floating-filter-input'])[1]")
	public WebElement tf_agSupplierID;
	
	@FindBy(xpath="(//*[@class='ag-floating-filter-input'])[2]")
	public WebElement tf_agSKU;
	
	@FindBy(id="ctl00_liCommerce")
	public WebElement lnk_Commerce;
	
	@FindBy(xpath="//*[contains(text(),'Orders') and @href=\"#\"]")
	public WebElement lnk_CommerceOrders;
	
	@FindBy(xpath="//*[contains(text(),'Manage Existing')]")
	public WebElement lnk_CommerceOrdersManageExisting;
	
	@FindBy(xpath="(//*[@id=\"cboxClose\"])[8]")
	public WebElement btn_closeSM;
	
	@FindBy(id="ctl00_mainContent_orderID")
	public WebElement tf_orderID;

	// Elements in Inventory Info screen
	@FindBy(xpath="(//*[@col-id='TotalOnhand'])[2]")
	public WebElement text_onhandInventory;
		
	@FindBy(xpath="(//*[@col-id='ReservedQuantity'])[2]")
	public WebElement text_reservedInventory;
	
	@FindBy(xpath="(//*[@col-id='ExternalReserveQty'])[2]")
	public WebElement text_externalReserveQty;
	
	@FindBy(xpath="(//*[@col-id='Quantity'])[2]")
	public WebElement text_availableInventory;
	
	public String eomCleanupCountDir = "C:\\Workspace\\GABC\\src\\test\\java\\com\\Shopify\\dataobjects\\ShippingOrders.xlsx";
	public String smokeTestDir = "C:\\Workspace\\GABC\\src\\test\\java\\com\\Shopify\\dataobjects\\SmokeTest.xlsx";
	public String siteManagerDir = "C:\\Workspace\\GABC\\src\\test\\java\\com\\Shopify\\dataobjects\\siteManager.xlsx";
	public String automationEmail = "automation.goldenabc@gmail.com";
	public String inventoryInfoEmptyCell = "//*[contains(text(),'--')]";
	public String warehouseSiteID = "2137";
	
	@FindBy(xpath="//*[@id=\"ctl00_mainContent_rptSites_ctl00_grdAff_ctl03_btnShowItemStatus\"]")
	public WebElement lnk_orderStatus;
	@FindBy(xpath="//li[contains(text(),'In Transit')]")
	public WebElement text_inTransitOrderStatus;
	
	
	
	@FindBy(id="ctl00_mainContent_btnSearch")
	public WebElement btn_searchOrderNumber;
	@FindBy(xpath="//*[contains(text(),'Ship Order')]")
	public WebElement btn_shipOrder;
	@FindBy(xpath="(//*[@class='m-payment-type'])[2]")
	public WebElement text_paymentMethod;
	@FindBy(xpath="//*[@id=\"lblInvoice\"]/div[3]/div[1]/p[2]/text()")
	public WebElement lbl_shippingMethod;
	@FindBy(xpath="//h2[contains(text(),'Items')]")
	public WebElement lbl_orderDetailsItems;
	@FindBy(xpath="//*[@id='rptItemsBySupplier_ctl00_rptItems_ctl00_chkItemAll']")
	public WebElement cb_chkAllItem;
	@FindBy(id="rptItemsBySupplier_ctl00_chkGenerate")
	public WebElement cb_genLabelsAndTracking;
	@FindBy(id="rptItemsBySupplier_ctl00_chkEmailCustomer")
	public WebElement cb_emailCustomer;
	@FindBy(id="rptItemsBySupplier_ctl00_chkSendReview")
	public WebElement cb_sendReviewRequest;
	@FindBy(id="rptItemsBySupplier_ctl00_chkCaptureFunds")
	public WebElement cb_captureFunds;
	@FindBy(id="rptItemsBySupplier_ctl00_rptItems_ctl01_ddlShipper")
	public WebElement dd_carrier;
	@FindBy(id="rptItemsBySupplier_ctl00_rptItems_ctl01_ddlShipperService")
	public WebElement dd_service;
	@FindBy(id="rptItemsBySupplier_ctl00_rptItems_ctl01_txtTrackNo")
	public WebElement tf_trackingNumber;
	@FindBy(id="rptItemsBySupplier_ctl00_rptItems_ctl01_hypSetAll")
	public WebElement lnk_copyToAll;	
	@FindBy(id="rptItemsBySupplier_ctl00_btnProcess")
	public WebElement btn_packAndSHipItems;
	@FindBy(id="mo-back")
	public WebElement btn_return;
	
	@FindBy(xpath="//*[contains(text(),'Cancelled ')]")
	public WebElement txt_Canceled;
	@FindBy(xpath="//*[contains(text(),'Fulfilled ')]")
	public WebElement txt_Fulfilled;
	@FindBy(xpath="//*[contains(text(),'Pick/Pack ')]")
	public WebElement txt_pickPack;
	
	
	
	
}
