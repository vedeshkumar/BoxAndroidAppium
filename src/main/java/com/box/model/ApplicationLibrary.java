package com.box.model;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;


public class ApplicationLibrary {

	public void logout(CommonLibrary comlib, Reports reports, WebDriver driver, String testCaseName) throws InterruptedException
	{
		driver.findElement(By.id(TestData.btn_signout)).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath(TestData.txt_xp_logout)).click();

		try {
			Assert.assertTrue(comlib.isElementPresent(driver, By.id(TestData.et_username)));
			Assert.assertTrue(comlib.isElementPresent(driver, By.id(TestData.et_password)));
			Assert.assertTrue(comlib.isElementPresent(driver, By.id(TestData.btn_login)));
			System.out.println("Login Page Validated");
			reports.writeIntoFile(driver, testCaseName, "Validate Logout button", "Click on logout", "Login page is displayed", reports.pass, "", comlib.getCurrentTime());
		} catch (Error e) {
			reports.writeIntoFile(driver, testCaseName, "Validate Logout button", "Click on logout", "Login page is not displayed", reports.fail, e.getMessage(), comlib.getCurrentTime());
			Assert.fail("Logout button validation failed");
		}
	}

	public void help(WebDriver driver)
	{
		driver.findElement(By.id(TestData.btn_welcome)).click();	
		String parentHandle = driver.getWindowHandle(); // get the current window handle
		driver.findElement(By.xpath(TestData.txt_xp_help)).click(); // click some link that opens a new window

		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
		System.out.println("test");
		// close newly opened window when done with it
		driver.switchTo().window(parentHandle); 
	}

	
	/*
	 * Launch application in android device.
	 */
	public AppiumDriver<MobileElement> launchAndroidApplication(AppiumDriver<MobileElement> driver) throws Exception {
		
		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "/app");
		File app = new File(appDir, ConfigurationLibrary.fileName);
//
//		AppiumDriverLocalService appiumDriverLocalService;
//		
//		AppiumServiceBuilder builder = new AppiumServiceBuilder().withAppiumJS(new File("/usr/local/lib/node_modules/appium/bin/appium.js"))
//				 .withArgument(GeneralServerFlag.APP, app.getAbsolutePath()).withArgument(GeneralServerFlag.LOG_LEVEL, "info").usingAnyFreePort(); /*and so on*/;
//				         appiumDriverLocalService = builder.build();
//				         appiumDriverLocalService.start();


		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", ConfigurationLibrary.deviceName);
		capabilities.setCapability("appium-version", "1.4.0.0");
		capabilities.setCapability("platformName", "android");
		capabilities.setCapability("udid",ConfigurationLibrary.deviceName);
		capabilities.setCapability("platformVersion", ConfigurationLibrary.apiLevel);
		capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("appPackage", ConfigurationLibrary.packageName);
		capabilities.setCapability("appActivity", ConfigurationLibrary.activityName);
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
		//driver = new AndroidDriver<MobileElement>(appiumDriverLocalService.getUrl(),capabilities);
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		return driver;
	}


	public void login(CommonLibrary comlib, Reports reports, WebDriver driver, String testCaseName ,String username , String password)
	{
		try {
			//comlib.ExplicitWait(driver, By.id(Repository.et_username));
			Assert.assertTrue(comlib.isElementPresent(driver, By.id(TestData.et_username)));
			Assert.assertTrue(comlib.isElementPresent(driver, By.id(TestData.et_password)));
			Assert.assertTrue(comlib.isElementPresent(driver, By.id(TestData.btn_login)));
			reports.writeIntoFile(driver, testCaseName, "Validate Login page", "Open browser with URL", "Login page is displayed", reports.pass, "", comlib.getCurrentTime());
		} catch (Error e) {
			reports.writeIntoFile(driver, testCaseName, "Validate Login page", "Open browser with URL", "Login page is not displayed", reports.fail, e.getMessage(), comlib.getCurrentTime());
			Assert.fail("Login page validation failed");
		}

		driver.findElement(By.id(TestData.et_username)).clear();
		driver.findElement(By.id(TestData.et_username)).sendKeys(username);
		driver.findElement(By.id(TestData.et_password)).clear();
		driver.findElement(By.id(TestData.et_password)).sendKeys(password);
		driver.findElement(By.id(TestData.btn_login)).click();

		try {
			comlib.ExplicitWait(driver, By.id(TestData.btn_signout));
			Assert.assertTrue(comlib.isElementPresent(driver, By.id(TestData.btn_signout)));
			reports.writeIntoFile(driver, testCaseName, "Validate Home page", "Login with valid credentials", "Home page is displayed", reports.pass, "", comlib.getCurrentTime());
		} catch (Exception e) {
			reports.writeIntoFile(driver, testCaseName, "Validate Home page", "Login with valid credentials", "Home page is not displayed", reports.fail, e.getMessage(), comlib.getCurrentTime());
			Assert.fail("Home page validation failed");
		}

	}


	public void unlock(CommonLibrary comlib,WebDriver driver) throws InterruptedException
	{
		if(comlib.isElementPresent(driver, By.xpath(TestData.prgNote_msg_xp_lockedMsg)))
		{
			System.out.println("Record is locked");
			Thread.sleep(3000);
			driver.findElement(By.xpath(TestData.prgNote_btn_xp_allItems)).click();			
		}
		else				
		{
			System.out.println("Record is not locked");
		}	

	}

}
