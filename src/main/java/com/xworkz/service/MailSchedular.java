package com.xworkz.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	final String fileName = "./xworkz.xlsx";

	@Autowired
	private SpringMailService emailService;
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${bdayMailSubject}")
	private String bdayMailSubject;
	@Value("${mailFrom}")
	private String mailFrom;

	public void birthadyMailSender() {
		logger.info("In birthadyMailSender");
		List<Subscriber> subcriberList = getListOfSubscribersFromExcel();

		for (Subscriber subscriber : subcriberList) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date date = new Date();
			
			 // logger.info("local date " + formatter.format(date));
			 // logger.info("subscriber dob " + subscriber.getDob() + " Formated " + formatter.format(subscriber.getDob()));
			 // logger.info("Mathed DOB " +formatter.format(subscriber.getDob()).equals(formatter.format(date)));
			 
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

	public List<Subscriber> getListOfSubscribersFromExcel() {

		Workbook workbook = null;
		List<Subscriber> subscribersList = new ArrayList<Subscriber>();
		logger.info("Staring..........");
		int i = 0;
		try (FileInputStream inputStream = new FileInputStream(new File(fileName))) {
			workbook = new XSSFWorkbook(inputStream);
			Sheet excelSheet = workbook.getSheetAt(0);
			logger.info("Last Row Number Is: " + excelSheet.getLastRowNum());
			logger.info("Excel file Is opened");

			for (Row row : excelSheet) { // For each Row.
				Cell dobCell = row.getCell(0); // Get the Cell at the Index / Column you want.
				Cell nameCell = row.getCell(1);
				Cell emailCell = row.getCell(2);
				subscribersList.add(new Subscriber(dobCell.getDateCellValue(), nameCell.getStringCellValue(),
						emailCell.getStringCellValue()));
				logger.info("No: " + (++i) + " Value: " + nameCell.getStringCellValue() + "Is Readed and Stored in List");
				logger.info(subscribersList.toString());
			}
		} catch (FileNotFoundException e) {
			logger.error("File " + fileName + " Is Not Found");
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return subscribersList;
	}

}