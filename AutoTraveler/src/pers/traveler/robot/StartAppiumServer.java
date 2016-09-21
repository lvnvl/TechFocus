package pers.traveler.robot;

import pers.traveler.log.Log;
import pers.traveler.tools.CmdUtil;

/**
 * Created by quqing on 16/6/14.
 */
public class StartAppiumServer extends Thread {
    private String command;
    private Boolean check;
    private int port;
    private static StartAppiumServer startAppiumServer;

    
    /**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	/**
	 * @return the check
	 */
	public Boolean getCheck() {
		return check;
	}
	/**
	 * @param check the check to set
	 */
	public void setCheck(Boolean check) {
		this.check = check;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
     * 使用单例模式实现，将startAppiumServer设置为程序执行时一直检测appium运行状态的后台进程
     * 每 10s 检查一次appium在当前端口上是否开放
     * @return StartAppiumServer
     */
    public static StartAppiumServer getStartAppiumServer(){
    	if(startAppiumServer == null){
    		startAppiumServer = new StartAppiumServer();
    	}
    	return startAppiumServer;
    }
	/**
     * @param command
     */
    private StartAppiumServer() {
        this.check = true;
    }

    /**
     * 启动Appium服务。
     */
    public void run() {
        //启动appium服务
        Log.logInfo("start appium >> " + command);
        CmdUtil.run(command);
//        while(check){
//        	try {
//				Thread.sleep(10*1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        	String checkCommand = "netstat -ano ";
//        	if(CmdUtil.run(
//        			CmdUtil.isWindows()?checkCommand+"|findstr " + port:checkCommand+"|grep " + port
//        					).length()>0?true:false){
//                Log.logInfo("checked appium down >>>>>>>>>>>>> rebooting!");
//        		CmdUtil.run(command);
//        	}
//        }
    }
}