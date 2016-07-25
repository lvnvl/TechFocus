package com.customized.appium.demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.customized.appium.CommonSetting;
//import com.beust.jcommander.Parameterized;
//import com.customized.appium.CommonSetting;
import com.customized.appium.algorithm.AutoTraversalById;
import com.customized.appium.framework.TestProject;
import com.customized.appium.model.RecordEventModel;
import com.customized.appium.util.ADBUtil;
import com.customized.appium.util.FileUtil;
import com.customized.appium.util.InitUtil;
import com.customized.appium.util.Log;

import io.appium.java_client.android.AndroidDriver;

/**
 * <p>
 * 框架demo：自动化测试</br>
 * Appium似乎无法检测应用跳转
 * XXX 单独测试同时调用Appium是否会产生同步问题。</br>
 * 区分同一次测试与多次重复测试
 * </p>
 * @author kaliwn
 *
 */
//@RunWith(Parameterized.class)
public class TestProjectDemo extends TestProject {

	private AndroidDriver driver;
	private List<RecordEventModel> lastEventList;  // 读取文档操作记录

	private String udid;
	private int port;
	private String path;
	private String apkName;
	private String packageName;
	private String workPath;
	private String activity;
	private AutoTraversalById at;

	/**
	 * 
	 * @param port
	 * @param udid
	 * @param path App's path
	 * @param apkName
	 */
	public TestProjectDemo(int port, String udid, String path, String apkName) {
		this.udid = udid;
		this.port = port;
		this.path = path;
		this.apkName = apkName;
	}

	/**
	 * 
	 * @param port
	 * @param udid
	 * @param packageName
	 * @param activity
	 * @param sa
	 */
	public TestProjectDemo(int port, String udid, String packageName, String activity, boolean sa) {
		this.udid = udid;
		this.port = port;
		this.packageName = packageName;
		this.activity = activity;
	}

//	@Parameters
//	public static Collection testDatas() {
//		// 测试参数：和构造函数中参数的位置一一对应。
//		return Arrays.asList(new Object[][] { { "", 2}});
//	}

	@Override
	public void onStart() throws Exception{
		// set up appium

		DesiredCapabilities capabilities;
		if(apkName!=null){
			capabilities= InitUtil.initAndroidCapabilitiesWithApk(path, apkName, udid);
		}else{
			capabilities = InitUtil.initAndroidCapabilitiesWithOutApk(packageName, activity, udid);
		}

		try {
			driver = new AndroidDriver(new URL("http://127.0.0.1:"+port+"/wd/hub"), capabilities); // 不同设备时启动不同端口
		} catch (MalformedURLException e) {
			Log.e(e.getMessage());
		}

	}

	@Override
	public void onDestroy() throws Exception{
		if (driver != null)
			driver.quit();
	}

	@Override
	public void onTest() throws Exception{
		int i=0;;
		initAutoTest();
		while(!at.isExit() && (i++ < CommonSetting.ERRORTIMES)){
			at.setForceClose(false);
			at.restartTest();
		}
	}

	private void initAutoTest() {

		String appName = (String) driver.getCapabilities().getCapability("appPackage");

		// 非安装应用将无法获取应用名
		if (appName == null) {
			String[] appname = driver.getAppStrings().split("=");// 非安装包方式首次会报错
			appName = appname[1].substring(0, appname[1].length() - 1);
		}

		// 初始化存储目录路径
		workPath = FileUtil.initFilePath(appName, udid, System.getProperty("user.dir"));

		lastEventList = FileUtil.readRecordFile(workPath, "record");

		at = new AutoTraversalById(driver, workPath);// N轮测试放不同文件夹？。。
		// 测试读取记录文件 或 重新开始评测
		if (lastEventList != null) {
			at.toLastExceptionPlace(lastEventList);
		} else {
			at.restartTest();
		}
	}

	public void stopTest() {
		if(at!=null)at.setExit(true);
	}

	// 测试app跳转是否能正常检测
	private void otherTest() {
		int i = 20;
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		ADBUtil.adbApsDetail();
		while (i-- > 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(i==5)
			try {
				driver.findElementById("com.huawei.camera:id/thumbnail_background_view").click();
			} catch (NoSuchElementException e) {
				
			}
			try {
				System.out.print(driver.getAppStrings()+" ");// 非安装包方式首次会报错
				System.out.print(driver.getCapabilities().getCapability("appPackage")+" ");
				System.out.println(driver.currentActivity());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}