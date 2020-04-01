/*
All the code that follow is Copyright (c) 2018, Sai Kiran Nataraja. All Rights Reserved.
Not for reuse without permission.
*/

package reusableLibrary;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.github.javafaker.Faker;

import io.github.bonigarcia.wdm.WebDriverManager;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import utilities.ExcelDataInputManager;
import utilities.ExcelReportManager;
import utilities.ExtentManager;
import utilities.LoggerUtility;

public class DriverFactory extends ExtentManager {

	protected String businessProcessName="";
	protected String bpTestSetName;
	protected LoggerUtility loggerInstance;
	protected ExtentReports exRepInst;
	protected ExtentTest exRepTestInst, exRepPareInst;
	protected ExcelReportManager xlRepMgr = null;
	protected ExtentHtmlReporter htmlReporterInstance;
	public final String getCurrentlyLoggedInUser = System.getProperty("user.name");
	protected WebDriver webdriver = null;
	protected String runOnBrowser = null;
	protected Properties OR = null;
	protected String baseURL = "";
	protected Random rand = new Random();
	protected Faker fk = new Faker(new Locale("en-US"));
	private int proxyType = ProxyType.AUTODETECT.ordinal();
	

	
	/**
	 * This is to create an extent manager instance before every new class
	 * 
	 * @author saikiran.nataraja
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@BeforeClass
	public void createExtentInstance() throws IOException, InterruptedException {
		 
		//-----------------------------------------BEFORE SUITE ------------------------------------------
		businessProcessName = super.getClass().getSimpleName();
		Reporter.log("Executing Business Process Name " + businessProcessName, PathConstants.debugMode);
		PathConstants.setDateFormatSettings(); // set the date format before starting each test
		if (PathConstants.debugMode) {
			PathConstants.ErrorReportPath = PathConstants.ReportPath + PathConstants.currentDateFormat + "_"
					+ businessProcessName + "_ErrorScreenshots";
		}
		//----------------------SET LOG, EXCEL & EXTENT REPORT NAMES--------------------------------------------
		PathConstants.ReportPath = PathConstants.ReportPath.replace(businessProcessName,"");
		String valueToReplace = PathConstants.ReportPath.replace(PathConstants.ReportPath.substring(0,PathConstants.ReportPath.indexOf("testResults")), "").trim();
		PathConstants.ReportPath = PathConstants.ReportPath.replace(valueToReplace,"testResults"+PathConstants.fs+businessProcessName+PathConstants.fs);
		String LOGGER_FILE = PathConstants.LogsPath+businessProcessName+PathConstants.fs+PathConstants.currentDateFormat+"_"+ PathConstants.LOGGER_FILENAME;
		String EXREP_OUTPUT_FILE = PathConstants.ReportPath + PathConstants.currentDateFormat +"_"+  PathConstants.EXREP_OUTPUT_FILENAME;
		String REP_INPUT_FILE = PathConstants.ReportPath + PathConstants.currentDateFormat +"_"+  PathConstants.REP_INPUT_FILENAME;
		
		
		valueToReplace = LOGGER_FILE.replace(LOGGER_FILE.substring(0,LOGGER_FILE.indexOf("_")+1), "").trim();
		LOGGER_FILE = LOGGER_FILE.replace(valueToReplace,businessProcessName +"_"+  PathConstants.LOGGER_FILENAME);
		System.setProperty("logFilename", LOGGER_FILE);
		Reporter.log("Logger File Name:" + LOGGER_FILE,PathConstants.debugMode);
		
		valueToReplace = REP_INPUT_FILE.replace(REP_INPUT_FILE.substring(0,REP_INPUT_FILE.indexOf("_")+1), "").trim();
		REP_INPUT_FILE = REP_INPUT_FILE.replace(valueToReplace,businessProcessName +"_"+  PathConstants.REP_INPUT_FILENAME);
		PathConstants.REP_INPUT_FILE = REP_INPUT_FILE;
		Reporter.log("ExcelReport File Name:" + REP_INPUT_FILE,PathConstants.debugMode);
		
		valueToReplace = EXREP_OUTPUT_FILE.replace(EXREP_OUTPUT_FILE.substring(0,EXREP_OUTPUT_FILE.indexOf("_")+1), "").trim();
		EXREP_OUTPUT_FILE = EXREP_OUTPUT_FILE.replace(valueToReplace,businessProcessName +"_"+  PathConstants.EXREP_OUTPUT_FILENAME);
		PathConstants.EXREP_OUTPUT_FILE = EXREP_OUTPUT_FILE;
		Reporter.log("ExtentReport File Name:" + EXREP_OUTPUT_FILE,PathConstants.debugMode);
		//-----------------------------------------BEFORE CLASS ------------------------------------------
		System.setProperty("log4j.configurationFile", PathConstants.log4j2Path);
		loggerInstance = new LoggerUtility();
		ExtentManager xtentMgr = new ExtentManager();
		exRepInst = xtentMgr.createExtentRep();
	}

	/**
	 * Function to create ExcelRepInstance
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void createExcelRepInstance() throws IOException, InterruptedException {
		// Instantiating the ExtentReports
		loggerInstance.startTestCase(businessProcessName);
		// Create testInstance
		exRepPareInst = exRepInst.createTest(businessProcessName,
				"'" + businessProcessName + "' is used to check details in Application.");
		exRepPareInst.assignAuthor(getCurrentlyLoggedInUser);
		exRepPareInst.assignCategory("Regression_" + businessProcessName);
		// Initialize the excel report
		xlRepMgr = new ExcelReportManager();
		xlRepMgr.initializeExcelReport(PathConstants.REP_INPUT_FILE);
		Thread.sleep(PathConstants.minWaitTime);
	}

	@Parameters({ "BrowserType" })
	@BeforeTest
	public void browserSetup(@Optional("chrome") String browser) throws IOException {
		runOnBrowser = browser;
		// Setup the webdriver manager to chrome for bonigarcia library to be fetched from.
		//.version("73.0.3683.68").version("74.0.3729.6").version("75.0.3770.8").version("76.0.3809.68").version("78.0.3904.70")
		WebDriverManager.getInstance(CHROME).forceCache().version("80.0.3987.106").setup();
		// Set the capabilities and launch the driver
		webdriver = new ChromeDriver(createChromeCapabilites());
		webdriver.manage().window().maximize();
		webdriver.manage().deleteAllCookies();
		webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		webdriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		PathConstants.executionStartTime = System.currentTimeMillis();
	}

	/**
	 * Function to create chrome capabilities
	 * 
	 * @author saikirannataraja
	 * @returns chromeoptions
	 */
	public ChromeOptions createChromeCapabilites() {
		ChromeOptions chromeOptions = new ChromeOptions();
		LinkedHashMap<String, Object> chromePrefs = new LinkedHashMap<String, Object>();
		// Set ACCEPT_SSL_CERTS variable to true
		chromePrefs.put(CapabilityType.ACCEPT_SSL_CERTS, true);
		chromePrefs.put(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.extensions_to_open", "pdf");
		chromePrefs.put("--always-authorize-plugins", false);
		chromePrefs.put("download.prompt_for_download", true);
		chromePrefs.put("download.default_directory", PathConstants.downloadsPath);
		chromePrefs.put("credentials_enable_service", false);
		chromePrefs.put("profile.password_manager_enabled", false);
		chromeOptions.setExperimentalOption("prefs", chromePrefs);
		//This capability will help to not load Chrome Automation extension. Due to which, "Failed to load extension" popup would not appear.
		chromeOptions.setExperimentalOption("useAutomationExtension", false);
		chromeOptions.setCapability("network.proxy.type", proxyType);
		chromeOptions.addArguments("--start-maximized");
		chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		//Enable below line if want to run in headless mode
//		chromeOptions.addArguments("--headless");
		// Optional, if not specified, WebDriver will search your path for chromedriver.
		// Commented the below line as the drivers are now being derived from
		// WebDriverManager class written by bonigarcia
//		System.setProperty("webdriver.chrome.driver", PathConstants.chromeDriverPath);
		System.setProperty("webdriver.chrome.args", "--disable-logging");
		System.setProperty("webdriver.chrome.silentOutput", "true");
		return chromeOptions;
	}

	/**
	 * Function to return browser version
	 * 
	 * @author saikiran.nataraja
	 * @return browser name and version
	 */
	public String getBrowserVersion() {
		String browser_version;
		Capabilities cap = ((RemoteWebDriver) webdriver).getCapabilities();
		String browsername = cap.getBrowserName();
		String GetuAgent = (String) ((JavascriptExecutor) webdriver).executeScript("return navigator.userAgent;");
		// This block to find out IE Version number
		if ("internet explorer".equalsIgnoreCase(browsername)) {
			// uAgent return as "MSIE 8.0 Windows" for IE8
			if (GetuAgent.contains("MSIE") && GetuAgent.contains("Windows")) {
				browser_version = GetuAgent.substring(GetuAgent.indexOf("MSIE") + 5, GetuAgent.indexOf("Windows") - 2);
			} else if (GetuAgent.contains("Trident/7.0")) {
				browser_version = "11.0";
			} else {
				browser_version = "0.0";
			}
		} else if ("firefox".equalsIgnoreCase(browsername)) {
			browser_version = GetuAgent.substring(GetuAgent.indexOf("Firefox")).split(" ")[0].replace("/", "-");
			browser_version = browser_version.replace("Firefox-", "");
		} else { // Browser version for chrome and Opera
			browser_version = cap.getVersion();
		}
		String browserversion = browser_version.substring(0, browser_version.indexOf('.'));
		return Captialize(browsername) + " browser (Version: " + browserversion + " )";
	}

	/**
	 * Function to Captialize the word
	 * 
	 * @param RequiredWord
	 * @return
	 */
	public String Captialize(String RequiredWord) {
		return RequiredWord.substring(0, 1).toUpperCase()
				+ RequiredWord.substring(1, RequiredWord.length()).toLowerCase();
	}

	/**
	 * Function to initialize Extent report and it must be called only @Test
	 * annotation
	 * 
	 * @author saikiran.nataraja
	 */
	public void initializeExtentReport(int iterationNum) {
		Reporter.log("Running the '"+businessProcessName + "' - Iteration Number: "+ String.valueOf(iterationNum),PathConstants.debugMode);
		loggerInstance.instanceCase(businessProcessName,String.valueOf(iterationNum));
		// Create testInstance
		exRepTestInst = exRepPareInst
				.createNode(businessProcessName + "_InputDataRowNumber_" + String.valueOf(iterationNum));
	}
	
	/**
	 * Function to return the seconds to time format
	 * @author saikirannataraja
	 * @param sec
	 * @return
	 */
	public String convertSecToTimeFormat(int sec) {
	    int seconds = sec % 60;
	    int minutes = sec / 60;
	        int hours = minutes / 60;
	        minutes %= 60;
	        if( hours >= 24) {
	            int days = hours / 24;
	            return String.format("%d days %02d:%02d:%02d", days,hours%24, minutes, seconds);
	        }
	        return String.format("%02d hrs:%02d min:%02d sec", hours, minutes, seconds);
	}

	/**
	 * Function to check if the business process is set to runnable or not
	 * 
	 * @author saikirannataraja
	 * @throws Exception
	 */
	public void checkIfBPRunnable() throws Exception {
		ExcelDataInputManager readScData;
		try {
			readScData = new ExcelDataInputManager(PathConstants.BUSPROCESS_INPUTFILE,
					PathConstants.BUSPROCESS_SHEETNAME);
			Reporter.log("Business Process Name: " + businessProcessName, PathConstants.debugMode);
			boolean getStatus = readScData.isBusinessProcessRunnable(businessProcessName);
			if (!getStatus) {
				xlRepMgr.setDataForReport(PathConstants.TESTREPORT_SHEETNAME, 1, PathConstants.TESTREPORT_FIRCOL,
						businessProcessName + "_InputRowData1");
				xlRepMgr.setDataForReport(PathConstants.TESTREPORT_SHEETNAME, 1, PathConstants.TESTREPORT_SECCOL,
						"NOT EXECUTED");
				xlRepMgr.setDataForReport(PathConstants.TESTREPORT_SHEETNAME, 1, PathConstants.TESTREPORT_FIVCOL,
						"Skipping Business ProcessName: '" + businessProcessName + "' as runmode set to NO");
				throw new SkipException(
						"Skipping Business ProcessName: '" + businessProcessName + "' as runmode set to NO");
			}
		} catch (IOException e) {
		}
	}

	@AfterTest
	public void closeSession() throws Exception {

		if (webdriver != null) {
			webdriver.close();
		}
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("taskkill /F /IM geckodriver.exe");
			rt.exec("taskkill /F /IM chromedriver.exe");
			rt.exec("taskkill /F /IM IEDriverServer.exe");
			saveExtentReport();
		} catch (IOException e) {
			// clean up state...
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Function to save the extent reports
	 * 
	 * @author saikirannataraja
	 */
	private void saveExtentReport() {
		// write all resources to report file
		exRepInst.flush();
	}

	@AfterClass
	public void tearDownFunction() {
		// tear down the class file

	}

	/**
	 * Capture the operations on Test completion
	 * 
	 * @param testResult
	 * @author saikiran.nataraja
	 * @throws Exception
	 */
	@AfterMethod
	public void operationsOnTestCompletion(ITestResult testResult) throws Exception {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			Reporter.log(" - FAILED.", true);
			String relativeScErrImgPath;
			// Create Error Screenshot Directory if doesn't exists
			File dir = new File(PathConstants.ErrorReportPath);
			dir.mkdirs();
			try {
				loggerInstance.info("Failure Stack Trace: " + testResult.getThrowable().getMessage());
				// Extent Reports take screenshot
				exRepTestInst.log(Status.FAIL, "Failure Stack Trace: " + testResult.getThrowable().fillInStackTrace().getLocalizedMessage());
			}catch (Exception e) {
				exRepTestInst.log(Status.FAIL, "Failure Stack Trace: 'Exception' caused" + e.getMessage());
			}
			// ONLY IF DEBUG MODE is true the screenshots must be captured
			if (PathConstants.debugMode==true) {
				File imagePath = new File(PathConstants.ErrorReportPath + PathConstants.fs + "Screen_"
						+ PathConstants.DateFormat + Captialize(runOnBrowser) + "_"
						+ businessProcessName.substring(0, Math.min(businessProcessName.length(), 20)) + ".png");
				try {
					// Capture screenshot of Screen - Enable if required
					relativeScErrImgPath = captureScreenshotOfScreen(imagePath, "Driver");
				} catch (Exception e) {
					relativeScErrImgPath = captureScreenshotOfScreen(imagePath, "Screen");
				}
				// adding screenshots to log
				exRepTestInst.log(Status.INFO, "Refer below Snapshot: ",
						MediaEntityBuilder.createScreenCaptureFromPath(relativeScErrImgPath).build());
			}
		} else if (testResult.getStatus() == ITestResult.SKIP) {
			loggerInstance.info("Test skipped: As the Business Process runmode is set to NO in the Trigger Sheet");
			exRepPareInst.log(Status.SKIP,
					"Test skipped: " + "As the Business Process runmode is set to NO in the Trigger Sheet");
		} else {
			loggerInstance.info("'" + businessProcessName + "' is passed.");
		}
		loggerInstance.endTestCase(businessProcessName);
	}

	/**
	 * Function to capture screenshot of the driver
	 * 
	 * @author saikirannataraja
	 * @param imagePath
	 * @param captStrategy
	 * @returns a .png file of the screen
	 * @throws HeadlessException
	 * @throws AWTException
	 * @throws IOException
	 */
	public String captureScreenshotOfScreen(File imagePath, String captStrategy)
			throws HeadlessException, AWTException, IOException {
		// Here the screenshot path is reduced to a maximum of 20 literals
		BufferedImage image = null;
		switch (captStrategy) {
		case "Screen":
			image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			break;

		case "Driver":
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
					.takeScreenshot(webdriver);
			image = screenshot.getImage();
			break;
		}
		ImageIO.write(image, "PNG", imagePath);
		// Check below line if the screenshot is NOT displayed properly
		String relativeErrImgPath = imagePath.getAbsoluteFile().toString().replace(PathConstants.ReportPath,
				"." + PathConstants.fs);
		Reporter.log(relativeErrImgPath, true);
		return relativeErrImgPath;
	}


}
