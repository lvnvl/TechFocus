package com.customized.appium.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.customized.appium.CommonSetting;
import com.customized.appium.model.DebugDevice;
import com.customized.appium.util.ADBUtil;
import com.customized.appium.util.CommonUtil;
import com.customized.appium.util.FileUtil;

/**
 * 外部主程序：执行单元测试项目
 * 断言 测试异常 捕获异常 测试方法的性能
 * appium -a 127.0.0.1 -p 4722
 * args : [port, device, apk]
 * @since 20160601:
 * 转为普通应用程序测试，取消单元测试.
 * 模拟后台交互
 * @author kaliwn
 */
public class MainRunTest {
	private static List<DebugDevice> deviceList;
	private static List<MyThread> threadList;
	private static String testTask = "backup.apk";
	private static int port = 4723;//端口号

	/**
	 * port deviceName
	 * @param args
	 */
	public static void main(String[] args) {
		// 根据设备列表开始分配任务和端口
		// 此处应改为外部Shell启动Appium多个端口(扫描已占用端口)
		CommonSetting.ADB_PATH = CommonUtil.getAndroidEnv()+"/platform-tools/adb";//获取android环境变量
		hiTest();
//		String udid = null;//设备udid
//		if(args.length>0){
//			try{
//				port = Integer.parseInt(args[0]);
//			}catch(NumberFormatException e){
//				System.out.println(e.getMessage());
//			}
//			udid = args[1];
//		}
//
//		if(udid==null){
//			System.out.println("java -jar x.jar port deviceName");
//			return;
//		}
//
//		deviceList(port);
//
//		if(deviceList!=null && deviceList.size()>0){
//			MainRunTest test = new MainRunTest();
//			threadList = new ArrayList<MyThread>(deviceList.size());
//
//			for(DebugDevice device: deviceList){
//				MyThread t = test.new MyThread(device.getPort(), device.getUdid(), null, device.getTask());
//				threadList.add(t);
//			}
//
//			for(MyThread t: threadList){
//				t.start();
//			}
//			waitCommand();
//			for(MyThread t: threadList){
//				t.cancel();
//			}
//		}

	}

	class MyThread extends Thread {
		boolean isStop = false;
		int port;
		String udid;
		String path;
		String apkName;
		TestProjectDemo t;

		MyThread(int port, String udid, String path, String apkName){
			this.port = port;
			this.udid = udid;
			this.path = path;
			this.apkName = apkName;
		}

		public void run() {
			t = new TestProjectDemo(port, udid, path, apkName);
			try {
				t.setTest();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				t.runTest();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				t.endTest();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void cancel() {
			if(t!=null) t.stopTest();
		}
	}

	static boolean waitCommand() {

		boolean isStop = false;
		int status = 0;
		while (!isStop) {
			try {
				while(System.in.available()>0){
					System.in.read();
					isStop = true;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			status = 0;
			for(MyThread t: threadList){
				if(!t.isAlive()){
					status +=1;
				}
			}
			if(status == threadList.size()) isStop = true;

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return isStop;
	}

	/**
	 * 单元测试方式因暂不能动态设置参数，只能以静态方式或配置方式，并不能控制其流程，故暂时该方式实现。
	 */
//	private void runJunitTest(){
//		Result result = JUnitCore.runClasses(TestProjectDemo.class);
//		for (Failure f : result.getFailures()) {
//			System.out.println("Failutes:"+f.toString()+f.getMessage());
//		}
//
//		if (result.wasSuccessful()) {
//			System.out.println("结束测试");
//		}
//	}

	/**
	 * 枚举设备列表，自动运行所有测试
	 */
	private static void deviceList(int port) {

		deviceList = ADBUtil.adbDevices(CommonSetting.ADB_PATH);
		int i = 0;
		if (null == deviceList) {
			return;
		}
		for (DebugDevice device : deviceList) {
			device.setTask(testTask);
			device.setPort(port + i++);
			System.out.println(device.getUdid() + ":" + device.getPort());
			FileUtil.exportRunningSettingWithJSON(deviceList);
		}
	}

	private static void hiTest() {
		TestProjectDemo t = new TestProjectDemo(port, null,"com.netease.qa.orangedemo", ".MainActivity", false);
		try {
			t.setTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			t.runTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			t.endTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
