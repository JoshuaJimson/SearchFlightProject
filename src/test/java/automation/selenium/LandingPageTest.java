package automation.selenium;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import pageObjects.LandingPage;
import pageObjects.SearchFlightsPage;

public class LandingPageTest extends App {

	public WebDriver driver;
	CommonFunctions cf = new CommonFunctions(driver);
	LandingPage lp = new LandingPage(driver);
	SearchFlightsPage sf = new SearchFlightsPage(driver);

	@BeforeMethod
	@Parameters ("browser")
	public void initialize(String browser) throws Exception {
		driver = initializeDriver(browser);
		driver.manage().deleteAllCookies();
		cf.openBrowserWithURL(PropertyManager.getInstance().getAppURL(), driver);
	}

	@DataProvider(name = "SearchFlights_Oneway")
	public String[][] getSearchFlightsTestData() throws IOException {
		// Prepare the path of excel file
		String filePath = System.getProperty("user.dir") + "\\data";
		int rowCount = 0, columnCount = 0;
		rowCount = cf.getRowCount(filePath, "SearchFlights.xlsx", "Oneway");
		columnCount = cf.getColumnCount(filePath, "SearchFlights.xlsx", "Oneway");
		String[][] inputData = new String[rowCount][columnCount];
		// Call read file method of the class to read data
		inputData = cf.readExcel(filePath, "SearchFlights.xlsx", "Oneway");
		return inputData;
	}

	@DataProvider(name = "FromToDropdown")
	public String[][] getFromToDropdownTestData() throws IOException {
		// Prepare the path of excel file
		String filePath = System.getProperty("user.dir") + "\\data";
		int rowCount = 0, columnCount = 0;
		rowCount = cf.getRowCount(filePath, "SearchFlights.xlsx", "FromToDropdown");
		columnCount = cf.getColumnCount(filePath, "SearchFlights.xlsx", "FromToDropdown");
		String[][] inputData = new String[rowCount][columnCount];
		// Call read file method of the class to read data
		inputData = cf.readExcel(filePath, "SearchFlights.xlsx", "FromToDropdown");
		return inputData;
	}

	@Test(dataProvider = "SearchFlights_Oneway")
	public void searchFlights(String trip, String fromCountryCode, String fromAirportName, String fromPlaceCode,
			String fromPlaceName, String toCountryCode, String toAirportName, String toPlaceCode, String toPlaceName,
			String depMonthYear, String depDate, String adults, String children, String infants, String stops,
			String departureTime)
			throws IOException, InterruptedException {

		SoftAssert softAssertion = new SoftAssert();

		// Set value for trip, String stops, String departureTime
		cf.clickObject(lp.getRadioButton(driver, trip), trip);

		// Set value to From dropdown
		cf.clickObject(lp.getFromDropdown(driver), "click drop down");

		String from = fromPlaceName + ", " + fromCountryCode + " - " + fromAirportName + " (" + fromPlaceCode + ")";
		String to = toPlaceName + ", " + toCountryCode + " - " + toAirportName + " (" + toPlaceCode + ")";

		cf.sendKeysToObject(lp.getFromDropdown(driver), "From dropdown", from);
		Thread.sleep(5000);
		cf.clickDropdownItem(lp.getValuesOfFromDropdown(driver), "From dropdown", from);

		// Set value to To dropdown
		cf.clickObject(lp.getToDropdown(driver), "click drop down");
		cf.sendKeysToObject(lp.getToDropdown(driver), "To dropdown", to);
		Thread.sleep(5000);
		cf.clickDropdownItem(lp.getValuesOfToDropdown(driver), "To dropdown", to);

		// Set the date on the Calendar
		cf.clickObject(lp.getDepartOnButton(driver), "click on calendar");
		Thread.sleep(3000);
		lp.setDate(lp.getRightArrowButton(driver), driver, depMonthYear, depDate);

		// Set value for count of Adults
		cf.selectDropdownItem(lp.getAdultsDropdown(driver), "select adult", adults);

		// Set value for count of Children
		cf.selectDropdownItem(lp.getChildDropdown(driver), "select child", children);

		// Set value for count of Infants
		cf.selectDropdownItem(lp.getInfantDropdown(driver), "select infant", infants);

		// Click on the Search Flights button
		cf.clickObject(lp.getSearchFlights(driver), "Search Flights");

		if (sf.getServerDownLabel(driver) == null) {

			Thread.sleep(5000);

			// Get value from search reslts trip radio button
			softAssertion.assertNotNull(sf.getRadioButtonReview(driver, trip), "Trip value not matching the input!!");

			// Get value from search results fromdropDown
			String fromDropdown = fromPlaceCode + " - " + fromPlaceName + ", " + fromCountryCode;
			softAssertion.assertEquals(cf.getValueFromElement(sf.getModifyDeparture(driver), "From dropdown"),
					fromDropdown, "fromDropdown values not matching the input!!");

			// Get value from search results todropDown
			String toDropdown = toPlaceCode + " - " + toPlaceName + ", " + toCountryCode;
			softAssertion.assertEquals(cf.getValueFromElement(sf.getModifyArrival(driver), "To dropdown"), toDropdown,
					"toDropdown values not matching the input!!");

			// Get number of from search results
			int expected = Integer.parseInt(adults) + Integer.parseInt(children) + Integer.parseInt(infants);
			String n = cf.getTextFromElement(sf.getNumberOfTravelers(driver), "Number of travellers");
			String[] a = n.split(" ");
			int actual = Integer.parseInt(a[0]);
			softAssertion.assertEquals(actual, expected, "number of travellers not matching the input!!");

			int lastElement = sf.getTotalFlightsDisplayed(driver);
			int numberOfDivElements = sf.getNumberOfRowsAfterSearch(driver, lastElement);
			if (numberOfDivElements != lastElement) {
				int numberOfExtraOptions = sf.getNumberOfExtraOptions(driver);
				numberOfDivElements+=numberOfExtraOptions;
			}
			softAssertion.assertEquals(lastElement, numberOfDivElements,
					"number of Flights not matching the number of div elements!!");

			softAssertion.assertAll();
		} else
			throw new SkipException("This test is skipped as the server is down during test execution");
	}

	@Test(dataProvider = "FromToDropdown")
	public void validateFromToDropDowns(String fromCountryCode, String fromAirportName, String fromPlaceCode,
			String fromPlaceName, String toCountryCode, String toAirportName, String toPlaceCode, String toPlaceName,
			String stops, String departureTime)
			throws InterruptedException {

		String from = fromPlaceName + ", " + fromCountryCode + " - " + fromAirportName + " (" + fromPlaceCode + ")";
		String to = toPlaceName + ", " + toCountryCode + " - " + toAirportName + " (" + toPlaceCode + ")";

		if (lp.getCookiesNotification(driver) != null) {
			cf.clickObject(lp.getCookiesNotification(driver), "Cookies notification close button");
		}

		// Click on the Search Flights button before entering any value
		cf.clickObject(lp.getSearchFlights(driver), "Search Flights");

		if (cf.getValueFromElement(lp.getFromDropdown(driver), "From dropdown") == null
				&& cf.getValueFromElement(lp.getToDropdown(driver), "To dropdown") == null) {

			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOf(lp.getErrorDepArrivalNotDisplayed(driver)));
			if (lp.getErrorDepArrivalNotDisplayed(driver).isDisplayed())
				cf.clickObject(lp.getErrorDepArrNotSelectedClose(driver), "Close Error message button");
			else {
				cf.logMessage("fail", "Error message is not displayed");
			}
		}

		cf.sendKeysToObject(lp.getFromDropdown(driver), "From dropdown", from);
		Thread.sleep(5000);
		// wait.until(ExpectedConditions.visibilityOfAllElements(lp.getValuesOfFromDropdown(driver)));
		cf.clickDropdownItem(lp.getValuesOfFromDropdown(driver), "From dropdown", from);

		String expectedFromDescription = fromPlaceCode + " - " + fromPlaceName + ", " + fromCountryCode;
		String actualFromDescription = cf.getValueFromElement(lp.getFromDropdown(driver), "From dropdown");
		Assert.assertEquals(actualFromDescription, expectedFromDescription,
				"The From description after item is selected is incorrectly displayed as " + actualFromDescription);

		cf.sendKeysToObject(lp.getToDropdown(driver), "To dropdown", to);
		Thread.sleep(5000);
		// wait.until(ExpectedConditions.visibilityOfAllElements(lp.getValuesOfFromDropdown(driver)));
		cf.clickDropdownItem(lp.getValuesOfToDropdown(driver), "To dropdown", to);

		String expectedToDescription = toPlaceCode + " - " + toPlaceName + ", " + toCountryCode;
		String actualToDescription = cf.getValueFromElement(lp.getToDropdown(driver), "To dropdown");
		Assert.assertEquals(actualToDescription, expectedToDescription,
				"The To description after item is selected is incorrectly displayed as " + actualToDescription);
	}

	@Test
	public void validateDepartOnDate() {
		DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");
		Date date = new Date();
		String sysDate = dateFormat.format(date);
		String departOnDate = cf.getValueFromElement(lp.getDepartOnButton(driver), "Depart On date button");

		if (departOnDate != null) {
			if (sysDate.equals(departOnDate)) {
				cf.logMessage("pass", "The default depart on date is correctly displayed as " + departOnDate);
			} else {
				cf.logMessage("fail", "The depart on date is not set to today's date by default");
			}
		} else {
			cf.logMessage("fail", "The depart on date is null!!");
		}
	}

	@DataProvider(name = "SearchFlights")
	public String[][] getSearchFlightsData() throws IOException {
		// Prepare the path of excel file
		String filePath = System.getProperty("user.dir") + "\\data";
		int rowCount = 0, columnCount = 0;
		rowCount = cf.getRowCount(filePath, "SearchFlights.xlsx", "Oneway");
		columnCount = cf.getColumnCount(filePath, "SearchFlights.xlsx", "Oneway");
		String[][] inputData = new String[rowCount][columnCount];
		// Call read file method of the class to read data
		inputData = cf.readExcel(filePath, "SearchFlights.xlsx", "Oneway");
		return inputData;
	}

	@AfterMethod(alwaysRun = true)
	public void teardown() {
		driver.manage().deleteAllCookies();
		driver.close();
	}
}
