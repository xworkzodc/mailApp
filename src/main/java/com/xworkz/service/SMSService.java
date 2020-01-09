package com.xworkz.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SMSService {
	
	Logger logger=LoggerFactory.getLogger(SMSService.class);
	
	public String sendCampaign(String apiKey,String secretKey,String useType, String phone, String message, String senderId){
		  StringBuilder content = new StringBuilder();
		    try {
		        // construct data
		        JSONObject urlParameters = new JSONObject();
		        urlParameters.put("apikey", apiKey);
		        urlParameters.put("secret", secretKey);
		        urlParameters.put("usetype", useType);
		        urlParameters.put("phone", phone);
		        urlParameters.put("message", URLEncoder.encode(message, "UTF-8"));
		        urlParameters.put("senderid", senderId);
		        URL obj = new URL("http://www.way2sms.com/api/v1/sendCampaign");
		        // send data
		        HttpURLConnection httpConnection = (HttpURLConnection) obj.openConnection();
		        httpConnection.setDoOutput(true);
		        httpConnection.setRequestMethod("POST");
		        httpConnection.setRequestProperty("Content-Type", "application/json");
		        DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
		        wr.write(urlParameters.toString().getBytes());
		        // get the response
		        BufferedReader bufferedReader = null;
		        if (httpConnection.getResponseCode() == 200) {
		        	logger.debug("Response code is {}",httpConnection.getResponseCode());
		            bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
		            logger.debug("Success contact number is {}",phone);
		        } else {
		            bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
		            logger.debug("Fail contact number is {}",phone);
		        }
		        String line;
		        while ((line = bufferedReader.readLine()) != null) {
		            content.append(line).append("\n");
		        }
		        bufferedReader.close();
		    } catch (Exception ex) {
		    	logger.error("Exception is {} and message is {}",ex,ex.getMessage());
		    }
		    logger.debug("Content is {}",content.toString());
		    return content.toString();
	    }
}
