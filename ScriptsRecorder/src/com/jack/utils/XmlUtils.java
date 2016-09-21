package com.jack.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jack.model.Action;
import com.jack.model.ScriptConfig;

public class XmlUtils {

	public XmlUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static ScriptConfig readScript(File script){
		ScriptConfig sc = new ScriptConfig();
		try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(script);
            //获得根元素结点  
            Element root = doc.getDocumentElement();
            if(!"script".equals(root.getTagName())){
            	return null;
            }
            NodeList appiumConfig = doc.getElementsByTagName("appium");
            if(appiumConfig.getLength() != 1){
            	return null;
            }
            sc.setApkPath(doc.getElementsByTagName("app").item(0).getTextContent());
            sc.setActivity(doc.getElementsByTagName("activity").item(0).getTextContent());
            sc.setPackageName(doc.getElementsByTagName("package").item(0).getTextContent());
            sc.setPort(doc.getElementsByTagName("port").item(0).getTextContent());
            
            NodeList actions = doc.getElementsByTagName("action");
//            System.out.println("actions num:" + actions.getLength());
            for (int i = 0; i < actions.getLength(); i++ ) {
                Node action = actions.item(i);
                sc.addAction(action.getTextContent());
//                System.out.println("--------\n"+action.getTextContent()+"\n---------");
            }
            System.out.println("parse script done!");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
		return sc;
	}
	
	/**
	 * 
	 * @param script script file
	 * @param ap     apk path
	 * @param pk     package name
	 * @param pt     port
	 * @param at     activity name
	 * @param actions action list
	 */
	public static void createXml(String script, String ap, String pk, String pt, String at, ArrayList<Action> actions){
		DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Document document = builder.newDocument();
        Element root = document.createElement("script"); 
        document.appendChild(root); 
        Element appium = document.createElement("appium");
        
        Element app = document.createElement("app");
        app.appendChild(document.createTextNode(ap));
        appium.appendChild(app);
        
        Element packageName = document.createElement("package");
        packageName.appendChild(document.createTextNode(pk));
        appium.appendChild(packageName);
        
        Element activity = document.createElement("activity");
        activity.appendChild(document.createTextNode(at));
        appium.appendChild(activity);
        
        Element port = document.createElement("port");
        port.appendChild(document.createTextNode(pt));
        appium.appendChild(port);
        
        root.appendChild(appium);
        
        Element acts = document.createElement("actions");
        
        for(Action a:actions){
        	Element act = document.createElement("action");
        	act.appendChild(document.createTextNode(a.getOperation()));
        	System.out.println("operation is: " + a.getOperation().substring(0, 10));
        	acts.appendChild(act);
        }
        
        root.appendChild(acts);
        
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(script));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            System.out.println("生成XML文件成功!");
        } catch (TransformerConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (TransformerException e) {
            System.out.println(e.getMessage());
        }
	}
}