package com.fdt.paymentgateway.service;

import com.fdt.common.util.SystemUtil;

import paypal.payflow.*;

// This class uses the Payflow SDK Data Objects to do a simple reference Credit transaction.
// The request is sent as a Data Object and the response received is also a Data Object.

public class JeffersonReferenceCredit {
    public JeffersonReferenceCredit() {
    }

    public static void main(String args[]) {
        System.out.println("------------------------------------------------------");
        System.out.println("Executing Sample from File: DOReferenceCredit.java");
        System.out.println("------------------------------------------------------");

        // Payflow Pro Host Name. This is the host name for the PayPal Payment Gateway.
        // For testing: 	pilot-payflowpro.paypal.com
        // For production:  payflowpro.paypal.com
        // DO NOT use payflow.verisign.com or test-payflow.verisign.com!
        SDKProperties.setHostAddress("payflowpro.paypal.com");
        SDKProperties.setHostPort(443);
        SDKProperties.setTimeOut(45);

        // Logging is by default off. To turn logging on uncomment the following lines:
        //SDKProperties.setLogFileName("payflow_java.log");
        //SDKProperties.setLoggingLevel(PayflowConstants.SEVERITY_DEBUG);
        //SDKProperties.setMaxLogFileSize(100000);

        // Uncomment the lines below and set the proxy address and port, if a proxy has to be used.
        //SDKProperties.setProxyAddress("");
        //SDKProperties.setProxyPort(0);

        // Create the Data Objects.
        // Create the User data object with the required user details.
        //UserInfo user = new UserInfo("<user>", "<vendor>", "<partner>", "<password>");

        UserInfo user = new UserInfo("EcomJeffersonMicroAPI", "EcomJeffersonMicro", "paypal", SystemUtil.decrypt("D0723E4B4E8AEAFC7D48A0D4E22F82B7F69DCD96AD053B11E59815436B3F8249AAAA45A3885EBF1B43627EC3755B98BC", "PBEWITHSHA256AND256BITAES-CBC-BC", "Nk0BusFu9aLE4qb"));
        //UserInfo user = new UserInfo("EcomJeffersonAPI", "EcomJefferson", "paypal", SystemUtil.decrypt("5C51F73303F157D2334FEDAE8A28595361D67BE11623937D080AC007E2E10960E643ECCCA7B3180CDCB009BACECE7EC9", "PBEWITHSHA256AND256BITAES-CBC-BC", "Nk0BusFu9aLE4qb"));
        //UserInfo user = new UserInfo("EcomDallasMicroAPI", "EcomDallasMicro", "paypal", SystemUtil.decrypt("5D3A55BC81C271F65F9D8D85E7B2EAB020E7BD2452D632580993A4E9B495812C0BAB6B16618CA882FBC9158A622C3EEE", "PBEWITHSHA256AND256BITAES-CBC-BC", "Nk0BusFu9aLE4qb"));
        // Create the Payflow Connection data object with the required connection details.
        
        
        
        
        
        
        PayflowConnectionData connection = new PayflowConnectionData();
        ///////////////////////////////////////////////////////////////////

        // If you want to change the amount being credited, you'll need to set the Amount object.
        //Invoice inv = new Invoice();
        // Set the amount object if you want to change the amount from the original transaction.
        // Currency Code USD is US ISO currency code.  If no code passed, USD is default.
        // See the Developer's Guide for the list of three-character currency codes available.
        //Currency amt = new Currency(new Double(5.00));
        //inv.setAmt(amt);
        //CreditTransaction trans = new CreditTransaction("<ORIG_TRANS_ID>", user, connection, inv, PayflowUtility.getRequestId());

        // Create a new Credit Transaction from the original transaction.  See above if you
        // need to change the amount.
        String saleTransactionReferenceNumbers[] = {
        		//"BQ0PC617DD8D"
        		"BS0PD57EBC7A"
 




};
        for(String txRefNumber: saleTransactionReferenceNumbers) {
	        CreditTransaction trans = new CreditTransaction(txRefNumber, user, connection, PayflowUtility.getRequestId());
	
	        // Submit the Transaction
	        Response resp = trans.submitTransaction();
	
	        // Display the transaction response parameters.
	        if (resp != null) {
	            // Get the Transaction Response parameters.
	            TransactionResponse trxnResponse = resp.getTransactionResponse();
	
	            // Create a new Client Information data object.
	            ClientInfo clInfo = new ClientInfo();
	
	            // Set the ClientInfo object of the transaction object.
	            trans.setClientInfo(clInfo);
	
	            if (trxnResponse != null) {
	                System.out.println(txRefNumber + ", "+  trxnResponse.getRespMsg() + ", "+ trxnResponse.getPnref());	                
	            }           
	            
	        }
        }

    }
}
