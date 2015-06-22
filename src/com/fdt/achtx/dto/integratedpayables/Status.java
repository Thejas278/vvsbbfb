package com.fdt.achtx.dto.integratedpayables;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Status {

	@XStreamAlias("Severity")
	private String severity = null;
	
	@XStreamAlias("PmtRefId")
	private String paymentReferenceId = null;
	
	@XStreamAlias("StatusDesc")
	private String statusDesc = null;
	
	@XStreamAlias("ErrorDesc")
	private String errorDesc = null;
	
	@XStreamAlias("AsOfDate")
	private String asOfDate = null;
	
	@XStreamAlias("AsOfTime")
	private String asOfTime = null;

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getPaymentReferenceId() {
		return paymentReferenceId;
	}

	public void setPaymentReferenceId(String paymentReferenceId) {
		this.paymentReferenceId = paymentReferenceId;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public String getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}

	public String getAsOfTime() {
		return asOfTime;
	}

	public void setAsOfTime(String asOfTime) {
		this.asOfTime = asOfTime;
	}

	@Override
	public String toString() {
		return "Status [severity=" + severity + ", paymentReferenceId="
				+ paymentReferenceId + ", statusDesc=" + statusDesc
				+ ", errorDesc=" + errorDesc + ", asOfDate=" + asOfDate
				+ ", asOfTime=" + asOfTime + "]";
	}

	
}
