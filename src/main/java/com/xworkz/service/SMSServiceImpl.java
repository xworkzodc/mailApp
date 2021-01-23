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
public class SMSServiceImpl implements SMSService {

	Logger logger = LoggerFactory.getLogger(SMSServiceImpl.class);

	public String sendSMS(String apiKey, String username, String sender, String phone, String message, String smsType,
			String route) {
		StringBuilder content = new StringBuilder();
		String line;
		try {
			String requestUrl = "http://www.k3digitalmedia.co.in/websms/api/http/index.php?" + "&username="
					+ URLEncoder.encode(username, "UTF-8") + "&apikey=" + URLEncoder.encode(apiKey, "UTF-8")
					+ "&apirequest=" + URLEncoder.encode(smsType, "UTF-8") + "&route="
					+ URLEncoder.encode(route, "UTF-8") + "&sender=" + URLEncoder.encode(sender, "UTF-8") + "&mobile="
					+ URLEncoder.encode(phone, "UTF-8") + "&message=" + URLEncoder.encode(message, "UTF-8");
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoOutput(true);
			uc.setRequestMethod("POST");

			DataOutputStream wr = new DataOutputStream(uc.getOutputStream());
			wr.write(requestUrl.toString().getBytes());
			// get the response
			BufferedReader bufferedReader = null;
			if (uc.getResponseCode() == 200) {
				logger.debug("Response code is {}", uc.getResponseCode());
				bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				logger.debug("Success URL is {}", uc);
				logger.info("SMS Sent Succesfuly to +" + phone);
				while ((line = bufferedReader.readLine()) != null) {
					content.append(line).append("\n");
				}
				bufferedReader.close();
				logger.debug("Content is {}", content);
				return content.toString();
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader(uc.getErrorStream()));
				logger.debug("Fail contact number is {}", requestUrl.length());
				logger.info("SMS Sent Faild to +" + phone);
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return null;

	}

	public String deliveryReports(String apiKey, String username, String apiRequest, String messageId) {
		try {
			StringBuilder content = new StringBuilder();
			String line;
			String requestUrl = "http://www.k3digitalmedia.co.in/websms/api/http/index.php?" + "&apikey="
					+ URLEncoder.encode(apiKey, "UTF-8") + "&apirequest=" + URLEncoder.encode(apiRequest, "UTF-8")
					+ "&username=" + URLEncoder.encode(username, "UTF-8") + "&messageid="
					+ URLEncoder.encode(messageId, "UTF-8");
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoOutput(true);
			uc.setRequestMethod("POST");

			DataOutputStream wr = new DataOutputStream(uc.getOutputStream());
			wr.write(requestUrl.toString().getBytes());
			// get the response
			BufferedReader bufferedReader = null;
			if (uc.getResponseCode() == 200) {
				logger.debug("Response code is {}", uc.getResponseCode());
				bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				logger.debug("Success URL is {}", uc);
				logger.info("Delivery Reports fecthed Successfully for +" + messageId);
				while ((line = bufferedReader.readLine()) != null) {
					content.append(line).append("\n");
				}
				logger.debug("Content is {}", content);
				return content.toString();
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader(uc.getErrorStream()));
				logger.info("Delivery Reports Not available for +" + messageId);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return null;
	}

	@Override
	public String checkSMSBalance(String apiKey, String username, String apiRequest, String route) {

		BufferedReader bufferedReader = null;
		StringBuilder content = new StringBuilder();
		String line;
		try {

			String requestUrl = "http://www.k3digitalmedia.co.in/websms/api/http/index.php?" + "&apikey="
					+ URLEncoder.encode(apiKey, "UTF-8") + "&apirequest=" + URLEncoder.encode(apiRequest, "UTF-8")
					+ "&username=" + URLEncoder.encode(username, "UTF-8") + "&route="
					+ URLEncoder.encode(route, "UTF-8");
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoOutput(true);
			uc.setRequestMethod("POST");

			DataOutputStream wr = new DataOutputStream(uc.getOutputStream());
			wr.write(requestUrl.toString().getBytes());
			// get the response
			if (uc.getResponseCode() == 200) {
				logger.debug("Response code is {}", uc.getResponseCode());
				bufferedReader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
				logger.debug("Request URL is {}", uc);
				logger.info("Balance fecthed Successfully for the Account =" + username);
				while ((line = bufferedReader.readLine()) != null) {
					content.append(line).append("\n");
				}
				bufferedReader.close();
				logger.debug("Content is {}", content);
				String string = content.toString();
				JSONObject json = new JSONObject(string);
				String balance = json.getString("balance");
				logger.info("balance={}", balance);
				return balance;
			} else {
				bufferedReader = new BufferedReader(new InputStreamReader(uc.getErrorStream()));
				logger.info("Balance fecth Faild forthe Account +" + username);
			}
			bufferedReader.close();
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return null;

	}
}
