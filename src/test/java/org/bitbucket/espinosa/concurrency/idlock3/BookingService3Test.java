package org.bitbucket.espinosa.concurrency.idlock3;

import org.bitbucket.espinosa.concurrency.util.BookingServiceAbstractTest;
import org.bitbucket.espinosa.concurrency.util.StopwatchRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for {@link BookingService3} focused on its thread safety and lock handling.
 *  
 * @author Espinosa
 */
public class BookingService3Test extends BookingServiceAbstractTest {
	
	@Rule
    public StopwatchRule watcher = new StopwatchRule();
	
	@Test
	public void testServiceThreadSafety() throws Exception {
		testServiceThreadSafety(new BookingService3());
    }
	// with waiting:    duration=97564 ms
	// without waiting: duration=186 ms
}