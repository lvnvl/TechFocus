package com.java.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class fileTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(System.getProperty("user.dir"));
//		System.out.println(File.separator);
		try{
			File file = new File(System.getProperty("user.dir")+File.separator+"testFile.txt");
			if(!file.exists()){
				file.createNewFile();
//				System.out.println("file not exists!!");
			}
			String str = FileUtils.readFileToString(file,"UTF-8");    //if file not exist,exception will throw here
			System.out.println("file content :"+ str);
//			System.out.println("file exists: "+ file.exists());
			System.out.println("file path : "+ file.getAbsolutePath());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println("end------------");
		
		File file2 = new File(System.getProperty("user.dir")+File.separator+"config");
		if(!file2.exists()) file2.mkdirs();
		file2 = new File(file2, "file.config");
		System.out.println("file exists: "+ file2.exists());
		System.out.println("file path : "+ file2.getAbsolutePath());
		try {
			FileUtils.writeStringToFile(file2, null, "UTF-8");
//			FileUtils.writeStringToFile(file2, "test not exists file writting and 中文", "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("file exists: "+ file2.exists());
	}

}
