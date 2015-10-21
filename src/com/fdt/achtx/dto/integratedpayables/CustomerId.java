package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class CustomerId {
	
	@XStreamAlias("SPName")
	private String spName = null;
	
	@XStreamAlias("CustPermId")
	private String customerPermId = null;

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getCustomerPermId() {
		return customerPermId;
	}

	public void setCustomerPermId(String customerPermId) {
		this.customerPermId = customerPermId;
	}
	
}
