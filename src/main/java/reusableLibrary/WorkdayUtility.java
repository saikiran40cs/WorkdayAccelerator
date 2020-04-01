/*
All the code that follow is Copyright (c) 2018, Sai Kiran Nataraja. All Rights Reserved.
Not for reuse without permission.
*/

package reusableLibrary;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;

import junit.framework.AssertionFailedError;
import utilities.LoggerUtility;

/**
 * @author saikirannataraja
 */
public class WorkdayUtility extends Exception {
	// The serializable class WorkdayUtility does not declare a static final
	// serialVersionUID field of type long
	private static final long serialVersionUID = 1L;

	private ExtentTest test;
	private WebDriver driver;
	private LoggerUtility logInstance;
	private JavascriptExecutor jScriptEx;
	private int driverMinWaitTime = 100;
	private int driverMidWaitTime = 300;
	private int driverMaxWaitTime = 500;

	public enum TextFieldCriteria {
		USINGADJLABEL_DATEWIDGETBOX,USINGADJLABEL_TEXTINPUTBOX,USINGADJLABEL_SEARCHBOX,USINGADJLABEL_PANEL_SEARCHBOX,USINGADJLABEL_ACTIVELIST_SEARCHBOX,USINGADJLABEL_FIELDSETCONTENT_SEARCHBOX,USINGADJLABEL_PANEL_TEXTAREA,USINGADJLABEL_RICHTEXTCONTENT,USINGADJLABEL_NUMERICINPUT,USINGADJLABEL_TEXTAREACRIT1,USINGADJLABEL_TEXTAREACRIT2,USINGTOPLABEL_SEARCHBOX,USINGTEXT,USINGXPATH,USINGID,USINGNULL;
	}
	
	public enum ButtonCriteria {
		USINGTEXT,USINGTITLE,USINGDATAAUTOMATIONID,USINGID,USINGXPATH,USINGLINK,USINGDIVLASTLINK,USINGDIV,USINGDIVINPANEL_ADD,USINGDIVINPANEL_REMOVE,USINGGRIDTITLELABEL,USINGFIELDSETCONTENT;
	}
	
	public enum TextContentCriteria{
		USINGPROMPTOPTION,USINGINPUT,USINGXPATH,USINGANCESTORPROMPT;
	}
	
	public enum RadioButtonCriteria{
		USINGTABLENAME,USINGTEXT,USINGPOSITION;
	}

	public enum PromptCriteria{
		USINGOPENPROMPT,USINGCLOSEDPROMPT,USINGXPATH,USINGTEXT,USINGPANEL,USINGACTIVELIST,USINGFIELDSETCONTENT;
	}
	
	public enum DropDownCriteria{
		USINGDEFAULT,USINGXPATH;
	}
	
	public enum CheckBoxCriteria{
		USINGDEFAULT,USINGPANEL,USINGXPATH;
	}
	
	public enum iFrameCriteria{
		USINGSTRING,USINGXPATH;
	}
	
	private WorkdayUtility(String message, Throwable cause) {
		super(message, cause);
	}

	public WorkdayUtility(WebDriver driver, ExtentTest test, LoggerUtility loggerInstance) {
		this.test = test;
		this.logInstance = loggerInstance;
		this.driver = driver;
		jScriptEx = (JavascriptExecutor) driver;
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
	}

	/**
	 * Function to get the exception details from the webelement object
	 * 
	 * @param objTextField
	 * @return
	 */
	public String getExceptionDetails(String objectName) {
		String errorMessage = "Unable to perform action on the Object";
		if(objectName.length()>0) {
		String testClass = Thread.currentThread().getStackTrace()[3].getClassName();
		String testMethod = Thread.currentThread().getStackTrace()[3].getMethodName();

		String pageClass = Thread.currentThread().getStackTrace()[2].getClassName();
		String pageMethod = Thread.currentThread().getStackTrace()[2].getMethodName();

		errorMessage = "Unable to perform action on webelement: '" + objectName + "', @ " + pageMethod + " in "
				+ pageClass + " while executing " + testMethod + " in " + testClass;
		PathConstants.exceptionDetail = errorMessage;
//			logInstance.info(ExceptionUtils.getStackTrace(ex));
			return errorMessage;
		}else {
			PathConstants.exceptionDetail = errorMessage;
			return errorMessage;
		}
	}

	/**
	 * Function to explicitly wait for the passed time
	 * 
	 * @param waitTime
	 */
	public void explicitWaitTime(int waitTime) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
			Thread.sleep(waitTime);
		} catch (Exception e) {
		}
	}

	/**
	 * Function to highlighter method
	 * 
	 * @param element
	 */
	public void highlighterMethod(WebElement element) {
		try {
			for (int i = 0; i < 3; i++) {
				jScriptEx.executeScript(
						"arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
				explicitWaitTime(driverMinWaitTime);
				jScriptEx.executeScript("arguments[0].setAttribute('style','border: solid 2px white');", element);
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * Function to get Text from Javascript into element
	 * @author saikirannataraja
	 * @param element
	 * @return 
	 * @throws InterruptedException
	 */
	public String getTextFromJSElement(WebElement element) throws WorkdayUtility {
		return (String)((JavascriptExecutor) driver).executeScript("return arguments[0].innerText;", element);
	}

	/**
	 * Function to scroll into element
	 * 
	 * @param element
	 * @throws InterruptedException
	 */
	public void scrollIntoElement(WebElement element) throws WorkdayUtility {
		jScriptEx.executeScript("arguments[0].scrollIntoView(true);", element);
		explicitWaitTime(driverMaxWaitTime);
	}

	/**
	 * Function to check if WebElement is displayed
	 * 
	 * @param webElement
	 * @return
	 */
	public boolean isWebElementDisplayed(WebElement webElement) {
		boolean bIsDisplayed = webElement.isDisplayed();
		String state = bIsDisplayed ? "displayed" : "not displayed";
		String strDesc = webElement.toString();
		Reporter.log(bIsDisplayed + strDesc + "Object is " + state, true);
		return bIsDisplayed;
	}

	/**
	 * Function to check if web element is enabled
	 * 
	 * @param webElement
	 * @return
	 */
	public boolean isWebElementEnabled(String element) {
		// Check if the WebElement is Enabled
		WebElement webElement = driver.findElement(By.xpath(element));
		boolean bIsEnabled = webElement.isEnabled();
		String state = bIsEnabled ? "enabled" : "disabled";
		String strDesc = webElement.toString();
		Reporter.log(
				bIsEnabled + "Check enabled state of object with description  " + strDesc + "Object state is " + state,
				true);
		return bIsEnabled;
	}

	/**
	 * Function to check if Webelement is selected or not
	 * 
	 * @param webElement
	 * @return
	 */
	public boolean isWebElementSelected(WebElement webElement) {
		boolean bIsSelected = webElement.isSelected();
		String state = bIsSelected ? "selected" : "unselected";
		String strDesc = webElement.toString();
		Reporter.log(bIsSelected + "Check selected state of object with description  " + strDesc + " Object state is "
				+ state, true);
		return bIsSelected;
	}

	/**
	 * Function to wait for element for timeInSeconds
	 * 
	 * @param xpathToWaitFor
	 * @param timeInSeconds
	 */
	public void waitForElement(WebElement xpathToWaitFor, int timeInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(xpathToWaitFor));
	}
	

	/**
	 * Function to get date values
	 * @param date
	 * @return
	 */
	public String getDateValueFromDetails(String date) {
		String genDate = date;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-5"));
		if (date.equalsIgnoreCase("0")) {
			genDate = sdf.format(new Date());
		} else if (date.contains("+")) {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, Integer.parseInt(date.split("\\+")[1]));
			genDate = sdf.format(c.getTime());
		} else if (date.contains("-")) {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, -Integer.parseInt(date.split("\\-")[1]));
			genDate = sdf.format(c.getTime());
		}
		return genDate;
	}
	
	/**
	 * Function to check if element present
	 * @param ele
	 * @return
	 */
	 public boolean isElementPresent(WebElement ele) {
        boolean isPresent = false;
        //search for elements and check if list is empty
        if (ele.isEnabled()) {
            isPresent = true;
        }
        return isPresent;
    }
	 

	/**
	 * Function to close Alert and get its text
	 * @returns String message from the alert
	 * @throws WorkdayUtility
	 */
	public String closeAlertAndGetItsText() throws WorkdayUtility {
		String alertMessage = "";
		try {
			driver.findElement(By.xpath("//div[@data-automation-id='errorWidgetBarViewAllCanvas' and text()='View All']/..")).click();
			explicitWaitTime(PathConstants.minWaitTime);
			alertMessage = driver.findElement(By.xpath("//div[@data-automation-id='errorWidgetMessageFieldCanvas']/ancestor::ol")).getText().strip();
			test.info(alertMessage);
			logInstance.warn("Viewed the alert details:" + alertMessage);
			explicitWaitTime(driverMinWaitTime);
			driver.findElement(By.xpath("//div[@data-automation-id='closeButton']")).click();
		}catch (Exception e) {
			alertMessage = "";
		}
		return alertMessage;
	}
	 
	public WorkdayUtility uploadFile(String xpathValue,String attachmentLocation, String message)
			throws WorkdayUtility {
		WebElement attachmentBrowse_Button = driver.findElement(By.xpath("//input[@data-automation-id='"+xpathValue+"']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", attachmentBrowse_Button);
		attachmentBrowse_Button.sendKeys(PathConstants.uploadsPath+attachmentLocation);
		if(!message.isBlank()) {
			test.info(message);	logInstance.info(message);
		}
		return this;
	}

	/**
	 * Function to set the text field values Using Top Label Name
	 * @param xpathValue
	 * @param strText
	 * @param message
	 * @returns WorkdayUtility class instance
	 * @throws WorkdayUtility
	 * @throws Exception
	 */
	public WorkdayUtility setTextFieldValue(TextFieldCriteria criteria, String xpathValue, String strText, String message)
			throws WorkdayUtility {
		WebElement objTextField = null;
		try {
			switch (criteria) {
			case USINGADJLABEL_DATEWIDGETBOX:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='dateWidgetInputBox']"));
				break;
				
			case USINGADJLABEL_TEXTINPUTBOX:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='textInputBox']"));
				break;
				
			case USINGADJLABEL_RICHTEXTCONTENT:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//div[@data-automation-id='richTextContent']"));
				break;
				
			case USINGADJLABEL_NUMERICINPUT:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='numericInput']"));
				break;
				
			case USINGADJLABEL_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;
				
			case USINGADJLABEL_PANEL_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue.split(":")[0]+"']/following-sibling::ul//div[@data-automation-id='panel']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;

			case USINGADJLABEL_PANEL_TEXTAREA:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue.split(":")[0]+"']/following-sibling::ul//div[@data-automation-id='panel']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//textarea[@data-automation-id='textAreaField']"));
				break;
				
			case USINGADJLABEL_ACTIVELIST_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//h3[text()='"+xpathValue.split(":")[0]+"']/following-sibling::div//div[@data-automation-id='activeList']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;

			case USINGADJLABEL_FIELDSETCONTENT_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//h3[text()='"+xpathValue.split(":")[0]+"']/../following-sibling::div//div[@data-automation-id='fieldSetContent']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;
			case USINGADJLABEL_TEXTAREACRIT1:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//textarea[@data-automation-id='textAreaField']"));
				break;	
				
			case USINGADJLABEL_TEXTAREACRIT2:
				objTextField = driver.findElement(By.xpath("//textarea[@data-automation-id='"+xpathValue+"']"));
				break;

			case USINGTOPLABEL_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue+"']/following-sibling::input"));
				break;
			case USINGTEXT:
				objTextField = driver.findElement(By.xpath("//*[text()='"+xpathValue+"']/../following-sibling::div//input"));
				break;
			case USINGXPATH:
				objTextField = driver.findElement(By.xpath(xpathValue));
				break;
			case USINGID:
				objTextField = driver.findElement(By.id(xpathValue));
				break;
			case USINGNULL:
				objTextField = driver.findElement(By.xpath("//div[@data-automation-label='" + strText + "' and @title='" + strText + "']"));
				break;
			}
			highlighterMethod(objTextField);
			objTextField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
			objTextField.sendKeys(strText + Keys.ENTER);
			if (!message.isBlank()) {
				test.info(message);	logInstance.info(message);
			}
			explicitWaitTime(PathConstants.minWaitTime);
		} catch (Exception ex) {
			Reporter.log("Set TextField Using Label Name value in element with description failed due to exception " + ex.getMessage()+ "Fail");
			if(objTextField!=null) {
				getExceptionDetails(objTextField.toString().substring(objTextField.toString().indexOf("->")));
			}else {
				PathConstants.exceptionDetail = "Unable to set the Text: " + strText + "for the field " + xpathValue ;
			}
//			assertWDEquals("NO ERROR", PathConstants.exceptionDetail, ex.getMessage());
			throw new WorkdayUtility(PathConstants.exceptionDetail, ex);
		}
		Reporter.log("Set TextField " + strText + " Value " + strText + " entered");
		return this;
	}

	/**
	 * Function to get the newest file in a folder
	 * @param filePath
	 * @param ext
	 * @return
	 */
	public String getTheNewestFile(String filePath, String ext) {
	    File theNewestFile = null;
	    File dir = new File(filePath);
	    FileFilter fileFilter = new WildcardFileFilter("*." + ext);
	    File[] files = dir.listFiles(fileFilter);

	    if (files.length > 0) {
	        /** The newest file comes first **/
	        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
	        theNewestFile = files[0];
	    }

	    return theNewestFile.getName();
	}
	
	/**
	 * Function to click on the object based on text
	 * @param objTextField
	 * @param message
	 * @return
	 * @throws WorkdayUtility
	 */
	public WorkdayUtility clickBasedOnCriteria(ButtonCriteria Criteria,String xpathValue, String message)
			throws WorkdayUtility {
		WebElement objTextField = null;
		try {
			switch (Criteria) {
			case USINGTEXT:
				objTextField = driver.findElement(By.xpath("//button[text()='"+xpathValue+"']"));
				break;
				
			case USINGTITLE:
				objTextField = driver.findElement(By.xpath("//button[@title='"+xpathValue+"']"));
				break;
			case USINGDATAAUTOMATIONID:
				objTextField = driver.findElement(By.xpath("//button[@data-automation-id='"+xpathValue+"']"));
				break;
			case USINGID:
				objTextField = driver.findElement(By.xpath("//button[@id='"+xpathValue+"']"));
				break;
			case USINGXPATH:
				objTextField = driver.findElement(By.xpath(xpathValue));
				break;
			case USINGDIV:
				objTextField = driver.findElement(By.xpath("//div[@title='"+xpathValue+"']"));
				break;
			case USINGLINK:
				objTextField = driver.findElement(By.xpath("//div[@data-automation-label='"+xpathValue+"']"));
				break;
			case USINGDIVLASTLINK:
				objTextField = driver.findElement(By.xpath("(//div[@data-automation-label='"+xpathValue+"'])[last()]"));
				break;
			case USINGDIVINPANEL_ADD:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue+"']/following-sibling::button[@data-automation-id='panelSetAddButton']"));
				break;
			case USINGDIVINPANEL_REMOVE:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue+"']/following-sibling::ul//button[@data-automation-id='panelSetRowDeleteButton']"));
				break;
			case USINGGRIDTITLELABEL:
				objTextField = driver.findElement(By.xpath("(//span[@data-automation-id='gridTitleLabel' and @title='"+xpathValue+"']/ancestor::div[@data-automation-groups-as-tabs='true']//td[@data-automation-id='tabGridVisbleCell-0'])[1]"));
				break;
			case USINGFIELDSETCONTENT:
				objTextField = driver.findElement(By.xpath("//h3[text()='"+xpathValue.split(":")[0]+"']/../following-sibling::div//div[@data-automation-id='fieldSetContent']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//span[@data-automation-id='promptIcon']"));
				break;
			}
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", objTextField);
			objTextField.click();
			explicitWaitTime(PathConstants.minWaitTime);
			if (!message.isBlank()) {
				test.info(message);	logInstance.info(message);
			}
		} catch (Exception ex) {
			if(objTextField!=null) {
				getExceptionDetails(objTextField.toString().substring(objTextField.toString().indexOf("->")));
			}else {
				PathConstants.exceptionDetail = "Unable to click the field " + xpathValue ;
			}
//			assertWDEquals("NO ERROR", PathConstants.exceptionDetail, ex.getMessage());
			throw new WorkdayUtility(PathConstants.exceptionDetail, ex);
		}
		return this;
	}
	
	/**
	 * Function to client elements from the list
	 * @param elemeList
	 * @param processNameToBeClicked
	 * @param message
	 */
	public WorkdayUtility clickElementFromTheList(List<WebElement> elemeList, String processNameToBeClicked,
			String message) throws WorkdayUtility {
		try {
			for (WebElement ele : elemeList) {
				if (ele.getText().contains(processNameToBeClicked)==true) {
					scrollIntoElement(ele);
					highlighterMethod(ele);
					ele.click();
					explicitWaitTime(PathConstants.minWaitTime);
					if (!message.isBlank()) {
						test.info(message);	logInstance.info(message);
					}
					break;
				}
			}
		} catch (Exception ex) {
			throw new WorkdayUtility(PathConstants.exceptionDetail, ex);
		}
		return this;
	}
	
	/**
	 * Function to Verify the page title on the object
	 * 
	 * @param objTextField
	 * @param message
	 * @return
	 * @throws WorkdayUtility
	 */
	public WorkdayUtility verifyPageTitle(String expected) throws WorkdayUtility {
		try {
			String Actual = driver.findElement(By.xpath("(//span[@data-automation-id='pageHeaderTitleText'])[last()]"))
					.getText();
			explicitWaitTime(PathConstants.minWaitTime);
			assertWDEquals(expected, Actual, "'" + Actual + "' Page is displayed successfully");
		} catch (Exception ex) {
			throw new WorkdayUtility(PathConstants.exceptionDetail, ex);
		}
		return this;
	}

	/**
	 * Function to Get Text from the object
	 * @param Criteria
	 * @param xpathValue
	 * @return
	 * @throws WorkdayUtility
	 */
	public String getTextContentFromObject(TextContentCriteria Criteria,String xpathValue) throws WorkdayUtility {
		WebElement objTextField = null;
		try {
			switch (Criteria) {
			case USINGPROMPTOPTION:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//div[@data-automation-id='promptOption']"));
				break;
			case USINGANCESTORPROMPT:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/ancestor::li//div[@data-automation-id='promptOption']"));
				break;
			case USINGINPUT:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input"));
				break;
			case USINGXPATH:
				objTextField = driver.findElement(By.xpath(xpathValue));
				break;
			}
			return objTextField.getText();
		} catch (WebDriverException ex) {
			Reporter.log("Get Text Content value in element with description failed due to exception " + ex.getMessage()
					+ "Fail");
			return "";
		}
	}

	
	/**
	 * Function to select the radio button
	 * @param Criteria
	 * @param tableAdjLabelName_Position
	 * @param objValue
	 * @param message
	 * @return
	 * @throws WorkdayUtility
	 */
	public WorkdayUtility radiobuttonSelection(RadioButtonCriteria Criteria,String tableAdjLabelName_Position,String objValue, String message) throws WorkdayUtility {
		WebElement objRadButn = null;
		String strDesc = "";
		try {
			switch (Criteria) {
			case USINGTABLENAME:
				objRadButn = driver.findElement(By.xpath("//div[text()='"+tableAdjLabelName_Position+"']/ancestor::td/following-sibling::td//span[@data-automation-id='radioBtn']/label[@data-automation-label='"+objValue+"']"));
				break;
				
			case USINGTEXT:
				objRadButn = driver.findElement(By.xpath("//label[text()=\""+objValue+"\"]"));
				break;
				
			case USINGPOSITION:
				objRadButn = driver.findElement(By.xpath("(//span[@data-automation-id='radioBtn']/label[@data-automation-label='"+objValue+"'])[position()="+tableAdjLabelName_Position+"]"));
				break;
			}
			strDesc = objRadButn.toString();
			scrollIntoElement(objRadButn);
			highlighterMethod(objRadButn);
			objRadButn.click();
			if (!message.isBlank()) {
				test.info(message);	logInstance.info(message);
			}
			explicitWaitTime(PathConstants.minWaitTime);
		} catch (Exception ex) {
			Reporter.log("Radio Button element with description " + strDesc
					+ "Wrapper method radiobuttonSelection() : Element with description " + strDesc
					+ " is either not visible or is not enabled" + "Fail");
			if(objRadButn!=null) {
				getExceptionDetails(objRadButn.toString().substring(objRadButn.toString().indexOf("->")));
			}else {
				PathConstants.exceptionDetail = "Unable to select the Radiobutton for the field: " + objValue ;
			}
//			assertWDEquals("NO ERROR", PathConstants.exceptionDetail, ex.getMessage());
			throw new WorkdayUtility(PathConstants.exceptionDetail, ex);
		}
		Reporter.log("Radio Button element with description " + strDesc);
		return this;
	}


	/**
	 * Function to check the checkbox selection
	 * @param objChkBox
	 * @param checkOrUncheck
	 * @param message
	 * @return
	 * @throws InterruptedException
	 */
	public WorkdayUtility checkBoxSelection(CheckBoxCriteria Criteria,String xpathValue, boolean checkOrUncheck, String message)	throws WorkdayUtility {
		WebElement objChkBox =null;
		switch (Criteria) {
		case USINGDEFAULT:
			objChkBox = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//div[@data-automation-id='checkbox']"));
			break;
			
		case USINGPANEL:
			objChkBox =  driver.findElement(By.xpath("//div[text()='"+xpathValue.split(":")[0]+"']/following-sibling::ul//div[@data-automation-id='panel']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//div[@data-automation-id='checkbox']"));
			break;
		case USINGXPATH:
			objChkBox =  driver.findElement(By.xpath(xpathValue));
			break;
		}
		String strDesc = objChkBox.toString();
		// Check if the object is enabled, if yes click the same
		if (objChkBox.isDisplayed() && objChkBox.isEnabled()) {
			// Check state of check box
			boolean isChecked = Boolean.valueOf(objChkBox.getAttribute("data-automationcheckboxchecked"));
			explicitWaitTime(driverMidWaitTime);
			// Check if Not Checked
			if (isChecked == checkOrUncheck) {
				highlighterMethod(objChkBox);
				objChkBox.click();
			}
			if (!message.isBlank()) {
				test.info(message);	logInstance.info(message);
			}
		} else {
			Reporter.log("Check CheckBox element with description " + strDesc
					+ "Wrapper method checkCheckBox() : Element with description " + strDesc
					+ " is either not visible or is not enabled" + "Fail");
		}

		Reporter.log("Check CheckBox element with description " + strDesc);
		return this;
	}


	/**
	 * Function to move to an element and click using actions class
	 * @param objFieldName
	 * @param message
	 * @return
	 * @throws WorkdayUtility
	 */
	public WorkdayUtility actionMoveToElementAndClick(String objFieldName, String message) throws WorkdayUtility {
		try {
			WebElement objActionField = driver.findElement(By.xpath("//div[@data-automation-label='"+objFieldName+"']"));
			scrollIntoElement(objActionField);
			highlighterMethod(objActionField);
			Actions action = new Actions(driver);
			explicitWaitTime(PathConstants.minWaitTime);
			action.moveToElement(objActionField).click().build().perform();
			if (!message.isBlank()) {
				test.info(message);	logInstance.info(message);
			}
		} catch (Exception ex) {
			Reporter.log("Not moved & Clicked on value in element with description failed due to exception "
					+ ex.getMessage() + "Fail");
			
			if(objFieldName!=null) {
				getExceptionDetails(objFieldName);
			}else {
				PathConstants.exceptionDetail = "Unable to perform move the element and click for the field " + objFieldName ;
			}
//			assertWDEquals("NO ERROR", PathConstants.exceptionDetail, ex.getMessage());
			throw new WorkdayUtility(PathConstants.exceptionDetail, ex);
		}
		return this;
	}

	/**
	 * @param elem
	 */
	public WorkdayUtility actionsMoveToElementByOffset(String element) {
		try {
			WebElement elem = driver.findElement(By.xpath(element));
			int width = elem.getSize().getWidth();
			Actions act = new Actions(driver);
			act.moveToElement(elem).moveByOffset((width/2)-2, 0).click().perform();
		}catch (Exception e) {
    	}
		return this;
	}
	
	/**
	 * Function to move to an element and click using actions class
	 * @param objFieldName
	 * @param message
	 * @return
	 * @throws WorkdayUtility
	 */
	public WorkdayUtility actionMouseHoverToElement(String objFieldName, String message) throws WorkdayUtility {
		try {
			WebElement objActionField = driver.findElement(By.xpath("//div[@data-automation-label='"+objFieldName+"']"));
			scrollIntoElement(objActionField);
			highlighterMethod(objActionField);
			Actions action = new Actions(driver);
			explicitWaitTime(PathConstants.minWaitTime);
			action.moveToElement(objActionField).build().perform();
			if (!message.isBlank()) {
				test.info(message);	logInstance.info(message);
			}
		} catch (Exception ex) {
			Reporter.log("Not moved & Clicked on value in element with description failed due to exception "
					+ ex.getMessage() + "Fail");
			if(objFieldName!=null) {
				getExceptionDetails(objFieldName);
			}else {
				PathConstants.exceptionDetail = "Unable to perform mouse hover the element and click for the field " + objFieldName ;
			}
//			assertWDEquals("NO ERROR", PathConstants.exceptionDetail, ex.getMessage());
			throw new WorkdayUtility(PathConstants.exceptionDetail, ex);
		}
		return this;
	}

	/**
	 * Function to switch the window with name
	 * @return
	 */
	public WorkdayUtility switchToWindow() {
		try {
			// driver.switchTo().window(strWindowName);
			// Switch to new window opened
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}
		} catch (Exception e) {
			Reporter.log("Switch Window : Exception occured : " + e + "Fail", false);
			throw (e);
		}

		return this;
	}
	
	/**
	 * Function to switch to parent window and Close Child window with name
	 * @param originalHandle
	 * @return
	 */
	public WorkdayUtility switchToParentWindowAndCloseChild(String originalHandle) {
		try {
			// Switch to new window opened
			for (String winHandle : driver.getWindowHandles()) {
				 if (!winHandle.equals(originalHandle)) {
					driver.switchTo().window(winHandle);
					driver.close();
				 }
			}
			  driver.switchTo().window(originalHandle);
		} catch (Exception e) {
			Reporter.log("Switch Window : Exception occured : " + e + "Fail", false);
			throw (e);
		}

		return this;
	}

	/**
	 * Function to switch to a frame
	 * @param criteria
	 * @param frameName
	 * @return
	 */
	public WorkdayUtility switchToFrame(iFrameCriteria criteria,String frameName) {
		try {
			switch (criteria) {
			case USINGSTRING:
				driver.switchTo().frame(frameName);
				break;

			case USINGXPATH:
				driver.switchTo().frame(driver.findElement(By.xpath(frameName)));
				break;
			}
		}catch (Exception e) {
			Reporter.log("Switch To Frame: Exception occured: "+ e + "Fail",false);
		}
		return this;
	}
	
	/**
	 * Function to validate value present in the table
	 * 
	 * @param wTable
	 * @param Value
	 * @return
	 */
	public boolean validateValuePresentInTable(String wTable, String Value) {
		int rowCount = driver.findElements(By.xpath(wTable + "/tr")).size();
		int colCount = driver.findElements(By.xpath(wTable + "/tr[1]/td")).size();
		boolean validateValuePresentInTable = false;
		for (int i = 2; i < rowCount; i++) {
			for (int j = 1; j < colCount; j++) {

				if (driver.findElement(By.xpath(wTable + "/tr[" + i + "]/td[" + j + "]")).getText().trim()
						.equalsIgnoreCase(Value)) {
					validateValuePresentInTable = true;
					break;
				}
			}
			if (validateValuePresentInTable) {
				break;
			}
		}
		return validateValuePresentInTable;
	}
	
	/**
	 * Function to verify text value exists or not
	 * @param criteria
	 * @param xpathValue
	 * @param strText
	 * @param message
	 * @return
	 * @throws WorkdayUtility
	 */
	public boolean verifyTextValueExists(TextFieldCriteria criteria, String xpathValue, String strText, String message)
			throws WorkdayUtility {
		WebElement objTextField = null;
		try {
			switch (criteria) {
			case USINGADJLABEL_DATEWIDGETBOX:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='dateWidgetInputBox']"));
				break;
				
			case USINGADJLABEL_TEXTINPUTBOX:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='textInputBox']"));
				break;
				
			case USINGADJLABEL_RICHTEXTCONTENT:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='richTextContent']"));
				break;
				
			case USINGADJLABEL_NUMERICINPUT:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='numericInput']"));
				break;
				
			case USINGADJLABEL_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;
				
			case USINGADJLABEL_PANEL_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue.split(":")[0]+"']/following-sibling::ul//div[@data-automation-id='panel']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;
				
			case USINGADJLABEL_PANEL_TEXTAREA:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue.split(":")[0]+"']/following-sibling::ul//div[@data-automation-id='panel']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//textarea[@data-automation-id='textAreaField']"));
				break;
				
			case USINGADJLABEL_ACTIVELIST_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//h3[text()='"+xpathValue.split(":")[0]+"']/following-sibling::div//div[@data-automation-id='activeList']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;
				
			case USINGADJLABEL_FIELDSETCONTENT_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//h3[text()='"+xpathValue.split(":")[0]+"']/../following-sibling::div//div[@data-automation-id='fieldSetContent']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;

			case USINGADJLABEL_TEXTAREACRIT1:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//textarea[@data-automation-id='textAreaField']"));
				break;	

			case USINGADJLABEL_TEXTAREACRIT2:
				objTextField = driver.findElement(By.xpath("//textarea[@aria-label='"+xpathValue+"']"));
				break;	
				
			case USINGTOPLABEL_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue+"']/following-sibling::input"));
				break;
			case USINGTEXT:
				objTextField = driver.findElement(By.xpath("//*[text()='"+xpathValue+"']/../following-sibling::div//input"));
				break;
			case USINGXPATH:
				objTextField = driver.findElement(By.xpath(xpathValue));
				break;
			case USINGID:
				objTextField = driver.findElement(By.id(xpathValue));	
				break;
			case USINGNULL:
				objTextField = driver.findElement(By.xpath("//div[@data-automation-label='" + strText + "' and @title='" + strText + "']"));
				break;
			}
			explicitWaitTime(PathConstants.minWaitTime);
			highlighterMethod(objTextField);
			if (!message.isBlank() && objTextField.getText().trim().equalsIgnoreCase(strText)) {
				test.info(message);	logInstance.info(message);
			}
			return true;
		} catch (Exception ex) {
			Reporter.log("Get TextField value in element with description failed due to exception " + ex.getMessage()
					+ "Fail");
			return false;
		}
	}
	
	/**
	 * Function to select Value From Dropdown
	 * @param Dr
	 * @param xpathValue
	 * @param strText
	 * @param message
	 * @return
	 * @throws WorkdayUtility
	 */
	public boolean selectValueFromDropdown(DropDownCriteria Criteria,String xpathValue, String strText, String message)
			throws WorkdayUtility {
		try {
			WebElement objSelectDropDownField= null;
			switch (Criteria) {
			case USINGDEFAULT:
				objSelectDropDownField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//div[@data-automation-id='selectWidget']"));
				break;
			case USINGXPATH:
				objSelectDropDownField =  driver.findElement(By.xpath(xpathValue));
				break;
			}
			scrollIntoElement(objSelectDropDownField);
			objSelectDropDownField.click();
			explicitWaitTime(PathConstants.minWaitTime);
			objSelectDropDownField = driver.findElement(By.xpath("//div[@data-automation-label='" + strText + "' and @title='" + strText + "']/parent::div"));
			highlighterMethod(objSelectDropDownField);
			objSelectDropDownField.click();
			if (!message.isBlank()) {
				test.info(message);	logInstance.info(message);
			}
			return true;
		} catch (Exception ex) {
			Reporter.log("Set the Dropdown Value in element with description failed due to exception " + ex.getMessage()
					+ "Fail");
			return false;
		}
	}
	
	

	/**
	 * Function to select Option from the prompt - drop down list
	 * @param Criteria
	 * @param textCriteria
	 * @param xpathValue
	 * @param textValue
	 * @param message
	 * @return
	 * @throws WorkdayUtility
	 */
	public WorkdayUtility selectValueFromPrompt(PromptCriteria Criteria,TextFieldCriteria textCriteria,String xpathValue,String textValue, String message) throws WorkdayUtility {
		WebElement objTextField = null;
		try {
			WebElement objDropdownField = null;
			boolean clickDropdown = false;
//			scrollIntoElement(objDropdownField);
			switch (Criteria) {
			case USINGOPENPROMPT:
				break;
			case USINGPANEL:
				objDropdownField = driver.findElement(By.xpath("//div[text()='"+xpathValue.split(":")[0]+"']/following-sibling::ul//div[@data-automation-id='panel']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//span[@data-automation-id='promptIcon']"));
				clickDropdown = true;
				break;
			case USINGACTIVELIST:
				objDropdownField = driver.findElement(By.xpath("//h3[text()='"+xpathValue.split(":")[0]+"']/following-sibling::div//div[@data-automation-id='activeList']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//span[@data-automation-id='promptIcon']"));
				clickDropdown = true;
				break;
			case USINGFIELDSETCONTENT:
				objDropdownField = driver.findElement(By.xpath("//h3[text()='"+xpathValue.split(":")[0]+"']/../following-sibling::div//div[@data-automation-id='fieldSetContent']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//span[@data-automation-id='promptIcon']"));
				clickDropdown = true;
				break;
			case USINGTEXT:
				objDropdownField = driver.findElement(By.xpath("//*[text()='"+xpathValue+"']/../following-sibling::div//span[@data-automation-id='promptIcon']"));
				clickDropdown = true;
				break;
			case USINGXPATH:
				objDropdownField = driver.findElement(By.xpath(xpathValue));
				clickDropdown = true;
				break;
			case USINGCLOSEDPROMPT:
				objDropdownField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//span[@data-automation-id='promptIcon']"));
				clickDropdown = true;
				break;
			}
			// Click Dropdown is set as True
			if(clickDropdown) {
				scrollIntoElement(objDropdownField);
				objDropdownField.click();
			}
			switch (textCriteria) {
			case USINGADJLABEL_DATEWIDGETBOX:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='dateWidgetInputBox']"));
				break;
				
			case USINGADJLABEL_NUMERICINPUT:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='numericInput']"));
				break;
				
			case USINGADJLABEL_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//label[text()='"+xpathValue+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;
				
			case USINGADJLABEL_PANEL_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue.split(":")[0]+"']/following-sibling::ul//div[@data-automation-id='panel']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;
				
			case USINGADJLABEL_ACTIVELIST_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//h3[text()='"+xpathValue.split(":")[0]+"']/following-sibling::div//div[@data-automation-id='activeList']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;
				
			case USINGADJLABEL_FIELDSETCONTENT_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//h3[text()='"+xpathValue.split(":")[0]+"']/following-sibling::div//div[@data-automation-id='fieldSetContent']//label[text()='"+xpathValue.split(":")[1]+"']/../following-sibling::div//input[@data-automation-id='searchBox']"));
				break;

			case USINGTOPLABEL_SEARCHBOX:
				objTextField = driver.findElement(By.xpath("//div[text()='"+xpathValue+"']/following-sibling::input"));
				break;
			case USINGTEXT:
				objTextField = driver.findElement(By.xpath("//*[text()='"+xpathValue+"']/../following-sibling::div//input"));
				break;
			case USINGXPATH:
				objTextField = driver.findElement(By.xpath(xpathValue));
				break;
			case USINGNULL:
				objTextField = driver.findElement(By.xpath("//div[@data-automation-label='" + xpathValue + "' and @title='" + xpathValue + "']"));
				break;
			default:
				break;
			}
			explicitWaitTime(PathConstants.minWaitTime);
			highlighterMethod(objTextField);
			objTextField.sendKeys(textValue + Keys.ENTER);
			if (!message.isBlank()) {
				test.info(message);	logInstance.info(message);
			}
//			explicitWaitTime(PathConstants.minWaitTime);
			selectValueFromSearchResults(textValue);
		} catch (Exception ex) {
			Reporter.log("Select value in element with description failed due to exception " + ex.getMessage() + "Fail");
			if(objTextField!=null) {
				getExceptionDetails(objTextField.toString().substring(objTextField.toString().indexOf("->")));
			}else {
				PathConstants.exceptionDetail = "Unable to select value From Prompt for the field " + xpathValue ;
			}
//			assertWDEquals("NO ERROR", PathConstants.exceptionDetail, ex.getMessage());
			throw new WorkdayUtility(PathConstants.exceptionDetail, ex);
		}

		Reporter.log("Select value from dropdown: Select value " + xpathValue + " Value " + textValue + " selected");
		return this;
	}
	
	
		

	/**
	 * Function to search value from search results and click the passed text
	 * @author saikirannataraja
	 * @param strText
	 */
	public void selectValueFromSearchResults(String strText) {
		List<WebElement> promptOptionList;
		List<WebElement> promptOptionRadioButtonList;
//		explicitWaitTime(PathConstants.minWaitTime);
		explicitWaitTime(driverMinWaitTime);
		int size = driver.findElements(By.xpath(
				"//div[@data-automation-mode='popup']//ul[@role='listbox' and contains(@aria-label,'Results')]//div/span[not(contains(@class,'Radio'))]"))
				.size();
		try {
			if (size != 0) {
				promptOptionList = driver.findElements(By.xpath(
						"//div[@data-automation-mode='popup']//ul[@role='listbox' and contains(@aria-label,'Results')]//div/span[not(contains(@class,'Radio'))]"));
				Reporter.log("Prompt Options List Size is: " + promptOptionList.size(), PathConstants.debugMode);
				promptOptionRadioButtonList = driver.findElements(By.xpath(
						"//div[@data-automation-mode='popup']//ul[@role='listbox' and contains(@aria-label,'Results')]//input[@type='radio']"));
				Reporter.log("Prompt Options RadioButton List Size is: " + promptOptionRadioButtonList.size(),
						PathConstants.debugMode);
				for (int i = 0; i < promptOptionList.size(); i++) {
					String promptOption = promptOptionList.get(i).getText();
					Reporter.log("Prompt Option value is: " + promptOption, PathConstants.debugMode);
					if (promptOption.equalsIgnoreCase(strText)) {
						clickBasedOnCriteria(ButtonCriteria.USINGLINK,strText, "");
						promptOptionRadioButtonList.get(i).click();
						break;
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	/**
	 * @throws WorkdayUtility
	 */
	public void checkIfErrorExists() throws WorkdayUtility {
		String alertMessageText = closeAlertAndGetItsText();
		if(!alertMessageText.isEmpty()) {
			assertWDEquals("NO Error",alertMessageText,"");
		}
	}

	/**
	 * AssertEquals function is used to verify the expected and actual value
	 * 
	 * @author saikiran.nataraja
	 * @param expected value needs to be passed
	 * @param actual   value retrieved from the application
	 * @param message  to be logged if the match is true
	 */
	public void assertWDEquals(Object expected, Object actual, String message) {
		if (expected == null && actual == null) {
			test.fail( "Expected and Actual values are NULL");
			logInstance.error("Expected and Actual values are NULL");
			Reporter.log("Expected and Actual values are NULL", false);
			return;
		}
		if (expected != null && expected.equals(actual)) {
			test.info(message);	logInstance.info(message);
			return;
		} else {
			String messageResult = "Expected Result: '" + expected + "' does NOT match with Actual Result: '" + actual
					+ "' ." + message;
			PathConstants.exceptionDetail = messageResult;
			logInstance.debug(messageResult);
			Reporter.log(messageResult);
			fail(format("", expected, actual));
		}
	}

	/**
	 * Sub to check Assertion failure messages
	 * 
	 * @author saikiran.nataraja
	 * @param message
	 */
	public void fail(String message) {
		if (message == null) {
			throw new AssertionFailedError();
		}
		throw new AssertionFailedError(message);
	}

	/**
	 * Sub to report the issue in the required formatted text
	 * 
	 * @author saikiran.nataraja
	 * @param message
	 * @param expected
	 * @param actual
	 * @return
	 */
	public String format(String message, Object expected, Object actual) {
		String formatted = "The Expected Outcome is not matched with Actual Outcome::";
		if (message != null && message.length() > 0) {
			formatted = message + "  ";
		}
		return formatted + "Expected :'" + expected + "' but Actual is:'" + actual + "'";
	}

}
