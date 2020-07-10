package com.xworkz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Getter
@Setter
@Service
public class SpringMailServiceImpl implements SpringMailService {

	private Logger logger = LoggerFactory.getLogger(SpringMailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;
	@Value("${mailFrom}")
	private String mailFrom;
	
	public SpringMailServiceImpl() {
		logger.info("created " + this.getClass().getSimpleName());
	}

	@Override
	public boolean validateAndSendMailByMailId(String to, String subject, String body) {
		logger.info("invoked validateAndSendMailByMailId of SpringMailServiceImpl...");

		SimpleMailMessage mail = new SimpleMailMessage();

		mail.setFrom(mailFrom);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(body);

		try {
			mailSender.send(mail);
			logger.info("Mail sent successfully");
			return true;
		} catch (MailException e) {
			logger.info("Mail sent Faild!");
			logger.error(e.getMessage(), e);
		}
		return false;
	}

}
