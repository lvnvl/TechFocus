package com.customized.appium.demo;

import java.io.File;
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
	/**
	 * port deviceName
	 * @param args
	 */
	public static void main(String[] args) {
		// 根据设备列表开始分配任务和端口
		// 此处应改为外部Shell启动Appium多个端口(扫描已占用端口)
		CommonSetting.ADB_PATH = CommonUtil.getAndroidEnv()+"/platform-tools/adb";//获取android环境变量
		TestProjectDemo t = new TestProjectDemo(Integer.parseInt(args[0]), args[1], 
				System.getProperty("user.dir")+File.separator+"apps"+File.separator, args[2]);
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
