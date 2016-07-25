package com.techfocus.dao;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class PathDAO {

	private String configPath;
	
	/**
	 * 
	 * @param configPath 配置文件绝对路径
	 */
	public PathDAO(String configPath) {
		super();
		// TODO Auto-generated constructor stub
		this.configPath = configPath;
	}

	public Map<String,String> getPaths() throws DocumentException{
		Map<String,String> paths = new HashMap<>();
		String path = "";
		String addTime = "";
		//创建SAXReader对象  
        SAXReader reader = new SAXReader();  
        //读取文件 转换成Document  
        Document document = reader.read(new File(configPath));  
        //获取根节点元素对象  
        Element root = document.getRootElement();  
        //遍历  paths
        Iterator<Element> iterator = root.elementIterator();  
        while(iterator.hasNext()){  
            Element e = iterator.next();  
            //遍历  path
            Iterator<Element> itor = e.elementIterator();
            while(itor.hasNext()){
            	Element node = itor.next();
            	//如果当前节点内容不为空，则输出  
                if(!(node.getTextTrim().equals(""))){  
                	if("root".equals(node.getName())){
                    	path = node.getText();
                    }else{
                    	addTime = node.getText();
                    }   
                }
            } 
//            System.out.println("path:"+ path +" addTime:"+addTime);
            paths.put(path, addTime); 
        }  
        
		return paths;
	}
}