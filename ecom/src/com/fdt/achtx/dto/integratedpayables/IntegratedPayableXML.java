package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CMA")
public class IntegratedPayableXML {
	
	@XStreamAlias("BankSvcRq")
	private BankServiceRequest bankServiceRequest = null;

	public BankServiceRequest getBankServiceRequest() {
		return bankServiceRequest;
	}

	public void setBankServiceRequest(BankServiceRequest bankServiceRequest) {
		this.bankServiceRequest = bankServiceRequest;
	}

	@Override
	public String toString() {
		return "IntegratedPayableXML [bankServiceRequest=" + bankServiceRequest
				+ "]";
	}

	
}
