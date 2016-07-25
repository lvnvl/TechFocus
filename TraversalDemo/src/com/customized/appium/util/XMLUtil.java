package com.customized.appium.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.customized.appium.model.AElementWidget;

public class XMLUtil {

	public static List<AElementWidget> dealWithXML(String xml){
		InputStream instream = new ByteArrayInputStream(xml.getBytes());
		try {
			return getPersons(instream);
		} catch (Exception e) {
			Log.e(e.getMessage());
		}
		return null;
	}

	public static List<AElementWidget> getPersons(InputStream instream) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();// 创建SAX解析工厂
		SAXParser paser = factory.newSAXParser();// 创建SAX解析器
		BuildTreeHandler handler = new BuildTreeHandler();// 创建事件处理程序
		paser.parse(instream, handler);// 开始解析
		instream.close();// 关闭输入流
		return handler.getElements();// 返回解析后的内容
	}
}
