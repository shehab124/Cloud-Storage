package com.example.Cloud.Storage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;


import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
     * Test that verifies that the home page is not accessible without logging in.
     */
	@Test void homeNotAccessible()
	{
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
     * Test that signs up a new user, logs that user in, verifies that they can access the home page,
     * then logs out and verifies that the home page is no longer accessible.
     */
	@Test void homeNotAccessibleAfterLogout()
	{
		doMockSignUp("logout","Test","logout","123");
		doLogIn("logout", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-btn")));
		WebElement logoutBtn = driver.findElement(By.id("logout-btn"));
		logoutBtn.submit();

		homeNotAccessible();
	}

	/**
     * creates a note and verifies that the note details are visible in the note list.
     */
	@Test void addNote()
	{
		// sign up and login
		try
		{
			doMockSignUp("addNote", "addNote", "addNote", "123");
		}
		catch (Exception e)
		{
			System.out.println("add note already signed up");
		}
		doLogIn("addNote", "123");

		// get note page
		driver.get("http://localhost:" + this.port + "/home?fragment=nav-notes");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// find add note btn
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note")));
		WebElement addNoteBtn = driver.findElement(By.id("add-note"));
		addNoteBtn.click();

		// insert data
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement title = driver.findElement(By.id("note-title"));
		title.click();
		title.sendKeys("hello");

		WebElement desc = driver.findElement(By.name("noteDescription"));
		desc.click();
		desc.sendKeys("My description");

		WebElement save = driver.findElement(By.id("save-changes-btn"));
		save.click();

		driver.navigate().back();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("1")));
		WebElement row = driver.findElement(By.id("1"));
		row.findElement(By.xpath(".//*[contains(text(), 'My description')]"));
	}

	/**
     * Test that logs in an existing user with existing notes,
	 * clicks the edit note button on an existing note,
     * changes the note data, saves the changes, and verifies that the changes appear in the note list.
     */
	@Test void updateNote()
	{
		addNote();

		// get note page
		driver.get("http://localhost:" + this.port + "/home?fragment=nav-notes");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// find edit btn
		WebElement editBtn = driver.findElement(By.xpath("//tr[@id='1']//button[@class='btn btn-success']"));
		editBtn.click();

		// update title
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement title = driver.findElement(By.id("note-title"));
		title.click();
		title.sendKeys("updated");

		// update desc
		WebElement desc = driver.findElement(By.name("noteDescription"));
		desc.click();
		desc.sendKeys("Update Desc");

		// click save
		WebElement save = driver.findElement(By.id("save-changes-btn"));
		save.click();

		driver.navigate().back();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("1")));
		WebElement row = driver.findElement(By.id("1"));
		row.findElement(By.xpath(".//*[contains(text(), 'Update Desc')]"));
	}

	/**
     * Test that logs in an existing user with existing notes,
	 * clicks the delete note button on an existing note,
	 * and verifies that the note no longer appears in the note list.
     */
	@Test
	void deleteNote()
	{
		addNote();

		// get note page
		driver.get("http://localhost:" + this.port + "/home?fragment=nav-notes");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// find delete btn
		WebElement deleteBtn = driver.findElement(By.xpath("//tr[@id='1']//a[@class='btn btn-danger']"));
		deleteBtn.click();

		driver.navigate().back();

		Assertions.assertThrows(NoSuchElementException.class, ()-> driver.findElement(By.id("1")));

	}

	/**
     * test that logs in an existing user,
	 * creates a credential and verifies that the credential details are visible in the credential list.
     */
	@Test
	void addCredential()
	{
		try{
			doMockSignUp("Cred", "Cred", "Cred", "Cred");

		}
		catch (Exception e)
		{
			System.out.println("Cred already signed up");
		}
		doLogIn("Cred", "Cred");

		// get credentials page
		driver.get("http://localhost:" + this.port + "/home?fragment=nav-credentials");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// find add cred btn
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential-btn")));
		WebElement addCredBtn = driver.findElement(By.id("add-credential-btn"));
		addCredBtn.click();

		// insert data
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement url = driver.findElement(By.id("credential-url"));
		url.click();
		url.sendKeys("www.gmail.com");

		WebElement username = driver.findElement(By.id("credential-username"));
		username.click();
		username.sendKeys("Chehab");

		WebElement password = driver.findElement(By.id("credential-password"));
		password.click();
		password.sendKeys("123");

		WebElement save = driver.findElement(By.id("save-changes"));
		save.click();

		driver.navigate().back();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("1")));
		WebElement row = driver.findElement(By.id("1"));
		row.findElement(By.xpath(".//*[contains(text(), 'Chehab')]"));
	}

	@Test
	void updateCredential()
	{
		addCredential();

        // get credentials page
        driver.get("http://localhost:" + this.port + "/home?fragment=nav-credentials");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

        // find edit btn
        WebElement editBtn = driver.findElement(By.xpath("//tr[@id='1']//button[@class='btn btn-success']"));
        editBtn.click();

        // insert data
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement url = driver.findElement(By.id("credential-url"));
        url.click();
        url.sendKeys("www.gmail.com");

        WebElement username = driver.findElement(By.id("credential-username"));
        username.click();
        username.sendKeys("Updated");

        WebElement password = driver.findElement(By.id("credential-password"));
        password.click();
        password.sendKeys("Updated");

        WebElement save = driver.findElement(By.id("save-changes"));
        save.click();

        driver.navigate().back();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("1")));
        WebElement row = driver.findElement(By.id("1"));
        row.findElement(By.xpath(".//*[contains(text(), 'Updated')]"));
	}

    @Test
    void deleteCredential()
    {
        addCredential();

        // get credentials page
        driver.get("http://localhost:" + this.port + "/home?fragment=nav-credentials");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

        // find delete btn
        WebElement deleteBtn = driver.findElement(By.xpath("//tr[@id='1']//a[@class='btn btn-danger']"));
        deleteBtn.click();

        driver.navigate().back();

        Assertions.assertThrows(NoSuchElementException.class, ()-> driver.findElement(By.id("1")));
    }

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	public void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	public void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling redirecting users
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		Assertions.assertEquals("http://localhost:" + this.port + "/signup", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling bad URLs
	 * gracefully, for example with a custom error page.
	 *
	 * Read more about custom error pages at:
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");

		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
	 * rest of your code.
	 * This test is provided by Udacity to perform some basic sanity testing of
	 * your code to ensure that it meets certain rubric criteria.
	 *
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code.
	 *
	 * Read more about file size limits here:
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}
}
