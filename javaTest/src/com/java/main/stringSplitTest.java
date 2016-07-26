package com.java.main;

public class stringSplitTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String bounds = "[0,100][2048,196]";
		String[] splitBounds = bounds.trim().split("[,\\[\\]]");
		System.out.println(splitBounds.length);
		int i = 0;
		for(String str:splitBounds){
//			System.out.println(Integer.parseInt(str));
			System.out.println(i++ + " is "+ str);
		}
	}

}
