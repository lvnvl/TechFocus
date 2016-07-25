package com.customized.appium.model;

public class DebugDevice {
	private String udid;
	private String device_usb;
	private String product;//:N1 
	private String model;//:N1
	private String device;//:Nokia_N1
	private String path;

	private String task; //任务：暂定要运行的apk文件名
	private int port;
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public String getDevice_usb() {
		return device_usb;
	}
	public void setDevice_usb(String device_usb) {
		this.device_usb = device_usb;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
