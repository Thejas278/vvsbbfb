package com.fdt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fdt.common.dao.JobManagementDAO;
import com.fdt.common.entity.ActiveJob;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:WebContent/WEB-INF/conf/spring/applicationContext.xml" })
public class JobManagementDAOTest {

    @Autowired
    private JobManagementDAO jobManagementDAO;

    public JobManagementDAOTest() throws URISyntaxException {
        Path path = Paths.get(getClass().getResource("").toURI());
        while (!path.toString().endsWith("WEB-INF")) {
            path = path.getParent();
        }
        System.setProperty("CONFIG_LOCATION", "file:" + path.resolve("conf"));
    }

    @Test
    @Transactional
    public void testCreateActiveJob() {
        Long id = jobManagementDAO.createActiveJob("testJobName", "testHostName");
        assertNotNull(id);
        List<ActiveJob> activeJobs = jobManagementDAO.getActiveJobs("testJobName");
        assertEquals(1, activeJobs.size());
    }

    @Test
    @Transactional
    public void testDeleteActiveJob() {
        Long id = jobManagementDAO.createActiveJob("testJobName", "testHostName");
        assertNotNull(id);
        List<ActiveJob> activeJobs = jobManagementDAO.getActiveJobs("testJobName");
        assertEquals(1, activeJobs.size());
        jobManagementDAO.deleteActiveJob(activeJobs.get(0));
        activeJobs = jobManagementDAO.getActiveJobs("testJobName");
        assertEquals(0, activeJobs.size());
    }

}