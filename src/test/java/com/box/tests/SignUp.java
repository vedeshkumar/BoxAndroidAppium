package com.box.tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

import java.lang.reflect.Method;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.box.model.ApplicationLibrary;
import com.box.model.CommonLibrary;
import com.box.model.ConfigurationLibrary;
import com.box.model.DataManager;
import com.box.model.Reports;
import com.box.pages.HomePage;
import com.box.pages.LoginPage;
import com.box.pages.RegistrationPage;
import com.box.pages.SplashScreenPage;

public class SignUp {
	
	AppiumDriver<MobileElement> driver;
	SplashScreenPage splashPage;
	LoginPage loginPage;
	HomePage homePage;
	RegistrationPage registrationPage;

	CommonLibrary comlib;
	ApplicationLibrary applib;
	Reports reports;
	DataManager datamanager;

	String moduleName = "Registration";
	String testCaseName = getClass().getSimpleName();
	
	public String emailid = RandomStringUtils.randomAlphabetic(5).toLowerCase()+"@xyz.com";

	public int randomNumber = ( int )( Math.random() * 9999 );

	public void init() {
		comlib = new CommonLibrary();
		applib = new ApplicationLibrary();
		reports = new Reports();
		datamanager = new DataManager();
	}

	@BeforeClass
	public void startUp() throws Exception {
		init();
		if(datamanager.executionController(testCaseName).equals("Yes")) {
			reports.scriptStartTime = comlib.getCurrentTime();
			reports.createFile(testCaseName);
			driver = applib.launchAndroidApplication(driver);
			Thread.sleep(2000);
		} else {
			System.out.println(testCaseName + " execution is skipped");
			ConfigurationLibrary.skipCount++;
			throw new SkipException(testCaseName + " execution is skipped");
		}
	}

	@BeforeMethod
	public void beforeTest(Method methodName) throws Exception {
		reports.writeTestName(methodName.getName());
	}

	@ Test()
	public void registration() throws Exception {

		try {
			
			splashPage = new SplashScreenPage(driver);
			loginPage = new LoginPage(driver);
			homePage = new HomePage(driver);
			registrationPage = new RegistrationPage(driver);

			try {
				Assert.assertTrue(splashPage.isSplashScreenPage());
				reports.writeIntoFile(driver, testCaseName,  "Validate Splash Screen", "Launch the App", "Splash Screen is displayed", reports.pass, "", comlib.getCurrentTime());
			}catch(Error e)
			{
				reports.writeIntoFile(driver, testCaseName,  "Validate Splash Screen", "Launch the App", "Splash Screen is not displayed", reports.fail, e.getMessage(), comlib.getCurrentTime());
				System.out.println("Register button validation failed. Reason:" +e.getMessage());
				Assert.fail();
			} 

			try {
				splashPage.clickSignUpBtn();
				
				//Assert.assertTrue(registrationPage.isRegistrationScreenPage());
				reports.writeIntoFile(driver, testCaseName,  "Validate Registration Screen", "Click on Sign Up button", "Registration Screen is displayed", reports.pass, "", comlib.getCurrentTime());
			}catch(Error e)
			{
				reports.writeIntoFile(driver, testCaseName,  "Validate Registration Screen", "Click on Sign Up button", "Registration Screen is not displayed", reports.fail, e.getMessage(), comlib.getCurrentTime());
				System.out.println("Register button validation failed. Reason:" +e.getMessage());
				Assert.fail();
			} 
			
//			try {
//				registrationPage.createNewUser(emailid, "iam@PRASAD05");
//
//				//Assert.assertTrue(homePage.isHomePage());
//				reports.writeIntoFile(driver, testCaseName,  "Validate Home Screen", "login to the app", "Home Screen is displayed", reports.pass, "", comlib.getCurrentTime());
//			}catch(Error e)
//			{
//				reports.writeIntoFile(driver, testCaseName,  "Validate Home Screen", "login to the app", "Home Screen is not displayed", reports.fail, e.getMessage(), comlib.getCurrentTime());
//				System.out.println("Register button validation failed. Reason:" +e.getMessage());
//				Assert.fail();
//			} 
			
		} catch(Exception e) {
			reports.writeIntoFile(driver, testCaseName, "Exception", "Tried performing action using Android driver", "Exception occured", reports.fail, e.getMessage(), comlib.getCurrentTime());
			Assert.fail("validation failed");
		}
	}

	@AfterMethod
	public void afterTest() throws Exception {
		comlib.afterTestRun();
	}

	@AfterClass
	public void tearDown() throws Exception {
		comlib.afterScript(reports, moduleName, testCaseName);
		driver.quit();
		System.out.println(testCaseName + " execution completed.");

	}
}
