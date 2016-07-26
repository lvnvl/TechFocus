package com.customized.appium;


public class CommonSetting {
	//
	public final static String LAYOUT  = "cover";
	public final static String PG_STOP = "stop";

	public static String ADB_PATH = null;

	/**
	 * 默认开启所有操作，否则只输出错误等基本日志
	 */
	public static boolean isOpenLog = true;
	/**
	 * 默认遍历深度，不超过5个activity
	 */
	public static int deepActivity = 5;

	/**
	 * 失败次数
	 */
	public static int ERRORTIMES = 10;
}
