package com.fdt.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fdt.ecom.dao.EComDAO;
import com.fdt.ecom.entity.Site;
import com.fdt.ecom.entity.SiteConfiguration;
import com.fdt.email.EMailUtil;
import com.fdt.email.EmailProducer;
import com.fdt.security.dao.UserDAO;
import com.fdt.security.entity.User;
import com.fdt.security.service.admin.UserAdminService;
import com.fdt.subscriptions.dao.SubDAO;
import com.fdt.subscriptions.dto.SubscriptionDTO;

import freemarker.template.TemplateException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:WebContent/WEB-INF/conf/spring/applicationContext.xml" })
public class EmailTemplateTest {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SubDAO subDAO;

    @Autowired
    private EComDAO eComDAO;

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private EmailProducer emailProducer;

    @Autowired
    private EMailUtil eMailUtil;

    @Value("${ecommerce.serverurl}")
    private String ecomServerURL = null;

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
        String emailId = "jon.miller@granicus.com";

        User user = userDAO.getUser(emailId);
        List<SubscriptionDTO> subscriptions = subDAO.getUserSubs(emailId, null, null, true, false);

        List<Site> sites = eComDAO.getSites();
        for (Site site : sites) {

            Long siteId = site.getId();
            String subscriptionSite = site.getDescription();

            SiteConfiguration siteConfig = eComDAO.getSiteConfiguration(siteId);

            if (siteConfig != null) {
                String templateFolder = siteConfig.getEmailTemplateFolder();
                String subject = siteConfig.getOverriddenSubscriptionWarningSub();
                String template = siteConfig.getOverriddenSubscriptionWarningTemplate();
    
                Map<String, Object> emailData = new HashMap<>();
                emailData.put("user", user);
                emailData.put("currentDate", new Date());
                emailData.put("expireDate", new Date());
                emailData.put("subscriptionSite", subscriptionSite);
                emailData.put("subscriptions", subscriptions);
                emailData.put("fromEmailAddress", fromEmailAddress);
                emailData.put("serverUrl", this.ecomServerURL);
    
                try {
                    eMailUtil.sendMailUsingTemplate(fromEmailAddress, emailId, subject,
                            templateFolder + template, emailData);
                } catch (MessagingException | IOException | TemplateException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}