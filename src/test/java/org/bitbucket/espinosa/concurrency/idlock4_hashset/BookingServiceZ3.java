package org.bitbucket.espinosa.concurrency.idlock4_hashset;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bitbucket.espinosa.concurrency.util.BookingService;
import org.bitbucket.espinosa.concurrency.util.ExclusivityCheck;

/**
 * A solution based on HashSet<br>
 * Source: https://stackoverflow.com/a/660002/1185845<br>
 * https://stackoverflow.com/questions/659915/synchronizing-on-an-integer-value<br>
 * Answer by Antonio<br>
 * 
 * While this solution is correct, I see a potentially problematic bottleneck in
 * the two "global" synchronised blocks. Every thread accessing this service is
 * blocked there, though only for a short while; solutions based on
 * {@link ConcurrentHashMap} should provide parallel access to lock map based on
 * internal lock stripping in {@link ConcurrentHashMap}, meaning that even id
 * lock evaluation is most likely done in parallel (non-blocking).
 */
public class BookingServiceZ3 implements BookingService {
	private final Set<Object> activeIds = new HashSet<>();

	private final ExclusivityCheck exch = new ExclusivityCheck();

	public void process(Object resourceId) {

		// acquire "lock" on a particular ID
		synchronized(activeIds) {
			while(activeIds.contains(resourceId)) {
				try { 
					activeIds.wait();   
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}
			}
			activeIds.add(resourceId);
		}
		try {

			// now resource referenced by resourceId
			// can be safely retrieved and modified

			System.out.println("Thread " + Thread.currentThread().getName() + " accessing ID " + resourceId);
			exch.checkOnEntry();
			exch.pretendDoingSomething();
			exch.checkonLeave();

		} finally {
			// release lock on item #id
			synchronized(activeIds) { 
				activeIds.remove(resourceId); 
				activeIds.notifyAll(); 
			}   
		}   
	}
}
