package com.customized.appium.util;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.OutputType;

import com.customized.appium.model.AElementWidget;

import io.appium.java_client.AppiumDriver;

/**
 * Appium 层 UI 操作，基于uiautomator
 * @author kaliwn
 * TODO 对象输入数据
 */
public class UIUtil {

	/**
	 * 获取对象坐标进行点击
	 * 
	 * @param element
	 * @param driver
	 */
	public static void click(AElementWidget element, AppiumDriver driver) {
		int[] position = ConvertUtil.convertBounds(element.getBounds());
		//addRecord(element, "click", null);
		if(position != null){
			driver.tap(1, position[0] + (position[2] - position[0]) / 2, position[1] + (position[3] - position[1]) / 2, 1);
		}else{
			Log.e("can not found bounds");
			//driver.findElementByXPath("").click();
		}
	}

	/**
	 * 获取对象坐标进行长按
	 * 
	 * @param element
	 * @param driver
	 * @param millisecond
	 *            毫秒 结果为（ millisecond * 5ms ）
	 */
	public static void longClick(AElementWidget element, AppiumDriver driver, int millisecond) {
		int[] position = ConvertUtil.convertBounds(element.getBounds());
		//addRecord(element, "longClick", null);
		if(position == null) {
			Log.e("unable to longClick");
			return;
		}
		driver.tap(1, position[0] + (position[2] - position[0]) / 2, position[1] + (position[3] - position[1]) / 2, millisecond);
	}

	/**
	 * 根据自定义ID
	 * 
	 * @param id
	 * @return
	 */
	public static AElementWidget findMyElementById(int id, List<AElementWidget> list) {
		for (AElementWidget e : list) {
			if (id == e.getID()) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 捕捉错误,3份样本，触发前，触发后，错误提示</br>
	 * 满足条件则输出3份文件
	 */
	public static boolean checkError(AppiumDriver driver, String path) {
		boolean isError = false;
		File cacheFile1 = driver.getScreenshotAs(OutputType.FILE);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		File cacheFile2 = driver.getScreenshotAs(OutputType.FILE);
		//driver.findElementsByClassName("android.widget.Button");
		String xml = driver.getPageSource();
		//XXX 繁体英文简体～粗略检索错误框～
		if (xml.contains("停止响应")||xml.contains("has stopped.")||xml.contains("停止运行")) {
			File cacheFile3 = driver.getScreenshotAs(OutputType.FILE);
			Date d = new Date();
			// 输出截图
			FileUtil.saveFile(cacheFile1, d.toString()+"checkpoint1.png", path);
			FileUtil.saveFile(cacheFile2, d.toString()+"checkpoint2.png", path);
			FileUtil.saveFile(cacheFile3, d.toString()+"checkpoint3.png", path);

			isError = true;
			cacheFile3 = null;
		}
		cacheFile1 = null;
		cacheFile2 = null;
		return isError;
	}

	public static void input(AElementWidget element, AppiumDriver driver) {
		int[] position = ConvertUtil.convertBounds(element.getBounds());
		//addRecord(element, "input", null);
		if(position != null){
			//
		}else{
			Log.e("can not found bounds");
		}
	}
}
