package com.epb.ah.bean;

public class CommonPayload {

	private String orgId;
	private String custId;
	private String ecshopId;

	public CommonPayload() {
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

	public String getEcshopId() {
		return ecshopId;
	}

	public void setEcshopId(String ecshopId) {
		this.ecshopId = ecshopId;
	}

}
