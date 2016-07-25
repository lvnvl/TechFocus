package com.customized.appium.framework;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * web driver android driver
 * 
 * @author kaliwn
 *
 */

public abstract class TestProject {
	Tester tester;

	@Before
	final public void setTest() throws Exception {
		System.out.println("Initialize settings");
		onStart();
	}

	@After
	final public void endTest() throws Exception {
		System.out.println("Prepare for ending test");
		onDestroy();
		System.out.println("Test was finished");
	}

	@Test
	final public void runTest() throws Exception {
		System.out.println("Start testing");
		onTest();
	}

	public Tester getTester() {
		return tester;
	}

	public void setTester(Tester tester) {
		this.tester = tester;
	}

	public abstract void onStart() throws Exception;
	public abstract void onDestroy() throws Exception;
	public abstract void onTest() throws Exception;

}
