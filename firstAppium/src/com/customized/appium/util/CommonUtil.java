package com.customized.appium.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtil {

	/**
	 * @param val
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String getMD5(String val) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes("UTF-8"));
		byte[] hash = md5.digest();
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	static boolean readTimeout(long timeoutMillis) {
		boolean isTimeOut = true;
		long timeout = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < timeout) {
			try {
				while(System.in.available()>0){
					System.in.read();
					isTimeOut = false;
				}
				if(!isTimeOut)
					break;
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				Thread.sleep(10); // 10ms读取一次
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return isTimeOut;
	}

	/**
	 * 暂时用于Linux／Unix系统的Android用户环境变量，一般用于没有系统变量的情况
	 */
	public static String getAndroidEnv() {

		System.out.println(System.getProperty("os.arch") + System.getProperty("os.name"));

		StringBuffer sb = new StringBuffer();
		ProcessBuilder pb1 = new ProcessBuilder("cat", System.getenv("HOME") + "/.bash_profile");
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

		String[] pas = sb.toString().split("export");
		for (String path : pas) {
			// ANDROID_HOME
			if (path.length() > 12 && path.contains("ANDROID_HOME")) {
				if (path.trim().startsWith("ANDROID_HOME")) {
					String[] rp = path.split("=");
					String s = rp[1].replace("\"", "");
					if (s.endsWith(File.separator)) {
						return s;
					}else{
						return s+File.separator;
					}
				}
			}
		}
		
		pb1 = new ProcessBuilder("cat", System.getenv("HOME") + "/.bashrc");
		sb = new StringBuffer("");
		try {
			Process p1 = pb1.start();
			BufferedReader bf1 = new BufferedReader(new InputStreamReader(p1.getInputStream(), "utf-8"));
			while ((line = bf1.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		pas = null;
		pas = sb.toString().split("export");
		for (String path : pas) {
			// ANDROID_HOME
			if (path.length() > 12 && path.contains("ANDROID_HOME")) {
				if (path.trim().startsWith("ANDROID_HOME")) {
					String[] rp = path.split("=");
					String s = rp[1].replace("\"", "");
					if (s.endsWith(File.separator)) {
						return s;
					}else{
						return s+File.separator;
					}
				}
			}
		}
		return null;
//		Map map = System.getenv();
//		Iterator it = map.entrySet().iterator();
//		while (it.hasNext()) {
//			Entry entry = (Entry) it.next();
//			System.out.print(entry.getKey() + "=");
//			System.out.println(entry.getValue());
//		}

//		Properties properties = System.getProperties();
//		Iterator it2 = properties.entrySet().iterator();
//		while (it2.hasNext()) {
//			Entry entry = (Entry) it2.next();
//			System.out.print(entry.getKey() + "=");
//			System.out.println(entry.getValue());
//		}
	}
}
