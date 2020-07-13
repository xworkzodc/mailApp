package com.xworkz.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.xworkz.dto.LoginDTO;

@Service
public class LoginServiceImpl implements LoginService {

	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	
	public LocalTime mailsentTime;
	public LocalTime loginTime;
	public String temporaryPass;
	
	@Autowired
	private SpringMailService mailService;

	@Value("${to.MailID}")
	private String toMailId;
	@Value("${to.MailSubject}")
	private String toMailSubject;
	

	public LoginServiceImpl() {
		logger.info("{} Is Created...........", this.getClass().getSimpleName());
	}

	@Override
	public boolean validateAndLogin(LoginDTO dto) {
		logger.info("invoked validateAndLogin...");

		String givenPassword = dto.getPassword();
		loginTime = LocalTime.now();
		logger.info("Logintime : {}", loginTime);
		Duration timeElapsed = Duration.between(mailsentTime, loginTime);
		long diffrencetime = timeElapsed.toMinutes();
		logger.info("Time taken : {}", diffrencetime + " minutes");

		if (diffrencetime <= 10 && temporaryPass != null && givenPassword.equals(temporaryPass)) {
			logger.info("givenPassword is correct");
			return true;
		} else {
			logger.info("givenPassword is Incorrect OR Time Out");
			return false;
		}

	}

	@Override
	public boolean generateOTP(HttpServletRequest request) {
		logger.info("invoked generateOTP in service...");

		try {
			String onetimepass = genarateRandomOTP();
			 temporaryPass = onetimepass;
			if (Objects.nonNull(onetimepass)) {
				logger.info("onetimepass generated in service...");
				String mailID = toMailId;
				String subject = toMailSubject;
				String body = "Hi X-workzodc" + "\n Your Xworkz Bulk Mail App, One Time Password is = " + onetimepass
						+ "\n Its' valid for 10 Minutes.";
				boolean mailvalidation = mailService.validateAndSendMailByMailId(mailID, subject, body);

				if (mailvalidation) {
					logger.info("success", "Your Password sent to your mailID");
					mailsentTime = LocalTime.now();
					logger.info("MailsentTime: {}", mailsentTime);
					return true;
				} else {
					logger.info("faild", "Password can't able send to your mailID!");
					return false;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return false;

	}

	@Override
	public String genarateRandomOTP() {
		String newRandomPassword = RandomStringUtils.randomNumeric(6);
		return newRandomPassword;
	}

}
