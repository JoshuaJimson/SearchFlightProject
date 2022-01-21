package pageObjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import automation.selenium.CommonFunctions;

public class SearchFlightsPage {

	public WebDriver driver;
	WebElement element;
	List<WebElement> elements;
	CommonFunctions cf = new CommonFunctions(driver);

	String radioButtonReviewXpath = "//button[text()='Search']/../..//button/div[.='trip']";

	By serverDownLabel = By
			.xpath("//*[contains(text(),'Sorry our servers are stumped with your request. Please check your url.')]");
    By moreOptionsLink = By.xpath("//*[contains(text(),'More options at the same price')]");
	By modifyDeparture = By.xpath("//input[@data-testid='modifyDeparture']");
	By modifyArrival = By.xpath("//input[@data-testid='modifyArrival']");
	By radioButtonReview = By.xpath("//button[text()='Search']/../..//button/div[.='trip']");
	By searchButton = By.xpath("//button[text()='Search']");
	By stopsLabel = By.xpath("//*[@class='ms-grid-column-3 ms-grid-row-2']/p");
	By numberOfTravelers = By
			.xpath("(//input[@data-testid='modifyDeparture']/../../../..//div[@class='p-relative']/button/div)[2]");
	By calendarButton = By.xpath("//div[text()='Return']/../../button/div");
	By searchResultsList = By.xpath("//div[@data-testid='tupple']/../..");
	By flightResultsDivElements = By.xpath("//div[@data-testid='tupple']");
	By flightResultsDisplayed = By.xpath("//b[@class='fs-inherit c-inherit']");
	By aboutUsLink = By.xpath("//a[@title='About Us']");
	String checkBox = "(//p[text()='how']/../../../div)[1]";
	
	By flightsDepartureTime = By.xpath("//*[@class='ms-grid-column-1 ms-grid-row-1']//*[@class='m-0 fs-5 fw-400 c-neutral-900']");
	By ArrivalDepartureTime = By.xpath("//*[@class='ms-grid-column-2 ms-grid-row-1']//*[@class='m-0 fs-5 fw-400 c-neutral-900']");

	public SearchFlightsPage(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement getServerDownLabel(WebDriver driver) {
		if (!cf.isElementPresent(serverDownLabel, driver, "Server down message"))
			element = null;
		else {
			element = driver.findElement(serverDownLabel);
			try {
				Assert.assertTrue(element.isDisplayed());
			} catch (Exception e) {
			}
		}
		return element;
	}

	public WebElement getModifyDeparture(WebDriver driver) {
		element = driver.findElement(modifyDeparture);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getModifyArrival(WebDriver driver) {
		element = driver.findElement(modifyArrival);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getRadioButtonReview(WebDriver driver, String trip) {
		String[] target = { "trip" };
		String[] replacement = { trip };
		String xpath = cf.replaceXpathVariables(radioButtonReviewXpath, target, replacement);
		if (cf.isElementPresent(By.xpath(xpath), driver, trip)) {
			element = driver.findElement(By.xpath(xpath));

			try {
				Assert.assertTrue(element.isDisplayed());
			} catch (Exception e) {
			}
		} else
			element = null;
		return element;
	}
	
	public WebElement getCheckBox(WebDriver driver, String Input) {
		String[] target = { "how" };
		String[] replacement = { Input };
		String xpath = cf.replaceXpathVariables(checkBox, target, replacement);
		
		JavascriptExecutor je = (JavascriptExecutor) driver;
		
		if (cf.isElementPresent(By.xpath(xpath), driver, Input)) {
			element = driver
					.findElement(By.xpath(xpath));
			je.executeScript("arguments[0].scrollIntoView(true);", element);

			try {
				Assert.assertTrue(element.isDisplayed());
			} catch (Exception e) {
			}
		} else
			element = null;
		return element;
	}

	public WebElement getSearchButton(WebDriver driver) {
		element = driver.findElement(searchButton);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getNumberOfTravelers(WebDriver driver) {
		element = driver.findElement(numberOfTravelers);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getCalendarButton(WebDriver driver) {
		element = driver.findElement(calendarButton);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getFlightsDisplayed(WebDriver driver) {
		element = driver.findElement(flightResultsDisplayed);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public int getNumberOfExtraOptions(WebDriver driver) throws InterruptedException{
		elements = driver.findElements(moreOptionsLink);
		int total = 0;
		for (WebElement oElement : elements){
			String[] arraySubStrings = oElement.getText().split(" ");
			int value = Integer.parseInt(arraySubStrings[0].substring(1));
			total+=value;
			Thread.sleep(2000);
		}
		return total;
	}

	public int getTotalFlightsDisplayed(WebDriver driver){
		WebElement element = getFlightsDisplayed(driver);
		String elementsDisplayed = cf.getTextFromElement(element,
					"Number of Flights Displayed");
			String[] b = elementsDisplayed.split(" ");
			int lastElement = Integer.parseInt(b[2]);
			return lastElement;
	}

	public int getNumberOfRowsAfterSearch(WebDriver driver, int lastElement) throws InterruptedException {
		elements = driver.findElements(flightResultsDivElements);
		int num = elements.size();

		cf.scrollDownPage(lastElement,driver);
		elements = driver.findElements(flightResultsDivElements);
		num = elements.size();
		return num;
	}
	
	public String[] getAllDepartureTime(WebDriver driver, int lastElement) throws InterruptedException {


		cf.scrollDownPage(lastElement,driver);
		elements = driver.findElements(flightsDepartureTime);
		String[] deptTime = new String[lastElement];
		for(int i=0;i<lastElement;i++) {
			deptTime[i]=elements.get(i).getText();
		}
		return deptTime;
	}
	
	public String[] getAllArrivalTime(WebDriver driver, int lastElement) throws InterruptedException {


		cf.scrollDownPage(lastElement,driver);
		elements = driver.findElements(ArrivalDepartureTime);
		String[] arrivalTime = new String[lastElement];
		for(int i=0;i<lastElement;i++) {
			arrivalTime[i]=elements.get(i).getText();
		}
		return arrivalTime;
	}
	
	public String compareDepAndArrivalTime(String dept,String arrv) {

		String time="";
		float deptTime=Float.parseFloat(dept);
		float arrivalTime=Float.parseFloat(arrv);
		if(deptTime>16 && arrivalTime<=22)
			time="Evening";
		

		return time;
	}

	public HashSet<String> getStopsList(WebDriver driver){
		ArrayList<String> listOfStops = new ArrayList<String>();
		HashSet<String> uniqueStops;
		elements = driver.findElements(stopsLabel);
		for(WebElement oElement : elements){
			listOfStops.add(oElement.getText().toString());
		}
		uniqueStops = new HashSet<String>(listOfStops);
		return uniqueStops;	
	}
}
