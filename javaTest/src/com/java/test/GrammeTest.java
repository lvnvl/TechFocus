package com.java.test;

import java.util.ArrayList;

public class GrammeTest {

	public GrammeTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		boolean b = false;
		System.out.println(String.valueOf(b));
		b = !b;
		// !b;
		System.out.println(b);
//		ArrayList<String> exams = new ArrayList<String>();
//		exams.add("math");
//		exams.add("english");
//		Student student = new Student(exams, "jack");
//		System.out.println("before name:" + student.getName());
//		Thread t1 = new MyThread(student);
//		t1.setName("t1");
//		Thread t2 = new MyThread(student);
//		t2.setName("t2");
//		t1.start();
//		t2.start();
//		try {
//			t2.join();
//			t1.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for(String str:student.getExams()){
//			System.out.println("\t\t" + str);
//		}
//		System.out.println("done name:" + student.getName());
	}

}
class MyThread extends Thread{
	private Student student;
	public MyThread(Student stu) {
		this.student = stu;
	}
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		String exam = String.valueOf(Math.random());
		student.addExam(Thread.currentThread().getName() + " " + exam);
	}
}