package com.xworkz.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.xworkz.util.EncryptionHelper;
import com.xworkz.util.ExcelHelper;

@Service
public class XworkzServiceImpl implements XworkzService {

	static Logger logger = LoggerFactory.getLogger(XworkzServiceImpl.class);
	@Autowired
	ExcelHelper excelHelper;
	@Autowired
	SMSService smsService;
	@Autowired
	EncryptionHelper encryptionHelper;
	@Value("${apiKey}")
	private String apiKey;
	@Value("${SMSusername}")
	private String SMSusername;
	@Value("${sender}")
	private String sender;
	@Value("${simpleSMS}")
	private String simpleSMS;
	@Value("${credit}")
	private String creditCheck;
	@Value("${route}")
	private String route;
	@Value("${report}")
	private String report;
	
	public List<String> getContactListFromXls(MultipartFile file) {
		if (!file.isEmpty()) {
			List<String> contactList = null;
			try {
				file.getBytes();
				String filename = file.getOriginalFilename();
				// Creating the directory to store file
				logger.info("File name is {}", filename);

				if ((file.getInputStream() != null)) {
					contactList = excelHelper.getContactListFromInputStream(file.getInputStream());
				}
				return contactList;
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
		return null;
	}

	public String sendBulkMSG(List<String> contactList, String message) {
		logger.info(contactList.toString());
		String res = null;
		for (String phone : contactList) {
			if (phone.length() != 0) {
				try {
					Thread.sleep(600);
					res = smsService.sendSMS(encryptionHelper.decrypt(apiKey), encryptionHelper.decrypt(SMSusername),encryptionHelper.decrypt(sender),
							phone, message,simpleSMS, route);
					logger.info("BulkMSG Result is {}", res);
				} catch (Exception e) {
					logger.error("\n\nMessage is {} and exception is {}\n\n\n\n\n", e.getMessage(), e);
				}

			}
		}
		return res;
	}

	@Override
	public String sendSingleSMS(String mobileNumber, String message) {
		try {
			logger.debug("smsType is :{} mobileNumber is :{} message is: {}",simpleSMS,mobileNumber,message);
			String res = smsService.sendSMS(encryptionHelper.decrypt(apiKey), encryptionHelper.decrypt(SMSusername),encryptionHelper.decrypt(sender),
					 mobileNumber, message,simpleSMS, route);
			logger.info("SingleSMS Result is {}", res);
			return res;
		} catch (Exception e) {
			logger.error("\n\nMessage is {} and exception is {}\n\n\n\n\n", e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String deliveryReports(String messageId) {
		try {
			String res = smsService.deliveryReports(encryptionHelper.decrypt(apiKey),
					encryptionHelper.decrypt(SMSusername), report, messageId);
			logger.info("Delivery Reports Result is {}", res);
			return res;
		} catch (Exception e) {
			logger.error("\n\nMessage is {} and exception is {}\n\n\n\n\n", e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String checkSMSBalance() {
		try {
			/*
			 * logger.debug("API KEY is {} SMSusername Key is {} Sender id is {}",apiKey,
			 * SMSusername,sender);
			 * 
			 * logger.
			 * debug("API KEY is {} SMSusername Key is {} Sender id is {} report is {}",
			 * encryptionHelper.decrypt(apiKey), encryptionHelper.decrypt(SMSusername),
			 * encryptionHelper.decrypt(sender), report);
			 */

			logger.debug("API KEY is {}", encryptionHelper.decrypt(apiKey));
			String result = smsService.checkSMSBalance(encryptionHelper.decrypt(apiKey), encryptionHelper.decrypt(SMSusername), creditCheck, route);
			logger.info("checkSMSBalance Result is {}", result);
			return result;
		} catch (Exception e) {
			logger.error("\n\nMessage is {} and exception is {}\n\n\n\n\n", e.getMessage(), e);
		}
		return null;
	}
}
