package com.fdt.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ACTIVE_JOB")
public class ActiveJob extends AbstractBaseEntity {

    private static final long serialVersionUID = 2857445878014109912L;

    @Column(name = "JOB_NAME", nullable = false)
    private String jobName;

    @Column(name = "HOST_NAME")
    private String hostName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        return "ActiveJob [jobName=" + jobName + ", hostName=" + hostName + ", id=" + id + ", createdDate="
                + createdDate + ", modifiedDate=" + modifiedDate + ", modifiedBy=" + modifiedBy
                + ", createdBy=" + createdBy + ", active=" + active + "]";
    }
}