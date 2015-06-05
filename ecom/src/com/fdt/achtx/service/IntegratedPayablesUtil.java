package com.fdt.achtx.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fdt.achtx.dto.ACHTxDTO;
import com.fdt.achtx.dto.integratedpayables.BankInfo;
import com.fdt.achtx.dto.integratedpayables.BankServiceRequest;
import com.fdt.achtx.dto.integratedpayables.CurrencyAmount;
import com.fdt.achtx.dto.integratedpayables.CustomerId;
import com.fdt.achtx.dto.integratedpayables.DepAcctIdFrom;
import com.fdt.achtx.dto.integratedpayables.DepAcctIdTo;
import com.fdt.achtx.dto.integratedpayables.IntegratedPayableXML;
import com.fdt.achtx.dto.integratedpayables.PaymentInstruction;
import com.fdt.achtx.dto.integratedpayables.PostalAddress;
import com.fdt.achtx.dto.integratedpayables.Status;
import com.fdt.achtx.dto.integratedpayables.TransferAddRequest;
import com.fdt.achtx.dto.integratedpayables.TransferAddResponse;
import com.fdt.achtx.dto.integratedpayables.TransferInfo;
import com.fdt.achtx.dto.sftp.SftpDTO;
import com.fdt.common.util.SystemUtil;
import com.fdt.ecom.entity.enums.PaymentType;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class IntegratedPayablesUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(IntegratedPayablesUtil.class);
	
	public static String  generateXMLRequest(ACHTxDTO achTxDTO, String achMode) throws IOException {
		XStream stream = new XStream(new DomDriver("UTF-8"));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
		stream.processAnnotations(BankInfo.class);
		stream.processAnnotations(BankServiceRequest.class);
		stream.processAnnotations(CurrencyAmount.class);
		stream.processAnnotations(CustomerId.class);
		stream.processAnnotations(DepAcctIdFrom.class);
		stream.processAnnotations(DepAcctIdTo.class);
		stream.processAnnotations(IntegratedPayableXML.class);
		stream.processAnnotations(PaymentInstruction.class);
		stream.processAnnotations(PostalAddress.class);
		stream.processAnnotations(IntegratedPayablesUtil.class);
		stream.processAnnotations(TransferAddRequest.class);
		stream.processAnnotations(TransferInfo.class);
		
		IntegratedPayableXML msg = new IntegratedPayableXML();				
		BankServiceRequest bankServiceRequest = new BankServiceRequest();
		bankServiceRequest.setRequestId(getRequestIdForBankSvcRq());
		
		CustomerId customerId = new CustomerId();
		customerId.setSpName(achTxDTO.getSpName());
		customerId.setCustomerPermId(achTxDTO.getCustomerPermId());
		
		TransferAddRequest transferAddRequest = new TransferAddRequest();		
		transferAddRequest.setRequestId(getRequestIdForTransferAddRequest());
		achTxDTO.setPaymentReferenceId(getPaymentReferenceId(achTxDTO.getPaymentType(), achTxDTO.getSiteId(), achMode));
		transferAddRequest.setPaymentReferenceId(achTxDTO.getPaymentReferenceId());
		transferAddRequest.setCustomerId(customerId);
		bankServiceRequest.setTransferAddRequest(transferAddRequest);
		
		TransferInfo transferInfo = new TransferInfo();		
		DepAcctIdFrom depAcctIdFrom = new DepAcctIdFrom();		
		depAcctIdFrom.setAccountId(achTxDTO.getFromAcctNumber());
		depAcctIdFrom.setAccountType(achTxDTO.getFromAccountType());
		depAcctIdFrom.setName(achTxDTO.getFromAcctName());
		BankInfo bankInfoFrom = new BankInfo();
		bankInfoFrom.setBankIdType(achTxDTO.getFromBankIdType());
		bankInfoFrom.setBankId("MNBDUS33");
		depAcctIdFrom.setBankInfo(bankInfoFrom);
		DepAcctIdTo depAcctIdTo = new DepAcctIdTo();		
		depAcctIdTo.setAccountId(achTxDTO.getToAcctNumber());
		depAcctIdTo.setAccountType(achTxDTO.getToAccountType());
		depAcctIdTo.setName(StringUtils.substring(achTxDTO.getSiteName(),0,32));
		BankInfo bankInfoTo = new BankInfo();
		bankInfoTo.setBankIdType(achTxDTO.getToBankIdType());
		bankInfoTo.setBankId(achTxDTO.getToAcctRoutingNo());
		bankInfoTo.setName(StringUtils.substring(achTxDTO.getToAcctName(),0,32));		
		PostalAddress postalAddress = new PostalAddress();
		postalAddress.setAddressLine1(StringUtils.substring(achTxDTO.getToAddressLine1()
				.concat(" ").concat(achTxDTO.getToAddressLine2()), 0, 32));
		postalAddress.setAddressLine2(StringUtils.substring(achTxDTO.getToCity()
				.concat(" ").concat(achTxDTO.getToState().concat(" ").concat(achTxDTO.getToZip())), 0, 32));
		postalAddress.setCountry("USA");
		bankInfoTo.setPostalAddress(postalAddress);
		depAcctIdTo.setBankInfo(bankInfoTo);
		CurrencyAmount currencyAmount = new CurrencyAmount();
		currencyAmount.setAmount(achTxDTO.getTxAmount());
		currencyAmount.setCurrencyCode("USD");		
		PaymentInstruction paymentInstruction = new PaymentInstruction();
		paymentInstruction.setPaymentFormat("CCD");
		paymentInstruction.setCompanyEntryDescription("Granicus");
		paymentInstruction.setTransactionCode("22");
		String description = "ACH For Site: " + achTxDTO.getSiteName() +
				", Payment Type:" +  achTxDTO.getPaymentType();
		paymentInstruction.setDescription(StringUtils.substring(description, 0, 75));		
		transferInfo.setDepAcctIdFrom(depAcctIdFrom);
		transferInfo.setDepAcctIdTo(depAcctIdTo);
		transferInfo.setCurrencyAmount(currencyAmount);
		transferInfo.setDueDate(getNextBusinessDay());
		transferInfo.setCategory("ACH Credit");
		transferInfo.setPaymentInstruction(paymentInstruction);		
		transferAddRequest.setTransferInfo(transferInfo);		
		msg.setBankServiceRequest(bankServiceRequest);		
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
		stream.toXML(msg, writer);
		String xml = outputStream.toString("UTF-8");
		return xml;
	}
	
	public static String generateFileName(ACHTxDTO achDTO, String achMode) {
		String fileName = null;
		if(!StringUtils.isBlank(achMode) && achMode.equalsIgnoreCase("P")) {
			fileName = "P";
		} else {
			fileName = "C";
		}
		fileName = fileName.concat("CBGI.").concat(achDTO.getCustomerPermId()).concat(".")
				.concat(achDTO.getPaymentReferenceId()).concat(".XML");		
		return fileName;
	}
	
	public static void sftpFile (ACHTxDTO achTxDTO) throws Exception {
		String SFTPWORKINGDIR = achTxDTO.getToWorkingDirectory();
        String content = achTxDTO.getXmlContent();
        String fileName = achTxDTO.getFileName();
        SftpDTO sftpDTO = null;
        try {
        	sftpDTO = getChannelToSftpServer(achTxDTO);
        	ChannelSftp channelSftp = sftpDTO.getChannelSftp();
            channelSftp.cd(SFTPWORKINGDIR);
            InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            channelSftp.put(stream, fileName);
            logger.info("File SFTPed successfully to SFTP Server");
        } catch (Exception exception) {
        	logger.error("Exception found while SFTPing File: {} to SFTP server" + exception, fileName); 
        	throw exception;
        }
        finally{
        	if(sftpDTO != null) {
        		disconnectChanneltoSftpServer(sftpDTO);
        	}
        }
    }
	
	public static void retrieveXMLResponseFiles(ACHTxDTO achTxDTO) throws Exception {
		String SFTPWORKINGDIR = achTxDTO.getFromWorkingDirectory();       
        String fileName = null;
        String localDir = achTxDTO.getLocalDir();
        SftpDTO sftpDTO = null;
        try {
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            BufferedOutputStream bos = null;            
            sftpDTO = getChannelToSftpServer(achTxDTO);
            ChannelSftp channelSftp = sftpDTO.getChannelSftp();
            Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(SFTPWORKINGDIR);
            channelSftp.cd(SFTPWORKINGDIR);
            //channelSftp.cd(SFTPWORKINGDIR);
            if (entries != null) {
				for (ChannelSftp.LsEntry lsEntry : entries) {
					if (lsEntry.getAttrs().isDir()
							&& (lsEntry.getFilename().contains("REJECTS") || lsEntry
									.getFilename().contains("CONFIRMS"))) {
						Vector<ChannelSftp.LsEntry> entries2 = channelSftp
								.ls(lsEntry.getFilename());
						channelSftp.cd(lsEntry.getFilename());
						if (entries2 != null) {
							for (ChannelSftp.LsEntry lsEntry2 : entries2) {
								if (!lsEntry2.getAttrs().isDir()) {

									fileName = lsEntry2.getFilename();
									logger.info(
											"Downloading File: {} From SFTP",
											fileName);
									bis = new BufferedInputStream(
											channelSftp.get(fileName));
									File newFile = new File(localDir + fileName);
									os = new FileOutputStream(newFile);
									bos = new BufferedOutputStream(os);
									int count;
									while ((count = bis.read(buffer)) > 0) {
										bos.write(buffer, 0, count);
									}
									bos.flush();
									bos.close();
								}
							}
						}
						channelSftp.cd("..");
					}
				
				}
			}
            logger.info("File(s) Downloaded Successfully from SFTP server to {}", localDir);
        } catch (Exception exception) {
        	logger.error("Exception found while Downloading File(s): {} From SFTP" + exception, fileName);
        	throw exception;
        }
        finally{
        	if(sftpDTO != null) {
        		disconnectChanneltoSftpServer(sftpDTO);
        	}
        }
    }
	
	public static IntegratedPayableXML unMarshallToIntegratedPayableXML(String xmlContent) throws IOException {		
			XStream stream = new XStream();		
			stream.processAnnotations(BankServiceRequest.class);
			stream.processAnnotations(IntegratedPayableXML.class);
			stream.processAnnotations(TransferAddResponse.class);
			stream.processAnnotations(Status.class);
			IntegratedPayableXML resp = (IntegratedPayableXML)stream.fromXML(xmlContent);
			return resp;			
		}

	public static void deleteAllFiles(String directory){
		try {
			FileUtils.cleanDirectory(new File(directory));
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	
	
	public static ACHTxDTO getAchInfoFromPaymentReferenceId(String paymentReferenceId){
		ACHTxDTO achTxDTO = null;
		if(!StringUtils.isBlank(paymentReferenceId) && paymentReferenceId.length() == 16) {
			achTxDTO = new ACHTxDTO();
			PaymentType paymentType = null;
			String siteIdString = paymentReferenceId.substring(13,15);
			String paymentTypeString = paymentReferenceId.substring(15);			
			if(paymentTypeString.equals("1")){
				paymentType = PaymentType.RECURRING;
			} else if(paymentTypeString.equals("2")) {
				paymentType = PaymentType.PAYASUGO;
			} else if(paymentTypeString.equals("3")) {
				paymentType = PaymentType.OTC;
			} else if(paymentTypeString.equals("4")) {
				paymentType = PaymentType.WEB;
			}
			achTxDTO.setPaymentType(paymentType);
			achTxDTO.setSiteId(Long.valueOf(siteIdString));			
		}
		return achTxDTO;
		
	}

	public static String getFileContent(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static File[] getListOfFiles(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File a, File b) {
                return (a.getName().compareTo(b.getName()));
            }});
        return files;
    }
	
	public static Date getStatusDate(String asOfDate, String asOfTime) throws ParseException {
		Date date = new Date();
		String dateString = asOfDate.concat(" ").concat(asOfTime);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		try {
	    	date = df.parse(dateString);		      
	    } catch (ParseException e) {
	    	 logger.error("Wrong format for asOfDate: {}, asOfTime: {} Exception: {}", asOfDate, asOfTime, e.getMessage());
	    	 throw e;
	    }
		return date;
	}
	
	private static String getRequestIdForBankSvcRq() {
		Date date = new Date();
		String prefix = SystemUtil.format(date.toString(), "EEE MMM dd hh:mm:ss zzz yyyy","yyMMddHH-mmss-");
		String randomString = UUID.randomUUID().toString();
    	randomString = randomString.replaceAll("-", "");
    	randomString = randomString.substring(0, 19).toUpperCase();
    	String suffix =
    			randomString.substring(0, 3) + "-" + randomString.substring(3, 7) + "-"
    			+ randomString.substring(7, 11) + randomString.substring(11, 19);
    	return prefix.concat("B").concat(suffix);
	}
	
	private static String getRequestIdForTransferAddRequest() {
		Date date = new Date();
		String prefix = SystemUtil.format(date.toString(), "EEE MMM dd hh:mm:ss zzz yyyy","yyMMddHH-mmss-");
		
		String randomString = UUID.randomUUID().toString();
    	randomString = randomString.replaceAll("-", "");
    	randomString = randomString.substring(0, 19).toUpperCase();
    	String suffix =
    			randomString.substring(0, 3) + "-" + randomString.substring(3, 7) + "-"
    			+ randomString.substring(7, 11) + randomString.substring(11, 19);
    	return prefix.concat("A").concat(suffix);
	}

	private static String getPaymentReferenceId(PaymentType paymentType, Long siteId, String achMode) {
		Date date = new Date();
		String siteIdString = String.valueOf(siteId);
		if(siteIdString.length() == 1){
			siteIdString = "0".concat(siteIdString);
		}
		String paymentTypeString = "0";
		if(paymentType.equals(PaymentType.RECURRING)){
			paymentTypeString = "1";
		} else if(paymentType.equals(PaymentType.PAYASUGO)) {
			paymentTypeString = "2";
		} else if(paymentType.equals(PaymentType.OTC)) {
			paymentTypeString = "3";
		} else if(paymentType.equals(PaymentType.WEB)) {
			paymentTypeString = "4";
		}
		String mode = null;
		if(!StringUtils.isBlank(achMode) && achMode.equalsIgnoreCase("P")) {
			mode = "P";
		} else {
			mode = "C";
		}		
		String prefix = SystemUtil.format(date.toString(), "EEE MMM dd hh:mm:ss zzz yyyy","yyMMddHHmmss");		
		return mode.concat(prefix).concat(siteIdString).concat(paymentTypeString);
	}
	
	private static String getNextBusinessDay() {
		DateTime dateTime = new DateTime();
		if(dateTime.getDayOfWeek() == 5) {
			dateTime = dateTime.plusDays(2);
		} else if(dateTime.getDayOfWeek() == 6) {
			dateTime = dateTime.plusDays(1);
		}
		dateTime = dateTime.plusDays(1);
		return SystemUtil.format(dateTime.toDate().toString(),  "EEE MMM dd hh:mm:ss zzz yyyy", "yyyy-MM-dd");
	}
	
	private static SftpDTO getChannelToSftpServer(ACHTxDTO achTxDTO)
			throws Exception {
		String SFTPHOST = achTxDTO.getSftpHost();
		int SFTPPORT = achTxDTO.getSftpPort();
		String SFTPUSER = achTxDTO.getSftpUser();
		String SFTPPASS = achTxDTO.getSftpPassword();		
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		logger.info("preparing the host information for sftp.");
		JSch jsch = new JSch();
		session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
		session.setPassword(SFTPPASS);
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		logger.info("Host connected.");
		channel = session.openChannel("sftp");
		channel.connect();
		logger.info("sftp channel opened and connected.");
		SftpDTO sftpDTO = new SftpDTO();
		sftpDTO.setChannel(channel);
		channelSftp = (ChannelSftp) channel;
		sftpDTO.setChannelSftp(channelSftp);
		sftpDTO.setSession(session);
		return sftpDTO;
	}
	
	private static void disconnectChanneltoSftpServer(SftpDTO sftpDTO) {
		ChannelSftp channelSftp = sftpDTO.getChannelSftp();
		Channel channel = sftpDTO.getChannel();
		Session session = sftpDTO.getSession();
		channelSftp.exit();
        logger.info("sftp Channel exited.");
        channel.disconnect();
        logger.info("Channel disconnected.");
        session.disconnect();
        logger.info("Host Session disconnected.");
	 }
	
	private static void  generateXMLForResponse() throws IOException{
		XStream stream = new XStream(new DomDriver("UTF-8"));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
		stream.processAnnotations(BankServiceRequest.class);
		stream.processAnnotations(IntegratedPayableXML.class);
		stream.processAnnotations(TransferAddResponse.class);
		stream.processAnnotations(Status.class);		
		IntegratedPayableXML response = new IntegratedPayableXML();				
		BankServiceRequest bankServiceResponse = new BankServiceRequest();
		bankServiceResponse.setRequestId("00000000-0000-0000-0000-000000000000");		
		TransferAddResponse transferAddResponse = new TransferAddResponse();
		Status status = new Status();
		status.setSeverity("Error");
		status.setPaymentReferenceId("IN001");
		status.setStatusDesc("Delivered by Comerica as ACK REF: 8221988");
		status.setErrorDesc("2002- THE VALUE DATE OF 2004-01-25 IS NOT A VALID BUSINESS DAY");
		status.setAsOfDate("2004-01-26");
		status.setAsOfTime("17:21:00");
		transferAddResponse.setStatus(status);
		transferAddResponse.setRequestId("00000000-0000-0000-0000-000000000000");
		transferAddResponse.setSpReferenceId("Rejected");
		bankServiceResponse.setTransferAddResponse(transferAddResponse);
		response.setBankServiceRequest(bankServiceResponse);
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n");
		stream.toXML(response, writer);
		String xml = outputStream.toString("UTF-8");
		System.out.println(xml);	
	}
	
	private static void retrieveXMLResponseFiles(String localDir) throws Exception {
		String SFTPHOST = "12.15.7.210";
        int SFTPPORT = 22;
        String SFTPUSER = "granicusinc";
        String SFTPPASS = "O<4ILg0b";
        String SFTPWORKINGDIR = "FROM_FILE_TRANSFER";
        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        String fileName = null;
        logger.info("preparing the host information for sftp.");
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            logger.info("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            logger.info("sftp channel opened and connected.");            
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            BufferedOutputStream bos = null;            
            channelSftp = (ChannelSftp) channel;
            Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(SFTPWORKINGDIR);
            channelSftp.cd(SFTPWORKINGDIR);
            
			if (entries != null) {
				for (ChannelSftp.LsEntry lsEntry : entries) {
					if (lsEntry.getAttrs().isDir()
							&& (lsEntry.getFilename().contains("REJECTS") || lsEntry
									.getFilename().contains("CONFIRMS"))) {
						Vector<ChannelSftp.LsEntry> entries2 = channelSftp
								.ls(lsEntry.getFilename());
						channelSftp.cd(lsEntry.getFilename());
						if (entries2 != null) {
							for (ChannelSftp.LsEntry lsEntry2 : entries2) {
								if (!lsEntry2.getAttrs().isDir()) {

									fileName = lsEntry2.getFilename();
									logger.info(
											"Downloading File: {} From SFTP",
											fileName);
									bis = new BufferedInputStream(
											channelSftp.get(fileName));
									File newFile = new File(localDir + fileName);
									os = new FileOutputStream(newFile);
									bos = new BufferedOutputStream(os);
									int count;
									while ((count = bis.read(buffer)) > 0) {
										bos.write(buffer, 0, count);
									}
									bos.flush();
									bos.close();
								}
							}
						}
						channelSftp.cd("..");
					}
				
				}
			}
            logger.info("File Downloaded Successfully from SFTP server to {}", localDir);
        } catch (Exception exception) {
        	logger.error("Exception found while Downloading File: {} From SFTP" + exception, fileName);
            throw exception;
        }
        finally{
            channelSftp.exit();
            logger.info("sftp Channel exited.");
            channel.disconnect();
            logger.info("Channel disconnected.");
            session.disconnect();
            logger.info("Host Session disconnected.");
        }
    }
	
	private static void send(String fileName) throws Exception {
		String SFTPHOST = "12.15.7.210";
        int SFTPPORT = 22;
        String SFTPUSER = "granicusinc";
        String SFTPPASS = "O<4ILg0b";
        String SFTPWORKINGDIR = "TO_FILE_TRANSFER";
        String content = "Granicus Acquired Amcad";
        

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        logger.info("preparing the host information for sftp.");
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            logger.info("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            logger.info("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(SFTPWORKINGDIR);
            InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            channelSftp.put(stream, fileName);
            logger.info("File transfered successfully to host.");
        } catch (Exception ex) {
             logger.info("Exception found while tranfer the response.");
             throw ex;
        }
        finally{
            channelSftp.exit();
            logger.info("sftp Channel exited.");
            channel.disconnect();
            logger.info("Channel disconnected.");
            session.disconnect();
            logger.info("Host Session disconnected.");
        }
    }

	public static void main(String[] args) throws Exception {
		/*String content = "Awesome... SFTP";
		String fileName = "Sample.txt";
		send(fileName);
			generateXMLResponse();*/
			/*XStream stream = new XStream();		
			stream.processAnnotations(BankServiceRequest.class);
			stream.processAnnotations(IntegratedPayableXML.class);
			stream.processAnnotations(TransferAddResponse.class);
			stream.processAnnotations(Status.class);
			String xmlContent = readFile("C:\\Users\\valampally\\Desktop\\ACH\\Reject.xml", StandardCharsets.UTF_8);
			IntegratedPayableXML resp = (IntegratedPayableXML)stream.fromXML(xmlContent);
			System.out.println(resp);*/
			/*ACHTxDTO achTxDTO1 = getAchInfoFromPaymentReferenceId("C150519172335032");
			ACHTxDTO achTxDTO2 = getAchInfoFromPaymentReferenceId("C150519172329033");
			ACHTxDTO achTxDTO3 = getAchInfoFromPaymentReferenceId("C150519172304031");
			System.out.println(achTxDTO1.getSiteId());
			System.out.println(achTxDTO1.getPaymentType());
			System.out.println(achTxDTO2.getSiteId());
			System.out.println(achTxDTO2.getPaymentType());
			System.out.println(achTxDTO3.getSiteId());
			System.out.println(achTxDTO3.getPaymentType());	
			String name = "Dallas";
			String substring = StringUtils.substring(name, 0,30);*/
			retrieveXMLResponseFiles("C:\\AchResponseFiles\\");
			deleteAllFiles("C:\\AchResponseFiles\\");
			//generateXMLForResponse();
		}
	/*public static void send (String fileName) {
    String SFTPHOST = "12.15.7.210";
    int SFTPPORT = 22;
    String SFTPUSER = "granicusinc";
    String SFTPPASS = "O<4ILg0b";
    String SFTPWORKINGDIR = "TO_FILE_TRANSFER";

    Session session = null;
    Channel channel = null;
    ChannelSftp channelSftp = null;
    logger.info("preparing the host information for sftp.");
    try {
        JSch jsch = new JSch();
        session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
        session.setPassword(SFTPPASS);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        logger.info("Host connected.");
        channel = session.openChannel("sftp");
        channel.connect();
        logger.info("sftp channel opened and connected.");
        channelSftp = (ChannelSftp) channel;
        channelSftp.cd(SFTPWORKINGDIR);
        File f = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(f);
        String dst = f.getName();
        channelSftp.put(fileInputStream, dst);
        logger.info("File transfered successfully to host.");
    } catch (Exception ex) {
         logger.info("Exception found while tranfer the response.");
    }
    finally{
        channelSftp.exit();
        logger.info("sftp Channel exited.");
        channel.disconnect();
        logger.info("Channel disconnected.");
        session.disconnect();
        logger.info("Host Session disconnected.");
    }
}*/

}
