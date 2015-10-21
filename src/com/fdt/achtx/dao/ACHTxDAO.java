package com.fdt.achtx.dao;

import java.util.Date;

import com.fdt.achtx.dto.ACHTxDTO;
import com.fdt.ecom.entity.enums.PaymentType;

public interface ACHTxDAO {

    public ACHTxDTO doACHTransfer(Long siteId, PaymentType paymentType, String modifiedBy, String machineIp);

    public ACHTxDTO getACHDetailsForTransfer(Long siteId, PaymentType paymentType, String createdBy, String machineName);

    public void updateCheckHistory(String checkNumber, String achTxRefNumber, String achStatus, 
    		String achResponseSeverity, String achResponseStatusDesc, String achResponseErrorDesc,
    		Date achResponseStatusDate, String modifiedBy, String machineIp);
    
    public ACHTxDTO getACHDetailsForSFTP();

	public ACHTxDTO getACHDetailsByPaymentReferenceID(String paymentReferenceId);

}
