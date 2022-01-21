package automation.selenium;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;



public class ListenersTest extends App implements ITestListener{
	ExtentTest test;
	ExtentReports extent= ExtentReporterNG.getExtentReportObject();
	//ExtentReports extent=new ExtentReports();
	ThreadLocal<ExtentTest> extentTest =new ThreadLocal<ExtentTest>();
	WebDriver driver = null;
	String methodName= null;
	
	@Override
	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);
		takeScreenshot(result);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		try {
			
			test.pass("Test Passed",MediaEntityBuilder.createScreenCaptureFromPath(getScreenShotPath(result.getMethod().getMethodName(), driver)).build());
		}catch(IOException e) {
			e.printStackTrace();
		}
		extentTest.get().log(Status.PASS, "Test passed successfully");
		takeScreenshot(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
//		extentTest.get().log(Status.FAIL, "Test failed!!!");
		try {
			test.fail(result.getThrowable().getMessage(),MediaEntityBuilder.createScreenCaptureFromPath(getScreenShotPath(result.getMethod().getMethodName(), driver)).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		takeScreenshot(result);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		extentTest.get().log(Status.SKIP, "Test skipped!");
		try {
			test.skip(result.getThrowable().getMessage(),MediaEntityBuilder.createScreenCaptureFromPath(getScreenShotPath(result.getMethod().getMethodName(), driver)).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		takeScreenshot(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
	}
	
	public void takeScreenshot(ITestResult result) {
		methodName = result.getMethod().getMethodName().toString();
		try {
		driver=(WebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());		
			getScreenShotPath(methodName, driver);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
