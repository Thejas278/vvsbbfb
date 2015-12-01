package com.fdt.ecom.service;

import static com.fdt.common.SystemConstants.NOTIFY_ADMIN;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.fdt.achtx.service.ACHTxService;
import com.fdt.otctx.service.OTCTXService;
import com.fdt.payasugotx.service.PayAsUGoTxService;
import com.fdt.recurtx.dto.OverriddenSubscriptionDTO;
import com.fdt.recurtx.dto.RecurTxSchedulerDTO;
import com.fdt.recurtx.service.RecurTxService;
import com.fdt.recurtx.service.admin.RecurTxAdminService;
import com.fdt.security.entity.User;
import com.fdt.security.entity.UserAccess;
import com.fdt.security.service.admin.UserAdminService;
import com.fdt.webtx.service.WebTxService;

@Service
public class EComSchedulerService {

    private final static Logger logger = LoggerFactory.getLogger(EComSchedulerService.class);

    @Autowired
    private RecurTxAdminService recurTXAdminService = null;
    
    @Autowired
    private ACHTxService aCHTransacationService = null;

    @Autowired
    private RecurTxService recurTxService = null;

    @Autowired
    private PayAsUGoTxService payAsUGoTxService = null;

    @Autowired
    private UserAdminService userAdminService = null;

    @Autowired
    private OTCTXService oTCTXService = null;

    @Autowired
    private WebTxService webTxService = null;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Value("${scheduler.ecom.isenabled}")
    private boolean isSchedulerEnabled = false;
    
    @Value("${achtransactions.scheduler.isenabled}")
    private boolean isACHSchedulerEnabled = false;

    @Scheduled(cron= "${scheduler.ecom.chargerecurringprofiles}")
    public void chargeRecurringProfiles() {
        if (!this.isSchedulerEnabled()) {
            logger.info("Skiping Scheduler Service Charge Recurring Profiles");
            return;
        }
        logger.info("Starting Scheduler Service To Charge Recurring Profiles");
        List <RecurTxSchedulerDTO> recurTxSchedulerDTOs = recurTXAdminService.getRecurProfilesForVerification();
        for (final RecurTxSchedulerDTO recurTxSchedulerDTO : recurTxSchedulerDTOs) {
            /** Start of Transaction **/
            transactionTemplate.execute(new TransactionCallback<Void>() {
                public Void doInTransaction(TransactionStatus txStatus) {
                    try {
                        recurTXAdminService.chargeRecurSub(recurTxSchedulerDTO);
                    } catch (Exception exp) {
                        logger.error(NOTIFY_ADMIN, "Exception in the EComScheduler for RecurTxSchedulerDTO {}",
                            recurTxSchedulerDTO, exp);
                        txStatus.setRollbackOnly();
                    }
                    return null;
                }
            });
            /** End of of Transaction **/
        }
        logger.info("Ending Scheduler Service Charge Recurring Profiles");
    }
    
    @Scheduled(cron= "${scheduler.ecom.disableoverriddensubscriptions}")
    public void disableOverriddenSubscriptions() {
        if (!this.isSchedulerEnabled()) {
            logger.info("Skiping Scheduler Service For Disabling Expired Overridden Subscriptions");
            return;
        }
        logger.info("Starting Scheduler Service To  Disable Overridden Subscriptions");
        List <OverriddenSubscriptionDTO> expiredOverriddenSubscriptionDTOList = recurTXAdminService.getExpiredOverriddenSubscriptions();
        for (final OverriddenSubscriptionDTO expiredOverriddenSubscriptionDTO : expiredOverriddenSubscriptionDTOList) {
            /** Start of Transaction **/
            transactionTemplate.execute(new TransactionCallback<Void>() {
                public Void doInTransaction(TransactionStatus txStatus) {
                	try {
                        recurTXAdminService.disableOverriddenSubscription(expiredOverriddenSubscriptionDTO);
                    } catch (Exception exp) {
                        logger.error(NOTIFY_ADMIN, "Exception in the ECom Scheduler For Disabling Expired Overridden Subscription {}",
                        		expiredOverriddenSubscriptionDTO, exp);                        
                    }
                    return null;
                }
            });
            /** End of of Transaction **/
        }
        logger.info("Ending Scheduler Service For Disabling Expired Overridden Subscriptions");
    }

    @Scheduled(cron= "${scheduler.ecom.warnexpiringoverriddensubscriptions}")
    public void warnExpiringOverriddenSubscriptions() {
        if (!this.isSchedulerEnabled()) {
            logger.info("Skiping Scheduler Service For Warning Expiring Overridden Subscriptions");
            return;
        }
        logger.info("Starting Scheduler Service For Warning Expiring Overridden Subscriptions");
        List <OverriddenSubscriptionDTO> dtoList = recurTXAdminService.getExpiringOverriddenSubscriptions();
        for (OverriddenSubscriptionDTO dto : dtoList) {
            userAdminService.warnExpiringOverriddenSubscription(dto);
        }
        logger.info("Ending Scheduler Service For Warning Expiring Overridden Subscriptions");
    }

    @Scheduled(cron= "${scheduler.ecom.cancelrecurringprofile}")
    public void cancelRecurringProfiles() {
        if (!this.isSchedulerEnabled()) {
            logger.info("Skiping Scheduler Service Cancel Recurring Profiles");
            return;
        }
        logger.info("Starting Scheduler Service Cancel Pay Pal Profiles");
        /** Start of Transaction **/
        transactionTemplate.execute(new TransactionCallback<Void>() {
            public Void doInTransaction(TransactionStatus txStatus) {
                try {
                    recurTXAdminService.archiveCancelledRecurSub();
                } catch (RuntimeException runTimeException) {
                    logger.error(NOTIFY_ADMIN, "Exception in the cancelRecurringProfiles", runTimeException);
                }
                return null;
            }
        });
        /** End of of Transaction **/
        logger.info("Ending Scheduler Service Cancel Recurring Profiles");
    }

    @Scheduled(cron= "${scheduler.ecom.notifyinactiveusers}")
    public void notifyInactiveUsers() {
        if (!this.isSchedulerEnabled()) {
            logger.info("Skiping Scheduler Service Notify Inactive Users");
            return;
        }
        logger.info("Starting Scheduler Service Notify Inactive Users");
        List<User> users = this.userAdminService.getInactiveUsers();
        for (User user : users) {
        	boolean isNotifyUser = true;
        	List<UserAccess> userAccessList = user.getUserAccessList();
        	if(userAccessList != null && userAccessList.size() > 0) {
        		for(UserAccess userAccess : userAccessList) {
            		if(userAccess.isAccessOverriden()) {
            			isNotifyUser = false;
            		}
            	}
            	if (isNotifyUser) {
            		this.userAdminService.notifyInactiveUsers(user);
            	}
        	}
        }
        logger.info("Ending Scheduler Service Notify Inactive Users");
    }

    @Scheduled(cron= "${scheduler.ecom.archivetransactions}")
    public void archiveTransactions() {
        if (!this.isSchedulerEnabled()) {
            logger.info("Skiping Scheduler Service Archive Transactions");
            return;
        }
        String archivedBy = "SYSTEM";
        String archiveComments = "Archived By ACCEPT Archiver.";
        logger.info("Starting Scheduler Service Archive Transactions");
        this.recurTxService.archiveRecurTransactions(archivedBy, archiveComments);
        this.payAsUGoTxService.archivePayAsUGoTransactions(archivedBy, archiveComments);
        this.oTCTXService.archiveOTCTransactions(archivedBy, archiveComments);
        this.webTxService.archiveWebTransactions(archivedBy, archiveComments);
        logger.info("Ending Scheduler Service Archive Transactions");
    }

    @PostConstruct
    public void scheduleJobsForACHTransfers() {
        if (!this.isACHSchedulerEnabled()) {
            logger.info("Skiping Scheduler Service For ACH Transfers.");
            return;
        }
        logger.info("Starting Scheduler Service For ACH Transfers.");
        try {
            this.aCHTransacationService.scheduleJobsForACHTransfersForAllSites();
            this.aCHTransacationService.scheduleJobForPollingACHResponses();
        }  catch (RuntimeException | SchedulerException runTimeException) {
            logger.error(NOTIFY_ADMIN, "Exception in Scheduler Service For ACH Transfers.", runTimeException);
        }
        logger.info("Ending Scheduler Service For ACH Transfers.");
    }

    private boolean isACHSchedulerEnabled() {
        return (new Boolean(this.isACHSchedulerEnabled)).booleanValue();
    }

    private boolean isSchedulerEnabled() {
        return (new Boolean(this.isSchedulerEnabled)).booleanValue();
    }
}