package com.epb.ah.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EcbestSkuView {

	@Id
	private BigDecimal stkRecKey;
	private String ecbestId;
	private String orgId;
	private String stkId;
	private String name;
	private Character skuLabel;
	private String urlAddr;
	private String priceLabel;
	private BigDecimal listPrice;
	private BigDecimal netPrice;
	private BigDecimal refPrice1;
	private String ref1;
	private Character refFlg1;
	private String brandName;

	public EcbestSkuView() {
		super();
	}

	public BigDecimal getStkRecKey() {
		return stkRecKey;
	}

	public void setStkRecKey(BigDecimal stkRecKey) {
		this.stkRecKey = stkRecKey;
	}

	public String getEcbestId() {
		return ecbestId;
	}

	public void setEcbestId(String ecbestId) {
		this.ecbestId = ecbestId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getStkId() {
		return stkId;
	}

	public void setStkId(String stkId) {
		this.stkId = stkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Character getSkuLabel() {
		return skuLabel;
	}

	public void setSkuLabel(Character skuLabel) {
		this.skuLabel = skuLabel;
	}

	public String getUrlAddr() {
		return urlAddr;
	}

	public void setUrlAddr(String urlAddr) {
		this.urlAddr = urlAddr;
	}

	public String getPriceLabel() {
		return priceLabel;
	}

	public void setPriceLabel(String priceLabel) {
		this.priceLabel = priceLabel;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}

	public BigDecimal getRefPrice1() {
		return refPrice1;
	}

	public void setRefPrice1(BigDecimal refPrice1) {
		this.refPrice1 = refPrice1;
	}

	public String getRef1() {
		return ref1;
	}

	public void setRef1(String ref1) {
		this.ref1 = ref1;
	}

	public Character getRefFlg1() {
		return refFlg1;
	}

	public void setRefFlg1(Character refFlg1) {
		this.refFlg1 = refFlg1;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

}
