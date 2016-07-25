package com.customized.appium.framework;

import java.io.File;

import io.appium.java_client.AppiumDriver;

/**
 * 倒入结构树</br>
 * 倒入Driver</br>
 * 待补充
 * @author kaliwn
 *
 */
public interface Tester {

	String hihi();
	File loadSettingFile();
	void output(String path);

	void setDriver(AppiumDriver driver);
	AppiumDriver getDriver();
}
