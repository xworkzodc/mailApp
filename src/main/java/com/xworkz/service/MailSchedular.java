package com.xworkz.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.xworkz.dto.Subscriber;

@Service("emailService")
public class MailSchedular {

	private Logger logger = LoggerFactory.getLogger(MailSchedular.class);

	@Autowired
	private SpringMailService emailService;
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${bdayMailSubject}")
	private String bdayMailSubject;
	@Value("${mailFrom}")
	private String mailFrom;

	public void birthadyMailSender() throws URISyntaxException, IOException {
		logger.info("Invoked birthadyMailSender");
		List<Subscriber> subcriberList = getListOfSubscribersFromExcel();

		for (Subscriber subscriber : subcriberList) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM");
			Date date = new Date();

			logger.info("local date " + formatter.format(date));
			logger.info("subscriber dob " + subscriber.getDob() + " Formated " + formatter.format(subscriber.getDob()));
			logger.info("Macthed DOB " + formatter.format(subscriber.getDob()).equals(formatter.format(date)));

			if (formatter.format(subscriber.getDob()).equals(formatter.format(date))) {
				Context context1 = new Context();
				context1.setVariable("subcriberName", subscriber.getFullName());

				String content = templateEngine.process("birthdayMailTemplate", context1);
				MimeMessagePreparator messagePreparator = mimeMessage -> {

					MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
					messageHelper.setFrom(mailFrom);
					messageHelper.setTo(subscriber.getEmailId());
					messageHelper.setSubject(bdayMailSubject);
					messageHelper.setText(content, true);
				};
				emailService.validateAndSendMailByMailId(messagePreparator);
			}
		}

	}

	@SuppressWarnings("resource")
	public List<Subscriber> getListOfSubscribersFromExcel() {
		List<Subscriber> subscribersList = new ArrayList<Subscriber>();
		try {
			int i = 0;
			URL url = new URL("https://github.com/xworkzodc/newsfeed/raw/master/xworkz.xlsx");
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();
			System.out.println("responseCode=" + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				Workbook workbook = null;
				logger.info("Staring..........");
				InputStream inputStream = httpConn.getInputStream();
				workbook = new XSSFWorkbook(inputStream);
				Sheet excelSheet = workbook.getSheetAt(0);
				logger.info("Last Row Number of Is Excel file: " + excelSheet.getLastRowNum());
				logger.info("Excel file Is opened");

				for (Row row : excelSheet) { // For each Row.

					Cell nameCell = row.getCell(0);
					Cell emailCell = row.getCell(1);
					Cell dobCell = row.getCell(2);

					subscribersList.add(new Subscriber(nameCell.getStringCellValue(), emailCell.getStringCellValue(), dobCell.getDateCellValue()));
					logger.info("No: " + (++i) + " Value: " + nameCell.getStringCellValue()
							+ " Data Is Read and Stored in List");
					logger.info(subscribersList.toString());
				}
			} else {
				logger.info("responseCode=" + responseCode);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return subscribersList;

	}

}