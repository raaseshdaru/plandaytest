package plandaytest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Testcase {

	ChromeDriver driver;
	
	//Page locators

	public static final String TEST_URL = "https://test1234.planday.com/";
	By usernameLocator = By.id("Username");
	By passwordLocator = By.id("Password");
	By loginButtonLocator = By.id("MainLoginButton");
	By scheduleLocator = By.xpath("//a[@href='/schedule']");
	By validationMessageLocator = By.className("validation-message");
	By iframeLocator = By.xpath("//body/div[@id='root']/div[1]/iframe[1]");
	By employeeCountLocator = By.xpath("//*[@class='row-header3']");
	By nextWeekInterval = By.xpath("//*[@class='button--right date-bar__button']");
	By todayButtonLocator = By.xpath("//button[text()='Today']");
	By addShiftLocator =  By.xpath("//div[contains(@class,'virtualized-board__row')][2]/div[contains(@class,'board__cell')][5]");
	By shiftStartTime = By.id("shiftStartEnd_start");
	By shiftEndTime = By.id("shiftStartEnd_end");
	By createButtonLocator = By.xpath("/html/body/div[6]/div/div/div/div[6]/div/div/ul/li[2]/button");
	By shiftCreatedLocator = By.xpath("//div[contains(@class,'virtualized-board__row')][2]");
	
	//User login method
	private void handleLogin() {
		driver.findElement(usernameLocator).sendKeys("plandayqa@outlook.com");
		driver.findElement(passwordLocator).sendKeys("APItesting21");
		driver.findElement(loginButtonLocator).click();
	}

	@BeforeMethod

	public void setUp() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(TEST_URL);
		driver.findElement(By.className("cookie-banner__button")).click();

	}

	@AfterMethod

	public void tearDown() {
		driver.quit();

	}

	@Test(description = "Test 1 Verifies if username, password and login button fields are displayed")
	public void VerifyLoginFieldsAvailability() {

		Assert.assertTrue(driver.findElement(usernameLocator).isDisplayed());
		Assert.assertTrue(driver.findElement(passwordLocator).isDisplayed());
		Assert.assertTrue(driver.findElement(loginButtonLocator).isDisplayed());

	}

	@Test(description = "Test 2 Verify if invalid login credentials produces validation message")
	public void VerifyIncorrectLoginCredentialsError() {

		boolean isValidationMessageDisplayed = false;
		driver.findElement(usernameLocator).sendKeys("xyz@gmail.com");
		driver.findElement(passwordLocator).sendKeys("1234");
		driver.findElement(loginButtonLocator).click();

		if (driver.findElements(validationMessageLocator).size() != 0) {
			isValidationMessageDisplayed = true;
		}
		Assert.assertTrue(isValidationMessageDisplayed);

	}

	@Test(description = "Test 3 Verify successful login")
	public void VerifyLogin() {
		handleLogin();
		Assert.assertEquals(driver.getTitle(), "Planday");
	}

	@Test(description = "Test 4,5 Verify click on schdeule redirects to the correct /schedule page")
	public void VerifyRedirectUrlContainsSchedule() {
		handleLogin();
		driver.findElement(scheduleLocator).click();
		Assert.assertTrue(driver.getCurrentUrl().contains("/schedule"));
	}

	@Test(description = "Test 6 Count and seert that the number of employees displayed is 3")
	public void CountAndAssertNumberOfEmployees() {

		handleLogin();
		driver.findElement(scheduleLocator).click();
		WebElement iframe = driver.findElement(iframeLocator);
		driver.switchTo().frame(iframe);

		int size = driver.findElements(employeeCountLocator).size();
		Assert.assertEquals((size - 1), 3);

	}

	@Test(description = "Test 7,8, 9 Verify shift created is visible on the schedule grid")
	public void VerifyShiftShownOnScheduleGrid() {

		handleLogin();

		driver.findElement(scheduleLocator).click();

		WebElement iframe = driver.findElement(iframeLocator);
		driver.switchTo().frame(iframe);

		driver.findElement(nextWeekInterval).click();

		driver.findElement(todayButtonLocator).click();
		driver.findElement(addShiftLocator).click();
		driver.findElement(shiftStartTime).sendKeys("9:00");
		driver.findElement(shiftEndTime).sendKeys("17:00");
		driver.findElement(createButtonLocator).click();
		WebElement ele = driver.findElement(shiftCreatedLocator);
		Assert.assertTrue(ele.getText().contains("Shift"));

	}

}
