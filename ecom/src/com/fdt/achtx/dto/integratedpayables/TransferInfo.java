package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class TransferInfo {
	
	@XStreamAlias("DepAcctIdFrom")
	private DepAcctIdFrom depAcctIdFrom = null;
	
	@XStreamAlias("DepAcctIdTo")
	private DepAcctIdTo depAcctIdTo = null;
	
	@XStreamAlias("CurAmt")
	private CurrencyAmount currencyAmount = null;
	
	@XStreamAlias("DueDt")
	private String dueDate = null;
	
	@XStreamAlias("Category")
	private String category = null;
	
	@XStreamAlias("PmtInstruction")
	private PaymentInstruction paymentInstruction = null;

	public DepAcctIdFrom getDepAcctIdFrom() {
		return depAcctIdFrom;
	}

	public void setDepAcctIdFrom(DepAcctIdFrom depAcctIdFrom) {
		this.depAcctIdFrom = depAcctIdFrom;
	}

	public DepAcctIdTo getDepAcctIdTo() {
		return depAcctIdTo;
	}

	public void setDepAcctIdTo(DepAcctIdTo depAcctIdTo) {
		this.depAcctIdTo = depAcctIdTo;
	}

	public CurrencyAmount getCurrencyAmount() {
		return currencyAmount;
	}

	public void setCurrencyAmount(CurrencyAmount currencyAmount) {
		this.currencyAmount = currencyAmount;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public PaymentInstruction getPaymentInstruction() {
		return paymentInstruction;
	}

	public void setPaymentInstruction(PaymentInstruction paymentInstruction) {
		this.paymentInstruction = paymentInstruction;
	}
	

}
