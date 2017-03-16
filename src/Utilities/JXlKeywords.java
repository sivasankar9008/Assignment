package Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class JXlKeywords extends CommonKeywords {
	public int testCaseDataRow = 0;
	public ArrayList<String> testCaseNames = new ArrayList<String>();
	public int dataRowNo = 0;
	public String currentTestCaseName = "";
	public static int rowCountNo, colCountNo;
	public int rowCount = 0, colCount = 0, maxRows = 100;
	public String wkbook = "";
	public File nf;
	public Workbook w;
	public int tempNo = 1;
	public Sheet s;
	public int testDataSheetNo = 0;
	public String currentExcelBook, currentExcelSheet;
	public List<Integer> iterationCount = new ArrayList<Integer>();

	public void useExcelSheet(String wkbook, int sheetNo) {
		currentExcelBook = wkbook;
		nf = new File(currentExcelBook);
		if (nf.exists()) {
			try {
				w = Workbook.getWorkbook(nf);
			} catch (BiffException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			s = w.getSheet(sheetNo);
			updateTestRowCount();
			updateTestColCount();
		}
	}

	public String getData(int row, int col) {
		Cell c;
		c = s.getCell(col - 1, row - 1);
		return c.getContents();
	}

	public void loadTestData() {
		useExcelSheet(getFrameworkConfigProperty("InputDataFile"), (testDataSheetNo - 1));
		Sheet readsheet = w.getSheet((testDataSheetNo - 1));
		for (int i = 0; i < readsheet.getRows(); i++) {
			String testCaseName = readsheet.getCell(0, i).getContents();
			testCaseNames.add(testCaseName);

		}
	}

	public int getTestDataRowNo(String testCaseName) {
		int rowNo = 0;
		loadTestData();
		for (String a : testCaseNames) {

			if (a.trim().equalsIgnoreCase(testCaseName.trim())) {

				rowNo = rowNo + 1;
				break;
			}
			rowNo++;
		}
		return rowNo;

	}

	public String getTextData(String Label) {
		return getTextData(dataRowNo, Label);
	}

	public String getTextData(int datasetNo, String colLabel) {

		return getData(datasetNo, returnColNo(getTestDataRowNo(currentTestCaseName), colLabel));

	}

	public int returnColNo(int datasetNo, String colLabel) {
		boolean flag = true;
		int temp = 0;
		while (flag) {

			try {
				temp++;
				if (getData(datasetNo, temp).trim().equalsIgnoreCase(colLabel.trim())) {
					flag = false;
					return temp;
				}

			} catch (Exception e) {
				CommonKeywords.testCaseFailed();
				break;
			}
		}
		return 0;
	}

	public void updateTestRowCount() {
		boolean flag = true;
		int temp = 0;
		while (flag) {
			temp++;
			try {
				if (getData(temp, 1).trim().length() == 0) {
					flag = false;
				}
			} catch (Exception e) {
				rowCountNo = temp - 2;
				break;
			}
		}

	}

	public void updateTestColCount() {
		boolean flag = true;
		int temp = 0;
		while (flag) {
			temp++;
			try {
				if (getData(1, temp).trim().length() == 0) {
					flag = false;
				}
			} catch (Exception e) {
				colCountNo = temp - 1;
				break;
			}
		}
	}

	public void closeExcelSheet() {
		currentExcelBook = "";
		w.close();
		nf = null;
	}

	public void textCaseDataRowCount() {
		try {
			testCaseDataRow = getTestDataRowNo(currentTestCaseName);
			for (int i = 1;; i++) {
				if (getData(testCaseDataRow + i, 2).equalsIgnoreCase("yes")) {
					iterationCount.add(Integer.valueOf(testCaseDataRow + i));
				} else {
					if (getData(testCaseDataRow + i, 2).equalsIgnoreCase("")) {
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void testDataRowNo(int no) {
		testCaseDataRow = no;
	}
}
