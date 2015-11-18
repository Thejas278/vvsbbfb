package com.fdt.common.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fdt.common.entity.ActiveJob;

@Repository
public class JobManagementDAOImpl extends AbstractBaseDAOImpl implements JobManagementDAO {

    @Override
    public List<ActiveJob> getActiveJobs(String jobName) {
        Criteria criteria = currentSession().createCriteria(ActiveJob.class)
                .add(Restrictions.eq("jobName", jobName));
        @SuppressWarnings("unchecked")
        List<ActiveJob> activeJobs = criteria.list();
        return activeJobs;
    }

    @Override
    public Long createActiveJob(String jobName, String hostName) {
        ActiveJob activeJob = new ActiveJob();
        activeJob.setJobName(jobName);
        activeJob.setHostName(hostName);
        activeJob.setActive(true);
        activeJob.setCreatedBy("SYSTEM");
        activeJob.setModifiedBy("SYSTEM");
        activeJob.setCreatedDate(new Date());
        activeJob.setModifiedDate(new Date());
        currentSession().save(activeJob);
        currentSession().flush();
        return activeJob.getId();
    }

    @Override
    public void deleteActiveJob(ActiveJob activeJob) {
        currentSession().delete(activeJob);
        currentSession().flush();
    }

    @Override
    public void deleteActiveJob(Long activeJobId) {
        deleteById(ActiveJob.class, activeJobId);
        currentSession().flush();
    }

}