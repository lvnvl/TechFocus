package com.java.main;

public class StringBufferTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String line = "1  4444";
		System.out.println(line.substring(line.length()-5, line.length()));
		System.out.println(1+Integer.valueOf(line.substring(line.length()-5, line.length()).trim()));
		line = "1 g4444";
		try{
			System.out.println(Integer.valueOf(line.substring(line.length()-5, line.length())));
		}catch(NumberFormatException nfe){
			System.out.println("transform error from str to int");
		}
		line = "Running activities (most recent first):      TaskRecord{1ca7fe6 #2963 A=com.meizu.notepaper U=0 sz=1}        Run #0: ActivityRecord{28a0de57 u0 com.meizu.notepaper/com.meizu.flyme.notepaper.app.NotePaperActivity t2963}    Running activities (most recent first):      TaskRecord{3a68fe84 #2710 A=com.meizu.flyme.launcher U=0 sz=1}        Run #0: ActivityRecord{46362c5 u0 com.meizu.flyme.launcher/.Launcher t2710}";
		System.out.println("--"+ line.substring(line.indexOf("A=")+2, line.indexOf("U=")-1)+"--");
		line = "	package: name='com.rili.android.client' versionCode='28' versionName='4.0'";
		System.out.println("--"+ line.substring(line.indexOf("='")+2, line.indexOf("' v")).trim()+"--");
		
		
		/*
		StringBuffer buff = new StringBuffer("SFA12A5F00ADF         device");

	    // append the string to StringBuffer
	    buff.append("ZAA12A5F00ADF         device");
	    buff.append("FS2A5F00ADF         device");

	    // trimToSize buffer
	    buff.trimToSize();
	    System.out.println(buff);
	    
	    System.out.println(buff.toString().trim());
	    
	    String[] devices = buff.toString().split("device");
	    
	    int i = 0;
	    for(String str:devices){
	    	System.out.println(i++ + " is " + str.trim());
	    }
	    */
	}

}
