package webAnalytics;

import java.io.File;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import junit.framework.Assert;





public class validation {
	
	@SuppressWarnings("deprecation")
	public static void validate(HashMap<String, String> data) throws Exception {
		//fetching value from data sheet and put it in hash map and validate it
		XSSFWorkbook wb = new XSSFWorkbook(new File(".\\InputData\\inputData.xlsx"));
		XSSFSheet sheet = wb.getSheet("submitSite");
		HashMap<String, String> input = new HashMap<String, String>();
		int columnCount = sheet.getRow(0).getLastCellNum();
		for(int i=0; i<columnCount; i++) {
			input.put(sheet.getRow(0).getCell(i).getStringCellValue(), sheet.getRow(1).getCell(i).getStringCellValue());
		}
		System.out.println(input);
		wb.close();
		for(String j: input.keySet()) {
			String value = data.get(j);
			Assert.assertEquals(value, input.get(j));
		}
	}
}
