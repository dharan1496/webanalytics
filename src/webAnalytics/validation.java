package webAnalytics;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.javascript.host.Map;



public class validation {
	
	public static void validate(HashMap<String, String> data) throws Exception {
		FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\DELL\\eclipse-workspace\\webAnalytics\\InputData\\inputData.xlsx"));
		XSSFWorkbook wb = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = wb.getSheet("submitSite");
		HashMap<String, String> input = new HashMap<String, String>();
		int columnCount = sheet.getRow(0).getLastCellNum();
		for(int i=0; i<columnCount; i++) {
			input.put(sheet.getRow(0).getCell(i).getStringCellValue(), sheet.getRow(1).getCell(i).getStringCellValue());
		}
		System.out.println(input);
		for(String j: input.keySet()) {
			String value = data.get(j);
			if(value.equals(input.get(j))){
				System.out.println(j+" tag is correct");
			}else {
				System.out.println(j+" tag is wrong");
			}
		}
		wb.close();
	}
}
