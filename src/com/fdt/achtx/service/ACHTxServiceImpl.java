package com.fdt.achtx.service;

import static com.fdt.common.SystemConstants.NOTIFY_ADMIN;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fdt.achtx.dao.ACHTxDAO;
import com.fdt.achtx.dto.ACHTxDTO;
import com.fdt.common.exception.SDLBusinessException;
import com.fdt.common.exception.SDLException;
import com.fdt.ecom.dao.EComDAO;
import com.fdt.ecom.entity.Site;
import com.fdt.ecom.entity.enums.PaymentType;

@Service("aCHTransacationService")
public class ACHTxServiceImpl implements ACHTxService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${achtransactions.mode}")
    private String achMode = null;
    
    @Value("${achtransactions.poll.cron}")
    private String achPollCronTrigger = null;
    
    @Value("${achtransactions.tempDirectory}")
    private String achTempDirectory = null;

    @Autowired
    private ACHTxDAO achDAO = null;
    
    @Autowired
    private EComDAO eComDAO = null;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ACHTxDTO getACHDetailsAndTransferXMLRequest(Long siteId, PaymentType paymentType,
        String createdBy, String machineName) throws SDLException {
    	ACHTxDTO achDTO = null;
    	try {
    		achDTO = this.achDAO.doACHTransfer(siteId, paymentType, createdBy, machineName);
    		if(achDTO != null && achDTO.getTxAmount() > 0.0d) {
    			 String xmlRequest = IntegratedPayablesUtil.generateXMLRequest(achDTO, achMode);
    			 logger.info("XML Request For Site: {}, Payment Type: {} : {}",achDTO.getSiteName(), paymentType, 
    					 xmlRequest);
    			 achDTO.setXmlContent(xmlRequest);
    			 achDTO.setFileName(IntegratedPayablesUtil.generateFileName(achDTO, achMode));
    			 
    			 String achTxRefNumber = achDTO.getPaymentReferenceId();
    			 achDTO.setTxRefNumber(achTxRefNumber);
                 this.achDAO.updateCheckHistory(achDTO.getCheckNumber(), achTxRefNumber, "REQUEST_SENT", null, null, null,
                		 new Date(), createdBy, machineName);
                 
    			 IntegratedPayablesUtil.sftpFile(achDTO);
    			 logger.info("XML File {} SFTPed Successfully..", achDTO.getFileName());
    			 
    		} else {
    			logger.info("There are No Tranasctions to be settled for Site: {} and Payment type: {}", siteId, paymentType);
    		}
	    } catch (Exception exception) {
	       throw new SDLException("Error Occurred while doing ACH Transfer: " + exception.getMessage());
	    }
        return achDTO;
    }
    
    @Transactional(readOnly = true)
    public ACHTxDTO getACHDetailsForSFTP() {
    	ACHTxDTO achDTO = this.achDAO.getACHDetailsForSFTP();
    	if(achDTO!=null) {
    		achDTO.setLocalDir(achTempDirectory);
    	}
        return achDTO;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public ACHTxDTO getACHDetailsForTransfer(Long siteId, PaymentType paymentType, String createdBy,
 		   String machineIp) throws SDLException, SDLBusinessException {
        ACHTxDTO achDTO = null;
        try {
            achDTO = this.achDAO.getACHDetailsForTransfer(siteId, paymentType, createdBy, machineIp);            
        } catch (Exception exception) {
            logger.error(NOTIFY_ADMIN, "Exception Occured in " + exception);
            throw new SDLException("Error Occurred while doing ACH Transfer: " + exception.getMessage());
        }
        return achDTO;
    }

    @Transactional(readOnly = true)
    public void scheduleJobsForACHTransfersForAllSites() throws SchedulerException {
        Scheduler scheduler = null;
        List<Site> sites = eComDAO.getSites();
        if (sites != null && sites.size() > 0) {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            logger.info("Scheduler Has Been Intialized to Do ACH Transfers.");
            for (Site site : sites) {
                if (site.isActive() && site.isAchEnabled() && site.isCronScheduleForAchTransferEnabled()) {

                    String scheduleStr = site.getCronTriggerForACHTransfer();
                    CronScheduleBuilder schedule = CronScheduleBuilder.cronSchedule(scheduleStr);

                    logger.info("Creating Job For Site: {} with Trigger: {} to Do ACH Transfers. ",
                            site.getName(), scheduleStr);

                    JobDetailImpl job = (JobDetailImpl) newJob(ACHTransferRqJob.class)
                            .withIdentity(site.getName(), "ACH Transfer Requests").build();
                    JobDataMap jobDataMap = new JobDataMap();
                    jobDataMap.put("siteId", site.getId().intValue());
                    job.setDurability(true);
                    job.setJobDataMap(jobDataMap);

                    Trigger trigger = newTrigger().withSchedule(schedule).build();
                    scheduler.scheduleJob(job, trigger);

                    logger.info("Successful Creation Of Job For Site: {} with Trigger: {} to Do ACH Transfers. ",
                            site.getName(), site.getCronTriggerForACHTransfer());
                }
            }
            logger.info("Scheduler Is Now Running Which Has Been Intialized to Do ACH Transfers.");
        } else {
            logger.error(NOTIFY_ADMIN,
                    "There are no Sites. Something went Wrong with eComDAO.getSites() method.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void scheduleJobForPollingACHResponses() throws SchedulerException {
        Scheduler scheduler = null;
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        logger.info("Scheduler Has Been Intialized to Retrive ACH Responses.");
        logger.info("Creating Job To Retrive ACH Responses with Trigger: {}", achPollCronTrigger);
        JobDetailImpl job = (JobDetailImpl) newJob(ACHTransferRspJob.class)
                .withIdentity("ACH Transfer Response", "ACH Transfer Responses").build();
        job.setDurability(true);
        Trigger trigger = newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(achPollCronTrigger)).build();
        scheduler.scheduleJob(job, trigger);
        logger.info("Scheduler Is Now Running Which Has Been Intialized To Retrive ACH Responses");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void updateCheckHistory(String checkNumber, String achTxRefNumber,
			String achStatus, String achResponseSeverity,
			String achResponseStatusDesc, String achResponseErrorDesc,
			Date achResponseStatusDate, String machineName, String modifiedBy) {
		this.achDAO.updateCheckHistory(checkNumber, achTxRefNumber, achStatus, 
				achResponseSeverity, achResponseStatusDesc, achResponseErrorDesc,
				achResponseStatusDate, machineName, modifiedBy);
		
	}

    @Transactional(readOnly = true)
	public ACHTxDTO getACHDetailsByPaymentReferenceID(String paymentReferenceId) {
		return this.achDAO.getACHDetailsByPaymentReferenceID(paymentReferenceId);
	}

}
