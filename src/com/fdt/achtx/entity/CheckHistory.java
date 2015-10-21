package com.fdt.achtx.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fdt.common.entity.AbstractBaseEntity;
import com.fdt.ecom.entity.enums.PaymentType;

@Entity
@Table(name = "ECOMM_CHECK_HIST")
public class CheckHistory extends AbstractBaseEntity {

    private static final long serialVersionUID = -5388872917370641890L;

    @Column(name = "SITE_ID", nullable = false)
    private Long siteId = null;

    @Column(name = "CHECKNUM", nullable = false)
    private Long checkNumber = null;

    @Column(name="PAYMENT_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = null;

    @Column(name="AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "BANK_ID", nullable = false)
    private Long bankId = null;

    @Transient
    private String bankName = null;

    @Column(name = "IS_VOIDED", nullable = false)
    @Type(type="yes_no")
    private boolean voided = false;

    @Column(name = "MACHINENAME", nullable = false)
    private String machineName = null;

    @Column(name = "TOTAL_TRANSACTIONS")
    private Long totalTransactions = null;

    @Column(name = "COMMENTS")
    private String comments = null;

    @Column(name = "IS_ECHECK", nullable = false)
    @Type(type="yes_no")
    private boolean echeck = false;

    @Column(name = "ACH_REFERENCE_NUM")
    private String achTxRefNumber = null;
    
    @Column(name = "ACH_STATUS")
    private String achStatus = null; 
    
    @Column(name = "ACH_RSP_SEVERITY")
    private String achResponseSeverity = null;
    
    @Column(name = "ACH_RSP_STATUS_DESC")
    private String achResponseStatusDesc = null;
    
    @Column(name = "ACH_RSP_ERROR_DESC")
    private String achResponseErrorDesc = null; 
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACH_RSP_STATUS_DATE")
    private Date achResponseStatusDate = null;

    @Transient
    private String siteName = null;

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNum(Long checkNumber) {
        this.checkNumber = checkNumber;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public boolean isVoided() {
        return voided;
    }

    public void setVoided(boolean isVoided) {
        this.voided = isVoided;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isEcheck() {
        return echeck;
    }

    public void setEcheck(boolean echeck) {
        this.echeck = echeck;
    }

    public String getAchTxRefNumber() {
        return achTxRefNumber;
    }

    public void setAchTxRefNumber(String achTxRefNumber) {
        this.achTxRefNumber = achTxRefNumber;
    }

	public String getAchStatus() {
		return achStatus;
	}

	public void setAchStatus(String achStatus) {
		this.achStatus = achStatus;
	}

	public String getAchResponseSeverity() {
		return achResponseSeverity;
	}

	public void setAchResponseSeverity(String achResponseSeverity) {
		this.achResponseSeverity = achResponseSeverity;
	}

	public String getAchResponseStatusDesc() {
		return achResponseStatusDesc;
	}

	public void setAchResponseStatusDesc(String achResponseStatusDesc) {
		this.achResponseStatusDesc = achResponseStatusDesc;
	}

	public String getAchResponseErrorDesc() {
		return achResponseErrorDesc;
	}

	public void setAchResponseErrorDesc(String achResponseErrorDesc) {
		this.achResponseErrorDesc = achResponseErrorDesc;
	}

	public Date getAchResponseStatusDate() {
		return achResponseStatusDate;
	}

	public void setAchResponseStatusDate(Date achResponseStatusDate) {
		this.achResponseStatusDate = achResponseStatusDate;
	}

	public void setCheckNumber(Long checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	public String toString() {
		return "CheckHistory [siteId=" + siteId + ", checkNumber="
				+ checkNumber + ", paymentType=" + paymentType + ", amount="
				+ amount + ", bankId=" + bankId + ", bankName=" + bankName
				+ ", voided=" + voided + ", machineName=" + machineName
				+ ", totalTransactions=" + totalTransactions + ", comments="
				+ comments + ", echeck=" + echeck + ", achTxRefNumber="
				+ achTxRefNumber + ", achStatus=" + achStatus
				+ ", achResponseSeverity=" + achResponseSeverity
				+ ", achResponseStatusDesc=" + achResponseStatusDesc
				+ ", achResponseErrorDesc=" + achResponseErrorDesc
				+ ", achResponseStatusDate=" + achResponseStatusDate
				+ ", siteName=" + siteName + "]";
	}

}
