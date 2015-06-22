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

    /*private String doFidelityACHTransfer(ACHTxDTO achDTO) throws SDLBusinessException, Exception {
    	String achTxRefNumber = null;
        String amount = String.valueOf(achDTO.getTxAmount());
        String siteName = achDTO.getSiteName();
        String tranNum = achDTO.getCheckNumber();
        String routingNumber = achDTO.getAcctRoutingNo();
        String accountNumber = achDTO.getAcctNumber();

        if(StringUtils.isBlank(amount)){
        	throw new SDLBusinessException("xAmount Cannot Be Null/Empty");
		}
        if(StringUtils.isBlank(siteName)){
        	throw new SDLBusinessException("xName i.e, xName Cannot Be Null/Empty");
		}
        if(StringUtils.isBlank(tranNum)){
        	throw new SDLBusinessException("xTranNum i.e, checkNumber Cannot Be Null/Empty");
		}
        if(StringUtils.isBlank(routingNumber)){
        	throw new SDLBusinessException("xRouting Cannot Be Null/Empty");
		}
        if(StringUtils.isBlank(accountNumber)){
        	throw new SDLBusinessException("xAccount Cannot Be Null/Empty");
		}

        Map<String, String> vars = new HashMap<String, String>();
        vars.put("xAmount", amount);
        vars.put("xName", siteName);
        vars.put("xTranNum", tranNum);
        vars.put("xRouting", routingNumber);
        vars.put("xAccount", accountNumber);
        RestTemplate restTemplate = new RestTemplate();
        logger.info("ACH Request String: " + url);
        String responseString = restTemplate.getForObject(url, String.class, vars);
		logger.info("ACH Response String: " + responseString);
		FidelityResponse fidelityResponse = this.constructFidelityResponse(SystemUtil.decodeURL(responseString));
		if(fidelityResponse.getxResult().equalsIgnoreCase("A")) {
			achTxRefNumber = fidelityResponse.getxRefNum();
		} else if (fidelityResponse.getxResult().equalsIgnoreCase("D")) {
			 throw new SDLBusinessException(fidelityResponse.getxError());
		} else if (fidelityResponse.getxResult().equalsIgnoreCase("E")) {
			 throw new SDLBusinessException(fidelityResponse.getxError());
		}
		return achTxRefNumber;
    }

   *//** This method is used to build FidelityResponse object from the fidelityResponse String.
    * The method tokenizes the responseString with '&' as delimiter and populates each variable in FidelityResponse object
    * accordingly
 * @throws SchedulerException **//*
   private FidelityResponse constructFidelityResponse(String responseString) {
		FidelityResponse fidelityResponse = null;
		StringTokenizer responseStringTokenizer = new StringTokenizer(responseString, "&");
		fidelityResponse = new FidelityResponse();
    	String keyValuePair = null;
		while (responseStringTokenizer.hasMoreTokens()) {
			keyValuePair = responseStringTokenizer.nextToken();
			if (!StringUtils.isBlank(keyValuePair)) {
				if (keyValuePair.contains("xResult")) {
					fidelityResponse.setxResult(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xStatus")) {
					fidelityResponse.setxStatus(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xError")) {
					fidelityResponse.setxError(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xAuthCode")) {
					fidelityResponse.setxAuthCode(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xRefNum")) {
					fidelityResponse.setxRefNum(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xToken")) {
					fidelityResponse.setxToken(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xBatch")) {
					fidelityResponse.setxBatch(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xAvsResult")) {
					fidelityResponse.setxAvsResult(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xAvsResultCode")) {
					fidelityResponse.setxAvsResultCode(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xCvvResult")) {
					fidelityResponse.setxCvvResult(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
				if (keyValuePair.contains("xCvvResultCode")) {
					fidelityResponse.setxCvvResultCode(StringUtils.substring(keyValuePair,
							StringUtils.indexOf(keyValuePair, "=") + 1));
				}
			}
		}
    	return fidelityResponse;
	}*/

    @Transactional(readOnly = true)
	public void scheduleJobsForACHTransfersForAllSites() throws SchedulerException {
    	Scheduler scheduler = null;
    	List<Site> sites = this.eComDAO.getSites();
    	if(sites!=null && sites.size()>0) {
    		scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			logger.info("Scheduler Has Been Intialized to Do ACH Transfers.");
    		for(Site site: sites) {
	    		if(site.isActive() && site.isAchEnabled() && site.isCronScheduleForAchTransferEnabled()) {
	    			logger.info("Creating Job For Site: {} with Trigger: {} to Do ACH Transfers. ", site.getName(), 
	    					site.getCronTriggerForACHTransfer());
	    			JobDetailImpl job = (JobDetailImpl) newJob(ACHTransferRqJob.class)
	    					.withIdentity(site.getName(), "ACH Transfer Requests").build();
	    			JobDataMap jobDataMap = new JobDataMap();
	    			jobDataMap.put("siteId", site.getId().intValue());
	    			job.setDurability(true);
	    			job.setJobDataMap(jobDataMap);
	    			Trigger trigger = newTrigger().withSchedule(
	    					CronScheduleBuilder.cronSchedule(site.getCronTriggerForACHTransfer())).build();
	    			scheduler.scheduleJob(job, trigger);
	    			logger.info("Successful Creation Of Job For Site: {} with Trigger: {} to Do ACH Transfers. ", 
	    					site.getName(), site.getCronTriggerForACHTransfer());
	    		}
	    	}
    		logger.info("Scheduler Is Now Running Which Has Been Intialized to Do ACH Transfers.");
    	} else {
    		logger.error(NOTIFY_ADMIN, "There are no Sites. Something went Wrong with eComDAO.getSites() method.");
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
		Trigger trigger = newTrigger().withSchedule(
				CronScheduleBuilder.cronSchedule(achPollCronTrigger)).build();
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
