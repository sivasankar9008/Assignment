package Global;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
public class CommonApplicationKeywords extends Utilities.SeleniumReusableKeywords
{
	
	 public boolean logoutSuccess=false;
	public void login(String machineName, String host, String port,
			String browser, String os,String osVersion,String username,String password)
	{
		if(logoutSuccess){
			logoutSuccess=false;
		}
		else
		{
			
			startServer(host,port);
			sleep(10);
			lanuchBrowser(machineName,host,port,browser,os,osVersion);
			switchToContext(COR.webViewContextName);
		}
			login(username,password);
	}
	
	public void login(String username,String password)
	{
				switchToContext(COR.webViewContextName);
				clickElement(COR.customerID);
				sendKeys(COR.userName,username);
				sendKeys(COR.password,password);
				clickElement(COR.login);
				waitForPageText("Accounts");
				stepPassed("Login succesfully");
	}
	public void logout()
	{
		switchToContext(COR.webViewContextName);
		clickElement(COR.logOut);
	}
	
	public void verifyTableValues(String name,String value,String page)
	{
		if(getElementText(name+" label@xpath=//*[contains(text(),'"+name+"')]//parent::tr//td[2]").contains(value))
		{
			stepPassed("Verfied the "+name+" acc name is '"+value+"' is displayed in "+page+" page");
		}
		else
		{
			stepFailed("The "+name+" name value is not '"+value+"' in "+page+" page");
		}
	}
	
	}