package com.xworkz.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.xworkz.dto.MailChimpCampaign;
import com.xworkz.dto.MailChimpList;
import com.xworkz.dto.MailChimpMailDetails;
import com.xworkz.dto.SendMailDTO;
import com.xworkz.util.EncryptionHelper;

@Service
@Configuration
public class OkHttpClientServiceImpl implements OkHttpClientService {

	@Value("${auth}")
	private String auth;
	@Value("${replyTo}")
	private String replyTo;
	@Value("${fromName}")
	private String fromName;
	@Value("${http}")
	private String http;

	@Value("${auth1}")
	private String auth1;
	@Value("${replyTo1}")
	private String replyTo1;
	@Value("${fromName}")
	private String fromName1;
	@Value("${http1}")
	private String http1;
	@Value("${bulkMailTemplate}")
	private String templatesLink;
	private Account accountId = Account.Default;

	@Autowired
	EncryptionHelper helper;
	
	private Logger logger = LoggerFactory.getLogger(OkHttpClientServiceImpl.class);

	public OkHttpClientServiceImpl() {
		logger.info("{} is created..........", this.getClass().getCanonicalName());
	}

	public boolean sendCompaign(String compaignId, Integer integer) {
		String a;
		String rt;
		String fn;
		String htp;
		if (integer == accountId.getId()) {
			a = auth1;
			rt = replyTo1;
			fn = fromName1;
			htp = http1;
		} else {
			a = auth;
			rt = replyTo;
			fn = fromName;
			htp = http;
		}
		try {
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, "");
			Request request = new Request.Builder()
					.url("https://" + htp + ".api.mailchimp.com/3.0/campaigns/" + compaignId + "/actions/send")
					.post(body).addHeader("Content-Type", "application/json")
					.addHeader("Content-Type", "application/json").addHeader("Authorization", helper.decrypt(a)).build();
			Response response = client.newCall(request).execute();
			logger.info("Campaign Response is {}", response.isSuccessful());
			if (response.isSuccessful()) {
				return true;
			}
			logger.debug("Response Is {}", response.body().string());
		} catch (IOException e) {
			logger.info("Error is {} and Message is {}", e, e.getMessage());
		}
		return false;

//		try {
//			OkHttpClient client = new OkHttpClient();
//			MediaType mediaType = MediaType.parse("application/json");
//			RequestBody body = RequestBody.create(mediaType, "");
//			Request request = new Request.Builder()
//					.url("https://us20.api.mailchimp.com/3.0/campaigns/" + compaignId + "/actions/send").post(body)
//					.addHeader("Content-Type", "application/json").addHeader("Content-Type", "application/json")
//					.addHeader("Authorization", auth).build();
//			Response response = client.newCall(request).execute();
//			logger.info("Campaign Response is {}", response.isSuccessful());
//			if (response.isSuccessful()) {
//				return true;
//			}
//			logger.debug("Response Is {}", response.body().string());
//		} catch (IOException e) {
//			logger.info("Error is {} and Message is {}", e, e.getMessage());
//		}
//		return false;
	}

	public boolean generateContent(String campaignId, SendMailDTO dto) {
		String a;
		String rt;
		String fn;
		String htp;
		if (dto.getMsgType() == accountId.getId()) {
			a = auth1;
			rt = replyTo1;
			fn = fromName1;
			htp = http1;
		} else {
			a = auth;
			rt = replyTo;
			fn = fromName;
			htp = http;
		}
//		try {
//			String finalHtmlString = getHTMLTextFromFile(dto);
//			if (finalHtmlString != null) {
//				JsonObject htmlText = new JsonObject();
//				htmlText.addProperty("html", finalHtmlString);
//				logger.info("Html Json Is {}", htmlText.toString());
//				OkHttpClient client = new OkHttpClient();
//				MediaType mediaType = MediaType.parse("application/json");
//				RequestBody body = RequestBody.create(mediaType, htmlText.toString());
//				Request request = new Request.Builder()
//						.url("https://us20.api.mailchimp.com/3.0/campaigns/" + campaignId + "/content").put(body)
//						.addHeader("Content-Type", "application/json").addHeader("Authorization", auth).build();
//				Response response = client.newCall(request).execute();
//				logger.info("Edit Response is {} ", response.body().string());
//				if (response.isSuccessful())
//					return true;
//			}
//		} catch (IOException e) {
//			logger.error("Error is {} and Message is {}", e, e.getMessage());
//		}
//		return false;
		try {
			String finalHtmlString = getHTMLTextFromFile(dto);
			if (finalHtmlString != null) {
				JsonObject htmlText = new JsonObject();
				htmlText.addProperty("html", finalHtmlString);
				logger.info("Html Json Is {}", htmlText.toString());
				OkHttpClient client = new OkHttpClient();
				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType, htmlText.toString());
				Request request = new Request.Builder()
						.url("https://" + htp + ".api.mailchimp.com/3.0/campaigns/" + campaignId + "/content").put(body)
						.addHeader("Content-Type", "application/json").addHeader("Authorization", helper.decrypt(a)).build();
				Response response = client.newCall(request).execute();
				logger.info("Edit Response is {} ", response.body().string());
				if (response.isSuccessful())
					return true;
			}
		} catch (IOException e) {
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		}
		return false;
	}

	public String createCompaign(String listId, SendMailDTO dto) {
		String a;
		String rt;
		String fn;
		String htp;
		if (dto.getMsgType() == accountId.getId()) {
			a = auth1;
			rt = replyTo1;
			fn = fromName1;
			htp = http1;
		} else {
			a = auth;
			rt = replyTo;
			fn = fromName;
			htp = http;
		}
		try {
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			JsonObject jsonObject = createJsonObject(listId, dto);
			logger.info(jsonObject.toString());
			RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
			Request request = new Request.Builder().url("https://" + htp + ".api.mailchimp.com/3.0/campaigns")
					.post(body).addHeader("Content-Type", "application/json").addHeader("Authorization", helper.decrypt(a))
					.addHeader("cache-control", "no-cache")
					.addHeader("Postman-Token", "8c397e18-518f-463b-b3a7-3a65a7d4b5b9").build();
			Response response = client.newCall(request).execute();
			String result = response.body().string();
			MailChimpCampaign campaign = new Gson().fromJson(result.toString(), MailChimpCampaign.class);
			return campaign.getId();
		} catch (IOException e) {
			logger.info("Error is {} and Message is {}", e, e.getMessage());
		}
		return null;

//		try {
//			OkHttpClient client = new OkHttpClient();
//			MediaType mediaType = MediaType.parse("application/json");
//			JsonObject jsonObject = createJsonObject(listId, dto);
//			logger.info(jsonObject.toString());
//			RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
//			Request request = new Request.Builder().url("https://us20.api.mailchimp.com/3.0/campaigns").post(body)
//					.addHeader("Content-Type", "application/json").addHeader("Authorization", auth)
//					.addHeader("cache-control", "no-cache")
//					.addHeader("Postman-Token", "8c397e18-518f-463b-b3a7-3a65a7d4b5b9").build();
//			Response response = client.newCall(request).execute();
//			String result = response.body().string();
//			MailChimpCampaign campaign = new Gson().fromJson(result.toString(), MailChimpCampaign.class);
//			return campaign.getId();
//		} catch (IOException e) {
//			logger.info("Error is {} and Message is {}", e, e.getMessage());
//		}
//		return null;
	}

	public JsonObject createJsonObject(String listId, SendMailDTO dto) {
		String a;
		String rt;
		String fn;
		String htp;
		if (dto.getMsgType() == accountId.getId()) {
			a = auth1;
			rt = replyTo1;
			fn = fromName1;
			htp = http1;
		} else {
			a = auth;
			rt = replyTo;
			fn = fromName;
			htp = http;
		}

		JsonObject lsId = new JsonObject();
		lsId.addProperty("list_id", listId);
		JsonObject settings = new JsonObject();
		settings.addProperty("subject_line", dto.getSubName());
		settings.addProperty("reply_to", rt);
		settings.addProperty("from_name", fn);
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("recipients", lsId);
		jsonObject.add("settings", settings);
		jsonObject.addProperty("type", "regular");
		return jsonObject;
	}

	public String getCompaignDetails() {
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url("https://us20.api.mailchimp.com/3.0/campaigns").get()
					.addHeader("Content-Type", "application/x-www-form-urlencoded")
					.addHeader("User-Agent", "Mozilla/5.0").addHeader("Authorization", auth)
					.addHeader("cache-control", "no-cache").build();
			Response response = client.newCall(request).execute();
			String result = response.body().string();
			return result;
		} catch (IOException e) {
			logger.error("Error Code is {} and Message is {} ", e, e.getMessage());
		}
		return null;
	}

	public String getAllMailChimpList(Integer mailid) {
		String a;
		String rt;
		String fn;
		String htp;
		if (mailid == accountId.getId()) {
			a = auth1;
			rt = replyTo1;
			fn = fromName1;
			htp = http1;
		} else {
			a = auth;
			rt = replyTo;
			fn = fromName;
			htp = http;
		}
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url("https://" + htp + ".api.mailchimp.com/3.0/lists").get()
					.addHeader("Content-Type", "application/x-www-form-urlencoded")
					.addHeader("User-Agent", "Mozilla/5.0").addHeader("Authorization", helper.decrypt(a))
					.addHeader("cache-control", "no-cache").build();
			Response response = client.newCall(request).execute();
			String result = response.body().string();
			return result;
		} catch (IOException e) {
			logger.error("Error Code is {} and Message is {} ", e, e.getMessage());
		}
		return null;
	}

	public String getListIdFromListName(String listName, Integer mailid) {
		logger.info("List name from dto {}", listName);
		MailChimpList chimpList = new Gson().fromJson(getAllMailChimpList(mailid), MailChimpList.class);
		for (MailChimpMailDetails chimpMailDetails : chimpList.getLists()) {
			if (!listName.isEmpty() && !chimpMailDetails.getName().isEmpty()
					&& listName.equalsIgnoreCase(chimpMailDetails.getName())) {
				logger.info("List is Founded....\n total member is {} and List id is {}",
						chimpMailDetails.getStats().getMemberCount(), chimpMailDetails.getId());
				logger.info("Data is ------------->>>> {}", chimpList);
				return chimpMailDetails.getId();
			}
		}
		return null;
	}

	public String getHTMLTextFromFile(SendMailDTO dto) {
		String data = "";
		try {
			RestTemplate restTemplate = new RestTemplate();
			URI url = new URI(templatesLink + dto.getFileName());
			HttpHeaders headers = new HttpHeaders();
			Charset utf8 = Charset.forName("UTF-8");
			org.springframework.http.MediaType mediaType = new org.springframework.http.MediaType("text", "html", utf8);
			headers.setContentType(mediaType);

			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			data = responseEntity.getBody();

			logger.info("html String is {}", data);
			data = replaceHTMLData(dto, data);
			logger.info("After Change the dynamic news {}", data);

		} catch (URISyntaxException e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {} ", e, e.getMessage());
		}
		return data;
	}

	public String replaceHTMLData(SendMailDTO dto, String data) {
		if (data.contains("dynamicNews")) {
			data = data.replace("dynamicNews", dto.getDynamicNews() != null ? dto.getDynamicNews() : "");
		}
		if (data.contains("imageURL")) {
			data = data.replace("imageURL", dto.getImageURL() != null ? dto.getImageURL() : "");
		}
		if (data.contains("courseName")) {
			data = data.replace("courseName", dto.getCourseName() != null ? dto.getCourseName() : "");
		}
		if (data.contains("dateYear")) {
			data = data.replace("dateYear", dto.getDateYear() != null ? dto.getDateYear() : "");
		}
		if (data.contains("batchCode")) {
			data = data.replace("batchCode", dto.getBatchCode() != null ? dto.getBatchCode() : "");
		}
		if (data.contains("birthdayQuotes")) {
			data = data.replace("birthdayQuotes", dto.getBirthdayQuotes() != null ? dto.getBirthdayQuotes() : "");
		}
		if (data.contains("courseStartDate")) {
			data = data.replace("courseStartDate", dto.getStartDate() != null ? dto.getStartDate() : "");
		}
		if (data.contains("courseTrainer")) {
			data = data.replace("courseTrainer", dto.getTrainerName() != null ? dto.getTrainerName() : "");
		}
		if (data.contains("courseTime")) {
			data = data.replace("courseTime", dto.getTime() != null ? dto.getTime() : "");
		}
		if (data.contains("courseFees")) {
			data = data.replace("courseFees", dto.getFees() != null ? dto.getFees() : "");
		}
		if (data.contains("courseMode")) {
			data = data.replace("courseMode", dto.getClassMode() != null ? dto.getClassMode() : "");
		}

		return data;
	}
}