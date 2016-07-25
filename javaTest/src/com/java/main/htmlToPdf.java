package com.java.main;

import java.io.FileOutputStream;
import java.io.IOException;

import com.pdfcrowd.Client;
import com.pdfcrowd.PdfcrowdError;

public class htmlToPdf {
	public static void main(String[] args){
		try{
			FileOutputStream fileStream;
			Client myClient = new Client("jackpants", "986c52888f5051d30a93470219c74545");
			fileStream = new FileOutputStream("appstest1.pdf");
			myClient.convertURI("http://www.appstest.cn/", fileStream);
			fileStream.close();
			Integer ntokens1 = myClient.numTokens();
			System.out.println("从URL装换用了"+ntokens1+"个token");
			
			fileStream = new FileOutputStream("appstest2.pdf");
			myClient.convertFile("E:\\appstest.html", fileStream);
			fileStream.close();
			Integer ntokens = myClient.numTokens();
			System.out.println("从URL装换用了"+(ntokens-ntokens1)+"个token");
		}
		catch(PdfcrowdError why){
			System.err.println(why.getMessage());
		}
		catch(IOException e){
			System.err.println(e.getMessage());
		}
	}
}
