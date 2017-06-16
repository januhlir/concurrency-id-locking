package org.bitbucket.espinosa.concurrency.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Unit test for various implementations of {@link BookingService} focused on
 * its thread safety and lock handling.
 * 
 * @author Espinosa
 */
public abstract class BookingServiceAbstractTest {
	private final int NO_OF_THREADS = 10;
	private final int NO_OF_ATTEMPTS = 1_000;

	private final int RESOURCE_ID = XorShiftRandom.nextInt();
	
	private final CountDownLatch start = new CountDownLatch(1);

	/**
	 * Test {@link BookingService#process(Object)} thread safety in multi
	 * concurrent and heavily contended environment.
	 */
	protected void testServiceThreadSafety(BookingService service) throws Exception {
		ExecutorService pool = Executors.newFixedThreadPool(NO_OF_THREADS);
		ExecutorCompletionService<Boolean> ecs = new ExecutorCompletionService<>(pool);
		for (int i = 0; i < NO_OF_THREADS; i++) {
			start.countDown();
			ecs.submit(() -> {
				for (int j = 0; j < NO_OF_ATTEMPTS; j++) {
					service.process(RESOURCE_ID);
				}
				return true;
			});
		}
		start.countDown(); // start all test loops at once
		
		for (int i = 0; i < NO_OF_THREADS; i++) {
			ecs.take().get(); // throws exception if any thread ended with exception
		}
	}
}