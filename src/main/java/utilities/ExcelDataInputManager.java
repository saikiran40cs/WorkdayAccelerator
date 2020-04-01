/*
All the code that follow is Copyright (c) 2018, Sai Kiran Nataraja. All Rights Reserved.
Not for reuse without permission.
*/

package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;

import reusableLibrary.PathConstants;


public class ExcelDataInputManager {
	File file;
	FileInputStream fis;
	private XSSFWorkbook wb;
	private XSSFSheet sh;

	/**
	 * Constructor used for excel input sheet
	 * 
	 * @param fileName
	 * @param sheetName
	 * @throws IOException
	 */
	public ExcelDataInputManager(String fileName, String sheetName) throws IOException {
		Reporter.log(fileName, false);
		File file = new File(fileName);
		fis = new FileInputStream(file);
		wb = (XSSFWorkbook) WorkbookFactory.create(fis);
		sh = wb.getSheet(sheetName);
	}

	/**
	 * Function to find whether the business process is runnable or not if there are merged regions
	 * @author saikirannataraja
	 * @return
	 */
	public boolean isBusinessProcessRunnable(String businessProcessName) {
		XSSFRow row = sh.getRow(0);
		boolean isBusinessProcessExecutable = false;
		// Iterate through each rows one by one
		for(int rowNumber = 0; rowNumber < sh.getLastRowNum(); rowNumber++) {
			row = (XSSFRow) sh.getRow(rowNumber);
		    for(int columnNumber = 0; columnNumber < row.getLastCellNum(); columnNumber++) {
				Reporter.log(getExtractedValue(rowNumber,1),PathConstants.debugMode);
				switch (sh.getRow(rowNumber).getCell(1).getStringCellValue()) {
				case PathConstants.BUSPROCESS_SHEET_TENANTTOBESELECTED:
					PathConstants.pageUrl = getExtractedValue(rowNumber,2);
					break;
				case PathConstants.BUSPROCESS_SHEET_USERNAMETOBEUSEDFORRUN:
					PathConstants.wUsername = getExtractedValue(rowNumber,2);
					break;
				case PathConstants.BUSPROCESS_SHEET_EXPLICITWAITTIMESELECTION:
					PathConstants.minWaitTime = Integer.parseInt(getExtractedValue(rowNumber,2));
					PathConstants.midWaitTime = PathConstants.midWaitTime + PathConstants.minWaitTime;
					PathConstants.maxWaitTime = PathConstants.midWaitTime + PathConstants.minWaitTime;
					break;
				case PathConstants.BUSPROCESS_SHEET_REGIONSELECTION:
					PathConstants.wCountry = getExtractedValue(rowNumber,2);
					break;
				}
				if (getExtractedValue(rowNumber,2).equalsIgnoreCase(PathConstants.YES) && getExtractedValue(rowNumber,1).equalsIgnoreCase(businessProcessName)) {
					Reporter.log("Yes" + getExtractedValue(rowNumber,1),PathConstants.debugMode);
					isBusinessProcessExecutable = true;
					break;
				}
				Reporter.log(getExtractedValue(rowNumber,1) + " "+ getExtractedValue(rowNumber,2), PathConstants.debugMode);
			}
			if (isBusinessProcessExecutable)
			break;
		}
		if (isBusinessProcessExecutable) {
			ExcelDataInputManager readScData = null;
			try {
				readScData = new ExcelDataInputManager(PathConstants.BUSPROCESS_INPUTFILE,
						PathConstants.DEFINITIONS_SHEETNAME);
				int dataDefRecordCount = readScData.getDataRowsCount();
				for (int i = 1; i <= dataDefRecordCount; i++) {
					LinkedHashMap<String, String> dataMap = readScData.getRowRecord(i);
					if (PathConstants.pageUrl.equalsIgnoreCase(dataMap.get("TenantName"))) {
						Reporter.log(dataMap.get("TenantName") + " " + dataMap.get("TenantURL"),
								PathConstants.debugMode);
						PathConstants.pageUrl = dataMap.get("TenantURL");
						break;
					}
				}
				for (int i = 1; i <= dataDefRecordCount; i++) {
					LinkedHashMap<String, String> dataMap = readScData.getRowRecord(i);
					if (PathConstants.wUsername.equalsIgnoreCase(dataMap.get("LoginUserName"))) {
						Reporter.log(dataMap.get("LoginUserName") + " " + dataMap.get("LoginPassphrase"),
								PathConstants.debugMode);
						PathConstants.wUsername = dataMap.get("LoginUserName");
						PathConstants.wPassword = dataMap.get("LoginPassphrase");
						return true;
					}
				}
			} catch (IOException e) {
			}
		}
		return isBusinessProcessExecutable;
	}
	
	/**
	 * Function to find whether the business process is runnable or not if there are merged regions
	 * @deprecated
	 * @author saikirannataraja
	 * @return
	 */
	public boolean isBusinessProcessRunnableForMergedCells(String businessProcessName) {
		XSSFRow row = sh.getRow(0);
		boolean isBusinessProcessExecutable = false;
		int scenarioRowNumber = 0;
		// Iterate through each rows one by one
		Iterator<Row> rowIterator = sh.iterator();
		while (rowIterator.hasNext()) {
			scenarioRowNumber = scenarioRowNumber + 1;
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			outer: while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				// will iterate over the Merged cells
				for (int i = 0; i < sh.getNumMergedRegions(); i++) {
					CellRangeAddress region = sh.getMergedRegion(i); // Region of merged cells
					int rowNum = region.getFirstRow(); // number of rows merged
					int colIndex = region.getFirstColumn(); // number of columns merged
					// check first cell of the region
					if (rowNum == cell.getRowIndex() && colIndex == cell.getColumnIndex()) {
						Reporter.log(getExtractedValue(rowNum,1),PathConstants.debugMode);
						switch (sh.getRow(rowNum).getCell(1).getStringCellValue()) {
						case PathConstants.BUSPROCESS_SHEET_TENANTTOBESELECTED:
							PathConstants.pageUrl = getExtractedValue(rowNum,2);
							break;
						case PathConstants.BUSPROCESS_SHEET_USERNAMETOBEUSEDFORRUN:
							PathConstants.wUsername = getExtractedValue(rowNum,2);
							break;
						case PathConstants.BUSPROCESS_SHEET_EXPLICITWAITTIMESELECTION:
							PathConstants.minWaitTime = Integer.parseInt(getExtractedValue(rowNum,2));
							PathConstants.midWaitTime = PathConstants.midWaitTime + PathConstants.minWaitTime;
							PathConstants.maxWaitTime = PathConstants.midWaitTime + PathConstants.minWaitTime;
							break;
						case PathConstants.BUSPROCESS_SHEET_REGIONSELECTION:
							PathConstants.wCountry = getExtractedValue(rowNum,2);
							break;
						}
						if (getExtractedValue(rowNum,2).equalsIgnoreCase(PathConstants.YES) && getExtractedValue(rowNum,1).equalsIgnoreCase(businessProcessName)) {
							Reporter.log("Yes" + getExtractedValue(rowNum,1),PathConstants.debugMode);
							isBusinessProcessExecutable = true;
							break;
						}
						Reporter.log(getExtractedValue(rowNum,1) + " "+ getExtractedValue(rowNum,2), PathConstants.debugMode);
						continue outer;
					}
					if (isBusinessProcessExecutable)
						break;
				}
			}
			if (isBusinessProcessExecutable)
				break;
		}
		if (isBusinessProcessExecutable) {
			ExcelDataInputManager readScData = null;
			try {
				readScData = new ExcelDataInputManager(PathConstants.BUSPROCESS_INPUTFILE,
						PathConstants.DEFINITIONS_SHEETNAME);
				int dataDefRecordCount = readScData.getDataRowsCount();
				for (int i = 1; i <= dataDefRecordCount; i++) {
					LinkedHashMap<String, String> dataMap = readScData.getRowRecord(i);
					if (PathConstants.pageUrl.equalsIgnoreCase(dataMap.get("TenantName"))) {
						Reporter.log(dataMap.get("TenantName") + " " + dataMap.get("TenantURL"),
								PathConstants.debugMode);
						PathConstants.pageUrl = dataMap.get("TenantURL");
						break;
					}
				}
				for (int i = 1; i <= dataDefRecordCount; i++) {
					LinkedHashMap<String, String> dataMap = readScData.getRowRecord(i);
					if (PathConstants.wUsername.equalsIgnoreCase(dataMap.get("LoginUserName"))) {
						Reporter.log(dataMap.get("LoginUserName") + " " + dataMap.get("LoginPassphrase"),
								PathConstants.debugMode);
						PathConstants.wUsername = dataMap.get("LoginUserName");
						PathConstants.wPassword = dataMap.get("LoginPassphrase");
						return true;
					}
				}
			} catch (IOException e) {
			}
		}
		return isBusinessProcessExecutable;
	}

	/**
	 * Function to get the row headers for the excel sheet
	 * 
	 * @return LinkedHashMap<String, String>
	 */
	private LinkedHashMap<String, Integer> getHeader() {
		LinkedHashMap<String, Integer> header = new LinkedHashMap<String, Integer>();
		XSSFRow row = sh.getRow(0);
		int celCount = row.getLastCellNum();
		for (int i = 0; i < celCount; i++) {
			Reporter.log(" Head " + row.getCell(i).getStringCellValue(), PathConstants.debugMode);
			header.put(row.getCell(i).getStringCellValue(), i);
		}
		return header;
	}

	/**
	 * Function to get the last row number from the excel sheet
	 * 
	 * @return int <Last row number>
	 */
	public int getDataRowsCount() {
		return sh.getLastRowNum();
	}

	/**
	 * Function to get the row record for the provided row number
	 * @author saikirannataraja
	 * @param dataRowNum
	 * @return LinkedHashMap<String, String>
	 */
	public LinkedHashMap<String, String> getRowRecord(int dataRowNum) {
		LinkedHashMap<String, String> rowData = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Integer> header = getHeader();
		//Usage of Lambda Expressions
		header.keySet().forEach((headerName) -> {
			String celValue = getExtractedValue(dataRowNum, (header.get(headerName)));
			Reporter.log("ColumnNumber: " + header.get(headerName) + "-" + headerName + ", ColumnValue :" + celValue,
					PathConstants.debugMode);
			rowData.put(headerName, celValue);
		}
				);
		return rowData;
	}

	/**
	 *Function to get extracted value
	 * @param dataRowNum
	 * @param cellNumber
	 * @return
	 */
	private String getExtractedValue(int dataRowNum, int cellNumber) {
		String celValue = "";
		try {
			Cell captureCelValue = sh.getRow(dataRowNum).getCell(cellNumber);
			Reporter.log("Cell Type is: "+ captureCelValue.getCellType(),PathConstants.debugMode);
			switch (captureCelValue.getCellType()) {
			case STRING:
				celValue = captureCelValue.getStringCellValue();
				break;
			case BLANK:
				celValue = "";
				break;
			case BOOLEAN:
				celValue = String.valueOf(captureCelValue.getBooleanCellValue());
				break;
			case FORMULA:
			case NUMERIC:
				celValue = String.valueOf(captureCelValue.getNumericCellValue());
				break;
				/**
				 if (DateUtil.isCellDateFormatted(captureCelValue)) { 
				 // format in form of
				 M/D/YY double d = captureCelValue.getNumericCellValue();
				 
				 Calendar cal = Calendar.getInstance(); cal.setTime(DateUtil.getJavaDate(d));
				 celValue = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
				 celValue =	 cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" +	 celValue;
				 }
				 */
			default:
				celValue = null;
				break;
			}
		} catch (Exception e) {			}
		return celValue;
	}

	/**
	 * Function to get the securityRole Data from the sheet passed
	 * Concept: Multiple key combinations get one value same as composite key in database
	 * @author saikirannataraja
	 * @param dataRowNum
	 * @return MultiKeyMap<String, String> 
	 */
	public MultiKeyMap<String, String> getSecurityRoleData() {
		MultiKeyMap<String, String> multiKeyMap = new MultiKeyMap<String, String>();
		int dataRowNum = getDataRowsCount();
		String securityRoleName = "", securityLocationName = "", securityRoleValue;
		for (int i = 1; i <= dataRowNum; i++) {
			securityRoleName = sh.getRow(i).getCell(0).getStringCellValue();
			securityLocationName = sh.getRow(i).getCell(1).getStringCellValue();
			securityRoleValue = sh.getRow(i).getCell(2).getStringCellValue();
			//Take ONLY FIRST occurrence of the ROLE & LOCATION NAME
			if (multiKeyMap.get(securityRoleName, securityLocationName) == null) {
				multiKeyMap.put(securityRoleName, securityLocationName, securityRoleValue);
				Reporter.log(
						"Security Role:" + securityRoleName + "- Security Location Name:" + securityLocationName
								+ ", Security Role Value:" + multiKeyMap.get(securityRoleName, securityLocationName),
						PathConstants.debugMode);
			}
		}
		return multiKeyMap;
	}

	
	/**
	 * Function to fetch the data definitions from a given sheet
	 * @author saikirannataraja
	 * @returns MultiKeyMap<String, String> 
	 */
	public MultiKeyMap<String, String> getDataDefntnsRecords() {
		MultiKeyMap<String, String> multiKeyMap = new MultiKeyMap<String, String>();
		XSSFRow row = sh.getRow(0);
		int rowCount = sh.getLastRowNum()+1;
		int celCount = row.getLastCellNum();
		String headerRowName, headerRowValue;
 		for (int defHeaders = 0; defHeaders < celCount; defHeaders++) {
			row = sh.getRow(0);
			headerRowName = row.getCell(defHeaders).getStringCellValue();
			Reporter.log(" Definition Headers: " + headerRowName, PathConstants.debugMode);
			for(int i=1; i<rowCount;i++) {
				headerRowValue = getExtractedValue(i, defHeaders);
				if(!headerRowValue.isBlank()) {
					Reporter.log(" Header values: " + headerRowValue, PathConstants.debugMode);
					multiKeyMap.put(headerRowName, headerRowValue,String.valueOf(i));
				}
			}
		}
		return multiKeyMap;
	}
	
	/**
	 * Function to check if a data row record exists
	 * @param defDataMap
	 * @param headerRowKey
	 * @param headerRowValue
	 * @return
	 */
	public boolean getRecordExists(MultiKeyMap<String, String> defDataMap,String headerRowKey, String headerRowValue) {
		boolean recordExists = false;
		try {
			if(defDataMap.get(headerRowKey,headerRowValue) != null) {
				recordExists = true;
			}
		}catch (Exception e) {
		}
		return recordExists;
	}
	
}
