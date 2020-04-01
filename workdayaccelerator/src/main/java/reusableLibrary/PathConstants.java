/*'*************************************************************************************************************************************************
' Class Name			: PathConstants
' Description			: Used to store the variables that are used across.
' How to Use			:
'-----------------------------------------------------------------
' Author                    Version          Creation Date         
'-----------------------------------------------------------------
' Sai Kiran Nataraja         v1.0             16-December-2018		
'*************************************************************************************************************************************************
 */
package reusableLibrary;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** @author saikiran.nataraja */

public final class PathConstants {
	
	public static boolean debugMode = false;
	
	public enum countryName {
		Argentina,Australia,Austria,Belgium,Canada,Chile,Denmark,Dubai,France,Germany,HongKong,India,Italy,Luxembourg,Malaysia,Netherlands,Singapore,SouthKorea,Spain,Sweden,Switzerland,UnitedKingdom,UnitedStatesOfAmerica;
	}
	
	public static int minWaitTime = 1161;
	public static int midWaitTime = 2000;
	public static int maxWaitTime = 2000;
	
	public static String pageUrl = "PAGE URL NOT FOUND";
	public static String wUsername = "USERNAME NOT FOUND";
	public static String wPassword = "PASSWORD NOT FOUND";
	public static String wCountry = "COUNTRY NOT FOUND";
	public static String wCountryState = "STATE NOT FOUND";
	public static long executionStartTime;
	public static long executionEndTime;
	public static long executionDuration;
	public static String exceptionDetail;

	
	// for additional accuracy use the String format for date as
	public static SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yy");
	public static SimpleDateFormat formatterForDate = new SimpleDateFormat("MMddyyyy"); 
	public static final String DateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss_").format(new Date());
	public static final String currentDateFormat = new SimpleDateFormat("ddMMyyyy").format(new Date());
	public static final SimpleDateFormat DocGenDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
//	public final static String WEEK_NUMBER = new SimpleDateFormat("w").format(new java.util.Date());
	
	// For Report start time and End time format
	private static SimpleDateFormat DateFormatSettings;
	public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
	public static final String fs = File.separator;
	
	public static final String configResources =  System.getProperty("user.dir") + fs + "src" + fs + "main" + fs + "resources";
	public static final String log4j2Path =  System.getProperty("user.dir")	+ fs + "configuration" + fs + "log4j2.properties";
	public static final String configPath =  System.getProperty("user.dir") + fs + "configuration" + fs;

	/** Test Data Path	*/
	public static final String BUSPROCESS_INPUTFILE = System.getProperty("user.dir") + fs +"AWDASuiteGenerator.xlsm";
	public static final String testDataPath = System.getProperty("user.dir") + fs + "testData" + fs ;
	
	/** Report Path	 */
	public static final String ORGANIZATIONROLES_INPUTFILENAME = "Organization_Roles.xlsx";
	public static String ReportPath = System.getProperty("user.dir") + fs + "testResults" + fs;
	public static String LogsPath = System.getProperty("user.dir") + fs + "testLogs" + fs;
	public static String ErrorReportPath = ReportPath;
	public static String EXREP_OUTPUT_FILE,REP_INPUT_FILE;
	public static String LOGGER_FILENAME = "rollinglog4j2.log";
	public static String EXREP_OUTPUT_FILENAME = "AutomationTestReport.html";
	public static String REP_INPUT_FILENAME ="AutomationExcelReport.xlsx";
	public static final String downloadsPath = System.getProperty("user.dir") + fs + "Downloads" + fs;
	public static final String uploadsPath = System.getProperty("user.dir") + fs + "Uploads" + fs;

	// Driver path location
	public static final String chromeDriverPath = System.getProperty("user.dir") + fs + "Drivers" + fs + "chromedriver.exe";

	/** Test Data Work sheet */
	public static final String TESTDATA_SHEETNAME = "TcLevelTestData";
	public static final String BUSPROCESS_SHEETNAME = "ScenarioGen";
	public static final String DEFINITIONS_SHEETNAME = "Definitions";
	public static final String ORGANIZATIONROLES_SHEETNAME = "Organization Roles";
	
	public static final String BUSPROCESS_SHEET_TENANTTOBESELECTED = "TenantToBeSelected";
	public static final String BUSPROCESS_SHEET_USERNAMETOBEUSEDFORRUN = "UserNameToBeUsedForRun";
	public static final String BUSPROCESS_SHEET_REGIONSELECTION = "RegionSelection";
	public static final String BUSPROCESS_SHEET_EXPLICITWAITTIMESELECTION = "ExplicitWaitTime(in ms)";
	public static final String BUSPROCESS_SHEET_BUSINESSPROCESS = "BusinessProcess";
	
	/** Report related data	 */
	public static final String TESTREPORT_SHEETNAME = "TestSuite Report";
	public static final String TESTREPORT_HEADERTITLE = "Automated Test Execution Report";
	public static final String TESTREPORT_FILELINKCOL ="Click to view Result";
	
	public static final String TESTREPORT_FIRCOL = "Name of the Executed Test Set";
	public static final String TESTREPORT_SECCOL ="Execution Status";
	public static final String TESTREPORT_TRDCOL ="Execution Start Time";
	public static final String TESTREPORT_FOUCOL ="Execution End Time";
	public static final String TESTREPORT_FIVCOL ="Execution Duration";
	public static final String TESTREPORT_SIXCOL ="Exceptions if any";

	/** RUN MODES FOR BROWSERS */
	public static final String RUNMODE = "Runmode";
	public static final String YES = "Y";
	public static final String NO = "N";

	private PathConstants() {
		throw new IllegalAccessError("PathConstants class");
	}

	/**
	 * @return the dateFormatSettings
	 */
	public static SimpleDateFormat getDateFormatSettings() {
		return DateFormatSettings;
	}

	/**
	 * @param dateFormatSettings the dateFormatSettings to set
	 */
	public static void setDateFormatSettings() {
		DateFormatSettings = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);
	}
	
	/** Role Names setting */
	public static final String ORGROLE_CompensationPartner = "Compensation Partner";
	public static final String ORGROLE_CompensationExecutive = "Compensation Executive";
	public static final String ORGROLE_HighPotentialPartner = "High Potential Partner";
	public static final String ORGROLE_HRAnalyst = "HR Analyst";
	public static final String ORGROLE_HRExecutive = "HR Executive";
	public static final String ORGROLE_HRPartner = "HR Partner";
	public static final String ORGROLE_HRTeamLeader = "HR Team Leader";
	public static final String ORGROLE_OnboardingCoordinatorLocal = "Onboarding Coordinator (Local)";
	public static final String ORGROLE_RelocationAnalyst = "Relocation Analyst";
	public static final String ORGROLE_RelocationSpecialist = "Relocation Specialist";
	public static final String ORGROLE_RetireePartner = "Retiree Partner";
	public static final String ORGROLE_SuccessionPartner = "Succession Partner";
	public static final String ORGROLE_TalentAcquisitionExecutive = "Talent Acquisition Executive";
	public static final String ORGROLE_TalentExecutiveViewOnly = "Talent Executive (View Only)";
	public static final String ORGROLE_Manager = "Manager";
	public static final String ORGROLE_ManagersManager = "Manager's Manager";
	public static final String ORGROLE_PayrollPartner = "Payroll Partner";
	public static final String ORGROLE_WorkCouncilLocal = "Work Council (Local)";

}
