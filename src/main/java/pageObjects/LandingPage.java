package pageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import automation.selenium.App;
import automation.selenium.CommonFunctions;

public class LandingPage extends App {

	public WebDriver driver;
	WebElement element;
	List<WebElement> elements;
	CommonFunctions cf = new CommonFunctions(driver);

	String radioButtonXPath = "//*[contains(text(),'label')]";
	String dayPickerButtonXpath = "//*[contains(text(),'monthAndYear')]/../..//div[@class='DayPicker-Day']//div[.='date']";

	By adultsCount = By.xpath("//div[@class='mb-4']/select");
	By childCount = By.xpath("//div[@class='mb-4']/following-sibling::div[2]/select");
	By infantsCount = By.xpath("//p[text()='Below 2 yrs']/../select");
	By fromDropdownWebElement = By.xpath("//*[contains(@placeholder,'Any worldwide city or airport')]");
	By toDropdownWebElement = By.xpath("(//*[contains(@placeholder,'Any worldwide city or airport')])[2]");
	By fromDropdownItems = By.xpath("//h4[contains(text(),'From')]/..//li/p");
	By toDropdownItems = By.xpath("//h4[contains(text(),'To')]/..//li/p");
	By departOnButton = By.xpath("//*[contains(@class,'homeCalender')]//button/div");
	By rightArrowButton = By.xpath("//*[@data-testid='rightArrow']");
	By searchFlightsButton = By.xpath("//button[text()='Search flights']");
	By errorDepArrNotSelected = By.xpath("//*[contains(text(),'Select Departure and Arrival airports/cities.')]");
	By errorDepArrNotSelectedClose = By.xpath(
			"//*[contains(text(),'Select Departure and Arrival airports/cities.')]/..//*[contains(@class,'br-100')]");
	By cookiesNotification = By.xpath("//p[contains(text(),'cookies')]/..//div");

	public LandingPage(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement getCookiesNotification(WebDriver driver) {
		if (cf.isElementPresent(cookiesNotification, driver, "Cookies notification")) {
			element = driver.findElement(cookiesNotification);
			try {
				Assert.assertTrue(element.isDisplayed());
			} catch (Exception e) {
			}
		}
		else{
			element = null;
		}
		return element;
	}

	public WebElement getErrorDepArrNotSelectedClose(WebDriver driver) {
		element = driver.findElement(errorDepArrNotSelectedClose);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getRadioButton(WebDriver driver, String label) {
		String[] target = { "label" };
		String[] replacement = { label };
		element = driver.findElement(By.xpath(cf.replaceXpathVariables(radioButtonXPath, target, replacement)));
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getAdultsDropdown(WebDriver driver) {
		element = driver.findElement(adultsCount);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getChildDropdown(WebDriver driver) {
		element = driver.findElement(childCount);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getInfantDropdown(WebDriver driver) {
		element = driver.findElement(infantsCount);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getDepartOnButton(WebDriver driver) {
		element = driver.findElement(departOnButton);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getRightArrowButton(WebDriver driver) {
		element = driver.findElement(rightArrowButton);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getDay(WebDriver driver, String monthYear, String date) {
		JavascriptExecutor je = (JavascriptExecutor) driver;

		String[] target = { "monthAndYear", "date" };
		String[] replacement = { monthYear, date };
		String path = cf.replaceXpathVariables(dayPickerButtonXpath, target, replacement);
		if (cf.isElementPresent(By.xpath(path), driver, "Month and Year")) {
			element = driver.findElement(By.xpath(path));
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			je.executeScript("arguments[0].scrollIntoView(true);",element);
			return element;
		} else
			return null;
	}

	public void setDate(WebElement rightArrow, WebDriver driver, String monthYear, String date) {
		while (getDay(driver, monthYear, date) == null) {
			cf.clickObject(rightArrow, "right arrow button");
		}
		cf.clickObject(getDay(driver, monthYear, date), "day");
	}

	public WebElement getFromDropdown(WebDriver driver) {
		element = driver.findElement(fromDropdownWebElement);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public List<WebElement> getValuesOfFromDropdown(WebDriver driver) {
		elements = driver.findElements(fromDropdownItems);
		return elements;
	}

	public WebElement getToDropdown(WebDriver driver) {
		element = driver.findElement(toDropdownWebElement);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public List<WebElement> getValuesOfToDropdown(WebDriver driver) {
		elements = driver.findElements(toDropdownItems);
		return elements;
	}

	public WebElement getSearchFlights(WebDriver driver) {
		element = driver.findElement(searchFlightsButton);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}

	public WebElement getErrorDepArrivalNotDisplayed(WebDriver driver) {
		element = driver.findElement(rightArrowButton);
		try {
			Assert.assertTrue(element.isDisplayed());
		} catch (Exception e) {
		}
		return element;
	}
}
