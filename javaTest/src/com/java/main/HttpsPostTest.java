package com.java.main;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import com.java.util.HttpsRequest;

public class HttpsPostTest {

	public static HttpsRequest httpsRequest;
	
	class UserInfo {
		String name;
		String passwd;
		UserInfo(String n,String p){
			name = n;
			passwd = p;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String echoStr = "";
		try {
			httpsRequest = new HttpsRequest();
			UserInfo u = new HttpsPostTest().new UserInfo("jack","123456");
//			echoStr = httpsRequest.sendPost("https://appstest.cn:8443/httpsTest/echo", u, "GSON");
			echoStr = httpsRequest.sendPost("https://appstest.cn:8443/httpsTest/login.jsp", u, "GSON");
			
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("result is "+ echoStr);
	}
}