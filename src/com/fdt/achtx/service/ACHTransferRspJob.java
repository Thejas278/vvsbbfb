package com.fdt.achtx.service;

import static com.fdt.common.SystemConstants.NOTIFY_ADMIN;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fdt.achtx.dto.ACHTxDTO;
import com.fdt.achtx.dto.integratedpayables.IntegratedPayableXML;
import com.fdt.achtx.dto.integratedpayables.Status;
import com.fdt.common.job.LoadBalancedJob;
import com.fdt.common.util.SystemUtil;

@Service
public class ACHTransferRspJob extends LoadBalancedJob {

    private final static Logger logger = LoggerFactory.getLogger(ACHTransferRspJob.class);

    @Override
    public void doExecute(JobExecutionContext job) {

        String machineName = getMachineName();
        String modifiedBy = getModifiedBy();
        ACHTxDTO achTxDTO = getACHTxService().getACHDetailsForSFTP();
        if (achTxDTO == null) {
            logger.error("ACH SFTP Details Missing !");
            return;
        }

        try {
            IntegratedPayablesUtil.retrieveXMLResponseFiles(achTxDTO);
            File[] files = IntegratedPayablesUtil.getListOfFiles(achTxDTO.getLocalDir());
            if (files != null && files.length > 0) {
                for (File file : files) {
                    processFile(machineName, modifiedBy, file);
                }
            } else {
                logger.info("No ACH response has been found. Time: {} ", SystemUtil.format(
                        new Date().toString(), "EEE MMM dd hh:mm:ss zzz yyyy", " yyyy/MM/dd_hh:mm:ss a"));
            }

            IntegratedPayablesUtil.deleteAllFiles(achTxDTO.getLocalDir());
        } catch (Exception e) {
            logger.error("Error While Retriving XML Response files ", e);
        }

    }

    private void processFile(String machineName, String modifiedBy, File file) {
        try {
            String xmlContent = IntegratedPayablesUtil.getFileContent(file.getAbsolutePath(),  StandardCharsets.UTF_8);
            IntegratedPayableXML achResponseObject = IntegratedPayablesUtil.unMarshallToIntegratedPayableXML(xmlContent);
            if (achResponseObject == null) {
                logger.warn("Failed to create ACH response object from file XML. Skipping.");
                return;
            }
            String referenceId = achResponseObject.getBankServiceRequest().getTransferAddResponse().getSpReferenceId();
            if (referenceId.equalsIgnoreCase("Acknowledged")) {
                logger.info("Acknowledged Status achResponseObject: {}", achResponseObject);
                Status status = achResponseObject.getBankServiceRequest().getTransferAddResponse().getStatus();
                String paymentReferenceId = status.getPaymentReferenceId();
                ACHTxDTO achTxDTO2 = getACHTxService().getACHDetailsByPaymentReferenceID(paymentReferenceId);
                if (achTxDTO2 != null && !StringUtils.isBlank(achTxDTO2.getCheckNumber())) {
                    if (isAlreadyUpdatedCheckHistory(achTxDTO2, "ACKNOWLEDGEMENT_RECIEVED")) {
                        logger.info("Skipping since ACKNOWLEDGEMENT_RECIEVED status is already updated.");
                        return;
                    }
                    getACHTxService().updateCheckHistory(achTxDTO2.getCheckNumber(), paymentReferenceId,
                            "ACKNOWLEDGEMENT_RECIEVED", status.getSeverity(), status.getStatusDesc(),
                            status.getErrorDesc(),
                            IntegratedPayablesUtil.getStatusDate(status.getAsOfDate(), status.getAsOfTime()),
                            modifiedBy, machineName);
                    logger.info("Acknowledged Status Updated.");
                } else {
                    logger.error("Some other ACKNOWLEDGEMENT_RECIEVED files are Present.");
                }
            } else if (referenceId.equalsIgnoreCase("Rejected")) {
                logger.info("Rejected Status achResponseObject: {}", achResponseObject);
                Status status = achResponseObject.getBankServiceRequest().getTransferAddResponse().getStatus();
                String paymentReferenceId = status.getPaymentReferenceId();
                ACHTxDTO achTxDTO2 = getACHTxService().getACHDetailsByPaymentReferenceID(paymentReferenceId);
                if (achTxDTO2 != null && !StringUtils.isBlank(achTxDTO2.getCheckNumber())) {
                    if (isAlreadyUpdatedCheckHistory(achTxDTO2, "REQUEST_REJECTED")) {
                        logger.info("Skipping since REQUEST_REJECTED status is already updated.");
                        return;
                    }
                    getACHTxService().updateCheckHistory(achTxDTO2.getCheckNumber(), paymentReferenceId,
                            "REQUEST_REJECTED", status.getSeverity(), status.getStatusDesc(),
                            status.getErrorDesc(),
                            IntegratedPayablesUtil.getStatusDate(status.getAsOfDate(), status.getAsOfTime()),
                            modifiedBy, machineName);
                } else {
                    logger.error("Some other REQUEST_REJECTED files are Present.");
                    return;
                }
                if (getEComService().doVoidCheck(Long.valueOf(achTxDTO2.getCheckNumber()),
                        "Voided By ACHTransferRspJob.", modifiedBy)) {
                    logger.info("Check Voided Successfully By ACHTransferRspJob.");
                } else {
                    logger.error("Check Could not be Voided By ACHTransferRspJob.");
                }
                logger.error(NOTIFY_ADMIN, "ACH Transfer Could not be Processed. Details: {}",
                        achResponseObject);
                logger.info("Rejected Status Updated.");
            } else if (referenceId.equalsIgnoreCase("Final Confirmation")) {
                logger.info("Final Confirmation Status achResponseObject: {}", achResponseObject);
                Status status = achResponseObject.getBankServiceRequest().getTransferAddResponse().getStatus();
                String paymentReferenceId = status.getPaymentReferenceId();
                ACHTxDTO achTxDTO2 = getACHTxService().getACHDetailsByPaymentReferenceID(paymentReferenceId);
                if (achTxDTO2 != null && !StringUtils.isBlank(achTxDTO2.getCheckNumber())) {
                    if (isAlreadyUpdatedCheckHistory(achTxDTO2, "FINAL_CONFIRMATION_RECIEVED")) {
                        logger.info("Skipping since FINAL_CONFIRMATION_RECIEVED status is already updated.");
                        return;
                    }
                    getACHTxService().updateCheckHistory(achTxDTO2.getCheckNumber(), paymentReferenceId, 
                            "FINAL_CONFIRMATION_RECIEVED", status.getSeverity(), status.getStatusDesc(), 
                            status.getErrorDesc(), 
                            IntegratedPayablesUtil.getStatusDate(status.getAsOfDate(),status.getAsOfTime()),
                            modifiedBy, machineName);
                    logger.info("Final Confirmation Status Updated.");
                } else {
                    logger.error("Some other FINAL_CONFIRMATION_RECIEVED files are Present.");
                }
            } else {
                logger.info("Other Status achResponseObject: {}", achResponseObject );
            }
        } catch (Exception e) {
            logger.error("Error While performing ACH Transfer Response Job ", e);
        }
    }

    @Override
    protected String getJobName() {
        return getClass().getSimpleName();
    }

    private boolean isAlreadyUpdatedCheckHistory(ACHTxDTO achTxDTO2, String achStatus) {
        if (achTxDTO2.getAchStatus().equals(achStatus)) {
            return true;
        } else {
            return false;
        }
    }

    private String getModifiedBy() {
        String createdBy = "ACH_RSP_POLL_SCHDLR";
        try {
            String appendTxt = SystemUtil.format(
                    new Date().toString(),
                    "EEE MMM dd hh:mm:ss zzz yyyy",
                    "_yyyy/MM/dd_hh:mm:ss_a");
            if (!StringUtils.isBlank(appendTxt)) {
                createdBy = createdBy.concat(appendTxt);
            }
        } catch (Exception e) {
            logger.error("Error while generating ACH Timestamp For modifiedBy ", e);
            throw e;
        }
        return createdBy;
    }

    private String getMachineName() {
        String machineName = "ECOM_SERVER";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            if (!StringUtils.isBlank(hostname)) {
                machineName = hostname;
            }
        } catch (UnknownHostException ex) {
            logger.error("Machine Name Can not be resolved");
        }
        return machineName;
    }
}
