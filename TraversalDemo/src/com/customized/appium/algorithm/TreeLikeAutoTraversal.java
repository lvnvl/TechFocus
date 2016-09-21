package com.customized.appium.algorithm;

import java.util.ArrayList;
import java.util.List;
import com.customized.appium.demo.EventDriver;
import com.customized.appium.model.AElementWidget;
import com.customized.appium.model.ActivityElement;
import com.customized.appium.util.UIUtil;
import com.customized.appium.util.XMLUtil;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class TreeLikeAutoTraversal {
	private List<ActivityElement> activityList;// 遍历过的Activity
	private String activityName;

	private boolean isForceClose = false;     // 遇到错误
	private boolean isExit = false;           // 退出自动遍历

	AndroidDriver driver;
	private String workPath;
	private EventDriver mdriver;

	public TreeLikeAutoTraversal (AndroidDriver driver, String workPath) {
		this.driver = driver;
		this.workPath = workPath;

		activityList = new ArrayList<ActivityElement>();
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
		String xml = driver.getPageSource();
		widgetList = XMLUtil.dealWithXML(xml);
		// 保存当前activity所有控件元素
		ActivityElement ae = new ActivityElement();
		ae.setActivityName(activityName);
		ae.setWidgets(widgetList);
		ae.setLevel(0);
		activityList.add(ae);

		if (widgetList != null && widgetList.size() > 0) {
			for (int i = 0; i < ae.getWidgets().size(); i++) {
				AElementWidget element = ae.getWidgets().get(i);
				if (element.getClickable().equals("true")){// 能点击的都点击
					if (isForceClose || isExit)
						break;
					Recursion(element, ae);
				}
			}
//			for (AElementWidget element : widgetList) {
//				if (element.getClickable().equals("true")){// 能点击的都点击
//					if (isForceClose || isExit)
//						break;
//					Recursion(element, ae);
//				}
//			}
		}
		isExit = true;
	}

	/**
	 * 递归式寻径，默认点击完成后返回</br>
	 * TODO 检测可能progress bar 遇到返回功能按钮或更新内容按钮
	 * @param element
	 */
	private void Recursion(AElementWidget element, ActivityElement ae) {
		List<AElementWidget> newWidgetList = null;
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
		newWidgetList = XMLUtil.dealWithXML(xml);
		// 判断界面是否同一个activity
		if (activityName.equals(driver.currentActivity())) {
			/**
			 * 同一个Activity时，检测页面变化、添加新的widget到list中
			 */
			List<AElementWidget> oldWidgetList = ae.getWidgets();
			for (int i = 0; i < newWidgetList.size(); i++) {
				AElementWidget elementTemp = newWidgetList.get(i);
				if (elementTemp.getClickable().equals("true")){// 能点击的,再检测是否已经加入到oldWidgetList中
					boolean isexist = false;
					for(int j = 0;j < oldWidgetList.size(); j++){
						AElementWidget elementTempOld = oldWidgetList.get(j);
						if (elementTempOld.getClickable().equals("true")
								&& elementTempOld.getClazz().equals(elementTemp.getClazz())){
							if(elementTempOld.getText()!=null 
									&& elementTemp.getText()!=null
									&& elementTempOld.getText().equals(elementTemp.getText())){
								isexist = true;
								break;
							}
						}
					}
					if(!isexist){
						oldWidgetList.add(elementTemp);
						ae.setWidgets(oldWidgetList);
					}
				}
			}
			return;
		}else{
			/**
			 * 跳转到不同的Activity,则判断Activity的level再进行处理
			 */
			ActivityElement newAE = new ActivityElement();
			newAE.setActivityName(driver.currentActivity());
			newAE.setWidgets(newWidgetList);
			if(activityList.indexOf(newAE) != -1){
				int oldLevel = ae.getLevel();
				int newLevel = activityList.get(activityList.indexOf(newAE)).getLevel();
				if(newLevel < oldLevel){
					//跳转向比自己高的层级，则不遍历，回退
				}else if(newLevel == oldLevel){
					//同级，不更新level，遍历
					if (newWidgetList != null && newWidgetList.size() > 0) {
						for (int i = 0; i < newWidgetList.size(); i++) {
							AElementWidget newElement = newWidgetList.get(i);
							if (newElement.getClickable().equals("true")){// 能点击的都点击
								if (isForceClose || isExit)
									break;
								Recursion(newElement, newAE);
							}
						}
					}
				}else{
					//确定跳转去的页面层级更低,更新level，遍历
					activityList.get(activityList.indexOf(newAE)).setLevel(oldLevel+1);
					if (newWidgetList != null && newWidgetList.size() > 0) {
						for (int i = 0; i < newWidgetList.size(); i++) {
							AElementWidget newElement = newWidgetList.get(i);
							if (newElement.getClickable().equals("true")){// 能点击的都点击
								if (isForceClose || isExit)
									break;
								Recursion(newElement, newAE);
							}
						}
					}
				}
			}else{
				//没有遍历过当前页面，则遍历，并添加到activityList
				newAE.setLevel(ae.getLevel()+1);
				activityList.add(newAE);
				if (newWidgetList != null && newWidgetList.size() > 0) {
					for (int i = 0; i < newWidgetList.size(); i++) {
						AElementWidget newElement = newWidgetList.get(i);
						if (newElement.getClickable().equals("true")){// 能点击的都点击
							if (isForceClose || isExit)
								break;
							Recursion(newElement, newAE);
						}
					}
				}
			}
		}
		driver.sendKeyEvent(AndroidKeyCode.BACK);
	}
}