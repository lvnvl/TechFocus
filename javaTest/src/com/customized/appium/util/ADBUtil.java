package com.customized.appium.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.customized.appium.CommonSetting;
import com.customized.appium.model.DebugDevice;

public class ADBUtil {

	/**
	 * 强行点击相对坐标
	 * @param e
	 */
	public static void adbClickElement(String x, String y, String adbPath) {
		ProcessBuilder pb1 = new ProcessBuilder(adbPath, "shell", "input", "touchscreen", "tap", x, y);
		try {
			pb1.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * adb 打开应用
	 * @param e
	 */
	public static void adbLaunchApp(String packageName, String launchActivity, String adbPath) {
		ProcessBuilder pb1 = new ProcessBuilder(adbPath, "shell", "am", "start", "-n", packageName+"/"+launchActivity);
		try {
			pb1.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void adbShell(String adbPath, String udid) {
		StringBuffer sb = new StringBuffer();
		ProcessBuilder pb1 = new ProcessBuilder(adbPath, "-s",
				udid, "shell", "/system/bin/uiautomator", "dump", "/data/local/tmp/uidump.xml");
		String line = null;
		try {
			Process p1 = pb1.start();
			BufferedReader bf1 = new BufferedReader(new InputStreamReader(p1.getInputStream(), "utf-8"));
			while ((line = bf1.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Process p = Runtime.getRuntime().exec(new String[]
		// {"adb","-s","L251367ZN1K3 shell pm list packages"});／／adb shell
		// dumpsys package com.examle.xx
		// -s 选择设备
		// ProcessBuilder pb = new
		// ProcessBuilder("/Users/kaliwn/Downloads/android-sdk-macosx/platform-tools/adb",
		// "-s", "L251367ZN1K3", "shell","/system/bin/uiautomator", "dump",
		// "/data/local/tmp/uidump.xml");
		ProcessBuilder pb = new ProcessBuilder(adbPath, "-s",
				udid, "shell", "cat", "/data/local/tmp/uidump.xml");// "pm",
																					// "list",
																					// "packages");
		try {
			Process p = pb.start();
			BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream(), "utf-8"));
			while ((line = bf.readLine()) != null) {
				sb.append(line);
				System.out.println(line);
			}
			BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			p.getOutputStream();
			// outputStream.write("cat /storage/emulated/legacy/window_dump.txt"
			// + "\r\n");
			outputStream.flush();
			while ((line = bf.readLine()) != null) {
				System.out.println(line);
			}
			bf.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//Adb开启端口转发
//		InetAddress serverAddr = null;    
//        try {
//			serverAddr = InetAddress.getByName("127.0.0.1");
//        Socket socket = new Socket(serverAddr, 15000);
//        socket.setKeepAlive(true);
//        socket.setSoTimeout(120 * 1000);
//        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
//        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
//        out.write("abcdefg".getBytes());    
//        out.flush();
//        } catch (Exception e) {
//        	e.printStackTrace();
//        }
	}

	/**
	 * 列出设备，注意离线设备
	 * @param adbPath
	 * @return
	 */
	public static List<DebugDevice> adbDevices(String adbPath) {

		StringBuffer sb = new StringBuffer();
		ProcessBuilder pb1 = new ProcessBuilder(adbPath, "devices");
		List<DebugDevice> devicesList;
		String line = null;
		try {
			Process p1 = pb1.start();
			BufferedReader bf1 = new BufferedReader(new InputStreamReader(p1.getInputStream(), "utf-8"));
			while ((line = bf1.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//List of devices attached
		if(sb.length()>24){
			sb.delete(0, 24);
			sb.trimToSize();
			String[] devices = sb.toString().split("device");
			devicesList = new ArrayList<DebugDevice>();
			for(String id:devices){
				DebugDevice dev = new DebugDevice();
				dev.setUdid(id.trim());
				devicesList.add(dev);
			}
			return devicesList;
		}else{
			Log.e("没有找到设备");
			return null;
		}
	}

	/**
	 * 模拟输入
	 * @param x
	 */
	public static void adbSendText(String x) {
		ProcessBuilder pb1 = new ProcessBuilder(CommonSetting.ADB_PATH, "shell", "input", "text", x);
		try {
			pb1.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// 使用前需判断当前系统环境 find
	// adb shell dumpsys activity activities | grep mFocusedActivity 或使用build-tools其它工具
	public static String adbApsDetail() {
		ProcessBuilder pb1 = new ProcessBuilder(CommonSetting.ADB_PATH, "shell", "dumpsys", "window", "w", "|grep",
				"\\/", "|grep", "name=");
		String line = null;
		StringBuffer sb = new StringBuffer();
		try {
			Process p1 = pb1.start();
			BufferedReader bf1 = new BufferedReader(new InputStreamReader(p1.getInputStream(), "utf-8"));
			while ((line = bf1.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(sb.length()>29){
			sb.delete(0, 28);
			sb.deleteCharAt(sb.length()-1);
			String[] data = sb.toString().split("/");
			return data[0];
		}else{
			Log.e("nothing with: "+sb.toString());
			return null;
		}
	}
}
