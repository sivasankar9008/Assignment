package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.testng.Assert;

public class CommonKeywords {
	public static String am_pm, minutes, hour, seconds, year, month, day;
	public int testCaseStepNo = 0;
	String testStepReport = "";

	public static String getFrameworkConfigProperty(String keyword) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(".\\Config\\FrameworkConfiguration.properties"));
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found Exception thrown while getting value of " + keyword
					+ " from Test Configuration file");
		} catch (IOException e) {
			System.out
					.println("IO Exception thrown while getting value of " + keyword + " from Test Configuration file");
		}
		System.out.println(
				"Getting value of " + keyword + " from Test Configuration file : " + properties.getProperty(keyword));

		return properties.getProperty(keyword).trim();
	}

	public static void deleteTempFiles() {
		File directory = new File(SeleniumReusableKeywords.testReportsDirectory + "/testng");
		File directory1 = new File(SeleniumReusableKeywords.testReportsDirectory + "/testng" + "\\Suite");
		File directory2 = new File(SeleniumReusableKeywords.testReportsDirectory + "/testng" + "\\old");
		File directory3 = new File(SeleniumReusableKeywords.testReportsDirectory + "/testng" + "\\junitreports");
		File directory4 = new File(SeleniumReusableKeywords.testReportsDirectory + "/testng" + "\\old\\Suite");
		File file1 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\copyfile.xml");
		File file2 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\temp.xml");
		File file3 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\emailable-report.html");
		File file4 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\index.html");
		File file5 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\testng-results.xml");
		File file6 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\testng-failed.xml");
		File file7 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\testng.css");
		File file8 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\PdfReport.html");
		File file9 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\pdfReport.pdf");
		File file10 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\pdfReport1.pdf");
		File file11 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\TestCaseResults.pdf");
		File file12 = new File(SeleniumReusableKeywords.testReportsDirectory + "\\pdffile.xml");
		file1.delete();
		file2.delete();
		file3.delete();
		file4.delete();
		file5.delete();
		file6.delete();
		file7.delete();
		file8.delete();
		file9.delete();
		file10.delete();
		file11.delete();
		file12.delete();
		if (directory.isDirectory()) {

			String files[] = directory.list();
			for (String temp : files) {
				File fileDelete = new File(directory, temp);
				fileDelete.delete();
			}
			if (directory.list().length == 0) {
				directory.delete();

			} else {

			}
		}
		if (directory1.isDirectory()) {

			String files1[] = directory1.list();
			for (String temp1 : files1) {
				File fileDelete1 = new File(directory1, temp1);
				fileDelete1.delete();
			}
			if (directory1.list().length == 0) {
				directory1.delete();

			} else {

			}
		}
		if (directory2.isDirectory()) {

			String files1[] = directory2.list();
			for (String temp1 : files1) {
				File fileDelete1 = new File(directory2, temp1);
				fileDelete1.delete();
			}
			if (directory2.list().length == 0) {
				directory2.delete();

			} else {

			}
		}
		if (directory3.isDirectory()) {

			String files1[] = directory3.list();
			for (String temp1 : files1) {
				File fileDelete1 = new File(directory3, temp1);
				fileDelete1.delete();
			}
			if (directory3.list().length == 0) {
				directory3.delete();

			} else {

			}
		}
		if (directory4.isDirectory()) {

			String files1[] = directory4.list();
			for (String temp1 : files1) {
				File fileDelete1 = new File(directory4, temp1);
				fileDelete1.delete();
			}
			if (directory4.list().length == 0) {
				directory4.delete();

			} else {

			}
		}
		directory4.delete();
		directory1.delete();
		directory2.delete();
		directory3.delete();
		directory.delete();
	}

	public void kickOff() {
		createTestReportDirectory();
		try {
			ConfigPropertiesFiles.createPropertiesFiles();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void cleanupFiles() {

		deleteTempFiles();

	}

	public static void testCaseFailed() {

		Assert.fail("Hello");

	}

	public void closeExcelSheet() {
		closeExcelSheet();
	}

	public void writeTestStepReport(String report, String currentMachineName, String currentTestCaseName) {
		try {
			Writer output;
			for (String machineNames : SeleniumReusableKeywords.testReportsDir) {
				if (machineNames.contains(currentMachineName)) {
					if (report.contains("<B></B>") || report.toLowerCase().contains("dataset")) {
						testCaseStepNo = 0;
						if (report.contains("<B></B>")) {
							testStepReport = "";
						}

					}
					testStepReport = testStepReport
							.replace("<center><a href='..\\" + currentMachineName
									+ ".html'>back</a></center></body></html>", "")
							+ report.replace("stepNo", testCaseStepNo + ".");
					PrintWriter writer = new PrintWriter(machineNames + "./" + currentTestCaseName + ".html");
					writer.print("");
					writer.close();
					output = new BufferedWriter(
							new FileWriter(machineNames + "./" + currentTestCaseName + ".html", true)); // clears
																										// file
																										// every
																										// time

					output.write( testStepReport);
					// output.write(testStepReport);
					output.write(
							"<center><a href='..\\" + currentMachineName + ".html'>back</a></center></body></html>");
					if (!(report.contains("TestFailure"))) {
						testCaseStepNo++;
					}

					output.close();
				}

			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void transferLogos() {

		try {
			for (int i = 0; i < SeleniumReusableKeywords.ipName.size(); i++) {
				(new File(SeleniumReusableKeywords.testReportsDirectory + "\\Logos\\")).mkdir();

				File sourcecompanyLogo = new File(
						".\\" + CommonKeywords.getFrameworkConfigProperty("Vayana_logo_path"));
				File sourceclientLogo = new File(".\\" + CommonKeywords.getFrameworkConfigProperty("client_logo_path"));
				File designationcompanyLogo = new File(SeleniumReusableKeywords.testReportsDirectory + "\\"
						+ CommonKeywords.getFrameworkConfigProperty("Vayana_logo_path"));
				File designationclientLogo = new File(SeleniumReusableKeywords.testReportsDirectory + "\\"
						+ CommonKeywords.getFrameworkConfigProperty("client_logo_path"));
				InputStream in = new FileInputStream(sourcecompanyLogo);
				OutputStream out = new FileOutputStream(designationcompanyLogo);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				InputStream in1 = new FileInputStream(sourceclientLogo);
				OutputStream out1 = new FileOutputStream(designationclientLogo);
				byte[] buf1 = new byte[1024];
				int len1;
				while ((len1 = in1.read(buf1)) > 0) {
					out1.write(buf1, 0, len1);
				}
				in1.close();
				out1.close();
			}
		}

		catch (FileNotFoundException ex) {

			System.out.println(ex.getMessage() + " in  the specified directory.");
			System.exit(0);

		}

		catch (IOException e) {

			System.out.println(e.getMessage());

		}

	}

	public static void transferFiles() {

		try {
			for (int i = 0; i < SeleniumReusableKeywords.ipName.size(); i++) {
				(new File(SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\")).mkdir();
				(new File(SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\css")).mkdir();
				(new File(SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\fonts")).mkdir();
				(new File(SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\js")).mkdir();
				transferFile(".\\Extras\\js\\bootstrap.min.js",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\js\\bootstrap.min.js");
				transferFile(".\\Extras\\js\\common.js",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\js\\common.js");
				transferFile(".\\Extras\\js\\jquery-1.9.1.js",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\js\\jquery-1.9.1.js");
				transferFile(".\\Extras\\js\\npm.js",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\js\\npm.js");
				transferFile(".\\Extras\\fonts\\glyphicons-halflings-regular.eot",
						SeleniumReusableKeywords.testReportsDirectory
								+ "\\Extras\\fonts\\glyphicons-halflings-regular.eot");
				transferFile(".\\Extras\\fonts\\glyphicons-halflings-regular.svg",
						SeleniumReusableKeywords.testReportsDirectory
								+ "\\Extras\\fonts\\glyphicons-halflings-regular.svg");
				transferFile(".\\Extras\\fonts\\glyphicons-halflings-regular.ttf",
						SeleniumReusableKeywords.testReportsDirectory
								+ "\\Extras\\fonts\\glyphicons-halflings-regular.ttf");
				transferFile(".\\Extras\\fonts\\glyphicons-halflings-regular.woff",
						SeleniumReusableKeywords.testReportsDirectory
								+ "\\Extras\\fonts\\glyphicons-halflings-regular.woff");
				transferFile(".\\Extras\\fonts\\glyphicons-halflings-regular.woff2",
						SeleniumReusableKeywords.testReportsDirectory
								+ "\\Extras\\fonts\\glyphicons-halflings-regular.woff2");
				transferFile(".\\Extras\\fonts\\OpenSans-Regular.eot",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\fonts\\OpenSans-Regular.eot");
				transferFile(".\\Extras\\fonts\\OpenSans-Regular.ttf",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\fonts\\OpenSans-Regular.ttf");
				transferFile(".\\Extras\\fonts\\OpenSans-Semibold.eot",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\fonts\\OpenSans-Semibold.eot");
				transferFile(".\\Extras\\fonts\\OpenSans-Semibold.ttf",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\fonts\\OpenSans-Semibold.ttf");
				transferFile(".\\Extras\\css\\bootstrap.min.css",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\css\\bootstrap.min.css");
				transferFile(".\\Extras\\css\\bootstrap.min.css.map",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\css\\bootstrap.min.css.map");
				transferFile(".\\Extras\\css\\font-awesome.min.css",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\css\\font-awesome.min.css");
				transferFile(".\\Extras\\css\\style.css",
						SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\css\\style.css");

			}

		}

		catch (Exception ex) {

			System.out.println(ex.getMessage());
			System.exit(0);

		}

	}

	public static void transferFile(String sourceFile, String designationFile) {
		try {
			File source = new File(sourceFile);
			File designation = new File(designationFile);
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(designation);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void transferCssFile() {

		try {
			for (int i = 0; i < SeleniumReusableKeywords.ipName.size(); i++) {
				(new File(SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\")).mkdir();

				File source = new File(".\\Extras\\style.css");
				File designation = new File(SeleniumReusableKeywords.testReportsDirectory + "\\Extras\\style.css");
				InputStream in = new FileInputStream(source);
				OutputStream out = new FileOutputStream(designation);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		}

		catch (FileNotFoundException ex) {

			System.out.println(ex.getMessage() + " in  the specified directory.");
			System.exit(0);

		}

		catch (IOException e) {

			System.out.println(e.getMessage());

		}

	}

	public static void createTestReportDirectory() {
		File curdir = new File(".");

		Calendar calendar = new GregorianCalendar();
		hour = ("0" + calendar.get(Calendar.HOUR));
		hour = hour.substring(hour.length() - 2);
		minutes = ("0" + calendar.get(Calendar.MINUTE));
		minutes = minutes.substring(minutes.length() - 2);
		seconds = ("0" + calendar.get(Calendar.SECOND));
		seconds = seconds.substring(seconds.length() - 2);
		year = "" + calendar.get(Calendar.YEAR);
		month = ("0" + (calendar.get(Calendar.MONTH) + 1));
		month = month.substring(month.length() - 2);
		day = ("0" + calendar.get(Calendar.DAY_OF_MONTH));
		day = day.substring(day.length() - 2);
		if (calendar.get(Calendar.AM_PM) == 0)
			am_pm = "AM";
		else
			am_pm = "PM";

		try {
			SeleniumReusableKeywords.testReportsDirectory = curdir.getCanonicalPath() + "\\TestReports\\" + day + "_"
					+ month + "_" + year + "_" + hour + "_" + minutes + "_" + seconds + "_" + am_pm;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(
					"IO Error while creating Output Directory : " + SeleniumReusableKeywords.testReportsDirectory);
		}

	}

}
