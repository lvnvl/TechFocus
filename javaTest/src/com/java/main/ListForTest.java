package com.java.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListForTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> aList = new ArrayList<Integer>();
		aList.add(2);
		aList.add(3);
		aList.add(4);
		aList.add(5);
		aList.add(6);
		for(int i = 0;i < aList.size(); i++){
			System.out.println("i is "+ aList.get(i));
			if(i == 3){
				aList.add(7);
				aList.add(8);
				aList.add(9);
			}
		}
//		Iterator<Integer> it = aList.iterator();
//		while(it.hasNext()){
//			Integer i = it.next();
//			System.out.println("i is "+ i);
//			if(i == 3){
//				it.remove();;
//			}
//		}
		System.out.println(aList.toString());
	}
}