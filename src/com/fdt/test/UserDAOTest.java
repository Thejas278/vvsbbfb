package com.fdt.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fdt.security.dao.UserDAO;
import com.fdt.security.entity.User;
import com.fdt.security.entity.UserAccess;
import com.fdt.security.entity.UserEvent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:WebContent/WEB-INF/conf/spring/applicationContext.xml" })
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    public UserDAOTest() throws URISyntaxException {
        Path path = Paths.get(getClass().getResource("").toURI());
        while (!path.toString().endsWith("WEB-INF")) {
            path = path.getParent();
        }
        System.setProperty("CONFIG_LOCATION", "file:" + path.resolve("conf"));
    }

    @Test
    @Transactional
    public void testGetUserAccessForFirmLevelUsers() {
        Long adminUserId = 15251L;
        Long accessId = 54L;
        List<UserAccess> list = userDAO.getUserAccessForFirmLevelUsers(adminUserId, accessId);
        assertFalse(list.isEmpty());
    }

    @Test
    @Transactional
    public void testGetUserDetails() {
        String userName = "admin@roam.comTMP1";
        User user = userDAO.getUser(userName);
        assertNotNull(user);
    }

    @Test
    @Transactional
    public void testUserEventDeleteByCriteria() {

        String userName = "admin@roam.comTMP1";
        User user = userDAO.getUser(userName);

        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setCreatedBy("test");
        userEvent.setCreatedDate(new Date());
        userEvent.setModifiedBy("test");
        userEvent.setModifiedDate(new Date());
        userDAO.saveUserEvent(userEvent);

        String token = userEvent.getToken();

        UserEvent newUserEvent = userDAO.findUserEvent(userName, token);
        assertNotNull(newUserEvent);

        userDAO.deleteUserEvents(userName, token);

        newUserEvent = userDAO.findUserEvent(userName, token);
        assertNull(newUserEvent);
    }
}