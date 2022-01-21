package stepdefinition;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import automation.selenium.App;
import automation.selenium.CommonFunctions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pageObjects.LandingPage;

public class Stepdef extends App{

	WebDriver driver = null;
	CommonFunctions cf = new CommonFunctions(driver);
	LandingPage lp = new LandingPage(driver);
	
	@Given("^I open browser with Clear Trip URL$")
	public void I_open_browser_with_Clear_Trip_URL() throws Exception {
		driver = initializeDriver("chrome");
	}

	@Given("^I select \"([^\"]*)\" as \"([^\"]*)\"$")
	public void I_select_Trip(String element, String value){
		WebElement ele = null;
		if(element.equalsIgnoreCase("trip")){
			ele = lp.getRadioButton(driver, value);
		}
		cf.clickObject(ele, element);
	}
}
