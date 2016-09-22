package com.java.main;

import cn.appstest.exception.PageLoadException;

public class ExceptionTest {

	public ExceptionTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			System.out.println("start exception test!");
			throw new PageLoadException("error page");
		}catch(PageLoadException pageExp){
			pageExp.printStackTrace();
			pageExp.handle();
		}
		System.out.println("end exception test!");
	}
}