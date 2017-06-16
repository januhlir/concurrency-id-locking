package org.bitbucket.espinosa.concurrency.util;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Utility object helping to check that only one thread is in the mutex code
 * block.
 */
public class ExclusivityCheck {
	private final AtomicReference<Thread> threadRef = new AtomicReference<>(null);
	private final XorShiftRandom randGen = new XorShiftRandom();
	private int maxWaitTimeInMs = 20;

	public void checkOnEntry() {
		Thread otherThread = threadRef.getAndSet(Thread.currentThread());
		if (otherThread != null) {
			throw new ExclusivityCheckException(
				"Thread " + Thread.currentThread().getName()
				+ " attempted to access thread while thread " 
				+ otherThread.getName() + " was still active in the mutex");
		}
	}

	public void checkonLeave() {
		Thread referencedThread = threadRef.getAndSet(null);
		if (referencedThread != Thread.currentThread()) {
			throw new ExclusivityCheckException(
				// it should not get here as entry check should fail first and finish testing
				"Thread " + Thread.currentThread().getName() 
				+ ", on leaving mutex, encountered another thread: " 
				+ referencedThread.getName() + " marked as active");
		}
	}

	public void pretendDoingSomething() {
		try {
			waitRandomTime();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e);
		}
	}

	private void waitRandomTime() throws InterruptedException {
		Thread.sleep(randGen.nextInt(maxWaitTimeInMs)); 
	}
}
