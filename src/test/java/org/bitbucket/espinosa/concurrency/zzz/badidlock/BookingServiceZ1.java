package org.bitbucket.espinosa.concurrency.zzz.badidlock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.bitbucket.espinosa.concurrency.util.BookingService;
import org.bitbucket.espinosa.concurrency.util.ExclusivityCheck;

/**
 * Flawed version. Intuitive and simple but contains potential race condition.
 * 
 * To get one, at least 3 threads has to be considered, all trying to lock on
 * the same ID in a very short sequence. What could happen isi that first thread
 * is just about to leave, so it removes lock from lock cache; while second
 * thread is still in the mutex, third thread arrives, new lock for the same ID
 * fot the same ID; it is a fresh new Lock, uncontested, so the third thread
 * enters mutex while second thread is potentially still there.
 * 
 * @author Espinosa
 */
public class BookingServiceZ1 implements BookingService {
	private final ConcurrentHashMap<Object, Object> idLocks = new ConcurrentHashMap<>();
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
	
	/**
	 * Implementation note: internally it uses
	 * {@link ConcurrentHashMap#computeIfAbsent(Object, Function)}, it is
	 * similar to {@link ConcurrentHashMap#putIfAbsent(Object, Function)};
	 * {@code computeIfAbsent()} however returns always current value, like
	 * {@code get()}, that is what we need, whereas {@code putIfAbsent()} would
	 * return previous value, in our case {@code null).
	 */
	private Object getLock(Object resourceId) {
		return idLocks.computeIfAbsent(resourceId, (key) -> {
			return new Object();
		});
	}
	
	private void releaseLock(Object resourceId) {
		idLocks.remove(resourceId);
	}
}
