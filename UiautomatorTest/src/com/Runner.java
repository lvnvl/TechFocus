package com;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import android.os.Bundle;
import android.os.RemoteException;

public class Runner extends UiAutomatorTestCase {

	public void testDemo() throws UiObjectNotFoundException {
		UiDevice device = getUiDevice();
		Bundle params = getParams();
		String installPackage = "";
		if (params.getString("package") != null) {
			installPackage = params.getString("package");// 获取需要安装的应用包名
		} else {
			return;
		}
		try {
			if(!device.isScreenOn()){
				device.wakeUp();
			}
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
//		this.watchInstallApp();
		/*
		 * 等待安装弹窗、直到应用被打开
		 */
		long waitTime = 0;
		while ( waitTime < 1000*60*5 && !installPackage.equals(device.getCurrentPackageName())) {
			try {
				UiObject installbtn1 = new UiObject(new UiSelector().clickable(true).textContains("安装"));
				UiObject installbtn2 = new UiObject(new UiSelector().clickable(true).textContains("允许"));
				UiObject installbtn3 = new UiObject(new UiSelector().clickable(true).textContains("确认"));
				UiObject installbtn4 = new UiObject(new UiSelector().clickable(true).textContains("Install"));
				if (installbtn1.exists()) {
					installbtn1.click();
				}
				if (installbtn2.exists()) {
					installbtn2.click();
				}
				if (installbtn3.exists()) {
					installbtn3.click();
				}
				if (installbtn4.exists()) {
					installbtn4.click();
				}
			} catch (UiObjectNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				Thread.sleep(1000);
				waitTime+=1000;
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}