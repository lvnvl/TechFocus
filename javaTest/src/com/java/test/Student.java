package com.java.test;

import java.util.ArrayList;

public class Student {

	private ArrayList<String> exams;
	private String name;
	public Student(ArrayList<String> exams, String name) {
		// TODO Auto-generated constructor stub
		this.exams = exams;
		this.name = name;
	}
	public void addExam(String str){
		synchronized (this) {
			exams.add(str);
			System.out.println("add: " + Thread.currentThread().getName() + " " + str);
		}
	}
	/**
	 * @return the exams
	 */
	public ArrayList<String> getExams() {
		return exams;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param exams the exams to set
	 */
	public void setExams(ArrayList<String> exams) {
		this.exams = exams;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

}
