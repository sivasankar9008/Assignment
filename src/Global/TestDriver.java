package Global;

import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;
import Utilities.CommonKeywords;

public class TestDriver extends CommonApplicationKeywords {

	public static void main(String[] args) {
		try {
			CommonKeywords common = new CommonKeywords();
			common.kickOff();
			TestNG testng = new TestNG();
			List<String> suites = new ArrayList<String>();
			suites.add("./Config/testng.xml");
			testng.setOutputDirectory(testReportsDirectory + "/testng");
			testng.setTestSuites(suites);
			testng.run();
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try {
				deleteTempFiles();

			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

}
