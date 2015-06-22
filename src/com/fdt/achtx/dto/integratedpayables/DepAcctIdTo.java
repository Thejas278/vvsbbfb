package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class DepAcctIdTo {
	
	@XStreamAlias("AcctId")
	private String accountId  = null;
	
	@XStreamAlias("AcctType")
	private String accountType  = null;
	
	@XStreamAlias("Name")
	private String name  = null;
	
	@XStreamAlias("BankInfo")
	private BankInfo bankInfo = null;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BankInfo getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(BankInfo bankInfo) {
		this.bankInfo = bankInfo;
	}	

}
