package com.customized.appium.model;

import java.util.List;

/**
 * 页面对象
 * @author kaliwn
 *
 */
public class ActivityElement {
	private String ActivityName;
	private List<AElementWidget> widgets;

	public String getActivityName() {
		return ActivityName;
	}
	public void setActivityName(String activityName) {
		ActivityName = activityName;
	}
	public List<AElementWidget> getWidgets() {
		return widgets;
	}
	public void setWidgets(List<AElementWidget> widgets) {
		this.widgets = widgets;
	}
}
