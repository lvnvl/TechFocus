package com.customized.appium.algorithm;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.customized.appium.demo.EventDriver;
import com.customized.appium.model.AElementWidget;
import com.customized.appium.model.ActivityElement;
import com.customized.appium.model.RecordEventModel;
import com.customized.appium.util.CommonUtil;
import com.customized.appium.util.UIUtil;
import com.customized.appium.util.XMLUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

/**
 * <p>
 * 自动遍历算法:</br>
 * 可能有滞后性，脏数据问题</br>
 * checkBox,layout,View</br>
 * DrawerLayout, ScrollView, HorizontalScrollView, ViewPage侧滑菜单r特殊控件处理</br>
 * gridView, ListView, tabView</br>
 * android.webkit.webview</br>
 * </p>
 * TODO 路径记录 防止遗漏或重复进入;防止跳出测试程序:拍照、文件管理等(需要根据包名判断, 全新安装应用需要)
 * @author kaliwn
 */
public class AutoTraversal {
	private List<ActivityElement> activityList;// 递归遍历
	private Map<String, String> activityMD5; // 不能重复标记
	//private List<AElementWidget> bufferWidgetList = null;
	private String lastActivityName;
	private String currentActivityName;

	private boolean isForceClose = false; // 遇到错误
	private boolean isExit = false; // 退出自动遍历

	AndroidDriver driver;
	private String workPath;
	private EventDriver mdriver;

	public AutoTraversal (AndroidDriver driver, String workPath) {
		this.driver = driver;
		this.workPath = workPath;

		activityList = new ArrayList<ActivityElement>();
		activityMD5 = new HashMap<String, String>();
		mdriver = new EventDriver(driver);
	}

	public boolean isForceClose() {
		return isForceClose;
	}

	public void setForceClose(boolean isForceClose) {
		this.isForceClose = isForceClose;
	}

	public boolean isExit() {
		return isExit;
	}
	
	public void setExit(boolean isExit) {
		this.isExit = isExit;
	}

	/**
	 * 重新构建树自动测试
	 */
	public void restartTest() {
		currentActivityName = driver.currentActivity();
		List<AElementWidget> widgetList = null;
		// dump need some time
		driver.findElementsByClassName("android.widget.Button");
		String xml = driver.getPageSource();

		try {
			activityMD5.put(currentActivityName, CommonUtil.getMD5(xml));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		widgetList = XMLUtil.dealWithXML(xml);
		//bufferWidgetList = widgetList;

		// 保存当前activity所有控件元素
		ActivityElement ae = new ActivityElement();
		ae.setActivityName(currentActivityName);
		ae.setWidgets(widgetList);
		activityList.add(ae);

		if (widgetList != null && widgetList.size() > 0) {
			for (AElementWidget element : widgetList) {
				if (element.getClickable().equals("true")){
					if (isForceClose || isExit)
						break;
					Recursion(element);
				}
			}
		}
		isExit = true;
	}

	/**
	 * 倒序遍历寻找跳转节点，快速定位最后触发错误activity</br>
	 */
	public void toLastExceptionPlace(List<RecordEventModel> lastEventList) {
		List<AElementWidget> widgetList = null;
		

		String bufferActivity = lastEventList.get(0).getActivity();
		int bufferID = lastEventList.get(0).getID();

		// 跳转的路径节点
		List<Integer> quickID = new ArrayList<Integer>();

		// 根据activity和标记，提取节点。
		for (RecordEventModel model : lastEventList) {
			if (!bufferActivity.equals(model.getActivity())) {
				bufferActivity = model.getActivity();
				quickID.add(bufferID);
			}
			bufferID = model.getID();
		}

		// 进入最后产生错误的activity
		String xml;
		for (int id : quickID) {
			xml = driver.getPageSource();
			widgetList = XMLUtil.dealWithXML(xml);
			ActivityElement ae = new ActivityElement();
			currentActivityName = driver.currentActivity();
			ae.setActivityName(currentActivityName);
			ae.setWidgets(widgetList);
			activityList.add(ae);
			UIUtil.click(UIUtil.findMyElementById(id, widgetList), driver);
		}

		xml = driver.getPageSource();
		widgetList = XMLUtil.dealWithXML(xml);
		bufferID = lastEventList.get(lastEventList.size() - 1).getID()+1;

		mdriver.setEventList(lastEventList);

		if (bufferID < widgetList.size()) {
			// 继续进行当前页面的测试
			for (; bufferID < widgetList.size(); bufferID++) {
				if (widgetList.get(bufferID).getClickable().equals("true")){//.getClazz().equals("android.widget.Button")) {
					if (isForceClose || isExit)
						break;
					Recursion(widgetList.get(bufferID));
				}
			}
		} else {
			driver.sendKeyEvent(AndroidKeyCode.BACK);
		}

		isExit = true;
		// 从最后一次记录xml开始运行，～
//		Set<Entry<String, List<AElementWidget>>> set = buffer.entrySet();
//		Iterator<Entry<String, List<AElementWidget>>> iter = set.iterator();

	}

	/**
	 * 递归前序遍历，默认点击完成后返回</br>
	 * @param element
	 */
	private void Recursion(AElementWidget element) {
		List<AElementWidget> widgetList = null;
		lastActivityName = currentActivityName;
		currentActivityName = driver.currentActivity();
		mdriver.click(element);
		// 检测异常，ForceClose, no answer
		isForceClose = UIUtil.checkError(driver, workPath);

		if (isForceClose) {
			mdriver.forceCloseEvent("record", workPath);// 输出日志// 输出日志
			// 输出配置文 输出操作记录
			// 关闭应用
			driver.findElementById("button1").click();
			driver.closeApp();
			return;
		}

		// 小米华为采用ADB shell获取界面XML
		String xml = driver.getPageSource();
		widgetList = XMLUtil.dealWithXML(xml);

		// 同一个activity,或界面属于部分变动，深度检测, 并加入新控件
		if (lastActivityName.equals(currentActivityName)) {
			//return;
			String bufferMD5 = null;
			try {
				bufferMD5 = CommonUtil.getMD5(xml);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// 1.md5进行首次判断
			if (bufferMD5.equals(activityMD5.get(currentActivityName))) {
				return;
			}

			// 2.向量比较(父节点与子节点属性作为向量)，主要针对非破坏性结构的UI,删除，新增
			List<AElementWidget> lastWidgetList = activityList.get(activityList.size()-1).getWidgets();
			int lsize = lastWidgetList.size();
			int wsize = widgetList.size();

			AElementWidget e1 = null;
			AElementWidget e2 = null;
			AElementWidget e3 = null;
			AElementWidget e4 = null;

			for(int i = 0, j = 0; i < lsize; i++){
				// 
				e1 = lastWidgetList.get(i);

				if(e1.getpNodeID()!=0){
					e3 = lastWidgetList.get(e1.getpNodeID()); // 获取父节点作为向量
				}

				for(; j<wsize; j++){
					e2 = widgetList.get(j);
					if(e2.getpNodeID()!=0){
						e4 = widgetList.get(e2.getpNodeID());
					}

					// 进行简单比较
					if(e1.getClazz().equals(e2.getClazz())){// 假如两个是相同类
						if(e3!=null||e4!=null){ // 假如有父节点
							if(e3.getClazz().equals(e4.getClazz())){
								// 则认为相同的控件,标记为重复
								e2.setIsRepeat(1);
							}
						}
					}
//					int[] size1 = ConvertUtil.convertBounds(e2.getBounds());
//					int[] size2 = ConvertUtil.convertBounds(e1.getBounds());
//					if(e3!=null||e4!=null){
//						int[] size3 = ConvertUtil.convertBounds(e2.getBounds());
//						int[] size4 = ConvertUtil.convertBounds(e1.getBounds());
//						
//					}
//					// 或者等比例缩小，前后的参考物
//					if((size1[2]-size1[0]) == (size2[2]-size2[0])){
//						break;
//					}
				}
			}
			// 注意是前序遍历的结果，匹配对应向量则为已存在，否则标记为特殊场景属性
			// 同时优化方法1的算法，只计算主要节点，子节点跳过，进入下个节点。

			// 追踪到父节点 ，根据父节点判断是listView内容更新，或者，
			// 修正点击事件
			mdriver.getEventList().get(mdriver.getEventList().size()-1).setIsRefresh("isRefresh");

			// XXX 列为新的遍历控件对象，倘若为菜单，点击后有可能会隐藏。
			ActivityElement ae = new ActivityElement();
			ae.setActivityName(currentActivityName);
			ae.setWidgets(widgetList);
			activityList.add(ae);

			//bufferWidgetList = null;
			//bufferWidgetList = widgetList;

			if (widgetList != null && widgetList.size() > 0) {
				for (AElementWidget clickelement : widgetList) {
					if ((clickelement.getClickable().equals("true")) && (clickelement.getIsRepeat()!=1)) {
						if (isForceClose == true)
							break;
						Recursion(clickelement);
					}
				}
			}
			driver.sendKeyEvent(AndroidKeyCode.BACK);// 侧滑菜单可能无效

			// activity额外添加标记?方便下次节点跳跃
			// CommonSettings.activityName = CommonSettings.activityName + element.getID();
		} else {
			// 跳转其它界面, 或跳出当前应用
			ActivityElement ae = new ActivityElement();
			ae.setActivityName(currentActivityName);
			ae.setWidgets(widgetList);
			activityList.add(ae);

			//bufferWidgetList = null;
			//bufferWidgetList = widgetList;

			try {
				activityMD5.put(currentActivityName, CommonUtil.getMD5(xml));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (widgetList != null && widgetList.size() > 0) {
				for (AElementWidget clickelement : widgetList) {
					if (clickelement.getClickable().equals("true")){
						if (isForceClose || isExit)
							break;
						Recursion(clickelement);
					}
				}
			}
		}
		driver.sendKeyEvent(AndroidKeyCode.BACK);
	}

}
