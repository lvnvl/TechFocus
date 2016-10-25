package com.jack.model;

import java.io.File;
import java.util.ArrayList;

import org.openqa.selenium.By;

import com.android.ddmlib.IDevice;
import com.android.uiautomator.UiAutomatorView;
import com.jack.appium.AppiumActUtil;
import com.jack.utils.ErrorHandler;

public class ScriptRunner extends Thread{

	private ScriptConfig sc;
	private UiAutomatorView view;
	private ArrayList<String> pages;

	private IDevice device;
	private int index;

	public ScriptRunner(IDevice device, ArrayList<String> pages, UiAutomatorView view, ScriptConfig sc, int index) {
		// TODO Auto-generated constructor stub
		this.device = device;
		this.index = index;
		this.pages = pages;
		this.view = view;
		this.sc = sc;
	}

	@Override
	public void run() {
		if(device == null){
			System.out.println("----------------------WTF!!!!----------");
		}
//		view.addRunningInfos(new RunningInfo(
//				Thread.currentThread().getName()
//				, "init"
//				, 1D
//				, "thread for " + device.getSerialNumber() + " started!"));
//		System.out.println("\t" + runInfo.toString());
		AppiumConfig appiumConfig = new AppiumConfig(new File(sc.getApkPath()), 
				device,
    			47111 + index);
		System.out.println(Thread.currentThread().getName() + " appium opened");
    	if(appiumConfig.getDriver() == null){
    		ErrorHandler.showError(view.getShell(), "start appium error!", new Exception("appium init error"));
    		return;
    	}
//		view.addRunningInfos(new RunningInfo(
//				Thread.currentThread().getName()
//				, "init"
//				, 5D
//				, "appium for " + device.getName() + " started!"));
    	int i = 1;int total = sc.getOperations().size();
    	for(String s:sc.getOperations()){
    		String type = s.split("::")[0];
    		try{
    			if(Operate.CLICK.equals(type)){
        			appiumConfig.getDriver().findElement(By.xpath(s.split("::")[1])).click();
        		}else if(Operate.INPUT.equals(type)){
        			appiumConfig.getDriver().findElement(
                			By.xpath(s.split("::")[1].split("[|]")[0])).sendKeys(s.split("[|]")[1]);
        		}else if(Operate.SWIPE.equals(type)){
        			AppiumActUtil.swipe(appiumConfig.getDriver(), s.split("|")[1]);
        		}else if(Operate.SLEEP.equals(type)){
        			Thread.sleep(Integer.valueOf(s.split("|")[1]) * 1000);
        		}else if(Operate.SENDKC.equalsIgnoreCase(type)){
        			AppiumActUtil.sendKeyCode(appiumConfig.getDriver(), s.split("|")[1]);
        		}
//    			view.addRunningInfos(new RunningInfo(
//    					Thread.currentThread().getName()
//    					, "replay"
//    					, (i++)*100/total
//    					, "steps(" + i + "/" + total+");" + type + "operations:" + s));
    		}catch(Exception e){
    			e.printStackTrace();
    			appiumConfig.getDriver().quit();
    			appiumConfig.close();
    			return;
    		}
    	}
    	appiumConfig.getDriver().quit();
    	appiumConfig.close();
	}
}
