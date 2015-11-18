package com.fdt.achtx.service;

import java.util.Date;

import org.quartz.SchedulerException;

import com.fdt.achtx.dto.ACHTxDTO;
import com.fdt.common.exception.SDLBusinessException;
import com.fdt.common.exception.SDLException;
import com.fdt.ecom.entity.enums.PaymentType;

public interface ACHTxService {

    /**
     * @param siteId
     * @param paymentType
     * @param createdBy
     * @param machineName
     * @return
     * @throws SDLException
     */
    public ACHTxDTO getACHDetailsAndTransferXMLRequest(Long siteId, PaymentType paymentType,
            String createdBy, String machineName) throws SDLException;

    /**
     * @param paymentType
     * @param siteId
     * @param machineIp
     * @param createdBy
     * @return
     * @throws SDLException
     * @throws SDLBusinessException
     */
    public ACHTxDTO getACHDetailsForTransfer(Long siteId, PaymentType paymentType, String createdBy,
            String machineIp) throws SDLException, SDLBusinessException;

    public void scheduleJobsForACHTransfersForAllSites() throws SchedulerException;

    public void scheduleJobForPollingACHResponses() throws SchedulerException;

    public ACHTxDTO getACHDetailsForSFTP();

    public void updateCheckHistory(String checkNumber, String achTxRefNumber, String achStatus,
            String achResponseSeverity, String achResponseStatusDesc, String achResponseErrorDesc,
            Date achResponseStatusDate, String machineName, String modifiedBy);

    public ACHTxDTO getACHDetailsByPaymentReferenceID(String paymentReferenceId);
}