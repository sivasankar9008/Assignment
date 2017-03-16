package Utilities;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import jxl.Sheet;

public class ConfigPropertiesFiles extends JXlKeywords {
	public static String tcName;
	public static String tcDescription;
	public static String tcModuleName;
	public static List<String> machineNames = new ArrayList<String>();
	static List<String> tcNames = new ArrayList<String>();
	public static HashMap<String, String> allTcDescription = new HashMap<String, String>();
	public static HashMap<String, String> allTcModuleName = new HashMap<String, String>();

	public static void createPropertiesFiles() {
		Properties properties = new Properties();
		int browserNo = 0;
		List<String> allMachineDeviceName = new ArrayList<String>(browserNo);
		List<String> allHostName = new ArrayList<String>(browserNo);
		List<String> portNo = new ArrayList<String>(browserNo);
		List<String> allMachineBrowserName = new ArrayList<String>(browserNo);
		List<String> operatingSystem = new ArrayList<String>(browserNo);
		List<String> browserVersionName = new ArrayList<String>(browserNo);
		List<String> operatingSystemVersionName = new ArrayList<String>(browserNo);
		List<String> testDataSheetNo = new ArrayList<String>(browserNo);

		try {
			JXlKeywords dd = new JXlKeywords();
			dd.useExcelSheet("./data/FrameworkConfiguration.xls", 0);
			Sheet readsheet = dd.w.getSheet(0);

			for (int i = 1; i < readsheet.getRows(); i++) {
				String Keyword = readsheet.getCell(1, i).getContents();
				String value = readsheet.getCell(2, i).getContents();
				properties.setProperty(Keyword, value);

			}

			properties.store(new FileOutputStream("./config/FrameworkConfiguration.properties"), null);
			dd.w.close();

			dd.useExcelSheet("./data/FrameworkConfiguration.xls", 1);
			Sheet sheet1 = dd.w.getSheet(1);
			for (int index = 1; index < sheet1.getRows(); index++) {
				if (sheet1.getCell(8, index).getContents().equalsIgnoreCase("yes")) {
					allMachineDeviceName.add(sheet1.getCell(1, index).getContents());
					machineNames.add(sheet1.getCell(1, index).getContents());
					allHostName.add(sheet1.getCell(2, index).getContents());
					portNo.add(sheet1.getCell(3, index).getContents());
					operatingSystem.add(sheet1.getCell(4, index).getContents());
					allMachineBrowserName.add(sheet1.getCell(6, index).getContents());
					browserVersionName.add(sheet1.getCell(7, index).getContents());
					operatingSystemVersionName.add(sheet1.getCell(5, index).getContents());
					testDataSheetNo.add(sheet1.getCell(9, index).getContents());
					// urls.add(sheet1.getCell(10, index).getContents());

				}

			}
			dd.w.close();

			browserNo = allMachineDeviceName.size();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("suite");
			doc.appendChild(rootElement);
			rootElement.setAttribute("name", "Suite");
			rootElement.setAttribute("parallel", "tests");
			int id = 1;
			for (int brow = 1; brow <= browserNo; brow++) {
				String idS = "" + id++;
				Element test = doc.createElement("test");
				test.setAttribute("name", allMachineDeviceName.get(brow - 1).toString());
				test.setAttribute("id", idS);

				Element classes = doc.createElement("classes");
				classes.setAttribute("name", idS);

				Element classs = doc.createElement("class");
				classs.setAttribute("name", "TestCases.TestCases");
				classs.setAttribute("id", idS);

				Element methods = doc.createElement("methods");
				methods.setAttribute("id", idS);

				Element parameter0 = doc.createElement("parameter");
				parameter0.setAttribute("name", "selenium.machinename");
				parameter0.setAttribute("value", allMachineDeviceName.get(brow - 1).toString());

				Element parameter1 = doc.createElement("parameter");
				parameter1.setAttribute("name", "selenium.host");
				parameter1.setAttribute("value", allHostName.get(brow - 1).toString());
				Element parameter2 = doc.createElement("parameter");
				parameter2.setAttribute("name", "selenium.port");
				parameter2.setAttribute("value", portNo.get(brow - 1).toString());
				Element parameter3 = doc.createElement("parameter");
				parameter3.setAttribute("name", "selenium.App");
				parameter3.setAttribute("value", allMachineBrowserName.get(brow - 1).toString());
				Element parameter5 = doc.createElement("parameter");
				parameter5.setAttribute("name", "selenium.os");
				parameter5.setAttribute("value", operatingSystem.get(brow - 1).toString());
				parameter5.setAttribute("id", idS);
				Element parameter7 = doc.createElement("parameter");
				parameter7.setAttribute("name", "selenium.osVersion");
				parameter7.setAttribute("value", operatingSystemVersionName.get(brow - 1).toString());
				parameter7.setAttribute("id", idS);
				Element parameter8 = doc.createElement("parameter");
				parameter8.setAttribute("name", "selenium.sheetNo");
				parameter8.setAttribute("value", testDataSheetNo.get(brow - 1).toString());
				parameter8.setAttribute("id", idS);
				rootElement.appendChild(test);
				test.appendChild(parameter0);
				test.appendChild(parameter1);
				test.appendChild(parameter2);
				test.appendChild(parameter3);
				test.appendChild(parameter5);
				test.appendChild(parameter7);
				test.appendChild(parameter8);
				test.appendChild(classes);
				classes.appendChild(classs);
				classes.appendChild(methods);

				dd.useExcelSheet(CommonKeywords.getFrameworkConfigProperty("Tc_Settings_Excelpath"), 0);
				Sheet readsheet1 = dd.w.getSheet(0);
				for (int i = 1; i < readsheet1.getRows(); i++) {
					String value = readsheet1.getCell(5, i).getContents();
					tcName = readsheet1.getCell(3, i).getContents();
					tcDescription = readsheet1.getCell(4, i).getContents();
					tcModuleName = readsheet1.getCell(1, i).getContents();
					if (value.trim().equalsIgnoreCase("Yes")) {

						Element include = doc.createElement("include");
						methods.appendChild(include);
						include.setAttribute("name", tcName);
						if (!(tcNames.contains(tcName))) {
							tcNames.add(tcName);
						}

						allTcDescription.put(tcName, tcDescription);
						allTcModuleName.put(tcName, tcModuleName);

					}

					else if (value.trim().equalsIgnoreCase("No")) {
						Element exclude = doc.createElement("exclude");
						methods.appendChild(exclude);
						exclude.setAttribute("name", tcName);

					} else {
						if (!value.trim().equalsIgnoreCase("")) {
							System.out.println("Warnin!!Invalid/Empty Execution flag");
						}

					}
				}
			}
			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer transformer = tff.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource xmlSource = new DOMSource(doc);
			StreamResult outputTarget = new StreamResult(".\\config\\testng.xml");
			transformer.transform(xmlSource, outputTarget);
			dd.w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}