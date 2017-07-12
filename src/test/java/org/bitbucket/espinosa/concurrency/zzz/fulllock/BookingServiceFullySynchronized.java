package org.bitbucket.espinosa.concurrency.zzz.fulllock;

import org.bitbucket.espinosa.concurrency.util.BookingService;
import org.bitbucket.espinosa.concurrency.util.ExclusivityCheck;

public class BookingServiceFullySynchronized implements BookingService {
	private final ExclusivityCheck exch = new ExclusivityCheck();
	
	public synchronized void process(Object resourceId) {
		System.out.println("Thread " + Thread.currentThread().getName() + " accessing exclusively ID " + resourceId);
		
		exch.checkOnEntry();
		exch.pretendDoingSomething();
		exch.checkonLeave();
	}
}
