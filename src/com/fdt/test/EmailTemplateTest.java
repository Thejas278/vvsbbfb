package com.fdt.test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fdt.email.EmailProducer;
import com.fdt.security.dao.UserDAO;
import com.fdt.security.entity.User;
import com.fdt.subscriptions.dao.SubDAO;
import com.fdt.subscriptions.dto.SubscriptionDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:WebContent/WEB-INF/conf/spring/applicationContext.xml" })
public class EmailTemplateTest {

    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private SubDAO subDAO;
    
    @Autowired
    private EmailProducer emailProducer;

    public EmailTemplateTest() throws URISyntaxException {
        Path path = Paths.get(getClass().getResource("").toURI());
        while (!path.toString().endsWith("WEB-INF")) {
            path = path.getParent();
        }
        System.setProperty("CONFIG_LOCATION", "file:" + path.resolve("conf"));
    }

    @Test
    @Transactional
    public void testSendOverriddenSubWarning() {

        String fromEmailAddress = "SUPPORTRMS@granicus.com";
        String templateFolder = "RecordsManagement/arlington/";
        String template = "overriddenSubWarning.stl";
        String subject = "Expiration Warning - Roam Arlington Records Management";
        String emailId = "jon.miller@granicus.com";

        /*
        User user = userDAO.getUser(emailId);
        List<SubscriptionDTO> subscriptions = subDAO.getUserSubs(emailId, null, null, true, false);
        */

        User user = new User();
        user.setFirstName("Jon");
        user.setLastName("Miller");

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setSiteName("ARLINGTON");
        dto.setSubscription("Arlington Image Access");

        String subscriptionSite = "ARLINGTON";

        Map<String, Object> emailData = new HashMap<>();
        emailData.put("user", new User());
        emailData.put("currentDate", new Date());
        emailData.put("subscriptionSite", subscriptionSite);
        emailData.put("subscriptions", Arrays.asList(dto));

        emailProducer.sendMailUsingTemplate(fromEmailAddress, emailId, subject, templateFolder
                + template, emailData);
    }
}