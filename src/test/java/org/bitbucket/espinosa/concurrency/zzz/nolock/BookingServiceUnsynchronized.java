package org.bitbucket.espinosa.concurrency.zzz.nolock;

import org.bitbucket.espinosa.concurrency.util.BookingService;

public class BookingServiceUnsynchronized implements BookingService {
	public void process(Object resourceId) {
		System.out.println("Thread " + Thread.currentThread().getName() + " accessing non-exclusively ID " + resourceId);
	}
}
