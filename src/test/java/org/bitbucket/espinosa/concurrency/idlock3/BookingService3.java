package org.bitbucket.espinosa.concurrency.idlock3;

import java.util.concurrent.locks.Lock;

import org.bitbucket.espinosa.concurrency.util.BookingService;
import org.bitbucket.espinosa.concurrency.util.ExclusivityCheck;

import com.google.common.util.concurrent.Striped;

/**
 * Use Guava's Stripped utility.
 * 
 * <p>There is no explicit lock release, either explicit or GC driven; it does not
 * have to, it operates on a different principle. Take for example
 * {@code Striped.lock(1024)}, that creates simple {@code Lock[1024]} array, eagerly initialized
 * with 1024 pregenerated Lock objects; see {@code Striped.CompactStriped}. There can be
 * billions of unique IDs in the application but your lock pool stays at 1024 Locks, 
 * always the same Locks. Striped operates on statistically <b>very low probability</b>
 * of 2, or more, IDs generating same hash trying to access mutex at the same
 * time.
 * 
 * <p>Downside is that Striped allows ID collisions; multiple threads could be
 * blocked despite locking on different IDs. The chances of this unnecessary
 * depends on size of Striped backing array, eagerly preinitilized as with
 * {@link Striped.CompactStriped}; or on maximum size of backing Map as in
 * {@link Striped.LargeLazyStriped}. The bigger the collection is, the more
 * hashes are covered, the lesser chance of collision.
 * 
 * @author Espinosa
 */
public class BookingService3 implements BookingService {
	// the higher number the lesser chance of lock collision occurrence
	// the lesser number, the more memory efficient it is
	private static final int NUMBER_OF_STRIPES = 1024;
	private static final Striped<Lock> idLocks = Striped.lock(NUMBER_OF_STRIPES);

	private final ExclusivityCheck exch = new ExclusivityCheck();

	public void process(Object resourceId) {
		Lock idLock = idLocks.get(resourceId);
		try {
			idLock.lock(); // lock on a particular ID

			// now resource referenced by resourceId
			// can be safely retrieved and modified

			System.out.println("Thread " + Thread.currentThread().getName() + " accessing ID " + resourceId);
			exch.checkOnEntry();
			exch.pretendDoingSomething();
			exch.checkonLeave();
		} finally {
			idLock.unlock();
		}
	}
}
