package com.xworkz.controller;

import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.google.gson.Gson;
import com.xworkz.dto.MailChimpList;
import com.xworkz.dto.SendMailDTO;
import com.xworkz.service.OkHttpClientService;
import com.xworkz.service.XworkzService;

@RestController
@RequestMapping("/")
public class MailController {

	@Autowired
	private XworkzService xworkzService;
	@Autowired
	private OkHttpClientService clientService;

	private Logger logger = LoggerFactory.getLogger(MailController.class);

	public MailController() {
		logger.debug("{} Is Created...........", this.getClass().getSimpleName());
	}

	@RequestMapping(value = "/sendSMS.do", method = RequestMethod.POST)
	public ModelAndView senBulkSMS(@RequestParam("uploadFile") MultipartFile file, @RequestParam("msg") String msg) {
		logger.debug("is called....");

		ModelAndView modelAndView = new ModelAndView("index");
		if (!file.isEmpty()) {
			logger.debug("contant type {}", file.getContentType());
			List<String> contactList = xworkzService.getContactListFromXls(file);
			if (Objects.nonNull(contactList)) {
				String response = xworkzService.sendBulkMSG(contactList, msg);
				if (Objects.nonNull(response))
				return modelAndView.addObject("msg", "Bulk SMS Sent Successfully");
			 }
			logger.debug("Is file is Empty :{}", contactList.isEmpty());
		} else {
			modelAndView.addObject("msg", "File is not found.........");
		}
		return modelAndView.addObject("msg", "Bulk SMS Sent Faild! Try Again.");
		
	}

	@RequestMapping(value = "/sendSingleSMS.do", method = RequestMethod.POST)
	public ModelAndView sendSingleSMS(@RequestParam("mobile") String mobileNumber,@RequestParam("message") String message) {
		logger.debug("sendSingleSMS() is called....");
		ModelAndView modelAndView = new ModelAndView("index");
		if (Objects.nonNull(mobileNumber) && Objects.nonNull(message)) {
			logger.debug("The Mobile Number entered is {}", mobileNumber);
			try {
				String response = xworkzService.sendSingleSMS(mobileNumber, message);
				    if (Objects.nonNull(response)) {
					JSONObject json = new JSONObject(response);
					String  status = json.getString("message");
					JSONArray messageId = json.getJSONArray("message-id");
					logger.debug("messageid={}", messageId.toString());
					return modelAndView.addObject("msg","The "+status+" To: " + mobileNumber + " and The Message Id: " + messageId.toString());
				    }
				    else
					return modelAndView.addObject("msg", "SMS Sent Faild To:" + mobileNumber);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return modelAndView.addObject("msg", "SMS Sent Faild To:" + mobileNumber);

	}

	@RequestMapping(value = "/reports.do", method = RequestMethod.POST)
	public ModelAndView deliveryReports(@RequestParam("messageId") String messageId) {
		logger.debug("deliveryReports() is called....");

		ModelAndView modelAndView = new ModelAndView("index");
		if (messageId != null) {
			try {
				logger.debug("The messageId entered is {}", messageId);
				String response = xworkzService.deliveryReports(messageId);
				if (Objects.nonNull(response)) {
					JSONObject json = new JSONObject(response);
					String status = json.getString("message_status");
					String mobile = json.getString("mobile");
					logger.debug("mobile number:{} message_status={}", mobile, status);
					return modelAndView.addObject("msg", "Fetched Delivery Reports for " + messageId + " The Mobile: "+ mobile + " And Status: " + status);
				} else {
					return modelAndView.addObject("msg","Delivery Reports Not available for the Message Id " + messageId);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return modelAndView.addObject("msg", "Delivery Reports Not available for the Message Id " + messageId);
	}

	@RequestMapping(value = "/checkSMSBalance.do", method = RequestMethod.POST)
	public ModelAndView checkSMSBalance() {
		ModelAndView modelAndView = new ModelAndView("index");
		try {
			logger.debug("checkSMSBalance() is called....");
			logger.debug("Fecthing Balance...");
			String result = xworkzService.checkSMSBalance();
			if (Objects.nonNull(result))
				return modelAndView.addObject("msg", "Total SMS Balance Left: " + result);
			else
				return modelAndView.addObject("msg", "Sorry couldn't able to fecth Balace at this time!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return modelAndView.addObject("msg", "Sorry couldn't able to fecth Balace at this time!");
	}

	@RequestMapping(value = "getListDetails.do", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public MailChimpList getList(@RequestHeader("Accept") HttpHeaders headers,
			@RequestParam("msgType") Integer msgType) {
		MailChimpList list = new Gson().fromJson(clientService.getAllMailChimpList(msgType), MailChimpList.class);
		logger.debug("List is {}", list);
		return list;

	}

	@RequestMapping(value = "newsfeed.do", method = RequestMethod.POST)
	public ModelAndView sendNewsFeed(@ModelAttribute SendMailDTO mailDTO) {
		logger.debug("Image Url Is {}", mailDTO.getImageURL());
		return sendMail(mailDTO);
	}

	@RequestMapping(value = "newJoiner.do", method = RequestMethod.POST)
	public ModelAndView sendNewJoiner(@ModelAttribute SendMailDTO mailDTO) {
		logger.debug("data is {}", mailDTO);
		clientService.getHTMLTextFromFile(mailDTO);
		return sendMail(mailDTO);
	}

	@RequestMapping(value = "feesPaid.do", method = RequestMethod.POST)
	public ModelAndView sendFeesAcknnowledgement(@ModelAttribute SendMailDTO mailDTO) {
		logger.debug("data is {}", mailDTO);
		return sendMail(mailDTO);
	}

	@RequestMapping(value = "birthdayGreeting.do", method = RequestMethod.POST)
	public ModelAndView sendBirthdayGreeting(@ModelAttribute SendMailDTO mailDTO) {
		logger.debug("Image Url is {}", mailDTO.getImageURL());
		return sendMail(mailDTO);
	}

	@RequestMapping(value = "courseContain.do", method = RequestMethod.POST)
	public ModelAndView sendCourseContain(@ModelAttribute SendMailDTO mailDTO) {
		logger.debug("data is {}", mailDTO);
		return sendMail(mailDTO);
	}

	public ModelAndView sendMail(SendMailDTO dto) {
		ModelAndView modelAndView = new ModelAndView("index");
		logger.debug("List Name=" + dto.getListName());
		logger.debug("Msg Id=" + dto.getMsgType());
		String listId = clientService.getListIdFromListName(dto.getListName(), dto.getMsgType());
		if (listId != null) {
			logger.debug("list Id Is Fetched..........................");
			String campaignId = clientService.createCompaign(listId, dto);
			return campaignCreate(dto, modelAndView, campaignId);
		} else {
			modelAndView.addObject("msg", "List Name Was Not Found............");
			logger.error("List Name Was Not Found............");
			return modelAndView;
		}
	}

	private ModelAndView campaignCreate(SendMailDTO dto, ModelAndView modelAndView, String campaignId) {
		if (campaignId != null) {
			logger.debug("Campaign Id Created Successfully...................");
			boolean edit = clientService.generateContent(campaignId, dto);
			return editMail(modelAndView, campaignId, edit, dto.getMsgType());
		} else {
			modelAndView.addObject("msg", "Campaign Is Not Created............");
			logger.error("Campaign Is Not Created............");
			return modelAndView;
		}
	}

	private ModelAndView editMail(ModelAndView modelAndView, String campaignId, boolean edit, Integer integer) {
		if (edit) {
			logger.debug("Html Edited Successfully.........................");
			boolean result = clientService.sendCompaign(campaignId, integer);
			return send(modelAndView, result);
		} else {
			modelAndView.addObject("msg", "Campaign Is not Edited................");
			logger.error("Campaign Is not Edited...............");
			return modelAndView;
		}
	}

	private ModelAndView send(ModelAndView modelAndView, boolean result) {
		if (result) {
			modelAndView.addObject("msg", "Mail sent Successfully...................");
			logger.debug("Mail sent Successfully...................");
			return modelAndView;
		} else {
			modelAndView.addObject("msg", "Mail sent faild, Please Try Again................");
			logger.error("Mail Was Not Sended................");
			return modelAndView;
		}
	}

}