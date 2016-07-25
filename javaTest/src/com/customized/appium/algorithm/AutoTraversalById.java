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
 * 自动遍历算法:
 * <li>重新自动测试</li>
 * checkBox,layout,View</br>
 * DrawerLayout, ScrollView, HorizontalScrollView, ViewPager</br>
 * gridView, ListView, tabView</br>
 * android.webkit.webview</br>
 * </p>
 * TODO 路径记录 防止遗漏或重复进入防止跳出测试程序:拍照、文件管理等
 * @author kaliwn
 */
public class AutoTraversalById {
	private List<ActivityElement> activityList;// 递归遍历
	private Map<String, String> activityMD5;
	private List<AElementWidget> bufferWidgetList = null;
	private String activityName;

	private boolean isForceClose = false; // 遇到错误
	private boolean isExit = false; // 退出自动遍历

	AndroidDriver driver;
	private String workPath;
	private EventDriver mdriver;

	public AutoTraversalById (AndroidDriver driver, String workPath) {
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
		activityName = driver.currentActivity();
		List<AElementWidget> widgetList = null;
		// dump need some time
		driver.findElementsByClassName("android.widget.Button");
		String xml = driver.getPageSource();

		try {
			activityMD5.put(activityName, CommonUtil.getMD5(xml));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		widgetList = XMLUtil.dealWithXML(xml);
		bufferWidgetList = widgetList;

		// 保存当前activity所有控件元素
		ActivityElement ae = new ActivityElement();
		ae.setActivityName(activityName);
		ae.setWidgets(widgetList);
		activityList.add(ae);

		if (widgetList != null && widgetList.size() > 0) {
			for (AElementWidget element : widgetList) {
				if (element.getClickable().equals("true")){// XXX 凡事能点击的都点击，忽略listView，但不忽略子控件
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
			activityName = driver.currentActivity();
			ae.setActivityName(activityName);
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
	 * 递归式寻径，默认点击完成后返回</br>
	 * TODO 检测可能progress bar 遇到返回功能按钮或更新内容按钮
	 * @param element
	 */
	private void Recursion(AElementWidget element) {
		List<AElementWidget> widgetList = null;
		activityName = driver.currentActivity();
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

		// 小米华为采用adb shell获取界面xml
		String xml = driver.getPageSource();
		widgetList = XMLUtil.dealWithXML(xml);

		// 判断界面是否同一个activity,或界面属于部分变动，需要进行深度判断
		if (activityName.equals(driver.currentActivity())) {
			return;
/*			String bufferMD5 = null;
			try {
				bufferMD5 = CommonUtil.getMD5(xml);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// md5进行首次判断
			if (bufferMD5.equals(activityMD5.get(CommonSettings.activityName))) {
				return;
			}

			// 节点判断,view, listView, viewPager，两棵树比较,向量？［相对位置］［相对大小］，->节点数量？
			// 方法2:向量比较(父节点与子节点属性作为向量)，针对非破坏性结构的UI
			List<AElementWidget> lastWidgetList = buffer.get(buffer.size()-1).getWidgets();
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

			// 方法1:普通父节点切面查找暂无效，需修正
//			Map<Integer, Integer> pIDs = new HashMap<Integer, Integer>();
//			int value = 0;
//			for(AElementWidget w:bufferWidgetList){
//				value = w.getpNodeID();
//				if(!pIDs.containsKey(value)){
//					pIDs.put(value, value);
//				}else{
//					pIDs.put(value, pIDs.get(value)+1);
//				}
//			}
//
//			// 新产生的树
//			Map<Integer, Integer> npIDs = new HashMap<Integer, Integer>();
//			for(AElementWidget w:widgetList){
//				value = w.getpNodeID();
//				if(!npIDs.containsKey(value)){
//					npIDs.put(value, value);
//				}else{
//					npIDs.put(value, npIDs.get(value)+1);
//				}
//			}
//
//			// list out the differences
//			List<Integer> diff = new ArrayList<Integer>();
//			if(npIDs.size()>=pIDs.size()){
//				Set<Entry<Integer, Integer>> set = npIDs.entrySet();
//				Iterator<Entry<Integer, Integer>> iter = set.iterator();
//				while(iter.hasNext()){
//					Entry<Integer, Integer> entry = (Entry<Integer, Integer>)iter.next();
//					if(pIDs.get(entry.getKey())!=null){
//						if(entry.getValue() != pIDs.get(entry.getKey())){
//							// difference ID
//							diff.add((Integer) entry.getKey());
//						}
//					}else{
//						// difference ID
//						diff.add((Integer) entry.getKey());
//					}
//				}
//			}else{
//				//额。。
//				System.out.println("it happends..0_0");
//			}
//			System.out.println(diff.size()+"。。T_T");
			// 追踪到父节点 ，根据父节点判断是listView内容更新，或者，
			// 修正点击事件
			CommonSettings.evenList.get(CommonSettings.evenList.size()-1).setIsRefresh("isRefresh");

			// 列为新的遍历控件对象，倘若为菜单，点击后有可能会隐藏。
			ActivityElements ae = new ActivityElements();
			ae.setActivityName(CommonSettings.activityName);
			ae.setWidgets(widgetList);
			buffer.add(ae);

			bufferWidgetList = null;
			bufferWidgetList = widgetList;

			if (widgetList != null && widgetList.size() > 0) {
				for (AElementWidget clickelement : widgetList) {
					if ((clickelement.getClazz().equals("android.widget.Button")) && (clickelement.getIsRepeat()!=1)) {
						if (isForceClose == true)
							break;
						Recursion(clickelement);
					}
				}
			}
			driver.sendKeyEvent(AndroidKeyCode.BACK);// 侧滑菜单可能无效，*/

			// activity额外添加标记?方便下次节点跳跃
			// CommonSettings.activityName = CommonSettings.activityName + element.getID();
		}else{

			ActivityElement ae = new ActivityElement();
			ae.setActivityName(activityName);
			ae.setWidgets(widgetList);
			activityList.add(ae);

			bufferWidgetList = null;
			bufferWidgetList = widgetList;

			if (widgetList != null && widgetList.size() > 0) {
				for (AElementWidget clickelement : widgetList) {
					if (clickelement.getClickable().equals("true")){//.getClazz().equals("android.widget.Button")) {
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
