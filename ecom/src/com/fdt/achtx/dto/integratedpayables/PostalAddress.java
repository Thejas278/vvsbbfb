package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class PostalAddress {
	
	@XStreamAlias("Addr1")
	private String addressLine1 = null;
	
	@XStreamAlias("Addr2")
	private String addressLine2 = null;
	
	@XStreamAlias("Country")
	private String country = null;

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}	
	
	
}
