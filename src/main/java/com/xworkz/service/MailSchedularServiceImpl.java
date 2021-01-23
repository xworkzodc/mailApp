package com.xworkz.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
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
import com.xworkz.util.EncryptionHelper;
import com.xworkz.util.ExcelFileColumn;
import com.xworkz.util.MailSchedularConstants;

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
	@Value("${ccmailID}")
	private String[] ccmailID;
	@Value("${bdayMailreportSubject}")
	private String reportSubject;
	private String mailId;
	@Autowired
	private EncryptionHelper encryptionHelper;

	public void birthadyMailSender() throws URISyntaxException, IOException {
		logger.info("Invoked birthadyMailSender in service");

		try {
			int totalMailsent = 0;
			boolean flag = false;
			Date today = new Date();
			List<Subscriber> subcriberList = getListOfSubscribersFromExcel();

			if (Objects.nonNull(subcriberList)) {
				for (Subscriber subscriber : subcriberList) {
					Integer originaldate = (int) ((subscriber.getDob())
							- MailSchedularConstants.ExcelCell_Dycription_value);
					String originalStringDate = originaldate.toString();
					//logger.info("originaldate {}", originalStringDate);

					originalStringDate = validateDOBLength(originaldate, originalStringDate);

					SimpleDateFormat formatter1 = new SimpleDateFormat(MailSchedularConstants.SimpleDateReadFormat_value);
					Date date = formatter1.parse(originalStringDate);
					SimpleDateFormat formatter2 = new SimpleDateFormat(MailSchedularConstants.SimpleDateWriteFormat_value);
					String formatedTodayDate = formatter2.format(today);
					//logger.info("local date {}", formatedTodayDate);

					String formatedDob = formatter2.format(date);
					
					LocalDate dob = new java.sql.Date(date.getTime()).toLocalDate();
					//logger.info("custom formated originaldate date {}", formatedDob);

					 if(Objects.nonNull(formatedDob) && Objects.nonNull(formatedTodayDate)) {
					if ((formatedTodayDate).equals(formatedDob)) {
						flag = true;
						totalMailsent = extractedAndEmailSending(subscriber, formatedTodayDate, formatedDob ,dob);	
					}
				}

				}
				if (flag == false) {
					String massage = "No birthday found for today's date";
					logger.info("No birthday found for today's date {}", today);
					sendReportMail(massage, today);
					logger.info("Report Mail sent");
				}		
				else {
					  String massage = "Total birthday mails sent";
					  logger.info("Total birthday mails sent {}", totalMailsent);
					  //sendReportMail(massage, totalMailsent);
				}

			} else {
				logger.info("subcriberList from getListOfSubscribersFromExcel Is null");
			}
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		}

	}

	

	private String validateDOBLength(Integer originaldate, String originalStringDate) {
		Integer length = originalStringDate.length();
		 if(Objects.nonNull(length) && Objects.nonNull(originaldate) ) {
		 if (length.equals(7)) {
			DecimalFormat decimalFormat = new DecimalFormat(MailSchedularConstants.DecimalFormat_value);
			String converted = decimalFormat.format(originaldate);
			originalStringDate = converted;
			//logger.info("originaldate value={}", originaldate);
			//logger.info("updated value= {}", originalStringDate);
		  }
		 }else {
			logger.info("originalStringDate length Is null");
		}
		return originalStringDate;
	}
	
	

	public int j = 0;
	private int extractedAndEmailSending(Subscriber subscriber, String formatedTodayDate, String formatedDob, LocalDate date) {
		if (Objects.nonNull(subscriber) && Objects.nonNull(formatedTodayDate) && Objects.nonNull(formatedDob)) {
            logger.info("no= {} subscriber = {} Macthed DOB = {}",(++j),subscriber.getFullName(),(formatedTodayDate).equals(formatedDob));
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			ResponseEntity<String> responseEntity = restTemplate.exchange(imagesJsonlink, HttpMethod.GET, entity,
					String.class);
			if (Objects.nonNull(responseEntity)) {
				String data = responseEntity.getBody();

				JSONObject jsonObject = new JSONObject(data);
				JSONArray arrayList = jsonObject.toJSONArray(jsonObject.names());
				Random random = new Random();
				Integer randomInt = random.nextInt(arrayList.length());
				Object imageLink = arrayList.get(randomInt);
				//logger.info("birthday image link= {}", imageLink);
				int age = getTheCurrentAge(date);
                
				Context context1 = new Context();
				context1.setVariable("subcriberName", subscriber.getFullName());
				context1.setVariable("imageLink", imageLink);
				context1.setVariable("dob", date);
				context1.setVariable("agePast", age-1);
				context1.setVariable("agePresent", age);

				if (Objects.nonNull(context1)) {
					String content = templateEngine.process("birthdayMailTemplate", context1);

					mailId = subscriber.getEmailId();
					logger.info("subscriber mailID {} ", mailId);
					if (Objects.nonNull(mailId)) {
						if (mailId.contains(MailSchedularConstants.Gmail_value_placer)) {
							mailId = mailId.replace(MailSchedularConstants.Gmail_value_placer,MailSchedularConstants.Gmail_value_Replacer);

							if (mailId.contains(MailSchedularConstants.Outlook_value_placer)) {
								mailId = mailId.replace(MailSchedularConstants.Outlook_value_placer,MailSchedularConstants.Outlook_value_Replacer);

								if (mailId.contains(MailSchedularConstants.Yahoo_value_placer)) {
									mailId = mailId.replace(MailSchedularConstants.Yahoo_value_placer,MailSchedularConstants.Yahoo_value_Replacer);
								}
							}
						}

						  MimeMessagePreparator messagePreparator = mimeMessage -> {

							MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
							messageHelper.setFrom(mailFrom);
							messageHelper.setCc(ccmailID);
							messageHelper.setTo(mailId);
							messageHelper.setSubject(bdayMailSubject);
							messageHelper.setText(content, true);

						};

						if (Objects.nonNull(messagePreparator))
							emailService.validateAndSendMailByMailId(messagePreparator);
					} else {
						logger.info("subscriber mail is empty {}", subscriber.getFullName());
					}
				}

			}
		} else {
			logger.info("got null object in extractedAndEmailSending");
		}
		return j;
	}
	
	
	private void sendReportMail(String message, Object object) {
		try {
			Context context = new Context();
			context.setVariable("reportMasseage", message);
			context.setVariable("reportTotal", object);
			
			if (Objects.nonNull(context)) {
				String content = templateEngine.process("dailyMailReport", context);
				MimeMessagePreparator messagePreparator = mimeMessage -> {

					MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
					messageHelper.setFrom(mailFrom);
					messageHelper.setCc(ccmailID);
					messageHelper.setTo(mailFrom);
					messageHelper.setSubject(reportSubject);
					messageHelper.setText(content, true);

				};

				if (Objects.nonNull(messagePreparator)) 
					emailService.validateAndSendMailByMailId(messagePreparator);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error is {} and Message is {}", e, e.getMessage());
		}
		
		
	}
	
	private int getTheCurrentAge(LocalDate givenDate){
		Period period = Period.between(givenDate, LocalDate.now());
		int age=period.getYears();
		return age;
	}


	public List<Subscriber> getListOfSubscribersFromExcel() throws IOException {
		List<Subscriber> subscribersList = new ArrayList<Subscriber>();
		Workbook workbook = null;
		ByteArrayInputStream inputStream = null;
		try {
			int i = 0;
			URI url = new URI(encryptionHelper.decrypt(excelFilelink));
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
			if (Objects.nonNull(responseEntity)) {
				byte[] result = responseEntity.getBody();

				logger.info("Staring..........");
				inputStream = new ByteArrayInputStream(result);
				if (Objects.nonNull(inputStream)) {
					workbook = new XSSFWorkbook(inputStream);
					Sheet excelSheet = workbook.getSheetAt(0);
					logger.info("Last Row Number of Is Excel file {} ", excelSheet.getPhysicalNumberOfRows());
					logger.info("Excel file Is opened");

					if (Objects.nonNull(excelSheet)) {
						for (Row row : excelSheet) { // For each Row.
                            
							if(row !=null) {
								
							Cell nameCell = row.getCell(ExcelFileColumn.ExcelFile_NAME_CELL);
							nameCell.setCellType(CellType.STRING);
							nameCell=row.getCell(ExcelFileColumn.ExcelFile_NAME_CELL, MissingCellPolicy.RETURN_BLANK_AS_NULL);
							
							Cell emailCell = row.getCell(ExcelFileColumn.ExcelFile_EMAIL_CELL);
							emailCell.setCellType(CellType.STRING);
							emailCell=row.getCell(ExcelFileColumn.ExcelFile_EMAIL_CELL, MissingCellPolicy.RETURN_BLANK_AS_NULL);
							
							Cell dobCell = row.getCell(ExcelFileColumn.ExcelFile_DOB_CELL);
							dobCell.setCellType(CellType.NUMERIC);
							dobCell=row.getCell(ExcelFileColumn.ExcelFile_DOB_CELL, MissingCellPolicy.RETURN_BLANK_AS_NULL);
							
							if(nameCell !=null || emailCell !=null || dobCell !=null){
							subscribersList.add(new Subscriber(nameCell.getStringCellValue(),emailCell.getStringCellValue(), dobCell.getNumericCellValue()));
							//logger.info("No: {} Value: {} Data Is Read and Stored in List", (++i),nameCell.getStringCellValue());
                            ++i;
							}
							else {
							logger.info("Cell Data is empty in getListOfSubscribersFromExcel in Row:{}", (++i));
							}
							
							}
							else {
							logger.info("Row: {} Data is empty in getListOfSubscribersFromExcel", (++i));
							}
						}
						logger.info("Total: {} Data Is Read and Stored in List", i);
						subscribersList.remove(0);
					}
				}
			} else {
				logger.info("responseEntity null in getListOfSubscribersFromExcel");
			}
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