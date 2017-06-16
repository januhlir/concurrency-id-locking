package org.bitbucket.espinosa.concurrency.idlock2b;

import java.util.concurrent.ConcurrentMap;

import org.bitbucket.espinosa.concurrency.util.BookingService;
import org.bitbucket.espinosa.concurrency.util.ExclusivityCheck;

import com.google.common.collect.MapMaker;

/**
 * Solution based on a weak values hash map, using Guava weak map implementation
 * prunes map of unreferenced mapped values and uses standard 'object equality'
 * when comparing keys.
 * 
 * This solution doesn't need an explicit lock release. Locks and lock mappings
 * are reclaimed only as a result of GC run, so typically when memory is
 * starting to get low. It is safe, removal cannot happen when treads are inside
 * mutex, or waiting to entry it, as ID and Locks are both also strongly
 * referenced at that point.
 * 
 * @author Espinosa
 */
public class BookingService2B implements BookingService {
	private static final ConcurrentMap<Object, Object> idLocks = new MapMaker().weakValues().makeMap();

	private final ExclusivityCheck exch = new ExclusivityCheck();
	
	public void process(final Object resourceId) {
		synchronized (getLock(resourceId)) { // lock on a particular ID
			// now resource referenced by resourceId
			// can be safely retrieved and modified

			System.out.println("Thread " + Thread.currentThread().getName() + " accessing ID " + resourceId);
			exch.checkOnEntry();
			exch.pretendDoingSomething();
			exch.checkonLeave();
		}
	}

	private Object getLock(Object resourceId) {
		return idLocks.computeIfAbsent(resourceId, (key) -> {
			return new Object();
		});
	}
}
