package org.bitbucket.espinosa.concurrency.zzz.fulllock;

import org.bitbucket.espinosa.concurrency.util.BookingService;

public class BookingServiceFullySynchronized implements BookingService {
	public synchronized void process(Object resourceId) {
		System.out.println("Thread " + Thread.currentThread().getName() + " accessing exclusively ID " + resourceId);
	}
}
