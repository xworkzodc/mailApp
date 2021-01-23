package com.xworkz.service;

public interface SMSService {
  
	public String sendSMS(String apiKey, String username, String sender, String phone, String message,
			String smsType,String route);
	
	public String deliveryReports(String apiKey, String username, String apiRequest ,String messageId);
	
	public String checkSMSBalance(String apiKey, String username, String apiRequest ,String route);
}
