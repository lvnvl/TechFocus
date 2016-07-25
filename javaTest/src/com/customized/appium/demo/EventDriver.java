package com.customized.appium.demo;

import java.util.ArrayList;
import java.util.List;

import com.customized.appium.model.AElementWidget;
import com.customized.appium.model.RecordEventModel;
import com.customized.appium.util.FileUtil;
import com.customized.appium.util.UIUtil;

import io.appium.java_client.android.AndroidDriver;

/**
 * 事件驱动类，点击或其它UI操作
 * 记录操作
 * @author kaliwn
 *
 */
public class EventDriver {

	private AndroidDriver driver;
	private List<RecordEventModel> eventList;

	public EventDriver(AndroidDriver driver) {
		this.driver = driver;
	}

	public List<RecordEventModel> getEventList() {
		return eventList;
	}

	public void setEventList(List<RecordEventModel> eventList) {
		this.eventList = eventList;
	}


	public void click(AElementWidget element){
		addRecord(element, "click", null, driver.currentActivity());
		UIUtil.click(element, driver);
	}

	public void forceCloseEvent(String path, String fileName){
		eventList.get(eventList.size()-1).setIsError("has stopped");
		FileUtil.exportRecordWithJSON(eventList, fileName, path);
	}

	/**
	 * 
	 * @param element
	 *            对象
	 * @param action
	 *            动作（点击，长按，滑动，输入）
	 * @param data
	 *            内容（滑动为坐标[x1,y1,x2,y2]，输入为文本）
	 */
	private void addRecord(AElementWidget element, String action, String data, String activityName) {
		if (eventList == null) {
			eventList = new ArrayList<RecordEventModel>();
		}
		RecordEventModel m = new RecordEventModel();
		m.setActivity(activityName);
		m.setAction(action);
		m.setData(data);
		m.setID(element.getID());
		eventList.add(m);

	}
}
