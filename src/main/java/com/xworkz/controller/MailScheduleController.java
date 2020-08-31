package com.xworkz.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.xworkz.service.MailSchedularService;

@RestController
@RequestMapping("/cron")
public class MailScheduleController {
	
	private Logger logger = LoggerFactory.getLogger(MailScheduleController.class);
	
	@Autowired
	private MailSchedularService mailSchedular;

	@RequestMapping(value = "/mailSchedule.do", method = RequestMethod.GET)
	public String sendScheduleMail() {
		logger.info("invoked sendScheduleMail() in controller");
		try {
			mailSchedular.birthadyMailSender();
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return "Job executed";
	}	
	
	
}
