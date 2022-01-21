package automation.selenium;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG {

	static ExtentReports extentReportObject;
	
	public static ExtentReports getExtentReportObject() {
		
		String path = System.getProperty("user.dir")+"\\reports\\index.html";
		ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter(path);
		extentSparkReporter.config().setReportName("Automation POC");
		extentSparkReporter.config().setDocumentTitle("ClearTrip - Test Automation results");
		
		extentReportObject = new ExtentReports();
		extentReportObject.attachReporter(extentSparkReporter);
		return extentReportObject;
		
	}
}
