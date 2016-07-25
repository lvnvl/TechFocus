package com.java.main;

import java.util.ArrayList;
import java.util.List;

public class generic {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		list.add("qqyumidi");
		list.add("corn");
		//list.add(100);//提示编译错误

		for (int i = 0; i < list.size(); i++) {
			String name = (String) list.get(i); 
			// name:cornException in thread "main" java.lang.ClassCastException: java.lang.Integer cannot 
			//be cast to java.lang.String
			//at com.java.main.generic.main(generic.java:16)
			System.out.println("name:" + name);
		}
	}

}
