package com.java.main;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.customized.appium.model.AElementWidget;
import com.customized.appium.util.XMLUtil;

public class XMLTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String xml = "";
		try{
			File file = new File(System.getProperty("user.dir")+File.separator+"1.xml");
			if(!file.exists()){
				file.createNewFile();
//				System.out.println("file not exists!!");
			}
			xml = FileUtils.readFileToString(file,"UTF-8");    //if file not exist,exception will throw here
			System.out.println("file content :"+ xml);
//			System.out.println("file exists: "+ file.exists());
//			System.out.println("file path : "+ file.getAbsolutePath());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		List<AElementWidget> widgetList = null;
		widgetList = XMLUtil.dealWithXML(xml);
		int i = 0;
		for(AElementWidget element:widgetList){
			System.out.println(i++ + " widget:\n"
					+ "\ttext:"+element.getText()
					+ "\n\tresource-id:"+element.getResourceId()
					+ "\n\tclass:"+element.getClazz()
					+ "\n\tcontent-desc:"+element.getContentDesc());
		}
	}
}