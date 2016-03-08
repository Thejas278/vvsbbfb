package com.fdt.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fdt.common.dao.JobManagementDAO;
import com.fdt.ecom.entity.CreditCard;
import com.fdt.security.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:WebContent/WEB-INF/conf/spring/applicationContext.xml" })
public class UserServiceTestCase {

	@Autowired
    private UserService userService = null;
	
	public UserServiceTestCase() {
        System.setProperty("CONFIG_LOCATION", "file:C:\\Users\\valampally\\git\\eAccept-eCom\\WebContent\\WEB-INF\\conf");
    }
	
	  /*@Test
	    public void getCreditCardDetailsByUserName() {
	    	String userName = "valampally@granicus.com";
	    	List<CreditCard> creditCardList = userService.getCreditCardDetails(userName);
	    	for (CreditCard creditCard : creditCardList) {
				System.out.println(creditCard);
			}
	   }*/
	  
	  @Test
	    public void getCreditCardDetailsByUserId() {
	    	Long userId = 284339l;
	    	List<CreditCard> creditCardList = userService.getCreditCardDetails(userId);
	    	for (CreditCard creditCard : creditCardList) {
				System.out.println(creditCard);
			}
	   }
}