package com.customized.appium.util;

import java.io.File;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * 初始化相关
 * @author kaliwn
 *
 */
public class InitUtil {

	/**
	 * 初始化功能，测试已安装程序
	 * @param appPackage 程序包名
	 * @param appActivity 程序页面
	 * @param udid 设备号 null则默认
	 * @return
	 */
	public static DesiredCapabilities initAndroidCapabilitiesWithOutApk(String appPackage, String appActivity, String udid) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");

		capabilities.setCapability("deviceName", ""+udid);// 设备名（必填，可任意内容）
		if(udid!=null)
		capabilities.setCapability("udid", udid);

		// 支持中文
		capabilities.setCapability("unicodeKeyboard", "True");
		capabilities.setCapability("resetKeyboard", "False");//不需重置
		capabilities.setCapability("noSign", "True");

		capabilities.setCapability("appPackage", appPackage);
		capabilities.setCapability("appActivity", appActivity);

		return capabilities;
	}

	/**
	 * 初始化功能，安装测试APP
	 * @param appPath 程序名（null则读取/apps/）
	 * xx.apk
	 * @param udid 指定运行设备号
	 * @return
	 */
	public static DesiredCapabilities initAndroidCapabilitiesWithApk(String appPath, String fileName, String udid) {
		File app = FileUtil.readAppFile(appPath, fileName);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("platformVersion", "5.1.1");

		capabilities.setCapability("deviceName", ""+udid);// 参数化时设置UDID
		if(udid!=null)
		capabilities.setCapability("udid", udid);

		capabilities.setCapability("unicodeKeyboard", "True");
		capabilities.setCapability("resetKeyboard", "False");//不需重置

		capabilities.setCapability("noSign", "True");

		// 设置安装app
		capabilities.setCapability("app", app.getAbsolutePath());

		return capabilities;
	}
}
