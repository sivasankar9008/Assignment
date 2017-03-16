package Utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import Utilities.JXlKeywords;
import Utilities.ConfigPropertiesFiles;

public class SeleniumReusableKeywords extends JXlKeywords {
	public String resultsDir = "";
	public String currentExecutionName = "";
	public RemoteWebDriver driver;
	public DesiredCapabilities capabilities = new DesiredCapabilities();
	public String identifier, locator, locatorDescription;
	public String currentBrowser = "";

	public WebElement webElement;
	public boolean isTestFailure = false;
	public int pageTextLoadWaitTime, pageElementLoadWaitTime, driverWaitTime, pageLoadWaitTime = 0;
	public static List<String> cssReportsDir = new ArrayList<String>();
	public static String testReportsDirectory;
	public static int testcasefailureNo, testcaseManualScreenshotNo;
	public static identifierType identifierType;
	public static boolean isWindowreadyStateStatus = true;
	public static List<String> ipName = new ArrayList<String>();
	public static List<String> testName = new ArrayList<String>();
	public static List<String> operatingSystemName = new ArrayList<String>();
	public static List<String> browserName = new ArrayList<String>();
	public static List<String> browserVersionName = new ArrayList<String>();
	public static List<String> operstingSystemVersionName = new ArrayList<String>();
	public static List<String> testReportsDir = new ArrayList<String>();
	public static List<String> pageResultsDir = new ArrayList<String>();
	public String currentExecutionOS = "";

	public void TestStart(String hostName, String testCaseName) {
		isTestFailure = false;
		currentTestCaseName = testCaseName;
		setEnvironmentTimeouts();
		testReportStart(hostName);
		iterationCount.clear();
		textCaseDataRowCount();
	}

	public void setup(String machineName, String host, String port, String browser, String os, String browserVersion,
			String osVersion) {
		setEnvironmentTimeouts();
		resultsFolder(machineName.replace(" ", ""), host, port, browser, os, browserVersion, osVersion);
		currentExecutionMachineName(machineName.replace(" ", ""));
		currentExecutionOS = os.toLowerCase();
	}

	public void setEnvironmentTimeouts() {
		setTimeoutsToVariables();
	}

	public void closeAllSessions() {
		driver.quit();
	}

	public enum platFormName {
		IOS, ANDROID

	}

	public enum browserType {
		CHROME, SAFARILAUNCHER
	}

	public void currentExecutionMachineName(String machineName) {
		currentExecutionName = machineName;
	}

	public void resultsFolder(String machineName, String host, String port, String browser, String os,
			String browserVersion, String osVersion) {

		resultsDir = SeleniumReusableKeywords.testReportsDirectory + "\\" + machineName.replace(" ", "");
		ipName.add(host);
		testName.add(browser);
		operatingSystemName.add(os);
		browserName.add(browser);
		browserVersionName.add(browserVersion);
		operstingSystemVersionName.add(osVersion);
		File dir = new File(resultsDir);
		dir.mkdir();
		(new File(resultsDir + "\\Resultfiles\\")).mkdir();
		testReportsDir.add(resultsDir + "\\Resultfiles\\");
		pageResultsDir.add(resultsDir + "\\" + machineName.replace(" ", "") + ".html");
		cssReportsDir.add(resultsDir + "\\style.css");
		transferLogos();
		transferFiles();
	}

	public void lanuchBrowser(String machineName, String host, String port, String browser, String os,
			 String osVersion) {

		platFormName b = platFormName.valueOf(os.toUpperCase().trim());
		currentBrowser = browser;
		System.out.println("Opening " + browser + " Browser...");
		try {
			capabilities.setCapability("newCommandTimeout",
					getFrameworkConfigProperty("AppiumTimeOut").toString().trim());

			switch (b) {
			case IOS:

				capabilities.setCapability("platformName", "iOS");
				capabilities.setCapability("platformVersion", osVersion);
				capabilities.setCapability("deviceName", machineName);
				capabilities.setCapability("nativeEvents", false);
				driver = (AppiumDriver<WebElement>) new RemoteWebDriver(
						new URL("http://" + host + ":" + port + "/wd/hub"), capabilities);
				break;
			case ANDROID:
				capabilities.setCapability("deviceName", "Android");
				if (getFrameworkConfigProperty("AppType").equalsIgnoreCase("web")) {
					capabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
				} else {
					capabilities.setCapability("appPackage", getFrameworkConfigProperty("AppPackage"));
					capabilities.setCapability("appActivity", getFrameworkConfigProperty("AppActivity"));
					capabilities.setCapability("autoWebview", true);
					capabilities.setCapability("recreateChromeDriverSessions", true);
				}
				driver = new AndroidDriver<WebElement>(new URL("http://" + host + ":" + port + "/wd/hub"),
						capabilities);

				break;

			}
			driver.manage().timeouts().implicitlyWait(driverWaitTime, TimeUnit.SECONDS);

		}

		catch (TimeoutException e) {
			stepFailed("Page fail to load within in " + getFrameworkConfigProperty("pageLoadWaitTime") + " seconds");
		} catch (Exception e) {
			System.out.println("Browser: Open Failure/Navigation cancelled, please check the application window.");
			System.out.println(e.toString());
			stepFailed("Browser: Open Failure/Navigation cancelled, please check the application window.");

		}
	}

	public void switchToContext(String contextName) {
		try {
			for (int i = 0; i <= 60; i++) {
				try {

					((AppiumDriver<WebElement>) driver).context(contextName);
					break;
				} catch (Exception e) {
					System.out.println(((AppiumDriver<WebElement>) driver).getContextHandles());
					Thread.sleep(1000);
					System.out.println(e.toString());
				}
				if (i > 60) {
					break;
				}
			}
		} catch (Exception e) {
			stepFailed(e.toString());
		}
	}

	public void identifyBy(String identifier) {
		identifierType i = identifierType.valueOf(identifier);
		switch (i) {
		case xpath:
			webElement = driver.findElement(By.xpath(locator));
			break;
		case id:
			webElement = driver.findElement(By.id(locator));
			break;
		case name:
			webElement = driver.findElement(By.name(locator));
			break;
		case lnktext:
			webElement = driver.findElement(By.linkText(locator));
			break;
		case partiallinktext:
			webElement = driver.findElement(By.partialLinkText(locator));
			break;
		case classname:
			webElement = driver.findElement(By.className(locator));
			break;
		case cssselector:
			webElement = driver.findElement(By.cssSelector(locator));
			break;
		case tagname:
			webElement = driver.findElement(By.tagName(locator));
			break;
		default:
			System.out.println("Element not found '" + locator + "'");
		}
	}

	public void waitForElement(String objName) {
		waitForElement(objName, pageElementLoadWaitTime);
	}

	public void waitTimeForException(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (Exception e) {
			stepFailed(e.toString());
		}
	}

	public void waitForElement(String objectName, int timeout) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			for (int i = 1; i <= timeout; i++) {

				try {
					findWebElement(objectName);
					break;
				} catch (InvalidSelectorException e) {
					System.out.println("InvalidSelectorException occured. Retrying..............");
					waitTimeForException(1);
				} catch (StaleElementReferenceException e) {
					System.out.println("StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
					System.out.println(e.toString());
				} catch (NoSuchElementException e) {

					System.out.println("NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					System.out.println("ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					stepFailed(e.toString());
				} catch (WebDriverException e) {
					System.out.println("WebDriverException occured. Retrying..............");
					waitTimeForException(1);
					System.out.println(e.toString());
				}

				if (i == timeout) {
					stepFailed(locatorDescription + " element not found within '" + timeout + "' seconds timeout ");
				}
			}
		} catch (Exception e) {
			stepFailed(e.toString());

		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(driverWaitTime, TimeUnit.SECONDS);
		}

	}

	public void findWebElement(String objectLocator) {
		parseidentifyByAndlocator(objectLocator);
		identifyBy(identifier);

	}

	public void sendKeys(String objectLocator, String inputValue) {
		waitForElementToDisplay(objectLocator, pageElementLoadWaitTime);
		for (int i = 1; i <= pageElementLoadWaitTime; i++) {
			try {

				webElement.click();
				webElement.clear();
				webElement.sendKeys(inputValue);
				Reporter.log("<Font >Click on :" + locatorDescription);
				stepPassed("Type '" + inputValue + "' in : " + locatorDescription);
				System.out.println("Typing '" + inputValue + "' in : " + locatorDescription);
				break;
			}

			catch (InvalidSelectorException e) {
				System.out.println("InvalidSelectorException occured. Retrying..............");
				waitTimeForException(1);
			} catch (StaleElementReferenceException e) {
				System.out.println("StaleElementReferenceException occured. Retrying..............");
				waitTimeForException(1);
			} catch (NoSuchElementException e) {

				System.out.println("NoSuchElementException occured. Retrying..............");
				waitTimeForException(1);
			} catch (ElementNotVisibleException e) {
				System.out.println("ElementNotVisibleException occured. Retrying..............");
				waitTimeForException(1);
			} catch (UnreachableBrowserException e) {
				stepFailed(e.toString());
			} catch (WebDriverException e) {
				System.out.println("WebDriverException occured. Retrying..............");
				waitTimeForException(1);
			} catch (Exception e) {
				stepFailed("Exception Error '" + e.toString() + "'");
			}
			if (i == pageElementLoadWaitTime) {
				stepFailed(locatorDescription + " element found but its not in editable/clickable state within "
						+ pageElementLoadWaitTime + " timeouts");
			}
		}
	}

	public void sleep(long waittime) {
		System.out.println("Waiting for " + waittime + " seconds...");
		try {
			Thread.sleep(waittime * 1000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Thread.sleep operation failed, during waitTime function call");
		}
	}

	public void selectTextOptionFromDropdown(String objLocator, String visibleValueToSelect) {
		waitForElementToDisplay(objLocator, pageElementLoadWaitTime);
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			for (int i = 1; i <= pageElementLoadWaitTime; i++) {
				try {
					Select select = new Select(webElement);
					select.selectByVisibleText(visibleValueToSelect);
					stepPassed("Select '" + visibleValueToSelect + "' option  from : " + locatorDescription + "'");
					break;

				} catch (InvalidSelectorException e) {
					System.out.println("InvalidSelectorException occured. Retrying..............");
					waitTimeForException(1);
				} catch (StaleElementReferenceException e) {
					System.out.println("StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
				} catch (NoSuchElementException e) {

					System.out.println("NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					System.out.println("ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					stepFailed(e.toString());
				} catch (WebDriverException e) {
					System.out.println("WebDriverException occured. Retrying..............");
					waitTimeForException(1);
				} catch (Exception e) {
					stepFailed("Exception Error '" + e.toString() + "'");
				}
				if (i == pageElementLoadWaitTime) {
					System.out.println("Could not select '" + visibleValueToSelect + "' from : " + locatorDescription);
					stepFailed("Unable to Select '" + visibleValueToSelect + "' option  from : " + locatorDescription
							+ "' drop down");
				}

			}
		} catch (Exception e) {
			stepFailed(e.toString());
		} finally {
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(driverWaitTime, TimeUnit.SECONDS);
		}
	}

	public void waitForPageText(String txt) {
		waitForPageText(txt, pageTextLoadWaitTime);
	}

	public void waitForPageText(String txt, int timeout) {
		int second;
		for (second = 0; second < timeout; second++) {
			/*
			 * try{ driver.getCurrentUrl(); } catch(Exception e){stepFailed(
			 * "WebDriver is not found");}
			 */
			if (second == timeout - 1) {
				System.out.println(
						"Text is not found ' " + txt + "' within " + pageTextLoadWaitTime + " seconds timeouts");
				stepFailed("'" + txt + "' text is not found  within " + pageTextLoadWaitTime + " seconds timeouts");
				break;
			}
			try {

				if (driver.getPageSource().contains(txt)) {
					System.out.println("Text: '" + txt + "' is present");
					break;
				}
			} catch (Exception e) {
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void clickElement(String objLocator) {
		waitForElementToDisplay(objLocator, pageElementLoadWaitTime);
		for (int i = 1; i <= pageElementLoadWaitTime; i++) {
			try {
				/*
				 * if(currentExecutionName.toLowerCase().contains("iphone") ||
				 * currentExecutionName.toLowerCase().contains("ipad")) {
				 * Actions actions = new Actions(driver);
				 * actions.moveToElement(webElement);
				 * actions.click().build().perform(); stepPassed("Click the '"
				 * +locatorDescription+"' element");
				 * 
				 * } else{
				 */

				webElement.click();
				stepPassed("Click the '" + locatorDescription + "'");
				// stepPassed();
				// }
				break;
			} catch (InvalidSelectorException e) {
				System.out.println("InvalidSelectorException occured. Retrying..............");
				waitTimeForException(1);
			} catch (StaleElementReferenceException e) {
				System.out.println("StaleElementReferenceException occured. Retrying..............");
				waitTimeForException(1);
			} catch (NoSuchElementException e) {

				System.out.println("NoSuchElementException occured. Retrying..............");
				waitTimeForException(1);
			} catch (ElementNotVisibleException e) {
				System.out.println("ElementNotVisibleException occured. Retrying..............");
				waitTimeForException(1);
			} catch (UnreachableBrowserException e) {
				stepFailed(e.toString());
			} catch (WebDriverException e) {
				System.out.println("WebDriverException occured. Retrying..............");
				waitTimeForException(1);
			} catch (Exception e) {
				stepFailed("Exception Error '" + e.toString() + "'");
			}

			if (i == pageElementLoadWaitTime) {
				stepFailed(locatorDescription + " element found but its not in editable/clickable state within "
						+ pageElementLoadWaitTime + " timeouts");
			}
		}

	}

	public boolean isElementPresent(String objectLocator) {
		try {
			findWebElement(objectLocator);
			return true;
		} catch (NoSuchElementException e) {

			return false;
		} catch (Exception e) {
			stepFailed(e.toString());
			return false;
		}

	}

	public String getElementText(String objLocator) {

		String getText = null;
		for (int i = 1; i <= pageElementLoadWaitTime; i++) {
			try {
				waitForElement(objLocator, pageElementLoadWaitTime);
				getText = webElement.getText();
				System.out.println("Sucessfully got the text '" + getText + "'");
				break;
			} catch (InvalidSelectorException e) {
				System.out.println("InvalidSelectorException occured. Retrying..............");
				waitTimeForException(1);
			} catch (StaleElementReferenceException e) {
				System.out.println("StaleElementReferenceException occured. Retrying..............");
				waitTimeForException(1);
			} catch (NoSuchElementException e) {

				System.out.println("NoSuchElementException occured. Retrying..............");
				waitTimeForException(1);
			} catch (ElementNotVisibleException e) {
				System.out.println("ElementNotVisibleException occured. Retrying..............");
				waitTimeForException(1);
			} catch (UnreachableBrowserException e) {
				stepFailed(e.toString());
			} catch (WebDriverException e) {
				System.out.println("WebDriverException occured. Retrying..............");
				waitTimeForException(1);
			} catch (Exception e) {
				stepFailed("Exception Error '" + e.toString() + "'");
			}

			if (i == pageElementLoadWaitTime) {
				stepFailed(locatorDescription + " element found but its not in editable/clickable state within "
						+ pageElementLoadWaitTime + " timeouts");

			}
		}
		return getText;

	}

	public void waitForElementToDisplay(String objLocator, int timeout) {
		boolean webElementStatus = false;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

			for (int i = 1; i <= timeout; i++) {
				try {
					// setting implicit wait for element found
					if (!webElementStatus) {
						findWebElement(objLocator);
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webElement);
						webElementStatus = true;
					}
					if (webElement.isDisplayed()) {
						break;
					} else {
						sleep(1);
					}
				} catch (InvalidSelectorException e) {
					System.out.println("InvalidSelectorException occured. Retrying..............");
					waitTimeForException(1);
				} catch (StaleElementReferenceException e) {
					System.out.println("StaleElementReferenceException occured. Retrying..............");
					waitTimeForException(1);
					System.out.println(e.toString());
				} catch (NoSuchElementException e) {

					System.out.println("NoSuchElementException occured. Retrying..............");
					waitTimeForException(1);
				} catch (ElementNotVisibleException e) {
					System.out.println("ElementNotVisibleException occured. Retrying..............");
					waitTimeForException(1);
				} catch (UnreachableBrowserException e) {
					stepFailed(e.toString());
				} catch (WebDriverException e) {
					System.out.println("WebDriverException occured. Retrying..............");
					waitTimeForException(1);
					System.out.println(e.toString());
				}
				if (i == timeout) {
					if (webElementStatus) {
						stepFailed(locatorDescription
								+ " element is present but its not in clickable/editable state within '" + timeout
								+ "' timeout");

					} else {
						stepFailed(locatorDescription + " element not found within '" + timeout + "' seconds timeout ");
					}
				}
			}
		} catch (Exception e) {
			stepFailed("Exception error '" + e.toString() + "'");

		} finally {
			webElementStatus = false;
			// Re-setting the implicit wait is set for the life of the WebDriver
			// object instance
			driver.manage().timeouts().implicitlyWait(driverWaitTime, TimeUnit.SECONDS);
		}

	}

	public void clickOnHiddenElement(String objectLocator) {
		try {
			waitForElement(objectLocator, pageElementLoadWaitTime);
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", webElement);
			// testReporter("green","Click on :"+locatorDescription);
			stepPassed("Click on :" + locatorDescription);
		} catch (Exception e) {
			stepFailed("Click on :" + locatorDescription);
		}
	}

	public boolean isElementDisplayed(String objectLocator) {
		findWebElement(objectLocator);

		if (webElement.isDisplayed()) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isTextPresent(String expectedText) {

		if (driver.getPageSource().contains(expectedText)) {
			return true;
		} else {
			return false;
		}

	}

	public void capturePageScreenShot(String filename) {
		File scrFile = null;
		String scrPath = SeleniumReusableKeywords.testReportsDirectory + "\\Screenshots\\";
		File file = new File(scrPath);
		file.mkdir();
		// maximiseWindow();
		if (driver.getClass().getName().equals("org.openqa.selenium.remote.RemoteWebDriver")) {
			driver = (RemoteWebDriver) new Augmenter().augment(driver);
		} else {
			// driver = driver;
		}
		// Augmenter augmenter = new Augmenter();
		try {
			scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(scrPath + filename + ".png"));
		} catch (org.openqa.selenium.UnhandledAlertException e) {
		} catch (Exception e) {
			stepFailed(e.toString());
			// screenShot(filename);

		} finally {
			if (scrFile == null) {
				System.out.println("This WebDriver does not support screenshots");
				// get us outa here
			}
		}

	}

	public void stepFailed(String report) {
		isTestFailure = true;
		if ((currentExecutionOS.equalsIgnoreCase("android"))
				&& (getFrameworkConfigProperty("AppType").equalsIgnoreCase("hybird"))) {
			switchToContext("NATIVE_APP");
		}
		SeleniumReusableKeywords.testcasefailureNo++;
		String scrPath = "..\\..\\Screenshots";

		writeTestStepReport("<font style='color:red'>stepNo" + report + "</font><br/>", currentExecutionName,
				currentTestCaseName);
		if (!SeleniumReusableKeywords.isWindowreadyStateStatus) {
			SeleniumReusableKeywords.isWindowreadyStateStatus = true;
		} else {
			capturePageScreenShot("TestFailure" + SeleniumReusableKeywords.testcasefailureNo);
		}
		writeTestStepReport(
				"<a href='" + scrPath + "\\TestFailure" + SeleniumReusableKeywords.testcasefailureNo + ".png'> ",
				currentExecutionName, currentTestCaseName);
		writeTestStepReport(
				"<img src='" + scrPath + "\\TestFailure" + SeleniumReusableKeywords.testcasefailureNo
						+ ".png' height='200' width='200'/><br></br></a></font>",
				currentExecutionName, currentTestCaseName);
		testCaseFailed();
	}

	public void stepPassed(String message) {
		writeTestStepReport("<font style='color:green'>stepNo" + message + "</font><br/>", currentExecutionName,
				currentTestCaseName);
	}

	public enum identifierType {
		xpath, name, id, lnktext, partiallinktext, classname, cssselector, tagname
	}

	public void parseidentifyByAndlocator(String identifyByAndLocator) {

		try {
			locatorDescription = identifyByAndLocator.substring(0, identifyByAndLocator.indexOf("@"));
			identifyByAndLocator = identifyByAndLocator.substring(identifyByAndLocator.indexOf("@") + 1);
		} catch (Exception e) {
			locatorDescription = "";
		} finally {
			identifier = identifyByAndLocator.substring(0, identifyByAndLocator.indexOf("=", 0)).toLowerCase();
			locator = identifyByAndLocator.substring(identifyByAndLocator.indexOf("=", 0) + 1);
			System.out.println(currentExecutionName + "-Identify By : " + identifier);
			System.out.println(currentExecutionName + "-Locator : " + locator);
			System.out.println(currentExecutionName + "Locator Description : " + locatorDescription);
			SeleniumReusableKeywords.identifierType = identifierType.valueOf(identifier);
		}
	}

	public void setTimeoutsToVariables() {

		pageElementLoadWaitTime = Integer.parseInt(getFrameworkConfigProperty("ElementLoadWaitTime").toString().trim());
		pageTextLoadWaitTime = Integer.parseInt(getFrameworkConfigProperty("TextLoadWaitTime").toString().trim());
		pageLoadWaitTime = Integer.parseInt(getFrameworkConfigProperty("PageLoadWaitTime").toString().trim());
		driverWaitTime = Integer.parseInt(getFrameworkConfigProperty("ImplicitlyWaitTime").toString().trim());
		System.out.println("Element time out set");
	}

	public void testReportStart(String machineName) {

		writeTestStepReport(
				"<B></B><h1 align='center' style='color:SaddleBrown'>"
						+ ConfigPropertiesFiles.allTcDescription.get(currentTestCaseName) + "</h1><br/>",
				currentExecutionName, currentTestCaseName);

	}

	public void stopProcess(String process) {

		CommandLine command = new CommandLine("cmd");
		command.addArgument("/c");
		command.addArgument("taskkill");
		command.addArgument("/F");
		command.addArgument("/IM");
		command.addArgument(process);

		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);

		try {
			executor.execute(command, resultHandler);
		} catch (IOException e) {

		}

	}

	public boolean verifyTask(String processName) {
		String line;
		int processCount = 0;
		boolean flag = true;

		Process process;
		try {

			process = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((line = input.readLine()) != null) {
				if (line.startsWith(processName)) {
					processCount += 1;
				}
			}
			if (processCount == 0) {
				flag = false;
			}
			input.close();
		} catch (IOException e) {

		}
		return flag;

	}

	public void startServer(String host, String port) {

		if (verifyTask("node.exe")) {
			stopProcess("node.exe");
		}
		if (verifyTask("chromedriver.exe")) {
			stopProcess("chromedriver.exe");
		}
		if (verifyTask("adb.exe")) {
			// stopProcess("adb.exe");
		}
		try {
			String appiumNode = getFrameworkConfigProperty("AppiumNodePath");
			String appiumServer = getFrameworkConfigProperty("AppiumServerPath");
			String myStr;
			myStr = SeleniumReusableKeywords.testReportsDirectory
					.substring(SeleniumReusableKeywords.testReportsDirectory.length() - 22);
			CommandLine command = new CommandLine("cmd");
			command.addArgument("/c");
			command.addArgument(appiumNode);
			command.addArgument(appiumServer);
			command.addArgument("--address");
			command.addArgument(host);
			command.addArgument("--port");
			command.addArgument(port);
			// command.addArgument("--no-reset");
			/*
			 * command.addArgument("--log");
			 * command.addArgument("./TestReports/"+myStr+"/AppiumLogs.log");
			 */DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
			DefaultExecutor executor = new DefaultExecutor();
			executor.setExitValue(1);

			executor.execute(command, resultHandler);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void takeScreenshot(String report) {
		if ((currentExecutionOS.equalsIgnoreCase("android"))
				&& (getFrameworkConfigProperty("AppType").equalsIgnoreCase("hybird"))) {
			switchToContext("NATIVE_APP");
		}
		SeleniumReusableKeywords.testcaseManualScreenshotNo++;
		String scrPath = "..\\..\\Screenshots";
		if (!(currentExecutionOS.equalsIgnoreCase("android"))
				&& !(getFrameworkConfigProperty("AppType").equalsIgnoreCase("hybird"))) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",
					driver.findElement(By.xpath("(//html)[1]")));
		}
		capturePageScreenShot("ManualScreenshotNo" + SeleniumReusableKeywords.testcaseManualScreenshotNo);
		writeTestStepReport("<a href='" + scrPath + "\\ManualScreenshotNo"
				+ SeleniumReusableKeywords.testcaseManualScreenshotNo + ".png'> ", currentExecutionName,
				currentTestCaseName);
		writeTestStepReport(
				"<img src='" + scrPath + "\\ManualScreenshotNo" + SeleniumReusableKeywords.testcaseManualScreenshotNo
						+ ".png' height='200' width='200'/><br></br></a></font>",
				currentExecutionName, currentTestCaseName);
	}

}
