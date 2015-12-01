package com.fdt.common.dao;

import java.util.List;

import com.fdt.common.entity.ActiveJob;

public interface JobManagementDAO {

    public List<ActiveJob> getActiveJobs(String jobName);

    public Long createActiveJob(String jobName, String hostName);

    public void deleteActiveJob(ActiveJob activeJob);

    public void deleteActiveJob(Long activeJobId);

}
