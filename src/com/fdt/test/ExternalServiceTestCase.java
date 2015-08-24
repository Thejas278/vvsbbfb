package com.fdt.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fdt.ecom.service.ExternalServiceTransactionInfo;
import com.fdt.otctx.entity.OTCTx;
import com.fdt.payasugotx.entity.PayAsUGoTx;
import com.fdt.payasugotx.service.PayAsUGoTxService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:C:\\Users\\valampally\\git\\eAccept-eCom\\WebContent\\WEB-INF\\conf\\spring\\applicationContext.xml" })
public class ExternalServiceTestCase {

	@Autowired
	@Qualifier("externalServiceTransactionInfo")
	private ExternalServiceTransactionInfo externalServiceTransactionInfo = null;
	
	 @Autowired
	 @Qualifier("PayAsUGoTxService")
	 private PayAsUGoTxService payAsUGoSubService = null;

	

	public ExternalServiceTestCase() {
		System.setProperty("CONFIG_LOCATION",
				"file:C:\\Users\\valampally\\git\\eAccept-eCom\\src\\com\\fdt\\test\\conf");
	}

	@Test
	public void lookupTx() {
		/*OTCTx otcTx =this.externalServiceTransactionInfo.getOTCTransactionByTxRefNum("ETHPB380D5C0", "MOBILE");
		System.out.println(otcTx.getSite().getMicroMerchant());
		System.out.println(otcTx.getSite().getMerchant());*/
		
		
		PayAsUGoTx payAsUGoTx =this.payAsUGoSubService.getPayAsUGoTxByTxRefNum("EQCPA22A35CD", "JEFFERSON");
		System.out.println(payAsUGoTx.getSite().getMicroMerchant());
		System.out.println(payAsUGoTx.getSite().getMerchant());
		

	}

}