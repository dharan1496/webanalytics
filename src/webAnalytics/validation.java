package webAnalytics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import junit.framework.Assert;

public class validation {
	static HashMap<String, String> input;

	public static void fetchInput() throws InvalidFormatException, IOException {
		// fetching value from data sheet and put it in hash map and validate it
		XSSFWorkbook wb = new XSSFWorkbook(new File(".\\InputData\\inputData.xlsx"));
		XSSFSheet sheet = wb.getSheet("submitSite");
		input = new HashMap<String, String>();
		int columnCount = sheet.getRow(0).getLastCellNum();
		for (int i = 0; i < columnCount; i++) {
			input.put(sheet.getRow(0).getCell(i).getStringCellValue(), sheet.getRow(1).getCell(i).getStringCellValue());
		}
		System.out.println(input);
		wb.close();
	}

	@SuppressWarnings("deprecation")
	public static void validate(HashMap<String, String> data) throws Exception {
		for (String j : input.keySet()) {
			String value = data.get(j);
			Assert.assertEquals(value, input.get(j));
		}
	}
}
