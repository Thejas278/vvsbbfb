package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class TransferAddRequest {
	
	@XStreamAlias("RqUID")
	private String requestId = null;
	
	@XStreamAlias("PmtRefId")
	private String paymentReferenceId = null;
	
	@XStreamAlias("CustId")
	private CustomerId customerId = null;
	
	@XStreamAlias("XferInfo")
	private TransferInfo transferInfo = null;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getPaymentReferenceId() {
		return paymentReferenceId;
	}

	public void setPaymentReferenceId(String paymentReferenceId) {
		this.paymentReferenceId = paymentReferenceId;
	}

	public CustomerId getCustomerId() {
		return customerId;
	}

	public void setCustomerId(CustomerId customerId) {
		this.customerId = customerId;
	}

	public TransferInfo getTransferInfo() {
		return transferInfo;
	}

	public void setTransferInfo(TransferInfo transferInfo) {
		this.transferInfo = transferInfo;
	}
	
	
	
}
