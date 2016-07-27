package com.java.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unused")
class People{
	public String name;
	public People(String name){
		this.name = name;
	}
	public void setName(String name){
		this.name = name;
	}
	public void hello(){
		System.out.println("hello "+ this.name + "!"); 
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(((People)obj).name.equals(this.name)){
			return true;
		}
		return false;
	}
	
}
public class ListForTest {

	public ListForTest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<People> peoples = new ArrayList<People>();
		People p1 = new People("jack");
		People p2 = new People("alice");
		People p3 = new People("allen");
		peoples.add(p1);
		peoples.add(p2);
		peoples.add(p3);
		System.out.println(peoples.contains(new People("jack")));
		peoples.get(peoples.indexOf(new People("jack"))).setName("jack sparrow");

		System.out.println(peoples.contains(new People("jack")));
		for(People p:peoples){
			p.hello();
		}
//		List<Integer> aList = new ArrayList<Integer>();
//		aList.add(2);
//		aList.add(3);
//		aList.add(4);
//		aList.add(5);
//		aList.add(6);
//		for(int i = 0;i < aList.size(); i++){
//			System.out.println("i is "+ aList.get(i));
//			if(i == 3){
//				aList.add(7);
//				aList.add(8);
//				aList.add(9);
//			}
//		}
////		Iterator<Integer> it = aList.iterator();
////		while(it.hasNext()){
////			Integer i = it.next();
////			System.out.println("i is "+ i);
////			if(i == 3){
////				it.remove();;
////			}
////		}
//		System.out.println(aList.toString());
//		new ListForTest().addOne(aList);
//		System.out.println(aList.toString());
	}

	@SuppressWarnings("unused")
	private void addOne(List<Integer> llist){
		llist.add(90);
	}	
}