/*
All the code that follow is Copyright (c) 2018, Sai Kiran Nataraja. All Rights Reserved.
Not for reuse without permission.
*/
package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.testng.Reporter;

import bsh.This;
import reusableLibrary.PathConstants;

public class LoggerUtility {
	// Initialize Log4j logs
	private String dSign ="$";
	private String sSign = "*";
	private static final int LOGLINELENGTH = 88;
	private static Logger log4jLogger;
	
	public LoggerUtility() {
		log4jLogger= (Logger) LogManager.getLogger(LoggerUtility.class.getName());
	}

	/**
	 * Function to update Log4j File Configuration
	 */
	private void updateLog4jFileName() { 
	    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
	    ctx.reconfigure();
	 }

	
	/**
	 * @author saikirannataraja
	 * @see This is to print log for the beginning of the test case
	 * @param sTestCaseName
	 */
	public void startTestCase(String sTestCaseName) {
		updateLog4jFileName();
		int getRepLength = sTestCaseName.length();
		getRepLength = (LOGLINELENGTH - getRepLength-2)/2 ;
		String dS = dSign.repeat(getRepLength);
		Reporter.log("log4j.configurationFile :: " + System.getProperty("log4j.configurationFile"),PathConstants.debugMode);
		log4jLogger.trace(sSign.repeat(LOGLINELENGTH));
		log4jLogger.info(dS+"  " + sTestCaseName + " "+dS);
		log4jLogger.trace(sSign.repeat(LOGLINELENGTH));
	}

	/**
	 * @author saikirannataraja
	 * @see This is to print log for the ending of the test case
	 * @param sTestCaseName
	 */
	public void endTestCase(String sTestCaseName) {
		log4jLogger.trace("XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-" + "             XXXXXXXXXXXXXXXXXXXXXX");
		log4jLogger.info("X");
		log4jLogger.info("X");
	}

	public void instanceCase(String bpName,String iterationNum) {
		int getRepLength = (bpName+"' - Iteration Number: "+iterationNum).length();
		getRepLength = (LOGLINELENGTH - getRepLength-4)/2 ;
		String dS = dSign.repeat(getRepLength);
		log4jLogger.trace(sSign.repeat(LOGLINELENGTH));
		log4jLogger.info(dS+" '"+bpName+"' - Iteration Number: "+iterationNum+"  "+dS);
		log4jLogger.trace(sSign.repeat(LOGLINELENGTH));
	}
	
	// Need to create these methods, so that they can be called
	public void info(String message) {
		log4jLogger.info(message);
	}

	public void warn(String message) {
		log4jLogger.warn(message);
	}

	public void error(String message) {
		log4jLogger.error(message);
	}

	public void fatal(String message) {
		log4jLogger.fatal(message);
	}

	public void debug(String message) {
		log4jLogger.debug(message);
	}

	public void trace(String message) {
		log4jLogger.trace(message);
	}
}
