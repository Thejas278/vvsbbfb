package com.fdt.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fdt.ecom.service.ExternalServiceTransactionInfo;
import com.fdt.otctx.entity.OTCTx;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:C:\\Users\\valampally\\git\\eAccept-eCom\\WebContent\\WEB-INF\\conf\\spring\\applicationContext.xml" })
public class ExternalServiceTestCase {

	@Autowired
	@Qualifier("externalServiceTransactionInfo")
	private ExternalServiceTransactionInfo externalServiceTransactionInfo = null;

	

	public ExternalServiceTestCase() {
		System.setProperty("CONFIG_LOCATION",
				"file:C:\\Users\\valampally\\git\\eAccept-eCom\\src\\com\\fdt\\test\\conf");
	}

	@Test
	public void lookupTx() {
		OTCTx otcResponseDTO =this.externalServiceTransactionInfo.getOTCTransactionByTxRefNum("BU0PD2A3C28C", "ARLINGTON");
		System.out.println(otcResponseDTO);
		

	}

}