package com.customized.appium.model;

/**
 * 事件记录
 * @author kaliwn
 *
 */
public class RecordEventModel {
	private String activity;
	private String action;
	private int ID;
	private String data;
	private String isError;
	private String isLayout;  // 标记是否侧滑菜单
	private String isRefresh; // 内容更新list view
	private String isSpecial; // 特殊场景出现

	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getIsError() {
		return isError;
	}
	public void setIsError(String isError) {
		this.isError = isError;
	}
	public String getIsLayout() {
		return isLayout;
	}
	public void setIsLayout(String isLayout) {
		this.isLayout = isLayout;
	}
	public String getIsRefresh() {
		return isRefresh;
	}
	public void setIsRefresh(String isRefresh) {
		this.isRefresh = isRefresh;
	}
	public String getIsSpecial() {
		return isSpecial;
	}
	public void setIsSpecial(String isSpecial) {
		this.isSpecial = isSpecial;
	}
}
