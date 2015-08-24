package com.fdt.paymentgateway.service;

import com.fdt.common.util.SystemUtil;

import paypal.payflow.*;

// This class uses the Payflow SDK Data Objects to do an Inquiry transaction.
//
// You perform an inquiry using a reference to an original transaction—either the PNREF
// value returned for the original transaction or the CUSTREF value that you specified for the original
// transaction.
//
// While the amount of information returned in an Inquiry transaction depends upon the VERBOSITY setting,
// Inquiry responses mimic the verbosity level of the original transaction as much as possible.
//
// Transaction results (especially values for declines and error conditions) returned by each PayPal-supported
// processor vary in detail level and in format. The Payflow Pro Verbosity parameter enables you to control
// the kind and level of information you want returned.  By default, Verbosity is set to LOW.
// A LOW setting causes PayPal to normalize the transaction result values. Normalizing the values limits
// them to a standardized set of values and simplifies the process of integrating Payflow Pro.
// By setting Verbosity to MEDIUM, you can view the processor?s raw response values. This setting is more
// "verbose" than the LOW setting in that it returns more detailed, processor-specific information.
// The request is sent as a Data Object and the response received is also a Data Object.

public class DOInquiry {
    public DOInquiry() {
    }

    public static void main(String args[]) {
        System.out.println("------------------------------------------------------");
        System.out.println("Executing Sample from File: DOInquiry.java");
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
        UserInfo user = new UserInfo("EcomJeffersonMicroAPI", "EcomJeffersonMicro", "paypal", SystemUtil.decrypt("D0723E4B4E8AEAFC7D48A0D4E22F82B7F69DCD96AD053B11E59815436B3F8249AAAA45A3885EBF1B43627EC3755B98BC", "PBEWITHSHA256AND256BITAES-CBC-BC", "Nk0BusFu9aLE4qb"));
        //UserInfo user = new UserInfo("EcomSmithMicroAPI", "EcomSmithMicro", "paypal", SystemUtil.decrypt("7194EAB05B5D695A327C8DA8A63460884ABE3E40EA5A5628482014CDA59352EA3D8BCAD6CCEE5E2A94B98676F2C2AB9E", "PBEWITHSHA256AND256BITAES-CBC-BC", "Nk0BusFu9aLE4qb"));
        //UserInfo user = new UserInfo("EcomDallasAPI", "EcomDallas", "paypal", SystemUtil.decrypt("039E2C36C8D29FA4A35A9627A6222F896D9799EF6291EDB9AFC0686A17AEA5FA3E1C001CB80B068C914983B15E98C077", "PBEWITHSHA256AND256BITAES-CBC-BC", "Nk0BusFu9aLE4qb"));
       
        //UserInfo user = new UserInfo("EcomRockwallMicroAPI", "EcomRockwallMicro", "paypal", SystemUtil.decrypt("F9F6AB5E00FBADF746C571D6E5D6DF09FCA3A4D1BDA5C2BA6122BAEE8DF6FBBF8430639A5B684A1675FED589BD2DA1F6", "PBEWITHSHA256AND256BITAES-CBC-BC", "Nk0BusFu9aLE4qb"));
        //UserInfo user = new UserInfo("EcomRockwallAPI", "EcomRockwall", "paypal", SystemUtil.decrypt("732E5A5E6DF403275C756E5311D36293302B92CD6ADCD9BD5DF3295F988369BDB3954812FAEB26EBDC9C9914D2965A0B", "PBEWITHSHA256AND256BITAES-CBC-BC", "Nk0BusFu9aLE4qb"));
        
        
        // Create the Payflow Connection data object with the required connection details.
        PayflowConnectionData connection = new PayflowConnectionData();

        // Create a new Inquiry Transaction.
        // Replace <PNREF> with a previous transaction ID that you processed on your account.
        InquiryTransaction trans = new InquiryTransaction("BS0PD57EBC7A", user, connection, PayflowUtility.getRequestId());

        // To use CUSTREF instead of PNREF you need to set the CustRef and include the INVOICE object in your
        // request.  Since you will be using CUSTREF instead of PNREF, PNREF will be "" (null).
        // Create a new Invoice data object with the Amount, Billing Address etc. details.
        //Invoice inv = new Invoice();
        //inv.setCustRef("CUSTREF1");
        //InquiryTransaction trans = new InquiryTransaction("", user, connection, inv, PayflowUtility.getRequestId());

        // Refer to the Payflow Pro Developer's Guide for more information regarding the parameters returned
        // when VERBOSITY is set.
        trans.setVerbosity("MEDIUM"); // Change VERBOSITY to MEDIUM to display additional information.

        // Submit the Transaction
        Response resp = trans.submitTransaction();

        // Display the transaction response parameters.
        if (resp != null) {
            // Get the Transaction Response parameters.
            TransactionResponse trxnResponse = resp.getTransactionResponse();
            System.out.println(resp.getResponseString());
           

            // Create a new Client Information data object.
            ClientInfo clInfo = new ClientInfo();

            // Set the ClientInfo object of the transaction object.
            trans.setClientInfo(clInfo);

            if (trxnResponse != null) {
                System.out.println("RESULT = " + trxnResponse.getResult());
                System.out.println("PNREF = " + trxnResponse.getPnref());
                System.out.println("--------------------------------------------");
                System.out.println("Original Response Data");
                System.out.println("--------------------------------------------");
                if ("LOW".equals(trans.getVerbosity().toUpperCase())) {
                	
                    System.out.println("RESULT = " + trxnResponse.getOrigResult());
                    System.out.println("PNREF = " + trxnResponse.getOrigPnref());
                    System.out.println("RESPMSG = " + trxnResponse.getRespMsg());
                    System.out.println("AUTHCODE = " + trxnResponse.getAuthCode());
                    System.out.println("CVV2MATCH = " + trxnResponse.getCvv2Match());
                    System.out.println("AVSADDR = " + trxnResponse.getAvsAddr());
                    System.out.println("AVSZIP = " + trxnResponse.getAvsZip());
                    System.out.println("IAVS = " + trxnResponse.getIavs());
                    System.out.println("CARDSECURE = " + trxnResponse.getProcCardSecure());
                } else if ("MEDIUM".equals(trans.getVerbosity().toUpperCase())) {
                	 //System.out.println("Amount = " + trxnResponse.get);
                    System.out.println("RESULT = " + trxnResponse.getOrigResult());
                    System.out.println("PNREF = " + trxnResponse.getOrigPnref());
                    System.out.println("RESPMSG = " + trxnResponse.getRespMsg());
                    System.out.println("AUTHCODE = " + trxnResponse.getAuthCode());
                    System.out.println("CVV2MATCH = " + trxnResponse.getCvv2Match());
                    System.out.println("AVSADDR = " + trxnResponse.getAvsAddr());
                    System.out.println("AVSZIP = " + trxnResponse.getAvsZip());
                    System.out.println("IAVS = " + trxnResponse.getIavs());
                    System.out.println("CARDSECURE = " + trxnResponse.getProcCardSecure());
                    System.out.println("HOSTCODE = " + trxnResponse.getHostCode());
                    System.out.println("RESPTEXT = " + trxnResponse.getRespText());
                    System.out.println("PROCAVS = " + trxnResponse.getProcAvs());
                    System.out.println("PROCCVV2 = " + trxnResponse.getProcCVV2());
                    System.out.println("ADDLMSGS = " + trxnResponse.getAddlMsgs());
                    System.out.println("TRANSSTATE = " + trxnResponse.getTransState());
                    System.out.println("DATE_TO_SETTLE = " + trxnResponse.getDateToSettle());
                    System.out.println("BATCHID = " + trxnResponse.getBatchId());
                    System.out.println("SETTLE_DATE = " + trxnResponse.getSettleDate());
                }
            }

            // Display the response.
            System.out.println("\n" + PayflowUtility.getStatus(resp));

            // Get the Transaction Context and check for any contained SDK specific errors (optional code).
            Context transCtx = resp.getContext();
            if (transCtx != null && transCtx.getErrorCount() > 0) {
                System.out.println("\nTransaction Errors = " + transCtx.toString());
            }
        }
    }
}
