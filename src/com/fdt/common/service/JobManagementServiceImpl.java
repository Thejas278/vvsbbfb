package com.fdt.common.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fdt.common.dao.JobManagementDAO;
import com.fdt.common.entity.ActiveJob;

@Service("jobManagementService")
public class JobManagementServiceImpl implements JobManagementService {

    private static final Logger logger = LoggerFactory.getLogger(JobManagementServiceImpl.class);

    @Autowired
    private JobManagementDAO jobManagementDAO;

    @Override
    @Transactional(readOnly = true)
    public List<ActiveJob> getActiveJobs(String jobName) {
        return jobManagementDAO.getActiveJobs(jobName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public Long createActiveJob(String jobName) {
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return jobManagementDAO.createActiveJob(jobName, hostName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void deleteActiveJob(ActiveJob activeJob) {
        jobManagementDAO.deleteActiveJob(activeJob);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void deleteActiveJob(Long activeJobId) {
        jobManagementDAO.deleteActiveJob(activeJobId);
    }
}
