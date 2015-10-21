package com.fdt.achtx.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.fdt.achtx.dto.ACHTxDTO;
import com.fdt.achtx.entity.CheckHistory;
import com.fdt.common.dao.AbstractBaseDAOImpl;
import com.fdt.ecom.entity.enums.PaymentType;

@Repository
public class ACHTxDAOImpl extends AbstractBaseDAOImpl implements ACHTxDAO   {

    public ACHTxDTO doACHTransfer(Long siteId, PaymentType paymentType, String modifiedBy, String machineIp) {
    	ACHTxDTO acHDTO = null;
        Session session = currentSession();
        Query sqlQuery = session.getNamedQuery("GET_ACH_DETAILS")
                             .setParameter("siteId", siteId)
                             .setParameter("paymentType", paymentType.toString())
                             .setParameter("modifiedBy", modifiedBy)
                             .setParameter("machineName", machineIp)
                             .setParameter("markAsSettled", "Y")
    						 .setParameter("doACH", "Y");
        List<Object> resultList = sqlQuery.list();
        if(!resultList.isEmpty()) {
            Object[] row = (Object[]) resultList.get(0);
            acHDTO = new ACHTxDTO();
            acHDTO.setSiteName(this.getString(row[0]));
            acHDTO.setCheckNum(this.getString(row[1]));
            acHDTO.setTotalTransactions(this.getInteger(row[2]));
            acHDTO.setTxAmount(this.getDoubleFromBigDecimal(row[3]));
            acHDTO.setResultMsg(this.getString(row[4]));
            acHDTO.setTxCutOffTime(this.getDate(row[5]));
            acHDTO.setAchLoginKey(row[6] == null ? null : this.getPbeStringEncryptor().decrypt(row[6].toString()));
            acHDTO.setPin(row[7] == null ? null : this.getPbeStringEncryptor().decrypt(row[7].toString()));
            acHDTO.setToAcctName(this.getString(row[8]));
            acHDTO.setToAcctNumber(this.getString(row[9]));
            acHDTO.setToAcctRoutingNo(this.getString(row[10]));
            acHDTO.setStartTxDate(this.getDate(row[11]));
            acHDTO.setClientIp(machineIp);
            acHDTO.setPaymentType(paymentType);
            acHDTO.setSiteId(siteId);
            acHDTO.setFromAccountType(this.getString(row[12]));
            acHDTO.setFromBankIdType(this.getString(row[13]));
            acHDTO.setSpName(this.getString(row[14]));
            acHDTO.setCustomerPermId(this.getString(row[15]));
            acHDTO.setToAccountType(this.getString(row[16]));
            acHDTO.setToBankIdType(this.getString(row[17]));
            acHDTO.setToAddressLine1(this.getString(row[18]));
            acHDTO.setToAddressLine2(this.getString(row[19]));
            acHDTO.setToCity(this.getString(row[20]));
            acHDTO.setToState(this.getString(row[21]));
            acHDTO.setToZip(this.getString(row[22]));
            acHDTO.setSftpHost(this.getString(row[23]));
            acHDTO.setSftpPort(this.getLongFromInteger(row[24]).intValue());
            acHDTO.setSftpUser(this.getString(row[25]));
            acHDTO.setSftpPassword(row[26] == null ? null : this.getPbeStringEncryptor().decrypt(row[26].toString()));
            acHDTO.setToWorkingDirectory(this.getString(row[27]));
            acHDTO.setFromWorkingDirectory(this.getString(row[28]));
            acHDTO.setFromAcctNumber(this.getString(row[29]));
            acHDTO.setFromAcctName(this.getString(row[30]));
            acHDTO.setFromAcctRoutingNo(this.getString(row[31]));
        }
        return acHDTO;
    }

    public ACHTxDTO getACHDetailsForTransfer(Long siteId,
            PaymentType paymentType, String modifiedBy, String machineIp) {
    	ACHTxDTO acHDTO = null;
        Session session = currentSession();
        Query sqlQuery = session.getNamedQuery("GET_ACH_DETAILS")
                             .setParameter("siteId", siteId)
                             .setParameter("paymentType", paymentType.toString())
                             .setParameter("modifiedBy", modifiedBy)
                             .setParameter("machineName", machineIp)
                             .setParameter("markAsSettled", "N")
    						 .setParameter("doACH", "N");
        List<Object> resultList = sqlQuery.list();
        if(!resultList.isEmpty()) {
            Object[] row = (Object[]) resultList.get(0);
            acHDTO = new ACHTxDTO();
            acHDTO.setSiteName(this.getString(row[0]));
            acHDTO.setCheckNum(this.getString(row[1]));
            acHDTO.setTotalTransactions(this.getInteger(row[2]));
            acHDTO.setTxAmount(this.getDoubleFromBigDecimal(row[3]));
            acHDTO.setResultMsg(this.getString(row[4]));
            acHDTO.setTxCutOffTime(this.getDate(row[5]));
            acHDTO.setAchLoginKey(row[6] == null ? null : this.getPbeStringEncryptor().decrypt(row[6].toString()));
            acHDTO.setPin(row[7] == null ? null : this.getPbeStringEncryptor().decrypt(row[7].toString()));
            acHDTO.setToAcctName(this.getString(row[8]));
            acHDTO.setToAcctNumber(this.getString(row[9]));
            acHDTO.setToAcctRoutingNo(this.getString(row[10]));
            acHDTO.setStartTxDate(this.getDate(row[11]));
            acHDTO.setClientIp(machineIp);
            acHDTO.setPaymentType(paymentType);
            acHDTO.setSiteId(siteId);
            acHDTO.setFromAccountType(this.getString(row[12]));
            acHDTO.setFromBankIdType(this.getString(row[13]));
            acHDTO.setSpName(this.getString(row[14]));
            acHDTO.setCustomerPermId(this.getString(row[15]));
            acHDTO.setToAccountType(this.getString(row[16]));
            acHDTO.setToBankIdType(this.getString(row[17]));
            acHDTO.setToAddressLine1(this.getString(row[18]));
            acHDTO.setToAddressLine2(this.getString(row[19]));
            acHDTO.setToCity(this.getString(row[20]));
            acHDTO.setToState(this.getString(row[21]));
            acHDTO.setToZip(this.getString(row[22]));
            acHDTO.setSftpHost(this.getString(row[23]));
            acHDTO.setSftpPort(this.getLongFromInteger(row[24]).intValue());
            acHDTO.setSftpUser(this.getString(row[25]));
            acHDTO.setSftpPassword(row[26] == null ? null : this.getPbeStringEncryptor().decrypt(row[26].toString()));
            acHDTO.setToWorkingDirectory(this.getString(row[27]));
            acHDTO.setFromWorkingDirectory(this.getString(row[28]));
            acHDTO.setFromAcctNumber(this.getString(row[29]));
            acHDTO.setFromAcctName(this.getString(row[30]));
            acHDTO.setFromAcctRoutingNo(this.getString(row[31]));
        }
        return acHDTO;
    }

    public void updateCheckHistory(String checkNumber, String achTxRefNumber, String achStatus, 
    		String achResponseSeverity, String achResponseStatusDesc, String achResponseErrorDesc,
    		Date achResponseStatusDate, String modifiedBy, String machineIp) {
        Session session = currentSession();
        Query checkHistoryQuery = session.createQuery("from CheckHistory where checkNumber = :checkNumber");
        checkHistoryQuery.setParameter("checkNumber", Long.parseLong(checkNumber));
        CheckHistory checkHistory = (CheckHistory)checkHistoryQuery.list().get(0);
        checkHistory.setAchTxRefNumber(achTxRefNumber);
        checkHistory.setModifiedDate(new Date());
        if(!StringUtils.isBlank(achStatus)) {
        	checkHistory.setAchStatus(StringUtils.substring(achStatus, 0, 48));
        }
        if(!StringUtils.isBlank(achResponseSeverity)) {
        	checkHistory.setAchResponseSeverity(StringUtils.substring(achResponseSeverity, 0, 48));
        }
		if(!StringUtils.isBlank(achResponseStatusDesc)) {
		    checkHistory.setAchResponseStatusDesc(StringUtils.substring(achResponseStatusDesc, 0, 98)); 	
		}
		if(!StringUtils.isBlank(achResponseErrorDesc)) {
			checkHistory.setAchResponseErrorDesc(StringUtils.substring(achResponseErrorDesc, 0, 98));
		}
		if(achResponseStatusDate != null) {
			checkHistory.setAchResponseStatusDate(achResponseStatusDate);
		}
		if(machineIp != null) {
			checkHistory.setMachineName(StringUtils.substring(machineIp, 0, 40));
		}
		if(modifiedBy != null) {
			checkHistory.setModifiedBy(StringUtils.substring(modifiedBy, 0, 48));
		}
		session.saveOrUpdate(checkHistory);
        session.flush();
    }

    @Cacheable("getACHDetailsForSFTP")
	public ACHTxDTO getACHDetailsForSFTP() {
		 ACHTxDTO acHDTO = null;
	        Session session = currentSession();
	        Query sqlQuery = session.getNamedQuery("GET_ACH_SFTP_INFO");
	        List<Object> resultList = sqlQuery.list();
	        if(!resultList.isEmpty()) {
	            Object[] row = (Object[]) resultList.get(0);
	            acHDTO = new ACHTxDTO();
	            acHDTO.setSftpHost(this.getString(row[0]));
	            acHDTO.setSftpPort(this.getLongFromInteger(row[1]).intValue());
	            acHDTO.setSftpUser(this.getString(row[2]));
	            acHDTO.setSftpPassword(row[3] == null ? null : this.getPbeStringEncryptor().decrypt(row[3].toString()));
	            acHDTO.setToWorkingDirectory(this.getString(row[4]));
	            acHDTO.setFromWorkingDirectory(this.getString(row[5])); 
	        }
	        return acHDTO;
	}

	public ACHTxDTO getACHDetailsByPaymentReferenceID(String paymentReferenceId) {
		ACHTxDTO acHDTO = null;
        Session session = currentSession();
        Query sqlQuery = session.getNamedQuery("GET_ACH_BY_PR_ID")
        						.setParameter("paymentReferenceId", paymentReferenceId);
        List<Object> resultList = sqlQuery.list();
        if(!resultList.isEmpty()) {
            Object[] row = (Object[]) resultList.get(0);
            acHDTO = new ACHTxDTO();
            acHDTO.setSiteId(this.getLongFromInteger(row[0]));
            acHDTO.setCheckNum(this.getString(row[1]));
            acHDTO.setPaymentType(this.getPaymentType(this.getString(row[2])));
            acHDTO.setPaymentReferenceId(paymentReferenceId);
            acHDTO.setAchStatus(this.getString(row[4]));
        }
        return acHDTO;
	}
    
    
}