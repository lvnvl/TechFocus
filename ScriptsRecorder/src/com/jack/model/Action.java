package com.jack.model;

public class Action {

	private String type;
	private String itemName;
	private String xPath;
	private String operation;
	
	/**
	 * universal constructor, default for click
	 * @param type     operate type
	 * @param itemName item name, like the name in a tree node
	 * @param xPath    item's xpath
	 */
	public Action(String type, String itemName, String xPath) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.itemName = itemName;
		this.xPath = xPath;
		operation = type + "::" + xPath ;
	}
	public void setInput(String input) {
		// TODO Auto-generated constructor stub
		operation += "|" + input;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @return the xPath
	 */
	public String getxPath() {
		return xPath;
	}
	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @param xPath the xPath to set
	 */
	public void setxPath(String xPath) {
		this.xPath = xPath;
	}
	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
}