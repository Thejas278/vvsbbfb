package com.fdt.test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fdt.recurtx.dao.RecurTxDAO;
import com.fdt.recurtx.dto.OverriddenSubscriptionDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:WebContent/WEB-INF/conf/spring/applicationContext.xml" })
public class RecurTxDAOTest {

    @Autowired
    private RecurTxDAO recurSubDAO;

    public RecurTxDAOTest() throws URISyntaxException {
        Path path = Paths.get(getClass().getResource("").toURI());
        while (!path.toString().endsWith("WEB-INF")) {
            path = path.getParent();
        }
        System.setProperty("CONFIG_LOCATION", "file:" + path.resolve("conf"));
    }


    @Test @Ignore
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void testGetRecurringProfilesForVerification() {
        recurSubDAO.getRecurringProfilesForVerification();
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void testGetExpiringOverriddenSubscriptions() {
        List<OverriddenSubscriptionDTO> results = recurSubDAO.getExpiringOverriddenSubscriptions();
        for (OverriddenSubscriptionDTO result : results) {
            System.out.println(result.toString());
        }
        
    }
    
    @Test
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void testUpdateIsOverriddenSubscriptionWarningSent() {

        Long userAccessId = 556400L;
        boolean isWarningSent = true;

        recurSubDAO.updateIsOverriddenSubscriptionWarningSent(userAccessId, isWarningSent);
    }
}