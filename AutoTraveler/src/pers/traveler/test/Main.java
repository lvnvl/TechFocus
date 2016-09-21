package pers.traveler.test;

import pers.traveler.constant.CmdConfig;
import pers.traveler.constant.PlatformName;
import pers.traveler.robot.Robot;
import pers.traveler.robot.RobotFactory;
import pers.traveler.tools.CmdUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by mac on 16/5/31.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
    	
    	/*
    	 * 查询系统中是否有appium测试正在进行中
    	 * 是： 等待，直到上个测试运行完成  
    	 * 否：运行本次测试
    	 * */
//    	String checkStr = CmdUtil.run(CmdUtil.isWindows()?CmdConfig.WIN_CHECK_APPIUM_SERVER:CmdConfig.LINUX_CHECK_APPIUM_SERVER);
//    	while(null != checkStr && !checkStr.isEmpty()){
//    		Thread.sleep(1000*60);
//    		checkStr = CmdUtil.run(CmdUtil.isWindows()?CmdConfig.WIN_CHECK_APPIUM_SERVER:CmdConfig.LINUX_CHECK_APPIUM_SERVER);
//    	}
//    	Thread.sleep(1000*((int)(10 + Math.random() * 200)));
    	
        Robot testRobot;
        try {
            if (args.length == 2) {
                String platForm = args[0];
                String configFile = args[1];
                if (platForm.equalsIgnoreCase("android")) {
                    testRobot = RobotFactory.build(PlatformName.Android, configFile);
                    testRobot.travel();
                } else if (platForm.equalsIgnoreCase("ios")) {
                    testRobot = RobotFactory.build(PlatformName.iOS, configFile);
                    testRobot.travel();
                } else {
                    System.err.println("第1个参数类型错误,请指定系统:android/ios!");
                }
            } else {
                System.err.println("参数个数只允许2个!");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}