package com.java.main;

public class stringSplitTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String model = "m1 note";
		System.out.println(model.replaceAll(" ", "_"));
		System.out.println(model.replaceAll("\\s", "_"));
//		String oper = "E:\\GitRepo\\TechFocus\\UiautomatorTest\\bin\\AutoInstaller.jar";
//		System.out.println(oper.substring(oper.lastIndexOf("\\")+1));
		
//		String oper = "input:://android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.EditText[1]|528437847@qq.com"; 
//		for(String s : oper.split("[|]")){
//			System.out.println(s);
//		}
		//		int a = 1;
//		int b = 15;
//		System.out.println(String.valueOf(a*100/b)+"%");
		
//		String oper = "click:://android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[4]";
//		for(String s : oper.split("::")){
//			System.out.println(s);
//		}
		System.out.println("------------");
		String bounds = "[0,100][2048,196]";
		String[] splitBounds = bounds.trim().split("[,\\[\\]]");
		for(String str:splitBounds){
			System.out.println("|"+str+"|");
		}
//		
//		System.out.println("meizu-m1_note-71MBBLE25GLV".split("-")[1]);
//
//    	System.out.println("+++++++");
//        System.err.println("call error");
//    	System.out.println("++++++");
//		System.out.println(splitBounds.length);
//		
//		bounds = "123"+"\t"+"asdd"+"\t"+"asdsad"+"\t"+"asdsad"+"\t"+"sadasda"+"\t";
//		splitBounds = bounds.trim().split("[\\t\\n]");
//		int i = 0;
//		for(String str:splitBounds){
////			System.out.println(Integer.parseInt(str));
//			System.out.println(i++ + " is "+ str);
//		}
//		
//		//E:\\log_for_auto\\png\\20160826093046\\
//		String cmd = "mkdir -p #dir#";
//		System.out.println("replaceAll is:"+cmd.replaceAll("#dir#", "E:\\\\log_for_auto\\\\png\\\\20160826093046\\\\"));
//		
//		String tips = "tips>>拒绝|允许>>//android.widget.Button[@resource-id='android:id/button1']";
//		int index = tips.indexOf(">>") + 2;
//		System.out.println(tips.split(">>")[1]);
//		tips = tips.substring(index);
//		System.out.println(tips.split(">>")[1]);
//		String[] keys;
//        String condition = tips.split(">>")[0];
//        if (condition.indexOf("|") != -1) {
//            keys = tips.split(">>")[0].split("\\|");
//        } else {
//            keys = new String[]{condition};
//        }
//    	System.out.println("+++++++");
//        System.err.println("call error");
//    	System.out.println("++++++");
//        for(String key:keys){
//        	System.out.println(key);
//        }
//
//    	System.out.println("-------");
	}
}