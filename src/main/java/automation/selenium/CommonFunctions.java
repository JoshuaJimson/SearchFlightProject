package automation.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class CommonFunctions extends App {

	public WebDriver driver;
	private static Logger log = LogManager.getLogger(CommonFunctions.class.getName());
	private XSSFWorkbook workbook;
	private XSSFSheet workSheet;
	private XSSFCell cell;

	public CommonFunctions(WebDriver driver) {
		this.driver = driver;
	}

	//Open the browser with a URL passed in the parameter
	public void openBrowserWithURL(String url, WebDriver driver) {
		driver.get(url);
		driver.manage().window().maximize();
		logMessage("pass","Browser is opened with URL: "+ url);
	}

	//Click on the object that is passed as parameter
	public void clickObject(WebElement element, String objectName) {
		try {
			if (element.isDisplayed()) {
				element.click();
				logMessage("pass", "Successfully clicked "+ objectName );
			}
		} catch (NoSuchElementException e) {
			logMessage("fail", "Failed to find "+ objectName);
			e.printStackTrace();
		}
	}

	//Type the value passed as parameter to an object
	public void sendKeysToObject(WebElement element, String objectName, String input) {
		try {
			if (element.isDisplayed()) {
				element.clear();
				element.sendKeys(input);
				logMessage("pass", "Successfully typed "+ input +"into "+ objectName );
			}
		} catch (NoSuchElementException e) {
			logMessage("fail", "Failed to find "+ objectName);
			e.printStackTrace();
		}
	}

	//Read data from Excel sheet
	public String[][] readExcel(String filePath, String fileName, String sheetName) throws IOException {
		// Create an object of File class to open .xlsx file
		File file = new File(filePath + "\\" + fileName);
		// Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		workbook = new XSSFWorkbook(inputStream);
		workSheet = workbook.getSheet(sheetName);

		// Find number of rows in excel file
		int rowCount = workSheet.getLastRowNum() - workSheet.getFirstRowNum();
		// Find number of columns in a row
		Row row = workSheet.getRow(0);
		String[][] tabArray = new String[workSheet.getLastRowNum()][row.getLastCellNum()];

		// Create a loop over all the rows of excel file to read it
		for (int i = 1; i <= rowCount; i++) {

			// Create a loop to print cell values in a row
			for (int j = 0; j < row.getLastCellNum(); j++) {
				// Store values in array
				tabArray[i - 1][j] = getCellData(i, j);
			}
		}
		
		logMessage("pass", "The values in the excel sheet is successfully stored in array.");
		return tabArray;
	}

	//Get the value present in a cell in an excel sheet
	public String getCellData(int rowNum, int colNum) {
		try {
			cell = workSheet.getRow(rowNum).getCell(colNum);
			String cellData = cell.getStringCellValue();
			return cellData;
		} catch (Exception e) {
			logMessage("fail", "Failed to get value from the Excel sheet");
			throw (e);
		}
	}

	//Get the count of rows in an excel sheet
	public int getRowCount(String filePath, String fileName, String sheetName) throws IOException {

		// Create an object of File class to open .xlsx file
		File file = new File(filePath + "\\" + fileName);
		// Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		workbook = new XSSFWorkbook(inputStream);
		workSheet = workbook.getSheet(sheetName);

		// Find number of rows in excel file
		int rowCount = workSheet.getLastRowNum() - workSheet.getFirstRowNum();
		logMessage("pass", "The number of rows in the sheet is "+ rowCount);
		return rowCount;
	}

	//Get the count of columns in an excel sheet
	public int getColumnCount(String filePath, String fileName, String sheetName) throws IOException {
		// Create an object of File class to open .xlsx file
		File file = new File(filePath + "\\" + fileName);
		// Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		workbook = new XSSFWorkbook(inputStream);
		workSheet = workbook.getSheet(sheetName);
		Row row = workSheet.getRow(0);
		int columnCount = row.getLastCellNum();
		logMessage("pass", "The number of columns in the sheet is "+ columnCount);
		return columnCount;
	}

	//Set value to a dropdown with <Select> tags
	public void selectDropdownItem(WebElement element, String objectName, String input) {
		try {
			if (element.isDisplayed()) {
				Select s = new Select(element);
				s.selectByValue(input);
				logMessage("pass", "Successfully selected value "+input+" from the dropdown");
			}
		} catch (NoSuchElementException e) {
			logMessage("fail", "Failed to select value "+ input +" from the dropdown");
			e.printStackTrace();
		}

	}

	//select the value from the dropdown that matches the value passed as parameter
	public void clickDropdownItem(List<WebElement> elements, String objectName, String input) {

		for (WebElement o : elements) {
			if (o.getText().equals(input)) {
				o.click();
				break;
			}
		}

	}

	//verifies if the element passed in the parameter is present on the screen
	public boolean isElementPresent(By locatorKey, WebDriver driver, String objectName) {

		if (driver.findElements(locatorKey).size() > 0) {
			logMessage("pass", "Found at least one element which matches "+objectName);
			return true;
		}
		else {
			logMessage("info", "Failed to find at least one element which matches "+objectName);
			return false;
		}
	}

	//Replaces the target values in xpath with the replacement values
	public String replaceXpathVariables(String xpath, String[] target, String[] replacement) {
		int arraySize = target.length;
		String finalXpath = null;

		for (int i = 0; i < arraySize; i++) {
			finalXpath = xpath.replace(target[i], replacement[i]);
			xpath = finalXpath;
			logMessage("pass", "Replaced label "+target[i]+" in the xpath with value "+ replacement[i]);
		}
		return xpath;
	}
	
	//a simple log message depending on the status (pass/fail)
	public void logMessage(String status,String message) {
		if(status.equalsIgnoreCase("pass"))
			log.info(message);
		else if(status.equalsIgnoreCase("fail"))
			log.error(message);
	}

	//To get the value from an input element
	public String getValueFromElement(WebElement element, String objectName) {
		String text = null;
		try {
			if (element.isDisplayed()) {
				text = element.getAttribute("value");
				logMessage("pass", "Successfully obtained value "+ text +"from "+ objectName );
			}
		} catch (NoSuchElementException e) {
			logMessage("fail", "Failed to get value from "+ objectName);
			e.printStackTrace();
		}
		return text;
	}
	
	//To get the innertext of an element
	public String getTextFromElement(WebElement element, String objectName) {
		String text = null;
		try {
			if (element.isDisplayed()) {
				text = element.getText();
				logMessage("pass", "Successfully obtained value "+ text +"from "+ objectName );
			}
		} catch (NoSuchElementException e) {
			logMessage("fail", "Failed to get value from "+ objectName);
			e.printStackTrace();
		}
		return text;
	}

	//To scroll down a page
	public void scrollDownPage(int lastElement, WebDriver driver) throws InterruptedException{
		int i = ((lastElement/21)*4)+2;
		Actions act = new Actions(driver);
		for (int x=0;x<i;x++){
			act.sendKeys(Keys.PAGE_DOWN).perform();
			Thread.sleep(2000);
		}
	}

	public void scrollUpPage(WebDriver driver) throws InterruptedException{
		Actions act = new Actions(driver);
		act.keyDown(Keys.CONTROL).sendKeys(Keys.HOME).build().perform();
		act.keyUp(Keys.CONTROL).perform();
		Thread.sleep(2000);
	}
}
