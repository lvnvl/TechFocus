package com.customized.appium.demo;

import java.io.File;

import com.customized.appium.framework.Tester;

import io.appium.java_client.AppiumDriver;

public class MyTester implements Tester{
	AppiumDriver driver;

	@Override
	public File loadSettingFile() {
		return null;
	}

	@Override
	public void output(String path) {
		
	}

	@Override
	public String hihi() {
		return "Que!";
	}

	@Override
	public void setDriver(AppiumDriver driver) {
		this.driver = driver;
		
	}

	@Override
	public AppiumDriver getDriver() {
		return driver;
	}
}
