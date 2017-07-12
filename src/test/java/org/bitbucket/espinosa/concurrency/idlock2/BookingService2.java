package org.bitbucket.espinosa.concurrency.idlock2;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.bitbucket.espinosa.concurrency.util.BookingService;
import org.bitbucket.espinosa.concurrency.util.ExclusivityCheck;

/**
 * Solution based on a JDK's WeakHashMap.
 * 
 * This solution doesn't need an explicit lock release. Locks and lock mappings
 * are reclaimed only as a result of GC run, so typically when memory is
 * starting to get low. It is safe, removal cannot happen when treads are inside
 * mutex, or waiting to entry it, as ID and Locks are both also strongly
 * referenced at that point.
 * 
 * @author Espinosa
 */
public class BookingService2 implements BookingService {
	// WeakHashMap uses, contrary to other Map implementations, uses 'object identity' 
	// instead of 'object equality'. That shouldn't pose any problem, String and Integer based IDs certainly can be used. 
	// It only means that retrieving "same" key from this map would yield null if the entry was garbage collected.
	// Same key, as in having same value, not necessary same same instance as the original key. 
	// In this lock storing context it is perfectly normal and to be expected.
	
	// WeakHashMap is not thread-safe, it has to be made thread safe externally
	
	private static final Map<Object, Object> idLocks = Collections.synchronizedMap(new WeakHashMap<>());
	
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
			System.out.println("Creating new locking object; thread " + Thread.currentThread().getName());
			return new Object();
		});
	}
}
