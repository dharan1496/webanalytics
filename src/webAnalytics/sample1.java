package webAnalytics;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class sample1 {
	
	static HashMap<String, String> tagsMap;
	public static void main(String[] args) throws Exception {
		// simple page (without many resources so that the output is
		// easy to understand
		String url = "https://www.awwwards.com/submit/";

		tracing(url);
	}

	@SuppressWarnings("deprecation")
	private static void tracing(String url) throws Exception {
		ChromeDriver driver = null;

		try {
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\DELL\\Desktop\\chromedriver.exe");

			DesiredCapabilities cap = DesiredCapabilities.chrome();
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
			cap.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

			Map<String, Object> perfLogPrefs = new HashMap<String, Object>();
			perfLogPrefs.put("traceCategories", "browser,devtools.timeline,devtools"); // comma-separated trace
																						// categories
			ChromeOptions option = new ChromeOptions();
			option.setExperimentalOption("w3c", false);
			option.setExperimentalOption("perfLoggingPrefs", perfLogPrefs);
			cap.setCapability(ChromeOptions.CAPABILITY, option);
			driver = new ChromeDriver(cap);

			// navigate to the page
			System.out.println("Navigate to " + url);
			driver.get(url);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			By by = By.xpath("//span[contains(text(),'SUBMIT')]");
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			driver.findElement(by).click();

			String currentURL = driver.getCurrentUrl();

			// then ask for all the performance logs from this request
			// one of them will contain the Network.responseReceived method
			// and we shall find the "last recorded url" response
			System.out.println("****************");
			System.out.println(driver.manage().logs().getAvailableLogTypes().toString());
			System.out.println("****************");
			LogEntries logs = driver.manage().logs().get("performance");
//			System.out.println("\nList of log entries:\n");
			for (Iterator<LogEntry> it = logs.iterator(); it.hasNext();) {
				LogEntry entry = it.next();

				try {
					JSONObject json = new JSONObject(entry.getMessage());
//					System.out.println(json.toString());
					JSONObject message = json.getJSONObject("message");
					String method = message.getString("method");
					String methodName = "Network.responseReceived";

					if (method != null) {
						if (methodName.equalsIgnoreCase(method)) {
//							System.out.println(method);
							JSONObject params = message.getJSONObject("params");

							// response
							JSONObject response = params.getJSONObject("response");
//							System.out.println(response.toString());

							// headers
//							JSONObject headers = (JSONObject) response.get("headers");
							JSONObject headers = response.getJSONObject("headers");
//							System.out.println(headers.toString());

							// server
							String server = headers.getString("server");
//							System.out.println(server);

							if (server.contains("roxygen-bolt")) {
								
								//url
//								System.out.println(response.getString("url"));
								String requestURL = decode(response.getString("url"));
								if (requestURL.contains("?")) {
									System.out.println(response.toString());
									String[] list = requestURL.split("\\?")[1].split("&");
									tagsMap = new HashMap<String, String>();
									for (String i : list) {
										String[] tag = i.split("=");
										try {
											tagsMap.put(tag[0], tag[1]);
										} catch (ArrayIndexOutOfBoundsException e) {
											tagsMap.put(tag[0], "");
										}
									}
									System.out.println(tagsMap);
//									for(String a: tagsMap.keySet()) {
//										System.out.println("key: "+a+" value: "+tagsMap.get(a));

//									}
									//validation
									validation.validate(tagsMap);
								}
							}
						}
					}

				} catch (JSONException e) {

//					e.printStackTrace();
				}

			}

		} finally {
			if (driver != null) {
				driver.quit();
			}
		}
	}

	public static String decode(String url) {
		try {
			String prevURL = "";
			String decodeURL = url;
			while (!prevURL.equals(decodeURL)) {
				prevURL = decodeURL;
				decodeURL = URLDecoder.decode(decodeURL, "UTF-8");
			}
			return decodeURL;
		} catch (UnsupportedEncodingException e) {
			return "Issue while decoding" + e.getMessage();
		}
	}
}
