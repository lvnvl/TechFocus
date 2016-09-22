package cn.appstest.exception;

public class PageLoadException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PageLoadException(String msg) {
		// TODO Auto-generated constructor stub
		System.out.println(msg);
	}
	
	public void handle(){
		System.out.println("handled");
	}
}