package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class BankInfo {

	@XStreamAlias("BankIdType")
	private String bankIdType  = null;
	
	@XStreamAlias("BankId")
	private String bankId  = null;
	
	@XStreamAlias("Name")
	private String name  = null;
	
	@XStreamAlias("PostAddr")
	private PostalAddress postalAddress  = null;

	public String getBankIdType() {
		return bankIdType;
	}

	public void setBankIdType(String bankIdType) {
		this.bankIdType = bankIdType;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PostalAddress getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(PostalAddress postalAddress) {
		this.postalAddress = postalAddress;
	}
	
	
}
