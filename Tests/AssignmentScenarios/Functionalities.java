
package AssignmentScenarios;

import Global.COR;

public class Functionalities extends Common_Utility
{

	public void verifyAccountDetails()
	{
		String accNo=getTextData("lblAccountNumber");
		String amount=getTextData("lblAmount");
		switchToContext(COR.webViewContextName);
		clickElement(OR.accounts);
		if(isElementDisplayed(accNo+"xpath@//span[text()='"+accNo+"']/parent::td/parent::tr/parent::table/tr[5]//span[text(),'"+amount+"']"))
		{
			stepPassed("Verified the "+accNo+" account no is displayed with correct balance -"+amount);
		}
		else
		{
			stepFailed("The "+accNo+" account no is not displayed with correct balance -"+amount);
		}
	}
	public void verifyBeneficiaryListnameorAcc()
	{
		String accNo=getTextData("lblAccountNumber");
		String name=getTextData("lblName");
		String typeBene=getTextData("drpBenetype");
		switchToContext(COR.webViewContextName);
		clickElement(OR.fundTransfer);
		selectTextOptionFromDropdown(OR.selectdropdown, typeBene);
		verifyTableValues(name, accNo, "beneficiarypage");
	}
}

