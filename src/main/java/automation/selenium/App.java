package automation.selenium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class App {
	public WebDriver driver;
	public Properties properties;
    private static Logger log = LogManager.getLogger(App.class.getName());

	public WebDriver initializeDriver(String browser) throws Exception {
		{
			String driverPath;
			String path = System.getProperty("user.dir");
			if (browser.equalsIgnoreCase("chrome")) {
				driverPath = PropertyManager.getInstance().getChromeDriverPath();
				System.setProperty("webdriver.chrome.driver", path + driverPath);
				driver = new ChromeDriver();
				log.info("ChromeDriver initialized for this test execution...");
			}
			else if (browser.equalsIgnoreCase("edge")) {
				driverPath = PropertyManager.getInstance().getEdgeDriverPath();
				System.setProperty("webdriver.edge.driver", path + driverPath);
				driver = new EdgeDriver();
				log.info("EdgeDriver initialized for this test execution...");
			}
			else{
				//If no browser passed throw exception
				throw new Exception("Browser is not correct");
			}
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	public String getScreenShotPath(String testCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destinationFile = System.getProperty("user.dir") + "\\reports\\" + testCaseName + ".png";
		FileUtils.copyFile(source, new File(destinationFile));
		return destinationFile;
	}
}
