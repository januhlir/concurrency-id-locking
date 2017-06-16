package org.bitbucket.espinosa.concurrency.idlock1;

import java.util.concurrent.ConcurrentHashMap;

import org.bitbucket.espinosa.concurrency.util.BookingService;
import org.bitbucket.espinosa.concurrency.util.ExclusivityCheck;

/**
 * Solution based on ID lock map with reference counting.
 * 
 * ConcurrentHashMap is efficient, high throughput map implementation, but in
 * this context it can be a bottleneck as all threads, regardless associated ID
 * are accessing the same instance.
 * 
 * Also, hash map structural modification, though nominally of O(1) complexity,
 * are not so trivial. This solution frequently calls insertions and removals
 * from the Map.
 * 
 * @author Espinosa
 */
public class BookingService1 implements BookingService {
	private static final ConcurrentHashMap<Object, LockCounter> idLocks = new ConcurrentHashMap<>();
	
	private final ExclusivityCheck exch = new ExclusivityCheck();

	public void process(Object resourceId) {
		synchronized (getLock(resourceId)) { // lock on a particular ID
			try {
				// now resource referenced by resourceId
				// can be safely retrieved and modified
				
				System.out.println("Thread " + Thread.currentThread().getName() + " accessing ID " + resourceId);
				exch.checkOnEntry();
				exch.pretendDoingSomething();
				exch.checkonLeave();
			} finally {
				releaseLock(resourceId);
			}
		}
	}
	
	private LockCounter getLock(Object resourceId) {
		return idLocks.compute(resourceId, (key, value) -> {
			if (value == null) {
				return new LockCounter(1);
			} else {
				value.increment();
				return value;
			}
		});
	}
	
	private void releaseLock(Object resourceId) {
		idLocks.compute(resourceId, (key, value) -> {
			value.decrement();
			if (value.value() < 1) {
				return null; 
				// returning null from compute() cause HashMap to remove whole entry; 
				// in another words, Map size() will decrement after returning 
			} else {
				return value;
			}
		});
	}
	
	private class LockCounter {
		private long count = 0;
		
		LockCounter(long initialCount) {
			count = initialCount;
		}
		
		void increment() {
			count += 1;
		}
		
		void decrement() {
			if (count == 0) {
				throw new IllegalArgumentException("Counter cannot decrease below 0");
			}
			count -= 1;
		}
		
		long value() {
			return count; 
		}
	}
}