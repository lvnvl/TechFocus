package pers.traveler.test;

import java.util.Scanner;

import pers.traveler.tools.CmdUtil;

public class MutiClick {

	public MutiClick() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int input = 99, clickTimes = 0;
		Scanner in = new Scanner(System.in);
		System.out.print("请输入连续点击次数（整数）：");
		input = in.nextInt();
		while(input != 0){
			clickTimes = input;
			while(clickTimes > 0){
				new MyThread().start();
//				String s = "adb shell input tap " + 
//						(600 + (int)((1032 - 600) * Math.random())) + " " + 
//						(450 + (int)((1100 - 450) * Math.random()));
//				CmdUtil.run("adb shell input tap " + 
//						(600 + (int)((1032 - 600) * Math.random())) + " " + 
//						(450 + (int)((1100 - 450) * Math.random())));
//				System.out.println("\t|"+s);
//				System.out.println("\t|"+CmdUtil.run(s)+"|");
				clickTimes -= 1;
			}
			System.out.print(clickTimes + " 请输入连续点击次数（整数）：");
			input = in.nextInt();
		}
		in.close();
	}
	private static class MyThread extends Thread{
		public void run(){
			CmdUtil.run("adb shell input tap " + 
					(600 + (int)((1032 - 600) * Math.random())) + " " + 
					(450 + (int)((1100 - 450) * Math.random())));
		}
	}
}