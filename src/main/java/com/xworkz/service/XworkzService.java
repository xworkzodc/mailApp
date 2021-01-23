package com.xworkz.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface XworkzService {

	public List<String> getContactListFromXls(MultipartFile file);

	public String sendBulkMSG(List<String> contactList, String message);
	
	public String sendSingleSMS(String mobileNumber,String msg);

	public String deliveryReports(String messageId);
	
	public String checkSMSBalance();
}
