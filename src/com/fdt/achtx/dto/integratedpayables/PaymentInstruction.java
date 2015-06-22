package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class PaymentInstruction {

	@XStreamAlias("PmtFormat")
	private String paymentFormat = null;
	
	@XStreamAlias("CompanyEntryDescription")
	private String companyEntryDescription = null;
	
	@XStreamAlias("TransactionCode")
	private String transactionCode = null;
	
	@XStreamAlias("IndividualIdNo")
	private String individualIdNo = null;
	
	@XStreamAlias("Desc")
	private String description = null;

	public String getPaymentFormat() {
		return paymentFormat;
	}

	public void setPaymentFormat(String paymentFormat) {
		this.paymentFormat = paymentFormat;
	}

	public String getCompanyEntryDescription() {
		return companyEntryDescription;
	}

	public void setCompanyEntryDescription(String companyEntryDescription) {
		this.companyEntryDescription = companyEntryDescription;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getIndividualIdNo() {
		return individualIdNo;
	}

	public void setIndividualIdNo(String individualIdNo) {
		this.individualIdNo = individualIdNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
		
}
