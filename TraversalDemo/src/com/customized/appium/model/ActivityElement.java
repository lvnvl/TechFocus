package com.customized.appium.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面对象
 * @author kaliwn
 *
 */
public class ActivityElement {
	private String ActivityName;
	private List<AElementWidget> widgets;
	private int level;            // 表示页面的层级，从首页到自己的最少点击次数（首页的level = 0）
	private Map<ActivityElement,List<Integer>> parentAcitivityElements;// 表示父节点及哪个widget跳转到自己
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj == null) return false;
		if(obj.getClass() != this.getClass()) return false;
		return this.getActivityName().equals(((ActivityElement) obj).getActivityName());
	}
	public ActivityElement() {
		super();
		this.parentAcitivityElements = new HashMap<ActivityElement,List<Integer>>();
	}
	public Map<ActivityElement, List<Integer>> getParentAcitivityElement() {
		return parentAcitivityElements;
	}
	public void setParentAcitivityElement(Map<ActivityElement, List<Integer>> parentAcitivityElement) {
		this.parentAcitivityElements = parentAcitivityElement;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
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