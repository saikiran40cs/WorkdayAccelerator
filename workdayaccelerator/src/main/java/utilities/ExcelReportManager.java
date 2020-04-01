/*
All the code that follow is Copyright (c) 2018, Sai Kiran Nataraja. All Rights Reserved.
Not for reuse without permission.
*/
package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;

import com.google.common.base.Function;

import reusableLibrary.PathConstants;

public class ExcelReportManager {

	// Create an Report in excel
	XSSFWorkbook workbook;
	XSSFSheet sheet;

	/**
	 * Function to generate excel report based on the test scripts
	 * 
	 * @author saikiran.nataraja
	 * @return
	 * @throws Exception
	 */
	public void initializeExcelReport(String path) throws IOException {
		File reportFile = new File(path);
		int rowNum = 0;
		if (reportFile.exists()) {
			Reporter.log("reportFile exists");
		} else {
			// Create an Report in excel
			workbook = new XSSFWorkbook();
			// Create an excel in the prescribed format
			FileOutputStream writeXLOutput = null;
			try {
				XSSFCellStyle topHeaderContents = workbook.createCellStyle();
				XSSFCellStyle topHeaderRightContents = workbook.createCellStyle();
				XSSFCellStyle tableHeader = workbook.createCellStyle();
				XSSFCellStyle tableContentsOnLeft = workbook.createCellStyle();
				XSSFCellStyle tableContentsOnRight = workbook.createCellStyle();
//				CreationHelper createHelper = workbook.getCreationHelper();
//				topHeaderContents.setDataFormat(createHelper.createDataFormat().getFormat(
//		                "dd/MM/yyyy hh:mm:ss"));
				XSSFRow row = null;
				// Header Column Names
				XSSFCell celNoOfTCPassed;
				XSSFCell celNoOfTCFailed;
				XSSFCell celNoOfTCNotExecuted;
				String HeaderTitle = PathConstants.TESTREPORT_HEADERTITLE;
				
				// Header Column Names
				XSSFCell celExecScriptName;
				XSSFCell celTestStatus;
				XSSFCell celExecStartTime;
				XSSFCell celExecEndTime;
				XSSFCell celExecDuration;
				XSSFCell celComments;
				XSSFCell celLinkToHTMLResults;
				// Set the Font details for the entire sheet
				XSSFFont defaultFont;
				XSSFFont headerFont;
				CreationHelper createHelper = workbook.getCreationHelper();
				XSSFCellStyle hlinkstyle = workbook.createCellStyle();
				XSSFFont hlinkfont = workbook.createFont();
				hlinkfont.setUnderline(XSSFFont.U_SINGLE);
				hlinkfont.setColor(IndexedColors.BLUE.getIndex());
				hlinkstyle.setFont(hlinkfont);

				headerFont = workbook.createFont();
				headerFont.setFontHeightInPoints((short) 11);
				headerFont.setFontName("Calibri");
				headerFont.setColor(IndexedColors.WHITE.getIndex());
				headerFont.setBold(true);
				headerFont.setItalic(false);

				defaultFont = workbook.createFont();
				defaultFont.setFontHeightInPoints((short) 11);
				defaultFont.setFontName("Calibri");
				defaultFont.setColor(IndexedColors.BLACK.getIndex());
				defaultFont.setBold(true);
				defaultFont.setItalic(false);

				// create style for cells in header row
				topHeaderContents.setFont(headerFont);
				topHeaderContents.setFillPattern(FillPatternType.NO_FILL);
				topHeaderContents.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
				topHeaderContents.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				// Set the border style for the workbook
				topHeaderContents.setAlignment(HorizontalAlignment.LEFT);
				
				topHeaderRightContents.setFont(headerFont);
				topHeaderRightContents.setFillPattern(FillPatternType.NO_FILL);
				topHeaderRightContents.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
				topHeaderRightContents.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				// Set the border style for the workbook
				topHeaderRightContents.setAlignment(HorizontalAlignment.RIGHT);
				
				// create style for cells in header row
				tableHeader.setFont(defaultFont);
				tableHeader.setFillPattern(FillPatternType.NO_FILL);
				tableHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				tableHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				// Set the border style for the workbook
				tableHeader.setBorderBottom(BorderStyle.THIN);
				tableHeader.setBorderLeft(BorderStyle.THIN);
				tableHeader.setBorderRight(BorderStyle.THIN);
				tableHeader.setBorderTop(BorderStyle.THIN);
				tableHeader.setAlignment(HorizontalAlignment.LEFT);

				// create style for cells in table contents
				tableContentsOnRight.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
				tableContentsOnRight.setFillPattern(FillPatternType.NO_FILL);
				tableContentsOnRight.setWrapText(false);
				// Set the border style for the workbook
				tableContentsOnRight.setBorderBottom(BorderStyle.THIN);
				tableContentsOnRight.setBorderLeft(BorderStyle.THIN);
				tableContentsOnRight.setBorderRight(BorderStyle.THIN);
				tableContentsOnRight.setBorderTop(BorderStyle.THIN);
				tableContentsOnRight.setAlignment(HorizontalAlignment.RIGHT);

				// create style for cells in table contents
				tableContentsOnLeft.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
				tableContentsOnLeft.setFillPattern(FillPatternType.NO_FILL);
				// tableContentsOnLeft.setWrapText(false);
				// Set the border style for the workbook
				tableContentsOnLeft.setBorderBottom(BorderStyle.THIN);
				tableContentsOnLeft.setBorderLeft(BorderStyle.THIN);
				tableContentsOnLeft.setBorderRight(BorderStyle.THIN);
				tableContentsOnLeft.setBorderTop(BorderStyle.THIN);
				tableContentsOnLeft.setAlignment(HorizontalAlignment.LEFT);

				// Create worksheet for individual test case level details
				sheet = workbook.createSheet(PathConstants.TESTREPORT_SHEETNAME);
				XSSFCellStyle hiddenstyle = workbook.createCellStyle();
				hiddenstyle.setHidden(true);
				row = sheet.createRow(rowNum++);
				// Header Column Names
				celExecScriptName = row.createCell(0);
				// Merges the cells
				CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 1);
				
				celExecScriptName.setCellValue(HeaderTitle);
				celExecScriptName.setCellStyle(topHeaderContents);
				sheet.addMergedRegion(cellRangeAddress);

				row = sheet.createRow(rowNum++);
				celExecScriptName = row.createCell(0);
				celExecScriptName.setCellValue("");

				String formulaToGetCount = "B10:B100";
				row = sheet.createRow(rowNum++);
				
				celNoOfTCPassed = row.createCell(0);
				celTestStatus = row.createCell(1);
				celNoOfTCPassed.setCellValue("Number of Test cases passed");
				celTestStatus.setCellFormula("COUNTIFS('TestSuite Report'!" + formulaToGetCount + ",\"PASSED\")");
				celNoOfTCPassed.setCellStyle(tableContentsOnLeft);
				celTestStatus.setCellStyle(tableContentsOnRight);

				row = sheet.createRow(rowNum++);
				celNoOfTCFailed = row.createCell(0);
				celTestStatus = row.createCell(1);
				celNoOfTCFailed.setCellValue("Number of Test cases failed");
				celTestStatus.setCellFormula("COUNTIFS('TestSuite Report'!" + formulaToGetCount + ",\"FAILED\")");
				celNoOfTCFailed.setCellStyle(tableContentsOnLeft);
				celTestStatus.setCellStyle(tableContentsOnRight);

				row = sheet.createRow(rowNum++);
				celNoOfTCNotExecuted = row.createCell(0);
				celTestStatus = row.createCell(1);
				celNoOfTCNotExecuted.setCellValue("Number of Test cases Not Executed");
				celTestStatus.setCellFormula("COUNTIFS('TestSuite Report'!" + formulaToGetCount + ",\"NOT EXECUTED\")");
				celNoOfTCNotExecuted.setCellStyle(tableContentsOnLeft);
				celTestStatus.setCellStyle(tableContentsOnRight);
				
				
				row = sheet.createRow(rowNum++);
				celExecScriptName = row.createCell(0);
				celTestStatus = row.createCell(1);
				celExecScriptName.setCellValue("TOTAL");
				celTestStatus.setCellFormula("SUM(B3:B5)");
				celExecScriptName.setCellStyle(topHeaderContents);
				celTestStatus.setCellStyle(topHeaderRightContents);

				// Individual test case level details
				workbook.setActiveSheet(0);

				row = sheet.createRow(rowNum++);
				celExecScriptName = row.createCell(0);
				celExecScriptName.setCellValue("");

				row = sheet.createRow(rowNum++);
				// Header Column Names
				cellRangeAddress = new CellRangeAddress(1, 1, 0, 1);
				celExecScriptName = row.createCell(0);
				celExecScriptName.setCellValue("Test case level execution details");
				celExecScriptName.setCellStyle(topHeaderContents);
				sheet.addMergedRegion(cellRangeAddress);
				celLinkToHTMLResults = row.createCell((short) 1);
				
				//Hyperlink to a file in the current directory
				celLinkToHTMLResults.setCellValue(PathConstants.TESTREPORT_FILELINKCOL);
				XSSFHyperlink link = (XSSFHyperlink)createHelper.createHyperlink(HyperlinkType.FILE);
				Reporter.log("Address Link to file: "+ PathConstants.EXREP_OUTPUT_FILE,PathConstants.debugMode);
				link.setAddress(PathConstants.EXREP_OUTPUT_FILE.replace(PathConstants.ReportPath, ""));
				celLinkToHTMLResults.setHyperlink(link);
				
				row = sheet.createRow(rowNum++);
				// Header Column Names
				celExecScriptName = row.createCell(0);
				celTestStatus = row.createCell(1);
				celExecStartTime = row.createCell(2);
				celExecEndTime = row.createCell(3);
				celExecDuration= row.createCell(4);
				celComments = row.createCell(5);
			  
				    
				// Set the Header names for the sheet
				celExecScriptName.setCellValue(PathConstants.TESTREPORT_FIRCOL);
				celTestStatus.setCellValue(PathConstants.TESTREPORT_SECCOL);
				celExecStartTime.setCellValue(PathConstants.TESTREPORT_TRDCOL);
				celExecEndTime.setCellValue(PathConstants.TESTREPORT_FOUCOL);
				celExecDuration.setCellValue(PathConstants.TESTREPORT_FIVCOL);
				celComments.setCellValue(PathConstants.TESTREPORT_SIXCOL);
				
				
				// set style for the table header
				celExecScriptName.setCellStyle(tableHeader);
				celTestStatus.setCellStyle(tableHeader);
				celExecStartTime.setCellStyle(tableHeader);
				celExecEndTime.setCellStyle(tableHeader);
				celExecDuration.setCellStyle(tableHeader);
				celComments.setCellStyle(tableHeader);
				celLinkToHTMLResults.setCellStyle(hlinkstyle);

				
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				sheet.autoSizeColumn(4);
				sheet.autoSizeColumn(5);

				// If you require it to make the entire directory path including parents,use
				// directory.mkdirs(); here instead.
				File directory = new File(reportFile.getParent());
				if (!directory.exists()) {
					directory.mkdirs();
				}
				writeXLOutput = new FileOutputStream(path);
				workbook.write(writeXLOutput);
			} catch (Exception e) {
				// Conversion into unchecked exception is also allowed
				Reporter.log(" The issue in Excel file creation is " + e.toString());
				throw new IOException(e);
			} finally {
				// Close files when they are no longer needed
				workbook.close();
				if (writeXLOutput != null) {
					writeXLOutput.close();
				}
			}
		}
	}

	/**
	 * Get Column Count for a particular row in a worksheet
	 * 
	 * @param sheetName
	 * @param rowNumber
	 * @return
	 */
	public int getColCountForParticularRow(String sheetName, int rowNumber) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int noOfColumns = sheet.getRow(rowNumber).getPhysicalNumberOfCells(); // getLastCellNum();
			return noOfColumns;
		}
	}

	/**
	 * @author saikiran.nataraja
	 * @param sheetName
	 * @param rowNum 
	 * @param colName
	 * @param rowNum
	 * @param data
	 * @see Function to set the data for a value from a test case
	 * @returns true or false if cell data is set @throws Exception
	 */
	public boolean setDataForReport(String sheetName, int rowNumToBeAdded, String colName, String data) throws Exception {
		try {
			FileInputStream fis = new FileInputStream(PathConstants.REP_INPUT_FILE);
			workbook = new XSSFWorkbook(fis);
			int rowNum = 8;
			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1)
				return false;
			sheet = workbook.getSheetAt(index);
			int colCount = getColCountForParticularRow(sheetName, rowNum);
			XSSFRow row = sheet.getRow(rowNum); // Get the Header Row Name as per test case
			Reporter.log("Column Count is:" + colCount, PathConstants.debugMode);
			for (int i = 0; i < colCount; i++) { //
				Reporter.log("Row Data:" + row.getCell(i).getStringCellValue().trim(), PathConstants.debugMode);
				if (!(row.getCell(i).getStringCellValue().isEmpty())) {
					if (row.getCell(i).getStringCellValue().trim().equals(colName)) {
						colNum = i;
						break;
					}
				}
			}
			if (colNum == -1)
				return false;
			
			rowNum = rowNum + rowNumToBeAdded;
			row = sheet.getRow(rowNum);// Get the Value of Header Row passed as per test case
			if (row == null) {
				row = sheet.createRow(rowNum);
			}
			XSSFCell cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			CellStyle cs = workbook.createCellStyle();
			cs.setWrapText(true);
			cs.setFillForegroundColor(IndexedColors.AUTOMATIC.getIndex());
			cs.setFillPattern(FillPatternType.NO_FILL);
			// Set the default cell format as text
			cs.setDataFormat((short) BuiltinFormats.getBuiltinFormat("text"));
			// Added to make all formatting a text
			cell.setCellStyle(cs);
			cell.setCellValue(data);

			for (int i = 0; i < colCount; i++) {
				sheet.autoSizeColumn(i);
			}
			FileOutputStream fileOut = new FileOutputStream(PathConstants.REP_INPUT_FILE);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			fis.close();
		} catch (Exception e) {
			Reporter.log("File IO Exception in ExcelReportManager.java" + e.toString(), PathConstants.debugMode);
			throw e;
		}
		return true;
	}

}
