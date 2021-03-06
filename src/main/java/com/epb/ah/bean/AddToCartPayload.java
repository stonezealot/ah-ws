package com.epb.ah.bean;

public class AddToCartPayload {

	private String orgId;
	private String custId;
	private String guestFlg;
	private String ecshopId;
	private String stkId;
	private String qty;
	private String cashcarry;
	private String installationFlg;

	public AddToCartPayload() {
		super();
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getGuestFlg() {
		return guestFlg;
	}

	public void setGuestFlg(String guestFlg) {
		this.guestFlg = guestFlg;
	}

	public String getEcshopId() {
		return ecshopId;
	}

	public void setEcshopId(String ecshopId) {
		this.ecshopId = ecshopId;
	}

	public String getStkId() {
		return stkId;
	}

	public void setStkId(String stkId) {
		this.stkId = stkId;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getCashcarry() {
		return cashcarry;
	}

	public void setCashcarry(String cashcarry) {
		this.cashcarry = cashcarry;
	}

	public String getInstallationFlg() {
		return installationFlg;
	}

	public void setInstallationFlg(String installationFlg) {
		this.installationFlg = installationFlg;
	}

}
