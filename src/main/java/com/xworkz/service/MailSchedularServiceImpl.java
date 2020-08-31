package com.xworkz.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import com.xworkz.dto.Subscriber;
import com.xworkz.util.ExcelFileColumn;

@Service("emailService")
public class MailSchedularServiceImpl implements MailSchedularService{

	private Logger logger = LoggerFactory.getLogger(MailSchedularServiceImpl.class);

	@Autowired
	private SpringMailService emailService;
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${bdayMailSubject}")
	private String bdayMailSubject;
	@Value("${mailFrom}")
	private String mailFrom;
	@Value("${excelFilelink}")
	private String excelFilelink;

	public void birthadyMailSender() throws URISyntaxException, IOException {
		logger.info("Invoked birthadyMailSender in service");
		List<Subscriber> subcriberList = getListOfSubscribersFromExcel();

		for (Subscriber subscriber : subcriberList) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM");
			Date date = new Date();

			logger.info("local date {}",formatter.format(date));
			logger.info( "Subcriber dob Formated {}",formatter.format(subscriber.getDob()));
			
			if (formatter.format(subscriber.getDob()).equals(formatter.format(date))) {
				logger.info("subscriber = {} Macthed DOB = {}" ,subscriber.getFullName(),formatter.format(subscriber.getDob()).equals(formatter.format(date)));
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
		List<Subscriber> subscribersList = new ArrayList<Subscriber>();
		try {
			int i = 0;
			URI url = new URI(excelFilelink);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
			byte[] result = responseEntity.getBody();
			
				Workbook workbook = null;
				logger.info("Staring..........");
				ByteArrayInputStream inputStream = new ByteArrayInputStream(result);
				workbook = new XSSFWorkbook(inputStream);
				Sheet excelSheet = workbook.getSheetAt(0);
				logger.info("Last Row Number of Is Excel file {} ", excelSheet.getLastRowNum());
				logger.info("Excel file Is opened");
				
				for (Row row : excelSheet) { // For each Row.

					Cell nameCell = row.getCell(ExcelFileColumn.ExcelFile_NAME_CELL);
					Cell emailCell = row.getCell(ExcelFileColumn.ExcelFile_EMAIL_CELL);
					Cell dobCell = row.getCell(ExcelFileColumn.ExcelFile_DOB_CELL);

					subscribersList.add(new Subscriber(nameCell.getStringCellValue(), emailCell.getStringCellValue(), dobCell.getDateCellValue()));
					logger.debug("No: {} Value: {} Data Is Read and Stored in List",(++i),nameCell.getStringCellValue());
				}
				inputStream.close();
				workbook.close();
			} 
	        catch (IOException | URISyntaxException e) {
			logger.error(e.getMessage());
		}
		return subscribersList;

	}

}