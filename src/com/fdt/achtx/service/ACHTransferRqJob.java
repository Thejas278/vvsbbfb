package com.fdt.achtx.service;

import static com.fdt.common.SystemConstants.NOTIFY_ADMIN;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fdt.achtx.dto.ACHTxDTO;
import com.fdt.common.exception.SDLException;
import com.fdt.common.job.LoadBalancedJob;
import com.fdt.common.util.SystemUtil;
import com.fdt.ecom.entity.enums.PaymentType;

@Service
public class ACHTransferRqJob extends LoadBalancedJob {

    private final static Logger logger = LoggerFactory.getLogger(ACHTransferRqJob.class);

    @Override
    public void doExecute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        Long siteId = Long.valueOf(jobDataMap.getIntValue("siteId"));
        String machineName = getMachineName();
        String createdBy = getCreatedBy();
        for (PaymentType paymentType : PaymentType.values()) {
            try {
                ACHTxDTO achTxDTO = getACHTxService().getACHDetailsAndTransferXMLRequest(
                        siteId, paymentType, createdBy, machineName);
                logger.info("Site {}  Payment Type: {} achTxDTO: {} ", siteId, paymentType, achTxDTO);
            } catch (SDLException e) {
                logger.error(NOTIFY_ADMIN, "Error In ACH Transfer Job", e);
            }
        }
    }

    @Override
    public String getJobName() {
        return getClass().getSimpleName();
    }

    private String getCreatedBy() {
        String createdBy = "ACH_RQ_XFER_SCHDLR";
        try {
            String appendTxt = SystemUtil.format(
                    new Date().toString(),
                    "EEE MMM dd hh:mm:ss zzz yyyy",
                    "_yyyy/MM/dd_hh:mm:ss_a");
            if (!StringUtils.isBlank(appendTxt)) {
                createdBy = createdBy.concat(appendTxt);
            }
        } catch (Exception e) {
            logger.error("Error while generating ACH Timestamp For createdBy", e);
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
