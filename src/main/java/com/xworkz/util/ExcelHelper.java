package com.xworkz.util;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import com.xworkz.dto.EnquiryDTO;

@Component
public class ExcelHelper {

	static Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

	public ExcelHelper() {
		logger.info("{} is created", this.getClass().getSimpleName());
	}

	public List<String> getContactListFromInputStream(InputStream inputStream) {
		List<String> mobileNumList = new ArrayList<String>();
		try {
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet excelSheet = workbook.getSheetAt(0);
			logger.info("Last Row Number Is: " + excelSheet.getLastRowNum());
			logger.info("Excel file Is opened");
			// logger.info("Size of Excel is {}", excelSheet.getLastRowNum());
			Row row;
			for (int i = 0; i <= excelSheet.getLastRowNum(); i++) {
				row = (Row) excelSheet.getRow(i);
				logger.debug("Data type is {}", row.getCell(0).getCellType());
				if (row.getCell(0).getCellType() == 0) {
					Long long1 = (long) row.getCell(0).getNumericCellValue();
					logger.info("Data: {} is read and stored.", long1);
					mobileNumList.add(String.valueOf(long1));
				}
			}
		} catch (Exception e) {
			logger.error("error is {} and message is {}", e, e.getMessage());
		}
		return mobileNumList;
	}

	public List<EnquiryDTO> getEnquiryListFromInputStream(InputStream inputStream) {
		List<EnquiryDTO> enquiryList = new ArrayList<EnquiryDTO>();
		try {
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet excelSheet = workbook.getSheetAt(0);
			logger.info("Last Row Number Is: " + excelSheet.getLastRowNum());
			logger.info("Excel file Is opened");
			
			if (Objects.nonNull(excelSheet)) {
				DataFormatter dataFormatter = new DataFormatter();
				excelSheet.forEach(row -> {
					EnquiryDTO enquiryDTO = new EnquiryDTO();
					
					enquiryDTO.setFullName(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.NAME,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setMobileNo(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.MOBILE,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setAlternateMobileNo(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.ALTERNATE_MOBILE,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setEmailId(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.EMAIL_ID,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setCourse(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.COURSE,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setBatchType(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.BATCH,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setSource(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.SOURCE,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setRefrenceName(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.REFRENCE,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setRefrenceMobileNo(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.REFRENCE_MOBILE,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setBranch(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.BRANCH,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setComments(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.COMMENTS,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
					enquiryDTO.setCounselor(dataFormatter.formatCellValue(row.getCell(EnquiryExcelFileColumn.COUNSELOR,MissingCellPolicy.RETURN_BLANK_AS_NULL)));
 
					if (Objects.nonNull(enquiryDTO.getFullName()) && Objects.nonNull(enquiryDTO.getEmailId()) && Objects.nonNull(enquiryDTO.getMobileNo())) {
						enquiryList.add(enquiryDTO);
					}
					else {
						logger.info("Fields Has empty values in Excel at Row:"+row.getRowNum());
					}
					
				});
				enquiryList.remove(0);
			} else {
				logger.error("ExcelSheet is Empty!");
			}
		} catch (Exception e) {
			logger.error("error is {} and message is {}", e, e.getMessage());
		}
		return enquiryList;
	}
}
