package com.xworkz.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import com.xworkz.dto.Subscriber;
import com.xworkz.util.ExcelFileColumn;
import org.apache.poi.ss.usermodel.CellType;

@Service("emailService")
public class MailSchedularServiceImpl implements MailSchedularService {

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
	@Value("${imagesJsonlink}")
	private String imagesJsonlink;

	public void birthadyMailSender() throws URISyntaxException, IOException {
		logger.info("Invoked birthadyMailSender in service");

		try {
			List<Subscriber> subcriberList = getListOfSubscribersFromExcel();

			for (Subscriber subscriber : subcriberList) {   
				Integer originaldate = (int) ((subscriber.getDob()) - 599);
				String originalStringDate = originaldate.toString();
				logger.info("originaldate {}", originalStringDate);
				
				Integer length = originalStringDate.length();	
				if (length.equals(7)) {
					DecimalFormat decimalFormat = new DecimalFormat("00000000");
					String converted = decimalFormat.format(originaldate);
					originalStringDate = converted;
					logger.info("updated value= {}",originalStringDate);	
				}

				SimpleDateFormat formatter1 = new SimpleDateFormat("ddMMyyy");
				Date date = formatter1.parse(originalStringDate);
				Date today = new Date();
				SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM");
				String formatedTodayDate = formatter2.format(today);
				logger.info("local date {}", formatedTodayDate);
				
				String formatedDob = formatter2.format(date);
				logger.info("custom formated originaldate date {}", formatedDob);

				if ((formatedTodayDate).equals(formatedDob)) {
					logger.info("subscriber = {} Macthed DOB = {}", subscriber.getFullName(),(formatedTodayDate).equals(formatedDob));
					
					RestTemplate restTemplate = new RestTemplate();
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
					ResponseEntity<String> responseEntity = restTemplate.exchange(imagesJsonlink, HttpMethod.GET, entity, String.class);
					String data = responseEntity.getBody();

					JSONObject jsonObject = new JSONObject(data);
				    JSONArray arrayList = jsonObject.toJSONArray(jsonObject.names());				
					Random random = new Random();
					Integer randomInt =  random.nextInt(arrayList.length());
					Object imageLink = arrayList.get(randomInt);
					logger.info("birthday image link= {}",imageLink);
					

					Context context1 = new Context();
					context1.setVariable("subcriberName", subscriber.getFullName());
					context1.setVariable("imageLink", imageLink);
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
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		}

	}

	public List<Subscriber> getListOfSubscribersFromExcel() throws IOException {
		List<Subscriber> subscribersList = new ArrayList<Subscriber>();
		Workbook workbook = null;
		ByteArrayInputStream inputStream = null;
		try {
			int i = 0;
			URI url = new URI(excelFilelink);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
			byte[] result = responseEntity.getBody();

			logger.info("Staring..........");
			inputStream = new ByteArrayInputStream(result);
			workbook = new XSSFWorkbook(inputStream);
			Sheet excelSheet = workbook.getSheetAt(0);
			logger.info("Last Row Number of Is Excel file {} ", excelSheet.getLastRowNum());
			logger.info("Excel file Is opened");

			for (Row row : excelSheet) { // For each Row.

				Cell nameCell = row.getCell(ExcelFileColumn.ExcelFile_NAME_CELL);
                                nameCell.setCellType(CellType.STRING);
				Cell emailCell = row.getCell(ExcelFileColumn.ExcelFile_EMAIL_CELL);
                                emailCell.setCellType(CellType.STRING);
				Cell dobCell = row.getCell(ExcelFileColumn.ExcelFile_DOB_CELL);
                                dobCell.setCellType(CellType.NUMERIC);
                
				subscribersList.add(new Subscriber(nameCell.getStringCellValue(), emailCell.getStringCellValue(),dobCell.getNumericCellValue()));
				logger.info("No: {} Value: {} Data Is Read and Stored in List", (++i), nameCell.getStringCellValue());
			}
                 subscribersList.remove(0);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		}

		finally {
			inputStream.close();
			workbook.close();
		}

		return subscribersList;

	}

}
