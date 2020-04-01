/*
All the code that follow is Copyright (c) 2018, Sai Kiran Nataraja. All Rights Reserved.
Not for reuse without permission.
*/

package utilities;

import java.io.File;

import org.testng.Reporter;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import reusableLibrary.PathConstants;

/**
 * THIS IS THE MAIN CORE FILE FOR EXTENT REPORTS
 * 
 * @author saikiran.nataraja
 */
public class ExtentManager {

	private static ExtentReports extent = null;
	private static ExtentTest test;
	private static ExtentHtmlReporter htmlReporter;

	public ExtentManager() {
		// Create ExtentReport Directory if doesnot exists
		File dir = new File(PathConstants.ReportPath);
		// If SecurityManager.checkWrite(java.lang.String) method denies write access to
		// the file.Hence made the directory writable
		if (!(dir.setWritable(true))) {
			Reporter.log("Exception in ExtentManager function");
		}
		dir.mkdirs();
	}

	public ExtentReports createExtentRep() {
//		if (extent != null)
//			return extent; // avoid creating new instance of HTML file if it is not null
		htmlReporter = getHtmlReporter();
		extent = new ExtentReports();
		extent.setSystemInfo("Operating System", System.getProperty("os.name"));
		extent.setSystemInfo("Java JDK Version", System.getProperty("java.version"));
		extent.setSystemInfo("Selenium Version", "3.141.59");
		// class view:
		extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
		extent.attachReporter(htmlReporter);
		return extent;
	}

	private static ExtentHtmlReporter getHtmlReporter() {
		Reporter.log(PathConstants.EXREP_OUTPUT_FILE, false);
		htmlReporter = new ExtentHtmlReporter(new File(PathConstants.EXREP_OUTPUT_FILE));
		htmlReporter.loadXMLConfig(PathConstants.configPath + "extent-config.xml");
		// make the charts visible on report open
//		htmlReporter.config().setChartVisibilityOnOpen(false);
		// chart location - top, bottom
//		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.setAppendExisting(false);
		return htmlReporter;
	}

	public static ExtentTest createTest(String name, String description) {
		test = extent.createTest(name, description);
		return test;
	}
}
