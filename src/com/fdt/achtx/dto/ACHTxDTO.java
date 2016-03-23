package com.fdt.achtx.dto;

import java.util.Date;

import com.fdt.common.dto.AbstractBaseDTO;
import com.fdt.ecom.entity.enums.PaymentType;

public class ACHTxDTO extends AbstractBaseDTO {
	
	private String localDir = null;
	
	private String sftpHost = null;
	
	private String achStatus = null;
	
	private int sftpPort = 22;
	
	private String sftpUser = null;
	
	private String sftpPassword = null;
	
	private String toWorkingDirectory = null;
	
	private String fromWorkingDirectory = null;
	
	private String xmlContent = null;
	
	private String fileName = null;
	
	private String paymentReferenceId = null;

    private static final long serialVersionUID = 5158423900142174558L;
    
    private Long siteId = null;
    
    private String spName = null;
    
    private String customerPermId = null;

    private String achLoginKey = null;

    private String pin = null;

    private String toAcctNumber = null;

    private String toAcctName = null;

    private String toAcctRoutingNo = null;
    
    private String toBankIdType = null;
    
    private String toAccountType = null;
    
    private String fromAcctNumber = null;

    private String fromAcctName = null;

    private String fromAcctRoutingNo = null;
    
    private String fromBankIdType = null;
    
    private String fromAccountType = null;

    private String clientIp = null;

    private double txAmount = 0d;

    private String description = null;

    private PaymentType paymentType = null;

    private String siteName = null;
    
    private String toAddressLine1 = null;
	
	private String toAddressLine2 = null;
	
	private String toCity = null;
	
	private String toState = null;
	
	private String toZip= null;
	
	private String comments= null;

    private Integer totalTransactions = 0;

    /** This is an output Variable **/
    private String txRefNumber = null;

    /** This is an output Variable **/
    private String checkNumber = null;

    /** This is an output Variable **/
    private int noOfTransactions = 0;

    /** This is an output Variable **/
    private String resultMsg = null;

    /** This is the transaction cut off time**/
    private Date txCutOffTime = null;

    /** This is the first transaction timestamp that is included in the check**/
    private Date startTxDate = null;

    public String getAchLoginKey() {
        return achLoginKey;
    }

    public void setAchLoginKey(String achLoginKey) {
        this.achLoginKey = achLoginKey;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getToAcctNumber() {
        return toAcctNumber;
    }

    public void setToAcctNumber(String toAcctNumber) {
        this.toAcctNumber = toAcctNumber;
    }

    public String getToAcctName() {
        return toAcctName;
    }

    public void setToAcctName(String toAcctName) {
        this.toAcctName = toAcctName;
    }

    public String getToAcctRoutingNo() {
        return toAcctRoutingNo;
    }

    public void setToAcctRoutingNo(String acctRoutingNo) {
        this.toAcctRoutingNo = acctRoutingNo;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public double getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(double txAmount) {
        this.txAmount = txAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getTxRefNumber() {
        return txRefNumber;
    }

    public void setTxRefNumber(String txRefNumber) {
        this.txRefNumber = txRefNumber;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNum(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public int getNoOfTransactions() {
        return noOfTransactions;
    }

    public void setNoOfTransactions(int noOfTransactions) {
        this.noOfTransactions = noOfTransactions;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Date getTxCutOffTime() {
        return txCutOffTime;
    }

    public void setTxCutOffTime(Date txCutOffTime) {
        this.txCutOffTime = txCutOffTime;
    }

    public Date getStartTxDate() {
		return startTxDate;
	}

	public void setStartTxDate(Date startTxDate) {
		this.startTxDate = startTxDate;
	}

	public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Integer getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

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

	public String getToBankIdType() {
		return toBankIdType;
	}

	public void setToBankIdType(String toBankIdType) {
		this.toBankIdType = toBankIdType;
	}

	public String getToAccountType() {
		return toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public String getFromAcctNumber() {
		return fromAcctNumber;
	}

	public void setFromAcctNumber(String fromAcctNumber) {
		this.fromAcctNumber = fromAcctNumber;
	}

	public String getFromAcctName() {
		return fromAcctName;
	}

	public void setFromAcctName(String fromAcctName) {
		this.fromAcctName = fromAcctName;
	}

	public String getFromAcctRoutingNo() {
		return fromAcctRoutingNo;
	}

	public void setFromAcctRoutingNo(String fromAcctRoutingNo) {
		this.fromAcctRoutingNo = fromAcctRoutingNo;
	}

	public String getFromBankIdType() {
		return fromBankIdType;
	}

	public void setFromBankIdType(String fromBankIdType) {
		this.fromBankIdType = fromBankIdType;
	}

	public String getFromAccountType() {
		return fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getToAddressLine1() {
		return toAddressLine1;
	}

	public void setToAddressLine1(String toAddressLine1) {
		this.toAddressLine1 = toAddressLine1;
	}

	public String getToAddressLine2() {
		return toAddressLine2;
	}

	public void setToAddressLine2(String toAddressLine2) {
		this.toAddressLine2 = toAddressLine2;
	}

	public String getToCity() {
		return toCity;
	}

	public void setToCity(String toCity) {
		this.toCity = toCity;
	}

	public String getToState() {
		return toState;
	}

	public void setToState(String toState) {
		this.toState = toState;
	}

	public String getToZip() {
		return toZip;
	}

	public void setToZip(String toZip) {
		this.toZip = toZip;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSftpHost() {
		return sftpHost;
	}

	public void setSftpHost(String sftpHost) {
		this.sftpHost = sftpHost;
	}

	public int getSftpPort() {
		return sftpPort;
	}

	public void setSftpPort(int sftpPort) {
		this.sftpPort = sftpPort;
	}

	public String getSftpUser() {
		return sftpUser;
	}

	public void setSftpUser(String sftpUser) {
		this.sftpUser = sftpUser;
	}

	public String getSftpPassword() {
		return sftpPassword;
	}

	public void setSftpPassword(String sftpPassword) {
		this.sftpPassword = sftpPassword;
	}

	public String getToWorkingDirectory() {
		return toWorkingDirectory;
	}

	public void setToWorkingDirectory(String toWorkingDirectory) {
		this.toWorkingDirectory = toWorkingDirectory;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFromWorkingDirectory() {
		return fromWorkingDirectory;
	}

	public void setFromWorkingDirectory(String fromWorkingDirectory) {
		this.fromWorkingDirectory = fromWorkingDirectory;
	}

	public String getPaymentReferenceId() {
		return paymentReferenceId;
	}

	public void setPaymentReferenceId(String paymentReferenceId) {
		this.paymentReferenceId = paymentReferenceId;
	}

	public String getLocalDir() {
		return localDir;
	}

	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}

	public String getAchStatus() {
		return achStatus;
	}

	public void setAchStatus(String achStatus) {
		this.achStatus = achStatus;
	}
	
	
    
}