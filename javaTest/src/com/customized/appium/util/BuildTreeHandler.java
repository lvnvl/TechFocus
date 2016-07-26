package com.customized.appium.util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.customized.appium.model.AElementWidget;

/**
 * 解析布局,处理流事件</br>
 * 节点算法</br>
 * id从0开始分配</br>
 * 可能加入深度限制<br/>
 * TODO 加入子节点数量
 * <br/>
 * 与uiautomator保持一致构建方式
 * @author kaliwn
 *
 */
public class BuildTreeHandler extends DefaultHandler {
	private List<AElementWidget> elements = null;
	// private String tagName = null;
	private AElementWidget element = null;
	private int count = 0;
	private int id = 0;

	public List<AElementWidget> getElements() {
		return elements;
	}

	public void setElements(List<AElementWidget> elements) {
		this.elements = elements;
	}

	// 文档开始
	public void startDocument() throws SAXException {
		elements = new ArrayList<AElementWidget>();
		count = 0;
		// tagName = null;
		id = 0;
	}

	// 节点开始
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if("hierarchy".equals(qName)) return;//首节点
		StringBuffer sb = new StringBuffer();
		for(int index = 0; index<count; index++)
			sb.append("-");
		//Log.v(sb.toString()+ qName+count + attributes.getValue("bounds")+localName);
		// tagName = localName;
		// 所有节点都创建一个对象
		element = new AElementWidget();
		element.setID(id++);
		element.setpNodeID(count-1);
		element.setmNodeID(count);
		element.setClazz(qName);
		//取出标记内的属性
		element.setEnabled(attributes.getValue("enabled"));
		element.setBounds(attributes.getValue("bounds"));
		element.setClickable(attributes.getValue("clickable"));
		element.setLongClickable(attributes.getValue("long-clickable"));
		element.setCheckable(attributes.getValue("checkable"));
		element.setChecked(attributes.getValue("checked"));
		element.setScrollable(attributes.getValue("scrollable"));
		element.setPassword(attributes.getValue("password"));
		element.setResourceId(attributes.getValue("resource-id"));//动态生成都控件不会有id
		element.setText(attributes.getValue("text"));
//		System.out.println("text is:"+element.getText());
		element.setContentDesc(attributes.getValue("content-desc"));
		elements.add(element);// 则将创建完成的view/widget加入到集合
		//listview 需要直接点击或者内部控件
		count++;
	}

	// 文本节点 只考虑控件属性值android一般不会出现文本内容，暂不做处理
//	public void characters(char[] ch, int start, int length) throws SAXException {
//		if (tagName != null) {// 文本节点必须前面要有元素节点开始标记
//			String data = new String(ch, start, length);// 取出文本节点的值
//			System.out.println("text:" + data);
//		}
//	}

	// 节点结束
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if("hierarchy".equals(qName)) return;
		count--;
		StringBuffer sb = new StringBuffer();
		for(int index = 0; index<count; index++)
			sb.append("-");
		//Log.v(sb.toString()+ qName+">");
	}
}