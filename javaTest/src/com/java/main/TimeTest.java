package com.java.main;

public class TimeTest {

	public TimeTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long lastMills = System.currentTimeMillis();
		System.out.println("start time:" + lastMills);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("after 1s time:" + (System.currentTimeMillis()-lastMills));
		lastMills = System.currentTimeMillis();
	}

}
