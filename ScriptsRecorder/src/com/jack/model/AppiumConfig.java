package com.jack.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.android.ddmlib.IDevice;

import io.appium.java_client.android.AndroidDriver;

public class AppiumConfig {
	private File APKFile;
	private IDevice mDevice;
	private String mPackage;
	private String mActivity;
	private int port;
	public static AndroidDriver<WebElement> driver;
	
	

	public AppiumConfig() {
		super();
	}

	public AppiumConfig(File apkFile, IDevice device, String packageName, String activityName, int p) {
		// TODO Auto-generated constructor stub
		APKFile = apkFile;
		mDevice = device;
		mPackage = packageName;
		mActivity = activityName;
		port = p;
		try {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("platformVersion", "5.1");
			String udid = mDevice.getSerialNumber();
			capabilities.setCapability("deviceName", mDevice.getName().split("-")[1]);// 设备名（必填，可任意内容）
			if (udid != null)
				capabilities.setCapability("udid", udid);
			// 支持中文
			capabilities.setCapability("unicodeKeyboard", "True");
			capabilities.setCapability("resetKeyboard", "True");// 不需重置
			capabilities.setCapability("noSign", "True");
			capabilities.setCapability("app", apkFile.getAbsolutePath());
			capabilities.setCapability("appPackage", mPackage);
			capabilities.setCapability("appActivity", mActivity);
			capabilities.setCapability("newCommandTimeout", "7200");//session spire time during two command
			capabilities.setCapability("noReset", "True");//don't wipe the app's cache data

//			System.out.println("DesiredCapabilities:" + capabilities.toString());
			
			driver = new AndroidDriver<>(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
			if(!driver.isAppInstalled(mPackage)){
				System.out.println("app not installed!");
				driver.installApp(apkFile.getAbsolutePath());
			}
//			System.out.println("init android driver done!");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the aPKFile
	 */
	public File getAPKFile() {
		return APKFile;
	}

	/**
	 * @return the mDevice
	 */
	public IDevice getmDevice() {
		return mDevice;
	}

	/**
	 * @return the mPackage
	 */
	public String getmPackage() {
		return mPackage;
	}

	/**
	 * @return the mActivity
	 */
	public String getmActivity() {
		return mActivity;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the driver
	 */
	public static AndroidDriver<WebElement> getDriver() {
		return driver;
	}

	/**
	 * @param aPKFile the aPKFile to set
	 */
	public void setAPKFile(File aPKFile) {
		APKFile = aPKFile;
	}

	/**
	 * @param mDevice the mDevice to set
	 */
	public void setmDevice(IDevice mDevice) {
		this.mDevice = mDevice;
	}

	/**
	 * @param mPackage the mPackage to set
	 */
	public void setmPackage(String mPackage) {
		this.mPackage = mPackage;
	}

	/**
	 * @param mActivity the mActivity to set
	 */
	public void setmActivity(String mActivity) {
		this.mActivity = mActivity;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}	
}
