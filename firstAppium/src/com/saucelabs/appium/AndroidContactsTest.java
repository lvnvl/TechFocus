package com.saucelabs.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.customized.appium.util.XMLUtil;

import java.io.File;
import java.net.URL;
import java.util.List;

public class AndroidContactsTest {
    private AppiumDriver<WebElement> driver;

    @Before
    public void setUp() throws Exception {
        // set up appium
        File classpathRoot = new File(System.getProperty("user.dir"));
//        System.out.println("classpathRoot is "+classpathRoot.toString());
        File appDir = new File(classpathRoot, "/apps/ContactManager");
        File app = new File(appDir, "ContactManager.apk");
        System.out.println("appPath is "+app.getAbsolutePath());
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","testAppium23");
        capabilities.setCapability("platformVersion", "4.4");
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("appPackage", "com.example.android.contactmanager");
        capabilities.setCapability("appActivity", ".ContactManager");
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        String xml = driver.getPageSource();
        System.out.println("xml is :"+xml);
        System.out.println("包含节点数量为"+ XMLUtil.dealWithXML(xml).size());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void addContact(){
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        WebElement el = driver.findElement(By.xpath(".//*[@text='Add Contact']"));
        el.click();

        String xml = driver.getPageSource();
        System.out.println("xml is :"+xml);
        System.out.println("包含节点数量为"+ XMLUtil.dealWithXML(xml).size());
        List<WebElement> textFieldsList = driver.findElementsByClassName("android.widget.EditText");
        textFieldsList.get(0).sendKeys("Some Name");
        textFieldsList.get(2).sendKeys("Some@example.com");
        driver.swipe(100, 500, 100, 100, 2);
        driver.findElementByXPath(".//*[@text='Save']").click();
    }

}
