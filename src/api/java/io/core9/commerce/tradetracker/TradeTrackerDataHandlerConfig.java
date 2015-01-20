package io.core9.commerce.tradetracker;

import io.core9.plugin.widgets.datahandler.DataHandlerDefaultConfig;

public class TradeTrackerDataHandlerConfig extends DataHandlerDefaultConfig {

	public static final String SESSION_KEY = "viaTradeTracker";

	private String campaignID = "";
	private String materialID = "";
	private String affiliateID = "";
	private String redirectURL = "";
	private String reference = "";
	private String cookieDomain = "";
	private int cookieDays = 365;

	public String getCampaignID() {
		return campaignID;
	}

	public void setCampaignID(String campaignID) {
		this.campaignID = campaignID;
	}

	public String getMaterialID() {
		return materialID;
	}

	public void setMaterialID(String materialID) {
		this.materialID = materialID;
	}

	public String getAffiliateID() {
		return affiliateID;
	}

	public void setAffiliateID(String affiliateID) {
		this.affiliateID = affiliateID;
	}

	public String getRedirectURL() {
		return redirectURL;
	}

	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	public int getCookieDays() {
		return cookieDays;
	}

	public void setCookieDays(int cookieDays) {
		this.cookieDays = cookieDays;
	}
}
