package com.fdt.achtx.service;

import static com.fdt.common.SystemConstants.NOTIFY_ADMIN;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.fdt.achtx.dto.ACHTxDTO;
import com.fdt.achtx.dto.integratedpayables.IntegratedPayableXML;
import com.fdt.achtx.dto.integratedpayables.Status;
import com.fdt.common.util.SystemUtil;
import com.fdt.ecom.service.EComService;

@Service
public class ACHTransferRspJob implements Job, ApplicationContextAware  {
	
	private final static Logger logger = LoggerFactory.getLogger(ACHTransferRspJob.class);
	
	private static ApplicationContext applicationContext = null;
	
	@Autowired
    private ACHTxService aCHTransacationService = null;
	
	@Autowired
    private EComService ecomService = null;

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException{
		
		String machineName = this.getMachineName();
		String modifiedBy = this.getModifiedBy();
		ACHTxDTO achTxDTO = getACHTxService().getACHDetailsForSFTP();
		if(achTxDTO == null) {
			logger.error("ACH SFTP Details Missing !");
			return;
		}
		try {
			IntegratedPayablesUtil.retrieveXMLResponseFiles(achTxDTO);
			File[] files = IntegratedPayablesUtil.getListOfFiles(achTxDTO.getLocalDir());
			if (files!=null && files.length >0) {
				for(File file: files) {
					try {
						String xmlContent = IntegratedPayablesUtil.getFileContent(file.getAbsolutePath(), 
								StandardCharsets.UTF_8);
						IntegratedPayableXML achResponseObject = IntegratedPayablesUtil
								.unMarshallToIntegratedPayableXML(xmlContent);
						if(achResponseObject!=null && achResponseObject.getBankServiceRequest()
								.getTransferAddResponse().getSpReferenceId().equalsIgnoreCase("Acknowledged")) {
							logger.info("Acknowledged Status achResponseObject: {}", achResponseObject );
							Status status = achResponseObject.getBankServiceRequest().getTransferAddResponse().getStatus();
							String paymentReferenceId = status.getPaymentReferenceId();
							ACHTxDTO achTxDTO2 = getACHTxService().getACHDetailsByPaymentReferenceID(paymentReferenceId);
							if(achTxDTO2!=null && !StringUtils.isBlank(achTxDTO2.getCheckNumber())) {
								if(isAlreadyUpdatedCheckHistory(achTxDTO2, "ACKNOWLEDGEMENT_RECIEVED")) {
									logger.info("Skipping since ACKNOWLEDGEMENT_RECIEVED status is already updated.");
									continue;									
								}
								getACHTxService().updateCheckHistory(achTxDTO2.getCheckNumber(), paymentReferenceId, 
										"ACKNOWLEDGEMENT_RECIEVED", status.getSeverity(), status.getStatusDesc(), 
										status.getErrorDesc(), 
										IntegratedPayablesUtil.getStatusDate(status.getAsOfDate(),status.getAsOfTime()),
										modifiedBy, machineName);
								logger.info("Acknowledged Status Updated.");
							} else {
								logger.error("Some other ACKNOWLEDGEMENT_RECIEVED files are Present.");
							}
						} else if(achResponseObject!=null && achResponseObject.getBankServiceRequest()
								.getTransferAddResponse().getSpReferenceId().equalsIgnoreCase("Rejected")) {
							logger.info("Rejected Status achResponseObject: {}", achResponseObject );
							Status status = achResponseObject.getBankServiceRequest().getTransferAddResponse().getStatus();
							String paymentReferenceId = status.getPaymentReferenceId();
							ACHTxDTO achTxDTO2 = getACHTxService().getACHDetailsByPaymentReferenceID(paymentReferenceId);
							if(achTxDTO2!=null && !StringUtils.isBlank(achTxDTO2.getCheckNumber())) {
								if(isAlreadyUpdatedCheckHistory(achTxDTO2, "REQUEST_REJECTED")) {
									logger.info("Skipping since REQUEST_REJECTED status is already updated.");
									continue;									
								}
								getACHTxService().updateCheckHistory(achTxDTO2.getCheckNumber(), paymentReferenceId, 
									"REQUEST_REJECTED", status.getSeverity(), status.getStatusDesc(), status.getErrorDesc(), 
									IntegratedPayablesUtil.getStatusDate(status.getAsOfDate(),status.getAsOfTime()),
									modifiedBy, machineName);
							} else {
								logger.error("Some other REQUEST_REJECTED files are Present.");
								continue;
							}
							
							if(getEComService().doVoidCheck(Long.valueOf(achTxDTO2.getCheckNumber()),
									"Voided By ACHTransferRspJob.", modifiedBy)) {
								logger.info("Check Voided Successfully By ACHTransferRspJob.");
							} else {
								logger.error("Check Could not be Voided By ACHTransferRspJob.");
							}
							logger.error(NOTIFY_ADMIN, "ACH Transfer Could not be Processed. Details: {}", achResponseObject);
							logger.info("Rejected Status Updated.");
						} else if(achResponseObject!=null && achResponseObject.getBankServiceRequest()
								.getTransferAddResponse().getSpReferenceId().equalsIgnoreCase("Final Confirmation")) {
							logger.info("Final Confirmation Status achResponseObject: {}", achResponseObject );
							Status status = achResponseObject.getBankServiceRequest().getTransferAddResponse().getStatus();
							String paymentReferenceId = status.getPaymentReferenceId();
							ACHTxDTO achTxDTO2 = getACHTxService().getACHDetailsByPaymentReferenceID(paymentReferenceId);
							if(achTxDTO2!=null && !StringUtils.isBlank(achTxDTO2.getCheckNumber())) {
								if(isAlreadyUpdatedCheckHistory(achTxDTO2, "FINAL_CONFIRMATION_RECIEVED")) {
									logger.info("Skipping since FINAL_CONFIRMATION_RECIEVED status is already updated.");
									continue;									
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
			} else {
				logger.info("No ACH response has been found. Time: {} ", SystemUtil
			    		.format(new Date().toString(), "EEE MMM dd hh:mm:ss zzz yyyy"," yyyy/MM/dd_hh:mm:ss a") );
			}
			
			IntegratedPayablesUtil.deleteAllFiles(achTxDTO.getLocalDir());	
		} catch (Exception e) {
			logger.error("Error While Retriving XML Response files ", e);
		}
		
	}
	
	private boolean isAlreadyUpdatedCheckHistory(ACHTxDTO achTxDTO2,
			String achStatus) {
		if(achTxDTO2.getAchStatus().equals(achStatus)) {
			return true;
		} else {
			return false;
		}		
	}

	public void setApplicationContext(ApplicationContext aContext) throws BeansException {
        applicationContext = aContext;
    }
    
    private static ACHTxService getACHTxService() {
    	ACHTxService aCHTransacationService = (ACHTxService) applicationContext.getBean("aCHTransacationService");
        return aCHTransacationService;
    }
    
    private static EComService getEComService() {
    	EComService ecomService = (EComService) applicationContext.getBean("ecomService");
        return ecomService;
    }
	
	private String getModifiedBy() {
		String createdBy = "ACH_RSP_POLL_SCHDLR";
    	try {    		
		    String appendTxt = SystemUtil
		    		.format(new Date().toString(), "EEE MMM dd hh:mm:ss zzz yyyy","_yyyy/MM/dd_hh:mm:ss_a");
		    if(!StringUtils.isBlank(appendTxt)) {
		    	createdBy = createdBy.concat(appendTxt);
		    }
		}
		catch (Exception e) {
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
		    if(!StringUtils.isBlank(hostname)) {
		    	machineName = hostname;
		    }		    
		}
		catch (UnknownHostException ex) {
			logger.error("Machine Name Can not be resolved");
		}
    	return machineName;
    }
}
