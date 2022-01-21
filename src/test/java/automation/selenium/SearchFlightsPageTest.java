package automation.selenium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import pageObjects.LandingPage;
import pageObjects.SearchFlightsPage;

public class SearchFlightsPageTest extends App {

	public WebDriver driver;
	CommonFunctions cf = new CommonFunctions(driver);
	LandingPage lp = new LandingPage(driver);
	SearchFlightsPage sf = new SearchFlightsPage(driver);

	@BeforeTest
	@Parameters ("browser")
	public void initialize(String browser) throws Exception {
		driver = initializeDriver(browser);
		driver.manage().deleteAllCookies();
		cf.openBrowserWithURL(PropertyManager.getInstance().getAppURL(), driver);
	}

	@DataProvider(name = "Test_Inputs")
	public String[][] getSearchFlightsTestData() throws IOException {
		// Prepare the path of excel file
		String filePath = System.getProperty("user.dir") + "\\data";
		int rowCount = 0, columnCount = 0;
		rowCount = cf.getRowCount(filePath, "SearchFlights.xlsx", "TestValues");
		columnCount = cf.getColumnCount(filePath, "SearchFlights.xlsx", "TestValues");
		String[][] inputData = new String[rowCount][columnCount];
		// Call read file method of the class to read data
		inputData = cf.readExcel(filePath, "SearchFlights.xlsx", "TestValues");
		return inputData;
	}

	@Test(dataProvider = "Test_Inputs")
	public void searchFlights(String trip, String fromCountryCode, String fromAirportName, String fromPlaceCode,
			String fromPlaceName, String toCountryCode, String toAirportName, String toPlaceCode, String toPlaceName,
			String depMonthYear, String depDate, String adults, String children, String infants, String stops,
			String departureTime) throws IOException, InterruptedException {

		// Set value for trip, String stops, String departureTimee
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

		// Click on the Search Flights button
		cf.clickObject(lp.getSearchFlights(driver), "Search Flights");

		if (sf.getServerDownLabel(driver) == null) {

			SoftAssert softAssert = new SoftAssert();

			// scroll down the page
			cf.scrollDownPage(sf.getTotalFlightsDisplayed(driver), driver);

			// get the unique value of Stops checkboxes
			HashSet<String> listOfStops = sf.getStopsList(driver);

			for (String s : listOfStops) {
				softAssert.assertTrue(sf.getCheckBox(driver, s).isDisplayed(),
						"The checkbox for " + s + " is displayed");

			}
			
			//Scroll up the page
			cf.scrollUpPage(driver);

			// click on the stop checkbox
			cf.clickObject(sf.getCheckBox(driver, stops), "Search Flights");
			
			Thread.sleep(3000);
			
			HashSet<String> currentlistOfStops = sf.getStopsList(driver);

			for (String s : currentlistOfStops) {
				softAssert.assertEquals(stops,s,"The values displayed note based on the selection");

			}


			// click on the Departure time checkbox
			cf.clickObject(sf.getCheckBox(driver, departureTime), "Search Flights");

//			Thread.sleep(3000);
//			int lastElement=sf.getTotalFlightsDisplayed(driver);
//			ArrayList<String> listOfUniqueTime = new ArrayList<String>();
//			HashSet<String> UniqueTime;
//			String[] alldeptTime=sf.getAllDepartureTime(driver, lastElement);
//			String[] allarrivalTime=sf.getAllArrivalTime(driver, lastElement);
//			
//			for(int i=0;i<lastElement;i++)
//			{
//				String currentListOfTime=sf.compareDepAndArrivalTime(alldeptTime[i], allarrivalTime[i]);
//				listOfUniqueTime.add(currentListOfTime);
//			}
//			
//			UniqueTime = new HashSet<String>(listOfUniqueTime);
//			
//			
//			System.out.println(UniqueTime);
//			System.out.println(departureTime);
//
//						
//			cf.scrollDownPage(sf.getTotalFlightsDisplayed(driver), driver);
//			
//			currentlistOfStops = sf.getStopsList(driver);
//
//			for (String s : currentlistOfStops) {
//				softAssert.assertNotEquals(stops,s,"The values displayed note based on the selection");
//
//			}

			softAssert.assertAll();

		} else
			throw new SkipException("This test is skipped as the server is down during test execution");

	}

}
