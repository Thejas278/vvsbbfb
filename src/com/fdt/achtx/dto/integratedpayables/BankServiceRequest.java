package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;


public class BankServiceRequest {
	
	@XStreamAlias("RqUID")
	private String requestId = null;
	
	@XStreamAlias("XferAddRq")
	private TransferAddRequest transferAddRequest = null;
	
	@XStreamAlias("XferAddRs")
	private TransferAddResponse transferAddResponse = null;
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public TransferAddRequest getTransferAddRequest() {
		return transferAddRequest;
	}

	public void setTransferAddRequest(TransferAddRequest transferAddRequest) {
		this.transferAddRequest = transferAddRequest;
	}

	public TransferAddResponse getTransferAddResponse() {
		return transferAddResponse;
	}

	public void setTransferAddResponse(TransferAddResponse transferAddResponse) {
		this.transferAddResponse = transferAddResponse;
	}

	@Override
	public String toString() {
		return "BankServiceRequest [requestId=" + requestId
				+ ", transferAddRequest=" + transferAddRequest
				+ ", transferAddResponse=" + transferAddResponse + "]";
	}
	
}
