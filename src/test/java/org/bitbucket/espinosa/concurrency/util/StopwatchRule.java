package org.bitbucket.espinosa.concurrency.util;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class StopwatchRule extends TestWatcher {

	private long startTime;

	@Override
	protected void starting(Description description) {
		System.out.println(""
				+ "test" + description.getMethodName() + " started"); 
		startTime = System.currentTimeMillis();
	}

	@Override
	protected void succeeded(Description description) {
		long finishTime = System.currentTimeMillis();
		long duration = finishTime - startTime;
		System.out.println(""
				+ description.getDisplayName() + " finished; "
				+ "duration=" + duration + " ms"); 
	}
}
