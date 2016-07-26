package com.customized.appium.model;

/**
 * 控件元素属性，兼容appium
 * 
 * @author kaliwn
 *
 */
public class AElementWidget {

	private int ID; // 自定义ID
	private String wName; // 自定义名称

	private String index;
	private String text;
	private String clazz;// "android.widget.Button"
	private String packg;// ="包名"
	private String contentDesc;// ="描述"
	private String checkable;// e="false"
	private String checked;// ="false"
	private String clickable;// ="true"
	private String enabled;// ="true"
	private String focusable;// ="true"
	private String focused;// ="false"
	private String scrollable;// ="false"
	private String longClickable;// ="false"
	private String password;// ="false"
	private String selected;// ="false"
	private String bounds;// ="[0,100][2048,196]"
	private String resourceId;// ="com.kaliwn.backup:id/btn_all"
	private String instance;// ="0"

	private int pNodeID; // 父节点ID
	private int mNodeID; // 节点id
	private int childAmounts; // 子节点个数
	private int isRepeat; // 重复节点

	public String getwName() {
		return wName;
	}

	public void setwName(String wName) {
		this.wName = wName;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getPackg() {
		return packg;
	}

	public void setPackg(String packg) {
		this.packg = packg;
	}

	public String getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}

	public String getCheckable() {
		return checkable;
	}

	public void setCheckable(String checkable) {
		this.checkable = checkable;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getClickable() {
		return clickable;
	}

	public void setClickable(String clickable) {
		this.clickable = clickable;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getFocusable() {
		return focusable;
	}

	public void setFocusable(String focusable) {
		this.focusable = focusable;
	}

	public String getFocused() {
		return focused;
	}

	public void setFocused(String focused) {
		this.focused = focused;
	}

	public String getScrollable() {
		return scrollable;
	}

	public void setScrollable(String scrollable) {
		this.scrollable = scrollable;
	}

	public String getLongClickable() {
		return longClickable;
	}

	public void setLongClickable(String longClickable) {
		this.longClickable = longClickable;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getBounds() {
		return bounds;
	}

	public void setBounds(String bounds) {
		this.bounds = bounds;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getpNodeID() {
		return pNodeID;
	}

	public void setpNodeID(int pNodeID) {
		this.pNodeID = pNodeID;
	}

	public int getmNodeID() {
		return mNodeID;
	}

	public void setmNodeID(int mNodeID) {
		this.mNodeID = mNodeID;
	}

	public int getChildAmounts() {
		return childAmounts;
	}

	public void setChildAmounts(int childAmounts) {
		this.childAmounts = childAmounts;
	}

	public int getIsRepeat() {
		return isRepeat;
	}

	public void setIsRepeat(int isRepeat) {
		this.isRepeat = isRepeat;
	}

}
