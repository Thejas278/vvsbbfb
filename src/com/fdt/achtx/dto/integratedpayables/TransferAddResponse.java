package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class TransferAddResponse {

	@XStreamAlias("Status")
	private Status status = null;
	
	@XStreamAlias("RqUID")
	private String requestId = null;
	
	@XStreamAlias("SPRefId")
	private String spReferenceId = null;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getSpReferenceId() {
		return spReferenceId;
	}

	public void setSpReferenceId(String spReferenceId) {
		this.spReferenceId = spReferenceId;
	}

	@Override
	public String toString() {
		return "TransferAddResponse [status=" + status + ", requestId="
				+ requestId + ", spReferenceId=" + spReferenceId + "]";
	}

}
