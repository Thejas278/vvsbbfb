package com.fdt.common.service;

import java.util.List;

import com.fdt.common.entity.ActiveJob;

public interface JobManagementService {

    public List<ActiveJob> getActiveJobs(String jobName);

    public Long createActiveJob(String jobName);

    public void deleteActiveJob(ActiveJob activeJob);

    public void deleteActiveJob(Long activeJobId);
}