package com.java.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemPropTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		System.out.println(System.getProperty("com.android.uiautomator.bindir"));
		for(Object key:System.getProperties().keySet()){
			System.out.println(key.toString() + ":" + System.getProperty(key.toString()));
		}
	}

}
