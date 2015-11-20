package com.fdt.common.job;

import java.util.List;
import java.util.Properties;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.fdt.achtx.service.ACHTxService;
import com.fdt.common.entity.ActiveJob;
import com.fdt.common.service.JobManagementService;
import com.fdt.ecom.service.EComService;

/**
 * <p>
 * This class, which implements Quartz's Job interface, should be extended by any new jobs
 * which want to take advantage of its database semaphore mechanism for locking and allowing
 * for execution across multiple nodes.
 * </p>
 * 
 * <p>
 * The sub-classed jobs should implement two methods:
 * </p>
 * <ul>
 * <li>{@code getJobName}</li>
 * <li>{@code doExecute}</li>
 * </ul>
 * 
 * <p>
 * {@code getJobName} can return nearly anything but it should be unique.
 * </p>
 * 
 * <p>
 * {@code doExecute} is passed the JobExecutionContext and should perform the normal work
 * of the job, while being confident that no other instance of the job is running at the same
 * time on another server.
 * </p>
 * 
 * @author Jon Miller
 */
public abstract class LoadBalancedJob implements Job, ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(LoadBalancedJob.class);

    protected static ApplicationContext applicationContext;

    protected abstract String getJobName();
    protected abstract void doExecute(JobExecutionContext job);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (clearToRun()) {
            logger.info("Executing job '{}' because no active instance found", getJobName());
            Long activeJobId = createActiveJob();
            logger.info("Created active job in database. ID = {}; job name = {}", activeJobId, getJobName());
            try {
                doExecute(jobExecutionContext);
            } finally {
                deleteActiveJob(activeJobId);
            }
        } else {
            logger.info("Skipping execution of job '{}' because there is an active instance", getJobName());
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LoadBalancedJob.applicationContext = applicationContext;
    }

    protected boolean clearToRun() {
        List<ActiveJob> activeJobs = getJobManagementService().getActiveJobs(getJobName());
        if (!activeJobs.isEmpty()) {
            int numberOfStaleJobs = 0;
            for (ActiveJob activeJob : activeJobs) {
                DateTime createdDate = new DateTime(activeJob.getCreatedDate());
                if (DateTime.now().minusMinutes(getJobTimeoutMins()).isAfter(createdDate)) {
                    logger.info("Delete stale active job. ID = {}; job name = {}; created date = {}; host name = {}",
                            activeJob.getId(), getJobName(), activeJob.getCreatedDate(), activeJob.getHostName());
                    numberOfStaleJobs++;
                    getJobManagementService().deleteActiveJob(activeJob);
                }
            }
            return numberOfStaleJobs == activeJobs.size();
        } else {
            return true;
        }
    }

    protected Long createActiveJob() {
        return getJobManagementService().createActiveJob(getJobName());
    }

    protected void deleteActiveJob(Long activeJobId) {
        getJobManagementService().deleteActiveJob(activeJobId);
    }

    protected static JobManagementService getJobManagementService() {
        Object service = applicationContext.getBean("jobManagementService");
        return (JobManagementService) service;
    }

    protected static ACHTxService getACHTxService() {
        Object service = applicationContext.getBean("aCHTransacationService");
        return (ACHTxService) service;
    }

    protected static EComService getEComService() {
        Object service = applicationContext.getBean("ecomService");
        return (EComService) service;
    }

    protected static Integer getJobTimeoutMins() {
        Properties properties = (Properties) applicationContext.getBean("serverProperties");
        String jobTimeoutMinsStr = properties.getProperty("achtransactions.jobTimeoutMins");
        return Integer.parseInt(jobTimeoutMinsStr);
    }
}
