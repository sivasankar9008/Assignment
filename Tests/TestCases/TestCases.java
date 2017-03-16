package TestCases;


import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import AssignmentScenarios.Functionalities;
import org.testng.annotations.Listeners;
import io.appium.java_client.AppiumDriver;

public class TestCases extends Functionalities
{
	@BeforeClass
	@Parameters({"selenium.machinename","selenium.host", "selenium.port", "selenium.browser", "selenium.os", "selenium.browserVersion", "selenium.osVersion","selenium.sheetNo"})
	public void precondition(String machineName,String host,String port,String browser,String os,String browserVersion,String osVersion,String sheetNo)
	{
		testDataSheetNo=Integer.parseInt(sheetNo);
		setup(machineName,host, port, browser,os,browserVersion,osVersion);
		
	}

	@AfterClass
	public void closeSessions()
	{
		closeAllSessions();
	}
	@Test(alwaysRun=true)
	@Parameters({"selenium.machinename","selenium.host", "selenium.port", "selenium.App", "selenium.os", "selenium.osVersion"})
	public void TC01(String machineName,String host,String port,String browser,String os,String browserVersion,Method method) 
	{
		try 
		{
			int loginCount=1;
			TestStart(machineName, method.getName());
			
			for (int i = 0; i < iterationCount.size(); i++) 
			{
				dataRowNo = Integer.parseInt(iterationCount.get(i).toString());
				if(loginCount==1)
				{
					String userName=getTextData("txtUserName");
					String password=getTextData("txtPassword");
					login(machineName,host, port, browser,os,browserVersion,userName,password);
					loginCount++;
				}
				writeTestStepReport("<font size=4 style='color:orange'>TestDataSet:" + (i + 1) + "</font><br/>",currentExecutionName, currentTestCaseName);
			
			}
		} 
		finally
		{
			testEnd();
		}
	}
	@Test(alwaysRun=true)
	@Parameters({"selenium.machinename","selenium.host", "selenium.port", "selenium.browser", "selenium.os", "selenium.osVersion"})
	public void TC02(String machineName,String host,String port,String browser,String os,String osVersion,Method method) 
	{
		try 
		{
			int loginCount=1;
			TestStart(machineName, method.getName());
			//Iteration for data driven testing
			for (int i = 0; i < iterationCount.size(); i++) 
			{
				dataRowNo = Integer.parseInt(iterationCount.get(i).toString());
				if(loginCount==1)
				{
					String userName=getTextData("txtUserName");
					String password=getTextData("txtPassword");
					login(machineName,host, port, browser,os,osVersion,userName,password);
					loginCount++;
				}
				writeTestStepReport("<font size=4 style='color:orange'>TestDataSet:" + (i + 1) + "</font><br/>",currentExecutionName, currentTestCaseName);
				verifyBeneficiaryListnameorAcc();
			}
		} 
		finally
		{
			testEnd();
		}
	}
	public void testEnd()
	{
		try{
			
			logout();
		}
		catch(Exception e)
		{
			stepFailed(e.toString());
			((AppiumDriver<WebElement>) driver).closeApp();
		}
	}
	}

	
